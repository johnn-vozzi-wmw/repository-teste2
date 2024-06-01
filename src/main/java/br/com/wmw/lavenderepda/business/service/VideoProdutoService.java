package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.lavenderepda.business.domain.VideoProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VideoProdutoDbxDao;
import totalcross.sys.Convert;
import totalcross.util.Vector;

public class VideoProdutoService extends CrudService {

	private static VideoProdutoService instance;

	public static VideoProdutoService getInstance() {
		if (instance == null) {
			instance = new VideoProdutoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { /*nao valida no app*/ }

	@Override
	protected CrudDao getCrudDao() {
		return VideoProdutoDbxDao.getInstance();
	}
	
	public void createDirsFotoProdutoEmp() throws SQLException {
		Vector cdEmpresaList = VideoProdutoDbxDao.getInstance().findCdEmpresaList();
		int size = cdEmpresaList.size();
		String produtoPath = VideoProduto.getPathVideo();
		for (int i = 0; i < size; i++) {
			FileUtil.createDirIfNecessaryQuietly(Convert.appendPath(produtoPath, cdEmpresaList.items[i] + ""));
		}
	}

}
