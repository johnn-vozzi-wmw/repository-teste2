package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfgBrinde;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.BonifCfgBrindeService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListBonifCfgBrindeForm extends LavendereCrudListForm {
	
	private final BonifCfgFaixaQtde bonifCfgFaixa;
	private final List<BonifCfgBrinde> brindesSelecionadosList;
	private Vector domainList;
	private Pedido pedido;

	public ListBonifCfgBrindeForm(BonifCfgFaixaQtde bonifCfgFaixa, Pedido pedido) {
		super(Messages.BONIFCFG_LISTA_BRINDES);
		this.bonifCfgFaixa = bonifCfgFaixa;
		this.brindesSelecionadosList = new ArrayList<>();
		this.pedido = pedido;
		constructorListContainer();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		BonifCfgBrinde bonfiCfgBrinde = new BonifCfgBrinde();
		bonfiCfgBrinde.cdEmpresa = bonifCfgFaixa.cdEmpresa;
		bonfiCfgBrinde.cdBonifCfg = bonifCfgFaixa.cdBonifCfg;
		return bonfiCfgBrinde;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return BonifCfgBrindeService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		//Sem necessidade de evento até o momento.
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		BonifCfgBrinde bonifCfgBrinde = (BonifCfgBrinde) domain;
		return new String[] {
				LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : StringUtil.getStringValue(bonifCfgBrinde.cdProduto) + " - ",
				bonifCfgBrinde.getProduto().dsProduto,
				Messages.BONIFCFG_QTD_ITEM + StringUtil.getStringValue(bonifCfgBrinde.qtBrinde)
			};
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		BonifCfgBrinde bonifCfgBrinde = (BonifCfgBrinde) domain;
		c.id = bonifCfgBrinde.cdProduto + ";" + bonifCfgBrinde.qtBrinde;
		c.domain = domain;
		c.setToolTip(bonifCfgBrinde.getProduto().dsProduto);
		if (!bonifCfgBrinde.isOpcional()) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE_OBRIGATORIO);
			c.eventsEnabled = false;
		}
	}
	
	@Override
	protected Vector getDomainList() throws SQLException {
		if (domainList == null) {
			domainList = super.getDomainList();
		}
		return domainList;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getDomainList();
	}

	private void constructorListContainer() {
		GridListContainer gridListContainer = new GridListContainer(3, 2, true, false, false, true);
		gridListContainer.setColPosition(2, RIGHT);
		gridListContainer.setUseSortMenu(false);
		gridListContainer.setBarTopSimple();
		listContainer = gridListContainer;
	}
	
	public List<BonifCfgBrinde> getBrindesSelecionadosList() {
		return this.brindesSelecionadosList;
	}

	public void setBrindesSelecionadosList() {
		int size = listContainer.size();
		for (int i = 0; i < size; i++) {
			BaseListContainer.Item c = (Item) listContainer.getContainer(i);
			BonifCfgBrinde bonifCfgBrinde = (BonifCfgBrinde) c.domain;
			//isChecked no framework esta ao contrario
			if (!listContainer.isChecked(i) || !bonifCfgBrinde.isOpcional()) {
				brindesSelecionadosList.add(bonifCfgBrinde);
			}
		}
	}

	public int getListBrindesSize() {
		return listContainer.size();
	}

	@Override
	public void list() throws SQLException {
		int listSize;
		Vector domainList;
		domainList = getDomainList(getDomainFilterSortable());
		listSize = domainList.size();
		List<BaseListContainer.Item> containersList = new ArrayList<>();
		if (listSize > 0) {
			BaseListContainer.Item c;
			BonifCfgBrinde domain;
			for (int i = 0; i < listSize; i++) {
				if (i % 250 == 0)
					VmUtil.executeGarbageCollector();
				domain = (BonifCfgBrinde) domainList.items[i];
				Produto produto = ProdutoService.getInstance().getProduto(domain.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class), domain.cdProduto);
				if (produto != null) {
					double preco = ItemTabelaPrecoService.getInstance().getPrecoProduto(produto, pedido);
					if (preco == 0d) {
						continue;
					}
				}
				c = new BaseListContainer.Item(listContainer.getLayout());
				containersList.add(c);
				c.id = domain.getRowKey();
				c.setID(ADD_ID.formatID(c.id));
				c.domain = getDomain(domain);
				c.setItens(getItem(domain));
				c.setToolTip(getToolTip(domain));
				c.setIconsLegend(getIconsLegend(domain), resizeIconsLegend, useLeftTopIcons);
				c.ignoreTotalizer = isIgnoreTotalizer(domain);
				setPropertiesInRowList(c, domain);
			}
			Container[] all = new Container[containersList.size()];
			for (int i = 0; i < containersList.size(); i++) {
				all[i] = containersList.get(i);
			}
			listContainer.addContainers(all);

			configureContainersBtHidden();
		} else {
			listContainer.scrollFinished = true;
		}
		afterList(domainList);
	}

}
