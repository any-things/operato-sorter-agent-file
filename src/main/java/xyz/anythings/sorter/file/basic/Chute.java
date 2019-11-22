package xyz.anythings.sorter.file.basic;

import java.io.RandomAccessFile;
import java.util.List;

import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.BatchinfochuteEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;

public class Chute {
	public void writeChuteDat(List<BatchinfochuteEntity> batchInfoChuteEntity, String path) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(
				path + "/" + Constants.CHUTE.toUpperCase() + Constants.DAT, "rw");

		Class cls = Class
				.forName(Constants.AGENT_PATH + "." + Constants.MODEL_PATH + "." + Constants.CHUTE.replace("_", ""));
		FileGet obj = (FileGet) cls.newInstance();
		createFile(raf, cls, obj, batchInfoChuteEntity);

		raf.close();
	}
	
	public synchronized void createFile(RandomAccessFile raf, Class cls, FileGet obj, List<BatchinfochuteEntity> batchInfoChuteEntity) throws Exception {
		if(batchInfoChuteEntity == null) {
			for(int i = 0 ; i < 200 ; i++) {
				raf.write(String.format("%03d", i+1).getBytes());
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeShort(0, (short) 1));
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeInt(0, 0));
				raf.write("                                                  ".getBytes());
				raf.write(Util.writeShort(0, (short) 0));
				raf.write(Util.writeShort(0, (short) 0));
				raf.write("                         ".getBytes());
			}
		} else {
			for(int i = 0 ; i < batchInfoChuteEntity.size() ; i++) {
				raf.write(String.format("%03d", i+1).getBytes());
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeShort(0, (short) 1));
				raf.write(Util.writeInt(0, Integer.parseInt(batchInfoChuteEntity.get(i).getPlanQty())));
				raf.write(Util.writeInt(0, 0));
				raf.write("                                                  ".getBytes());
				raf.write(Util.writeShort(0, (short) 0));
				raf.write(Util.writeShort(0, (short) 0));
				raf.write("                         ".getBytes());
			}
			for(int i = batchInfoChuteEntity.size()  ; i < 200 ; i++) {
				raf.write(String.format("%03d", i + 1).getBytes());
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeShort(0, (short) 1));
				raf.write(Util.writeInt(0, 0));
				raf.write(Util.writeInt(0, 0));
				raf.write("                                                  ".getBytes());
				raf.write(Util.writeShort(0, (short) 0));
				raf.write(Util.writeShort(0, (short) 0));
				raf.write("                         ".getBytes());
			}
		}
	}
}
