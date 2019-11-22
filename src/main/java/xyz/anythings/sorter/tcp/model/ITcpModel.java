package xyz.anythings.sorter.tcp.model;

import xyz.anythings.sorter.tcp.service.ITcpService;

/**
 * TCP 관련 모델 인터페이스
 * 
 * @author shortstop
 */
public interface ITcpModel {
	
	/**
	 * TCP 관련 모델을 처리하기 위한 서비스 인스턴스를 리턴
	 * 
	 * @return
	 */
	public ITcpService tcpServiceBean();
	
	/**
	 * 메시지로 부터 모델 객체로 변환
	 * 
	 * @param message
	 * @return
	 */
	public ITcpModel parse(String message);
	
	/**
	 * TCP 서버 혹은 클라이언트에 보낼 메시지를 리턴
	 * 
	 * @return
	 */
	public String toMessage();

}
