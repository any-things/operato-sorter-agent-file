package xyz.anythings.sorter.model;

public class BatchinfoEntity {
	private int id;
	private int mode;
	private String type;
	private String isMerged;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsMerged() {
		return isMerged;
	}
	public void setIsMerged(String isMerged) {
		this.isMerged = isMerged;
	}
}
