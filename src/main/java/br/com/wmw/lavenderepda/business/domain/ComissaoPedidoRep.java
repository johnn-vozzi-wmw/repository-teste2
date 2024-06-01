package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.util.ValueUtil;

public class ComissaoPedidoRep extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPCOMISSAOPEDIDOREP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public int cdComissaoPedidoRep;
    public String dsComissaoPedidoRep;
    public double vlPctMaxComissao;
    public String cdCorFaixa;
    
    // Não persistentes
    public TemaSistema temaAtual;
    public CorSistema corSistema;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComissaoPedidoRep) {
            ComissaoPedidoRep comissaoPedidoRep = (ComissaoPedidoRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, comissaoPedidoRep.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, comissaoPedidoRep.cdRepresentante) && 
                ValueUtil.valueEquals(cdTabelaPreco, comissaoPedidoRep.cdTabelaPreco) && 
                ValueUtil.valueEquals(cdComissaoPedidoRep, comissaoPedidoRep.cdComissaoPedidoRep);
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
        primaryKey.append(cdComissaoPedidoRep);
        return primaryKey.toString();
    }

}