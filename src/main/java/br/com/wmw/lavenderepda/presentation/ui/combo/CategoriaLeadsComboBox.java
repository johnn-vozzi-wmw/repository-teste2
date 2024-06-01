package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.service.CategoriaService;
import totalcross.util.Vector;

public class CategoriaLeadsComboBox extends BaseComboBox {
	
	public CategoriaLeadsComboBox() throws SQLException {
		super(Messages.CATEGORIA_NOME_ENTIDADE);
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		carregaCategorias();
	}
	
	public void setValue(String value) {
		if (value != null) {
			Categoria categoria = new Categoria();
			categoria.cdEmpresa = SessionLavenderePda.cdEmpresa;
			categoria.cdCategoria = value;
			select(categoria);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public String getValue() {
		Categoria categoria = (Categoria) getSelectedItem();
		if (categoria != null) {
			return categoria.cdCategoriaLeads;
		}
		return ValueUtil.VALOR_NI;
	}
	
	private void carregaCategorias() throws SQLException {
		Categoria categoriaFilter = new Categoria();
		categoriaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		categoriaFilter.buscaCategoriaLeads = true;
		Vector categoriaList = CategoriaService.getInstance().findAllByExample(categoriaFilter); 
		categoriaList.qsort();
		add(categoriaList);
	}

}
