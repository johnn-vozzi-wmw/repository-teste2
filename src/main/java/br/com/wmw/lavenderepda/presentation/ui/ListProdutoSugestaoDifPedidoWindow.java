package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.sys.Convert;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoSugestaoDifPedidoWindow extends WmwListWindow {

	private LabelValue lbMsgFechamento;
	public Pedido pedido;
	private CadItemPedidoForm cadItemPedidoForm;
	public boolean onFechamentoPedido;
	public boolean onAcessoManual;
	private ButtonPopup btCancelar;
	private ButtonPopup btFecharPedido;

	public ListProdutoSugestaoDifPedidoWindow(Pedido pedido, boolean onFechamentoPedido) {
		super(Messages.PEDIDO_LABEL_SUGESTAO_DIF_ULT_PEDIDOS);
		this.pedido = pedido;
		this.onFechamentoPedido = onFechamentoPedido;
		singleClickOn = true;
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
		lbMsgFechamento = new LabelValue("", CENTER);
        constructorListContainer();
		setDefaultRect();
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return PedidoService.getInstance();
	}

    private void constructorListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(3, RIGHT);
    	listContainer.setUseSortMenu(false);
    }

    protected BaseDomain getDomainFilter() {
    	return new Pedido();
    }

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return ProdutoService.getInstance().getSugestaoVendaDifPedidoDomainList(pedido);
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ItemPedidoErpDif itemPedidoErpDif = (ItemPedidoErpDif)domain;
		//--
		Vector itens = new Vector(0);
		itens.addElement(itemPedidoErpDif.cdProduto);
		itens.addElement(" - " + ProdutoService.getInstance().getDsProduto(itemPedidoErpDif.cdProduto));
		itens.addElement(StringUtil.getStringValue(itemPedidoErpDif.dtEmissaoPedido));
		itens.addElement(StringUtil.getStringValueToInterface(itemPedidoErpDif.qtItemfisicoOrg) + "/" + StringUtil.getStringValueToInterface(itemPedidoErpDif.qtItemFisicoErp));
		//--
		return (String[]) itens.toObjectArray();
	}

    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    private Produto getSelectedProduto() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
    	return ProdutoService.getInstance().getProduto(c.items[0]);
    }

	protected void onFormStart() {
		int yTop = getTop() + HEIGHT_GAP;
		if (onFechamentoPedido) {
			UiUtil.add(this, lbMsgFechamento, LEFT, yTop);
			yTop = AFTER + HEIGHT_GAP;
		}
		UiUtil.add(this, listContainer, LEFT, yTop, FILL, FILL);
		if (onFechamentoPedido) {
			addButtonPopup(btFecharPedido);
			addButtonPopup(btCancelar);
		}
		ajustaComponents();
	}

	public void reposition() {
		super.reposition();
		ajustaComponents();
	}

	private void ajustaComponents() {
		int yTop = getTop() + HEIGHT_GAP;
		if (onFechamentoPedido) {
			lbMsgFechamento.setText(Convert.insertLineBreak(width-6, lbMsgFechamento.fm, Messages.SUGESTAO_MSG_FECHAMENTO_PEDIDO));
			lbMsgFechamento.setRect(CENTER, yTop, width, PREFERRED);
			yTop = AFTER + HEIGHT_GAP;
		}
		listContainer.reposition();
	}

	protected void addBtFechar() {
		if (!onFechamentoPedido) {
			super.addBtFechar();
		}
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCancelar) {
					fecharWindow();
				} else if (event.target == btFecharPedido) {
					fecharWindow();
					if (MainLavenderePda.getInstance().getActualForm() instanceof CadPedidoForm) {
						CadPedidoForm cadPedidoForm = (CadPedidoForm)MainLavenderePda.getInstance().getActualForm();
						Pedido pedido = cadPedidoForm.getPedido();
						pedido.ignoraValidacaoSugestaoDifProdutos = true;
						cadPedidoForm.fecharPedido();
						pedido.ignoraValidacaoSugestaoDifProdutos = false;
					}
				}
				break;
			}
		}
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

	public void detalhesClick() throws SQLException {
		Produto produto = getSelectedProduto();
		if (ValueUtil.isEmpty(produto.dsProduto)) {
			UiUtil.showErrorMessage(Messages.SUGESTAO_MSG_PRODUTO_NAO_ENCONTRADO);
			return;
		}
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(pedido, produto)) {
			UiUtil.showWarnMessage(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
			return;
		}
		cadItemPedidoForm.produtoSelecionado = produto;
		cadItemPedidoForm.gridClickAndRepaintScreen(!cadItemPedidoForm.inVendaUnitariaMode);
		if (cadItemPedidoForm.inVendaUnitariaMode) {
			unpop();
			cadItemPedidoForm.fromWindowSugestaoProduto = true;
			cadItemPedidoForm.setFocusInQtde();
		}
	}

	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		if (onFechamentoPedido) {
			cadItemPedidoForm.voltarClick();
		}
	}

	public boolean hasSugestaoVendaProd() throws SQLException {
		return getDomainList(getDomainFilter()).size() > 0;
	}

	//@Override
	protected void onUnpop() {}

}
