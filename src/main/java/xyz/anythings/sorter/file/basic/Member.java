package xyz.anythings.sorter.file.basic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.ErrorcodeEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;

public class Member {
	public void writeMemberDat(String path, List<ErrorcodeEntity> errorCodeList) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		RandomAccessFile raf = new RandomAccessFile(
				path + "/" + Constants.MEMBER.toUpperCase() + Constants.DAT, "rw");

		Class cls = Class
				.forName(Constants.AGENT_PATH + "." + Constants.MODEL_PATH + "." + Constants.MEMBER.replace("_", ""));
		FileGet obj = (FileGet) cls.newInstance();
		createFile(raf, cls, obj, errorCodeList);

		raf.close();
	}
	
	public synchronized void createFile(RandomAccessFile raf, Class cls, FileGet obj, List<ErrorcodeEntity> errorCodeList) throws IOException {
		for(int i = 0 ; i < errorCodeList.size() ; i++) {
			byte[] memberName = Util.koEncodeByte(40, errorCodeList.get(i).getDisplay());
			
			raf.write(String.format("%-30s", errorCodeList.get(i).getKey()).getBytes());
			raf.write(memberName);
			raf.write("@                   ".getBytes());
			raf.write("         ".getBytes());
			raf.write(0x0A);
		}
	}
}
