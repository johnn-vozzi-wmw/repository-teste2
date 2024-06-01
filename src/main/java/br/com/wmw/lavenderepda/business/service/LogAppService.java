package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.business.domain.LogApp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LogAppDbxDao;

public class LogAppService extends CrudService {

    private static LogAppService instance;
    private static String cdLogOld;
    
    private LogAppService() {
        //--
    }
    
    public static LogAppService getInstance() {
        if (instance == null) {
            instance = new LogAppService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return LogAppDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

    
    public void logPedido(String flTipoLog, String vlChave, String cdCliente, String dsDetalhes, String... params) {
    	LogApp logApp;
		try {
			String cdLog = generateIdGlobal();
			cdLog = cdLog.equals(cdLogOld) ? cdLog + 1 : cdLog;
			logApp = new LogApp(cdLog, flTipoLog, vlChave, LogApp.CD_PROCESSO_EMISSAO_PEDIDO, MessageUtil.getMessage(dsDetalhes, params), cdCliente);
			insert(logApp);
			cdLogOld = cdLog;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }

    public void logEstoque(String vlChave, String dsDetalhes, String... params) {
        LogApp logApp;
        try {
            String cdLog = generateIdGlobal();
            cdLog = cdLog.equals(cdLogOld) ? cdLog + 1 : cdLog;
            logApp = new LogApp(cdLog, LogApp.FL_TIPO_LOG_INFO, vlChave, LogApp.CD_PROCESSO_ESTOQUE, MessageUtil.getMessage(dsDetalhes, params), null);
            insert(logApp);
            cdLogOld = cdLog;
        } catch (Exception e) {
            ExceptionUtil.handle(e);
        }
    }

	public void deleteLogEnviadoServidor() {
		LogAppDbxDao.getInstance().deleteLogEnviadoServidor();
	}
    
}