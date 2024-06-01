package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.KitTabPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemKitPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ItemKitService extends CrudService {

	private static ItemKitService instance;

	private ItemKitService() {
		//--
	}

	public static ItemKitService getInstance() {
		if (ItemKitService.instance == null) {
			ItemKitService.instance = new ItemKitService();
		}
		return ItemKitService.instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return ItemKitPdbxDao.getInstance();
	}

	//@Override
	public void validate(final BaseDomain domain) {
	}

	public void validateMinMaxItem(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		KitTabPreco kitTabPrecoFilter = new KitTabPreco();
		kitTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		kitTabPrecoFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		Vector kitTabPrecoList = KitTabPrecoService.getInstance().findAllByExample(kitTabPrecoFilter);
		//--
		ItemKit itemKitFilter = new ItemKit();
		itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemKitFilter.cdProduto = itemPedido.cdProduto;
		Vector result = ItemKitService.getInstance().findAllByExample(itemKitFilter);
		int sizeResults = result.size();
		ItemKit itemKit;
		Kit kit;
		KitTabPreco kitTab;
		for (int i = 0; i < sizeResults; i++) {
			itemKit = (ItemKit) result.items[i];
			boolean kitValido = false;
			for (int x = 0; x < kitTabPrecoList.size(); x++) {
				kitTab = (KitTabPreco)kitTabPrecoList.items[x];
				if (!ValueUtil.isEmpty(itemKit.cdKit) && itemKit.cdKit.equals(kitTab.cdKit)) {
					kitValido = true;
					break;
				}
			}
			if (kitValido) {
				kit = KitService.getInstance().getKit(itemKit.cdKit);
				if ((kit != null) && kit.isVigente()) {
					if (ValueUtil.round(itemKit.qtMinItem) > ValueUtil.round(itemPedido.getQtItemFisico())) {
						throw new ValidationException(MessageUtil.getMessage(Messages.KIT_MSG_QTMENOR, itemKit.qtMinItem));
					}
					if (itemKit.qtMaxItem > 0 && (ValueUtil.round(itemKit.qtMaxItem) < ValueUtil.round(itemPedido.getQtItemFisico()))) {
						throw new ValidationException(MessageUtil.getMessage(Messages.KIT_MSG_QTMAX, itemKit.qtMaxItem));
					}
				}
			}
		}
		kitTabPrecoList = null;
		result = null;
	}

	public boolean isItemPedidoPertenceKit(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		KitTabPreco kitTabPrecoFilter = new KitTabPreco();
		kitTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		kitTabPrecoFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		Vector kitTabPrecoList = KitTabPrecoService.getInstance().findAllByExample(kitTabPrecoFilter);
		//--
		ItemKit itemKitFilter = new ItemKit();
		itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemKitFilter.cdProduto = itemPedido.cdProduto;
		Vector result = ItemKitService.getInstance().findAllByExample(itemKitFilter);
		int sizeResults = result.size();
		ItemKit itemKit;
		Kit kit;
		KitTabPreco kitTab;
		for (int i = 0; i < sizeResults; i++) {
			itemKit = (ItemKit) result.items[i];
			boolean kitValido = false;
			for (int x = 0; x < kitTabPrecoList.size(); x++) {
				kitTab = (KitTabPreco) kitTabPrecoList.items[x];
				if (!ValueUtil.isEmpty(itemKit.cdKit) && itemKit.cdKit.equals(kitTab.cdKit)) {
					kitValido = true;
					break;
				}
			}
			if (kitValido) {
				kit = KitService.getInstance().getKit(itemKit.cdKit);
				if ((kit != null) && kit.isVigente()) {
					return true;
				}
			}
		}
		kitTabPrecoList = null;
		result = null;
		return false;
	}

	public Vector findProdutosByKit(String cdKit, Vector listProdutos) throws SQLException {
		Vector result = new Vector();
		ItemKit itemKitFilter = new ItemKit();
		itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemKitFilter.cdKit = cdKit;
		Vector itemKitList = ItemKitPdbxDao.getInstance().findCdProdutosByExample(itemKitFilter);
		int size = listProdutos.size();
		for (int i = 0; i < size; i++) {
			ProdutoBase produto = (ProdutoBase)listProdutos.items[i];
			if (itemKitList.indexOf(produto.cdProduto) != -1) {
				result.addElement(produto);
			}
		}
		listProdutos = null;
		itemKitList = null;
		return result;
	}

	public void verificaQtdeMinimaDosItensPedidoPorKit(Pedido pedido) throws SQLException {
		//--
		int size = pedido.itemPedidoList.size();
		Hashtable kitHash = new Hashtable(10);
		Vector result;
		for ( int i = 0 ; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			//--
			KitTabPreco kitTabPrecoFilter = new KitTabPreco();
			kitTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			kitTabPrecoFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
			Vector kitTabPrecoList = KitTabPrecoService.getInstance().findAllByExample(kitTabPrecoFilter);
			//--
			ItemKit itemKitFilter = new ItemKit();
			itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			itemKitFilter.cdProduto = itemPedido.cdProduto;
			result = ItemKitService.getInstance().findAllByExample(itemKitFilter);
			int sizeResults = result.size();
			ItemKit itemKit;
			Kit kit;
			for (int j = 0; j < sizeResults; j++) {
				itemKit = (ItemKit) result.items[j];
				boolean kitValido = false;
				for (int x = 0; x < kitTabPrecoList.size(); x++) {
					KitTabPreco kitTab = (KitTabPreco)kitTabPrecoList.items[x];
					if (!ValueUtil.isEmpty(itemKit.cdKit) && itemKit.cdKit.equals(kitTab.cdKit)) {
						kitValido = true;
						break;
					}
				}
				if (kitValido) {
					kit = KitService.getInstance().getKit(itemKit.cdKit);
					if ((kit != null) && kit.isVigente()) {
						kitHash.put(kit, StringUtil.getStringValue((ValueUtil.getIntegerValue((String) kitHash.get(kit)) + 1)));
					}
				}
			}
		}
		Vector resultsValues = kitHash.getKeys();
		int sizeResults = resultsValues.size();
		StringBuffer strBuffer = new StringBuffer();
		Kit kit;
		ItemKit itemKitFilter = new ItemKit();
		for ( int i = 0 ; i < sizeResults; i++) {
			kit = (Kit) resultsValues.items[i];
			int qtMinKit = kit.qtMinItensKit;
			if (qtMinKit == 0) { // Se não tiver qt minima, busca quantos item tem no kit para ser a qt min
				itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				itemKitFilter.cdKit = kit.cdKit;
				qtMinKit = ItemKitService.getInstance().countByExample(itemKitFilter);
			}
			int qtAtualKit = ValueUtil.getIntegerValue((String) kitHash.get(kit));
			if (qtAtualKit < qtMinKit) {
				strBuffer.append("|").append(KitService.getInstance().getDsKit(kit.cdKit)).append(" - Mín: ").append(qtMinKit).append(" At.: ").append(qtAtualKit);
			}
		}
		if (!ValueUtil.isEmpty(strBuffer.toString())) {
			StringBuffer strBuffer2 = new StringBuffer();
			throw new ValidationException(strBuffer2.append(Messages.KIT_MSG_VALIDACAO).append(strBuffer.toString()).toString());
		}
	}

	public Vector findItemKitJoinProduto(ItemKit itemKit) throws SQLException {
		return ItemKitPdbxDao.getInstance().findItemKitJoinProduto(itemKit);
	}

	public boolean isProdutoPossuiKit(String cdKit , String cdProduto, String flBonificado) throws SQLException {
    	ItemKit itemKitFilter = getNewItemKit(cdKit, cdProduto, flBonificado);
    	BaseDomain domain = findByRowKeyInCache(itemKitFilter);
    	return domain != null;
    }

	private ItemKit getNewItemKit(String cdKit, String cdProduto, String flBonificado) {
		ItemKit itemKitFilter = new ItemKit();
		itemKitFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemKitFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	itemKitFilter.cdProduto = cdProduto;
    	itemKitFilter.cdKit = cdKit;
    	itemKitFilter.cdTabelaPreco = ItemKit.CD_TABELA_PRECO_ZERO;
    	itemKitFilter.flBonificado = flBonificado;
		return itemKitFilter;
	}
	
	public double findVlPctDesconto(String cdKit, String cdProduto, String flBonificado) throws SQLException {
		ItemKit itemKitFilter = getNewItemKit(cdKit, cdProduto, flBonificado);
		String vlPctDesconto = findColumnByRowKey(itemKitFilter.getRowKey(), "VLPCTDESCONTO");
		return ValueUtil.getDoubleValue(vlPctDesconto);
		
	}
	
	public Vector findAllKitByItemPedido(ItemKit itemKitFilter) throws SQLException {
		return ItemKitPdbxDao.getInstance().findAllKitByItemPedido(itemKitFilter);
	}

	public int findQtItemKitByKit(Kit kit) throws SQLException {
		ItemKit itemKit = new ItemKit();
		itemKit.cdEmpresa = kit.cdEmpresa;
		itemKit.cdRepresentante = kit.cdRepresentante;
		itemKit.cdKit = kit.cdKit;
		itemKit.cdTabelaPreco = kit.cdTabelaPreco;
		return countByExample(itemKit);
	}

	public void validateEstoque(Vector itemKitList) {
		int size = itemKitList.size();
		for (int i = 0; i < size; i++) {
			ItemKit itemKit = (ItemKit) itemKitList.items[i];
			if (itemKit.semEstoque) {
				if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
					if (!itemKit.hasSimilaresPreenchidos() && itemKit.hasSimilares()) {
						throw new ValidationException(MessageUtil.getMessage(Messages.KIT_TIPO_3_ERROR_ESTOQUE_TEM_SIMILAR, itemKit.toString()));
					} else if (!itemKit.hasSimilares()) {
						throw new ValidationException(MessageUtil.getMessage(Messages.KIT_TIPO_3_ERROR_ESTOQUE, itemKit.toString()));
					}
				} else {
					throw new ValidationException(MessageUtil.getMessage(Messages.KIT_TIPO_3_ERROR_ESTOQUE, itemKit.toString()));
				}
			}
		}
	}

	public void validateItemKitAtreladoJaInserido(Pedido pedido, Vector itemKitList, String cdKit) throws SQLException {
		if (ValueUtil.isNotEmpty(itemKitList) && !LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
			String produtoJaInserido = ItemKitPdbxDao.getInstance().getProdutoInseridoByPedidoAndKit(pedido, itemKitList, cdKit);
			if (ValueUtil.isNotEmpty(produtoJaInserido)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.KIT_TIPO_3_ITEM_ATRELADO_JA_INSERIDO, ProdutoService.getInstance().getDescriptionWithId(produtoJaInserido)));
			}
		}
	}

	public Vector validateItemKitAtreladoJaInseridoSimilares(Pedido pedido, Vector itemKitList, String cdKit) throws SQLException {
		int size = itemKitList.size();
		Vector itemKitFinal = new Vector();
		for (int i = 0; i < size; i++) {
			ItemKit itemKit = (ItemKit) itemKitList.items[i];
			if (itemKit.hasSimilaresPreenchidos()) {
				int sizeSimilares = itemKit.similaresList.size();
				for (int j = 0; j < sizeSimilares; j++) {
					Produto produto = (Produto) itemKit.similaresList.items[j];
					if (produto.qtItemPedido <= 0) {
						continue;
					}
					itemKitFinal.addElement(getItemKitFromSimilar(itemKit, produto));
				}
			} else {
				itemKitFinal.addElement(itemKit);
			}
		}
		validateItemKitAtreladoJaInserido(pedido, itemKitFinal, cdKit);
		return itemKitFinal;
	}

	public void insertOrUpdateItemKitSimilares(Pedido pedido, Vector itemKitFinal, int qtKit) throws SQLException {
		int size = itemKitFinal.size();
		List<ItemPedido> result = new ArrayList<>();
		Vector itemPedidoListOld = new Vector();
		itemPedidoListOld.addElementsNotNull(pedido.itemPedidoList.items);
		String cdProduto = "";
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			for (int i = 0; i < size; i++) {
				ItemKit itemKit = (ItemKit) itemKitFinal.items[i];
				cdProduto = itemKit.cdProduto;
				if (itemKitJaFoiAdicionado(pedido, itemKit)) {
					PedidoService.getInstance().updateSingleItemPedidoFromItemKit3(pedido, qtKit, itemKit);
				} else {
					result.add(PedidoService.getInstance().createSingleItemPedidoFromItemKit3(pedido, qtKit, itemKit));
				}
			}
			for (ItemPedido itemPedido : result) {
				cdProduto = itemPedido.cdProduto;
				PedidoService.getInstance().validateEstoqueForKitTipo3(pedido, itemPedido);
				PedidoService.getInstance().atualizaNuSeqProduto(itemPedido);
				itemPedido.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
				if (LavenderePdaConfig.isPermiteItemBonificado() && itemPedido.isItemBonificacao()) {
					PedidoService.getInstance().insertItemPedidoBonificacao(pedido, itemPedido);
				} else {
					PedidoService.getInstance().insertItemPedido(pedido, itemPedido);
				}
			}
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
			pedido.itemPedidoList = itemPedidoListOld;
			resetQtItemFisico(result);
			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
			if (e instanceof ValidationException) {
				String msgMotivo = Messages.ITEM_KIT_IDENTIFY_ERRO_ESTOQUE.equals(((ValidationException) e).extraParams) ? Messages.ITEM_KIT_ERRO_ESTOQUE_PRODUTO : Messages.MSG_MOTIVO;
				throw new ValidationException(e.getMessage() + " " + MessageUtil.getMessage(msgMotivo, ProdutoService.getInstance().getDsProduto(cdProduto)));
			}
			throw e;
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}

	public void resetQtItemFisico(List<ItemPedido> itemPedidoList) throws SQLException {
		for (ItemPedido itemPedido : itemPedidoList) {
			itemPedido.setQtItemFisico(ValueUtil.getDoubleSimpleValue(ItemPedidoService.getInstance().findColumnByRowKey(itemPedido.getRowKey(), ItemPedido.DS_COLUNA_QTITEMFISICO)));
		}
	}

	private boolean itemKitJaFoiAdicionado(Pedido pedido, ItemKit itemKit) throws SQLException {
		ItemPedido itemPedidoFilter = ItemPedidoService.getInstance().createNewItemPedido(pedido);
		itemPedidoFilter.cdKit = itemKit.cdKit;
		itemPedidoFilter.cdProduto = itemKit.cdProduto;
		if (itemKit.isBonificado()) {
			itemPedidoFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
			if (LavenderePdaConfig.usaKitBonificadoEPoliticaBonificacao()) {
				itemPedidoFilter.nuSeqProduto = 0;
			}
		}
		return ItemPedidoPdbxDao.getInstance().countItemPedidoPertencemAoKit(itemPedidoFilter) > 0;
	}

	private ItemKit getItemKitFromSimilar(ItemKit itemKit, Produto produto) {
		ItemKit itemKitSimilar = new ItemKit();
		itemKitSimilar.cdEmpresa = itemKit.cdEmpresa;
		itemKitSimilar.cdRepresentante = itemKit.cdRepresentante;
		itemKitSimilar.cdKit = itemKit.cdKit;
		itemKitSimilar.cdTabelaPreco = itemKit.cdTabelaPreco;
		itemKitSimilar.cdProduto = produto.cdProduto;
		itemKitSimilar.qtItemKit = (int) produto.qtItemPedido;
		itemKitSimilar.vlUnitarioKit = itemKit.vlUnitarioKit;
		itemKitSimilar.dsProduto = produto.dsProduto;
		itemKitSimilar.flBonificado = itemKit.flBonificado;
		return itemKitSimilar;
	}
	
	public ItemKit findItemKitByItemPedido(ItemPedido itemPedido) throws SQLException {
		ItemKit filter = new ItemKit();
		filter.cdEmpresa = itemPedido.cdEmpresa;
		filter.cdRepresentante = itemPedido.cdRepresentante;
		filter.cdKit = itemPedido.cdKit;
		filter.cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(itemPedido.pedido);
		filter.cdProduto = itemPedido.cdProduto;
		if (LavenderePdaConfig.isPermiteItemBonificado()) {
			filter.flBonificado = itemPedido.isItemBonificacao() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		} else {
			filter.flBonificado = ValueUtil.VALOR_NAO;
		}
		ItemKit itemKit = null;
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && ValueUtil.isNotEmpty(itemPedido.getProduto().cdAgrupadorSimilaridade)) {
			filter.cdAgrupadorSimilaridade = itemPedido.getProduto().cdAgrupadorSimilaridade;
			itemKit = ItemKitPdbxDao.getInstance().findItemKitSimilar(filter);
		} 
		if (itemKit == null) {
			filter.cdAgrupadorSimilaridade = null;
			itemKit = (ItemKit) findByPrimaryKey(filter);
		}
		return itemKit;
	}

	public boolean isItensKitComPreco(ItemKit itemKitFilter, String cdTabelaPreco) throws SQLException {
		Vector itemKitList = findAllByExample(itemKitFilter);
		if (ValueUtil.isEmpty(itemKitList)) {
			return false;
		}
		int size = itemKitList.size();
		for (int i = 0; i < size; i++) {
			ItemKit itemKit = (ItemKit) itemKitList.items[i];
			boolean itemKitComPreco = ItemKitPdbxDao.getInstance().isItemKitComPreco(itemKit, cdTabelaPreco);
			if (!itemKitComPreco) {
				return false;
			}
		}
		return true;
	}
}