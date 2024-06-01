package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VerbaProduto;
import br.com.wmw.lavenderepda.business.domain.VerbaTabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaProdutoPdbxDao;
import totalcross.util.Vector;

public class VerbaProdutoService extends CrudService {

    private static VerbaProdutoService instance;

    private VerbaProdutoService() {
        //--
    }

    public static VerbaProdutoService getInstance() {
        if (instance == null) {
            instance = new VerbaProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaProdutoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector getVerbaProduto(VerbaTabelaPreco verbaTabelaPreco, ItemPedido itemPedido) throws SQLException {
    	Vector verbaProdutoList = new Vector();
		VerbaProduto verbaProdutoFilter = new VerbaProduto();
		verbaProdutoFilter.cdEmpresa = verbaTabelaPreco.cdEmpresa;
		verbaProdutoFilter.cdRepresentante = verbaTabelaPreco.cdRepresentante;
		verbaProdutoFilter.cdVerba = verbaTabelaPreco.cdVerba;
		verbaProdutoFilter.cdProduto = itemPedido.cdProduto;
		verbaProdutoFilter.flGeraVerba = ValueUtil.VALOR_SIM;
		VerbaProduto verbaProduto = (VerbaProduto)VerbaProdutoService.getInstance().findByRowKey(verbaProdutoFilter.getRowKey());
		if (verbaProduto != null) {
			verbaProdutoList.addElement(verbaProduto);
		}
		verbaProdutoFilter.flGeraVerba = ValueUtil.VALOR_NAO;
		verbaProduto = (VerbaProduto)VerbaProdutoService.getInstance().findByRowKey(verbaProdutoFilter.getRowKey());
		if (verbaProduto != null) {
			verbaProdutoList.addElement(verbaProduto);
		}
		return verbaProdutoList;
    }
}