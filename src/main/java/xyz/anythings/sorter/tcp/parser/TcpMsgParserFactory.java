package xyz.anythings.sorter.tcp.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import xyz.anythings.sorter.service.tcp.SorterMessageParser;
import xyz.anythings.sorter.tcp.TcpConfigConstants;
import xyz.elidom.util.BeanUtil;
import xyz.elidom.util.ValueUtil;

/**
 * TCP Message Parser Factory
 * 
 * @author shortstop
 */
@Component
public class TcpMsgParserFactory {

	/**
	 * 설정 
	 */
	@Autowired
	private Environment env;
	
	private String tcpMsgParser;
	
	public String getTcpMsgParserName() {
		if(this.tcpMsgParser == null) {
			this.tcpMsgParser = this.env.getProperty(TcpConfigConstants.TCP_SERVER_MESSAGE_PARSER);
		}
		
		return this.tcpMsgParser;
	}
	
	public ITcpMsgParser getTcpMsgParser() {
		String parser = this.getTcpMsgParserName();
		
		if(ValueUtil.isEmpty(parser) || ValueUtil.isEqualIgnoreCase(parser, "")) {
			return BeanUtil.get(SorterMessageParser.class);
		} else {
			return null;
		}
	}

}
