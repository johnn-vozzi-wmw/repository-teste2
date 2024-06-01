package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescontoGrupo extends BaseDomain {

	public static String TABLE_NAME = "TBLVPDESCONTOGRUPO";

	public static String SIGLE_EXCEPTION = "DQG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdGrupoProduto1;
    public String cdGrupoDescProd;
    public String cdTabelaPreco;
    public double qtItem;
    public double vlPctDesconto;

    //Não persistente
    public double vlDesconto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescontoGrupo) {
            DescontoGrupo descontogrupo = (DescontoGrupo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descontogrupo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, descontogrupo.cdRepresentante) &&
                ValueUtil.valueEquals(cdGrupoProduto1, descontogrupo.cdGrupoProduto1) &&
                ValueUtil.valueEquals(cdGrupoDescProd, descontogrupo.cdGrupoDescProd) &&
                ValueUtil.valueEquals(cdTabelaPreco, descontogrupo.cdTabelaPreco) &&
                ValueUtil.valueEquals(qtItem, descontogrupo.qtItem);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto1);
        primaryKey.append(";");
        primaryKey.append(cdGrupoDescProd);
        primaryKey.append(";");
        primaryKey.append(cdTabelaPreco);
        primaryKey.append(";");
        primaryKey.append(qtItem);
        return primaryKey.toString();
    }

}