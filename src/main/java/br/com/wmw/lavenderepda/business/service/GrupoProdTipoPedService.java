package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProdTipoPedDbxDao;

public class GrupoProdTipoPedService extends CrudService {

    private static GrupoProdTipoPedService instance;

    private GrupoProdTipoPedService() {
        //--
    }

    public static GrupoProdTipoPedService getInstance() {
        if (instance == null) {
            instance = new GrupoProdTipoPedService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return GrupoProdTipoPedDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        GrupoProd1TipoPed grupoProd1TipoPed = (GrupoProd1TipoPed) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(grupoProd1TipoPed.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(grupoProd1TipoPed.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_CDREPRESENTANTE);
        }
        //cdGrupoProduto1
        if (ValueUtil.isEmpty(grupoProd1TipoPed.cdGrupoProduto1)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_CDGRUPOPRODUTO1);
        }
        //cdTipoPedido
        if (ValueUtil.isEmpty(grupoProd1TipoPed.cdTipoPedido)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_CDTIPOPEDIDO);
        }
        //flAtivo
        if (ValueUtil.isEmpty(grupoProd1TipoPed.flAtivo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_FLATIVO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(grupoProd1TipoPed.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(grupoProd1TipoPed.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(grupoProd1TipoPed.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.GRUPOPROD1TIPOPED_LABEL_CDUSUARIO);
        }
*/
    }

}