package xyz.anythings.sorter.data;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.model.ErrorcodeEntity;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class ErrorcodeData {
	String uri =  "" /*Constants.getWasIp()*/ + "/data/lov/errorCode";
	
	public List<ErrorcodeEntity> getErrorCode() throws Exception {
		String value = HttpUtil.executeGetMethod(uri);
		List<ErrorcodeEntity> errorCodeList = new ArrayList<ErrorcodeEntity>();
		
		if(value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if(data != null) {
				for(int i = 0 ; i < data.size() ; i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					ErrorcodeEntity errorCode = (ErrorcodeEntity) FormatUtil.jsonToObject(jsonObj.toString(), ErrorcodeEntity.class);
					
					errorCodeList.add(errorCode);
				}
				return errorCodeList;
			}
		}
		return null;
	}
}
