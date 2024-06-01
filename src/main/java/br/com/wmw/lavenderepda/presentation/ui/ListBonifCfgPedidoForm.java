package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.BonifCfgService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class ListBonifCfgPedidoForm extends LavendereCrudListForm {
	
	private Pedido pedido;
	private Cliente cliente;
	
	public ListBonifCfgPedidoForm(Pedido pedido) throws SQLException {
		super(Messages.TITULO_POLITICA_BONIFICACAO);
		this.pedido = pedido;
		constructorListContainer();
	}
	
	public ListBonifCfgPedidoForm(Cliente cliente) throws SQLException {
		super(Messages.TITULO_POLITICA_BONIFICACAO);
		this.cliente = cliente;
		constructorListContainer();
	}
	
	private void constructorListContainer() {
		GridListContainer gridListContainer = new GridListContainer(5, 2);
		gridListContainer.setColPosition(3, RIGHT);
		gridListContainer.setUseSortMenu(false);
		gridListContainer.setBarTopSimple();
		listContainer = gridListContainer;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		if (pedido != null) {
			Vector domainList = pedido.getBonifCfgPedidoList();
			Vector bonifCfgList = BonifCfgService.getInstance().getBonifCfgListByPedido(pedido);
			int size = bonifCfgList.size();
			if (LavenderePdaConfig.isUsaRegraContaCorrenteQuantidade()) {
				for (int i = 0; i < size; i++) {
					if (!domainList.contains(bonifCfgList.items[i])) {
						domainList.addElement(bonifCfgList.items[i]);
					}
				}
			}
			return domainList;
		} else {
			return BonifCfgService.getInstance().getBonifCfgListByCliente(cliente);
		}
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		BonifCfg bonifPed = (BonifCfg) domain;

		double valueToInterface = 0d;
		if (bonifPed.isTipoRegraQuantidade()) {
			valueToInterface = bonifPed.sumQtBonificacao;
		} else if (bonifPed.isTipoRegraValor()) {
			valueToInterface = bonifPed.sumVlBonificacao;
		} else if (bonifPed.isTipoRegraContaCorrente()) {
			valueToInterface = bonifPed.qtSaldoContaCorrente;
		}
		String dsSaldo = ValueUtil.VALOR_NI;
		if (!bonifPed.isTipoRegraContaCorrente()) {
			dsSaldo = valueToInterface > 0 ? Messages.MSG_SALDO_EXCEDENTE_PEDIDO : Messages.MSG_SALDO_TOTALMENTE_CONSUMIDO;
		}
		return new String[]{
				StringUtil.getStringValue(bonifPed.cdBonifCfg),
				" - " + bonifPed.dsBonifCfg,
				dsSaldo,
				ValueUtil.VALOR_NI,
				Messages.LABEL_SALDO + ( bonifPed.isTipoRegraQuantidade() || bonifPed.isTipoRegraContaCorrente() ? "" : Messages.MOEDA ) + StringUtil.getStringValueToInterface(valueToInterface)
		};
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return BonifCfgService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new BonifCfg();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case PenEvent.PEN_UP: {
				if (event.target instanceof BaseListContainer.Item && ((BaseListContainer.Item)event.target).layout == listContainer.getLayout() && pedido != null) {
					BaseListContainer.Item c = (Item) listContainer.getSelectedItem();
					String cdBonifCfg = c.getItem(0);
					BonifCfg bonifCfg = (BonifCfg) BonifCfgService.getInstance().findByPrimaryKey(new BonifCfg(pedido.cdEmpresa, cdBonifCfg));
					new ListItemPedidoBonifCfgWindow(pedido, bonifCfg).popup();
				}
				break;
			}
		}
		
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		BonifCfg bonCfg = (BonifCfg) domain;
		double valueToCompare = bonCfg.isTipoRegraQuantidade() ? bonCfg.sumQtBonificacao : bonCfg.sumVlBonificacao;
		if (!bonCfg.isOpcional() && valueToCompare > 0) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_GRID_BONIFCFG_OBRIGATORIA_SALDO_PENDENTE);
		}
		c.setToolTip(bonCfg.dsBonifCfg);
	}
	
}
