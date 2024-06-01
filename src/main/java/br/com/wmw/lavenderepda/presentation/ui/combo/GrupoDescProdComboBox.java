package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoDescProd;
import br.com.wmw.lavenderepda.business.service.GrupoDescProdService;
import totalcross.util.Vector;

public class GrupoDescProdComboBox extends BaseComboBox {
	
	public GrupoDescProdComboBox() throws SQLException {
		super();
		defaultItemType = DefaultItemType_ALL;
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			loadGrupoDescProd();
		}
	}

	public GrupoDescProd getGrupoDescProd() {
		GrupoDescProd grupoDescProd = (GrupoDescProd) getSelectedItem();
		if (grupoDescProd != null) {
			return grupoDescProd;
		} else {
			return null;
		}
	}

	public String getValue() {
		GrupoDescProd grupoDescProd = (GrupoDescProd) getSelectedItem();
		if (grupoDescProd != null) {
			return grupoDescProd.cdGrupoDescProd;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		GrupoDescProd grupoDescProd = new GrupoDescProd();
		grupoDescProd.cdEmpresa = SessionLavenderePda.cdEmpresa;
		grupoDescProd.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		grupoDescProd.cdGrupoDescProd = value;
		select(grupoDescProd);
	}

	public void loadGrupoDescProd() throws SQLException {
		removeAll();
		GrupoDescProd grupoDescProd = new GrupoDescProd();
		grupoDescProd.cdEmpresa = SessionLavenderePda.cdEmpresa;
		grupoDescProd.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		Vector list = GrupoDescProdService.getInstance().findAllByExampleCombo(grupoDescProd);
		list.qsort();
		add(list);
	}

}
