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
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.IpiService;
import br.com.wmw.lavenderepda.business.service.ItemNfeReferenciaService;
import br.com.wmw.lavenderepda.business.service.STService;
import br.com.wmw.lavenderepda.business.service.TributosService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListItemNfeReferenciaForm extends LavendereCrudListForm {
	
	private Nfe nfe;
	private Pedido pedido;
	
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lvQtTotalItens;
	private LabelTotalizador lvVlTotalItensComTributos;
	private static final String COL_NUSEQPRODUTO = "NUSEQPRODUTO";
	private static final String COL_CDREFERENCIA = "CDREFERENCIA";
	private static final String COL_DSREFERENCIA = "DSREFERENCIA";
	
	
	public ListItemNfeReferenciaForm(Pedido pedido) throws SQLException {
		super(Messages.ITEMNFEREFERENCIA_NOME_ENTIDADE);
		this.nfe = pedido.getInfoNfe();
		this.pedido = pedido;		
		lvQtTotalItens = new LabelTotalizador("999999999,999");
		lvVlTotalItensComTributos = new LabelTotalizador("999999999,999");
		sessaoTotalizadores = new SessionTotalizerContainer();
		setBaseCrudCadForm(new CadItemNfeForm());
		singleClickOn = true;
		listResizeable = false;
		constructorListContainer();
	}
	
	private void constructorListContainer() {
		configListContainer("CDREFERENCIA");
		int itemCount = LavenderePdaConfig.isConfigGradeProduto() ? 6 : 4;
		listContainer = new GridListContainer(itemCount, 2);
    	listContainer.setColPosition(1, RIGHT);
    	listContainer.setColPosition(3, RIGHT);
		
		String[][] matriz = new String[3][2];		
		matriz[0][0] = Messages.ITEMNFEREFERENCIA_LABEL_NUSEQPRODUTO;
		matriz[0][1] = "NUSEQPRODUTO";
		matriz[1][0] = Messages.ITEMNFEREFERENCIA_LABEL_CDREFERENCIA;
		matriz[1][1] = "CDREFERENCIA";
		matriz[2][0] = Messages.ITEMNFEREFERENCIA_LABEL_DSREFERENCIA;
		matriz[2][1] = "DSREFERENCIA";
	
		listContainer.setColsSort(matriz);
		listContainer.btResize.setVisible(false);
    }

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new ItemNfeReferencia();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemNfeReferenciaService.getInstance();
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vector list = new Vector();
		list = ItemNfeReferenciaService.getInstance().getItemNfeReferenciaList(nfe);
		realizaOrdenacaoListaItens(domain, list);
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

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemNfeReferencia itemNfeReferencia = (ItemNfeReferencia) domain;
		String vlItem = LavenderePdaConfig.ocultaInfosValoresPedido ? " " : " - " + StringUtil.getStringValueToInterface(itemNfeReferencia.vlItemPedido);
		String[] item = {StringUtil.getStringValue(itemNfeReferencia.dsReferencia), 
						 "",
						 StringUtil.getStringValueToInterface(itemNfeReferencia.qtItemFisico) + " " + UnidadeService.getInstance().getDsUnidade(itemNfeReferencia.cdUnidade) + vlItem,
						 LavenderePdaConfig.ocultaInfosValoresPedido ? " " : StringUtil.getStringValueToInterface(itemNfeReferencia.vlTotalItemPedido), 
						 Messages.PEDIDODESCERP_LABEL_NUSEQUENCIA + ":" + itemNfeReferencia.nuSeqProduto,
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
		ItemNfeReferencia.sortAttr = domain.sortAtributte;
   		if ((COL_CDREFERENCIA.equals(domain.sortAtributte)) || COL_NUSEQPRODUTO.equals(domain.sortAtributte)) {
   			SortUtil.qsortInt(list.items, 0, list.size() - 1, true);
   		}  else if (COL_DSREFERENCIA.equalsIgnoreCase(domain.sortAtributte)) {
   			SortUtil.qsortString(list.items, 0, list.size() - 1, ValueUtil.getBooleanValue(domain.sortAsc));
   		} else {
   			list.qsort();
   		}
   		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO) && !COL_DSREFERENCIA.equalsIgnoreCase(domain.sortAtributte)) {
   			list.reverse();
   		}
	}
	
	protected String getToolTip(BaseDomain domain) throws SQLException {
		ItemNfeReferencia item = (ItemNfeReferencia) domain;
		return StringUtil.getStringValue(item.dsReferencia);	
    }

}
