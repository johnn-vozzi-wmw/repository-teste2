package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProdTipoPed;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto2;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto2PdbxDao;
import totalcross.util.Vector;

public class GrupoProduto2Service extends CrudService {

    private static GrupoProduto2Service instance;

    private GrupoProduto2Service() {
        //--
    }

    public static GrupoProduto2Service getInstance() {
        if (instance == null) {
            instance = new GrupoProduto2Service();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return GrupoProduto2PdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getDsGrupoProduto(String cdGrupoProduto1, String cdGrupoProduto2) throws SQLException {
		GrupoProduto2 cdGrupoProduto2Filter = new GrupoProduto2();
		cdGrupoProduto2Filter.cdGrupoProduto1 = cdGrupoProduto1;
		cdGrupoProduto2Filter.cdGrupoProduto2 = cdGrupoProduto2;
		cdGrupoProduto2Filter.dsGrupoProduto2 = findColumnByRowKey(cdGrupoProduto2Filter.getRowKey(), "DSGRUPOPRODUTO2");
    	return cdGrupoProduto2Filter.toString();
    }

    public String getLabelGrupoProduto2() {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			if (temp.length > 1) {
				return temp[1];
			}
		}
    	return Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2;
    }

    public String getDsGrupoProduto(Produto produto) throws SQLException {
		GrupoProduto2 grupoProduto2Filter = new GrupoProduto2();
		grupoProduto2Filter.cdGrupoProduto1 = produto.cdGrupoProduto1;
		grupoProduto2Filter.cdGrupoProduto2 = produto.cdGrupoProduto2;
		grupoProduto2Filter.dsGrupoProduto2 = findColumnByRowKey(grupoProduto2Filter.getRowKey(), "DSGRUPOPRODUTO2");
    	return grupoProduto2Filter.toString();
    }
    
    public Vector loadGrupoProduto2(Pedido pedido, String cdGrupoProduto1) throws SQLException {
    	GrupoProduto2 grupoProduto2Filter = new GrupoProduto2();
    	grupoProduto2Filter.cdGrupoProduto1 = cdGrupoProduto1;
    	try {
	    	prepareGrupoProdTipoPedFilter(pedido, grupoProduto2Filter, cdGrupoProduto1);
    		prepareGrupoProdExistentesFilter(pedido, grupoProduto2Filter, cdGrupoProduto1);
    	} catch (FilterNotInformedException e) {
    		return new Vector(0);
    	}
    	return findAllByExample(grupoProduto2Filter);
    }
    
    private void prepareGrupoProdExistentesFilter(Pedido pedido, GrupoProduto2 grupoProduto2Filter, String cdGrupoProduto1) throws SQLException {
    	if (LavenderePdaConfig.usaFiltroApenasGruposProdutosExistentesCargaRepresentante) {
	    	TabelaPreco tabPreco = pedido != null ? pedido.getTabelaPreco() : null;
			String cdTabelaPreco = tabPreco != null ? tabPreco.cdTabelaPreco : null;
			grupoProduto2Filter.produtoFilter = new Produto();
			grupoProduto2Filter.produtoFilter.cdTabelaPreco = cdTabelaPreco;
			grupoProduto2Filter.produtoFilter.cdGrupoProduto1 = cdGrupoProduto1;
			grupoProduto2Filter.filterByGrupoProdutoExistente = true;
			if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && pedido != null) {
				grupoProduto2Filter.restricaoFilter = RestricaoService.getInstance().getRestricaoFilter("PROD.CDPRODUTO", SessionLavenderePda.getCliente().cdCliente, null, 1, null, true, false, false);
				grupoProduto2Filter.addCteRestricao = true;
			}
    	}
	}
    
    private void prepareGrupoProdTipoPedFilter(Pedido pedido, GrupoProduto2 grupoProduto2Filter, String cdGrupoProduto1) throws SQLException {
    	if (pedido != null && LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
    		if (pedido.getTipoPedido() == null) {
    			throw new FilterNotInformedException();
    		}
	    	grupoProduto2Filter.grupoProdTipoPedFilter = new GrupoProdTipoPed();
	    	grupoProduto2Filter.grupoProdTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    	grupoProduto2Filter.grupoProdTipoPedFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProdTipoPed.class);
	    	grupoProduto2Filter.grupoProdTipoPedFilter.cdGrupoProduto1 = !LavenderePdaConfig.ocultaGrupoProduto1 ? cdGrupoProduto1 : null;
	    	grupoProduto2Filter.grupoProdTipoPedFilter.cdTipoPedido = pedido.getTipoPedido().cdTipoPedido;
	    	grupoProduto2Filter.grupoProdTipoPedFilter.excecaoGrupoProdutoFilter = pedido.getTipoPedido().isFlExcecaoGrupoProduto();
    	}
    }

}