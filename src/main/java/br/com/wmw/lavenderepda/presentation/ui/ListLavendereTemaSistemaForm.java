package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ListTemaSistemaForm;
import br.com.wmw.framework.presentation.ui.ext.BaseListBox;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;

public class ListLavendereTemaSistemaForm extends ListTemaSistemaForm {

	public ListLavendereTemaSistemaForm() throws SQLException {
		super();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return TemaSistemaLavendereService.getTemaSistemaLavendereInstance();
	}

	@Override
	protected int getCdTemaAtual() throws SQLException {
		return ValueUtil.getIntegerValue(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.temaPadraoSistema));
	}

	@Override
	public void btHidedClickInList() throws SQLException {
		TemaSistemaLavendereService.getTemaSistemaLavendereInstance().clearTemaCache();
		String cdTema = StringUtil.getStringValue(((TemaSistema) getSelectedDomain()).cdTema);
		ConfigInternoService.getInstance().addValueGeral(ConfigInterno.temaPadraoSistema, cdTema);
		//--
		CadItemPedidoForm.invalidateInstance();
		ListItemPedidoForm.invalidateInstance();
		MainLavenderePda.getInstance().configuraCoresSistema();
		MainLavenderePda.getInstance().setPersonFontsAndSizes();
		MainLavenderePda.getInstance().removeBaseFormsButNotMenu();
		MainMenu menuP = new MainMenu(Messages.MENU_PRINCIPAL);
		MainLavenderePda.getInstance().setNewMainForm(menuP);
		MainLavenderePda.getInstance().menuSuperior = new MenuSuperior();
		MainLavenderePda.getInstance().menuSuperior.setVisible(false);
		BaseUIForm.invalidateNotificacaoForm();
		ListNotificacaoForm.showNotificacaoIfHasNotificacaoNaoLidos();
		show(new ListLavendereTemaSistemaForm());
		
	}

}
