package br.com.wmw.lavenderepda.business.domain;

public class TabPrecoGrupoProd extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPTABPRECOGRUPOPROD";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdGrupoProduto1;
	public double qtMinProduto;
	public double qtMinPedido;
	public double qtMinGrade1;
	public double qtMinGrade2;

	@Override
	public String getCdDomain() {
		return null;
	}

	@Override
	public String getDsDomain() {
		return null;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdTabelaPreco + ";" + cdGrupoProduto1 + ";";
	}

}
