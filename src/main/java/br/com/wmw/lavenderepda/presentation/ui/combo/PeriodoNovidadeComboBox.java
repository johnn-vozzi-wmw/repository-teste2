package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PeriodoNovidade;
import br.com.wmw.lavenderepda.business.service.PeriodoNovidadeService;

public class PeriodoNovidadeComboBox extends BaseComboBox{

	public PeriodoNovidadeComboBox() throws SQLException {
		super(Messages.VALORINDICADOR_PERIODO);
		load();
	}

	public int getValue() {
		PeriodoNovidade periodoNovidade = (PeriodoNovidade) getSelectedItem();
		if (periodoNovidade != null) {
			return periodoNovidade.cdPeriodoNovidade;
		} else {
			return DefaultItemNull;
		}
	}

	public void setValue(int value) {
			PeriodoNovidade periodoNovidade = new PeriodoNovidade();
			periodoNovidade.cdPeriodoNovidade = value;
			select(periodoNovidade);
	}

	private void load() throws SQLException {
		add(PeriodoNovidadeService.getInstance().findAll());
	}

}
