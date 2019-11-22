package xyz.anythings.sorter.file.model;

import java.lang.reflect.Field;

import xyz.anythings.sorter.file.annotation.Marker;

public class Member implements FileGet {
	@Marker(offset = 0, length = 30)
	private String memberNo;
	@Marker(offset = 30, length = 40)
	private String memberName;
	@Marker(offset = 70, length = 20)
	private String subNo;
	@Marker(offset = 90, length = 10)
	private String filler;
	
	private int recordSize = 100;
	
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	public String getSubNo() {
		return subNo;
	}
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
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