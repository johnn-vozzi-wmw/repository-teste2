package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoComplemento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoComplementoDbxDao;
import totalcross.util.Vector;

public class ProdutoComplementoService extends CrudService {

    private static ProdutoComplementoService instance;
    
    private ProdutoComplementoService() {
        //--
    }
    
    public static ProdutoComplementoService getInstance() {
        if (instance == null) {
            instance = new ProdutoComplementoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoComplementoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        ProdutoComplementar produtoComplementar = (ProdutoComplementar) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(produtoComplementar.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOCOMPLEMENTAR_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(produtoComplementar.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOCOMPLEMENTAR_LABEL_CDREPRESENTANTE);
        }
        //cdProduto
        if (ValueUtil.isEmpty(produtoComplementar.cdProduto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOCOMPLEMENTAR_LABEL_CDPRODUTO);
        }
        //cdProdutoComplementar
        if (ValueUtil.isEmpty(produtoComplementar.cdProdutoComplementar)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOCOMPLEMENTAR_LABEL_CDPRODUTOCOMPLEMENTAR);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(produtoComplementar.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOCOMPLEMENTAR_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(produtoComplementar.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOCOMPLEMENTAR_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(produtoComplementar.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOCOMPLEMENTAR_LABEL_CDUSUARIO);
        }
*/
    }
    
    public Vector findAllProdutoComplementarByProdList(Vector itemPedidoList) throws SQLException {
    	Vector result = new Vector();
    	for (int i = 0; i < itemPedidoList.size(); i++) {
    		ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
    		ProdutoComplemento produtoComplementar = new ProdutoComplemento();
    		produtoComplementar.cdEmpresa = itemPedido.cdEmpresa;
			produtoComplementar.cdRepresentante = itemPedido.cdRepresentante;
 			produtoComplementar.cdProduto = itemPedido.cdProduto;
 			Vector produtoComplementarlist = findAllByExample(produtoComplementar);
 			if (produtoComplementarlist.size() != 0) {
 				result = VectorUtil.concatVectors(result, produtoComplementarlist);
 			}
    	}
    	return result;
    }
    
    public Vector findAllProdutoComplementarByProd(Produto produto) throws SQLException {
    	ProdutoComplemento produtoComplemento = new ProdutoComplemento();
    	produtoComplemento.cdEmpresa = produto.cdEmpresa;
    	produtoComplemento.cdRepresentante = produto.cdRepresentante;
    	produtoComplemento.cdProduto = produto.cdProduto;
    	return findAllByExample(produtoComplemento);
    }

}