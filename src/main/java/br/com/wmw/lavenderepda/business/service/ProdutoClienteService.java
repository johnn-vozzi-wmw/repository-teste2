package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoClienteDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoTabPrecoPdbxDao;
import totalcross.util.Vector;

public class ProdutoClienteService extends CrudService {

    private static ProdutoClienteService instance;

    public static ProdutoClienteService getInstance() {
        if (instance == null) {
            instance = new ProdutoClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ProdutoClienteDbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws SQLException { /**/ }
    
    public double getVlPctFidelidade(String cdProduto, String cdCliente) throws SQLException {
    	return findProdutoClienteColumnValue(cdProduto, cdCliente, ProdutoCliente.NMCOLUNA_VLPCTFIDELIDADE);
    }

	public double getVlRetornoProduto(String cdProduto, String cdCliente) throws SQLException {
		return findProdutoClienteColumnValue(cdProduto, cdCliente, ProdutoCliente.NMCOLUNA_VLRETORNOPRODUTO);
	}

	public double getVlPctRoyalt(String cdProduto, String cdCliente) throws SQLException {
		return findProdutoClienteColumnValue(cdProduto, cdCliente, ProdutoCliente.NMCOLUNA_VLPCTROYALT);
	}

	private double findProdutoClienteColumnValue(String cdProduto, String cdCliente, String nmColuna) throws SQLException {
		ProdutoCliente produtoCliente = new ProdutoCliente();
		produtoCliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoCliente.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoCliente.class);
		produtoCliente.cdProduto = cdProduto;
		produtoCliente.cdCliente = cdCliente;
		return ValueUtil.getDoubleSimpleValue(findColumnByRowKey(produtoCliente.getRowKey(), nmColuna));
	}
	
	public boolean isPossuiRegistroExclusivo() throws SQLException {
    	ProdutoCliente filter = new ProdutoCliente();
    	filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoCliente.class);
    	filter.flTipoRelacao = ProdutoCliente.RELACAOEXCLUSIVA;
    	filter.validandoCount = true;
    	return countByExample(filter) > 0;
    }
	
	public ProdutoCliente getProdutoClienteFilter(String cdCliente) {
		ProdutoCliente produtoClienteFilter = new ProdutoCliente();
		produtoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoClienteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoCliente.class);
		produtoClienteFilter.cdCliente = cdCliente;
		produtoClienteFilter.flTipoRelacaoList = new String[] {ProdutoCliente.RELACAOEXCLUSIVA, ProdutoCliente.RELACAOEXCECAO, ProdutoCliente.RELACAORESTRICAO};
		return produtoClienteFilter;
	}
	
	public Vector findProdutoClienteListProdutoFilter(ProdutoCliente produtoClienteFilter) throws SQLException {
		return ProdutoClienteDbxDao.getInstance().findProdutoClienteListProdutoFilter(produtoClienteFilter);
	}
	
}