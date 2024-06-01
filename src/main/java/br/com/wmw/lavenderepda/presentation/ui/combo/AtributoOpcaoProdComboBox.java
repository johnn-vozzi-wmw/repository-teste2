package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AtributoOpcaoProd;
import br.com.wmw.lavenderepda.business.service.AtributoOpcaoProdService;
import totalcross.util.Vector;

public class AtributoOpcaoProdComboBox  extends BaseComboBox {

	private String cdAtributo = null;

	public AtributoOpcaoProdComboBox(String title) {
		super(title);
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		add(FrameworkMessages.OPCAO_ESCOLHA_OPCAO);
	}

	public String getValue() {
		AtributoOpcaoProd atributoProd = (AtributoOpcaoProd)getSelectedItem();
		if (atributoProd != null) {
			return atributoProd.cdAtributoOpcaoProduto;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		AtributoOpcaoProd atributoProd = new AtributoOpcaoProd();
		atributoProd.cdEmpresa = SessionLavenderePda.cdEmpresa;
		atributoProd.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		atributoProd.cdAtributoProduto = cdAtributo;
		atributoProd.cdAtributoOpcaoProduto = value;
		select(atributoProd);
	}

	public void load(String cdAtributo) throws SQLException {
		removeAll();
		this.cdAtributo = cdAtributo;
		Vector result = AtributoOpcaoProdService.getInstance().findAllByEmpresaAndRep(cdAtributo);
		result.qsort();
		add(result);
	}

}
