package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGradeErpDif;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoGradeErpDifDbxDao;
import totalcross.util.Vector;

public class ItemPedidoGradeErpDifService extends CrudService {

    private static ItemPedidoGradeErpDifService instance;

    private ItemPedidoGradeErpDifService() {
        //--
    }

    public static ItemPedidoGradeErpDifService getInstance() {
        if (instance == null) {
            instance = new ItemPedidoGradeErpDifService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemPedidoGradeErpDifDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public Vector findAllItemPedidoGradePorItemPedido(ItemPedido itemPedido) {
    	if (itemPedido == null) return new Vector();
    	try {
			ItemPedidoGradeErpDif itemPedidoGradeErpDifFilter = new ItemPedidoGradeErpDif();
			itemPedidoGradeErpDifFilter.cdEmpresa = itemPedido.cdEmpresa;
			itemPedidoGradeErpDifFilter.cdRepresentante = itemPedido.cdRepresentante;
			itemPedidoGradeErpDifFilter.flOrigemPedido = itemPedido.flOrigemPedido;
			itemPedidoGradeErpDifFilter.nuPedido = itemPedido.nuPedido;
			itemPedidoGradeErpDifFilter.cdProduto = itemPedido.cdProduto;
			itemPedidoGradeErpDifFilter.flTipoItemPedido = itemPedido.flTipoItemPedido;
			return ItemPedidoGradeErpDifDbxDao.getInstance().findAllItemPedidoGradePorItemPedido(itemPedidoGradeErpDifFilter);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.ITEMGRADEERPDIF_ERRO_CARREGAMENTO_GRADES, ex.getMessage()));
			return new Vector();
		}
    }
	
}