package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.ValueUtil;

public class PesquisaRespAppFoto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISARESPAPPFOTO";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdCliente;
	public String cdPesquisaApp;
	public String cdQuestionario;
	public String cdPergunta;
	public String cdResposta;
    public String imFoto;
    public String flEnviadoServidor;
    
    //Não persistentes
    public String cdRespostaDiferente;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PesquisaRespApp) {
			PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) obj;
			return ValueUtil.valueEquals(cdEmpresa, pesquisaRespAppFoto.cdEmpresa) && ValueUtil.valueEquals(cdRepresentante, pesquisaRespAppFoto.cdRepresentante)
					&& ValueUtil.valueEquals(cdCliente, pesquisaRespAppFoto.cdCliente) && ValueUtil.valueEquals(cdPesquisaApp, pesquisaRespAppFoto.cdPesquisaApp)
					&& ValueUtil.valueEquals(cdQuestionario, pesquisaRespAppFoto.cdQuestionario) && ValueUtil.valueEquals(cdPergunta, pesquisaRespAppFoto.cdPergunta)
					&& ValueUtil.valueEquals(cdResposta, pesquisaRespAppFoto.cdResposta);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(cdCliente);
		primaryKey.append(";");
		primaryKey.append(cdPesquisaApp);
		primaryKey.append(";");
		primaryKey.append(cdQuestionario);
		primaryKey.append(";");
		primaryKey.append(cdPergunta);
		primaryKey.append(";");
		primaryKey.append(cdResposta);
		return primaryKey.toString();
	}
	
	public static String getPathImg() {
		return FotoUtil.getPathImg(PesquisaRespAppFoto.class);
	}
	
	public boolean isVisitaFotoEnviadaServidor() {
    	return ValueUtil.VALOR_SIM.equals(flEnviadoServidor);
	}
}
