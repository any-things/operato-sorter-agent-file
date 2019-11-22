package xyz.anythings.sorter.file.model.tcp;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;

import xyz.anythings.sorter.tcp.model.ITcpMsgBuffer;
import xyz.anythings.sorter.util.Constants;
import xyz.elidom.sys.util.ValueUtil;

public class TcpMsgBuffer implements ITcpMsgBuffer {
	/**
	 * 총 메시지 길이
	 */
	private int totalLength = 0;
	/**
	 * 초기 버퍼
	 */
	byte[] buffer = new byte[0];
	
	public TcpMsgBuffer() {
		this.buffer = new byte[0];
	}
	
	/**
	 * 기존 buffer에 신규 버퍼의 데이터 추가 후 버퍼에 담긴 데이터 길이가 totalLength보다 크거나 같다면 단위 메시지가 모두 도착한 것이므로 버퍼 클리어 한 후 리턴 
	 * 
	 * @param buf
	 */
	private String addBuffer(IoBuffer buf) {
		int bufPos = buf.limit();
		byte[] rawMsg = new byte[bufPos];
		
		for(int i = 0 ; i < bufPos ; i++) {
			rawMsg[i] = buf.get(i);
		}
		
		this.buffer = ArrayUtils.addAll(this.buffer, rawMsg);
		// 버퍼에 담긴 데이터 길이가 totalLength보다 크거나 같다면 단위 메시지가 모두 도착한 것이므로 버퍼 클리어 한 후 리턴 
		if(this.buffer.length >= this.totalLength) {
			String totalMsg = null;
			try {
				totalMsg = new String(this.buffer, Constants.ENCODE_TYPE);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.clear();
			return totalMsg;
			
		// 버퍼에 담긴 데이터 길이가 totalLength보다 작다면 아직 처리할 수 없음
		} else {
			return null;
		}
	}
	
	/**
	 * 메시지 파싱 시도
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public synchronized String tryToParse(Object message) {		
		if (!(message instanceof IoBuffer)) {
			return null;
		}
		
		IoBuffer buf = (IoBuffer) message;
		
		// 첫 번째 읽은 경우 
		if(totalLength == 0) {
			// 제어 코드를 읽고 
			String ctrlCd = this.parseControlCode(buf);
			
			if(ctrlCd != null) {
				// 제어 코드별 총 길이를 구하여 
				this.totalLength = this.findMsgLengthByCtrlCd(ctrlCd);
				// 버퍼에 메시지를 추가하고 
				return this.addBuffer(buf);
			}
			
		// 두 번째 이상 읽은 경우 
		} else {
			// 버퍼에 메시지를 추가하고 
			return this.addBuffer(buf);
		}
		
		return null;
	}
	
	/**
	 * 버퍼 초기화
	 */
	@Override
	public void clear() {
		if(this.buffer != null) {
			this.totalLength = 0;
			this.buffer = new byte[0];
			this.buffer = null;
		}
	}
	
	/**
	 * 문자열 메시지로 부터 모델로 파싱
	 * 
	 * @param buffer
	 */
	public String parseControlCode(IoBuffer buffer) {
		if(buffer != null) {
			// buffer를 문자열로 파싱 후 제어 코드 파싱 
			String message = null;
			try {
				message = new String(buffer.array(), Constants.ENCODE_TYPE);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return parseControlCode(message);
		} else {
			return null;
		}
	}
	
	/**
	 * 제어 코드로 총 메시지 길이를 찾아 리턴
	 * 
	 * @param ctrlCd
	 * @return
	 */
	public int findMsgLengthByCtrlCd(String ctrlCd) {
		// 시작
		if(ValueUtil.isEqualIgnoreCase(ctrlCd, "")) {
			return 0;
		// 재시작
		} else if(ValueUtil.isEqualIgnoreCase(ctrlCd, "")) {
			return 0;
		// 종료
		} else {
			return 0;
		}
	}
	
	/**
	 * message로 부터 제어 코드 파싱하여 리턴
	 * 
	 * @param message
	 * @return
	 */
	public static String parseControlCode(String message) {
		// 15번째 부터 세자리를 파싱하여 리턴  
		if(message != null && message.length() >= 18) {
			return message.substring(15, 18);
		} else {
			return null;
		}
	}
}
