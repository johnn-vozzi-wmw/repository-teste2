package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RestricaoProduto extends BaseDomain {

	public static String TABLE_NAME = "TBLVPRESTRICAOPRODUTO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdRestricao;
	public String cdProduto;
	public String cdGrupoProduto1;
	public String cdGrupoProduto2;
	public String cdGrupoProduto3;
	public String cdFornecedor;
	public int qtdRestricao;

	//-- Não persistente
	public double qtItemComprando;
	public double qtItemDisponivel;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdRestricao);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		primaryKey.append(";");
		primaryKey.append(cdGrupoProduto1);
		primaryKey.append(";");
		primaryKey.append(cdGrupoProduto2);
		primaryKey.append(";");
		primaryKey.append(cdGrupoProduto3);
		primaryKey.append(";");
		primaryKey.append(cdFornecedor);
		return primaryKey.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RestricaoProduto)) return false;
		RestricaoProduto that = (RestricaoProduto) o;
		return ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa)
			&& ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante)
			&& ValueUtil.valueEquals(cdRestricao, that.cdRestricao)
			&& ValueUtil.valueEquals(cdProduto, that.cdProduto)
			&& ValueUtil.valueEquals(cdGrupoProduto1, that.cdGrupoProduto1)
			&& ValueUtil.valueEquals(cdGrupoProduto2, that.cdGrupoProduto2)
			&& ValueUtil.valueEquals(cdGrupoProduto3, that.cdGrupoProduto3)
			&& ValueUtil.valueEquals(cdFornecedor, that.cdFornecedor);
	}

}
