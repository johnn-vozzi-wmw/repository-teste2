package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.RelNovidadePesquisaMercado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaMercadoConfigDbxDao;
import totalcross.util.Vector;

public class PesquisaMercadoConfigService extends CrudService {

	private static PesquisaMercadoConfigService instance;

	private PesquisaMercadoConfigService() {
	}

	public static PesquisaMercadoConfigService getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoConfigService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return PesquisaMercadoConfigDbxDao.getInstance();
	}

	public Vector findAllByExampleWithQuantidades(PesquisaMercadoConfig pesquisaMercadoConfigFilter) throws SQLException {
		if (LavenderePdaConfig.permiteInserirNovoExcluirItemPesquisaMercado()) {
			return PesquisaMercadoConfigDbxDao.getInstance().findAllByExampleWithQuantidadesAdicionadas(pesquisaMercadoConfigFilter);
		} else {
			return PesquisaMercadoConfigDbxDao.getInstance().findAllByExampleWithQuantidades(pesquisaMercadoConfigFilter);
		}
	}

	public Vector findAllByExampleWithQuantidadesSemQtAdicionadas(PesquisaMercadoConfig pesquisaMercadoConfigFilter) throws SQLException {
		return PesquisaMercadoConfigDbxDao.getInstance().findAllByExampleWithQuantidades(pesquisaMercadoConfigFilter);
	}

	public Vector findAllPesquisaMercadoExpirada() throws SQLException {
		return PesquisaMercadoConfigDbxDao.getInstance().findAllPesquisaMercadoExpirada(DateUtil.getCurrentDate());
	}

	public String findDsByCd(RelNovidadePesquisaMercado relNovidadePesquisaMercado) throws SQLException {
		PesquisaMercadoConfig pesquisaMercadoConfigFilter = new PesquisaMercadoConfig();
		pesquisaMercadoConfigFilter.cdEmpresa = relNovidadePesquisaMercado.cdEmpresa;
		pesquisaMercadoConfigFilter.cdRepresentante = relNovidadePesquisaMercado.cdRepresentante;
		pesquisaMercadoConfigFilter.cdPesquisaMercadoConfig = relNovidadePesquisaMercado.cdPesquisaMercadoConfig;
		PesquisaMercadoConfig pesquisaMercadoConfig = (PesquisaMercadoConfig) findByRowKey(pesquisaMercadoConfigFilter.getRowKey());
		if (pesquisaMercadoConfig != null && pesquisaMercadoConfig.dsPesquisaMercado != null) {
			return pesquisaMercadoConfig.dsPesquisaMercado;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public String[] getListFormItem(PesquisaMercadoConfig pesquisaMercadoConfig, String[] item) {
		item[0] = pesquisaMercadoConfig.dsPesquisaMercado;
		item[1] = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_CODIGO_LABEL, pesquisaMercadoConfig.cdPesquisaMercadoConfig);
		item[2] = Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE;
		item[3] = ValueUtil.VALOR_NI;
		item[4] = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_VIGENCIA, new Object[]{pesquisaMercadoConfig.dtInicialVigencia.toString(), pesquisaMercadoConfig.dtFimVigencia.toString()});
		String qtItens;
		if (pesquisaMercadoConfig.qtItensPesquisa == 1) {
			qtItens = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_ITEM, ValueUtil.getIntegerValue(pesquisaMercadoConfig.qtItensPesquisa));
		} else {
			qtItens = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_ITENS, ValueUtil.getIntegerValue(pesquisaMercadoConfig.qtItensPesquisa));
		}
		if (pesquisaMercadoConfig.qtConcorrentesPesquisa == 1) {
			item[5] = qtItens + " / " + MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_CONCORRENTE, ValueUtil.getIntegerValue(pesquisaMercadoConfig.qtConcorrentesPesquisa));
		} else {
			item[5] = qtItens + " / " + MessageUtil.getMessage(Messages.PESQUISA_MERCADO_PROD_CONC_CONCORRENTES, ValueUtil.getIntegerValue(pesquisaMercadoConfig.qtConcorrentesPesquisa));
		}
		if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos()) {
			item[6] = ValueUtil.VALOR_NI;
		}
		return item;
	}
}
