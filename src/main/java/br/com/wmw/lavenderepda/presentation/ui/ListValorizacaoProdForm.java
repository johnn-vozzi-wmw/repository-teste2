package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ValorizacaoProd;
import br.com.wmw.lavenderepda.business.service.ValorizacaoProdService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ValorizacaoProdStatusComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListValorizacaoProdForm extends BaseCrudListForm {
	
	public EditDate dtFiltroInicio;
	public EditDate dtFiltroFim;
	public ValorizacaoProdStatusComboBox cbStatus;

	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lbVlTotalItens;
	
    public ListValorizacaoProdForm() throws SQLException {
        super(Messages.VALORIZACAOPROD_TITULO_LISTA);
        setBaseCrudCadForm(new CadValorizacaoProdForm());
        singleClickOn = true;
        dtFiltroInicio = new EditDate();
        dtFiltroInicio.setValue(DateUtil.getCurrentDate());
        dtFiltroFim = new EditDate();
        dtFiltroFim.setValue(DateUtil.getCurrentDate());
        cbStatus = new ValorizacaoProdStatusComboBox();
        cbStatus.setSelectedIndex(1);
		sessaoTotalizadores = new SessionTotalizerContainer();
		lbVlTotalItens = new LabelTotalizador("999999999,99");
		lbVlTotalItens.setValue(Messages.VALORIZACAOPROD_TOTALIZADOR_VALOR_PRODUTO);
        constructorListContainer();
    }
    
    private void constructorListContainer() {
        listContainer = new GridListContainer(8, 3);
        listContainer.setColPosition(3, LEFT);
        listContainer.setColPosition(4, CENTER);
        listContainer.setColPosition(5, RIGHT);
        listContainer.setColPosition(6, LEFT);
        listContainer.setColPosition(7, RIGHT);
		listContainer.setColTotalizerRight(7, Messages.VALORIZACAOPROD_TOTALIZADOR_VALOR_PRODUTO);
	}
    
    @Override
    protected CrudService getCrudService() {
        return ValorizacaoProdService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		ValorizacaoProd domainFilter = new ValorizacaoProd();
		return domainFilter;
	}
    
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	ValorizacaoProd filtro = new ValorizacaoProd();
    	filtro.flTipoAlteracao = cbStatus.getValue();
    	filtro.dtValorizacaoFiltroInic = dtFiltroInicio.getValue();
    	filtro.dtValorizacaoFiltroFim = dtFiltroFim.getValue();
    	return ValorizacaoProdService.getInstance().findValorizacoes(edFiltro.getText(), filtro);
    }
    
    @Override
    protected String[] getItem(Object domain) {
        ValorizacaoProd valorizacaoProd = (ValorizacaoProd) domain;
        String[] item = {
        		StringUtil.getStringValue(valorizacaoProd.cdProduto),
        		StringUtil.getStringValue(" - "),
        		StringUtil.getStringValue(valorizacaoProd.dsProduto),
        		StringUtil.getStringValue(Messages.VALORIZACAOPROD_LISTA_QTITEM + StringUtil.getStringValueToInterface(valorizacaoProd.qtItem)),
        		StringUtil.getStringValue(" "),
        		StringUtil.getStringValue(Messages.VALORIZACAOPROD_LABEL_VLUNITARIO_ABREV + StringUtil.getStringValueToInterface(valorizacaoProd.vlUnitario, 2)),
        		StringUtil.getStringValue(Messages.VALORIZACAOPROD_LISTA_DTVALORIZACAO + DateUtil.formatDateDDMMYYYY(valorizacaoProd.dtValorizacao) + " - " + valorizacaoProd.hrValorizacao),
        		StringUtil.getStringValue(Messages.VALORIZACAOPROD_LABEL_RS + StringUtil.getStringValueToInterface(valorizacaoProd.vlTotalItem, 2))
        };
        return item;
    }

    @Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }
    
    @Override
    protected void onFormStart() {
		UiUtil.add(barBottomContainer, btNovo, RIGHT - WIDTH_GAP * 2, CENTER);
        UiUtil.add(this, new LabelName(Messages.VALORIZACAOPROD_LABEL_DTVALORIZACAO), dtFiltroInicio, getLeft(), getNextY());
        UiUtil.add(this, dtFiltroFim, AFTER + 10, SAME);
        UiUtil.add(this, new LabelName(Messages.VALORIZACAOPROD_LABEL_FLENVIADO), cbStatus, getLeft(), getNextY());
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
        UiUtil.add(this, listContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
    }
    
    @Override
    protected void filtrarClick() throws SQLException {
		list();
    }
    
    @Override
    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
		listContainer.updateTotalizerRight();
    }
    
    @Override
    public void updateCurrentRecord(BaseDomain domain) throws SQLException {
    	super.updateCurrentRecord(domain);
		listContainer.updateTotalizerRight();
    }
    
    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target instanceof CalendarWmw || event.target == cbStatus) {
				filtrarClick();
			}
			break;
		}
		case EditIconEvent.PRESSED: {
			if (event.target == edFiltro) {
				filtrarClick();
			}
			break;
		}
    	}
    }
    
}
