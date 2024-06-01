package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cesta;
import br.com.wmw.lavenderepda.business.service.CestaService;

public class CestaComboBox extends BaseComboBox {

	private String CESTA_NAO_USAR = "";

	public CestaComboBox(String title) throws SQLException {
		this(title, true);
	}

	public CestaComboBox(String title, String cdCliente, boolean usaOpcaoTodos) throws SQLException {
		this(title, true, cdCliente, usaOpcaoTodos);
	}

	public CestaComboBox(String title, int defaultItemType) throws SQLException {
		super(title);
		this.defaultItemType = defaultItemType;
		load(false, null, defaultItemType, true);
	}

	public CestaComboBox(String title, boolean defaultItem) throws SQLException {
		this(title, defaultItem, null, true);
	}

	public CestaComboBox(String title, boolean defaultItem, String cdCliente) throws SQLException {
		this(title, defaultItem, cdCliente, false);
	}
	
	public CestaComboBox(String title, boolean defaultItem, String cdCliente, boolean usaOpcaoTodos) throws SQLException {
		super(title);
		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
			load(defaultItem, cdCliente, 0, usaOpcaoTodos);
		}
	}

	public String getValue() {
		if (getSelectedItem() instanceof Cesta) {
			return ((Cesta) getSelectedItem()).cdCesta;
		}
		return null;
	}

	public void setValue(String value) throws SQLException {
		if (ValueUtil.isEmpty(value)) {
			select(CESTA_NAO_USAR);
		} else {
			Cesta cesta = new Cesta();
			cesta.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			cesta.cdEmpresa = SessionLavenderePda.cdEmpresa;
			cesta.cdCesta = value;
			cesta = (Cesta) CestaService.getInstance().findByRowKey(cesta.getRowKey());
			select(cesta);
		}
	}

	public void load(boolean defaultItem, String cdCliente, int defaultItemType, boolean usaOpcaoTodos) throws SQLException {
		removeAll();
		add(CestaService.getInstance().findAllCestas(cdCliente));
		if (defaultItem) {
			if (defaultItemType == 0) {
				if (usaOpcaoTodos) {
					this.defaultItemType = DefaultItemType_ALL;
				}
				CESTA_NAO_USAR = Messages.CESTA_NAO_USAR;
				add(CESTA_NAO_USAR);
			} else {
				this.defaultItemType = defaultItemType;
			}
		}
		qsort();
	}
}
