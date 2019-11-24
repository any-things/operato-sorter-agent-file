package xyz.anythings.sorter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.anythings.sorter.data.BatchData;
import xyz.anythings.sorter.data.BatchinfochuteData;
import xyz.anythings.sorter.data.ErrorcodeData;
import xyz.anythings.sorter.data.MergeData;
import xyz.anythings.sorter.file.basic.Chute;
import xyz.anythings.sorter.file.basic.Member;
import xyz.anythings.sorter.model.BatchEntity;
import xyz.anythings.sorter.model.BatchinfoEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.exception.server.ElidomRuntimeException;
import xyz.elidom.util.FormatUtil;

@Service
@Transactional
public class BatchinfoService {
	private final int batchStart = 1;
	private final int batchReStart = 2;
	private final int batchStop = 3;
	private final int batchPause = 4;
	
	int mode = 0;
	String type = null;
	String dataPath = null;
	String datePath = null;
	String batchPath = null;
	
	private void batch(String message) {
		BatchinfoEntity batchInfoEntity = FormatUtil.jsonToObject(message, BatchinfoEntity.class);
		batchStart(batchInfoEntity);
	}
	
	private void batchStart(BatchinfoEntity input) {
		mode = input.getMode();
		type = input.getType();
		
		BatchData batch = new BatchData();
		
		try {
			BatchEntity batchEntity = batch.getBatch(input.getId());
			
			if(batchEntity != null) {
				dataPath = Constants.FILE_ROOT_PATH + "/DATA";
				datePath = dataPath + "/DATE" + batchEntity.getCreated().substring(5, 10).replaceAll("-", "");
				batchPath = datePath + "/" + batchEntity.getCode();
				
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
					
					break;
				case batchReStart:
					break;
				case batchStop:
					break;
				case batchPause:
					break;
				default:
					break;
				}
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
