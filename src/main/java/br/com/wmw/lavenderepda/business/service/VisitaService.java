package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.domain.ReagendaAgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import br.com.wmw.lavenderepda.business.domain.VisitaPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaFotoDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaPdbxDao;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.sys.Time;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class VisitaService extends CrudService {

    private static VisitaService instance;

    private VisitaService() {
        //--
    }

    public static VisitaService getInstance() {
        if (instance == null) {
            instance = new VisitaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VisitaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			return;
		}
        Visita visita = (Visita) domain;
        if (Visita.FL_VISITA_NAOPOSITIVADA.equals(visita.flVisitaPositivada)) {
	        if (ValueUtil.isEmpty(visita.cdMotivoRegistroVisita)) {
	            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
	        }
        }
        if (LavenderePdaConfig.isValorMinCaractersParaObservacaoMotivoVisita() && !visita.ignoraValidacaoObservacao) {
            if (ValueUtil.isEmpty(visita.dsObservacao) || visita.dsObservacao.length() < LavenderePdaConfig.nuMinCaracterObservacaoMotivoVisita) {
            	throw new ValidationException(MessageUtil.getMessage(Messages.VISITA_MSG_QTDADE_MIN_OBS, new String[]{StringUtil.getStringValueToInterface(LavenderePdaConfig.nuMinCaracterObservacaoMotivoVisita)}));
            }
        }
    }

    public void insertVisitaByPedido(Pedido pedido) throws SQLException {
    	if (LavenderePdaConfig.usaPositivacaoVisitaPorTipoPedido && !pedido.getTipoPedido().isPermitePositivacaoVisita()) {
    		return;
    	}
    	Cliente cliente = SessionLavenderePda.getCliente();
    	cliente = cliente == null ? pedido.getCliente() : cliente;
    	Visita visita = instanceNewVisita(cliente);
    	visita.flVisitaAgendada = pedido.insertVisita ? ValueUtil.VALOR_SIM : visita.flVisitaAgendada;
    	if(ValueUtil.isNotEmpty(pedido.getVisita().cdMotivoRegistroVisita)) {
    		visita.cdMotivoRegistroVisita = pedido.getVisita().cdMotivoRegistroVisita;
        	visita.dsObservacao = pedido.getVisita().dsObservacao;
    	}
    	visita.flVisitaPositivada = Visita.FL_VISITA_POSITIVADA;
    	visita.nuSequencia = pedido.nuSequenciaAgenda;
    	if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor && SessionLavenderePda.getAgenda() != null) {
    		visita.cdRepOriginal = SessionLavenderePda.getAgenda().cdRepOriginal;
    	}
    	visita.nuPedido = pedido.nuPedido;
    	if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
    		visita.flLiberadoSenha = SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada;
    	}
		validateDuplicated(visita);
		populateFlAgendada(visita);
    	getCrudDao().insert(visita);
    	insertVisitaFoto(visita);
    }
    
    protected Visita geraVisitaAutomaticaPorPedido(Pedido pedido, String flPedidoSemVisita, String flPedidoOutroCliente) throws SQLException {
    	Visita visita = instanceNewVisita(SessionLavenderePda.getCliente());
    	visita.nuSequencia = 0;
    	visita.flVisitaPositivada = Visita.FL_VISITA_POSITIVADA;
    	visita.flPedidoSemVisita = flPedidoSemVisita;
    	visita.flPedidoOutroCliente = flPedidoOutroCliente;
    	if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor && SessionLavenderePda.getAgenda() != null) {
    		visita.cdRepOriginal = SessionLavenderePda.getAgenda().cdRepOriginal;
    	}
    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
    		AgendaVisita agendaVisita = SessionLavenderePda.getAgenda() != null ? SessionLavenderePda.getAgenda() : AgendaVisitaService.getInstance().findAgendaVisitaPendente(pedido.cdRepresentante, pedido.cdCliente);
			visita.cdTipoAgenda = agendaVisita != null ? agendaVisita.cdTipoAgenda : null;
    	}
    	if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
    		visita.flLiberadoSenha = SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada;
    	}
    	validateDuplicated(visita);
    	populateFlAgendada(visita);
    	getCrudDao().insert(visita);
    	return visita;
    }

	private Visita instanceNewVisita(Cliente cliente) throws SQLException {
		Visita visita = new Visita();
    	setDataEHoraServidor(visita);
    	visita.cdVisita = generateIdGlobal();
    	visita.cdCliente = cliente.cdCliente;
    	visita.cdEmpresa = cliente.cdEmpresa;
    	visita.cdRepresentante = cliente.cdRepresentante;
    	visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	visita.dtVisita = DateUtil.getCurrentDate();
    	visita.hrVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.dtChegadaVisita = DateUtil.getCurrentDate();
    	visita.hrChegadaVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	AgendaVisita agendaVisita = SessionLavenderePda.getAgenda() != null ? SessionLavenderePda.getAgenda() : AgendaVisitaService.getInstance().findAgendaVisitaPendente(visita.cdRepresentante, visita.cdCliente);
    	visita.dtAgendaVisita = agendaVisita != null ? agendaVisita.dtAgendaAtual : null;
    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
    		visita.cdTipoAgenda = agendaVisita != null ? agendaVisita.cdTipoAgenda : null;
    	}
		return visita;
	}

	private void setDataEHoraServidor(Visita visita) throws SQLException {
		if (LavenderePdaConfig.registraFusoHorarioVisita) {
			String dataHoraServidorAtual = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor);
			try {
				UiUtil.showProcessingMessage();
				new LavendereTc2Web().obterDataHoraServidorAtualizada();
			} catch (Exception e) {
				throw new ValidationException(e.getMessage());
			} finally {
				UiUtil.unpopProcessingMessage();
			}
	    	String dataHoraServidorGerada = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor);
	    	Time horaServidor = new Time(ValueUtil.getLongValue(dataHoraServidorGerada));
			try {
				if (ValueUtil.valueEquals(dataHoraServidorAtual, dataHoraServidorGerada)) {
					horaServidor = getHoraServidorCalculada(visita.hrVisita);
				}
				visita.dtServidor = new Date(horaServidor);
			} catch (InvalidDateException e) {
				throw new ValidationException(Messages.VISITA_MSG_DATA_SERVIDOR_INVALIDA);
			}
			visita.hrServidor = horaServidor.toString();
		}
	}
	
	private void setTipoAgenda(Visita visita) throws SQLException {
    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita && ValueUtil.isEmpty(visita.cdTipoAgenda)) {
    		AgendaVisita agendaVisita = SessionLavenderePda.getAgenda() != null ? SessionLavenderePda.getAgenda() : AgendaVisitaService.getInstance().findAgendaVisitaPendente(visita.cdRepresentante, visita.cdCliente);
    		visita.cdTipoAgenda = agendaVisita != null ? agendaVisita.cdTipoAgenda : null;
    	}
	}
	
	private Time getHoraServidorCalculada(String hrVisita) throws SQLException {
		Time novaHoraServidor = new Time();
		if (ValueUtil.isNotEmpty(hrVisita)) {
			novaHoraServidor.hour = ValueUtil.getIntegerValue(hrVisita.substring(0, 2));
			novaHoraServidor.minute = ValueUtil.getIntegerValue(hrVisita.substring(3, 5));
			novaHoraServidor.second = ValueUtil.getIntegerValue(hrVisita.substring(6, 8));
			String dataHoraServidor = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor);
			String dataHoraPda = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraPda);
			Time horaServidor = new Time(ValueUtil.getLongValue(dataHoraServidor));
			Time horaPda = new Time(ValueUtil.getLongValue(dataHoraPda));
			try {
				novaHoraServidor.inc(0, 0, TimeUtil.getSecondsRealBetween(horaServidor, horaPda));
				return novaHoraServidor;
			} catch (Throwable ex) {
				throw new ValidationException(Messages.CONFIGINTERNO_MSG_HORA_SERVIDOR_INVALIDA);
			}
		}
		return novaHoraServidor;
	}

    public void insert(BaseDomain domain) throws SQLException  {
    	Visita visita = (Visita) domain;
    	setDataEHoraServidor(visita);
    	setTipoAgenda(visita);
    	populateFlAgendada(visita);
    	super.insert(domain);
        if (LavenderePdaConfig.usaVisitaFoto) {
        	VisitaFotoService.getInstance().insertOrUpdateVisitaFotoList(visita);
        }
    }
    
    public void insert(BaseDomain domain, boolean isSetDadosAlteracao) throws SQLException  {
    	Visita visita = (Visita) domain;
    	setDataEHoraServidor(visita);
    	setTipoAgenda(visita);
    	populateFlAgendada(visita);
    	super.insert(visita);
    	if (!isSetDadosAlteracao) {
    		VisitaService.getInstance().updateColumn(visita.getRowKey(), "FLTIPOALTERACAO", BaseDomain.FLTIPOALTERACAO_ORIGINAL, totalcross.sql.Types.VARCHAR);
    		visita.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		}
    	if (LavenderePdaConfig.usaVisitaFoto) {
    		VisitaFotoService.getInstance().insertOrUpdateVisitaFotoList(visita);
    	}
    }

    public void update(BaseDomain domain) throws SQLException {
    	super.update(domain);
    	if (LavenderePdaConfig.usaVisitaFoto) {
    		Visita visita = (Visita) domain;
    		VisitaFotoService.getInstance().insertOrUpdateVisitaFotoList(visita);
    	}
    }
    
    @Override
    public void update(BaseDomain domain, boolean isSetDadosAlteracao) throws SQLException {
    	super.update(domain, isSetDadosAlteracao);
    	if (LavenderePdaConfig.usaVisitaFoto) {
    		Visita visita = (Visita) domain;
    		VisitaFotoService.getInstance().insertOrUpdateVisitaFotoList(visita);
    	}
    }

    public void insertVisitaFoto(Visita visita) throws SQLException {
		if (LavenderePdaConfig.isUsaAgendaDeVisitas() || LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			for (int i = 0; i < visita.getVisitaFotoList().size(); i++) {
				VisitaFoto visitaFoto =  (VisitaFoto) visita.getVisitaFotoList().items[i];
				visitaFoto.cdEmpresa = visita.cdEmpresa;
				visitaFoto.cdRepresentante = visita.cdRepresentante;
				visitaFoto.flOrigemVisita = visita.flOrigemVisita;
				visitaFoto.cdVisita = visita.cdVisita;
			}
			VisitaFotoService.getInstance().insertOrUpdateVisitaFotoList(visita);
		}
	}

    public void updateByMotivoNaoPositivado(BaseDomain domain) throws SQLException {
    	Visita visita = (Visita) domain;
    	//-- Busca Registro de visita atual
    	Visita visitaFilter = new Visita();
        visitaFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        visitaFilter.cdEmpresa = SessionLavenderePda.getCliente().cdEmpresa;
        visitaFilter.cdRepresentante = SessionLavenderePda.getCliente().cdRepresentante;
        visitaFilter = (Visita)(findAllByExample(visitaFilter)).items[0];
        //-- Atualiza campo do registro de visita encontrado
        visitaFilter.cdMotivoRegistroVisita = visita.cdMotivoRegistroVisita;
        visitaFilter.dsObservacao = visita.dsObservacao;
        //-- Update
        update(visitaFilter);
    }


    public Vector findVisitasAntigas(Date currenteDate) throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		visitaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		visitaFilter.dtVisita = AgendaVisitaService.getInstance().getLastValidDay(currenteDate);
		return ((VisitaPdbxDao)getCrudDao()).findVisitasAntigas(visitaFilter);

	}

	public void deletaVisitasAntigas() {
		try {
			Date currenteDate = DateUtil.getCurrentDate();
			currenteDate.advance(-10);
			Visita visitaFilter = new Visita();
			visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			visitaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			visitaFilter.dtVisita = currenteDate;
			if (LavenderePdaConfig.usaVisitaFoto) {
				try {
					Vector visitasAntigasList = ((VisitaPdbxDao)getCrudDao()).findVisitasAntigas(visitaFilter);                                
					for (int i = 0; i < visitasAntigasList.size(); i++) {
						Visita visitaAntiga = (Visita) visitasAntigasList.items[i];	
						VisitaFotoService.getInstance().deleteAllVisitaFotoByVisita(visitaAntiga);
					}
				} catch (Throwable e) {
					//Não faz nada
				}
			}
			deletaVisitaByData(visitaFilter);
			if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
				try {
					visitaFilter.dtChegadaVisita = currenteDate;
					Vector visitasAntigasList = ((VisitaPdbxDao)getCrudDao()).findVisitasAntigas(visitaFilter);                                
					for (int i = 0; i < visitasAntigasList.size(); i++) {
						Visita visitaAntiga = (Visita) visitasAntigasList.items[i];
						delete(visitaAntiga);
					}
				} catch (Throwable e) {
					//Não faz nada
				}
			}
		} catch (Throwable e) { /*Não faz nada. Apenas não exclui, pois nao achou nenhum registro*/	}
	}
	
	@Override
	public void delete(BaseDomain domain) throws java.sql.SQLException {
		super.delete(domain);
		if (LavenderePdaConfig.usaVisitaFoto) {
			VisitaFotoService.getInstance().deleteAllVisitaFotoByVisita((Visita)domain);
		}
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			VisitaPedidoService.getInstance().deleteAllVisitaPedidoByVisita((Visita)domain);
		}
	}

	public void deletaVisitaByData(Visita visita) {
		((VisitaPdbxDao)getCrudDao()).deletaVisitaByData(visita);
	}
	
    public void deleteByPedido(Pedido pedido) throws SQLException {
    	Visita visitaExample = new Visita();
    	visitaExample.cdCliente = pedido.cdCliente;
    	visitaExample.cdEmpresa = pedido.cdEmpresa;
    	visitaExample.cdRepresentante = pedido.cdRepresentante;
    	visitaExample.nuPedido = pedido.nuPedido;
    	if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
    		visitaExample.nuSequencia = pedido.nuSequenciaAgenda;
    	}
    	Vector visitaList = findAllByExample(visitaExample);
    	for (int i = 0; i < visitaList.size(); i++) {
    		Visita visita = (Visita)visitaList.items[i];
    		delete(visita);
		}
    }

    public void reabreVisita(Visita visita) throws SQLException {
    	visita.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		getCrudDao().update(visita);
    }
    
    public Visita findVisitaManualForClienteToday(String cdCliente) throws SQLException {
        Visita visita = new Visita();
        visita.cdCliente = cdCliente;
        visita.dtVisita = new Date();
        visita.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
        Vector visitaHojeList = findAllByExample(visita);
        if (visitaHojeList.size() > 0) {
        	return (Visita)visitaHojeList.items[0];
        }
        return null;
    }

    public Vector findVisitasDistinct(Date diaAgendaVisita, String cdRepresentante) throws SQLException {
    	Visita visitaFilter = new Visita();
    	visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	visitaFilter.cdRepresentante = cdRepresentante;
    	visitaFilter.dtVisita = diaAgendaVisita;
    	if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
    		visitaFilter.dtChegadaVisita = diaAgendaVisita;
    		if (ValueUtil.isNotEmpty(diaAgendaVisita) && !diaAgendaVisita.equals(DateUtil.getCurrentDate())) {
    			visitaFilter.dtChegadaVisita = DateUtil.getCurrentDate();
    		}
    	}
    	if (DateUtil.getCurrentDate().isBefore(diaAgendaVisita)) {
    		visitaFilter.dtAgendaVisita = diaAgendaVisita;
    		if (LavenderePdaConfig.isNaoConsideraRegistroVisitaManualTolerancia() || LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia()) {
    			visitaFilter.filterDtVisita = DateUtil.getCurrentDate();
    		}
    	}
    	Vector visitasList = findAllByExample(visitaFilter);
    	//--
    	Vector listReturn = new Vector();
		int size = visitasList.size();
        for (int i = 0; i < size; i++) {
    		boolean insert = true;
    		Visita visita = (Visita) visitasList.items[i];
    		int sizeReturn = listReturn.size();
    		for (int j = 0; j < sizeReturn; j++) {
    			Visita visitaTemp = (Visita)listReturn.items[j];
    			if (visitaTemp != null) {
    				boolean clienteEncontrado = false;
    				if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
    					if (ValueUtil.valueEquals(visita.cdCliente, visitaTemp.cdCliente) && (visita.nuSequencia == visitaTemp.nuSequencia)) {
	    					clienteEncontrado = true;
	    				}
    				} else {
	    				if (ValueUtil.valueEquals(visita.cdCliente, visitaTemp.cdCliente)) {
	    					clienteEncontrado = true;
	    				}
    				}
    				if (clienteEncontrado) {
	    				if (Visita.FL_VISITA_NAOPOSITIVADA.equals(visitaTemp.flVisitaPositivada)) {
							listReturn.removeElement(visitaTemp);
						} else {
							insert = false;
						}
    				}
    			}
    		}
    		if (insert) {
    			listReturn.addElement(visita);
    		}
    	}
        visitasList = null;
    	return listReturn;
    }

    public Visita findVisitaByPedido(Pedido pedido) throws SQLException {
    	Visita visitaFilter = new Visita();
    	visitaFilter.cdEmpresa = pedido.cdEmpresa;
    	visitaFilter.cdRepresentante = pedido.cdRepresentante;
    	visitaFilter.cdCliente = pedido.cdCliente;
    	visitaFilter.nuPedido = pedido.nuPedido;
    	visitaFilter.dtVisita = pedido.dtEmissao;
    	if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
    		visitaFilter.nuSequencia = pedido.nuSequenciaAgenda;
    	}
    	Vector visitaList = VisitaService.getInstance().findAllByExample(visitaFilter);
    	Visita visita = new Visita();
    	if (visitaList.size() != 0) {
    		visita = (Visita) visitaList.items[0];
    	}
    	if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita && ValueUtil.isEmpty(visita.cdVisita)) {
			VisitaPedido visitaPedidoFilter = new VisitaPedido();
			visitaPedidoFilter.cdEmpresa = pedido.cdEmpresa;
			visitaPedidoFilter.cdRepresentante = pedido.cdRepresentante;
			visitaPedidoFilter.nuPedido = pedido.nuPedido;
			Vector visitaPedidoFilterList = VisitaPedidoService.getInstance().findAllByExample(visitaPedidoFilter);
			if (ValueUtil.isNotEmpty(visitaPedidoFilterList)) {
				VisitaPedido visitaPedido = (VisitaPedido) visitaPedidoFilterList.items[0]; 
				visitaFilter.cdVisita = visitaPedido.cdVisita;
				visitaFilter.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
				visita = (Visita) VisitaService.getInstance().findByRowKey(visitaFilter.getRowKey());
			}
		}
    	if (LavenderePdaConfig.usaVisitaFoto && visita != null && ValueUtil.isNotEmpty(visita.cdVisita)) {
    		visita.setVisitaFotoList(VisitaFotoService.getInstance().findAllVisitaFotoByVisita(visita));
    	}
    	return visita;
    }

	public void fechaVisita(Visita visita) throws SQLException {
		fechaVisita(visita, true);
	}
	
	public void fechaVisita(Visita visita, boolean updateVisitaPedido) throws SQLException {
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita && visita.isPedidoOutroCliente() && SessionLavenderePda.visitaAndamento != null && !ValueUtil.valueEquals(SessionLavenderePda.visitaAndamento.cdCliente, visita.cdCliente)) {
			visita.dtSaidaVisita = DateUtil.getCurrentDate();
			visita.hrSaidaVisita = TimeUtil.getCurrentTimeHHMMSS();
		}
		visita.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
		getCrudDao().update(visita);
		if (updateVisitaPedido) {
			VisitaPedidoService.getInstance().updateVisitasPedidoParaEnvio(visita);
		}
		if (LavenderePdaConfig.usaVisitaFoto) {
			visita.setVisitaFotoList(VisitaFotoService.getInstance().findAllVisitaFotoByVisita(visita));
			VisitaFotoService.getInstance().insertOrUpdateVisitaFotoList(visita);
			VisitaFotoDao.getInstance().updateFotosVisitaParaEnvio(visita.cdEmpresa, visita.cdRepresentante, visita.flOrigemVisita, visita.cdVisita, visita.flTipoAlteracao);
		}
	}
	
	public void enviaDadosBackgroundVisita(final Visita visita) throws SQLException {
		if (LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema()) {
    		PontoGpsService.getInstance().startColetaGpsPontoEspecificoSistema();
    	}
    	if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
    		String cdSessao = PedidoService.getInstance().generateIdGlobal();
    		EnviaDadosThread.getInstance().enviaVisita(cdSessao, visita);
    	}
    	this.fechaVisita(visita);
	}
	
	public Visita findVisitaEmAndamento() throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		visitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		visitaFilter.filterHrChegadaVisitaNotNull = true;
		visitaFilter.filterHrSaidaVisitaNull = true;
		visitaFilter.filterNuPedidoNull = true;
		Vector visitaList = VisitaService.getInstance().findAllByExample(visitaFilter);
		return visitaList.size() > 0 ? (Visita) visitaList.items[0] : null;
	}
	
	public Visita findLastRegistroSaidaDoCliente(String cdCliente) throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		visitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		visitaFilter.filterHrChegadaVisitaNotNull = true;
		visitaFilter.filterNuPedidoNull = true;
		visitaFilter.cdCliente = cdCliente;
		visitaFilter.sortAtributte = "DTSAIDAVISITA, HRSAIDAVISITA";
		visitaFilter.sortAsc = "N, N";
		Vector lastRegistroSaidaList = findAllByExample(visitaFilter);
		
		return lastRegistroSaidaList.size() > 0 ? (Visita) lastRegistroSaidaList.items[0] : new Visita();
	}
	
	public Visita findLastRegistroChegadaRepresentante() throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		visitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		visitaFilter.filterHrChegadaVisitaNotNull = true;
		visitaFilter.filterNuPedidoNull = true;
		visitaFilter.sortAtributte = "DTCHEGADAVISITA, HRCHEGADAVISITA";
		visitaFilter.sortAsc = "N, N";
		Vector lastRegistroChegadaList = findAllByExample(visitaFilter);
		return lastRegistroChegadaList.size() > 0 ? (Visita) lastRegistroChegadaList.items[0] : new Visita();
	}
	
	public boolean permiteAtualizarRegistroSaida(Visita ultimoRegistroChegadaDoRepresentante, Visita ultimoRegistroSaidaDoCliente) {
		if (ultimoRegistroChegadaDoRepresentante != null && ultimoRegistroSaidaDoCliente != null) {
			if (ultimoRegistroChegadaDoRepresentante.dtChegadaVisita.isAfter(ultimoRegistroSaidaDoCliente.dtSaidaVisita)) {
				return false;
			} else {
				return ultimoRegistroChegadaDoRepresentante.hrChegadaVisita != null && ultimoRegistroSaidaDoCliente.hrSaidaVisita != null && ultimoRegistroChegadaDoRepresentante.hrChegadaVisita.compareTo(ultimoRegistroSaidaDoCliente.hrSaidaVisita) <= 0 && !ultimoRegistroChegadaDoRepresentante.isVisitaReagendada();
			}
		}
		return false;
	}
	
	public void atualizaVisitaAposEnvioServidor() throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		visitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		visitaFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		visitaFilter.filterFlTipoAlteracao = true;
		Vector visitaList = VisitaService.getInstance().findAllByExample(visitaFilter);
		int size = visitaList.size();
		for (int i = 0; i < size; i++) {
			Visita visita = (Visita) visitaList.items[i];
			if (visita != null) {
				VisitaPdbxDao.getInstance().updateVisitaTramsmitidaComSucesso(visita);
			}
		}
	}
	
	public boolean isExisteVisitaPositivadaParaClienteProspect() throws SQLException {
		Visita visitaFilter = new Visita();
        visitaFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
        visitaFilter.cdEmpresa = SessionLavenderePda.getCliente().cdEmpresa;
        visitaFilter.cdRepresentante = SessionLavenderePda.getCliente().cdRepresentante;
        visitaFilter.flVisitaPositivada = Visita.FL_VISITA_POSITIVADA;
		return ValueUtil.isNotEmpty(findAllByExample(visitaFilter));
	}

	public void insertVisitaPositivadaParaClienteProspects(String cdClienteOriginal, int nuSequenciaVisitaCliente) throws SQLException {
    	if (LavenderePdaConfig.isUsaRegistroManualDeVisitaSemAgenda() || LavenderePdaConfig.isUsaAgendaDeVisitas()) {
	    	Visita visita = instanceNewVisita(SessionLavenderePda.getCliente());
			visita.cdCliente = cdClienteOriginal;
	    	visita.flVisitaPositivada = Visita.FL_VISITA_POSITIVADA;
	    	visita.nuSequencia = nuSequenciaVisitaCliente;
	    	visita.dtSaidaVisita = DateUtil.getCurrentDate();
	    	visita.hrSaidaVisita = TimeUtil.getCurrentTimeHHMMSS();
	    	if (SessionLavenderePda.getAgenda() != null) {
	    		visita.dtAgendaVisita = SessionLavenderePda.getAgenda().dtAgendaAtual;
	    		visita.dtVisita = SessionLavenderePda.getAgenda().dtAgendaAtual;
			}
	    	visita.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
			validateDuplicated(visita);
	    	getCrudDao().insert(visita);
    	}
    }
	
	public void deleteVisitaByNovoCliente(NovoCliente domain) throws SQLException {
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && ValueUtil.isNotEmpty(domain.cdClienteOriginal)) {
			Visita visita = new Visita();
			visita.cdEmpresa = domain.cdEmpresa;
			visita.cdCliente = domain.cdClienteOriginal;
			visita.cdRepresentante = domain.cdRepresentante;
			deleteAllByExample(visita);
		}
	}
	
	public void insertVisitaByReagendamento(AgendaVisita agendaVisita, String cdMotivoReagendamento, String dsObservacao, String flTipoVisita, Date dtAgendaAtual) throws SQLException {
		final Date dtRegistro = DateUtil.getCurrentDate();
		final String hrRegistro = TimeUtil.getCurrentTimeHHMMSS();
		Visita visita = new Visita();
		visita.cdEmpresa = agendaVisita.cdEmpresa;
		visita.cdRepresentante = agendaVisita.cdRepresentante;
		visita.cdCliente = agendaVisita.cdCliente;
		visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
		visita.dtVisita = agendaVisita.dtAgendaAtual;
		visita.dtChegadaVisita = dtRegistro;
		visita.hrChegadaVisita = hrRegistro;
		visita.dtSaidaVisita = dtRegistro;
		visita.hrSaidaVisita = hrRegistro;
		visita.dtAgendaVisita = dtAgendaAtual;
		if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
			visita.cdTipoAgenda = agendaVisita.cdTipoAgenda;
		}
		visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
			visita.cdRepOriginal = agendaVisita.cdRepOriginal;
		}
		if (Visita.FL_VISITA_REAGENDADA.equals(flTipoVisita)) {
			visita.flVisitaReagendada = ValueUtil.VALOR_SIM;
			visita.flVisitaAgendada = ValueUtil.VALOR_SIM;
		} else if (Visita.FL_VISITA_TRANSFERIDA.equals(flTipoVisita)) {
			visita.flVisitaTransferida = ValueUtil.VALOR_SIM;
			visita.flVisitaAgendada = ValueUtil.VALOR_SIM;
		}
		visita.cdMotivoReagendamento = cdMotivoReagendamento;
		visita.dsObservacao = dsObservacao;
		visita.nuSequencia = agendaVisita.nuSequencia;
		visita.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;

		populateFlAgendada(visita);
		if (retornaVisitaJaExistente(visita) == null) {
			visita.cdVisita = generateIdGlobal();
			getCrudDao().insert(visita);
		}
	}
	
	public void insertVisitaByReagendaAgendaVisita(ReagendaAgendaVisita reagenda, String flTipoVisita) throws SQLException {
		final Date dtRegistro = DateUtil.getCurrentDate();
		final String hrRegistro = TimeUtil.getCurrentTimeHHMMSS();
		Visita visita = new Visita();
		visita.cdEmpresa = reagenda.cdEmpresa;
		visita.cdRepresentante = reagenda.cdRepresentante;
		visita.cdCliente = reagenda.cdCliente;
		visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
		visita.dtVisita = DateUtil.getDateValue(reagenda.dtAgendaOriginal);
		visita.dtChegadaVisita = dtRegistro;
		visita.hrChegadaVisita = hrRegistro;
		visita.dtSaidaVisita = dtRegistro;
		visita.hrSaidaVisita = hrRegistro;
		visita.dtAgendaVisita = DateUtil.getDateValue(reagenda.dtAgendaOriginal);
		if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
			visita.cdTipoAgenda = flTipoVisita;
		}
		visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
			visita.cdRepOriginal = reagenda.cdRepresentante;
		}
		if (Visita.FL_VISITA_REAGENDADA.equals(flTipoVisita)) {
			visita.flVisitaReagendada = ValueUtil.VALOR_SIM;
			visita.flVisitaAgendada = ValueUtil.VALOR_SIM;
		} else if (Visita.FL_VISITA_TRANSFERIDA.equals(flTipoVisita)) {
			visita.flVisitaTransferida = ValueUtil.VALOR_SIM;
			visita.flVisitaAgendada = ValueUtil.VALOR_SIM;
		}
		visita.cdMotivoReagendamento = reagenda.cdMotivoReagendamento;
		visita.dsObservacao = reagenda.dsObservacao;
		visita.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
		visita.nuSequencia = reagenda.nuSequencia;
		if (retornaVisitaJaExistente(visita) == null) {
			visita.cdVisita = generateIdGlobal();
			getCrudDao().insert(visita);
		}
	}

	private Visita retornaVisitaJaExistente(Visita visita) throws SQLException {
		Vector list = findAllByExample(visita);
		if (list.size() > 0) {
			return (Visita) list.elementAt(0);
		}
		return null;
	}

	public void atualizaVisitaEmAndamento(Pedido pedido) {
		if (SessionLavenderePda.visitaAndamento != null && ValueUtil.valueEquals(SessionLavenderePda.visitaAndamento.cdCliente, pedido.cdCliente)) {
			try {
				update(SessionLavenderePda.visitaAndamento, false);
			} catch (Throwable ex) {
				ExceptionUtil.handle(ex);
			}
		}
	}
	
	public boolean verificaToleranciaVisitaForaAgenda(Cliente cliente) throws SQLException {
		return getQtClientesAtendidosNoDia(cliente) - getNuVisitasManual(cliente) < getNuMaxVisitasForaAgendaPermitida(cliente);
	}
	
	public boolean isVisitasForaAgendaDoDiaDentroTolerancia(Cliente cliente) throws SQLException {
		return getNuVisitasForaAgendaDoDia(cliente) - getNuVisitasManual(null) <= getNuMaxVisitasForaAgendaPermitida(cliente);
	}

    public double getNuMaxVisitasForaAgendaPermitida(Cliente cliente) throws SQLException {
    	return ValueUtil.round((getQtAgendasDoDia(cliente) / 100) * LavenderePdaConfig.getVlPctToleranciaVisitasForaAgenda(), 0);
    }    

    private int getNuVisitasForaAgendaDoDia(Cliente cliente) throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdEmpresa = ValueUtil.isNotEmpty(cliente.cdEmpresa) ? cliente.cdEmpresa : SessionLavenderePda.cdEmpresa;
		visitaFilter.cdRepresentante = ValueUtil.isNotEmpty(cliente.cdRepresentante) ? cliente.cdRepresentante : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		visitaFilter.nuSequencia = 0;
		visitaFilter.dtVisita = DateUtil.getCurrentDate();
		visitaFilter.dtChegadaVisita = visitaFilter.dtVisita;
		return countByExample(visitaFilter) + 1;
    }
    
    private double getQtAgendasDoDia(Cliente cliente) throws SQLException {
		AgendaVisita agendaVisitaFilter = new AgendaVisita();
		agendaVisitaFilter.cdEmpresa = ValueUtil.isNotEmpty(cliente.cdEmpresa) ? cliente.cdEmpresa : SessionLavenderePda.cdEmpresa;
		agendaVisitaFilter.cdRepresentante = ValueUtil.isNotEmpty(cliente.cdRepresentante) ? cliente.cdRepresentante : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector agendaListTemp = AgendaVisitaService.getInstance().findAgendasVisitas(agendaVisitaFilter, DateUtil.getCurrentDate(), -1, false, null, null);
		return agendaListTemp.size();
    }

	private int getQtClientesAtendidosNoDia(Cliente cliente) throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdEmpresa = ValueUtil.isNotEmpty(cliente.cdEmpresa) ? cliente.cdEmpresa : SessionLavenderePda.cdEmpresa;
		visitaFilter.cdRepresentante = ValueUtil.isNotEmpty(cliente.cdRepresentante) ? cliente.cdRepresentante : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		visitaFilter.nuSequencia = 0;
		visitaFilter.dtVisita = DateUtil.getCurrentDate();
		visitaFilter.dtChegadaVisita = visitaFilter.dtVisita;
		Vector clientesVisitadosNoDiaList = findDistinctClientesVisitadosNoDia(visitaFilter, cliente.cdCliente);
		return clientesVisitadosNoDiaList.size();
	}
	
	private Vector findDistinctClientesVisitadosNoDia(Visita visitaFilter, String cdCliente) throws SQLException {
		Vector visitaListTemp = findAllByExample(visitaFilter);
		Hashtable distinctClientesVisitadosHash = new Hashtable(0);
    	if (ValueUtil.isNotEmpty(visitaListTemp)) {
    		int size = visitaListTemp.size();
    		for (int i = 0; i < size; i++) {
    			Visita visita = (Visita)visitaListTemp.items[i];
    			if (!AgendaVisitaService.getInstance().isHasAgendaVisitaNoDia(visita.cdCliente) && !ValueUtil.valueEquals(visita.cdCliente, cdCliente)) {
    				if (distinctClientesVisitadosHash.get(visita.cdCliente) == null) {
    					distinctClientesVisitadosHash.put(visita.cdCliente, visita);
    				}
				}
    		}
    	}
    	return distinctClientesVisitadosHash.getValues();
	}
	
	public void atualizaClienteDaVisita(Visita visita) throws SQLException {
		visita.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		VisitaService.getInstance().updateColumn(visita.getRowKey(), "CDCLIENTE", visita.cdCliente, totalcross.sql.Types.VARCHAR);
		SessionLavenderePda.reloadVisitaAndamento();
	}
	
	protected void reagendaVisitaEmAndamento(AgendaVisita agendaVisita, String cdMotivoReagendamento, String dsObservacao) throws SQLException {
		Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
		visitaEmAndamento.dtSaidaVisita = DateUtil.getCurrentDate();
		visitaEmAndamento.hrSaidaVisita = TimeUtil.getCurrentTimeHHMMSS();
		visitaEmAndamento.cdMotivoReagendamento = cdMotivoReagendamento;
		visitaEmAndamento.dsObservacao = dsObservacao;
		visitaEmAndamento.flVisitaReagendada = ValueUtil.VALOR_SIM;
		update(visitaEmAndamento);
		SessionLavenderePda.visitaAndamento = null;
	}
	
	public boolean isVisitaEmAndamento(Visita visitaEmAndamento) {
		return visitaEmAndamento != null && !visitaEmAndamento.isVisitaEnviadaServidor();
	}
	
	public void desfazVisitaEmAndamento(Visita visitaEmAndamento) throws SQLException {
		delete(visitaEmAndamento);
		SessionLavenderePda.visitaAndamento = null;
		if (LavenderePdaConfig.usaVisitaFoto) {
    		VisitaFotoService.getInstance().deleteAllVisitaFotoByVisita(visitaEmAndamento);
    	}
	}
	
	public int getNuVisitasManual(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.isNaoConsideraRegistroVisitaManualTolerancia()) {
			Visita visita = new Visita();
			visita.cdEmpresa = SessionLavenderePda.cdEmpresa;
			visita.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		    visita.dtVisita = DateUtil.getCurrentDate();
			visita.dtChegadaVisita = visita.dtVisita;
		    visita.flVisitaManual = ValueUtil.VALOR_SIM;
		    if (LavenderePdaConfig.usaToleranciaVisitasForaDeAgendaPorClienteVisitado) {
		    	Vector list = findDistinctClientesVisitadosNoDia(visita, cliente.cdCliente);
		    	return list.size();
		    }
		    return countByExample(visita);
		} else if (LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia()) {
			Visita visita = new Visita();
			visita.cdEmpresa = SessionLavenderePda.cdEmpresa;
			visita.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			visita.nuSeqFilter = "0";
			visita.dtVisita = AgendaVisitaService.getInstance().getLastValidDay(DateUtil.getCurrentDate());
			visita.dtChegadaVisita = DateUtil.getCurrentDate();
			visita.flVisitaManual = ValueUtil.VALOR_SIM;
			if (LavenderePdaConfig.usaToleranciaVisitasForaDeAgendaPorClienteVisitado) {
		    	Vector list = findDistinctClientesVisitadosNoDia(visita, cliente.cdCliente);
		    	return list.size();
		    }
			return countByExample(visita);
		}
		return 0;
	}
	
	@Override
	public String generateIdGlobal() throws SQLException {
		int proximoCodigo = ValueUtil.getIntegerValue(super.generateIdGlobal());
		while (proximoCodigo <= ultimoCodigo) {
			proximoCodigo += 1;
		}
		ultimoCodigo = proximoCodigo;
		return StringUtil.getStringValue(proximoCodigo);
	}
	
	private void populateFlAgendada(Visita visita) throws SQLException {
		if (!LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			if (findAgendaVisitaPendente(visita) != null) {
				visita.flVisitaAgendada = ValueUtil.VALOR_SIM;
			} else {
				visita.flVisitaAgendada = ValueUtil.VALOR_NAO;
			}
		}
		if (ValueUtil.VALOR_NAO.equals(visita.flVisitaAgendada)) {
    		visita.dtAgendaVisita = null;
    		visita.nuSequencia = 0;
    	}
	}

	private AgendaVisita findAgendaVisitaPendente(Visita visita) throws SQLException {
		Date dtFilter = ValueUtil.VALOR_SIM.equals(visita.flVisitaAgendada) ? visita.dtAgendaVisita : DateUtil.getCurrentDate();
		return AgendaVisitaService.getInstance().findAgendaVisitaPendente(visita.cdRepresentante, visita.cdCliente, dtFilter, false, dtFilter, dtFilter, LavenderePdaConfig.isContabVariosPedidoRota());
	}
	
	public boolean validaPresencaClienteCoordenada(Cliente cliente) {
		if (cliente == null) return true;
		if (!LavenderePdaConfig.bloquearRegistroChegadaRepNaoPresente || VmUtil.isSimulador()) return true;
		GpsData gpsData = GpsService.getInstance().forceReadData();
		double distancia = PontoGpsService.getInstance().calculaDistancia(gpsData.latitude, gpsData.longitude, cliente.cdLatitude, cliente.cdLongitude) * 1000;
		
		return distancia <= LavenderePdaConfig.nuMetrosToleranciaRepresentantePresenteCliente;
	}
	
	public void geraVisitaNovoCliente(NovoCliente novoCli) throws SQLException {
		Visita visita = new Visita();
    	setDataEHoraServidor(visita);
    	visita.cdCliente = ValueUtil.isEmpty(novoCli.cdClienteOriginal)? novoCli.cdNovoCliente : novoCli.cdClienteOriginal; 
    	visita.cdEmpresa = novoCli.cdEmpresa;
    	visita.cdRepresentante = novoCli.cdRepresentante;
    	visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	visita.dtVisita = DateUtil.getCurrentDate();
    	visita.hrVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.dtChegadaVisita = DateUtil.getCurrentDate();
    	visita.hrChegadaVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.dtSaidaVisita = visita.dtChegadaVisita;
    	visita.hrSaidaVisita = visita.hrChegadaVisita;
    	visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	visita.flNovoClienteProspect = ValueUtil.VALOR_SIM;
    	if (novoCli.cdLatitude != 0 && novoCli.cdLongitude != 0) {
    		visita.flLocalizacaoChegada = Visita.FLLOCALIZACAO_PRESENTE;
    		visita.flLocalizacaoSaida = Visita.FLLOCALIZACAO_PRESENTE;
    	}
    	if (retornaVisitaJaExistente(visita) == null) {
			visita.cdVisita = generateIdGlobal();
			visita.flTipoAlteracao = Visita.FLTIPOALTERACAO_INSERIDO;
			getCrudDao().insert(visita);
		}
	}
	
	public void geraVisitaNovoProspect(Prospect novoProspect) throws SQLException {
		Visita visita = new Visita();
    	setDataEHoraServidor(visita);
    	visita.cdCliente = novoProspect.cdProspect; 
    	visita.cdEmpresa = novoProspect.cdEmpresa;
    	visita.cdRepresentante = novoProspect.cdRepresentante;
    	visita.flOrigemVisita = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	visita.dtVisita = DateUtil.getCurrentDate();
    	visita.hrVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.dtChegadaVisita = DateUtil.getCurrentDate();
    	visita.hrChegadaVisita = TimeUtil.getCurrentTimeHHMMSS();
    	visita.dtSaidaVisita = visita.dtChegadaVisita;
    	visita.hrSaidaVisita = visita.hrChegadaVisita;
    	visita.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	visita.flNovoClienteProspect = ValueUtil.VALOR_SIM;
    	if (novoProspect.cdLatitude != 0 && novoProspect.cdLongitude != 0) {
    		visita.flLocalizacaoChegada = Visita.FLLOCALIZACAO_PRESENTE;
    		visita.flLocalizacaoSaida = Visita.FLLOCALIZACAO_PRESENTE;
    	}
    	if (retornaVisitaJaExistente(visita) == null) {
			visita.cdVisita = generateIdGlobal();
			visita.flTipoAlteracao = Visita.FLTIPOALTERACAO_INSERIDO;
			getCrudDao().insert(visita);
		}
	}
    
}
