package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoChurnComboBox;

public class CadClienteChurnWindow extends WmwCadWindow {
	
	private MotivoChurnComboBox cbMotivoChurn;
	private EditMemo emObservacao;

	public CadClienteChurnWindow() throws SQLException {
		super(Messages.CLIENTECHURN_NOME_ENTIDADE);
		cbMotivoChurn = new MotivoChurnComboBox();
		cbMotivoChurn.remove(0);
		emObservacao = new EditMemo("", 3, 500);
		scrollable = false;
		setDefaultRect();
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		ClienteChurn clienteChurn = (ClienteChurn) getDomain();
		clienteChurn.setMotivoChurn(cbMotivoChurn.getValue());
		clienteChurn.dsObsChurn = emObservacao.getValue();
		return clienteChurn;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		ClienteChurn clienteChurn = (ClienteChurn) domain;
		emObservacao.setValue(clienteChurn.dsObsChurn);
	}

	@Override
	protected void clearScreen() throws SQLException { }

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new ClienteChurn();
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
	public void initUI() {
		super.initUI();
		UiUtil.add(this, new LabelName(Messages.CLIENTECHURN_MOTIVO), cbMotivoChurn, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.OBSERVACAO_LABEL), emObservacao, getLeft(), getNextY());
	}
	
	@Override
	protected void visibleState() throws SQLException {
		btExcluir.setVisible(false);
		ajustaTamanhoBotoes();
	}
	
}
