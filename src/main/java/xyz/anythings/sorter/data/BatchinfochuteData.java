package xyz.anythings.sorter.data;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class BatchinfochuteData {
String uri = "" /*Constants.getWasIp()*/ + "/data/batchinfo/";
	
	public List<BatchinfochuteData> getChutePlanQty(int batchId) throws Exception {
		String value = HttpUtil.executeGetMethod(uri + batchId + "/chute");
		List<BatchinfochuteData> totalChuteList = new ArrayList<BatchinfochuteData>();

		if (value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					BatchinfochuteData batchInfo = (BatchinfochuteData) FormatUtil.jsonToObject(jsonObj.toString(), BatchinfochuteData.class);
					
					totalChuteList.add(batchInfo);
				}
				return totalChuteList;
			}
		}
		return null;
	}
}
