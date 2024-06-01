package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemLiberacao extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPITEMLIBERACAO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdUsuarioLiberacao;
    public String nmUsuario;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemLiberacao) {
            ItemLiberacao itemLiberacao = (ItemLiberacao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemLiberacao.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, itemLiberacao.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, itemLiberacao.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, itemLiberacao.nuPedido) && 
                ValueUtil.valueEquals(cdUsuarioLiberacao, itemLiberacao.cdUsuarioLiberacao);
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
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(cdUsuarioLiberacao);
        return primaryKey.toString();
    }

}