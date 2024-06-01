package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Pergunta;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaRespAppPdbxDao;
import totalcross.util.Vector;

public class PesquisaRespAppService extends CrudPersonLavendereService {

	private static PesquisaRespAppService instance;

	public static PesquisaRespAppService getInstance() {
		if (instance == null) {
			instance = new PesquisaRespAppService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return PesquisaRespAppPdbxDao.getInstance();
	}

	public PesquisaRespApp findRespostaParaInterface(PesquisaApp pesquisaApp, Questionario questionario, String cdPergunta) throws SQLException {
		PesquisaRespApp pesquisaRespAppFilter = montaFiltro(pesquisaApp, questionario, cdPergunta);
		pesquisaRespAppFilter.limit = 1;
    	Vector results = PesquisaRespAppPdbxDao.getInstance().findAllByExample(pesquisaRespAppFilter);
    	return ValueUtil.isEmpty(results) ? null : (PesquisaRespApp) results.items[0];
	}
	
	public Vector findRespostasParaInterface(PesquisaApp pesquisaApp, Questionario questionario, String cdPergunta) throws SQLException {
		PesquisaRespApp pesquisaRespAppFilter = montaFiltro(pesquisaApp, questionario, cdPergunta);
    	Vector results = PesquisaRespAppPdbxDao.getInstance().findAllByExample(pesquisaRespAppFilter);
    	return results;
	}

	private PesquisaRespApp montaFiltro(PesquisaApp pesquisaApp, Questionario questionario, String cdPergunta) {
		PesquisaRespApp pesquisaRespAppFilter = new PesquisaRespApp();
    	pesquisaRespAppFilter.cdEmpresa = questionario.cdEmpresa;
    	pesquisaRespAppFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	pesquisaRespAppFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
    	pesquisaRespAppFilter.cdQuestionario = questionario.cdQuestionario;
    	pesquisaRespAppFilter.cdPergunta = cdPergunta;
    	pesquisaRespAppFilter.cdCliente = questionario.cdCliente;
    	pesquisaRespAppFilter.sortAtributte = "DTRESPOSTA,HRRESPOSTA";
    	pesquisaRespAppFilter.sortAsc = "N,N";
		return pesquisaRespAppFilter;
	}
	
	public PesquisaRespApp findUltimaRespostaParaInterface(PesquisaApp pesquisaApp, Questionario questionario, String cdPergunta) throws SQLException {
		PesquisaRespApp pesquisaRespAppFilter = new PesquisaRespApp();
    	pesquisaRespAppFilter.cdEmpresa = questionario.cdEmpresa;
    	pesquisaRespAppFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	pesquisaRespAppFilter.cdCliente = questionario.cdCliente;
    	pesquisaRespAppFilter.cdQuestionario = questionario.cdQuestionario;
    	pesquisaRespAppFilter.cdPergunta = cdPergunta;
    	return (PesquisaRespApp) PesquisaRespAppPdbxDao.getInstance().findLastPesquisaRespApp(pesquisaRespAppFilter);
	}

	public void insertOrUpdate(PesquisaRespApp domain) throws SQLException{
		if (findByRowKey(domain.getRowKey()) == null) {
			insert(domain);
		} else {
			domain.dtResposta = DateUtil.getCurrentDate();
			domain.hrResposta = TimeUtil.getCurrentTimeHHMMSS();
			update(domain);
		}
	}

	public PesquisaRespApp findLast(PesquisaRespApp filter) throws SQLException {
		return PesquisaRespAppPdbxDao.getInstance().findLast(filter);
	}

	public void deleteAllByPesquisaApp(PesquisaApp pesquisaApp) throws SQLException{
		PesquisaRespApp pesquisaRespAppFilter = new PesquisaRespApp();
		pesquisaRespAppFilter.cdEmpresa = pesquisaApp.cdEmpresa;
		pesquisaRespAppFilter.cdCliente = pesquisaApp.cdCliente;
		pesquisaRespAppFilter.cdQuestionario = pesquisaApp.cdQuestionario;
		pesquisaRespAppFilter.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		PesquisaRespAppPdbxDao.getInstance().deleteAllByExample(pesquisaRespAppFilter);
	}
	
	public void deleteAllByPerguntaPesquisa(PesquisaApp pesquisaApp,Pergunta pergunta ) throws SQLException{
		PesquisaRespApp pesquisaRespAppFilter = new PesquisaRespApp();
		pesquisaRespAppFilter.cdEmpresa = pesquisaApp.cdEmpresa;
		pesquisaRespAppFilter.cdCliente = pesquisaApp.cdCliente;
		pesquisaRespAppFilter.cdQuestionario = pesquisaApp.cdQuestionario;
		pesquisaRespAppFilter.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		pesquisaRespAppFilter.cdPergunta = pergunta.cdPergunta;
		PesquisaRespAppPdbxDao.getInstance().deleteAllByExample(pesquisaRespAppFilter);
	}
	
	public void excluiPesquisasNovoCliente(Vector pesquisaAppList) throws SQLException {
		int size = pesquisaAppList.size();
		for (int i = 0; i < size; i++) {
			PesquisaApp pesquisaApp = (PesquisaApp)pesquisaAppList.items[i];
			PesquisaRespApp pesquisaRespAppFilter = new PesquisaRespApp();
			pesquisaRespAppFilter.cdEmpresa = pesquisaApp.cdEmpresa;
			pesquisaRespAppFilter.cdRepresentante = pesquisaApp.cdRepresentante;
			pesquisaRespAppFilter.cdCliente = pesquisaApp.cdCliente;
			pesquisaRespAppFilter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
			pesquisaRespAppFilter.cdQuestionario = pesquisaApp.cdQuestionario;
			PesquisaRespAppService.getInstance().deleteAllByExample(pesquisaRespAppFilter);
			PesquisaAppService.getInstance().delete(pesquisaApp);
		}
	}

}
