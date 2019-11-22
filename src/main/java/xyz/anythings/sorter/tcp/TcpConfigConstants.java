package xyz.anythings.sorter.tcp;

public class TcpConfigConstants {

	/**
	 * TCP Server
	 */
	// 서버 구동 실패 시, 재구동 시도 횟수.
	public static final String TCP_SERVER_START_RETRY_COUNT = "tcp.server.start.retry.count";
	
	// 메시지 파서
	public static final String TCP_SERVER_MESSAGE_PARSER = "tcp.message.parser";

	// 요청받은 메세지의 Prefix 정보의 byte Size.
	public static final String TCP_MESSAGE_PREFIX_SIZE = "tcp.message.prefix.size";

	// WAS 시작 시, TCP 서버를 구동 할 Port 목록.
	public static final String TCP_SERVER_PORT_LIST = "tcp.server.port.list";
	
	// WAS 시작 시, TCP 클라이언트를 구동하기 위한 TCP 서버 IP 목록
	public static final String TCP_IP_LIST_FOR_CLIENT = "tcp.server.ip.list.for.client";
	
	// WAS 시작 시, TCP 클라이언트를 구동하기 위한 TCP 서버 Port 목록
	public static final String TCP_PORT_LIST_FOR_CLIENT = "tcp.server.port.list.for.client";

	// TCP 서버에서 처리 할 Message byte size에 대한 설정.
	public static final String TCP_SERVER_READ_BUFFER_SIZE = "tcp.server.read.buffer.size";

	// 서버로 전송된 Message를 Polling 할 주기에 대한 설정인것 같음;
	public static final String TCP_SERVER_IDLE_TIME = "tcp.server.idle.time";

	// Publish 서버로 설정 할 Port 설정.
	public static final String TCP_PUBLISH_SERVER_PORT = "tcp.publish.server.port";
	
	// Publish 할 메시지 내용 설정.
	public static final String TCP_PUBLISH_MESSAGE = "tcp.publish.message";

	/**
	 * TCP Client
	 */
	// 서버와의 연결 제한 시간.
	public static final String TCP_CONNECTION_TIMEOUT = "tcp.connection.timeout";
	
	// 서버와의 연결 재시도 할 대기 시간.
	public static final String TCP_RETRY_WAIT_TIME = "tcp.retry.wait.time";
	
	// 로컬 아이피 주소
	public static final String TCP_LOCAL_IP = "tcp.local.ip";
}