package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;


public class TabelaDb extends BaseDomain {
	
	public static final String NMCOLUNA_NMTABELA = "nmTabela";
	public static final String NMCOLUNA_NUREGISTROS = "nuRegistros";
	public static final String NMCOLUNA_NUMAIORCARIMBO = "nuMaiorCarimbo";

	public String nmTabela;
	public int nuRegistros;
	public int nuMaiorCarimbo;

	// Ordenação
	public static String sortAttr;

	public TabelaDb() {
	}

	public TabelaDb(String nmTabela) {
		this.nmTabela = nmTabela;
	}

	public int getSortIntValue() {
		return "nuMaiorCarimbo".equals(sortAttr) ? nuMaiorCarimbo : nuRegistros;
	}

	public String toString() {
		return nmTabela;
	}

	@Override
	public String getPrimaryKey() {
		return nmTabela;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TabelaDb) {
			TabelaDb tabelaDb = (TabelaDb) obj;
			return ValueUtil.valueEquals(nmTabela, tabelaDb.nmTabela);
		}
		return false;
	}

}
