package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondTipoPedidoDbxDao;

public class CondTipoPedidoService extends CrudService {

    private static CondTipoPedidoService instance;

    private CondTipoPedidoService() {
        //--
    }

    public static CondTipoPedidoService getInstance() {
        if (instance == null) {
            instance = new CondTipoPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondTipoPedidoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        CondTipoPedido condTipoPedido = (CondTipoPedido) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(condTipoPedido.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(condTipoPedido.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_CDREPRESENTANTE);
        }
        //cdCondicaoPagamento
        if (ValueUtil.isEmpty(condTipoPedido.cdCondicaoPagamento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_CDCONDICAOPAGAMENTO);
        }
        //cdTipoPedido
        if (ValueUtil.isEmpty(condTipoPedido.cdTipoPedido)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_CDTIPOPEDIDO);
        }
        //flAtivo
        if (ValueUtil.isEmpty(condTipoPedido.flAtivo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_FLATIVO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(condTipoPedido.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(condTipoPedido.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(condTipoPedido.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDTIPOPEDIDO_LABEL_CDUSUARIO);
        }
*/
    }

}