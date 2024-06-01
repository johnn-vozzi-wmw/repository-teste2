package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;

public class CadNotaFiscalDynWindow extends WmwWindow  {
	
	private CadNotaFiscalDynForm cadNotaFiscalDynForm;

	public CadNotaFiscalDynWindow(NotaFiscal notaFiscal) throws SQLException {
		super(Messages.NOTA_FISCAL);
		cadNotaFiscalDynForm = new CadNotaFiscalDynForm(notaFiscal);
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadNotaFiscalDynForm, LEFT, getTop(), FILL, FILL);
	}
	
	@Override
	protected void onPopup() {
		try {
			cadNotaFiscalDynForm.carregaCampos();
			cadNotaFiscalDynForm.onFormShow();
			super.onPopup();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		
	}
	

}
