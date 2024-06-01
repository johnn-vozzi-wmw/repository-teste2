package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cesta;
import br.com.wmw.lavenderepda.business.service.CestaService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;


public class ListCestaForm extends WmwListWindow {

	public String cdCestaSelecionada;

	public ListCestaForm() {
		super(Messages.CESTA_ESCOLHA_UMA);
		btFechar.setText(FrameworkMessages.BOTAO_SELECIONAR);
		setRect(20);
	}

    //@Override
    protected CrudService getCrudService() throws java.sql.SQLException {
        return CestaService.getInstance();
    }

    protected BaseDomain getDomainFilter() {
    	return new Cesta();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
        return CestaService.getInstance().findAllCestas(null);
    }

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
        Cesta cesta = (Cesta) domain;
    	String[] item = {
    			StringUtil.getStringValue(cesta.cdCesta),
    			StringUtil.getStringValue(cesta.dsCesta),
	        	""};
    	return item;
    }

    //@Override
    protected String getSelectedRowKey() {
        String[] item = grid.getSelectedItem();
        return item[0];
    }

    //@Override
    public void list() throws java.sql.SQLException {
    	super.list();
		grid.add(new String[]{"", Messages.CESTA_NAO_USAR});
    	grid.qsort(1);
    }

    //@Override
    protected void onFormStart() {
    	GridColDefinition[] gridColDefiniton = {
    			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
    			new GridColDefinition(Messages.CESTA_NOME_ENTIDADE, -100, LEFT)};
    	grid = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, grid);
        grid.setRect(LEFT, getTop()+HEIGHT_GAP, FILL, FILL);
    }
    //@Override
  	protected void addBtFechar() {
  		UiUtil.add(this, btFechar, CENTER, BOTTOM-HEIGHT_GAP);
  	}


    //@Override
    public void detalhesClick() throws SQLException {
    	if (grid.getSelectedIndex() != -1) {
    		cdCestaSelecionada = getSelectedRowKey();
    		unpop();
    	} else {
    		UiUtil.showInfoMessage(MessageUtil.getMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID , Messages.CESTA_NOME_ENTIDADE));
    	}
    }

    //@Override
	protected void onFormEvent(Event event) throws SQLException {}

	protected void btFecharClick() throws SQLException {
		detalhesClick();
	}

}
