package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotivoColeta;
import br.com.wmw.lavenderepda.business.service.MotivoColetaService;
import totalcross.util.Vector;

public class MotivoColetaComboBox  extends BaseComboBox {

	public MotivoColetaComboBox() throws SQLException {
		super(Messages.LABEL_MOTIVOCOLETA);
		this.defaultItemType = BaseComboBox.DefaultItemType_SELECT_ONE_ITEM;
		load();
	}

	public String getValue() {
		MotivoColeta motivoColeta = (MotivoColeta) getSelectedItem();
		if (motivoColeta != null) {
			return motivoColeta.cdMotivo;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			MotivoColeta motivoColeta = new MotivoColeta();
			motivoColeta.cdMotivo = value;
			select(motivoColeta);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load() throws java.sql.SQLException {
		removeAll();
		MotivoColeta motivoColeta = new MotivoColeta();
		motivoColeta.flEncerraAutomatico = ValueUtil.VALOR_NAO;
		Vector list = MotivoColetaService.getInstance().findAllByExample(motivoColeta);
		list.qsort();
		add(list);
		setSelectedIndex(0);
	}

}
