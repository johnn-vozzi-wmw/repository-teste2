package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoCondPagtoCliDbxDao;

public class TipoCondPagtoCliService extends CrudService {

    private static TipoCondPagtoCliService instance;

    private TipoCondPagtoCliService() {
        //--
    }

    public static TipoCondPagtoCliService getInstance() {
        if (instance == null) {
            instance = new TipoCondPagtoCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoCondPagtoCliDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        TipoCondPagtoCli tipoCondPagtoCli = (TipoCondPagtoCli) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(tipoCondPagtoCli.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(tipoCondPagtoCli.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_CDREPRESENTANTE);
        }
        //cdCliente
        if (ValueUtil.isEmpty(tipoCondPagtoCli.cdCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_CDCLIENTE);
        }
        //cdCondicaoPagamento
        if (ValueUtil.isEmpty(tipoCondPagtoCli.cdCondicaoPagamento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_CDCONDICAOPAGAMENTO);
        }
        //cdTipoPagamento
        if (ValueUtil.isEmpty(tipoCondPagtoCli.cdTipoPagamento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_CDTIPOPAGAMENTO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(tipoCondPagtoCli.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(tipoCondPagtoCli.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(tipoCondPagtoCli.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TIPOCONDPAGTOCLI_LABEL_CDUSUARIO);
        }
*/
    }

}