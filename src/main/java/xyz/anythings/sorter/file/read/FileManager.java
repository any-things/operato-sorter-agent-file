package xyz.anythings.sorter.file.read;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import xyz.anythings.sorter.model.BatchEntity;

@Service
public class FileManager {
	
	private Map<String, FileRead> clientMap = new HashMap<String, FileRead>(1);
	
	private FileRead getFileRead(String fileId) {		
		if(this.clientMap.containsKey(fileId)) {
			return this.clientMap.get(fileId);
		} else {
			return null;
		}
	}

	private String generateId(String modelName) {
		return new StringBuilder().append(modelName).toString();
	}
	
	public boolean startFileServer(BatchEntity batchEntity, String path, String modelName) {
		String fileId = this.generateId(modelName);
		FileRead fileRead = this.getFileRead(fileId);
		
		if(fileRead != null) {
			fileRead.stop();
			this.clientMap.remove(fileId);
		}
		
		fileRead = new FileRead();
		fileRead.readFile(batchEntity, path, modelName);
		this.clientMap.put(fileId, fileRead);
		
		return true;
	}

	public boolean stopFileServer(String modelName) {
		String fileId = this.generateId(modelName);
		FileRead fileRead = this.getFileRead(fileId);
		
		if(fileRead != null) {
			fileRead.stop();
			this.clientMap.remove(fileId);
		}
		
		return true;
	}
}
