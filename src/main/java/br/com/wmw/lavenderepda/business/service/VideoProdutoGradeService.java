package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.lavenderepda.business.domain.VideoProdutoGrade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VideoProdutoGradeDbxDao;
import totalcross.sys.Convert;
import totalcross.util.Vector;

public class VideoProdutoGradeService extends CrudService {

	private static VideoProdutoGradeService instance;

	public static VideoProdutoGradeService getInstance() {
		if (instance == null) {
			instance = new VideoProdutoGradeService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { /*nao valida no app*/ }

	@Override
	protected CrudDao getCrudDao() {
		return VideoProdutoGradeDbxDao.getInstance();
	}
	
	public void createDirsFotoProdutoEmp() throws SQLException {
		Vector cdEmpresaList = VideoProdutoGradeDbxDao.getInstance().findCdEmpresaList();
		int size = cdEmpresaList.size();
		String produtoPath = VideoProdutoGrade.getPathVideo();
		for (int i = 0; i < size; i++) {
			FileUtil.createDirIfNecessaryQuietly(Convert.appendPath(produtoPath, cdEmpresaList.items[i] + ""));
		}
	}


}
