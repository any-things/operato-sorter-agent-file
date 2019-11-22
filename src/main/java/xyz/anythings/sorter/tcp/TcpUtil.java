package xyz.anythings.sorter.tcp;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.anythings.sorter.util.Constants;
import xyz.elidom.util.ValueUtil;

/**
 * TCP 관련 유틸리티
 * 
 * @author shortstop
 */
public class TcpUtil {
	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(TcpUtil.class);

	private static final String NONE = "None";

	/**
	 * 연결된 Session으로 메시지를 String형태로 전송.
	 * 
	 * @param session
	 * @param message
	 * @return
	 */
	public static boolean send(IoSession session, String message) {
		if (message == null || message.isEmpty()) {
			return true;
		} else {
			String resultMsg = null;
			try {
				resultMsg = new String(message.getBytes());
				
				return send(session, resultMsg.getBytes(Constants.ENCODE_TYPE));
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}

	/**
	 * 연결된 Session으로 메시지 Byte 형태로 전송.
	 * 
	 * @param session
	 * @param rawMsg
	 * @return
	 */
	public static boolean send(IoSession session, byte[] rawMsg) {
		if (rawMsg == null) {
			return true;
		}

		String remoteAddress = getRemoteAddress(session);
		if (ValueUtil.isEqual(remoteAddress, NONE)) {
			logger.info("Remote session is empty. Already closed.");
//			throw new ElidomServiceException("Remote session is empty. Already closed.");
		}

		IoBuffer buffer = IoBuffer.allocate(rawMsg.length, false);
		buffer.put(rawMsg);
		buffer.flip();
		session.write(buffer);

		if(logger.isInfoEnabled()) {
			String message = null;
			try {
				message = new String(buffer.array(), Constants.ENCODE_TYPE);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("SEND REQUEST [{} -> {}] : {}", getServerAddress(session), remoteAddress, message);
		}
		
		return true;
	}
	
	/**
	 * message로 부터 startIdx 부터 length 만큼 잘라서 리턴
	 * 
	 * @param message
	 * @param startIdx
	 * @param length
	 * @return
	 */
	public static String parseByIdx(String message, int startIdx, int length) {
		return message.substring(startIdx, length);
	}
	
	/**
	 * 수신된 메시지를 String 형태로 변환.
	 * 
	 * @param message
	 * @return
	 */
	public static String parseRawMessage(Object message) {
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			return new String(buffer.array());
		} else {
			return null;
		}
	}

	/**
	 * Close Session
	 * 
	 * @param session
	 * @return
	 */
	public static boolean closeSession(IoSession session) {
		if (ValueUtil.isNotEmpty(session)) {
			if (session.isClosing()) {
				logger.info("ALREADY_CLOSED [{}] : {}", TcpUtil.getServerAddress(session), TcpUtil.getRemoteAddress(session));
			}
			
			session.closeNow();
		}

		return true;
	}

	/**
	 * Session이 접속하고 있는 Server의 접속 주소 조회.
	 * 
	 * @param session
	 * @return
	 */
	public static String getServerAddress(IoSession session) {
		return TcpUtil.getAddress(session, true);
	}

	/**
	 * Session의 Remote 접속 주소 조회
	 * 
	 * @param session
	 * @return
	 */
	public static String getRemoteAddress(IoSession session) {
		return TcpUtil.getAddress(session, false);
	}

	/**
	 * Session의 Remote 접속 주소 조회
	 * 
	 * @param session
	 * @param isServer
	 * @return
	 */
	private static String getAddress(IoSession session, boolean isServer) {
		if (ValueUtil.isEmpty(session)) {
			return null;
		} else {
			String addressInfo = isServer ? ValueUtil.toString(session.getLocalAddress()) : ValueUtil.toString(session.getRemoteAddress());
			return addressInfo == null ? NONE : addressInfo.substring(addressInfo.indexOf("/") + 1);
		}
	}

	/**
	 * int형을 byte배열로 바꿈
	 * 
	 * @param integer
	 * @param order
	 * @return
	 */
	public static byte[] intTobyte(int integer, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
		buff.order(order);
		buff.putInt(integer);
		return buff.array();
	}

	/**
	 * byte배열을 int형로 바꿈
	 * 
	 * @param bytes
	 * @param order
	 * @return
	 */
	public static int byteToInt(byte[] bytes, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(bytes.length);
		buff.order(order);
		buff.put(bytes);
		buff.flip();
		return buff.getInt();
	}
	
	public static String appendSpace(String str, int len) {
		int strLength = 0;
		try {
			strLength = str.getBytes(Constants.ENCODE_TYPE).length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String tempStr = str;
		
		if(strLength < len) {
			int endCount = len - strLength;
			
			for(int i = 0 ; i < endCount ; i++) {
				str = str + " ";
			}
		} else if(strLength > len){
			byte[] temp = new byte[len];
			System.arraycopy(str.getBytes(), 0, temp, 0, len);
			str = new String(temp);
		}
		
		if(str.length() == 0) {
			byte[] temp = new byte[len];
			System.arraycopy(tempStr.getBytes(), 0, temp, 0, len - 1);
			str = new String(temp);
		}
		
		return str;
	}
}
