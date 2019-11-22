package xyz.anythings.sorter.file.model;

import java.lang.reflect.Field;

import xyz.anythings.sorter.file.annotation.Marker;

public class SortQ implements FileGet {
	@Marker(offset = 0, length = 20)
	private String majorCode1;
	@Marker(offset = 20, length = 10)
	private String majorCode2;
	@Marker(offset = 30, length = 10)
	private String majorCode3;
	@Marker(offset = 40, length = 20)
	private String minorCode;
	@Marker(offset = 60, length = 20)
	private String barcode;
	@Marker(offset = 80, length = 40)
	private String itemCode;
	@Marker(offset = 120, length = 40)
	private String itemName;
	@Marker(offset = 160, length = 4)
	private int itemCapacity;
	@Marker(offset = 164, length = 4)
	private int price;
	@Marker(offset = 168, length = 3)
	private String chuteNo;
	@Marker(offset = 171, length = 4)
	private int order;
	@Marker(offset = 175, length = 4)
	private int result;
	@Marker(offset = 179, length = 4)
	private int reserved;
	@Marker(offset = 183, length = 50)
	private String option;
	@Marker(offset = 233, length = 67)
	private String filler;
	
	private int recordSize = 300;
	
	public String getMajorCode1() {
		return majorCode1;
	}
	public void setMajorCode1(String majorCode1) {
		this.majorCode1 = majorCode1;
	}

	public String getMajorCode2() {
		return majorCode2;
	}
	public void setMajorCode2(String majorCode2) {
		this.majorCode2 = majorCode2;
	}

	public String getMajorCode3() {
		return majorCode3;
	}
	public void setMajorCode3(String majorCode3) {
		this.majorCode3 = majorCode3;
	}

	public String getMinorCode() {
		return minorCode;
	}
	public void setMinorCode(String minorCode) {
		this.minorCode = minorCode;
	}

	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemCapacity() {
		return itemCapacity;
	}
	public void setItemCapacity(int itemCapacity) {
		this.itemCapacity = itemCapacity;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	public String getChuteNo() {
		return chuteNo;
	}
	public void setChuteNo(String chuteNo) {
		this.chuteNo = chuteNo;
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

	public int getReserved() {
		return reserved;
	}
	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
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