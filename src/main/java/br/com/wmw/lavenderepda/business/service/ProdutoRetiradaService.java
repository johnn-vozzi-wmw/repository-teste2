package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ProdutoRetirada;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoRetiradaDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ProdutoRetiradaService extends CrudService {

    private static ProdutoRetiradaService instance = null;
    
    private ProdutoRetiradaService() {
        //--
    }
    
    public static ProdutoRetiradaService getInstance() {
        if (instance == null) {
            instance = new ProdutoRetiradaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoRetiradaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }
	
    public Date getDtMaxRetiradaProduto(Cliente cliente, String cdProduto) throws SQLException {
		ProdutoRetirada produtoRetiradaFilter = new ProdutoRetirada();
    	produtoRetiradaFilter.cdEmpresa = cliente.cdEmpresa;
    	produtoRetiradaFilter.cdRepresentante = cliente.cdRepresentante;
    	produtoRetiradaFilter.cdCliente = cliente.cdCliente;
    	produtoRetiradaFilter.cdProduto = cdProduto;
    	produtoRetiradaFilter.dtMaxRetiradaStartFilter = DateUtil.getCurrentDate();
    	produtoRetiradaFilter.sortAtributte = ProdutoRetirada.COLUMN_DTMAXRETIRADA;
    	produtoRetiradaFilter.sortAsc = ValueUtil.VALOR_NAO;
    	produtoRetiradaFilter.limit = 1;
    	Vector produtoRetiradaList = findAllByExample(produtoRetiradaFilter);
		return ValueUtil.isEmpty(produtoRetiradaList) ? null : ((ProdutoRetirada)produtoRetiradaList.items[0]).dtMaxRetirada;
	}
    
}