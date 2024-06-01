package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Segmento;
import br.com.wmw.lavenderepda.business.service.SegmentoService;
import totalcross.util.Vector;

public class SegmentoComboBox extends BaseComboBox {

	public SegmentoComboBox() {
		super(LavenderePdaConfig.usaSegmentoNoPedido ? Messages.PEDIDO_LABEL_SEGMENTO : "");
	}

	public String getValue() {
		Segmento segmento = (Segmento)getSelectedItem();
		if (segmento != null) {
			return segmento.cdSegmento;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (value != null) {
			Segmento segmento = new Segmento();
			segmento.cdEmpresa = SessionLavenderePda.cdEmpresa;
			segmento.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Segmento.class);
			segmento.cdSegmento = value;
			//--
			select(segmento);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void setDefaultValue() {
		Object[] segmentos = getItems();
		if (segmentos != null) {
			for (int i = 0; i < segmentos.length; i++) {
				if (((Segmento)segmentos[i]).isFlDefault()) {
					setSelectedIndex(i);
					break;
				}
			}
		}
	}

	public void load(Cliente cliente) throws SQLException {
		removeAll();
		Vector list = SegmentoService.getInstance().loadSegmentoListForCombo(cliente);
		if (!ValueUtil.isEmpty(list)) {
			add(list);
			qsort();
		}
	}

}
