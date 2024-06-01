package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.service.LogSyncBackgroundService;
import br.com.wmw.framework.presentation.ui.BasePopUpWindow;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.AgendaCadastroService;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.ClienteEndAtuaService;
import br.com.wmw.lavenderepda.business.service.ContatoService;
import br.com.wmw.lavenderepda.business.service.FotoClienteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ReorganizarDadosService;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Rect;

public class AdmDeleteDadosAntigosWindow extends BasePopUpWindow {

	public AdmDeleteDadosAntigosWindow() {
		super(Messages.REORGANIZARDADOS_TABELAS);
		//--
		Rect r = getRect();
		r.height = Font.getDefaultFontSize() * 6;
		r.width = (r.width * 10) / 12;
		r.x = CENTER;
		r.y = CENTER;
		setRect(r);
		//--
		add(new LabelValue(Messages.REORGANIZARDADOS_MSG_AGUARDE), CENTER, ((r.height * 1) / 4) + 5);
		add(new LabelValue(Messages.REORGANIZARDADOS_MSG_EXCLUINDO_DADOS_ANTIGOS), CENTER, ((r.height * 1) / 2) + 7);
	}

	public void show(boolean chamadaManualPeloMenu) {
		try {
			ReorganizarDadosService reorganizarDadosService = new ReorganizarDadosService();
			//Executa apenas uma vez por semana, ou se a chamada for feita pelo menu
			if (reorganizarDadosService.isReorganizarDadosNecessario() || chamadaManualPeloMenu) {
//				popupNonBlocking();
				try {
					//***************************************** ATENCAO ****************************************************//
					//**** Em alguns casos de exclusão, é necessário atualizar o carimbo (ver o delete de Consignacao) *****//
					//******************************************************************************************************//
					reorganizarDadosService.deleteRecadosAntigos();
					PedidoService.getInstance().deletePedidosPdaByPedidosErp();
					ContatoService.getInstance().deleteContatosPdaByContatosErp();
					FotoClienteService.getInstance().deleteFotoClienteByFotoClienteErp();
					FotoClienteService.getInstance().deleteAllFotosExcluidas();
					FotoClienteService.getInstance().deleteFotoClienteLimitePrazo();
					ClienteEndAtuaService.getInstance().deleteClienteEndAtuaAntigos();
					LogSyncBackgroundService.getInstance().deleteLogSyncBackgroundAntigos(LavenderePdaConfig.getNuDiasPermanenciaLogSyncBackground());
					reorganizarDadosService.deletePedidosAntigos();
					reorganizarDadosService.deletePedidosPerdidosAntigos();
					reorganizarDadosService.deleteNovidadesAntigas();
					reorganizarDadosService.deletePesquisasMercadoAntigas();
					reorganizarDadosService.deleteConsignacaoAndItensConsigAntigos();
					reorganizarDadosService.updateNuSegundosUltimoReorganizarDados();
					reorganizarDadosService.deleteClienteAtuaAntigos();
					reorganizarDadosService.deletaVisitasAntigas();
					reorganizarDadosService.deleteResumosDiaAntigos();
					reorganizarDadosService.deleteCargasPedidoAntigos();
					reorganizarDadosService.deleteLogWGps();
					reorganizarDadosService.deletePedidoConsignacaoLimitePrazo();
					reorganizarDadosService.deleteNovoClienteAnaAntigo();
					reorganizarDadosService.deleteRemessasEstoque();
					reorganizarDadosService.deleteValorizacaoProdutoAntigos();
					reorganizarDadosService.deleteRegistroNovoClienteAntigos();
					reorganizarDadosService.deleteDepositoBancarioEFechamentoDiarioAntigos();
					reorganizarDadosService.deleteEstoqueRepAntigos();
					reorganizarDadosService.deleteCargaProduto();
					reorganizarDadosService.deleteLogAppEnviadoServidor();
					reorganizarDadosService.deleteVerbaSaldoForaVigencia();
					reorganizarDadosService.deleteRegistrosProspect();
					reorganizarDadosService.deletePdfPedido();
					reorganizarDadosService.deletePesquisaMercadoProdutoConcorrentesAntigas();
					reorganizarDadosService.limparNuOrdemManualAgendaVisita();
					reorganizarDadosService.deleteNovidadesSolAutorizacaoAntigas();
					reorganizarDadosService.deletePdfPedido();
					if (LavenderePdaConfig.usaDataLimiteValidadeAgendaVisita || LavenderePdaConfig.permiteIndicarAgendaVisitaValidaParaDataUnica) {
						reorganizarDadosService.executaLimpezaAgendasDataFinalUltrapassada();
					}
					//***************************************** ATENCAO ****************************************************//
					//**** Em alguns casos de exclusão, é necessário atualizar o carimbo (ver o delete de Consignacao) *****//
					//******************************************************************************************************//
				} finally {
//					unpop();
				}
				if (chamadaManualPeloMenu) {
					UiUtil.showSucessMessage(Messages.REORGANIZARDADOS_MSG_DADOS_REORGANIZADOS_COM_SUCESSO);
				}
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
	}

	public void onWindowEvent(Event event) throws java.sql.SQLException {
	}
}
