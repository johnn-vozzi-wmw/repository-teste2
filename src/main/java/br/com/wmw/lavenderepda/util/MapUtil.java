package br.com.wmw.lavenderepda.util;

import java.util.HashMap;

import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class MapUtil {

	public static HashMap<String, String> convertHashtableToHashMap(final Hashtable hashtableTotalcross) {
		int size = hashtableTotalcross.size();
		HashMap<String, String> newHashMap = new HashMap<>(size);
		Vector keys = hashtableTotalcross.getKeys();
		
		for (int n = 0; n < size; n++) {
			String key = (String) keys.items[n];
			newHashMap.put(key, (String) hashtableTotalcross.get(key));
		}
		return newHashMap;
	}
}
