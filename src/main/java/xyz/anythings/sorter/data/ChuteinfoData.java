package xyz.anythings.sorter.data;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.ChuteinfoEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class ChuteinfoData {
String uri = "" /*Constants.getWasIp()*/ + "/data/batchinfo/";
	
	public List<ChuteinfoEntity> getBatchInfo(int batchId) throws Exception {
		String value = HttpUtil.executeGetMethod(uri + batchId);
		List<ChuteinfoEntity> batchInfoList = new ArrayList<ChuteinfoEntity>();

		if (value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					ChuteinfoEntity batchInfo = (ChuteinfoEntity) FormatUtil.jsonToObject(jsonObj.toString(), ChuteinfoEntity.class);
					
					batchInfoList.add(batchInfo);
				}
				return batchInfoList;
			}
		}
		return null;
	}
	
	public void writeSortQ(List<ChuteinfoEntity> batchinfoEntity, String path, String type) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(
				path + "/" + Constants.SORTQ.toUpperCase() + Constants.DAT, "rw");

		Class cls = Class
				.forName(Constants.AGENT_PATH + "." + Constants.MODEL_PATH + "." + Constants.SORTQ.replace("_", ""));
		FileGet obj = (FileGet) cls.newInstance();
		createFile(raf, cls, obj, batchinfoEntity, type);

		raf.close();
	}
	
	public synchronized void createFile(RandomAccessFile raf, Class cls, FileGet obj, List<ChuteinfoEntity> batchinfoEntity, String type) throws Exception {
		String strBlock2 = "                    ";
		
		for(int i = 0 ; i < batchinfoEntity.size() ; i++) {
			if(type.equals(Constants.RETURN_BATCH)) {
				strBlock2 = batchinfoEntity.get(i).getInvoiceNo();
			}
			
			byte[] byteSkuName = Util.koEncodeByte(40, batchinfoEntity.get(i).getSkuName());

			raf.write(String.format("%-20s", batchinfoEntity.get(i).getSkuBarCode()).getBytes());
			raf.write(String.format("%-20s",strBlock2).getBytes());
			raf.write("                    ".getBytes());
			raf.write(String.format("%-20s", batchinfoEntity.get(i).getSkuBarCode()).getBytes());
			raf.write(String.format("%-40s", batchinfoEntity.get(i).getSkuCode()).getBytes());
			raf.write(byteSkuName);
			raf.write(Util.writeInt(0, 0));//
			raf.write(Util.writeInt(0, 0));//
			raf.write(String.format("%03d", Integer.parseInt(batchinfoEntity.get(i).getChuteId())).getBytes());
			raf.write(Util.writeInt(0, Integer.parseInt(batchinfoEntity.get(i).getPlanQty())));
			raf.write(Util.writeInt(0, 0));//
			raf.write(Util.writeInt(0, 0));//
			raf.write("                                                  ".getBytes());
			raf.write("                                                                   ".getBytes());
		}
	}
}