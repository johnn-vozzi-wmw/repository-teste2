package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.SupervisorRepService;
import totalcross.util.Vector;

public class RepresentanteSupervComboBox extends BaseComboBox {

	public RepresentanteSupervComboBox() throws SQLException {
		super(SessionLavenderePda.isUsuarioSupervisor() ? Messages.REPRESENTANTE_NOME_ENTIDADE : "");
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			carregaRepresentantes();
		}
	}

	public RepresentanteSupervComboBox(int defaultItemType) throws SQLException {
		super(Messages.REPRESENTANTE_NOME_ENTIDADE);
		this.defaultItemType = defaultItemType;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			carregaRepresentantes();
		}
	}

	public String getValue() {
		SupervisorRep supervisorRep = (SupervisorRep)getSelectedItem();
		if (supervisorRep != null) {
			gravaRepresentante(supervisorRep.cdRepresentante + ";" + supervisorRep.getDsDomain());
			return supervisorRep.cdRepresentante;
		} else {
			gravaRepresentante("");
			return null;
		}
	}
	
	public void setValue(String value) throws SQLException {
		SupervisorRep supervisorRep = new SupervisorRep();
		supervisorRep.cdEmpresa = SessionLavenderePda.cdEmpresa;
		supervisorRep.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		supervisorRep.cdRepresentante = value;
		select(supervisorRep);
		configureSession();
	}

	public void configureSession() throws SQLException {
		SupervisorRep supervisorRep;
		if (getSelectedIndex() != -1) {
			supervisorRep = (SupervisorRep)getSelectedItem();
			if (supervisorRep != null) {
				SessionLavenderePda.setRepresentante(supervisorRep.cdRepresentante, supervisorRep.representante.nmRepresentante);
			}
		}
	}

	public void carregaRepresentantes() throws SQLException {
		removeAll();
		SupervisorRep supervisorRepFilter = new SupervisorRep();
		supervisorRepFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		supervisorRepFilter.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		//--
		Vector listSupRepresentantes = SupervisorRepService.getInstance().findAllByExample(supervisorRepFilter);
		if (listSupRepresentantes != null) {
			int sizeListRepresentantes = listSupRepresentantes.size();
			SupervisorRep supervisorRep;
			for (int i = 0; i < sizeListRepresentantes; i++) {
				supervisorRep = (SupervisorRep)listSupRepresentantes.items[i];
				supervisorRep.representante = new Representante();
				supervisorRep.representante.cdRepresentante = supervisorRep.cdRepresentante;
				supervisorRep.representante.nmRepresentante = RepresentanteService.getInstance().getDescription(supervisorRep.cdRepresentante);
			}
			listSupRepresentantes.qsort();
			add(listSupRepresentantes);
		}
	}

	private void gravaRepresentante(String value) {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.ULTIMOREPSELECBYSUPERVISOR;
		configInterno.vlChave = "0";
		configInterno.vlConfigInterno = value;
		configInterno.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		try {
			if (ValueUtil.isEmpty(value)) {
				if (ConfigInternoService.getInstance().findByRowKey(configInterno.getRowKey()) != null) {
					ConfigInternoService.getInstance().delete(configInterno);
				}
			} else {
				ConfigInternoService.getInstance().insert(configInterno);
			}
		} catch (Throwable e) {
			try {
				ConfigInternoService.getInstance().update(configInterno);
			} catch (SQLException e1) {
				ExceptionUtil.handle(e1);
			}
		}
	}
	
}
