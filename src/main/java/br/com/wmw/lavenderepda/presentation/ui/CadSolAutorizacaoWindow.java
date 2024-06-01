package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;

import java.sql.SQLException;

public class CadSolAutorizacaoWindow extends WmwWindow {

	public CadSolAutorizacaoForm cadSolAutorizacaoForm;
	public SolAutorizacao solAutorizacao;
	public Pedido pedido;
	public ItemPedido itemPedido;

	public CadSolAutorizacaoWindow(Pedido pedido, ItemPedido itemPedido, SolAutorizacao solAutorizacao) throws SQLException {
		super(Messages.SOL_AUTORIZACAO_ANALISE_AUTORIZACAO_LABEL);
		this.solAutorizacao = solAutorizacao;
		cadSolAutorizacaoForm = new CadSolAutorizacaoForm(pedido, itemPedido, true, solAutorizacao);
		btFechar.setText(Messages.SOL_AUTORIZACAO_ANALISE_VOLTAR_LABEL);
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadSolAutorizacaoForm, LEFT, getTop(), FILL, FILL);
		cadSolAutorizacaoForm.showRequestedFocus();
	}
	
}
