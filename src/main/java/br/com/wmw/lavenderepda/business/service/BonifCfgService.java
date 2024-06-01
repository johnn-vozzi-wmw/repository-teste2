package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgCategoria;
import br.com.wmw.lavenderepda.business.domain.BonifCfgCliente;
import br.com.wmw.lavenderepda.business.domain.BonifCfgConjunto;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFamilia;
import br.com.wmw.lavenderepda.business.domain.BonifCfgLinha;
import br.com.wmw.lavenderepda.business.domain.BonifCfgProduto;
import br.com.wmw.lavenderepda.business.domain.BonifCfgRepresentante;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.validation.BonifCfgContaCorrenteException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BonifCfgDbxDao;
import totalcross.util.Vector;

public class BonifCfgService extends CrudService {
	
	private static BonifCfgService instance;
	
	public static BonifCfgService getInstance() {
		if (instance == null) {
			instance = new BonifCfgService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		//Esta entidade não é cadastrada no app
	}

	@Override
	protected CrudDao getCrudDao() {
		return BonifCfgDbxDao.getInstance();
	}
	
	private Vector getBonifCfgAplicaveis(BonifCfg bonCfg) throws SQLException {
		return ((BonifCfgDbxDao)getCrudDao()).findAllBonifCfgByExample(bonCfg);
	}
	
	private int countBonifCfgAplicaveis(BonifCfg bonCfg) throws SQLException {
		return BonifCfgDbxDao.getInstance().countAllBonifCfgByExample(bonCfg);
	}
	
	public Vector getBonifCfgAplicaveis(ItemPedido itemPedido) throws SQLException {
		BonifCfg bonCfg = getBonifCfgFilter(itemPedido);
		
		return getBonifCfgAplicaveis(bonCfg);
	}

	private BonifCfg getBonifCfgFilter(ItemPedido itemPedido) throws SQLException {
		Produto produto = itemPedido.getProduto();
		Cliente cliente = itemPedido.pedido.getCliente();
		return getBonifCfgFilter(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.isItemBonificacao(), itemPedido.isItemVendaNormal(), produto, cliente);
	}
	
	public Vector getBonifCfgListByPedido(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		BonifCfg bonCfg = getBonifCfgFilter(pedido.cdEmpresa, pedido.cdRepresentante, pedido.isPedidoBonificacao(), pedido.isPedidoVenda(), null, cliente);
		bonCfg.cdTipoRegraBonificacao = BonifCfg.CDTIPOREGRA_CONTACORRENTE;
		return getBonifCfgAplicaveis(bonCfg);
	}
	
	public Vector getBonifCfgListByCliente(Cliente cliente) throws SQLException {
		BonifCfg bonCfg = getBonifCfgFilter(cliente.cdEmpresa, cliente.cdRepresentante, false, false, null, cliente);
		bonCfg.cdTipoRegraBonificacao = BonifCfg.CDTIPOREGRA_CONTACORRENTE;
		return getBonifCfgAplicaveis(bonCfg);
	}
	
	public boolean isPedidoComBonifCfg(Pedido pedido) throws SQLException {
		Cliente cliente = pedido.getCliente();
		BonifCfg bonCfg = getBonifCfgFilter(pedido.cdEmpresa, pedido.cdRepresentante, pedido.isPedidoBonificacao(), pedido.isPedidoVenda(), null, cliente);
		bonCfg.cdTipoRegraBonificacao = BonifCfg.CDTIPOREGRA_CONTACORRENTE;
		return countBonifCfgAplicaveis(bonCfg) > 0;
	}

	protected BonifCfg getBonifCfgFilter(String cdEmpresa, String cdRepresentante, boolean isBonificacao, boolean isVendaNormal, Produto produto, Cliente cliente) {
		BonifCfg bonCfg = new BonifCfg();
		bonCfg.cdEmpresa = cdEmpresa;
		
		String flPermiteBonificar = isBonificacao ? ValueUtil.VALOR_SIM : null;
		String flGeraBonificacao = isVendaNormal ? ValueUtil.VALOR_SIM : null;
		
		if (produto != null) {
			if (LavenderePdaConfig.isUsaPoliticaBonificacaoLinha()) {
				BonifCfgLinha bonCfgLin = new BonifCfgLinha();
				bonCfgLin.cdEmpresa = cdEmpresa;
				bonCfgLin.cdLinha = produto.cdLinha;
				bonCfgLin.flPermiteBonificar = flPermiteBonificar;
				bonCfgLin.flGeraBonificacao = flGeraBonificacao;
				bonCfg.setBonCfgLinha(bonCfgLin);
			}
			if (LavenderePdaConfig.isUsaPoliticaBonificacaoFamilia()) {
				BonifCfgFamilia bonCfgFam = new BonifCfgFamilia();
				bonCfgFam.cdEmpresa = cdEmpresa;
				bonCfgFam.cdFamilia = produto.cdFamiliaPadrao;
				bonCfgFam.flPermiteBonificar = flPermiteBonificar;
				bonCfgFam.flGeraBonificacao = flGeraBonificacao;
				bonCfgFam.setProdutoFilter(produto);
				bonCfg.setBonCfgFam(bonCfgFam);
			}
			
			if (LavenderePdaConfig.isUsaPoliticaBonificacaoConjuntos()) {
				BonifCfgConjunto bonCfgConjunto = new BonifCfgConjunto();
				bonCfgConjunto.cdEmpresa = cdEmpresa;
				bonCfgConjunto.cdConjunto = produto.cdConjunto;
				bonCfgConjunto.flPermiteBonificar = flPermiteBonificar;
				bonCfgConjunto.flGeraBonificacao = flGeraBonificacao;
				bonCfg.setBonifCfgConjunto(bonCfgConjunto);
			}
			if (LavenderePdaConfig.isUsaPoliticaBonificacaoProduto()) {
				BonifCfgProduto bonCfgPrd = new BonifCfgProduto();
				bonCfgPrd.cdEmpresa = cdEmpresa;
				bonCfgPrd.cdProduto = produto.cdProduto;
				bonCfgPrd.flGeraBonificacao = flGeraBonificacao;
				bonCfgPrd.flPermiteBonificar = flPermiteBonificar;
				bonCfg.setBonifCfgProduto(bonCfgPrd);
			}
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoCategoria()) {
			BonifCfgCategoria bonCfgCat = new BonifCfgCategoria();
			if (cliente != null) {
				bonCfgCat.cdEmpresa = cdEmpresa;
				bonCfgCat.cdCategoria = cliente.cdCategoria;
			}
			bonCfg.setBonCfgCat(bonCfgCat);
		}
		
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoRepresentante()) {
			BonifCfgRepresentante bonCfgRep = new BonifCfgRepresentante();
			bonCfgRep.cdEmpresa = cdEmpresa;
			bonCfgRep.cdRepresentante = cdRepresentante;
			bonCfg.setBonCfgRep(bonCfgRep);
		}

		if (LavenderePdaConfig.isUsaPoliticaBonificacaoClientes()) {
			BonifCfgCliente bonCfgCli = new BonifCfgCliente();
			bonCfgCli.cdEmpresa = cdEmpresa;
			bonCfgCli.cdCliente = cliente.cdCliente;
			bonCfg.setBonifCfgCliente(bonCfgCli);
		}
		return bonCfg;
	}

	public BonifCfg getBonifCfgByFaixaQtde(BonifCfgFaixaQtde bonifCfgFaixaQtdePercorrida) throws SQLException {
		BonifCfg bonifCfgFilter = new BonifCfg();
		bonifCfgFilter.cdEmpresa = bonifCfgFaixaQtdePercorrida.cdEmpresa;
		bonifCfgFilter.cdBonifCfg = bonifCfgFaixaQtdePercorrida.cdBonifCfg;
		return (BonifCfg) BonifCfgService.getInstance().findByPrimaryKey(bonifCfgFilter);
	}
	
	public boolean isItemPedidoPossuiBonifCfgAplicaveis(ItemPedido itemPedido) throws SQLException {
		BonifCfg bonCfg = getBonifCfgFilter(itemPedido);
		return countBonifCfgAplicaveis(bonCfg) > 0;
	}
	
	private BonifCfg findBonifCfgByItemBonifCfg(ItemPedidoBonifCfg itemBon) throws SQLException {
		BonifCfg bonifCfgFilter = new BonifCfg();
		bonifCfgFilter.cdEmpresa = itemBon.cdEmpresa;
		bonifCfgFilter.cdBonifCfg = itemBon.cdBonifCfg;
		return (BonifCfg) BonifCfgService.getInstance().findByPrimaryKey(bonifCfgFilter);
	}
	
	public void validateBonificacaoContaCorrente(Pedido pedido) throws SQLException {
    	Vector itemPedidoBonifCfg = ItemPedidoBonifCfgService.getInstance().findAllItensBonifCfgByPedido(pedido, null);
    	int size = itemPedidoBonifCfg.size();
    	for (int i = 0; i < size; i++) {
    		ItemPedidoBonifCfg itemPedidoBon = (ItemPedidoBonifCfg) itemPedidoBonifCfg.items[i];
    		BonifCfg bonifCfg = findBonifCfgByItemBonifCfg(itemPedidoBon);
    		if (bonifCfg.qtSaldoContaCorrente < itemPedidoBon.qtBonificacao) {
    			throw new BonifCfgContaCorrenteException(Messages.BONIFCFGCONTACORRENTE_SALDO_INSUFICIENTE_AVISO, bonifCfg.qtSaldoContaCorrente, itemPedidoBon.qtBonificacao);
    		}
    	}
    }
    
    public void ajustaBonifContaCorrente(Pedido pedido) throws SQLException {
    	Vector itemPedidoBonifCfg = ItemPedidoBonifCfgService.getInstance().findAllItensBonifCfgByPedido(pedido, null);
    	int size = itemPedidoBonifCfg.size();
    	Map<String, BonifCfg> mapBonifCfgContaCorrente = new HashMap<>(size);
    	for (int i = 0; i < size; i++) {
    		ItemPedidoBonifCfg itemPedidoBon = (ItemPedidoBonifCfg) itemPedidoBonifCfg.items[i];
    		BonifCfg bonifCfg = getBonifCfgContaCorrente(mapBonifCfgContaCorrente, itemPedidoBon);
    		if (bonifCfg.qtSaldoContaCorrente == 0d) {
    			ItemPedidoBonifCfgService.getInstance().delete(itemPedidoBon);
    		} else if (bonifCfg.qtSaldoContaCorrente < itemPedidoBon.qtBonificacao) {
    			itemPedidoBon.qtBonificacao = bonifCfg.qtSaldoContaCorrente;
    			bonifCfg.qtSaldoContaCorrente -= itemPedidoBon.qtBonificacao;
    			ItemPedidoBonifCfgService.getInstance().update(itemPedidoBon);
    		} else {
    			bonifCfg.qtSaldoContaCorrente -= itemPedidoBon.qtBonificacao;
    		}
    	}
    }
    
    private BonifCfg getBonifCfgContaCorrente(Map<String, BonifCfg> mapBonifCfgContaCorrente, ItemPedidoBonifCfg itemPedidoBon) throws SQLException {
    	if (mapBonifCfgContaCorrente.containsKey(itemPedidoBon.getBonifCfg().getRowKey())) {
    		return mapBonifCfgContaCorrente.get(itemPedidoBon.getBonifCfg().getRowKey());
    	}
    	BonifCfg bonifCfg = findBonifCfgByItemBonifCfg(itemPedidoBon);
    	mapBonifCfgContaCorrente.put(bonifCfg.getRowKey(), bonifCfg);
    	return bonifCfg;
    }
    
    public boolean aplicaBonifContaCorrente(Pedido pedido) throws SQLException {
    	boolean houveAlteracao = false;
    	Vector itemPedidoList = pedido.itemPedidoList;
    	pedido.itemTotalmenteConvertidoBonifList = new Vector(0);
    	int size = itemPedidoList.size();
    	for (int i = 0; i < size; i++) {
    		ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
    		ItemPedidoBonifCfg itemPedidoBon = ItemPedidoBonifCfgService.getInstance().findItemPedidoBonifCfgByItemPedido(itemPedido);
    		if (itemPedidoBon != null) {
    			if (itemPedido.qtItemFisico > itemPedidoBon.qtBonificacao) {
    				itemPedido.qtItemFisico -= itemPedidoBon.qtBonificacao;
	    			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_QTD;
	    			ItemPedidoService.getInstance().calculate(itemPedido, pedido);
	    			houveAlteracao = pedido.aplicouBonifContaCorrente = true;
    			} else {
    				pedido.itemTotalmenteConvertidoBonifList.addElement(itemPedido);
    			}
    		}
    	}
    	if (houveAlteracao) {
    		PedidoService.getInstance().calculate(pedido);
    	}
    	return houveAlteracao;
    }
    
    public void criaPedidoBonificado(Pedido pedido) throws SQLException {
    	Vector itemPedidoList = pedido.itemPedidoList;
    	if (ValueUtil.isNotEmpty(pedido.itemTotalmenteConvertidoBonifList)) {
    		itemPedidoList = VectorUtil.concatVectors(itemPedidoList, pedido.itemTotalmenteConvertidoBonifList);
    		pedido.itemTotalmenteConvertidoBonifList.removeAllElements();
    	}
    	int size = itemPedidoList.size();
    	Vector novoItemPedidoList = new Vector(size);
    	for (int i = 0; i < size; i++) {
    		ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
    		ItemPedidoBonifCfg itemPedidoBon = ItemPedidoBonifCfgService.getInstance().findItemPedidoBonifCfgByItemPedido(itemPedido);
    		if (itemPedidoBon != null) {
    			BonifCfg bonifCfg = findBonifCfgByItemBonifCfg(itemPedidoBon);
    			ItemPedido itemPedidoNovo = (ItemPedido) itemPedido.clone();
    			itemPedidoNovo.qtItemFisico = itemPedidoBon.qtBonificacao;
    			itemPedidoNovo.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
    			novoItemPedidoList.addElement(itemPedidoNovo);
    			bonifCfg.qtSaldoContaCorrente -= itemPedidoBon.qtBonificacao;
    			updateSaldoContaCorrente(bonifCfg);
    			EstoqueService.getInstance().recalculaEstoqueConsumido(itemPedidoBon.cdProduto);
    		}
    	}
    	if (ValueUtil.isNotEmpty(novoItemPedidoList)) {
    		String nuPedidoOriginal = pedido.nuPedido;
    		TipoPedido tipoPedido = TipoPedidoService.getInstance().findTipoPedidoBonifContaCorrente(pedido.cdEmpresa, pedido.cdRepresentante); 
    		if (tipoPedido != null) {
    			pedido.cdTipoPedido = tipoPedido.cdTipoPedido;
    		}
    		Pedido pedidoNovo = ClienteService.getInstance().copyAndInsertPedidoCliente(pedido, pedido.getCliente(), novoItemPedidoList, false, false, false);
    		pedidoNovo.nuPedidoRelBonificacao = nuPedidoOriginal;
    		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedido() || LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
    			tipoPedido = pedido.getTipoPedido();
    			if (LavenderePdaConfig.usaRetornoAutomaticoDadosNfe || LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
    				pedido.flGeraNfe = tipoPedido != null ? tipoPedido.flGeraNfe : "";
    			}
    		}
    		PedidoService.getInstance().update(pedidoNovo);
    		PedidoService.getInstance().fecharPedido(pedidoNovo);
    	}
    }
    
    private void updateSaldoContaCorrente(BonifCfg bonifCfg) throws SQLException {
    	BonifCfgDbxDao.getInstance().updateSaldoContaCorrente(bonifCfg);
    }
	
}
