package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import totalcross.util.Vector;

public class CondicaoComercialComboBox extends BaseComboBox {

	public CondicaoComercialComboBox() {
		super(Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL);
	}

	public String getValue() {
		CondicaoComercial condicaoComercial = (CondicaoComercial) getSelectedItem();
		if (condicaoComercial != null) {
			return condicaoComercial.cdCondicaoComercial;
		} else {
			return null;
		}
	}
	
	public CondicaoComercial getCondicaoComercial() {
		CondicaoComercial condicaoComercial = (CondicaoComercial) getSelectedItem();
		if (condicaoComercial != null) {
			return condicaoComercial;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (value != null) {
			CondicaoComercial condicaoComercial = new CondicaoComercial();
			condicaoComercial.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoComercial.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoComercial.class);
			condicaoComercial.cdCondicaoComercial = value;
			//--
			select(condicaoComercial);
		} else {
			setSelectedIndex(0);
		}
	}

	public void setDefaultValue() {
		Object[] condicoesComerciais = getItems();
		if (condicoesComerciais != null) {
			for (int i = 0; i < condicoesComerciais.length; i++) {
				if (((CondicaoComercial) condicoesComerciais[i]).isFlDefault()) {
					setSelectedIndex(i);
					break;
				}
			}
		}
	}

	public void carregaCondicoesComerciais(Pedido pedido) throws SQLException {
		carregaCondicoesComerciais(pedido, false);
	}

	public void carregaCondicoesComerciais(Pedido pedido, boolean flCondicaoComercialParaItensPedido) throws SQLException {
		removeAll();
		Vector list = CondicaoComercialService.getInstance().loadCondicaoComercialListForCombo(pedido, flCondicaoComercialParaItensPedido);
		if (ValueUtil.isNotEmpty(list)) {
			list.qsort();
			add(list);
		}
	}
	
	public void carregaCondicoesComerciaisForListProduto() throws SQLException {
		removeAll();
		Vector list = CondicaoComercialService.getInstance().findAllCondicaoComercialByEmpAndRepForListProduto();
		if (ValueUtil.isNotEmpty(list)) {
			list.qsort();
			add(list);
		}
	}

}
