package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EntidadeDbxDao;

public class EntidadeService extends CrudService {

    private static EntidadeService instance;

    private EntidadeService() {
        //--
    }

    public static EntidadeService getInstance() {
        if (instance == null) {
            instance = new EntidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return EntidadeDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        Entidade entidade = (Entidade) domain;

        //cdSistema
        if (ValueUtil.isEmpty(entidade.cdSistema)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_CDSISTEMA);
        }
        //nmEntidade
        if (ValueUtil.isEmpty(entidade.nmEntidade)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_NMENTIDADE);
        }
        //nmDomain
        if (ValueUtil.isEmpty(entidade.nmDomain)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_NMDOMAIN);
        }
        //dsEntidade
        if (ValueUtil.isEmpty(entidade.dsEntidade)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_DSENTIDADE);
        }
        //flAuditaIns
        if (ValueUtil.isEmpty(entidade.flAuditaIns)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_FLAUDITAINS);
        }
        //flAuditaUpd
        if (ValueUtil.isEmpty(entidade.flAuditaUpd)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_FLAUDITAUPD);
        }
        //flAuditaDel
        if (ValueUtil.isEmpty(entidade.flAuditaDel)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_FLAUDITADEL);
        }
        //flDinamico
        if (ValueUtil.isEmpty(entidade.flDinamico)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_FLDINAMICO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(entidade.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(entidade.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(entidade.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ENTIDADE_LABEL_CDUSUARIO);
        }
*/
    }

}