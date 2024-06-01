package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteSetorOrigemPdbxDao;

public class ClienteSetorOrigemService extends CrudService {

    private static ClienteSetorOrigemService instance;

    private ClienteSetorOrigemService() {
        //--
    }

    public static ClienteSetorOrigemService getInstance() {
        if (instance == null) {
            instance = new ClienteSetorOrigemService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteSetorOrigemPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        Clientesetororigem clientesetororigem = (Clientesetororigem) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(clientesetororigem.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(clientesetororigem.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_CDREPRESENTANTE);
        }
        //cdTipoclirede
        if (ValueUtil.isEmpty(clientesetororigem.cdTipoclirede)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_CDTIPOCLIREDE);
        }
        //cdClirede
        if (ValueUtil.isEmpty(clientesetororigem.cdClirede)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_CDCLIREDE);
        }
        //cdSetor
        if (ValueUtil.isEmpty(clientesetororigem.cdSetor)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_CDSETOR);
        }
        //cdOrigemsetor
        if (ValueUtil.isEmpty(clientesetororigem.cdOrigemsetor)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_CDORIGEMSETOR);
        }
        //dsOrigemsetor
        if (ValueUtil.isEmpty(clientesetororigem.dsOrigemsetor)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_DSORIGEMSETOR);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(clientesetororigem.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(clientesetororigem.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(clientesetororigem.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESETORORIGEM_LABEL_CDUSUARIO);
        }
*/
    }

}