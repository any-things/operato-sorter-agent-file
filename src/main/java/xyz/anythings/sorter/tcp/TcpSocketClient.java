package xyz.anythings.sorter.tcp;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import xyz.anythings.sorter.tcp.model.ITcpMsgBuffer;
import xyz.anythings.sorter.tcp.parser.TcpMsgParserFactory;
import xyz.anythings.sorter.util.Constants;
import xyz.elidom.util.BeanUtil;
import xyz.elidom.util.ThreadUtil;
import xyz.elidom.util.ValueUtil;

/**
 * 소켓 클라이언트
 * 
 * @author shortstop
 */
public class TcpSocketClient extends IoHandlerAdapter {
	
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 현재 실행 중인지 여부
	 */
	private boolean isRunning;
	/**
	 * 세션
	 */
	private IoSession session;
	/**
	 * 클라이언트로 부터의 요청을 처리하는 핸들러 
	 */
	@Autowired
	private ITcpClientMessageHandler tcpClientMessageHandler;
	
	/**
	 * 메시지 버퍼 추상화 팩토리
	 */
	@Autowired
	private TcpMsgParserFactory parserFactory;
	/**
	 * 메시지 버퍼
	 */
	private ITcpMsgBuffer buffer = null;
	
	@Autowired
	private Environment env;
	@Autowired
	private TcpServiceManager tcpServiceManager;

	/**
	 * start
	 * 
	 * @param address
	 * @param port
	 * @param connectionTimeOut
	 * @param retryWaitTime
	 */
	public void start(String address, int port, long connectionTimeOut, long retryWaitTime) {
		if (this.isRunning) {
			return;
		}

		ThreadUtil.doAsynch(() -> {
			NioSocketConnector connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(connectionTimeOut);
			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.setHandler(this);

			this.isRunning = true;

			while (this.isRunning) {
				try {
					ConnectFuture future = connector.connect(new InetSocketAddress(address, port));
					future.awaitUninterruptibly();
					this.setSession(future.getSession());
					break;

				} catch (RuntimeIoException e) {
					this.logger.error("TCP Connection Error.", e);
					ThreadUtil.sleep(retryWaitTime);
				}
			}

			if (this.session != null) {
				this.session.getCloseFuture().awaitUninterruptibly();
			}

			connector.dispose();
		});
	}

	/**
	 * TCP 클라이언트가 실행 중인지 여부
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * TCP 서버와 접속되었는지 여부
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return this.session == null ? false : this.session.isConnected();
	}

	/**
	 * stop
	 */
	void stop() {
		this.isRunning = false;
		
		if (ValueUtil.isNotEmpty(this.session)) {
			this.session.closeNow();
		}
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		this.setSession(session);
		
		this.logger.info("Client session [" + session.getId() + "] is created.");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		this.setSession(session);
		
		this.logger.info("Client session [" + session.getId() + "] is opened.");
		
		// 세션이 오픈되는 순간 메시지 버퍼를 생성 
		if(this.buffer == null) {
			if(this.parserFactory == null) this.parserFactory = BeanUtil.get(TcpMsgParserFactory.class);
			this.buffer = this.parserFactory.getTcpMsgParser().newTcpMsgBuffer();
		} else {
			this.buffer.clear();
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		this.setSession(session);
		
		this.logger.info("Client session [" + session.getId() + "] is closed.");
		
		if(this.env == null) this.env = BeanUtil.get(Environment.class);
		
		String ipStr = env.getProperty(TcpConfigConstants.TCP_IP_LIST_FOR_CLIENT);
		String portStr = env.getProperty(TcpConfigConstants.TCP_PORT_LIST_FOR_CLIENT);
		
		if (!ValueUtil.isEmpty(portStr)) {
			String[] ipArr = StringUtils.tokenizeToStringArray(ipStr, ",");
			String[] portArr = StringUtils.tokenizeToStringArray(portStr, ",");
			
			for (int i = 0 ; i < ipArr.length ; i++) {
				String ip = ipArr[i];
				String port = portArr[i];
				Integer clientPort = ValueUtil.toInteger(port);
				
//				this.stop();
				
				long connectionTimeOut = ValueUtil.toLong(this.env.getProperty(TcpConfigConstants.TCP_CONNECTION_TIMEOUT, "10000"));
				long retryWaitTime = ValueUtil.toLong(this.env.getProperty(TcpConfigConstants.TCP_RETRY_WAIT_TIME, "60000"));
				
				NioSocketConnector connector = new NioSocketConnector();
				connector.setConnectTimeoutMillis(connectionTimeOut);
				connector.getFilterChain().addLast("logger", new LoggingFilter());
				connector.setHandler(this);

				this.isRunning = true;
				while (this.isRunning) {
					try {
						ConnectFuture future = connector.connect(new InetSocketAddress(ip, clientPort));
						future.awaitUninterruptibly();
						this.setSession(future.getSession());
						break;

					} catch (RuntimeIoException e) {
						this.logger.error("TCP Connection Error.", e);
						ThreadUtil.sleep(retryWaitTime);
					}
				}
				

//				if (this.session != null) {
//					this.session.getCloseFuture().awaitUninterruptibly();
//				}
//
//				connector.dispose();
			}
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		this.setSession(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		this.setSession(session);
		
		this.logger.error(cause.getMessage(), cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		this.setSession(session);
		// 메시지 파싱
		String parsedMsg = this.buffer.tryToParse(message);
		
		this.logger.info("parsedMsg : {}", parsedMsg);
		// 메시지가 null이 아니라면 즉 한 단위 메시지가 모두 도착했다면 서비스로 처리 ...
		if (parsedMsg != null) {
			String serverAddress = TcpUtil.getRemoteAddress(session);
			this.logger.info("RECEIVE [{} <- {}]: {}", serverAddress, parsedMsg);
			
			if(this.tcpClientMessageHandler == null) this.tcpClientMessageHandler = BeanUtil.get(ITcpClientMessageHandler.class);
			
			ThreadUtil.doAsynch(() -> tcpClientMessageHandler.receive(serverAddress, session, parsedMsg));
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		this.setSession(session);
		this.logger.info(message.toString());
	}

	/**
	 * message를 접속 중인 TCP 서버로 전송.
	 *
	 * @param message
	 */
	public void send(String message) {
		try {
			this.send(message.getBytes(Constants.ENCODE_TYPE));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * byte[] raw 데이터로 서버에 전송
	 *
	 * @param rawMsg
	 */
	public void send(byte[] rawMsg) {
		if (this.session != null && this.session.isConnected()) {
			TcpUtil.send(this.session, rawMsg);
		}
	}

	/**
	 * 현재 TCP Session 리턴
	 * 
	 * @return
	 */
	public IoSession getSession() {
		return this.session;
	}

	/**
	 * 현재 TCP Session 설정
	 * 
	 * @param session
	 */
	public void setSession(IoSession session) {
		this.session = session;
	}

}