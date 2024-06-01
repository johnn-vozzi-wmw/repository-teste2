package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoSegDbxDao;

public class CondPagtoSegService extends CrudService {

    private static CondPagtoSegService instance;
    
    private CondPagtoSegService() {
        //--
    }
    
    public static CondPagtoSegService getInstance() {
        if (instance == null) {
            instance = new CondPagtoSegService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondPagtoSegDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        CondPagtoSeg condPagtoSeg = (CondPagtoSeg) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(condPagtoSeg.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDPAGTOSEG_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(condPagtoSeg.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDPAGTOSEG_LABEL_CDREPRESENTANTE);
        }
        //cdSegmento
        if (ValueUtil.isEmpty(condPagtoSeg.cdSegmento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDPAGTOSEG_LABEL_CDSEGMENTO);
        }
        //cdCondicaoPagamento
        if (ValueUtil.isEmpty(condPagtoSeg.cdCondicaoPagamento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDPAGTOSEG_LABEL_CDCONDICAOPAGAMENTO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(condPagtoSeg.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDPAGTOSEG_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(condPagtoSeg.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDPAGTOSEG_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(condPagtoSeg.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONDPAGTOSEG_LABEL_CDUSUARIO);
        }
*/
    }

}