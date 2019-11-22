package xyz.anythings.sorter.util;

public class Constants {
	public final static String FILE_ROOT_PATH = "V:";
	
	public final static String BATCHDB = "BatchData";
	public final static String EXECBAT = "Execbat";
	public final static String SORTQ = "Sort_Q";
	public final static String CHUTE = "Chute";
	public final static String SORTA = "Sort_A";
	public final static String REBUILD = "Rebuild";
	public final static String MEMBER = "Member";
	public final static String SIMPLE = "Simple";
	public final static String DAT = ".DAT";
	
	public static final String AGENT_PATH = "net.cj.agent";
	public static final String DATA_PATH = "data";
	public static final String MODEL_PATH = "file.model";
	
	public static final int BATCH_PORT = 1600;
	public static final int LABEL_PRINT_PORT = 1601;
	public static final int ASSIGN_CHUTE_PORT = 1603;
	
	public static final String RETURN_BATCH = "CLPReturnBatch";
	public static final String RETURN_BATCH_NODATA = "CLPReturnBatch_NODATA";
	public static final String RELEASE_BATCH = "CLPReleaseBatch";
	
	public static String WAS_IP = null;
	public static String ERROR_MSG_AGENT_WEB_SOCKET = null;
	public static String CONTROL_PC_DATA_FOLDER = null;
	public static String CONTROL_PC_IP = null;
	public static String MPI_PORT = null;
	
	public static final String DEFAULT_WAS_IP = "http://52.188.58.230:80";
	public static final String DEFAULT_ERROR_MSG_AGENT_WEB_SOCKET = "ws://52.188.58.230:3000/";
	public static final String DEFAULT_CONTROL_PC_DATA_FOLDER = "\\\\192.168.100.101\\DATA";
	public static final String DEFAULT_CONTROL_PC_IP = "192.168.100.101";
	public static final String DEFAULT_MPI_PORT = "55555";
	
	public static final String MARK_A = "A";
	public static final String MARK_C = "C";
	public static final String MARK_S = "S";
	public static final String MARK_E = "E";
	public static final String MARK_e = "e";
	public static final String MARK_R = "R";
	public static final String MARK_F = "F";
	public static final String MARK_B = "B";
	
	public static final String SOCKET_BATCH_START = "1";
	public static final String SOCKET_BATCH_STOP = "2";
	public static final String SOCKET_BATCH_PAUSE = "3";
	public static final String SOCKET_BATCH_COMMAND = "B";
	public static final String SOCKET_BATCH_SORT_CONTINUE = "0";
	public static final String SOCKET_BATCH_SORT_BALANCING = "1";
	
	public static final String RESULT_OK = "OK";
	public static final String RESULT_FAIL = "FAIL";
	
	public static final String STATUS_READY = "READY";	
	public static final String STATUS_CREATED = "CREATED";	
	public static final String STATUS_IN_WORK = "IN_WORK";	
	public static final String STATUS_STOPPED = "STOPPED";	
	public static final String STATUS_SOME_FINISHED = "SOME_FINISHED";
	public static final String STATUS_ALL_FINISHED = "ALL_FINISHED";
	
	public static final int CHUTE_COUNT = 12;
	
	public static final String ENCODE_TYPE = "MS949";
	
	public static final String NO_CHUTE_ID = "999";
	public static final String ASSIGN_CHUTE_COMMAND = "Q";
	
	public static final String DRIVE_COMMAND = "cmd /c subst V: C:\\PAS";
	public static final String ERROR_MSG_AGENT_PATH = "V:\\agent\\ErrorMsgAgent\\ErrorMessageAgent.exe";
	public static final String TASKLIST_COMMAND = "cmd /c tasklist /fi \"imagename eq \"";
	public static final String ERROR_MSG_AGENT_NAME = "ErrorMessageAgent";
	
	public static final String NO_LABEL = "NOLABEL";
	public static final String BATCH_STATUS_READY = "Ready";
	public static final String BATCH_STATUS_RUNNING = "Running";
	public static final String BATCH_STATUS_STOP = "Stop";
	public static final String BATCH_STATUS_PAUSE = "Pause";
	
	public static final String SORTER_CONTROL = "SORTER_CONTROL";
	public static final String SORTER_FULL_BOX = "SORTER_FULL_BOX";
	public static final String ASSIGN_CHUTE = "ASSIGN_CHUTE";
}
