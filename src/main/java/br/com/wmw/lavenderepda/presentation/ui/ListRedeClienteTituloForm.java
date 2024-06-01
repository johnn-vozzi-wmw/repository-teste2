package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.RedeCliente;
import br.com.wmw.lavenderepda.business.domain.RedeClienteTitulo;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.service.RedeClienteTituloService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListRedeClienteTituloForm extends LavendereCrudListForm {

	private RedeClienteTitulo redeClienteTitulo;
	private String nuTitulo = "";
	private String dtVencimento = "";
	private String vlPago = "";
	private String nuNf = "";
	private String vlNf = "";
	
	private LabelValue lbRede;
	private LabelValue lbRedeCliente;
	
	public ListRedeClienteTituloForm(RedeClienteTitulo redeClienteFilter) throws SQLException {
        super(Messages.TITULOFINANCEIRO_NOME_ENTIDADE);
        setCadForm(redeClienteFilter);
        singleClickOn = true;
    	this.redeClienteTitulo = redeClienteFilter;
		listContainer = new GridListContainer(9, 3);
		listContainer.setColPosition(5, RIGHT);
		configListContainer("NUTITULO");
		listContainer.setColsSort(new String[][]{{Messages.TITULOFINANCEIRO_NOME_ENTIDADE, "NUTITULO"}, {Messages.TITULOFINANCEIRO_LABEL_DTVENCIMENTO, "DTVENCIMENTO"}});
		listContainer.setColTotalizerRight(2, Messages.TITULOFINANCEIRO_LABEL_TOTALTITULOS);
		listResizeable = false;
		loadMessages();
		
		lbRede = new LabelValue("@@@@@@@@@@");
		lbRede.setForeColor(ColorUtil.sessionContainerForeColor);
		lbRede.setText(Messages.REDE_NOME_ENTIDADE + ": "+ redeClienteFilter.rede.toString());
		
		lbRedeCliente = new LabelValue("@@@@@@@@@@");
		lbRedeCliente.setForeColor(ColorUtil.sessionContainerForeColor);
		lbRedeCliente.setText(Messages.CLIENTE_NOME_ENTIDADE + ": "+ redeClienteFilter.redeCliente.toString());
    }

	private void setCadForm(RedeClienteTitulo redeClienteFilter) throws SQLException {
        setBaseCrudCadForm(new CadTituloFinanceiroDynForm(redeClienteFilter));
	}

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return RedeClienteTituloService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new RedeCliente();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Vector list = new Vector();
    	if (redeClienteTitulo != null && redeClienteTitulo.cdCliente != null) {
    		redeClienteTitulo.sortAtributte = domain.sortAtributte;
    		redeClienteTitulo.sortAsc = domain.sortAsc;
    		list = RedeClienteTituloService.getInstance().findAllByExample(redeClienteTitulo);
    	}
        return list;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        RedeClienteTitulo redeClienteTitulo = (RedeClienteTitulo) domain;
        return new String[] {
        		nuTitulo + StringUtil.getStringValue(redeClienteTitulo.nuTitulo),
                " - " + Messages.PRODUTO_LABEL_RS,
                StringUtil.getStringValueToInterface(redeClienteTitulo.vlTitulo),
                vlPago + Messages.PRODUTO_LABEL_RS,
                StringUtil.getStringValueToInterface(redeClienteTitulo.vlPago),
                dtVencimento + StringUtil.getStringValue(redeClienteTitulo.dtVencimento),
                nuNf + StringUtil.getStringValue(redeClienteTitulo.nuNf),
                " - " + vlNf + Messages.PRODUTO_LABEL_RS,
                StringUtil.getStringValueToInterface(redeClienteTitulo.vlNf)};
    }
    
    //@Override
    protected String getSelectedRowKey() throws SQLException {
		return ((BaseListContainer.Item) listContainer.getSelectedItem()).id;
    }

    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		if (((TituloFinanceiro)domain).dtVencimento.isBefore(DateUtil.getCurrentDate()) && ValueUtil.isEmpty(((TituloFinanceiro)domain).dtPagamento)) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_LISTA_CLIENTE_ATRASO);
		}
    }

    private void loadMessages() {
    	nuTitulo = Messages.TITULOFINANCEIRO_NOME_ENTIDADE + " ";
        dtVencimento = Messages.TITULOFINANCEIRO_LABEL_DTVENCIMENTO + " ";
        vlPago = Messages.TITULOFINANCEIRO_LABEL_VLPAGO + " ";
        nuNf = Messages.TITULOFINANCEIRO_LABEL_NUNF + " ";
        vlNf = Messages.TITULOFINANCEIRO_LABEL_VLNF + " ";
    }


    //@Override
    protected void onFormStart() throws SQLException {
    	
    	SessionContainer containerDados = new SessionContainer();
    	UiUtil.add(this, containerDados, LEFT, getNextY() - HEIGHT_GAP, FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
		UiUtil.add(containerDados, lbRede, getLeft(), getNextY(), getWFill(), UiUtil.getLabelPreferredHeight());
		UiUtil.add(containerDados, lbRedeCliente, getLeft(), getNextY() - HEIGHT_GAP, getWFill(), UiUtil.getLabelPreferredHeight());
		
        containerDados.resizeHeight();
        containerDados.resetSetPositions();
        containerDados.setRect(KEEP, KEEP, FILL, containerDados.getHeight() + HEIGHT_GAP);
        
        UiUtil.add(this, listContainer, LEFT, getNextY() - HEIGHT_GAP, FILL, FILL  - barBottomContainer.getHeight());
    }

	public BaseDomain getSelectedDomain() throws SQLException {
		return getCrudService().findByRowKey(getSelectedRowKey());
	}

	//@Override
    protected void onFormEvent(Event event) throws SQLException { }

	@Override
	public void detalhesClick() throws SQLException {
		if (listContainer != null) {
			BaseDomain domain = getCrudService().findByRowKeyDyn(getSelectedRowKey());
			getBaseCrudCadForm().edit(domain);
			show(getBaseCrudCadForm());
		} else {
			int indexSelected = gridEdit.getSelectedIndex();
			if (indexSelected >= 0) {
				BaseDomain domain = getCrudService().findByRowKeyDyn(getSelectedRowKey());
				getBaseCrudCadForm().edit(domain);
				show(getBaseCrudCadForm());
			} else {
				UiUtil.showGridEmptySelectionMessage("");
			}
		}
	}


}