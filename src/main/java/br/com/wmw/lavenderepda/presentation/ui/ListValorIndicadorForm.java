package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.business.service.IndicadorService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.ValorIndicadorService;
import br.com.wmw.lavenderepda.business.service.ValorIndicadorWmwService;
import br.com.wmw.lavenderepda.presentation.ui.combo.IndicadorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PeriodoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.sys.Settings;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListValorIndicadorForm extends BaseCrudListForm {
	private IndicadorComboBox cbIndicador;
	private PeriodoComboBox cbPeriodo;
	private RepresentanteSupervComboBox cbRepresentante;
	private boolean relPorRepresentante;

    public ListValorIndicadorForm(boolean relPorRepresentante) throws SQLException {
        super(relPorRepresentante ? Messages.MENU_OPCAO_PRODUTREP : Messages.MENU_OPCAO_PRODUTINDICADOR);
        setBaseCrudCadForm(new CadValorIndicadorForm());
        cbIndicador = new IndicadorComboBox();
        cbPeriodo = new PeriodoComboBox(PeriodoComboBox.PERIODOVALORINDICADOR);
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        this.relPorRepresentante = relPorRepresentante;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ValorIndicadorService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new ValorIndicador();
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
    	Vector list = new Vector();
    	Vector listIndicadorWmw = new Vector();
    	String rep = "";
    	String indicador = null;
        if (relPorRepresentante) {
        	rep = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentante.getValue() : SessionLavenderePda.getRepresentante().cdRepresentante;
        	list = ValorIndicadorService.getInstance().getValorIndicadorListByRep(cbPeriodo.getValue(), rep);
        	listIndicadorWmw = ValorIndicadorWmwService.getInstance().findAllValorIndicadorByRep(cbPeriodo.getValue(), rep);
        } else {
        	indicador = cbIndicador.getValue();
        	list = ValorIndicadorService.getInstance().getValorIndicadorList(cbPeriodo.getValue(), indicador);
        	listIndicadorWmw = ValorIndicadorWmwService.getInstance().findAllValorIndicador(cbPeriodo.getValue(), indicador);
        }
        ValorIndicadorService.getInstance().addTicketMedioList(list, rep, indicador, cbPeriodo.getValue(), SessionLavenderePda.isUsuarioSupervisor());
        return VectorUtil.concatVectors(list, listIndicadorWmw);
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        ValorIndicador valorIndicador = (ValorIndicador) domain;
        String[] item = {
                StringUtil.getStringValue(valorIndicador.rowKey),
                StringUtil.getStringValue(relPorRepresentante ? IndicadorService.getInstance().getDescription(valorIndicador.cdIndicador) : RepresentanteService.getInstance().getDescription(valorIndicador.cdRepresentante)),
                ValorIndicadorService.getInstance().applyMaskOnDsVlIndicador(valorIndicador.dsVlIndicador, valorIndicador.dsMascaraFormato)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }
    
    //@Override
    protected void onFormStart() throws SQLException {
        if (relPorRepresentante) {
        	if (SessionLavenderePda.isUsuarioSupervisor()) {
        		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
        	} else {
            	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), getNextY());
        	}
        } else {
        	UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_LABEL_INDICADOR), cbIndicador, getLeft(), getTop() + HEIGHT_GAP);
        }
    	UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_PERIODO), cbPeriodo, getLeft(), AFTER + HEIGHT_GAP);
        //--
        int ww = fm.stringWidth("ww");
        if (relPorRepresentante) {
	        GridColDefinition[] gridColDefiniton = {
                new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
                new GridColDefinition(Messages.VALORINDICADOR_LABEL_INDICADOR, getLarguraColuna(), LEFT),
                new GridColDefinition(Messages.VALORINDICADOR_LABEL_DSVLINDICADOR, ww, RIGHT)};
	        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        } else {
        	GridColDefinition[] gridColDefiniton = {
                new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
                new GridColDefinition(Messages.REPRESENTANTE_NOME_ENTIDADE, getLarguraColuna(), LEFT),
                new GridColDefinition(Messages.VALORINDICADOR_LABEL_DSVLINDICADOR, ww, RIGHT)};
	        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        }
        gridEdit.setGridControllable(); 
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
    }

    public void loadDefaultFilters() throws SQLException {
        if (relPorRepresentante) {
	    	if (SessionLavenderePda.isUsuarioSupervisor()) {
	    		cbRepresentante.setSelectedIndex(0);
	        	if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
	            	cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
	            }
	        }
    	} else {
            cbIndicador.setSelectedIndex(0);
    	}
        cbPeriodo.setSelectedIndex(0);
    }

    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if ((event.target == cbIndicador) || (event.target == cbPeriodo) || (event.target == cbRepresentante)) {
					if ((event.target == cbRepresentante) && ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
					}
					list();
				}
				break;
			}
    	}
    }
    
    private int getLarguraColuna() { 
        int width = Settings.screenWidth; 
        width = width > Settings.screenHeight ? Settings.screenHeight : width;
        return ValueUtil.getIntegerValueRoundUp((width / 3) * 2);
   }
}