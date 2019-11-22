package xyz.anythings.sorter.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.model.AssignEntity;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class AssignData {
	String uri = "" /*Constants.getWasIp()*/ + "/data/assign";
	
	public AssignEntity assignChuteId(String batchId, String skuBarCode) throws Exception {
		AssignEntity output = new AssignEntity();
		
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("batchId", batchId);
		params.put("skuBarCode", skuBarCode.trim());//LE4C3C10_BRH_M,   LE4C3C10_BRH_S
		params.put("invoiceNo", "");
		
		list.add(params);
		
		String jsonString = FormatUtil.toJsonString(list);
		
		String value = HttpUtil.executePostMethodForJson(uri, jsonString);
		if (value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					output = (AssignEntity) FormatUtil.jsonToObject(jsonObj.toString(), AssignEntity.class);
					
					return output;
				}
			}
		}
		
		return output;
	}
}
