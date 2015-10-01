package lib.utils;

public class StringUtils {

	public static String join(String separator, String [] chunks) {
		String s = "";
		for(int i = 0;i<chunks.length;i++){
			if(i != 0)
				s += separator;
			
			s += chunks[i];
		}
		return s;
	}
	
	public static String join(String separator, Object [] chunks, StringConvertor convertor) {
		String s = "";
		for(int i = 0;i<chunks.length;i++){
			if(i != 0)
				s += separator;
			
			s += convertor.getChunk(chunks[i]);
		}
		return s;
	}
	
	public interface StringConvertor {
		public String getChunk(Object o);
	}
	
	public static String[] split(String separator, String text) {
		int count = 0;
		int lastIndex = 0;
		while((lastIndex = text.indexOf(separator, lastIndex)) != -1)
			count++;
		
		String[] chunks = new String[count];
		int fromIndex = 0;
		int toIndex = 0;
		int chunk = 0;
		while((toIndex = text.indexOf(separator, fromIndex)) != -1){
			chunks[chunk] = text.substring(fromIndex, toIndex);
			fromIndex = toIndex+1;
			chunk++;
		}
		return chunks;
	}
	
}
