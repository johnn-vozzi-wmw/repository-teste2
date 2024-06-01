package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Combo extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPCOMBO";

	public static final String SUGERE_APOS_INSERCAO = "1";
	public static final String SUGERE_AO_FECHAR_PEDIDO = "2";
	public static final String OPCAO_MENU_INFERIOR = "3";
	public static final String NOMECOLUNA_CDCOMBO = "CDCOMBO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCombo;
	public String cdTabelaPreco;
	public String dsCombo;
	public Date dtVigenciaInicial;
	public Date dtVigenciaFinal;
	public String flReplicaParcial;
	public String nuPedido;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Combo) {
			Combo combo = (Combo) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, combo.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, combo.cdRepresentante) &&
			ValueUtil.valueEquals(cdCombo, combo.cdCombo) &&
			ValueUtil.valueEquals(cdTabelaPreco, combo.cdTabelaPreco);
		}
		return false;
	}
	
	@Override
	public String getCdDomain() {
		return cdCombo;
	}

	@Override
	public String getDsDomain() {
		return dsCombo;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdCombo + ";" + cdTabelaPreco;
	}

}
