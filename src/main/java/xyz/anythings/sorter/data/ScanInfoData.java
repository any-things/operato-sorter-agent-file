package xyz.anythings.sorter.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.SortInfoEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class ScanInfoData {
String uri =  "" /*Constants.getWasIp()*/ + "/data/scaninfo";
	
	public boolean scanInfo(FileGet fileGet, String batchId) throws Exception {
		String invoiceNo = (fileGet.getSomething("resultCode1").toString() + fileGet.getSomething("resultCode2").toString()).trim();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("batchId", batchId);
		params.put("skuBarCode", fileGet.getSomething("majorCode1").toString().trim());
		params.put("quantity", fileGet.getSomething("count").toString().trim());
		params.put("chuteCode", fileGet.getSomething("chuteNo").toString().trim());
		params.put("inputPcNo", fileGet.getSomething("inNo").toString().trim());
		params.put("invoiceNo", invoiceNo);
		
		list.add(params);
		
		String jsonString = FormatUtil.toJsonString(list);
		String value = HttpUtil.executePostMethodForJson(uri, jsonString);
//		String value = HttpUtil.executeGetMethod(uri);
		JSONArray data = Util.jsonArrayParser(value);
		
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				JSONObject jsonObj = (JSONObject) data.get(i);
				SortInfoEntity sortInfo = (SortInfoEntity) FormatUtil.jsonToObject(jsonObj.toString(),
						SortInfoEntity.class);
				
				System.out.println("scanInfo batchId : " + sortInfo.getBatchId());
				System.out.println("scanInfo skuBarCode : " + sortInfo.getSkuBarCode());
				System.out.println("scanInfo invoiceNo : " + sortInfo.getInvoiceNo());
				System.out.println("scanInfo quantity : " + sortInfo.getQuantity());
				System.out.println("scanInfo result : " + sortInfo.getResult());
				if(sortInfo.getResult().equals(Constants.RESULT_OK)) {
					return true;
				}
			}
		}
		return false;
	}
}