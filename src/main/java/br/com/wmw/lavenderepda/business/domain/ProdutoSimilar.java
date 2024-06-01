package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.ProdutoService;

public class ProdutoSimilar extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPPRODUTOSIMILAR";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdProdutoSimilar;

    // Não persistente
    public Produto produto;
    public boolean buscouProduto;
    public double qtEstoque;
    public String cdAgrupProdSimilar;
    public String cdClienteFilter;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoSimilar) {
            ProdutoSimilar produtosimilar = (ProdutoSimilar) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtosimilar.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, produtosimilar.cdRepresentante) &&
                ValueUtil.valueEquals(cdProduto, produtosimilar.cdProduto) &&
                ValueUtil.valueEquals(cdProdutoSimilar, produtosimilar.cdProdutoSimilar);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdProdutoSimilar);
        return primaryKey.toString();
    }

    public String getCdProdutoSimilarFromRowKey() {
    	return rowKey.split(";")[3];
    }

    public String getDsProduto() throws SQLException {
    	if (getProduto() != null) {
    		return StringUtil.getStringValue(getProduto().dsProduto);
    	}
    	return cdProdutoSimilar;
    }

    public Produto getProduto() throws SQLException {
    	if (!buscouProduto && !ValueUtil.isEmpty(cdProdutoSimilar)) {
    		produto = ProdutoService.getInstance().getProduto(cdProdutoSimilar);
    		if (produto.cdEmpresa == null) {
    			produto = null;
    		}
    		buscouProduto = true;
    	}
    	return produto;
    }

	@Override
	public String getCdDomain() {
		return cdProdutoSimilar;
	}

	@Override
	public String getDsDomain() {
		try {
			return getDsProduto();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		return ValueUtil.VALOR_NI;
	}
    

}