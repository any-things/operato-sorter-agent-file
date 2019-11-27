package xyz.anythings.sorter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.anythings.sorter.data.BatchData;
import xyz.anythings.sorter.data.BatchinfochuteData;
import xyz.anythings.sorter.data.ChuteinfoData;
import xyz.anythings.sorter.data.ErrorcodeData;
import xyz.anythings.sorter.data.MergeData;
import xyz.anythings.sorter.file.basic.Chute;
import xyz.anythings.sorter.file.basic.Member;
import xyz.anythings.sorter.file.read.FileManager;
import xyz.anythings.sorter.model.BatchEntity;
import xyz.anythings.sorter.model.BatchinfoEntity;
import xyz.anythings.sorter.model.BatchinfochuteEntity;
import xyz.anythings.sorter.model.ChuteinfoEntity;
import xyz.anythings.sorter.model.ErrorcodeEntity;
import xyz.anythings.sorter.model.MergeEntity;
import xyz.anythings.sorter.tcp.TcpSocketClientFactory;
import xyz.anythings.sorter.tcp.TcpUtil;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.exception.server.ElidomRuntimeException;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.ValueUtil;

@Service
@Transactional
public class BatchinfoService {
	private final int batchStart = 1;
	private final int batchReStart = 2;
	private final int batchStop = 3;
	private final int batchPause = 4;
	
	@Autowired
	protected TcpSocketClientFactory tcpSocketClientFactory;
	
	@Autowired
	protected FileManager fileManager;
	
	private void batch(String message) {
		BatchinfoEntity batchInfoEntity = FormatUtil.jsonToObject(message, BatchinfoEntity.class);
		batchStart(batchInfoEntity);
	}
	
	private void batchStart(BatchinfoEntity input) {
		int mode = input.getMode();
		String type = input.getType();
		String batchMode = "";
		String batchSort = "";
		
		BatchData batch = new BatchData();
		
		try {
			BatchEntity batchEntity = batch.getBatch(input.getId());
			
			if(batchEntity != null) {
				String dataPath = Constants.FILE_ROOT_PATH + "/DATA";
				String datePath = dataPath + "/DATE" + batchEntity.getCreated().substring(5, 10).replaceAll("-", "");
				String batchPath = datePath + "/" + batchEntity.getCode();
				
				Chute chute = new Chute();
				MergeData merge = new MergeData();
				Member member = new Member();
				ErrorcodeData errorCode = new ErrorcodeData();
				BatchinfochuteData batchInfoChute = new BatchinfochuteData();
				
				switch (mode) {
				case batchStart:
					Util.makeFile(batchPath, "");
					Util.makeFile(batchPath, Constants.SORTQ.toUpperCase() + Constants.DAT);
					Util.makeFile(batchPath, Constants.CHUTE.toUpperCase() + Constants.DAT);
					Util.makeFile(batchPath, Constants.SORTA.toUpperCase() + Constants.DAT);
					Util.makeFile(batchPath, Constants.MEMBER.toUpperCase() + Constants.DAT);
					Util.makeFile(batchPath, Constants.REBUILD.toUpperCase() + Constants.DAT);
					Util.makeFile(batchPath, Constants.SIMPLE.toUpperCase() + Constants.DAT);
					
					batch.writeFile(Constants.EXECBAT, batchEntity, dataPath, type);
					
					// 예정있는 반품
					if(ValueUtil.isEqualIgnoreCase(type, Constants.RETURN_BATCH)) {
						/**
						 * /data/batchinfo/batchId 로 service 호출
						 **/
						ChuteinfoData chuteInfo = new ChuteinfoData();
						List<ChuteinfoEntity> chuteinfoEntity = chuteInfo
								.getBatchInfo(input.getId());
						if (chuteinfoEntity == null) {
							handleUnkownMessage("해당 Batch Code에 할당된 Chute가 없습니다.");
							break;
						}
						List<BatchinfochuteEntity> batchInfoChuteEntity = batchInfoChute.getChutePlanQty(input.getId());
						if (batchInfoChuteEntity == null) {
							handleUnkownMessage("해당 Batch Code에 할당된 Chute가 없습니다.");
							break;
						}
						
						/**
						 * return받은 값으로 CHUTE.DATA FILE 작성
						 */
						chute.writeChuteDat(batchInfoChuteEntity, batchPath);
						/**
						 * return받은 값으로 SORT_Q.DAT FILE 작성
						 **/
						chuteInfo.writeSortQ(chuteinfoEntity, batchPath, type);
						
						List<ErrorcodeEntity> errorCodeList = errorCode.getErrorCode();
						/**
						 * return받은 값으로 MEMBER.DAT FILE 작성
						 **/
						member.writeMemberDat(batchPath, errorCodeList);
					// 예정없는 반품
					} else if(ValueUtil.isEqualIgnoreCase(type, Constants.RETURN_BATCH_NODATA)) {
						chute.writeChuteDat(null, batchPath);
					// 출고
					} else if(ValueUtil.isEqualIgnoreCase(type, Constants.RELEASE_BATCH)) {
						ChuteinfoData chuteInfo = new ChuteinfoData();
						List<ChuteinfoEntity> chuteinfoEntity = chuteInfo.getBatchInfo(input.getId());
						if (chuteinfoEntity == null) {
							handleUnkownMessage("해당 Batch Code에 할당된 Chute가 없습니다");
							break;
						}
						List<BatchinfochuteEntity> batchInfoChuteEntity = batchInfoChute.getChutePlanQty(input.getId());
						if (batchInfoChuteEntity == null) {
							handleUnkownMessage("해당 Batch Code에 할당된 Chute가 없습니다");
							break;
						}
						/**
						 * return받은 값으로 CHUTE.DATA FILE 작성
						 **/
						chute.writeChuteDat(batchInfoChuteEntity, batchPath);
						/**
						 * return받은 값으로 SORT_Q.DAT FILE 작성
						 **/
						chuteInfo.writeSortQ(chuteinfoEntity, batchPath, type);
					}
					
					batchMode = TcpUtil.appendSpace(Constants.SOCKET_BATCH_START, 1);
					batchSort = TcpUtil.appendSpace(Constants.SOCKET_BATCH_SORT_CONTINUE, 1);
					
					fileManager.startFileServer(batchEntity, Constants.DEFAULT_CONTROL_PC_DATA_FOLDER, Constants.BATCHDB);
					
					break;
				case batchReStart:
					if(ValueUtil.toBoolean(input.getIsMerged())) {
						List<MergeEntity> mergeList = merge.getMerge(input.getId());
						if (mergeList != null) {
							/**
							 * REBUILD.DAT FILE 작성
							 **/
							merge.writeRebuild(mergeList, batchPath, type);
						}
					}
					
					batchMode = TcpUtil.appendSpace(Constants.SOCKET_BATCH_START, 1);
					batchSort = TcpUtil.appendSpace(Constants.SOCKET_BATCH_SORT_CONTINUE, 1);
					
					fileManager.startFileServer(batchEntity, Constants.DEFAULT_CONTROL_PC_DATA_FOLDER, Constants.BATCHDB);
					break;
				case batchStop:
					batchMode = TcpUtil.appendSpace(Constants.SOCKET_BATCH_STOP, 1);
					batchSort = TcpUtil.appendSpace(Constants.SOCKET_BATCH_SORT_CONTINUE, 1);
					
					fileManager.stopFileServer(Constants.BATCHDB);
					break;
				case batchPause:
					batchMode = TcpUtil.appendSpace(Constants.SOCKET_BATCH_PAUSE, 1);
					batchSort = TcpUtil.appendSpace(Constants.SOCKET_BATCH_SORT_CONTINUE, 1);
					
					fileManager.stopFileServer(Constants.BATCHDB);
					break;
				default:
					break;
				}
				String socketCommand = TcpUtil.appendSpace(Constants.SOCKET_BATCH_COMMAND, 1);
				String batchCode = TcpUtil.appendSpace(batchEntity.getCode(), 8);
				String batchName = TcpUtil.appendSpace(batchEntity.getName(), 20);
				String batchCreated = TcpUtil.appendSpace(batchEntity.getCreated(), 8);
				
				StringBuffer buffer = new StringBuffer();
				buffer.append(socketCommand).append(batchMode).append(batchCode).append(batchName).append(batchCreated).append(batchSort);
				
				tcpSocketClientFactory.send("", 0, buffer.toString());
			} else {
				
			}
		} catch (Exception e) {
			this.handleUnkownMessage("등록되지 않은 Batch Code 입니다.");
		}
		
	}
	
	private void handleUnkownMessage(String msgObj) {
		throw new ElidomRuntimeException(msgObj);
	}
}
