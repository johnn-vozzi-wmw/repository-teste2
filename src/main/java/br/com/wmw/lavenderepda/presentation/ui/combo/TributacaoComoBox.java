package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.service.TributacaoService;
import totalcross.util.Vector;

public class TributacaoComoBox extends BaseComboBox {
	
	public TributacaoComoBox() {
		super(Messages.LABEL_TRIBUTACAO);
		this.defaultItemType = BaseComboBox.DefaultItemType_SELECT_ONE_ITEM;
	}
	
	public String getValue() {
		Tributacao tributacao = (Tributacao) getSelectedItem();
		if (tributacao != null) {
			return tributacao.cdTributacaoCliente;
		} else {
			return null;
		}
	}
	
	public void setValue(String cdTributacaoCliente, String cdTipoPedido) {
		Tributacao tributacao = new Tributacao();
		tributacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tributacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (cdTributacaoCliente != null) {
			tributacao.cdTributacaoCliente = cdTributacaoCliente;
			tributacao.cdTipoPedido = cdTipoPedido;
			select(tributacao);
			if (getSelectedIndex() == -1) {
				tributacao.cdTipoPedido = Tributacao.CDTIPOPEDIDOVALORPADRAO;
				select(tributacao);
			}
		} else {
			setSelectedIndex(-1);
		}
	}
	
	@Override
	public void select(Object object) {
		for (int i = 1; i < this.size(); i++) {
			Tributacao tributacao = (Tributacao) getItemAt(i);
			if (tributacao.equalsSemUf((Tributacao)object)) {
				object = tributacao;
				break;
			}
		}
		super.select(object);
	}

	public void loadComboTributacao() throws SQLException {
		removeAll();
		Tributacao tributacaoFilter = new Tributacao();
		tributacaoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tributacaoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector tributacaoList = TributacaoService.getInstance().findDistinctTributacao(tributacaoFilter);
		tributacaoList.qsort();
		add(tributacaoList);
	}
}
