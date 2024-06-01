package br.com.wmw.lavenderepda.presentation.ui.combo;
import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMetaPerid;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaPeridService;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PlatVendaMetaPeridComboBox extends BaseComboBox{
	
	public PlatVendaMetaPeridComboBox() throws SQLException {
		super(Messages.METAS_PERIODO);
		load();
	}
	
	public String getValue() {
		PlatVendaMetaPerid platVendaMetaPerid = (PlatVendaMetaPerid) getSelectedItem();
			return platVendaMetaPerid != null ? platVendaMetaPerid.cdPeriodo : ValueUtil.VALOR_NI;
	}
	
	public void setValue(String cdPeriodo) {
		PlatVendaMetaPerid platVendaMetaPerid = new PlatVendaMetaPerid();
		platVendaMetaPerid.cdPeriodo = cdPeriodo;
		select(platVendaMetaPerid);
	}
	
	public void load() throws SQLException {
		removeAll();
		Vector periodosList = PlatVendaMetaPeridService.getInstance().getAllGroupBy();
		periodosList.qsort();
		add(periodosList);
		int size = periodosList.size();
		for (int i = 0; i < size; i++) {
			PlatVendaMetaPerid periodo = (PlatVendaMetaPerid)periodosList.items[i];
			Date dtAtual = DateUtil.getCurrentDate();
			if (DateUtil.isAfterOrEquals(dtAtual, periodo.dtInicial) && DateUtil.isBeforeOrEquals(dtAtual, periodo.dtFinal)) {
				setSelectedIndex(i);
				return;
			}
		}
		setSelectedIndex(0);
	}
	
	public String getDsPeriodo() {
		PlatVendaMetaPerid platVendaMetaPerid = (PlatVendaMetaPerid) getSelectedItem();
		return platVendaMetaPerid != null ? platVendaMetaPerid.toString() : ValueUtil.VALOR_NI;
	}
	
}
