package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.lavenderepda.presentation.ui.ListAtendimentoHistForm;

public class WindowUtil {
	
	public static void btAtendimentoHistClick(BaseContainer container, String cdEmpresa, String cdCliente) throws SQLException {
		container.show(new ListAtendimentoHistForm(cdEmpresa, cdCliente));
	}

}
