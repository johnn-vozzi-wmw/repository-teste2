package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoPagtoTabPreco extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPTIPOPAGTOTABPRECO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdTipoPagamento;
	
	public TipoPagtoTabPreco() {}
	
	public TipoPagtoTabPreco(String cdEmpresa, String cdRepresentante, String cdTabelaPreco) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdTabelaPreco = cdTabelaPreco;
	}
	

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoPagamento) {
        	TipoPagtoTabPreco tipoPagtoTabPreco = (TipoPagtoTabPreco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoPagtoTabPreco.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tipoPagtoTabPreco.cdRepresentante) &&
                ValueUtil.valueEquals(cdTabelaPreco, tipoPagtoTabPreco.cdTabelaPreco) &&
                ValueUtil.valueEquals(cdTipoPagamento, tipoPagtoTabPreco.cdTipoPagamento);
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
		primaryKey.append(cdTabelaPreco);
		primaryKey.append(";");
		primaryKey.append(cdTipoPagamento);
		return primaryKey.toString();
	}

}
