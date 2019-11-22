package xyz.anythings.sorter.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xyz.anythings.sorter.file.context.MyContextContainer;
import xyz.anythings.sorter.file.model.Execbat;
import xyz.anythings.sorter.file.model.FileGet;
import xyz.elidom.sys.entity.ErrorLog;
import xyz.elidom.util.BeanUtil;
import xyz.elidom.util.ValueUtil;

public class Util {
	public static float readFloat(byte[] bin, int offset) throws Exception {
		int l = 0;
		if (bin.length >= offset + 4) {
			l = ((0xff & bin[offset + 0]) << 24) | ((0xff & bin[offset + 1]) << 16) | ((0xff & bin[offset + 2]) << 8)
					| ((0xff & bin[offset + 3]));
		} else
			throw new Exception("ByteFormat : Invalid Length");

		return Float.intBitsToFloat(l);
	}

	public static int readInt(byte[] bin, int offset) throws Exception {
		int l = 0;
		if (bin.length >= offset + 4) {
			l = ((0xff & bin[offset + 0]) << 24) | ((0xff & bin[offset + 1]) << 16) | ((0xff & bin[offset + 2]) << 8)
					| ((0xff & bin[offset + 3]));
		} else
			throw new Exception("ByteFormat : Invalid Length");
		return l;
	}

	public static short readShort(byte[] bin, int offset) throws Exception {
		short l = 0;
		if (bin.length >= offset + 2) {
			l = (short) (((0xff & bin[offset + 0]) << 8) | ((0xff & bin[offset + 1])));
		} else
			throw new Exception("ByteFormat : Invalid Length");
		return l;
	}

	public static byte[] writeFloat(int offset, float val) throws Exception {
		byte[] bout = new byte[4];

		int value = Float.floatToRawIntBits(val);

		bout[offset + 3] = (byte) ((value & 0xff000000) >> 24);
		bout[offset + 2] = (byte) ((value & 0x00ff0000) >> 16);
		bout[offset + 1] = (byte) ((value & 0x0000ff00) >> 8);
		bout[offset + 0] = (byte) ((value & 0x000000ff));

		return bout;
	}

	public static byte[] writeInt(int offset, int value) throws Exception {
		byte[] bout = new byte[4];

		bout[offset + 3] = (byte) ((value & 0xff000000) >> 24);
		bout[offset + 2] = (byte) ((value & 0x00ff0000) >> 16);
		bout[offset + 1] = (byte) ((value & 0x0000ff00) >> 8);
		bout[offset + 0] = (byte) ((value & 0x000000ff));

		return bout;
	}

	public static byte[] writeShort(int offset, short value) throws Exception {
		byte[] bout = new byte[2];
		if (bout.length < offset + 2)
			throw new Exception("ByteFormat : Invalid Length");

		bout[offset + 1] = (byte) ((value & 0xff00) >> 8);
		bout[offset + 0] = (byte) ((value & 0x00ff));

		return bout;
	}

	public static byte[] getbytes(byte src[], int offset, int length) {
		byte dest[] = new byte[length];
		System.arraycopy(src, offset, dest, 0, length);
		return dest;
	}

	public static char[] getChars(char src[], int offset, int length) {
		char dest[] = new char[length];
		System.arraycopy(src, offset, dest, 0, length);
		return dest;
	}

	public static Object byteParsing(String fileName, byte[] bytes) throws Exception {
		MyContextContainer demo = new MyContextContainer();
		Execbat obj = demo.get(Execbat.class, bytes);

		return obj;
	}

	public static FileGet searchRecord(String batchId, RandomAccessFile randomAccessFile, MyContextContainer mContext,
			Class cls, FileGet obj) throws InstantiationException, IllegalAccessException, Exception {
		byte[] buffer = new byte[obj.getRecordSize()];
		randomAccessFile.seek(0);

		while (randomAccessFile.read(buffer) != -1) {
			obj = mContext.get(cls.newInstance().getClass(), buffer);

//			if (obj.getSomething("batchNo").equals(batchId)) {
			if (obj.getSomething("checkFlag").equals(batchId)) {
				break;
			}
		}
		return obj;
	}

	public static void deleteRecord(String batchId, RandomAccessFile randomAccessFile, MyContextContainer mContext,
			Class cls, FileGet obj) throws InstantiationException, IllegalAccessException, Exception {
		randomAccessFile.seek(0);
		byte[] buffer = new byte[obj.getRecordSize()];
		byte[] bytes = new byte[0];
		int read = -1;
		while ((read = randomAccessFile.read(buffer)) != -1) {
			obj = mContext.get(cls.newInstance().getClass(), buffer);
//			if (obj.getSomething("batchNo").equals(batchId)) {
			if (obj.getSomething("checkFlag").equals(batchId)) {
				int changePointer = (int) (randomAccessFile.getFilePointer() - obj.getRecordSize()) + obj.getOffset("checkFlag");
				randomAccessFile.seek(changePointer);
				randomAccessFile.write(String.format("%010d", 12345).getBytes());
				
//				String asdf = new String();
//				randomAccessFile.write(asdf.getBytes());
//				randomAccessFile.read(buffer);
				break;
			}
		}
	}
	
	public static void makeFile(String rootPath, String makeFileName) throws IOException {
		File f1 = new File(rootPath);

		if (!f1.isDirectory()) {
			f1.mkdirs();
		}
		f1 = new File(rootPath + "/" + makeFileName);
		f1.createNewFile();
	}
	
	public static JSONArray jsonArrayParser(String value) throws ParseException {
		JSONArray jsonArray = new JSONArray();
		JSONParser parser = new JSONParser();
		
		Object obj = parser.parse(value);
		jsonArray = (JSONArray) obj;

		return jsonArray;
	}
	
	public static String DateToday(String dateTimeFormat) {
		Date date = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat(dateTimeFormat);
		String today = sdformat.format(date);
		
		return today;
	}
	
	public static String getBatchStatus(String status) {
		String convertStatus = "0";
		
		if(status.equals(Constants.STATUS_CREATED)) {
			convertStatus = "0";
		} else if(status.equals(Constants.STATUS_READY)) {
			convertStatus = "0";
		} else if(status.equals(Constants.STATUS_IN_WORK)) {
			convertStatus = "2";
		} else if(status.equals(Constants.STATUS_STOPPED)) {
			convertStatus = "1";
		} else if(status.equals(Constants.STATUS_SOME_FINISHED)) {
			convertStatus = "3";
		} else if(status.equals(Constants.STATUS_ALL_FINISHED)) {
			convertStatus = "4";
		}
		
		return convertStatus;
	}
	
	public static byte[] koEncodeByte(int length, String koName) throws UnsupportedEncodingException {
		byte[] memberName = new byte[length];
		int skuNameBytes = koName.getBytes(Constants.ENCODE_TYPE).length > length ? length : koName.getBytes(Constants.ENCODE_TYPE).length;
		for(int j = 0 ; j < skuNameBytes ; j++) {
			memberName[j] = koName.getBytes(Constants.ENCODE_TYPE)[j];
		}
		if(skuNameBytes < length) {
			for(int k = skuNameBytes ; k < memberName.length ; k++) {
				memberName[k] = 0x20;
			}
		}
		return memberName;
	}
	
	public static byte stringToHex(String value) {
		int hex = Integer.parseInt(value, 16);
		
		return (byte)hex;
	}
	
	// hex to byte[]
	public static byte[] hexToByteArray(String hex) {
	    if (hex == null || hex.length() == 0) {
	        return null;
	    }
	 
	    byte[] ba = new byte[hex.length() / 2];
	    for (int i = 0; i < ba.length; i++) {
	        ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
	    }
	    return ba;
	}
	 
	// byte[] to hex
	public static String byteArrayToHex(byte[] ba) {
	    if (ba == null || ba.length == 0) {
	        return null;
	    }
	 
	    StringBuffer sb = new StringBuffer(ba.length * 2);
	    String hexNumber;
	    for (int x = 0; x < ba.length; x++) {
	        hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
	 
	        sb.append(hexNumber.substring(hexNumber.length() - 2));
	    }
	    return sb.toString();
	}
}
