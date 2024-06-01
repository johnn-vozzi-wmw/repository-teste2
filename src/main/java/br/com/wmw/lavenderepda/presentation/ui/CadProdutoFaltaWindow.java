package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoFaltaService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoUnidadeComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;

public class CadProdutoFaltaWindow extends WmwWindow {

    private LabelValue lbProdutoFalta;
    private BaseComboBox comboBox;
    private ProdutoUnidadeComboBox cbUnidadeAlternativa;
    private ButtonPopup btConfirmar;
    private LabelName lbQtEstoque;
    private LabelName lbQtFaltante;
    private LabelName lbUnidadeAlternativa;
    private LabelValue lbConversaoUnidade;
    private LabelValue lvQtEstoque;
    private LabelValue lvCdUnidadEstoque;
    private LabelValue lvCdUnidadeFalta;
    private LabelValue lvQtFaltante;
    private ItemPedido itemPedido; 
    private boolean apenasCadastraProdutoFalta;
    private boolean acessoManual;
	private boolean editing;
	private boolean enterConfirmaInsercao;
    private EditNumberInt edQtPretendida;
    public double qtItemFisico;
    public double qtItemFalta;
    public int nuAcao = 0;
    
    public CadProdutoFaltaWindow(ItemPedido itemPedido, String msgErro, boolean acessoManual, boolean editing) throws SQLException {
        super(Messages.PRODUTOFALTA_TITULO_CADASTRO);
        this.itemPedido = itemPedido;
        this.acessoManual = acessoManual;
		this.editing = editing;
        lbProdutoFalta = new LabelValue(" - ", LEFT);
        lbProdutoFalta.setID("lbProdutoFalta");
        lbProdutoFalta.autoMultipleLines = true;
        btConfirmar = new ButtonPopup(Messages.PRODUTOFALTA_CONFIRMAR);
        if (msgErro == null) {
        	apenasCadastraProdutoFalta = true;
	        montaCbUnidadeAlternativa(itemPedido);
        	edQtPretendida = new EditNumberInt("9999999999", 9);
        	edQtPretendida.setID("edQtPretendida");
        	lbQtFaltante = new LabelName(Messages.PRODUTOFALTA_REGISTRAR_QTD_PRETENDIDA);
        } else {
        	lbQtEstoque = new LabelName(Messages.PRODUTOFALTA_ESTOQUE);
        	lbQtFaltante = new LabelName(Messages.PRODUTOFALTA_REGISTRAR_QTD_FALTANTE);
        	String[] values = msgErro.split(" ");
        	lvQtFaltante = new LabelValue(findLvQtFaltante(itemPedido, values));
        	lvQtFaltante.setID("lvQtFaltante");
        	lvQtEstoque = new LabelValue(findQtlvEstoque(itemPedido));
        	lvQtEstoque.setID("lvQtEstoque");
			String cdUnidade = ProdutoUnidadeService.getInstance().getDescricaoByExample(new ProdutoUnidade(itemPedido));
        	lvCdUnidadEstoque = new LabelValue(cdUnidade);
        	lvCdUnidadEstoque.setID("lvCdUnidadEstoque");
        	lvCdUnidadeFalta = new LabelValue(cdUnidade);
        	lvCdUnidadeFalta.setID("lvCdUnidadeFalta");
        	lbConversaoUnidade  = new LabelValue(" - ", LEFT);
        	lbConversaoUnidade.setID("lbConversaoUnidade");
        	lbConversaoUnidade.autoMultipleLines = true;
        	if (lvQtEstoque.getDoubleValue() < 0) {
        		lvQtEstoque.setValue(Math.abs(lvQtEstoque.getDoubleValue()));
        	}
        	comboBox = new BaseComboBox();
        	comboBox.setID("comboBox");
        	comboBox.add(new String[] {Messages.PRODUTOFALTA_OPCAO, 
        			MessageUtil.getMessage(Messages.PRODUTOFALTA_QTD_FALTA, new String[] {lvQtEstoque.getValue(), cdUnidade, lvQtFaltante.getValue(), cdUnidade}),
        			MessageUtil.getMessage(Messages.PRODUTOFALTA_QTD, new String[] {lvQtEstoque.getValue(), cdUnidade}),
        			MessageUtil.getMessage(Messages.PRODUTOFALTA_FALTA, new String[] {lvQtFaltante.getValue(), cdUnidade})});
        	
        	setOpcaoDefaultCombo();
        }
        qtItemFisico = itemPedido.getQtItemFisico();
        setDefaultRect();
    }
    

	private void montaCbUnidadeAlternativa(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			lbUnidadeAlternativa = new LabelName(Messages.TOOLTIP_LABEL_UNIDADE_ALTERNATIVA);
			cbUnidadeAlternativa = new ProdutoUnidadeComboBox();
			cbUnidadeAlternativa.setID("cbUnidadeAlternativa");
			cbUnidadeAlternativa.load(itemPedido);
			cbUnidadeAlternativa.setValue(itemPedido.cdUnidade);
		}
	}


	private String findQtlvEstoque(ItemPedido itemPedido) throws SQLException {
		return StringUtil.getStringValueToInterface(EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), itemPedido.getProdutoUnidade(), itemPedido.estoque.qtEstoque), 0);
	}

	private String findLvQtFaltante(ItemPedido itemPedido, String[] values) throws SQLException {
		double qtFaltante;
		if (LavenderePdaConfig.bloqueiaFechamentoPedidoProdutoSemEstoque) {
			qtFaltante =  EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), itemPedido.getProdutoUnidade(), (ValueUtil.getDoubleValueSeparador(values[2])) - itemPedido.estoque.qtEstoque);
		} else {
			qtFaltante =  EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), itemPedido.getProdutoUnidade(), ValueUtil.getDoubleValueSeparador(values[7]));
		}
		if (ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, qtFaltante) < itemPedido.nuConversaoUnidade) {
			qtFaltante = 1;
		}
		return StringUtil.getStringValueToInterface(qtFaltante, 0);
	}

	private void setOpcaoDefaultCombo() {
		if (LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto()) {
			int valorComboPadrao = ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.permiteDecidirModoRegistroFaltaEstoqueProduto) ? 1 : ValueUtil.getIntegerValue(LavenderePdaConfig.permiteDecidirModoRegistroFaltaEstoqueProduto);
			comboBox.setSelectedIndex(ValueUtil.valueEquals(4, valorComboPadrao) ? 0 : valorComboPadrao);
		} else {
			comboBox.setSelectedIndex(0);
		}
	}

	@Override
    public void initUI() {
    	super.initUI();
    	if (!apenasCadastraProdutoFalta) {
    		UiUtil.add(this, lbProdutoFalta, getLeft(), getTop(), FILL, PREFERRED);
    		lbProdutoFalta.setText(Messages.PRODUTOFALTA_LABEL);
    		UiUtil.add(this, lbQtEstoque, lvQtEstoque, getLeft(), AFTER + HEIGHT_GAP_BIG, PREFERRED);
    		UiUtil.add(this, lvCdUnidadEstoque, AFTER + HEIGHT_GAP, SAME);
    		UiUtil.add(this, lbQtFaltante, lvQtFaltante, getLeft(), AFTER + HEIGHT_GAP_BIG, PREFERRED);
    		UiUtil.add(this, lvCdUnidadeFalta, AFTER + HEIGHT_GAP, SAME);
    		UiUtil.add(this, comboBox, getLeft(), AFTER + HEIGHT_GAP_BIG);
    		if (LavenderePdaConfig.usaUnidadeAlternativa) {
    			UiUtil.add(this, lbConversaoUnidade, getLeft(), AFTER, FILL, PREFERRED);
    			montaLabelConversaoUnidade();
			}
    	} else {
    		UiUtil.add(this, lbProdutoFalta, getLeft(), getTop(), FILL, PREFERRED);
    		lbProdutoFalta.setText(acessoManual ? Messages.PRODUTOFALTA_REGISTRAR_FALTA_MANUAL : Messages.PRODUTOFALTA_SEM_ESTOQUE);
    		if (LavenderePdaConfig.usaUnidadeAlternativa) {
	    		UiUtil.add(this, lbUnidadeAlternativa, getLeft(), AFTER, FILL, PREFERRED);
	    		UiUtil.add(this, cbUnidadeAlternativa, getLeft(), AFTER, getPreferredWidth());
    		}
    		UiUtil.add(this, lbQtFaltante, edQtPretendida, getLeft(), AFTER + HEIGHT_GAP_BIG, PREFERRED);
			try {
				setFocusInQtde();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        enterConfirmaInsercao = LavenderePdaConfig.isUsaTeclaEnterComoConfirmacaoItemPedido();
    	addButtonPopup(btConfirmar);
    	addButtonPopup(btFechar);
    }


	private void montaLabelConversaoUnidade() {
			lbConversaoUnidade.setText(Messages.PRODUTOFALTA_CONVERSAO_UNIDADE);
			lbConversaoUnidade.transparentBackground = true;
			lbConversaoUnidade.setForeColor(ColorUtil.labelNameForeColor);
			lbConversaoUnidade.setFont(UiUtil.defaultFontSmall);
	}
    
    @Override
    public void onEvent(Event event) {
    	try {
    		super.onEvent(event);
    		switch (event.type) {
    		case KeyEvent.SPECIAL_KEY_PRESS: {
	    		KeyEvent ke = (KeyEvent) event; 
	    		if (ke.isActionKey() && enterConfirmaInsercao && !(ke.target instanceof EditFiltro)) {
	    			ControlEvent ce = new ControlEvent(ControlEvent.PRESSED, btConfirmar);
	    			postEvent(ce);
	    		}
    		}
	    	case ControlEvent.PRESSED:
	    		if (event.target == btConfirmar) {
	    			btConfirmarClick();
	    		}
				if (event.target == btFechar) {
					desfazAlteracoes();
				}
	    		break;
	    	default:
	    		break;
    		}
    	} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
    	}
    }
    
	private void btConfirmarClick() throws SQLException {
		if (apenasCadastraProdutoFalta) {
			if (edQtPretendida.getValueInt() == 0) {
				UiUtil.showErrorMessage(Messages.PRODUTOFALTA_QTD_NAO_INFORMADA);
				return;
			}
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				itemPedido.cdUnidade = cbUnidadeAlternativa.getValue();
			}
			ProdutoFaltaService.getInstance().registraFaltaProduto(itemPedido, edQtPretendida.getValueInt());
			nuAcao = 4;
		} else {
			if (comboBox.getSelectedItem() == null || Messages.PRODUTOFALTA_OPCAO.equals(comboBox.getSelectedItem())) {
				UiUtil.showErrorMessage(Messages.PRODUTOFALTA_OPCAO_ERRO);
				return;
			}
			if (comboBox.getSelectedIndex() == 1) {
				qtItemFisico = ValueUtil.getDoubleValueSeparador(lvQtEstoque.getValue());
				qtItemFalta = lvQtFaltante.getDoubleValue();
				nuAcao = 1;
				ProdutoFaltaService.getInstance().registraFaltaProduto(itemPedido, lvQtFaltante.getIntegerValue(), qtItemFisico);
			} else if (comboBox.getSelectedIndex() == 2) {
				qtItemFisico = ValueUtil.getDoubleValueSeparador(lvQtEstoque.getValue());
				nuAcao = 2;
			} else if (comboBox.getSelectedIndex() == 3) {
				nuAcao = 3;
				qtItemFisico = 0;
				ProdutoFaltaService.getInstance().registraFaltaProduto(itemPedido, lvQtFaltante.getIntegerValue(), 0);
			}
			if (nuAcao != 4) {
				itemPedido.fromProdutoFaltaWindow = true;
			}
		}
		btFecharClick();
	}

	private void desfazAlteracoes() {
		itemPedido.fromProdutoFaltaWindow = true;
		if (0 != itemPedido.getOldQtItemFisico()) qtItemFisico = itemPedido.getOldQtItemFisico();
	}
	
	public void setFocusInQtde() throws SQLException {
		edQtPretendida.requestFocus();
		edQtPretendida.setCursorPos(edQtPretendida.getLength(), edQtPretendida.getLength());
	}


}
