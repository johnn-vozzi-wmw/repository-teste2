package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import totalcross.util.Date;

public class DiaAgendaComboBox extends BaseComboBox {

	private Date lastValidDate;

	public DiaAgendaComboBox(String title, Date lastValidDate) throws SQLException {
		super(title);
		this.lastValidDate = lastValidDate;
		load(this.lastValidDate);
	}

	public Date getLastValidDate() {
		return lastValidDate;
	}

	public Date getValue() {
		if (getSelectedItem() != null) {
			return DateUtil.getDateValue((Date)getSelectedItem());
		} else {
			return null;
		}
	}

	public void load(Date lastValidDate) throws SQLException {
		AgendaVisitaService.getInstance().getLastValidDay(lastValidDate);
		add(lastValidDate);
		Date data = null;
		for (int i = 0; i <= 6; i++) {
			data = DateUtil.getCurrentDate();
			if (i > 0) {
				data.advance(i);
				if (LavenderePdaConfig.isSistemaPermiteCadastroApenasSabado() && DateUtil.DATA_SEMANA_DOMINGO == data.getDayOfWeek()) {
					continue;
				}
				if (LavenderePdaConfig.isSistemaPermiteCadastroApenasDomingo() && DateUtil.DATA_SEMANA_SABADO == data.getDayOfWeek()) {
					continue;
				} else if (ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaAgendaVisitaFinalDeSemana)) {
					if (DateUtil.DATA_SEMANA_DOMINGO == data.getDayOfWeek() || DateUtil.DATA_SEMANA_SABADO == data.getDayOfWeek()){
						continue;
					}
				}
			}
			add(data);
			
		}
	}

}