package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFaDes;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DescProgConfigFaDesService;
import br.com.wmw.lavenderepda.business.service.DescProgConfigFamService;
import br.com.wmw.lavenderepda.business.service.DescProgressivoConfigService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoDescProgConfigFamComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.FaixaProgressItem;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.FlowContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelDescontoProgressivoForm extends LavendereCrudListForm {

	private boolean skipListDescForm;
	private final Pedido pedido;
	private DescProgressivoConfig descProgressivoConfig;
	private CadPedidoForm pedidoForm;
	private boolean fromItemPedido;

	private Vector listDescProgConfigFaDes;

	private LabelName lbCliente;
	private LabelName lbVlMinimo;
	private LabelName lbVlMaximo;
	private LabelName lbQtFamiliaVsDesc;
	private LabelName lbFimVigencia;
	private LabelName lbTipoConfigFam;

	private LabelValue lvCliente;
	private LabelValue lvVlMinimoAtual;
	private LabelValue lvVlPedidoAtual;
	private LabelValue lvVlPedidoAtualMax;
	private LabelValue lvVlMinimoTotal;
	private LabelValue lvFimVigencia;

	private LabelValue lvVlMaximoAtual;
	private LabelValue lvVlMaximoTotal;

	private FaixaProgressItem faixaProgressItem;
	private FlowContainer containerVlMinimo;
	private FlowContainer containerVlMaximo;

	private TipoDescProgConfigFamComboBox tipoDescProgConfigFamComboBox;

	public RelDescontoProgressivoForm(DescProgressivoConfig descProgressivoConfig, boolean skipListDescForm, Pedido pedido, CadPedidoForm pedidoForm, boolean fromItemPedido) throws SQLException {
		super(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_REL_TITLE, descProgressivoConfig.dsDescProgressivo));
		this.pedido = pedido;
		loadDomains(descProgressivoConfig);
		this.skipListDescForm = skipListDescForm;
		this.pedidoForm = pedidoForm;
		this.fromItemPedido = fromItemPedido;
		singleClickOn = true;
		constructorListContainer();
		initializeComponents();
	}

	private void loadDomains(DescProgressivoConfig descProgressivoConfig) throws SQLException {
		if (usaPedido()) {
			descProgressivoConfig.pedidoFilter = pedido;
		}
		this.descProgressivoConfig = DescProgressivoConfigService.getInstance().findVlMinimoAndQtdFamiliaByDescProgressivo(descProgressivoConfig);
		DescProgConfigFaDes descProgConfigFaDes = new DescProgConfigFaDes(descProgressivoConfig.cdEmpresa, descProgressivoConfig.cdRepresentante, descProgressivoConfig.cdDescProgressivo);
		descProgConfigFaDes.sortAtributte = "QTFAMILIA";
		descProgConfigFaDes.sortAsc = ValueUtil.VALOR_SIM;
		this.listDescProgConfigFaDes = DescProgConfigFaDesService.getInstance().findAllByExample(descProgConfigFaDes);
	}

	@Override
	protected String getBtVoltarTitle() {
		if (skipListDescForm) {
			if (fromItemPedido) return Messages.ITEMPEDIDO_NOME_ENTIDADE;
			if (pedido != null) return Messages.PEDIDO_NOME_ENTIDADE;
			return Messages.CLIENTE_NOME_ENTIDADE;
		}
		return super.getBtVoltarTitle();
	}

	private void initializeComponents() {
		lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		lbVlMinimo = new LabelName(Messages.DESC_PROG_CONFIG_VL_MINIMO);
		lbVlMaximo = new LabelName(Messages.DESC_PROG_CONFIG_VL_MAXIMO);
		lbQtFamiliaVsDesc = new LabelName(Messages.DESC_PROG_CONFIG_QTD_FAMILIA_X_DESCONTO);
		lbFimVigencia = new LabelName(Messages.DESC_PROG_CONFIG_VIGENCIA_FIM);
		lbTipoConfigFam = new LabelName(Messages.DESC_PROG_CONFIG_TIPOCONFIGFAM);
		lvCliente = new LabelValue(descProgressivoConfig.cliente.toString());
		lvVlMinimoAtual = new LabelValue();
		if (descProgressivoConfig.atingiuVlMinimo()) {
			lvVlMinimoAtual = new LabelValue(Messages.DESC_PROG_CONFIG_VL_MINIMO_JA_ATINGIDO);
			lvVlMinimoAtual.setForeColor(ColorUtil.softGreen);
		} else {
			lvVlMinimoAtual = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_ATUAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlAtingido)));
			lvVlMinimoAtual.setForeColor(ColorUtil.softRed);
		}
		if (descProgressivoConfig.atingiuVlMaximo()) {
			lvVlMaximoAtual = new LabelValue(Messages.DESC_PROG_CONFIG_VL_MAXIMO_JA_ATINGIDO);
			lvVlMaximoAtual.setForeColor(ColorUtil.softRed);
		} else {
			lvVlMaximoAtual = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MAXIMO_ATUAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlAtingidoMax)));
			lvVlMaximoAtual.setForeColor(ColorUtil.softGreen);
		}
		lvVlMinimoTotal = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_TOTAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlMinDescProgressivo)));
		lvVlMaximoTotal = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MAXIMO_TOTAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlMaxDescProgressivo)));
		if (usaPedido()) {
			lvVlPedidoAtual = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_PEDIDO_ATUAL, descProgressivoConfig.vlAtingidoPedidoAtual));
			lvVlPedidoAtual.setForeColor(ColorUtil.softBlue);
			lvVlPedidoAtualMax = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_PEDIDO_ATUAL, descProgressivoConfig.vlAtingidoPedidoAtualMax));
			lvVlPedidoAtualMax.setForeColor(ColorUtil.softBlue);
		}
		int daysBetween = DateUtil.getDaysBetween(descProgressivoConfig.dtFimVigencia, DateUtil.getCurrentDate());
		String dias = daysBetween > 1 ? Messages.DESC_PROG_CONFIG_DIA_PLURAL : Messages.DESC_PROG_CONFIG_DIA_SINGULAR;
		lvFimVigencia = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_DIAS_PARA_APURACAO, new String[] {StringUtil.getStringValue(descProgressivoConfig.dtFimVigencia), StringUtil.getStringValue(daysBetween), dias}));
		lvFimVigencia.setFont(UiUtil.defaultFontSmall);
		tipoDescProgConfigFamComboBox = new TipoDescProgConfigFamComboBox();
		tipoDescProgConfigFamComboBox.setSelectedIndex(1);
	}

	private void constructorListContainer() {
		configListContainer("tb.CDFAMILIADESCPROG");
		listContainer = new GridListContainer(4, 2);
		setColsSort("FLCONSUMIUERP");
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		DescProgConfigFam item = (DescProgConfigFam) domain;
		DescProgConfigFam filter = (DescProgConfigFam) getDomainFilter();
		Vector itens = new Vector();
		itens.addElement(item.toString());
		itens.addElement(ValueUtil.VALOR_NI);
		itens.addElement(MessageUtil.getMessage(Messages.DESC_PROG_FAMILIA_QTPRODUTO, item.qtProdutos));
		if (filter.isFlTipoFamiliaCon()) {
			itens.addElement(getMsgFamiliaConsome(item));
			listContainer.setColColor(3, getItemColor(item.getFlConsumiuErp() || item.getFlConsumiuPdaOutros(), item.getFlConsumiuPda()));
		} else if (filter.isFlTipoFamiliaPro()) {
			itens.addElement(getMsgFamiliaProduz(item));
			listContainer.setColColor(3, getItemColor(item.getFlProduziuErp() || item.getFlProduziuPdaOutros(), item.getFlProduziuPda()));
		} else if (filter.isFlFamAcuValorMin()) {
			itens.addElement(getMsgFamiliaAcumula(item));
			listContainer.setColColor(3, getItemColor(item.getFlAcumulouErp() || item.getFlAcumulouPdaOutros(), item.getFlAcumulouPda()));
		} else if (filter.isFlFamAcuValorMax()) {
			itens.addElement(getMsgFamiliaAcumula(item));
			listContainer.setColColor(3, getItemColor(item.getFlAcumulouMaxErp() || item.getFlAcumulouMaxPdaOutros(), item.getFlAcumulouMaxPda()));
		}
		return (String[]) itens.toObjectArray();
	}

	private String getMsgFamiliaAcumula(DescProgConfigFam item) {
		if (item.getFlAcumulouErp() || item.getFlAcumulouMaxErp() || item.getFlAcumulouPdaOutros() || item.getFlAcumulouMaxPdaOutros()) {
			return Messages.DESC_PROG_FAMILIA_ACUMULOU;
		} else if (item.getFlAcumulouPda() || item.getFlAcumulouMaxPda()) {
			return Messages.DESC_PROG_FAMILIA_ACUMULOU_PEDIDO_ATUAL;
		} else if (descProgressivoConfig.atingiuVlMinimo()){
			return Messages.DESC_PROG_FAMILIA_LISTA_JA_ATINGIU_VL_MINIMO;
		} else if (descProgressivoConfig.atingiuVlMaximo()){
			return Messages.DESC_PROG_FAMILIA_LISTA_JA_ATINGIU_VL_MAXIMO;
		} else {
			return Messages.DESC_PROG_FAMILIA_PENDENTE;
		}
	}

	private String getMsgFamiliaProduz(DescProgConfigFam item) {
		if (item.getFlProduziuErp() || item.getFlProduziuPdaOutros()) {
			return Messages.DESC_PROG_FAMILIA_PRODUZIDO;
		} else if (item.getFlProduziuPda()) {
			return Messages.DESC_PROG_FAMILIA_PRODUZIDO_PEDIDO_ATUAL;
		} else {
			return Messages.DESC_PROG_FAMILIA_PENDENTE;
		}
	}

	private String getMsgFamiliaConsome(DescProgConfigFam item) {
		if (item.getFlConsumiuErp() || item.getFlConsumiuPdaOutros()) {
			return Messages.DESC_PROG_FAMILIA_CONSUMIDO;
		} else if (item.getFlConsumiuPda()) {
			return Messages.DESC_PROG_FAMILIA_CONSUMIDO_PEDIDO_ATUAL;
		} else {
			return "";
		}
	}

	private int getItemColor(boolean erp, boolean pda) {
		if (erp) {
			return ColorUtil.softGreen;
		} else if (pda) {
			return ColorUtil.softBlue;
		} else {
			return ColorUtil.componentsForeColor;
		}
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		DescProgConfigFam descProgConfigFam = new DescProgConfigFam();
		descProgConfigFam.cdEmpresa = descProgressivoConfig.cdEmpresa;
		descProgConfigFam.cdRepresentante = descProgressivoConfig.cdRepresentante;
		descProgConfigFam.cdDescProgressivo = descProgressivoConfig.cdDescProgressivo;
		tipoDescProgConfigFamComboBox.applyValue(descProgConfigFam);
		return descProgConfigFam;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		final DescProgConfigFam descProgConfigFam = (DescProgConfigFam)domain;
		if (usaPedido()) {
			descProgConfigFam.pedidoFilter = pedido;
		}
		descProgConfigFam.cliente = descProgressivoConfig.cliente;
		return DescProgConfigFamService.getInstance().findAllFamByDescProg(descProgConfigFam);
	}

	@Override
	public void onFormExibition() throws SQLException {
		list();
		updateInformation();
	}

	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return domain.toString();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return DescProgConfigFamService.getInstance();
	}
	
	private void updateLabels() {
		lvCliente.setText(descProgressivoConfig.cliente.toString());
		int daysBetween = DateUtil.getDaysBetween(descProgressivoConfig.dtFimVigencia, DateUtil.getCurrentDate());
		String dias = daysBetween > 1 ? Messages.DESC_PROG_CONFIG_DIA_PLURAL : Messages.DESC_PROG_CONFIG_DIA_SINGULAR;
		lvFimVigencia.setText(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_DIAS_PARA_APURACAO, new String[] {StringUtil.getStringValue(descProgressivoConfig.dtFimVigencia), StringUtil.getStringValue(daysBetween), dias}));
		lvFimVigencia.setFont(UiUtil.defaultFontSmall);
	}
	
	private void updateInformation() throws SQLException {
		loadDomains(descProgressivoConfig);
		updateLabels();
		updateVlMinimoContainer();
		updateVlMaximoContainer();
		updateFaixaProgressItem();
		add(listContainer);
	}
	
	private void updateVlMinimoContainer() {
		if (containerVlMinimo != null) remove(containerVlMinimo);
		containerVlMinimo = new FlowContainer(0, 0);
		containerVlMinimo.transparentBackground = true;
		
		if (descProgressivoConfig.vlMinDescProgressivo <= 0) {
			containerVlMinimo.add(new LabelValue(Messages.DESC_PROG_CONFIG_VL_MINIMO_NAO_EXIGIDO));
		} else {

			containerVlMinimo.add(lvVlMinimoAtual);
			lvVlMinimoTotal.setText(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_TOTAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlMinDescProgressivo)));
			
			if (!descProgressivoConfig.atingiuVlMinimo()) {
				lvVlMinimoAtual.setText(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_ATUAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlAtingido)));
				lvVlMinimoAtual.setForeColor(ColorUtil.softRed);
				if (usaPedido()) {
					lvVlPedidoAtual.setText(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_PEDIDO_ATUAL, descProgressivoConfig.vlAtingidoPedidoAtual));
					lvVlPedidoAtual.setForeColor(ColorUtil.softBlue);
					
					containerVlMinimo.add(new LabelValue(Messages.DESC_PROG_CONFIG_SINAL_MAIS));
					containerVlMinimo.add(lvVlPedidoAtual);
				}
			} else {
				lvVlMinimoAtual.setText(Messages.DESC_PROG_CONFIG_VL_MINIMO_JA_ATINGIDO);
				lvVlMinimoAtual.setForeColor(ColorUtil.softGreen);
			}
			
			containerVlMinimo.add(lvVlMinimoTotal);
			
			lvVlMinimoTotal = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MINIMO_TOTAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlMinDescProgressivo)));
		}
		UiUtil.add(this, containerVlMinimo, SAME, AFTER, PARENTSIZE, PREFERRED, lbVlMinimo);
	}
	
	private void updateVlMaximoContainer() {
		if (containerVlMaximo != null) remove(containerVlMaximo);
		containerVlMaximo = new FlowContainer(0, 0);
		containerVlMaximo.transparentBackground = true;
		if (descProgressivoConfig.vlMaxDescProgressivo <= 0) {
			containerVlMaximo.add(new LabelValue(Messages.DESC_PROG_CONFIG_VL_MAXIMO_NAO_EXIGIDO));
		} else {
			containerVlMaximo.add(lvVlMaximoAtual);
			lvVlMaximoTotal.setText(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MAXIMO_TOTAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlMaxDescProgressivo)));
			if (!descProgressivoConfig.atingiuVlMaximo()) {
				lvVlMaximoAtual.setText(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MAXIMO_ATUAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlAtingidoMax)));
				lvVlMaximoAtual.setForeColor(ColorUtil.softGreen);
				if (usaPedido()) {
					lvVlPedidoAtualMax.setText(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MAXIMO_PEDIDO_ATUAL, descProgressivoConfig.vlAtingidoPedidoAtualMax));
					lvVlPedidoAtualMax.setForeColor(ColorUtil.softBlue);
					containerVlMaximo.add(new LabelValue(Messages.DESC_PROG_CONFIG_SINAL_MAIS));
					containerVlMaximo.add(lvVlPedidoAtualMax);
				}
			} else {
				lvVlMaximoAtual.setText(Messages.DESC_PROG_CONFIG_VL_MAXIMO_JA_ATINGIDO);
				lvVlMaximoAtual.setForeColor(ColorUtil.softRed);
			}
			containerVlMaximo.add(lvVlMaximoTotal);
			lvVlMaximoTotal = new LabelValue(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VL_MAXIMO_TOTAL, StringUtil.getStringValueToInterface(descProgressivoConfig.vlMaxDescProgressivo)));
		}
		UiUtil.add(this, containerVlMaximo, SAME, AFTER, PARENTSIZE, PREFERRED, lbVlMaximo);
	}

	private void updateFaixaProgressItem() throws SQLException{
		if (faixaProgressItem != null) remove(faixaProgressItem);
		HashMap<Integer, String> hashMap = new HashMap<>();
		int size = listDescProgConfigFaDes.size();
		for (int i = 0; i < size; i++) {
			DescProgConfigFaDes descProgConfigFaDes = (DescProgConfigFaDes) listDescProgConfigFaDes.items[i];
			String pct = MessageUtil.getMessage(Messages.DESC_PROG_FAIXA_PCT, StringUtil.getStringValueToInterface(descProgConfigFaDes.vlPctDescProg));
			String tooltip = ValueUtil.isNotEmpty(descProgConfigFaDes.dsFaixa) ? descProgConfigFaDes.dsFaixa : MessageUtil.getMessage(Messages.DESC_PROG_FAIXA_DEFAULT, new String[] {StringUtil.getStringValueToInterface(descProgConfigFaDes.qtFamilia), StringUtil.getStringValueToInterface(descProgConfigFaDes.vlPctDescProg)});
			hashMap.put(descProgConfigFaDes.qtFamilia, FaixaProgressItem.montaStringHash(pct, tooltip));
		}
		int qtTotal = ((DescProgConfigFaDes) listDescProgConfigFaDes.items[listDescProgConfigFaDes.size() - 1]).qtFamilia;
		int qtExtra = 0;
		if (usaPedido()) {
			qtExtra = DescProgressivoConfigService.getInstance().countQtExtraByPedido(descProgressivoConfig, pedido, false);
			if (!LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
				descProgressivoConfig.qtAtingido += DescProgressivoConfigService.getInstance().countQtExtraByPedido(descProgressivoConfig, pedido, true);
			}
		}
		faixaProgressItem = new FaixaProgressItem(qtTotal, descProgressivoConfig.qtAtingido, qtExtra, hashMap, Messages.DESC_PROG_FAMILIA_ITEM, 2, ColorUtil.softGreen, ColorUtil.softBlue);
		UiUtil.add(this, faixaProgressItem, getLeft(), AFTER, getWFill(), ValueUtil.getIntegerValue(UiUtil.getControlPreferredHeight() + UiUtil.getLabelPreferredHeight() - WIDTH_GAP_BIG), lbQtFamiliaVsDesc);
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbCliente, lvCliente, LEFT + WIDTH_GAP_BIG, getNextY(), FILL);
		UiUtil.add(this, lbVlMinimo, LEFT + WIDTH_GAP_BIG, getNextY());
		updateVlMinimoContainer();
		UiUtil.add(this, lbVlMaximo, LEFT + WIDTH_GAP_BIG, getNextY());
		updateVlMaximoContainer();
		UiUtil.add(this, lbQtFamiliaVsDesc, getLeft(), AFTER - HEIGHT_GAP, FILL);
		updateFaixaProgressItem();
		UiUtil.add(this, lbFimVigencia, lvFimVigencia, getLeft(), AFTER, FILL);
		UiUtil.add(this, lbTipoConfigFam, tipoDescProgConfigFamComboBox, getLeft(), getNextY(), getWFill());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
		updateLabels();
	}
	
	private boolean usaPedido() {
		return pedido != null;
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == tipoDescProgConfigFamComboBox) {
					String selected = (String) tipoDescProgConfigFamComboBox.getSelectedItem();
					if (selected.equals(Messages.DESC_PROG_FAM_COMBO_CONSOME)) {
						setColsSort("FLCONSUMIUERP");
					} else if (selected.equals(Messages.DESC_PROG_FAM_COMBO_PRODUZ)) {
						setColsSort("FLPRODUZIUERP");
					} else if (selected.equals(Messages.DESC_PROG_FAM_COMBO_ACUMULA)) {
						setColsSort("FLACUMULOUERP");
					}
					list();
				}
				break;
		}
	}


	private void setColsSort(String fourthSort) {
		listContainer.setColsSort(new String[][]{
				{Messages.DESC_PROG_FAMILIA_CDFAMILIADESCPROG, "tb.CDFAMILIADESCPROG"},
				{Messages.DESC_PROG_FAMILIA_DSFAMILIAPROD, "DESCPROGFAMILIA.DSFAMILIAPROD"},
				{Messages.DESC_PROG_FAMILIA_QUANTIDADE, "QTPRODUTOS"},
				{Messages.DESC_PROG_FAMILIA_ACUMULOU, fourthSort},
		});
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		DescProgConfigFam selectedDomain = (DescProgConfigFam) getSelectedDomain();
		selectedDomain.cliente = descProgressivoConfig.cliente;
		RelDescontoProgFamiliaProdForm form = new RelDescontoProgFamiliaProdForm(selectedDomain, pedidoForm);
		show(form);
	}

}
