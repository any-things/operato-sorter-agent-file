package xyz.anythings.sorter.tcp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import xyz.elidom.exception.server.ElidomRuntimeException;
import xyz.elidom.util.ValueUtil;

/**
 * TCP 클라이언트 생성을 위한 팩토리
 * 
 * @author shortstop
 */
@Service
public class TcpSocketClientFactory {

	/**
	 * 설정 
	 */
	@Autowired
	private Environment env;
	/**
	 * 클라이언트 Map
	 */
	private Map<String, TcpSocketClient> clientMap = new HashMap<String, TcpSocketClient>(3);

	/**
	 * TCP Socket Client 객체 가져오기 실행.
	 * 
	 * @param clientId
	 * @return
	 */
	private TcpSocketClient getTcpSocketClient(String clientId) {		
		if(this.clientMap.containsKey(clientId)) {
			return this.clientMap.get(clientId);
		} else {
			return null;
		}
	}

	/**
	 * address, port로 TCP Client 조회
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	private TcpSocketClient getTcpSocketClient(String address, int port) {
		return this.getTcpSocketClient(this.generateId(address, port));
	}

	/**
	 * address, Port 정보를 통해 키 값을 설정.
	 * ex)127.0.0.1:9090
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	private String generateId(String address, int port) {
		return new StringBuilder().append(address).append(":").append(port).toString();
	}
	
	/**
	 * 접속
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	public boolean connect(String address, int port) {
		String clientId = this.generateId(address, port);
		TcpSocketClient client = this.getTcpSocketClient(clientId);
		
		if (client != null) {
			client.stop();
			this.clientMap.remove(clientId);
		}

		long connectionTimeOut = ValueUtil.toLong(this.env.getProperty(TcpConfigConstants.TCP_CONNECTION_TIMEOUT, "10000"));
		long retryWaitTime = ValueUtil.toLong(this.env.getProperty(TcpConfigConstants.TCP_RETRY_WAIT_TIME, "60000"));
		client = new TcpSocketClient();
		client.start(address, port, connectionTimeOut, retryWaitTime);
		this.clientMap.put(clientId, client);
		return true;
	}
	
	/**
	 * 접속 여부 확인.
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	public boolean isConnected(String address, Integer port) {
		TcpSocketClient client = this.getTcpSocketClient(address, port);
		return (client == null) ? false : client.isConnected();
	}

	/**
	 * 접속 해제.
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	public boolean disconnect(String address, int port) {
		String clientId = this.generateId(address, port);
		TcpSocketClient client = this.getTcpSocketClient(clientId);
		
		if (client != null) {
			clientMap.remove(clientId);
			client.stop();
		}
		
		return true;
	}
	
	/**
	 * 전체 접속 해제
	 */
	public void disconnectAll() {
		Iterator<TcpSocketClient> iter = this.clientMap.values().iterator();
		
		while(iter.hasNext()) {
			TcpSocketClient client = iter.next();
			client.stop();
		}
		
		this.clientMap.clear();
	}
	
	/**
	 * TCP 클라이언트를 이용하여 메시지 전송
	 * 
	 * @param address
	 * @param port
	 * @param message
	 */
	public void send(String address, int port, String message) {
		TcpSocketClient client = this.getTcpSocketClient(address, port);
		
		if(client != null) {
			client.send(message);
		} else {
			throw new ElidomRuntimeException("Not found TCP Client by ID [" + this.generateId(address, port) + "]!");
		}
	}
	
	public Map<String, String> getClientList() {
		Set set = clientMap.keySet();
		 Map<String, String> result = new HashMap<>();

		Iterator iterator = set.iterator();
		int idx = 1;
		while(iterator.hasNext()){
		  String key = (String)iterator.next();
		  result.put(ValueUtil.toString(idx), key);
		  idx++;
		}
		return result;
	}
}