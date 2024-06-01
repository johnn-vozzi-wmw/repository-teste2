package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.util.Date;

public class VideoProdutoGrade extends LavendereBaseDomain implements FileProperties {

	public static final String TABLE_NAME = "TBLVPVIDEOPRODUTOGRADE";

	public String cdEmpresa;
	public String dsAgrupadorGrade;
	public String nmVideo;
	public String cdVideoProdutoGrade;
	public Date dtModificacao;
	public String hrModificacao;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(dsAgrupadorGrade);
		primaryKey.append(";");
		primaryKey.append(nmVideo);
		return primaryKey.toString();
	}

	@Override
	public String getCdDomain() {
		return nmVideo;
	}

	@Override
	public String getDsDomain() {
		return dsAgrupadorGrade;
	}

	public static String getPathVideo() {
		return FotoUtil.getPathVideo(VideoProdutoGrade.class.getSimpleName());
	}

	@Override
	public String getFileName() {
		return Convert.appendPath(cdEmpresa, nmVideo);
	}

	@Override
	public String getAbsolutePath() {
		return Convert.appendPath(getPathVideo(), getFileName());
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
		json.put("cdEmpresa", cdEmpresa);
		json.put("dsAgrupadorGrade", dsAgrupadorGrade);
		json.put("fileName", nmVideo);
		return json;
	}

	@Override
	public String getHttpEndpoint() {
		return LavendereWeb2Tc.ACTION_GET_VIDEO_PRODUTO_GRADE;
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
