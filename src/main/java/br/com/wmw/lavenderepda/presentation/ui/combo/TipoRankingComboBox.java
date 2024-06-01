package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.TipoRanking;
import br.com.wmw.lavenderepda.business.service.TipoRankingService;

public class TipoRankingComboBox extends BaseComboBox {
	
	public TipoRankingComboBox() throws SQLException {
		super(Messages.TIPORANKING_NOME_ENTIDADE);
		loadTipoRanking();
	}
	
	public int getValue() {
		TipoRanking tipoRanking = (TipoRanking) getSelectedItem();
		if (tipoRanking != null) {
			return tipoRanking.cdTipoRanking;
		} else {
			return DefaultItemNull;
		}
	}
	
	public void setValue(int value) {
		TipoRanking tipoRanking = new TipoRanking();
		tipoRanking.cdTipoRanking = value;
		select(tipoRanking);
	}
	
	private void loadTipoRanking() throws SQLException {
		removeAll();
		add(TipoRankingService.getInstance().findAll());
	}

}
