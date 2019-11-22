package xyz.anythings.sorter.file.context;

import java.lang.reflect.Field;

import xyz.anythings.sorter.file.annotation.Marker;
import xyz.anythings.sorter.util.Util;

public class MyContextContainer {
	public MyContextContainer() {}
	
	private <T> T invokeAnnonations(T instance, byte[] bytes) throws Exception {
		Field [] fields = instance.getClass().getDeclaredFields();
		for( Field field : fields ){
        	Marker annotation = field.getAnnotation(Marker.class);
            if( annotation != null ){
                field.setAccessible(true);
                int offset = annotation.offset();
                int length = annotation.length();
                
                if(field.getType() == String.class) {
                	byte[] subArr = Util.getbytes(bytes, offset, length);
                	String value = new String(subArr);
                    field.set(instance, value);
                } else if (field.getType() == int.class) {
                	byte[] subArr = Util.getbytes(bytes, offset, length);
                	int value = Util.readInt(subArr, 0);
                    field.set(instance, value);
                } else if (field.getType() == Short.class) {
                	byte[] subArr = Util.getbytes(bytes, offset, length);
                	short value = Util.readShort(subArr, 0);
                    field.set(instance, value);
                }
            }
        }
        return instance;
	}
	
	public <T> T get(Class clazz, byte[] bytes) throws Exception {
		T instance = (T) clazz.newInstance();
        instance = invokeAnnonations(instance, bytes);
        return instance;
	}
}
