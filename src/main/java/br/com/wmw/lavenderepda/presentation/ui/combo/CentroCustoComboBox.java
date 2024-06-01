package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CentroCusto;
import br.com.wmw.lavenderepda.business.service.CentroCustoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaClienteService;
import totalcross.util.Vector;

public class CentroCustoComboBox extends BaseComboBox {

	public CentroCustoComboBox() {
		super();
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
	}

	public CentroCustoComboBox(int defaultItemType) {
		super();
		this.defaultItemType = defaultItemType;
	}
	
	public String getValue() {
		CentroCusto centroCusto = (CentroCusto) getSelectedItem();
		return centroCusto != null ? centroCusto.cdCentroCusto : null;
	}

	public void setValue(String value) {
		setValue(value, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
	}
	
	public void setValue(String value, String cdRepresentante) {
		if (ValueUtil.isNotEmpty(value)) {
			CentroCusto centroCusto = new CentroCusto();
			centroCusto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			centroCusto.cdRepresentante = cdRepresentante;
			centroCusto.cdCentroCusto = value;
			//--
			select(centroCusto);
		} else {
			setSelectedIndex(0);
			return;
		}
		select(new CentroCusto(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(CentroCusto.class), value));
	}

	public void carregaCentroCusto() throws SQLException {
		carregaCentroCusto(false, SessionLavenderePda.getCdRepresentanteFiltroDados(CentroCusto.class));
	}
	
	public void carregaCentroCusto(String cdRepresentante) throws SQLException {
		carregaCentroCusto(false, cdRepresentante);
	}
	
	public void carregaCentroCusto(boolean fromAllClients) throws SQLException {
		carregaCentroCusto(fromAllClients, SessionLavenderePda.getCdRepresentanteFiltroDados(CentroCusto.class));
	}
	
	public void carregaCentroCusto(boolean fromAllClients, String cdRepresentante) throws SQLException {
		removeAll();
		CentroCusto centroCustoFilter = new CentroCusto();
		centroCustoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		centroCustoFilter.cdRepresentante = cdRepresentante;
		if (!fromAllClients) {			
			centroCustoFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		Vector centroCustoList;
		if (fromAllClients) {
			centroCustoList = CentroCustoService.getInstance().findAllDistinctByExample(centroCustoFilter);
		} else {
			centroCustoFilter.ignoreCliente = !PlataformaVendaClienteService.getInstance().hasPlataformaForThisClient(centroCustoFilter.cdEmpresa, cdRepresentante, centroCustoFilter.cdCliente);
			centroCustoList = CentroCustoService.getInstance().findAllByExample(centroCustoFilter);
		}
		if (ValueUtil.isEmpty(centroCustoList)) return; 
		
		centroCustoList.qsort();
		add(centroCustoList);
		setSelectedIndex(centroCustoList.size() == 1 ? 1 : 0);
	}

}
