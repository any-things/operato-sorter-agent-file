package xyz.anythings.sorter.file.model;

import java.lang.reflect.Field;

import xyz.anythings.sorter.file.annotation.Marker;

public class Chute implements FileGet {
	@Marker(offset = 0, length = 3)
	private String chuteNo;
	@Marker(offset = 3, length = 4)
	private int maxCaseCapacity;
	@Marker(offset = 7, length = 4)
	private int caseCapacity;
	@Marker(offset = 11, length = 2)
	private Short caseNo;
	@Marker(offset = 13, length = 4)
	private int order;
	@Marker(offset = 17, length = 4)
	private int result;
	@Marker(offset = 21, length = 50)
	private String option;
	@Marker(offset = 71, length = 2)
	private Short chuteType;
	@Marker(offset = 73, length = 2)
	private Short itemCount;
	@Marker(offset = 75, length = 25)
	private String filler;
	
	private int recordSize = 100;
	
	public String getChuteNo() {
		return chuteNo;
	}
	public void setChuteNo(String chuteNo) {
		this.chuteNo = chuteNo;
	}
	
	public int getMaxCaseCapacity() {
		return maxCaseCapacity;
	}
	public void setMaxCaseCapacity(int maxCaseCapacity) {
		this.maxCaseCapacity = maxCaseCapacity;
	}
	
	public int getCaseCapacity() {
		return caseCapacity;
	}
	public void setCaseCapacity(int caseCapacity) {
		this.caseCapacity = caseCapacity;
	}
	
	public Short getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(Short caseNo) {
		this.caseNo = caseNo;
	}
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	
	public Short getChuteType() {
		return chuteType;
	}
	public void setChuteType(Short chuteType) {
		this.chuteType = chuteType;
	}
	
	public Short getItemCount() {
		return itemCount;
	}
	public void setItemCount(Short itemCount) {
		this.itemCount = itemCount;
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