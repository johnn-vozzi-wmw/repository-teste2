package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoKit;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoKitDbxDao;
import totalcross.util.Vector;

public class ProdutoKitService extends CrudService {

    private static ProdutoKitService instance;

    private ProdutoKitService() {
        //--
    }

    public static ProdutoKitService getInstance() {
        if (instance == null) {
            instance = new ProdutoKitService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoKitDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        ProdutoKit produtoKit = (ProdutoKit) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(produtoKit.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(produtoKit.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_CDREPRESENTANTE);
        }
        //cdKit
        if (ValueUtil.isEmpty(produtoKit.cdKit)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_CDKIT);
        }
        //cdProduto
        if (ValueUtil.isEmpty(produtoKit.cdProduto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_CDPRODUTO);
        }
        //qtItemFisico
        if (ValueUtil.isEmpty(produtoKit.qtItemFisico)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_QTITEMFISICO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(produtoKit.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(produtoKit.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(produtoKit.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOKIT_LABEL_CDUSUARIO);
        }
*/
    }

    public void loadItemKitPedidoFromProdutoKit(ItemPedido itemPedido) throws SQLException {
    	ProdutoKit produtoKitFilter = new ProdutoKit();
    	produtoKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoKitFilter.cdKit = itemPedido.cdProduto;
		Vector listProdutoKit = ProdutoKitService.getInstance().findAllByExample(produtoKitFilter);
		int size = listProdutoKit.size();
		itemPedido.itemKitPedidoList = new Vector(size);
		for (int i = 0; i < size; i++) {
			ProdutoKit produtoKit = (ProdutoKit)listProdutoKit.items[i];
			ItemKitPedido itemKit = new ItemKitPedido();
			itemKit.cdEmpresa = itemPedido.cdEmpresa;
			itemKit.cdRepresentante = itemPedido.cdRepresentante;
			itemKit.cdKit = itemPedido.cdProduto;
			itemKit.nuPedido = itemPedido.nuPedido;
			itemKit.flOrigemPedido = itemPedido.flOrigemPedido;
			itemKit.flTipoItemPedido = ValueUtil.isEmpty(itemPedido.flTipoItemPedido) ? TipoItemPedido.TIPOITEMPEDIDO_NORMAL : itemPedido.flTipoItemPedido;
			itemKit.nuSeqProduto = itemPedido.nuSeqProduto;
			itemKit.cdProduto = produtoKit.cdProduto;
			itemKit.qtItemFisico = produtoKit.qtItemFisico;
			itemPedido.itemKitPedidoList.addElement(itemKit);
		}
    }

    public ProdutoKit getProdutoKitByItemKitPedido(ItemKitPedido itemKitPedido) throws SQLException {
    	ProdutoKit produtoKitFilter = new ProdutoKit();
    	produtoKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoKitFilter.cdKit = itemKitPedido.cdKit;
    	produtoKitFilter.cdProduto = itemKitPedido.cdProduto;
    	return (ProdutoKit)findByRowKey(produtoKitFilter.getRowKey());
    }

}