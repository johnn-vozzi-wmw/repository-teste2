package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class MargemContribFaixa extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPMARGEMCONTRIBFAIXA";
	
	private static final String FL_ACAO_VERBA_CONSOME = "C";
	private static final String FL_ACAO_VERBA_GERA = "G";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdFaixa;
    public String dsFaixa;
    public double vlPctMargemInicio;
    public double vlPctMargemFim;
    public String flBloqueiaPedido;
    public String flAcaoVerba;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MargemContribFaixa) {
            MargemContribFaixa margemContribFaixa = (MargemContribFaixa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, margemContribFaixa.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, margemContribFaixa.cdRepresentante) && 
                ValueUtil.valueEquals(cdFaixa, margemContribFaixa.cdFaixa);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdFaixa);
        return primaryKey.toString();
    }

	public boolean isBloqueiaPedido() {
		return ValueUtil.VALOR_SIM.equals(flBloqueiaPedido);
	}

	public boolean isConsomeVerba() {
		return FL_ACAO_VERBA_CONSOME.equals(flAcaoVerba);
	}

	public boolean isGeraVerba() {
		return FL_ACAO_VERBA_GERA.equals(flAcaoVerba);
	}
	
	public String getDsAcaoVerba() {
		if (isGeraVerba()) return Messages.MARGEMCONTRIBUICAO_ACAO_GERA;
		else if (isConsomeVerba()) return Messages.MARGEMCONTRIBUICAO_ACAO_CONSOME;
		else return Messages.MARGEMCONTRIBUICAO_ACAO_NEUTRA;
	}

}