package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadePeso;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.DescQuantidadePesoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDescQuantidadePesoWindow extends WmwListWindow {

	private String cdTabelaPreco;
	public TabelaPrecoComboBox cbTabelaPreco;
	public LabelName lbTabelaPrecoAbrev;

    public ListDescQuantidadePesoWindow() {
		super(Messages.DESC_QUANTIDADE_PESO_TITULO);
		cbTabelaPreco = new TabelaPrecoComboBox();
		lbTabelaPrecoAbrev = new LabelName(Messages.TABELAPRECO_NOME_ENTIDADE);
    }
    
    public ListDescQuantidadePesoWindow(Pedido pedido) throws SQLException {
    	super(Messages.DESC_QUANTIDADE_PESO_TITULO);
    	cbTabelaPreco = new TabelaPrecoComboBox(true);
    	this.cdTabelaPreco = pedido.cdTabelaPreco;
		lbTabelaPrecoAbrev = new LabelName(Messages.TABELAPRECO_NOME_ENTIDADE);
    	cbTabelaPreco.loadTabelaPrecoDescQuantidadePeso();
    	cbTabelaPreco.setSelectedIndex(0);
    	for (int i = 1; i < cbTabelaPreco.size(); i++) {
			TabelaPreco tb = (TabelaPreco) cbTabelaPreco.getItemAt(i);
			if (ValueUtil.valueEquals(cdTabelaPreco, tb.cdTabelaPreco)) {
				cbTabelaPreco.setValue(cdTabelaPreco);
			}
		}
		if (!ValueUtil.valueEquals(cdTabelaPreco, cbTabelaPreco.getValue())) {
			this.cdTabelaPreco = "0";
		}
    }
    
    public ListDescQuantidadePesoWindow(ItemPedido itemPedido) throws SQLException {
    	super(Messages.DESC_QUANTIDADE_PESO_TITULO);
    	cbTabelaPreco = new TabelaPrecoComboBox(true);
    	this.cdTabelaPreco = itemPedido.cdTabelaPreco;
    	lbTabelaPrecoAbrev = new LabelName(Messages.TABELAPRECO_NOME_ENTIDADE);
    	cbTabelaPreco.loadTabelaPrecoDescQuantidadePeso();
    	cbTabelaPreco.setSelectedIndex(0);
    	for (int i = 1; i < cbTabelaPreco.size(); i++) {
			TabelaPreco tb = (TabelaPreco) cbTabelaPreco.getItemAt(i);
			if (ValueUtil.valueEquals(cdTabelaPreco, tb.cdTabelaPreco)) {
				cbTabelaPreco.setValue(cdTabelaPreco);
			}
		}
    	if (!ValueUtil.valueEquals(cdTabelaPreco, cbTabelaPreco.getValue())) {
			this.cdTabelaPreco = "0";
		}
    }

    //@Override
    protected CrudService getCrudService() throws java.sql.SQLException {
        return DescQuantidadePesoService.getInstance();
    }

    protected BaseDomain getDomainFilter() {
    	DescQuantidadePeso descQuantidadePesoFilter = new DescQuantidadePeso();
		descQuantidadePesoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		descQuantidadePesoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		descQuantidadePesoFilter.cdTabelaPreco = this.cdTabelaPreco != null ? this.cdTabelaPreco : "0";
		return descQuantidadePesoFilter;
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
    	Vector listDescQuantidadePeso = DescQuantidadePesoService.getInstance().findAllByExample(getDomainFilter());
    	for (int i = 0; i < listDescQuantidadePeso.size(); i++) {
			DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) listDescQuantidadePeso.items[i];
			descQuantidadePeso.nmTabelaPreco = TabelaPrecoService.getInstance().getDsTabelaPreco(descQuantidadePeso.cdTabelaPreco);
    	}
    	return listDescQuantidadePeso;
    }

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
    	DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) domain;
		String[] item = {
			descQuantidadePeso.nmTabelaPreco,
			StringUtil.getStringValueToInterface(descQuantidadePeso.vlPeso),			
			StringUtil.getStringValueToInterface(descQuantidadePeso.vlPctDesconto) + "%"			
		};
		return item;
    }

    //@Override
    protected String getSelectedRowKey() {
        String[] item = grid.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() {
        int oneChar = fm.charWidth('A');
    	GridColDefinition[] gridColDefiniton = {
    		new GridColDefinition(Messages.FAIXA_DESC_NMTABELAPRECO, oneChar * 15, LEFT),
			new GridColDefinition(Messages.FAIXA_DESC_PESO, oneChar * 10, LEFT),
			new GridColDefinition(Messages.FAIXA_DESC_PCTDESC, oneChar * 5, LEFT),
			};
    	grid = UiUtil.createGridEdit(gridColDefiniton);
    	grid.setGridControllable();
        //-- 
    	
    	if(LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
    		UiUtil.add(this, grid, LEFT, getTop()+ HEIGHT_GAP+70, FILL, FILL);
    		UiUtil.add(this, lbTabelaPrecoAbrev, cbTabelaPreco, getLeft(), getTop() );
		}
		else {
			UiUtil.add(this, grid, LEFT, getTop(), FILL, FILL);
		}
 //   	
        grid.setGridControllable();
    }

	// @Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == cbTabelaPreco) {
				cbTabelaPrecoClick();
			}
		}
		}
	} 

	protected void cbTabelaPrecoClick() throws SQLException {
		this.cdTabelaPreco = cbTabelaPreco.getValue();
		list();
	}
    
}
