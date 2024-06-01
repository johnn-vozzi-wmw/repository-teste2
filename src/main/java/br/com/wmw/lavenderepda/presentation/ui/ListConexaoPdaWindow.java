package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListConexaoPdaWindow extends WmwListWindow {

	public ListConexaoPdaWindow() {
        super(Messages.CONEXAOPDA_TITULO_CADASTRO);
        setBaseCadWindow(new CadConexaoPdaWindow());
        listContainer = new GridListContainer(2, 1);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		singleClickOn = true;
		sortAtributte = "DSCONEXAO";
		setDefaultRect();
    }

    protected CrudService getCrudService() throws java.sql.SQLException {
        return ConexaoPdaService.getInstance();
    }

    protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
    	return getCrudService().findAll();
    }

    protected String[] getItem(Object domain) throws java.sql.SQLException {
		ConexaoPda conexao = (ConexaoPda) domain;
		String[] item = {StringUtil.getStringValue(conexao.dsConexao) + getSufixoConexao(conexao),
		StringUtil.getStringValue(conexao.dsUrlWebService) };
		return item;
	}

	private String getSufixoConexao(ConexaoPda conexao) {
		String sufixo = "";
		if (ValueUtil.getBooleanValue(StringUtil.getStringValue(conexao.flDefault))) {
			sufixo = "  (" + Messages.CONEXAOPDA_LABEL_FLDEFAULT + ")";
		} else if (ValueUtil.getBooleanValue(StringUtil.getStringValue(conexao.flConexaoSecundaria))) {
			sufixo = "  (" + Messages.CONEXAOPDA_LABEL_FLCONEXAOSECUNDARIA + ")";
		}
		return sufixo;
	}

    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	protected BaseDomain getDomainFilter() {
		return new ConexaoPda();
	}

	protected void onFormStart() {
		UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL);
	}

	protected void onFormEvent(Event event) throws SQLException {
	}

	public void updateCurrentRecord(BaseDomain domain) throws java.sql.SQLException {
		list();
	}
	public void singleClickInList() throws SQLException {
		if (Session.isModoSuporte || LavenderePdaConfig.permiteEditarConexaoPda || ValueUtil.VALOR_SIM.equalsIgnoreCase(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.PERMITEEDITARCONEXAOPDA))) {
			super.singleClickInList();
		} else {
			UiUtil.showWarnMessage(Messages.CONEXAO_EDICAO_NAO_PERMITIDA);
		}
	}

}