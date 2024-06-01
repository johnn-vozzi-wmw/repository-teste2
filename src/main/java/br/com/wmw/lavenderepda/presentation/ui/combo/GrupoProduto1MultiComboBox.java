package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseMultiComboBox;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import totalcross.util.Vector;

public class GrupoProduto1MultiComboBox extends BaseMultiComboBox {

	public GrupoProduto1MultiComboBox() {
		super(FrameworkMessages.TITULO_PADRAO_COMBO_BOX);
	}
	
	
	public String getValue() {
		Object[] grupoProduto1List = getSelectedItems();
		String cdGrupoProduto1List = "";
		for(int i = 0; i < grupoProduto1List.length; i++) {
			cdGrupoProduto1List += ((GrupoProduto1)grupoProduto1List[i]).cdGrupoproduto1 +";";
		}
		return cdGrupoProduto1List;
	}
	
	public void load() throws java.sql.SQLException {
		Vector grupoProduto1List = GrupoProduto1Service.getInstance().findAll();
		grupoProduto1List.qsort();
		add(grupoProduto1List);
	}
}
