package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotivoPerda;
import br.com.wmw.lavenderepda.business.service.MotivoPerdaService;
import totalcross.util.Vector;

public class MotivoPerdaPedidoComboBox extends BaseComboBox {
	
	public MotivoPerdaPedidoComboBox() throws SQLException {
		super(Messages.PEDIDO_LABEL_MOTIVO_PEDIDO_PERDIDO_COMBO);
		this.defaultItemType = BaseComboBox.DefaultItemType_SELECT_ONE_ITEM;
		load();
	}

	public MotivoPerda getMotivoPerda() {
		return (MotivoPerda)getSelectedItem();
	}

	public String getValue() {
		MotivoPerda MotivoPerda = (MotivoPerda)getSelectedItem();
		if (MotivoPerda != null) {
			return MotivoPerda.cdMotivoPerda;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public void setValue(String value) {
		MotivoPerda MotivoPerda = new MotivoPerda();
		MotivoPerda.cdMotivoPerda = value;
		select(MotivoPerda);
	}

	private void load() throws SQLException {
		Vector list = MotivoPerdaService.getInstance().findAll();
		list.qsort();
		add(list);
	}

}
