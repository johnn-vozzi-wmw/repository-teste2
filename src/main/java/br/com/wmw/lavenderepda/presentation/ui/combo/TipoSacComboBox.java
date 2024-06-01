package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.TipoSac;
import br.com.wmw.lavenderepda.business.service.RedeService;
import br.com.wmw.lavenderepda.business.service.TipoSacService;
import totalcross.util.Vector;

public class TipoSacComboBox extends BaseComboBox {
	
	public TipoSacComboBox() throws SQLException {
		super(Messages.SAC_LABEL_TIPO_SAC);
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		carregaTiposSac();
	}
	
	public void setValue(String value) {
		if (value != null) {
			TipoSac tpSac = new TipoSac();
			tpSac.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tpSac.cdTipoSac = value;
			select(tpSac);
		} else {
			setSelectedIndex(0);
		}
	}
	
	public String getValue() {
		TipoSac tpSac = (TipoSac) getSelectedItem();
		if (tpSac != null) {
			return tpSac.cdTipoSac;
		}
		return ValueUtil.VALOR_NI;
	}
	
	private void carregaTiposSac() throws SQLException {
		TipoSac tpSac = new TipoSac();
		tpSac.cdEmpresa = SessionLavenderePda.cdEmpresa;
		Vector tpSacList = TipoSacService.getInstance().findAllByExample(tpSac);
		tpSacList.qsort();
		add(tpSacList);
		if (tpSacList.size() > 1) {
			setSelectedIndex(0);
		}
	}

}
