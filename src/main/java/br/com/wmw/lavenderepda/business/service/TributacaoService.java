package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TributacaoPdbxDao;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class TributacaoService extends CrudService {

    private static TributacaoService instance;
    
    private static String cdEmpresaFiltroCache;
    private static String cdRepresentanteFiltroCache;
    private static String cdClienteFiltroCache;
    private static String[] cdTributacaoClienteFiltroCache = new String[2];
    
    private TributacaoService() {
        //--
    }

    public static TributacaoService getInstance() {
        if (instance == null) {
            instance = new TributacaoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TributacaoPdbxDao.getInstance();
    }
    
    public Vector findDistinctTributacao(Tributacao tributacaoFilter) throws SQLException {
    	Vector tributacaoList = TributacaoPdbxDao.getInstance().findAllByExample(tributacaoFilter);
    	Hashtable distinctTributacaoHash = new Hashtable(0);
    	if (ValueUtil.isNotEmpty(tributacaoList)) {
    		int size = tributacaoList.size();
    		for (int i = 0; i < size; i++) {
    			Tributacao tributacao = (Tributacao)tributacaoList.items[i];
    			if (distinctTributacaoHash.get(tributacao.cdTributacaoCliente) == null) {
    				tributacao.cdTributacaoProduto = null;
    				distinctTributacaoHash.put(tributacao.cdTributacaoCliente, tributacao);
    			}
    		}
		}
    	return distinctTributacaoHash.getValues();
    }

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}
	
    public Tributacao getTributacao(Cliente cliente, String cdTributacaoProduto, TipoPedido tipoPedido, String cdUf, TributacaoConfig tributacaoConfig) throws SQLException {
    	Tributacao tributacaoFilter = new Tributacao();
    	tributacaoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tributacaoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Tributacao.class);
    	tributacaoFilter.cdTributacaoCliente = cliente.cdTributacaoCliente;
    	tributacaoFilter.cdTributacaoProduto = cdTributacaoProduto;
    	loadCacheTributacao(cliente);
    	String cdTipoPedido = tipoPedido == null || tipoPedido.isUsaTributacaoGeral() ? Tributacao.CDTIPOPEDIDOVALORPADRAO : tipoPedido.cdTipoPedido;
    	Tributacao tributacao = (Tributacao) findTributacaoDeAcordoComTipoPedidoEUF(tributacaoFilter, cdTipoPedido, cdUf);
		if (tributacao == null) {
			tributacao = findTributacaoParaCdTributacaoCliente2(cliente, cdUf, tributacaoFilter, cdTipoPedido, tributacao);
		}
		if (tributacao == null) {
			tributacao = findTributacaoParaCdTributacaoCliente1ProdutoZero(cliente, cdUf, tributacaoFilter, cdTipoPedido, tributacao);
		}
		if (tributacao == null) {
			tributacao = findTributacaoParaCdTributacaoCliente2(cliente, cdUf, tributacaoFilter, cdTipoPedido, tributacao);
		}
		return tributacao;
    }

	private Tributacao findTributacaoParaCdTributacaoCliente1ProdutoZero(Cliente cliente, String cdUf, Tributacao tributacaoFilter, String cdTipoPedido, Tributacao tributacao) throws SQLException {
		tributacaoFilter.cdTributacaoCliente = cliente.cdTributacaoCliente;
		if (ValueUtil.isEmpty(tributacaoFilter.cdTributacaoCliente)) return null;
		tributacaoFilter.cdTributacaoProduto = Tributacao.CDTRIBUTACAOPRODUTOPADRAO;
		tributacao = (Tributacao) findTributacaoDeAcordoComTipoPedidoEUF(tributacaoFilter, cdTipoPedido, cdUf);
		return tributacao;
	}

	private Tributacao findTributacaoParaCdTributacaoCliente2(Cliente cliente, String cdUf, Tributacao tributacaoFilter, String cdTipoPedido, Tributacao tributacao) throws SQLException {
		tributacaoFilter.cdTributacaoCliente = cliente.cdTributacaoCliente2;
		if (tributacaoFilter.cdTributacaoCliente == null) return null;
		tributacao = (Tributacao) findTributacaoDeAcordoComTipoPedidoEUF(tributacaoFilter, cdTipoPedido, cdUf);
		return tributacao;
	}
    
    private Tributacao findTributacaoDeAcordoComTipoPedidoEUF(Tributacao tributacaoFilter, String cdTipoPedido, String cdUf) throws SQLException {
    	String cdUfOriginal = tributacaoFilter.cdUf;
    	tributacaoFilter.cdTipoPedido = cdTipoPedido;
    	tributacaoFilter.cdUf = ValueUtil.isNotEmpty(cdUf) ? cdUf : Tributacao.CDUFVALORPADRAO;
    	Tributacao tributacao = (Tributacao) findByRowKeyInCache(tributacaoFilter);
    	if (tributacao == null && !Tributacao.CDTIPOPEDIDOVALORPADRAO.equals(cdTipoPedido) && !Tributacao.CDUFVALORPADRAO.equals(cdUf)) {
    		tributacao = findTributacaoDeAcordoComTipoPedidoEUF(tributacaoFilter, cdTipoPedido, Tributacao.CDUFVALORPADRAO);
    	}
    	if (tributacao == null && !Tributacao.CDTIPOPEDIDOVALORPADRAO.equals(cdTipoPedido)) {
    		tributacao = findTributacaoDeAcordoComTipoPedidoEUF(tributacaoFilter, Tributacao.CDTIPOPEDIDOVALORPADRAO, ValueUtil.isNotEmpty(cdUfOriginal) ? cdUfOriginal : cdUf);
    	}
    	if (tributacao == null &&  !Tributacao.CDUFVALORPADRAO.equals(cdUf)) {
    		tributacao = findTributacaoDeAcordoComTipoPedidoEUF(tributacaoFilter, Tributacao.CDTIPOPEDIDOVALORPADRAO, Tributacao.CDUFVALORPADRAO);
    	}
    	return tributacao;
    }
    
	public void loadCacheTributacao(Cliente cliente) throws SQLException {
		if (isCacheInitialized()) {
			if (ValueUtil.valueEquals(cdRepresentanteFiltroCache, SessionLavenderePda.getRepresentante().cdRepresentante) 
					&& ValueUtil.valueEquals(cdEmpresaFiltroCache, SessionLavenderePda.cdEmpresa) 
					&& ValueUtil.valueEquals(cdClienteFiltroCache, cliente.cdCliente) 
					&& ValueUtil.valueEquals(cdTributacaoClienteFiltroCache[0], cliente.cdTributacaoCliente)
					&& ValueUtil.valueEquals(cdTributacaoClienteFiltroCache[1], cliente.cdTributacaoCliente2)) {
				return;
			}
			clearCache();
		}
		Tributacao tributacaoFilter = new Tributacao();
    	tributacaoFilter.cdEmpresa = cdEmpresaFiltroCache = SessionLavenderePda.cdEmpresa;
    	tributacaoFilter.cdRepresentante = cdRepresentanteFiltroCache = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	cdClienteFiltroCache = cliente.cdCliente;
    	if (ValueUtil.isNotEmpty(cliente.cdTributacaoCliente)) {
    		if (ValueUtil.isNotEmpty(cliente.cdTributacaoCliente2)) {
    			tributacaoFilter.cdTributacaoClienteFilter = new String[2];
    			tributacaoFilter.cdTributacaoClienteFilter[0] = cdTributacaoClienteFiltroCache[0] = tributacaoFilter.cdTributacaoCliente = cliente.cdTributacaoCliente;
    			tributacaoFilter.cdTributacaoClienteFilter[1] = cdTributacaoClienteFiltroCache[1] = cliente.cdTributacaoCliente2;
    		} else {
    			tributacaoFilter.cdTributacaoCliente = cdTributacaoClienteFiltroCache[0] = cliente.cdTributacaoCliente;
    			cdTributacaoClienteFiltroCache[1] = cliente.cdTributacaoCliente2;
    		}
    	}
		initCacheByExample(tributacaoFilter);
	}

}
