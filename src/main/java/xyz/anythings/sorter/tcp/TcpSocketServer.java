package xyz.anythings.sorter.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import xyz.anythings.sorter.tcp.model.ITcpMsgBuffer;
import xyz.anythings.sorter.tcp.parser.TcpMsgParserFactory;
import xyz.elidom.util.DateUtil;
import xyz.elidom.util.ThreadUtil;
import xyz.elidom.util.ValueUtil;

/**
 * TCP 서버
 * 
 * @author shortstop
 */
@Service
public class TcpSocketServer extends IoHandlerAdapter {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 설정
	 */
	@Autowired
	private Environment env;
	/**
	 * 클라이언트로 부터의 요청을 처리하는 핸들러 
	 */
	@Autowired
	private ITcpServerMessageHandler tcpServiceManager;
	/**
	 * 메시지 버퍼 추상화 팩토리
	 */
	@Autowired
	private TcpMsgParserFactory tcpMsgParserFactory;

	/**
	 * 포트와 세션을 쌍으로 관리
	 */
	private Map<Integer, Map<Long, IoSession>> PORT_SESSION_MAP = new ConcurrentHashMap<Integer, Map<Long, IoSession>>();
	/**
	 * 포트와 버퍼를 쌍으로 관리 - 클라이언트로의 요청이 한 번이 아닌 여러 번에 걸쳐 나눠서 올 수 있으므로 포트별 메시지 관리
	 */
	private Map<Integer, Map<Long, ITcpMsgBuffer>> PORT_BUFFER_MAP = new ConcurrentHashMap<Integer, Map<Long, ITcpMsgBuffer>>();
	/**
	 * Acceptor
	 */
	private IoAcceptor acceptor;

	/**
	 * TCP Server Start
	 * 
	 * @param port
	 */
	public boolean start(int port) {
		
		this.stop(port);
		int retryCount = ValueUtil.toInteger(env.getProperty(TcpConfigConstants.TCP_SERVER_START_RETRY_COUNT), 3);

		for (int i = 0; i < retryCount; i++) {
			try {
				PORT_SESSION_MAP.put(port, new HashMap<Long, IoSession>());
				PORT_BUFFER_MAP.put(port, new HashMap<Long, ITcpMsgBuffer>());
				this.getIoAcceptor().bind(new InetSocketAddress(port));
				this.logger.info("STARTED [Port : " + port + "]");
				return true;
				
			} catch (IOException e) {
				if ((i + 1) == retryCount) {
					logger.error(e.getMessage());
				}

				ThreadUtil.sleep(20000);
			}
		}

		return false;
	}

	/**
	 * TCP Server Stop
	 * 
	 * @param port
	 */
	public void stop(int port) {
		// Port Unbind
		SocketAddress bindSocketAddress = this.getBindSocketAddress(port);
		if (bindSocketAddress != null) {
			this.getIoAcceptor().unbind(bindSocketAddress);
			logger.info("STOPED [Port : " + port + "]");
		}

		// Session Close
		List<IoSession> sessions = this.listClientSession(port);
		sessions.forEach(session -> session.closeNow());
	}

	/**
	 * 모든 서버 종료
	 */
	public void stopAll() {
		Set<Integer> ports = this.getServerPorts();
		for (Integer port : ports) {
			this.stop(port);
		}
	}

	/**
	 * 접속 중인 모든 Client로 데이터 전송.
	 * 
	 * @param port
	 * @param message
	 */
	public void publish(int port, String message) {
		if (message != null && !message.isEmpty()) {
			List<IoSession> sessions = this.listClientSession(port);
			sessions.forEach(session -> ThreadUtil.doAsynch(() -> TcpUtil.send(session, message.getBytes())));			
		}
	}

	/**
	 * Server Port 가져오기 실행.
	 * 
	 * @return
	 */
	public Set<Integer> getServerPorts() {
		return PORT_SESSION_MAP.keySet();
	}

	/**
	 * Server 별 접속 Client 정보 가져오기 실행.
	 * 
	 * @return
	 */
	public Map<Integer, List<String>> getClientList() {
		Map<Integer, List<String>> clientListMap = new HashMap<Integer, List<String>>();

		Set<Integer> ports = this.getServerPorts();
		for (Integer port : ports) {
			List<String> list = this.getClientList(port);
			clientListMap.put(port, list);
		}

		return clientListMap;
	}

	/**
	 * Server에 해당하는 Client 정보 가져오기 실행.
	 * 
	 * @param port
	 * @return
	 */
	public List<String> getClientList(int port) {
		List<String> list = new ArrayList<String>();
		Map<Long, IoSession> map = PORT_SESSION_MAP.get(port);
		map.forEach((id, session) -> list.add(TcpUtil.getRemoteAddress(session)));
		return list;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		Map<Long, IoSession> ioSessionMap = this.getSessionMap(session);
		if (ioSessionMap != null) {
			ioSessionMap.put(session.getId(), session);
		}

		Map<Long, ITcpMsgBuffer> bufferMap = this.getBufferMap(session);
		if (bufferMap != null) {
			bufferMap.put(session.getId(), this.tcpMsgParserFactory.getTcpMsgParser().newTcpMsgBuffer());
		}

		this.logger.info("CREATED[{}]: {}", TcpUtil.getServerAddress(session), TcpUtil.getRemoteAddress(session));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		this.logger.info("OPENED[{}] : {}", TcpUtil.getServerAddress(session), TcpUtil.getRemoteAddress(session));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Map<Long, IoSession> ioSessionMap = this.getSessionMap(session);
		if (ioSessionMap != null) {
			ioSessionMap.remove(session.getId());
		}

		Map<Long, ITcpMsgBuffer> bufferMap = this.getBufferMap(session);
		if (bufferMap != null) {
			bufferMap.get(session.getId()).clear();
			bufferMap.remove(session.getId());
		}
		
		try {
			session.closeNow();
		} catch (Throwable e) {
		}
		
		this.logger.info("CLOSED[{}] : {}", TcpUtil.getServerAddress(session), TcpUtil.getRemoteAddress(session));
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		this.logger.info("EXCEPTION[{}] : {} \n", TcpUtil.getServerAddress(session), cause.getMessage(), cause);
		
		if(cause.getMessage().toLowerCase().startsWith("operation timed out")) {
			this.logger.info("Operation timed out [{}] : \n Last Write Time {} \n Last Read Time {}", TcpUtil.getServerAddress(session)
					, DateUtil.dateTimeStr(new Date(session.getLastWriteTime()))
					, DateUtil.dateTimeStr(new Date(session.getLastReadTime())));
		}
		
		TcpUtil.closeSession(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Map<Long, ITcpMsgBuffer> bufferMap = this.getBufferMap(session);
		String parsedMsg = bufferMap.get(session.getId()).tryToParse(message);

		if (parsedMsg != null) {
			String serverAddress = TcpUtil.getServerAddress(session);
			this.logger.info("RECEIVE[{} <- {}]: {}", serverAddress, TcpUtil.getRemoteAddress(session), parsedMsg);
			ThreadUtil.doAsynch(() -> tcpServiceManager.receive(serverAddress, session, parsedMsg));
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		String value = TcpUtil.parseRawMessage(message);
		logger.info("Write to Send TCP Buffer [{} -> {}] : {}", TcpUtil.getServerAddress(session), TcpUtil.getRemoteAddress(session), value);
	}

	/**
	 * Server에 접속되어 있는, Client Session 리스트 조회
	 * 
	 * @param port
	 * @return
	 */
	private List<IoSession> listClientSession(int port) {
		List<IoSession> sessionList = new ArrayList<IoSession>();

		Map<Long, IoSession> ioSessionMap = PORT_SESSION_MAP.get(port);
		if (ValueUtil.isNotEmpty(ioSessionMap)) {
			ioSessionMap.forEach((sessionId, session) -> sessionList.add(session));
		}

		return sessionList;
	}

	/**
	 * IoAcceptor 조회
	 * 
	 * @return
	 */
	private IoAcceptor getIoAcceptor() {
		if (this.acceptor == null) {
			int bufferSize = ValueUtil.toInteger(env.getProperty(TcpConfigConstants.TCP_SERVER_READ_BUFFER_SIZE), 5242880); // 5Mb
			int idleTime = ValueUtil.toInteger(env.getProperty(TcpConfigConstants.TCP_SERVER_IDLE_TIME), 10);
			acceptor = new NioSocketAcceptor();
			// acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.setHandler(this);
			acceptor.getSessionConfig().setReadBufferSize(bufferSize);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
		}
		
		return this.acceptor;
	}

	/**
	 * Port별 Session 정보 조회
	 * 
	 * @param session
	 * @return
	 */
	private Map<Long, IoSession> getSessionMap(IoSession session) {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getLocalAddress();
		int port = inetSocketAddress.getPort();
		return PORT_SESSION_MAP.get(port);
	}

	/**
	 * session별 메시지 버퍼 조회
	 * 
	 * @param session
	 * @return
	 */
	private Map<Long, ITcpMsgBuffer> getBufferMap(IoSession session) {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getLocalAddress();
		int port = inetSocketAddress.getPort();
		return PORT_BUFFER_MAP.get(port);
	}

	/**
	 * Port에 Binding된 Socket Address 정보 조회 
	 * 
	 * @param port
	 * @return
	 */
	private SocketAddress getBindSocketAddress(int port) {
		Set<SocketAddress> addressList = this.getIoAcceptor().getLocalAddresses();

		for (SocketAddress address : addressList) {
			InetSocketAddress bindAddress = (InetSocketAddress) address;
			int bindPort = bindAddress.getPort();

			if (ValueUtil.isEqual(bindPort, port)) {
				return address;
			}
		}
		
		return null;
	}

}