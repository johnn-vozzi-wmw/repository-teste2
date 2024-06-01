package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BarContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMeta;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMetaAtua;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMetaHist;
import br.com.wmw.lavenderepda.business.service.CentroCustoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaAtuaService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaHistService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadPlatVendaMetaForm extends BaseLavendereCrudPersonCadForm {
	
	private int TABPANEL_PLANEJAMENTOMETAS;
	private int TABPANEL_HISTORICO;
	
	private SessionContainer containerPlanejar;
	private SessionContainer containerHistorico;
	
	private final boolean usuarioSupervisor = SessionLavenderePda.isUsuarioSupervisor();
	private boolean saveAll;
	private boolean topFilters;
	
	private Map<Integer, PlatVendaMeta> platVendaMetaHash;
	private PlatVendaMeta platVendaMeta;
	private LabelName lbCentroCusto;
	private LabelName lbPlataformaVenda;
	private LabelName lbCliente;
	private LabelValue lbDsCentroCusto;
	private LabelValue lbDsPlataformaVenda;
	private LabelValue lbDsCliente;
	private BaseGridEdit gridPlanejamento;
	private BaseGridEdit gridHistorico;
	private EditNumberFrac edPlanejamento;
	private LabelValue lvTotalPlanejamentoRep;
	private LabelValue lvTotalPlanejamentoSup;
	private LabelValue lvTotalContratada;
	private ButtonAction btConcluirPlanej;
	
	private BarContainer barTopListContainer;
	private ButtonAction btResize;
	
	private ListPlanejarVendaMetaForm listPlanejarVendaMetaForm;
	
	public CadPlatVendaMetaForm(PlatVendaMeta platVendaMeta, ListPlanejarVendaMetaForm listPlanejarVendaMetaForm) throws SQLException {
		super(Messages.MENU_PLANEJAR_META);
		this.platVendaMeta = platVendaMeta;
		this.listPlanejarVendaMetaForm = listPlanejarVendaMetaForm;
		lbCentroCusto = new LabelName(Messages.PLANEJAMENTOMETAVENDA_LABEL_CENTRO_DE_CUSTO);
		lbPlataformaVenda = new LabelName(Messages.PLANEJAMENTOMETAVENDA_LABEL_PLATAFORMA_DE_VENDA);
		lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		lbDsCentroCusto = new LabelValue("");
		lbDsPlataformaVenda = new LabelValue("");
		lbDsCliente = new LabelValue("");
		edPlanejamento = new EditNumberFrac("99999999", 9);
		platVendaMetaHash = new HashMap<>();
		lvTotalPlanejamentoRep = new LabelValue();
		lvTotalPlanejamentoSup = new LabelValue();
		lvTotalContratada = new LabelValue();
		btConcluirPlanej = new ButtonAction(usuarioSupervisor ? Messages.PLANEJAMENTOMETAVENDA_LABEL_ENCERRAR_PLANEJ : Messages.PLANEJAMENTOMETAVENDA_LABEL_CONCLUIR_PLANEJ, "images/concluirPlanejamentoMeta.png");
		
		containerPlanejar = new SessionContainer();
		containerPlanejar.setBackColor(ColorUtil.componentsBackColor);
		containerHistorico = new SessionContainer();
		containerHistorico.setBackColor(ColorUtil.componentsBackColor);
		
        barTopListContainer = new BarContainer();
		btResize = new ButtonAction("");
		btResize.setBorder(BORDER_NONE);
		btResize.transparentBackground = true;   
		barBottomContainer.setVisible(true);
		
		labelSettings();
	}

	private void labelSettings() throws SQLException {
		lbDsCentroCusto.setValue(CentroCustoService.getInstance().findCentroCusto(platVendaMeta.cdEmpresa, platVendaMeta.cdRepresentante, platVendaMeta.cdCentroCusto).toString());
		lbDsPlataformaVenda.setValue(PlataformaVendaService.getInstance().findPlataformaVenda(platVendaMeta.cdEmpresa, platVendaMeta.cdPlataformaVenda).toString());
		lbDsCliente.setValue(ClienteService.getInstance().getCliente(platVendaMeta.cdEmpresa, platVendaMeta.cdRepresentante, platVendaMeta.cdCliente).toString());
	}
	
	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		return new PlatVendaMeta();
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		//
	}

	@Override
	protected void clearScreen() throws SQLException {
		//
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new PlatVendaMeta();
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PlatVendaMetaService.getInstance();
	}
	
	@Override
	protected void addComponentesFixosInicio() throws SQLException {
		Container tabPanel = tabDinamica.getContainer(TABPANEL_PLANEJAMENTOMETAS);
		UiUtil.add(tabPanel, lbCliente, lbDsCliente, getLeft(), TOP);
		UiUtil.add(tabPanel, lbCentroCusto, lbDsCentroCusto, SAME, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(tabPanel, lbPlataformaVenda, lbDsPlataformaVenda, SAME, AFTER + HEIGHT_GAP_BIG);
		
    	UiUtil.add(tabPanel, barTopListContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, UiUtil.getListContainerBarTopHeight());
		int iconsDefaultWidthHeight = (int)(((double)barTopListContainer.getHeight() / 5) * 4);
		btResize.setImage(getBtResizeImage(iconsDefaultWidthHeight));
		UiUtil.add(barTopListContainer, btResize, RIGHT - barTopListContainer.getHeight() / 4, CENTER, iconsDefaultWidthHeight, iconsDefaultWidthHeight);
		
		montaGridPlanejamento();
		UiUtil.add(tabPanel, gridPlanejamento, LEFT, AFTER + HEIGHT_GAP, FILL, PREFERRED);
		carregaGridPlanejamento();
		gridPlanejamentoSettings();
		
		tabPanel = tabDinamica.getContainer(TABPANEL_HISTORICO);
		montaGridHistorico();
		UiUtil.add(tabPanel, gridHistorico, LEFT, TOP, FILL, FILL);
		carregaGridHistorico();
		gridHistoricoSettings();
		addBarButtons();
		if (isAllBlocked()) {
			disableBarButtons();
		}
	}
	
	private Image getBtResizeImage(int imageHeigth) {
		Image resizeImg = UiUtil.getImage("images/resize.png");
		resizeImg.applyColor2(ColorUtil.baseForeColorSystem);
		return UiUtil.getSmoothScaledImage(resizeImg, imageHeigth, imageHeigth);
	}   
	
	private void carregaGridPlanejamento() throws SQLException {
		Vector listPlatVendaMeta = PlatVendaMetaService.getInstance().getAllByPeriodo(platVendaMeta, false);
		double totalContratada = 0;
		double totalPlanejamentoRep = 0;
		double totalPlanejamentoSup = 0;
		int size = listPlatVendaMeta.size();
		for (int i = 0; i < size; i++) {
			PlatVendaMeta platVendaMetaRef = (PlatVendaMeta)listPlatVendaMeta.items[i];
			PlatVendaMetaAtuaService.getInstance().setPropertiesByAtuaIfExists(platVendaMetaRef);
			gridPlanejamento.add(new String[] {
					platVendaMetaRef.getRowKey(),
					Date.getMonthName(platVendaMetaRef.dtMeta.getMonth()) + " - " + platVendaMetaRef.dtMeta.getYear(),
					StringUtil.getStringValueToInterface(platVendaMetaRef.vlMetaContratada),
					StringUtil.getStringValueToInterface(platVendaMetaRef.vlMetaPlanejadaRep),
					StringUtil.getStringValueToInterface(platVendaMetaRef.vlMetaPlanejadaSup)
				}, i);
			
			totalContratada += platVendaMetaRef.vlMetaContratada;
			totalPlanejamentoRep += platVendaMetaRef.vlMetaPlanejadaRep;
			totalPlanejamentoSup += platVendaMetaRef.vlMetaPlanejadaSup;
			platVendaMetaHash.put(i, platVendaMetaRef);
		}
		lvTotalContratada.setValue(totalContratada);
		lvTotalPlanejamentoRep.setValue(totalPlanejamentoRep);
		lvTotalPlanejamentoSup.setValue(totalPlanejamentoSup);
		gridPlanejamento.add(new String[] {
					"",
					Messages.LABEL_TOTAL,
					lvTotalContratada.getValue(),
					lvTotalPlanejamentoRep.getValue(),
					lvTotalPlanejamentoSup.getValue()
				}, gridPlanejamento.size());
	}
	
	private void carregaGridHistorico() throws SQLException {
		PlatVendaMetaHist platVendaMetaHist = new PlatVendaMetaHist();
		platVendaMetaHist.cdEmpresa = platVendaMeta.cdEmpresa;
		platVendaMetaHist.cdRepresentante = platVendaMeta.cdRepresentante;
		platVendaMetaHist.cdCliente = platVendaMeta.cdCliente;
		platVendaMetaHist.cdCentroCusto = platVendaMeta.cdCentroCusto;
		platVendaMetaHist.cdPlataformaVenda = platVendaMeta.cdPlataformaVenda;
		platVendaMetaHist.sortAtributte = "DTMETA";
		platVendaMetaHist.sortAsc = ValueUtil.VALOR_SIM;
		Vector listPlatVendaMetaHist = PlatVendaMetaHistService.getInstance().findAllByExample(platVendaMetaHist);
		int size = listPlatVendaMetaHist.size();
		for (int i = 0; i < size; i++) {
			PlatVendaMetaHist platVendaMetaHistRef = (PlatVendaMetaHist) listPlatVendaMetaHist.items[i];
			gridHistorico.add(new String[] {
					Date.getMonthName(platVendaMetaHistRef.dtMeta.getMonth()) + " - " + platVendaMetaHistRef.dtMeta.getYear(),
					StringUtil.getStringValueToInterface(platVendaMetaHistRef.vlVendaRealizada)
			});
		}
	}

	private void montaGridPlanejamento() {
		GridColDefinition[] gridColDefinition = new GridColDefinition[] {
				new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
				new GridColDefinition(Messages.METAS_PERIODO, -25, LEFT),
				new GridColDefinition(Messages.PLANEJAMENTOMETAVENDA_TITULOCOLGRID_CONTRATADA, -25, LEFT),
				new GridColDefinition(Messages.PLANEJAMENTOMETAVENDA_TITULOCOLGRID_PLANEJ_REP, -25, LEFT),
				new GridColDefinition(Messages.PLANEJAMENTOMETAVENDA_TITULOCOLGRID_PLANEJ_SUP, -25, LEFT)
		};
		gridPlanejamento = UiUtil.createGridEdit(gridColDefinition);
	}
	
	private void montaGridHistorico() {
		GridColDefinition[] gridColDefinition = new GridColDefinition[] {
				new GridColDefinition(Messages.METAS_PERIODO, -50, LEFT),
				new GridColDefinition(Messages.PLANEJAMENTOMETAVENDA_TITULOCOLGRID_HISTORICO_TOTALVENDASREALIZADAS, -50, LEFT)
		};
		gridHistorico = UiUtil.createGridEdit(gridColDefinition);
	}
	
	private void gridPlanejamentoSettings() throws SQLException {
		int size = gridPlanejamento.size();
		gridPlanejamento.setGridControllable();
		if (!ValueUtil.valueEquals(this.platVendaMeta.flEncerrado, ValueUtil.VALOR_SIM)) {
			if (!usuarioSupervisor && !ValueUtil.valueEquals(this.platVendaMeta.flPlanejado, ValueUtil.VALOR_SIM)) {
				edPlanejamento = gridPlanejamento.setColumnEditableDouble(3, true, 9);
			} else if (usuarioSupervisor && (ValueUtil.valueEquals(this.platVendaMeta.flPlanejado, ValueUtil.VALOR_SIM) || isAllCellsBlockedForRep(size))) {
				edPlanejamento = gridPlanejamento.setColumnEditableDouble(4, true, 9);
			}
		}
		gridPlanejamento.aligns[1] = LEFT;
		gridPlanejamento.aligns[2] = RIGHT;
		gridPlanejamento.aligns[3] = RIGHT;
		gridPlanejamento.aligns[4] = RIGHT;
		gridPlanejamento.gridController.setRowBackColor(gridPlanejamento.captionsBackColor, size - 1);
		gridPlanejamento.gridController.setRowDisable(size - 1);
		for (int i = 0; i < size - 1; i++) {
			if (usuarioSupervisor) {
				setCelColorByVlMeta(i);
			}
			disableCelByDtBloqueio(i);
		}
	}

	private void setCelColorByVlMeta(int index) {
		double vlContratada = ValueUtil.getDoubleValue(gridPlanejamento.getCellText(index, 2));
		double vlPlanejada = ValueUtil.getDoubleValue(gridPlanejamento.getCellText(index, 3));
		int cor = vlPlanejada < vlContratada ? LavendereColorUtil.COR_FUNDO_CEDULA_PLANEJAR_METAS_METAPLANEJADA_INFERIOR_METACONTRATADA : LavendereColorUtil.COR_FUNDO_CEDULA_PLANEJAR_METAS_METAPLANEJADA_SUPERIOR_METACONTRATADA;
		gridPlanejamento.gridController.setCelBackColor(cor, index, 3);
	}
	
	private void disableCelByDtBloqueio(int index) throws SQLException {
		if (!isDtAtualAfterOrEqualsDtBloqueio(index)) {
			return;
		}
		gridPlanejamento.gridController.setRowDisable(index);
		PlatVendaMeta platVendaMeta = platVendaMetaHash.get(index);
		platVendaMeta.bloqueada = true;
	}
	
	private boolean isAllCellsBlockedForRep(int size) throws SQLException {
		int diasBloqueio = LavenderePdaConfig.getDiasBloqueioMetaRepCadastroMetasPlataformaVenda();
		for (int i = 0; i < size - 1; i++) {
			if (!isDtAtualAfterOrEqualsDtBloqueio(diasBloqueio, i)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isDtAtualAfterOrEqualsDtBloqueio(int index) throws SQLException {
		int diasBloqueio = usuarioSupervisor ? LavenderePdaConfig.getDiasBloqueioMetaSupCadastroMetasPlataformaVenda() : LavenderePdaConfig.getDiasBloqueioMetaRepCadastroMetasPlataformaVenda();
		return isDtAtualAfterOrEqualsDtBloqueio(diasBloqueio, index);
	}
	
	private boolean isDtAtualAfterOrEqualsDtBloqueio(int diasBloqueio, int index) throws SQLException {
		String rowKey = gridPlanejamento.getCellText(index, 0);
		Date dtAtual = DateUtil.getCurrentDate();
		Date dtMeta = ((PlatVendaMeta) PlatVendaMetaService.getInstance().findByRowKey(rowKey)).dtMeta;
		Date dtBloqueio = DateUtil.getDateValue(dtMeta);
		DateUtil.decDay(dtBloqueio, diasBloqueio);
		if (DateUtil.isAfterOrEquals(dtAtual, dtBloqueio)) {
			return true;
		}
		return false;
	}
	
	private void gridHistoricoSettings() {
		gridHistorico.aligns[0] = LEFT;
		gridHistorico.aligns[1] = RIGHT;
	}

	private void atualizaLvTotal() {
		double totalPlanejemanto = 0;
		int coluna = usuarioSupervisor ? 4 : 3;
		int size = gridPlanejamento.size() - 1;
		for (int i = 0; i < size; i++) {
			totalPlanejemanto += ValueUtil.getDoubleValue(gridPlanejamento.getItem(i)[coluna]);
		}
		if (usuarioSupervisor) {
			lvTotalPlanejamentoSup.setValue(totalPlanejemanto);
			gridPlanejamento.setCellText(gridPlanejamento.size() - 1, coluna, lvTotalPlanejamentoSup.getValue());
		} else {
			lvTotalPlanejamentoRep.setValue(totalPlanejemanto);
			gridPlanejamento.setCellText(gridPlanejamento.size() - 1, coluna, lvTotalPlanejamentoRep.getValue());
		}
	}
	
	@Override
	protected void addBarButtons() {
		if (usuarioSupervisor && !ValueUtil.valueEquals(this.platVendaMeta.flEncerrado, ValueUtil.VALOR_SIM)) {
			UiUtil.add(barTopContainer, btSalvar, RIGHT - WIDTH_GAP_BIG, CENTER, PREFERRED);
			UiUtil.add(barBottomContainer, btConcluirPlanej, 5);
		} else if (!usuarioSupervisor && !ValueUtil.valueEquals(this.platVendaMeta.flPlanejado, ValueUtil.VALOR_SIM) && !ValueUtil.valueEquals(this.platVendaMeta.flEncerrado, ValueUtil.VALOR_SIM)) {
			UiUtil.add(barTopContainer, btSalvar, RIGHT - WIDTH_GAP_BIG, CENTER, PREFERRED);
			UiUtil.add(barBottomContainer, btConcluirPlanej, 5);
		}
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == edPlanejamento) {
					edPlanejamentoChange();
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (event.target instanceof EditNumberFrac) {
					atualizaLvTotal();
				}
				break;
			}
			case ControlEvent.PRESSED: {
				if (event.target == btConcluirPlanej) {
					btConcluirPlanejClick();
				} else if (event.target == btResize) {
					if(!topFilters) {
						disableFieldValues();
					} else {
						enableFieldsValues();
					}
					topFilters = !topFilters;
				}
			}
		}
	}
	
	private void edPlanejamentoChange() {
		PlatVendaMeta platVendaMeta = platVendaMetaHash.get(gridPlanejamento.lastRow);
		if (usuarioSupervisor) {
			platVendaMeta.vlMetaPlanejadaSup = edPlanejamento.getValueDouble();
		} else {
			platVendaMeta.vlMetaPlanejadaRep = edPlanejamento.getValueDouble();
		}
		if (edPlanejamento.getValueDouble() == 0) {
			platVendaMeta.edited = false;
		} else {			
			platVendaMeta.edited = true;
		}
	}
	
	private void btConcluirPlanejClick() throws SQLException {
		if (!UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.PLANEJAMENTOMETAVENDA_MSG_CONFIRMAR_CONCLUSAO, new String[] {
				usuarioSupervisor ? Messages.PLANEJAMENTOMETAVENDA_MSG_LABEL_ENCERRAR : Messages.PLANEJAMENTOMETAVENDA_MSG_LABEL_CONCLUIR ,
				usuarioSupervisor ? Messages.PLANEJAMENTOMETAVENDA_MSG_LABEL_ENCERRADOS : Messages.PLANEJAMENTOMETAVENDA_MSG_LABEL_CONCLUIDOS
		}))) {
			return;
		}
		saveAll = true;
		onSave();
	}
	
	@Override
	protected void onSave() throws SQLException {
		if (!usuarioSupervisor) {
			int mes = validaValorPlanejadoRepEPegaMes();
			if (mes == 0) {
				doInsertOrUpdate();
				listPlanejarVendaMetaForm.savedFromCad = true;
				close();
				return;
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PLANEJAMENTOMETAVENDA_MSG_VL_MINIMO_PLANEJAMENTO, new String[] {
						Date.getMonthName(mes),
						StringUtil.getStringValueToInterface(LavenderePdaConfig.getPercValorMinimoMetaRepCadastroMetasPlataformaVenda())
					}));
				saveAll = false;
				return;
			}
		}
		doInsertOrUpdate();
		listPlanejarVendaMetaForm.savedFromCad = true;
		close();
	}
	
	private void doInsertOrUpdate() throws SQLException {
		for (Entry<Integer, PlatVendaMeta> entry : platVendaMetaHash.entrySet()) {
			PlatVendaMeta platVendaMetaValue = entry.getValue();
			if (!platVendaMetaValue.edited && !saveAll) {
				continue;
			}
			if (saveAll) {
				if (usuarioSupervisor) {
					platVendaMetaValue.flEncerrado = ValueUtil.VALOR_SIM;
				} else {
					platVendaMetaValue.vlMetaPlanejadaSup = platVendaMetaValue.vlMetaPlanejadaRep;
					platVendaMetaValue.flPlanejado = ValueUtil.VALOR_SIM;
				}
			}
			PlatVendaMetaAtua platVendaMetaAtua = new PlatVendaMetaAtua(platVendaMetaValue);
			PlatVendaMetaAtuaService.getInstance().insertOrUpdate(platVendaMetaAtua);
			listPlanejarVendaMetaForm.hashCachedList.get(this.platVendaMeta.getRowKey()).items[entry.getKey()] = platVendaMetaValue;
		}
	}

	private int validaValorPlanejadoRepEPegaMes() {
		double porcentagemMinimo =  LavenderePdaConfig.getPercValorMinimoMetaRepCadastroMetasPlataformaVenda() / 100;
		
		for (PlatVendaMeta platVendaMeta : platVendaMetaHash.values()) {
			if (platVendaMeta.bloqueada || (!platVendaMeta.edited && !saveAll)) {
				continue;
			}
			BigDecimal vlMinimo = ValueUtil.getBigDecimalValue(platVendaMeta.vlMetaContratada * porcentagemMinimo).setScale(ValueUtil.doublePrecisionInterface, BigDecimal.ROUND_HALF_DOWN);
			BigDecimal vlMetaPlanejadaRep = ValueUtil.getBigDecimalValue(platVendaMeta.vlMetaPlanejadaRep);
			if (vlMetaPlanejadaRep.compareTo(vlMinimo) < 0) {
				return platVendaMeta.dtMeta.getMonth();
			}
		}
		return 0;
	}

	@Override
	protected String getDsTable() throws SQLException {
		return PlatVendaMeta.TABLE_NAME;
	}
	
	@Override
	protected void addTabsFixas(Vector tableTitles) throws SQLException {
		String planejamentoMetasLabel = Messages.PLANEJAMENTOMETAVENDA_TAB_PLANEJAMENTOMETAS;
		int indexPlanejar = tableTitles.indexOf(planejamentoMetasLabel);
		if (indexPlanejar != -1) {
			tableTitles.removeElement(planejamentoMetasLabel);
		}
		tableTitles.insertElementAt(planejamentoMetasLabel, 0);
		TABPANEL_PLANEJAMENTOMETAS = 0;
		TABPANEL_HISTORICO = 0;
		int posicaoFixa = 1;
		
		String historicoLabel = Messages.TITULOFINANCEIRO_LABEL_DSHISTORICO;
		int indexHistorico = tableTitles.indexOf(historicoLabel);
		if (indexHistorico == -1) {
			tableTitles.insertElementAt(historicoLabel, posicaoFixa);
			posicaoFixa++;
			TABPANEL_HISTORICO = tableTitles.indexOf(historicoLabel);
		} else {
			TABPANEL_HISTORICO = indexHistorico;
		}
	}
	
	private void disableFieldValues() {
		barTopListContainer.setRect(LEFT, TOP, FILL, UiUtil.getListContainerBarTopHeight());
		gridPlanejamento.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
		lbCliente.setVisible(false);
		lbDsCliente.setVisible(false);
		lbCentroCusto.setVisible(false);
		lbDsCentroCusto.setVisible(false);
		lbPlataformaVenda.setVisible(false);
		lbDsPlataformaVenda.setVisible(false);
		
	}
	
	private void enableFieldsValues() {
		barTopListContainer.setRect(LEFT, lbDsPlataformaVenda.getY2() + HEIGHT_GAP, FILL, UiUtil.getListContainerBarTopHeight());
		gridPlanejamento.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, PREFERRED);
		lbCliente.setVisible(true);
		lbDsCliente.setVisible(true);
		lbCentroCusto.setVisible(true);
		lbDsCentroCusto.setVisible(true);
		lbPlataformaVenda.setVisible(true);
		lbDsPlataformaVenda.setVisible(true);
	}

	private boolean isAllBlocked() {
		for (PlatVendaMeta platVendaMeta : platVendaMetaHash.values()) {
			if (!platVendaMeta.bloqueada) {
				return false;
			}
		}
		return true;
	}
	
	private void disableBarButtons() {
		btSalvar.setVisible(false);
		btConcluirPlanej.setVisible(false);
	}
	
}
