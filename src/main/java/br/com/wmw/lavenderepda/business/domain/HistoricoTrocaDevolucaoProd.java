package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;

public class HistoricoTrocaDevolucaoProd extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPHISTTROCADEVOLUCAOPROD";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdProduto;
	public String cdCliente;
	public String dsProduto;
	public double percTrocaDevSeisMeses;
	public double percTrocaDevTresMeses;
	public double percTrocaDevTrintaDias;
	public String qtVendaSeisMeses;
	public String qtVendaTresMeses;
	public String qtVendaTrintaDias;
	public int diasSemCompras;

	//Não persistente
	public boolean possuiHistorico;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdProduto);
		primaryKey.append(";");
		primaryKey.append(cdCliente);
		return primaryKey.toString();
	}
}
