package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PesquisaRespApp extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISARESPAPP";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPesquisaApp;
	public String cdQuestionario;
	public String cdPergunta;
	public String cdResposta;
	public String cdCliente;
	public Date dtResposta;
	public String hrResposta;
	public String dsResposta;
	public String dsObservacao;

	public PesquisaRespApp(String dsTabela) {
		super(dsTabela);
	}

	public PesquisaRespApp() {
		super(TABLE_NAME);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PesquisaRespApp) {
			PesquisaRespApp pesquisaRespApp = (PesquisaRespApp) obj;
			return ValueUtil.valueEquals(cdEmpresa, pesquisaRespApp.cdEmpresa) && ValueUtil.valueEquals(cdRepresentante, pesquisaRespApp.cdRepresentante)
					&& ValueUtil.valueEquals(cdPesquisaApp, pesquisaRespApp.cdPesquisaApp) && ValueUtil.valueEquals(cdQuestionario, pesquisaRespApp.cdQuestionario) 
					&& ValueUtil.valueEquals(cdPergunta, pesquisaRespApp.cdPergunta) && ValueUtil.valueEquals(cdResposta, pesquisaRespApp.cdResposta);
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
		primaryKey.append(cdPesquisaApp);
		primaryKey.append(";");
		primaryKey.append(cdQuestionario);
		primaryKey.append(";");
		primaryKey.append(cdPergunta);
		primaryKey.append(";");
		primaryKey.append(cdResposta);
		return primaryKey.toString();
	}
	
}
