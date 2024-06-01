package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.NoConnectionAvailableException;
import br.com.wmw.framework.presentation.ui.AdmDebugConfigWindow;
import br.com.wmw.framework.presentation.ui.AdmDebugConfigWindow.ConfirmAcess;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TabelaDb;
import br.com.wmw.lavenderepda.business.service.TabelaDbService;
import br.com.wmw.lavenderepda.business.service.UsuarioPdaService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DefaultLavendereCrudDbxDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.sync.async.AsyncManutencaoDatabase;
import br.com.wmw.lavenderepda.sync.async.TypeAsyncManutencao;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class AdmTabelasWindow extends WmwWindow {

	private GridListContainer listContainer;
	private HttpConnection conexaoPda;
	private ButtonOptions btMenuInferior;
	private EditFiltro edFiltro;
	private Vector tabelaInexistenteList;
	private ButtonPopup btUsuarioSessao;
	private int clickCount;

	public AdmTabelasWindow() {
		super(Messages.ADMINISTRACAO_NOME_ENTIDADE);
		scrollable = false;
		edFiltro = new EditFiltro("999999999", 50);
		btMenuInferior = new ButtonOptions();
		btMenuInferior.transparentBackground = false;
		btMenuInferior.setBackColor(ColorUtil.formsBackColor);
		btUsuarioSessao = new ButtonPopup(Messages.ADMINISTRACAO_USUARIO_SESSAO);
		addItensMenuInferior();
		criaGrid();
		setDefaultRect();
		recuperaUsuarioSessao();
	}

	@Override
	public void initUI() {
		super.initUI();
		addButtonPopup(btFechar);
		addButtonPopup(btUsuarioSessao);
		addButtonOptions(btMenuInferior);
		UiUtil.add(this, edFiltro, getLeft(), TOP + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - footerH);
	}
	
	private void addItensMenuInferior() {
		btMenuInferior.removeAll();
		btMenuInferior.addItem(Messages.ADMINISTRACAO_LIMPAR_REGISTROS);
		btMenuInferior.addItem(Messages.ADMINISTRACAO_RECEBER_ARQUIVOS);
		btMenuInferior.addItem(Messages.ADMINISTRACAO_RESET_DATABASE);
		btMenuInferior.addItem(Messages.ADMINISTRACAO_RECOVER_CORRUPT);
	}

	private void criaGrid() {
		listContainer = new GridListContainer(4, 2, true, false);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColsSort(new String[][] {{Messages.ADM_TABELAS_NOME, TabelaDb.NMCOLUNA_NMTABELA}, {Messages.ADM_TABELAS_NUM_REGISTROS, TabelaDb.NMCOLUNA_NUREGISTROS}, {Messages.ADM_TABELAS_MAIOR_CARIMBO, TabelaDb.NMCOLUNA_NUMAIORCARIMBO}});
		listContainer.atributteSortSelected = TabelaDb.NMCOLUNA_NMTABELA;
		listContainer.sortAsc = ValueUtil.VALOR_SIM;
	}
	
	public int getGridSize() {
		return listContainer.size();
	}

	boolean showConexao; 
	private void checkConexao() {
		
		new AsyncUIControl(true) {
			@Override
			public void execute() {
				if (conexaoPda == null) {
					LogSyncTimer timer = new LogSyncTimer("Verificando conexão").newLogOnFinish();
					try {
						showConexao = !SyncManager.isConexaoPdaDisponivel();
					} catch (Throwable e) {
						LogSync.error(e.getMessage());
					} finally {
						timer.finish();
					}
				}
			}
			@Override
			public void after() {
				if (showConexao) {
					showConexaoWindow();
				}
			}
		}.open();
	}

	private void recuperaUsuarioSessao() {
		if (ValueUtil.isEmpty(Session.getCdUsuario())) {
			try {
				Session.setCdUsuario(UsuarioPdaService.getInstance().getCdUsuarioPda());
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}
	
	private void showConexaoWindow() {
		if (LavenderePdaConfig.usaSistemaModoOffLine) {
			UiUtil.showInfoMessage(Messages.MENU_SISTEMA_OFFLINE);
		} else {
			if (MainLavenderePda.getInstance().isInSyncForm()) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MENU_SISTEMA_INDISPONIVEL_FORM, new String[] { Messages.MENU_CONFIGURACOES_CONEXAO, "Sincronização" }));
			} else {
				ListConexaoPdaWindow listUsuarioPdaConexaoWindow = new ListConexaoPdaWindow();
				listUsuarioPdaConexaoWindow.popup();
			}
		}
	}
	
	@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btUsuarioSessao) {
						showWindowSessaoUsuario();
					} 
					break;
				}
				case EditIconEvent.PRESSED : {
					if (event.target == edFiltro) {
						list();
					}
					break;
				}
				case ButtonOptionsEvent.OPTION_PRESS: {
					if (event.target == btMenuInferior) {
						if (btMenuInferior.selectedItem.equals(Messages.ADMINISTRACAO_LIMPAR_REGISTROS)) {
							btLimpaTabelaClick();
						} else if (btMenuInferior.selectedItem.equals(Messages.ADMINISTRACAO_RECEBER_ARQUIVOS)) {
							btReceberAtualizacaoTabelasClick();
						} else if (btMenuInferior.selectedItem.equals(Messages.ADMINISTRACAO_RESET_DATABASE)) {
							btResetDadosClick();
						} else if (btMenuInferior.selectedItem.equals(Messages.ADMINISTRACAO_RECOVER_CORRUPT)) {
							btRecoverDadosClick();
						}
					}
					break;
				}
				case KeyEvent.SPECIAL_KEY_PRESS: {
					if (event.target == edFiltro && ((KeyEvent)event).isActionKey()) {
						list();
						edFiltro.requestFocus();
					}
					break;
				}
				case PenEvent.PEN_UP: {
					PenEvent e = ((PenEvent) event);
					if (e.target instanceof AdmTabelasWindow) {
						clickCount++;
						if (clickCount == 5) {
							clickCount = 0;
							AdmDebugConfigWindow debugConfigWindow = new AdmDebugConfigWindow();
							ConfirmAcess confirmAcess = debugConfigWindow.new ConfirmAcess("Liberar acesso");
							if (VmUtil.isJava()) {
								confirmAcess.liberado = true;
							} else {
								confirmAcess.popup();
							}
							if (confirmAcess.liberado) {
								debugConfigWindow.popup();
							}
						}
					}
					if ((e.target instanceof BaseListContainer.Item) && (listContainer.isEventoClickUnicoDisparado())) {
						listContainer.checkSelectedLine(!listContainer.isChecked(listContainer.getSelectedIndex()));
					}
					break;
				}
				case ControlEvent.WINDOW_CLOSED: {
					if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
						if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
							listContainer.reloadSortSettings();
							list();
						}
					}
					break;
				}
			}
		} catch (NoConnectionAvailableException e) {
			throw e;
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private void showWindowSessaoUsuario() {
		WmwInputBox inputUsuario = new WmwInputBox(Messages.USUARIO_SESSAO, Messages.USUARIO_SESSAO_INDICAR_USUARIO, Session.getCdUsuario());
		inputUsuario.setEnable(ValueUtil.isEmpty(Session.getCdUsuario()));
		inputUsuario.popup();
		if ((inputUsuario.getPressedButtonIndex() == 1) && ValueUtil.isEmpty(Session.getCdUsuario())) {
			Session.setCdUsuario(inputUsuario.getValue());
			UiUtil.showSucessMessage(Messages.USUARIO_SESSAO_INSERIDO_SUCESSO);
		}
	}

	private void btLimpaTabelaClick() {
		LoadingBoxWindow mbProcessando = UiUtil.createProcessingMessage();
		mbProcessando.popupNonBlocking();
		try {
			Vector tabelaList = getCheckedTabelas();
			int size = tabelaList.size();
			if (ValueUtil.isEmpty(tabelaList)) {
				UiUtil.showWarnMessage(Messages.ADMINISTRACAO_MSG_NENHUMA_TABELA_SELECIONADA);
				return;
			}
			if (UiUtil.showMessage(FrameworkMessages.TITULO_MSG_ATENCAO, Messages.ADMINISTRACAO_MSG_LIMPEZA_REGISTROS,
					TYPE_MESSAGE.WARN, new String[] { FrameworkMessages.BOTAO_NAO, FrameworkMessages.BOTAO_SIM }) == 0) {
				return;
			}
			limpaRegistrosTabelasSelecionadas(tabelaList);
			if (ValueUtil.isEmpty(tabelaInexistenteList)) {
				UiUtil.showSucessMessage(Messages.ADMINISTRACAO_MSG_LIMPEZA_REGISTROS_SUCESSO);
				return;
			}
			if (ValueUtil.valueEquals(size, tabelaInexistenteList.size())) {
				UiUtil.showErrorMessage(Messages.ADM_TABELAS_ERRO_LIMPEZA_TABELAS_SELECIONADAS);
			} else {						
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.ADMINISTRACAO_MSG_RECEBIMENTO_SUCESSO_EXCECAO, tabelaInexistenteList.toString(", ")));
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		} finally {
			mbProcessando.unpop();
		}
	}
	
	private void limpaRegistrosTabelasSelecionadas(Vector tabelaList) throws SQLException {
		if (ValueUtil.isEmpty(tabelaList)) return;
		tabelaInexistenteList = new Vector();
		int size = tabelaList.size();
		for (int i = 0; i < size; i++) {
			TabelaDb tabela = (TabelaDb)tabelaList.items[i];
			if (verificaSeTabelaExiste(tabela)) {
				try {
					DefaultLavendereCrudDbxDao.getInstance().truncateTable(tabela.nmTabela);
				} catch (Throwable ex) {
					ExceptionUtil.handle(ex);
				}
			} else {
				tabelaList.removeElement(tabelaInexistenteList.items[i]);
			}
		}
		list();
	}
	
	private boolean verificaSeTabelaExiste(TabelaDb tabela) throws SQLException {
		boolean exists = DefaultLavendereCrudDbxDao.getCurrentDriver().exists(tabela.nmTabela);
		if (!exists) {
			tabelaInexistenteList.addElement(tabela);
		}
		return exists;
	}

	private void btResetDadosClick() {
		if (UiUtil.showWarnConfirmYesNoMessage(Messages.ADMINISTRACAO_MSG_CONFIRMACAO_RESET)) {
			new AsyncManutencaoDatabase(TypeAsyncManutencao.RESET).open();
		}
	}

	private void btReceberAtualizacaoTabelasClick() {
		Vector tabelaList = getCheckedTabelas();
		if (ValueUtil.isEmpty(tabelaList)) {
			UiUtil.showWarnMessage(Messages.ADMINISTRACAO_MSG_NENHUMA_TABELA_SELECIONADA);
			return;
		}
		if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ADMINISTRACAO_MSG_RECEBIMENTO_TABELAS)) {
			return;
		}
		checkConexao();
		try {
			int size = tabelaList.size();
			limpaRegistrosTabelasSelecionadas(tabelaList);
			new AsyncUIControl(true) {
				
				@Override
				public void execute() {
					try {
						SyncManager.recebeAtualizacaoTabelas(tabelaList);
						list();
					} catch (Exception e) {
						LogSync.error(e.getMessage());
					}
				}
			}.open();
			if (ValueUtil.isEmpty(tabelaInexistenteList)) {
				UiUtil.showSucessMessage(Messages.ADMINISTRACAO_MSG_RECEBIMENTO_SUCESSO);
				return;
			}
			if (ValueUtil.valueEquals(size, tabelaInexistenteList.size())) {
				UiUtil.showErrorMessage(Messages.ADM_TABELAS_ERRO_DOWNLOAD_TABELAS_SELECIONADAS);
			} else {						
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.ADMINISTRACAO_MSG_RECEBIMENTO_SUCESSO_EXCECAO, tabelaInexistenteList.toString(", ")));
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
	}

	public void list() throws SQLException {
    	LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize;
		Vector domainList;
		try {
			listContainer.removeAllContainers();
			listContainer.uncheckAll();
			//---
			domainList = getDomainList();
			listSize = domainList.size();
			Container[] all = new Container[listSize];
			//--
			if (listSize > 0) {
				BaseListContainer.Item c;
				TabelaDb domain;
				for (int i = 0; i < listSize; i++) {
			        all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
			        domain = (TabelaDb) domainList.items[i];
			        c.setItens(getItem(domain));
				}
				listContainer.addContainers(all);
			}
		} finally {
			msg.unpop();
		}
    }

    protected Vector getDomainList() throws SQLException {
    	Vector tableList = TabelaDbService.getInstance().getTabelaList();
    	Vector domainList = new Vector(tableList.size());
		int size = tableList.size();
		if (!ValueUtil.isEmpty(tableList)) {
			for (int i = 0; i < size; i++) {
				TabelaDb tabela = (TabelaDb) tableList.items[i];
				if (ValueUtil.isEmpty(edFiltro.getValue()) || tabela.nmTabela.contains(edFiltro.getValue().toUpperCase())) {
					domainList.addElement(tabela);
				}
			}
		}
		//Ordenação
		if (ValueUtil.isNotEmpty(listContainer.atributteSortSelected)) {
			if (listContainer.atributteSortSelected.equals(TabelaDb.NMCOLUNA_NUMAIORCARIMBO)) {  
				TabelaDb.sortAttr = TabelaDb.NMCOLUNA_NUMAIORCARIMBO;
				SortUtil.qsortInt(domainList.items, 0, domainList.size() - 1, listContainer.sortAsc.equals(ValueUtil.VALOR_SIM));
			} else if (listContainer.atributteSortSelected.equals(TabelaDb.NMCOLUNA_NUREGISTROS)) {
				TabelaDb.sortAttr = TabelaDb.NMCOLUNA_NUREGISTROS;    
				SortUtil.qsortInt(domainList.items, 0, domainList.size() - 1, listContainer.sortAsc.equals(ValueUtil.VALOR_SIM));
			} else {
				TabelaDb.sortAttr = TabelaDb.NMCOLUNA_NMTABELA;
				domainList.qsort();
				if (ValueUtil.isNotEmpty(listContainer.sortAsc)) {
					if (listContainer.sortAsc.equals(ValueUtil.VALOR_NAO)) {
						domainList.reverse();
					}
				}
			}
		}
		return domainList;
    }

	public void clearGrid() {
		listContainer.removeAll();
		listContainer.clear();
	}

    protected String[] getItem(Object domain) throws SQLException {
        TabelaDb tabela = (TabelaDb) domain;
        return new String[] { StringUtil.getStringValue(tabela.nmTabela), "", Messages.ADMINISTRACAO_LABEL_QTD_REGISTROS + StringUtil.getStringValue(tabela.nuRegistros), Messages.ADMINISTRACAO_LABEL_MAIOR_CARIMBO + StringUtil.getStringValue(tabela.nuMaiorCarimbo)};
	}

    private Vector getCheckedTabelas() {
    	Vector list = new Vector();
    	int[] checkedItens = listContainer.getCheckedItens();
    	TabelaDb tabela;
	    for (int checkedIten : checkedItens) {
		    tabela = new TabelaDb();
		    tabela.nmTabela = listContainer.getValueFromContainer(checkedIten, 0);
		    list.addElement(tabela);
	    }
		return list;
    }

    @Override
	protected void onUnpop() {
		super.onUnpop();
		listContainer.removeAll();
	}

	private void btRecoverDadosClick() {
		if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ADMINISTRACAO_MSG_CONFIRMACAO_RECOVER)) {
			return;
		}
		new AsyncManutencaoDatabase(TypeAsyncManutencao.RECOVER).open();
	}
	
}
