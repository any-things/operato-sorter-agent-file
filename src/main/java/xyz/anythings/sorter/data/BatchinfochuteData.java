package xyz.anythings.sorter.data;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.model.BatchinfochuteEntity;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class BatchinfochuteData {
String uri = "" /*Constants.getWasIp()*/ + "/data/batchinfo/";
	
	public List<BatchinfochuteEntity> getChutePlanQty(int batchId) throws Exception {
		String value = HttpUtil.executeGetMethod(uri + batchId + "/chute");
		List<BatchinfochuteEntity> totalChuteList = new ArrayList<BatchinfochuteEntity>();

		if (value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					BatchinfochuteEntity batchInfo = (BatchinfochuteEntity) FormatUtil.jsonToObject(jsonObj.toString(), BatchinfochuteEntity.class);
					
					totalChuteList.add(batchInfo);
				}
				return totalChuteList;
			}
		}
		return null;
	}
}
