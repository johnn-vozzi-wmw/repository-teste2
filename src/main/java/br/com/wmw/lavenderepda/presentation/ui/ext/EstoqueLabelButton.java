package br.com.wmw.lavenderepda.presentation.ui.ext;

import br.com.wmw.framework.presentation.ui.IAddId;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.sys.Settings;
import totalcross.ui.Container;

public class EstoqueLabelButton extends Container implements IAddId {
	public String ID;

	public BaseButton btAcao;
	public BaseButton btGradeEstoque;
	public LabelValue lbValue;
	public boolean usaSufixoEmEstoque;
	private boolean naoUsaQuantidadeFracionada;
	private String dsLocalEstoque;

	public EstoqueLabelButton(boolean naoUsaQuantidadeFracionada) {
		lbValue = new LabelValue(" ");
		this.naoUsaQuantidadeFracionada = naoUsaQuantidadeFracionada;
	}

	public void initUI() {
		UiUtil.add(this, lbValue, LEFT, TOP, PREFERRED, height);
		if (LavenderePdaConfig.usaEstoqueOnline && !LavenderePdaConfig.usaControleEstoquePorRemessa) {
			btAcao = new BaseButton(UiUtil.getColorfulImage("images/reload.png", (height / 3) * 2, (height / 3) * 2));
			btAcao.transparentBackground = true;
			btAcao.useBorder = false;
			UiUtil.add(this, btAcao, AFTER, TOP, height, height);
		}
		if (showGradeEstoqueButton()) {
			btGradeEstoque = new BaseButton(UiUtil.getColorfulImage("images/grade.png", (height / 3) * 2, (height / 3) * 2));
			btGradeEstoque.transparentBackground = true;
			btGradeEstoque.useBorder = false;
			UiUtil.add(this, btGradeEstoque, AFTER, TOP, height, height);
		}
	}

	public void setValue(double value) {
		if (naoUsaQuantidadeFracionada) {
			lbValue.setValue(ValueUtil.getIntegerValue(value));
		} else {
			lbValue.setValue(value);
		}
		adjustComponent();
	}
	
	public void setValue(double value, String dsLocalEstoque) {
		this.dsLocalEstoque = dsLocalEstoque;
		setValue(value);
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
		if (usaSufixoEmEstoque) {
			if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto() && dsLocalEstoque != null) {
				String cdLocalEstoque = dsLocalEstoque;
				String dsLocalEstoqueSemCd = dsLocalEstoque;
				if (dsLocalEstoque.contains("[")) {
					cdLocalEstoque = dsLocalEstoque.substring(dsLocalEstoque.indexOf("["));
					dsLocalEstoqueSemCd = dsLocalEstoque.substring(0, dsLocalEstoque.indexOf("["));
				}
				dsLocalEstoque = StringUtil.getStringAbreviada(dsLocalEstoqueSemCd, Settings.screenWidth - UiUtil.fontVerySmall.fm.stringWidth(Messages.MSG_QTD_LOCAL_ESTOQUE + cdLocalEstoque)) + cdLocalEstoque;
				lbValue.setText(MessageUtil.getMessage(Messages.MSG_QTD_LOCAL_ESTOQUE, new String[]{lbValue.getValue(), dsLocalEstoque}));
			} else {
				lbValue.setText(MessageUtil.getMessage(Messages.MSG_QTD_ESTOQUE, lbValue.getValue()));
			}
		}
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
		if (btGradeEstoque != null) {
			btGradeEstoque.setEnabled(enable);
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
		if (btAcao != null && btAcao.isVisible()) {
			btAcao.setRect(AFTER, CENTER, height, height);
		}
		if (btGradeEstoque != null && btGradeEstoque.isVisible()) {
			btGradeEstoque.setRect(AFTER, CENTER, height, height);
		}
	}
	
	public void setForeColor(int c) {
		super.setForeColor(c);
		lbValue.setForeColor(c);
	}
	
	public void setBtGradeVisibleOnly(boolean isVisible) {
		setGradeEstoqueButtonVisibility(isVisible);
		setAcaoButtonVisibility(!isVisible);
	}
	
	public void setAcaoButtonVisibility(boolean isVisible) {
		if (btAcao != null) btAcao.setVisible(isVisible);
	}
	
	public void setGradeEstoqueButtonVisibility(boolean isVisible) {
		if (btGradeEstoque != null) btGradeEstoque.setVisible(showGradeEstoqueButton() && isVisible);
	}
	
	private boolean showGradeEstoqueButton() {
		return LavenderePdaConfig.isConfigGradeProduto();
	}

	public <T> T setID(String ID) {
		return ADD_ID.setID(this , ID);
	}
	public String getID() {
		return ADD_ID.getID(this);
	}
}
