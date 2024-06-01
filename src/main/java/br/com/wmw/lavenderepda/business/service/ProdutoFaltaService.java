package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoFalta;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoFaltaDbxDao;
import totalcross.util.Date;

public class ProdutoFaltaService extends CrudService {

    private static ProdutoFaltaService instance;
    
    private ProdutoFaltaService() {
        //--
    }
    
    public static ProdutoFaltaService getInstance() {
        if (instance == null) {
            instance = new ProdutoFaltaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoFaltaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public void registraFaltaProduto(ItemPedido itemPedido, int qtFaltante) throws SQLException {
		insertOrUpdate(createProdutoFalta(itemPedido, qtFaltante, 0d, null));
	}

	public void registraFaltaProduto(ItemPedido itemPedido, int qtFaltante, double qtAtendida) throws SQLException {
		insertOrUpdate(createProdutoFalta(itemPedido, qtFaltante, qtAtendida, null));
	}

	public void registraFaltaProdutoEstoquePrevisto(ItemPedido itemPedido, int qtFaltante, double qtAtendida, Date dtEstoque) throws SQLException {
		insertOrUpdate(createProdutoFalta(itemPedido, qtFaltante, qtAtendida, dtEstoque));
	}
	
	private ProdutoFalta createProdutoFalta(ItemPedido itemPedido, int qtFaltante, double qtAtendida, Date dtEstoque) throws SQLException {
		ProdutoFalta produtoFalta = new ProdutoFalta();
		produtoFalta.cdEmpresa = itemPedido.cdEmpresa;
		produtoFalta.cdRepresentante = itemPedido.cdRepresentante;
		produtoFalta.cdCliente = itemPedido.pedido.cdCliente;
		produtoFalta.cdProdutoFalta = generateIdGlobal();
		produtoFalta.cdProduto = itemPedido.cdProduto;
		produtoFalta.cdTabelaPreco = itemPedido.cdTabelaPreco;
		produtoFalta.dtFaltante = dtEstoque != null ? dtEstoque : DateUtil.getCurrentDate(); 
		produtoFalta.dtRegistro = DateUtil.getCurrentDate();
		produtoFalta.qtFaltante = qtFaltante; 
		produtoFalta.qtAtendida = ValueUtil.getIntegerValue(qtAtendida);
		produtoFalta.vlPctDesconto = itemPedido.vlPctDesconto;
		produtoFalta.nuPedido = itemPedido.nuPedido;
		produtoFalta.cdUnidade = itemPedido.cdUnidade;
		if (LavenderePdaConfig.usaControleEstoquePorEstoquePrevisto()) {
			produtoFalta.dtEntregaPedido = itemPedido.pedido.dtEntrega;
		}
		return produtoFalta;
	}

	public void deleteProdutoFalta(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaRegistroProdutoFaltante || LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto()) {
			ProdutoFalta produtoFaltaFilter = new ProdutoFalta();
			produtoFaltaFilter.cdEmpresa = itemPedido.cdEmpresa;
			produtoFaltaFilter.cdRepresentante = itemPedido.cdRepresentante;
			produtoFaltaFilter.cdCliente = itemPedido.pedido.cdCliente;
			produtoFaltaFilter.cdProduto = itemPedido.cdProduto;
			produtoFaltaFilter.dtFaltante = itemPedido.pedido.dtEmissao;
			deleteAllByExample(produtoFaltaFilter);
		}
	}
	
	public void deleteProdutoFaltaByPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaRegistroProdutoFaltante || LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto()) {
			ProdutoFalta produtoFaltaFilter = new ProdutoFalta();
			produtoFaltaFilter.cdEmpresa = pedido.cdEmpresa;
			produtoFaltaFilter.cdRepresentante = pedido.cdRepresentante;
			produtoFaltaFilter.cdCliente = pedido.cdCliente;
			produtoFaltaFilter.nuPedido = pedido.nuPedido;
			produtoFaltaFilter.dtFaltante = pedido.dtEmissao;
			deleteAllByExample(produtoFaltaFilter);
		}
	}
	
	public boolean verificaProdutoFaltaEstoquePrevistoNaoExiste(ItemPedido itemPedido) throws SQLException {
		ProdutoFalta produtoFaltaFilter = new ProdutoFalta();
		produtoFaltaFilter.cdEmpresa = itemPedido.cdEmpresa;
		produtoFaltaFilter.cdRepresentante = itemPedido.cdRepresentante;
		produtoFaltaFilter.cdCliente = itemPedido.pedido.cdCliente;
		produtoFaltaFilter.cdProduto = itemPedido.cdProduto;
		produtoFaltaFilter.nuPedido = itemPedido.nuPedido;
		produtoFaltaFilter.dtEntregaPedido = itemPedido.pedido.dtEntrega;
		return ProdutoFaltaDbxDao.getInstance().countProdutoFaltaEstoquePrevisto(produtoFaltaFilter) == 0;
	}
	
	private void insertOrUpdate(ProdutoFalta produtoFalta) throws SQLException {
		try {
			insert(produtoFalta);
		} catch (Throwable e) {
			update(produtoFalta);
		}
	}
}