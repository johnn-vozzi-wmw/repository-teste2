package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto4;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.GrupoProduto4PdbxDao;
import totalcross.util.Vector;

public class GrupoProduto4Service extends CrudService {

    private static GrupoProduto4Service instance;
    
    private GrupoProduto4Service() { }
    
    public static GrupoProduto4Service getInstance() {
        if (instance == null) {
            instance = new GrupoProduto4Service();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return GrupoProduto4PdbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) throws SQLException { }

	public Vector loadGrupoProduto4(Pedido pedido, String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3) throws SQLException {
		GrupoProduto4 grupoProduto4Filter = new GrupoProduto4();
    	grupoProduto4Filter.cdGrupoProduto1 = cdGrupoProduto1;
    	grupoProduto4Filter.cdGrupoProduto2 = cdGrupoProduto2;
    	grupoProduto4Filter.cdGrupoProduto3 = cdGrupoProduto3;
		prepareGrupoProdExistentesFilter(pedido, grupoProduto4Filter, cdGrupoProduto1, cdGrupoProduto2, cdGrupoProduto3);
    	
		return findAllByExample(grupoProduto4Filter);
    }
	
	 private void prepareGrupoProdExistentesFilter(Pedido pedido, GrupoProduto4 grupoProduto4Filter, String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3) throws SQLException {
	    	if (LavenderePdaConfig.usaFiltroApenasGruposProdutosExistentesCargaRepresentante) {
		    	TabelaPreco tabPreco = pedido != null ? pedido.getTabelaPreco() : null;
				String cdTabelaPreco = tabPreco != null ? tabPreco.cdTabelaPreco : null;
				grupoProduto4Filter.produtoFilter = new Produto();
				grupoProduto4Filter.produtoFilter.cdTabelaPreco = cdTabelaPreco;
				grupoProduto4Filter.produtoFilter.cdGrupoProduto1 = cdGrupoProduto1;
				grupoProduto4Filter.produtoFilter.cdGrupoProduto2 = cdGrupoProduto2;
				grupoProduto4Filter.produtoFilter.cdGrupoProduto3 = cdGrupoProduto3;
				grupoProduto4Filter.filterByGrupoProdutoExistente = true;
				if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && pedido != null) {
					grupoProduto4Filter.restricaoFilter = RestricaoService.getInstance().getRestricaoFilter("PROD.CDPRODUTO", SessionLavenderePda.getCliente().cdCliente, null, 1, null, true, false, false);
					grupoProduto4Filter.addCteRestricao = true;
				}
	    	}
		}
}