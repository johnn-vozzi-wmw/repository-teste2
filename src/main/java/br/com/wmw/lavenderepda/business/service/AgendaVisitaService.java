package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaCadastro;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.vo.AgendaVisitaReagendamentoParams;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.MotRegistroVisita;
import br.com.wmw.lavenderepda.business.domain.StatusAgenda;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.dto.AgendaVisitaDTO;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AgendaVisitaDbxDao;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONArray;
import totalcross.json.JSONObject;
import totalcross.sys.Settings;
import totalcross.sys.Time;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class AgendaVisitaService extends CrudService {

    private static AgendaVisitaService instance;

    private AgendaVisitaService() {
        //--
    }

    public static AgendaVisitaService getInstance() {
        if (instance == null) {
            instance = new AgendaVisitaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AgendaVisitaDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public int getQtAgendasDiasUteisNoMes(String cdEmpresa, String cdRepresentante) throws SQLException {
    	int countAgenda = 0;
		AgendaVisita agendaFilter = new AgendaVisita();
		agendaFilter.cdEmpresa = cdEmpresa;
		agendaFilter.cdRepresentante = cdRepresentante;
		Vector agendaListTemp = findAllByExample(agendaFilter);
		int size = agendaListTemp.size();
		if (size > 0) {
			Date dtBase = DateUtil.getCurrentDate();
			int nuDiasMes = dtBase.getDaysInMonth();
			dtBase = DateUtil.getDateValue(1, dtBase.getMonth(), dtBase.getYear());
			for (int j = 1; j <= nuDiasMes; j++) {
				if ((dtBase.getDayOfWeek() != 0) && (dtBase.getDayOfWeek() != 6)) {
					countAgenda += getQtAgendasNoDia(dtBase, agendaListTemp);
				}
				dtBase.advance(1);
			}
		}
		return countAgenda;
    }

	public int getQtAgendasNoDia(Date dtBase, Vector agendaListTemp) {
		int size = agendaListTemp.size();
		int countAgenda = 0;
		for (int x = 0; x < size; x++) {
			AgendaVisita agenda = (AgendaVisita) agendaListTemp.items[x];
			if ((dtBase.getDayOfWeek() + 1) == agenda.nuDiaSemana && validaDtAgenda(agenda, dtBase)) {
				if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() && isFlSemanaMesAgendaValida(agenda, dtBase, 1)) {
					countAgenda++;
				}
				if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes() && isFlSemanaMesAgendaValida(agenda, dtBase, 2)
						&& verifyFrequenciaAgenda(agenda, dtBase)) {
					countAgenda++;
				}
				if (LavenderePdaConfig.isUsaAgendaDeVisitas() && !LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()
						&& !LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes() && verifyFrequenciaAgenda(agenda, dtBase)) {
					countAgenda++;
				}
			}
		}
		return countAgenda;
	}

	private boolean isFlSemanaMesAgendaValida(AgendaVisita agenda, Date dtBase, int usaAgendaVisitaBaseadaNaSemanaDoMes) {
		if (agenda.isValidaFlSemanaMes(usaAgendaVisitaBaseadaNaSemanaDoMes)) {
			int semanaMes = getFlSemanaMes(dtBase);
			return (agenda.flSemanaMes.equals(StringUtil.getStringValue(semanaMes))) || (ValueUtil.VALOR_SIM.equals(agenda.flSemanaMes));
		}
		return true;
	}

	private AgendaVisita getAgendaVisitaFilter() {
		AgendaVisita agendaFilter = new AgendaVisita();
		agendaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		agendaFilter.sortAsc = ValueUtil.VALOR_SIM;
    	agendaFilter.sortAtributte = "NUSEQUENCIA";
		return agendaFilter;
	}

	public Vector findAgendasVisitas(Date dtAgenda, int statusAgenda) throws SQLException {
    	return findAgendasVisitas(getAgendaVisitaFilter(), dtAgenda, statusAgenda, false, null, null);
	}
	
    public Vector findAgendasVisitas(Date dtAgendaInicio, Date dtAgendaFim) throws SQLException {
    	return findAgendasVisitas(getAgendaVisitaFilter(), dtAgendaInicio, StatusAgenda.STATUSAGENDA_TODOS, false, null, dtAgendaInicio, dtAgendaFim, null, true);
    }
    
    public Vector findAgendasVisitas(AgendaVisita agendaVisitaFilter, Date dtAgendaInicio, Date dtAgendaFim) throws SQLException {
    	return findAgendasVisitas(agendaVisitaFilter, dtAgendaInicio, StatusAgenda.STATUSAGENDA_TODOS, false, null, dtAgendaInicio, dtAgendaFim, null, true);
    }
    public Vector findAgendasVisitas(AgendaVisita agendaFilter, Date dtAgenda, int statusAgenda,  boolean isckClientesAtrasadosChecked, String flTipoCadastroCliente , String flTipoClienteRede) throws SQLException {
    	return findAgendasVisitas(agendaFilter, dtAgenda, statusAgenda, isckClientesAtrasadosChecked, flTipoCadastroCliente, null, null, flTipoClienteRede);
    }
    
    public Vector findAgendasVisitas(AgendaVisita agendaFilter, Date dtAgenda, int statusAgenda,  boolean isckClientesAtrasadosChecked, String flTipoCadastroCliente, Date dtInicial, Date dtFinal, String flTipoClienteRede) throws SQLException {
    	return findAgendasVisitas(agendaFilter, dtAgenda, statusAgenda, isckClientesAtrasadosChecked, flTipoCadastroCliente, dtInicial, dtFinal, flTipoClienteRede, false);
    }
    
	public Vector findAgendasVisitas(AgendaVisita agendaFilter, Date dtAgenda, int statusAgenda,  boolean isckClientesAtrasadosChecked, String flTipoCadastroCliente, Date dtInicial, Date dtFinal, String flTipoClienteRede, boolean ignoraNuDiaSemanaFilter) throws SQLException {
		agendaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaFilter.nuDiaSemanaFilter = ignoraNuDiaSemanaFilter ? "" : StringUtil.getStringValue(dtAgenda.getDayOfWeek() + 1);
		agendaFilter.statusAgendaCdPositivado = statusAgenda;
		agendaFilter.dtAgenda = dtAgenda;
		boolean isFiltroAgendaVisitaPorPeriodo = dtInicial != null && dtFinal != null;
		Vector agendaList = new Vector();
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() || LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
			//--
			AgendaVisita agendaVisitaFilter = (AgendaVisita) agendaFilter.clone();
			agendaVisitaFilter.cliente = new Cliente();
			agendaVisitaFilter.cliente.flTipoCadastro = flTipoCadastroCliente;
			agendaVisitaFilter.cliente.flTipoClienteRede = flTipoClienteRede;
			if (isFiltroAgendaVisitaPorPeriodo) {
				agendaList = findAgendaVisitasPorPeriodo(dtInicial, dtFinal, agendaVisitaFilter, true);
			} else {
				if (ValueUtil.isNotEmpty(dtAgenda)) {
					agendaVisitaFilter.nuDiaSemana = dtAgenda.getDayOfWeek() + 1;
				}
				agendaList = findAllByExampleSummary(agendaVisitaFilter);
				excluiAgendasBaseadasFlSemanaMes(dtAgenda, agendaList);
				excluiAgendasBaseadasNaDataBase(dtAgenda, agendaList);
			}
		} else if (LavenderePdaConfig.isUsaAgendaDeVisitas()) {
			if (isFiltroAgendaVisitaPorPeriodo) {
				agendaList = findAgendaVisitasPorPeriodo(dtInicial, dtFinal, agendaFilter, false);
			} else {
				if (ValueUtil.isNotEmpty(dtAgenda)) {
					agendaFilter.nuDiaSemana = dtAgenda.getDayOfWeek() + 1;
				}
				agendaList = findAllByExampleSummary(agendaFilter);
			}
		}
		Date dtAgendaFiltroUltrapassada = !isFiltroAgendaVisitaPorPeriodo ? dtAgenda : null;
		excluiAgendaComDataFinalUltrapassada(agendaList, dtAgendaFiltroUltrapassada);
		if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			Vector listVisitasToday = VisitaService.getInstance().findVisitasDistinct(dtAgenda, agendaFilter.cdRepresentante);
			agendaList = statusAgenda == -1 ? agendaList : filterByStatusAgenda(agendaList, statusAgenda, listVisitasToday);
		}
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && isckClientesAtrasadosChecked) {
			agendaList = filterClientesSemPedidoXDias(agendaList);
		}
		if (agendaList.size() > 0) {
			excluiAgendasAnterioresADtBaseouDtCriacao(agendaList, dtAgenda);
		}
		return agendaList;
//		return filterByClientesValidos(agendaList, flTipoCadastroCliente, flTipoClienteRede, agendaFilter.clienteCoordenadaFilter);
	}

	public boolean validaDtAgenda(AgendaVisita agenda, Date dtAgenda) {
		Date dataAgenda = ValueUtil.isEmpty(agenda.dtAgenda) ? dtAgenda : agenda.dtAgenda;
		return DateUtil.isBeforeOrEquals(ValueUtil.isEmpty(agenda.dtBase) ? agenda.dtCriacao : agenda.dtBase, dataAgenda);
	}
	
	public void excluiAgendasAnterioresADtBaseouDtCriacao(Vector agendaList, Date dtAgenda) {
		for (int i = 0; i < agendaList.size(); i++) {
			AgendaVisita agenda = (AgendaVisita) agendaList.items[i];
			if (!validaDtAgenda(agenda, dtAgenda)) {
				agendaList.removeElement(agenda);
				i--;
			}
		}
	}
	
	public int getFlSemanaMes(Date dtAgenda) {
		if (LavenderePdaConfig.isIdentificaSemanaAgendaVisitaConformeSemanaMes()) {
			return DateUtil.getNuSemanaMesConformePrimeiraSemana(dtAgenda);
		}
		BigDecimal bd = new BigDecimal(dtAgenda.getDay());
		bd = bd.divide(new BigDecimal(7), BigDecimal.ROUND_UP);
		int semanaMes = bd.intValue();
		return semanaMes;
	}

	private void excluiAgendasBaseadasNaDataBase(Date dtAgenda, Vector agendaList) {
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes() && agendaList.size() > 0) {
			for (int i = 0; i < agendaList.size(); i++) {
				AgendaVisita agenda = (AgendaVisita) agendaList.items[i];
				if (agenda.isAgendaVisitaBaseadaNaDataBase()) {
					if (!verifyFrequenciaAgenda(agenda, dtAgenda)) {
						agendaList.removeElement(agenda);
						i--;
					}
				}
			}
		}
	}
	
	private void excluiAgendaComDataFinalUltrapassada(Vector agendaList, Date dtAgenda) {
		if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica) {
			Date dtAgendaFilter;
			for (int i = 0; i < agendaList.size(); i++) {
 				AgendaVisita agenda = (AgendaVisita) agendaList.items[i];
 				dtAgendaFilter = dtAgenda != null ? dtAgenda : agenda.dtAgenda;
				if (ValueUtil.isNotEmpty(agenda.dtFinal) && (agenda.dtFinal.isBefore(DateUtil.getCurrentDate()) || (ValueUtil.isNotEmpty(dtAgendaFilter) && agenda.dtFinal.isBefore(dtAgendaFilter)))) {
					agendaList.removeElement(agenda);
					i--;
				}
			}
		}
	}
	
	public AgendaVisita findAgendaVisitaPendente(String cdRepresentante, String cdCliente) throws SQLException {
		return findAgendaVisitaPendente(cdRepresentante, cdCliente, DateUtil.getCurrentDate(), false);
	}
	
	public AgendaVisita findAgendaVisitaPendente(String cdRepresentante, String cdCliente, boolean isNovoPedido) throws SQLException {
		return findAgendaVisitaPendente(cdRepresentante, cdCliente, DateUtil.getCurrentDate(), isNovoPedido);
	}
	
	public AgendaVisita findAgendaVisitaPendente(String cdRepresentante, String cdCliente, Date dtAgendaVisitaAtual) throws SQLException{
		return findAgendaVisitaPendente(cdRepresentante, cdCliente, DateUtil.getCurrentDate(), false);
	}
	
	public AgendaVisita findAgendaVisitaPendente(String cdRepresentante, String cdCliente, Date dtAgendaVisitaAtual, boolean isNovoPedido) throws SQLException {
		return findAgendaVisitaPendente(cdRepresentante, cdCliente, DateUtil.getCurrentDate(), false, null, null);
	}
	
	public AgendaVisita findAgendaVisitaPendente(String cdRepresentante, String cdCliente, Date dtAgendaVisitaAtual, boolean isNovoPedido, Date dtInicial, Date dtFinal) throws SQLException {
		return findAgendaVisitaPendente(cdRepresentante, cdCliente, DateUtil.getCurrentDate(), false, null, null, false);
	}
	
	public AgendaVisita findAgendaVisitaPendente(String cdRepresentante, String cdCliente, Date dtAgendaVisitaAtual, boolean isNovoPedido, Date dtInicial, Date dtFinal, boolean contabVariosPedidoRota) throws SQLException {
		AgendaVisita agendaVisitaFilter = new AgendaVisita();
    	agendaVisitaFilter.cdRepresentante = cdRepresentante;
    	agendaVisitaFilter.cdCliente = cdCliente;
    	agendaVisitaFilter.filterNovoPedido = isNovoPedido;
    	agendaVisitaFilter.contabVariosPedidoRota = contabVariosPedidoRota;
    	Date dtAgendaAtual = dtAgendaVisitaAtual == null ? DateUtil.getCurrentDate() : dtAgendaVisitaAtual; 
    	agendaVisitaFilter.orderByHrAgendaDif = LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente;
    	AgendaVisita agendaVisita = null;
    	Vector agendaVisitaList;
    	if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && dtInicial != null && dtFinal != null ) {
    		agendaVisitaList = findAgendasVisitas(agendaVisitaFilter, dtAgendaAtual, StatusAgenda.STATUSAGENDA_CDAVISITAR, false, null, dtInicial, dtFinal, null);
		} else {
			agendaVisitaList = findAgendasVisitas(agendaVisitaFilter, dtAgendaAtual, StatusAgenda.STATUSAGENDA_CDAVISITAR, false, null, null);
		}
    	if (ValueUtil.isEmpty(agendaVisitaList) && !LavenderePdaConfig.naoObrigaCompletarAgendaDiaAnterior) {
    		dtAgendaAtual = getLastValidDay(dtAgendaAtual);
    		agendaVisitaList = findAgendasVisitas(agendaVisitaFilter, dtAgendaAtual, StatusAgenda.STATUSAGENDA_CDAVISITAR, false, null, null);
    	}
    	if (agendaVisitaList.size() > 0) {
    		agendaVisita = (AgendaVisita) agendaVisitaList.items[0];
    		if (agendaVisita != null) {
    			agendaVisita.dtAgendaAtual = dtAgendaAtual;
    		}
    	}
    	return agendaVisita;
	}

	private Vector filterClientesSemPedidoXDias(Vector agendaList) throws SQLException {
		Vector newAgendaList = new Vector();
		int nuDiasSemPedido = LavenderePdaConfig.nuDiasFiltroClienteSemPedido;
		for (int i = 0; i < agendaList.size() - 1; i++) {
			AgendaVisita agendaVisita = (AgendaVisita)agendaList.items[i];
			int nuDiasSemPedidoCliente = ValueUtil.getIntegerValue(ClienteService.getInstance().getClienteColumn(agendaVisita.cdEmpresa, agendaVisita.cdRepresentante, agendaVisita.cdCliente, "nuDiasSemPedido"));
			if (nuDiasSemPedidoCliente >= nuDiasSemPedido) {
				newAgendaList.addElement(agendaVisita);
			}
		}
		return newAgendaList;
	}

	protected Vector filterByClientesValidos(Vector agendaListFilter, String flTipoCadastroCliente, String flTipoClienteRede, boolean clienteCoordenadaFilter) throws SQLException {
		Vector agendaList = new Vector();
		int size = agendaListFilter.size();
		for (int x = 0; x < size; x++) {
			AgendaVisita agenda = (AgendaVisita) agendaListFilter.items[x];
			agenda.cliente = ClienteService.getInstance().getCliente(agenda.cdEmpresa, agenda.cdRepresentante, agenda.cdCliente);
			if (ValueUtil.isNotEmpty(agenda.cliente.cdCliente)) {
				if (isPassouFiltroTipoCadastroCliente(flTipoCadastroCliente, agenda.cliente.flTipoCadastro) && isPassouFiltroTipoClienteRede(flTipoClienteRede, agenda.cliente.cdRede)) {
					if (clienteCoordenadaFilter) {
						if (agenda.cliente.isClientePossuiCoordenada()) {
							agendaList.addElement(agenda);
						}	
					} else {						
						agendaList.addElement(agenda);
					}
				}
			}
		}
		return agendaList;
	}

	private boolean isPassouFiltroTipoCadastroCliente(String flTipoCadastroCliente, String flTipoCadastroNaTabela) {
		boolean adicionaNaAgenda = true;
		if (ValueUtil.isNotEmpty(flTipoCadastroCliente) && (!ValueUtil.valueEquals(Cliente.TIPO_TODOS,flTipoCadastroCliente))) {
			adicionaNaAgenda = false;
			if (ValueUtil.valueEquals(Cliente.TIPO_PROSPECTS,flTipoCadastroCliente) && ValueUtil.valueEquals(flTipoCadastroCliente, flTipoCadastroNaTabela)) {
				adicionaNaAgenda = true;
			} else {
				adicionaNaAgenda = Cliente.TIPO_CLIENTE.equals(flTipoCadastroCliente) && !Cliente.TIPO_PROSPECTS.equals(flTipoCadastroNaTabela);
			}
		}
		return adicionaNaAgenda;
	}
					
	private boolean isPassouFiltroTipoClienteRede(String flTipoClienteRede, String cdRede) {
		boolean adicionaNaAgenda = true;
		if (ValueUtil.isNotEmpty(flTipoClienteRede) && (!ValueUtil.valueEquals(Cliente.TIPO_TODOS, flTipoClienteRede))) {
			adicionaNaAgenda = false;
			if (ValueUtil.valueEquals(Cliente.TIPO_REDE, flTipoClienteRede) && ValueUtil.isNotEmpty(cdRede)) {
				adicionaNaAgenda = true;
				} else {
				adicionaNaAgenda = ValueUtil.valueEquals(Cliente.TIPO_INDIVIDUAL, flTipoClienteRede) && ValueUtil.isEmpty(cdRede);
				}
			}
		return adicionaNaAgenda;
		}


	private Vector filterByStatusAgenda(Vector agendaListFilter, int statusAgenda, Vector listVisitasToday) throws SQLException {
		Vector agendaList = new Vector();
		int size = agendaListFilter.size();
		for (int x = 0; x < size; x++) {
			AgendaVisita agenda = (AgendaVisita) agendaListFilter.items[x];
			boolean visited = false;
			int size2 = listVisitasToday.size();
			for (int i = 0; i < size2; i++) {
				Visita visita = (Visita) listVisitasToday.items[i];
				if (isVisitaDoClienteDaAgenda(agenda, visita) && (Visita.FL_VISITA_POSITIVADA.equals(visita.flVisitaPositivada))) {
					visited = isVisited(visita);
					if (statusAgenda == StatusAgenda.STATUSAGENDA_CDPOSITIVADO) {
						agendaList.addElement(agenda);
						i = size2;
					}
				} else if (isVisitaDoClienteDaAgenda(agenda, visita) && (Visita.FL_VISITA_NAOPOSITIVADA.equals(visita.flVisitaPositivada))) {
					visited = isVisited(visita);
					if (statusAgenda == StatusAgenda.STATUSAGENDA_CDNAOPOSITIVADO) {
						agendaList.addElement(agenda);
						i = size2;
					}
				} else if (isVisitaDoClienteDaAgenda(agenda, visita) && (ValueUtil.VALOR_SIM.equals(visita.flVisitaTransferida))) {
					visited = isVisited(visita);
					if (statusAgenda == StatusAgenda.STATUSAGENDA_CDTRANSFERIDO) {
						agendaList.addElement(agenda);
						i = size2;
					}
				} else if (isVisitaDoClienteDaAgenda(agenda, visita) && (ValueUtil.VALOR_SIM.equals(visita.flVisitaReagendada))) {
					visited = isVisited(visita);
					if (statusAgenda == StatusAgenda.STATUSAGENDA_CDREAGENDADO) {
						agendaList.addElement(agenda);
						i = size2;
					}
				}
			}
			if (!visited && (statusAgenda == (StatusAgenda.STATUSAGENDA_CDAVISITAR))) {
				agendaList.addElement(agenda);
			}
		}
		agendaListFilter = null;
		return agendaList;
	}

	private boolean isVisitaDoClienteDaAgenda(AgendaVisita agenda, Visita visita) {
		boolean isVisitaDoClienteDaAgenda = ValueUtil.valueEquals(visita.cdCliente, agenda.cdCliente);
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			isVisitaDoClienteDaAgenda = isVisitaDoClienteDaAgenda && visita.dtVisita.equals(agenda.dtAgenda);
		}
		boolean isSequenciaDaVisitaDoClienteDaAgenda = true;
		if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			isSequenciaDaVisitaDoClienteDaAgenda = visita.nuSequencia == agenda.nuSequencia;
		}
		boolean isAgendaRelacionadaVisitaMultAgendasNoDia = true;
		if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
			isAgendaRelacionadaVisitaMultAgendasNoDia = ValueUtil.valueEquals(agenda.getRowKey(), visita.cdChaveAgendaOrigem);
		}
		if (isVisitaDoClienteDaAgenda && isSequenciaDaVisitaDoClienteDaAgenda && isAgendaRelacionadaVisitaMultAgendasNoDia) {
			agenda.setVisita(visita);
			return true;
		}
		return false;
	}

	private boolean isVisited(Visita visita) {
		if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			return ValueUtil.isNotEmpty(visita.dtSaidaVisita);
		}
		return true;
	}

	public Date getLastValidDay(Date currenteDate) throws SQLException {
		AgendaVisita agendaVisita = new AgendaVisita();
		agendaVisita.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaVisita.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Date lastValidDate = currenteDate;
		lastValidDate.advance(-1);
		if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == lastValidDate.getDayOfWeek()) {
			lastValidDate.advance(-1);
		} else if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == lastValidDate.getDayOfWeek()) {
			lastValidDate.advance(-1);
		} else if (ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaAgendaVisitaFinalDeSemana)) {
			if (DateUtil.DATA_SEMANA_DOMINGO == lastValidDate.getDayOfWeek()) {
				lastValidDate.advance(-1);
			}
			if (DateUtil.DATA_SEMANA_SABADO == lastValidDate.getDayOfWeek()) {
				lastValidDate.advance(-1);
			}
		}
		//--
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) {
			int semanaMes = getFlSemanaMes(lastValidDate);
			agendaVisita.flSemanaMes = StringUtil.getStringValue(semanaMes);
		}
		//--
		agendaVisita.nuDiaSemanaFilter = StringUtil.getStringValue(lastValidDate.getDayOfWeek() + 1);
		Vector agendaListTemp = new Vector();
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			agendaListTemp = findAgendaVisitasPorPeriodo(lastValidDate, lastValidDate, agendaVisita, LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes());
		} else {
			agendaListTemp = findAllByExample(agendaVisita);
		}
		//--
		int size = agendaListTemp.size();
		if (size == 0) {
			int nuMaxDias = 3;
			boolean foundLasValidDay = false;
			for (int i = 0; i < nuMaxDias; i++) {
				lastValidDate.advance(-1);
				if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == lastValidDate.getDayOfWeek()) {
					i--;
					continue;
				}
				if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == lastValidDate.getDayOfWeek()) {
					i--;
					continue;
				} 
				if (ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaAgendaVisitaFinalDeSemana)) {
					if (DateUtil.DATA_SEMANA_DOMINGO == lastValidDate.getDayOfWeek() || DateUtil.DATA_SEMANA_SABADO == lastValidDate.getDayOfWeek()) {
						i--;
						continue;
					}
				}
				if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) {
					int semanaMes = getFlSemanaMes(lastValidDate);
					agendaVisita.flSemanaMes = StringUtil.getStringValue(semanaMes);
				}
				agendaVisita.nuDiaSemanaFilter = StringUtil.getStringValue(lastValidDate.getDayOfWeek() + 1);
				if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
					agendaListTemp = findAgendaVisitasPorPeriodo(lastValidDate, lastValidDate, agendaVisita, LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes());
				} else {
					agendaListTemp = findAllByExample(agendaVisita);
				}
				size = agendaListTemp.size();
				if (size > 0) {
					foundLasValidDay = true;
					break;
				}
			}
			if (!foundLasValidDay) {
				for (int i = 0; i < nuMaxDias; i++) {
					lastValidDate.advance(1);
					if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == lastValidDate.getDayOfWeek()) {
						i--;
						continue;
					}
					if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == lastValidDate.getDayOfWeek()) {
						i--;
						continue;
					}
					if (ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaAgendaVisitaFinalDeSemana)) {
						if (DateUtil.DATA_SEMANA_DOMINGO == lastValidDate.getDayOfWeek() || DateUtil.DATA_SEMANA_SABADO == lastValidDate.getDayOfWeek()) {
							i--;
							continue;
						}
					}
				}
			}
		}
		return lastValidDate;
	}

	public boolean isHasVisitaPendenteOnDay(Date lastValidDate) throws SQLException {
		if (!LavenderePdaConfig.consideraAgendaPendenteCargaInicial() && ConfigInternoService.getInstance().isPrimeiroAcessoAoSistema()) {
			return false;
		}
		final Vector visitasPendingList;
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			visitasPendingList = findAgendasVisitas(getAgendaVisitaFilter(), lastValidDate, StatusAgenda.STATUSAGENDA_CDAVISITAR, false, null, lastValidDate, lastValidDate, null);
		} else {
			visitasPendingList = findAgendasVisitas(lastValidDate, StatusAgenda.STATUSAGENDA_CDAVISITAR);
		}
	    return !visitasPendingList.isEmpty();
	}

    public boolean verifyFrequenciaAgenda(AgendaVisita agenda, Date dtAgenda) {
    	if (AgendaVisita.FLTIPOFREQUENCIA_SEMANAL.equals(agenda.flTipoFrequencia) || AgendaVisita.FLTIPOFREQUENCIA_SEM_FREQUENCIA.equals(agenda.flTipoFrequencia)) {
    		return true;
    	}
    	boolean usaAgendaPorPeriodo = LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && ValueUtil.isNotEmpty(agenda.dtAgenda);
		if (AgendaVisita.FLTIPOFREQUENCIA_UNICA.equals(agenda.flTipoFrequencia) && dtAgenda != null) {
    			return usaAgendaPorPeriodo ? agenda.dtAgenda.equals(agenda.dtFinal) : dtAgenda.equals(agenda.dtFinal);
		}
		if (!usaAgendaPorPeriodo && dtAgenda == null || agenda.dtBase == null) {
			return false;
		}
    	if (AgendaVisita.FLTIPOFREQUENCIA_QUINZENAL.equals(agenda.flTipoFrequencia)) {
    		Date dtAtual = usaAgendaPorPeriodo ? agenda.dtAgenda: dtAgenda;
    		int semanasPassadas = DateUtil.getWeeksBetween(agenda.dtBase, dtAtual);
    		return semanasPassadas % 2 == 0;
    	} else if (AgendaVisita.FLTIPOFREQUENCIA_MENSAL.equals(agenda.flTipoFrequencia)) {
    		Date dtAtual = usaAgendaPorPeriodo ? agenda.dtAgenda: dtAgenda;
    		int semanasPassadas = DateUtil.getWeeksBetween(agenda.dtBase, dtAtual);
    		return semanasPassadas % 4 == 0;
    	} else if (AgendaVisita.FLTIPOFREQUENCIA_BIMESTRAL.equals(agenda.flTipoFrequencia)) {
    		return validaFrequencia(agenda, usaAgendaPorPeriodo, dtAgenda, 2);
    	} else if (AgendaVisita.FLTIPOFREQUENCIA_TRIMESTRAL.equals(agenda.flTipoFrequencia)) {
    		return validaFrequencia(agenda, usaAgendaPorPeriodo, dtAgenda, 3);
    	}
    	return false;
    }
    
    private boolean validaFrequencia(AgendaVisita agenda, boolean usaAgendaPorPeriodo, Date dtAgenda, int nuFrequencia) {
    	int mesInicio = agenda.dtBase.getMonth();
    	Date dataAgenda = usaAgendaPorPeriodo ? agenda.dtAgenda : dtAgenda;
		int mesAtual = dataAgenda.getMonth() + (dataAgenda.getYear() - agenda.dtBase.getYear()) * 12;
		int semanaInicio = getFlSemanaMes(agenda.dtBase);
		int semanaAtual = getFlSemanaMes(dtAgenda);
		int diferencaMeses = mesAtual - mesInicio;
	    return diferencaMeses % nuFrequencia == 0 && semanaInicio == semanaAtual;
    }
    
    public Date getDtLimiteReagendamento(Date dtAgendaAtual) throws SQLException {
    	Date dtAgenda = null;
		try {
			dtAgenda = new Date(ValueUtil.getIntegerValue(DateUtil.formatDateYYYYMMDD(dtAgendaAtual)));
		} catch (InvalidDateException e) {
			return new Date();
		}
    	if (ValueUtil.isNotEmpty(dtAgenda)) {
    		DateUtil.advanceUtilDays(dtAgenda, LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia);
    		ajustToValidDate(dtAgenda);
    	}
    	return dtAgenda;
    }

	private void ajustToValidDate(Date dtAgenda) throws SQLException {
		if (FeriadoService.getInstance().isDtFeriado(dtAgenda)) {
			DateUtil.decDay(dtAgenda, 1);
			ajustToValidDate(dtAgenda);
		} else if (DateUtil.DATA_SEMANA_DOMINGO == DateUtil.getDayOfWeek(dtAgenda)) {
			DateUtil.decDay(dtAgenda, 2);
			ajustToValidDate(dtAgenda);
		}
	}
	
	public void reagendarAgendaVisita(AgendaVisita agendaVisita, AgendaVisitaReagendamentoParams util) throws SQLException {
		validaReagendarAgenda(util.cdMotivoReagendamento, util.hrReagendamento, util.dtReagendamento, util.dtLimiteReagendamento, util.dataAgendaOriginal, agendaVisita.dtAgendaAtual, agendaVisita.hrAgenda);
		AgendaVisita reagendamento = (AgendaVisita) agendaVisita.clone();
		AgendaCadastro agendaCadastro = null;
		if (!(LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal && util.dtReagendamento.equals(agendaVisita.dtAgendaAtual))) {
			reagendamento.nuDiaSemana = util.dtReagendamento.getDayOfWeek() + 1;
			int semanaMes = getFlSemanaMes(util.dtReagendamento);
			reagendamento.flSemanaMes = StringUtil.getStringValue(semanaMes);
			reagendamento.dtAgendaOriginal = util.dataAgendaOriginal;
			reagendamento.nuSequencia = 0;
	    	Vector agendaVisitaList = findAgendasVisitas(reagendamento, util.dtReagendamento, StatusAgenda.STATUSAGENDA_CDAVISITAR, false, null, null);
	    	reagendamento.nuSequencia = agendaVisitaList.size() + 1;
		}
		reagendamento.dtCriacao = DateUtil.getCurrentDate();
		reagendamento.hrAgenda = util.hrReagendamento.substring(0, 2) + ":" + util.hrReagendamento.substring(2, 4);
		reagendamento.hrAgendaFim = ValueUtil.isNotEmpty(util.hrReagendamentoFim) ?  util.hrReagendamentoFim.substring(0, 2) + ":" + util.hrReagendamentoFim.substring(2, 4) : null;
		validaHoraFimDaAgendaDeVisita(reagendamento.hrAgenda, reagendamento.hrAgendaFim);
		reagendamento.dtFinal = util.dtReagendamento;
		if (LavenderePdaConfig.realizaCadastroAgendaVisitaAoReagendar && LavenderePdaConfig.usaCadastroAgendaVisita && LavenderePdaConfig.usaReagendamentoAgendaVisita) {
			agendaCadastro = new AgendaCadastro(reagendamento);
			agendaCadastro.flAgendaReagendada = ValueUtil.VALOR_SIM;
		}
    	try {
    		if (LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal && util.dtReagendamento.equals(agendaVisita.dtAgendaAtual)) {
    			update(reagendamento);
    			if (LavenderePdaConfig.realizaCadastroAgendaVisitaAoReagendar && agendaCadastro != null) {
    				AgendaCadastroService.getInstance().insertOrUpdateAgendaCadastro(agendaCadastro);
    			}
    			salvaRegistroDaHoraDeAgendamentoAnterior(agendaVisita.getRowKey(), agendaVisita.dtAgendaAtual, agendaVisita.hrAgenda);
    			if (ClienteService.getInstance().isPossuiVisitaEmAndamento(reagendamento.cdCliente)) {
    				Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
    				if (VisitaService.getInstance().isVisitaEmAndamento(visitaEmAndamento)) {
    					VisitaService.getInstance().desfazVisitaEmAndamento(visitaEmAndamento);
        			}
    				if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
    					SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada = null;
    				}
    			}
    		} else {
    			validaAgendaJaExistente(reagendamento);
    			insert(reagendamento);
    			if (LavenderePdaConfig.realizaCadastroAgendaVisitaAoReagendar && agendaCadastro != null) {
    				AgendaCadastroService.getInstance().insert(agendaCadastro);
    			}
    			if (ClienteService.getInstance().isPossuiVisitaEmAndamento(reagendamento.cdCliente)) {
    				VisitaService.getInstance().reagendaVisitaEmAndamento(agendaVisita, util.cdMotivoReagendamento, util.dsObservacao);
    			} else {
    				VisitaService.getInstance().insertVisitaByReagendamento(agendaVisita, util.cdMotivoReagendamento, util.dsObservacao, Visita.FL_VISITA_REAGENDADA, util.dtAgendaAtual);
    			}
    		}
		} catch (Throwable e) {
			throw new ValidationException(Messages.VALIDACAO_AGENDA_JA_EXISTENTE);
		}
	}
	
	public void validaHoraFimDaAgendaDeVisita(String hrAgenda, String hrAgendaFim) {
		if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
			if (LavenderePdaConfig.isHabilitaEObrigaHoraFimAgendaVisita()) {
				if (ValueUtil.isEmpty(hrAgenda)) {
					throw new ValidationException(Messages.MSG_AGENDAVISITA_HORA_INICIO_NAO_INFORMADA);
				}
				if (ValueUtil.isEmpty(hrAgendaFim)) {
					throw new ValidationException(Messages.MSG_AGENDAVISITA_HORA_FIM_NAO_INFORMADA);
				}
			}
			if (ValueUtil.isNotEmpty(hrAgenda) && ValueUtil.isNotEmpty(hrAgendaFim)) {
				hrAgenda = !hrAgenda.contains(":") ? StringUtil.insertStringPos(hrAgenda, ":", 2) : hrAgenda;
				hrAgendaFim = !hrAgendaFim.contains(":") ? StringUtil.insertStringPos(hrAgendaFim, ":", 2) : hrAgendaFim;
				if (TimeUtil.getSecondsBetween(hrAgenda + ":00", hrAgendaFim + ":00") >= 0) {
					throw new ValidationException(Messages.MSG_AGENDAVISITA_HORA_INICIO_MAIOR_OU_IGUAL_HORA_FIM);
				}
			}
		}
	}
	
	
	private void validaAgendaJaExistente(AgendaVisita agenda) throws Exception {
		AgendaVisita agendaVisitaFilter = new AgendaVisita();
		agendaVisitaFilter.cdEmpresa = agenda.cdEmpresa;
		agendaVisitaFilter.cdRepresentante = agenda.cdRepresentante;
		agendaVisitaFilter.cdCliente = agenda.cdCliente;
		agendaVisitaFilter.nuDiaSemana = agenda.nuDiaSemana;
		agendaVisitaFilter.flSemanaMes = agenda.flSemanaMes;
		if (findAllByExample(agendaVisitaFilter).size() > 0) {
			throw new Exception();
		}
	}


	private void salvaRegistroDaHoraDeAgendamentoAnterior(String rowKey, Date dtAgendaAtual, String hrAgenda) throws SQLException {
		if (LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal) {
			String vlConfigInternoSalvo = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.salvaDataHoraOriginalAgendaVisita, rowKey);
			if (ValueUtil.isEmpty(vlConfigInternoSalvo)) {
				StringBuilder vlConfigInterno = new StringBuilder();
				vlConfigInterno.append(dtAgendaAtual).append(hrAgenda);
				ConfigInternoService.getInstance().addValue(ConfigInterno.salvaDataHoraOriginalAgendaVisita, rowKey, vlConfigInterno.toString());
			}
		}
	}

	public void validaReagendarAgenda(String cdMotivoReagendamento, String hrReagendamento, Date dtReagendamento, Date dtLimiteReagendamento, Date dataAgendaOriginal, Date dtAgendaAtual, String hrAgendaOriginal) throws SQLException {
		if (ValueUtil.isEmpty(dtReagendamento)) {
			throw new ValidationException(Messages.VALIDACAO_CAMPO_DT);
		}
		if (ValueUtil.isEmpty(hrReagendamento)) {
			throw new ValidationException(Messages.VALIDACAO_CAMPO_HR);
		}
		if (LavenderePdaConfig.usaMotivoReagendamentoTransferenciaAgenda && ValueUtil.isEmpty(cdMotivoReagendamento) && !(LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal && dtReagendamento.equals(dtAgendaAtual))) {
			throw new ValidationException(Messages.VALIDACAO_MOTIVO);
		}
		if (LavenderePdaConfig.nuDiasMaximoReagendamentoTransferencia > 0 && dtReagendamento.isAfter(dtLimiteReagendamento)) {
			throw new ValidationException(Messages.VALIDACAO_DTLIMITE_ULTRAPASSADA);
		}
		if (dtReagendamento.isBefore(DateUtil.getCurrentDate()) || dtReagendamento.equals(DateUtil.getCurrentDate())) {
			if (LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal && dtReagendamento.equals(DateUtil.getCurrentDate())) {
				Time horaAgendamento = getHora(hrReagendamento.substring(0, 2) + ":" + hrReagendamento.substring(2, 4));
				Time horaAtual = TimeUtil.getCurrentTime();
				if (TimeUtil.getMillisRealBetween(horaAgendamento, horaAtual) < 0) {
					throw new ValidationException(Messages.VALIDACAO_DATA_ATUAL);
				}
				
			} else {
				throw new ValidationException(Messages.VALIDACAO_DATA_ATUAL);
			}
		}
		if (ValueUtil.isNotEmpty(hrReagendamento)) {
			StringBuilder horaAgendamento = new StringBuilder();
			horaAgendamento.append(hrReagendamento.substring(0, 2)).append(":").append(hrReagendamento.substring(2, 4));
			if (!TimeUtil.isValidTimeHHMM(horaAgendamento.toString())) {
				throw new ValidationException(Messages.HORA_AGENDAMENTO_INVALIDO);
			}
			if (dtAgendaAtual.equals(dtReagendamento) && ValueUtil.valueEquals(hrAgendaOriginal, horaAgendamento.toString())) {
				throw new ValidationException(Messages.AGENDA_VISITA_POSSUI_REAGENDAMENTO);
			}
		}
		if (FeriadoService.getInstance().isDtFeriado(dtReagendamento)) {
			throw new ValidationException(Messages.VALIDACAO_DT_FERIADO);
		}
		validaDataAgendaVisitaNoFinalDeSemana(DateUtil.getDayOfWeek(dtReagendamento));
	}
	
	private void validaDataAgendaVisitaNoFinalDeSemana(int nuDiaSemana) {
    		if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == nuDiaSemana) {
    			throw new ValidationException(Messages.MSG_AGENDAVISITA_DOMINGO_NAO_PERMITIDO);
    		} else if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == nuDiaSemana) {
    			throw new ValidationException(Messages.MSG_AGENDAVISITA_SABADO_NAO_PERMITIDO);
    		} else if (LavenderePdaConfig.isAgendaVisitaFinalDeSemanaNaoHabilitado() && (DateUtil.DATA_SEMANA_DOMINGO == nuDiaSemana || DateUtil.DATA_SEMANA_SABADO == nuDiaSemana)) {
    			throw new ValidationException(Messages.MSG_AGENDAVISITA_FINAL_SEMANA_NAO_PERMITIDO);
    		}
    	}
 
	public boolean isHasAgendaVisitaNoDia(String cdCliente) throws SQLException {
		AgendaVisita agendaVisitaFilter = new AgendaVisita();
		agendaVisitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaVisitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		agendaVisitaFilter.cdCliente = cdCliente;
		Vector agendaVisitaList = findAllByExample(agendaVisitaFilter);
		if (agendaVisitaList.size() > 0) {
			return getQtAgendasNoDia(DateUtil.getCurrentDate(), agendaVisitaList) > 0;
		}
		return false;
	}
	
	private Time getHora(String hora) {
		Time horaLimite = new Time();
		horaLimite.hour = ValueUtil.getIntegerValue(StringUtil.split(hora, ':', 0));
	    horaLimite.minute = ValueUtil.getIntegerValue(StringUtil.split(hora, ':', 1));
	    horaLimite.second = 0;
	    return horaLimite;
	}

	public void criaVisitaAgendaReagendadaServidorByAgendaVisita() throws SQLException {
		AgendaVisita agendaVisitaFilter = new AgendaVisita();
		agendaVisitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaVisitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		agendaVisitaFilter.flAgendaReagendada = ValueUtil.VALOR_SIM;
		insertVisitaByReagendamento(agendaVisitaFilter);
	}

	private void insertVisitaByReagendamento(AgendaVisita agendaVisitaFilter) throws SQLException {
		Vector agendaVisitaList = findAllByExample(agendaVisitaFilter);
		int size = agendaVisitaList.size();
		for (int i = 0; i < size; i++) {
			AgendaVisita agendaVisita = (AgendaVisita) agendaVisitaList.items[i];
			agendaVisita.dtAgendaAtual = agendaVisita.dtAgendaOriginal;
			insertByAgenda(agendaVisita);
		}
	}

	private void insertByAgenda(AgendaVisita agendaVisita) throws SQLException {
		VisitaService.getInstance().insertVisitaByReagendamento(agendaVisita, agendaVisita.cdMotivoReagendamento, agendaVisita.dsObservacao, Visita.FL_VISITA_REAGENDADA, agendaVisita.dtAgendaReagendada);
		agendaVisita.flAgendaReagendada = ValueUtil.VALOR_NAO;
		update(agendaVisita);
	}
	
	public boolean isHasAgendaVisitaNoDiaAnterior(String cdCliente) throws SQLException {
		AgendaVisita agendaVisitaFilter = new AgendaVisita();
		agendaVisitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaVisitaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		agendaVisitaFilter.cdCliente = cdCliente;
		Vector agendaVisitaList = findAllByExample(agendaVisitaFilter);
		if (agendaVisitaList.size() > 0) {
			return getQtAgendasNoDia(getLastValidDay(DateUtil.getCurrentDate()), agendaVisitaList) > 0;
		}
		return false;
	}
	
	public Vector replicaVisitaNaoPositivadaEmTodasEmpresas(Visita visitaReplicar, String cdMotivoRegistroVisita) throws SQLException {
		if (cdMotivoRegistroVisita == null) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
		}
		MotRegistroVisita motRegistroVisita = MotRegistroVisitaService.getInstance().getMotivoVisitaByFilter(visitaReplicar.cdEmpresa, cdMotivoRegistroVisita);
		if (!Visita.FL_VISITA_NAOPOSITIVADA.equals(motRegistroVisita.flVisitaPositivada)) {
			return new Vector();
		}
		Vector allOutrosCdEmpresas = RepresentanteEmpService.getInstance().findAllOutrosCdEmpresas(visitaReplicar.cdEmpresa, visitaReplicar.cdRepresentante);
    	Vector cdEmpresasComVisitasExistentes = getCdEmpresasComVisitasExistentes(visitaReplicar);
    	int sizeCdEmpresasComVisitasExistentes = cdEmpresasComVisitasExistentes.size();
    	for (int i = 0; i < sizeCdEmpresasComVisitasExistentes; i++) {
    		allOutrosCdEmpresas.removeElement(cdEmpresasComVisitasExistentes.items[i]);
		}
    	Vector visitasReplicadasList = new Vector(allOutrosCdEmpresas.size());
    	if (ValueUtil.isNotEmpty(allOutrosCdEmpresas)) {
    		int size = allOutrosCdEmpresas.size();
			for (int i = 0; i < size; i++) {
				String cdEmpresaASerDuplicada = (String) allOutrosCdEmpresas.items[i];
				int motRegistroVisitaCount = MotRegistroVisitaService.getInstance().countMotivosRegistrosVisitas(cdEmpresaASerDuplicada, cdMotivoRegistroVisita);
				if (motRegistroVisitaCount > 0) {
					//Cria uma visita semelhante a atual(não positivada) para as outras empresas.
					Visita visita = (Visita) visitaReplicar.clone();
					visita.rowKey = null;
	    			visita.cdVisita = VisitaService.getInstance().generateIdGlobal();
	    			visita.cdEmpresa = cdEmpresaASerDuplicada;
	    			visita.cdRepresentante = visitaReplicar.cdRepresentante;
	    			visita.flVisitaManual = ValueUtil.VALOR_SIM;
		    		VisitaService.getInstance().insert(visita);
		    		visitasReplicadasList.addElement(visita);
				}
			}
    	}

    	return visitasReplicadasList;
    }
	
	private Vector getCdEmpresasComVisitasExistentes(Visita visitaReplicar) throws SQLException {
		Visita visitaFilter = new Visita();
		visitaFilter.cdCliente = visitaReplicar.cdCliente;
		visitaFilter.cdEmpresaNegacao = visitaReplicar.cdEmpresa;
		visitaFilter.dtVisita = visitaReplicar.dtVisita;
		return VisitaService.getInstance().findColumnValuesByExample(visitaFilter, "CDEMPRESA");
	}
	
	private Vector findAgendaVisitasPorPeriodo(Date dtInicial, Date dtFinal, AgendaVisita agendaVisitaFilter, boolean isConsideraSemanaMes) throws SQLException {
		Vector agendaList = new Vector(30);
		int dias = DateUtil.getDaysBetween(dtFinal, dtInicial);
		agendaVisitaFilter.dtAgenda = DateUtil.getDateValue(dtInicial);
		if (agendaVisitaFilter.dtAgenda == null) {
			return agendaList;
		}
		agendaVisitaFilter.dtFinal = dtFinal;
		boolean isPermiteAgendaSabado = LavenderePdaConfig.isSistemaPermiteCadastroParaSabadoEDomingo() || LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado();
		boolean isPermiteAgendaDomingo = LavenderePdaConfig.isSistemaPermiteCadastroParaSabadoEDomingo() || LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo();
		for (int i = 0; i <= dias; i++) {
			agendaVisitaFilter.dtAgenda.advance(i == 0 ? 0 : 1);
			int diaCont = agendaVisitaFilter.dtAgenda.getDayOfWeek() + 1;
			if (isPermiteAgendaDomingo && diaCont == 1) {
				agendaList = getAgendaPorPeriodo(agendaVisitaFilter, isConsideraSemanaMes, agendaList, diaCont);
			} else if (isPermiteAgendaSabado && diaCont == 7) {
				agendaList = getAgendaPorPeriodo(agendaVisitaFilter, isConsideraSemanaMes, agendaList, diaCont);
			} else if (diaCont > 1 && diaCont < 7) {
				agendaList = getAgendaPorPeriodo(agendaVisitaFilter, isConsideraSemanaMes, agendaList, diaCont);
			}
		}
		return agendaList;
	}

	private Date calculaPrimeiraRecorrenciaAgendaNoPeriodo(Date dtInicial, Date dtFinal, AgendaVisita agendaVisita) throws SQLException {
		int dias = DateUtil.getDaysBetween(dtFinal, dtInicial);
		agendaVisita.dtAgenda = DateUtil.getDateValue(dtInicial);
		if (agendaVisita.dtAgenda == null) {
			return null;
		}
		agendaVisita.dtFinal = dtFinal;
		boolean isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes = LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes();
		boolean isUsaAgendaVisitaBaseadaNaSemanaDoMes = LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes();
		boolean isUsaAgendaDeVisitas = LavenderePdaConfig.isUsaAgendaDeVisitas();
		for (int i = 0; i <= dias; i++) {
			agendaVisita.dtAgenda.advance(i == 0 ? 0 : 1);
			int diaCont = agendaVisita.dtAgenda.getDayOfWeek() + 1;
			if (ValueUtil.valueEquals(diaCont, agendaVisita.nuDiaSemana) && validaDtAgenda(agendaVisita, agendaVisita.dtAgenda)) {
				if (isUsaAgendaVisitaBaseadaNaSemanaDoMes && isFlSemanaMesAgendaValida(agendaVisita, agendaVisita.dtAgenda, 1)) {
					return agendaVisita.dtAgenda;
				}
				if (isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes && isFlSemanaMesAgendaValida(agendaVisita, agendaVisita.dtAgenda, 2)
						&& verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgenda)) {
					return agendaVisita.dtAgenda;
				}
				if (isUsaAgendaDeVisitas && !isUsaAgendaVisitaBaseadaNaSemanaDoMes
						&& !isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes && verifyFrequenciaAgenda(agendaVisita, agendaVisita.dtAgenda)) {
					return agendaVisita.dtAgenda;
				}
			}
		}
		return null;
	}
	
	private Vector getAgendaPorPeriodo(AgendaVisita agendaVisitaFilter, boolean isConsideraSemanaMes, Vector agendaList, int diaCont) throws SQLException {
		agendaVisitaFilter.nuDiaSemana = diaCont;
		agendaVisitaFilter.flSemanaMes = null;
		Vector list2 = AgendaVisitaDbxDao.getInstance().findAllByExampleSummary(agendaVisitaFilter);
		excluiAgendasBaseadasNaDataBase(agendaVisitaFilter.dtAgenda, list2);
		if (isConsideraSemanaMes) {
			excluiAgendasBaseadasFlSemanaMes(agendaVisitaFilter.dtAgenda, list2);
		}
		addDatesParaAgendasEModoOrd(list2, agendaVisitaFilter.dtAgenda, agendaVisitaFilter.sortAtributte);
		agendaList = VectorUtil.concatVectors(agendaList, list2);
		return agendaList;
	}
	
	private void excluiAgendasBaseadasFlSemanaMes(Date dtAgenda, Vector agendaList) {
		if (agendaList.size() > 0) {
		    boolean usaAgendaSemanaDoMes = LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes();
		    boolean usaAgendaFrequenciaDataESemanaDoMes = LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes();
		    if (usaAgendaSemanaDoMes || usaAgendaFrequenciaDataESemanaDoMes) {
		        for (int i = 0; i < agendaList.size(); i++) {
		            AgendaVisita agenda = (AgendaVisita) agendaList.items[i];
		            if (!isFlSemanaMesAgendaValida(agenda, dtAgenda, getVlParametro(usaAgendaSemanaDoMes, usaAgendaFrequenciaDataESemanaDoMes))) {
		                agendaList.removeElement(agenda);
		                i--;
		            }
		        }
		    }
		}
	}

	private int getVlParametro(boolean usaAgendaSemanaDoMes, boolean usaAgendaFrequenciaDataESemanaDoMes) {
	    if (usaAgendaSemanaDoMes) {
	        return 1;
	    } else if (usaAgendaFrequenciaDataESemanaDoMes) {
	        return 2;
	    }
	    return 0;
	}
	private void addDatesParaAgendasEModoOrd(Vector agendaList, Date dtAgenda, String sortAtribute) {
		int size = agendaList.size();
		Date date = DateUtil.getDateValue(dtAgenda);
		for (int i = 0; i < size; i++) {
			((AgendaVisita)agendaList.items[i]).dtAgenda = date;
			((AgendaVisita)agendaList.items[i]).sortAtributte = sortAtribute;
		}
	}

	public int getMaxNuSequenciaAgenda(AgendaVisita agendaVisita) throws SQLException {
		if (ValueUtil.valueEquals(agendaVisita.flTipoFrequencia, AgendaVisita.FLTIPOFREQUENCIA_UNICA) || isPermiteMultAgendasNoDiaMesmoCliente()) {
			return AgendaVisitaDbxDao.getInstance().findMaxNuSequencia(agendaVisita);
		}
		return 0;
	}

	public boolean isPermiteMultAgendasNoDiaMesmoCliente() {
		return LavenderePdaConfig.isHabilitaEObrigaHoraFimAgendaVisita() && LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente;
	}
	
	public void excluiAgendaVisitaDataFinalUltrapassada() {
		AgendaVisitaDbxDao.getInstance().excluiAgendaVisitaDataFinalUltrapassada();
	}
	
	public boolean isDtBaseAgendaAlreadyExists(AgendaVisita agendaVisita) throws SQLException {
		if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
			return false;
		}
		return AgendaVisitaDbxDao.getInstance().findAgendaVisitaByDtBase(agendaVisita);
	}
	
    public boolean validaAgendaJaExistenteMesmaDataHorario(AgendaVisita agendaVisita) throws SQLException {
    	AgendaVisita agendaVisitaFilter = new AgendaVisita();
    	agendaVisitaFilter.cdEmpresa = agendaVisita.cdEmpresa;
    	agendaVisitaFilter.cdRepresentante = agendaVisita.cdRepresentante;
    	Date dtAgendaVisita = carregaPrimeiraRecorrencia(agendaVisita);
    	agendaVisitaFilter.hrAgendaConflito = agendaVisita.hrAgenda;
    	agendaVisitaFilter.hrAgendaFimConflito = agendaVisita.hrAgendaFim;
    	Vector listaAgendasNoDia = findAgendasVisitas(agendaVisitaFilter, dtAgendaVisita, dtAgendaVisita);
    	return ValueUtil.isNotEmpty(listaAgendasNoDia) && (listaAgendasNoDia.size() > 1 || ValueUtil.valueNotEqualsIfNotNull(((AgendaVisita) listaAgendasNoDia.items[0]).getPrimaryKey(), agendaVisita.getPrimaryKey()));
    }
    
	public Date carregaPrimeiraRecorrencia(AgendaVisita agendaVisita) throws SQLException {
		if (agendaVisita.isFrequenciaBaseadaDtBase() && ValueUtil.isNotEmpty(agendaVisita.dtBase)) {
			return agendaVisita.dtBase;
		}
		Date dtInicial = agendaVisita.dtBase != null ? agendaVisita.dtBase : agendaVisita.dtCriacao;
		Date dtFinal = DateUtil.getDateValue(dtInicial);
		dtFinal.advance(180);
	    return calculaPrimeiraRecorrenciaAgendaNoPeriodo(dtInicial, dtFinal, agendaVisita);
	}
    
	public void recebeAtualizacaoAgendaVisita(double cdLatitude, double cdLongitude, Vector agendaVisitaList) throws Exception {
		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
		if (erpToPda.ordenaAgendaVisita(cdLatitude, cdLongitude, getJsonAgendaList(agendaVisitaList))) {
			ConfigIntegWebTc configIntegWebTc = new ConfigIntegWebTc();
			configIntegWebTc.dsTabela = AgendaVisita.TABLE_NAME;
			configIntegWebTc = (ConfigIntegWebTc)ConfigIntegWebTcService.getInstance().findByPrimaryKey(configIntegWebTc);
			ConfigIntegWebTc[] configArray = {configIntegWebTc};
			Vector list = new Vector(configArray);
			erpToPda.ignoreAntesEDepoisReceberDados = true;
			erpToPda.recebeDadosDisponiveisServidor(SyncManager.getInfoAtualizacaoByWeb2SyncList(list));
		}
	}
	
	public void limpaNuOrdemManualAgenda() throws SQLException {
		AgendaVisita filter = new AgendaVisita();
		filter.nuDiaSemana = DateUtil.getDayOfWeek(DateUtil.getCurrentDate()) + 1;
		filter.flSemanaMes = StringUtil.getStringValue(getFlSemanaMes(DateUtil.getCurrentDate()));
		AgendaVisitaDbxDao.getInstance().limpaNuOrdemManualAgenda(filter);
	}
	
	private String getJsonAgendaList(Vector agendaVisitaList) {
		int size = agendaVisitaList.size();
		JSONObject[] jsonList = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			AgendaVisita agendaVisita = (AgendaVisita)agendaVisitaList.items[i];
			jsonList[i] = new JSONObject(new AgendaVisitaDTO(agendaVisita));
		}
		JSONArray jsonArray = new JSONArray(jsonList);
		return jsonArray.toString();
	}

	public GpsData getCoordenadaAtual(boolean showLoading) {
		LoadingBoxWindow msg = showLoading ? UiUtil.createProcessingMessage(Messages.CAD_COORD_COLETANDO) : null;
		if (showLoading) msg.popupNonBlocking();
		try {
			GpsData gpsData = GpsService.getInstance().forceReadData();
			if (gpsData.isGpsOff()) {
				UiUtil.showWarnMessage(Messages.CAD_COORD_GPS_DESLIGADO);
				return null;
			} else if (gpsData.isSuccess()) {
				return gpsData;
			} else {
				UiUtil.showWarnMessage(Messages.CAD_COORD_COLETA_ERRO);
			}
		} catch (Throwable ex) {
			//Apenas nao realiza a coleta
		} finally {
			if (msg != null) msg.unpop();
		}
		return null;
	}

	public void processaCalculoRotaPrimeiroAcessoDia() {
		if (!LavenderePdaConfig.automatizaCalculoRota || VmUtil.isSimulador()) return;

		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.MSG_CALCULANDO_ROTAS);
		try {
			mb.popupNonBlocking();

			GpsData data = getCoordenadaAtual(false);
			if (data == null) return;

			Vector agendaVisitaList = AgendaVisitaService.getInstance().findAgendasVisitas(
					new AgendaVisita(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante),
					DateUtil.getCurrentDate(),
					StatusAgenda.STATUSAGENDA_TODOS,
					false,
					null,
					null
			);
			if (ValueUtil.isEmpty(agendaVisitaList)) return;

			recebeAtualizacaoAgendaVisita(data.latitude, data.longitude, agendaVisitaList);

			UiUtil.showSucessMessage(Messages.MSG_ROTA_CALCULADA, UiUtil.DEFAULT_MESSAGETIME_SHORT);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			mb.unpop();
		}
	}
	
	public Date getUltimoDiaUtil() {
		Date ultimoDiaUtil = DateUtil.getCurrentDate();
		ultimoDiaUtil.advance(-1);
		if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && ValueUtil.valueEquals(DateUtil.DATA_SEMANA_DOMINGO, ultimoDiaUtil.getDayOfWeek())) {
			ultimoDiaUtil.advance(-1);
		} else if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo() && ValueUtil.valueEquals(DateUtil.DATA_SEMANA_SABADO, ultimoDiaUtil.getDayOfWeek())) {
			ultimoDiaUtil.advance(-1);
		} else if (ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaAgendaVisitaFinalDeSemana)) {
			if (DateUtil.DATA_SEMANA_DOMINGO == ultimoDiaUtil.getDayOfWeek()) {
				ultimoDiaUtil.advance(-1);
			}
			if (DateUtil.DATA_SEMANA_SABADO == ultimoDiaUtil.getDayOfWeek()) {
				ultimoDiaUtil.advance(-1);
			}
		}
		return ultimoDiaUtil;
	}

}
