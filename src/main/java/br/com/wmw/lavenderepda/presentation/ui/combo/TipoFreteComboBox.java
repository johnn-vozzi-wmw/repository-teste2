package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.service.TipoFreteService;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import totalcross.util.Vector;

public class TipoFreteComboBox extends BaseComboBox {

	public TipoFreteComboBox() throws SQLException {
		super(LavenderePdaConfig.isUsaTipoFretePedido() ? Messages.TIPOFRETE_LABEL_TIPOFRETE : ValueUtil.VALOR_NI);
		if (LavenderePdaConfig.isUsaTipoFretePedido()) {
			loadTipoFrete(null);
		}
	}

	public String getValue() {
		TipoFrete tipoFrete = (TipoFrete) getSelectedItem();
		if (tipoFrete != null) {
			return tipoFrete.cdTipoFrete;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}
	
	public void setValue(String value, String cdUf) {
		if (value != null) {
			TipoFrete tipoFrete = new TipoFrete();
			tipoFrete.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoFrete.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoFrete.class);
			tipoFrete.cdTipoFrete = value;
			tipoFrete.cdUf = LavenderePdaConfig.usaTipoFretePorEstado ? cdUf : TipoFrete.CD_ESTADO_PADRAO;
			select(tipoFrete);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void loadTipoFrete(Pedido pedido) throws SQLException {
		removeAll();
		Vector tipoFreteList;
		try {
			TipoFrete tipoFrete = TipoFreteService.getInstance().getTipoFreteFilterByPedido(pedido);
			tipoFreteList = TipoFreteService.getInstance().findAllByExample(tipoFrete);
		} catch (FilterNotInformedException e) {
			tipoFreteList = new Vector(0);
		}
		add(tipoFreteList);
		qsort();
	}
	
	public void selectTipoFretePadrao() {
		Object[] items = getItems();
		if (ValueUtil.isNotEmpty(items)) {
			if (items.length == 1) {
				setSelectedIndex(0);
			} else {
				for (int i = 0; i < items.length; i++) {
					TipoFrete tipoFreteDefault = (TipoFrete) items[i];
					if (tipoFreteDefault.isDefault()) {
						select(tipoFreteDefault);
						break;
					}
				} 
			}
		}
	}


}
