package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Date;

public class PesquisaMercadoConfig extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISAMERCADOCONFIG";
	public static final String APPOBJ_CAMPOS_FILTRO_PESQUISA_MERCADO = "Pesquisa de Mercado";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPesquisaMercadoConfig;
	public String dsPesquisaMercado;
	public Date dtInicialVigencia;
	public Date dtFimVigencia;

	//não persistentes
	public int qtItensPesquisa;
	public int qtConcorrentesPesquisa;
	public String dsProdutoFiltro;

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdPesquisaMercadoConfig;
	}

	@Override
	public String getDescription() {
		return dsPesquisaMercado + MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_CODIGO_LABEL, cdPesquisaMercadoConfig);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PesquisaMercadoConfig) {
			PesquisaMercadoConfig other = (PesquisaMercadoConfig) obj;
			return ValueUtil.valueEquals(cdEmpresa, other.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, other.cdRepresentante)
					&& ValueUtil.valueEquals(cdPesquisaMercadoConfig, other.cdPesquisaMercadoConfig);
		} else {
			return false;
		}
	}

	public boolean isExpirada() {
		return DateUtil.isAfter(dtFimVigencia);
	}
}
