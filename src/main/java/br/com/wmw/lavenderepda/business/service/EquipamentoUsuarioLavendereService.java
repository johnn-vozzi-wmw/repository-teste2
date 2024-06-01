package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.EquipamentoUsuario;
import br.com.wmw.framework.business.service.EquipamentoUsuarioService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.util.EquipamentoUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EquipamentoLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EquipamentoUsuarioLavendereDbxDao;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;

public class EquipamentoUsuarioLavendereService extends EquipamentoUsuarioService {

	private static final String CONNECTION_TIME_OUT = "connect timed out";

	private static EquipamentoUsuarioLavendereService instance;

	private EquipamentoUsuarioLavendereService() {
		//--
	}

	public static EquipamentoUsuarioLavendereService getInstance() {
		if (instance == null) {
			instance = new EquipamentoUsuarioLavendereService();
		}
		return instance;
	}

	public void updateUsuarioEquipamentoNaoPermitido() {
		try {
			updateFlUsoPermitido(EquipamentoUtil.getCdEquipamento(), Session.getCdUsuario(), ValueUtil.VALOR_NAO);
		} catch (Throwable ex) {
			// Nada
		}
	}

	public boolean verificaEquipamentoLiberadoUsuario(ParamsSync sync) throws SQLException {
		if (Session.isModoSuporte) {
			return true;
		}
		if (LavenderePdaConfig.bloqueiaSistemaEquipamentoInativo) {
			return validaEquipamentoLiberadoRemoto(sync) || validaEquipamentoLiberadoBanco();
		}
		return true;
	}

	private boolean validaEquipamentoLiberadoRemoto(ParamsSync sync) throws SQLException {
		if (ValueUtil.isEmpty(Session.getCdUsuario())) {
			return true;
		}
		try {
			String retorno = validaEquipamentoLiberadoNoServidor(sync);
			if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, retorno)) {
				persisteEquipamentoUsuario(ValueUtil.VALOR_SIM);
				return true;
			} else if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, retorno)) {
				persisteEquipamentoUsuario(ValueUtil.VALOR_NAO);
				return false;
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return isEquipamentoUsuarioLiberadoBanco();
	}

	private boolean isEquipamentoUsuarioLiberadoBanco() throws SQLException {
		return ValueUtil.getBooleanValue(EquipamentoUsuarioLavendereService.getInstance().findColumnByRowKey(new EquipamentoUsuario().buildRowKey().getRowKey(), "FLUSOPERMITIDO"));
	}

	private String validaEquipamentoLiberadoNoServidor(ParamsSync sync) throws Exception {
		LavendereWeb2Tc lavendereWeb2Tc = new LavendereWeb2Tc(sync);
		return lavendereWeb2Tc.validaEquipamentoLiberadoNoServidor(EquipamentoUtil.getCdEquipamento(), Session.getCdUsuario());
	}

	@Override
	protected CrudDao getCrudDao() {
		return EquipamentoUsuarioLavendereDbxDao.getInstance();
	}
	
	/**
	 * @throws SQLException
	 */
	public void controlaEquipamento(ParamsSync sync) throws SQLException {
		if (Session.isModoSuporte || ValueUtil.isEmpty(Session.getCdUsuario())) {
			VmUtil.debug("Ignorando controle de equipamento, por estar em modo suporte ou sem usuário na sessão.");
			return;
		}
		EquipamentoUtil.criaArquivoControleEquipamento(sync);
		EquipamentoLavendereService.getInstance().insertOrUpdateEquipamento();
		EquipamentoUsuarioLavendereService.getInstance().insertOrUpdateEquipamentoUsuario(LavenderePdaConfig.habilitaEquipamentoParaUsoPorPadrao);
		EquipamentoUsuarioLavendereService.getInstance().enviaDadosEquipamentoServidor(sync);
	}

	public void enviaDadosEquipamentoServidor(ParamsSync sync) throws SQLException {
		if (isNecessarioEnviarCadastroEquipamento()) {
			try {
				conectaParaEnvioEquipamento(sync);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	private void conectaParaEnvioEquipamento(ParamsSync defaultParamsSync) throws Exception {
		LavendereTc2Web pdaToErp = new LavendereTc2Web(defaultParamsSync);
		pdaToErp.paramsSync.openTimeout = 3000;
		pdaToErp.paramsSync.readTimeout = 15000;
		pdaToErp.paramsSync.nuMaxTentativas = 1;
		pdaToErp.paramsSync.stopOnError = true;
		pdaToErp.conectaEnviaDadosServidor(SyncManager.getInfoAtualizacaoByWeb2SyncList(EquipamentoLavendereService.getInstance().getConfigIntegWebTcList()));
	}
	
	private boolean isNecessarioEnviarCadastroEquipamento() throws SQLException {
		return EquipamentoLavendereDbxDao.getInstance().isPossuiDadosEnvioServidor() || EquipamentoUsuarioLavendereDbxDao.getInstance().isPossuiDadosEnvioServidor();
	}

}