package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.NotificacaoPda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NotificacaoPdaDbxDao;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;

public class NotificacaoPdaService extends CrudService {

    private static NotificacaoPdaService instance;

    private NotificacaoPdaService() {
        //--
    }

    public static NotificacaoPdaService getInstance() {
        if (instance == null) {
            instance = new NotificacaoPdaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return NotificacaoPdaDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }

    private void insertOrUpdate(NotificacaoPda notificacaoPda) throws SQLException {
    	validate(notificacaoPda);
    	boolean isInsert = true;
    	try {
    		validateDuplicated(notificacaoPda);
		} catch (ValidationException ex) {
			isInsert = false;
		}
		//--
		setDadosAlteracao(notificacaoPda);
		//--
		if (isInsert) {
			getCrudDao().insert(notificacaoPda);
		} else {
			getCrudDao().update(notificacaoPda);
		}
    }

	public void createNotificacaoPdaInicioPedidoAndSend2Web(String cdSessao, int nuSequencia) throws SQLException {
    	if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
    		NotificacaoPda notificacaoPda = new NotificacaoPda();
    		notificacaoPda.cdNotificacao = NotificacaoPda.CD_NOTIFICACAOPDA_VISITA_EM_ANDAMENTO;
    		notificacaoPda.cdChave = notificacaoPda.getChave();
    		notificacaoPda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		notificacaoPda.dsNotificacao = SessionLavenderePda.getCliente().cdCliente;
    		notificacaoPda.dtNotificacao = DateUtil.getCurrentDate();
    		notificacaoPda.hrNotificacao = TimeUtil.getCurrentTimeHHMM();
    		notificacaoPda.nuSequencia = nuSequencia;
    		//--
    		insertOrUpdate(notificacaoPda);
    		//--
    		EnviaDadosThread.getInstance().montaDadosEnvioNotificaoBackGround(cdSessao, notificacaoPda.getRowKey());
    	}
    }

    public void createNotificacaoPdaReferenteAlteracaoFlRecebeEmail(String cdEmpresa, String cdCliente, String value) throws SQLException {
		NotificacaoPda notificacaoPda = new NotificacaoPda();
		notificacaoPda.cdNotificacao = NotificacaoPda.CD_NOTIFICACAOPDA_ALTERACAO_RECEBIMENTO_EMAIL;
		notificacaoPda.cdChave = notificacaoPda.getChaveFlagsCLiente(cdEmpresa, cdCliente, value);
		notificacaoPda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		notificacaoPda.dsNotificacao = SessionLavenderePda.getCliente().cdCliente;
		notificacaoPda.dtNotificacao = DateUtil.getCurrentDate();
		notificacaoPda.hrNotificacao = TimeUtil.getCurrentTimeHHMM();
		//--
		String flRecebeEmailOld = ClienteService.getInstance().getFlRecebeEmailOld(cdCliente);
		if (ValueUtil.isNotEmpty(flRecebeEmailOld) && !flRecebeEmailOld.equals(value)) {
			notificacaoPda.cdChave = notificacaoPda.getChaveFlagsCLiente(cdEmpresa, cdCliente, ValueUtil.VALOR_NAO.equals(value) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO);
			NotificacaoPda notificacaoPdaOld = (NotificacaoPda) findByRowKey(notificacaoPda.getRowKey());
			if (notificacaoPdaOld != null) {
				delete(notificacaoPda);
			} else {
				notificacaoPda.cdChave = notificacaoPda.getChaveFlagsCLiente(cdEmpresa, cdCliente, ValueUtil.VALOR_NAO.equals(value) ? ValueUtil.VALOR_NAO : ValueUtil.VALOR_SIM);
				insertOrUpdate(notificacaoPda);
			}
		}
    }

    public void createNotificacaoPdaReferenteAlteracaoFlRecebeSMS(String cdEmpresa, String cdCliente, String value) throws SQLException {
		NotificacaoPda notificacaoPda = new NotificacaoPda();
		notificacaoPda.cdNotificacao = NotificacaoPda.CD_NOTIFICACAOPDA_ALTERACAO_RECEBIMENTO_SMS;
		notificacaoPda.cdChave = notificacaoPda.getChaveFlagsCLiente(cdEmpresa, cdCliente, value);
		notificacaoPda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		notificacaoPda.dsNotificacao = SessionLavenderePda.getCliente().cdCliente;
		notificacaoPda.dtNotificacao = DateUtil.getCurrentDate();
		notificacaoPda.hrNotificacao = TimeUtil.getCurrentTimeHHMM();
		//--
		String flRecebeSMSOld = ClienteService.getInstance().getFlRecebeSMSOld(cdCliente);
		if (ValueUtil.isNotEmpty(flRecebeSMSOld) && !flRecebeSMSOld.equals(value)) {
			notificacaoPda.cdChave = notificacaoPda.getChaveFlagsCLiente(cdEmpresa, cdCliente, ValueUtil.VALOR_NAO.equals(value) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO);
			NotificacaoPda notificacaoPdaOld = (NotificacaoPda) findByRowKey(notificacaoPda.getRowKey());
			if (notificacaoPdaOld != null) {
				delete(notificacaoPda);
			} else {
				notificacaoPda.cdChave = notificacaoPda.getChaveFlagsCLiente(cdEmpresa, cdCliente, ValueUtil.VALOR_NAO.equals(value) ? ValueUtil.VALOR_NAO : ValueUtil.VALOR_SIM);
				insertOrUpdate(notificacaoPda);
			}
		}
    }

    public void restauraNotificacaoPdaAndSend2Web(String cdSessao) throws SQLException {
    	if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
    		NotificacaoPda notificacaoPda = restauraNotificacaoPda();
    		//--
    		EnviaDadosThread.getInstance().montaDadosEnvioNotificaoBackGround(cdSessao, notificacaoPda.getRowKey());
    	}
    }

    public NotificacaoPda restauraNotificacaoPda() throws SQLException {
    	NotificacaoPda notificacaoPda = new NotificacaoPda();
    	if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
    		notificacaoPda.cdNotificacao = NotificacaoPda.CD_NOTIFICACAOPDA_VISITA_EM_ANDAMENTO;
    		notificacaoPda.cdChave = notificacaoPda.getChave();
    		notificacaoPda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		//--
    		insertOrUpdate(notificacaoPda);
    	}
    	return notificacaoPda;
    }

    public void deleteAlteracoesTabelaNotificacoes() throws SQLException {
    	if (LavenderePdaConfig.usaAtualizacaoEscolhaClienteEnvioEmail) {
			NotificacaoPda notificacaoPda = new NotificacaoPda();
			notificacaoPda.cdNotificacao = NotificacaoPda.CD_NOTIFICACAOPDA_ALTERACAO_RECEBIMENTO_EMAIL;
			deleteAllByExample(notificacaoPda);
		}
    	if (LavenderePdaConfig.permiteIndicarRecebimentoSMSNoCadastroCliente) {
    		NotificacaoPda notificacaoPda = new NotificacaoPda();
    		notificacaoPda.cdNotificacao = NotificacaoPda.CD_NOTIFICACAOPDA_ALTERACAO_RECEBIMENTO_SMS;
    		deleteAllByExample(notificacaoPda);
    	}
    }

}