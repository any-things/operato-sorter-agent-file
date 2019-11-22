package xyz.anythings.sorter.tcp.model;

/**
 * 메시지 버퍼
 * 
 * @author shortstop
 */
public interface ITcpMsgBuffer {

	/**
	 * 메시지 파싱 시도
	 * 기존 buffer에 신규 버퍼의 데이터 추가 후 버퍼에 담긴 데이터 길이가 totalLength보다 크거나 같다면 단위 메시지가 모두 도착한 것이므로 버퍼 클리어 한 후 리턴 
	 * 
	 * @param message
	 * @return
	 */
	public String tryToParse(Object message);
	
	/**
	 * 버퍼 클리어
	 */
	public void clear();
}
