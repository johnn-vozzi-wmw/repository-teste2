package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DapMatricula;
import br.com.wmw.lavenderepda.business.service.DapMatriculaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDapMatriculaForm extends LavendereCrudListForm {
	
	private LabelValue lvDsCliente;
	private Cliente cliente;

	public ListDapMatriculaForm(Cliente cliente) throws SQLException {
		super(Messages.LISTA_DAP_MATRICULA_TITULO);
		lvDsCliente = new LabelValue(cliente.toString());
		this.cliente = cliente;
		singleClickOn = true;
		constructorListContainer();
	}

	private void constructorListContainer() {
		configListContainer("tb.DSCIDADE");
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		String[][] colsSort = new String[3][2];
		colsSort[0][0] = Messages.LISTA_DAP_CLIENTE_COL_MATRICULA;
		colsSort[0][1] = "tb.CDDAPMATRICULA";
		colsSort[1][0] = Messages.LISTA_DAP_CLIENTE_COL_SAFRA;
		colsSort[1][1] = "saf.DSSAFRA";
		colsSort[2][0] = Messages.LISTA_DAP_CLIENTE_COL_CIDADE;
		colsSort[2][1] = "tb.DSCIDADE";
		listContainer.setColsSort(colsSort);
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		DapMatricula dapClienteFilter = new DapMatricula();
		dapClienteFilter.cdEmpresa = cliente.cdEmpresa;
		dapClienteFilter.cdCliente = cliente.cdCliente;
		return dapClienteFilter;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return DapMatriculaService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lvDsCliente, getLeft(), getTop() + HEIGHT_GAP_BIG);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException { }
	
	@Override
	public void detalhesClick() throws SQLException {
		String id = listContainer.getSelectedId();
		DapMatricula dapClienteSelected = (DapMatricula) DapMatriculaService.getInstance().findByRowKey(id);
		dapClienteSelected.cliente = cliente;
		if (dapClienteSelected != null) {
			show(new ListDapForm(dapClienteSelected));			
		}
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		DapMatricula dapMatricula = (DapMatricula) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(Messages.LIST_DAP_MATRICULA + dapMatricula.cdDapMatricula));
		item.addElement("");
		item.addElement(StringUtil.getStringValue(dapMatricula.getDsLocal()));
		item.addElement(StringUtil.getStringValue(Messages.LIST_DAP_SAFRA + dapMatricula.dsSafra));
		return (String[]) item.toObjectArray();
	}

}