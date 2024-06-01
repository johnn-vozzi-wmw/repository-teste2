package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import br.com.wmw.lavenderepda.business.domain.MotivoPendenciaJust;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.MotivoPendenciaJustService;
import br.com.wmw.lavenderepda.business.service.MotivoPendenciaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelMotivoPendenciaWindow extends LavendereWmwListWindow {

	private Pedido pedido;
	private MotivoPendenciaJust motivoPendenciaJust;
	private MotivoPendencia motivoPendencia;
	private LabelValue lvMotivoPendenciaPrincipal;
	private LabelValue lvMotivoPendenciaPrincipalJust;
	private EditMemo lvObservacaoMotivoPendencia;

	public RelMotivoPendenciaWindow(Pedido pedido) throws SQLException {
		super(Messages.REL_ITENS_PENDENTES_TITULO);
		this.pedido = pedido;
		if (pedido.isPedidoPossuiJustificativa() && pedido.isPedidoPossuiMotivoPendencia()) {
			setMotivoPendencia(pedido);
			if (isRelPossuiDescricaoMotivoPendencia()) {
				lvMotivoPendenciaPrincipal = new LabelValue(motivoPendencia.toString());
				lvMotivoPendenciaPrincipalJust = new LabelValue(motivoPendenciaJust.toString());
				lvObservacaoMotivoPendencia = new EditMemo("", 5, 500);
				lvObservacaoMotivoPendencia.setText(StringUtil.getStringValue(pedido.dsObsMotivoPendencia));
				lvObservacaoMotivoPendencia.setEditable(false);
			}
		}
		constructorListContainer();
		setDefaultRect();
	}

	private void setMotivoPendencia(Pedido pedido) throws SQLException {
		MotivoPendencia motivoPendencia = new MotivoPendencia(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdMotivoPendencia);
		motivoPendencia = (MotivoPendencia) MotivoPendenciaService.getInstance().findByMotivoPendencia(motivoPendencia.cdMotivoPendencia);
		this.motivoPendencia = motivoPendencia;
		MotivoPendenciaJust motivoPendenciaJust = new MotivoPendenciaJust(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdMotivoPendencia, pedido.cdMotivoPendenciaJust);
		motivoPendenciaJust = (MotivoPendenciaJust) MotivoPendenciaJustService.getInstance().findByMotivoPendenciaJust(motivoPendencia.cdMotivoPendencia,motivoPendenciaJust.cdMotivoPendenciaJust);
		this.motivoPendenciaJust = motivoPendenciaJust;
	}

	private void constructorListContainer() {
		configListContainer("PRODUTO.DSPRODUTO");
		listContainer = new GridListContainer(2, 1);
		String[][] sort = new String[4][2];
		sort[0][0] = Messages.REL_ITENS_PENDENTES_SORT_CDPRODUTO;
		sort[0][1] = "PRODUTO.CDPRODUTO";
		sort[1][0] = Messages.REL_ITENS_PENDENTES_SORT_DSPRODUTO;
		sort[1][1] = "PRODUTO.DSPRODUTO";
		sort[2][0] = Messages.REL_ITENS_PENDENTES_SORT_CDMOTIVO;
		sort[2][1] = "MOTIVOPENDENCIA.CDMOTIVOPENDENCIA";
		sort[3][0] = Messages.REL_ITENS_PENDENTES_SORT_DSMOTIVO;
		sort[3][1] = "MOTIVOPENDENCIA.DSMOTIVOPENDENCIA";
		listContainer.setColsSort(sort);
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
		if (pedido.isPedidoPossuiJustificativa()) {
			listContainer.resizeable = true;
		}
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return ((ItemPedidoService) getCrudService()).findProdutosPendentesByItemPedido((ItemPedido) domain);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido)domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(itemPedido.getProduto()));
		item.addElement(StringUtil.getStringValue(itemPedido.motivoPendencia));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemPedidoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedido(pedido);
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (isRelPossuiDescricaoMotivoPendencia()) {
			UiUtil.add(this, new LabelName(Messages.MOTIVO_PENDENCIA_JUST_PRINCIPAL),lvMotivoPendenciaPrincipal, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.MOTIVO_PENDENCIA_JUST_COMBO),lvMotivoPendenciaPrincipalJust, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.MOTIVO_PENDENCIA_JUST_OBSERVACAO), lvObservacaoMotivoPendencia, getLeft(), getNextY());
		}
		UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

	private boolean isRelPossuiDescricaoMotivoPendencia() {
		return motivoPendencia != null && motivoPendenciaJust != null && ValueUtil.isNotEmpty(motivoPendencia.dsMotivoPendencia) && ValueUtil.isNotEmpty(motivoPendenciaJust.dsMotivoPendenciaJust);
	}

}
