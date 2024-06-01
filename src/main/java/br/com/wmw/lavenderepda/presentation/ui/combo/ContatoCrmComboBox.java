package br.com.wmw.lavenderepda.presentation.ui.combo;


import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ContatoCrm;
import br.com.wmw.lavenderepda.business.service.ContatoCrmService;
import totalcross.util.Vector;


public class ContatoCrmComboBox extends BaseComboBox {
	
	public String cdContato;
	
	public ContatoCrmComboBox(String title) throws SQLException {
		super(title);
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		loadContatoCrm();
	}
	
	public String getValue() {
		ContatoCrm contatoErp = (ContatoCrm) getSelectedItem();
		if (contatoErp != null) {
			return contatoErp.cdContato;
		}
		return ValueUtil.VALOR_NI;
	}
	
	public void setValue(String cdContato, String cdClienteSac) {
		if (ValueUtil.isNotEmpty(cdContato)) {
			ContatoCrm contatoCrm = new ContatoCrm();
			contatoCrm.cdEmpresa = SessionLavenderePda.cdEmpresa;
			contatoCrm.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			if (SessionLavenderePda.getCliente() != null) {
				contatoCrm.cdCliente = SessionLavenderePda.getCliente().cdCliente;
			} else {
				contatoCrm.cdCliente = cdClienteSac;
			}
			contatoCrm.cdContato = cdContato;
			select(contatoCrm);
		} else {
			setSelectedIndex(0);
		}
	}
	
	public void loadContatoCrm() throws SQLException {
		removeAll();
		ContatoCrm contatoCrmFilter = new ContatoCrm();
		contatoCrmFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		contatoCrmFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (SessionLavenderePda.getCliente() != null) {
			contatoCrmFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
			
		}
		Vector contatoList = ContatoCrmService.getInstance().findAllByExample(contatoCrmFilter);
		contatoList.qsort();
		add(contatoList);
		if (contatoList.size() > 1) {
			setSelectedIndex(0);
		}
	}

}
