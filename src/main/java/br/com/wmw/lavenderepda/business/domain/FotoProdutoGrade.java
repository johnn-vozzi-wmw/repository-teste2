package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.util.Date;

public class FotoProdutoGrade extends BaseDomain implements FileProperties {

	public static final String TABLE_NAME = "TBLVPFOTOPRODUTOGRADE";
	public static final String NM_COLUNA_NMFOTO = "nmFoto";

	public String dsAgrupadorGrade;
	public String nmFoto;
	public String cdFotoProdutoGrade;
	public int nuTamanho;
	public Date dtModificacao;
	public String hrModificacao;
	public int nuCarimbo;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(dsAgrupadorGrade);
		primaryKey.append(";");
		primaryKey.append(nmFoto);
		return primaryKey.toString();
	}

	public boolean equals(Object obj) {
		if (obj instanceof FotoProdutoGrade) {
			FotoProdutoGrade fotoProdutoGrade = (FotoProdutoGrade) obj;
			return
					ValueUtil.valueEquals(dsAgrupadorGrade, fotoProdutoGrade.dsAgrupadorGrade) &&
							ValueUtil.valueEquals(nmFoto, fotoProdutoGrade.nmFoto);
		}
		return false;
	}
	
	public static String getPathImg() {
		return FotoUtil.getPathImg(FotoProdutoGrade.class);
	}

	@Override
	public String getFileName() {
		return nmFoto;
	}

	@Override
	public String getAbsolutePath() {
		return Convert.appendPath(getPathImg(), getFileName());
	}

	@Override
	public String getHrModificacao() {
		return hrModificacao;
	}

	@Override
	public Date getDtModificacao() {
		return dtModificacao;
	}

	@Override
	public JSONObject getRequestJson() {
		JSONObject json = new JSONObject();
		json.put("dsAgrupadorGrade", dsAgrupadorGrade);
		json.put("fileName", nmFoto);
		return json;
	}

	@Override
	public String getHttpEndpoint() {
		return LavendereWeb2Tc.ACTION_GET_FOTO_PRODUTO_GRADE;
	}

	@Override
	public String getNmCampoUpdateRecebimento() {
		return BaseDomain.NMCAMPOROWKEY;
	}

	@Override
	public String getVlCampoUpdateRecebimento() {
		return rowKey;
	}

}
