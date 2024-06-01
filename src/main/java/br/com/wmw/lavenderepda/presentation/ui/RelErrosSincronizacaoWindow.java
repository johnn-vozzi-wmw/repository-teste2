package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ExpandableContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Vector;

public class RelErrosSincronizacaoWindow extends WmwWindow {

	private Vector erroList;

	public RelErrosSincronizacaoWindow() {
		super(Messages.REL_ERRO_SYNC_TITULO, WmwWindow.MODE_ERROR);
		erroList = LogSync.getErrorList();
		makeUnmovable();
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		if (ValueUtil.isNotEmpty(erroList)) {
			int size = erroList.size();
			for (int i = 0; i < size; i++) {
				String erro = (String) erroList.items[i];
				createListItem(erro, erro);
			}
		}
	}

	private void createListItem(String title, String description) {
		ExpandableContainer exp = new ExpandableContainer(title, ColorUtil.errorColor);
		UiUtil.add(this, exp, LEFT, AFTER + 1, FILL);
		if (ValueUtil.isNotEmpty(description)) {
			exp.ctMaximizedContainers.setBackColor(ColorUtil.popupBackColor);
			LabelValue lbDescription = new LabelValue();
			lbDescription.autoMultipleLines = true;
			exp.addComponentInMaximizedContainer(lbDescription);
			lbDescription.setMultipleLinesText(description);
		}
	}
	
	@Override
	protected void postUnpop() {
		erroList.removeAllElements();
	}

}