package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
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
import br.com.wmw.lavenderepda.business.domain.ColunaTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.HistVendaListTabPreco;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ColunaTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.HistVendaListTabPrecoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelHistListaTabPrecoWindow extends WmwListWindow {
	
	private int cdListaTabelaPreco;
	private int cdColunaTabelaPreco;
	private String cdGrupoProduto1;
	private Pedido pedido;
	private ItemPedido itemEdicao;
	
	private LabelValue lbDestaque;
	private ButtonPopup btGrupos;

	public RelHistListaTabPrecoWindow(Pedido pedido, int cdListaTabelaPreco, int cdColunaTabelaPreco, String cdGrupoProduto1, ItemPedido itemEdicao) throws SQLException {
		super(Messages.BOTAO_HISTLISTATABPRECO);
		this.pedido = pedido;
		this.itemEdicao = itemEdicao;
		this.cdListaTabelaPreco = cdListaTabelaPreco;
		this.cdColunaTabelaPreco = cdColunaTabelaPreco;
		this.cdGrupoProduto1 = cdGrupoProduto1;
		btGrupos = new ButtonPopup(Messages.HISTVENDASLISTATABPRECO_VER_GRUPOS, UiUtil.getIconButtonAction("images/previous.png"));
		lbDestaque = new LabelValue();
		loadLbDestaque();
		constructorListContainer();
		singleClickOn = true;
		setDefaultRect();
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(8, 2);
		listContainer.setBarTopSimple();
		listContainer.resizeable = false;
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(5, RIGHT);
		listContainer.setColPosition(7, RIGHT);
	}
	
	private void loadLbDestaque() throws SQLException {
		ColunaTabelaPreco filter = new ColunaTabelaPreco();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.cdColunaTabelaPreco = cdColunaTabelaPreco;
		String dsLabel = ColunaTabelaPrecoService.getInstance().findColumnByRowKey(filter.getRowKey(), ColunaTabelaPreco.NOME_COLUNA_DSCOLUNATABELAPRECO);
		if (cdGrupoProduto1 != null) {
			dsLabel += " -> " + GrupoProduto1Service.getInstance().getDsGrupoProduto(cdGrupoProduto1);
		}
		lbDestaque.setText(dsLabel);
	}
	
	private Vector getDomainListInicial() throws SQLException {
		HistVendaListTabPreco filter = (HistVendaListTabPreco)getDomainFilter();
		filter.cdListaTabelaPreco = cdListaTabelaPreco;
		return HistVendaListTabPrecoService.getInstance().findAllHistVendasByExample(filter, itemEdicao, cdListaTabelaPreco);
	}
	
	private Vector getDomainListDetalhado() throws SQLException {
		HistVendaListTabPreco filter = (HistVendaListTabPreco)getDomainFilter();
		filter.cdGrupoProduto1 = cdGrupoProduto1;
		return HistVendaListTabPrecoService.getInstance().findAllDetalhesByExample(filter, itemEdicao, cdListaTabelaPreco);
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return (ValueUtil.isEmpty(cdGrupoProduto1)) ? getDomainListInicial() : getDomainListDetalhado();
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		HistVendaListTabPreco hist = (HistVendaListTabPreco) domain;
		boolean usaQtItemInteiro = LavenderePdaConfig.isUsaQtdInteiro();
		if (ValueUtil.isEmpty(cdGrupoProduto1)) {
			return new String[] {
					StringUtil.getStringValue(hist.grupoProduto1), ValueUtil.VALOR_NI,
					Messages.META_REALIZADO, Messages.HISTVENDASLISTATABPRECO_MEDIA_HISTORICO,
					StringUtil.getStringValueToInterface(hist.qtRealizado, usaQtItemInteiro ? 0 : ValueUtil.doublePrecisionInterface) + " " + Messages.PEDIDO_LABEL_ITEM,
					StringUtil.getStringValueToInterface(hist.mediaQtRealizado, usaQtItemInteiro ? 0 : ValueUtil.doublePrecisionInterface) + " " + Messages.PEDIDO_LABEL_ITEM,
					Messages.MOEDA + " " + StringUtil.getStringValueToInterface(hist.vlRealizado), Messages.MOEDA + " " + StringUtil.getStringValueToInterface(hist.mediaVlRealizado)
					
			};
		} else {
			return new String[] {
				StringUtil.getStringValue(hist.dsListaTabelaPreco), ValueUtil.VALOR_NI,
				Messages.META_REALIZADO, ValueUtil.VALOR_NI,
				StringUtil.getStringValueToInterface(hist.qtRealizado, usaQtItemInteiro ? 0 : ValueUtil.doublePrecisionInterface) + " " + Messages.PEDIDO_LABEL_ITEM, ValueUtil.VALOR_NI,
				Messages.MOEDA + " " + StringUtil.getStringValueToInterface(hist.vlRealizado),ValueUtil.VALOR_NI
			};
		}
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return HistVendaListTabPrecoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		HistVendaListTabPreco filter = new HistVendaListTabPreco();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.cdCliente = pedido.cdCliente;
		filter.cdColunaTabelaPreco = cdColunaTabelaPreco;
		return filter;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbDestaque, getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
		ajustaBtGrupos();
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		if (ValueUtil.isEmpty(cdGrupoProduto1)) {
			HistVendaListTabPreco hist = (HistVendaListTabPreco)getSelectedDomain2();
			cdGrupoProduto1 = hist.cdGrupoProduto1;
			ajustaBtGrupos();
			loadLbDestaque();
			list();
		}
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btGrupos) {
					cdGrupoProduto1 = null;
					ajustaBtGrupos();
					loadLbDestaque();
					list();
				}
			break;	
		}
	}
	
	private void ajustaBtGrupos() {
		if (ValueUtil.isEmpty(cdGrupoProduto1)) {
			cFundoFooter.remove(btGrupos);
			ajustaTamanhoBotoes();
		} else {
			addButtonPopup(btGrupos);
		}
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}

}
