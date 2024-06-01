package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.EditTimeMask;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaCadastro;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.AgendaCadastroService;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ModoAgendaVisitaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RecorrenciaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoAgendaComboBox;
import totalcross.ui.Control;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class CadAgendaVisitaWindow extends WmwCadWindow {
	
	private LabelName lbNmCliente;
	private ModoAgendaVisitaComboBox cbModoAgendaVisitaComboBox;
	private TipoAgendaComboBox cbTipoAgendaComboBox;
	private LabelName lbRecorrencia;
	private LabelName lbDtAgenda;
	private LabelName lbDtInicio;
	private LabelName lbDiasSemana;
	private LabelName lbHoraAgenda;
	private LabelName lbHoraAgendaFim;
	private LabelName lbDtLimite;
	private LabelName lbSequencia;
	private LabelValue lvNmCliente;
	private PushButtonGroupBase pbDsDiaSemana;
	private EditDate edDtAgenda;
	private EditText edDiaSemana;
	private EditDate edDtLimite;
	private EditDate edDtInicio;
	private EditText edDiaSemanaInicio;
	private RecorrenciaComboBox cbRecorrenciaComboBox;
	private EditTimeMask edHrAgendaVisita;
	private EditTimeMask edHrFimAgendaVisita;
	private EditNumberInt edNuSequencia;
	private Cliente cliente;
	private Control lbMarcadores;
	
	public CadAgendaVisitaWindow(Cliente cliente) throws SQLException {
		super(Messages.NOVA_AGENDA_VISITA);
		btSalvar.setText(Messages.BT_CONFIRMAR);
		this.cliente = cliente;
		criaComponentesDaTela();
		setDefaultRect();
	}
	
	private void criaComponentesDaTela() throws SQLException {
		lbNmCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		lbRecorrencia = new LabelName(Messages.RECORRENCIA_AGENDA_VISITA);
        cbModoAgendaVisitaComboBox = new ModoAgendaVisitaComboBox().setID("cbModoAgendaVisitaComboBox");
        cbTipoAgendaComboBox = new TipoAgendaComboBox().setID("cbTipoAgendaComboBox");
		lbDtAgenda = new LabelName(Messages.DATA_AGENDA);
		lbDtInicio = new LabelName(Messages.DATA_PRIMEIRA_VISITA);
		lbDiasSemana = new LabelName(Messages.DIAS_SEMANA);
		lbHoraAgenda = new LabelName(LavenderePdaConfig.isUsaHoraFimAgendaVisita() ? Messages.HORA_AGENDA_VISITA_INICIO : Messages.HORA_AGENDA_VISITA);
		lbHoraAgendaFim = new LabelName(Messages.HORA_FIM_AGENDA_VISITA);
		lbSequencia = new LabelName(Messages.AGENDAVISITA_LABEL_SEQUENCIA);
		lvNmCliente = new LabelValue();
		lvNmCliente.setText(cliente.toString());
		pbDsDiaSemana = new PushButtonGroupBase(getDiasSemana(), false, -1, -1, UiUtil.defaultGap * 2, 1, true, PushButtonGroupBase.CHECK);
		edDtAgenda = new EditDate().setID("edDtAgenda");
		edDtInicio = new EditDate().setID("edDtInicio");
		edDiaSemanaInicio = new EditText("", 15).setID("edDiaSemanaInicio");
		edDiaSemanaInicio.setEnabled(false);
		lbDtLimite = new LabelName(Messages.DATA_LIMITE);
		edDiaSemana = new EditText("", 15).setID("edDiaSemana");
		edDiaSemana.setEnabled(false);
		edDtLimite = new EditDate().setID("edDtLimite");
		cbRecorrenciaComboBox = new RecorrenciaComboBox().setID("cbRecorrenciaComboBox");
		cbRecorrenciaComboBox.setSelectedIndex(0);
		edHrAgendaVisita = new EditTimeMask(EditTimeMask.FORMATO_HHMM).setID("edHrAgendaVisita");
		edHrFimAgendaVisita = new EditTimeMask(EditTimeMask.FORMATO_HHMM).setID("edHrFimAgendaVisita");
		edNuSequencia = new EditNumberInt("9999999999", 9).setID("edNuSequencia");
	}

	//@Override
	protected BaseDomain screenToDomain() throws SQLException {
		AgendaCadastro agendaCadastro = (AgendaCadastro) getDomain();
		agendaCadastro.nuDiaSemana = -1;
		if (cbModoAgendaVisitaComboBox.getSelectedIndex() == 0) {
			setDadosPorData(agendaCadastro);
		} else {
			setDadosPorDiaDaSemana(agendaCadastro);
			
		}
		agendaCadastro.flTipoFrequencia = cbRecorrenciaComboBox.getValue();
		if (ValueUtil.isNotEmpty(edHrAgendaVisita.getValue())) {
			agendaCadastro.hrAgenda = edHrAgendaVisita.getText();
		}
		if (ValueUtil.isNotEmpty(edHrFimAgendaVisita.getValue())) {
			agendaCadastro.hrAgendaFim = edHrFimAgendaVisita.getText();
		}
		if ((LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() || LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes())) {
			if ((LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita) && ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_UNICA, cbRecorrenciaComboBox.getValue())) {
				agendaCadastro.dtFinal = agendaCadastro.dtBase;
			} else {
				agendaCadastro.dtFinal = edDtLimite.getValue();
			}
		}
		agendaCadastro.nuSequencia = (!LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) ? 1 : edNuSequencia.getValueInt();
		agendaCadastro.nuSequenciaAgenda = AgendaVisitaService.getInstance().getMaxNuSequenciaAgenda((AgendaVisita)getDomain());
		if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
			agendaCadastro.cdTipoAgenda = cbTipoAgendaComboBox.getValue();
		}
		agendaCadastro.dtCriacao = DateUtil.getCurrentDate();
		return agendaCadastro;
	}
	
	@Override
	protected void onSave() throws SQLException {
		setDomain(screenToDomain());
		if (validaRegistroDuplicado()) {
			insertOrUpdate(getDomain());
			if (isEditing()) {
				updateCurrentRecordInList(getDomain());
			} else {
				list();
			}
		} else {
			return;
		}
		fecharWindow();
	}
	
	private boolean validaRegistroDuplicado() throws SQLException {
		if (LavenderePdaConfig.isSolicitaConfirmacaoSubstituirAgenda() || LavenderePdaConfig.isSolicitaConfirmacaoSubstituirAgendaComSenha()) {
			try {
				AgendaVisitaService.getInstance().validateDuplicated(getDomain());
				if (ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_UNICA, ((AgendaVisita)getDomain()).flTipoFrequencia) && AgendaVisitaService.getInstance().isDtBaseAgendaAlreadyExists((AgendaVisita)getDomain())) {
					UiUtil.showErrorMessage(Messages.AGENDAVISITA_RECORRENCIA_UNICA_DATA_JA_SELECIONADA);
					return false;
				}
			} catch (ValidationException e) {
				if (UiUtil.showConfirmYesNoMessage(Messages.CAD_AGENDA_VISITA_DUPLICADA)) {
					if (LavenderePdaConfig.isSolicitaConfirmacaoSubstituirAgenda()) {
						((AgendaCadastro) getDomain()).atualizaAgenda = true;
						return true;
					} else {
						AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow();
						admSenhaDinamicaWindow.setMensagem(Messages.CAD_AGENDA_VISITA_SENHA);
						admSenhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_ATUALIZACAO_AGENDA_VISITA);
						admSenhaDinamicaWindow.setNuCnpj(cliente.nuCnpj);
						if (admSenhaDinamicaWindow.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
							((AgendaCadastro) getDomain()).atualizaAgenda = true;
							return true;
						}
					}
				} 
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				return false;
			}
			return true;
		} else if (ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_UNICA, ((AgendaVisita)getDomain()).flTipoFrequencia) && AgendaVisitaService.getInstance().isDtBaseAgendaAlreadyExists((AgendaVisita)getDomain())) {
			UiUtil.showErrorMessage(Messages.AGENDAVISITA_RECORRENCIA_UNICA_DATA_JA_SELECIONADA);
			return false;
		} else if (AgendaVisitaService.getInstance().isPermiteMultAgendasNoDiaMesmoCliente() && AgendaVisitaService.getInstance().validaAgendaJaExistenteMesmaDataHorario((AgendaVisita)getDomain())) {
			UiUtil.showErrorMessage(Messages.AGENDAVISITA_ERRO_AGENDA_MESMO_HORARIO);
			return false;
		}
		return true;
	}

	private void setDadosPorDiaDaSemana(AgendaCadastro agendaCadastro) {
		agendaCadastro.dtBase = edDtInicio.getValue();
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() && ValueUtil.isNotEmpty(edDtAgenda.getValue())) {
			agendaCadastro.nuDiaSemana =  DateUtil.getDayOfWeek(edDtAgenda.getValue()) + 1; 
			agendaCadastro.flSemanaMes = StringUtil.getStringValue(AgendaVisitaService.getInstance().getFlSemanaMes(edDtAgenda.getValue()));
		}
		if (ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_SEMANAL, cbRecorrenciaComboBox.getValue())) {
			if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
				agendaCadastro.nuDiaSemana = getNuDiaSemana();
			}
			agendaCadastro.flSemanaMes  = ValueUtil.VALOR_SIM;
		} else {
			if (ValueUtil.isNotEmpty(edDtAgenda.getValue())) {
				agendaCadastro.nuDiaSemana =  DateUtil.getDayOfWeek(edDtAgenda.getValue()) + 1; 
				agendaCadastro.flSemanaMes = StringUtil.getStringValue(AgendaVisitaService.getInstance().getFlSemanaMes(edDtAgenda.getValue()));
			}
			if (ValueUtil.isNotEmpty(edDtInicio.getValue())) {
				agendaCadastro.nuDiaSemana =  DateUtil.getDayOfWeek(edDtInicio.getValue()) + 1; 
				agendaCadastro.flSemanaMes = StringUtil.getStringValue(AgendaVisitaService.getInstance().getFlSemanaMes(edDtInicio.getValue()));
			}
		}
	}

	private void setDadosPorData(AgendaCadastro agendaCadastro) {
		if (ValueUtil.isNotEmpty(edDtAgenda.getValue())) {
			agendaCadastro.nuDiaSemana =  DateUtil.getDayOfWeek(edDtAgenda.getValue()) + 1;
			agendaCadastro.flSemanaMes = StringUtil.getStringValue(AgendaVisitaService.getInstance().getFlSemanaMes(edDtAgenda.getValue()));
			if (isSetDatBaseDeAcordoComADataDaAgenda()) {
				agendaCadastro.dtBase = edDtAgenda.getValue();
			}
		}
	}

	//@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException { }

	//@Override
	protected void clearScreen() throws java.sql.SQLException { }

	//@Override
	protected BaseDomain createDomain() throws SQLException {
		return new AgendaCadastro();
	}

	//@Override
	protected String getEntityDescription() {
		return Messages.NOVA_AGENDA_VISITA;
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return AgendaCadastroService.getInstance();
	}
	
	//@Override
	public void initUI() {
		super.initUI();
		addComponentsScreen();
	}
	
	private void addComponentsScreen() {
		UiUtil.add(this, lbNmCliente, lvNmCliente, getLeft(), getTop() + HEIGHT_GAP);
		if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
			UiUtil.add(this, new LabelName(Messages.TIPO_AGENDA), cbTipoAgendaComboBox, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
			UiUtil.add(this, new LabelName(Messages.MODO_AGENDA), cbModoAgendaVisitaComboBox, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(this, lbRecorrencia, cbRecorrenciaComboBox, getLeft(), AFTER + HEIGHT_GAP);
        if (cbModoAgendaVisitaComboBox.getSelectedIndex() == 0) {
    		UiUtil.add(this, lbDtAgenda, edDtAgenda, getLeft(), AFTER + HEIGHT_GAP);
    		UiUtil.add(this, edDiaSemana, edDtAgenda.getX2() + WIDTH_GAP, SAME);
        } else  {
        	if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) {
        		UiUtil.add(this, lbDtAgenda, edDtAgenda, getLeft(), AFTER + HEIGHT_GAP);
        		UiUtil.add(this, edDiaSemana, edDtAgenda.getX2() + WIDTH_GAP, SAME);
        	} else if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes() && ValueUtil.isNotEmpty(cbRecorrenciaComboBox.getValue()) && ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_SEMANAL, cbRecorrenciaComboBox.getValue())) {
        		UiUtil.add(this, lbDiasSemana, pbDsDiaSemana, getLeft(), AFTER + HEIGHT_GAP);
        	}
        	if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
        		if (ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_SEM_FREQUENCIA, cbRecorrenciaComboBox.getValue())) {
        			UiUtil.add(this, lbDtAgenda, edDtAgenda, getLeft(), AFTER + HEIGHT_GAP);
            		UiUtil.add(this, edDiaSemana, edDtAgenda.getX2() + WIDTH_GAP, SAME);
        		} else if (!ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_SEMANAL, cbRecorrenciaComboBox.getValue())) {
            		UiUtil.add(this, lbDtInicio, edDtInicio, getLeft(), AFTER + HEIGHT_GAP);
            		UiUtil.add(this, edDiaSemanaInicio, edDtInicio.getX2() + WIDTH_GAP, SAME);
        		}
        	}
        	
        }
		if ((LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() || LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) && LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita) {
			if (!LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica || !ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_UNICA, cbRecorrenciaComboBox.getValue())) {
				UiUtil.add(this, lbDtLimite, edDtLimite, getLeft(), AFTER + HEIGHT_GAP);
			}
		}
		UiUtil.add(this, lbHoraAgenda, edHrAgendaVisita, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		if (LavenderePdaConfig.isUsaHoraFimAgendaVisita()) {
			UiUtil.add(this, lbHoraAgendaFim, edHrFimAgendaVisita, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
			UiUtil.add(this, lbSequencia, edNuSequencia, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		
		adicionaMarcadoresTela();
	}
	
	private void adicionaMarcadoresTela()  {
		if (!LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) return;
		
		Vector marcadores = buscaMarcadoresDeCliente();
		if (marcadores == null) return;
		
		UiUtil.add(this, lbMarcadores = new LabelName(Messages.CLIENTE_MARCADORES), getLeft(), AFTER + HEIGHT_GAP);
		lbMarcadores.appId = 1;
		
		for (int i = 0; i < marcadores.size(); i++) {
			Marcador marcador = (Marcador) marcadores.items[i];
			LabelValue label = new LabelValue(marcador.dsMarcador);
			label.appId = 1;
			if (marcador.imMarcadorAtivo == null) {
				UiUtil.add(this, label, LEFT + WIDTH_GAP_BIG, AFTER);
				continue;
			}
			Image image = UiUtil.getImage(marcador.imMarcadorAtivo);
			image = UiUtil.getSmoothScaledImage(image, lbNmCliente.getPreferredHeight(), lbNmCliente.getPreferredHeight());
			ImageControl img = new ImageControl(image);
			img.appId = 1;
			label.split(this.getWidth() - img.getWidth() - (WIDTH_GAP_BIG * 5));
			UiUtil.add(this, img, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(this, label, AFTER + WIDTH_GAP_BIG, SAME);
			
		}
	}

	private Vector buscaMarcadoresDeCliente() {
		try {
			return MarcadorService.getInstance().buscaMarcadoresPorCliente(cliente);
		} catch (SQLException e) {
			return null;
		}
	}

	
	//@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbModoAgendaVisitaComboBox) {
					cbModoAgendaChange();
				} else if (event.target == cbRecorrenciaComboBox) {
					cbRecorrenciaChange();
				} else if (event.target instanceof CalendarWmw) {
					edDataAgendaChange();
				}
			default:
				break;
		}
	}
	
	
	//@Override
	protected void beforeSave() throws SQLException {
		validaDadosDaTela();
		AgendaCadastro agendaCadastro = (AgendaCadastro) getDomain();
		agendaCadastro.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaCadastro.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		agendaCadastro.cdCliente = cliente.cdCliente;
	}
	

	private String[] getDiasSemana() {
		if (LavenderePdaConfig.isSistemaPermiteCadastroParaSabadoEDomingo()) {
			return new String[] {FrameworkMessages.DATA_SEMANA_DOMINGO_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEGUNDA_ABREVIADO, FrameworkMessages.DATA_SEMANA_TERCA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUARTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUINTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEXTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SABADO_ABREVIADO};
		}
		if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado()) {
			return new String[] {FrameworkMessages.DATA_SEMANA_SEGUNDA_ABREVIADO, FrameworkMessages.DATA_SEMANA_TERCA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUARTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUINTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEXTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SABADO_ABREVIADO};
		}
		if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo()) {
			return new String[] {FrameworkMessages.DATA_SEMANA_DOMINGO_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEGUNDA_ABREVIADO, FrameworkMessages.DATA_SEMANA_TERCA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUARTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUINTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEXTA_ABREVIADO, };
		}
		return new String[] {FrameworkMessages.DATA_SEMANA_SEGUNDA_ABREVIADO, FrameworkMessages.DATA_SEMANA_TERCA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUARTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_QUINTA_ABREVIADO, FrameworkMessages.DATA_SEMANA_SEXTA_ABREVIADO};
	}
	
	private void cbModoAgendaChange() {
		edDtAgenda.setText("");
		edDiaSemana.setText("");
		pbDsDiaSemana.setSelectedIndex(-1);
		cbRecorrenciaComboBox.load(cbModoAgendaVisitaComboBox.getSelectedIndex() == 0 ? ModoAgendaVisitaComboBox.FLMODO_POR_DATA : ModoAgendaVisitaComboBox.FLMODO_POR_DIA_SEMANA);
		cbRecorrenciaChange();
	}
	
	private void cbRecorrenciaChange() {
		edDtLimite.setText("");
		edDtInicio.setText("");
		edDiaSemanaInicio.setText("");
		scBase.removeAll();
		addComponentsScreen();
		scBase.reposition();
	}

	private void edDataAgendaChange() {
		edDiaSemana.setText("");
		edDiaSemanaInicio.setText("");
		if (ValueUtil.isNotEmpty(edDtAgenda.getValue())) {
			int dia = DateUtil.getDayOfWeek(edDtAgenda.getValue());
			edDiaSemana.setText(DateUtil.getDiaSemana(dia));
		} 
		if (ValueUtil.isNotEmpty(edDtInicio.getValue())) {
			int dia = DateUtil.getDayOfWeek(edDtInicio.getValue());
			edDiaSemanaInicio.setText(DateUtil.getDiaSemana(dia));
		}
	}
	
	private boolean isDataAgendaVisivelNaTela() {
		if (cbModoAgendaVisitaComboBox.getSelectedIndex() == 0 || LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) {
			return true;
		}
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes() && ValueUtil.isNotEmpty(cbRecorrenciaComboBox.getValue()) && ValueUtil.valueEquals(AgendaVisita.FLDSTIPOFREQUENCIA_SEM_FREQUENCIA, cbRecorrenciaComboBox.getValue())) {
			return true;
		}
		return false;
	}
	
	
	private void validaDadosDaTela() {
		if (isDataAgendaVisivelNaTela()) {
			if (ValueUtil.isEmpty(edDtAgenda.getValue())) {
				throw new ValidationException(Messages.MSG_DATA_AGENDA_NAO_INFORMADA);
			}
			if (edDtAgenda.getValue().isBefore(DateUtil.getCurrentDate())) {
				throw new ValidationException(Messages.MSG_DATA_AGENDA_MENOR_DATA_ATUAL);
			}
			if (ValueUtil.isNotEmpty(edDtLimite.getValue())) {
				if (edDtAgenda.getValue().isAfter(edDtLimite.getValue())) {
					throw new ValidationException(Messages.MSG_DATA_AGENDA_MAIOR_DATA_LIMITE);
				}
			}
		}
		if (cbModoAgendaVisitaComboBox.getSelectedIndex() == 1) {
			if (ValueUtil.isNotEmpty(edDtInicio.getValue())) {
				if (edDtInicio.getValue().isBefore(DateUtil.getCurrentDate())) {
					throw new ValidationException(Messages.MSG_DATA_INICIO_MENOR_DATA_ATUAL);
				}
				if (ValueUtil.isNotEmpty(edDtLimite.getValue()) && edDtInicio.getValue().isAfter(edDtLimite.getValue())) {
					throw new ValidationException(Messages.MSG_DATA_INICIO_MAIOR_DATA_LIMITE);
				}
			}
		}
		AgendaVisitaService.getInstance().validaHoraFimDaAgendaDeVisita(edHrAgendaVisita.getValue(), edHrFimAgendaVisita.getValue());
	}
	
	private int getNuDiaSemana() {
		if (ValueUtil.valueEquals(FrameworkMessages.DATA_SEMANA_DOMINGO_ABREVIADO, pbDsDiaSemana.getSelectedItem())) {
			return 1;
		}
		if (ValueUtil.valueEquals(FrameworkMessages.DATA_SEMANA_SEGUNDA_ABREVIADO, pbDsDiaSemana.getSelectedItem())) {
			return 2;
		}
		if (ValueUtil.valueEquals(FrameworkMessages.DATA_SEMANA_TERCA_ABREVIADO, pbDsDiaSemana.getSelectedItem())) {
			return 3;
		}
		if (ValueUtil.valueEquals(FrameworkMessages.DATA_SEMANA_QUARTA_ABREVIADO, pbDsDiaSemana.getSelectedItem())) {
			return 4;
		}
		if (ValueUtil.valueEquals(FrameworkMessages.DATA_SEMANA_QUINTA_ABREVIADO, pbDsDiaSemana.getSelectedItem())) {
			return 5;
		}
		if (ValueUtil.valueEquals(FrameworkMessages.DATA_SEMANA_SEXTA_ABREVIADO, pbDsDiaSemana.getSelectedItem())) {
			return 6;
		} 
		if (ValueUtil.valueEquals(FrameworkMessages.DATA_SEMANA_SABADO_ABREVIADO, pbDsDiaSemana.getSelectedItem())) {
			return 7;
		}
		return -1;
	}
	
	private boolean isSetDatBaseDeAcordoComADataDaAgenda() {
		boolean retorno = ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_QUINZENAL, cbRecorrenciaComboBox.getValue()) || ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_MENSAL, cbRecorrenciaComboBox.getValue());
		return retorno	|| ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_UNICA, cbRecorrenciaComboBox.getValue()) || ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_BIMESTRAL, cbRecorrenciaComboBox.getValue()) || ValueUtil.valueEquals(AgendaVisita.FLTIPOFREQUENCIA_TRIMESTRAL, cbRecorrenciaComboBox.getValue());   
	}
	
	
}
