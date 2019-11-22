package xyz.anythings.sorter.tcp;

import org.apache.mina.core.session.IoSession;

/**
 * 서버 소켓으로 메시지 받은 후 처리 핸들러
 * 
 * @author shortstop
 */
public interface ITcpServerMessageHandler {
	
	/**
	 * 메시지 처리
	 * 
	 * @param serverAddress
	 * @param clientSession
	 * @param message
	 */
	public void receive(String serverAddress, IoSession clientSession, String message);
}
