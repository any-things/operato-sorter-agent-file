package xyz.anythings.sorter.file.model.tcp;

import xyz.anythings.sorter.service.tcp.SorterControlService;
import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.service.ITcpService;
import xyz.elidom.util.BeanUtil;

public class SorterControl implements ITcpModel {
	String command = null;
	String batchMode = null;
	String batchNo = null;
	String batchName = null;
	String batchCreatedDate = null;
	String batchSort = null;
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getBatchMode() {
		return batchMode;
	}

	public void setBatchMode(String batchMode) {
		this.batchMode = batchMode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getBatchCreatedDate() {
		return batchCreatedDate;
	}

	public void setBatchCreatedDate(String batchCreatedDate) {
		this.batchCreatedDate = batchCreatedDate;
	}

	public String getBatchSort() {
		return batchSort;
	}

	public void setBatchSort(String batchSort) {
		this.batchSort = batchSort;
	}

	@Override
	public ITcpService tcpServiceBean() {
		return BeanUtil.get(SorterControlService.class);
	}
	
	@Override
	public ITcpModel parse(String message) {
		return this;
	}
	
	@Override
	public String toMessage() {
		StringBuffer buffer = new StringBuffer();
		return buffer.toString();
	}
}
