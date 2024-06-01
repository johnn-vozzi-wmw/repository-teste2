package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ListClienteAgendaMapaWindow extends LavendereWmwListWindow {
	
	private Vector domainList;
	private ButtonPopup btSelecionar;
	private LabelValue lblDsLimitacaoGoogleMaps;
	public boolean indexSelected;
	public int indexList;
	int iconSize;
	private Map<String, Image> indicadoresMap;

	public ListClienteAgendaMapaWindow(Vector domainList) {
		super(Messages.AGENDA_MAPA_TITULO);
		btSelecionar = new ButtonPopup("  " + FrameworkMessages.BOTAO_SELECIONAR + "  ");
		this.domainList = domainList;
		constructorListContainer();
		useLeftTopIcons = true;
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
			indicadoresMap = new HashMap<>();
			resizeIconsLegend = false;
			useLeftTopIcons = true;
			iconSize = listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size;
		}
		lblDsLimitacaoGoogleMaps = new LabelValue("");
		setDefaultRect();
		lblDsLimitacaoGoogleMaps.setMultipleLinesText(Messages.LIMITACAO_GOOGLE_MAPS);
	}
	
    @Override
    protected Image[] getIconsLegend(BaseDomain domain) {
    	Cliente cliente = (Cliente) domain;
    	Image[] iconsLegend = null;
		try {
			iconsLegend = ClienteService.getInstance().getIconsIndicadores(cliente, indicadoresMap, iconSize, Cliente.APRESENTA_LISTAGENDA);
		} catch (SQLException e) {
		}
    	return ValueUtil.isNotEmpty(iconsLegend) ? iconsLegend : super.getIconsLegend(domain);
    }
    
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return domainList;
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Cliente cliente = (Cliente) domain;
        Vector item = new Vector();
        item.addElement(StringUtil.getStringValue(cliente.cdCliente));
        item.addElement(" - " + StringUtil.getStringValue(cliente.nmRazaoSocial));
        return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}

	@Override
	protected BaseDomain getDomainFilter() {
			return new Cliente();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lblDsLimitacaoGoogleMaps, getLeft(), getTop());
        UiUtil.add(this, listContainer, LEFT, AFTER + UiUtil.getLabelPreferredHeight()*3, FILL, FILL);
        addButtonPopup(btSelecionar);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btSelecionar) {
				btSelelecionarClick();
			}
			break;
		}
	}
	
	private void btSelelecionarClick() throws SQLException {
		if (listContainer.getSelectedIndex() == -1) {
			UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_NAO_SELECIONADO);
			return;
		}
		indexList = listContainer.getSelectedIndex();
		indexSelected = true;
		unpop();
	}

	private void constructorListContainer() {
		listContainer = new GridListContainer(2, 2);
		listContainer.setBarTopSimple();
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
	}

}
