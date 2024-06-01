package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaAppPdbxDao;
import totalcross.util.Vector;

public class PesquisaAppService extends CrudPersonLavendereService {

	private static PesquisaAppService instance;

	public static PesquisaAppService getInstance() {
		if (instance == null) {
			instance = new PesquisaAppService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return PesquisaAppPdbxDao.getInstance();
	}

	public PesquisaApp findLast(BaseDomain baseDomain) throws SQLException {
		return PesquisaAppPdbxDao.getInstance().findLast(baseDomain);
	}

	public void criaNovaPesquisa(Questionario questionario, String cdPesquisaAppExistente, boolean isObrigaRespostaRegistroChegada) throws SQLException {
		PesquisaApp novaPesquisa = new PesquisaApp();
		novaPesquisa.cdEmpresa = questionario.cdEmpresa;
		novaPesquisa.cdCliente = questionario.cdCliente;
		novaPesquisa.cdQuestionario = questionario.cdQuestionario;
		novaPesquisa.cdRepresentante = SessionLavenderePda.getRepresentante().getCdRepresentanteTxt();
		if (isObrigaRespostaRegistroChegada) {
			novaPesquisa.cdVisita = SessionLavenderePda.visitaAndamento.cdVisita;
			novaPesquisa.cdPesquisaApp = cdPesquisaAppExistente;
		}
		//limpa as tabelas PesquisaApp e PesquisaRespApp e PesquisaRespAppFoto
		if (ValueUtil.isEmpty(cdPesquisaAppExistente) && !isObrigaRespostaRegistroChegada || ValueUtil.isNotEmpty(cdPesquisaAppExistente)) {
			deletePesquisaByPesquisaApp(novaPesquisa);
		}
		//Define um ID Global para que não haja problemas no sync
		novaPesquisa.cdPesquisaApp = generateIdGlobal();
		novaPesquisa.flPesquisaNovoCliente = isNovoCliente(novaPesquisa) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		insert(novaPesquisa);
	}
	
	private boolean isNovoCliente(PesquisaApp pesquisaApp) throws SQLException {
		Cliente clienteFilter = new Cliente();
		clienteFilter.cdEmpresa = pesquisaApp.cdEmpresa;
		clienteFilter.cdRepresentante = pesquisaApp.cdRepresentante;
		clienteFilter.cdCliente = pesquisaApp.cdCliente;
		clienteFilter = (Cliente)ClienteService.getInstance().findByPrimaryKey(clienteFilter);
		return clienteFilter.isNovoCliente();
	}
	
	public Vector findAllByNovoCliente(NovoCliente domain) throws SQLException {
		PesquisaApp pesquisaAppFilter = new PesquisaApp();
    	pesquisaAppFilter.cdEmpresa = domain.cdEmpresa;
    	pesquisaAppFilter.cdRepresentante = domain.cdRepresentante;
    	pesquisaAppFilter.cdCliente = domain.nuCnpj;
    	Vector pesquisaAppList = PesquisaAppService.getInstance().findAllByExample(pesquisaAppFilter);
    	return pesquisaAppList;
	}

	public PesquisaApp findDataFimPesquisaByPesquisaApp(PesquisaApp pesquisaApp, Questionario questionario) throws SQLException {
		return PesquisaAppPdbxDao.getInstance().findDataFimPesquisaByPesquisaApp(pesquisaApp, questionario);
	}
	
	public void deletePesquisasByVisitaAndamento() throws SQLException {
		if (SessionLavenderePda.visitaAndamento == null || !LavenderePdaConfig.isObrigaRespostaRegistroChegada()) {
			return;
		}
		PesquisaApp pesquisaAppFilter = new PesquisaApp();
		pesquisaAppFilter.cdEmpresa = SessionLavenderePda.visitaAndamento.cdEmpresa;
		pesquisaAppFilter.cdRepresentante = SessionLavenderePda.visitaAndamento.cdRepresentante;
		pesquisaAppFilter.cdCliente = SessionLavenderePda.visitaAndamento.cdCliente;
		pesquisaAppFilter.cdVisita = SessionLavenderePda.visitaAndamento.cdVisita;
		Vector pesquisaAppList = PesquisaAppPdbxDao.getInstance().findAllByExample(pesquisaAppFilter);
		for (PesquisaApp pesquisaApp : VectorUtil.iterateOver(pesquisaAppList, PesquisaApp.class)) {
			deletePesquisaByPesquisaApp(pesquisaApp);
		}
	}
	
	private void deletePesquisaByPesquisaApp(PesquisaApp pesquisaApp) throws SQLException {
		PesquisaAppPdbxDao.getInstance().deleteAllByExample(pesquisaApp);
		PesquisaRespAppService.getInstance().deleteAllByPesquisaApp(pesquisaApp);
		PesquisaRespAppFotoService.getInstance().deleteAllByPesquisaApp(pesquisaApp);
	}
}
