package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.MarcadorProduto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MarcadorProdutoDbxDao;
import totalcross.util.Vector;

public class MarcadorProdutoService extends CrudService {

    private static MarcadorProdutoService instance;
    
    private MarcadorProdutoService() {}

	@Override public void validate(BaseDomain domain) {}
	@Override protected CrudDao getCrudDao() { return MarcadorProdutoDbxDao.getInstance(); }
    public static MarcadorProdutoService getInstance() { return instance == null ? instance = new MarcadorProdutoService() : instance; }

    public Vector findAllByProdutoAndCliente(ProdutoBase produto, Cliente cliente, boolean showFlAgrupadorGrade) throws SQLException{
    	final MarcadorProduto filter = createMarcadorProdutoFilter(produto, cliente != null ? cliente.cdCliente : null);
    	
    	if (produto.cdMarcadores == null) {
    		produto.cdMarcadores = new Vector();
    	} else {
    		produto.cdMarcadores.removeAllElements();
    	}
    	
    	return MarcadorProdutoDbxDao.getInstance().findAllByProdutoFilter(filter, showFlAgrupadorGrade, produto.cdMarcadores);
    }
    
    public Vector findMarcadoresByProduto(ProdutoBase produto, String cdCliente) throws SQLException {
    	final MarcadorProduto filter = createMarcadorProdutoFilter(produto, cdCliente);
    	return MarcadorProdutoDbxDao.getInstance().findMarcadoresByProduto(filter);
    }
    
    private static MarcadorProduto createMarcadorProdutoFilter(ProdutoBase produto, String cdCliente) {
    	MarcadorProduto filter = new MarcadorProduto();
    	filter.cdEmpresa = produto.cdEmpresa;
    	filter.cdRepresentante = produto.cdRepresentante;
    	filter.cdProduto = produto.cdProduto;
    	filter.cdCliente = cdCliente;
    	return filter;
    }

}