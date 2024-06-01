package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;

public class NivelLiberacaoPedidoComboBox extends BaseComboBox {

	public NivelLiberacaoPedidoComboBox() {
		super(Messages.NIVEL_LIBERACAO_PEDIDO_TITLE);
	}

	public void loadNiveisLiberacao() throws SQLException {
		removeAll();
		int maxNuOrdemLiberacao = ItemPedidoService.getInstance().getMaxNuOrdemLiberacaoItemPendente();
		if (SessionLavenderePda.nuOrdemLiberacaoUsuario > maxNuOrdemLiberacao)
			maxNuOrdemLiberacao = SessionLavenderePda.nuOrdemLiberacaoUsuario;
		for (int i = 1; i <= maxNuOrdemLiberacao; i++) {
			add(i);
		}
	}

}
