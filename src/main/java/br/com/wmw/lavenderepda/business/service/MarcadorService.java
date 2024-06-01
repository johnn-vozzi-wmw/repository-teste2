package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.MarcadorCliente;
import br.com.wmw.lavenderepda.business.domain.MarcadorPedido;
import br.com.wmw.lavenderepda.business.domain.MarcadorProduto;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MarcadorDbxDao;
import totalcross.util.Vector;

public class MarcadorService extends CrudService {

    private static MarcadorService instance;
    
    private MarcadorService() {
        //--
    }
    
    public static MarcadorService getInstance() {
        if (instance == null) {
            instance = new MarcadorService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return MarcadorDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {

    }
 
    public Vector buscaMarcadoresPorCliente(Cliente cliente) throws SQLException {
    	MarcadorCliente marcadorCliente = new MarcadorCliente(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCliente);
    	return MarcadorDbxDao.getInstance().buscaMarcadoresPorCliente(marcadorCliente);
    }
    
    public Vector buscaMarcadoresDePedido(Pedido pedido) throws SQLException {
    	MarcadorPedido marcadorPedido = new MarcadorPedido(pedido.cdEmpresa, pedido.cdRepresentante, pedido.flOrigemPedido, pedido.nuPedido);
    	return MarcadorDbxDao.getInstance().buscaMarcadoresDePedido(marcadorPedido);
    }
    
    public Vector buscaMarcadoresVigentes(String entidadeMarcador) throws SQLException {
    	return MarcadorDbxDao.getInstance().buscaMarcadoresVigentes(entidadeMarcador);
    }
    
    public Vector buscaMarcadoresVigentesDePedidos(String cdRepresentante) throws SQLException {
    	return MarcadorDbxDao.getInstance().buscaMarcadoresVigentesDePedidos(cdRepresentante);
    }
    
    public HashMap<String, Marcador> buscaMarcadoresVigentesHash(String nmEntidade) throws SQLException {
    	return MarcadorDbxDao.getInstance().buscaMarcadoresVigentesHash(nmEntidade);
    }

    public Vector findMarcadoresByProduto(ProdutoBase produto) throws SQLException {
    	MarcadorProduto filter = new MarcadorProduto();
    	filter.cdEmpresa = produto.cdEmpresa;
    	filter.cdRepresentante = produto.cdRepresentante;
    	filter.cdProduto = produto.cdProduto;
    	return MarcadorDbxDao.getInstance().findMarcadoresByProduto(filter);
    }

}