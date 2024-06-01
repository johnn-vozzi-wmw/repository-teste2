package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import br.com.wmw.lavenderepda.business.domain.StatusCargaPedido;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.RotaEntregaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RotaEntregaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusCargaPedidoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListCargaPedidoForm extends LavendereCrudListForm {

	private RotaEntregaComboBox cbRotaEntrega;
	private StatusCargaPedidoComboBox cbStatusCargaPedido;
	private ButtonAction btFecharCargaPedido;
    
	public ListCargaPedidoForm() throws SQLException {
        super(Messages.CARGAPEDIDO_TITULO_LISTA);
        setBaseCrudCadForm(new CadCargaPedidoForm(null, false));
        singleClickOn = true;
        listContainer = new GridListContainer(6, 2, true, false);
        listContainer.setColPosition(3, RIGHT);
        listContainer.setColPosition(5, RIGHT);
        cbRotaEntrega = new RotaEntregaComboBox();
        cbRotaEntrega.load(BaseComboBox.DefaultItemType_ALL);
        cbRotaEntrega.setSelectedIndex(0);
        cbStatusCargaPedido = new StatusCargaPedidoComboBox();
        configListContainer("CDCARGAPEDIDO");
        listContainer.setColsSort(new String[][]{{Messages.CODIGO, "CDCARGAPEDIDO"}, {Messages.CARGAPEDIDO_DESCRICAO, "DSCARGAPEDIDO"}});
        btFecharCargaPedido = new ButtonAction(Messages.CARGAPEDIDO_FECHAR_CARGA, "images/fecharCarga.png");
    }
    
    //@Override
    protected CrudService getCrudService() throws SQLException {
        return CargaPedidoService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() throws SQLException {
		CargaPedido domainFilter = new CargaPedido();
		return domainFilter;
	}
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	CargaPedido cargaPedidoFilter = (CargaPedido) domain;
    	cargaPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cargaPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	cargaPedidoFilter.cdRotaEntrega = cbRotaEntrega.getValue();
    	Vector cargaPedidoList = getCrudService().findAllByExample(cargaPedidoFilter);
    	if (ValueUtil.isNotEmpty(cbStatusCargaPedido.getValue())) {
    		for (int i = 0; i < cargaPedidoList.size(); i++) {
    			CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
    			if (!CargaPedidoService.getInstance().getDsStatusCarga(cargaPedido).equals(cbStatusCargaPedido.getDescription())) {
    				cargaPedidoList.removeElement(cargaPedido);
    				i--;
				}
			}
    	}
        return cargaPedidoList;
    }
    
  //  @Override
    protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
    	super.setPropertiesInRowList(containerItem, domain);
    	if (LavenderePdaConfig.isValidaPesoMinimoCargaPedido()) {
	    	CargaPedido cargaPedido = (CargaPedido) domain;
	    	double qtPesoCargaPedido = PedidoService.getInstance().findQtPesoTotalPedidosCargaPedido(cargaPedido.cdCargaPedido, null);
			if (qtPesoCargaPedido < LavenderePdaConfig.qtdPesoMinimoCargaPedido) {
				containerItem.setBackColor(LavendereColorUtil.COR_CARGA_PEDIDO_ABAIXO_PESO_MIN);
			} else {
				containerItem.setBackColor(LavendereColorUtil.COR_CARGA_PEDIDO_ACIMA_PESO_MIN);
			}
    	}
    }
    
    //@Override
	protected String[] getItem(Object domain) throws SQLException {
		CargaPedido cargaPedido = (CargaPedido) domain;
		Date dtVencimento = CargaPedidoService.getInstance().getDtOldestPedidoCargaPedido(cargaPedido);
		if (ValueUtil.isNotEmpty(dtVencimento)) {
			DateUtil.addDay(dtVencimento, LavenderePdaConfig.nuDiasValidadeCargaPedido);
		}
		String[] item = {
			StringUtil.getStringValue(cargaPedido.cdCargaPedido) + " - ",
			StringUtil.getStringValue(cargaPedido.dsCargaPedido),
			Messages.CARGAPEDIDO_LABEL_PESO + " - " + StringUtil.getStringValue(PedidoService.getInstance().findQtPesoTotalPedidosCargaPedido(cargaPedido.cdCargaPedido, null)) + " " + Messages.CARGAPEDIDO_KG,
			Messages.CARGAPEDIDO_LABEL_VALIDADE + " - " + StringUtil.getStringValue(dtVencimento),
			StringUtil.getStringAbreviada(StringUtil.getStringValue(RotaEntregaService.getInstance().getRotaEntregaByCargaPedido(cargaPedido)), width / 2),
			Messages.CARGAPEDIDO_STATUS + " - " + StringUtil.getStringValue(CargaPedidoService.getInstance().getDsStatusCarga(cargaPedido))};
		return item;
	}

	//@Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    //@Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	CargaPedido cargaPedido = (CargaPedido) domain;
    	return cargaPedido.toString();
    }
    
    //@Override
    public void visibleState() {
    	super.visibleState();
    	btFecharCargaPedido.setVisible(StatusCargaPedido.STATUS_CARGAPEDIDO_ABERTO.equals(cbStatusCargaPedido.getValue()));
    	listContainer.setCheckable(StatusCargaPedido.STATUS_CARGAPEDIDO_ABERTO.equals(cbStatusCargaPedido.getValue()));
    }
    
    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, new LabelName(Messages.CARGAPEDIDO_ROTA), cbRotaEntrega, getLeft(), getTop() + HEIGHT_GAP);
    	UiUtil.add(this, new LabelName(Messages.CARGAPEDIDO_STATUS), cbStatusCargaPedido, getLeft(), AFTER + HEIGHT_GAP);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL , FILL - barBottomContainer.getHeight());
        UiUtil.add(this, barBottomContainer, LEFT, BOTTOM, FILL, UiUtil.getBarMenuPreferredHeight());
        UiUtil.add(barBottomContainer, btFecharCargaPedido, 1);
        UiUtil.add(barBottomContainer, btNovo, 5);
    }

    //@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbRotaEntrega) {
					list();
				} else if (event.target == cbStatusCargaPedido) {
					visibleState();
					list();
				} else if (event.target == btFecharCargaPedido) {
					btFecharCargaPedidoClick();
				} 
				
				break;
			default:
				break;
		}
	}

	private void btFecharCargaPedidoClick() throws SQLException {
		Vector cargaPedidoList = new Vector();
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		for (int i = 0; i < gridSize; i++) {
			CargaPedido cargaPedido = (CargaPedido)CargaPedidoService.getInstance().findByRowKey(listContainer.getId(checkedItens[i]));
			cargaPedidoList.addElement(cargaPedido);
		}
		if (cargaPedidoList.size() == 0) {
			UiUtil.showInfoMessage(Messages.CARGAPEDIDO_MSG_NENHUM_PEDIDO_SELECIONADO);
			return;
		}
		if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.CARGAPEDIDO_MSG_CONFIRM_FECHARCARGAS, cargaPedidoList.size()))) {
			String cargaPedidosNaoFechados = "";
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			try {
				cargaPedidosNaoFechados = CargaPedidoService.getInstance().fecharCargasPedido(cargaPedidoList);
			} finally {
				msg.unpop();
				if (ValueUtil.isEmpty(cargaPedidosNaoFechados)) {
					UiUtil.showConfirmMessage(Messages.CARGAPEDIDO_MSG_FECHARCARGAS_SUCESSO);
				} else {
					RelFechamentoCargaPedidosWindow relFechamentoCargaPedidosWindow = new RelFechamentoCargaPedidosWindow(cargaPedidosNaoFechados);
					relFechamentoCargaPedidosWindow.popup();
				}
				list();
			}
		}
		
	}
    

}
