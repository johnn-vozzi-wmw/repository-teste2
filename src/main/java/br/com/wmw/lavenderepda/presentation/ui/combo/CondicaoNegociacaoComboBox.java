package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoNegociacao;
import br.com.wmw.lavenderepda.business.service.CondicaoNegociacaoService;

public class CondicaoNegociacaoComboBox extends BaseComboBox {
	
	public CondicaoNegociacaoComboBox() throws SQLException {
		super(Messages.PEDIDO_LABEL_CONDICAONEGOCIACAO);
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		loadCondicaoNegociacao();
		setSelectedIndex(0);
	}
	
	public CondicaoNegociacao getValue() {
		CondicaoNegociacao condicaoNegociacao = (CondicaoNegociacao) getSelectedItem();
		return condicaoNegociacao != null ? condicaoNegociacao : new CondicaoNegociacao();
	}
	
	public void setValue (CondicaoNegociacao condicaoNegociacao) {
		if (condicaoNegociacao != null) {
			select(condicaoNegociacao);
		} else {
			setSelectedIndex(0);
		}
	}
	
	public void setValue (String cdCondicaoNegociacao) {
		if (ValueUtil.isNotEmpty(cdCondicaoNegociacao)) {
			CondicaoNegociacao condicaoNegociacao = new CondicaoNegociacao();
			condicaoNegociacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoNegociacao.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoNegociacao.class);
			condicaoNegociacao.cdCondicaoNegociacao = cdCondicaoNegociacao;
			select(condicaoNegociacao);
		} else {
			setSelectedIndex(0);
		}
	}
	
	private void loadCondicaoNegociacao() throws SQLException {
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido) {
			add(CondicaoNegociacaoService.getInstance().loadCondicaoNegociacao());
		}
	}

}
