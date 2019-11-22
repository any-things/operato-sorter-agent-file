package xyz.anythings.sorter.tcp.service;

import org.apache.mina.core.session.IoSession;

import xyz.anythings.sorter.tcp.model.ITcpModel;

/**
 * TCP 관련 서비스 인터페이스 
 * 
 *  
 * @author shortstop
 */
public interface ITcpService {
	
	/**
	 * request를 받아서 처리
	 * 
	 * @param session
	 * @param request
	 */
	public void doService(IoSession session, ITcpModel request);
}
