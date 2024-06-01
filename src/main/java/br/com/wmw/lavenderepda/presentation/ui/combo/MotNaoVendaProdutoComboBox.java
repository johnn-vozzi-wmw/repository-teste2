package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotNaoVendaProduto;
import br.com.wmw.lavenderepda.business.service.MotNaoVendaProdutoService;
import totalcross.util.Vector;

public class MotNaoVendaProdutoComboBox extends BaseComboBox {
	
	public MotNaoVendaProdutoComboBox() throws SQLException {
		super(Messages.VISITA_LABEL_CDMOTIVOREGISTROVISITA);
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		carregaMotNaoVendaProduto();
		setSelectedIndex(0);
	}
	
 	public String getValue() {
		MotNaoVendaProduto motNaoVendaProduto = (MotNaoVendaProduto) getSelectedItem();
		return motNaoVendaProduto != null ? motNaoVendaProduto.cdMotivo : ValueUtil.VALOR_NI;
	}
	
	private void carregaMotNaoVendaProduto() throws SQLException {
		Vector motNaoVendaProdutos = MotNaoVendaProdutoService.getInstance().findAll();
		motNaoVendaProdutos.qsort();
		add(motNaoVendaProdutos);
	}
	
	public String getFlExigeJustificativa() {
		MotNaoVendaProduto motNaoVendaProduto = (MotNaoVendaProduto) getSelectedItem();
		motNaoVendaProduto = motNaoVendaProduto != null ? motNaoVendaProduto : new MotNaoVendaProduto();
		return motNaoVendaProduto.flExigeJustificativa;
				
		
	}

}
