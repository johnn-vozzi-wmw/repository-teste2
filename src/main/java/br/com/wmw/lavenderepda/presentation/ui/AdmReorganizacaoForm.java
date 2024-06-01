package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class AdmReorganizacaoForm extends BaseUIForm {

	private LabelValue lbInfoReorganizar;
	private BaseButton btReorganizar;

	public AdmReorganizacaoForm() {
		super(Messages.REORGANIZARDADOS_PEDIDO);
	}

	//@Override
	protected void onFormStart() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(false);
		barBottomContainer.setVisible(false);
		//--
		add(lbInfoReorganizar = new LabelValue(MessageUtil.quebraLinhas(Messages.REORGANIZARDADOS_MSG_INFO)), CENTER, CENTER - (height / 12));
		lbInfoReorganizar.align = CENTER;
		//--
		add(btReorganizar = new BaseButton(Messages.REORGANIZARDADOS_LABEL_REORGANIZAR), CENTER, AFTER + (HEIGHT_GAP * 4));
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		if (event.type == ControlEvent.PRESSED) {
			if (event.target == btReorganizar) {
				AdmDeleteDadosAntigosWindow reorganizarDadosForm = new AdmDeleteDadosAntigosWindow();
				reorganizarDadosForm.show(true);
				close();
			}
		}
	}

	//@Override
	public void onFormClose() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(true);
	}

}
