package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ProdutoCondPagto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoCondPagtoDbxDao;
import totalcross.util.Vector;

public class ProdutoCondPagtoService extends CrudService {
	
	private static ProdutoCondPagtoService instance;

	public static ProdutoCondPagtoService getInstance() {
		if (instance == null) {
			instance = new ProdutoCondPagtoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { /**/ }

	@Override
	protected CrudDao getCrudDao() {
		return new ProdutoCondPagtoDbxDao();
	}

	public ProdutoCondPagto getProdutoCondPagtoFilter(String cdCondicaoPagamento) {
		ProdutoCondPagto produtoCondPagtoFilter = new ProdutoCondPagto();
		produtoCondPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoCondPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoCondPagto.class);
		produtoCondPagtoFilter.cdCondicaoPagamento = cdCondicaoPagamento;
		produtoCondPagtoFilter.flTipoRelacaoList = new String[] {ProdutoCondPagto.RELACAOEXCLUSIVA, ProdutoCondPagto.RELACAOEXCECAO, ProdutoCondPagto.RELACAORESTRICAO};
		return produtoCondPagtoFilter;
	}
	
	public boolean isPossuiRegistroExclusivo() throws SQLException {
		ProdutoCondPagto filter = new ProdutoCondPagto();
    	filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoCondPagto.class);
    	filter.flTipoRelacao = ProdutoCondPagto.RELACAOEXCLUSIVA;
    	filter.validandoCount = true;
    	return countByExample(filter) > 0;
    }
	
	public Vector findProdutoCondPagtoListProdutoFilter(ProdutoCondPagto produtoCondPagtoFilter) throws SQLException {
		return ProdutoCondPagtoDbxDao.getInstance().findProdutoCondPagtoListProdutoFilter(produtoCondPagtoFilter);
	}
}
