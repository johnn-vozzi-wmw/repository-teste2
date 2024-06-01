package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPagtoCliDbxDao;

public class TipoPagtoCliService extends CrudService {

    private static TipoPagtoCliService instance;

    private TipoPagtoCliService() {
        //--
    }

    public static TipoPagtoCliService getInstance() {
        if (instance == null) {
            instance = new TipoPagtoCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoPagtoCliDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        TipoPagtoCli tipoPagtoCli = (TipoPagtoCli) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(tipoPagtoCli.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTOCLI_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(tipoPagtoCli.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTOCLI_LABEL_CDREPRESENTANTE);
        }
        //cdTipoPagamento
        if (ValueUtil.isEmpty(tipoPagtoCli.cdTipoPagamento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTOCLI_LABEL_CDTIPOPAGAMENTO);
        }
        //cdCliente
        if (ValueUtil.isEmpty(tipoPagtoCli.cdCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTOCLI_LABEL_CDCLIENTE);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(tipoPagtoCli.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTOCLI_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(tipoPagtoCli.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTOCLI_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(tipoPagtoCli.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOPAGTOCLI_LABEL_CDUSUARIO);
        }
*/
    }

}