package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercial;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PoliticaComercialDbxDao;

import java.sql.SQLException;

public class PoliticaComercialService extends CrudService {

    private static PoliticaComercialService instance;
    
    private PoliticaComercialService() {
        //--
    }
    
    public static PoliticaComercialService getInstance() {
        if (instance == null) {
            instance = new PoliticaComercialService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PoliticaComercialDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }
    
    public PoliticaComercial findPoliticaComercial(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	Produto produto = itemPedido.getProduto();
	    PoliticaComercial politicaComercial = getFiltersPoliticaComercialPedido(pedido);
	    if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			politicaComercial.cdTabelaPreco = itemPedido.cdTabelaPreco;
		}
	    politicaComercial.cdProduto = itemPedido.cdProduto;
	    politicaComercial.cdLinha = produto.cdLinha;
	    politicaComercial.cdClasse = produto.cdClasse;
	    politicaComercial.cdGrupoProduto1 = produto.cdGrupoProduto1;
	    politicaComercial.cdConjunto = produto.cdConjunto;
	    politicaComercial.cdMarca = produto.cdMarca;
	    politicaComercial.cdColecao = produto.cdColecao;
	    politicaComercial.cdStatusColecao = produto.cdStatusColecao;
	    if (LavenderePdaConfig.usaFamiliaPadraoPoliticaComercial()) {
		    politicaComercial.cdFamilia = produto.cdFamiliaPadrao;
	    }
	    politicaComercial.cdRepresentantePolitica = itemPedido.cdRepresentante;
	    politicaComercial.dsReferencia = produto.dsReferencia;
	    politicaComercial.flVendaEncerrada = produto.flVendaEncerrada; 	
	    if (LavenderePdaConfig.isUsaAgrupadorKitPolitica() && itemPedido.isKitTipo3()) {
	    	Kit kit = KitService.getInstance().getKit(itemPedido.cdKit, pedido);
	    	politicaComercial.dsAgrupadorKit = kit.dsAgrupadorKit;
	    }
    	return PoliticaComercialDbxDao.getInstance().findByRegraPoliticaComercial(politicaComercial);
    }

	private static PoliticaComercial getFiltersPoliticaComercialPedido(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		PoliticaComercial politicaComercial = new PoliticaComercial();
		politicaComercial.cdEmpresa = pedido.cdEmpresa;
		politicaComercial.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		politicaComercial.cdGrupoCondPagtoPolitComerc = pedido.getCondicaoPagamento().cdGrupoCondPagtoPolitComerc;
		politicaComercial.cdCentroCusto = pedido.cdCentroCusto;
		politicaComercial.cdPlataformaVenda = pedido.cdPlataformaVenda;
		politicaComercial.cdCategoria = cliente.cdCategoria;
		politicaComercial.cdCliente = cliente.cdCliente;
		politicaComercial.cdGrupoDescCli = cliente.cdGrupoDescCli;
		politicaComercial.cdTabelaPreco = pedido.cdTabelaPreco;
		politicaComercial.cdRepresentantePolitica = pedido.cdRepresentante;
		return politicaComercial;
	}

	public void createTabelaTemporariaPoliticaComercialPedido(Pedido pedido) throws SQLException {
		PoliticaComercialDbxDao.getInstance().createTabelaTemporariaPoliticaComercialPedido(getFiltersPoliticaComercialPedido(pedido));
	}
	
	public void dropTabelaTemporariaPoliticaComercialPedido() throws SQLException {
		PoliticaComercialDbxDao.getInstance().dropTabelaTemporariaPoliticaComercialPedido();
	}
	
	 public PoliticaComercial findPoliticaComercialItemPedido(ItemPedido itemPedido) throws SQLException {
	 	PoliticaComercial politicaComercialFilter = new PoliticaComercial();
		politicaComercialFilter.cdEmpresa = itemPedido.cdEmpresa;
		politicaComercialFilter.cdPoliticaComercial = itemPedido.cdPoliticaComercial;
		return (PoliticaComercial) findByPrimaryKey(politicaComercialFilter);
	 }

}