package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseMultiComboBox;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import totalcross.util.Vector;

public class MarcadorMultiComboBox extends BaseMultiComboBox {
	
	public MarcadorMultiComboBox() {
		super(Messages.MARCADOR_MARCADORES);
	}
	
	public void load() throws SQLException {
		load(true);
	}
	
	public void load(boolean keepAlreadySelectedFilters) throws SQLException {
		removeAll();
		Vector list = MarcadorService.getInstance().buscaMarcadoresVigentes(Marcador.ENTIDADE_MARCADOR_PRODUTO);
		add(list);
		if (keepAlreadySelectedFilters) {
			setSelectedFiltrosPadrao(list);
		}
	}
	
	public Vector getSelected() {
		Object[] selectedItems = getSelectedItems();
		ordenaListaMarcadorDescricaoESequencia(selectedItems);
		Vector list = new Vector();
		for (Object o : selectedItems) {
			list.addElement(((Marcador) o).cdMarcador);
		}
		return list;
	}

	private void ordenaListaMarcadorDescricaoESequencia(Object[] selectedItems) {
		if (ValueUtil.isEmpty(selectedItems)) return;
		SortUtil.qsortString(selectedItems, 0, selectedItems.length - 1, true);
	}

	public Vector getSelectedMarcador() {
		Object[] selectedItems = getSelectedItems();
		ordenaListaMarcadorDescricaoESequencia(selectedItems);
		return new Vector(selectedItems);
	}

	public void setSelectItens(Vector marcadorPadraoList) {
		unselectAll();
		if (ValueUtil.isEmpty(marcadorPadraoList)) return;
		int size = marcadorPadraoList.size();
		for (int i = 0; i < size; i++) {
			Marcador marcador = (Marcador)marcadorPadraoList.items[i];
			select(marcador);
		}
	}

	private void setSelectedFiltrosPadrao(Vector marcadorList) {
		if (ValueUtil.isEmpty(marcadorList)) return;
		int size = marcadorList.size();
		Vector marcadorProdutoPadraoList = new Vector();
		for (int i = 0; i < size; i++) {
			Marcador marcador = (Marcador)marcadorList.items[i];
			if (marcador.isFiltroSelecionadoPorPadrao()) {
				marcadorProdutoPadraoList.addElement(marcador);
			}
		}
		setSelectItens(marcadorProdutoPadraoList);
	}
}
