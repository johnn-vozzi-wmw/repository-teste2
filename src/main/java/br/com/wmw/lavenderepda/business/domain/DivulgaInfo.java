package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.interfaces.FileProperties;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.ui.image.Image;
import totalcross.util.Date;

public class DivulgaInfo extends LavendereBasePersonDomain implements FileProperties {
	
	public static final String TABLE_NAME = "TBLVPDIVULGAINFO";
	public static final String TABLE_NAME_WEB = "TBLVWDIVULGAINFO";
	
	public static final String CAMPO_NMIMAGEM = "nmimagem";
	public static final String TIPO_DIVULGA_TEXTO = "T";
	public static final String TIPO_DIVULGA_IMAGEM = "I";
	public static final String TIPO_DIVULGA_LINK = "L";
	
	public static final String DIR_CAMINHO_IMAGEM = "";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
	public String cdDivulgaInfo;
	public String dsDivulgaInfo;
	public String flTipoDivulgaInfo;
	public String dsTipoDivulgaInfoTxt;
	public String nmImagem;
	public Date dtInicial;
	public Date dtFinal;
	public String nmUrl;
	public String nmUrlImagemLink;

	// Não Persistente
	public boolean visualizado;
	public String[] cdClienteInFilter;
	public Image imageUrlLink;
	
	public DivulgaInfo() {
		this(TABLE_NAME);
	}

	public DivulgaInfo(String tableName) {
		super(tableName);
	}
	
	public DivulgaInfo(final String cdEmpresa, final String cdRepresentante, final String cdCliente) {
		super(TABLE_NAME);
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdCliente = cdCliente;
	}
	
	public static String getPathImg() {
		return FotoUtil.getPathImg(DivulgaInfo.class);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DivulgaInfo)) return false;
		DivulgaInfo descontoPacote = (DivulgaInfo) obj;
		return ValueUtil.valueEquals(cdEmpresa, descontoPacote.cdEmpresa) &&
			   ValueUtil.valueEquals(cdRepresentante, descontoPacote.cdRepresentante) &&
			   ValueUtil.valueEquals(cdDivulgaInfo, descontoPacote.cdDivulgaInfo) &&
			   ValueUtil.valueEquals(cdCliente, descontoPacote.cdCliente);
	}

	public String getCdDomain() {
		return cdDivulgaInfo;
	}

	public String getDsDomain() {
		return getCdDomain();
	}

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdDivulgaInfo);
        return strBuffer.toString();
    }
    
	public boolean isTipoImagem() {
		return ValueUtil.valueEquals(TIPO_DIVULGA_IMAGEM, flTipoDivulgaInfo);
	}
	
	public boolean isTipoTexto() {
		return ValueUtil.valueEquals(TIPO_DIVULGA_TEXTO, flTipoDivulgaInfo);
	}

	public boolean isTipoLink() {
		return ValueUtil.valueEquals(TIPO_DIVULGA_LINK, flTipoDivulgaInfo);
	}

	@Override
	public String getFileName() {
		return nmImagem;
	}

	@Override
	public String getAbsolutePath() {
		return Convert.appendPath(DivulgaInfo.getPathImg(), nmImagem);
	}

	@Override
	public String getHrModificacao() {
		return null;
	}

	@Override
	public Date getDtModificacao() {
		return null;
	}

	@Override
	public JSONObject getRequestJson() {
		JSONObject json = new JSONObject();
		json.put("cdEmpresa", cdEmpresa);
		json.put("cdRepresentante", cdRepresentante);
		json.put("cdCliente", cdCliente);
		json.put("cdDivulgaInfo", cdDivulgaInfo);
		json.put("nmFoto", nmImagem);
		return json;
	}

	@Override
	public String getHttpEndpoint() {
		return LavendereWeb2Tc.ACTION_GET_FOTO_DIVULGACAO;
	}

	@Override
	public String getNmCampoUpdateRecebimento() {
		return CAMPO_NMIMAGEM;
	}

	@Override
	public String getVlCampoUpdateRecebimento() {
		return nmImagem;
	}
	
}
