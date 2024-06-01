package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.LoteProduto;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LoteProdutoPdbxDao;
import totalcross.util.Vector;

public class LoteProdutoService extends CrudService {

    private static LoteProdutoService instance;

    private LoteProdutoService() {
        //--
    }

    public static LoteProdutoService getInstance() {
        if (instance == null) {
            instance = new LoteProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return LoteProdutoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public LoteProduto findByItemPedido(ItemPedido itemPedido) throws SQLException {
    	LoteProduto loteProduto = instanceLoteProduto(itemPedido);
    	if (loteProduto.cdEmpresa != null && loteProduto.cdRepresentante != null && loteProduto.cdProduto != null && loteProduto.cdLoteproduto != null && loteProduto.flOrigemEstoque != null) {
    		return (LoteProduto)findByRowKey(loteProduto.getRowKey());	
		}
    	return null;
    }
    
    public Vector findAllByItemPedido(ItemPedido itemPedido, String cdLocal) throws SQLException {
    	LoteProduto loteFilter = instanceLoteProduto(itemPedido);
    	loteFilter.cdLocal = cdLocal;
    	loteFilter.agrupaCdLoteProduto = true;
    	return findAllByExample(loteFilter);
    }

	private LoteProduto instanceLoteProduto(ItemPedido itemPedido) {
		LoteProduto lote = new LoteProduto();
		lote.cdEmpresa = itemPedido.cdEmpresa;
		lote.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(LoteProduto.class);
		lote.cdProduto = itemPedido.cdProduto;
		lote.cdLoteproduto = itemPedido.cdLoteProduto;
		lote.cdTabelaPrecoFilter = ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco) ? itemPedido.cdTabelaPreco : itemPedido.pedido.cdTabelaPreco;
		lote.flOrigemEstoque = LoteProduto.FLORIGEMSALDOERP;
		return lote;
	}
    
    public boolean isProdutoPossuiLoteProduto(ItemPedido itemPedido) throws SQLException {
    	return countByExample(instanceLoteProduto(itemPedido)) > 0;
    }

	public void validateEstoque(ItemPedido itemPedido) throws SQLException {
		validateEstoque(itemPedido, true);
	}
	
	public void validateEstoque(ItemPedido itemPedido, boolean validaDiferenca) throws SQLException {
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto() && !ignoraEstoqueTipoPedido(itemPedido)) {
			LoteProduto loteProdutoFilter = new LoteProduto(itemPedido.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(LoteProduto.class), itemPedido.cdProduto, itemPedido.cdLoteProduto);
			double qtSaldoEstoque = LoteProdutoPdbxDao.getInstance().sumByExample(loteProdutoFilter, "QTESTOQUE");
			double qtItemFisicoConversaoUnidade = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()); 
			qtItemFisicoConversaoUnidade -= validaDiferenca ? ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getOldQtItemFisico()) : 0;
			if (qtSaldoEstoque < qtItemFisicoConversaoUnidade) {
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ESTOQUE_INSUFICIENTE, StringUtil.getStringValueToInterface(qtItemFisicoConversaoUnidade - qtSaldoEstoque)));
			}
		}
	}

	public double getQtEstoqueLote(LoteProduto lote) throws SQLException {
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
			return getQtEstoquePorLoteDisponivel(lote.cdEmpresa, lote.cdProduto, lote.cdLoteproduto);
		}
		return lote.qtEstoquedisponivel;
	}

	public double getQtEstoqueLoteDisponivel(String cdProduto) throws SQLException {
		return getQtEstoquePorLoteDisponivel(SessionLavenderePda.cdEmpresa, cdProduto, null);
	}

	public double getQtEstoquePorLoteDisponivel(String cdEmpresa, String cdProduto, String cdLoteProduto) throws SQLException {
		LoteProduto loteProdutoFilter = new LoteProduto(cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(LoteProduto.class), cdProduto, cdLoteProduto);
		return LoteProdutoPdbxDao.getInstance().sumByExample(loteProdutoFilter, "QTESTOQUE"); 
	}

	public void consomeEstoqueLote(ItemPedido itemPedido) throws SQLException {
		double qtEstoqueOld = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getOldQtItemFisico());
		LoteProduto loteProdutoFilter = new LoteProduto(itemPedido.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(LoteProduto.class), itemPedido.cdProduto, itemPedido.cdLoteProduto);
		loteProdutoFilter.flOrigemEstoque = LoteProduto.FLORIGEMSALDOPDA;
		loteProdutoFilter.qtEstoque = (ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()) - qtEstoqueOld) * -1;
		int updateResult = LoteProdutoPdbxDao.getInstance().updateEstoqueConsumidoPda(loteProdutoFilter);
		if (updateResult == 0) {
			insert(loteProdutoFilter);
		}
	}
	
	public void removeEstoqueConsumido(ItemPedido itemPedido) throws SQLException {
		LoteProduto loteProdutoFilter = new LoteProduto(itemPedido.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(LoteProduto.class), itemPedido.cdProduto, itemPedido.cdLoteProduto);
		loteProdutoFilter.flOrigemEstoque = LoteProduto.FLORIGEMSALDOPDA;
		loteProdutoFilter.qtEstoque = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
		LoteProdutoPdbxDao.getInstance().updateEstoqueConsumidoPda(loteProdutoFilter);
	}

	public void updateEstoquePdaToERP(Pedido pedido) throws SQLException {
    	if (pedido.getTipoPedido() != null && pedido.getTipoPedido().isIgnoraControleEstoque()) return;
		ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		itemPedidoFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		itemPedidoFilter.flOrigemPedido = Estoque.FLORIGEMESTOQUE_PDA;
		itemPedidoFilter.nuPedido = pedido.nuPedido;
		Vector listItemPedido = ItemPedidoService.getInstance().findAllByExampleUnique(itemPedidoFilter);
		for (int i = 0; i < listItemPedido.size(); i++) { 
			ItemPedido itemPedido = (ItemPedido) listItemPedido.items[i];
			removeEstoqueConsumido(itemPedido);
			double qtItemFisicoConversaoUnidade = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
			LoteProduto loteProdutoFilter = new LoteProduto(itemPedido.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(LoteProduto.class), itemPedido.cdProduto, itemPedido.cdLoteProduto);
			loteProdutoFilter.flOrigemEstoque = LoteProduto.FLORIGEMSALDOERP;
			loteProdutoFilter.qtEstoque = qtItemFisicoConversaoUnidade * -1;
			LoteProdutoPdbxDao.getInstance().updateEstoqueConsumidoPda(loteProdutoFilter);
		}
	}

	public Vector findLoteProdutoPor(String cdGrupoProduto1, LoteProduto loteProdutoFilter, String flOrigemEstoque, String cdLocal, boolean ignoraLocal) throws SQLException {
		return LoteProdutoPdbxDao.getInstance().findLoteProdutoPor(cdGrupoProduto1, loteProdutoFilter, flOrigemEstoque, cdLocal, ignoraLocal);
	}
	
	public boolean validaCdLocal(ItemPedido itemPedido, String cdLocal) throws SQLException {
		LoteProduto lote = findByItemPedido(itemPedido);
		return lote != null ? ValueUtil.valueEquals(cdLocal, lote.cdLocal) : false;
	}
	
	private boolean ignoraEstoqueTipoPedido(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.pedido == null || itemPedido.pedido.getTipoPedido() == null) return false;
		return itemPedido.pedido.getTipoPedido().isIgnoraControleEstoque();
	}

	public void consomeEstoqueLote(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		ItemPedido item;
		double oldQtFisico;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido) pedido.itemPedidoList.items[i];
			oldQtFisico = item.getOldQtItemFisico();
			item.setOldQtItemFisico(0.0);
			consomeEstoqueLote(item);
			item.setOldQtItemFisico(item.getQtItemFisico());
		}
	}

	public void removeEstoqueConsumido(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		ItemPedido item;
		for (int i = 0; i < size; i++) {
			item = (ItemPedido) pedido.itemPedidoList.items[i];
			removeEstoqueConsumido(item);
		}
	}
	
}