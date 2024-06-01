package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PeriodoEntrega;
import br.com.wmw.lavenderepda.business.service.PeriodoEntregaService;

public class PeriodoEntregaComboBox  extends BaseComboBox {

	public String cdPeriodoEntrega;
	
	public PeriodoEntregaComboBox(String cdPeriodoEntrega) throws SQLException {
		super(Messages.LABEL_ENTIDADE_PERIODOENTREGA);
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		this.cdPeriodoEntrega = cdPeriodoEntrega;
		load();
	}

	public String getValue() {
		PeriodoEntrega periodoEntrega = (PeriodoEntrega) getSelectedItem();
		if (periodoEntrega != null) {
			return periodoEntrega.cdPeriodoEntrega;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			PeriodoEntrega periodoEntrega = new PeriodoEntrega();
			periodoEntrega.cdEmpresa = SessionLavenderePda.cdEmpresa;
			periodoEntrega.cdPeriodoEntrega = value;
			select(periodoEntrega);
		} else {
			setSelectedIndex(0);
		}
	}

	public void load() throws SQLException {
		removeAll();
		add(PeriodoEntregaService.getInstance().findAllPeriodoEntrega(cdPeriodoEntrega));
	}

}
