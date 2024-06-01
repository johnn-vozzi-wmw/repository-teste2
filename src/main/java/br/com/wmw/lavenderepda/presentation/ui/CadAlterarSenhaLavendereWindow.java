package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.business.domain.UsuarioHistAlteracao;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.CadAlterarSenhaWindow;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.UsuarioHistAlteracaoLavendereService;
import br.com.wmw.lavenderepda.business.service.UsuarioLavendereService;
import totalcross.util.Date;

public class CadAlterarSenhaLavendereWindow extends CadAlterarSenhaWindow {
		
	protected boolean senhaAlteradaComSucesso = false;

	public CadAlterarSenhaLavendereWindow(Usuario usuario) throws SQLException {
		super(usuario);
	}
	
	@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return UsuarioLavendereService.getInstance();
	}
	
	@Override
	protected void onPopup() {
		super.onPopup();
		senhaAlteradaComSucesso = false;
	}
	
	@Override
	public void initUI() {
		super.initUI();
		adicionaMensagemExpressaoRegular();
	}

	private void adicionaMensagemExpressaoRegular() {
		if (LavenderePdaConfig.isUsaExpressaoRegularValidacaoSenhaUsuario()) {
			String mensagemExpressaoRegularValidacaoSenhaUsuario = LavenderePdaConfig.mensagemExpressaoRegularValidacaoSenhaUsuario;
			if (ValueUtil.isNotEmpty(mensagemExpressaoRegularValidacaoSenhaUsuario)) {
				EditMemo edMensagemExpressaoRegularValidacaoSenhaUsuario = new EditMemo("@@@@@@@@@@", 10, mensagemExpressaoRegularValidacaoSenhaUsuario.length());
				edMensagemExpressaoRegularValidacaoSenhaUsuario.setEnabled(false);
				edMensagemExpressaoRegularValidacaoSenhaUsuario.setValue(mensagemExpressaoRegularValidacaoSenhaUsuario);
				UiUtil.add(this, edMensagemExpressaoRegularValidacaoSenhaUsuario, getLeft(), getNextY());
			}
		}
	}
	
	
	@Override
	protected void beforeSave() throws SQLException {
		if (LavenderePdaConfig.isQtdDiasExpiracaoSenhaLigado()) {
			Usuario usuario = (Usuario) getDomain();
			usuario.dtUltimaAlteracaoSenha = new Date();
		}
		super.beforeSave();
	}
	
	@Override
	protected void afterSave() throws SQLException {
		UsuarioHistAlteracaoLavendereService.getInstance().insereUsuarioAlteracao((Usuario) getDomain(), UsuarioHistAlteracao.CDTIPOALTERACAOSENHA);
		SessionLavenderePda.usuarioPdaRep.usuario = (Usuario) UsuarioLavendereService.getInstance().findByRowKey(SessionLavenderePda.usuarioPdaRep.usuario.getRowKey());
		if (!LavenderePdaConfig.usaSistemaModoOffLine) {
			Usuario usuario = (Usuario) getDomain();
			if (usuario.envioUsuarioServidorBackGround) {
				UsuarioLavendereService.getInstance().enviaUsuarioServidor();
			} else if (UiUtil.showConfirmYesNoMessage(FrameworkMessages.MSG_CONF_ENVIAR_DADOS_ALTERADOS)) {
				try {
					UiUtil.showProcessingMessage();
					UsuarioLavendereService.getInstance().enviaUsuarioServidor();
				} finally {
					UiUtil.unpopProcessingMessage();
				}
			}
		}
		senhaAlteradaComSucesso = true;
	}

}
