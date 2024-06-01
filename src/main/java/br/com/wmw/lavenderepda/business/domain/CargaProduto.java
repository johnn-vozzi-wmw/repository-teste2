package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;

public class CargaProduto extends BaseDomain {

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdCargaProduto;
    public double qtSolicitado;
    public Date dtCadastro;
    public String hrCadastro;
    public Date dtAlteracao;
    public String hrAlteracao;
	//--
    private Produto produto;
	public String cdStatusFilter;
	public String dsProdutoFilter;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CargaProduto) {
            CargaProduto cargaProduto = (CargaProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cargaProduto.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, cargaProduto.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, cargaProduto.cdProduto) && 
                ValueUtil.valueEquals(cdCargaProduto, cargaProduto.cdCargaProduto);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdCargaProduto);
        return primaryKey.toString();
    }

    public Produto getProduto() {
    	if (produto != null) {
    		return produto;
    	}
    	return produto = new Produto();
    }

	public String getStatus() {
		return isEnviadoServidor() ? Messages.CARGAPRODUTO_STATUS_ENVIADO : Messages.CARGAPRODUTO_STATUS_NAO_ENVIADO;
	}
}