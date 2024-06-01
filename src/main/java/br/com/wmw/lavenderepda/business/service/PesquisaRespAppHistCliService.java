package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespAppHistCli;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaRespAppHistCliDbxDao;
import totalcross.util.Vector;

public class PesquisaRespAppHistCliService extends CrudService {

	private static PesquisaRespAppHistCliService instance;

	private PesquisaRespAppHistCliService() {
		//--
	}

	public static PesquisaRespAppHistCliService getInstance() {
		if (instance == null) {
			instance = new PesquisaRespAppHistCliService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return PesquisaRespAppHistCliDbxDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) {
	}

	public PesquisaRespApp findRespostaParaInterface(PesquisaApp pesquisaApp, Questionario questionario, String cdPergunta) throws SQLException {
		PesquisaRespAppHistCli pesquisaRespAppHistCliFilter = montaFiltro(questionario, cdPergunta);
		Vector pesquisaRespAppHistCliResults = findAllByExample(pesquisaRespAppHistCliFilter);
		if (!ValueUtil.isEmpty(pesquisaRespAppHistCliResults)) {
			PesquisaRespAppHistCli pesquisaRespAppHistCliResult = (PesquisaRespAppHistCli) pesquisaRespAppHistCliResults.items[0];
			PesquisaRespApp pesquisaRespAppRetorno = alteraCamposChaveRetorno(pesquisaApp, pesquisaRespAppHistCliResult);
			return pesquisaRespAppRetorno;
		}
		return null;
	}
	
	public Vector processaPesquisaRespHistoricoParaInterfaceCheck(PesquisaApp pesquisaApp, Questionario questionario, String cdPergunta, boolean doInsert) throws SQLException {
		PesquisaRespAppHistCli pesquisaRespAppHistCliFilter = montaFiltro(questionario, cdPergunta);
		Vector pesquisaRespAppHistCliResults = findAllByExample(pesquisaRespAppHistCliFilter);
		if (!ValueUtil.isEmpty(pesquisaRespAppHistCliResults)) {
			int size = pesquisaRespAppHistCliResults.size();
			PesquisaRespApp resposta;
			PerguntaResposta pergResp;
			for (int i = 0; i < size; i++) {
				resposta = alteraCamposChaveRetorno(pesquisaApp, (PesquisaRespAppHistCli)pesquisaRespAppHistCliResults.items[i]);
				if (doInsert) PesquisaRespAppService.getInstance().insert(resposta);
				
				pergResp = new PerguntaResposta();
				pergResp.cdEmpresa = resposta.cdEmpresa;
				pergResp.cdPergunta = resposta.cdPergunta;
				pergResp.cdResposta = resposta.cdResposta;
				pesquisaRespAppHistCliResults.items[i] = pergResp;
			}
			return pesquisaRespAppHistCliResults;
		}
		return null;
	}

	private PesquisaRespApp alteraCamposChaveRetorno(PesquisaApp pesquisaApp, PesquisaRespAppHistCli pesquisaRespAppHistCliResult) {
		PesquisaRespApp pesquisaRespAppRetorno = new PesquisaRespApp();
		pesquisaRespAppRetorno.cdEmpresa = pesquisaRespAppHistCliResult.cdEmpresa;
		pesquisaRespAppRetorno.cdRepresentante = pesquisaApp.cdRepresentante;
		pesquisaRespAppRetorno.cdCliente = pesquisaRespAppHistCliResult.cdCliente;
		pesquisaRespAppRetorno.cdPesquisaApp = pesquisaApp.cdPesquisaApp;
		pesquisaRespAppRetorno.cdQuestionario = pesquisaRespAppHistCliResult.cdQuestionario;
		pesquisaRespAppRetorno.cdPergunta = pesquisaRespAppHistCliResult.cdPergunta;
		pesquisaRespAppRetorno.cdResposta = pesquisaRespAppHistCliResult.cdResposta;
		pesquisaRespAppRetorno.dsResposta = pesquisaRespAppHistCliResult.dsResposta;
		pesquisaRespAppRetorno.dsObservacao = pesquisaRespAppHistCliResult.dsObservacao;
		pesquisaRespAppRetorno.cdUsuario = pesquisaRespAppHistCliResult.cdUsuario;
		pesquisaRespAppRetorno.flTipoAlteracao = pesquisaRespAppHistCliResult.flTipoAlteracao;
		return pesquisaRespAppRetorno;
	}

	private PesquisaRespAppHistCli montaFiltro(Questionario questionario, String cdPergunta) {
		PesquisaRespAppHistCli pesquisaRespAppHistCliFilter = new PesquisaRespAppHistCli();
		pesquisaRespAppHistCliFilter.cdEmpresa = questionario.cdEmpresa;
		pesquisaRespAppHistCliFilter.cdCliente = questionario.cdCliente;
		pesquisaRespAppHistCliFilter.cdQuestionario = questionario.cdQuestionario;
		pesquisaRespAppHistCliFilter.cdPergunta = cdPergunta;
		return pesquisaRespAppHistCliFilter;
	}
	
	public boolean existePesquisaNoHistorico(Questionario questionario) throws SQLException {
		PesquisaRespAppHistCli pesquisaRespAppHistCliFilter = new PesquisaRespAppHistCli();
		pesquisaRespAppHistCliFilter.cdEmpresa = questionario.cdEmpresa;
		pesquisaRespAppHistCliFilter.cdCliente = questionario.cdCliente;
		pesquisaRespAppHistCliFilter.cdQuestionario = questionario.cdQuestionario;
		Vector pesquisaRespAppHistCliResults = findAllByExample(pesquisaRespAppHistCliFilter);
		return pesquisaRespAppHistCliResults != null && pesquisaRespAppHistCliResults.size() > 0;
	}

}