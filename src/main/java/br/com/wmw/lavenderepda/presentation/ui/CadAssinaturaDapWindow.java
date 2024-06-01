package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ConnectionException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.SaveWhiteboardEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditWhiteboard;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.service.DapLaudoService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadAssinaturaDapWindow extends WmwWindow {

	private EditWhiteboard edWAssinaturaTec;
	private EditWhiteboard edWAssinaturaAgr;
	private ButtonPopup btConfirmar;
	private DapLaudo dapLaudo;
	private boolean readOnly;
	
	public CadAssinaturaDapWindow(DapLaudo dapLaudo) throws SQLException {
		this(dapLaudo, false);
	}

	public CadAssinaturaDapWindow(DapLaudo dapLaudo, boolean readOnly) throws SQLException {
		super(readOnly ? Messages.CAD_DAPLAUDO_ASSINATURA_TITULO_READONLY : Messages.CAD_DAPLAUDO_ASSINATURA_TITULO);
		this.dapLaudo = dapLaudo;
		edWAssinaturaTec = new EditWhiteboard(dapLaudo.getNmAssinaturaTec());
		edWAssinaturaAgr = new EditWhiteboard(dapLaudo.getNmAssinaturaCli());
		edWAssinaturaAgr.setImagePath(DapLaudoService.ASSINATURAPATH);
		edWAssinaturaTec.setImagePath(DapLaudoService.ASSINATURAPATH);
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		this.readOnly = readOnly;
		if (readOnly) {
			domainToScreen();
		} else {
			if (isCarregaImagemAssinaturaArmazenada()) {
				carregaImagemAssinaturaArmazenada();
			}
			carregaAssinaturaDaPasta();
		}
		setDefaultRect();
	}

	private boolean isFotoExists(EditWhiteboard editWhiteboard) {
		boolean ret = false;
		File file = null;
		try {
			file = new File(editWhiteboard.getImagePath());
			ret = file.exists();
			return ret;
		} catch (IOException e) {
			return ret;
		} finally {
			FileUtil.closeFile(file);
		}
	}

	private boolean isFotoNotExists(EditWhiteboard editWhiteboard) {
		return !isFotoExists(editWhiteboard);
	}

	private boolean isCarregaImagemAssinaturaArmazenada(){
		return isFotoNotExists(edWAssinaturaAgr) || isFotoNotExists(edWAssinaturaTec);
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btConfirmar) {
					btConfirmarClick();
				}
				break;
			case SaveWhiteboardEvent.SAVE_WHITEBOARD_PRESS: {
				if (event.target == edWAssinaturaAgr) {
					atribuiImagemAssinaturaCli();
				} else if (event.target == edWAssinaturaTec) {
					atribuiImagemAssinaturaTec();
				}
			}
		}
	}
	
	private void atribuiImagemAssinaturaTec() {
		try {
			dapLaudo.imAssTecnico = DapLaudoService.getInstance().getImageToBytes(edWAssinaturaTec.getImagePath());
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private void atribuiImagemAssinaturaCli() {
		try {
			dapLaudo.imAssCliente = DapLaudoService.getInstance().getImageToBytes(edWAssinaturaAgr.getImagePath());
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}
	}

	private void btConfirmarClick() {
		dapLaudo.dtEmissao = DateUtil.getCurrentDate();
		dapLaudo.flStatusLaudo = DapLaudo.FLSTATUSLAUDO_FECHADO;
		try {
			if (isCarregaImagemAssinaturaArmazenada()) {
				geraImagemImagemAssinaturaArmazenada();
				if (isCarregaImagemAssinaturaArmazenada()) {
					LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_DAPLAUDO, MessageUtil.getMessage(Messages.CAD_DAPLAUDO_ERRO_ENVIODAP_LOG, dapLaudo.getRowKey()));
				}
			}
			DapLaudoService.getInstance().insert(dapLaudo);
		} catch (ValidationException e) {
			dapLaudo.flStatusLaudo = DapLaudo.FLSTATUSLAUDO_ABERTO;
			UiUtil.showErrorMessage(e.getMessage());
			return;
		} catch (SQLException e) {
			dapLaudo.flStatusLaudo = DapLaudo.FLSTATUSLAUDO_ABERTO;
			ExceptionUtil.handle(e);
			return;
		}

		try {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_DAPLAUDO_ENVIANDODAP);
			msg.popupNonBlocking();
			try {
				SyncManager.enviaDapLaudoAtua(dapLaudo.isDapFechado());
			} finally {
				msg.unpop();
			}
			unpop();
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_DAPLAUDO, MessageUtil.getMessage(Messages.DAPATUA_ENVIADO_SUCESSO_LOG, dapLaudo.getRowKey()));
		} catch (ConnectionException e) {
			UiUtil.showErrorMessage(Messages.CAD_DAPLAUDO_ERRO_ENVIODAP);
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_DAPLAUDO, MessageUtil.getMessage(Messages.CAD_DAPLAUDO_ERRO_ENVIODAP_LOG, dapLaudo.getRowKey()));
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_DAPLAUDO, MessageUtil.getMessage(Messages.CAD_DAPLAUDO_ERRO_ENVIODAP_LOG, dapLaudo.getRowKey()));
			ExceptionUtil.handle(e);
		} finally {
			try {
				dapLaudo.enviadoServidor = DapLaudoService.getInstance().isDapEnviadoServidor(dapLaudo);
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	public void domainToScreen() throws SQLException {
		if (dapLaudo.enviadoServidor) {
			if (dapLaudo.imAssTecnico != null) {
				loadImageEdWAssinatura(edWAssinaturaTec, dapLaudo.imAssTecnico);
			}
			if (dapLaudo.imAssCliente != null) {
				loadImageEdWAssinatura(edWAssinaturaAgr, dapLaudo.imAssCliente);
			}
		} else {
			carregaAssinaturaDaPasta();
		}
	}

	private void carregaAssinaturaDaPasta() {
		if (isCarregaImagemAssinaturaArmazenada()) {
			carregaImagemAssinaturaArmazenada();
		}
		edWAssinaturaAgr.loadImage();
		atribuiImagemAssinaturaCli();
		edWAssinaturaTec.loadImage();
		atribuiImagemAssinaturaTec();
	}

	private void carregaImagemAssinaturaArmazenada() {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_DAPLAUDO_CARREGANDOIMAGENS);
		msg.popupNonBlocking();
		try {
			geraImagemImagemAssinaturaArmazenada();
		} finally {
			msg.unpop();
		}
	}

	private void geraImagemImagemAssinaturaArmazenada() {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_DAPLAUDO_CARREGANDOIMAGENS);
		msg.popupNonBlocking();
		try {
			if (dapLaudo.imAssTecnico != null) {
				DapLaudoService.getInstance().geraImagemAssinaturaDap(dapLaudo.imAssTecnico,  dapLaudo.getNmAssinaturaTec());
			}
			if (dapLaudo.imAssCliente != null) {
				DapLaudoService.getInstance().geraImagemAssinaturaDap(dapLaudo.imAssCliente, dapLaudo.getNmAssinaturaCli());
			}
		} finally {
			msg.unpop();
		}
	}

	private void loadImageEdWAssinatura(EditWhiteboard edWAssinatura, byte[] imgbytes) {
		try {
			edWAssinatura.loadImage(imgbytes);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	@Override
	public void initUI() {
		super.initUI();
		if (!readOnly) {
			addButtonPopup(btConfirmar);
		}
		addButtonPopup(btFechar);
		UiUtil.add(this, new LabelName(Messages.CAD_DAP_CAMPO_ASSINATURA_TEC), edWAssinaturaTec, getLeft(), getTop(), FILL - HEIGHT_GAP, PREFERRED);
		UiUtil.add(this, new LabelName(Messages.CAD_DAP_CAMPO_ASSINATURA_CLI), edWAssinaturaAgr, getLeft(), AFTER + HEIGHT_GAP_BIG, FILL - HEIGHT_GAP, PREFERRED);
		setEnable();
	}

	private void setEnable() {
		edWAssinaturaAgr.setEditable(!readOnly);
		edWAssinaturaTec.setEditable(!readOnly);
	}

}
