package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.MotRegistroVisita;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.MotRegistroVisitaService;

public class MotRegistroVisitaComboBox extends BaseComboBox {

	public MotRegistroVisitaComboBox() {
		super(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
	}

	public String getValue() {
		MotRegistroVisita motRegistroVisita = (MotRegistroVisita)getSelectedItem();
		if (motRegistroVisita != null) {
			return motRegistroVisita.cdMotivoRegistroVisita;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (value != null) {
			MotRegistroVisita motRegistroVisita = new MotRegistroVisita();
			motRegistroVisita.cdEmpresa = SessionLavenderePda.cdEmpresa;
			motRegistroVisita.cdMotivoRegistroVisita = value;
			select(motRegistroVisita);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void load(String flPositivada) throws SQLException {
		MotRegistroVisita motRegistroVisita = new MotRegistroVisita();
		motRegistroVisita.cdEmpresa = SessionLavenderePda.cdEmpresa;
		motRegistroVisita.flVisitaPositivada = flPositivada;
		aplicaFiltroTipoCadastroCliente(motRegistroVisita);
		add(MotRegistroVisitaService.getInstance().findAllByExample(motRegistroVisita));
	}

	public void load() throws java.sql.SQLException {
		MotRegistroVisita motRegistroVisita = new MotRegistroVisita();
		motRegistroVisita.cdEmpresa = SessionLavenderePda.cdEmpresa;
		aplicaFiltroTipoCadastroCliente(motRegistroVisita);
		add(MotRegistroVisitaService.getInstance().findAllByExample(motRegistroVisita));
	}
	
	public void loadToRegistroSaidaCliente() throws SQLException {
		if (LavenderePdaConfig.isUsaRegistroManualDeVisitaSemAgenda() || LavenderePdaConfig.usaPositivacaoAgendaVisitaSemPedido()) {
			load();
		} else {
			load(ValueUtil.VALOR_NAO);
		}
	}
	
	private void aplicaFiltroTipoCadastroCliente(MotRegistroVisita motRegistroVisitaFilter) throws SQLException {
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && SessionLavenderePda.getCliente() != null) {
			Cliente cliente = ClienteService.getInstance().getCliente(SessionLavenderePda.getCliente().cdEmpresa, SessionLavenderePda.getCliente().cdRepresentante, SessionLavenderePda.getCliente().cdCliente);
			if (cliente.isModoDeProspeccao()) {
				motRegistroVisitaFilter.flTipoCadastroCliente = cliente.flTipoCadastro;
			}
		}
	}
}
