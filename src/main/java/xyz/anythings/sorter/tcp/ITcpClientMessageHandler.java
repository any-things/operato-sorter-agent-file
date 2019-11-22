package xyz.anythings.sorter.tcp;

import org.apache.mina.core.session.IoSession;

/**
 * 클라이언트가 차단기 서버에 요청 후 응답에 대한 메시지 처리 핸들러
 *  
 * @author shortstop
 */
public interface ITcpClientMessageHandler {
	
	/**
	 * 메시지 처리
	 * 
	 * @param serverAddress
	 * @param clientSession
	 * @param message
	 */
	public void receive(String serverAddress, IoSession clientSession, String message);
}
