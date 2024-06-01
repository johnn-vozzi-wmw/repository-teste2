package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.IpiService;
import br.com.wmw.lavenderepda.business.service.ItemNfeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.STService;
import br.com.wmw.lavenderepda.business.service.TributosService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItemNfeForm extends LavendereCrudListForm {
	
	private Nfe nfe;
	private Pedido pedido;
	
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lvQtTotalItens;
	private LabelTotalizador lvVlTotalItensComTributos;
	private static final String COL_NUSEQPRODUTO = "NUSEQPRODUTO";
	private static final String COL_CDPRODUTO = "CDPRODUTO";
	private static final String COL_DSPRODUTO = "DSPRODUTO";
	
	
	public ListItemNfeForm(Pedido pedido) throws SQLException {
		super(Messages.ITEMNFE_NOME_ENTIDADE);
		this.nfe = pedido.getInfoNfe();
		this.pedido = pedido;
		setBaseCrudCadForm(new CadItemNfeForm());
		
		lvQtTotalItens = new LabelTotalizador("999999999,999");
		lvVlTotalItensComTributos = new LabelTotalizador("999999999,999");
		sessaoTotalizadores = new SessionTotalizerContainer();
		singleClickOn = true;
		listResizeable = false;
		constructorListContainer();
	}
	
	private void constructorListContainer() {
		configListContainer("CDPRODUTO");
		int itemCount = LavenderePdaConfig.isConfigGradeProduto() ? 6 : 4;
		listContainer = new GridListContainer(itemCount, 2);
    	listContainer.setColPosition(1, RIGHT);
    	listContainer.setColPosition(3, RIGHT);
    	
		int size = 3;
		size = LavenderePdaConfig.isMostraDescricaoReferencia() ? size + 1 : size;
		
		String[][] matriz = new String[size][2];
		matriz[0][0] = Messages.ITEMNFE_LABEL_NUSEQPRODUTO;
		matriz[0][1] = "NUSEQPRODUTO";
		matriz[1][0] = Messages.ITEMNFE_LABEL_CDPRODUTO;
		matriz[1][1] = "CDPRODUTO";
		matriz[2][0] = Messages.ITEMNFE_LABEL_DSPRODUTO;
		matriz[2][1] = "DSPRODUTO";
		int position = 3;
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSREFERENCIA;
			matriz[position][1] = "DSREFERENCIA";
			position++;
		}
		
		listContainer.setColsSort(matriz);
		listContainer.btResize.setVisible(false);
    }

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new ItemNfe();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemNfeService.getInstance();
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vector list = new Vector();
		list = ItemNfeService.getInstance().getItemNfeList(nfe);
		realizaOrdenacaoListaItens(domain, list);
		preparaDescricaoNfeToSort(list);
		updateTotalizadores();
		return list;
	}
	
	private void updateTotalizadores() {
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens()) {
			if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				lvVlTotalItensComTributos.setValue(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_TRIBUTOS + " " + StringUtil.getStringValueToInterface(TributosService.getInstance().getVlTotalPedidoComTributos(pedido)));
			} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
				lvVlTotalItensComTributos.setValue(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_COM_ST + " " + StringUtil.getStringValueToInterface(STService.getInstance().getVlTotalItensComSt(pedido)));
			} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				lvVlTotalItensComTributos.setValue(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_IPI + " " + StringUtil.getStringValueToInterface(IpiService.getInstance().getVlTotalPedidoComIpi(pedido)));
			}	
				lvVlTotalItensComTributos.reposition();
		}	
		lvQtTotalItens.setValue(Messages.ITEMPEDIDO_LABEL_QT_TOTALITENS + " " + StringUtil.getStringValueToInterface(pedido.getQtItensLista(), LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
		lvQtTotalItens.reposition();
		sessaoTotalizadores.reposition();
	}

	private void preparaDescricaoNfeToSort(Vector list) throws SQLException {
		if (ValueUtil.isEmpty(list)) return;
		for (int i = 0; i < list.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) list.items[i];
			itemNfe.dsProduto = itemNfe.getProduto().dsProduto;
		}
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemNfe itemNfe = (ItemNfe) domain;
		String dsGrade = "";
		if (LavenderePdaConfig.isConfigGradeProduto() && ItemNfeService.getInstance().isItemNfeGrade(itemNfe)) {
			ItemPedidoGrade itemPedidoGrade = ItemNfeService.getInstance().getItemPedidoGradeByItemNfe(itemNfe);
			dsGrade = ItemPedidoGradeService.getInstance().getDescricaoGradeResumida(itemPedidoGrade);
		}
		String vlItem = LavenderePdaConfig.ocultaInfosValoresPedido ? " " : " - " + StringUtil.getStringValueToInterface(itemNfe.vlItemPedido);
		String[] item = {itemNfe.getDsItemNfe(), 
						 "",
						 StringUtil.getStringValueToInterface(itemNfe.qtItemFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface) + " " + UnidadeService.getInstance().getDsUnidade(itemNfe.cdUnidade) + vlItem,
						 LavenderePdaConfig.ocultaInfosValoresPedido ? " " : StringUtil.getStringValueToInterface(itemNfe.vlTotalItemPedido), 
						 dsGrade, 
						 ""};
		return item;
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		int heigthGrid = FILL - barBottomContainer.getHeight();
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(sessaoTotalizadores, lvQtTotalItens, LEFT + UiUtil.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
		boolean addRight = true;
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens() && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				UiUtil.add(sessaoTotalizadores, lvVlTotalItensComTributos, RIGHT - UiUtil.getTotalizerGap(), SAME, PREFERRED, PREFERRED);
				addRight = !addRight;
			}
		}
		sessaoTotalizadores.resizeHeight();
    	sessaoTotalizadores.resetSetPositions();
    	sessaoTotalizadores.setRect(LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
    	sessaoTotalizadores.setRect(LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
    	heigthGrid -= sessaoTotalizadores.getHeight();
    	UiUtil.add(this, listContainer, LEFT, getTop(), FILL, heigthGrid);
	}

	protected void onFormEvent(Event event) throws SQLException {}
	
	private void realizaOrdenacaoListaItens(BaseDomain domain, Vector list) {
		ItemNfe.sortAttr = domain.sortAtributte;
   		if ((LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto && COL_CDPRODUTO.equals(domain.sortAtributte)) || COL_NUSEQPRODUTO.equals(domain.sortAtributte)) {
   			SortUtil.qsortInt(list.items, 0, list.size() - 1, true);
   		}  else if (COL_DSPRODUTO.equalsIgnoreCase(domain.sortAtributte)) {
   			SortUtil.qsortString(list.items, 0, list.size() - 1, ValueUtil.getBooleanValue(domain.sortAsc));
   		} else {
   			list.qsort();
   		}
   		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO) && !COL_DSPRODUTO.equalsIgnoreCase(domain.sortAtributte)) {
   			list.reverse();
   		}
	}
	
	protected String getToolTip(BaseDomain domain) throws SQLException {
		ItemNfe item = (ItemNfe) domain;
	    String str = item.getDsItemNfe();
	    return str;
    }

}
