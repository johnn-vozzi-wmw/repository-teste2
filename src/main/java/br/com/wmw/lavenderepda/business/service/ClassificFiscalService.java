package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClassificFiscal;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClassificFiscalDbxDao;

public class ClassificFiscalService extends CrudService {

    private static ClassificFiscalService instance;

    private ClassificFiscalService() {
        //--
    }

    public static ClassificFiscalService getInstance() {
        if (instance == null) {
            instance = new ClassificFiscalService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClassificFiscalDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        ClassificFiscal classificFiscal = (ClassificFiscal) domain;

        //cdClassificFiscal
        if (ValueUtil.isEmpty(classificFiscal.cdClassificFiscal)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLASSIFICFISCAL_LABEL_CDCLASSIFICFISCAL);
        }
        //dsClassificFiscal
        if (ValueUtil.isEmpty(classificFiscal.dsClassificFiscal)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLASSIFICFISCAL_LABEL_DSCLASSIFICFISCAL);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(classificFiscal.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLASSIFICFISCAL_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(classificFiscal.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLASSIFICFISCAL_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(classificFiscal.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLASSIFICFISCAL_LABEL_CDUSUARIO);
        }
*/
    }

    public String getDsClassificacaoFiscal(String cdClassificacaoFiscal) throws SQLException {
    	ClassificFiscal classificFiscalFilter = new ClassificFiscal();
    	classificFiscalFilter.cdClassificFiscal = cdClassificacaoFiscal;
    	classificFiscalFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	classificFiscalFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	classificFiscalFilter.dsClassificFiscal = findColumnByRowKey(classificFiscalFilter.getRowKey(), "DSCLASSIFICFISCAL");
    	return classificFiscalFilter.toString();
    }

}