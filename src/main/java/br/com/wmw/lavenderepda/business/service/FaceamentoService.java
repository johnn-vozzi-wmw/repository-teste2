package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Faceamento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FaceamentoPdbxDao;

public class FaceamentoService extends CrudService {

    private static FaceamentoService instance;

    private FaceamentoService() {
        //--
    }

    public static FaceamentoService getInstance() {
        if (instance == null) {
            instance = new FaceamentoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FaceamentoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
        Faceamento faceamento = (Faceamento) domain;
        if (ValueUtil.isEmpty(faceamento.cdProduto)) {
        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ITEMPEDIDO_LABEL_PRODUTO);
        }
        //qtPontoequilibrio
        if (faceamento.qtPontoEquilibrio == 0) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FACEAMENTO_LABEL_QTPONTOEQUILIBRIO);
        }
    }
    
    @Override
    public void update(BaseDomain domain) throws SQLException {
    	super.update(domain);
    	Faceamento faceamento = (Faceamento) domain;
    	FaceamentoEstoqueService.getInstance().recalculaSugestaoVenda(faceamento.qtPontoEquilibrio);
    	
    }
    
}