package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class RelVariaveisCalculoWindow extends WmwWindow {
	
	private BaseGridEdit grid;
	HashMap<String, Object> hash = new HashMap<>();

	public RelVariaveisCalculoWindow(HashMap<String, Object> hash) {
		super(Messages.MENU_OPCAO_DETALHES_VARIAVEIS_DE_CALCULO);
		this.hash = hash;
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition("CHAVE", -50, LEFT),
			new GridColDefinition("VALOR", -50, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		grid.setGridControllable();
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		try {
			super.initUI();
			UiUtil.add(scBase, grid, LEFT, TOP, FILL, FILL);
			loadGrid();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}
	
	private void loadGrid() throws SQLException {
		if (ValueUtil.isEmpty(hash)) return;
    	for (String key : hash.keySet()) {
    		String[] item = getItem(key, hash.get(key));
    		if (item == null) continue;
    		grid.add(item);
		}
    	grid.qsort(0);
	}
	
	private String[] getItem(String key, Object value) {
		if (ValueUtil.isEmpty(key) || value == null) return null;
		try {
	        String[] item = {
	                StringUtil.getStringValue(key),
	                StringUtil.getStringValue(value.toString())};
	        return item;
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
		return null;
    }

}
