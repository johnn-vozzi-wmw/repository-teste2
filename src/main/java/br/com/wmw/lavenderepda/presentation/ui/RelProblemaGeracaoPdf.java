package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.util.Vector;

public class RelProblemaGeracaoPdf extends WmwWindow {

	private BaseGridEdit grid;
	private Vector pedidoProblemaList;
	
	private RelProblemaGeracaoPdf(String title) {
		super(title);
		makeUnmovable();
		setDefaultRect();
	}
	
	public RelProblemaGeracaoPdf(String title, Vector pedidoProblemaList) {
		this(title);
		this.pedidoProblemaList = pedidoProblemaList;
		carregaGrid();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		int oneChar = fm.charWidth('A');
		StringBuffer dsErro = new StringBuffer();
        dsErro.append(FrameworkMessages.TITULO_MSG_ERRO);
    	int widthDsErro = oneChar * 40;
    	while (fm.stringWidth(dsErro.toString()) < widthDsErro) {
    		dsErro.append("   ");
    	}
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
				new GridColDefinition(Messages.PEDIDO_LABEL_NUPEDIDO, oneChar * 15, LEFT),
				new GridColDefinition(dsErro.toString(), 25, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, FILL - WIDTH_GAP, FILL - footerH);
		addButtonPopup(btFechar);
	}

	public void carregaGrid() {
		String[] pedidoErro = new String[3];
		for (int i = 0; i < pedidoProblemaList.size(); i++) {
			Pedido pedido = (Pedido) pedidoProblemaList.items[i];
			pedidoErro[0] = pedido.rowKey;
			pedidoErro[1] = pedido.nuPedido;
			pedidoErro[2] = pedido.msgProblemaGeracaoPdfOffline;
			grid.add(pedidoErro);
		}
		ajustaTamanhoBotoes();
	}

}
