package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.service.ItemKitService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListKitTipo3VigenciaExtrapoladaWindow extends WmwListWindow {

	private final Vector kitsExtrapolados;
	public boolean excluiuKits;
	private ButtonPopup btExcluir;

	public ListKitTipo3VigenciaExtrapoladaWindow(Vector kitsExtrapolados) {
		super(Messages.KIT_TIPO_3_VIGENCIA_EXTRAPOLADA_LIST_TITLE);
		this.kitsExtrapolados = new Vector();
		this.kitsExtrapolados.addElementsNotNull(kitsExtrapolados.items);
		constructorListContainer();
		btExcluir = new ButtonPopup(FrameworkMessages.BOTAO_EXCLUIR);
		setDefaultRect();
	}


	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return kitsExtrapolados;
	}

	private void constructorListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(3, RIGHT);
		listContainer.resizeable = false;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Kit kit = (Kit) domain;
		return new String[]{kit.toString(),
				ValueUtil.VALOR_NI,
				MessageUtil.getMessage(Messages.KIT_TIPO_3_VIGENCIA_VENCIDA_EM, StringUtil.getStringValue(kit.dtVigenciaFinal)),
				MessageUtil.getMessage(Messages.ITEMKIT_QT_ITEM_KIT, StringUtil.getStringValueToInterface(ItemKitService.getInstance().findQtItemKitByKit(kit)))};
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new Kit();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelValue(Messages.KIT_TIPO_3_VIGENCIA_EXTRAPOLADA_MSG), getLeft(), getNextY());
		UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
		addButtonPopup(btExcluir);
		addBtFechar();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btExcluir) {
					btExcluirClick();
				}
				break;
		}

	}

	private void btExcluirClick() throws SQLException {
		excluiuKits = true;
		fecharWindow();
	}
}
