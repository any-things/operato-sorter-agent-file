package xyz.anythings.sorter.file.read;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.anythings.sorter.data.CasecutData;
import xyz.anythings.sorter.data.ErrorinfoData;
import xyz.anythings.sorter.data.ScanInfoData;
import xyz.anythings.sorter.data.SortCompleteData;
import xyz.anythings.sorter.data.SortInfoData;
import xyz.anythings.sorter.file.context.MyContextContainer;
import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.BatchEntity;
import xyz.anythings.sorter.model.CasecutEntity;
import xyz.anythings.sorter.model.ErrorinfoEntity;
import xyz.anythings.sorter.model.SortInfoEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.exception.server.ElidomRuntimeException;
import xyz.elidom.util.ThreadUtil;
import xyz.elidom.util.ValueUtil;

public class FileRead {
	public static long lineCnt = 0;
	/**
	 * 현재 실행 중인지 여부
	 */
	private boolean isRunning;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void readFile(BatchEntity batchEntity, String path, String modelName) {
		
		if (this.isRunning) {
			return;
		}
		
		ThreadUtil.doAsynch(() -> {
			this.isRunning = true;
			
			try {
				RandomAccessFile raf = new RandomAccessFile(path + "/" + batchEntity.getCreated().substring(0, 10).replaceAll("-", "") + "_" + batchEntity.getCode() + ".DB", "rw");
				MyContextContainer mContext = new MyContextContainer();
				Class<?> cls = Class.forName(Constants.AGENT_PATH + "." + Constants.MODEL_PATH + "." + modelName.replace("_", ""));
				FileGet obj = (FileGet) cls.newInstance();
				
				while (this.isRunning) {
					try {
						System.out.println("length: " + obj.getRecordSize());
						raf.seek(lineCnt * obj.getRecordSize());
						
						byte[] buffer = new byte[obj.getRecordSize()];
						
						this.isRunning = (raf.read(buffer)) != -1;
						obj = mContext.get(cls.newInstance().getClass(), buffer);
						
						if (modelName.equals(Constants.BATCHDB)) {
							String action = ValueUtil.toString(obj.getSomething("mark"));
							
							switch (action) {
							case Constants.MARK_C :
								CasecutData casecut = new CasecutData();
								List<CasecutEntity> casecutEntity = casecut.caseCut(batchEntity.getCode(), obj.getSomething("chuteNo").toString());
								if(casecutEntity == null) {
									this.handleUnkownMessage("Case Cut 오류");
								}
								
								break;
							case Constants.MARK_A :
								if(/* -- batch type이 출고인지 체크해야함  this.type.equals(Constants.RELEASE_BATCH) ||*/ obj.getSomething("resultCode3").toString().trim().equals(("0"))) {
									SortInfoData sortInfo = new SortInfoData();
									SortInfoEntity sortInfoEntity = sortInfo.sortInfo(obj, batchEntity.getCode());
									
									// TODO RebuildPath 를 체크하고 데이터가 있는지 체크
//									if(this.type.equals(Constants.RELEASE_BATCH)) {
//										if(this.rebuildPath != null && sortInfoEntity.getIsCompleted().equals("Y")) {
//											writeRebuildFinish(this.rebuildPath, sortInfoEntity.getChuteCode());
//										}
//									}
									
									if(sortInfoEntity == null) {
										this.handleUnkownMessage("실적 오류");
									} else {
										// TODO 표시기 점등
									}
								} else {
									ErrorinfoData errorInfo = new ErrorinfoData();
									List<ErrorinfoEntity> errorInfoList = errorInfo.errorInfo(obj, batchEntity.getCode());
									
									if(errorInfoList == null) {
										this.handleUnkownMessage("사유코드 실적 오류");
									}
								}
								
								break;
							case Constants.MARK_S :
								
								break;
							case Constants.MARK_E :
								SortCompleteData sortComplete = new SortCompleteData();
								sortComplete.sortComplete(batchEntity.getCode(), obj.getSomething("majorCode1").toString().trim());
								
								break;
							case Constants.MARK_e :
								SortCompleteData sortCompleteData = new SortCompleteData();
								sortCompleteData.sortComplete(batchEntity.getCode(), obj.getSomething("majorCode1").toString().trim());
								
								break;
							case Constants.MARK_R : 
								// Error Reject
								ScanInfoData scanInfo = new ScanInfoData();
								boolean boolScanInfo = scanInfo.scanInfo(obj, batchEntity.getCode());
								
								if(!boolScanInfo) {
									throw new Exception("스캔오류 직진슈트");
								}
								
								break;
							case Constants.MARK_F :
								break;
							case Constants.MARK_B :
								// TODO 투입구 PC
								break;
							default:
								this.handleUnkownMessage("Not Found Mark");
							}
						} 

						System.out.println("lineCnt : " + lineCnt);
						lineCnt++;
					} catch (Exception e) {
						this.logger.error("File Read Error.", e);
//						ThreadUtil.sleep(retryWaitTime);
					}
				}
				
				raf.close();
			} catch (FileNotFoundException e1) {
				this.logger.error("FileNotFoundException", e1);
			} catch (IOException e) {
				this.logger.error("IOException.", e);
			} catch (Exception e) {
				this.logger.error("Exception.", e);
			} catch (Throwable e) {
				this.logger.error("Throwable.", e);
			} 
		});
	}
	
	void stop() {
		this.isRunning = false;
	}
	
	private void handleUnkownMessage(String msgObj) {
		throw new ElidomRuntimeException(msgObj);
	}
	
	private RandomAccessFile getAccessFile(BatchEntity batchEntity, String path) throws FileNotFoundException {
		RandomAccessFile raf = new RandomAccessFile(path + "/" + batchEntity.getCreated().substring(0, 10).replaceAll("-", "") + "_" + batchEntity.getCode() + ".DB", "rw");
		
		return raf;
	}
	
	private void writeRebuildFinish(String path, String chuteNo) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(path + "/" + Constants.REBUILD.toUpperCase() + Constants.DAT, "rw");
		Class cls = Class
				.forName(Constants.AGENT_PATH + "." + Constants.MODEL_PATH + "." + Constants.REBUILD.replace("_", ""));
		FileGet obj = (FileGet) cls.newInstance();
		createFile(raf, cls, obj, chuteNo);

		raf.close();
	}
	
	private synchronized void createFile(RandomAccessFile raf, Class cls, FileGet obj, String chuteNo) throws Exception {
		raf.write(" ".getBytes());
		raf.write("E".getBytes());
		raf.write("                    ".getBytes());
		raf.write("          ".getBytes());
		raf.write("          ".getBytes());
		raf.write("                    ".getBytes());
		raf.write("                    ".getBytes());
		raf.write("                                        ".getBytes());
		raf.write("                                        ".getBytes());
		raf.write(Util.writeInt(0, 0));//
		raf.write(Util.writeInt(0, 0));//
		raf.write(chuteNo.getBytes());
		raf.write(Util.writeInt(0, 0));
		raf.write(Util.writeInt(0, 0));//
		raf.write(Util.writeInt(0, 0));//
		raf.write("                                                  ".getBytes());
		raf.write("                                                                   ".getBytes());
//		sortQ 정보 write			
		raf.write("         ".getBytes());
		raf.write("                                                ".getBytes());
		raf.write(0x0A);
	}
}