package br.com.wmw.lavenderepda.presentation.ui.ext;

import br.com.wmw.framework.presentation.ui.IAddId;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.ui.Container;

public class TotalPedidoLabelButton extends Container implements IAddId {
	public String ID;

	public BaseButton btGradeTotaisPorItemPedido;
	public LabelValue lbValue;

	public TotalPedidoLabelButton() {
		lbValue = new LabelValue(" ");
		if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
			btGradeTotaisPorItemPedido = new BaseButton(UiUtil.getColorfulImage("images/grade.png", (UiUtil.getControlPreferredHeight() / 3) * 2, (UiUtil.getControlPreferredHeight() / 3) * 2));
			btGradeTotaisPorItemPedido.transparentBackground = true;
			btGradeTotaisPorItemPedido.useBorder = false;
		}
		lbValue.setID("lbValue");
	}

	public void initUI() {
		UiUtil.add(this, lbValue, LEFT, TOP, PREFERRED, height);
		if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
			UiUtil.add(this, btGradeTotaisPorItemPedido, AFTER, TOP, height, height);
		}
	}

	public void setValue(double value) {
		lbValue.setValue(value);
		adjustComponent();
	}

	public void setValue(int value) {
		lbValue.setValue(value);
		adjustComponent();
	}

	public void setText(String s) {
		lbValue.setText(s);
		adjustComponent();
	}
	
	private void adjustComponent() {
		setRectComponents();
	}

	public String getText() {
		return lbValue.getText();
	}
	public String getValue() {
		return lbValue.getValue();
	}
	public double getDoubleValue() {
		return ValueUtil.getDoubleValue(lbValue.getValue());
	}

	public void setEnabled(boolean enable) {
		if (btGradeTotaisPorItemPedido != null) {
			btGradeTotaisPorItemPedido.setEnabled(enable);
		}
	}

	public void reposition() {
		super.reposition();
		setRectComponents();
	}

	public void setRect(int x, int y, int width, int height) {
		super.setRect(x, y, width, height);
		setRectComponents();
	}

	public void setRectComponents() {
		lbValue.setRect(LEFT, TOP, PREFERRED, height);
		if (btGradeTotaisPorItemPedido != null && btGradeTotaisPorItemPedido.isVisible()) {
			btGradeTotaisPorItemPedido.setRect(AFTER, CENTER, height, height);
		}
	}
	
	public void setGradeEstoqueButtonVisibility(boolean isVisible) {
		if (btGradeTotaisPorItemPedido != null) btGradeTotaisPorItemPedido.setVisible(isVisible);
	}
	
	public void setForeColor(int c) {
		super.setForeColor(c);
		lbValue.setForeColor(c);
	}


	public <T> T setID(String ID) {
		return ADD_ID.setID(this , ID);
	}
	public String getID() {
		return ADD_ID.getID(this);
	}
}