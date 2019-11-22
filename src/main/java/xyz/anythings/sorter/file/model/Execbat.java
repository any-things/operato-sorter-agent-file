package xyz.anythings.sorter.file.model;

import java.lang.reflect.Field;

import xyz.anythings.sorter.file.annotation.Marker;

public class Execbat implements FileGet {
	@Marker(offset = 0, length = 1)
	private String lineNo;
	@Marker(offset = 1, length = 1)
	private String job;
	@Marker(offset = 2, length = 8)
	private String makeDate;
	@Marker(offset = 10, length = 8)
	private String batchNo;
	@Marker(offset = 18, length = 20)
	private String batchName;
	@Marker(offset = 38, length = 1)
	private String statusFlag;
	@Marker(offset = 39, length = 10)
	private String checkFlag;
	@Marker(offset = 49, length = 4)
	private int maxCaseCapacity;
	@Marker(offset = 53, length = 1)
	private String resultFlag;
	@Marker(offset = 54, length = 4)
	private int startTime;
	@Marker(offset = 58, length = 4)
	private int loadTime;
	@Marker(offset = 62, length = 1)
	private String workLineNo;
	@Marker(offset = 63, length = 1)
	private String sendCancelFlag;
	@Marker(offset = 64, length = 1)
	private String operateType;
	@Marker(offset = 65, length = 35)
	private String filler;
	
	private int recordSize = 100;

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public int getMaxCaseCapacity() {
		return maxCaseCapacity;
	}

	public void setMaxCaseCapacity(int maxCaseCapacity) {
		this.maxCaseCapacity = maxCaseCapacity;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
	}

	public String getWorkLineNo() {
		return workLineNo;
	}

	public void setWorkLineNo(String workLineNo) {
		this.workLineNo = workLineNo;
	}

	public String getSendCancelFlag() {
		return sendCancelFlag;
	}

	public void setSendCancelFlag(String sendCancelFlag) {
		this.sendCancelFlag = sendCancelFlag;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
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
            if( annotation != null && field.getName().equals(fieldName) ){
                offset = annotation.offset();
                break;
            }
		}
		
		return offset;
	}
}