package br.com.wmw.lavenderepda.business.domain;

public class TabPrecoClasseProd extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPTABPRECOCLASSEPROD";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdTabelaPreco;
	public String cdClasse;
	public double qtMinProduto;
	public double qtMinPedido;
	
	public TabPrecoClasseProd() {
		
	}
	
	public TabPrecoClasseProd(String cdEmpresa, String cdRepresentante, String cdTabelaPreco, String cdClasse) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdTabelaPreco = cdTabelaPreco;
		this.cdClasse = cdClasse;
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
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(";");
    	strBuffer.append(cdClasse);
        return strBuffer.toString();
	}

}
