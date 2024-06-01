package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoAud;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoAudDbxDao;
import totalcross.sql.ResultSet;

public class ItemPedidoAudService extends CrudService {

    private static ItemPedidoAudService instance;
    
    private ItemPedidoAudService() {
        //--
    }
    
    public static ItemPedidoAudService getInstance() {
        if (instance == null) {
            instance = new ItemPedidoAudService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemPedidoAudDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public ItemPedidoAud getItemPedidoAud(ItemPedido itemPedido) throws SQLException {
		ItemPedidoAud itemPedidoAudFilter = new ItemPedidoAud();
		addChaveItemPedido(itemPedido, itemPedidoAudFilter);
		return (ItemPedidoAud) findByRowKey(itemPedidoAudFilter.getRowKey());
	}

	private void addChaveItemPedido(ItemPedido itemPedido,ItemPedidoAud itemPedidoAud) {
		itemPedidoAud.cdEmpresa = itemPedido.cdEmpresa;
		itemPedidoAud.cdRepresentante = itemPedido.cdRepresentante;
		itemPedidoAud.nuPedido = itemPedido.nuPedido;
		itemPedidoAud.cdProduto = itemPedido.cdProduto;
		itemPedidoAud.flOrigemPedido = itemPedido.flOrigemPedido;
		itemPedidoAud.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedidoAud.nuSeqProduto = itemPedido.nuSeqProduto;
	}

	public void insert(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isItemVendaNormal() && (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade)) {
			ItemPedidoAud itemPedidoAud = itemPedido.getItemPedidoAud();
			addChaveItemPedido(itemPedido, itemPedidoAud);
			atualizaInfosAntesSalvar(itemPedido, itemPedidoAud);
			insert(itemPedidoAud);
		}
	}

	private void atualizaInfosAntesSalvar(ItemPedido itemPedido, ItemPedidoAud itemPedidoAud) throws SQLException {
		itemPedidoAud.dtInsercaoItem = DateUtil.getCurrentDate();
		itemPedidoAud.vlPctMargemRentabilidade = itemPedido.getItemTabelaPreco().vlPctMargemRentabilidade;
		Tributacao tributacao = itemPedido.getTributacaoItem();
		if (tributacao != null) {
			itemPedidoAud.vlPctPis = tributacao.vlPctPis;
			itemPedidoAud.vlPctIcms = tributacao.vlPctIcms;
			itemPedido.vlIcms = tributacao.vlIcms;
			itemPedido.vlSt = tributacao.vlIcmsRetido;
		}
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemTabelaPreco != null) {
			if (LavenderePdaConfig.isUsaMargemProdutoAplicadaNoValorBaseVerba() && itemPedido.getProduto() != null) {
				itemPedido.vlBaseFlex = itemTabelaPreco.vlBase * (1 - itemPedido.getProduto().vlPctMargemProduto / 100);
			} else {
				itemPedido.vlBaseFlex = itemTabelaPreco.vlBase;
			}
			itemPedido.nuPromocao = itemTabelaPreco.nuPromocao;
			itemPedido.vlDescontoPromo = itemTabelaPreco.vlDescontoPromo;
			itemPedido.dtInicioPromocao = itemTabelaPreco.dtInicioPromocao;
		}
	}

	public void update(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isItemVendaNormal() && (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade)) {
			ItemPedidoAud itemPedidoAud = itemPedido.getItemPedidoAud();
			atualizaInfosAntesSalvar(itemPedido, itemPedidoAud);
			try {
				update(itemPedidoAud);
			} catch (Throwable e) {
			}
		}
	}

	public void delete(ItemPedido itemPedido) {
		if (itemPedido.isItemVendaNormal() && (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade)) {
			try {
				delete(itemPedido.getItemPedidoAud());
			} catch (Throwable e) {
			}
		}
	}
	
	public String findRSItemPedidoAud(Pedido pedido) throws SQLException {
		ItemPedidoAud itemPedidoAudFilter = createItemPedidoAudFilter(pedido);
		return ItemPedidoAudDbxDao.getInstance().findAllByExampleSql(itemPedidoAudFilter);
	}

	public ItemPedidoAud createItemPedidoAudFilter(Pedido pedido) {
		ItemPedidoAud itemPedidoAudFilter = new ItemPedidoAud();
		itemPedidoAudFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoAudFilter.cdRepresentante = pedido.cdRepresentante;
		itemPedidoAudFilter.flOrigemPedido = pedido.flOrigemPedido;
		itemPedidoAudFilter.nuPedido = pedido.nuPedido;
		return itemPedidoAudFilter;
	}
}