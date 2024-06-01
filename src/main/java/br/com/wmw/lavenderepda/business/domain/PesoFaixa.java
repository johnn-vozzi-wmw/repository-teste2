package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.util.ValueUtil;

public class PesoFaixa extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPESOFAIXA";

    public String cdEmpresa;
    public double qtPeso;
    public String dsFaixa;
    public String flFaixaIdeal;
    public String cdCorFaixa;
    public String flTipoAlteracao;
    
    // Não persistentes
    public TemaSistema temaAtual;
    public CorSistema corSistema;

    public PesoFaixa() { super(); }

    public PesoFaixa(String cdEmpresa, String flFaixaIdeal) {
        super();
        this.cdEmpresa = cdEmpresa;
        this.flFaixaIdeal = flFaixaIdeal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PesoFaixa) {
            PesoFaixa comissaoPedidoRep = (PesoFaixa) obj;
            return ValueUtil.valueEquals(cdEmpresa, comissaoPedidoRep.cdEmpresa)
                && ValueUtil.valueEquals(qtPeso, comissaoPedidoRep.qtPeso);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(qtPeso);
        return primaryKey.toString();
    }

    public boolean isFaixaIdeal() {
        return ValueUtil.getBooleanValue(flFaixaIdeal);
    }

}