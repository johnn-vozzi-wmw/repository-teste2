package br.com.wmw.lavenderepda.sync;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncExecution;
import br.com.wmw.framework.async.AsyncWindow;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.business.service.FotoClienteErpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoEmpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import totalcross.io.File;
import totalcross.io.IOException;

public class SyncCargaInicialFotos implements AsyncExecution {

	private LavendereWeb2Tc web2tc;
	
	public SyncCargaInicialFotos(LavendereWeb2Tc lavendereWeb2Tc) {
		web2tc = lavendereWeb2Tc;
	}

	public void processaCargaInicialFotos() throws SQLException {
		LogSyncTimer timer = new LogSyncTimer("Início recebimento carga inicial foto", "Fim recebimento carga inicial foto").newLogOnFinish();
		try {
			if (isRecebeFoto()) {
				execCargaInicialFotos();
			}
		} catch (Throwable e) {
			LogSync.error(e.getMessage());
			ExceptionUtil.handle(e);
		} finally {
			timer.finish();
		}
		
	}

	private boolean isRecebeFoto() throws SQLException, IOException {
		boolean existeConfigInternoCargaPendente = existeConfigInternoCargaPendente();
		return (existeFotoProdutoCargaInicial() && (existeConfigInternoCargaPendente || naoExisteDiretorio(Produto.getPathImg()))) 
				|| (existeFotoProdutoEmpCargaInicial() && (existeConfigInternoCargaPendente || naoExisteDiretorio(Produto.getPathImg()))) 
				|| (existeFotoClienteCargaInicial() && (existeConfigInternoCargaPendente || naoExisteDiretorio(Cliente.getPathImg())))
				|| (existeDivulgaInfoCargaInicial() && (existeConfigInternoCargaPendente || naoExisteDiretorio(DivulgaInfo.getPathImg())))
				|| (existeFotoProdutoGradeCargaInicial() && (existeConfigInternoCargaPendente || naoExisteDiretorio(FotoProdutoGrade.getPathImg())));
	}
	
	public static boolean existeConfigInternoCargaPendente() throws SQLException {
		return ValueUtil.isNotEmpty(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.CARGAINICIALFOTOSPENDENTE));
	}
	
	public boolean confirmRecebimentoCargaInicial() {
		int dialogResult = getResultConfirmWindowMessage();
		if (dialogResult == 0) {
			salvaConfigSucesso();
			updateReceberFotosAoFalharRecebimentoCargaInicial();	
		}
		return dialogResult == 2;
	}

	private int getResultConfirmWindowMessage() {
		final String[] opcoes = new String[] {FrameworkMessages.BOTAO_NAO, FrameworkMessages.BOTAO_MAISTARDE, FrameworkMessages.BOTAO_SIM};
		return UiUtil.showConfirmMessage(FrameworkMessages.TITULO_MSG_CONFIRMACAO, Messages.FOTOS_CARGA_INICIAL_DESEJA_RECEBER, opcoes);
	}

	private void execCargaInicialFotos() {
		try {
			LogSync.logSection(Messages.FOTOS_CARGA_INICIAL_RECEBIMENTO_INICIO);
			createDirs();
			boolean recebeuFotos = recebeFotos();
			if (recebeuFotos) {
				LogSync.sucess(Messages.FOTOS_CARGA_INICIAL_RECEBIMENTO_FIM);
				updateReceberNovamenteAtualizacoesDePossiveisFotosSobrescritas();
			} else {
				LogSync.warn(Messages.FOTOS_CARGA_INICIAL_RECEBIMENTO_VAZIO);
				updateReceberFotosAoFalharRecebimentoCargaInicial();
			}
			salvaConfigSucesso();
			removeBtAlertAoReceberPelaTelaSincronizacao();
		} catch (Exception e) {
			LogSync.error(Messages.FOTOS_CARGA_INICIAL_ERRO + " " + e.getMessage());
			ExceptionUtil.handle(e);
		}
	}

	private boolean recebeFotos() throws Exception {
		boolean recebeuFotos = false;
		try {
			recebeuFotos = web2tc.recebeFotosCargaInicial();
		} catch (Exception e) {
			LogSync.error(e.getMessage());
		}
		return recebeuFotos;
	}
	

	private void updateReceberNovamenteAtualizacoesDePossiveisFotosSobrescritas() {
		try {
			if (existeFotoProdutoCargaInicial()) {
				FotoProdutoService.getInstance().updateResetReceberFotosNovamente();
			}
			if (existeFotoProdutoEmpCargaInicial()) {
				FotoProdutoEmpService.getInstance().updateResetReceberFotosNovamente();
			}
			if (existeFotoClienteCargaInicial()) {
				FotoClienteErpService.getInstance().updateResetReceberFotosNovamente();
			}
			if (existeDivulgaInfoCargaInicial()) {
				DivulgaInfoService.getInstance().updateResetReceberFotosNovamente();
			}
			if (existeFotoProdutoGradeCargaInicial()) {
				FotoProdutoGradeService.getInstance().updateResetReceberFotosNovamente();
			}
		} catch (SQLException e) {
			LogSync.error(Messages.FOTOS_CARGA_INICIAL_ERRO);
			ExceptionUtil.handle(e);
		}
	}
	
	private void updateReceberFotosAoFalharRecebimentoCargaInicial() {
		try {
			if (existeFotoProdutoCargaInicial()) {
				FotoProdutoService.getInstance().updateReceberFotosAoFalharRecebimentoCargaInicial();
			}
			if (existeFotoProdutoEmpCargaInicial()) {
				FotoProdutoEmpService.getInstance().updateReceberFotosAoFalharRecebimentoCargaInicial();
			}
			if (existeFotoClienteCargaInicial()) {
				FotoClienteErpService.getInstance().updateReceberFotosAoFalharRecebimentoCargaInicial();
			}
			if (existeDivulgaInfoCargaInicial()) {
				DivulgaInfoService.getInstance().updateReceberFotosAoFalharRecebimentoCargaInicial();
			}
			if (existeFotoProdutoGradeCargaInicial()) {
				FotoProdutoGradeService.getInstance().updateReceberFotosAoFalharRecebimentoCargaInicial();
			}
		} catch (SQLException e) {
			LogSync.error(Messages.FOTOS_CARGA_INICIAL_ERRO);
			ExceptionUtil.handle(e);
		}
	}

	private void removeBtAlertAoReceberPelaTelaSincronizacao() {
		if (existeAlertaConfirmacaoRecebimento()) {
			BaseUIForm.getMsgConfirmAlerta().removeElement(Messages.FOTOS_CARGA_INICIAL_DESEJA_RECEBER);
		}
	}

	private void salvaConfigSucesso() {
		ConfigInternoService.getInstance().removeConfigInternoGeral(ConfigInterno.CARGAINICIALFOTOSPENDENTE, ConfigInterno.VLCHAVEDEFAULT);
	}
	
	private boolean existeFotoProdutoCargaInicial() throws SQLException {
		return LavenderePdaConfig.mostraFotoProduto && FotoProdutoService.getInstance().possuiFotoGeradaCargaInicial();
	}
	
	private boolean existeFotoProdutoEmpCargaInicial() throws SQLException {
		return LavenderePdaConfig.usaFotoProdutoPorEmpresa && FotoProdutoEmpService.getInstance().possuiFotoGeradaCargaInicial();
	}
	
	private boolean existeFotoClienteCargaInicial() throws SQLException {
		return LavenderePdaConfig.usaFotoCliente() && FotoClienteErpService.getInstance().possuiFotoGeradaCargaInicial();
	}
	
	private boolean existeDivulgaInfoCargaInicial() throws SQLException {
		return LavenderePdaConfig.usaDivulgaInformacao && DivulgaInfoService.getInstance().possuiFotoGeradaCargaInicial();
	}

	private boolean existeFotoProdutoGradeCargaInicial() throws SQLException {
		return LavenderePdaConfig.isUsaFotoProdutoGrade() && FotoProdutoGradeService.getInstance().possuiFotoGeradaCargaInicial();
	}
	
	private boolean naoExisteDiretorio(String dir) throws IOException {
		return !FileUtil.openFile(dir, File.DONT_OPEN).exists();
	}

	private boolean existeAlertaConfirmacaoRecebimento() {
		return BaseUIForm.getMsgConfirmAlerta().indexOf(Messages.FOTOS_CARGA_INICIAL_DESEJA_RECEBER) != -1;
	}

	private void createDirs() throws IOException {
		if (LavenderePdaConfig.isMostraFotoProduto() || LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			FileUtil.createDirIfNecessary(Produto.getPathImg());
		}
		if (LavenderePdaConfig.usaFotoCliente()) {
			FileUtil.createDirIfNecessary(Cliente.getPathImg());
		}
		if (LavenderePdaConfig.usaDivulgaInformacao) {
			FileUtil.createDirIfNecessary(DivulgaInfo.getPathImg());
		}
		if (LavenderePdaConfig.isUsaFotoProdutoGrade()) {
			FileUtil.createDirIfNecessary(FotoProdutoGrade.getPathImg());
		}
	}
	
	public void executeForeGround() {
		if (confirmRecebimentoCargaInicial()) {
			new AsyncWindow(this).popup();
		}
	}

	@Override
	public boolean beforeExecuteAsync() {
		return true;
	}

	@Override
	public void executeAsync() {
		try {
			processaCargaInicialFotos();
		} catch (Exception e) {
			LogSync.error(e.getMessage());
		}
		
	}

	@Override
	public void afterExecuteASync() {
		//Not necessary in this context
	}
	
}
