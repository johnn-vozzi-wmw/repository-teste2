package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.business.domain.DescontoVenda;
import br.com.wmw.lavenderepda.business.service.DescontoVendaService;
import totalcross.util.Vector;

public class FaixasDescontoVendaComboBox extends BaseComboBox {
	
	public FaixasDescontoVendaComboBox(String title) {
		super(title);
	}
	
	public DescontoVenda getValue() {
		DescontoVenda descontoVenda = (DescontoVenda)getSelectedItem();
		if (descontoVenda != null) {
			return descontoVenda;
		} else {
			return null;
		}
	}
	
	public void setValue(DescontoVenda descVendaFilter) {
		if (descVendaFilter != null) {
			DescontoVenda descontoVenda = new DescontoVenda();
			descontoVenda.cdEmpresa = descVendaFilter.cdEmpresa;
			descontoVenda.vlVenda = descVendaFilter.vlVenda;
			descontoVenda.cdUf = descVendaFilter.cdUf;
			select(descontoVenda);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}
	
	public void load(String cdEmpresa, String cdEstadoComercial) throws SQLException {
		Vector descontoVendaList = DescontoVendaService.getInstance().findDescontoVendaByEmpresaUfCliente(cdEmpresa, cdEstadoComercial);
		removeAll();
		add(descontoVendaList);
	}

}
