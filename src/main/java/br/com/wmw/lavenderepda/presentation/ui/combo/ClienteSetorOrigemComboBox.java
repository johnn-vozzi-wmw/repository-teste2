package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClienteSetorOrigem;
import br.com.wmw.lavenderepda.business.service.ClienteSetorOrigemService;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ClienteSetorOrigemComboBox extends BaseComboBox{

	public ClienteSetorOrigemComboBox(String title) {
		super(title);
	}

	public String getValue() {
		try {
			ClienteSetorOrigem clienteSetorOrigem = (ClienteSetorOrigem)getSelectedItem();
			if (clienteSetorOrigem != null) {
				return clienteSetorOrigem.cdOrigemSetor;
			} else {
				return "";
			}
		} catch (Throwable e) {
			return getSelectedItem().toString();
		}
	}

	public String getCondicaoPagamento() {
		ClienteSetorOrigem clienteSetorOrigem = (ClienteSetorOrigem)getSelectedItem();
		if (clienteSetorOrigem != null) {
			return clienteSetorOrigem.cdCondicaoPagamento;
		} else {
			return "";
		}
	}

	public void setValue(String cdOrigemSetor, String cdCliRede, String cdTipoCliRede) {
		if (ValueUtil.isNotEmpty(cdOrigemSetor) && ValueUtil.isNotEmpty(cdCliRede) && ValueUtil.isNotEmpty(cdTipoCliRede)) {
			ClienteSetorOrigem clienteSetorOrigem = new ClienteSetorOrigem();
			clienteSetorOrigem.cdEmpresa = SessionLavenderePda.cdEmpresa;
			clienteSetorOrigem.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			clienteSetorOrigem.cdOrigemSetor = cdOrigemSetor;
			clienteSetorOrigem.cdCliRede = cdCliRede;
			clienteSetorOrigem.cdTipoCliRede = cdTipoCliRede.toLowerCase();
			clienteSetorOrigem.cdSetor = null;
			//--
			select(clienteSetorOrigem);
		} else {
			setSelectedIndex(0);
		}
	}


	public void load(String cdTipoCliRede, String cdContrato) throws SQLException {
		if (ValueUtil.isNotEmpty(cdContrato)) {
			removeAll();
			ClienteSetorOrigem clienteSetorOrigem = new ClienteSetorOrigem();
			clienteSetorOrigem.cdEmpresa = SessionLavenderePda.cdEmpresa;
			clienteSetorOrigem.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			clienteSetorOrigem.cdTipoCliRede = cdTipoCliRede;
			clienteSetorOrigem.cdCliRede = cdContrato;
			Vector clienteSetorOrigemList = ClienteSetorOrigemService.getInstance().findAllByExample(clienteSetorOrigem);
			// Retira registros duplicados.
			Hashtable hash = new Hashtable(0);
			if (ValueUtil.isNotEmpty(clienteSetorOrigemList)) {
				int size = clienteSetorOrigemList.size();
				for (int i = 0; i < size; i++) {
					ClienteSetorOrigem value = (ClienteSetorOrigem) clienteSetorOrigemList.items[i];
					hash.put(value.cdOrigemSetor, value);
				}
			}
			clienteSetorOrigemList = hash.getValues();
			hash = null;
			//--
			if (ValueUtil.isEmpty(clienteSetorOrigemList)) {
				defaultItemType = DefaultItemType_NONE_ITEN;
			}
			add(clienteSetorOrigemList);
			setSelectedIndex(0);
		}
	}

	public void loadClienteSemContrato() {
		removeAll();
		add(ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO);
		setSelectedIndex(0);
		setEnabled(false);
	}
}