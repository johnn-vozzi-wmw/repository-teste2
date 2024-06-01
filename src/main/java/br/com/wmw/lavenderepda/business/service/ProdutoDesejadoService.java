package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ProdutoDesejado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoDesejadoDbxDao;
import totalcross.util.Date;

public class ProdutoDesejadoService extends CrudPersonLavendereService {

    private static ProdutoDesejadoService instance = null;
    
    private ProdutoDesejadoService() {
        //--
    }
    
    public static ProdutoDesejadoService getInstance() {
        if (instance == null) {
            instance = new ProdutoDesejadoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoDesejadoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws SQLException {
        ProdutoDesejado produtoDesejado = (ProdutoDesejado) domain;
        //cdProdutoDesejado
        if (ValueUtil.isEmpty(produtoDesejado.cdProdutoDesejado)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTODESEJADO_LABEL_CDPRODUTODESEJADO);
        }
        //dsProdutoDesejado
		if (LavenderePdaConfig.qtCaracteresMinimoDescricaoProdutoDesejado <= 0 && ValueUtil.isEmpty(produtoDesejado.dsProdutoDesejado)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTODESEJADO_LABEL_DSPRODUTODESEJADO);
		} else if (produtoDesejado.dsProdutoDesejado.length() < LavenderePdaConfig.qtCaracteresMinimoDescricaoProdutoDesejado) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PRODUTODESEJADO_ERRO_QTD_CARACTERES, new Object[]{StringUtil.getStringValue(LavenderePdaConfig.qtCaracteresMinimoDescricaoProdutoDesejado)}));
		}
		//conrrente 
		if (!ValueUtil.getBooleanValue(produtoDesejado.flOutroConcorrente) && ValueUtil.isEmpty(produtoDesejado.cdConcorrente)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTODESEJADO_LABEL_CDCONCORRENTE);
		}
		//dsOutroConcorrente
		if (ValueUtil.getBooleanValue(produtoDesejado.flOutroConcorrente) && ValueUtil.isEmpty(produtoDesejado.dsOutroConcorrente)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTODESEJADO_LABEL_DESC_OUTRO_CONCORRENTE);
		}
        super.validate(domain);
    }
    
    public void executaLimpezaProdutoDesejado() throws SQLException {
    	if (LavenderePdaConfig.nuDiasPermanenciaRegistroProdutosDesejados >= 0) {
    		ProdutoDesejado produtoDesejadoFilter = new ProdutoDesejado.Builder(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante).build();
    		Date dataLimite = DateUtil.getCurrentDate();
    		DateUtil.decDay(dataLimite, LavenderePdaConfig.nuDiasPermanenciaRegistroProdutosDesejados);
    		produtoDesejadoFilter.dtEmissaoPedidoFilter = dataLimite;
    		deleteAllByExample(produtoDesejadoFilter);
    	}
    }
    
    @Override
    public void insert(BaseDomain domain) throws SQLException {
		validate(domain);
		validateDuplicated(domain);
		//--
		domain.cdUsuario = Session.getCdUsuario();
		//--
		getCrudDao().insert(domain);
    }
    
    @Override
    public void update(BaseDomain domain) throws SQLException {
    	update(domain, false);
    }
    
    public void updateFlTipoAlteracaoByExample(BaseDomain domain) {
    	try {
    		ProdutoDesejadoDbxDao.getInstance().updateFlTipoAlteracaoByExample(domain);
    	} catch (Throwable e) {	}
    }

}