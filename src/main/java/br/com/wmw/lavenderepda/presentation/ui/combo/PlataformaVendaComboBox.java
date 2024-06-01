package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PlataformaVenda;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliente;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaClienteService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaService;
import totalcross.util.Vector;

public class PlataformaVendaComboBox extends BaseComboBox {

	public PlataformaVendaComboBox() {
		super();
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
	}

	public PlataformaVendaComboBox(int defaultItemType) {
		super();
		this.defaultItemType = defaultItemType;
	}
	
	public String getValue() {
		PlataformaVenda plataforma = (PlataformaVenda) getSelectedItem();
		return plataforma != null ? plataforma.cdPlataformaVenda : null;
	}

	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			PlataformaVenda plataforma = new PlataformaVenda();
			plataforma.cdEmpresa = SessionLavenderePda.cdEmpresa;
			plataforma.cdPlataformaVenda = value;
			//--
			select(plataforma);
		} else {
			setSelectedIndex(0);
		}
	}

	public void carregaPlataformas(String cdCentroCusto) throws SQLException {
		carregaPlataformas(cdCentroCusto, null, SessionLavenderePda.getCdRepresentanteFiltroDados(getClass()));
	}
	
	public void carregaPlataformas(String cdCentroCusto, String cdCliente) throws SQLException {
		carregaPlataformas(cdCentroCusto, cdCliente, SessionLavenderePda.getCdRepresentanteFiltroDados(getClass()));
	}
	
	public void carregaPlataformas(String cdCentroCusto, String cdCliente, String cdRepresentante) throws SQLException {
		removeAll();
		addDefaultItens();
		setSelectedIndex(0);
		
		if (ValueUtil.isEmpty(cdCentroCusto)) return;
		
		boolean fromAllClients = cdCliente == null;
		PlataformaVendaCliente filter = new PlataformaVendaCliente();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdCentroCusto = cdCentroCusto;
		if (!fromAllClients) {			
			filter.cdCliente = cdCliente;
		}
		filter.cdRepresentante = cdRepresentante;
		Vector plataformaList;
		if (fromAllClients) {
			plataformaList = PlataformaVendaService.getInstance().getDistinctiListPlataformaVendaByPlataformaVendaCliente(filter);
		} else {
			filter.ignoreCliente = !PlataformaVendaClienteService.getInstance().hasPlataformaForThisClient(filter.cdEmpresa, cdRepresentante, filter.cdCliente);
			plataformaList = PlataformaVendaService.getInstance().getListPlataformaVendaByPlataformaVendaCliente(filter);
		}
		if (ValueUtil.isNotEmpty(plataformaList)) {
			removeAll();
			plataformaList.qsort();
			add(plataformaList);
			setSelectedIndex(plataformaList.size() == 1 ? 1 : 0);
		}
	}

}
