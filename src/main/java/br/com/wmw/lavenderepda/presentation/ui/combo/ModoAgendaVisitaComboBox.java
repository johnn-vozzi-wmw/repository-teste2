package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class ModoAgendaVisitaComboBox extends BaseComboBox {
	
	public static final String FLMODO_POR_DATA = "1";
	public final String FLMODO_POR_DATA_DESC = "Por Data";
	public static final String FLMODO_POR_DIA_SEMANA = "2";
	public final String FLMODO_POR_DIA_SEMANA_DESC = "Por Dia Da Semana";
	
	public ModoAgendaVisitaComboBox() {
		super(Messages.MODO_AGENDA);
		load();
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaFrequenciaDataBaseENaSemanaDoMes()) {
        	setSelectedIndex(0);
        } else {
        	setSelectedIndex(1);
        }
	}
	
	
	private void load() {
		removeAll();
		add(FLMODO_POR_DATA_DESC);
		add(FLMODO_POR_DIA_SEMANA_DESC);
	}

}
