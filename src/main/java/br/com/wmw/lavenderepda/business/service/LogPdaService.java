package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.SyncLogService;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.UsuarioPda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LogPdaPdbxDao;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.util.Vector;

public class LogPdaService extends CrudService implements SyncLogService {

    private static LogPdaService instance;

    private LogPdaService() {
        //--
    }

    public static LogPdaService getInstance() {
        if (instance == null) {
            instance = new LogPdaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return LogPdaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    //@Override
    public void validateInsert(BaseDomain domain) {
    	//Desnecessário no caso do LogPda, porque no insert já faz
    	//um controle diferenciado para evitar duplicidade
    }

    //----------------------------------------------------------------------------------

    //@Override
    public String generateIdGlobal() {
    	return TimeUtil.getCurrentDateTimeDDDDDSSSSSMMM();
    }

    public void log(String cdNivel, int cdCategoria, String dsLog) {
    	LogPda logPda = new LogPda();
    	logPda.cdNivel = cdNivel;
    	if ((LavenderePdaConfig.nivelRegistroDeLogNoPda > 0) && (logPda.getCdNivelInt() >= LavenderePdaConfig.nivelRegistroDeLogNoPda)) {
    		logPda.cdCategoria = cdCategoria;
    		dsLog = StringUtil.clearSpecialChars(dsLog);
    		dsLog = StringUtil.changeStringAccented(dsLog);
    		if (ValueUtil.isNotEmpty(dsLog) && dsLog.length() > 400) {
    			dsLog = dsLog.substring(0, 400);
    		}
    		logPda.dsLog = dsLog;
    		logPda.dtLog = DateUtil.getCurrentDate();
    		logPda.hrLog = TimeUtil.getCurrentTimeHHMM();
    		int inc = 0;
    		try {
    			do {
    				logPda.cdLog = StringUtil.getStringValue(ValueUtil.getLongValue(generateIdGlobal()) + inc);
    				inc++;
    			} while (findColumnByRowKey(logPda.getRowKey(), "ROWKEY") != null);
    			insert(logPda);
    		} catch (Throwable e) {
			}
    	}
    }

    public void fatal(int cdCategoria, String dsLog) {
    	log(LogPda.LOG_NIVEL_FATAL, cdCategoria, dsLog);
    }

    public void error(int cdCategoria, String dsLog) {
    	log(LogPda.LOG_NIVEL_ERROR, cdCategoria, dsLog);
    }

    public void warn(int cdCategoria, String dsLog) {
    	log(LogPda.LOG_NIVEL_WARN, cdCategoria, dsLog);
    }

    public void info(int cdCategoria, String dsLog) {
    	log(LogPda.LOG_NIVEL_INFO, cdCategoria, dsLog);
    }

    public void debug(int cdCategoria, String dsLog) {
    	log(LogPda.LOG_NIVEL_DEBUG, cdCategoria, dsLog);
    }

    public void trace(int cdCategoria, String dsLog) {
    	log(LogPda.LOG_NIVEL_TRACE, cdCategoria, dsLog);
    }


    //----------------------------------------------------------------------------------

    public void logMemoria(String msg) {
    	double memory = (double) Vm.getFreeMemory() / (double) 1048576;
		String strMemory = StringUtil.getStringValue(memory, ValueUtil.doublePrecision);
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(strMemory);
    	strBuffer.append("Mb (");
    	strBuffer.append(Vm.getFreeMemory());
    	strBuffer.append(" bytes)");
		info(LogPda.LOG_CATEGORIA_MEMORIA, MessageUtil.getMessage(msg, strBuffer.toString()));
    }

    public void logRecuperarDadosRemoto(String nmTabela) {
		warn(LogPda.LOG_CATEGORIA_RECUPERAR_DADOS_REMOTO, MessageUtil.getMessage(Messages.LOGPDA_MSG_RECUPERAR_DADOS_REMOTO, nmTabela));
    }

    public void logRecuperarDadosRemotoSucesso(String tableName) {
    	warn(LogPda.LOG_CATEGORIA_RECUPERAR_DADOS_REMOTO, MessageUtil.getMessage(Messages.LOGPDA_MSG_RECUPERAR_DADOS_REMOTO_SUCESSO, tableName));
    }

    public void logBackup(String tableName) {
    	debug(LogPda.LOG_CATEGORIA_BACKUP, MessageUtil.getMessage(Messages.LOGPDA_MSG_BACKUP_CARD, tableName));
    }

    public void logRecuperaBackup(String tableName, Time time) {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(time.day);
    	strBuffer.append("/");
    	strBuffer.append(time.month);
    	strBuffer.append("/");
    	strBuffer.append(time.year);
    	warn(LogPda.LOG_CATEGORIA_BACKUP, MessageUtil.getMessage(Messages.LOGPDA_MSG_BACKUP_PDA, new String [] {tableName, strBuffer.toString()}));
    }

    public void logAlteraParametroPda(int param, String valor) {
		warn(LogPda.LOG_CATEGORIA_ALTERA_PARAMETRO, MessageUtil.getMessage(Messages.LOGPDA_MSG_ALTERADO_PARAMETRO, new String []  {StringUtil.getStringValueToInterface(param), valor}));
    }

    //----------------------------------------------------------------------------------
    // Métodos implementados para a Interface SyncLogService
    //----------------------------------------------------------------------------------

	public void logSyncError(Throwable ex) {
		logSyncError(ex.getMessage());
	}

	public void logSyncError(String msg) {
		error(LogPda.LOG_CATEGORIA_SYNC, msg);
	}

	public void logSyncWarn(String msg) {
		warn(LogPda.LOG_CATEGORIA_SYNC, msg);
	}

	public void logSyncInfo(String msg) {
		info(LogPda.LOG_CATEGORIA_SYNC, msg);
	}

	public void logSyncDebug(String msg) {
		debug(LogPda.LOG_CATEGORIA_SYNC, msg);
	}

	public void logSyncTrace(String msg) {
		trace(LogPda.LOG_CATEGORIA_SYNC, msg);
	}

	protected void setDadosAlteracao(BaseDomain domain) {
    	domain.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
    	if (ValueUtil.isEmpty(Session.getCdUsuario())) {
    		try {
    			Vector usuariosList = UsuarioPdaService.getInstance().findAll();
    			UsuarioPda usu = (UsuarioPda)usuariosList.items[0];
    			domain.cdUsuario = usu.cdUsuario;
    			usuariosList = null;
    		} catch (Throwable e) {
    		}
    	} else {
    		domain.cdUsuario = Session.getCdUsuario();
    	}
    	if (ValueUtil.isEmpty(domain.cdUsuario)) { // Em alguns pontos está inserindo Log com Usuario vazio. Com isso dá um erro ao inserir no banco na web. Esse Log é para descobrirmos pontos do sistema que o problema ocorre.
    		throw new ValidationException(Messages.MSG_NAO_FOI_POSSIVEL_INSERIR_LOG_PDA);
    	}
	}

	public void saveLogErroEnvioDadosByCache() {
		Vector erroEnvioDados = EnviaDadosThread.getInstance().erroEnvioDados;
		if (erroEnvioDados != null) {
			int size = erroEnvioDados.size();
			for (int i = 0; i < size; i++) {
				String errorMessage = (String) erroEnvioDados.items[i];
				errorMessage = errorMessage.replaceAll("'", "\"");
				LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_ENVIO_BACKGROUND, MessageUtil.getMessage(Messages.LOG_ERRO_ENVIO_BACKGROUND, errorMessage));
			}
		}
	}

}