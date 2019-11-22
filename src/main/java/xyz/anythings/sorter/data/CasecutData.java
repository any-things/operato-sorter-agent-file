package xyz.anythings.sorter.data;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.model.CasecutEntity;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class CasecutData {
String uri = "" /*Constants.getWasIp()*/ + "/data/casecut/";
	
	public List<CasecutEntity> caseCut(String batchId, String chuteNo) throws Exception {
		String value = HttpUtil.executeGetMethod(uri + batchId + "/" + chuteNo);
		List<CasecutEntity> casecutList = new ArrayList<CasecutEntity>();
		
		if(value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if(data != null) {
				for (int i = 0 ; i < data.size() ; i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					CasecutEntity casecut = (CasecutEntity) FormatUtil.jsonToObject(jsonObj.toString(), CasecutEntity.class);
					
					casecutList.add(casecut);
				}
				return casecutList;
			}
		}
		return null;
	}
}