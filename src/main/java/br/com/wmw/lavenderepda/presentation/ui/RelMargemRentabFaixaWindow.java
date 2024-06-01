package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.MargemRentabFaixa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.MargemRentabFaixaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

import java.sql.SQLException;

public class RelMargemRentabFaixaWindow extends WmwWindow {
	
	private BaseGridEdit grid;
	private LabelContainer produtoContainer;
	private ItemPedido itemPedido;
	private Pedido pedido;
	private Vector margemRentabFaixaList;
	private int indexImage;
	double vlPctMargemRentab;

	public RelMargemRentabFaixaWindow(Pedido pedido, ItemPedido itemPedido, double vlPctMargemRentab) throws SQLException {
		super(Messages.TITULO_REL_FAIXAS_DE_MARGEM_OU_RENTABILIDADE);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		this.margemRentabFaixaList = getRentabilidadeFaixaList();
		this.vlPctMargemRentab = vlPctMargemRentab;
		validaSePossuiMargemRentabilidade(itemPedido);
		produtoContainer = new LabelContainer("");
		GridColDefinition[] gridColDefiniton;
		if (LavenderePdaConfig.isOcultaRentabilidadeTelaFaixaRentabilidade()) {
			gridColDefiniton = instanciaGridColunaPctOcultada();
		} else {
			gridColDefiniton = instanciaGridDefault();
		}
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		grid.setGridControllable();
		setDefaultRect();
	}
	
	private GridColDefinition[] instanciaGridColunaPctOcultada() {
		return new GridColDefinition[] {
				new GridColDefinition(Messages.RENTABILIDADEFAIXA_DS_FAIXA, -55, LEFT),
				new GridColDefinition(Messages.RENTABILIDADEFAIXA_COR, -45, CENTER)};
	}
	
	private GridColDefinition[] instanciaGridDefault() {
		return new GridColDefinition[] {
				new GridColDefinition(Messages.RENTABILIDADEFAIXA_DS_FAIXA, -65, LEFT),
				new GridColDefinition(Messages.RENTABILIDADEFAIXA_PORCENTAGEM, -20, CENTER),
				new GridColDefinition(Messages.RENTABILIDADEFAIXA_COR, -15, CENTER) };
	}

	private void validaSePossuiMargemRentabilidade(ItemPedido itemPedido) {
		if (itemPedido != null && itemPedido.pedido.isPedidoAberto() && ValueUtil.isEmpty(margemRentabFaixaList)) {
			throw new ValidationException(Messages.ITEMPEDIDO_NAO_POSSUI_MARGEM_RENTABILIDADE);
		}
		if (pedido != null && pedido.isPedidoAberto() && ValueUtil.isEmpty(margemRentabFaixaList)) {
			throw new ValidationException(Messages.PEDIDO_NAO_POSSUI_MARGEM_RENTABILIDADE);
		}
	}
	
	@Override
	public void initUI() {
		try {
			super.initUI();
			if (itemPedido != null) {
				produtoContainer.setDescricao(itemPedido.getProduto().toString());
				UiUtil.add(scBase, produtoContainer, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
			}
			UiUtil.add(scBase, grid, LEFT, itemPedido != null ? AFTER : TOP, FILL, FILL);
			loadGrid();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}
	
	private void loadGrid() throws SQLException {
		if (ValueUtil.isNotEmpty(margemRentabFaixaList)) {
			MargemRentabFaixa margemRentabFaixaAtingida = getMargemRentabFaixaAtingida();
			for (int i = 0; i < margemRentabFaixaList.size(); i++) {
				MargemRentabFaixa margemRentabFaixa = (MargemRentabFaixa) margemRentabFaixaList.items[i];
				grid.add(getItem(margemRentabFaixa));
				if (margemRentabFaixaAtingida != null) {
					if (margemRentabFaixaAtingida.vlPctMargemRentab == margemRentabFaixa.vlPctMargemRentab) {
						grid.gridController.setRowForeColor(ColorUtil.baseForeColorSystem, i);
					}
				}
			}
		}
	}
	
	private Vector getRentabilidadeFaixaList() throws SQLException {
		if (pedido == null && ValueUtil.isEmpty(itemPedido.cdMargemRentab)) return null;
		MargemRentabFaixa margemRentabFaixaFilter = new MargemRentabFaixa();
		margemRentabFaixaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		margemRentabFaixaFilter.cdMargemRentab = pedido != null ? pedido.cdMargemRentab : itemPedido.cdMargemRentab;
		margemRentabFaixaFilter.vlPctMargemRentab = 10000;
		return MargemRentabFaixaService.getInstance().findAllByExample(margemRentabFaixaFilter, MargemRentabFaixa.NOME_COLUNA_VLPCTMARGEMRENTAB);
	}
	
	private String[] getItem(Object domain) {
		try {
			MargemRentabFaixa margemRentabFaixa = (MargemRentabFaixa) domain;
			int corFaixa = MargemRentabFaixaService.getInstance().getCorMargemRentabFaixa(margemRentabFaixa.cdCorFaixa);
			Image iconeFaixa = UiUtil.getIconButtonAction("images/rentabilidade.png", corFaixa, true);
			String[] item;
			if (LavenderePdaConfig.isOcultaRentabilidadeTelaFaixaRentabilidade()) {
				item = instanciaItemColunaOculta(margemRentabFaixa);
				grid.setImage(StringUtil.getStringValue(indexImage++), iconeFaixa, false);
			} else {
				item = instanciaItemDefault(margemRentabFaixa);
				grid.setImage(Messages.RENTABILIDADE_NOME_ENTIDADE + " " + StringUtil.getStringValueToInterface(margemRentabFaixa.vlPctMargemRentab), iconeFaixa, false);
			}

			return item;
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
		return null;
	}

	private String[] instanciaItemDefault(MargemRentabFaixa margemRentabFaixa) {
		return new String[] {
				StringUtil.getStringValue(margemRentabFaixa.dsCorFaixa),
				StringUtil.getStringValue(LavenderePdaConfig.isOcultaPercentualFaixaRentabilidadeSePositiva(margemRentabFaixa) ? ValueUtil.VALOR_NI : StringUtil.getStringValueToInterface(margemRentabFaixa.vlPctMargemRentab)),
				StringUtil.getStringValue(Messages.RENTABILIDADE_NOME_ENTIDADE + " " + StringUtil.getStringValueToInterface(margemRentabFaixa.vlPctMargemRentab))
		};
	}
	
	private String[] instanciaItemColunaOculta(MargemRentabFaixa margemRentabFaixa) {
		return new String[] {
					StringUtil.getStringValue(margemRentabFaixa.dsCorFaixa),
					StringUtil.getStringValue(indexImage)
			};
	}
	
	private MargemRentabFaixa getMargemRentabFaixaAtingida() throws SQLException {
		if (pedido != null) {
			return MargemRentabFaixaService.getInstance().findMargemRentabFaixa(pedido.cdEmpresa, pedido.cdMargemRentab, pedido.vlPctMargemRentab);
		} else if (itemPedido != null) {
			return MargemRentabFaixaService.getInstance().findMargemRentabFaixa(itemPedido.cdEmpresa, itemPedido.cdMargemRentab, itemPedido.vlPctMargemRentab);
		}
		return null;
	}

}
