package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;

public class CadAtendimentoAtivWindow extends WmwWindow {

	public CadAtendimentoAtivForm cadAtendimentoAtivForm;


	public CadAtendimentoAtivWindow(AtendimentoAtiv atendimentoAtiv) throws SQLException {
		super("SACs");
		cadAtendimentoAtivForm = new CadAtendimentoAtivForm(true);
		scrollable = false;
		setDefaultRect();
		cadAtendimentoAtivForm.edit(atendimentoAtiv);
	}

	public void initUI() {
	   try {
		super.initUI();
		UiUtil.add(this, cadAtendimentoAtivForm , LEFT , getTop() , FILL , FILL - footerH);
		cadAtendimentoAtivForm.visibleState();
	   } catch (Throwable e) {e.printStackTrace();}
	}

	
}
