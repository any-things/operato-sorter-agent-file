package xyz.anythings.sorter.tcp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TcpServiceManager {

	@Autowired
	private TcpSocketServer tcpSocketServer;

	@Autowired
	private TcpSocketClientFactory tcpSocketClientFactory;

	public boolean startClient(String ip, int port) {
		return this.tcpSocketClientFactory.connect(ip, port);
	}
	
	public void stopClient(String ip, int port) {
		this.tcpSocketClientFactory.disconnect(ip, port);
	}
	
	/**
	 * ALL TCP Server Stop
	 */
	public void stopClientsAll() {
		this.tcpSocketClientFactory.disconnectAll();
	}
	
	/**
	 * TCP Server Start
	 * 
	 * @param port
	 * @return
	 */
	public boolean startServer(int port) {
		return tcpSocketServer.start(port);
	}

	/**
	 * TCP Server Stop
	 * 
	 * @param port
	 */
	public void stopServer(int port) {
		tcpSocketServer.stop(port);
	}

	/**
	 * ALL TCP Server Stop
	 */
	public void stopAll() {
		tcpSocketServer.stopAll();
	}

	/**
	 * Server 별 접속 Client 정보 가져오기 실행.
	 * 
	 * @return
	 */
	public  Map<String, String> getClientList() {
		return tcpSocketClientFactory.getClientList();
	}

	/**
	 * 접속 중인 모든 Client로 데이터 전송.
	 * 
	 * @param port
	 * @param message
	 */
	public void publish(int port, String message) {
		tcpSocketServer.publish(port, message);
	}

}
