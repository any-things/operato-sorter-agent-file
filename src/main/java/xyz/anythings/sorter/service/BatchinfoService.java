package xyz.anythings.sorter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.anythings.sorter.model.BatchinfoEntity;
import xyz.elidom.util.FormatUtil;

@Service
@Transactional
public class BatchinfoService {
	private void batch(String message) {
		BatchinfoEntity batchInfoEntity = FormatUtil.jsonToObject(message, BatchinfoEntity.class);
		batchStart(batchInfoEntity);
	}
	
	private void batchStart(BatchinfoEntity input) {
		
	}
}
