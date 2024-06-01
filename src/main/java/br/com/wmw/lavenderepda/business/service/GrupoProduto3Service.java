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
import br.com.wmw.lavenderepda.business.domain.GrupoProduto3;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto3PdbxDao;
import totalcross.util.Vector;

public class GrupoProduto3Service extends CrudService {

    private static GrupoProduto3Service instance;

    private GrupoProduto3Service() {
        //--
    }

    public static GrupoProduto3Service getInstance() {
        if (instance == null) {
            instance = new GrupoProduto3Service();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return GrupoProduto3PdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }


    public GrupoProduto3 getGrupoProduto(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3) throws SQLException {
		GrupoProduto3 grupoProduto3Filter = new GrupoProduto3();
		grupoProduto3Filter.cdGrupoProduto1 = cdGrupoProduto1;
		grupoProduto3Filter.cdGrupoProduto2 = cdGrupoProduto2;
		grupoProduto3Filter.cdGrupoProduto3 = cdGrupoProduto3;
		return (GrupoProduto3)findByRowKey(grupoProduto3Filter.getRowKey());
    }

    public String getDsGrupoProduto(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3) throws SQLException {
    	GrupoProduto3 grupoProduto3Filter = new GrupoProduto3();
    	grupoProduto3Filter.cdGrupoProduto1 = cdGrupoProduto1;
    	grupoProduto3Filter.cdGrupoProduto2 = cdGrupoProduto2;
    	grupoProduto3Filter.cdGrupoProduto3 = cdGrupoProduto3;
    	grupoProduto3Filter.dsGrupoProduto3 = findColumnByRowKey(grupoProduto3Filter.getRowKey(), "DSGRUPOPRODUTO3");
    	return grupoProduto3Filter.toString();
    }

    public String getLabelGrupoProduto3() {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			if (temp.length > 2) {
				return temp[2];
			}
		}
    	return Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3;
    }

    public String getDsGrupoProduto(Produto produto) throws SQLException {
    	return getDsGrupoProduto(produto.cdGrupoProduto1, produto.cdGrupoProduto2, produto.cdGrupoProduto3);
    }

    public Vector loadGrupoProduto3(Pedido pedido, String cdGrupoProduto1, String cdGrupoProduto2) throws SQLException {
    	GrupoProduto3 grupoProduto3Filter = new GrupoProduto3();
    	grupoProduto3Filter.cdGrupoProduto1 = cdGrupoProduto1;
    	grupoProduto3Filter.cdGrupoProduto2 = cdGrupoProduto2;
    	try {
	    	prepareGrupoProdTipoPedFilter(pedido, grupoProduto3Filter, cdGrupoProduto1, cdGrupoProduto2);
    		prepareGrupoProdExistentesFilter(pedido, grupoProduto3Filter, cdGrupoProduto1, cdGrupoProduto2);
    	} catch (FilterNotInformedException e) {
    		return new Vector(0);
    	}
    	return findAllByExample(grupoProduto3Filter);
    }
    
    private void prepareGrupoProdExistentesFilter(Pedido pedido, GrupoProduto3 grupoProduto3Filter, String cdGrupoProduto1, String cdGrupoProduto2) throws SQLException {
    	if (LavenderePdaConfig.usaFiltroApenasGruposProdutosExistentesCargaRepresentante) {
	    	TabelaPreco tabPreco = pedido != null ? pedido.getTabelaPreco() : null;
			String cdTabelaPreco = tabPreco != null ? tabPreco.cdTabelaPreco : null;
			grupoProduto3Filter.produtoFilter = new Produto();
			grupoProduto3Filter.produtoFilter.cdTabelaPreco = cdTabelaPreco;
			grupoProduto3Filter.produtoFilter.cdGrupoProduto1 = cdGrupoProduto1;
			grupoProduto3Filter.produtoFilter.cdGrupoProduto2 = cdGrupoProduto2;
			grupoProduto3Filter.filterByGrupoProdutoExistente = true;
			if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && pedido != null) {
				grupoProduto3Filter.restricaoFilter = RestricaoService.getInstance().getRestricaoFilter("PROD.CDPRODUTO", SessionLavenderePda.getCliente().cdCliente, null, 1, null, true, false, false);;
				grupoProduto3Filter.addCteRestricao = true;
			}
    	}
	}
    
    private void prepareGrupoProdTipoPedFilter(Pedido pedido, GrupoProduto3 grupoProduto3Filter, String cdGrupoProduto1, String cdGrupoProduto2) throws SQLException {
    	if (pedido != null && LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
    		if (pedido.getTipoPedido() == null) {
    			throw new FilterNotInformedException();
    		}
    		grupoProduto3Filter.grupoProdTipoPedFilter = new GrupoProdTipoPed();
    		grupoProduto3Filter.grupoProdTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		grupoProduto3Filter.grupoProdTipoPedFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProdTipoPed.class);
    		grupoProduto3Filter.grupoProdTipoPedFilter.cdGrupoProduto1 = !LavenderePdaConfig.ocultaGrupoProduto1 ? cdGrupoProduto1 : null;
    		grupoProduto3Filter.grupoProdTipoPedFilter.cdGrupoProduto2 = cdGrupoProduto2;
    		grupoProduto3Filter.grupoProdTipoPedFilter.cdTipoPedido = pedido.getTipoPedido().cdTipoPedido;
    		grupoProduto3Filter.grupoProdTipoPedFilter.excecaoGrupoProdutoFilter = pedido.getTipoPedido().isFlExcecaoGrupoProduto();
    	}
    }

}