package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseRadio;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.CheckBooleanGroup;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pergunta;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.PerguntaRespostaService;
import br.com.wmw.lavenderepda.business.service.PerguntaService;
import br.com.wmw.lavenderepda.business.service.PesquisaAppService;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppFotoService;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppHistCliService;
import br.com.wmw.lavenderepda.business.service.PesquisaRespAppService;
import br.com.wmw.lavenderepda.business.service.QuestionarioService;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.RadioGroupController;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadPesquisaForm extends BaseCrudCadForm {

	private int indexPergunta;

	private Vector listPerguntas;
	private Vector listRespostasPerguntaAtual;
	private Vector listRespostasEmBancoMultiCheckCache;
	private BaseRadio[] listRadioRespostas;
	private PerguntaResposta respostaAnterior;

	private Pergunta perguntaAtual;
	private Questionario questionario;
	private PesquisaApp pesquisaApp;

	private RadioGroupController radioGroup;
	private LabelValue edPergunta;
	private LabelName lbResposta;
	private LabelName lbRespostaAdicional;
	private EditMemo edRespostaMemo;
	private EditMemo edRespostaAdicionalMemo;
	private EditText edRespostaNumber;
	private EditDate edRespostaDate;
	private CheckBooleanGroup checkGroup;
	ButtonAction totalizador;

	private boolean emAndamento;
	private boolean onSelectPesquisaWindow;

	public ButtonAction btAnterior;
	public ButtonAction btProxima;
	private ButtonAction btFinalizar; 

	private BaseScrollContainer sc;
	private BaseScrollContainer bs;
	public boolean finalizado;
	private boolean showMessage;
	private boolean adicionandoPerguntaSecundaria;

	public CadPesquisaForm(boolean onSelectPesquisaWindow, Questionario questionario) throws SQLException {
		super(Messages.PESQUISAS_CLIENTE_PERGUNTA);
		this.questionario = questionario;
		this.onSelectPesquisaWindow = onSelectPesquisaWindow;
		initUi();
		recuperaPesquisaApp();
		defineListaDePerguntas(questionario);
		indexPergunta = 0;
		btSalvar.setVisible(false);
	}

	@Override
	protected void onFormStart() throws SQLException {

	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		montaForm();
	}

	public void montaForm() throws SQLException {
		if (ValueUtil.isEmpty(listPerguntas)) return;
		definePerguntaAtual(null);
		isPerguntaRespostaHistorico();
		defineRespostasPossiveis();
		montaTela();
		defineRespostaPreenchida();
		visibleState();
	}

	private void isPerguntaRespostaHistorico() throws SQLException {
		if (isFlRespostaAutomaticaPeloHistorico() && !showMessage && PesquisaRespAppHistCliService.getInstance().existePesquisaNoHistorico(questionario)) {
			this.showMessage = true;
			UiUtil.showMessage(Messages.PESQUISAS_CLIENTE_AVISO_RESPOSTA_PREENCHIDA, TYPE_MESSAGE.INFO);
		}
	}

	private void defineRespostaPreenchida() throws SQLException {
		if (Pergunta.TIPO_PERGUNTA_MULTI_CHECK.equals(perguntaAtual.cdTipoPergunta)) {
			if (checkGroup == null) return;
			Vector listRespostas = buscaPesquisaRespsAppDoDispositivoOuHistorico();
			listRespostasEmBancoMultiCheckCache = listRespostas;
			populaPerguntasSecundariasPerguntasRespostas(listRespostasEmBancoMultiCheckCache);
			checkGroup.setCheckFromObjects(Arrays.asList(listRespostas.items));
			return;
		}
		PesquisaRespApp pesquisaRespApp = buscaPesquisaRespAppDoDispositivoOuHistorico();
		PerguntaResposta perguntaResposta = new PerguntaResposta();
		if (pesquisaRespApp != null) {
			perguntaResposta = PerguntaRespostaService.getInstance().findPerguntaRespostaByPesquisaRespApp(pesquisaRespApp);
		} else if (Pergunta.TIPO_PERGUNTA_MULTIPLA.equals(perguntaAtual.cdTipoPergunta)) {
			respostaAnterior = null;
		}
		if (isFlRespostaAutomaticaPeloHistorico() || isFlRespostaAutomaticaPelaEntidadeCampo() || pesquisaRespApp != null || (perguntaResposta != null && !ValueUtil.isEmpty(perguntaResposta.dsResposta))) {
			String respostaByEntidadeCampo = new String();
			if (isFlRespostaAutomaticaPelaEntidadeCampo()) {
				respostaByEntidadeCampo = getRespostaByEntidadeColuna();
			}
			if (pesquisaRespApp != null || isFlRespostaAutomaticaPelaEntidadeCampo()) {
				switch (perguntaAtual.cdTipoPergunta) {
					case Pergunta.TIPO_PERGUNTA_MULTIPLA:
						if (perguntaResposta != null && listRespostasPerguntaAtual.contains(perguntaResposta)) {
							radioGroup.setSelectedIndex(listRespostasPerguntaAtual.indexOf(perguntaResposta));
							carregaPerguntaRespostaNoBanco(perguntaResposta);
						} else if (isFlRespostaAutomaticaPelaEntidadeCampo() && !ValueUtil.isEmpty(respostaByEntidadeCampo)) {
							radioGroup.setSelectedIndex(retornaIndexListRespostas(listRespostasPerguntaAtual, respostaByEntidadeCampo));
						} else if (pesquisaRespApp != null && !ValueUtil.isEmpty(pesquisaRespApp.dsResposta)) {
							radioGroup.setSelectedIndex(retornaIndexListRespostas(listRespostasPerguntaAtual, pesquisaRespApp.dsResposta));
						}
						break;

					case Pergunta.TIPO_PERGUNTA_TEXTO:
						if (pesquisaRespApp != null && !ValueUtil.isEmpty(pesquisaRespApp.dsResposta)) {
							edRespostaMemo.setText(pesquisaRespApp.dsResposta);
						} else if (isFlRespostaAutomaticaPelaEntidadeCampo() && !ValueUtil.isEmpty(respostaByEntidadeCampo)) {
							edRespostaMemo.setText(respostaByEntidadeCampo);
						}
						break;

					case Pergunta.TIPO_PERGUNTA_NUMERICO:
						if (pesquisaRespApp != null && !ValueUtil.isEmpty(pesquisaRespApp.dsResposta)) {
							edRespostaNumber.setValue(pesquisaRespApp.dsResposta);
						} else if (isFlRespostaAutomaticaPelaEntidadeCampo() && !ValueUtil.isEmpty(respostaByEntidadeCampo)) {
							edRespostaNumber.setValue(respostaByEntidadeCampo);
						}
						break;

					case Pergunta.TIPO_PERGUNTA_DATE:
						if (pesquisaRespApp != null && !ValueUtil.isEmpty(pesquisaRespApp.dsResposta)) {
							edRespostaDate.setText(pesquisaRespApp.dsResposta);
						} else if (isFlRespostaAutomaticaPelaEntidadeCampo() && !ValueUtil.isEmpty(respostaByEntidadeCampo)) {
							edRespostaDate.setText(respostaByEntidadeCampo);
						}
						break;

					default:
						break;
				}
				if (!isFlRespostaAutomaticaPelaEntidadeCampo()) {
					setRespostaAdicional(pesquisaRespApp);
				}
			}
		}
	}

	private boolean isFlRespostaAutomaticaPeloHistorico() {
		return Pergunta.FLRESPOSTAAUTOMATICA_PELO_HISTORICO.equals(perguntaAtual.flRespostaAutomatica);
	}

	private boolean isFlRespostaAutomaticaPelaEntidadeCampo() {
		return Pergunta.FLRESPOSTAAUTOMATICA_PELA_ENTIDADE_E_CAMPO.equals(perguntaAtual.flRespostaAutomatica);
	}

	private int retornaIndexListRespostas(Vector listRespostasPerguntaAtual, String respostaByEntidadeCampo) {
		int size = listRespostasPerguntaAtual.size();
		for (int i = 0; i < size; i++) {
			String dsPerguntaResposta = ((PerguntaResposta) listRespostasPerguntaAtual.items[i]).dsResposta;
			if (ValueUtil.valueEquals(dsPerguntaResposta, respostaByEntidadeCampo)) {
				return i;
			}
		}
		return 0;
		}

	private void carregaPerguntaRespostaNoBanco(PerguntaResposta perguntaResposta) {
		respostaAnterior = perguntaResposta;
		respostaAnterior.listPerguntasSecundarias = ((PerguntaResposta)listRespostasPerguntaAtual.items[listRespostasPerguntaAtual.indexOf(perguntaResposta)]).listPerguntasSecundarias;
	}

	private void setRespostaAdicional(PesquisaRespApp pesquisaRespApp) {
		if (pesquisaRespApp == null || ValueUtil.isEmpty(pesquisaRespApp.dsObservacao)) return;
		
		if ((perguntaAtual.exibeObservacao() || perguntaAtual.flQuestionarioPerguntaExibeObservacao)) {
			edRespostaAdicionalMemo.setText(pesquisaRespApp.dsObservacao);
		}
	}

	private PesquisaRespApp buscaPesquisaRespAppDoDispositivoOuHistorico() throws SQLException {
		//Busca na pesquisa atual (em andamento):
		PesquisaRespApp pesquisaRespApp = PesquisaRespAppService.getInstance().findRespostaParaInterface(pesquisaApp, questionario, perguntaAtual.cdPergunta);
		//Busca do histórico retornado da view na web:
		PesquisaRespApp pesquisaRespAppHist = PesquisaRespAppHistCliService.getInstance().findRespostaParaInterface(pesquisaApp, questionario, perguntaAtual.cdPergunta);
		if (pesquisaRespApp == null && pesquisaRespAppHist != null) {
			pesquisaRespAppHist.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
			PesquisaRespAppService.getInstance().insert(pesquisaRespAppHist);
			return pesquisaRespAppHist;
		} 
		return pesquisaRespApp;
	}
	
	private Vector buscaPesquisaRespsAppDoDispositivoOuHistorico() throws SQLException {
		//Busca na pesquisa atual (em andamento):
		Vector pesquisaRespApp = PesquisaRespAppService.getInstance().findRespostasParaInterface(pesquisaApp, questionario, perguntaAtual.cdPergunta);
		//Busca do histórico retornado da view na web:
		Vector pesquisaRespAppHistList = PesquisaRespAppHistCliService.getInstance().processaPesquisaRespHistoricoParaInterfaceCheck(pesquisaApp, questionario, perguntaAtual.cdPergunta, ValueUtil.isEmpty(pesquisaRespApp));
		if (ValueUtil.isEmpty(pesquisaRespApp) && ValueUtil.isNotEmpty(pesquisaRespAppHistList)) {
			return pesquisaRespAppHistList;
		} 
		
		return processaPerguntasRespostasParaCheckComponent(pesquisaRespApp);
	}

	private Vector processaPerguntasRespostasParaCheckComponent(Vector pesquisaRespList) throws SQLException {
		int size = pesquisaRespList.size();
		for (int i = 0; i < size; i++) {
			PesquisaRespApp resposta = (PesquisaRespApp) pesquisaRespList.items[i]; 
			resposta.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
			
			PerguntaResposta pergResp = new PerguntaResposta();
			pergResp.cdEmpresa = resposta.cdEmpresa;
			pergResp.cdPergunta = resposta.cdPergunta;
			pergResp.cdResposta = resposta.cdResposta;
			pesquisaRespList.items[i] = pergResp;
		}
		return pesquisaRespList;
	}

	private void defineListaDePerguntas(Questionario questionario) throws SQLException {
		if (questionario == null) return;
		listPerguntas = PerguntaService.getInstance().findAllPerguntasByQuestionario(questionario);
	}

	private void definePerguntaAtual(Pergunta perguntaObrigatoria) throws SQLException {
		Pergunta pergunta = perguntaObrigatoria;
		if (!emAndamento && pergunta == null) {
			pergunta = PerguntaService.getInstance().recuperaProximaPergunta(pesquisaApp);
			if (pergunta != null && listPerguntas.contains(pergunta)) {
				int index = listPerguntas.indexOf(pergunta);
				indexPergunta = listPerguntas.size()-1 == index ? index : index+1;
			}
		} else {
			if (pergunta != null && listPerguntas.contains(pergunta)) {
				indexPergunta = listPerguntas.indexOf(pergunta);
			}
		}
		emAndamento = true;
		perguntaAtual = (Pergunta) listPerguntas.items[indexPergunta];
		perguntaAtual.isObrigatoria = isPerguntaObrigatoria();
		perguntaAtual.flQuestionarioPerguntaExibeObservacao = PerguntaService.getInstance().isExibeObservacao(questionario.cdEmpresa, questionario.cdQuestionario, perguntaAtual.cdPergunta);
	}

	private void defineRespostasPossiveis() throws SQLException {
		PerguntaResposta perguntaRespostaFilter = new PerguntaResposta();
		perguntaRespostaFilter.cdEmpresa = perguntaAtual.cdEmpresa;
		perguntaRespostaFilter.cdPergunta = perguntaAtual.cdPergunta;
		perguntaRespostaFilter.sortAtributte = "NUSEQUENCIA";
		perguntaRespostaFilter.sortAsc = ValueUtil.VALOR_SIM;
		listRespostasPerguntaAtual = PerguntaRespostaService.getInstance().findAllByExample(perguntaRespostaFilter);
		populaPerguntasSecundariasPerguntasRespostas(listRespostasPerguntaAtual);
	}

	public void montaTela() {
		montaContainerDaPergunta();
		montaContainerDeRespostas();
		adicionaBotoesInferiores();
	}

	private void montaContainerDaPergunta() {
		sc = new BaseScrollContainer();
		String dsPergunta = getCurrentQuestionString();
		edPergunta = new LabelValue(Convert.insertLineBreak(Settings.screenWidth - fmH , fm, dsPergunta));
		UiUtil.add(this, sc, LEFT, getTop(), FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
		UiUtil.add(sc, edPergunta, getLeft() + WIDTH_GAP_BIG, TOP + HEIGHT_GAP_BIG);
		edPergunta.setForeColor(ColorUtil.sessionContainerForeColor);
		sc.setBackColor(ColorUtil.sessionContainerBackColor);
		sc.setRect(sc.getX(), sc.getY(), sc.getWidth(), Math.min(edPergunta.getHeight() * 2, 250));;
	}

	private void montaContainerDeRespostas() {
		bs = new BaseScrollContainer(false, true);
		UiUtil.add(this, bs, LEFT, sc.getY2() + 2, FILL, FILL - barBottomContainer.getHeight() - HEIGHT_GAP);
		switch (perguntaAtual.cdTipoPergunta) {
		case Pergunta.TIPO_PERGUNTA_MULTIPLA:
			radioGroup = new RadioGroupController();
			addRadioOptions();
			break;

		case Pergunta.TIPO_PERGUNTA_TEXTO:
			edRespostaMemo = new EditMemo("@@@@@@@@@@", FILL, FILL);
			edRespostaMemo.setMaxLength(4000);
			UiUtil.add(bs, lbResposta, edRespostaMemo, getLeft() + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP_BIG , getWFill() - WIDTH_GAP_BIG, 200);
			edRespostaMemo.requestFocus();
			break;

		case Pergunta.TIPO_PERGUNTA_NUMERICO:
			edRespostaNumber = new EditText("@@@@@@@@@@@@@@", 200);
			edRespostaNumber.setValidChars("0123456789,");
			UiUtil.add(bs, lbResposta, edRespostaNumber, getLeft() + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP_BIG, getWFill() - WIDTH_GAP_BIG);
			edRespostaNumber.requestFocus();
			break;

		case Pergunta.TIPO_PERGUNTA_DATE:
			edRespostaDate = new EditDate();
			UiUtil.add(bs, lbResposta, edRespostaDate, getLeft() + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP_BIG);
			break;

		case Pergunta.TIPO_PERGUNTA_MULTI_CHECK:
			addCheckOptions(); 
			break;

		default:
			break;
		}
		adicionaRespostaAdicional();
	}

	private void adicionaRespostaAdicional() {
		if (!perguntaAtual.exibeObservacao() && !perguntaAtual.flQuestionarioPerguntaExibeObservacao) return;
			edRespostaAdicionalMemo = new EditMemo("@@@@@@@@@@", FILL, FILL);
			UiUtil.add(bs, lbRespostaAdicional, edRespostaAdicionalMemo, getLeft() + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP_BIG*2 , getWFill() - WIDTH_GAP_BIG, 150);
		}
	
	private void adicionaBotoesInferiores() {
		
		if (listPerguntas.indexOf(perguntaAtual) > 0) UiUtil.add(barBottomContainer, btAnterior, 2);    	
		if (!isUltimaPergunta()) {
			UiUtil.add(barBottomContainer, btProxima, 4);
		} else {
			if (!onSelectPesquisaWindow)
			UiUtil.add(barBottomContainer, btFinalizar, 5);
		}
		totalizador = new ButtonAction(indexPergunta + 1 + " / " + listPerguntas.size());
		totalizador.setEnabled(false);
		UiUtil.add(barBottomContainer, totalizador, 3);
	}

	public boolean isUltimaPergunta() {
		return listPerguntas.indexOf(perguntaAtual) == listPerguntas.size() -1;
	}

	private String getCurrentQuestionString() {
		String dsPergunta = perguntaAtual.dsPergunta;
		if (perguntaAtual.isObrigatoria) {
			dsPergunta += " *";
		}
		return dsPergunta;
	}

	private void initUi() {
		btAnterior = new ButtonAction(Messages.PESQUISAS_CLIENTE_BT_ANTERIOR, "images/previous.png");
		btProxima = new ButtonAction(Messages.PESQUISAS_CLIENTE_BT_PROXIMA, "images/next.png");
		btFinalizar = new ButtonAction(Messages.PESQUISAS_CLIENTE_BT_FINALIZAR, "images/sair.png");
		lbResposta  = new LabelName();
		lbResposta.setText(Messages.PESQUISAS_CLIENTE_RESPOSTA);
		lbRespostaAdicional = new LabelName();
		lbRespostaAdicional.setText(Messages.PESQUISAS_CLIENTE_RESPOSTA_ADICIONAL);
	}

	public void visibleState() throws SQLException {
		super.visibleState();
		barTopContainer.setVisible(!onSelectPesquisaWindow);
		if (perguntaAtual != null && ValueUtil.getBooleanValue(perguntaAtual.flSomenteLeitura)) {
			disableEdit();
		}
	}

	private void disableEdit() {
		if (edRespostaMemo != null && edRespostaMemo.isEditable() && edRespostaNumber == null) {
			edRespostaMemo.setEditable(false);
		} else if (edRespostaDate != null && edRespostaDate.isEnabled() && edRespostaNumber == null) {
			edRespostaDate.setEditable(false);
		} else if (edRespostaNumber != null && edRespostaNumber.isEditable()) {
			edRespostaNumber.setEditable(false);
		}
	}

	protected int getTop() {
		return onSelectPesquisaWindow ? TOP : super.getTop();
	}

	@Override
	public String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() {
		return QuestionarioService.getInstance();
	}

	@Override
	protected BaseDomain createDomain() {
		return new Questionario();
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		Questionario questionario = ((Questionario) getDomain());
		return questionario;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) {}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btAnterior) {
				btAnteriorClick();
			} else if (event.target == btProxima) {
				btProximaClick();
			} else if (event.target == btFinalizar) {
				btFinalizarClick();
			} else if (event.target == btSalvar) {
				salvarClick();
				this.voltarClick();
			}
			break;
		default:
			break;
		}
	}

	public void btFinalizarClick() throws SQLException {
		salvarPergunta();
		if (!isUltimaPergunta()) {
			btProximaClick();
			return;
		}
		Vector perguntaObrigatoriaList = PerguntaService.getInstance().findPerguntaObrigatoriaNaoRespondida(questionario, pesquisaApp, PerguntaService.getInstance().getCdPerguntaArray(listPerguntas));
		if (!ValueUtil.isEmpty(perguntaObrigatoriaList) && perguntaObrigatoriaNaoRespondida(perguntaObrigatoriaList)) {
			UiUtil.showInfoMessage(Messages.PESQUISAS_CLIENTE_OBRIGATORIAS);
			changePergunta();
		} else {
			finalizarPesquisa();
		}
	}

	public boolean perguntaObrigatoriaNaoRespondida(Vector perguntaObrigatoriaList) throws SQLException {
		int size = perguntaObrigatoriaList.size();
		for (int i = 0; i < size; i++) {
			Pergunta perguntaObrigatoria = (Pergunta) perguntaObrigatoriaList.items[i];
			if (perguntaObrigatoria != null && !ValueUtil.getBooleanValue(perguntaObrigatoria.flSomenteLeitura)) {
				definePerguntaAtual(perguntaObrigatoria);
				return true;
			}
		}
		return false;
	}

	private void finalizarPesquisa() throws SQLException {
		pesquisaApp.dtFimPesquisa = DateUtil.getCurrentDate();
		pesquisaApp.hrFimPesquisa = TimeUtil.getCurrentTimeHHMMSS();
		PesquisaAppService.getInstance().update(pesquisaApp);
		if (LavenderePdaConfig.validaPesquisasPorCliente()) {
			ClienteService.getInstance().updateDtFimPesquisa(pesquisaApp);
		}
		UiUtil.showInfoMessage(Messages.PESQUISAS_CLIENTE_FINALIZADA);
		if (onSelectPesquisaWindow) {
			finalizado = true;
		} else {
			this.voltarClick();
		}
	}

	private void btProximaClick() throws SQLException {
		if (perguntaAtual.isObrigatoria && isPerguntaNaoPreenchida()) {
			throw new ApplicationException(Messages.PESQUISAS_CLIENTE_PERGUNTA_OBRIGATORIA_NAO_PREENCHIDA);
		}
		salvarPergunta();
		if (indexPergunta < listPerguntas.size() -1) indexPergunta++;
		changePergunta();
	}

	private void btAnteriorClick() throws SQLException {
		salvarPergunta();
		if (indexPergunta > 0) indexPergunta--;
		changePergunta();
	}


	@Override
	protected void salvarClick() throws SQLException {
		if (isPerguntaNaoPreenchida()) return;
		salvarPergunta();
	}

	private boolean isPerguntaNaoPreenchida() {
		switch (perguntaAtual.cdTipoPergunta) {
		case Pergunta.TIPO_PERGUNTA_MULTIPLA:
			PerguntaResposta perguntaResposta = getRespostaSelecionadaMult();
			return ValueUtil.valueEquals(PerguntaResposta.CD_RESPOSTAMULT_PADRAO, perguntaResposta.cdResposta);
		case Pergunta.TIPO_PERGUNTA_TEXTO:
			return ValueUtil.isEmpty(edRespostaMemo.getValue());
		case Pergunta.TIPO_PERGUNTA_NUMERICO:
			return ValueUtil.isEmpty(edRespostaNumber.getText());
		case Pergunta.TIPO_PERGUNTA_DATE:
			return ValueUtil.isEmpty(edRespostaDate.getText());
		case Pergunta.TIPO_PERGUNTA_MULTI_CHECK:
			return checkGroup.getCheckedObjects().size() == 0;
		default:
			break;
		}
		return true;
	}
	
	private void abrePesquisaFotoWindow(PerguntaResposta perguntaResposta) throws SQLException {
		CadPesquisaFotoWindow cadPesquisaFoto = new CadPesquisaFotoWindow(perguntaResposta);
		cadPesquisaFoto.popup();
	}
	
	private boolean excluiFotoJaExistentePerguntaResposta(PerguntaResposta perguntaResposta) throws SQLException {
		if (PesquisaRespAppFotoService.getInstance().isExisteFotoByPesquisaAppAndPerguntaResposta(pesquisaApp, perguntaResposta)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.MSG_AVISO_EXCLUIR_FOTO_RESPOSTA)) {
				PesquisaRespAppFotoService.getInstance().deleteAllByPesquisaAppAndPerguntaResposta(pesquisaApp, perguntaResposta);
				return true;
			}
			return false;
		}
		return true;
	}

	private void salvarPergunta() throws SQLException {
		PesquisaRespApp resposta = montaCabecalhoResposta();

		if (perguntaAtual.exibeObservacao() || perguntaAtual.flQuestionarioPerguntaExibeObservacao) {
			resposta.dsObservacao = edRespostaAdicionalMemo.getValue();
		}

		switch (perguntaAtual.cdTipoPergunta) {
			case Pergunta.TIPO_PERGUNTA_MULTIPLA:
				PerguntaResposta perguntaResposta = getRespostaSelecionadaMult();
				resposta.cdResposta = perguntaResposta.cdResposta;
				resposta.dsResposta = perguntaResposta.dsResposta;
				processaPerguntasSecundarias(Pergunta.TIPO_PERGUNTA_MULTIPLA, perguntaResposta);
				PesquisaRespAppFotoService.getInstance().deleteAllByPesquisaAppAndPerguntaRespostaDiferente(pesquisaApp, perguntaAtual.cdPergunta, resposta.cdResposta);
				if (perguntaResposta.isObrigaFoto()) {
					if (!adicionandoPerguntaSecundaria && excluiFotoJaExistentePerguntaResposta(perguntaResposta)) {
						abrePesquisaFotoWindow(perguntaResposta);
						if (perguntaResposta.pesquisaRespAppFoto != null) {
							PesquisaRespAppFotoService.getInstance().insertByPesquisaAndPerguntaResposta(pesquisaApp, perguntaResposta);
						} else {
							throw new ApplicationException(Messages.RESPOSTA_FOTO_INVALIDA);
						}						
					}
				}
				PesquisaRespAppService.getInstance().deleteAllByPerguntaPesquisa(pesquisaApp, perguntaAtual);
				break;

			case Pergunta.TIPO_PERGUNTA_TEXTO:
				resposta.cdResposta = PerguntaResposta.CD_RESPOSTALIVRE_PADRAO;
				String dsResposta = edRespostaMemo.getValue();
				if (ValueUtil.isNotEmpty(dsResposta)){
					resposta.dsResposta = dsResposta;
				}
				processaPerguntasSecundarias(Pergunta.TIPO_PERGUNTA_TEXTO, (PerguntaResposta) listRespostasPerguntaAtual.items[0]);
				break;

			case Pergunta.TIPO_PERGUNTA_NUMERICO:
				resposta.cdResposta = PerguntaResposta.CD_RESPOSTALIVRE_PADRAO;
				if (!ValueUtil.isEmpty(edRespostaNumber.getText())) {
					resposta.dsResposta = edRespostaNumber.getValue() + "";
				}
				processaPerguntasSecundarias(Pergunta.TIPO_PERGUNTA_NUMERICO, (PerguntaResposta) listRespostasPerguntaAtual.items[0]);
				break;
			case Pergunta.TIPO_PERGUNTA_DATE:
				resposta.cdResposta = PerguntaResposta.CD_RESPOSTALIVRE_PADRAO;
				if (!ValueUtil.isEmpty(edRespostaDate.getText())){
					resposta.dsResposta = edRespostaDate.getText();
				}
				processaPerguntasSecundarias(Pergunta.TIPO_PERGUNTA_DATE, (PerguntaResposta) listRespostasPerguntaAtual.items[0]);
				break;
			case Pergunta.TIPO_PERGUNTA_MULTI_CHECK:
				List<Object> list = checkGroup.getCheckedObjects();
				salvaRespostaMultiCheck(list, resposta);
				deletaDesselecionadas(list, resposta);
				return;
			default:
				break;
		}
		adicionandoPerguntaSecundaria = false;
		if (!PerguntaResposta.CD_RESPOSTAMULT_PADRAO.equals(resposta.cdResposta)) {
			PesquisaRespAppService.getInstance().insertOrUpdate(resposta);
		}
	}

	private PerguntaResposta getRespostaSelecionadaMult() {
		int selectedIndex = radioGroup.getSelectedIndex();
		if (selectedIndex == -1) {
			PerguntaResposta resposta =  new PerguntaResposta();
			resposta.cdResposta = PerguntaResposta.CD_RESPOSTAMULT_PADRAO; 
			return resposta;
		} else {
			return (PerguntaResposta) listRespostasPerguntaAtual.items[selectedIndex];
		}
	}
	
	private void deletaDesselecionadas(List<Object> listChecked, PesquisaRespApp resposta) throws SQLException {
		if (listRespostasEmBancoMultiCheckCache == null) return;
		int size = listRespostasEmBancoMultiCheckCache.size();
		try {
			for (int i = 0; i < size; i++) {
	 			PerguntaResposta resp = (PerguntaResposta) listRespostasEmBancoMultiCheckCache.items[i];
				if (listChecked.contains(resp)) continue;
				resposta.cdResposta = resp.cdResposta;
				deleteRespostasPerguntasSecundarias((PerguntaResposta) listRespostasPerguntaAtual.items[listRespostasPerguntaAtual.indexOf(resp)]);
				PesquisaRespAppService.getInstance().delete(resposta);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private void salvaRespostaMultiCheck(List<Object> listChecked, PesquisaRespApp resposta) throws SQLException {
		if (listChecked.isEmpty()) {
			resposta.cdResposta = PerguntaResposta.CD_RESPOSTAMULT_PADRAO;
		PesquisaRespAppService.getInstance().insertOrUpdate(resposta);
			return;
	}
		for (Object obj : listChecked) {
			PerguntaResposta atual = (PerguntaResposta) obj;
			resposta.cdResposta = atual.cdResposta;
			resposta.dsResposta = atual.dsResposta;
			adicionaPerguntaSecundaria(atual);
			PesquisaRespAppService.getInstance().insertOrUpdate(resposta);
		}
	}

	private PesquisaRespApp montaCabecalhoResposta() {
		PesquisaRespApp resposta = new PesquisaRespApp();
		resposta.cdEmpresa = perguntaAtual.cdEmpresa;
		resposta.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		resposta.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		resposta.cdPergunta = perguntaAtual.cdPergunta;
		resposta.cdCliente = pesquisaApp.cdCliente;
		resposta.cdQuestionario = questionario.cdQuestionario;
		return resposta;
	}

	private void changePergunta() throws SQLException {
		cleanAndRepaintComponents();
		onFormShow();
	}

	private void recuperaPesquisaApp() throws SQLException {
		PesquisaApp pesquisaAppFilter = new PesquisaApp();
		pesquisaAppFilter.cdEmpresa = questionario.cdEmpresa;
		pesquisaAppFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pesquisaAppFilter.cdQuestionario = questionario.cdQuestionario;
		pesquisaAppFilter.cdCliente = questionario.cdCliente;
		this.pesquisaApp = PesquisaAppService.getInstance().findLast(pesquisaAppFilter);
	}

	@Override
	public void reposition() {
		super.reposition();
		montaContainerDaPergunta();
		resetRectContainerRespostas();

	}

	private void resetRectContainerRespostas() {
		bs.setRect(LEFT, sc.getY2() + 2, FILL, FILL - barBottomContainer.getHeight() - HEIGHT_GAP);
		redrawContainerRespostasWithRadios();
		redrawContainerWithChecks();
		redrawRespostaAdicional();
	}

	private void redrawRespostaAdicional() {
		if (!perguntaAtual.exibeObservacao() && !perguntaAtual.flQuestionarioPerguntaExibeObservacao || 
				!(ValueUtil.valueEquals(perguntaAtual.cdTipoPergunta, Pergunta.TIPO_PERGUNTA_MULTIPLA) || ValueUtil.valueEquals(perguntaAtual.cdTipoPergunta, Pergunta.TIPO_PERGUNTA_MULTIPLA))) return;
		String valorAtual = edRespostaAdicionalMemo.getValue();
		edRespostaAdicionalMemo = new EditMemo("@@@@@@@@@@", FILL, FILL);
		edRespostaAdicionalMemo.setValue(valorAtual);
		UiUtil.add(bs, lbRespostaAdicional, edRespostaAdicionalMemo, getLeft() + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP_BIG*2 , FILL - WIDTH_GAP_BIG*2, 150);
	}

	private void redrawContainerRespostasWithRadios() {
		if (!Pergunta.TIPO_PERGUNTA_MULTIPLA.equals(perguntaAtual.cdTipoPergunta)) return;
		bs.removeAll();
		bs = new BaseScrollContainer(false, true);
		int resposta = radioGroup.getSelectedIndex();
		radioGroup = new RadioGroupController();
		UiUtil.add(this, bs, LEFT, sc.getY2() + 2, FILL, FILL - barBottomContainer.getHeight() - HEIGHT_GAP);
		addRadioOptions(); 
		radioGroup.setSelectedIndex(resposta);
	}
	
	private void redrawContainerWithChecks() {
		if (!Pergunta.TIPO_PERGUNTA_MULTI_CHECK.equals(perguntaAtual.cdTipoPergunta)) return;
		bs.removeAll();
		bs = new BaseScrollContainer(false, true);
		UiUtil.add(this, bs, LEFT, sc.getY2() + 2, FILL, FILL - barBottomContainer.getHeight() - HEIGHT_GAP);
		addCheckOptions(true);
	}

	private void addRadioOptions() {
		int qtRespostas = listRespostasPerguntaAtual.size();
		listRadioRespostas = new BaseRadio[qtRespostas];
		for (int i = 0; i < qtRespostas; i++) {
			String dsResposta = ((PerguntaResposta) listRespostasPerguntaAtual.items[i]).dsResposta;
			UiUtil.add(this, listRadioRespostas[i] = new BaseRadio(bs, dsResposta, radioGroup), getLeft(), AFTER, FILL, FILL - barBottomContainer.getHeight());
		}
	}
	
	private void addCheckOptions() {
		addCheckOptions(false);
	}
	
	private void addCheckOptions(boolean redrawing) {
		List<Object> list = ValueUtil.vectorToList(listRespostasPerguntaAtual, Object.class);
		
		checkGroup = redrawing ? checkGroup : new CheckBooleanGroup(list, bs);
		UiUtil.add(bs, checkGroup, getLeft(), AFTER, FILL, checkGroup.getHeight() + HEIGHT_GAP_BIG);
	}

	private void cleanAndRepaintComponents() {
		sc.removeAll();
		sc.reposition();
		sc.repaintNow();
		bs.removeAll();
		bs.resetPreviousControl();
		barBottomContainer.removeAll();
		barBottomContainer.repaintNow();
	}

	@Override
	protected void clearScreen() throws SQLException {}
	
	private void populaPerguntasSecundariasPerguntasRespostas(Vector listPerguntasRespostas) throws SQLException {
		int size = listPerguntasRespostas.size();
		PerguntaResposta resposta;
		for (int i = 0; i < size; i++) {
			resposta = (PerguntaResposta) listPerguntasRespostas.items[i];
			if (ValueUtil.isEmpty(resposta.cdPerguntasSecundarias)) continue;
			resposta.listPerguntasSecundarias = PerguntaService.getInstance().findPerguntasSecundariasByPerguntaResposta(resposta);
			listaPerguntasSecundariasParaHash(resposta);
		}
	}

	private void listaPerguntasSecundariasParaHash(PerguntaResposta resposta) {
		int size = resposta.listPerguntasSecundarias.size();
		Pergunta pergunta;
		resposta.hashPerguntasSecundarias = new HashMap<String, Pergunta>();
		for (int i = 0; i < size; i++) {
			pergunta = (Pergunta) resposta.listPerguntasSecundarias.items[i];
			resposta.hashPerguntasSecundarias.put(pergunta.cdPergunta, pergunta);
		}
	}

	private void deleteRespostasPerguntasSecundarias(PerguntaResposta resposta) throws SQLException {
		if (resposta == null || ValueUtil.isEmpty(resposta.listPerguntasSecundarias)) return;
		int size = resposta.listPerguntasSecundarias.size();
		Pergunta pergunta;
		for(int i = 0; i < size ; i++) {
			pergunta = (Pergunta) resposta.listPerguntasSecundarias.items[i];
			Vector listPesquisaResp =  PesquisaRespAppService.getInstance().findRespostasParaInterface(pesquisaApp, questionario, pergunta.cdPergunta);
			deleteSecundariasDasSecundarias(resposta, listPesquisaResp);
			try {
				PesquisaRespAppService.getInstance().deleteAllByPerguntaPesquisa(pesquisaApp, pergunta);
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
			listPerguntas.removeElement(pergunta);
		}
	}
	
	private void processaPerguntasSecundarias(String tipoPerguntaMultipla, PerguntaResposta resposta) throws SQLException {
		switch (tipoPerguntaMultipla) {
		case Pergunta.TIPO_PERGUNTA_MULTIPLA:
			boolean respostaDiferente = respostaAnterior != null && !respostaAnterior.equals(resposta);
			if (respostaDiferente && ValueUtil.isNotEmpty(respostaAnterior.listPerguntasSecundarias)) {
				deleteRespostasPerguntasSecundarias(respostaAnterior);
			}
			if (ValueUtil.isEmpty(resposta.listPerguntasSecundarias)) return;
			adicionaPerguntaSecundaria(resposta);
			return;
		case Pergunta.TIPO_PERGUNTA_TEXTO:
			if (ValueUtil.isEmpty(edRespostaMemo.getValue() + "")) {
				deleteRespostasPerguntasSecundarias(resposta);
				return;
			}
			adicionaPerguntaSecundaria(resposta);
			return;
		case Pergunta.TIPO_PERGUNTA_NUMERICO:
			if (resposta != null && validaExpressaoInteger(resposta)) {
				adicionaPerguntaSecundaria(resposta);
				return;
			}
			deleteRespostasPerguntasSecundarias(resposta);
			return;
		case Pergunta.TIPO_PERGUNTA_DATE:
			if (resposta != null && validaExpressaoData(resposta)) {
				adicionaPerguntaSecundaria(resposta);
				return;
			}
			deleteRespostasPerguntasSecundarias(resposta);
			return;
			
		case Pergunta.TIPO_PERGUNTA_MULTI_CHECK:
			adicionaPerguntaSecundaria(resposta);
			return;
		default:
			break;
		}
	}
	
	private void deleteSecundariasDasSecundarias(PerguntaResposta resposta, Vector listPesquisaResp) throws SQLException {
		int size2 = listPesquisaResp.size();
		for (int j = 0; j < size2; j++) {
			PesquisaRespApp pesquisaResp = (PesquisaRespApp) listPesquisaResp.items[j];
			PerguntaResposta pergRespSec = PerguntaRespostaService.getInstance().findPerguntaRespostaByPesquisaRespApp(pesquisaResp);
			if (pergRespSec == null || ValueUtil.isEmpty(pergRespSec.cdPerguntasSecundarias)) continue;
			pergRespSec.listPerguntasSecundarias = PerguntaService.getInstance().findPerguntasSecundariasByPerguntaResposta(pergRespSec);
			deleteRespostasPerguntasSecundarias(pergRespSec);
		}
	}
	
	private boolean validaExpressaoInteger(PerguntaResposta resposta) {
		if (resposta == null || ValueUtil.isEmpty(resposta.dsExpressao)) return false;
		String[] exp = resposta.dsExpressao.split(";");
		if(exp.length < 1) return false;
		switch(exp[0]){
		case "<":
			return ValueUtil.getIntegerValue(edRespostaNumber.getValue()) < ValueUtil.getIntegerValue(exp[1]);
		case ">":
			return ValueUtil.getIntegerValue(edRespostaNumber.getValue()) > ValueUtil.getIntegerValue(exp[1]);
		case ">=":
			return ValueUtil.getIntegerValue(edRespostaNumber.getValue()) >= ValueUtil.getIntegerValue(exp[1]);
		case "<=":
			return ValueUtil.getIntegerValue(edRespostaNumber.getValue()) <= ValueUtil.getIntegerValue(exp[1]);
		}
		return false;
	}
	
	private boolean validaExpressaoData(PerguntaResposta resposta) {
		if (ValueUtil.isEmpty(resposta.dsExpressao)) {
			return false;
		}
		String[] exp = resposta.dsExpressao.split(";");
		if (exp.length < 1) {
			return false;
		}
		String[] dateArray = exp[1].trim().split("/");
		Date date = DateUtil.getDateValue(ValueUtil.getIntegerValue(dateArray[0]), ValueUtil.getIntegerValue(dateArray[1]), ValueUtil.getIntegerValue(dateArray[2]));
		switch(exp[0]){
		case "<":
			return edRespostaDate.getValue().isBefore(date);
		case ">":
			return edRespostaDate.getValue().isAfter(date);
		}
		return false;
	}

	private String getRespostaByEntidadeColuna() throws SQLException {
		String respostaByEntidadeCampo = ClienteService.getInstance().getClienteColumn(pesquisaApp.cdEmpresa, pesquisaApp.cdRepresentante, pesquisaApp.cdCliente, perguntaAtual.nmCampo);
		if (ValueUtil.valueEquals(Pergunta.TIPO_PERGUNTA_DATE, perguntaAtual.cdTipoPergunta)) {
			respostaByEntidadeCampo = respostaByEntidadeCampo.replaceAll("-","/");
		}
		return respostaByEntidadeCampo;
	}

	private void adicionaPerguntaSecundaria(PerguntaResposta resposta) throws SQLException {
		if (resposta == null || ValueUtil.isEmpty(resposta.hashPerguntasSecundarias)) return;
		int index = indexPergunta;
		String[] lista = resposta.cdPerguntasSecundarias.split(",");
		Pergunta pergunta;
		for(String cdPergunta : lista) {
			pergunta = (Pergunta) resposta.hashPerguntasSecundarias.get(cdPergunta);
			if (pergunta == null || listPerguntas.contains(pergunta)) continue;
			index++;
			listPerguntas.insertElementAt(pergunta, index);
			adicionandoPerguntaSecundaria = true;
		}
	}

	private boolean isPerguntaObrigatoria() throws SQLException {
		boolean flObrigatoria = PerguntaService.getInstance().isPerguntaObrigatoria(questionario.cdEmpresa, questionario.cdQuestionario, perguntaAtual.cdPergunta);
		if (isFlRespostaAutomaticaPelaEntidadeCampo() && ValueUtil.getBooleanValue(perguntaAtual.flSomenteLeitura) && ValueUtil.isEmpty(getRespostaByEntidadeColuna())) {
			flObrigatoria = false;
		}
		return flObrigatoria;
	}

}
