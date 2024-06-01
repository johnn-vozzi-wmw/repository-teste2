package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class LoteProduto extends BaseDomain {

	public static final String SORT_COLUMN_CD_LOTE_PRODUTO = "cdLoteproduto";
	public static final String SORT_COLUMN_DT_VALIDADE = "dtValidade";
	public static final String SORT_COLUMN_QTD_ESTOQUE = "qtEstoque";
	public static final String SORT_COLUMN_QTD_ESTOQUE_RESERVADO = "qtEstoqueReservado";
	public static final String SORT_COLUMN_VL_PCVIDAUTILPRODUTO = "vlPctvidaUtilProduto";
	public static final String SORT_COLUMN_VL_PCTDESCONTO = "vlPctDesconto";
	public static final String SORT_COLUMN_VL_BASEITEMSTRING = "vlBaseItemString";

	public static String TABLE_NAME = "TBLVPLOTEPRODUTO";
	public static String FLORIGEMSALDOERP = "E";
	public static String FLORIGEMSALDOPDA = "P";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdProduto;
	public String cdLoteproduto;
	public double qtEstoquedisponivel;
	public double qtEstoquereservado;
	public Date dtValidade;
	public double vlPctvidautilproduto;
	public double qtEstoque;
	public String flOrigemEstoque;
	public String cdLocal;
	// --
	private Local local;
	public boolean ignoraLocal;

	// Não Persiste
	public String vlPctDesconto;
	public String vlBaseItemString;
	public int qtTabPrecoLoteProd;
	public String cdTabelaPrecoFilter;
	public boolean agrupaCdLoteProduto;
	public static String sortColumn;
	// Override
	public boolean equals(Object obj) {
		if (obj instanceof LoteProduto) {
			LoteProduto loteproduto = (LoteProduto) obj;
			return ValueUtil.valueEquals(cdEmpresa, loteproduto.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, loteproduto.cdRepresentante)
					&& ValueUtil.valueEquals(cdProduto, loteproduto.cdProduto)
					&& ValueUtil.valueEquals(cdLoteproduto, loteproduto.cdLoteproduto)
					&& ValueUtil.valueEquals(flOrigemEstoque, loteproduto.flOrigemEstoque);
		}
		return false;
	}

	public LoteProduto() {
	}

	public LoteProduto(String cdEmpresa, String cdRepresentante, String cdProduto, String cdLoteProduto) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdProduto = cdProduto;
		this.cdLoteproduto = cdLoteProduto;
	}

	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(cdEmpresa);
		strBuffer.append(";");
		strBuffer.append(cdRepresentante);
		strBuffer.append(";");
		strBuffer.append(cdProduto);
		strBuffer.append(";");
		strBuffer.append(cdLoteproduto);
		strBuffer.append(";");
		strBuffer.append(flOrigemEstoque);
		return strBuffer.toString();
	}

	public Local getLocal() {
		if (local == null) {
			local = new Local();
		}
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	@Override
	public String getSortStringValue() {
		if (SORT_COLUMN_CD_LOTE_PRODUTO.toUpperCase().equals(sortColumn)) {
			return cdLoteproduto;
		} else if (SORT_COLUMN_VL_PCTDESCONTO.toUpperCase().equals(sortColumn)) {
			return vlPctDesconto;
		} else if (SORT_COLUMN_VL_BASEITEMSTRING.toUpperCase().equals(sortColumn)) {
			return vlBaseItemString;
		}
		return super.getSortStringValue();
	}

	@Override
	public double getSortDoubleValue() {
		if (SORT_COLUMN_QTD_ESTOQUE.toUpperCase().equals(sortColumn)) {
			return qtEstoque;
		} else if (SORT_COLUMN_QTD_ESTOQUE_RESERVADO.equals(sortColumn)) {
			return qtEstoquereservado;
		} else if (SORT_COLUMN_VL_PCVIDAUTILPRODUTO.equals(sortColumn)) {
			return vlPctvidautilproduto;
		}
		return super.getSortDoubleValue();
	}

	@Override
	public int getSortIntValue() {
		if (SORT_COLUMN_DT_VALIDADE.equals(sortColumn)) {
			return dtValidade != null ? dtValidade.getDateInt() : 0;
		}
		return super.getSortIntValue();
	}
}