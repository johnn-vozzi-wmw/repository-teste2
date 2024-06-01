package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CampanhaPublicitaria;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.CampanhaPublicitariaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

import java.sql.SQLException;

public class CampanhaPublicitariaWindow extends LavendereWmwListWindow {

	private static final String DEFAULT_ORDER_COLUMNS = "CDCAMPANHAPUBLICITARIA";

	private Pedido pedido;
	private StringBuilder cdCampanhasSelecionadas;
	private StringBuilder dsCampanhasSelecionadas;
	public ButtonPopup btSalvar;
	private LabelValue lbDescricao;
	private Vector campanhasPublicitarias;

	public CampanhaPublicitariaWindow(Pedido pedido) {
		super(Messages.CAMPANHA_PUBLICITARIA);
		this.pedido = pedido;
		campanhasPublicitarias = new Vector();
		lbDescricao = new LabelValue(Messages.CAMPANHA_PUBLICITARIA_SELECIONE_CAMPANHA);
		btSalvar = new ButtonPopup(FrameworkMessages.BOTAO_SALVAR);
		constructorListContainer();
		setDefaultRect();
	}

	private void constructorListContainer() {
		configListContainer(DEFAULT_ORDER_COLUMNS);
		listContainer = new GridListContainer();
		listContainer.setUseSortMenu(true);
		listContainer.setColsSort(new java.lang.String[][]{
				{Messages.CODIGO, DEFAULT_ORDER_COLUMNS}
		});
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
		listContainer.setCheckable(true);
	}

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		CampanhaPublicitaria campanhaPublicitaria = new CampanhaPublicitaria();
		campanhaPublicitaria.cdEmpresa = SessionLavenderePda.cdEmpresa;
		campanhaPublicitaria.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		campanhaPublicitaria.cdCliente = pedido.getCliente().cdCliente;
		campanhasPublicitarias = CampanhaPublicitariaService.getInstance().findAllByExample(campanhaPublicitaria);
		campanhasPublicitarias.insertElementAt(CampanhaPublicitariaService.getInstance().getCampanhaPublicitariaPadrao(pedido.getCliente().cdCliente), 0);
		return campanhasPublicitarias;
	}

	@Override
	public void list() throws SQLException {
		super.list();
		afterList();
	}

	private void afterList() {
		if (ValueUtil.isEmpty(pedido.cdCampanhaPublicitaria) || ValueUtil.isEmpty(campanhasPublicitarias)) return;
		String[] selectedItems = pedido.cdCampanhaPublicitaria.split(";");
		for (String selectedItem : selectedItems) {
			int size = campanhasPublicitarias.size();
			for (int i = 0; i < size; i++) {
				BaseListContainer.Item item = (BaseListContainer.Item) listContainer.getContainer(i);
				if (ValueUtil.valueEquals(selectedItem, item.id.split(";")[2])) {
					listContainer.checkedItens.addElement(i);
					item.setImage(false, false);
				}
			}
		}
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
		CampanhaPublicitaria campanhaPublicitaria = (CampanhaPublicitaria) domain;
		return new String[] {StringUtil.getStringValue(campanhaPublicitaria.dsCampanhaPublicitaria)};
	}

	@Override
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return CampanhaPublicitariaService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new Pedido();
	}

	@Override
	protected void onFormStart() throws SQLException {
		lbDescricao.setText(MessageUtil.quebraLinhas(lbDescricao.getText(), width - 20));
		UiUtil.add(this, lbDescricao, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	public void screenResized() {
		super.screenResized();
		lbDescricao.setText(MessageUtil.quebraLinhas(lbDescricao.getText(), width - 20));
		lbDescricao.reposition();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					btSalvarCampanhasRelacionadas();
				} else if (event.target == btFechar) {
					super.btFecharClick();
				}
				break;
			}
		}
	}

	public String getCdCampanhaPublicitariaSelecionada() {
		return cdCampanhasSelecionadas.toString();
	}

	public String getDsCampanhaPublicitariaSelecionada() {
		return dsCampanhasSelecionadas.toString();
	}

	private void btSalvarCampanhasRelacionadas() throws SQLException {
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		CampanhaPublicitaria campPublicitaria;
		cdCampanhasSelecionadas = new StringBuilder();
		dsCampanhasSelecionadas = new StringBuilder();
		for (int checkedIten : checkedItens) {
			campPublicitaria = (CampanhaPublicitaria) CampanhaPublicitariaService.getInstance().findByRowKey(listContainer.getId(checkedIten));
			if (listContainer.getId(checkedIten).contains(Messages.CD_CAMPANHA_PUBLICITARIA_PADRAO)) {
				dsCampanhasSelecionadas.append(Messages.CD_CAMPANHA_PUBLICITARIA_PADRAO).append(";");
				cdCampanhasSelecionadas.append(Messages.CD_CAMPANHA_PUBLICITARIA_PADRAO).append(";");
			}
			if (campPublicitaria != null) {
				cdCampanhasSelecionadas.append(campPublicitaria.cdCampanhaPublicitaria).append(";");
				dsCampanhasSelecionadas.append(campPublicitaria.dsCampanhaPublicitaria).append(";");
			}
		}
		unpop();
	}

	@Override
	protected void addButtons() {
		addButtonPopup(btSalvar);
		addBtFechar();
	}
}
