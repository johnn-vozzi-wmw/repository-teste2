package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.UsuarioHistAlteracao;
import br.com.wmw.framework.business.service.RestricaoSenhaUsuarioService;
import br.com.wmw.framework.business.service.UsuarioHistAlteracaoService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.EncodeUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioHistAlteracaoLavendereDbxDao;
import totalcross.util.Vector;

public class UsuarioHistAlteracaoLavendereService extends UsuarioHistAlteracaoService {
	
	public static UsuarioHistAlteracaoLavendereService instance;
	
	private UsuarioHistAlteracaoLavendereService() {
		super();
	}

	public static UsuarioHistAlteracaoLavendereService getInstance() {
		if (instance == null) {
			instance = new UsuarioHistAlteracaoLavendereService();
		}
		return instance;
	}

	protected CrudDao getCrudDao() {
		return UsuarioHistAlteracaoLavendereDbxDao.getInstance();
	}
	
	protected void validaListaRestricaoSenhaUsuario(String dsSenha) throws SQLException {
		if (LavenderePdaConfig.usaListaRestricaoSenhaUsuario) {
			if (RestricaoSenhaUsuarioService.getInstance().isExisteSenhaNaListaRestricaoSenhaUsuario(dsSenha)) {
				throw new ValidationException(Messages.MSG_SENHA_PALAVRA_NAO_PERMITIDA);
			}
		}
	}
	
	protected void validaControleRepeticaoSenhaUsuario(String cdUsuario, String dsSenhaNova) throws SQLException {
		int qtdRepeticaoSenhaUsuario = LavenderePdaConfig.nuRegistrosControleRepeticaoSenhaUsuario;
		if (qtdRepeticaoSenhaUsuario > 0) {
			Vector historicoSenhasAlteradasList = UsuarioHistAlteracaoLavendereDbxDao.getInstance().findHistoricoSenhasAlteradas(cdUsuario, qtdRepeticaoSenhaUsuario);
			if (ValueUtil.isNotEmpty(historicoSenhasAlteradasList)) {
				int size = historicoSenhasAlteradasList.size();
				dsSenhaNova = EncodeUtil.encodeToSHA1(dsSenhaNova);
				for (int i = 0; i < size; i++) {
					UsuarioHistAlteracao usuarioHistAlteracao = (UsuarioHistAlteracao) historicoSenhasAlteradasList.items[i];
					if (ValueUtil.valueEquals(dsSenhaNova, usuarioHistAlteracao.vlColuna)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.MSG_SENHA_JA_UTILIZADA, StringUtil.getStringValue(qtdRepeticaoSenhaUsuario)));
					}
				}
			}
		}
	}
	
}
