package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CCCliPorTipo;
import totalcross.ui.gfx.Color;

public class RelCCClientePorTipoPedidoWindow extends WmwWindow {

	private EditNumberFrac edVlFinal;
	private EditNumberFrac edvlMort;
	private EditNumberFrac edvlPeso;
	private EditNumberFrac edvlQuebra;
	private EditText ednuDocPeso;
	private EditText ednuDocQuebra;
	private EditText ednuDocMort;
	private LabelValue lbDoc;

	public RelCCClientePorTipoPedidoWindow() {
		super("Previsão do Pedido");
		edvlMort = new EditNumberFrac("999999999", 5, 2);
		edvlMort.setEnabled(false);
		edvlPeso = new EditNumberFrac("999999999", 5, 2);
		edvlPeso.setEnabled(false);
		edvlQuebra = new EditNumberFrac("999999999", 5, 2);
		edvlQuebra.setEnabled(false);
		edVlFinal = new EditNumberFrac("999999999", 5, 2);
		edVlFinal.setEnabled(false);
		ednuDocPeso = new EditText("",10);
		ednuDocPeso.setEnabled(false);
		ednuDocQuebra = new EditText("",10);
		ednuDocQuebra.setEnabled(false);
		ednuDocMort = new EditText("",10);
		ednuDocMort.setEnabled(false);
		lbDoc = new LabelValue(Messages.CCCLIPORTIPO_LABEL_DOC);
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		btFechar.setText(FrameworkMessages.BOTAO_FECHAR);
		UiUtil.add(this, new LabelName(Messages.CCCLIPORTIPO_LABEL_VLPESO), getLeft(), TOP + HEIGHT_GAP, PREFERRED, PREFERRED);
		UiUtil.add(this, edvlPeso, getLeft(), AFTER, PREFERRED - (WIDTH_GAP * 5), PREFERRED);
		UiUtil.add(this, lbDoc.getText(), AFTER + WIDTH_GAP * 2, SAME);
		UiUtil.add(this, ednuDocPeso, AFTER + WIDTH_GAP, SAME);
		UiUtil.add(this, new LabelName(Messages.CCCLIPORTIPO_LABEL_VLQUEBRA), getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, edvlQuebra, getLeft(), AFTER, PREFERRED - (WIDTH_GAP * 5), PREFERRED);
		UiUtil.add(this, lbDoc.getText(), AFTER + WIDTH_GAP * 2, SAME);
		UiUtil.add(this, ednuDocQuebra, AFTER + WIDTH_GAP, SAME);
		UiUtil.add(this, new LabelName(Messages.CCCLIPORTIPO_LABEL_VLMORTALIDADE), getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, edvlMort, getLeft(), AFTER, PREFERRED - (WIDTH_GAP * 5), PREFERRED);
		UiUtil.add(this, lbDoc.getText(), AFTER + WIDTH_GAP * 2, SAME);
		UiUtil.add(this, ednuDocMort, AFTER + WIDTH_GAP, SAME);
		UiUtil.add(this, new LabelName(Messages.CCCLIPORTIPO_LABEL_PREV_VLGERAL), getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, edVlFinal, getLeft(), AFTER, PREFERRED - (WIDTH_GAP * 5), PREFERRED);
	}

	public void domainToScreen(CCCliPorTipo cCCliPeso , CCCliPorTipo cCCliQuebraAndMort , double vlTotalPedido) {
		edvlMort.setValue(cCCliQuebraAndMort.vlMortalidade);
		edvlQuebra.setValue(cCCliQuebraAndMort.vlQuebra);
		edvlPeso.setValue(cCCliPeso.vlPeso);
		double vlSaldoCC = (cCCliQuebraAndMort.vlMortalidade  + cCCliQuebraAndMort.vlQuebra) + cCCliPeso.vlPeso;
		double vlFinalCC = vlTotalPedido - vlSaldoCC;
		edVlFinal.setValue(vlFinalCC);
		if (edVlFinal.getValueDouble() == 0) {
			edVlFinal.setForeColor(ColorUtil.componentsForeColor);
    	} else if (edVlFinal.getValueDouble() < 0) {
    		edVlFinal.setForeColor(Color.RED);
    	} else {
    		edVlFinal.setForeColor(Color.BLUE);
    	}
		ednuDocPeso.setValue(cCCliPeso.nuDocumento);
		ednuDocQuebra.setValue(cCCliQuebraAndMort.nuDocumento);
		ednuDocMort.setValue(cCCliQuebraAndMort.nuDocumento);
	}

}
