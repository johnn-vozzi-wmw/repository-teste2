package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.util.Date;

public class CatalogoExterno extends LavendereBaseDomain implements FileProperties {

	public static String TABLE_NAME = "TBLVPCATALOGOEXTERNO";

	public String cdCatalogoExterno;
	public String cdEmpresa;
	public String nmArquivo;
	public String dsCatalogoExterno;
	public int nuTamanho;
	public String nmExtensao;
	public Date dtAlteracao;
	public String hrAlteracao;
	public int nuCarimbo;

	//Nao persistentes
	public boolean filtraFlTipoAlteracaoNotNull;

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdCatalogoExterno);
		primaryKey.append(";");
		primaryKey.append(cdEmpresa);
		return primaryKey.toString();
	}

	public boolean equals(Object obj) {
		if (obj instanceof CatalogoExterno) {
			CatalogoExterno catalogoExterno = (CatalogoExterno) obj;
			return
					ValueUtil.valueEquals(dsCatalogoExterno, catalogoExterno.dsCatalogoExterno) &&
							ValueUtil.valueEquals(nmArquivo, catalogoExterno.nmArquivo);
		}
		return false;
	}
	
	public static String getPathCatalogoExterno() {
		String filePath = BaseDomain.getDirBaseArquivosInCard() + "CatalogoExterno/";
		if (VmUtil.isAndroid()) {
			return "/sdcard/" + filePath;
		} else {
			return Convert.appendPath(Settings.appPath, filePath);
		}
	}

	@Override
	public String getCdDomain() {
		return cdCatalogoExterno;
	}

	@Override
	public String getDsDomain() {
		return dsCatalogoExterno;
	}

	@Override
	public String getFileName() {
		return nmArquivo;
	}

	@Override
	public String getAbsolutePath() {
		return Convert.appendPath(getPathCatalogoExterno(), getFileName());
	}

	@Override
	public String getHrModificacao() {
		return hrAlteracao;
	}

	@Override
	public Date getDtModificacao() {
		return dtAlteracao;
	}

	@Override
	public JSONObject getRequestJson() {
		JSONObject json = new JSONObject();
		json.put("cdCatalogoExterno", cdCatalogoExterno);
		json.put("cdEmpresa", cdEmpresa);
		json.put("fileName", nmArquivo);
		return json;
	}

	@Override
	public String getHttpEndpoint() {
		return LavendereWeb2Tc.ACTION_GET_CATALOGO_EXTERNO;
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
