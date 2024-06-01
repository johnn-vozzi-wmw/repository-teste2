package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PeriodoNovidade;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.RelNovidadePesquisaMercado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelNovidadePesquisaMercadoDbxDao;
import totalcross.util.Vector;

public class RelNovidadePesquisaMercadoService extends CrudService {

	private static RelNovidadePesquisaMercadoService instance;

	public static RelNovidadePesquisaMercadoService getInstance() {
		if (instance == null) {
			instance = new RelNovidadePesquisaMercadoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return RelNovidadePesquisaMercadoDbxDao.getInstance();
	}

	public Vector findRelNovidadePesquisaMercado(String dsFiltro, int periodoNovidade) throws SQLException {
		RelNovidadePesquisaMercado relNovidadePesquisaMercado = getPesquisaMercadoFilter(dsFiltro, periodoNovidade);
		return RelNovidadePesquisaMercadoDbxDao.getInstance().findAllByExampleQuantidadeJoinConfig(relNovidadePesquisaMercado);
	}

	public Vector findAllByExample(String dsFiltro, String cdTipoNovidade, int periodoNovidade) throws SQLException {
		RelNovidadePesquisaMercado relNovidadePesquisaMercado = getPesquisaMercadoFilter(dsFiltro, periodoNovidade);
		relNovidadePesquisaMercado.cdTipoNovidade = cdTipoNovidade;
		return RelNovidadePesquisaMercadoDbxDao.getInstance().findAllByExampleJoinConfig(relNovidadePesquisaMercado);
	}

	private RelNovidadePesquisaMercado getPesquisaMercadoFilter(String dsFiltro, int periodoNovidade) {
		RelNovidadePesquisaMercado relNovidadePesquisaMercado = new RelNovidadePesquisaMercado();
		relNovidadePesquisaMercado.cdEmpresa = SessionLavenderePda.cdEmpresa;
		relNovidadePesquisaMercado.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		relNovidadePesquisaMercado.dsFiltro = dsFiltro;
		if (periodoNovidade == PeriodoNovidade.PERIODO_NOVIDADE_CDHOJE) {
			relNovidadePesquisaMercado.dtEmissaoRelatorio = DateUtil.getCurrentDate();
		}
		relNovidadePesquisaMercado.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
		return relNovidadePesquisaMercado;
	}

	public void deleteRelNovidadePesquisaMercadoExpirada(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		if (LavenderePdaConfig.geraNovidadePesquisaMercado()) {
			RelNovidadePesquisaMercado relNovidadePesquisaMercado = new RelNovidadePesquisaMercado();
			relNovidadePesquisaMercado.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
			relNovidadePesquisaMercado.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
			relNovidadePesquisaMercado.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
			deleteAllByExample(relNovidadePesquisaMercado);
		}
	}

	public String getDsVigencia(RelNovidadePesquisaMercado relNovidadePesquisaMercado) throws SQLException {
		PesquisaMercadoConfig pesquisaMercadoConfigFilter = new PesquisaMercadoConfig();
		pesquisaMercadoConfigFilter.cdEmpresa = relNovidadePesquisaMercado.cdEmpresa;
		pesquisaMercadoConfigFilter.cdRepresentante = relNovidadePesquisaMercado.cdRepresentante;
		pesquisaMercadoConfigFilter.cdPesquisaMercadoConfig = relNovidadePesquisaMercado.cdPesquisaMercadoConfig;
		PesquisaMercadoConfig pesquisaMercadoConfig = (PesquisaMercadoConfig) PesquisaMercadoConfigService.getInstance().findByRowKey(pesquisaMercadoConfigFilter.getRowKey());
		if (pesquisaMercadoConfig != null) {
			return MessageUtil.getMessage(Messages.RELNOVIDADE_DETALHES_PESQUISA_MERCADO_SEPARADOR_VIGENCIA, new String[]{pesquisaMercadoConfig.dtInicialVigencia.toString(), pesquisaMercadoConfig.dtFimVigencia.toString()});
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public PesquisaMercadoConfig getConfigWithQuantidades(RelNovidadePesquisaMercado relNovidadePesquisaMercado) throws SQLException {
		PesquisaMercadoConfig pesquisaMercadoConfigFilter = new PesquisaMercadoConfig();
		pesquisaMercadoConfigFilter.cdEmpresa = relNovidadePesquisaMercado.cdEmpresa;
		pesquisaMercadoConfigFilter.cdRepresentante = relNovidadePesquisaMercado.cdRepresentante;
		pesquisaMercadoConfigFilter.cdPesquisaMercadoConfig = relNovidadePesquisaMercado.cdPesquisaMercadoConfig;
		Vector configList = PesquisaMercadoConfigService.getInstance().findAllByExampleWithQuantidadesSemQtAdicionadas(pesquisaMercadoConfigFilter);
		if (ValueUtil.isNotEmpty(configList)) {
			return (PesquisaMercadoConfig) configList.items[0];
		} else {
			return pesquisaMercadoConfigFilter;
		}
	}
}
