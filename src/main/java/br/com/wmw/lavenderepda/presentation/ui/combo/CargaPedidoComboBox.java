package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import totalcross.util.Vector;

public class CargaPedidoComboBox extends BaseComboBox {

	public CargaPedidoComboBox() {
		super(Messages.CARGAPEDIDO_NOME_ENTIDADE);
	}

	public CargaPedido getCargaPedido() {
		CargaPedido cargaPedido = (CargaPedido) getSelectedItem();
		if (cargaPedido != null) {
			return cargaPedido;
		} else {
			return null;
		}
	}

	public String getValue() {
		CargaPedido cargaPedido = (CargaPedido) getSelectedItem();
		if (cargaPedido != null) {
			return cargaPedido.cdCargaPedido;
		} else {
			return null;
		}
	}


	public void setValue(String value) throws SQLException {
		if (value != null) {
			CargaPedido cargaPedido = new CargaPedido();
			cargaPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
			cargaPedido.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			cargaPedido.cdCargaPedido = value;
			select(cargaPedido);
			if (getValue() == null) {
				add(CargaPedidoService.getInstance().findByRowKey(cargaPedido.getRowKey()));
				select(cargaPedido);
			}
		} else {
			setSelectedIndex(-1);
		}
	}

	public void loadCargaPedido(String cdCliente, boolean loadApenasCargasAbertas) throws SQLException {
		removeAll();
		Vector cargaPedidoList = CargaPedidoService.getInstance().findAllCombo(cdCliente);
		if (ValueUtil.isNotEmpty(cargaPedidoList)) {
			if (loadApenasCargasAbertas) {
				removeCargasQueNaoSaoAbertas(cargaPedidoList);
			}
			cargaPedidoList.qsort();
			add(cargaPedidoList);
		}
	}

	private void removeCargasQueNaoSaoAbertas(Vector cargaPedidoList) throws SQLException {
		for (int i = 0; i < cargaPedidoList.size(); i++) {
			CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
			if (cargaPedido.isCargaFechada() || cargaPedido.isEnviadoServidor() || CargaPedidoService.getInstance().validaDataValidadeCarga(cargaPedido)) {
				cargaPedidoList.removeElement(cargaPedido);
				i--;
			}
		}
	}

}
