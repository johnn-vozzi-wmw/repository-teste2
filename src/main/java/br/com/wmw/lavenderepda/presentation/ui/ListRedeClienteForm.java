package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RedeCliente;
import br.com.wmw.lavenderepda.business.domain.RedeClienteTitulo;
import br.com.wmw.lavenderepda.business.service.RedeClienteService;
import br.com.wmw.lavenderepda.business.service.RedeClienteTituloService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListRedeClienteForm extends LavendereCrudListForm {

	private RedeCliente redeClienteFilter;

	public ListRedeClienteForm(RedeCliente redeClienteFilter) throws SQLException {
        super(Messages.TITULOFINANCEIRO_NOME_ENTIDADE);
        singleClickOn = true;
    	this.redeClienteFilter = redeClienteFilter;
		listContainer = new GridListContainer(getNuItensGridCliente(), 2);
		configListContainer("NMRAZAOSOCIAL");
		listContainer.setColsSort(new String[][]{{Messages.CODIGO, "CDCLIENTE"}, {Messages.CLIENTE_NOME_ENTIDADE, "NMRAZAOSOCIAL"}});
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
			listContainer.setColsSort(new String[][]{{Messages.PRODUTO_LABEL_CODIGO, "CDCLIENTE"}, {Messages.PEDIDO_LABEL_NMRAZAOSOCIAL, "NMRAZAOSOCIAL"}, {Messages.CLIENTE_LABEL_NMFANTASIA_LISTA, "NMFANTASIA"}});
		}
		listResizeable = false;
		ScrollPosition.AUTO_HIDE = true;
    }
	
	private int getNuItensGridCliente() {
		return (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) ? 6 : 4;
	}

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return RedeClienteService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new RedeCliente();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Vector list = new Vector();
    	if (redeClienteFilter != null && redeClienteFilter.cdRede != null) {
    		redeClienteFilter.sortAtributte = domain.sortAtributte;
    		redeClienteFilter.sortAsc = domain.sortAsc;
    		list = RedeClienteService.getInstance().findAllByExample(redeClienteFilter);
    	} 
        return list;
    }
    
    //@Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	RedeCliente redeCliente = (RedeCliente) domain;
    	return LavenderePdaConfig.exibeNomeFantasiaListaClientes() ? StringUtil.getStringValue(redeCliente.nmFantasia) : StringUtil.getStringValue(redeCliente.nmRazaoSocial);
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	RedeCliente redeCliente = (RedeCliente) domain;
    	Vector item = new Vector(0);
    	item.addElement(StringUtil.getStringValue(redeCliente.cdCliente));
		item.addElement(" - " + StringUtil.getStringValue(redeCliente.getNmClienteImpressao()));
    	item.addElement(FrameworkMessages.CAMPO_VAZIO);
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
			item.addElement(StringUtil.getStringValue(redeCliente.nmFantasia));
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
    	String dsEndereco = StringUtil.getStringValue(redeCliente.dsCidadeComercial) +
        					StringUtil.getStringValue(ValueUtil.isNotEmpty(redeCliente.dsEstadoComercial) ? new StringBuffer(" - ").append(redeCliente.dsEstadoComercial) : FrameworkMessages.CAMPO_VAZIO) +
        					StringUtil.getStringValue(ValueUtil.isNotEmpty(redeCliente.dsBairroComercial) ? new StringBuffer(" - ").append(redeCliente.dsBairroComercial) : FrameworkMessages.CAMPO_VAZIO);
    	item.addElement(dsEndereco);
        return (String[]) item.toObjectArray();
    }
    
    //@Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		if (((RedeCliente)domain).isAtrasado()) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_LISTA_CLIENTE_ATRASO);
		}
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
    }

    public void visibleState() {
    	super.visibleState();
		barTopContainer.setVisible(false);
		barBottomContainer.setVisible(false);
    }
    
    @Override
    public void detalhesClick() throws SQLException {
    	RedeCliente redeCliente = (RedeCliente) getSelectedDomain();
    	if (redeCliente != null) {
    		RedeClienteTitulo redeClienteTituloFilter = getRedeClienteTituloFilter(redeCliente);
    		if (redeClienteTituloFilter.rede != null) {
    			if (RedeClienteTituloService.getInstance().countByExample(redeClienteTituloFilter) <= 0) {
    				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.LISTA_REDECLIENTE_MSG_CLIENTE_SEM_TITULO, redeClienteTituloFilter.cdCliente));
    				return;
    			}
		    	ListRedeClienteTituloForm list = new ListRedeClienteTituloForm(redeClienteTituloFilter);
		    	show(list);
    		} else {
    			UiUtil.showErrorMessage(Messages.REDECLIENTE_REDE_INVALIDA);
    		}
    	}
    }

	private RedeClienteTitulo getRedeClienteTituloFilter(RedeCliente redeCliente) throws SQLException {
		RedeClienteTitulo redeClienteTituloFilter = new RedeClienteTitulo();
		redeClienteTituloFilter.cdCliente = redeCliente.cdCliente;
		redeClienteTituloFilter.rede = SessionLavenderePda.getCliente().getRede();
		redeClienteTituloFilter.redeCliente = redeCliente;
		return redeClienteTituloFilter;
	}

	//@Override
    protected void onFormEvent(Event event) throws SQLException { }


}