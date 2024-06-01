package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusSolicitacaoAutorizacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoAutorizacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListSolAutorizacaoForm extends LavendereCrudListForm {

	private RepresentanteSupervComboBox cbRepresentante;
	private LabelName lbTipoAutorizacao;
	private TipoAutorizacaoComboBox cbTipoAutorizacao;
	private LabelValue lvCliente;
	private LabelValue lvNuPedido;

	private LabelName lbStatusAutorizacao;
	private StatusSolicitacaoAutorizacaoComboBox cbStatusAutorizacao;

	private Pedido pedido;
	private ItemPedido itemPedido;

	public ListSolAutorizacaoForm(Pedido pedido, ItemPedido itemPedido, boolean isOpenByMenu) throws SQLException {
		super(Messages.SOL_AUTORIZACAO_LIST_TITLE);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		setBaseCrudCadForm(new CadSolAutorizacaoForm(pedido, itemPedido,false, null));
		singleClickOn = true;
		constructorListContainer();

		lbTipoAutorizacao = new LabelName(Messages.SOL_AUTORIZACAO_TIPO_AUTORIZACAO_LABEL);
		cbTipoAutorizacao = new TipoAutorizacaoComboBox();
		cbTipoAutorizacao.setSelectedIndex(0);

		cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_ALL);
		cbRepresentante.setSelectedIndex(0);

		lbStatusAutorizacao = new LabelName(Messages.SOL_AUTORIZACAO_STATUS_AUTORIZACAO_LABEL);
		cbStatusAutorizacao = new StatusSolicitacaoAutorizacaoComboBox(BaseComboBox.DefaultItemType_ALL);
		cbStatusAutorizacao.setSelectedIndex(isReadOnly() ? 0 : 1);

		if (isOpenByMenu) return;
		if (!isReadOnly()) {
			SolAutorizacaoService.getInstance().recebeAtualizacao();
			Pedido pedidoAutorizacao = pedido != null ? pedido : itemPedido.pedido;
			pedidoAutorizacao.solAutorizacaoPedidoCache.clearCaches(pedidoAutorizacao);
			if (itemPedido != null) {
				itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
			}
		} else {
			String nuPedido = getNuPedido(pedido, itemPedido);
			String dsCliente = "";
			if (pedido != null) {
				dsCliente = pedido.getCliente().toString();
			} else if (itemPedido.pedido != null) {
				dsCliente = itemPedido.pedido.getCliente().toString();
			}
			lvCliente = new LabelValue(dsCliente);
			lvNuPedido = new LabelValue(nuPedido);
		}
	}

	private String getNuPedido(Pedido pedido, ItemPedido itemPedido) {
		Pedido pedidoAutorizacao = pedido != null ? pedido : itemPedido.pedido;
		if (pedidoAutorizacao != null && (ValueUtil.isEmpty(pedidoAutorizacao.nuPedido) || ValueUtil.isEmpty(pedidoAutorizacao.itemPedidoList))) {
			throw new ValidationException(Messages.SOL_AUTORIZACAO_PEDIDO_SEM_ITENS);
		}
		return pedidoAutorizacao == null ? ValueUtil.VALOR_NI : pedidoAutorizacao.nuPedido;
	}

	private boolean isReadOnly() {
		return this.pedido != null || this.itemPedido != null;
	}

	private void constructorListContainer() {
		configListContainer("PRODUTO.DSPRODUTO");
		listContainer = new GridListContainer(isReadOnly() ? 6 : 8 , 2);
		if (isReadOnly()) {
			listContainer.setColsSort(new String[][]{
					{Messages.SOL_AUTORIZACAO_SORT_DS_SEQUENCIA, "TB.NUPEDIDO, tb.CDPRODUTO, tb.CDSOLAUTORIZACAO"},
					{Messages.STATUS_SOLICITACAO_AUTORIZACAO_AUTORIZADO, "tb.FLAUTORIZADO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_DSPRODUTO, "PRODUTO.DSPRODUTO"},
					{Messages.SOL_AUTORIZACAO_DS_TIPO_SOL_AUTORIZACAO, "tb.CDTIPOSOLAUTORIZACAO"},
					{Messages.SOL_AUTORIZACAO_QT_ITEM_FISICO, "tb.QTITEMFISICO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_VL_UNITARIO, "tb.VLITEMPEDIDO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_VL_TOTAL, "tb.VLTOTALITEMPEDIDO"}
			});
		} else {
			listContainer.setColsSort(new String[][]{
					{Messages.SOL_AUTORIZACAO_SORT_DS_SEQUENCIA, "TB.NUPEDIDO, tb.CDPRODUTO, tb.CDSOLAUTORIZACAO"},
					{Messages.STATUS_SOLICITACAO_AUTORIZACAO_AUTORIZADO, "tb.FLAUTORIZADO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_DSPRODUTO, "PRODUTO.DSPRODUTO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_CLIENTE, "CLIENTE.NMRAZAOSOCIAL"},
					{Messages.SOL_AUTORIZACAO_DS_TIPO_SOL_AUTORIZACAO, "tb.CDTIPOSOLAUTORIZACAO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_NUPEDIDO, "tb.NUPEDIDO"},
					{Messages.SOL_AUTORIZACAO_QT_ITEM_FISICO, "tb.QTITEMFISICO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_VL_UNITARIO, "tb.VLITEMPEDIDO"},
					{Messages.SOL_AUTORIZACAO_SORT_DS_VL_TOTAL, "tb.VLTOTALITEMPEDIDO"}
			});
		}
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(isReadOnly() ? 5 : 7, RIGHT);
		listContainer.getLayout().allowSpecificItemColor = true;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		SolAutorizacao item = (SolAutorizacao) domain;
		Vector itens = new Vector();
		itens.addElement(item.produto.toString());
		itens.addElement(ValueUtil.VALOR_NI);
		itens.addElement(item.tipoSolicitacaoAutorizacaoEnum.getTitle());
		itens.addElement(item.getStatus());
		if (!isReadOnly()) {
			itens.addElement(MessageUtil.getMessage(Messages.SOL_AUTORIZACAO_CLIENTE_LIST_ITEM, item.cliente.toString()));
		}
		itens.addElement(ValueUtil.VALOR_NI);
		if (!isReadOnly()) {
			itens.addElement(MessageUtil.getMessage(Messages.SOL_AUTORIZACAO_PEDIDO_LIST_ITEM, item.nuPedido));
		}
		itens.addElement(MessageUtil.getMessage(Messages.SOL_AUTORIZACAO_SEQUENCIA_LIST_ITEM, item.cdSolAutorizacao));

		return (String[]) itens.toObjectArray();
	}

	@Override
	protected void setPropertiesInRowList(BaseListContainer.Item c, BaseDomain domain) throws SQLException {
		super.setPropertiesInRowList(c, domain);
		SolAutorizacao item = (SolAutorizacao) domain;
		if (item.isAutorizado()) {
			c.specificItemColor[3] = ColorUtil.softGreen;
		} else if (item.isNaoAutorizado()) {
			c.specificItemColor[3] = ColorUtil.softRed;
		} else {
			c.specificItemColor[3] = ColorUtil.componentsForeColor;
		}
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		SolAutorizacao solAutorizacao = new SolAutorizacao();
		solAutorizacao.flAutorizado = cbStatusAutorizacao.getValue();
		if (!isReadOnly()) {
			solAutorizacao.tipoSolicitacaoAutorizacaoEnum = cbTipoAutorizacao.getValue();
			solAutorizacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
			solAutorizacao.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentante.getValue() : SessionLavenderePda.getRepresentante().cdRepresentante;
			getEdFiltroValue(solAutorizacao, edFiltro.getValue(), true, true, true);
		}

		if (itemPedido != null) {
			getSolAutorizacaoPedidoFilter(solAutorizacao, itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.flOrigemPedido, itemPedido.nuPedido, itemPedido.cdCliente);
			solAutorizacao.cdProduto = itemPedido.cdProduto;
			solAutorizacao.flTipoItemPedido = itemPedido.flTipoItemPedido;
			solAutorizacao.nuSeqProduto = itemPedido.nuSeqProduto;
		} else if (pedido != null) {
			getSolAutorizacaoPedidoFilter(solAutorizacao, pedido.cdEmpresa, pedido.cdRepresentante, pedido.flOrigemPedido, pedido.nuPedido, pedido.cdCliente);
			getEdFiltroValue(solAutorizacao, edFiltro.getValue(), false, true, false);
		}
		solAutorizacao.filterPermission = true;
		return solAutorizacao;
	}

	private void getEdFiltroValue(SolAutorizacao solAutorizacao, String edFiltroValue, boolean filtraCliente, boolean filtraProduto, boolean filtraPedido) {
		if (ValueUtil.isEmpty(edFiltroValue)) return;
		edFiltroValue = "%" + edFiltroValue + "%";
		if (filtraProduto) getEdFiltroProduto(solAutorizacao, edFiltroValue);
		if (filtraCliente) getEdFiltroCliente(solAutorizacao, edFiltroValue);
		if (filtraPedido) solAutorizacao.nuPedidoLike = edFiltroValue;
	}

	private void getEdFiltroCliente(SolAutorizacao solAutorizacao, String edFiltroValue) {
		solAutorizacao.cliente = new Cliente();
		solAutorizacao.cliente.nmRazaoSocial = edFiltroValue;
		solAutorizacao.cliente.cdCliente = edFiltroValue;
		solAutorizacao.cliente.nmFantasia = edFiltroValue;
	}

	private void getEdFiltroProduto(SolAutorizacao solAutorizacao, String edFiltroValue) {
		solAutorizacao.produto = new Produto();
		solAutorizacao.produto.cdProdutoLikeFilter = edFiltroValue;
		solAutorizacao.produto.dsProduto = edFiltroValue;
		solAutorizacao.produto.nuCodigoBarras = edFiltroValue;
		solAutorizacao.produto.dsReferenciaLikeFilter = edFiltroValue;
		solAutorizacao.produto.dsCodigoAlternativo = edFiltroValue;
	}

	private void getSolAutorizacaoPedidoFilter(SolAutorizacao solAutorizacao, String cdEmpresa, String cdRepresentante, String flOrigemPedido, String nuPedido, String cdCliente) {
		solAutorizacao.cdEmpresa = cdEmpresa;
		solAutorizacao.cdRepresentante = cdRepresentante;
		solAutorizacao.flOrigemPedido = flOrigemPedido;
		solAutorizacao.nuPedido = nuPedido;
		solAutorizacao.cdCliente = cdCliente;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		SolAutorizacao domainFilter = (SolAutorizacao) domain;
		return SolAutorizacaoService.getInstance().findAllByExample(domainFilter);
	}

	@Override
	public void onFormExibition() throws SQLException {
		list();
	}

	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return ((SolAutorizacao) domain).produto.toString();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return SolAutorizacaoService.getInstance();
	}

	@Override
	public void initUI() {
		if (!this.isReadOnly()) {
			super.initUI();
			return;
		}
		if (listContainer != null) {
			listContainer.resizeable = listResizeable;
			listContainer.atributteSortSelected = sortAtributte;
			listContainer.sortAsc = sortAsc;
		}
		try {
			onFormStart();
			exibited = true;
			loadDefaultFilters();
			list();
		} catch (Throwable ex) {
			showException(ex);
		}
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor() && !isReadOnly()) {
			UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
		}
		if (!isReadOnly()) {
			UiUtil.add(this, lbTipoAutorizacao, cbTipoAutorizacao, getLeft(), getNextY());
		} else {
			UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lvCliente, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.PEDIDO_NOME_ENTIDADE), lvNuPedido, getLeft(), getNextY());
		}
		UiUtil.add(this, lbStatusAutorizacao, cbStatusAutorizacao, getLeft(), getNextY());
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
		int height = isReadOnly() ? 0 : barBottomContainer.getHeight();
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - height);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbRepresentante) {
					cbRepresentanteChange();
				} else if (event.target == cbTipoAutorizacao) {
					list();
				} else if (event.target == cbStatusAutorizacao) {
					list();
				}
			break;
			case EditIconEvent.PRESSED:
				if (event.target == edFiltro) {
					list();
				}
				break;
		}
	}

    protected void filtrarClick() throws SQLException {
    	list();
    }

	private void cbRepresentanteChange() throws SQLException {
		SessionLavenderePda.setRepresentante(ValueUtil.isNotEmpty(cbRepresentante.getValue()) ? ((SupervisorRep)cbRepresentante.getSelectedItem()).representante : null);
		list();
	}

	@Override
	public void detalhesClick() throws SQLException {
		SolAutorizacao selectedDomain = (SolAutorizacao) getSelectedDomain();
		if (!this.isReadOnly()) {
			setBaseCrudCadForm(new CadSolAutorizacaoForm(null, null, false, selectedDomain));
			super.detalhesClick();
		} else {
			CadSolAutorizacaoWindow cadSolAutorizacaoWindow = new CadSolAutorizacaoWindow(pedido, itemPedido, selectedDomain);
			cadSolAutorizacaoWindow.cadSolAutorizacaoForm.edit(selectedDomain);
			cadSolAutorizacaoWindow.popup();
		}
	}

}
