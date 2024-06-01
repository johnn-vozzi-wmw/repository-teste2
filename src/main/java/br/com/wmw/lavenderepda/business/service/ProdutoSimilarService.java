package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoSimilarDbxDao;
import totalcross.util.Vector;

public class ProdutoSimilarService extends CrudService {

    private static ProdutoSimilarService instance;

    private ProdutoSimilarService() {
        //--	
    }

    public static ProdutoSimilarService getInstance() {
        if (instance == null) {
            instance = new ProdutoSimilarService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoSimilarDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        Produtosimilar produtosimilar = (Produtosimilar) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(produtosimilar.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOSIMILAR_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(produtosimilar.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOSIMILAR_LABEL_CDREPRESENTANTE);
        }
        //cdProduto
        if (ValueUtil.isEmpty(produtosimilar.cdProduto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOSIMILAR_LABEL_CDPRODUTO);
        }
        //cdProdutoSimilar
        if (ValueUtil.isEmpty(produtosimilar.cdProdutosimilar)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOSIMILAR_LABEL_CDPRODUTOSIMILAR);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(produtosimilar.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOSIMILAR_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(produtosimilar.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOSIMILAR_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(produtosimilar.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTOSIMILAR_LABEL_CDUSUARIO);
        }
*/
    }
    
    public Vector findProdutosSimilaresByProduto(Produto produto) throws SQLException {
    	ProdutoSimilar filter = new ProdutoSimilar();
    	filter.cdEmpresa = produto.cdEmpresa;
    	filter.cdRepresentante = produto.cdRepresentante;
    	filter.cdProduto = produto.cdProduto;
    	return findAllByExample(filter);
    }

}