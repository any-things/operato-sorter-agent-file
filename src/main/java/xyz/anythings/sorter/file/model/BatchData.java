package xyz.anythings.sorter.file.model;

import java.lang.reflect.Field;

import xyz.anythings.sorter.file.annotation.Marker;

public class BatchData implements FileGet {
	@Marker(offset = 0, length = 1)
	private String mark;
	@Marker(offset = 1, length = 1)
	private String lineNo;
	@Marker(offset = 2, length = 1)
	private String inNo;
	@Marker(offset = 3, length = 8)
	private String workDate;
	@Marker(offset = 11, length = 9)
	private String workTime;
	@Marker(offset = 20, length = 20)
	private String majorCode1;
	@Marker(offset = 40, length = 10)
	private String majorCode2;
	@Marker(offset = 50, length = 10)
	private String majorCode3;
	@Marker(offset = 60, length = 3)
	private String chuteNo;
	@Marker(offset = 63, length = 3)
	private String caseNo;
	@Marker(offset = 66, length = 10)
	private String resultCode1;
	@Marker(offset = 76, length = 10)
	private String resultCode2;
	@Marker(offset = 86, length = 10)
	private String resultCode3;
	@Marker(offset = 96, length = 10)
	private String count;
	@Marker(offset = 106, length = 20)
	private String minorCode;
	@Marker(offset = 126, length = 4)
	private String plcCode;
	@Marker(offset = 130, length = 19)
	private String filler;
	@Marker(offset = 149, length = 1)
	private String lf;

	private int recordSize = 150;

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

	public String getResultCode1() {
		return resultCode1;
	}

	public void setResultCode1(String resultCode1) {
		this.resultCode1 = resultCode1;
	}

	public String getResultCode2() {
		return resultCode2;
	}

	public void setResultCode2(String resultCode2) {
		this.resultCode2 = resultCode2;
	}

	public String getResultCode3() {
		return resultCode3;
	}

	public void setResultCode3(String resultCode3) {
		this.resultCode3 = resultCode3;
	}

	public String getMinorCode() {
		return minorCode;
	}

	public void setMinorCode(String minorCode) {
		this.minorCode = minorCode;
	}

	public String getPlcCode() {
		return plcCode;
	}

	public void setPlcCode(String plcCode) {
		this.plcCode = plcCode;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getInNo() {
		return inNo;
	}

	public void setInNo(String inNo) {
		this.inNo = inNo;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getChuteNo() {
		return chuteNo;
	}

	public void setChuteNo(String chuteNo) {
		this.chuteNo = chuteNo;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
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
		for (Field field : fields) {
			Marker annotation = field.getAnnotation(Marker.class);
			if (annotation != null) {
				offset = annotation.offset();
			}
		}

		return offset;
	}

}
