package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelFechamentoCargaPedidosWindow extends WmwWindow {
	
	private BaseGridEdit grid;
	private LabelValue lbDescricao1;
	private LabelValue lbDescricao2;
	private String cargaPedidoNaoFechadas;
	private ButtonPopup btForcarFechamentoCargaPedido;

	public RelFechamentoCargaPedidosWindow(String cargaNaoFechadas) {
		super(Messages.CARGAPEDIDO_FECHAMENTO_RELATORIO);
		lbDescricao1 = new LabelValue(Messages.CARGAPEDIDO_MSG_FECHARCARGAS_ERRO_PART1, CENTER);
		lbDescricao2 = new LabelValue(Messages.CARGAPEDIDO_MSG_FECHARCARGAS_ERRO_PART2, CENTER);
		cargaPedidoNaoFechadas = cargaNaoFechadas;
		makeUnmovable();
		btForcarFechamentoCargaPedido = new ButtonPopup(Messages.BOTAO_FECHAR_CARGAS_ABR);
		scrollable = false;
		setDefaultRect();
	}
	
	//@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbDescricao1, CENTER + WIDTH_GAP, TOP);
		UiUtil.add(this, lbDescricao2, CENTER + WIDTH_GAP, AFTER);
        int oneChar = fm.charWidth('A');
        StringBuffer dsErro = new StringBuffer();
        dsErro.append(FrameworkMessages.TITULO_MSG_ERRO);
    	int widthDsErro = oneChar * 40;
    	while (fm.stringWidth(dsErro.toString()) < widthDsErro) {
    		dsErro.append("   ");
    	}
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
												new GridColDefinition(Messages.CARGAPEDIDO_CARGA_PEDIDO, oneChar * 9, LEFT),
												new GridColDefinition(dsErro.toString(), 10, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT , AFTER , FILL - WIDTH_GAP, FILL - footerH);
		carregaGrid();
		addButtonPopup(btForcarFechamentoCargaPedido);
		addButtonPopup(btFechar);
	}
	
	public void carregaGrid() {
		String[] cargaPedidosErros = StringUtil.split(cargaPedidoNaoFechadas, '&');
		boolean podeForcarFechamentoCargaPedido = true;
		for (int i = 0; i < cargaPedidosErros.length; i++) {
			String[] dados = StringUtil.split(cargaPedidosErros[i], '*');
			String messageErro = dados[3].replace('|', ' ');
			String[] cargaPedidoErro = {dados[0], dados[1], messageErro};
			grid.add(cargaPedidoErro);
		}
		if (!podeForcarFechamentoCargaPedido) {
			btForcarFechamentoCargaPedido.setVisible(true);
		}
		ajustaTamanhoBotoes();
	}
	
	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}
	
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btForcarFechamentoCargaPedido) {
					btForcarFechamentoCargaPedidoClick();
				}
				break;
			}
		}
	}

	private void btForcarFechamentoCargaPedidoClick() {
		try {
			Vector cargaPedidoGrid = grid.getItemsVector();
			Vector  cargaPedidoList = new Vector();
			CargaPedido cargaPedido = null;
			for (int i = 0; i < cargaPedidoGrid.size(); i++) {
				String[] row = (String[]) cargaPedidoGrid.items[i];
				cargaPedido = (CargaPedido) CargaPedidoService.getInstance().findByRowKey(row[0]);
				if (cargaPedido != null) {
					cargaPedidoList.addElement(cargaPedido);
				}
			}
			cargaPedidoNaoFechadas = CargaPedidoService.getInstance().fecharCargasPedido(cargaPedidoList);
			if (ValueUtil.isEmpty(cargaPedidoNaoFechadas)) {
				UiUtil.showConfirmMessage(Messages.CARGAPEDIDO_MSG_FECHARCARGAS_SUCESSO);
				this.unpop();
			} else {
				UiUtil.showErrorMessage("Problema ao fechar algumas cargas.");
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
		}
		
	}

	

}
