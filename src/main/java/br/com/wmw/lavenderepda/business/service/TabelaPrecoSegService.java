package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoSegDbxDao;

public class TabelaPrecoSegService extends CrudService {

    private static TabelaPrecoSegService instance;
    
    private TabelaPrecoSegService() {
        //--
    }
    
    public static TabelaPrecoSegService getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoSegService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TabelaPrecoSegDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*    
        TabelaPrecoSeg tabelaPrecoSeg = (TabelaPrecoSeg) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(tabelaPrecoSeg.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECOSEG_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(tabelaPrecoSeg.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECOSEG_LABEL_CDREPRESENTANTE);
        }
        //cdSegmento
        if (ValueUtil.isEmpty(tabelaPrecoSeg.cdSegmento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECOSEG_LABEL_CDSEGMENTO);
        }
        //cdTabelaPreco
        if (ValueUtil.isEmpty(tabelaPrecoSeg.cdTabelaPreco)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECOSEG_LABEL_CDTABELAPRECO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(tabelaPrecoSeg.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECOSEG_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(tabelaPrecoSeg.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECOSEG_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(tabelaPrecoSeg.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.TABELAPRECOSEG_LABEL_CDUSUARIO);
        }
*/
    }

}