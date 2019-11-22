package xyz.anythings.sorter.data;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.MergeEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class MergeData {
String uri = "" /*Constants.getWasIp()*/ + "/data/merge/batchinfo/";
	
	public List<MergeEntity> getMerge(int batchId) throws Exception {
		String value = HttpUtil.executeGetMethod(uri + batchId);
		List<MergeEntity> mergeList = new ArrayList<MergeEntity>();
		
		if(value != null) {
			JSONArray data = Util.jsonArrayParser(value);
			if(data != null) {
				for(int i = 0 ; i < data.size() ; i++) {
					JSONObject jsonObj = (JSONObject) data.get(i);
					MergeEntity merge = (MergeEntity) FormatUtil.jsonToObject(jsonObj.toString(), MergeEntity.class);
					
					mergeList.add(merge);
				}
				return mergeList;
			}
		}
		return null;
	}
	
	public void writeRebuild(List<MergeEntity> mergeList, String path, String type) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(path + "/" + Constants.REBUILD.toUpperCase() + Constants.DAT, "rw");
		Class cls = Class
				.forName(Constants.AGENT_PATH + "." + Constants.MODEL_PATH + "." + Constants.REBUILD.replace("_", ""));
		FileGet obj = (FileGet) cls.newInstance();
		createFile(raf, cls, obj, mergeList, type);

		raf.close();
	}
	
	public synchronized void createFile(RandomAccessFile raf, Class cls, FileGet obj, List<MergeEntity> mergeList, String type) throws Exception {
//		sortQ 정보 set해야한다.
		String strBlock2 = "                    ";
		
		for(int i = 0 ; i < mergeList.size() ; i++) {
			if(type.equals(Constants.RETURN_BATCH)) {
				strBlock2 = mergeList.get(i).getInvoiceNo();
				if(strBlock2 == null) {
					strBlock2 = "123456789";
				}
			}
			
			byte[] byteSkuName = Util.koEncodeByte(40, mergeList.get(i).getSkuName());
			
			raf.write(" ".getBytes());
			raf.write("I".getBytes());
			raf.write(String.format("%-20s", mergeList.get(i).getSkuBarCode()).getBytes());
			raf.write(String.format("%-20s",strBlock2).getBytes());
			raf.write("                    ".getBytes());
			raf.write(String.format("%-20s", mergeList.get(i).getSkuBarCode()).getBytes());
			raf.write(String.format("%-40s", mergeList.get(i).getSkuCode()).getBytes());
			raf.write(byteSkuName);
			raf.write(Util.writeInt(0, 0));//
			raf.write(Util.writeInt(0, 0));//
			raf.write(String.format("%03d", Integer.parseInt(mergeList.get(i).getChuteId())).getBytes());
			raf.write(Util.writeInt(0, Integer.parseInt(mergeList.get(i).getPlanQty())));
			raf.write(Util.writeInt(0, 0));//
			raf.write(Util.writeInt(0, 0));//
			raf.write("                                                  ".getBytes());
			raf.write("                                                                   ".getBytes());
//			sortQ 정보 write			
			raf.write("         ".getBytes());
			raf.write("                                                ".getBytes());
			raf.write(0x0A);
		}
	}
}