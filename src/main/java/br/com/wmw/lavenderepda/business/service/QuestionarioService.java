package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import br.com.wmw.lavenderepda.integration.dao.pdbx.QuestionarioPdbxDao;
import totalcross.util.Vector;

public class QuestionarioService extends CrudPersonLavendereService {

	private static QuestionarioService instance;
	private List<Questionario> questionarioList;

	public static QuestionarioService getInstance() {
		if (instance == null) {
			instance = new QuestionarioService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return QuestionarioPdbxDao.getInstance();
	}

	public List<Questionario> findAllQuestionario(Questionario questionario) throws SQLException {
		Vector questionarios = QuestionarioService.getInstance().findAllByExample(questionario);
		int size = questionarios.size();
		questionarioList = new ArrayList<Questionario>();
		for (int i = 0; i < size; i++) {
			questionarioList.add((Questionario) questionarios.items[i]);
		}
		return questionarioList;
	}

}
