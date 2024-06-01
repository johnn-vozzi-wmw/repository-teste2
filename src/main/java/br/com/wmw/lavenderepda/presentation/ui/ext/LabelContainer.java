package br.com.wmw.lavenderepda.presentation.ui.ext;

import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;

public class LabelContainer extends SessionContainer {

	private LabelValue lbDsValue;
	//--
    public BaseToolTip tipDsValue;


	public LabelContainer(String value) {
		lbDsValue = new LabelValue();
		lbDsValue.setBackForeColors(ColorUtil.sessionContainerBackColor, ColorUtil.sessionContainerForeColor);
		tipDsValue = new BaseToolTip(lbDsValue, "");
		setDescricao(value);
	}

	public void setDescricao(String value) {
		lbDsValue.setText(value);
		tipDsValue.setText(value);
	}

	@Override
	public void initUI() {
        UiUtil.add(this, lbDsValue, LEFT + UiUtil.BASE_MARGIN_GAP, TOP + BaseContainer.HEIGHT_GAP, FILL - UiUtil.BASE_MARGIN_GAP, PREFERRED);
	}
	
	public static int getStaticHeight() {
		return UiUtil.getLabelPreferredHeight() + BaseUIForm.HEIGHT_GAP * 2;
	}
}
