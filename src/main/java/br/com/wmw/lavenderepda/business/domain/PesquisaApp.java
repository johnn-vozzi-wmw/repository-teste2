package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import totalcross.util.Date;

public class PesquisaApp extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISAAPP";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPesquisaApp;
	public String cdQuestionario;
	public String cdCliente;
	public Date dtInicioPesquisa;
	public String hrInicioPesquisa;
	public Date dtFimPesquisa;
	public String hrFimPesquisa;
	public String flPesquisaNovoCliente;
	public String cdVisita;

	public PesquisaApp(String dsTabela) {
		super(dsTabela);
	}

	public PesquisaApp() {
		super(TABLE_NAME);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PesquisaApp) {
			PesquisaApp pesquisaApp = (PesquisaApp) obj;
			return ValueUtil.valueEquals(cdEmpresa, pesquisaApp.cdEmpresa) && ValueUtil.valueEquals(cdRepresentante, pesquisaApp.cdRepresentante)
					 && ValueUtil.valueEquals(cdPesquisaApp, pesquisaApp.cdPesquisaApp);
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
		return primaryKey.toString();
	}

	public static PesquisaApp getDomain() {
		PesquisaApp domain = new PesquisaApp();
		domain.cdRepresentante = SessionLavenderePda.getCliente().cdRepresentante;
		domain.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		domain.cdEmpresa = SessionLavenderePda.getCliente().cdEmpresa;
		return domain;
	}
	
}
