package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.StatusRequisicaoServ;

public class StatusRequisicaoServComboBox extends BaseComboBox {
	
	public StatusRequisicaoServComboBox() {
		super(Messages.REQUISICAOSERV_STATUS);
		load();
	}
	
	public String getValue() {
		if (getSelectedItem() instanceof StatusRequisicaoServ) {
			return ((StatusRequisicaoServ)getSelectedItem()).flStatus;
		}
		return null;
	}
	
	public void load() {
		add(FrameworkMessages.OPCAO_TODOS);
		add(new StatusRequisicaoServ(StatusRequisicaoServ.FLSTATUSPENDENTE, Messages.REQUISICAOSERV_DSSTATUSPENDENTE));
		add(new StatusRequisicaoServ(StatusRequisicaoServ.FLSTATUSANDAMENTO, Messages.REQUISICAOSERV_DSSTATUSANDAMENTO));
		add(new StatusRequisicaoServ(StatusRequisicaoServ.FLSTATUSENCERRADO, Messages.REQUISICAOSERV_DSSTATUSENCERRADO));
		add(new StatusRequisicaoServ(StatusRequisicaoServ.FLSTATUSCANCELADO, Messages.REQUISICAOSERV_DSSTATUSCANCELADO));
		setSelectedIndex(0);
	}

}
