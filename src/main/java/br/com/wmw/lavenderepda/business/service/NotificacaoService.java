package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Notificacao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NotificacaoDbxDao;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

import java.sql.SQLException;

public class NotificacaoService extends CrudService {

	private static final Lock LOCK = new Lock();
	private static NotificacaoService instance;

	private NotificacaoService() {
		//--
	}

	public static NotificacaoService getInstance() {
		if (instance == null) {
			instance = new NotificacaoService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return NotificacaoDbxDao.getInstance();
	}

	@Override
	public String generateIdGlobal() throws SQLException {
		long proximoCodigo = ValueUtil.getLongValue(TimeUtil.getCurrentDateTimeDDDDDSSSSSMMM());
		while (proximoCodigo <= ultimoCodigo) {
			proximoCodigo = ValueUtil.getLongValue(TimeUtil.getCurrentDateTimeDDDDDSSSSSMMM());
		}
		ultimoCodigo = proximoCodigo;
		return StringUtil.getStringValue(proximoCodigo);
	}

	public synchronized String generateIntIdGlobalSynchronized() throws SQLException {
		synchronized (LOCK) {
			return generateIdGlobal();
		}
	}

	public int insertOrIgnore(Vector list, boolean controleChave) throws SQLException {
		return ((NotificacaoDbxDao) getCrudDao()).insertOrIgnore(list, controleChave);
	}

	public int marcarComoLido(Notificacao notificacaoFilter) throws SQLException {
		return ((NotificacaoDbxDao) getCrudDao()).marcarComoLido(notificacaoFilter);
	}

	@Override
	public void validate(BaseDomain domain) {
/*    
        NotificacaoSistema notificacaoSistema = (NotificacaoSistema) domain;

        //cdSistema
        if (ValueUtil.isEmpty(notificacaoSistema.cdSistema)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_CDSISTEMA);
        }
        //cdNotificacao
        if (ValueUtil.isEmpty(notificacaoSistema.cdNotificacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_CDNOTIFICACAO);
        }
        //cdConfigNotificacaoSistema
        if (ValueUtil.isEmpty(notificacaoSistema.cdConfigNotificacaoSistema)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_CDCONFIGNOTIFICACAO);
        }
        //cdTipoNotificacao
        if (ValueUtil.isEmpty(notificacaoSistema.cdTipoNotificacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_CDTIPONOTIFICACAO);
        }
        //vlChave
        if (ValueUtil.isEmpty(notificacaoSistema.vlChave)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_VLCHAVE);
        }
        //dsMensagem
        if (ValueUtil.isEmpty(notificacaoSistema.dsMensagem)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_DSMENSAGEM);
        }
        //cdUsuarioDestino
        if (ValueUtil.isEmpty(notificacaoSistema.cdUsuarioDestino)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_CDUSUARIODESTINO);
        }
        //flLido
        if (ValueUtil.isEmpty(notificacaoSistema.flLido)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_FLLIDO);
        }
        //flAtivo
        if (ValueUtil.isEmpty(notificacaoSistema.flAtivo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_FLATIVO);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(notificacaoSistema.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(notificacaoSistema.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(notificacaoSistema.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_CDUSUARIO);
        }
        //dtAlteracao
        if (ValueUtil.isEmpty(notificacaoSistema.dtAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_DTALTERACAO);
        }
        //hrAlteracao
        if (ValueUtil.isEmpty(notificacaoSistema.hrAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.NOTIFICACAOSISTEMA_LABEL_HRALTERACAO);
        }
*/
	}
	
	public boolean existeNotificacaoGerada() {
		try {
			return count() > 0;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}
	
}