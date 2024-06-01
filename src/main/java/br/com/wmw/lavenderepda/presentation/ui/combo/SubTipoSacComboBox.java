package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.SubTipoSac;
import br.com.wmw.lavenderepda.business.domain.TipoSac;
import br.com.wmw.lavenderepda.business.service.SubTipoSacService;
import br.com.wmw.lavenderepda.business.service.TipoSacService;
import totalcross.util.Vector;

public class SubTipoSacComboBox extends BaseComboBox {
	
	public String cdTipoSac;
	
	public SubTipoSacComboBox() throws SQLException {
		super(Messages.SAC_LABEL_TIPO_SAC);
		this.defaultItemType = DefaultItemType_ALL;
	}
	
	public void setValue(TipoSac tipoSac) {
		if (tipoSac != null) {
			SubTipoSac subTipoSac = new SubTipoSac(); 
			subTipoSac.cdEmpresa = SessionLavenderePda.cdEmpresa;
			subTipoSac.cdTipoSac = tipoSac.cdTipoSac;
			subTipoSac.cdSubTipoSac = tipoSac.subTipoSac.cdSubTipoSac;
			select(subTipoSac);
		} else {
			setSelectedIndex(0);
		}
	}
	
	public String getValue() {
		SubTipoSac subTipoSac = (SubTipoSac) getSelectedItem();
		if (subTipoSac != null) {
			return subTipoSac.cdTipoSac;
		}
		return ValueUtil.VALOR_NI;
	}
	
	public void carregaSubTiposSac() throws SQLException {
		removeAll();
		SubTipoSac subTipoSac = new SubTipoSac();
		subTipoSac.cdEmpresa = SessionLavenderePda.cdEmpresa;
		subTipoSac.cdTipoSac = cdTipoSac;
		Vector subTipoSacList = SubTipoSacService.getInstance().findAllByExample(subTipoSac);
		subTipoSacList.qsort();
		if (subTipoSacList.size() > 0) {
			add(subTipoSacList);
			
		}
	}

}
