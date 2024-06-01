package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.QuestionarioPerguntaDbxDao;

public class QuestionarioPerguntaService extends CrudService {

	private static QuestionarioPerguntaService instance;

	private QuestionarioPerguntaService() {}

	public static QuestionarioPerguntaService getInstance() {
		if (instance == null) {
			instance = new QuestionarioPerguntaService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return QuestionarioPerguntaDbxDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) {}

	public boolean isPerguntaObrigatoria(String cdEmpresa, String cdQuestionario, String cdPergunta) throws SQLException {
		return QuestionarioPerguntaDbxDao.getInstance().isPerguntaObrigatoria(cdEmpresa, cdQuestionario, cdPergunta);
	}
	
	public boolean isExibeObservacao(String cdEmpresa, String cdQuestionario, String cdPergunta) throws SQLException {
		return QuestionarioPerguntaDbxDao.getInstance().isExibeObservacao(cdEmpresa, cdQuestionario, cdPergunta);
	}
}