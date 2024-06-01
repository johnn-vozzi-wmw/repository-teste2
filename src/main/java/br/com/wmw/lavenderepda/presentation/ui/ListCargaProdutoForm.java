package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CargaProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.CargaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusCargaProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListCargaProdutoForm extends LavendereCrudListForm {

	private StatusCargaProdComboBox cbStatus;
	
    public ListCargaProdutoForm() throws SQLException {
        super(Messages.CARGAPRODUTO_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadCargaProdutoForm());
        singleClickOn = true;
        listContainer = new GridListContainer(6, 2);
        listContainer.setColPosition(3, RIGHT);
        listContainer.setColPosition(5, RIGHT);
        cbStatus = new StatusCargaProdComboBox();
        edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
    }
    
    @Override
    protected CrudService getCrudService() {
        return CargaProdutoService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		CargaProduto domainFilter = new CargaProduto();
		domainFilter.cdStatusFilter = cbStatus.getValue();
		ProdutoService.getInstance().addProdutoFilterByDsFiltro(edFiltro.getValue(), domainFilter.getProduto());
		return domainFilter;
	}
    
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
        return getCrudService().findAllByExample(domain);
    }
    
    @Override
    protected String[] getItem(Object domain) {
        CargaProduto cargaProduto = (CargaProduto) domain;
        Vector itens = new Vector();
		ProdutoService.getInstance().addDescricaoProdutoLista(cargaProduto.getProduto(), itens);
		itens.addElement(StringUtil.getStringValue(cargaProduto.dtCadastro));
		itens.addElement(cargaProduto.hrCadastro);
		itens.addElement(Messages.CARGAPRODUTO_LABEL_QTSOLICITADO + " " +StringUtil.getStringValueToInterface(cargaProduto.qtSolicitado));
		itens.addElement(cargaProduto.getStatus());
		return (String[])itens.toObjectArray();
    }

    @Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

   
    //--------------------------------------------------------------

    @Override
    protected void onFormStart() {
		UiUtil.add(barBottomContainer, btNovo, 5); 
        //--
		UiUtil.add(this, new LabelName(Messages.CARGAPRODUTO_STATUS), cbStatus, getLeft(), getNextY());
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
        UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - barBottomContainer.getHeight());
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbStatus) {
					list();
				}
			}
    	}
    }
    
    @Override
    protected void filtrarClick() throws SQLException {
    	super.filtrarClick();
    	list();
    }
    
    @Override
    public void onEvent(Event event) {
    	super.onEvent(event);
    }
    
    @Override
    public void detalhesClick() throws SQLException {
    	CadEditCargaProdutoForm cadEditCargaProdutoForm = new CadEditCargaProdutoForm((CargaProduto) getSelectedDomain());
    	cadEditCargaProdutoForm.setBaseCrudListForm(this);
    	show(cadEditCargaProdutoForm);
    }
}
