package br.com.wmw.lavenderepda.util;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.json.JSONArray;
import totalcross.json.JSONObject;

public class JSONUtil {
	
	public static String percorreTags(String tag, JSONObject currentObject) {
		JSONObject jsonResponse = currentObject;
		String[] tagsJson = StringUtil.split(tag, '.');
		int tagsLength = tagsJson.length;
		int posicaoArray = -1;
		
		if (!tag.contains("[")) {
			posicaoArray = ValueUtil.getIntegerValue(tagsJson[tagsLength - 1]);
			if (ValueUtil.isValidNumber(tagsJson[tagsLength - 1])) {
				tagsLength--;
			}
		}
		
		Object current = jsonResponse;
		for (int j = 0; j < tagsLength; j++) {
			current = getCurrent(current, tagsJson[j], j == 0, posicaoArray);
		}
		
		return current.toString();
	}


	private static Object getCurrent(Object current, String tag, boolean first, int posicaoArray) {
		int index = 0;
		if (tag.contains("[")) {
			String[] tagAndIndex = StringUtil.split(tag, '[');
			tag = tagAndIndex[0];
			index = ValueUtil.getIntegerValue(tagAndIndex[1].replace("]", ""));
		}
		Object valueTag = ((JSONObject) current).get(tag);
		if (valueTag instanceof JSONArray) {
			if (first && posicaoArray > -1) {
				index = 0;
			} else if (posicaoArray >= 0) {
				index = posicaoArray;
			}
			valueTag = ((JSONArray) valueTag).get(index);
		}
		return valueTag;
	}
}
