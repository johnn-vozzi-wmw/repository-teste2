package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ContratoProdEst;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContratoProdEstDbxDao;

public class ContratoProdEstService extends CrudService {

    private static ContratoProdEstService instance;

    private ContratoProdEstService() {
        //--
    }

    public static ContratoProdEstService getInstance() {
        if (instance == null) {
            instance = new ContratoProdEstService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContratoProdEstDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
        ContratoProdEst contratoProdEst = (ContratoProdEst) domain;
        //--
        if (contratoProdEst.qtEstoqueAtual > contratoProdEst.contratoProduto.qtProdutoContrato) {
            throw new ValidationException(MessageUtil.getMessage(Messages.GRADE_MSG_ALERTA_ESTOQUE_MAIOR_QTDADE, new String [] {StringUtil.getStringValueToInterface(contratoProdEst.qtEstoqueAtual) , StringUtil.getStringValueToInterface(contratoProdEst.contratoProduto.qtProdutoContrato)}));
        }
        //--
        if (ValueUtil.isEmpty(contratoProdEst.qtEstAtual)) {
        	String dsMensagem = FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO;
        	dsMensagem += MessageUtil.getMessage(Messages.GRADE_MSG_ESTOQUE_ATUAL, ProdutoService.getInstance().getDsProduto(contratoProdEst.cdProduto));
        	//--
        	throw new ValidationException(dsMensagem);
        }
    }
}