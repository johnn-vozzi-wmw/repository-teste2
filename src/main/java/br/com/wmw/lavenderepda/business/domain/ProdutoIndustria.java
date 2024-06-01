package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ProdutoIndustria extends LavendereBasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPRODUTOINDUSTRIA";
	public static final String TABLE_NAME_WEB = "TBLVWPRODUTOINDUSTRIA";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdProduto;
	public String cdProdIndustria;
	public String cdRefProdIndustria;
	public String cdSugestaoVenda;
	public String dsProdIndustria;
	public String dsRefProdIndustria;
	public double vlLitroSugestao;

	//-- Não Persistente
	public String[] cdSugestaoVendaIn;

	public ProdutoIndustria() {
		this(TABLE_NAME);
	}
	

	public ProdutoIndustria(String tableName) {
		super(tableName);
	}

	public ProdutoIndustria(final String cdEmpresa, final String cdRepresentante, final String cdSugestaoVenda) {
		this(TABLE_NAME);
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdSugestaoVenda = cdSugestaoVenda;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ProdutoIndustria)) return false;
		ProdutoIndustria produtoIndustria = (ProdutoIndustria) obj;
		return ValueUtil.valueEquals(cdEmpresa, produtoIndustria.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, produtoIndustria.cdRepresentante) &&
			   ValueUtil.valueEquals(cdProduto, produtoIndustria.cdProduto) &&
			   ValueUtil.valueEquals(cdProdIndustria, produtoIndustria.cdProdIndustria) &&
			   ValueUtil.valueEquals(cdRefProdIndustria, produtoIndustria.cdRefProdIndustria) &&
			   ValueUtil.valueEquals(cdSugestaoVenda, produtoIndustria.cdSugestaoVenda);
	}

	public String getCdDomain() {
		return cdRefProdIndustria;
	}

	public String getDsDomain() {
		return dsRefProdIndustria;
	}

	public String getPrimaryKey() {
		StringBuilder strBuffer = new StringBuilder();
		strBuffer.append(cdEmpresa);
		strBuffer.append(";");
		strBuffer.append(cdRepresentante);
		strBuffer.append(";");
		strBuffer.append(cdProduto);
		strBuffer.append(";");
		strBuffer.append(cdProdIndustria);
		strBuffer.append(";");
		strBuffer.append(cdRefProdIndustria);
		strBuffer.append(";");
		strBuffer.append(cdSugestaoVenda);
		return strBuffer.toString();
	}

}