package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServMotivo;
import br.com.wmw.lavenderepda.business.service.RequisicaoServMotivoService;

public class RequisicaoServMotivoComboBox extends BaseComboBox {
	
	public RequisicaoServMotivoComboBox() throws SQLException {
		super(Messages.REQUISICAOSERV_COMBO_MOTIVO);
	}
	
	public String getFlObrigaObservacao() {
		RequisicaoServMotivo requisicaoServMotivo = (RequisicaoServMotivo) getSelectedItem();
		if (requisicaoServMotivo != null) {
			return requisicaoServMotivo.flObrigaObservacao;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}
	
	public String getValue() {
		RequisicaoServMotivo requisicaoServMotivo = (RequisicaoServMotivo) getSelectedItem();
		if (requisicaoServMotivo != null) {
			return requisicaoServMotivo.cdRequisicaoServMotivo;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}
	
	public void reloadRequisicaoServMotivoCombo(String cdTipo) throws SQLException {
		removeAll();
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		add(RequisicaoServMotivoService.getInstance().findAllByUsoMotivo(cdTipo));
		setSelectedIndex(size() == 2 ? 1 : 0);
	}

}
