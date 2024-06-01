package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.service.FornecedorService;
import totalcross.util.Vector;

public class FornecedorComboBox extends BaseComboBox {

	public FornecedorComboBox() throws SQLException {
		super(LavenderePdaConfig.usaFiltroFornecedor ? Messages.FORNECEDOR_NOME_ENTIDADE : "");
		this.defaultItemType = BaseComboBox.DefaultItemType_ALL;
		//--
		if (LavenderePdaConfig.usaFiltroFornecedor) {
			load();
		}
	}

	public String getValue() {
		Fornecedor fornecedor = (Fornecedor)getSelectedItem();
		if (fornecedor != null) {
			return fornecedor.cdFornecedor;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.cdFornecedor = value;
		fornecedor.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		fornecedor.cdEmpresa = SessionLavenderePda.cdEmpresa;
		select(fornecedor);
	}

	public void load() throws java.sql.SQLException {
		removeAll();
		Fornecedor fornecedorFilter = new Fornecedor();
		fornecedorFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		Vector fornList = FornecedorService.getInstance().findAllForcedorAssociadoRep(fornecedorFilter);
		fornList.qsort();
		add(fornList);
	}

}
