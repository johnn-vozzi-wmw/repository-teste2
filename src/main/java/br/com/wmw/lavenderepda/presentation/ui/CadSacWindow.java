package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;

public class CadSacWindow extends WmwWindow {

	private CadSacForm cadSacForm;

    public CadSacWindow() throws SQLException {
		super("SAC");
		cadSacForm = new CadSacForm(true);
		scrollable = true;
		setDefaultRect();
    }

	public void initUI() {
	   try {
		super.initUI();
		cadSacForm.cadSacWindow = this;
		UiUtil.add(this, cadSacForm, LEFT , getTop() , FILL , FILL - footerH);
		cadSacForm.visibleState();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void edit(BaseDomain baseDomain) throws SQLException {
		cadSacForm.edit(baseDomain);
	}


}