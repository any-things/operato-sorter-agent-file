package xyz.anythings.sorter.tcp.parser;

import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.model.ITcpMsgBuffer;

/**
 * 메시지 파서
 * 
 * @author shortstop
 */
public interface ITcpMsgParser {

	/**
	 * 메시지를 모델 객체로 파싱
	 * 
	 * @param message
	 * @return
	 */
	public ITcpModel parseMessage(String message);
	
	/**
	 * ITcpMsgBuffer 구현 객체를 생성
	 * 
	 * @return
	 */
	public ITcpMsgBuffer newTcpMsgBuffer();
}
