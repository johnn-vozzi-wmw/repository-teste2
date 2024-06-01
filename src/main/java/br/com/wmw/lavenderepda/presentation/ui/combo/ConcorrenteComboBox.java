package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClienteConc;
import br.com.wmw.lavenderepda.business.domain.Concorrente;
import br.com.wmw.lavenderepda.business.service.ClienteConcService;
import br.com.wmw.lavenderepda.business.service.ConcorrenteService;
import totalcross.util.Vector;

public class ConcorrenteComboBox extends BaseComboBox {

	public ConcorrenteComboBox(String title) {
		super(title);
	}

	public String getValue() {
		Concorrente concorrente = (Concorrente)getSelectedItem();
		if (concorrente != null) {
			return concorrente.cdConcorrente;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		Concorrente concorrente = new Concorrente();
		concorrente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		concorrente.cdConcorrente = value;
		select(concorrente);
	}

	public void load() throws SQLException {
		Concorrente concorrente = new Concorrente();
		concorrente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		Vector list = ConcorrenteService.getInstance().findAllByExample(concorrente);
		if (ValueUtil.isNotEmpty(list)) {
			list.qsort();
			add(list);
		}
	}
	
	public void loadClienteConc() throws SQLException {
		Concorrente concorrenteFilter = new Concorrente();
		concorrenteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		Vector concorrenteList = ConcorrenteService.getInstance().findAllByExample(concorrenteFilter);
		if (ValueUtil.isNotEmpty(concorrenteList)) {
			Vector list = new Vector();
			ClienteConc clienteConcFilter = new ClienteConc(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, SessionLavenderePda.getCliente().cdCliente); 
			Vector clienteConcList = ClienteConcService.getInstance().findAllByExample(clienteConcFilter);
			if (ValueUtil.isNotEmpty(clienteConcList)) {
				int size = concorrenteList.size();
				for (int i = 0; i < size; i++) {
					Concorrente concorrente = (Concorrente) concorrenteList.items[i];
					int sizeClienteConcList = clienteConcList.size();
					for (int j = 0; j < sizeClienteConcList; j++) {
						ClienteConc clienteConc = (ClienteConc) clienteConcList.items[j];
						if (clienteConc.cdConcorrente.equals(concorrente.cdConcorrente)) {
							list.addElement(concorrente);
						}
					}
				}
			}
			if (ValueUtil.isNotEmpty(list)) {
				list.qsort();
				add(list);
			} else {
				concorrenteList.qsort();
				add(concorrenteList);
			}
		}
	}
}
