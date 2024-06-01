package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MetaVendaTipoDbxDao;

public class MetaVendaTipoService extends CrudService {

    private static MetaVendaTipoService instance;

    private MetaVendaTipoService() {
        //--
    }

    public static MetaVendaTipoService getInstance() {
        if (instance == null) {
            instance = new MetaVendaTipoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MetaVendaTipoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        MetaVendaFamilia metaVendaFamilia = (MetaVendaFamilia) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(metaVendaFamilia.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_CDEMPRESA);
        }
        //cdMetaVenda
        if (ValueUtil.isEmpty(metaVendaFamilia.cdMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_CDMETAVENDA);
        }
        //cdFamilia
        if (ValueUtil.isEmpty(metaVendaFamilia.cdFamilia)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_CDFAMILIA);
        }
        //vlMetaVenda
        if (ValueUtil.isEmpty(metaVendaFamilia.vlMetaVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_VLMETAVENDA);
        }
        //vlRealizadoVenda
        if (ValueUtil.isEmpty(metaVendaFamilia.vlRealizadoVenda)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_VLREALIZADOVENDA);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(metaVendaFamilia.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(metaVendaFamilia.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(metaVendaFamilia.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.METAVENDAFAMILIA_LABEL_CDUSUARIO);
        }
*/
    }

}