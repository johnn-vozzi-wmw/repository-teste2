package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AplicacaoProduto;
import br.com.wmw.lavenderepda.business.domain.Cultura;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Praga;
import br.com.wmw.lavenderepda.business.service.AplicacaoProdutoService;
import br.com.wmw.lavenderepda.business.service.CulturaService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PragaService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class CadInfoComplementarItemPedidoWindow extends WmwWindow {
	
	private EditDate edDtEntrega;
	private EditNumberInt edPosVinco1, edPosVinco2, edPosVinco3, edPosVinco4, edPosVinco5, edPosVinco6, edPosVinco7, edPosVinco8, edPosVinco9, edPosVinco10;
	private EditText edDsCultura, edDsPraga, edDsDose;
	private BaseButton btFiltrarCultura, btFiltrarPraga;
	private EditNumberFrac edAltura, edLargura, edComprimento;
	private ItemPedido itemPedido;
	private ButtonPopup btSalvar;
	private boolean pedidoAberto;
	public boolean canceladoInsercaoInfoComplementares;
	private String cdCultura, cdPraga;
	private boolean isObrigaCulturaPraga;

	public CadInfoComplementarItemPedidoWindow(ItemPedido itemPedido, boolean pedidoAberto) throws SQLException {
		super(Messages.LABEL_INFOS_COMPLEMENTARES);
		this.itemPedido = itemPedido;
		this.pedidoAberto = pedidoAberto;
		edDtEntrega = new EditDate();
		edAltura = new EditNumberFrac("9999999999", 9);
		edAltura.autoSelect = true;
		edLargura = LavenderePdaConfig.usaValorInteiroLargura() ? new EditNumberFrac("9999999999", 9, 0) : new EditNumberFrac("9999999999", 9);
		edLargura.autoSelect = true;
		edComprimento = LavenderePdaConfig.usaValorInteiroComprimento() ? new EditNumberFrac("9999999999", 9, 0) : new EditNumberFrac("9999999999", 9);
		edComprimento.autoSelect = true;
		edPosVinco1 = new EditNumberInt("9999999999", 9);
		edPosVinco1.autoSelect = true;
		edPosVinco2 = new EditNumberInt("9999999999", 9);
		edPosVinco2.autoSelect = true;
		edPosVinco3 = new EditNumberInt("9999999999", 9);
		edPosVinco3.autoSelect = true;
		edPosVinco4 = new EditNumberInt("9999999999", 9);
		edPosVinco4.autoSelect = true;
		edPosVinco5 = new EditNumberInt("9999999999", 9);
		edPosVinco5.autoSelect = true;
		edPosVinco6 = new EditNumberInt("9999999999", 9);
		edPosVinco6.autoSelect = true;
		edPosVinco7 = new EditNumberInt("9999999999", 9);
		edPosVinco7.autoSelect = true;
		edPosVinco8 = new EditNumberInt("9999999999", 9);
		edPosVinco8.autoSelect = true;
		edPosVinco9 = new EditNumberInt("9999999999", 9);
		edPosVinco9.autoSelect = true;
		edPosVinco10 = new EditNumberInt("9999999999", 9);
		edPosVinco10.autoSelect = true;
		edDsCultura = new EditText("@@@@@@@@@@", 100);
		edDsCultura.drawBackgroundWhenDisabled = true;
		edDsCultura.setValue(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
		edDsCultura.setEditable(false);
		edDsCultura.disableClipboardMenu = true;
		btFiltrarCultura = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		edDsPraga = new EditText("@@@@@@@@@@", 100);
		edDsPraga.drawBackgroundWhenDisabled = true;
		edDsPraga.setEditable(false);
		edDsPraga.disableClipboardMenu = true;
		edDsDose = new EditText("@@@@@@@@@@", 100);
		edDsDose.drawBackgroundWhenDisabled = true;
		edDsDose.setEditable(false);
		edDsDose.disableClipboardMenu = true;
		btFiltrarPraga = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		btSalvar = new ButtonPopup(Messages.LABEL_BT_SALVAR);
		findFlObrigaCulturaPraga();
		setDefaultRect();
		domainToScreen();
		setEditable();
		loadValoresHashComplementares(itemPedido);
	}

	private String labelObrigatoria(String label) {
		if (LavenderePdaConfig.loadCamposObrigatoriosInfoComplementarItemPedido().size() == 0) {
			return label;
		}
		return LavenderePdaConfig.loadCamposObrigatoriosInfoComplementarItemPedido().containsKey(label) ?  label + "*" : label;
	}

	private void setEditable() {
		edDtEntrega.setEditable(pedidoAberto);
		edAltura.setEditable(pedidoAberto);
		edLargura.setEditable(pedidoAberto); 
		edComprimento.setEditable(pedidoAberto);
		edPosVinco1.setEditable(pedidoAberto);
		edPosVinco2.setEditable(pedidoAberto); 
		edPosVinco3.setEditable(pedidoAberto); 
		edPosVinco4.setEditable(pedidoAberto); 
		edPosVinco5.setEditable(pedidoAberto); 
		edPosVinco6.setEditable(pedidoAberto); 
		edPosVinco7.setEditable(pedidoAberto); 
		edPosVinco8.setEditable(pedidoAberto); 
		edPosVinco9.setEditable(pedidoAberto); 
	    edPosVinco10.setEditable(pedidoAberto);
	    btFiltrarPraga.setEnabled(isBtFiltrarPragaVisible());
	}
	
	private boolean isBtFiltrarPragaVisible() {
		return !ValueUtil.valueEquals(edDsCultura.getValue(), FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
	}

	@Override
	public void initUI() {
		super.initUI();
		List<String> camposList = LavenderePdaConfig.getInfoComplementarItemPedidoListaCampos();
		if (camposList.contains("dtEntrega")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_DTENTREGA)), edDtEntrega, getLeft(), getNextY());
		if (camposList.contains("vlAltura")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_ALTURA)), edAltura, getLeft(), getNextY());
		if (camposList.contains("vlLargura")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_LARGURA)), edLargura, getLeft(), getNextY());
		if (camposList.contains("vlComprimento")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLCOMPRIMENTO)), edComprimento, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco1")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO1)), edPosVinco1, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco2")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO2)), edPosVinco2, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco3")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO3)), edPosVinco3, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco4")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO4)), edPosVinco4, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco5")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO5)), edPosVinco5, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco6")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO6)), edPosVinco6, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco7")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO7)), edPosVinco7, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco8")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO8)), edPosVinco8, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco9")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO9)), edPosVinco9, getLeft(), getNextY());
		if (camposList.contains("vlPosVinco10")) UiUtil.add(this, new LabelName(labelObrigatoria(Messages.LABEL_VLPOSVINCO10)), edPosVinco10, getLeft(), getNextY());
		if (camposList.contains("cdCultura")) {
			UiUtil.add(this, new LabelName(isObrigaCulturaPraga ? Messages.LABEL_CDCULTURA+"*" : Messages.LABEL_CDCULTURA), edDsCultura, getLeft(), getNextY(), FILL - btFiltrarCultura.getPreferredWidth() - (WIDTH_GAP * 2) - WIDTH_GAP_BIG);
			if (pedidoAberto) UiUtil.add(this, btFiltrarCultura, RIGHT - WIDTH_GAP_BIG, SAME, PREFERRED, SAME);
		}
		if (camposList.contains("cdPraga")) {
			UiUtil.add(this, new LabelName(isObrigaCulturaPraga ? Messages.LABEL_CDPRAGA+"*" : Messages.LABEL_CDPRAGA), edDsPraga, getLeft(), getNextY(), FILL - btFiltrarPraga.getPreferredWidth() - (WIDTH_GAP * 2) - WIDTH_GAP_BIG);
			if (pedidoAberto) UiUtil.add(this, btFiltrarPraga, RIGHT - WIDTH_GAP_BIG, SAME, PREFERRED, SAME);
		}
		if (camposList.contains("vlDose")) UiUtil.add(this, new LabelName(isObrigaCulturaPraga ? Messages.LABEL_DOSE+"*" : Messages.LABEL_DOSE), edDsDose, getLeft(), getNextY());
		if (pedidoAberto) {
			addButtonPopup(btSalvar);
		}
		addButtonPopup(btFechar);
	}

	private void domainToScreen() throws SQLException {
		List<String> camposList = LavenderePdaConfig.getInfoComplementarItemPedidoListaCampos();
		edDtEntrega.setValue(itemPedido.dtEntrega);
		edAltura.setValue(itemPedido.vlAltura);
		edLargura.setValue(itemPedido.vlLargura);
		edComprimento.setValue(itemPedido.vlComprimento);
		if (ValueUtil.isNotEmpty(itemPedido.cdCultura)) {
			edDsCultura.setValue(getDsCultura(itemPedido.cdCultura));
			edDsPraga.setText(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
		}
		edDsPraga.setValue(ValueUtil.isNotEmpty(itemPedido.cdPraga) ? getDsPraga(itemPedido.cdPraga) : edDsPraga.getValue());
		cdCultura = itemPedido.cdCultura;
		cdPraga = itemPedido.cdPraga;
		edDsDose.setValue(ValueUtil.isNotEmpty(itemPedido.cdCultura) && ValueUtil.isNotEmpty(itemPedido.cdPraga) ? itemPedido.vlDose : ValueUtil.VALOR_NI);
		edPosVinco1.setValue(camposList.contains("vlPosVinco1") ? itemPedido.vlPosVinco1 : 0);
		edPosVinco2.setValue(camposList.contains("vlPosVinco2") ? itemPedido.vlPosVinco2 : 0);
		edPosVinco3.setValue(camposList.contains("vlPosVinco3") ? itemPedido.vlPosVinco3 : 0);
		edPosVinco4.setValue(camposList.contains("vlPosVinco4") ? itemPedido.vlPosVinco4 : 0);
		edPosVinco5.setValue(camposList.contains("vlPosVinco5") ? itemPedido.vlPosVinco5 : 0);
		edPosVinco6.setValue(camposList.contains("vlPosVinco6") ? itemPedido.vlPosVinco6 : 0);
		edPosVinco7.setValue(camposList.contains("vlPosVinco7") ? itemPedido.vlPosVinco7 : 0);
		edPosVinco8.setValue(camposList.contains("vlPosVinco8") ? itemPedido.vlPosVinco8 : 0);
		edPosVinco9.setValue(camposList.contains("vlPosVinco9") ? itemPedido.vlPosVinco9 : 0);
		edPosVinco10.setValue(camposList.contains("vlPosVinco10") ? itemPedido.vlPosVinco10 : 0);
	}
	
	public void onWindowEvent(final Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					salvarClick();
				} else if (event.target == btFechar) {
					canceladoInsercaoInfoComplementares = true;
				} else if (event.target == btFiltrarCultura){
					btFiltroCulturaClick();
				} else if (event.target == btFiltrarPraga) {
					btFiltroPragaClick();
				}
				break;
			}
		}
	}

	private void beforeSave(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaObrigaInfoComplementarItemPedido()) {
			ItemPedidoService.getInstance().validateCamposObrigatoriosInfosComplementarItemPedido(itemPedido);
		}
		validadaCampos();
		if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto()) {
			ItemPedidoService.getInstance().validateCalculoPrecoPorMetroQuadrado(itemPedido);
		}
		ItemPedidoService.getInstance().validaComprimentoLargura(itemPedido);	
	}
	
	private void salvarClick() throws SQLException {
		ItemPedido itemPedidoValidator = (ItemPedido) itemPedido.clone();
		atribuiValores(itemPedidoValidator);
		loadValoresHashComplementares(itemPedidoValidator);
		beforeSave(itemPedidoValidator);
		
		if(LavenderePdaConfig.usaValidaPosicaoVincoLargura() && ItemPedidoService.getInstance().validaPosicaoVincoLargura(itemPedidoValidator)) {
			UiUtil.showErrorMessage(Messages.INFO_COMPLEMENTAR_VINCO_DIFERENTE_DE_LARGURA);
			return;
		}
		
		atribuiValores(itemPedido);
		
		loadValoresHashComplementares(itemPedido);
		if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto()) {
			itemPedido.vlItemPedido = ItemPedidoService.getInstance().realizaCalculaPrecoPorMetroQuadradoItemPedido(itemPedido);
		} else if (LavenderePdaConfig.calculaPrecoPorVolumeProduto) {
			itemPedido.vlIndiceVolume = ItemPedidoService.getInstance().realizaCalculoPrecoPorVolumeProduto(itemPedido);
		}
		PedidoService.getInstance().loadValorBaseItemPedido(itemPedido.pedido, itemPedido);
		fecharWindow();
	}

	private void atribuiValores(ItemPedido itemPedido) {
		itemPedido.dtEntrega = edDtEntrega.getValue();
		itemPedido.vlAltura = edAltura.getValueDouble();
		itemPedido.vlLargura = edLargura.getValueDouble();
		itemPedido.vlComprimento = edComprimento.getValueDouble();
		itemPedido.vlPosVinco1 = edPosVinco1.getValueInt();
		itemPedido.vlPosVinco2 = edPosVinco2.getValueInt();
		itemPedido.vlPosVinco3 = edPosVinco3.getValueInt();
		itemPedido.vlPosVinco4 = edPosVinco4.getValueInt();
		itemPedido.vlPosVinco5 = edPosVinco5.getValueInt();
		itemPedido.vlPosVinco6 = edPosVinco6.getValueInt();
		itemPedido.vlPosVinco7 = edPosVinco7.getValueInt();
		itemPedido.vlPosVinco8 = edPosVinco8.getValueInt();
		itemPedido.vlPosVinco9 = edPosVinco9.getValueInt();
		itemPedido.vlPosVinco10 = edPosVinco10.getValueInt();
		itemPedido.vlDose = edDsDose.getValue();
		itemPedido.cdCultura = cdCultura;
		itemPedido.cdPraga = cdPraga;
	}
	
	private void validadaCampos() {
		Date date = edDtEntrega.getValue();
		if (date != null && date.isBefore(new Date())) {
			throw new ValidationException(Messages.ERRO_CAMPO_OBRIGATORIO_DATAMENORATUAL);
		}
	}
	
	private void loadValoresHashComplementares(ItemPedido itemPedido) {
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_DTENTREGA, edDtEntrega.getValue());
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_ALTURA, edAltura.getValueDouble() != 0.0 ? edAltura.getValueDouble() : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_LARGURA, edLargura.getValueDouble() != 0.0 ? edLargura.getValueDouble() : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLCOMPRIMENTO, edComprimento.getValueDouble() != 0.0 ? edComprimento.getValueDouble() : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO1, edPosVinco1.getValueInt() != 0 ? edPosVinco1.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO2, edPosVinco2.getValueInt() != 0 ? edPosVinco2.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO3, edPosVinco3.getValueInt() != 0 ? edPosVinco3.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO4, edPosVinco4.getValueInt() != 0 ? edPosVinco4.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO5, edPosVinco5.getValueInt() != 0 ? edPosVinco5.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO6, edPosVinco6.getValueInt() != 0 ? edPosVinco6.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO7, edPosVinco7.getValueInt() != 0 ? edPosVinco7.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO8, edPosVinco8.getValueInt() != 0 ? edPosVinco8.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO9, edPosVinco9.getValueInt() != 0 ? edPosVinco9.getValueInt() * 1.0 : null);
		itemPedido.hashValoresInfoComplementares.put(Messages.LABEL_VLPOSVINCO10, edPosVinco10.getValueInt() != 0 ? edPosVinco10.getValueInt() * 1.0 : null);
	}
	
	private void btFiltroCulturaClick() throws SQLException {
		ListCulturaWindow listCultura= new ListCulturaWindow(itemPedido.cdProduto, isObrigaCulturaPraga);
		listCultura.popup();
		if (listCultura.clear) {
			edDsCultura.setValue(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
			edDsPraga.setValue(ValueUtil.VALOR_NI);
			edDsDose.setValue(ValueUtil.VALOR_NI);
			btFiltrarPraga.setEnabled(false);
			cdCultura = null;
			cdPraga = null;
			return;
		} else if (listCultura.cultura != null) {
			edDsCultura.setValue(listCultura.cultura.dsCultura);
			cdCultura = listCultura.cultura.cdCultura;
			edDsPraga.setValue(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
			cdPraga = null;
			edDsDose.setValue(ValueUtil.VALOR_NI);
			btFiltrarPraga.setEnabled(true);
		}
	}

	private void findFlObrigaCulturaPraga() throws SQLException {
		GrupoProduto1 grupoProduto1 = GrupoProduto1Service.getInstance().findGrupoProduto1ByItemPedido(itemPedido);
		isObrigaCulturaPraga = grupoProduto1 != null && ValueUtil.valueEquals(grupoProduto1.flObrigaCulturaPraga, ValueUtil.VALOR_SIM);
	}

	private void btFiltroPragaClick() throws SQLException {
		ListPragaWindow listPraga = new ListPragaWindow(itemPedido.cdProduto, cdCultura, isObrigaCulturaPraga);
		listPraga.popup();
		if (listPraga.clear) {
			edDsPraga.setValue(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
			edDsDose.setValue(ValueUtil.VALOR_NI);
			cdPraga = null;
			return;
		} else if (listPraga.praga != null) {
			edDsPraga.setValue(listPraga.praga.dsNomeCientPraga);
			cdPraga = listPraga.praga.cdPraga;
			edDsDose.setValue(getVlDoseByAplicacaoProdutoFilter());
		}
	}

	private String getVlDoseByAplicacaoProdutoFilter() throws SQLException {
		AplicacaoProduto aplicacaoProduto = new AplicacaoProduto();
		aplicacaoProduto.cdProduto = itemPedido.cdProduto;
		aplicacaoProduto.cdCultura = cdCultura;
		aplicacaoProduto.cdPraga = cdPraga;
		aplicacaoProduto = (AplicacaoProduto) AplicacaoProdutoService.getInstance().findByPrimaryKey(aplicacaoProduto); 
		return aplicacaoProduto == null ? ValueUtil.VALOR_NI : aplicacaoProduto.vlDose;
	}
	
	private String getDsCultura(String cdCultura) throws SQLException {
		Cultura culturaFilter = new Cultura();
		culturaFilter.cdCultura = cdCultura;
		culturaFilter = (Cultura)CulturaService.getInstance().findByPrimaryKey(culturaFilter);
		return culturaFilter == null ? ValueUtil.VALOR_NI : culturaFilter.toString();
	}
	
	private String getDsPraga(String cdPraga) throws SQLException {
		Praga pragaFilter = new Praga();
		pragaFilter.cdPraga = cdPraga;
		pragaFilter = (Praga)PragaService.getInstance().findByPrimaryKey(pragaFilter);
		return pragaFilter == null ? ValueUtil.VALOR_NI : pragaFilter.toString();
	}
	
}
