package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RotaEntregaCliDbxDao;

public class RotaEntregaCliService extends CrudService {

    private static RotaEntregaCliService instance;
    
    private RotaEntregaCliService() {
        //--
    }
    
    public static RotaEntregaCliService getInstance() {
        if (instance == null) {
            instance = new RotaEntregaCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RotaEntregaCliDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        RotaEntregaCli rotaEntregaCli = (RotaEntregaCli) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(rotaEntregaCli.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ROTAENTREGACLI_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(rotaEntregaCli.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ROTAENTREGACLI_LABEL_CDREPRESENTANTE);
        }
        //cdRotaEntrega
        if (ValueUtil.isEmpty(rotaEntregaCli.cdRotaEntrega)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ROTAENTREGACLI_LABEL_CDROTAENTREGA);
        }
        //cdCliente
        if (ValueUtil.isEmpty(rotaEntregaCli.cdCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ROTAENTREGACLI_LABEL_CDCLIENTE);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(rotaEntregaCli.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ROTAENTREGACLI_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(rotaEntregaCli.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ROTAENTREGACLI_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(rotaEntregaCli.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ROTAENTREGACLI_LABEL_CDUSUARIO);
        }
*/
    }

}