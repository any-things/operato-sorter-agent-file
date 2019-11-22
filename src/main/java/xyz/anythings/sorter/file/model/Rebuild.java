package xyz.anythings.sorter.file.model;

import java.lang.reflect.Field;

import xyz.anythings.sorter.file.annotation.Marker;

public class Rebuild implements FileGet {
	@Marker(offset = 0, length = 1)
	private String flag;
	@Marker(offset = 1, length = 1)
	private String mark;
	@Marker(offset = 2, length = 300)
	private SortQ sortQ;
	@Marker(offset = 302, length = 9)
	private String Time;
	@Marker(offset = 311, length = 48)
	private String filler;

	@Marker(offset = 359, length = 1)
	private String lf;
	
	private int recordSize = 360;

	public SortQ getSortQ() {
		return sortQ;
	}
	public void setSortQ(SortQ sortQ) {
		this.sortQ = sortQ;
	}
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
	
	public String getLf() {
		return lf;
	}
	public void setLf(String lf) {
		this.lf = lf;
	}
	
	@Override
	public void setSomething(String name, Object value) {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals(name)) {
				try {
					field.set(this, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public Object getSomething(String name) {
		Object obj = null;

		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals(name)) {
				try {
					obj = field.get(this);
					break;
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return obj;
	}
	
	@Override
	public int getRecordSize() {
		return this.recordSize;
	}

	@Override
	public int getOffset(String fieldName) {
		int offset = -1;
		Field[] fields = this.getClass().getDeclaredFields();
		for( Field field : fields ){
			Marker annotation = field.getAnnotation(Marker.class);
            if( annotation != null ){
                offset = annotation.offset();
            }
		}
		
		return offset;
	}
}