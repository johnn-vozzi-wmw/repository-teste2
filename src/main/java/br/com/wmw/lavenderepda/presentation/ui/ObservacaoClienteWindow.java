package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;

public class ObservacaoClienteWindow extends WmwWindow {

	private LabelName lbObs;
	private EditMemo edObs;

	public ObservacaoClienteWindow(String dsObservacao) {
		super(Messages.OBSERVACAO_CLIENTE);
		edObs = new EditMemo("@@@@@@@@@@", 6, 4000);
		edObs.setText(dsObservacao);
		edObs.setEditable(false);
		lbObs = new LabelName(Messages.PESQUISA_MERCADO_TITULO_OBSERVACAO);
		btFechar.setText(FrameworkMessages.BOTAO_OK);
		setRect(8, 65);
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbObs, edObs, getLeft(), getNextY());
	}

}
