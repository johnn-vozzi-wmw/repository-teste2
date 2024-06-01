package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCod;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCodAtua;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoClienteCodAtuaDbxDao;

public class ProdutoClienteCodAtuaService extends CrudService {
	
	private static ProdutoClienteCodAtuaService instance;
	
	public static ProdutoClienteCodAtuaService getInstance() {
		if (instance == null) {
			instance = new ProdutoClienteCodAtuaService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return ProdutoClienteCodAtuaDbxDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}
	
	@Override
	public void insert(BaseDomain domain) throws SQLException {
		atualiza(domain, ProdutoClienteCod.FLACAO_INSERCAO);
	}
	
	@Override
	public void update(BaseDomain domain) throws SQLException {
		atualiza(domain, ProdutoClienteCod.FLACAO_ATUALIZACAO);
	}
	
	@Override
	public void delete(BaseDomain domain) throws SQLException {
		atualiza(domain, ProdutoClienteCod.FLACAO_EXCLUSAO);
	}

	private void atualiza(BaseDomain domain, String flAcao) throws SQLException {
		ProdutoClienteCodAtua produtoClienteCodAtuaNew = new ProdutoClienteCodAtua((ProdutoClienteCod) domain);
		ProdutoClienteCodAtua produtoClienteCodAtua = (ProdutoClienteCodAtua)findAllByExample(produtoClienteCodAtuaNew).items[0];
		if (produtoClienteCodAtua == null || produtoClienteCodAtua.isEnviadoServidor()) {
			produtoClienteCodAtuaNew.cdProdutoClienteAtua = generateIdGlobal();
			produtoClienteCodAtuaNew.flAcao = flAcao;
			super.insert(produtoClienteCodAtuaNew);
		} else {
			produtoClienteCodAtuaNew.cdProdutoClienteAtua = produtoClienteCodAtua.cdProdutoClienteAtua;
			produtoClienteCodAtuaNew.flAcao = flAcao;
			super.update(produtoClienteCodAtuaNew);
		}
	}
	
}
