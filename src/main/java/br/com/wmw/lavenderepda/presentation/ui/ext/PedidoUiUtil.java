package br.com.wmw.lavenderepda.presentation.ui.ext;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.Equipamento;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilterDyn;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.EquipamentoUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.EquipamentoLavendere;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.ComboService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.EquipamentoLavendereService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.business.service.TipoEnderecoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.business.validation.ReservaEstoqueException;
import br.com.wmw.lavenderepda.presentation.ui.AdmSenhaDinamicaWindow;
import br.com.wmw.lavenderepda.presentation.ui.CadPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.ListDocNaoImpressoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ListFornecedoresNaoPositivadosWindow;
import br.com.wmw.lavenderepda.presentation.ui.ListItemComboVencidoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ListItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.RelItensDescQtdGrupoProdAutoWindow;
import br.com.wmw.lavenderepda.presentation.ui.RelItensDivergentesSemEstoqueWindow;
import br.com.wmw.lavenderepda.presentation.ui.RelNotificacaoItemWindow;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.sync.async.RetornoDadosAutomaticoRunnable;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.sys.Convert;
import totalcross.sys.Time;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;


public final class PedidoUiUtil {

	public static boolean enviaPedido(boolean isFromItemPedido, boolean showSucessErrorMessageSendPedido) {
		return enviaPedido(false, isFromItemPedido, showSucessErrorMessageSendPedido);
	}

	public static boolean enviaPedido(boolean isFromTelaCadPedidoPrimeiroPlano, boolean isFromItemPedido, boolean showSucessErrorMessageSendPedido) {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_ENVIANDO_DADOS);
		mb.popupNonBlocking();
		try {
			SessionLavenderePda.houveErroPedidosRestrito = false;
			ParamsSync paramsSync = HttpConnectionManager.getDefaultParamsSync();
			paramsSync.isFromTelaCadPedidoPrimeiroPlano = isFromTelaCadPedidoPrimeiroPlano;
			boolean houveErroEnvio = SyncManager.envieDados(paramsSync, SyncManager.getInfoAtualizacaoPedidos());
			//--
			if (SessionLavenderePda.envioPedidosBloqueadoHoraLimite) {
				if (UiUtil.showConfirmYesNoMessage(getMensagemBloqueioEnvioPedidos())) {
					AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
					senhaForm.setMensagem(Messages.PEDIDO_MSG_ENVIO_BLOQUEADO_HORALIMITE_SENHA);
					senhaForm.setChaveSemente(SenhaDinamica.SENHA_SISTEMA_ENVIAR_PEDIDO_BYHORA);
					if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
						UiUtil.showConfirmMessage("Envio de pedidos liberado com sucesso");
						SessionLavenderePda.autorizadoPorSenhaEnviarPedidosQualquerHorario = true;
						paramsSync.isFromTelaCadPedidoPrimeiroPlano = isFromTelaCadPedidoPrimeiroPlano;
						SyncManager.envieDados(paramsSync, SyncManager.getInfoAtualizacaoPedidos());
					} else {
						throw new Exception();
					}
				} else {
					throw new Exception();
				}
			}
			//--
			if (showSucessErrorMessageSendPedido) {
				if (!houveErroEnvio) {
					UiUtil.showSucessMessage(isFromItemPedido ? Messages.ITEMPEDIDO_MSG_PEDIDO_FECHADO_SUCESSO : FrameworkMessages.MSG_SYNC_INFO_ENVIO_CONCLUIDO);
					return true;
				} else {
					if (SessionLavenderePda.houveErroPedidosRestrito) {
						UiUtil.showWarnMessage(Messages.PRODUTO_RESTRITO_NAO_ENVIADO);
					} else {
						UiUtil.showWarnMessage(FrameworkMessages.SINCRONIZACAO_MSG_ERRO_ENVIO_PEDIDO_AUTOMATICO);
					}
					return false;
				}
			} else {
				return !houveErroEnvio;
			}
		} 
		catch (Throwable e) {
			UiUtil.showErrorMessage(Messages.SINCRONIZACAO_MSG_ENVIO_INCOMPLETO);
			return false;
		} finally {
			mb.unpop();
		}
	}

	public static void showRelInsercaoPedidoWindow(final Pedido pedido) {
		if (pedido.countItensSuceso > 0 || pedido.countItensErro > 0) {
			int result = UiUtil.showMessage(MessageUtil.getMessage(Messages.REL_INSERCAO_ITENS_RESUMO, new String[] { StringUtil.getStringValueToInterface(pedido.countItensSuceso),  StringUtil.getStringValueToInterface(pedido.countItensErro) }), new String[] { FrameworkMessages.BOTAO_OK, FrameworkMessages.BOTAO_DETALHES });
			if (result == 1) {
				RelNotificacaoItemWindow relInsercaoForm = new RelNotificacaoItemWindow(pedido.itensErros, false);
				relInsercaoForm.popup();
				//--
				pedido.itensErros = null;
				pedido.countItensErro = 0;
				pedido.countItensSuceso = 0;
			}
		}
	}

	public static void enviaPedidosClienteOutrasEmpresas(String cdSessao, Pedido pedidoAtual) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
		pedidoFilter.cdCliente = pedidoAtual.cdCliente;
		Vector pedidoList = PedidoService.getInstance().findAllByExampleOnlyPda(pedidoFilter);
		if (ValueUtil.isNotEmpty(pedidoList)) {
			for (int i = 0; i < pedidoList.size(); i++) {
				Pedido pedido = (Pedido) pedidoList.items[i];
				EnviaDadosThread.getInstance().enviaPedido(cdSessao, pedido);
				EnviaDadosThread.getInstance().enviaVisita(cdSessao, VisitaService.getInstance().findVisitaByPedido(pedido));
			}
		}
	}
	
	public static String getMensagemBloqueioEnvioPedidos() throws SQLException {
		List<String> horaLimiteList = LavenderePdaConfig.getHoraLimiteParaEnvioPedidosList();
		String horaLimiteInicial = null;
		String horaLimiteFinal = null;
		Time horaServidor = new Time(ValueUtil.getLongValue(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor)));
		if (horaLimiteList.size() == 1) {
			horaLimiteInicial = horaLimiteList.get(0);
		} else {
			int nuIntervalos = horaLimiteList.size() / 2;
			int nuPosicaoIntervalo = 0;
			try {
				for (int i = 0; i < nuIntervalos; i++) {
					Time hrLimiteInicial = getHoraLimite(horaLimiteList.get(nuPosicaoIntervalo), horaServidor);
					Time hrLimiteFinal = getHoraLimite(horaLimiteList.get(nuPosicaoIntervalo + 1), horaServidor);
					if (hrLimiteInicial.getTimeLong() <= horaServidor.getTimeLong() && horaServidor.getTimeLong() <= hrLimiteFinal.getTimeLong()) {
						horaLimiteInicial = horaLimiteList.get(nuPosicaoIntervalo);
						horaLimiteFinal = horaLimiteList.get(nuPosicaoIntervalo + 1);
						break;
					}
					nuPosicaoIntervalo += 2;
				}
			} catch (Throwable ex) {
				horaLimiteInicial = horaLimiteList.get(0);
			}
		}
		String horaAtual = TimeUtil.getTimeHHMM(horaServidor);
		String horaAtualPda = TimeUtil.getCurrentTimeHHMM();
		String mensagem = MessageUtil.getMessage(Messages.PEDIDO_MSG_ENVIO_BLOQUEADO_HORALIMITE, new String[] { horaLimiteInicial, horaAtual, horaAtualPda });
		if (ValueUtil.isNotEmpty(horaLimiteFinal)) {
			mensagem = MessageUtil.getMessage(Messages.PEDIDO_MSG_ENVIO_BLOQUEADO_PERIODOLIMITE, new String[] { horaLimiteInicial, horaLimiteFinal, horaAtual, horaAtualPda });
		}
		return mensagem;
	}
	
	private static Time getHoraLimite(String hora, Time time) {
		Time horaLimite = new Time(time.getTimeLong());
		horaLimite.hour = ValueUtil.getIntegerValue(StringUtil.split(hora, ':', 0));
	    horaLimite.minute = ValueUtil.getIntegerValue(StringUtil.split(hora, ':', 1));
	    horaLimite.second = 0;
	    return horaLimite;
	}
	
	public static boolean isPeriodoDeEnvioPedidoUltrapassado(Time timeAtual) {
		List<String> horaLimiteList = LavenderePdaConfig.getHoraLimiteParaEnvioPedidosList();
	    if (horaLimiteList.size() == 1) {
	    	return timeAtual.getTimeLong() > getHoraLimite(horaLimiteList.get(0), timeAtual).getTimeLong();
	    }
	    int nuValidacoesIntervalos = horaLimiteList.size() / 2;
	    boolean horaLimiteInicialUltrapassada = false;
	    boolean horaLimiteFinalUltrapassada = false;
	    int posicaoIntervalo = 0;
	    try {
	    	for (int i = 0; i < nuValidacoesIntervalos; i++) {
	    		horaLimiteInicialUltrapassada =  getHoraLimite(horaLimiteList.get(posicaoIntervalo), timeAtual).getTimeLong() <=  timeAtual.getTimeLong();
	 	    	horaLimiteFinalUltrapassada = timeAtual.getTimeLong() <= getHoraLimite(horaLimiteList.get(posicaoIntervalo + 1), timeAtual).getTimeLong();
	 	    	if (horaLimiteInicialUltrapassada && horaLimiteFinalUltrapassada) {
	 	    		return true;
	 	    	}
	 	    	posicaoIntervalo += 2;
	 		}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	    return horaLimiteInicialUltrapassada && horaLimiteFinalUltrapassada;	    
	}
	
	public static boolean isHoraLimiteExtrapolada(String horaLimite) {
		if (!isHoraLimiteDeEnvioParaDataDeEntregaDoPedidoLigado(horaLimite)) return false;
		
		Time horaAtualPda = TimeUtil.getCurrentTime();
		Time horaLimiteDeEnvioParaDataDeEntregaDoPedido = getHoraLimite(horaLimite, horaAtualPda);
		return TimeUtil.getMillisRealBetween(horaAtualPda, horaLimiteDeEnvioParaDataDeEntregaDoPedido) > 0;
	}

	public static boolean isHoraLimiteDeEnvioParaDataDeEntregaDoPedido(Pedido pedido, int diaSemana) throws SQLException {
		return isHoraLimiteExtrapolada(getHoraLimiteDeEnvio(pedido, diaSemana));
	}
	
	public static boolean isHoraLimiteDeEnvioParaDataDeEntregaDoPedidoLigado(String horaLimiteDeEnvio) {
		try {
			int hora = Convert.toInt(StringUtil.split(horaLimiteDeEnvio, ':', 0));
			int minuto = Convert.toInt(StringUtil.split(horaLimiteDeEnvio, ':', 1));
			return hora >= 0 && hora <= 23 && minuto >= 0 && minuto <= 59;
		} catch (Throwable e) {
			return false;
		}
	}

	public static String getHoraLimiteDeEnvio(Pedido pedido, int diaSemana) throws SQLException {
		if (pedido == null) return "";
		if (ValueUtil.isNotEmpty(pedido.cdEmpresa) && ValueUtil.isNotEmpty(pedido.cdRepresentante) && ValueUtil.isNotEmpty(pedido.cdCliente) && ValueUtil.isNotEmpty(pedido.cdEnderecoCliente)) {
			String horaLimiteEnvio = TipoEnderecoService.getInstance().findHoraLimiteEnvioPedidoPor(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente);
			if (ValueUtil.isNotEmpty(horaLimiteEnvio)) {
				return horaLimiteEnvio;
			}
		}
		return LavenderePdaConfig.horaLimiteDeEnvioParaDataDeEntregaDoPedido(diaSemana, pedido.getCliente().isPessoaFisica());
	}
	
	public static boolean validateValorFreteManual(Pedido pedido) throws SQLException {
		if ((!LavenderePdaConfig.isPermiteInserirFreteManual() && !LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) || LavenderePdaConfig.isPermiteValorFreteManualMaiorQueValorPedido())
			return true; 

		if (pedido.vlFrete < 0) {
			UiUtil.showErrorMessage(Messages.FRETE_MSG_ERRO_MANUAL_FRETE_INVALIDO);
			return false;
		} else if (pedido.vlFrete > pedido.vlTotalItens) {
			UiUtil.showErrorMessage(Messages.FRETE_MSG_ERRO_MANUAL_FRETE_INVALIDO_MAIOR_QUE_VLTOTALPEDIDO);
			return false;
		}
		return true;
	}
	
	public static PopUpSearchFilterDyn getPopUpSearchClienteEntrega(Hashtable hashComponentes) {
		Object popUpClienteEntrega = hashComponentes.get(Pedido.NMCOLUNA_CDCLIENTEENTREGA);
		return popUpClienteEntrega instanceof PopUpSearchFilterDyn ? (PopUpSearchFilterDyn) popUpClienteEntrega : null; 
	}
	
	public static boolean isLiberaSenhaDiaEntregaPedidoWindow(Pedido pedido, String msg) throws SQLException {
		AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
		senhaForm.setMensagem(msg);
		senhaForm.setData(pedido.dtEntrega);
		senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_DIA_ENTREGA_PEDIDO);
		boolean senhaValida = senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA;
		if (senhaValida) {
			pedido.cdUsuarioLibEntrega = senhaForm.cdUsuarioLiberado;
		}
		return senhaValida;
	}
	
	public static boolean isUsaCondicaoPgtoTipoFretePedidoRelacionado(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() && pedido.isPedidoBonificacao() && PedidoService.getInstance().isPedidoComBonificacaoRelacionada(pedido);
	}
	
	public static double getPctDescEspecial(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaDescontoPonderadoPedido && LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda && pedido.isPedidoBonificacao() && !pedido.getTipoPedido().isFlTipoCreditoFrete() && !pedido.getTipoPedido().isFlTipoCreditoCondicao()) {
			Pedido pedidoVendaRelacionado = PedidoService.getInstance().getPedidoRelBonificacao(pedido);
			if (pedidoVendaRelacionado != null) {
				return pedidoVendaRelacionado.vlPctDesconto;
			}
		} 
		return pedido.vlPctDesconto;
	}
	
	public static double getValorCreditoDisponivel(Pedido pedido) throws SQLException {
		Pedido pedidoRel = PedidoService.getInstance().getPedidoRelBonificacao(pedido);
		return getValorCreditoDisponivel(pedido, pedidoRel);
	}

	public static double getValorCreditoDisponivel(Pedido pedidoOriginal, Pedido pedidoRel) throws SQLException {
		pedidoOriginal.vlCreditoDisponivelConsumo = 0d;
		if (pedidoRel == null) return  0d;
		
		if (pedidoOriginal.getTipoPedido().isFlTipoCreditoCondicao()) {
			pedidoOriginal.vlCreditoDisponivelConsumo = pedidoRel.vlTotalCreditoCondicao;
		} else if (pedidoOriginal.getTipoPedido().isFlTipoCreditoFrete()) {
			pedidoOriginal.vlCreditoDisponivelConsumo = pedidoRel.vlTotalCreditoFrete;
		}
		return pedidoOriginal.vlCreditoDisponivelConsumo;
	}
	
	public static boolean isConfirmaPedidoDtEntregaFinalSemanaFeriado(Pedido pedido) throws SQLException {
		if (!PedidoService.getInstance().isPedidoDtEntregaFinalSemanaFeriado(pedido.dtEntrega)) return true;
		
		if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
			if (ValueUtil.valueEquals(pedido.dtEntrega, pedido.dtEntregaLiberada)) return true;
			
			if (!isLiberaSenhaDiaEntregaPedidoWindow(pedido, Messages.PEDIDO_MSG_CONFIRMACAO_LIBERACAO_DTENTREGA)) return false;
			
			pedido.dtEntregaLiberada = pedido.dtEntrega;
			UiUtil.showInfoMessage(Messages.PEDIDO_MSG_SUCESSO_LIBERACAO_SENHA_DATA_ENTREGA);
			return true;
		} else if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMACAO_LIBERACAO_DTENTREGA)) {
			pedido.flLiberadoEntrega = ValueUtil.VALOR_SIM;
			return true;
		}
		return false;
	}
	
	public static Image getEmptyIcon() {
		int heigthContainerIcons = (int)(UiUtil.getControlPreferredHeight() * 1.2);
		int alturaLargura = (int)(heigthContainerIcons * 0.70);
		return UiUtil.getEmptyImage(alturaLargura, alturaLargura);
	}
	
	public static boolean showPopupUsaVerbaReplicacaoPedido(Pedido pedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaConfirmacaoVerbaReplicacaoPedido() && pedido.isIgnoraControleVerba()) return false;
		
		double vlVerba = LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 ? ValueUtil.round(pedido.vlVerbaPedido) +  pedido.vlDesconto * -1 : ValueUtil.round(pedido.vlVerbaPedido);
		if (vlVerba * -1 <= 0) return false;
		
		return UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_CONFIRMACAO_PEDIDO_SUGESTAO, StringUtil.getStringValuePositivo(vlVerba)));
	}
	
	public static String getTitlePopupAvisoSugestaoVendaObrigatoria(String flTipoSugestaoVenda) {
		String title = FrameworkMessages.TITULO_MSG_AVISO + "! ";
		return SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE.equals(flTipoSugestaoVenda) ? title + Messages.PEDIDO_LABEL_SUGESTAO_VENDAS : title + Messages.PEDIDO_LABEL_SUGESTAO_VENDAS_COM_QTDE;
	}

	public static boolean showPopupLiberacaoSenhaSugestaoVendaObrigatoria(Pedido pedido, Vector sugestoesVigentes, String flTipoSugestaoVenda, boolean outrasEmpresas) throws SQLException {
		String title = getTitlePopupAvisoSugestaoVendaObrigatoria(flTipoSugestaoVenda);
		String msg = outrasEmpresas ?  MessageUtil.getMessage(Messages.VALIDACAO_ENVIAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA_OUTRAS_EMPRESAS, pedido.getCliente().toString()) : Messages.VALIDACAO_FECHAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA;
		if (!UiUtil.showConfirmYesNoMessage(title, msg + " " + Messages.MSG_LIBERAR_SENHA)) return false;
		
		AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
		senhaForm.setMensagem(Messages.SUGESTAO_VENDA_OBRIGATORIA_MSG_SENHA);
		senhaForm.setCdCliente(pedido.getCliente().cdCliente);
		senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_SUGESTADO_VENDA);
		if (!senhaForm.show()) return false;
		
		pedido.flSugestaoVendaLiberadoSenha = ValueUtil.VALOR_SIM;
		ConfigInternoService.getInstance().saveConfigInternoSugestaoVendaObrigatoria(sugestoesVigentes, pedido.getCliente(), flTipoSugestaoVenda);
		return true;
		
	}
	
	public static String msgRecebimentoOuProcessamentoDados(boolean recebimento, boolean nfe, boolean boleto, boolean erpDif) {
		StringBuffer msgRecebendoDados = new StringBuffer();
		msgRecebendoDados.append(recebimento ? Messages.PEDIDO_MSG_RECEBENDO_DADOS : Messages.PEDIDO_MSG_PROCESSANDO_DADOS);
		if (nfe) {
			msgRecebendoDados.append(Messages.PEDIDO_MSG_RECEBENDO_DADOS_NFE).append(",");
		}
		if (boleto) {
			if (!erpDif && nfe) {
				msgRecebendoDados.deleteCharAt(msgRecebendoDados.length() - 1);
				msgRecebendoDados.append(" e");
			}
			msgRecebendoDados.append(Messages.PEDIDO_MSG_RECEBENDO_DADOS_BOLETO).append(",");
		}
		if (erpDif) {
			if (boleto || nfe) {
				msgRecebendoDados.deleteCharAt(msgRecebendoDados.length() - 1);
				msgRecebendoDados.append(" e");
			}
			msgRecebendoDados.append(recebimento ? Messages.PEDIDO_MSG_RECEBENDO_DADOS_PEDIDOERP : Messages.PEDIDO_MSG_PROCESSANDO_DADOS_PEDIDOERP).append(",");
		}
		msgRecebendoDados.deleteCharAt(msgRecebendoDados.length() - 1);
		msgRecebendoDados.append(".");
		return msgRecebendoDados.toString();
	}
	
	public static boolean isPossuiConexao() {
		try {
			return SyncManager.isConexaoPdaDisponivel();
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean isCondPagtoVerificado(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.emiteAlertaUsuarioVerificacaoCondPagto && isPedidoUsaCondicaoPagamento(pedido)) {
			boolean resultadoVerificacaoCondPagto = UiUtil.showWarnConfirmYesNoMessage(Messages.PEDIDO_MSG_VERIFICACAO_CONDPAGTO);
			return !resultadoVerificacaoCondPagto;
		}
		return false;
	}
	
	public static boolean isPedidoSemCliente(Pedido pedido) throws SQLException {
		return (LavenderePdaConfig.isPermitePedidoNovoCliente() && pedido.getCliente().isNovoCliente()) || pedido.getCliente().isClienteDefaultParaNovoPedido();
	}
	
	public static boolean validateReabrirPedidoUI(Pedido pedido) throws SQLException {
		try {
			PedidoService.getInstance().validateReabrirPedidoUI(pedido);
		} catch (ReservaEstoqueException ex) {
			if (LavenderePdaConfig.isNaoPermiteFecharPedidoSemReservaDeEstoque()
					|| LavenderePdaConfig.isUsaReservaEstoqueCorrente()) {
				UiUtil.showErrorMessage(Messages.PEDIDO_MSG_VALIDACAO_ERRO_INATIVACAO_RESERVA_ESTOQUE);
				return false;
			}
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMACAO_ERRO_INATIVACAO_RESERVA_ESTOQUE)) {
				pedido.ignoraInativacaoReservaEstoque = true;
				return validateReabrirPedidoUI(pedido);
			} 
			return false;
		}
		return true;
	}
	
	public static boolean isConfirmPedidoAbaixoRentMinima(Pedido pedido) throws SQLException {
		if (!pedido.isPedidoBonificacao() && pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false) < RentabilidadeFaixaService.getInstance().getVlPctRentabilidadeFaixaMinima(pedido.getRentabilidadeFaixaList())) {
			return UiUtil.showConfirmYesNoMessage(Messages.RENTABILDDADEFAIXA_MSG_PEDIDO_RENTABILIDADE_MINIMA);
		}
		return true;
	}
	
	public static boolean positivacaoItensByFornecedores(CadPedidoForm cadPedidoForm, Pedido pedido, boolean isEnabled) throws SQLException {
		if (!LavenderePdaConfig.isAvisaUsuarioPositivacaoFornecedor() && ValueUtil.isEmpty(pedido.itemPedidoList) || !isEnabled) return false;
			
		Vector fornecedorList = PedidoService.getInstance().getFornecedoresNaoPositivadosList(pedido);
		if (ValueUtil.isEmpty(fornecedorList)) return false;
		
		ListFornecedoresNaoPositivadosWindow listFornecedoresNaoPositivadosWindow = new ListFornecedoresNaoPositivadosWindow(pedido);
		listFornecedoresNaoPositivadosWindow.popup();
		return !listFornecedoresNaoPositivadosWindow.isContinuarClick;
	}
	
	public static boolean executaValidacoesIniciais(Pedido pedido, boolean resultado, boolean showMessageConfirmClosePedido) throws SQLException {
		if (LavenderePdaConfig.emiteAlertaUsuarioVerificacaoCondPagto && isPedidoUsaCondicaoPagamento(pedido)
				&& !resultado && showMessageConfirmClosePedido && !LavenderePdaConfig.isUsaDtAberturaEFundacao()) {
			resultado = UiUtil.showWarnConfirmYesNoMessage(Messages.PEDIDO_MSG_VERIFICACAO_CONDPAGTO);
			if (!resultado) {
				return false;
			}
			resultado = true;
		}
		if (LavenderePdaConfig.usaFotoPedidoNoSistema) {
			if (LavenderePdaConfig.usaVerificacaoFotoPedidoPresentePedido == 2 && !pedido.verificadoSePosuiFoto) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_SEM_FOTO_CONFIRM_REPRESENTANTE_CAD)) {
					return false;
				} else {
					pedido.verificadoSePosuiFoto = true;
				}
				resultado = true;
			}
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			if (LavenderePdaConfig.usaControleRentabilidadePorFaixa == 1) {
				if (!PedidoUiUtil.isConfirmPedidoAbaixoRentMinima(pedido)) {
					return false;
				}
			//Avisa rentabilidade abaixo de esperado
			} else if (LavenderePdaConfig.isAvisoRentabilidadeItemAbaixoEsperadoFechandoPedido()) {
				if (LavenderePdaConfig.isAvisoRentabilidadeItemAbaixoMinimo() && ItemTabelaPrecoService.getInstance().isAlgumItemRentabilidadeAbaixoMinimo(pedido)) {
					UiUtil.showWarnMessage(Messages.PEDIDO_MSG_PCT_RENTABILIDADE_ABAIXO_MINIMO);
				} else if (LavenderePdaConfig.isAvisoRentabilidadeItemAbaixoEsperadoQualquerNivel() && ItemTabelaPrecoService.getInstance().isAlgumItemRentabilidadeAbaixoEsperado(pedido)) {
					UiUtil.showWarnMessage(Messages.PEDIDO_MSG_PCT_RENTABILIDADE_ABAIXO_ESPERADO);
				}
			}
		}
		if (LavenderePdaConfig.isExibeComboMenuInferior() && ComboService.getInstance().isPedidoComComboForaVigencia(pedido)) {
			if (UiUtil.showConfirmMessage(Messages.COMBO_NOME_ENTIDADE, Messages.MSG_COMBO_VENCIDA, new String[] {FrameworkMessages.BOTAO_FECHAR, Messages.BOTAO_VER_COMBO}) == 1) {
				new ListItemComboVencidoWindow(pedido).popup();
			}
			return false;
		}
		return true;
	}

	public static boolean isPedidoUsaCondicaoPagamento(Pedido pedido) throws SQLException {
		return (!pedido.isPedidoTroca() && !pedido.isPedidoBonificacao() && !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamento()) || (pedido.isPedidoBonificacao() && LavenderePdaConfig.usaCondicaoPagamentoPedidoBonificacao);
	}
	
	public static void showListItensDivergentesDescComissaoGrupo(CadPedidoForm cadPedidoForm) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByDescComissao(true);
		cadPedidoForm.show(listItemPedidoForm);
	}

	public static void showListItensDivergentesDescQtdGrupo(CadPedidoForm cadPedidoForm) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByDescontoGrupo(true);
		cadPedidoForm.show(listItemPedidoForm);
	}

	public static void showListItensDivergentesDescPacote(CadPedidoForm cadPedidoForm) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByDescontoPacote(true);
		cadPedidoForm.show(listItemPedidoForm);
	}

	public static void showListItensDivergentesCalculoVinco(CadPedidoForm cadPedidoForm) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByCalculoVinco(true);
		cadPedidoForm.show(listItemPedidoForm);
	}

	public static void showListItensDivergentesDescProgQtd(CadPedidoForm cadPedidoForm) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByDescProgQtd(true);
		cadPedidoForm.show(listItemPedidoForm);
	}

	public static void showListItensDescQtdGrupoAplicadoAuto(CadPedidoForm cadPedidoForm) throws SQLException {
		RelItensDescQtdGrupoProdAutoWindow relItensDescQtdGrupoProdAplicadoAutoWindow = new RelItensDescQtdGrupoProdAutoWindow(cadPedidoForm, cadPedidoForm.getPedido(), Messages.DESCCOMIGRUPO_ITENS_DESC_AUTO);
		relItensDescQtdGrupoProdAplicadoAutoWindow.popup();
	}

	public static void showListItensDivergentesRestricaoVendaUn(CadPedidoForm cadPedidoForm, TipoPedido tipoPedidoNew) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByRestricaoVendaUn(true);
		listItemPedidoForm.tipoPedidoItensNaoConforme = tipoPedidoNew;
		cadPedidoForm.show(listItemPedidoForm);
	}

	public static void showListItensDivergentesTipoPedido(CadPedidoForm cadPedidoForm, TipoPedido tipoPedidoNew) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNaoConformeByTipoPedido(true);
		listItemPedidoForm.tipoPedidoItensNaoConforme = tipoPedidoNew;
		cadPedidoForm.show(listItemPedidoForm);
	}

	public static void showListItensRenegociarConsumoVerba(CadPedidoForm cadPedidoForm) throws SQLException {
		ListItemPedidoForm listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensNegociacaoConsumoVerba(true);
		cadPedidoForm.show(listItemPedidoForm);
	}
	
	public static boolean validaDataUltimoRecebimentoDados() throws SQLException {
		try {
			String dataHoraUltRecebimentoString = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraUtimoRecebimentoDados);
			if (ValueUtil.isEmpty(dataHoraUltRecebimentoString)) {
				throw new InvalidDateException();
			}
			Date dataHoraUltRecebimento = new Date(TimeUtil.getTime(dataHoraUltRecebimentoString));
			if (new Date().isAfter(dataHoraUltRecebimento)) {
				throw new InvalidDateException();
			}
		} catch (InvalidDateException ex) {
			UiUtil.showWarnMessage(Messages.NECESSARIO_RECEBER_ATUALIZACAO_DADOS);
			return false;
		}
		return true;
	}
	
	public static void retornaMensagem(Pedido pedido, double vlTotalPedidoAnterior, Vector itensErro) {
		if (ValueUtil.isNotEmpty(itensErro)) {
			RelNotificacaoItemWindow relProdutoErroWindow = new RelNotificacaoItemWindow(Messages.TITULO_REL_ITENS_ERRO_RECALCULO, itensErro, false, Messages.FECHAR_PEDIDO_ITENS_ERRO_RECALCULO);
			relProdutoErroWindow.popup();
		} else if (vlTotalPedidoAnterior == pedido.vlTotalPedido) {
			UiUtil.showInfoMessage(Messages.PEDIDO_RECALCULADO_COM_SUCESSO_SEM_ALTERACAO);
		} else {
			Object[] params = {StringUtil.getStringValueToInterface(vlTotalPedidoAnterior), StringUtil.getStringValueToInterface(pedido.vlTotalPedido)};
			String mensagem = MessageUtil.getMessage(Messages.PEDIDO_RECALCULADO_COM_SUCESSO_COM_ALTERACAO, params);
			UiUtil.showInfoMessage(mensagem);
		}
	}
	
	public static double getEstoqueProdutoServidor(String cdProduto, String cdLocalEstoque) throws Exception {
		ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
		ps.nuMaxTentativas = 1;
		LavendereWeb2Tc lwWeb2Tc = new LavendereWeb2Tc(ps);
		String estoqueServidorAndUpdatePda = lwWeb2Tc.getEstoqueServidorAndUpdatePda(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class), cdProduto, cdLocalEstoque);
		return ValueUtil.getDoubleValue(estoqueServidorAndUpdatePda);
	}
	
	public static void logFechamentoEquipamento(Pedido pedido) {
		try {
			Equipamento equipamentoFilter = new EquipamentoLavendere();
			equipamentoFilter.cdEquipamento = EquipamentoUtil.getCdEquipamento();
			Equipamento equipamento = (Equipamento) EquipamentoLavendereService.getInstance().findByRowKey(equipamentoFilter.getRowKey());
			equipamento = equipamento != null ? equipamento : Equipamento.instanceNewEquipamento();
			
			StringBuilder msg = new StringBuilder();
			msg.append("Pedido ").append(pedido.nuPedido);
			msg.append(" fechado com o equipamento (");
			addDescricaoEquipamento(msg, "cdEquipamento", equipamento.cdEquipamento);
			addDescricaoEquipamento(msg, "nuImei", equipamento.nuImei);
			addDescricaoEquipamento(msg, "nuSerialNumber", equipamento.nuSerialNumber);
			msg.append(") do usuario ").append(Session.getCdUsuario());
			LogPdaService.getInstance().info(LogPda.LOG_FECHAMENTO_EQUIPAMENTO, msg.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private static void addDescricaoEquipamento(StringBuilder msg, String campo, String valor) {
		msg.append(campo).append(" = ");
		if (ValueUtil.isNotEmpty(valor)) {
			msg.append(valor);
		}
		msg.append(", ");
	}
	
	public static void enviaRecebeDadosPedido() throws SQLException {
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
			if (BaseUIForm.isListDadosRecebidosWindowNull()) {
				BaseUIForm.setListDadosRecebidosWindow(new ListDocNaoImpressoWindow());
			}
			boolean testeConexao = false;
			boolean tentarNovamente = false;
			do {
				testeConexao = SyncManager.isConexaoPdaDisponivel();
				if (!testeConexao) {
					tentarNovamente = UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SEM_CONEXAO_SERVIDOR);
				}
			} while (!testeConexao && tentarNovamente);
			if (testeConexao) {
				EnviaDadosThread.getInstance().addQueue();
				RetornoDadosAutomaticoRunnable.addQueue();
			} else {
				UiUtil.showInfoMessage(Messages.PEDIDO_MSG_TENTATIVA_RECEBIMENTO_DADOS_IMPRESSAO);
			}
		} else {
			EnviaDadosThread.getInstance().addQueue();
		}
	}
	
	public static void enviaConsignacao() {
		new AsyncUIControl() {

			boolean houveErroEnvio = false;
			@Override
			public void execute() {
				try {
					houveErroEnvio = SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync(), PedidoConsignacaoService.getInstance().getSynInfoTable());
				} catch (Throwable e) {
					houveErroEnvio = false;
					LogSync.error(e.getMessage());
				}
			}

			public void after() {
				if (houveErroEnvio) {
					UiUtil.showWarnMessage(Messages.PEDIDO_CONSIGNACAO_MSG_ERRO_ENVIO);
					return;
				}
				UiUtil.showSucessMessage(Messages.PEDIDO_CONSIGNACAO_MSG_ENVIADO_SUCESSO);
			}
			
		}.open();
	}

	public static void mostraAvisosAposFechamentoPedidoPelaLista(Vector pedidoList) {
		Vector tipoMensagemList = PedidoService.getInstance().getTiposMensagensAposFechamentoPedidoPelaLista(pedidoList);
		int size = tipoMensagemList.size();
		for (int i = 0; i < size; i++) {
			switch ((int)tipoMensagemList.elementAt(i)) {
			case 1:
				UiUtil.showInfoMessage(Messages.FECHAR_PEDIDO_LOTE_PEDIDO_PENDENTE);
				break;
			case 2:
				UiUtil.showInfoMessage(Messages.BONIFCFG_AVISO_PEDIDOS_CONVERTIDOS_BONIFICACAO);
				break;
			default:
				break;
			}
		}
	}

}
