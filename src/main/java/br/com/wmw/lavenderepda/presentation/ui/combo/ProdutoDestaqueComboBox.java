package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseMultiComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ProdutoDestaque;
import br.com.wmw.lavenderepda.business.service.ProdutoDestaqueService;
import totalcross.util.Vector;

public class ProdutoDestaqueComboBox extends BaseMultiComboBox {

	public ProdutoDestaqueComboBox() throws SQLException {
		super(LavenderePdaConfig.isUsaGrupoDestaqueProduto() ? Messages.PRODUTO_LABEL_PRODUTO_DESTAQUE : "");
		//--
		
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			load();
		}
	}
	
	public void setSelectItens(Vector produtoDestaqueFiltroPadraoList) {
		unselectAll();
		if (ValueUtil.isNotEmpty(produtoDestaqueFiltroPadraoList)) {
			int size = produtoDestaqueFiltroPadraoList.size();
			for (int i = 0; i < size; i++) {
				ProdutoDestaque produtoDestaque = (ProdutoDestaque)produtoDestaqueFiltroPadraoList.items[i];
				select(produtoDestaque);
			}
		}
	}

	public void setSelectItens(String cdGrupoDestaqueList) {
		unselectAll();
		String[] cdGrupoDestaqueToArray = cdGrupoDestaqueList != null ? cdGrupoDestaqueList.split(";") : null;  
		if (ValueUtil.isNotEmpty(cdGrupoDestaqueToArray)) {
			for (String cdGrupoDestaqueItem : cdGrupoDestaqueToArray) {
				ProdutoDestaque produtoDestaque = new ProdutoDestaque();
				produtoDestaque.cdEmpresa = SessionLavenderePda.cdEmpresa;
				produtoDestaque.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
				produtoDestaque.cdGrupoDestaque = cdGrupoDestaqueItem;
				select(produtoDestaque);
			}
		}
	}

	public String getValue() {
		Object[] produtoDestaqueList = getSelectedItems();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < produtoDestaqueList.length; i++) {
			str.append(((ProdutoDestaque) produtoDestaqueList[i]).cdGrupoDestaque).append(";");
		}
		if (str.length() > 0) {
			str.deleteCharAt(str.length() -1);
		}
		return str.toString();
	}

	public void load() throws java.sql.SQLException {
		ProdutoDestaque produtoDestaqueFilter = new ProdutoDestaque();
		produtoDestaqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoDestaqueFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		Vector produtoDestaqueList = ProdutoDestaqueService.getInstance().findAllByExample(produtoDestaqueFilter);
		produtoDestaqueList.qsort();
		add(produtoDestaqueList);
		setSelectedFiltrosPadrao(produtoDestaqueList);
	}

	private void setSelectedFiltrosPadrao(Vector produtoDestaqueList) {
		if (ValueUtil.isNotEmpty(produtoDestaqueList)) {
			int size = produtoDestaqueList.size();
			Vector produtoDestaqueFiltroPadraoList = new Vector();
			for (int i = 0; i < size; i++) {
				ProdutoDestaque produtoDestaque = (ProdutoDestaque)produtoDestaqueList.items[i];
				if (produtoDestaque.isFiltroSelecionadoPorPadrao()) {
					produtoDestaqueFiltroPadraoList.addElement(produtoDestaque);
				}
			}
			setSelectItens(produtoDestaqueFiltroPadraoList);
		}
	}
	
}
