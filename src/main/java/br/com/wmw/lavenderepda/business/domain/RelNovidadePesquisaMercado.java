package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoConfigService;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import totalcross.util.Date;

public class RelNovidadePesquisaMercado extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPRELNOVPESQUISAMERC";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPesquisaMercadoConfig;
	public String cdTipoNovidade;
	public String dsNovidadePesquisa;
	public Date dtEmissaoRelatorio;

	//não persistentes
	public int qtRegistrosTipoNovidade;
	public String dsFiltro;

	@Override
	public String getPrimaryKey() {
		StringBuilder pk = new StringBuilder();
		pk.append(cdEmpresa).append(";");
		pk.append(cdRepresentante).append(";");
		pk.append(cdPesquisaMercadoConfig).append(";");
		pk.append(cdTipoNovidade).append(";");
		return pk.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelNovidadePesquisaMercado) {
			RelNovidadePesquisaMercado other = (RelNovidadePesquisaMercado) obj;
			return ValueUtil.valueEquals(cdEmpresa, other.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, other.cdRepresentante)
					&& ValueUtil.valueEquals(cdPesquisaMercadoConfig, other.cdPesquisaMercadoConfig)
					&& ValueUtil.valueEquals(cdTipoNovidade, other.cdTipoNovidade);
		} else {
			return false;
		}
	}

	@Override
	public String getSortStringValue() {
		try {
			if (RelNovidadeProd.SORT_COLUMN_DSPRODUTO.equals(RelNovidadeProd.sortAttr)) {
				return PesquisaMercadoConfigService.getInstance().findDsByCd(this);
			}
			if (RelNovidadeProd.SORT_COLUMN_DSNOVIDADEPRODUTO.equals(RelNovidadeProd.sortAttr)) {
				return TipoNovidadeService.getInstance().getDsTipoNovidade(cdTipoNovidade);
			}
		} catch (SQLException ex) {
			return dsNovidadePesquisa;
		}
		return super.getSortStringValue();
	}

	@Override
	public int getSortIntValue() {
		if (RelNovidadeProd.SORT_COLUMN_CDPRODUTO.equals(RelNovidadeProd.sortAttr)) {
			return ValueUtil.getIntegerValue(cdPesquisaMercadoConfig);
		}
		if (RelNovidadeProd.SORT_COLUMN_DTEMISSAORELATORIO.equals(RelNovidadeProd.sortAttr) && ValueUtil.isNotEmpty(dtEmissaoRelatorio)) {
			return dtEmissaoRelatorio.getDateInt();
		}
		return super.getSortIntValue();
	}

}
