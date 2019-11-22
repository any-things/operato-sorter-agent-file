package xyz.anythings.sorter.data;

import java.io.RandomAccessFile;

import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.BatchEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class BatchData {
	public static int lineNo = 0;
	String uri = "" /*Constants.getWasIp()*/ + "/data/batch/";

	public BatchData getBatch(int batchId) throws Exception {
		String value = HttpUtil.executeGetMethod(uri + batchId);
		
		if (value != null && !value.isEmpty()) {
			BatchData batch = (BatchData) FormatUtil.jsonToObject(value, BatchData.class);

			return batch;
		}
		return null;
	}

	public void writeFile(String fileName, BatchEntity batchEntity, String path, String type) throws NumberFormatException, Exception {
		RandomAccessFile raf = new RandomAccessFile(
				path + "/" + fileName.toUpperCase() + Constants.DAT, "rw");

		Class cls = Class
				.forName(Constants.AGENT_PATH + "." + Constants.MODEL_PATH + "." + fileName.replace("_", ""));
		FileGet obj = (FileGet) cls.newInstance();
		createFile(raf, cls, obj, batchEntity, type);
		lineNo++;
		raf.close();
	}

	public synchronized void createFile(RandomAccessFile raf, Class cls, FileGet obj, BatchEntity batchEntity, String type) throws NumberFormatException, Exception {
		String ststus =Util.getBatchStatus(batchEntity.getStatus());
		String strJob = " ";
		if(type.equals(Constants.RETURN_BATCH)) {
			strJob = "1";
		} else if(type.equals(Constants.RETURN_BATCH_NODATA)) {
			strJob = "3";
		} else if(type.equals(Constants.RELEASE_BATCH)) {
			strJob = " ";
		}
		
		byte[] batchName = Util.koEncodeByte(20, batchEntity.getName());
		raf.seek(raf.length());

		obj.setSomething("lineNo", " ");
		obj.setSomething("job", strJob);
		obj.setSomething("makeDate", batchEntity.getCreated().substring(0, 10).replaceAll("-", ""));
		obj.setSomething("batchNo", String.format("%-8s", batchEntity.getCode()));
		obj.setSomething("batchName", new String(batchName, Constants.ENCODE_TYPE));
		obj.setSomething("statusFlag", ststus);
		obj.setSomething("checkFlag", "??????????");
		obj.setSomething("maxCaseCapacity", 0);
		obj.setSomething("resultFlag", " ");
		obj.setSomething("startTime", 0);
		obj.setSomething("loadTime", 0);
		obj.setSomething("workLineNo", " ");
		obj.setSomething("sendCancelFlag", " ");
		obj.setSomething("operateType", " ");
		obj.setSomething("filler", "                                   ");

		raf.write(obj.getSomething("lineNo").toString().getBytes());
		raf.write(obj.getSomething("job").toString().getBytes());
		raf.write(obj.getSomething("makeDate").toString().getBytes());
		raf.write(obj.getSomething("batchNo").toString().getBytes());
		raf.write(batchName);
		raf.write(obj.getSomething("statusFlag").toString().getBytes());
		raf.write(obj.getSomething("checkFlag").toString().getBytes());
		raf.write(Util.writeInt(0, Integer.parseInt(obj.getSomething("maxCaseCapacity").toString())));
		raf.write(obj.getSomething("resultFlag").toString().getBytes());
		raf.write(Util.writeInt(0, Integer.parseInt(obj.getSomething("startTime").toString())));
		raf.write(Util.writeInt(0, Integer.parseInt(obj.getSomething("loadTime").toString())));
		raf.write(obj.getSomething("workLineNo").toString().getBytes());
		raf.write(obj.getSomething("sendCancelFlag").toString().getBytes());
		raf.write(obj.getSomething("operateType").toString().getBytes());
		raf.write(obj.getSomething("filler").toString().getBytes());
	}
}
