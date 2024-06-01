package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Pergunta;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PerguntaPdbxDao;
import totalcross.util.Vector;

public class PerguntaService extends CrudPersonLavendereService {

	private static PerguntaService instance;

	public static PerguntaService getInstance() {
		if (instance == null) {
			instance = new PerguntaService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return PerguntaPdbxDao.getInstance();
	}

	public Vector findAllPerguntasByQuestionario(Questionario questionario) throws SQLException{
		Questionario questionarioFilter = new Questionario();
		questionarioFilter.cdEmpresa = questionario.cdEmpresa;
		questionarioFilter.cdQuestionario = questionario.cdQuestionario;
		return PerguntaPdbxDao.getInstance().findAllPerguntasByQuestionario(questionarioFilter);
	}

	public Pergunta recuperaProximaPergunta(PesquisaApp pesquisaApp) throws SQLException {
		if (pesquisaApp == null) return null;
		PesquisaRespApp filter = new PesquisaRespApp();
		filter.cdEmpresa = pesquisaApp.cdEmpresa;
		filter.cdRepresentante = pesquisaApp.cdRepresentante;
		filter.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		filter.cdQuestionario = pesquisaApp.cdQuestionario;
		filter.cdCliente = pesquisaApp.cdCliente;
		PesquisaRespApp pesquisaRespAppResult = PesquisaRespAppService.getInstance().findLast(filter);
		if (pesquisaRespAppResult != null) {
			Pergunta perguntaFilter = new Pergunta();
			perguntaFilter.cdEmpresa = pesquisaRespAppResult.cdEmpresa;
			perguntaFilter.cdPergunta = pesquisaRespAppResult.cdPergunta;
			perguntaFilter = (Pergunta) findByRowKey(perguntaFilter.getRowKey());
			
			return perguntaFilter;
		}
		return null;
	}
	
	public boolean isPerguntaObrigatoria(String cdEmpresa, String cdQuestionario, String cdPergunta) throws SQLException {
		return QuestionarioPerguntaService.getInstance().isPerguntaObrigatoria(cdEmpresa, cdQuestionario, cdPergunta);
	}
	
	public boolean isExibeObservacao(String cdEmpresa, String cdQuestionario, String cdPergunta) throws SQLException {
		return QuestionarioPerguntaService.getInstance().isExibeObservacao(cdEmpresa, cdQuestionario, cdPergunta);
	}	

	public int countPerguntasObrigatorias(String cdQuestionario) throws SQLException {
		return PerguntaPdbxDao.getInstance().countPerguntasObrigatorias(cdQuestionario);
	}
	
	public Vector findPerguntaObrigatoriaNaoRespondida(Questionario questionario, PesquisaApp pesquisaApp, String[] cdPesquisaArray) throws SQLException {
		return PerguntaPdbxDao.getInstance().findUltimaPerguntaObrigatoriaNaoRespondida(questionario, pesquisaApp, cdPesquisaArray);
	}
	
	public Vector findPerguntasSecundariasByPerguntaResposta(PerguntaResposta filter) throws SQLException {
		return PerguntaPdbxDao.getInstance().findAllPerguntasSecundariasByPerguntaResposta(filter);
	}
	
	public String[] getCdPerguntaArray(Vector listPergunta) {
		if (ValueUtil.isNotEmpty(listPergunta)) {
			int size = listPergunta.size();
			String[] cdPerguntaArray = new String[size];
			for (int i = 0; i < size; i++) {
				cdPerguntaArray[i] = ((Pergunta)listPergunta.items[i]).cdPergunta;
			}
			return cdPerguntaArray;
		}
		return null;
	}

}
