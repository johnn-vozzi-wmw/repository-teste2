package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaVendaRepDbxDao;

public class MetaVendaRepService extends CrudService {

    private static MetaVendaRepService instance;

    private MetaVendaRepService() {
        //--
    }

    public static MetaVendaRepService getInstance() {
        if (instance == null) {
            instance = new MetaVendaRepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetaVendaRepDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        MetaVendaRep metaVendaRep = (MetaVendaRep) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(metaVendaRep.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_CDEMPRESA);
        }
        //cdMetaVenda
        if (ValueUtil.isEmpty(metaVendaRep.cdMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_CDMETAVENDA);
        }
        //cdGerente
        if (ValueUtil.isEmpty(metaVendaRep.cdGerente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_CDGERENTE);
        }
        //cdTipoMetaVenda
        if (ValueUtil.isEmpty(metaVendaRep.cdTipoMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_CDTIPOMETAVENDA);
        }
        //cdSupervisor
        if (ValueUtil.isEmpty(metaVendaRep.cdSupervisor)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_CDSUPERVISOR);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(metaVendaRep.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_CDREPRESENTANTE);
        }
        //vlMetaVenda
        if (ValueUtil.isEmpty(metaVendaRep.vlMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_VLMETAVENDA);
        }
        //vlRealizadoVenda
        if (ValueUtil.isEmpty(metaVendaRep.vlRealizadoVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_VLREALIZADOVENDA);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(metaVendaRep.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(metaVendaRep.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(metaVendaRep.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAREP_LABEL_CDUSUARIO);
        }
*/
    }

}