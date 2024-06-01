package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PerguntaRespostaPdbxDao;

public class PerguntaRespostaService extends CrudPersonLavendereService {

	private static PerguntaRespostaService instance;

	public static PerguntaRespostaService getInstance() {
		if (instance == null) {
			instance = new PerguntaRespostaService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return PerguntaRespostaPdbxDao.getInstance();
	}

	public PerguntaResposta findPerguntaRespostaByPesquisaRespApp(PesquisaRespApp pesquisaRespAppFilter) throws SQLException {
		PesquisaRespApp result = (PesquisaRespApp) PesquisaRespAppService.getInstance().findLast(pesquisaRespAppFilter);
		if (result != null) {
			PerguntaResposta perguntaRespostaFilter = new PerguntaResposta();
			perguntaRespostaFilter.cdEmpresa = result.cdEmpresa;
			perguntaRespostaFilter.cdPergunta = result.cdPergunta;
			perguntaRespostaFilter.cdResposta = result.cdResposta;
			return (PerguntaResposta) findByRowKey(perguntaRespostaFilter.getRowKey());
		}
		return null;
	}

}
