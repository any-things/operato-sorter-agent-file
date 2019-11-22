package xyz.anythings.sorter.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.PrinterName;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import xyz.anythings.sorter.file.model.FileGet;
import xyz.anythings.sorter.model.ErrorinfoEntity;
import xyz.anythings.sorter.util.Constants;
import xyz.anythings.sorter.util.Util;
import xyz.elidom.util.FormatUtil;
import xyz.elidom.util.HttpUtil;

public class ErrorinfoData {
String uri =  "" /*Constants.getWasIp()*/ + "/data/errorinfo";
	
	public List<ErrorinfoEntity> errorInfo(FileGet fileGet, String batchId) throws Exception {
		String invoiceNo = (fileGet.getSomething("resultCode1").toString() + fileGet.getSomething("resultCode2").toString()).trim();
		List<ErrorinfoEntity> errorInfoList = new ArrayList<ErrorinfoEntity>();
		
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("batchId", batchId);
		params.put("skuBarCode", fileGet.getSomething("majorCode1").toString().trim());
		params.put("invoiceNo", invoiceNo);
		params.put("errorCode", fileGet.getSomething("resultCode3").toString().trim());
		params.put("inputPcNo", fileGet.getSomething("inNo").toString().trim());
		
		list.add(params);
		
		String jsonString = FormatUtil.toJsonString(list);
		String value = HttpUtil.executePostMethodForJson(uri, jsonString);
		JSONArray data = Util.jsonArrayParser(value);
		
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				JSONObject jsonObj = (JSONObject) data.get(i);
				ErrorinfoEntity errorInfo = (ErrorinfoEntity) FormatUtil.jsonToObject(jsonObj.toString(),
						ErrorinfoEntity.class);
				
				errorInfoList.add(errorInfo);
			}
		}
		return errorInfoList;
	}
	
	public String printTest() {
		String msg = Constants.RESULT_FAIL;
		
		String command = "^XA"
				+ "^CI51"
				+ "^FO30,25^GB438,287,1^FS"
				+ "^FO31,264^GB437,0,1^FS"
				+ "^FO31,216^GB437,0,1^FS"
				+ "^FO31,168^GB436,0,1^FS"
				+ "^FO31,120^GB436,0,1^FS"
				+ "^FO31,71^GB436,0,1^FS"
				+ "^FO30,279^A6,31,31,^FD사유코드^FS"
				+ "^FO30,230^A6,31,31,^FD상     품^FS"
				+ "^FO31,183^A6,25,25,^FD운송장번호^FS"
				+ "^FO31,135^A6,31,31,^FD거래처명^FS"
				+ "^FO30,87^A6,25,25,^FD거래처코드^FS"
				+ "^FO148,279^A6,31,31,^FD!@#{errorCode}^FS"
				+ "^FO146,230^A6,31,31,^FD!@#{styleCode}!@#{colorCode}!@#{sizeCode}^FS"
				+ "^FO148,135^A6,27,27,^FD!@#{customerName}^FS"
				+ "^FO147,87^A6,31,31,^FD!@#{customerCode}^FS"
				+ "^FO147,40^A6,31,31,^FD!@#{returnDate}^FS"
				+ "^FO31,40^A6,31,31,^FDDO 일자^FS"
				+ "^FO141,25^GB0,286,1^FS"
				+ "^FO154,183^A6,28,28,^FD!@#{invoiceNo}^FS"
				+ "^XZ";
		
		msg = this.sendToPrinterCommand("\\\\Shortstop\\BIXOLON SLP-DX420 - BPL-Z", command);
		
		return msg;
	}
	
	public String setUpErrorLabelPrint(List<ErrorinfoEntity> errorInfoList) {
		String msg = Constants.RESULT_FAIL;
		String errorLabelDownloadUri = "" /*Constants.getWasIp()*/ + "/data/errorlabel";
        
        for(int i = 0 ; i < errorInfoList.size() ; i++) {
        	InputStream is = null;
            String command = "";
            
            try {
    			URL url = new URL(errorLabelDownloadUri);
    			URLConnection urlConnection = url.openConnection();
    			is = urlConnection.getInputStream();
    			
    			byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = is.read(buffer)) != -1) {
                    command += new String(Util.getbytes(buffer, 0, readBytes), Constants.ENCODE_TYPE);
                }
    		} catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            command = mappingCommand(command, errorInfoList.get(i));
//    		msg = this.sendToPrinterCommand("\\\\Minu\\BIXOLON SLP-DX420", command);
    		msg = this.sendToPrinterCommand(errorInfoList.get(i).getPrinterName(), command);
        }
        
		return msg;
	}
	
	public String mappingCommand(String command, ErrorinfoEntity errorinfoEntity) {
		String content = "";
		
		content = command.replace("!@#{returnDate}", errorinfoEntity.getReturnDate() == null ? "" : errorinfoEntity.getReturnDate());
		content = content.replace("!@#{customerCode}", errorinfoEntity.getCustomerCode() == null ? "" : errorinfoEntity.getCustomerCode());
		content = content.replace("!@#{customerName}", errorinfoEntity.getCustomerName() == null ? "" : errorinfoEntity.getCustomerName());
		content = content.replace("!@#{invoiceNo}", errorinfoEntity.getInvoiceNo() == null ? "" : errorinfoEntity.getInvoiceNo());
		content = content.replace("!@#{styleCode}", errorinfoEntity.getStyleCode() == null ? "" : errorinfoEntity.getStyleCode());
		content = content.replace("!@#{colorCode}", errorinfoEntity.getColorCode() == null ? "" : errorinfoEntity.getColorCode());
		content = content.replace("!@#{sizeCode}", errorinfoEntity.getSizeCode() == null ? "" : errorinfoEntity.getSizeCode());
		content = content.replace("!@#{errorCode}", errorinfoEntity.getErrorCode() == null ? "" : errorinfoEntity.getErrorCode());
		
		return content;
	}
	
	public static void refreshSystemPrinterList() {
	    Class<?>[] classes = PrintServiceLookup.class.getDeclaredClasses();
	    for (int i = 0; i < classes.length; i++) {
	        if ("javax.print.PrintServiceLookup$Services".equals(classes[i].getName())) {
//	            sun.awt.AppContext.getAppContext().remove(classes[i]);
	            break;
	        }
	    }
	}
	
	public String sendToPrinterCommand(String printerName, String command) {
		String msg = Constants.RESULT_FAIL;
		refreshSystemPrinterList();
		try {
			PrintService pService = null;
			String sPrinterName = null;
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

			for (int i = 0; i < services.length; i++) {

				PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
				sPrinterName = ((PrinterName) attr).getValue().toUpperCase();

//				System.out.println("sPrinterName********************" + sPrinterName);
				
				if (sPrinterName.compareTo(printerName.toUpperCase()) == 0) {
					pService = services[i];
//					System.out.println("%%%%%%%%%%%%%%%%%%%%%%" + sPrinterName);
					break;
				}
			}

			if (pService != null) {
				byte[] by = command.getBytes(Constants.ENCODE_TYPE);// 투입구 용
				DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

				DocPrintJob job = pService.createPrintJob();
				Doc nic = new SimpleDoc(by, flavor, null);
				job.print(nic, null);

				msg = Constants.RESULT_OK;
			} else {
				System.out.println("printer is not found.");
			}
		} catch (PrintException e) {
			System.out.println(e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println(e);
		} catch (RuntimeException e) {
		} catch (Throwable e) {
			System.out.println("Throwable" + e.getCause());
		}
		return msg;
	}
}