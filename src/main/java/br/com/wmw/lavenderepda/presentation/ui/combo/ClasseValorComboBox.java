package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ClasseValor;
import br.com.wmw.lavenderepda.business.service.ClasseValorService;
import totalcross.util.Vector;

public class ClasseValorComboBox extends BaseComboBox {

	public ClasseValorComboBox() {
		super();
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
	}

	public String getValue() {
		ClasseValor classeValor = (ClasseValor) getSelectedItem();
		return classeValor != null ? classeValor.cdClasseValor : null;
	}

	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			ClasseValor classeValor = new ClasseValor();
			classeValor.cdEmpresa = SessionLavenderePda.cdEmpresa;
			classeValor.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			classeValor.cdClasseValor = value;
			//--
			select(classeValor);
		} else {
			setSelectedIndex(0);
		}
	}

	public void carregaClasseValor() throws SQLException {
		removeAll();
		ClasseValor classeValorFilter = new ClasseValor();
		classeValorFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		classeValorFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector classeValorList = ClasseValorService.getInstance().findAllByExample(classeValorFilter);
		if (ValueUtil.isNotEmpty(classeValorList)) {
			classeValorList.qsort();
			add(classeValorList);
		}
	}

}
