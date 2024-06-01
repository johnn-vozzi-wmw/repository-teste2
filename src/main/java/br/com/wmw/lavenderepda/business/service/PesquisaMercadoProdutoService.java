package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaMercadoProdutoDbxDao;
import totalcross.util.Vector;

public class PesquisaMercadoProdutoService extends CrudService {

	private static PesquisaMercadoProdutoService instance;

	private PesquisaMercadoProdutoService() {
	}

	public static PesquisaMercadoProdutoService getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoProdutoService();
		}
		return instance;
	}


	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return PesquisaMercadoProdutoDbxDao.getInstance();
	}

	public Vector findProdutosAdicionados(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		return PesquisaMercadoProdutoDbxDao.getInstance().findProdutosAdicionados(pesquisaMercadoConfig);
	}

	public PesquisaMercadoProduto findByRegRowKey(String rowkey) throws SQLException {
		return PesquisaMercadoProdutoDbxDao.getInstance().findByRegRowKey(rowkey);
	}

	public boolean itemIsAdicionado(PesquisaMercadoProduto pesquisaMercadoProduto) {
		return ValueUtil.isNotEmpty(pesquisaMercadoProduto.rowKey) && pesquisaMercadoProduto.rowKey.split(";").length > 3;
	}

	public void deleteRegistrosAntigos(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		PesquisaMercadoProduto pesquisaMercadoProduto = new PesquisaMercadoProduto();
		pesquisaMercadoProduto.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoProduto.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		deleteAllByExample(pesquisaMercadoProduto);
	}
}
