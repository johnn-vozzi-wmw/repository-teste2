package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondPagtoTabPreco;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondPagtoTabPrecoPdbxDao;
import totalcross.util.Vector;

public class CondPagtoTabPrecoService extends CrudService {

    private static CondPagtoTabPrecoService instance;

    private CondPagtoTabPrecoService() {
        //--
    }

    public static CondPagtoTabPrecoService getInstance() {
        if (instance == null) {
            instance = new CondPagtoTabPrecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondPagtoTabPrecoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    private CondPagtoTabPreco getCondicaoPagamentoTabPrecoFilter(String cdCondicaoPagamento, String cdTabelaPreco) {
    	CondPagtoTabPreco condicaoPagamentoFilter = new CondPagtoTabPreco();
    	condicaoPagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	condicaoPagamentoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
    	condicaoPagamentoFilter.cdTabelaPreco = cdTabelaPreco;
    	condicaoPagamentoFilter.cdCondicaoPagamento = cdCondicaoPagamento;
		return condicaoPagamentoFilter;
	}
    
    public double qtMinValorCondpagtoTabPreco(String cdCondicaoPagamento, String cdTabelaPreco) throws SQLException {
    	return ValueUtil.getDoubleValue(findVlColumn("qtMinValor", cdCondicaoPagamento, cdTabelaPreco));
    }
    
    public int qtMinProdutoCondpagtoTabPreco(String cdCondicaoPagamento, String cdTabelaPreco) throws SQLException {
    	return ValueUtil.getIntegerValue(findVlColumn("qtMinProduto", cdCondicaoPagamento, cdTabelaPreco));
    }
    
    private String findVlColumn(String nmColumn, String cdCondicaoPagamento, String cdTabelaPreco) throws SQLException {
    	return findColumnByRowKey(getCondicaoPagamentoTabPrecoFilter(cdCondicaoPagamento, cdTabelaPreco).getRowKey(), nmColumn);
    }
    
    @Override
    public Vector findAllByExample(BaseDomain domain) throws SQLException {
    	CondPagtoTabPreco condPagtoTabPreco = (CondPagtoTabPreco) domain;
    	if (LavenderePdaConfig.usaCondPagtoTabPrecoComVigencia) {
    		condPagtoTabPreco.dtInicial = DateUtil.getCurrentDate();
        	condPagtoTabPreco.dtFinal = DateUtil.getCurrentDate();
    	}
    	return super.findAllByExample(condPagtoTabPreco);
    }

}