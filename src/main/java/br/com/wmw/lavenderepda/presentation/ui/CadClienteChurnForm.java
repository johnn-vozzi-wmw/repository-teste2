package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoChurnComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadClienteChurnForm extends BaseLavendereCrudPersonCadForm {
	
	private EditText edCdChurn;
	private EditText edCliente;
	private EditDate edDataEntradaChurn;
	private MotivoChurnComboBox cbMotivoChurn;
	private EditMemo emObservacao;
	private ButtonAction btAlterar;
	private ButtonAction btDesfazer;

	public CadClienteChurnForm() throws SQLException {
		super(Messages.CLIENTECHURN_NOME_ENTIDADE);
		criaComponentes();
	}


	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		ClienteChurn clienteChurn = (ClienteChurn) domain;
		
		edCdChurn.setValue(clienteChurn.cdChurn);
		edCliente.setValue(clienteChurn.getCliente().toString());
		edDataEntradaChurn.setValue(clienteChurn.dtEntradaChurn);
		cbMotivoChurn.setValue(clienteChurn.cdMotivoChurn);
		emObservacao.setValue(clienteChurn.dsObsChurn);
	}
	
	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		super.screenToDomain();
		
		ClienteChurn clienteChurn = (ClienteChurn) getDomain();
		clienteChurn.setMotivoChurn(cbMotivoChurn.getValue());
		clienteChurn.dsObsChurn = emObservacao.getValue();
		return clienteChurn;
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ClienteChurnService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.CLIENTECHURN_CODIGO), edCdChurn, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), edCliente, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CLIENTECHURN_DTENTRADA), edDataEntradaChurn, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CLIENTECHURN_MOTIVO), cbMotivoChurn, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), emObservacao, getLeft(), getNextY());
		UiUtil.add(barBottomContainer, btAlterar , 5);
		UiUtil.add(barBottomContainer, btDesfazer , 5);
		
	}
	
	@Override
	protected String getDsTable() throws SQLException {
		return ClienteChurn.TABLE_NAME;
	}


	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new ClienteChurn();
	}


	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btAlterar) {
					alterarClick();
				} else if (event.target == btDesfazer) {
					bloqueiaTelaParaEdicao();
				}
				break;
				
			}
		}
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		bloqueiaTelaParaEdicao();
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		bloqueiaTelaParaEdicao();
		super.voltarClick();
	}
	
	@Override
	protected void afterSave() throws SQLException {
		getBaseCrudListForm().list();
		bloqueiaTelaParaEdicao();
	}
	
	@Override
	protected void visibleState() throws SQLException {
		btExcluir.setVisible(false);
	}
	
	private void criaComponentes() throws SQLException {
		edCdChurn = new EditText("@@@@@@@@@@", 20);
		edCliente = new EditText("@@@@@@@@@@", 125);
		edDataEntradaChurn = new EditDate();
		cbMotivoChurn = new MotivoChurnComboBox();
		cbMotivoChurn.remove(0);
		emObservacao = new EditMemo("", 3, 500);
		emObservacao.drawBackgroundWhenDisabled = true;
		btAlterar = new ButtonAction(Messages.BOTAO_HABILITAR_ALTERAR, "images/reorganizar.png");
		btDesfazer = new ButtonAction(Messages.BOTAO_HABILITAR_DESFAZER, "images/reorganizar.png");
		
		edCdChurn.setEditable(false);
		edCliente.setEditable(false);
		edDataEntradaChurn.setEditable(false);
	}
	
	private void alterarClick() {
		habitaEdicaoTela();
	}
	
	private void habitaEdicaoTela() {
		btSalvar.setVisible(true);
		btAlterar.setVisible(false);
		btDesfazer.setVisible(true);
		cbMotivoChurn.setEditable(true);
		emObservacao.setEnabled(true);
	}
	
	private void bloqueiaTelaParaEdicao() throws SQLException {
		btSalvar.setVisible(false);
		btDesfazer.setVisible(false);
		btAlterar.setVisible(true);
		cbMotivoChurn.setEditable(false);
		emObservacao.setEnabled(false);
	}

}
