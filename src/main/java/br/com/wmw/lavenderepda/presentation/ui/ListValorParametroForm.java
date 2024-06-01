package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.ValorParametroService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListValorParametroForm extends LavendereCrudListForm {

    public ListValorParametroForm() throws SQLException {
        super(Messages.CONFIGPARAMETRO_NOME_ENTIDADE);
        constructorListContainer();
        setBaseCrudCadForm(new CadValorParametroForm());
        singleClickOn = true;
        listResizeable = false;
    }

    private void constructorListContainer() {
    	configListContainer("CDPARAMETRO");
        listContainer = new GridListContainer(3, 2);
    	listContainer.setUseBtHidden(Messages.BOTAO_ATIVAR);
        listContainer.setColPosition(2, RIGHT);
        listContainer.setUseSortMenu(false);
        listContainer.setBarTopSimple();
    }
    
    @Override
    protected CrudService getCrudService() throws SQLException {
        return ValorParametroService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new ValorParametro();
    }

    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	return ((ValorParametroService)getCrudService()).findAllConfigParamPda();
    }

    @Override
    protected String[] getItem(Object domain) throws SQLException {
    	ValorParametro configParametro = (ValorParametro) domain;
        //--
        String[] item = {
                StringUtil.getStringValue(configParametro.cdParametro + "  "),
                StringUtil.getStringValue(configParametro.dsParametro),
                StringUtil.getStringValue(configParametro.vlParametro.equals(ValueUtil.VALOR_SIM) || !configParametro.vlParametro.equals(ValueUtil.VALOR_NAO) ? Messages.BOTAO_ATIVO : Messages.BOTAO_INATIVO)
                };
        return item;
    }

    @Override
    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    @Override
    protected void onFormStart() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(false);
        UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL);
    }

    @Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	ValorParametro domain = (ValorParametro) super.getSelectedDomain();
    	domain.dsParametro = listContainer.getValueFromContainer(listContainer.getSelectedIndex(), 1);
    	return domain;
    }
    
    @Override
    public void onFormExibition() throws SQLException {
    	clearGrid();
    	list();
    }
    
    @Override
    public void detalhesClick() throws SQLException {
    	if (listContainer != null) {
			BaseDomain domain = getSelectedDomain();
			setBaseCrudCadForm(new CadValorParametroForm((ValorParametro) domain));
			getBaseCrudCadForm().edit(domain);
			show(getBaseCrudCadForm());
		} else {
			int indexSelected = gridEdit.getSelectedIndex();
			if (indexSelected >= 0) {
				BaseDomain domain = getSelectedDomain();
				if (getBaseCrudCadForm() != null) {
					getBaseCrudCadForm().edit(domain);
					show(getBaseCrudCadForm());
				} else {
					getBaseCrudCadMenuForm().edit(domain);
					show(getBaseCrudCadMenuForm());
				}
			} else {
				UiUtil.showGridEmptySelectionMessage(getBaseCrudCadForm().title);
			}
		}
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    }

    @Override
    protected void configureContainerBtHidden(Item containerItem) throws SQLException {
    	if (Messages.BOTAO_ATIVO.equals(containerItem.getItem(2))) {
    		containerItem.setBtHiddenConfigs("  " + Messages.BOTAO_INATIVAR + "  ", LavendereColorUtil.COR_BOTAO_HIDDEN_ORANGE, Color.getRGB(60, 60, 60));
    	} else {
    		containerItem.setBtHiddenConfigs("    " + Messages.BOTAO_ATIVAR + "    ", LavendereColorUtil.COR_BOTAO_HIDDEN_GREEN, Color.getRGB(60, 60, 60));
    	}
    }

    @Override
    public void updateCurrentRecord(BaseDomain domain) throws SQLException {
    	super.updateCurrentRecord(domain);
    	configureContainerBtHidden((BaseListContainer.Item)listContainer.getBaseListContainer().getSelectedItem());
    }
    
    @Override
    public void onFormClose() throws SQLException {
    	LavenderePdaConfig.loadValorParametros();
		MainLavenderePda.getInstance().setMenuVisibility(true);
    	super.onFormClose();
    }
    
    @Override
    public void btHidedClickInList() throws SQLException {
    	ValorParametro valorParametro = (ValorParametro)getSelectedDomain();
    	valorParametro.vlParametro = StringUtil.getStringValue(!ValueUtil.getBooleanValue(valorParametro.vlParametro));
    	ValorParametroService.getInstance().update(valorParametro);
    	configureContainerBtHidden((BaseListContainer.Item)listContainer.getBaseListContainer().getSelectedItem());
    	updateCurrentRecord(valorParametro);
	}

}
