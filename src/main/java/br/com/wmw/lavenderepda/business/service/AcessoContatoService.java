package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.EmailUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AcessoContato;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AcessoContatoDbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.util.ElementNotFoundException;
import totalcross.util.Hashtable;

public class AcessoContatoService extends CrudService {

    private static AcessoContatoService instance = null;
    
    private AcessoContatoService() {
        //--
    }
    
    public static AcessoContatoService getInstance() {
        if (instance == null) {
            instance = new AcessoContatoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AcessoContatoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    	AcessoContato acessoContato = (AcessoContato) domain;
    	
    	//dsNome
        if (ValueUtil.isEmpty(acessoContato.dsNome) || ValueUtil.valueEquals(acessoContato.dsNome, Messages.ACESSOCONTATO_LABEL_DSNOME)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ACESSOCONTATO_LABEL_DSNOME);
        }
        //dsEmail
        if (ValueUtil.isEmpty(acessoContato.dsEmail) || ValueUtil.valueEquals(acessoContato.dsEmail, Messages.ACESSOCONTATO_LABEL_DSEMAIL)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.ACESSOCONTATO_LABEL_DSEMAIL);
        } else if (!EmailUtil.validateEmail(acessoContato.dsEmail)) {
        	throw new ValidationException(Messages.ACESSOCONTATO_MSG_EMAIL_INVALIDO);
        }
    	
        //cdEmpresa
        if (ValueUtil.valueEquals(acessoContato.dsEmpresa, Messages.ACESSOCONTATO_LABEL_CDEMPRESA)) {
        	acessoContato.dsEmpresa = null;
        }
               
    }
    
    @Override
    public void insert(BaseDomain domain) throws SQLException {
    	AcessoContato acessoContato = (AcessoContato) domain;
    	acessoContato.cdAcessoContato = generateIdGlobal();
    	acessoContato.dtCadastro = DateUtil.getCurrentDate();
    	acessoContato.hrCadastro = TimeUtil.getCurrentTimeHHMMSS();
    	super.insert(acessoContato);
    }
    
    public void sendDadosServidor() throws Exception {
    	SyncInfo syncInfo = new SyncInfo();
		syncInfo.tableName = AcessoContato.TABLE_NAME;
		syncInfo.keys = new String[] {"cdEmpresa", "cdUsuario"};
		syncInfo.flRemessa = false;
		syncInfo.flRetorno = true;
		Hashtable hash = new Hashtable(1);
		hash.put(syncInfo.tableName, syncInfo);
		SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), hash);
    }
    
    public boolean isNovoAcesso() throws SQLException {
    	return ValueUtil.isEmpty(findAll());
    }
    
    public boolean isEnviado() throws SQLException {
    	AcessoContato acessoContato = null;
    	try {
			acessoContato = (AcessoContato) findAll().pop();
		} catch (ElementNotFoundException e) {
			return false;
		}
    	return acessoContato.isEnviadoServidor();
    }

}