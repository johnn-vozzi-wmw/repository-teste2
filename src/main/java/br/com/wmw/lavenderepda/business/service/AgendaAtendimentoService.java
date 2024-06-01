package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaAtendimento;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AgendaAtendimentoDbxDao;
import br.com.wmw.lavenderepda.presentation.ui.CadTransferenciaAgendaWindow.AgendaUtil;
import totalcross.sys.Time;
import totalcross.util.Date;

public class AgendaAtendimentoService extends CrudService {

    private static AgendaAtendimentoService instance;
    
    private AgendaAtendimentoService() {
        //--
    }
    
    public static AgendaAtendimentoService getInstance() {
        if (instance == null) {
            instance = new AgendaAtendimentoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AgendaAtendimentoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void transferirAgendaVisita(AgendaUtil agendaUtil) throws SQLException {
    	AgendaVisita agendaVisita = agendaUtil.getAgendaVisitaTrasnf(); 
    	String cdMotivoTransferencia = agendaUtil.getDsMotivoAgenda();
    	String hrTransferencia = agendaUtil.getDsHoraAgenda();
    	Date dtTransferencia = agendaUtil.getDtAgenda();
    	Date dtLimiteReagendamento = agendaUtil.getDtLimiteReagendamento();
    	String dsObservacao = agendaUtil.getDsObs();
    	String cdUsuario = agendaUtil.getCdSupervisor();
    	Date dtAgendaAtual = agendaUtil.getDtAgendaA();
		validaReagendarTransferirAgenda(cdMotivoTransferencia, hrTransferencia, dtTransferencia, dtLimiteReagendamento, cdUsuario);
		AgendaAtendimento agendaAtendimento = new AgendaAtendimento();
		agendaAtendimento.cdEmpresa = agendaVisita.cdEmpresa;
		agendaAtendimento.dsAgendaAtendimento = MessageUtil.getMessage(Messages.TRANSFERENCIA_LABEL_DSAGENDA, SessionLavenderePda.usuarioPdaRep.cdUsuario);
		agendaAtendimento.cdCliente = agendaVisita.cdCliente;
		agendaAtendimento.cdRepresentante = agendaVisita.cdRepresentante;
		agendaAtendimento.nuFone = ClienteService.getInstance().getNuFoneCliente(agendaVisita.cdEmpresa, agendaVisita.cdRepresentante, agendaVisita.cdCliente);
		agendaAtendimento.dtAgendaAtendimento = dtTransferencia;
		agendaAtendimento.dtVencimento = dtTransferencia;
		agendaAtendimento.hrAgendaAtendimento = hrTransferencia.substring(0, 2) + ":" + hrTransferencia.substring(2, 4);
		agendaAtendimento.dsObservacao = dsObservacao;
		agendaAtendimento.cdUsuarioAgenda = cdUsuario;
		agendaAtendimento.cdMotivoTransferencia = cdMotivoTransferencia;
		int inc = 0;
		do {
			agendaAtendimento.cdAgendaAtendimento = StringUtil.getStringValue(ValueUtil.getLongValue(generateIdGlobal()) + inc);
			inc++;
		} while (findColumnByRowKey(agendaAtendimento.getRowKey(), "ROWKEY") != null);
		insert(agendaAtendimento);
		VisitaService.getInstance().insertVisitaByReagendamento(agendaVisita, cdMotivoTransferencia, dsObservacao, Visita.FL_VISITA_TRANSFERIDA, dtAgendaAtual);
	}
    
    public void validaReagendarTransferirAgenda(String cdMotivoTransferencia, String hrTransferencia, Date dtTransferencia, Date dtLimiteTransferencia, String cdUsuario) throws SQLException {
		if (ValueUtil.isEmpty(dtTransferencia)) {
			throw new ValidationException(Messages.VALIDACAO_CAMPO_DT);
		}
		if (ValueUtil.isEmpty(hrTransferencia)) {
			throw new ValidationException(Messages.VALIDACAO_CAMPO_HR);
		}
		if (ValueUtil.isEmpty(cdUsuario)) {
			throw new ValidationException(Messages.TRANSFERENCIA_VALIDACAO_USUARIO);
		}
		if (LavenderePdaConfig.usaMotivoReagendamentoTransferenciaAgenda && ValueUtil.isEmpty(cdMotivoTransferencia)) {
			throw new ValidationException(Messages.VALIDACAO_MOTIVO);
		}
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia != 0 && dtTransferencia.isAfter(dtLimiteTransferencia)) {
			throw new ValidationException(Messages.VALIDACAO_DTLIMITE_ULTRAPASSADA);
		}
		if (DateUtil.DATA_SEMANA_DOMINGO == DateUtil.getDayOfWeek(dtTransferencia) || DateUtil.DATA_SEMANA_SABADO == DateUtil.getDayOfWeek(dtTransferencia)) {
			throw new ValidationException(Messages.VALIDACAO_DT_FINAL_DE_SEMANA);
		}
		if (dtTransferencia.isBefore(DateUtil.getCurrentDate())) {
			throw new ValidationException(Messages.VALIDACAO_DATA_MENOR_ATUAL);
		}
		Time timeTransferencia = TimeUtil.getCurrentTime();
		timeTransferencia.hour = ValueUtil.getIntegerValue(hrTransferencia.substring(0, 2));
		timeTransferencia.minute = ValueUtil.getIntegerValue(hrTransferencia.substring(2, 4));
		if (dtTransferencia.equals(DateUtil.getCurrentDate()) && (TimeUtil.getTimeAsLong(timeTransferencia) - TimeUtil.getTimeAsLong(TimeUtil.getCurrentTime())) < 0) {
			throw new ValidationException(Messages.VALIDACAO_DATA_ATUAL_HR_MENOR);
		}
		if (FeriadoService.getInstance().isDtFeriado(dtTransferencia)) {
			throw new ValidationException(Messages.VALIDACAO_DT_FERIADO);
		}
	}
}