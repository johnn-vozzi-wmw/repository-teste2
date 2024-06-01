package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteSegDbxDao;

public class ClienteSegService extends CrudService {

    private static ClienteSegService instance;
    
    private ClienteSegService() {
        //--
    }
    
    public static ClienteSegService getInstance() {
        if (instance == null) {
            instance = new ClienteSegService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteSegDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        ClienteSeg clienteSeg = (ClienteSeg) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(clienteSeg.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(clienteSeg.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_CDREPRESENTANTE);
        }
        //cdSegmento
        if (ValueUtil.isEmpty(clienteSeg.cdSegmento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_CDSEGMENTO);
        }
        //cdCliente
        if (ValueUtil.isEmpty(clienteSeg.cdCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_CDCLIENTE);
        }
        //flDefault
        if (ValueUtil.isEmpty(clienteSeg.flDefault)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_FLDEFAULT);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(clienteSeg.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(clienteSeg.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(clienteSeg.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTESEG_LABEL_CDUSUARIO);
        }
*/
    }

}