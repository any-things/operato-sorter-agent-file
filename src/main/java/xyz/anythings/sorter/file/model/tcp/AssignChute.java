package xyz.anythings.sorter.file.model.tcp;

import xyz.anythings.sorter.service.tcp.AssignChuteService;
import xyz.anythings.sorter.tcp.TcpUtil;
import xyz.anythings.sorter.tcp.model.ITcpModel;
import xyz.anythings.sorter.tcp.service.ITcpService;
import xyz.elidom.util.BeanUtil;

public class AssignChute implements ITcpModel {
	
	String batchNo = null;
	String barcode = null;
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@Override
	public ITcpService tcpServiceBean() {
		return BeanUtil.get(AssignChuteService.class);
	}
	
	@Override
	public ITcpModel parse(String message) {
		this.batchNo = TcpUtil.parseByIdx(message, 10, 18);
		this.barcode = TcpUtil.parseByIdx(message, 18, 38);
		
		return this;
	}
	
	@Override
	public String toMessage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.batchNo).append(this.barcode);
		return buffer.toString();
	}
}
