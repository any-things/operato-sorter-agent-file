package xyz.anythings.sorter.tcp.handler;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.anythings.sorter.tcp.ITcpClientMessageHandler;
import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.parser.TcpMsgParserFactory;
import xyz.elidom.exception.server.ElidomServiceException;

/**
 * 서버 메시지를 받아서 객체로 변환하여 해당 서비스로 전달하여 처리하는 메시지 핸들러
 * 
 * @author shortstop
 */
@Service
@Transactional
public class TcpClientMessageHandler implements ITcpClientMessageHandler {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TcpMsgParserFactory parserFactory;

	@Override
	public void receive(String serverAddress, IoSession clientSession, String message) {
		try {
			// 메시지로 부터 모델 파싱
			ITcpModel request = this.parserFactory.getTcpMsgParser().parseMessage(message);
			
			if(request != null) {
				request.tcpServiceBean().doService(clientSession, request);
			}

		} catch (Throwable e) {
			logger.warn("Message : {}", message);
			logger.warn(e.getMessage(), e);

			throw new ElidomServiceException(e);
		}
	}
}