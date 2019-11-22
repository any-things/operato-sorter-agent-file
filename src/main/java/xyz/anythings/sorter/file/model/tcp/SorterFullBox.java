package xyz.anythings.sorter.file.model.tcp;

import xyz.anythings.sorter.service.tcp.SorterFullBoxService;
import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.service.ITcpService;
import xyz.elidom.util.BeanUtil;

public class SorterFullBox implements ITcpModel {
	String result = null;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public ITcpService tcpServiceBean() {
		return BeanUtil.get(SorterFullBoxService.class);
	}
	
	@Override
	public ITcpModel parse(String message) {
		this.result = "Success";
		
		return this;
	}
	
	@Override
	public String toMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.result);
		return buffer.toString();
	}
}
