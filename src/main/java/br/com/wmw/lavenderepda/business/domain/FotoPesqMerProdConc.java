package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;

public class FotoPesqMerProdConc extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPFOTOPESQMERPRODCONC";
	public static final String DEFAULT_CDPRODUTO = "0";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPesquisaMercadoConfig;
	public String nmFoto;
	public String cdProduto;
	public String cdCliente;
	public int cdFoto;
	public String flEnviadoServidor;

	public static String getImagePath() {
		return FotoUtil.getPathImg(FotoPesqMerProdConc.class);
	}

	@Override
	public String getPrimaryKey() {
		StringBuilder pk = new StringBuilder();
		pk.append(cdEmpresa).append(";");
		pk.append(cdRepresentante).append(";");
		pk.append(cdPesquisaMercadoConfig).append(";");
		pk.append(nmFoto).append(";");
		pk.append(cdProduto).append(";");
		pk.append(cdCliente).append(";");
		return pk.toString();
	}

	public boolean isFotoPesquisaMercadoEnviadaServidor() {
		return ValueUtil.getBooleanValue(flEnviadoServidor);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FotoPesqMerProdConc) {
			FotoPesqMerProdConc other = (FotoPesqMerProdConc) obj;
			return ValueUtil.valueEquals(cdEmpresa, other.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, other.cdRepresentante)
					&& ValueUtil.valueEquals(cdPesquisaMercadoConfig, other.cdPesquisaMercadoConfig)
					&& ValueUtil.valueEquals(nmFoto, other.nmFoto)
					&& ValueUtil.valueEquals(cdProduto, other.cdProduto)
					&& ValueUtil.valueEquals(cdCliente, other.cdCliente);
		} else {
			return false;
		}
	}

}
