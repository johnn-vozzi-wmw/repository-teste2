package br.com.wmw.lavenderepda.report.pdf.tagmap;

import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.regex.Matcher;
import totalcross.util.regex.Pattern;

public class Elementos extends GenericTag {
	
	public static final String ATTR_SQL = "sql";
	
	public String sqlPs;
	public List<GenericTag> genericTagList = new ArrayList<GenericTag>();
	public List<String> sqlParamsNamesList = new ArrayList<String>();
	

	public void preparaSql(String sql) {
		if (ValueUtil.isEmpty(sql)) {
			return;
		}
		Pattern p = Pattern.compile("(\\{#(.+?\\b)\\})"); 
		Matcher m = p.matcher(sql); 
		while (m.find()) { 
			String conteudo = m.group(1); 
			sqlParamsNamesList.add(conteudo.substring(2, conteudo.length() - 1));
			sql = sql.replace(conteudo, " ? ");
		}
		sqlPs = new String(sql);
	}
	
}
