package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DivisaoVenda;
import br.com.wmw.lavenderepda.business.domain.Segmento;
import br.com.wmw.lavenderepda.business.service.DivisaoVendaService;
import totalcross.util.Vector;

public class DivisaoVendaComboBox extends BaseComboBox {

	public DivisaoVendaComboBox() throws SQLException {
		super(Messages.DIVISAO_VENDA_LABEL_COMBO);
		carregaDivisaoVenda();
	}

	public String getValue() {
		DivisaoVenda divisaoVenda = (DivisaoVenda)getSelectedItem();
		if (ValueUtil.valueNotEqualsIfNotNull(divisaoVenda, ValueUtil.VALOR_NULL)) {
			return divisaoVenda.cdDivisaoVenda;
		}
		return null;
	}
	
	public DivisaoVenda getDivisaoVenda() {
		return (DivisaoVenda) getSelectedItem();
	}

	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			DivisaoVenda divisaoVenda = new DivisaoVenda();
			divisaoVenda.cdEmpresa = SessionLavenderePda.cdEmpresa;
			divisaoVenda.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(DivisaoVenda.class);
			divisaoVenda.cdDivisaoVenda = value;
			select(divisaoVenda);
		} else {
			setSelectedIndex(BaseComboBox.DefaultItemNull);
		}
	}

	public void carregaDivisaoVenda() throws SQLException {
		removeAll();
		DivisaoVenda divisaoVenda = new DivisaoVenda();
		divisaoVenda.cdEmpresa = SessionLavenderePda.cdEmpresa;
		divisaoVenda.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(DivisaoVenda.class);
		Vector list = DivisaoVendaService.getInstance().findAllByExampleOrderByDsDivisaoVenda(divisaoVenda);
		if (ValueUtil.isNotEmpty(list)) {
			add(list);
			setSelectedIndex(0);
		}
	}

}
