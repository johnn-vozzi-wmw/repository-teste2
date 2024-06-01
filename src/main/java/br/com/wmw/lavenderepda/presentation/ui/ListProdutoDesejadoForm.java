package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Concorrente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoDesejado;
import br.com.wmw.lavenderepda.business.service.ConcorrenteService;
import br.com.wmw.lavenderepda.business.service.ProdutoDesejadoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListProdutoDesejadoForm extends LavendereCrudListForm {
	
	private Vector concorrenteList;
	private Pedido pedido;

    public ListProdutoDesejadoForm(Pedido pedido) throws SQLException {
        super(Messages.PRODUTODESEJADO_TITULO_LISTA);
        setBaseCrudCadForm(new CadProdutoDesejadoForm(pedido));
        singleClickOn = true;
        listContainer = new GridListContainer(3,2);
        listContainer.setColsSort(new String[][]{{Messages.PRODUTODESEJADO_LABEL_DSPRODUTODESEJADO, ProdutoDesejado.NMCOLUNA_DSPRODUTODESEJADO}});
    	listContainer.resizeable = false;
    	listContainer.btResize.setVisible(false);
    	configListContainer(ProdutoDesejado.NMCOLUNA_DSPRODUTODESEJADO);
        loadConcorrenteList();
        this.pedido = pedido;
    }
    
    //@Override
    protected CrudService getCrudService() {
        return ProdutoDesejadoService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
    	if (OrigemPedido.FLORIGEMPEDIDO_ERP.equals(pedido.flOrigemPedido)) {
    		Pedido pedidoClone = (Pedido) this.pedido.clone();
    		pedidoClone.flOrigemPedido = pedido.flOrigemPedidoRelacionado;
			pedidoClone.nuPedido = ValueUtil.isNotEmpty(pedido.nuPedidoRelacionado) ? pedido.nuPedidoRelacionado : ValueUtil.VALOR_EMBRANCO;
    		return new ProdutoDesejado.Builder(pedidoClone).build();
    	}
		return new ProdutoDesejado.Builder(pedido).build();
	}
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
        return getCrudService().findAllByExampleDyn(domain);
    }
    
    //@Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	return ProdutoDesejadoService.getInstance().findByRowKeyDyn(getSelectedRowKey());
    }
    
    //@Override
    protected String[] getItem(Object domain) {
        ProdutoDesejado produtoDesejado = (ProdutoDesejado) domain;
        String[] item = {StringUtil.getStringValue(produtoDesejado.dsProdutoDesejado), "", getDsConcorrente(produtoDesejado)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    //--------------------------------------------------------------
    
    //@Override
    protected void onFormStart() {
    	UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
		UiUtil.add(barBottomContainer, btNovo , 5); 
    }

    public void visibleState() {
    	super.visibleState();
    	btNovo.setVisible(!pedido.isPedidoTransmitido() && !OrigemPedido.FLORIGEMPEDIDO_ERP.equals(pedido.flOrigemPedido));
    }
 
    //@Override
    protected void onFormEvent(Event event) {
    }
    
    private void loadConcorrenteList() throws SQLException {
    	Concorrente concorrente = new Concorrente();
    	concorrente.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	concorrenteList = ConcorrenteService.getInstance().findAllByExample(concorrente);
    }
    
    private String getDsConcorrente(ProdutoDesejado produtoDesejado) {
    	if (produtoDesejado != null) {
    		if (ValueUtil.isNotEmpty(produtoDesejado.cdConcorrente)) {
    			int size = concorrenteList.size();
    			for (int i = 0; i < size; i++) {
    				Concorrente concorrente = (Concorrente) concorrenteList.items[i];
    				if (concorrente.cdConcorrente.equals(produtoDesejado.cdConcorrente)) {
    					return StringUtil.getStringValue(concorrente.dsConcorrente);
    				}
    			}
    		} else if (ValueUtil.isNotEmpty(produtoDesejado.dsOutroConcorrente)) {
    			return produtoDesejado.dsOutroConcorrente;
    		}
    	}
    	return "";
    }
    
}
