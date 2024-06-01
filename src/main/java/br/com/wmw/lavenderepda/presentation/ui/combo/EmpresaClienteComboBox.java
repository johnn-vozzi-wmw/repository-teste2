package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import totalcross.util.Vector;

public class EmpresaClienteComboBox extends BaseComboBox {

	public int size;

	public EmpresaClienteComboBox(String title) {
		super(title);
	}

	public String getValue() {
		Empresa empresa = (Empresa)getSelectedItem();
		if (empresa != null) {
			return empresa.cdEmpresa;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		Empresa empresa = new Empresa();
		empresa.cdEmpresa = value;
		select(empresa);
	}


	public void loadEmpresasCliente(String cdCliente) throws SQLException {
		Vector listEmpresaCliente = ClienteService.getInstance().findAllEmpresaCliente(cdCliente);
		Vector listEmpresas = new Vector();
		int size = listEmpresaCliente.size();
		for (int i = 0; i < size; i++) {
			Empresa empresaFilter = new Empresa();
			empresaFilter.cdEmpresa = listEmpresaCliente.items[i].toString();
			empresaFilter = (Empresa) EmpresaService.getInstance().findByRowKey(empresaFilter.getRowKey());
			if (empresaFilter != null) {
				listEmpresas.addElement(empresaFilter);
			}
		}
		listEmpresas.qsort();
		add(listEmpresas);
		this.size = size;
	}
	
	public void loadEmpresasRepresentante(String cdRepresentante) throws SQLException {
		Vector listEmpresas = EmpresaService.getInstance().findAllByRepresentante(cdRepresentante);
		listEmpresas.qsort();
		add(listEmpresas);
		this.size = listEmpresas.size();
	}

}