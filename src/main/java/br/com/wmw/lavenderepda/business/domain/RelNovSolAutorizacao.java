package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import totalcross.util.Date;

public class RelNovSolAutorizacao extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPRELNOVSOLAUTORIZACAO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdSolAutorizacao;
	public int cdTipoSolAutorizacao;
	public String flOrigemPedido;
	public String cdTipoNovidade;
	public String nuPedido;
	public String cdCliente;
	public String cdProduto;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public String flAutorizado;
	public Date dtEmissaoRelatorio;

	//Não persistente
	public int qtRegistrosTipoNovidade;
	public String dsFiltro;
	public String dsProduto;

	public RelNovSolAutorizacao() {
		super(TABLE_NAME);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RelNovSolAutorizacao)) return false;
		RelNovSolAutorizacao that = (RelNovSolAutorizacao) o;
		return cdTipoSolAutorizacao == that.cdTipoSolAutorizacao &&
				nuSeqProduto == that.nuSeqProduto &&
				ValueUtil.valueEquals(cdEmpresa, that.cdEmpresa) &&
				ValueUtil.valueEquals(cdRepresentante, that.cdRepresentante) &&
				ValueUtil.valueEquals(cdSolAutorizacao, that.cdSolAutorizacao) &&
				ValueUtil.valueEquals(cdTipoNovidade, that.cdTipoNovidade);
	}

	@Override
	public String getPrimaryKey() {
		StringBuilder pk = new StringBuilder();
		pk.append(cdEmpresa).append(";");
		pk.append(cdRepresentante).append(";");
		pk.append(cdSolAutorizacao).append(";");
		pk.append(cdTipoNovidade).append(";");
		return pk.toString();
	}

	@Override
	public String getSortStringValue() {
		try {
			if (RelNovidadeProd.SORT_COLUMN_DSPRODUTO.equals(RelNovidadeProd.sortAttr)) {
				return cdProduto;
			}
			if (RelNovidadeProd.SORT_COLUMN_DSNOVIDADEPRODUTO.equals(RelNovidadeProd.sortAttr)) {
				return TipoNovidadeService.getInstance().getDsTipoNovidade(cdTipoNovidade);
			}
		} catch (SQLException ex) {
			return cdProduto;
		}
		return super.getSortStringValue();
	}

	@Override
	public int getSortIntValue() {
		if (RelNovidadeProd.SORT_COLUMN_CDPRODUTO.equals(RelNovidadeProd.sortAttr)) {
			return ValueUtil.getIntegerValue(nuPedido);
		}
		if (RelNovidadeProd.SORT_COLUMN_DTEMISSAORELATORIO.equals(RelNovidadeProd.sortAttr) && ValueUtil.isNotEmpty(dtEmissaoRelatorio)) {
			return dtEmissaoRelatorio.getDateInt();
		}
		return super.getSortIntValue();
	}

}


