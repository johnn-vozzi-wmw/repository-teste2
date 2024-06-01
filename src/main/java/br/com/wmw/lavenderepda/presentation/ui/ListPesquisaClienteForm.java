package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.NfDevolucaoService;
import br.com.wmw.lavenderepda.business.service.PesquisaAppService;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppHistCliService;
import br.com.wmw.lavenderepda.business.service.QuestionarioService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class ListPesquisaClienteForm extends BaseCrudListForm {

	protected GridListContainer listContainerGeracao;
	private SessionContainer sessaoDica;
	private boolean onSelectPesquisaWindow;
	private Cliente cliente;
	private LabelName lbPesquisaCliente;
	private boolean aprovouNotas;
	private boolean fromNFDevolucaoWindow;

	public ListPesquisaClienteForm(boolean onSelectPesquisaWindow, Cliente cliente) {
		super(Messages.PESQUISAS_CLIENTE_TITULO);
		this.onSelectPesquisaWindow = onSelectPesquisaWindow;
		if (cliente != null) {
			this.cliente = cliente;
		}
		singleClickOn = true;
		listResizeable = false;
		sessaoDica = new SessionContainer();
		lbPesquisaCliente = new LabelName(Messages.PESQUISAS_CLIENTE_SELECIONE);
		lbPesquisaCliente.setForeColor(ColorUtil.sessionContainerForeColor);
		constructorListContainer();
	}

	private void constructorListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColsSort(new String[][] {{"Nome", Questionario.NMCOLUNA_NMQUESTIONARIO}});
		listContainer.atributteSortSelected = sortAtributte = Questionario.NMCOLUNA_NMQUESTIONARIO;
		listContainer.sortAsc = sortAsc;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		Questionario questionarioFilter = new Questionario();
		questionarioFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		return questionarioFilter;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return QuestionarioService.getInstance();
	}

	//@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, sessaoDica, LEFT, getTop(), FILL, UiUtil.getLabelPreferredHeight() + HEIGHT_GAP_BIG);
		UiUtil.add(sessaoDica, lbPesquisaCliente, getLeft(), CENTER, PREFERRED, PREFERRED);
		UiUtil.add(this, listContainer, LEFT, AFTER, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

	@Override
	public void onFormExibition() throws SQLException {
		list();
		if (!fromNFDevolucaoWindow) {
			final boolean obrigaAprovacaoNotasDevolucaoAposPesquisa = NfDevolucaoService.getInstance().hasNotaDevolucaoPendente(NfDevolucao.getNfDevolucao()) && LavenderePdaConfig.obrigaAprovacaoNotasDeDevolucao() && LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira;
			if (todasPesquisasRespondidas() && obrigaAprovacaoNotasDevolucaoAposPesquisa) {
				showNfDevolucaoWindow();
			}
			
		}
		fromNFDevolucaoWindow = false;
		super.onFormExibition();
	}

	private void showNfDevolucaoWindow() throws SQLException {
		UiUtil.showConfirmMessage(Messages.NFDEVOLUCAO_OBRIGA_APROVAR_REPROVAR);
		fromNFDevolucaoWindow = true;
		new ListNfDevolucaoWindow(NfDevolucao.getNfDevolucao()).popup();
	}

	//@Override
	protected String[] getItem(Object domain) throws SQLException {
		Questionario questionario = (Questionario) domain;
		String[] item = {
				StringUtil.getStringValue(questionario.nmQuestionario),
				StringUtil.getStringValue(ValueUtil.VALOR_NI),
				StringUtil.getStringValue(ValueUtil.VALOR_NI),
				StringUtil.getStringValue(ValueUtil.VALOR_NI)};
		return item;
	}

	@Override
	public void singleClickInList() throws SQLException {
		super.singleClickInList();
		Questionario questionario = (Questionario) getSelectedDomain();
		if (questionario == null) {
			return;
		}
		questionario.cdCliente = cliente.cdCliente;
		if (criaOuContinuaPesquisa(questionario)) {
			if (onSelectPesquisaWindow) {
				CadPesquisaWindow pesquisaWindow = new CadPesquisaWindow(onSelectPesquisaWindow, questionario);
				pesquisaWindow.popup();
				list();
			} else {
				show(new CadPesquisaForm(false, questionario));
			}
		}
	}
	//@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	public void setPropertiesInRowList(BaseListContainer.Item c, BaseDomain domain) throws SQLException {
		if (LavenderePdaConfig.nuDiasRespPesq() > 0 || (LavenderePdaConfig.isObrigaRespostaRegistroChegada() && SessionLavenderePda.visitaAndamento != null)) {
			Questionario questionario = (Questionario) domain;
			PesquisaApp pesquisaAppFilter = PesquisaApp.getDomain();
			Date dataPesquisaAppFilter;
			if (LavenderePdaConfig.validaPesquisasPorCliente() && !LavenderePdaConfig.isObrigaRespostaRegistroChegada()) {
				String clientePesquisaFilter = ClienteService.getInstance().findColumnByRowKey(cliente.getRowKey(), Cliente.NMCOLUNA_DTFIMPESQUISA);
				dataPesquisaAppFilter = DateUtil.toDate(clientePesquisaFilter);
			} else {
				if (LavenderePdaConfig.isObrigaRespostaRegistroChegada() && SessionLavenderePda.visitaAndamento != null) {
					pesquisaAppFilter.cdVisita = SessionLavenderePda.visitaAndamento.cdVisita;
				}
				pesquisaAppFilter = PesquisaAppService.getInstance().findDataFimPesquisaByPesquisaApp(pesquisaAppFilter, questionario);
				dataPesquisaAppFilter = pesquisaAppFilter.dtFimPesquisa;
			}
			Date dtAtual = DateUtil.getCurrentDate();
			Date dtLimiteRespPesq;
			int dias = LavenderePdaConfig.nuDiasRespPesq();
			if (ValueUtil.isEmpty(dataPesquisaAppFilter)) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PESQUISA_PENDENTE);
			} else {
				dtLimiteRespPesq = dataPesquisaAppFilter;
				dtLimiteRespPesq.advance(dias);
				if (dtLimiteRespPesq.isBefore(dtAtual)) {
					listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PESQUISA_PENDENTE);
				}
			}
		}
	}



	private boolean criaOuContinuaPesquisa(Questionario questionario) throws SQLException {
		PesquisaApp pesquisaFilter = new PesquisaApp();
		pesquisaFilter.cdEmpresa = questionario.cdEmpresa;
		pesquisaFilter.cdCliente = questionario.cdCliente;
		pesquisaFilter.cdQuestionario = questionario.cdQuestionario;
		pesquisaFilter.cdUsuario = questionario.cdUsuario;
		boolean isObrigaRespostaRegistroChegada = SessionLavenderePda.visitaAndamento != null && LavenderePdaConfig.isObrigaRespostaRegistroChegada();
		if (isObrigaRespostaRegistroChegada) {
			pesquisaFilter.cdVisita = SessionLavenderePda.visitaAndamento.cdVisita;
		}
		PesquisaApp pesquisa = PesquisaAppService.getInstance().findLast(pesquisaFilter);
		if (pesquisa == null) {
			PesquisaAppService.getInstance().criaNovaPesquisa(questionario, null, isObrigaRespostaRegistroChegada);
		} else if (!ValueUtil.isEmpty(pesquisa.dtFimPesquisa) && UiUtil.showConfirmYesNoMessage(Messages.PESQUISAS_CLIENTE_ATENCAO, Messages.PESQUISAS_CLIENTE_NOVAPESQUISA))  {
			PesquisaAppService.getInstance().criaNovaPesquisa(questionario, pesquisa.cdPesquisaApp, isObrigaRespostaRegistroChegada);
		} else if (ValueUtil.isEmpty(pesquisa.dtFimPesquisa)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PESQUISAS_CLIENTE_ATENCAO, Messages.PESQUISAS_CLIENTE_ANDAMENTO)) {
				PesquisaAppService.getInstance().criaNovaPesquisa(questionario, pesquisa.cdPesquisaApp, isObrigaRespostaRegistroChegada);
			}
		} else {
			return false;
		}
		return true;
	}

	public void visibleState() {
		super.visibleState();
		barTopContainer.setVisible(!onSelectPesquisaWindow);
		barBottomContainer.setVisible(false);
	}

	protected int getTop() {
		if (onSelectPesquisaWindow) {
			return TOP;
		} else {
			return super.getTop();
		}
	}


	public boolean possuiPesquisaPendente() throws SQLException {
		PesquisaApp pesquisaAppFilter;
		Date dtAtual = DateUtil.getCurrentDate();
		Date dtLimiteRespPesq;
		int dias = LavenderePdaConfig.nuDiasRespPesq();
		if (LavenderePdaConfig.validaPesquisasPorCliente() && !LavenderePdaConfig.isObrigaRespostaRegistroChegada()) {
			String clientePesquisaFilter = ClienteService.getInstance().findColumnByRowKey(cliente.getRowKey(), Cliente.NMCOLUNA_DTFIMPESQUISA);
			Date dataPesquisaAppFilter = DateUtil.toDate(clientePesquisaFilter);
			if (!ValueUtil.isEmpty(dataPesquisaAppFilter)) {
				dtLimiteRespPesq = dataPesquisaAppFilter;
				dtLimiteRespPesq.advance(dias);
				if (dtLimiteRespPesq.isBefore(dtAtual)) {
					return true;
				}
			} else {
				return true;
			}
		} else {
			List<Questionario> questionarioList = QuestionarioService.getInstance().findAllQuestionario((Questionario) getDomainFilter());
			int size = questionarioList.size();
			for (int i = 0; i < size; i++) {
				pesquisaAppFilter = PesquisaApp.getDomain();
				if (LavenderePdaConfig.isObrigaRespostaRegistroChegada() && SessionLavenderePda.visitaAndamento != null) {
					pesquisaAppFilter.cdVisita = SessionLavenderePda.visitaAndamento.cdVisita;
				}
				pesquisaAppFilter = PesquisaAppService.getInstance().findDataFimPesquisaByPesquisaApp(pesquisaAppFilter, (Questionario) questionarioList.get(i));
				if (!ValueUtil.isEmpty(pesquisaAppFilter.dtFimPesquisa)) {
					dtLimiteRespPesq = pesquisaAppFilter.dtFimPesquisa;
					dtLimiteRespPesq.advance(dias);
					if (dtLimiteRespPesq.isBefore(dtAtual)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean todasPesquisasRespondidas() throws SQLException {
	    PesquisaApp pesquisaAppFilter;
	    List<Questionario> questionarioList = QuestionarioService.getInstance().findAllQuestionario((Questionario) getDomainFilter());
	    int size = questionarioList.size();

	    for (int i = 0; i < size; i++) {
	        pesquisaAppFilter = PesquisaApp.getDomain();
	        pesquisaAppFilter = PesquisaAppService.getInstance().findDataFimPesquisaByPesquisaApp(pesquisaAppFilter, questionarioList.get(i));
	        
	        if (ValueUtil.isEmpty(pesquisaAppFilter.dtFimPesquisa)) {
	            return false;
	        }
	    }
	    return true; 
	}
}
