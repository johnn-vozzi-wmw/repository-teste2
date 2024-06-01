package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class HistVendaListTabPreco extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPHISTVENDALISTTABPRECO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
	public int cdListaTabelaPreco;
	public int cdColunaTabelaPreco;
	public String cdGrupoProduto1;
	public double qtRealizado;
	public double vlRealizado;
	
	//Nao persistente
	public double mediaQtRealizado;
	public double mediaVlRealizado;
	public String dsListaTabelaPreco;
	public GrupoProduto1 grupoProduto1;
	public boolean media;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdCliente + ";" + cdListaTabelaPreco + ";" + cdColunaTabelaPreco + ";" + cdGrupoProduto1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HistVendaListTabPreco) {
			HistVendaListTabPreco hist = (HistVendaListTabPreco) obj;
			return ValueUtil.valueEquals(cdEmpresa, hist.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, hist.cdRepresentante) &&
			ValueUtil.valueEquals(cdCliente, hist.cdCliente) &&
			ValueUtil.valueEquals(cdListaTabelaPreco, hist.cdListaTabelaPreco) &&
			ValueUtil.valueEquals(cdColunaTabelaPreco, hist.cdColunaTabelaPreco) &&
			ValueUtil.valueEquals(cdGrupoProduto1, hist.cdGrupoProduto1);
		}
		return false;
	}

}
