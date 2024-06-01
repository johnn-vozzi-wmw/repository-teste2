package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CepDbxDao;

public class CepService extends CrudService {

    private static CepService instance;
    
    private CepService() {
        //--
    }
    
    public static CepService getInstance() {
        if (instance == null) {
            instance = new CepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CepDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        Cep cep = (Cep) domain;

        //dsCep
        if (ValueUtil.isEmpty(cep.dsCep)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CEP_LABEL_DSCEP);
        }
        //cdLogradouro
        if (ValueUtil.isEmpty(cep.cdLogradouro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CEP_LABEL_CDLOGRADOURO);
        }
        //cdBairro
        if (ValueUtil.isEmpty(cep.cdBairro)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CEP_LABEL_CDBAIRRO);
        }
        //cdCidade
        if (ValueUtil.isEmpty(cep.cdCidade)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CEP_LABEL_CDCIDADE);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(cep.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CEP_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(cep.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CEP_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(cep.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CEP_LABEL_CDUSUARIO);
        }
*/
    }
    
}