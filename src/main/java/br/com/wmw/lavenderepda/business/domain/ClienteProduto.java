package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ClienteProduto extends ProdutoTipoRelacaoBase {

	public static String TABLE_NAME = "TBLVPCLIENTEPRODUTO";
	public static final String NMCOLUNA_CDCLIENTE = "CDCLIENTE";
	
	public String cdCliente;
	
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteProduto) {
            ClienteProduto clienteProduto = (ClienteProduto) obj;
            return ValueUtil.valueEquals(cdEmpresa, clienteProduto.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, clienteProduto.cdRepresentante)
					&& ValueUtil.valueEquals(cdCliente, clienteProduto.cdCliente)
					&& ValueUtil.valueEquals(cdProduto, clienteProduto.cdProduto)
					&& ValueUtil.valueEquals(flTipoRelacao, clienteProduto.flTipoRelacao);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(flTipoRelacao);
        return primaryKey.toString();
    }
    
    @Override
    public String getCdDomainEntidade() {
    	return this.cdCliente;
    }

    @Override
    public String getCdDomainEntidadeNomeColuna() {
    	return NMCOLUNA_CDCLIENTE;
    }
}