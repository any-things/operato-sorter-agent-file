package xyz.anythings.sorter.data;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.model.CasecutallEntity;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class Casecutall {
	String uri = "" /*Constants.getWasIp()*/ + "/data/casecut/";
	
	public List<CasecutallEntity> casecutAll(String batchId) throws Exception {
		String value = HttpUtil.executeGetMethod(uri + batchId);
		List<CasecutallEntity> casecutList = new ArrayList<CasecutallEntity>();
		
		if(value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if(data != null) {
				for(int i = 0 ; i < data.size() ; i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					CasecutallEntity casecutAll = (CasecutallEntity) FormatUtil.jsonToObject(jsonObj.toString(), CasecutallEntity.class);
					
					casecutList.add(casecutAll);
				}
				return casecutList;
			}
		}
		return null;
	}
}