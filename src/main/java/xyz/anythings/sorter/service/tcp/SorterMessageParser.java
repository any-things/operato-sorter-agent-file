package xyz.anythings.sorter.service.tcp;

import xyz.anythings.sorter.file.model.tcp.AssignChute;
import xyz.anythings.sorter.file.model.tcp.SorterControl;
import xyz.anythings.sorter.file.model.tcp.SorterFullBox;
import xyz.anythings.sorter.file.model.tcp.TcpMsgBuffer;
import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.model.ITcpMsgBuffer;
import xyz.anythings.sorter.tcp.parser.ITcpMsgParser;
import xyz.anythings.sorter.util.Constants;
import xyz.elidom.exception.server.ElidomServiceException;
import xyz.elidom.sys.util.ValueUtil;

public class SorterMessageParser implements ITcpMsgParser {
	
	@Override
	public ITcpModel parseMessage(String message) {
		return this.parseMessage(null, message);
	}
	
	@Override
	public ITcpMsgBuffer newTcpMsgBuffer() {
		return new TcpMsgBuffer();
	}
	
	/**
	 * 메시지 파싱
	 * 
	 * @param message
	 * @return
	 */
	public ITcpModel parseMessage(String ctrlCd, String message) {
		String ctrlCode = ctrlCd == null ? TcpMsgBuffer.parseControlCode(message) : ctrlCd;
		if(ValueUtil.isEqualIgnoreCase(ctrlCode, Constants.SORTER_CONTROL)) {
			SorterControl sc = new SorterControl();
			return sc.parse(message);
		} else if(ValueUtil.isEqualIgnoreCase(ctrlCode, Constants.SORTER_FULL_BOX)) {
			SorterFullBox sfb = new SorterFullBox();
			return sfb.parse(message);
		} else if(ValueUtil.isEqualIgnoreCase(ctrlCode, Constants.ASSIGN_CHUTE)) {
			AssignChute ac = new AssignChute();
			return ac.parse(message);
		} else {
			throw new ElidomServiceException("Invalid Control Code. [" + message + "]");
		}
	}

}
