package xyz.anythings.sorter.model;

public class AssignEntity {
	private String batchId;
	private String skuCode;
	private String invoiceNo;
	private String chuteId;
	private String result;
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getChuteId() {
		return chuteId;
	}
	public void setChuteId(String chuteId) {
		this.chuteId = chuteId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}