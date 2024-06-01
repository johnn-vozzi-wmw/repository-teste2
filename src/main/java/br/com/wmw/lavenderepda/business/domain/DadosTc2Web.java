package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.io.ByteArrayStream;
import totalcross.util.Vector;

public class DadosTc2Web {

	public static final String FLTIPOALTERACAO_ORIGINAL = "";

	public ByteArrayStream cbas;
	public int nuLinhas;
	public Vector rowKeys = new Vector(1);
	public String tableName;
	public String cdRegistro;
	public Vector imageList;


	public boolean equals(Object obj) {
		if (obj instanceof DadosTc2Web) {
			DadosTc2Web dadosTc2Web = (DadosTc2Web) obj;
			return ValueUtil.valueEquals(nuLinhas, dadosTc2Web.nuLinhas) &&
					ValueUtil.valueEquals(rowKeys.items[0], dadosTc2Web.rowKeys.items[0]) &&
					ValueUtil.valueEquals(tableName, dadosTc2Web.tableName);

		}
		return false;
	}
}
