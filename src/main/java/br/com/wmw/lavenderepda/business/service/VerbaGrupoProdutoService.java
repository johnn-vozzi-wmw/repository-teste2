package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaGrupoProdutoDbxDao;

public class VerbaGrupoProdutoService extends CrudService {

    private static VerbaGrupoProdutoService instance;
    
    private VerbaGrupoProdutoService() {
        //--
    }
    
    public static VerbaGrupoProdutoService getInstance() {
        if (instance == null) {
            instance = new VerbaGrupoProdutoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return VerbaGrupoProdutoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }
    
    public double getVlPctVerba(ItemPedido itemPedido) throws SQLException {
    	VerbaGrupoProduto filter = new VerbaGrupoProduto();
    	filter.cdEmpresa = itemPedido.cdEmpresa;
    	filter.cdRepresentante = itemPedido.cdRepresentante;
    	filter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
    	filter.cdTabelaPreco = itemPedido.getCdTabelaPreco();
    	VerbaGrupoProduto verbaGrupoProduto = (VerbaGrupoProduto)findByPrimaryKey(filter);
    	return verbaGrupoProduto != null ? verbaGrupoProduto.vlPctVerbaPositiva : 0d;
    }

}