package br.com.wmw.lavenderepda.presentation.ui.combo;


import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import br.com.wmw.lavenderepda.business.service.ContatoErpService;
import totalcross.util.Vector;

public class ContatoErpComboBox extends BaseComboBox {
	
	public ContatoErpComboBox(String title) {
		super(title);
	}
	
	public String getValue() {
		ContatoErp contatoErp = (ContatoErp) getSelectedItem();
		if (contatoErp != null) {
			return contatoErp.cdContato;
		}
		return "";
	}
	
	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			ContatoErp contatoErp = new ContatoErp();
			contatoErp.cdEmpresa = SessionLavenderePda.cdEmpresa;
			contatoErp.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			contatoErp.cdCliente = SessionLavenderePda.getCliente().cdCliente;
			contatoErp.cdContato = value;
			select(contatoErp);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}
	
	public void load(String cdCliente) throws SQLException {
		removeAll();
		ContatoErp contatoErpFilter = new ContatoErp();
		contatoErpFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		contatoErpFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		contatoErpFilter.cdCliente = cdCliente;
		Vector list = ContatoErpService.getInstance().findAllByExample(contatoErpFilter);
		if (list != null) {
			add(list);
			qsort();
		}
		contatoErpFilter.flDefault = ValueUtil.VALOR_SIM;
		contatoErpFilter = ContatoErpService.getInstance().findContatoDefault(contatoErpFilter);
		if (contatoErpFilter != null) {
			setValue(contatoErpFilter.cdContato);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

}
