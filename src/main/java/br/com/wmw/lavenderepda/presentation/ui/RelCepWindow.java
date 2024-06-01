package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.service.BairroService;
import br.com.wmw.lavenderepda.business.service.CepService;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.LogradouroService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelCepWindow extends LavendereWmwListWindow {

	public EditText edFiltro;
	public BaseButton btFiltrar;
	private LabelValue lbDescricao;
	public Cep cepSelecionado;
	Vector cepList;

	public RelCepWindow(Vector cepList) throws SQLException {
		super(Messages.CEP_TITULO_LISTA);
		lbDescricao = new LabelValue(Messages.CLIENTEENDERECO_SELECIONE_ENDERECO);
		this.cepList = cepList;
		singleClickOn = true;
		edFiltro = new EditText("9999999999999", 25);
		edFiltro.autoSelect = true;
		btFiltrar = new BaseButton(FrameworkMessages.BOTAO_FILTRAR);
		loadInfoCep();
		constructorListContainer();
		setDefaultRect();
	}

	private void loadInfoCep() throws SQLException {
		int size = cepList.size();
		for (int i = 0; i < size; i++) {
			Cep cep = (Cep) cepList.items[i];
			cep.dsLogradouro = LogradouroService.getInstance().getDsLogradouro(cep.cdLogradouro);
			cep.dsCidade = CidadeService.getInstance().getNmCidade(cep.cdCidade);
			cep.dsBairro = BairroService.getInstance().getDsBairro(cep.cdBairro);
		}
	}

	private void constructorListContainer() {
		configListContainer("LOGRADOURO", true);
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
    	listContainer.setColsSort(new String[][] {{"Logradouro", "LOGRADOURO"}, {"Bairro", "BAIRRO"}});
    }

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		int size = cepList.size();
		Vector listFinal = new Vector();
		listFinal.addElementsNotNull(cepList.items);
		if (ValueUtil.isNotEmpty(edFiltro.getValue())) {
			listFinal.removeAllElements();
		}
		for (int i = 0; i < size; i++) {
			Cep cep = (Cep) cepList.items[i];
			if (ValueUtil.isNotEmpty(edFiltro.getValue()) && ValueUtil.isNotEmpty(cep.dsLogradouro)) {
				if (cep.dsLogradouro.toUpperCase().contains(edFiltro.getValue().toUpperCase())) {
					listFinal.addElement(cep);
				}
			}
		}
		Cep.sortAttr = domain.sortAtributte;
		SortUtil.qsortString(listFinal.items, 0, listFinal.size() - 1, true);
		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
			listFinal.reverse();
		}
		return listFinal;
	}

	//@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
        Cep cep = (Cep) domain;
        String[] item = {
            cep.dsCep,
            StringUtil.getStringValue(cep.dsCidade),
            StringUtil.getStringValue(cep.dsLogradouro),
            StringUtil.getStringValue(cep.dsBairro)
        };
        return item;
    }
    
	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return CepService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
		return new Cep();
	}

	protected void onFormStart() {
		lbDescricao.setText(MessageUtil.quebraLinhas(lbDescricao.getText(), width - 20));
		UiUtil.add(this, lbDescricao, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
		UiUtil.add(this, edFiltro, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, FILL - btFiltrar.getPreferredWidth() - (WIDTH_GAP * 2));
		UiUtil.add(this, btFiltrar, AFTER + WIDTH_GAP, SAME, PREFERRED, edFiltro.getHeight());
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	public void screenResized() {
		super.screenResized();
		lbDescricao.setText(MessageUtil.quebraLinhas(lbDescricao.getText(), width - 20));
		lbDescricao.reposition();
		edFiltro.reposition();
		btFiltrar.reposition();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFiltrar) {
					list();
				}
			}
		
		}
	}

	public void singleClickInList() throws SQLException {
		super.singleClickInList();
		cepSelecionado = (Cep) getSelectedDomain();
		fecharWindow();
	}
	
	@Override
	protected void fecharWindow() throws SQLException {
		if (cepSelecionado == null) {
			UiUtil.showInfoMessage(Messages.CEP_MSG_SELECIONAR_CEP);
		} else {
			super.fecharWindow();
		}
	}

}