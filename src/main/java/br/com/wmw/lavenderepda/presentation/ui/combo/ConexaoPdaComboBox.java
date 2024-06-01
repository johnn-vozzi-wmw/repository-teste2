package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import totalcross.util.Vector;

public class ConexaoPdaComboBox extends BaseComboBox {

	public ConexaoPdaComboBox(String title) {
		super(title);
	}

	public HttpConnection getValue() {
		ConexaoPda conexaoPda = (ConexaoPda) getSelectedItem();
		if (conexaoPda != null) {
			return conexaoPda;
		} else {
			return null;
		}
	}

	public void setValue(HttpConnection conexao) {
		select(conexao);
	}

	public void carregueConexoes() throws SQLException {
		//Carrega as conexões do usuário
		ConexaoPda filter = new ConexaoPda();
		filter.cdUsuario = Session.getCdUsuario();
		Vector conexaoList = ConexaoPdaService.getInstance().findAllByExample(filter);
		//--
		int conexaoListSize = 0;
		ConexaoPda conexaoDefault = null;
		if (conexaoList != null) {
			conexaoListSize = conexaoList.size();
			ConexaoPda conexao;
			for (int i = 0; i < conexaoListSize; i++) {
				conexao = (ConexaoPda) conexaoList.items[i];
				if (conexao.isDefault()) {
					conexaoDefault = conexao;
				}
				add(conexao);
			}
		}
		//Seleciona a conexão na combo
		if (conexaoDefault != null) {
			select(conexaoDefault);
		} else if (conexaoListSize > 0) {
			setSelectedIndex(0);
		}
		conexaoList = null;
	}


}
