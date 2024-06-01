package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class ListItensComGradeInconsistenteWindow extends WmwListWindow {
	
	private LabelValue lbDescricao;
	public CadPedidoForm cadPedidoForm;
	private Pedido pedido;
	
	public ListItensComGradeInconsistenteWindow(Pedido pedido, CadPedidoForm cadPedidoForm) {
		super(Messages.PEDIDO_ITENS_GRADE_INCONSISTENTE);
        this.pedido = pedido;
        this.cadPedidoForm = cadPedidoForm;
        singleClickOn = !LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado;
        lbDescricao = new LabelValue(Messages.REL_ITENS_GRADE_INCONSISTENTE_MSG_NAO_INSERIDOS_PEDIDO, CENTER);
        constructorListContainer();
        setDefaultRect();
	}

    private void constructorListContainer() {
		listContainer = new GridListContainer(2, 2);
    	listContainer.setUseSortMenu(false);
    	listContainer.setBarTopSimple();
    }

    public void screenResized() {
		super.screenResized();
		lbDescricao.setText(MessageUtil.quebraLinhas(Messages.REL_ITENS_GRADE_INCONSISTENTE_MSG_NAO_INSERIDOS_PEDIDO, width - 20));
		lbDescricao.reposition();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}
    
    @Override
    protected void onFormStart() throws SQLException {
    	lbDescricao.setText(MessageUtil.quebraLinhas(Messages.REL_ITENS_GRADE_INCONSISTENTE_MSG_NAO_INSERIDOS_PEDIDO, width - 20));
    	UiUtil.add(this, lbDescricao, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
    }

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return PedidoService.getInstance().getAllItensFromPedidoWithGradeInconsistences(pedido);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		Vector vector = new Vector(2);
		vector.addElement(StringUtil.getStringValue(itemPedido.cdProduto));
		vector.addElement(" - " + StringUtil.getStringValue(itemPedido.getDsProduto()));
		return (String[]) vector.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemPedidoGradeService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedidoGrade();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case PenEvent.PEN_UP: {
				if (event.target instanceof BaseListContainer.Item) {
					ItemPedido itemPedido = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(getSelectedRowKey());
					if (itemPedido != null) {
						if (ValueUtil.valueEquals(ItemPedidoGradeService.getInstance().verificaInconsistenciasGrade(itemPedido), 0)) {
							hasNoOneGradeInconsistent(itemPedido);
						} else {
							hasOneGradeInconsistent(itemPedido);
						}
					}
				}
				break;
			}
    	}
	}

	private void hasNoOneGradeInconsistent(ItemPedido itemPedido) throws SQLException {
		if (UiUtil.showConfirmYesCancelMessage(Messages.MENSAGEM_ERRO_NENHUMA_GRADE_ENCONTRADA) == 1) {
			if (itemPedido.pedido == null) itemPedido.pedido = pedido;
			CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
			cadItemPedidoForm.delete(itemPedido);
			cadItemPedidoForm.atualizaProdutoNaGrid(ProdutoTabPrecoService.getInstance().getProdutoTabPreco(itemPedido.getProduto().cdProduto), false);
			list();
			if (pedido.isPedidoAberto()) {
				cadPedidoForm.afterCrudItemPedido();
			}
			if (listContainer.size() == 0) {
				fecharWindow();
			}
		}
	}

	private void hasOneGradeInconsistent(ItemPedido itemPedido) throws SQLException {
		CadItemPedidoForm cadItemPedidoForm = CadItemPedidoForm.getInstance(cadPedidoForm, pedido);
		cadItemPedidoForm.fromProdutoPendenteGiroMultInsercao = true;
		cadItemPedidoForm.edit(itemPedido);
		MainLavenderePda.getInstance().show(cadItemPedidoForm);
		cadItemPedidoForm.btPreviousItem.setVisible(false);
		cadItemPedidoForm.lbItemByItem.setVisible(false);
		cadItemPedidoForm.btNextItem.setVisible(false);
		fecharWindow();
	}

}