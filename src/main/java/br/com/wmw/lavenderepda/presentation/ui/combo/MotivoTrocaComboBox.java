package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MotivoTroca;
import br.com.wmw.lavenderepda.business.service.MotivoTrocaService;
import totalcross.util.Vector;

public class MotivoTrocaComboBox  extends BaseComboBox {

	public MotivoTrocaComboBox() throws SQLException {
		super(Messages.ITEMPEDIDO_LABEL_MOTTROCA);
		this.defaultItemType = BaseComboBox.DefaultItemType_SELECT_ONE_ITEM;
		load();
	}

	public String getValue() {
		MotivoTroca motivoTroca = (MotivoTroca)getSelectedItem();
		if (motivoTroca != null) {
			return motivoTroca.cdMotivotroca;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			MotivoTroca motivoTroca = new MotivoTroca();
			motivoTroca.cdEmpresa = SessionLavenderePda.cdEmpresa;
			motivoTroca.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			motivoTroca.cdMotivotroca = value;
			select(motivoTroca);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load() throws java.sql.SQLException {
		removeAll();
		MotivoTroca motivoTroca = new MotivoTroca();
		motivoTroca.cdEmpresa = SessionLavenderePda.cdEmpresa;
		motivoTroca.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector list = MotivoTrocaService.getInstance().findAllByExample(motivoTroca);
		SortUtil.qsortString(list.items, 0, list.size() - 1, true);
		add(list);
	}

}
