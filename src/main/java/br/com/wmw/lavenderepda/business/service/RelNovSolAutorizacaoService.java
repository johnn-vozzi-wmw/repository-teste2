package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PeriodoNovidade;
import br.com.wmw.lavenderepda.business.domain.RelNovSolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.TipoNovidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelNovSolAutorizacaoDbxDao;
import totalcross.util.Vector;

public class RelNovSolAutorizacaoService extends CrudService {

	private static RelNovSolAutorizacaoService instance;

	public static RelNovSolAutorizacaoService getInstance() {
		if (instance == null) {
			instance = new RelNovSolAutorizacaoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return RelNovSolAutorizacaoDbxDao.getInstance();
	}

	public Vector findRelNovSolAutorizacao(String dsFiltro, int periodoNovidade) throws SQLException {
		RelNovSolAutorizacao relNovSolAutorizacao = getSolAutorizacaoFilter(dsFiltro, periodoNovidade);
		return RelNovSolAutorizacaoDbxDao.getInstance().findAllByExampleQuantidade(relNovSolAutorizacao);
	}

	public Vector findAllByExample(String dsFiltro, String cdTipoNovidade, int periodoNovidade) throws SQLException {
		RelNovSolAutorizacao relNovSolAutorizacao = getSolAutorizacaoFilter(dsFiltro, periodoNovidade);
		relNovSolAutorizacao.cdTipoNovidade = cdTipoNovidade;
		return RelNovSolAutorizacaoDbxDao.getInstance().findAllByExample(relNovSolAutorizacao);
	}

	private RelNovSolAutorizacao getSolAutorizacaoFilter(String dsFiltro, int periodoNovidade) {
		RelNovSolAutorizacao relNovSolAutorizacao = new RelNovSolAutorizacao();
		relNovSolAutorizacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
		relNovSolAutorizacao.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		relNovSolAutorizacao.dsFiltro = dsFiltro;
		if (periodoNovidade == PeriodoNovidade.PERIODO_NOVIDADE_CDHOJE) {
			relNovSolAutorizacao.dtEmissaoRelatorio = DateUtil.getCurrentDate();
		}
		relNovSolAutorizacao.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
		return relNovSolAutorizacao;
	}
	public void createAndInsertNovidade(SolAutorizacao solAutorizacao) throws SQLException {
		RelNovSolAutorizacao relNovSolAutorizacao = new RelNovSolAutorizacao();
		relNovSolAutorizacao.cdEmpresa = solAutorizacao.cdEmpresa;
		relNovSolAutorizacao.cdRepresentante = solAutorizacao.cdRepresentante;
		relNovSolAutorizacao.cdSolAutorizacao = solAutorizacao.cdSolAutorizacao;
		relNovSolAutorizacao.cdTipoSolAutorizacao = solAutorizacao.tipoSolicitacaoAutorizacaoEnum.ordinal();
		relNovSolAutorizacao.flOrigemPedido = solAutorizacao.flOrigemPedido;
		relNovSolAutorizacao.cdTipoNovidade = TipoNovidade.TIPONOVIDADESOLAUTORIZACAO_RESPOSTA_AUTORIZACAO;
		relNovSolAutorizacao.nuPedido = solAutorizacao.nuPedido;
		relNovSolAutorizacao.cdCliente = solAutorizacao.cdCliente;
		relNovSolAutorizacao.cdProduto = solAutorizacao.cdProduto;
		relNovSolAutorizacao.flTipoItemPedido = solAutorizacao.flTipoItemPedido;
		relNovSolAutorizacao.nuSeqProduto = solAutorizacao.nuSeqProduto;
		relNovSolAutorizacao.flAutorizado = solAutorizacao.flAutorizado;
		relNovSolAutorizacao.dtEmissaoRelatorio = DateUtil.getCurrentDate();
		relNovSolAutorizacao.rowKey = relNovSolAutorizacao.getRowKey();
		insert(relNovSolAutorizacao);
	}

	public void deleteAllByDtEmissao(RelNovSolAutorizacao relNovSolAutorizacao) {
		RelNovSolAutorizacaoDbxDao.getInstance().deleteAllByDtEmissao(relNovSolAutorizacao);
	}
}
