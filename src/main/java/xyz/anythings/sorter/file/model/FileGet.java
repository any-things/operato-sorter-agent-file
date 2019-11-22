package xyz.anythings.sorter.file.model;

public interface FileGet {
	public Object getSomething (String name);
	public void setSomething (String name, Object value);
	public int getRecordSize();
	public int getOffset(String fieldName);
}
