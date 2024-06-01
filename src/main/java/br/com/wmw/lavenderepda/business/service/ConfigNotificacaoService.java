package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConfigNotificacaoDbxDao;
import br.com.wmw.lavenderepda.thread.ServicoNotificacaoThread;
import totalcross.util.Hashtable;
import totalcross.util.Vector;
import java.sql.SQLException;
import java.util.LinkedHashSet;

public class ConfigNotificacaoService extends CrudService {

	private static ConfigNotificacaoService instance;

	private ConfigNotificacaoService() {
		//--
	}

	public static ConfigNotificacaoService getInstance() {
		if (instance == null) {
			instance = new ConfigNotificacaoService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return ConfigNotificacaoDbxDao.getInstance();
	}

	public void startNotificacao(LinkedHashSet<String> classNames, Hashtable infoList) {
		Hashtable newInfoList = new Hashtable(0);
		if (classNames == null || classNames.isEmpty()) {
			return;
		} else {
			infoList.copyInto(newInfoList);
		}
		ServicoNotificacaoThread.asyncPoolExecute(classNames, newInfoList);
	}

	@Override
	public void validate(BaseDomain domain) {
/*    
        ConfigNotificacaoSistema configNotificacaoSistema = (ConfigNotificacaoSistema) domain;

        //cdSistema
        if (ValueUtil.isEmpty(configNotificacaoSistema.cdSistema)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_CDSISTEMA);
        }
        //cdConfigNotificacaoSistema
        if (ValueUtil.isEmpty(configNotificacaoSistema.cdConfigNotificacaoSistema)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_CDCONFIGNOTIFICACAO);
        }
        //cdTipoNotificacao
        if (ValueUtil.isEmpty(configNotificacaoSistema.cdTipoNotificacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_CDTIPONOTIFICACAO);
        }
        //nmEntidade
        if (ValueUtil.isEmpty(configNotificacaoSistema.nmEntidade)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_NMENTIDADE);
        }
        //dsMensagem
        if (ValueUtil.isEmpty(configNotificacaoSistema.dsMensagem)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_DSMENSAGEM);
        }
        //sqLUsuarioDestino
        if (ValueUtil.isEmpty(configNotificacaoSistema.sqLUsuarioDestino)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_SQLUSUARIODESTINO);
        }
        //dsSql
        if (ValueUtil.isEmpty(configNotificacaoSistema.dsSql)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_dsSql);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(configNotificacaoSistema.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(configNotificacaoSistema.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(configNotificacaoSistema.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_CDUSUARIO);
        }
        //dtAlteracao
        if (ValueUtil.isEmpty(configNotificacaoSistema.dtAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_DTALTERACAO);
        }
        //hrAlteracao
        if (ValueUtil.isEmpty(configNotificacaoSistema.hrAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONFIGNOTIFICACAOSISTEMA_LABEL_HRALTERACAO);
        }
*/
	}

	public Vector creatRows(String sql) throws SQLException {
		return ConfigNotificacaoDbxDao.getInstance().creatRows(sql);
	}

	public Vector findUsuario(String sql) throws SQLException {
		return ConfigNotificacaoDbxDao.getInstance().findColumnUsuarios(sql);
	}

	public boolean existeConfigNotificacao() {
		try {
			return count() > 0;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}

}