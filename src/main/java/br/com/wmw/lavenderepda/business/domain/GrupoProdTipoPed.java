package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class GrupoProdTipoPed extends BaseDomain {

	public static String TABLE_NAME = "TBLVPGRUPOPRODTIPOPED";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public String cdTipoPedido;
    
    //Nao Persistentes
    public boolean excecaoGrupoProdutoFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoProdTipoPed) {
            GrupoProdTipoPed grupoProd1TipoPed = (GrupoProdTipoPed) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, grupoProd1TipoPed.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, grupoProd1TipoPed.cdRepresentante) &&
                ValueUtil.valueEquals(cdGrupoProduto1, grupoProd1TipoPed.cdGrupoProduto1) &&
                ValueUtil.valueEquals(cdGrupoProduto2, grupoProd1TipoPed.cdGrupoProduto2) &&
                ValueUtil.valueEquals(cdGrupoProduto3, grupoProd1TipoPed.cdGrupoProduto3) &&
                ValueUtil.valueEquals(cdTipoPedido, grupoProd1TipoPed.cdTipoPedido);
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
        primaryKey.append(cdGrupoProduto1);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto2);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto3);
        primaryKey.append(";");
        primaryKey.append(cdTipoPedido);
        return primaryKey.toString();
    }

}