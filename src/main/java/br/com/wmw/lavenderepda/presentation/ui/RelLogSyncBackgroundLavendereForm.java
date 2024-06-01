package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.RelLogSyncBackgroundForm;

public class RelLogSyncBackgroundLavendereForm extends RelLogSyncBackgroundForm {
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		MainLavenderePda.getInstance().setMenuVisibility(false);
	}
	
	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		MainLavenderePda.getInstance().setMenuVisibility(true);
	}

}
