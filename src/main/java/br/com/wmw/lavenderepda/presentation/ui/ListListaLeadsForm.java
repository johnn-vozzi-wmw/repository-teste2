package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ListaLeads;
import br.com.wmw.lavenderepda.business.domain.Place;
import br.com.wmw.lavenderepda.business.service.ListaLeadsService;
import br.com.wmw.lavenderepda.business.service.PlaceService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoCadastroListaLeadsBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListListaLeadsForm extends LavendereCrudListForm {
	
	private EditText diaSemana;
	private EditDate edDtInicial;
	private EditDate edDtFinal;
	private ButtonAction btBuscarNovaLista;
	private BaseButton btFiltro;
	private EditText diaSemanaInicial;
	private EditText diaSemanaFinal;
	private TipoCadastroListaLeadsBox cbTipoCadastroListaLeadsBox;

    public ListListaLeadsForm() throws SQLException {
        super(Messages.LISTALEADS_NOME_ENTIDADE_LISTA);
        diaSemana = new EditText("", 15);
        diaSemana.setEnabled(false);
		singleClickOn = true;
		edDtInicial = new EditDate();
		edDtInicial.setValue(DateUtil.getCurrentDate());
		edDtFinal = new EditDate();
		edDtFinal.setValue(DateUtil.getCurrentDate());
		diaSemanaInicial = new EditText("", 15);
	    diaSemanaInicial.setEnabled(false);
	    diaSemanaFinal = new EditText("", 15);
	    diaSemanaFinal.setEnabled(false);
		cbTipoCadastroListaLeadsBox = new TipoCadastroListaLeadsBox();
		cbTipoCadastroListaLeadsBox.setSelectedIndex(0);
		btFiltro = new BaseButton("", UiUtil.getColorfulImage("images/search.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		btBuscarNovaLista = new ButtonAction(Messages.LISTALEADS_LABEL_NOVALISTA, "images/add.png");
		listContainer = new GridListContainer(6, 2);
		listContainer.setColsSort(new String[][] { { Messages.LISTALEADS_SORT_CDLISTA, ListaLeads.NMCOLUNA_CDLISTA }, { Messages.LISTALEADS_SORT_DTCRIACAO, ListaLeads.NMCOLUNA_DTCRIACAO } });
		configListContainer(ListaLeads.NMCOLUNA_CDLISTA);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(5, RIGHT);
		setTextDiaSemanaPeriodo();
    }

    @Override
    public void onFormExibition() throws SQLException {
    	super.onFormExibition();
    	listarMantendoScroll();
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return ListaLeadsService.getInstance();
    }

    @Override
    protected String[] getItem(Object domain) throws SQLException {
        ListaLeads listaLeads = (ListaLeads) domain;
        if (listaLeads.placesList == null) {
        	Place placeFilter = new Place();
        	placeFilter.cdEmpresa = listaLeads.cdEmpresa;
        	placeFilter.cdRepresentante = listaLeads.cdRepresentante;
        	placeFilter.cdLista = listaLeads.cdLista;
        	listaLeads.placesList = PlaceService.getInstance().findAllByExample(placeFilter);
        }
        Vector item = new Vector(0);
        item.addElement(StringUtil.getStringValue(listaLeads.cdLista));
        item.addElement(StringUtil.getStringValue(" - " + listaLeads.nmLista));
        item.addElement(StringUtil.getStringValue(Messages.LISTALEADS_LABEL_TIPOCADASTRO + ":" + (listaLeads.flTpCadastro.equals(ListaLeads.LEADS_TIPO_WEB) ? Messages.OPCAO_WEB : Messages.OPCAO_PDA)));
		item.addElement(FrameworkMessages.CAMPO_VAZIO);
        item.addElement(StringUtil.getStringValue(Messages.LISTALEADS_LABEL_QTDPLACES + ": " + (listaLeads.placesList != null ? listaLeads.placesList.size() : 0)));
        item.addElement(StringUtil.getStringValue(Messages.LISTALEADS_SORT_DTCRIACAO + ": " + listaLeads.dtCriacao));
    	return (String[]) item.toObjectArray();
    }

    @Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
    }

	@Override
    protected void onFormStart() throws SQLException {
		UiUtil.add(barBottomContainer, btBuscarNovaLista, 5);
       	UiUtil.add(this, new LabelName(Messages.AGENDAVISITA_LABEL_PERIODO), getLeft(), getNextY());
    	UiUtil.add(this, edDtInicial, getLeft(), getNextY());
       	UiUtil.add(this, edDtFinal, AFTER + WIDTH_GAP, SAME);
    	UiUtil.add(this, btFiltro, AFTER + WIDTH_GAP, SAME);
		UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_TIPOCADASTRO), cbTipoCadastroListaLeadsBox, getLeft(), getNextY());
        UiUtil.add(this, edFiltro, getLeft(), getNextY());
        UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
    }
    
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btBuscarNovaLista) {
				btBuscarNovaListaLeadsClick();
			} else if (event.target == btFiltro) {
				filtrarClick();
			} else if (event.target == cbTipoCadastroListaLeadsBox) {
				list();
			}
			break;
		}
		case ValueChangeEvent.VALUE_CHANGE: {
			if (event.target == edDtInicial || event.target == edDtFinal) {
				if (event.target == edDtInicial) {
					edDtInicialClick();
				} else {
					edDtFinalClick();
				}
				setTextDiaSemanaPeriodo();
			}
			break;
		}
		}
	}

	@Override
	public void singleClickInList() throws SQLException {
		show(new ListPlaceForm((ListaLeads) super.getSelectedDomain()));		
	}

	@Override
    protected void filtrarClick() throws SQLException {
    	list();
    }
	
	private void listarMantendoScroll() throws SQLException {
		if (listContainer != null) {
			int lastSel = listContainer.getSelectedIndex();
			list();
			if (lastSel > 0 && listContainer.getItemCount() > lastSel) {
				listContainer.setSelectedIndex(lastSel);
				listContainer.setScrollPos(listContainer.getSelectedItem().getY());
			}
		}
	}
	
	@Override
    protected BaseDomain getDomainFilter() throws SQLException {
    	ListaLeads listaLeadsFilter = new ListaLeads();
    	listaLeadsFilter.dtInicioFiltro = edDtInicial.getValue();
    	listaLeadsFilter.dtFimFiltro = edDtFinal.getValue();
    	listaLeadsFilter.flTpCadastro = cbTipoCadastroListaLeadsBox.getValue();
    	listaLeadsFilter.dsFiltro = edFiltro.getValue();
    	listaLeadsFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	listaLeadsFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(getClass());
    	return listaLeadsFilter;
    }

    public void setTextDiaSemana(int dia) {
		diaSemana.setText(DateUtil.getDiaSemana(dia));
	}
	
	private void edDtInicialClick() throws SQLException {
		if (ValueUtil.isEmpty(edDtInicial.getValue()) || ValueUtil.isEmpty(edDtFinal.getValue())) {
			return;
		}
		
		validaDtInicial();
	}
	
	private void edDtFinalClick() {
		if (ValueUtil.isEmpty(edDtFinal.getValue()) || ValueUtil.isEmpty(edDtInicial.getValue())) {
			return;
		}
		validaDtFinal();
	}

	private void validaDtInicial() {
		if (DateUtil.isAfter(edDtInicial.getValue(), edDtFinal.getValue())) {
			edDtInicial.setValue(DateUtil.getCurrentDate());
			throw new ValidationException(Messages.LEADS_ERRO_INICIAL_POSTERIOR_FINAL);
		}
		if (LavenderePdaConfig.getNuDiasMaxFiltroDataListaLeadsLigado()) {
			if (DateUtil.getDaysBetween(edDtFinal.getValue(), edDtInicial.getValue()) > LavenderePdaConfig.getNuDiasMaxFiltroDataListaLeads()) {
				edDtFinal.setValue(edDtInicial.getValue());
				throw new ValidationException(MessageUtil.getMessage(Messages.LISTALEADS_ERRO_DIFERENCA_DATA_MAIOR_LIMITE, LavenderePdaConfig.getNuDiasMaxFiltroDataListaLeads()));
			}
		}
	}
	
	private void validaDtFinal() {
		if (DateUtil.isBefore(edDtFinal.getValue(), edDtInicial.getValue())) {
			edDtFinal.setValue(edDtInicial.getValue());
			throw new ValidationException(Messages.LEADS_ERRO_FINAL_ANTERIOR_INICIAL);
		}
		if (LavenderePdaConfig.getNuDiasMaxFiltroDataListaLeadsLigado()) {
			if (DateUtil.getDaysBetween(edDtFinal.getValue(), edDtInicial.getValue()) > LavenderePdaConfig.getNuDiasMaxFiltroDataListaLeads()) {
				edDtFinal.setValue(edDtInicial.getValue());
				throw new ValidationException(MessageUtil.getMessage(Messages.LISTALEADS_ERRO_DIFERENCA_DATA_MAIOR_LIMITE, LavenderePdaConfig.getNuDiasMaxFiltroDataListaLeads()));
			}
		}
	}
	
    private void setTextDiaSemanaPeriodo() {
    	if (ValueUtil.isEmpty(edDtInicial.getValue()) || ValueUtil.isEmpty(edDtFinal.getValue())) {
    		return;
    	}
    	diaSemanaInicial.setText(DateUtil.getDiaSemana(edDtInicial.getValue().getDayOfWeek()));
    	diaSemanaFinal.setText(DateUtil.getDiaSemana(edDtFinal.getValue().getDayOfWeek()));
    }
	
	private void btBuscarNovaListaLeadsClick() throws SQLException {
		show(new ListPlaceForm());
	}
	
}
