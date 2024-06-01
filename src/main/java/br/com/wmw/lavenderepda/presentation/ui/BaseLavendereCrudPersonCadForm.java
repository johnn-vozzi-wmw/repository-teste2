package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseCrudPersonCadForm;
import br.com.wmw.framework.presentation.ui.ext.HyperLinkEditText;
import br.com.wmw.framework.presentation.ui.ext.LabelHyperLink;
import br.com.wmw.lavenderepda.business.service.AcessoMaterialService;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;

public abstract class BaseLavendereCrudPersonCadForm extends BaseCrudPersonCadForm {

	public BaseLavendereCrudPersonCadForm(String newTitle) {
		super(newTitle);
	}

	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		if (event.type == PenEvent.PEN_DOWN) {
			if (event.target instanceof LabelHyperLink && ((LabelHyperLink) event.target).isContabilizaAcesso()) {
				AcessoMaterialService.getInstance().insereAcesso(((LabelHyperLink) event.target).getText()); 
			} else if (event.target instanceof HyperLinkEditText && ((HyperLinkEditText) event.target).isContabilizaAcesso()) {
				AcessoMaterialService.getInstance().insereAcesso(((HyperLinkEditText) event.target).getText());
			}
		}		
	}

}
