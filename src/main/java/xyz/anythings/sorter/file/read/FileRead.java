package xyz.anythings.sorter.file.read;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.anythings.sorter.file.context.MyContextContainer;
import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.BatchEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.ThreadUtil;

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
							if(obj.getSomething("mark").equals(Constants.MARK_C)) {
								
							} else if (obj.getSomething("mark").equals(Constants.MARK_A)) {
								
							} else if (obj.getSomething("mark").equals(Constants.MARK_S)) {
								
							} else if (obj.getSomething("mark").equals(Constants.MARK_E)) {
								
							} else if (obj.getSomething("mark").equals(Constants.MARK_e)) {
								
							} else if (obj.getSomething("mark").equals(Constants.MARK_R)) {
								
							} else if (obj.getSomething("mark").equals(Constants.MARK_F)) {
								
							} else if (obj.getSomething("mark").equals(Constants.MARK_B)) {
								
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
		
//		try {
//			
//
//			
//
//			while ((raf.read(buffer)) != -1) {
//				obj = mContext.get(cls.newInstance().getClass(), buffer);
//
//				if (modelName.equals(Constants.BATCHDB)) {
//					if(obj.getSomething("mark").equals(Constants.MARK_C)) {
////						Casecut casecut = new Casecut();
////						List<CasecutEntity> casecutEntity = casecut.caseCut(batchFileEntity.getId(), obj.getSomething("chuteNo").toString());
////						if(casecutEntity == null) {
////							throw new Exception("Case Cut 오류");
////						}
////						
////						Clplabelformatdownload labelFormatDownload = new Clplabelformatdownload();
////						String msg = labelFormatDownload.setUpLabelPrint(batchFileEntity.getId(), obj.getSomething("chuteNo").toString(), labelPath, casecutEntity, type);
////						if(msg.equals(Constants.RESULT_FAIL)) {
////							throw new Exception("라벨 양식 오류");
////						}
//					} else if (obj.getSomething("mark").equals(Constants.MARK_A)) {
//						if(this.type.equals(Constants.RELEASE_BATCH) || obj.getSomething("resultCode3").toString().trim().equals(("0"))) {
////							SortInfo sortInfo = new SortInfo();
////							SortInfoEntity sortInfoEntity = sortInfo.sortInfo(obj, batchFileEntity.getId());
////							
////							if(this.type.equals(Constants.RELEASE_BATCH)) {
////								if(this.rebuildPath != null && sortInfoEntity.getIsCompleted().equals("Y")) {
////									writeRebuildFinish(this.rebuildPath, sortInfoEntity.getChuteCode());
////								}
////							}
////							
////							if(sortInfoEntity == null) {
////								throw new Exception("실적 오류");
////							} else {
////								String chuteNo = obj.getSomething("chuteNo").toString();
////								int chute = Integer.parseInt(chuteNo);
////								Indicator indi = new Indicator();
////								IndicatorEntity indicator = indi.indicatorChute(Integer.toString(chute));
////								
////								byte[] byteNodeId = Util.hexToByteArray(indicator.getNodeId());
////								String strValue = "0";
////								
////								if(type.equals(Constants.RETURN_BATCH_NODATA)) {
////									strValue = indicator.getSortedQty();
////								} else {
////									strValue = Integer.toString(Integer.parseInt(indicator.getPlanQty()) - Integer.parseInt(indicator.getSortedQty()));
////								}
////							}
//						} else {
////							Errorinfo errorInfo = new Errorinfo();
////							List<ErrorinfoEntity> errorInfoList = errorInfo.errorInfo(obj, batchFileEntity.getId());
////							
////							if(errorInfoList == null) {
////								throw new Exception("사유코드 실적 오류");
////							}
////							
////							String msg = errorInfo.setUpErrorLabelPrint(errorInfoList);
////							if(msg.equals(Constants.RESULT_FAIL)) {
////								throw new Exception("사유코드 라벨 양식 오류");
////							}
//						}
//						
//						if(!this.type.equals(Constants.RELEASE_BATCH)) {
////							Map<String, Object> out = new HashMap<String, Object>();
////							out.put("batchId", batchFileEntity.getId());
////							out.put("barcode", obj.getSomething("majorCode1").toString());
////							out.put("inputPcNo", obj.getSomething("inNo").toString());
////							out.put("event", Constants.MARK_A);
////							
////							String inputPc = obj.getSomething("inNo").toString().equals(" ") ? "0" : obj.getSomething("inNo").toString();
////							Util.sendMessage(Constants.getSimpMessagingTemplate(), out, String.format("%03d", Integer.parseInt(inputPc)));
//						}
//					} else if (obj.getSomething("mark").equals(Constants.MARK_S)) {
//						// Batch 시작
//					} else if (obj.getSomething("mark").equals(Constants.MARK_E)) {
//						// Batch 종료
////						Sortcomplete sortComplete = new Sortcomplete();
////						sortComplete.sortComplete(batchFileEntity.getId(), obj.getSomething("majorCode1").toString().trim());
//						
//					} else if (obj.getSomething("mark").equals(Constants.MARK_e)) {
//						// Batch 중단
////						Sortcomplete sortComplete = new Sortcomplete();
////						sortComplete.sortComplete(batchFileEntity.getId(), obj.getSomething("majorCode1").toString().trim());
//					} else if (obj.getSomething("mark").equals(Constants.MARK_R)) {
//						// Error Reject
////						ScanInfo scanInfo = new ScanInfo();
////						boolean boolScanInfo = scanInfo.scanInfo(obj, batchFileEntity.getId());
////						
////						if(!boolScanInfo) {
////							throw new Exception("스캔오류 직진슈트");
////						}
//					} else if (obj.getSomething("mark").equals(Constants.MARK_F)) {
//						// Chute complete
//					} else if (obj.getSomething("mark").equals(Constants.MARK_B)) {
//						//투입구PC 
//						
////						if(!this.type.equals(Constants.RELEASE_BATCH)) {
////							String barcode = (obj.getSomething("resultCode1").toString() + obj.getSomething("resultCode2").toString()).trim();
////							
////							Map<String, Object> out = new HashMap<String, Object>();
////							out.put("batchId", batchFileEntity.getId());
////							out.put("barcode", barcode);
////							out.put("inputPcNo", obj.getSomething("inNo").toString());
////							out.put("event", Constants.MARK_B);
////							
////							String inputPc = obj.getSomething("inNo").toString().equals(" ") ? "0" : obj.getSomething("inNo").toString();
////							Util.sendMessage(Constants.getSimpMessagingTemplate(), out, String.format("%03d", Integer.parseInt(inputPc)));
////						}
//					}
//				}
//				
//				System.out.println("lineCnt : " + lineCnt);
//				lineCnt++;
//			}
//		} catch (FileNotFoundException e) {
////			lineCnt++;
//			try {
//				raf = getAccessFile();
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		} catch (IOException e) {
////			lineCnt++;
//			try {
////				raf.close();
//				raf = getAccessFile();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//		} catch (Exception e) {
//			lineCnt++;
//		}
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