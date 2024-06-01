package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Notificacao;
import br.com.wmw.lavenderepda.business.service.NotificacaoService;
import totalcross.sql.Types;
import totalcross.ui.event.Event;

import java.sql.SQLException;

public class CadNotificacaoForm extends BaseCrudCadForm {

	private EditText edDsTipoNotificacao;
	private LabelValue lvMensagem;
	public ButtonOptions btOpcoes;


	public CadNotificacaoForm() {
		super(Messages.NOTIFICACAO_NOME_ENTIDADE);
		edDsTipoNotificacao = new EditText("@@@@@@@@@@", 100);
		lvMensagem = new LabelValue();
		btOpcoes = new ButtonOptions();
		scrollable = true;
	}

	@Override
	public String getEntityDescription() {
		return Messages.NOTIFICACAO_NOME_ENTIDADE;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return NotificacaoService.getInstance();
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new Notificacao();
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		Notificacao notificacao = (Notificacao) getDomain();
		notificacao.dsTipoNotificacao = edDsTipoNotificacao.getValue();
		notificacao.dsMensagem = lvMensagem.getValue();
		return notificacao;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) {
		Notificacao notificacao = (Notificacao) domain;
		edDsTipoNotificacao.setValue(notificacao.dsTipoNotificacao);
		lvMensagem.setMultipleLinesOriginalBreakText(notificacao.dsMensagem);
	}

	@Override
	protected void clearScreen() {
		edDsTipoNotificacao.setText("");
		lvMensagem.setMultipleLinesOriginalBreakText("");
	}

	@Override
	protected void visibleState() throws SQLException {
		edDsTipoNotificacao.setEnabled(!isEditing());
		lvMensagem.setEnabled(!isEditing());
		btOpcoes.removeItem(Messages.NOTIFICACAO_BUTTON_MARCAR_NAO_LIDO);
		btOpcoes.addItem(Messages.NOTIFICACAO_BUTTON_MARCAR_NAO_LIDO);
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		edDsTipoNotificacao.setEditable(false);
		lvMensagem.setEnabled(false);
		btExcluir.setVisible(false);
		scBase.scrollToOrigin();
		scBase.resize();
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		updateFlido(ValueUtil.VALOR_SIM);
		btOpcoes.setVisible(true);
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(barBottomContainer, btOpcoes, 3);
		UiUtil.add(scBase, new LabelName(Messages.NOTIFICACAO_LABEL_DSTIPONOTIFICACAO), edDsTipoNotificacao, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.NOTIFICACAO_LABEL_DSMENSAGEM), lvMensagem, getLeft(), getNextY());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ButtonOptionsEvent.OPTION_PRESS: {
			if (event.target == btOpcoes) {
				if (Messages.NOTIFICACAO_BUTTON_MARCAR_NAO_LIDO.equals(btOpcoes.selectedItem)) {
					updateFlido(ValueUtil.VALOR_NAO);
					btOpcoes.setVisible(false);
				}
			}
			break;
		}
		}
	}

	private void updateFlido(String flLido) throws SQLException {
		getCrudService().updateColumn(getDomain().getRowKey(), Notificacao.NMCOLUNA_FLLIDO, flLido, Types.VARCHAR);
		((Notificacao) getDomain()).flLido = flLido;
	}

}
