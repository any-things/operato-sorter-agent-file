package xyz.anythings.sorter.model;

public class ScanInfoEntity {
	private String batchId;
	private String skuBarCode;
	private String invoiceNo;
	private String chuteCode;
	private String result;
	private String quantity;
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getSkuBarCode() {
		return skuBarCode;
	}
	public void setSkuBarCode(String skuBarCode) {
		this.skuBarCode = skuBarCode;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getChuteCode() {
		return chuteCode;
	}
	public void setChuteCode(String chuteCode) {
		this.chuteCode = chuteCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}