package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClienteSetorOrigem;
import br.com.wmw.lavenderepda.business.service.ClienteSetorOrigemService;
import totalcross.util.Vector;

public class ClienteSetorComboBox extends BaseComboBox{

	public ClienteSetorComboBox(String title) {
		super(title);
	}

	public String getValue() {
		try {
			ClienteSetorOrigem clienteSetor = (ClienteSetorOrigem)getSelectedItem();
			if (clienteSetor != null) {
				return clienteSetor.cdSetor;
			} else {
				return "";
			}
		} catch (Throwable e) {
			return getSelectedItem().toString();
		}
	}

	public void setValue(String cdOrigemSetor, String cdCliRede, String cdTipoCliRede, String cdSetor) {
		if (ValueUtil.isNotEmpty(cdOrigemSetor) && ValueUtil.isNotEmpty(cdCliRede) && ValueUtil.isNotEmpty(cdTipoCliRede) && ValueUtil.isNotEmpty(cdSetor)) {
			ClienteSetorOrigem clienteSetorOrigem = new ClienteSetorOrigem();
			clienteSetorOrigem.cdEmpresa = SessionLavenderePda.cdEmpresa;
			clienteSetorOrigem.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			clienteSetorOrigem.cdOrigemSetor = cdOrigemSetor;
			clienteSetorOrigem.cdCliRede = cdCliRede;
			clienteSetorOrigem.cdTipoCliRede = cdTipoCliRede;
			clienteSetorOrigem.cdSetor = cdSetor;
			//--
			select(clienteSetorOrigem);
		} else {
			setSelectedIndex(0);
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

	public void load(String cdTipoclirede, String cdContrato  , String cdSetorOrigem) throws SQLException {
		if (ValueUtil.isNotEmpty(cdSetorOrigem)) {
			removeAll();
			ClienteSetorOrigem clienteSetor = new ClienteSetorOrigem();
			clienteSetor.cdEmpresa = SessionLavenderePda.cdEmpresa;
			clienteSetor.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			clienteSetor.cdTipoCliRede = cdTipoclirede;
			clienteSetor.cdCliRede = cdContrato;
			clienteSetor.cdOrigemSetor = cdSetorOrigem;
			Vector list = new Vector();
			list = ClienteSetorOrigemService.getInstance().findAllByExampleSummary(clienteSetor);
			if (ValueUtil.isEmpty(list)) {
				defaultItemType = DefaultItemType_NONE_ITEN;
				setEnabled(false);
			} else {
				defaultItemType = DefaultItemType_NONE;
			}
				add(list);
		} else {
			showNoneItem();
		}
	}

	public void showNoneItem() {
		removeAll();
		setEnabled(false);
	}

	public void loadClienteSemContrato() {
		removeAll();
		add(ClienteSetorOrigem.CLIENTE_SETOR_ORIGEM_SEM_CONTRATO);
		setSelectedIndex(0);
		setEnabled(false);
	}
}