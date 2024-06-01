package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaService;
import totalcross.util.Vector;

public class SugestaoVendaComboBox extends BaseComboBox {

	public SugestaoVendaComboBox(Pedido pedido, String flTipoSugestaoVenda, boolean isFechamentoPedido) throws SQLException {
		this(pedido, flTipoSugestaoVenda, DefaultItemType_ALL, isFechamentoPedido);
	}

	public SugestaoVendaComboBox(Pedido pedido, String flTipoSugestaoVenda, int defaultItemType, boolean isFechamentopedido) throws SQLException {
		super(Messages.SUGESTAO_VENDAS_DESCRICAO);
		if (defaultItemType != 0) {
			this.defaultItemType = defaultItemType;
		}
		load(pedido, flTipoSugestaoVenda, isFechamentopedido);
	}

	public String getValue() {
		SugestaoVenda sugestaoVenda = (SugestaoVenda)getSelectedItem();
		if (sugestaoVenda != null) {
			return sugestaoVenda.cdSugestaoVenda;
		} else {
			String cdSugestoes = "";
			for (int i = 1; i < size(); i++) {
				cdSugestoes += ((SugestaoVenda)getItemAt(i)).cdSugestaoVenda;
				if (i+1 < size()) {
					cdSugestoes += ",";
				}
			}
			return cdSugestoes;
		}
	}

	public void setValue(String cdSugestaoVenda) {
		SugestaoVenda sugestaoVenda = new SugestaoVenda();
		sugestaoVenda.cdEmpresa = SessionLavenderePda.cdEmpresa;
		sugestaoVenda.cdSugestaoVenda = cdSugestaoVenda;
		select(sugestaoVenda);
	}

	public void load(Pedido pedido, String flTipoSugestaoVenda, boolean isFechamentoPedido) throws SQLException {
		removeAll();
		//--
		Vector sugestoesNaoObrigatoriaList = SugestaoVendaService.getInstance().findAllSugestoesPendentesNoPedido(pedido, flTipoSugestaoVenda, false, isFechamentoPedido);
		Vector sugestoesObrigatoriasList = SugestaoVendaService.getInstance().findAllSugestoesPendentesNoPedido(pedido, flTipoSugestaoVenda, true, isFechamentoPedido);
		Vector sugestoesFinalList = VectorUtil.concatVectors(sugestoesNaoObrigatoriaList, sugestoesObrigatoriasList);
		//--
		sugestoesFinalList.qsort();
		add(sugestoesFinalList);
	}

	public Vector getSugestoesVendaSemQtde() {
		Vector listSugestaoVenda = new Vector(size());
		for (int i = 1; i < size(); i++) {
			listSugestaoVenda.addElement(getItemAt(i));
		}
		return listSugestaoVenda;
	}

	public Vector getSugestoesVendaComQtde() {
		Vector listSugestaoVenda = new Vector(size());
		for (int i = 0; i < size(); i++) {
			listSugestaoVenda.addElement(getItemAt(i));
		}
		return listSugestaoVenda;
	}

}