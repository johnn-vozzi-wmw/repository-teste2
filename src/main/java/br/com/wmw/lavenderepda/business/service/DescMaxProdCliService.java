package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.DescMaxProdCli;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescMaxProdCliDbxDao;

public class DescMaxProdCliService extends CrudService {

    private static DescMaxProdCliService instance;
    
    private DescMaxProdCliService() {
        //--
    }
    
    public static DescMaxProdCliService getInstance() {
        if (instance == null) {
            instance = new DescMaxProdCliService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return DescMaxProdCliDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {}
    
    public void loadDescMaxProdCli(ItemPedido itemPedido) throws SQLException {
    	DescMaxProdCli filter = new DescMaxProdCli();
    	filter.cdEmpresa = itemPedido.cdEmpresa;
    	filter.cdRepresentante = itemPedido.cdRepresentante;
    	filter.cdCliente = itemPedido.pedido.cdCliente;
    	filter.cdGrupoDescMaxProd = itemPedido.getProduto().cdGrupoDescMaxProd;
    	itemPedido.descMaxProdCli = (DescMaxProdCli)findByPrimaryKey(filter);
    }

}