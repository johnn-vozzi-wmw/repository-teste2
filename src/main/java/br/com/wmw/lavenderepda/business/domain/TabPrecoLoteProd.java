package br.com.wmw.lavenderepda.business.domain;

public class TabPrecoLoteProd extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPTABPRECOLOTEPROD";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdLoteProduto;

	public TabPrecoLoteProd() {
	
	}

	public TabPrecoLoteProd(String cdEmpresa, String cdRepresentante, String cdLoteProduto, String cdTabelaPreco) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdLoteProduto = cdLoteProduto;
		this.cdTabelaPreco = cdTabelaPreco;
	}

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
		return cdEmpresa + ";" + cdRepresentante + ";" + cdTabelaPreco + ";" + cdLoteProduto + ";";
	}

}
