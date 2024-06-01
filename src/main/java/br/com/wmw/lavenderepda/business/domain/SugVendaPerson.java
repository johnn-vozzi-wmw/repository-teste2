package br.com.wmw.lavenderepda.business.domain;

import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.ui.image.Image;
import totalcross.util.Date;

public class SugVendaPerson extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPSUGVENDAPERSON";
	
	public static final int CAMPO_VLT = 1;
	public static final int CAMPO_VEP = 2;
	public static final int CAMPO_QE = 3;
	public static final int CAMPO_VLHIS = 4;
	public static final int CAMPO_QTDHIS = 5;
	public static final int CAMPO_VUE = 6;
	
	public static final String ORDEMCDPRODUTO = "1";
	public static final String ORDEMDSPRODUTO = "2";
	public static final String ORDEMQTESTOQUE = "3";
	public static final String ORDEMVLHIST = "4";
	public static final String ORDEMQTDHIST = "5";
	public static final String ORDEMASC = "A";
	public static final String ORDEMDESC = "D";

	public String cdSugVendaPerson;
	public String dsSugVendaPerson;
	public String dsSugVendaPersonAbrev;
	public Date dtVigenciaInicial;
	public Date dtVigenciaFinal;
	public int nuRelevancia;
	public byte[] imSugVendaPerson;
	public String dsComandoCliente;
	public String dsComandoProduto;
	
	//--Nao persistentes
	public List<String> cdSugsFilter;
	public Image image;
	public boolean unchecked;
	
	@Override
	public String getPrimaryKey() {
		return cdSugVendaPerson;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SugVendaPerson) {
			SugVendaPerson sugVendaPerson = (SugVendaPerson)obj;
			return ValueUtil.valueEquals(cdSugVendaPerson, sugVendaPerson.cdSugVendaPerson);
		}
		return false;
	}
	
	public static String getParamSortAttr() {
		String ordemCol = LavenderePdaConfig.dsOrdenacaoListaSugPerson;
		if (ordemCol.equals(ORDEMCDPRODUTO)) {
			return ProdutoBase.SORT_COLUMN_CDPRODUTO;
		} else if (ordemCol.equals(ORDEMDSPRODUTO)) {
			return ProdutoBase.SORT_COLUMN_DSPRODUTO;
		} else if (ordemCol.equals(ORDEMQTESTOQUE) && LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			return ProdutoBase.SORT_COLUMN_ESTOQUE;
		} else if (ordemCol.equals(ORDEMVLHIST) && LavenderePdaConfig.dsInformacoesComplementaresListaSugestaoVendaPerson.indexOf(String.valueOf(SugVendaPerson.CAMPO_VLHIS)) != -1) {
			return ProdutoBase.SORT_COLUMN_VLMEDIOHIST;
		} else if (ordemCol.equals(ORDEMQTDHIST) && LavenderePdaConfig.dsInformacoesComplementaresListaSugestaoVendaPerson.indexOf(String.valueOf(SugVendaPerson.CAMPO_QTDHIS)) != -1) {
			return ProdutoBase.SORT_COLUMN_QTDMEDHIST;
		} else {
			return ProdutoBase.SORT_COLUMN_CDPRODUTO;
		}
	}

}
