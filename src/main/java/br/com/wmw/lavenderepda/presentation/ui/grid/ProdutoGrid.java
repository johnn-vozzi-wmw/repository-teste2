package br.com.wmw.lavenderepda.presentation.ui.grid;

import br.com.wmw.framework.presentation.ui.ext.BaseGrid;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;

public class ProdutoGrid extends BaseGrid {


	public ProdutoGrid(GridColDefinition[] gridColDefinition) {
		this(gridColDefinition, false);
	}

	public ProdutoGrid(GridColDefinition[] gridColDefinition, boolean checkEnabled) {
		super(UiUtil.getGridColCaptions(gridColDefinition),
				UiUtil.getGridColWidths(gridColDefinition),
				UiUtil.getGridColAligns(gridColDefinition),
				checkEnabled);
	}

}
