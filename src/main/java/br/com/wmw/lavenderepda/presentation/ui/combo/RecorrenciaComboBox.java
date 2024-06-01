package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;

public class RecorrenciaComboBox extends BaseComboBox {
	
	public RecorrenciaComboBox() {
		super(Messages.RECORRENCIA_AGENDA_VISITA);
		load(ModoAgendaVisitaComboBox.FLMODO_POR_DATA);
	}
	
	public void setValue(String flOpcaoSelecionada) {
		switch (flOpcaoSelecionada) {
			case AgendaVisita.FLTIPOFREQUENCIA_SEM_FREQUENCIA:
				select(AgendaVisita.FLTIPOFREQUENCIA_SEM_FREQUENCIA);
				break;
			case AgendaVisita.FLTIPOFREQUENCIA_SEMANAL:
				select(AgendaVisita.FLTIPOFREQUENCIA_SEMANAL);
				break;
			case AgendaVisita.FLTIPOFREQUENCIA_QUINZENAL:
				select(AgendaVisita.FLTIPOFREQUENCIA_QUINZENAL);
		        break;
			case AgendaVisita.FLTIPOFREQUENCIA_MENSAL:
				select(AgendaVisita.FLTIPOFREQUENCIA_MENSAL);
		        break;
			case AgendaVisita.FLTIPOFREQUENCIA_UNICA:
				select(AgendaVisita.FLTIPOFREQUENCIA_UNICA);
		        break;
			case AgendaVisita.FLTIPOFREQUENCIA_BIMESTRAL:
				select(AgendaVisita.FLTIPOFREQUENCIA_BIMESTRAL);
		        break;
			case AgendaVisita.FLTIPOFREQUENCIA_TRIMESTRAL:
				select(AgendaVisita.FLTIPOFREQUENCIA_TRIMESTRAL);
		        break;
		    default :
		    	select("");
		    	
		}
	}
	
	public String getValue() {
		String select = (String)getSelectedItem();
		if (select != null) {
			switch (select) {
				case AgendaVisita.FLDSTIPOFREQUENCIA_SEM_FREQUENCIA:
					return AgendaVisita.FLTIPOFREQUENCIA_SEM_FREQUENCIA;
				case AgendaVisita.FLDSTIPOFREQUENCIA_SEMANAL:
					return AgendaVisita.FLTIPOFREQUENCIA_SEMANAL;
				case AgendaVisita.FLDSTIPOFREQUENCIA_QUINZENAL:
					return AgendaVisita.FLTIPOFREQUENCIA_QUINZENAL;
				case AgendaVisita.FLDSTIPOFREQUENCIA_MENSAL:
					return AgendaVisita.FLTIPOFREQUENCIA_MENSAL;
				case AgendaVisita.FLDSTIPOFREQUENCIA_UNICA:
					return AgendaVisita.FLTIPOFREQUENCIA_UNICA;
				case AgendaVisita.FLDSTIPOFREQUENCIA_BIMESTRAL:
					return AgendaVisita.FLTIPOFREQUENCIA_BIMESTRAL;
				case AgendaVisita.FLDSTIPOFREQUENCIA_TRIMESTRAL:
					return AgendaVisita.FLTIPOFREQUENCIA_TRIMESTRAL;
			}
		}
		return "";
	}

	public void load(String modoAgenda) {
		removeAll();
		if (LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) {
			add(AgendaVisita.FLDSTIPOFREQUENCIA_SEM_FREQUENCIA);
			add(AgendaVisita.FLDSTIPOFREQUENCIA_SEMANAL);
		} else if (ValueUtil.valueEquals(ModoAgendaVisitaComboBox.FLMODO_POR_DATA, modoAgenda)) {
			if (LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica) {
				add(AgendaVisita.FLDSTIPOFREQUENCIA_UNICA);
			}
			add(AgendaVisita.FLDSTIPOFREQUENCIA_SEM_FREQUENCIA);
			add(AgendaVisita.FLDSTIPOFREQUENCIA_QUINZENAL);
			add(AgendaVisita.FLDSTIPOFREQUENCIA_MENSAL);
			add(AgendaVisita.FLDSTIPOFREQUENCIA_BIMESTRAL);
			add(AgendaVisita.FLDSTIPOFREQUENCIA_TRIMESTRAL);
		} else {
			add(AgendaVisita.FLDSTIPOFREQUENCIA_SEM_FREQUENCIA);
			add(AgendaVisita.FLDSTIPOFREQUENCIA_SEMANAL);
		}
		setSelectedIndex(0);
	}

}
