package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.print.PedidoConsignacaoManagerPrint;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDevolucaoPedidoConsignadoForm extends LavendereCrudListForm {
	
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lvQtTotalItens;
	public CadPedidoForm cadPedidoForm;
	private ButtonOptions bmOpcoes;
	
	private Pedido pedido;

	public ListDevolucaoPedidoConsignadoForm(Pedido pedido, CadPedidoForm cadPedidoForm) throws SQLException {
		super(Messages.PEDIDO_CONSIGNACAO_LISTA_DEVOLUCAO);
		this.pedido = pedido;
		this.cadPedidoForm = cadPedidoForm;
		setBaseCrudCadForm(new CadDevolucaoPedidoConsignadoForm(pedido));
		listResizeable = false;
		constructorListContainer();
		sessaoTotalizadores = new SessionTotalizerContainer();
		lvQtTotalItens = new LabelTotalizador("999999999,999");
		btExcluir = new ButtonAction(FrameworkMessages.BOTAO_EXCLUIR, "images/delete.png");
		bmOpcoes = new ButtonOptions();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new PedidoConsignacao();
	}

	@Override
	protected CrudService getCrudService() {
		return PedidoConsignacaoService.getInstance();
	}

	@Override
	protected void onFormStart() {
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(sessaoTotalizadores, lvQtTotalItens, LEFT + UiUtil.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
		UiUtil.add(barBottomContainer, btNovo, 5);
		UiUtil.add(barBottomContainer, btExcluir, 1);
		UiUtil.add(barBottomContainer, bmOpcoes, 3);
		sessaoTotalizadores.resizeHeight();
    	sessaoTotalizadores.resetSetPositions();
    	sessaoTotalizadores.setRect(LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
    	UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight() - sessaoTotalizadores.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) {
		switch (event.type) {
			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == bmOpcoes) {
					if (bmOpcoes.selectedItem.equals(Messages.BOTAO_IMPRESSAO_DEVOLUCAO)) {
						btImprimirDevolucaoClick();
					}
				}
			}
		}
	}
	
	@Override
	protected void excluirClick() throws SQLException {
		PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) getSelectedDomain();
		if (pedidoConsignacao == null) {
			UiUtil.showErrorMessage(Messages.PEDIDO_CONSIGNACAO_MSG_NENHUMA_DEVOLUCAO_SELECIONADA); 
		} else if (pedidoConsignacao.isEnviadoServidor() || pedidoConsignacao.isPedidoConsignacaoIniciadoProcessoEnvio()) {
			UiUtil.showErrorMessage(Messages.PEDIDO_CONSIGNACAO_MSG_DEVOLUCAO_ENVIADO_SERVIDOR);
		} else if (PedidoService.getInstance().isClientePossuiLimiteCreditoConsignado(pedido, false) && PedidoConsignacaoService.getInstance().isPossuiEstoqueDisponivel(pedidoConsignacao, pedido)) {
			if (UiUtil.showConfirmDeleteMessage(Messages.BOTAO_DEVOLUCAO_CONSIGNACAO_PEDIDO)) {
				PedidoService.getInstance().atualizaPedidoAntesDeExcluirDevolucao(pedido, pedidoConsignacao);
				pedidoConsignacao.nuSeqConsignacao = 0;
				PedidoConsignacaoService.getInstance().deleteAllByExample(pedidoConsignacao);
				UiUtil.showConfirmMessage(Messages.PEDIDO_CONSIGNACAO_MSG_DEVOLUCAO_EXCLUIDA_SUCESSO);
				list();
			}
		}
	}
	
	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		cadPedidoForm.afterCrudItemPedido();
	}
	
	@Override
	public void visibleState() {
		super.visibleState();
		btExcluir.setVisible(listContainer.size() > 0);
		bmOpcoes.setVisible(btExcluir.isVisible());
	}
	
	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		if (listContainer.getSelectedItem() != null) {
			return ((PedidoConsignacaoService) getCrudService()).findRegistroDevolvidoSelecionado(getSelectedRowKey());
		} 
		return null;
	}
	
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Vector pedidoConsignacaoList = ((PedidoConsignacaoService) getCrudService()).findItensDevolvidosPorPedido(pedido);
    	pedidoConsignacaoList = ((PedidoConsignacaoService) getCrudService()).getListaAgrupadaPorProdutosEQuePossuamQtDevolvida(pedidoConsignacaoList);
    	calculaTotalItensDevolvidos(pedidoConsignacaoList);
    	return pedidoConsignacaoList;
    }
    
    @Override
    protected void afterList(Vector domainList) {
    	visibleState();
    }
   
	private void calculaTotalItensDevolvidos(Vector pedidoConsignacaoList) {
		double qtTotalItens = 0;
		for (int i = 0; i < pedidoConsignacaoList.size(); i++) {
			PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) pedidoConsignacaoList.items[i];
			qtTotalItens += pedidoConsignacao.qtItemFisico;
		}
		lvQtTotalItens.setValue(Messages.ITEMPEDIDO_LABEL_QT_TOTALITENS + " " + StringUtil.getStringValueToInterface(qtTotalItens));
		lvQtTotalItens.reposition();
		sessaoTotalizadores.reposition();
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		return getItemDevolvido(domain);
	}
	
	private void constructorListContainer() {
    	configListContainer("CDPRODUTO");
    	int itemCount = LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil ? 5 : 4;
    	listContainer = new GridListContainer(itemCount, 2);
    	listContainer.setColsSort(new String[][]{{"Código", "CDPRODUTO"}});
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColTotalizerRight(3, Messages.ITEMPEDIDO_LABEL_VLTOTALITENSPEDIDOS);
    }
	
	private String[] getItemDevolvido(Object domain) throws SQLException {
		PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) domain;
		StringBuffer info1 = new StringBuffer();
		info1.append(pedidoConsignacao.produto.cdProduto).append(" - ").append(StringUtil.getStringValue(pedidoConsignacao.produto.dsProduto));
		StringBuffer info2 = new StringBuffer();
		info2.append(pedidoConsignacao.qtItemFisico).append(" ").append(UnidadeService.getInstance().getDsUnidade(pedidoConsignacao.cdUnidade));
		String[] item = {
				info1.toString(), 
				"", 
				info2.toString(),
				StringUtil.getStringValueToInterface(ValueUtil.round(pedidoConsignacao.vlItemPedido * pedidoConsignacao.qtItemFisico)),
				LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil ? Messages.LOTEPRODUTO_LOTE + " " + pedidoConsignacao.cdLoteProduto : ""};
		return item;
	}
	
	@Override
	public void novoClick() throws SQLException {
		if (pedido.isPercentualDevolucaoClienteUltrapassada(pedido.vlTotalDevolucoes)) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_CONSIGNACAO_MSG_PERCENTUAL_DEVOLUCAO_CLIENTE_ULTRAPASSADA, new String[] {StringUtil.getStringValueToInterface(pedido.getVlPercentualDevolucaoAtingido(pedido.vlTotalDevolucoes)), StringUtil.getStringValueToInterface(pedido.getCliente().vlPctDevolucaoConsig)}));
		} else if (pedido.isPedidoConsignadoVencido()) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_CONSIGNACAO_MSG_CONSIGNACAO_VENCIDA, StringUtil.getStringValue(pedido.getDataVencimentoConsignacao())));
		} else {
			limpaDevolucoesEmCache();
			super.novoClick();
		}
	}

	private void limpaDevolucoesEmCache() {
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.qtDevolvida = 0;
		}
	}
	
	private void addItensOnButtonMenu() {
		bmOpcoes.removeAll();
		if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
			bmOpcoes.addItem(Messages.BOTAO_IMPRESSAO_DEVOLUCAO);
		}
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		addItensOnButtonMenu();
	}
	
	private void btImprimirDevolucaoClick() {
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		try {
			pedido.setPedidoConsignacaoDevolucaoList(null);
			PedidoConsignacaoManagerPrint pedidoConsignacaoManagerPrint = new PedidoConsignacaoManagerPrint(pedido);
			pb.makeUnmovable();
			pb.popupNonBlocking();
			pedidoConsignacaoManagerPrint.imprimePedidoConsignacaoDevolucao(false, true);
			if (LavenderePdaConfig.usaImpressaoComprovanteConsignacaoDevolucao == 2) {
				if (UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAOCONSIGNACAO_DEVOLUCAO_MSG_CONFIRMACAO_COMPROVANTE)) {
					pedidoConsignacaoManagerPrint.imprimePedidoConsignacaoDevolucao(true, true);
				}
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAOCONSIGNACAO_DEVOLUCAO_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}
	
}
