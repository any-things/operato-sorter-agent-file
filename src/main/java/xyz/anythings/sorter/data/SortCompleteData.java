package xyz.anythings.sorter.data;

import java.util.HashMap;
import java.util.Map;

import xyz.anythings.sorter.model.SortCompleteEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class SortCompleteData {
	String uri = "" /*Constants.getWasIp()*/ + "/data/sortcomplete";
	
	public boolean sortComplete(String batchId, String runningTime) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("batchId", batchId);
		params.put("runningTime", runningTime);
		
		String jsonString = FormatUtil.toJsonString(params);
		
		String value = HttpUtil.executePostMethodForJson(uri, jsonString);
		
		if(value != null) {
			SortCompleteEntity sortCompleteEntity = FormatUtil.jsonToObject(value, SortCompleteEntity.class);
			
			if(sortCompleteEntity.getResult().equals(Constants.RESULT_OK)) {
				return true;
			}
		}
		return false;
	}
}
