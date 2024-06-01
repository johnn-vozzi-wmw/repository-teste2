package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgBrinde;
import br.com.wmw.lavenderepda.business.domain.BonifCfgFaixaQtde;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.validation.SaldoBonificacaoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoBonifCfgDbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ListBonifCfgBrindeWindow;
import totalcross.sql.Types;
import totalcross.util.Vector;

public class ItemPedidoBonifCfgService extends CrudService {
	
	private static ItemPedidoBonifCfgService instance;
	private boolean isFirstItemPedidoFromList;
	public List<String> errorsList;

	public static ItemPedidoBonifCfgService getInstance() {
		if (instance == null) {
			instance = new ItemPedidoBonifCfgService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
		StringBuffer strBuffer = new StringBuffer();
		ItemPedidoBonifCfg itemPedBon = (ItemPedidoBonifCfg) domain; 
		
		//cdEmpresa
		if (ValueUtil.isEmpty(itemPedBon.cdEmpresa)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.EMPRESA_NOME_ENTIDADE).toString());
		}
		//cdRepresentante
		if (ValueUtil.isEmpty(itemPedBon.cdRepresentante)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.REPRESENTANTE_NOME_ENTIDADE).toString());
		}
		//flOrigemPedido
		if (ValueUtil.isEmpty(itemPedBon.flOrigemPedido)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.PEDIDO_LABEL_FLORIGEM).toString());
		}
		//nuPedido
		if (ValueUtil.isEmpty(itemPedBon.nuPedido)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.PEDIDO_LABEL_NUPEDIDO).toString());
		}
		//flTipoItemPedido
		if (ValueUtil.isEmpty(itemPedBon.flTipoItemPedido)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.ITEMPEDIDO_LABEL_FLTIPOITEMPEDIDO).toString());
		}
		//cdProduto
		if (ValueUtil.isEmpty(itemPedBon.cdProduto)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID).append(Messages.PRODUTO_NOME_ENTIDADE).toString());
		}
		//cdBonifCfg
		if (ValueUtil.isEmpty(itemPedBon.cdBonifCfg)) {
			strBuffer.setLength(0);
			throw new ValidationException(strBuffer.append(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO).append(Messages.BONIFCFG_NOME_ENTIDADE).toString());
		}
	}

	@Override
	protected CrudDao getCrudDao() {
		return ItemPedidoBonifCfgDbxDao.getInstance();
	}

	private ItemPedidoBonifCfg createItemPedBonCfgByItemPedidoAndBonifCfg(ItemPedido itemPedido, BonifCfg bonifCfg) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfg = new ItemPedidoBonifCfg(itemPedido);
		if (itemPedido.isItemBonificacao()) {
			itemPedidoBonifCfg.flTipoRegistro = itemPedido.isItemBrinde ? ItemPedidoBonifCfg.FLTIPOREGISTRO_BRINDE : ItemPedidoBonifCfg.FLTIPOREGISTRO_DEBITO;
		} else {
			itemPedidoBonifCfg.flTipoRegistro = ItemPedidoBonifCfg.FLTIPOREGISTRO_CREDITO;
		}
		itemPedidoBonifCfg.cdBonifCfg = bonifCfg.cdBonifCfg;
		itemPedidoBonifCfg.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		if (itemPedidoBonifCfg.isFlTipoRegistroBrinde()) {
			itemPedidoBonifCfg.flBonificacaoAutomatica = ValueUtil.VALOR_NAO;
		} else {
			itemPedidoBonifCfg.flBonificacaoAutomatica = bonifCfg.flBonificacaoAutomatica;
		}

		if (bonifCfg.isTipoRegraQuantidade()) {
			if (itemPedidoBonifCfg.isFlTipoRegistroDebito()) {
				setQtBonificacaoConsumoBonifCfgFaixaQtde(itemPedido, itemPedidoBonifCfg);
			} else if (itemPedidoBonifCfg.isFlTipoRegistroCredito()) {
				setQtBonificacaoCreditoBonifCfgFaixaQtde(itemPedido, bonifCfg, itemPedidoBonifCfg);
			} else {
				itemPedidoBonifCfg.qtBonificacao = itemPedido.qtItemBrinde;
			}
		} else if (bonifCfg.isTipoRegraValor()) {
			itemPedidoBonifCfg.vlBonificacao = getVlBonificacaoCalculado(itemPedido, itemPedidoBonifCfg, bonifCfg);
		}
		return itemPedidoBonifCfg;
	}

	private void setQtBonificacaoConsumoBonifCfgFaixaQtde(ItemPedido itemPedido, ItemPedidoBonifCfg itemPedidoBonifCfg) {
		String qtConsumirMapKey = new BonifCfg(itemPedidoBonifCfg).getRowKey();
		Double qtConsumirBonifCfgFaixaQtde = itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.get(qtConsumirMapKey);
		if (qtConsumirBonifCfgFaixaQtde == null) {
			qtConsumirBonifCfgFaixaQtde = 0d;
		}
		if (itemPedido.getQtItemFisico() - itemPedido.qtBonificadoBonifCfg >= qtConsumirBonifCfgFaixaQtde) {
			itemPedidoBonifCfg.qtBonificacao = qtConsumirBonifCfgFaixaQtde;
			itemPedido.qtBonificadoBonifCfg += qtConsumirBonifCfgFaixaQtde;
			qtConsumirBonifCfgFaixaQtde = 0d;
		} else {
			double qtItemFisicoBonificacaoRestante = itemPedido.getQtItemFisico() - itemPedido.qtBonificadoBonifCfg;
			qtConsumirBonifCfgFaixaQtde -= qtItemFisicoBonificacaoRestante;
			itemPedidoBonifCfg.qtBonificacao = qtItemFisicoBonificacaoRestante;
			itemPedido.qtBonificadoBonifCfg += qtItemFisicoBonificacaoRestante;
		}
		itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.put(qtConsumirMapKey, qtConsumirBonifCfgFaixaQtde);
	}

	private void setQtBonificacaoCreditoBonifCfgFaixaQtde(ItemPedido itemPedido, BonifCfg bonifCfg, ItemPedidoBonifCfg itemPedidoBonifCfg) throws SQLException {
		Double qtFaixaFilter = itemPedido.pedido.qtSaldoBonifCfgFaixaQtde.get(bonifCfg.cdBonifCfg);
		BonifCfgFaixaQtde bonifCfgFaixaQtdeAtingida = BonifCfgFaixaQtdeService.getInstance().findBonifCfgFaixaQtdeAtingida(bonifCfg, qtFaixaFilter);
		BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual = itemPedido.pedido.bonifCfgFaixaQtdeAtualMap.get(bonifCfg.cdBonifCfg);

		itemPedidoBonifCfg.qtBonificacao = BonifCfgFaixaQtdeService.getInstance().getQtBonificacaoAtingida(bonifCfgFaixaQtdeAtingida, qtFaixaFilter, bonifCfgFaixaQtdeAtual == null ? 0 : bonifCfgFaixaQtdeAtual.qtBonificacaoAtual, isFirstItemPedidoFromList, bonifCfg.isBonificacaoAutomatica());
		if (itemPedidoBonifCfg.qtBonificacao == 0) {
			return;
		}

		if (bonifCfgFaixaQtdeAtingida != null) {
			bonifCfgFaixaQtdeAtingida.qtBonificacaoAtual = itemPedidoBonifCfg.qtBonificacao;
		}
		String qtConsumirMapKey = bonifCfg.getRowKey();
		atualizaControleConsumoRegras(itemPedido, itemPedidoBonifCfg, bonifCfgFaixaQtdeAtingida, bonifCfgFaixaQtdeAtual, qtConsumirMapKey);
		if (bonifCfgFaixaQtdeAtingida != null) {
			itemPedido.pedido.bonifCfgFaixaQtdeAtualMap.put(bonifCfg.cdBonifCfg, bonifCfgFaixaQtdeAtingida);
		} else {
			itemPedido.pedido.bonifCfgFaixaQtdeAtualMap.remove(bonifCfg.cdBonifCfg);
		}
	}

	private void atualizaControleConsumoRegras(ItemPedido itemPedido, ItemPedidoBonifCfg itemPedidoBonifCfg, BonifCfgFaixaQtde bonifCfgFaixaQtdeAtingida, BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual, String qtConsumirMapKey) {
		Double qtConsumir = itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.get(qtConsumirMapKey);
		if (qtConsumir == null) {
			qtConsumir = 0d;
		}
		if (bonifCfgFaixaQtdeAtingida != null && bonifCfgFaixaQtdeAtual != null) {
			Double qtConsumirOld = qtConsumir;
			qtConsumir = itemPedidoBonifCfg.qtBonificacao;
			bonifCfgFaixaQtdeAtingida.qtBonificacaoAtual = qtConsumir;
			itemPedidoBonifCfg.qtBonificacao -= qtConsumirOld;
		} else {
			qtConsumir += itemPedidoBonifCfg.qtBonificacao;
		}
		itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.put(qtConsumirMapKey, qtConsumir);
	}

	public Vector getSumVlBonicacaoByBonifCfgPedido(Pedido pedido) throws SQLException {
		return ((ItemPedidoBonifCfgDbxDao)getCrudDao()).getSumVlBonicacaoByBonifCfgPedido(pedido);
	}

	public void processaPoliticasBonificacaoPedido(ItemPedido itemPedido, boolean onInsertItemPedido) throws SQLException {
		processaPoliticasBonificacaoPedido(itemPedido, onInsertItemPedido, true);
	}
	
	public void processaPoliticasBonificacaoPedido(ItemPedido itemPedido, boolean onInsertItemPedido, boolean includeExtraFilters) throws SQLException {
		errorsList = new ArrayList<>();
		if (itemPedido.isIgnoraControleBonifCfgBrinde) {
			return;
		}
		initializeControlMaps(itemPedido.pedido);
		moveBonifCfgFaixaQtdeAtualToOldMap(itemPedido.pedido);
		deleteAllItemPedidoBonifCfgAplicaveis(itemPedido, includeExtraFilters);
		Vector itemPedidoFilterList = getListaItemPedidoOrganizadaParaProcessamento(itemPedido, onInsertItemPedido);
		for (int i = 0; i < itemPedidoFilterList.size(); i++) {
			processaInsercaoRegras(getItemPedidoParaProcessarRegras((ItemPedido) itemPedidoFilterList.elementAt(i)), i);
		}
		itemPedido.pedido.forceRefreshBonifCfgPedidoList = true;
		itemPedido.pedido.refreshListItemPedidoFromPoliticaBonificacaoAtualizada = true;
		
		limpaBonifCfgFaixaQtdeExcluidasMapControlePoliticas(itemPedido.pedido, itemPedidoFilterList);
		BonifCfgFaixaQtdeService.getInstance().avisaTrocaBonifCfgFaixaQtde(itemPedido.pedido);
		BonifCfgFaixaQtdeService.getInstance().avisaFaixaBonifCfgFaixaQtdeDeleted(itemPedido.pedido);
	}

	private Vector getListaItemPedidoOrganizadaParaProcessamento(ItemPedido itemPedido, boolean onInsertItemPedido) {
		Vector itemPedidoFilterList = getItemPedidoFilterList(itemPedido);
		sortItemPedidoList(itemPedidoFilterList);
		return itemPedidoFilterList;
	}

	private Vector getItemPedidoFilterList(ItemPedido itemPedido) {
		Vector itemPedidoFilterList = new Vector();
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			int size = itemPedido.pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido item = (ItemPedido) itemPedido.pedido.itemPedidoList.items[i];
				if (!item.isKitTipo3()) {
					itemPedidoFilterList.addElement(item);
				}
			}
		} else {
		itemPedidoFilterList.addElementsNotNull(itemPedido.pedido.itemPedidoList.items);
		}
		return itemPedidoFilterList;
	}

	private void limpaBonifCfgFaixaQtdeExcluidasMapControlePoliticas(Pedido pedido, Vector itemPedidoFilterList) {
		if (ValueUtil.isEmpty(itemPedidoFilterList)) {
			pedido.bonifCfgFaixaQtdeAtualMap.clear();
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoProduto()) {
			Map<String, BonifCfgFaixaQtde> newMapAtual = new HashMap<>();
			for (BonifCfgFaixaQtde bonifCfgFaixaQtde : pedido.bonifCfgFaixaQtdeAtualMap.values()) {
				if (!bonifCfgFaixaQtde.deleted) {
					newMapAtual.put(bonifCfgFaixaQtde.cdBonifCfg, bonifCfgFaixaQtde);
				}
			}
			pedido.bonifCfgFaixaQtdeAtualMap = newMapAtual;
		}
	}

	private void deleteAllItemPedidoBonifCfgAplicaveis(ItemPedido itemPedido, boolean includeExtraFilters) throws SQLException {
		ItemPedidoBonifCfg filter = new ItemPedidoBonifCfg(itemPedido);
		filter.cdProduto = null;
		filter.flTipoItemPedido = null;
		if (includeExtraFilters) {
			filter.flBonificacaoAutomatica = ValueUtil.VALOR_NAO;
		filter.notInFlTipoRegistro = new String[]{ItemPedidoBonifCfg.FLTIPOREGISTRO_BRINDE};
		}
		deleteAllByExample(filter);
	}

	private void processaInsercaoRegras(ItemPedido itemPedidoParaProcessarRegras, int i) throws SQLException {
		if (encerraInsercaoItemPedidoBonifCfg(itemPedidoParaProcessarRegras)) {
			return;
		}
		
		inicializaValoresItemPedidoEmProcessamento(itemPedidoParaProcessarRegras, i);
		insertAllItemPedidoBonifCfg(itemPedidoParaProcessarRegras);
		VerbaService.getInstance().updateVerbaBonifCfg(itemPedidoParaProcessarRegras);
	}
	
	private boolean encerraInsercaoItemPedidoBonifCfg(ItemPedido itemPedido) throws SQLException {
		if (itemPedido == null) return true;
		
		if (itemPedido.isItemBonificacao()) {
			ItemPedido itemPedidoResult = (ItemPedido) ItemPedidoService.getInstance().findByPrimaryKey(itemPedido);
			return itemPedidoResult == null;
		}
		return false;
	}

	private void inicializaValoresItemPedidoEmProcessamento(ItemPedido itemPedidoParaProcessarRegras, int i) {
		itemPedidoParaProcessarRegras.vlBonificado = 0;
		itemPedidoParaProcessarRegras.qtBonificadoBonifCfg = 0;
		itemPedidoParaProcessarRegras.forceRefreshBonifCfgList = true;
		isFirstItemPedidoFromList = i == 0;
	}

	private ItemPedido getItemPedidoParaProcessarRegras(ItemPedido itemPedido) throws SQLException {
		if (!itemPedido.isItemBonificacao()) {
			return itemPedido;
		}

		ItemPedidoBonifCfg itemPedidoBonifCfgDebito = new ItemPedidoBonifCfg(itemPedido);
		Vector resultList = findAllByExample(itemPedidoBonifCfgDebito);
		if (ValueUtil.isNotEmpty(resultList)) {
			return getItemPedidoBonificacaoParaProcessamento(itemPedido, resultList);
		}
		return itemPedido;
	}

	private ItemPedido getItemPedidoBonificacaoParaProcessamento(ItemPedido itemPedido, Vector resultList) throws SQLException {
		ItemPedido itemPedidoBanco = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedido.getRowKey());
		ItemPedido itemPedidoParcialProcessamento = (ItemPedido) itemPedidoBanco.clone();
		itemPedidoParcialProcessamento.pedido = itemPedido.pedido;
		for (int i = 0; i < resultList.size(); i++ ) {
			ItemPedidoBonifCfg itemPedidoBonifCfgDebito = (ItemPedidoBonifCfg) resultList.elementAt(i);
			if (itemPedidoBonifCfgDebito.isBonificacaoAutomatica() || itemPedidoBonifCfgDebito.isFlTipoRegistroBrinde()) {
				itemPedidoParcialProcessamento.qtItemFisico -= itemPedidoBonifCfgDebito.qtBonificacao;
			}
		}
		if (itemPedidoParcialProcessamento.qtItemFisico <= 0) {
			return null;
		}
		itemPedidoParcialProcessamento.isIgnoraControleBonifCfgBrinde = true;
		ItemPedidoService.getInstance().calculate(itemPedidoParcialProcessamento, itemPedidoParcialProcessamento.pedido);
		return itemPedidoParcialProcessamento;
	}

	public void initializeControlMaps(Pedido pedido) throws SQLException {
		pedido.qtSaldoBonifCfgFaixaQtde = new HashMap<>();
		pedido.qtConsumirBonifCfgFaixaQtde = new HashMap<>();
		pedido.bonifCfgFaixaQtdeOldMap = new HashMap<>();
		if (pedido.bonifCfgFaixaQtdeAtualMap == null) {
			pedido.bonifCfgFaixaQtdeAtualMap = BonifCfgFaixaQtdeService.getInstance().findBonifCfgFaixaByPedido(pedido);
		}
	}
		
	public void moveBonifCfgFaixaQtdeAtualToOldMap(Pedido pedido) {
		for (BonifCfgFaixaQtde bonifCfgFaixaQtde : pedido.bonifCfgFaixaQtdeAtualMap.values()) {
			pedido.bonifCfgFaixaQtdeOldMap.put(bonifCfgFaixaQtde.cdBonifCfg, bonifCfgFaixaQtde);
		}
		pedido.bonifCfgFaixaQtdeAtualMap.clear();
	}

	public void validateSaldoBonificacao(Pedido pedido) throws SQLException {
		Vector pedidoBonifCfgList = pedido.getBonifCfgPedidoList();
		for (int i = 0; i < pedidoBonifCfgList.size(); i++) {
			BonifCfg pedidoBonifCfg = ((BonifCfg)pedidoBonifCfgList.items[i]);
			double valueToCompare = pedidoBonifCfg.isTipoRegraQuantidade() ? pedidoBonifCfg.sumQtBonificacao : pedidoBonifCfg.sumVlBonificacao;
			if (!pedidoBonifCfg.isOpcional() && valueToCompare > 0) {
				throw new SaldoBonificacaoException(Messages.MSG_SALDO_OBRIGATORIO_EXCEDENTE_PEDIDO);
			}
		}
	}
	
	private double getVlBonificacaoCalculado(ItemPedido itemPedido, ItemPedidoBonifCfg itemPedidoBon, BonifCfg bonifCfg) {
		if (!bonifCfg.isBonifCfgValorSobreVenda()) return 0;
		
		double vlBonificacao;
		if (itemPedidoBon.isFlTipoRegistroCredito()) {
			vlBonificacao = ValueUtil.round(itemPedido.getVlTotalItem() * bonifCfg.vlPctSobreVenda / 100);
		} else {
			vlBonificacao = itemPedido.getVlTotalItem() - itemPedido.vlBonificado;
		}
		return vlBonificacao;
	}
	
	public Vector findAllItensBonifCfgByPedido(Pedido pedido, String cdBonifCfg) throws SQLException {
		ItemPedidoBonifCfg itemPedBonFilter = createItemPedidoBonCfgByPedido(pedido);
		itemPedBonFilter.cdBonifCfg = cdBonifCfg;
		
		return ((ItemPedidoBonifCfgDbxDao)getCrudDao()).findAllItemPedidoBonif(itemPedBonFilter);
	}
	
	public boolean isItemComBonificacaoSaldoPendente(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isItemBonificacao()) {
			ItemPedidoBonifCfg itemPedBonFilter = new ItemPedidoBonifCfg(itemPedido);
			itemPedBonFilter.inFlTipoRegistro = new String [] {ItemPedidoBonifCfg.FLTIPOREGISTRO_DEBITO, LavenderePdaConfig.isUsaPoliticaBonificacaoComBrindes() ? ItemPedidoBonifCfg.FLTIPOREGISTRO_BRINDE : null};
			Double[] sumBonificacaoResult = sumBonificacaoBonifCfgValorQtd(itemPedBonFilter);
			
			if (sumBonificacaoResult == null) {
				return true;
			} else {
				return (sumBonificacaoResult[0] != null && sumBonificacaoResult[0] < ValueUtil.round(itemPedido.getVlTotalItem())) 
						|| sumBonificacaoResult[1] != null && sumBonificacaoResult[1] < ValueUtil.round(itemPedido.getQtItemFisico());
			}
		}
		return false;
	}
	
	private boolean prepareAndInsertValorSobreVenda(ItemPedido itemPedido, BonifCfg bonifCfg) throws SQLException {
		ItemPedidoBonifCfg itemPedBon = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedido, bonifCfg);
		itemPedBon.cdBonifCfg = bonifCfg.cdBonifCfg;
		itemPedBon.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		
		if (itemPedido.isItemBonificacao()) {
			ItemPedidoBonifCfg itemPedBonFilter = new ItemPedidoBonifCfg(itemPedido);
			itemPedBonFilter.cdBonifCfg = bonifCfg.cdBonifCfg;
			double vlSaldoBonificacao = ((ItemPedidoBonifCfgDbxDao) getCrudDao()).getVlSaldoBonificacao(itemPedBonFilter);
			if (vlSaldoBonificacao == 0) return false;
			
			if (itemPedBon.vlBonificacao <= vlSaldoBonificacao) {
				itemPedido.vlBonificado += itemPedBon.vlBonificacao;
				insert(itemPedBon);
				return true;
			} else {
				itemPedBon.vlBonificacao = vlSaldoBonificacao; 
				itemPedido.vlBonificado += itemPedBon.vlBonificacao;
				insert(itemPedBon);
				return false;
			}
		} else {
			insert(itemPedBon);
			return false;
		}
	}
	
	private boolean prepareAndInsertFaixaQtd(ItemPedido itemPedido, BonifCfg bonifCfg) throws SQLException {
		if (!itemPedido.isItemBonificacao()) {
			updateQtSaldoBonifCfgFaixaQtdePedido(itemPedido, bonifCfg);
		}
		ItemPedidoBonifCfg itemPedBon = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedido, bonifCfg);
		if (itemPedBon.isFlTipoRegistroDebito() && itemPedBon.qtBonificacao == 0) {
			return ValueUtil.round(itemPedido.qtBonificadoBonifCfg) == ValueUtil.round(itemPedido.getQtItemFisico());
		} else {
			if (itemPedBon.isFlTipoRegistroCredito()) {
				insertOrUpdateItemPedidoBonifCfgCredito(itemPedBon);
				if (isDeveConcederOuRemoverBrindes(itemPedido.pedido, itemPedBon)) {
					processaBrindesRelacionados(itemPedido, itemPedBon, bonifCfg);
				}
				return false;
			} else {
				insert(itemPedBon);
				return ValueUtil.round(itemPedido.qtBonificadoBonifCfg) == ValueUtil.round(itemPedido.getQtItemFisico());
			}
		}
	}

	private void processaBrindesRelacionados(ItemPedido itemPedido, ItemPedidoBonifCfg itemPedBon, BonifCfg bonifCfg) throws SQLException {
		BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual = itemPedido.pedido.bonifCfgFaixaQtdeAtualMap.get(itemPedBon.cdBonifCfg);
		BonifCfgFaixaQtde bonifCfgFaixaQtdeOld = itemPedido.pedido.bonifCfgFaixaQtdeOldMap.get(itemPedBon.cdBonifCfg);
		if (ValueUtil.valueEqualsIfNotNull(bonifCfgFaixaQtdeOld, bonifCfgFaixaQtdeAtual) && isBrindesJaInseridos(itemPedBon, bonifCfgFaixaQtdeAtual)) {
			return;
		}

		if (isDeveRemoverBrindes(bonifCfgFaixaQtdeAtual, bonifCfgFaixaQtdeOld)) {
			removeBrindesItemPedidoFromPedido(itemPedido.pedido, itemPedBon, bonifCfgFaixaQtdeOld);
		} else {
			if (necessitaConcederBrindes(itemPedBon, bonifCfgFaixaQtdeAtual) && !ValueUtil.valueEqualsIfNotNull(bonifCfgFaixaQtdeOld, bonifCfgFaixaQtdeAtual)) {
				ListBonifCfgBrindeWindow listBonifCfgBrindeWindow = new ListBonifCfgBrindeWindow(bonifCfgFaixaQtdeAtual, itemPedido.pedido);
				if (listBonifCfgBrindeWindow.listBonifCfgBrindeForm.getListBrindesSize() > 0) {
					listBonifCfgBrindeWindow.popup();
					persisteBrindes(itemPedido, bonifCfg, listBonifCfgBrindeWindow.getBrindesSelecionadosList());
				}
			}
		}
	}

	private boolean isBrindesJaInseridos(ItemPedidoBonifCfg itemPedidoBonifCfgCredito, BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgBrindeFilter = getItemPedidoBonifCfgBrindesPorRegraFilter(itemPedidoBonifCfgCredito, bonifCfgFaixaQtdeAtual);
		return countByExample(itemPedidoBonifCfgBrindeFilter) > 0;
	}

	private void persisteBrindes(ItemPedido itemPedido, BonifCfg bonifCfg, List<BonifCfgBrinde> bonifCfgBrindeList) throws SQLException {
		for (BonifCfgBrinde bonifCfgBrinde : bonifCfgBrindeList) {
			ItemPedido itemPedidoBonificadoFilter = ItemPedidoService.getInstance().getNewItemPedido(itemPedido.pedido);
			itemPedidoBonificadoFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
			itemPedidoBonificadoFilter.cdProduto = bonifCfgBrinde.cdProduto;
			itemPedidoBonificadoFilter.nuSeqProduto = getNuSeqItemBonifCfg(new ItemPedidoBonifCfg(itemPedido));
			ItemPedido itemPedidoBonificadoExists = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoBonificadoFilter.getRowKey());
			if (itemPedidoBonificadoExists != null) {
				updateItemPedidoBrindeExistente(itemPedido, bonifCfgBrinde, itemPedidoBonificadoExists, bonifCfg);
			} else {
				insereItemPedidoBrinde(itemPedido, bonifCfgBrinde, itemPedidoBonificadoFilter, bonifCfg);
			}
		}
	}

	private void insereItemPedidoBrinde(ItemPedido itemPedido, BonifCfgBrinde bonifCfgBrinde, ItemPedido itemPedidoBonificadoFilter, BonifCfg bonifCfg) throws SQLException {
		itemPedidoBonificadoFilter.pedido = itemPedido.pedido;
		itemPedidoBonificadoFilter.isItemBrinde = true;
		itemPedidoBonificadoFilter.qtItemBrinde = bonifCfgBrinde.qtBrinde;
		changeItemTabelaPrecoSimple(itemPedidoBonificadoFilter, itemPedido.pedido.cdTabelaPreco);
		ItemPedidoService.getInstance().preparaItemPedidoBonificacao(itemPedidoBonificadoFilter, bonifCfgBrinde.qtBrinde, true);
		try {
			ItemPedidoService.getInstance().insert(itemPedidoBonificadoFilter);
			itemPedido.pedido.itemPedidoList.addElement(itemPedidoBonificadoFilter);
			ItemPedidoBonifCfg itemPedidoBonifCfgBrinde = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedidoBonificadoFilter, bonifCfg);
			insert(itemPedidoBonifCfgBrinde);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			errorsList.add(e.getMessage());
		}
	}

	private void updateItemPedidoBrindeExistente(ItemPedido itemPedido, BonifCfgBrinde bonifCfgBrinde, ItemPedido itemPedidoBonificadoExists, BonifCfg bonifCfg) throws SQLException {
		itemPedidoBonificadoExists.pedido = itemPedido.pedido;
		itemPedidoBonificadoExists.isItemBrinde = true;
		itemPedidoBonificadoExists.qtItemBrinde = bonifCfgBrinde.qtBrinde;
		ItemPedidoBonifCfg itemPedidoBonifCfgBrinde = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedidoBonificadoExists, bonifCfg);
		double qtExistente = 0d;
		if (findByRowKey(itemPedidoBonifCfgBrinde.getRowKey()) != null) {
			qtExistente = itemPedidoBonificadoExists.getQtItemFisico();
		}
		double newQtItemFisico = bonifCfgBrinde.qtBrinde + qtExistente;
		ItemPedidoService.getInstance().preparaItemPedidoBonificacao(itemPedidoBonificadoExists, newQtItemFisico, false);
		try {
			ItemPedidoService.getInstance().update(itemPedidoBonificadoExists);
			atualizaItemNaListaDeItensDoPedido(itemPedidoBonificadoExists);
			insertOrUpdateItemPedidoBonifCfgCredito(itemPedidoBonifCfgBrinde);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			errorsList.add(e.getMessage());
		}
	}

	private boolean necessitaConcederBrindes(ItemPedidoBonifCfg itemPedidoBonifCfgCredito, BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgFilter = getItemPedidoBonifCfgBrindesPorRegraFilter(itemPedidoBonifCfgCredito, bonifCfgFaixaQtdeAtual);
		int count = countByExample(itemPedidoBonifCfgFilter);
		// se maior que 0, brinde ja foi concedido
		return count == 0;
	}

	private ItemPedidoBonifCfg getItemPedidoBonifCfgBrindesPorRegraFilter(ItemPedidoBonifCfg itemPedidoBonifCfgCredito, BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual) {
		ItemPedidoBonifCfg itemPedidoBonifCfgFilter = new ItemPedidoBonifCfg();
		itemPedidoBonifCfgFilter.cdEmpresa = bonifCfgFaixaQtdeAtual.cdEmpresa;
		itemPedidoBonifCfgFilter.cdRepresentante = itemPedidoBonifCfgCredito.cdRepresentante;
		itemPedidoBonifCfgFilter.nuPedido = itemPedidoBonifCfgCredito.nuPedido;
		itemPedidoBonifCfgFilter.flOrigemPedido = itemPedidoBonifCfgCredito.flOrigemPedido;
		itemPedidoBonifCfgFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		itemPedidoBonifCfgFilter.cdBonifCfg = bonifCfgFaixaQtdeAtual.cdBonifCfg;
		itemPedidoBonifCfgFilter.flTipoRegistro = ItemPedidoBonifCfg.FLTIPOREGISTRO_BRINDE;
		return itemPedidoBonifCfgFilter;
	}

	private boolean isDeveRemoverBrindes(BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual, BonifCfgFaixaQtde bonifCfgFaixaQtdeOld) {
		return (bonifCfgFaixaQtdeAtual == null && bonifCfgFaixaQtdeOld != null)
				|| (bonifCfgFaixaQtdeOld != null && bonifCfgFaixaQtdeOld.isConcedeBrindes() && !bonifCfgFaixaQtdeAtual.isConcedeBrindes());
	}

	private void bonificaItemPedidoAutomaticamente(ItemPedido itemPedido, BonifCfg bonifCfg, ItemPedidoBonifCfg itemPedidoBonifCfgCredito) {
		try {
			ItemPedidoBonifCfg itemPedidoBonifCfgDebito = getItemPedidoBonifCfgFilterBonificacoesAutomaticas(itemPedido);
			itemPedidoBonifCfgDebito.cdBonifCfg = bonifCfg.cdBonifCfg;
			Vector resultList = findAllByExample(itemPedidoBonifCfgDebito);
			if (ValueUtil.isNotEmpty(resultList)) {
				atualizaBonificacaoAutomatica(itemPedido, itemPedidoBonifCfgCredito, resultList, bonifCfg);
			} else {
				persisteBonificacaoAutomatica(itemPedido, itemPedidoBonifCfgCredito, bonifCfg);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			errorsList.add(e.getMessage());
		}
	}

	private void atualizaBonificacaoAutomatica(ItemPedido itemPedido, ItemPedidoBonifCfg itemPedidoBonifCfgCredito, Vector resultList, BonifCfg bonifCfg) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgDebito = getItemPedidoBonifCfgDebitoFilter(itemPedidoBonifCfgCredito, resultList);
		ItemPedido itemPedidoBonificadoAutomatico = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoBonifCfgDebito.getPrimaryKeyItemPedido());
		if (itemPedidoBonificadoAutomatico != null) {
			try {
				double newQtItemFisico = itemPedidoBonificadoAutomatico.getQtItemFisico() - itemPedidoBonifCfgDebito.qtBonificacao + itemPedidoBonifCfgCredito.qtBonificacao;
				if (newQtItemFisico != itemPedidoBonifCfgDebito.qtBonificacao || itemPedidoBonifCfgCredito.qtBonificacao == 0) {
					itemPedidoBonifCfgDebito.qtBonificacao = itemPedidoBonifCfgCredito.qtBonificacao;
					itemPedidoBonificadoAutomatico.pedido = itemPedido.pedido;
					if (itemPedidoBonifCfgCredito.qtBonificacao == 0) {
						ItemPedidoService.getInstance().deleteBonificacaoAutomatica(itemPedido, bonifCfg, itemPedidoBonifCfgDebito);
					} else {
						atualizaItemPedidoBonificacaoAutomatica(newQtItemFisico, itemPedidoBonificadoAutomatico, itemPedidoBonifCfgDebito, bonifCfg, false);
						insertOrUpdateItemPedidoBonifCfgCredito(itemPedidoBonifCfgCredito);
					}
				} else {
					Double qtConsumir = itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.get(bonifCfg.getRowKey());
					if (qtConsumir != null) {
						qtConsumir -= itemPedidoBonifCfgCredito.qtBonificacao;
						itemPedido.pedido.qtConsumirBonifCfgFaixaQtde.put(bonifCfg.getRowKey(), qtConsumir);
					}
				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
				errorsList.add(e.getMessage());
			}
		}
	}

	private ItemPedidoBonifCfg getItemPedidoBonifCfgDebitoFilter(ItemPedidoBonifCfg itemPedidoBonifCfgCredito, Vector resultList) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgDebito = null;
		for (int i = 0; i < resultList.size(); i++) {
			itemPedidoBonifCfgDebito = (ItemPedidoBonifCfg) resultList.elementAt(i);
			if (itemPedidoBonifCfgDebito.cdBonifCfg.equals(itemPedidoBonifCfgCredito.cdBonifCfg)) {
				break;
			} else {
				itemPedidoBonifCfgDebito = null;
			}
		}
		if (itemPedidoBonifCfgDebito == null ) {
			itemPedidoBonifCfgDebito = createNewItemPedidoBonifCfgDebito(itemPedidoBonifCfgCredito);
		}
		return itemPedidoBonifCfgDebito;
	}

	private ItemPedidoBonifCfg createNewItemPedidoBonifCfgDebito(ItemPedidoBonifCfg itemPedidoBonifCfgCredito) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgDebito;
		itemPedidoBonifCfgDebito = new ItemPedidoBonifCfg();
		itemPedidoBonifCfgDebito.cdEmpresa = itemPedidoBonifCfgCredito.cdEmpresa;
		itemPedidoBonifCfgDebito.cdRepresentante = itemPedidoBonifCfgCredito.cdRepresentante;
		itemPedidoBonifCfgDebito.flOrigemPedido = itemPedidoBonifCfgCredito.flOrigemPedido;
		itemPedidoBonifCfgDebito.nuPedido = itemPedidoBonifCfgCredito.nuPedido;
		itemPedidoBonifCfgDebito.cdProduto = itemPedidoBonifCfgCredito.cdProduto;
		itemPedidoBonifCfgDebito.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		itemPedidoBonifCfgDebito.nuSeqProduto = getNuSeqItemBonifCfg(itemPedidoBonifCfgDebito);
		itemPedidoBonifCfgDebito.isCreateItemPedidoBonifCfg = true;
		return itemPedidoBonifCfgDebito;
	}

	public void atualizaItemPedidoBonificacaoAutomatica(double newQtItemFisico, ItemPedido itemPedidoBonificado, ItemPedidoBonifCfg itemPedidoBonifCfgDebito, BonifCfg bonifCfg, boolean onDelete) throws SQLException {
		itemPedidoBonificado.isBonificacaoAutomatica = true;
		ItemPedidoService.getInstance().preparaItemPedidoBonificacao(itemPedidoBonificado, newQtItemFisico, false);
		ItemPedidoService.getInstance().update(itemPedidoBonificado);
		atualizaItemNaListaDeItensDoPedido(itemPedidoBonificado);
		if (!onDelete) {
			if (itemPedidoBonifCfgDebito.isCreateItemPedidoBonifCfg) {
				ItemPedidoBonifCfg itemPedidoBonifCfgBonificadoAutomatico = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedidoBonificado, bonifCfg);
				insert(itemPedidoBonifCfgBonificadoAutomatico);
			} else {
				setQtBonificacaoConsumoBonifCfgFaixaQtde(itemPedidoBonificado, itemPedidoBonifCfgDebito);
				update(itemPedidoBonifCfgDebito);
			}
		}
	}

	private void atualizaItemNaListaDeItensDoPedido(ItemPedido itemPedidoBonificado) {
		itemPedidoBonificado.pedido.itemPedidoList.removeElement(itemPedidoBonificado);
		itemPedidoBonificado.pedido.itemPedidoList.addElement(itemPedidoBonificado);
	}

	public void atualizaItemPedidoBonificacaoAutomaticaOnDelete(double newQtItemFisico, ItemPedido itemPedidoBonificado) throws SQLException {
		atualizaItemPedidoBonificacaoAutomatica(newQtItemFisico, itemPedidoBonificado, null, null, true);
	}

	private void persisteBonificacaoAutomatica(ItemPedido itemPedido, ItemPedidoBonifCfg itemPedidoBonifCfgCredito, BonifCfg bonifCfg) throws SQLException {
		ItemPedido itemPedidoBonificadoFilter = ItemPedidoService.getInstance().getNewItemPedido(itemPedido.pedido);
		itemPedidoBonificadoFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		itemPedidoBonificadoFilter.cdProduto = itemPedidoBonifCfgCredito.cdProduto;
		itemPedidoBonificadoFilter.nuSeqProduto = getNuSeqItemBonifCfg(new ItemPedidoBonifCfg(itemPedido));
		ItemPedido itemPedidoBonificadoExists = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoBonificadoFilter.getRowKey());
		if (itemPedidoBonificadoExists != null) {
			double newQtItemFisico = itemPedidoBonificadoExists.getQtItemFisico() + itemPedidoBonifCfgCredito.qtBonificacao;
			ItemPedidoBonifCfg newItemPedidoBonifCfgDebito = createNewItemPedidoBonifCfgDebito(itemPedidoBonifCfgCredito);
			newItemPedidoBonifCfgDebito.nuSeqProduto = itemPedidoBonificadoFilter.nuSeqProduto;
			itemPedidoBonificadoExists.pedido = itemPedido.pedido;
			if (itemPedidoBonifCfgCredito.qtBonificacao > 0) {
				atualizaItemPedidoBonificacaoAutomatica(newQtItemFisico, itemPedidoBonificadoExists, newItemPedidoBonifCfgDebito, bonifCfg, false);
			}
		} else {
			itemPedidoBonificadoFilter.pedido = itemPedido.pedido;
			if (itemPedidoBonifCfgCredito.qtBonificacao > 0) {
				insereItemPedidoBonificacaoAutomatica(itemPedido, itemPedidoBonifCfgCredito, bonifCfg, itemPedidoBonificadoFilter);
			}
		}
		insertOrUpdateItemPedidoBonifCfgCredito(itemPedidoBonifCfgCredito);
	}

	private void insertOrUpdateItemPedidoBonifCfgCredito(ItemPedidoBonifCfg itemPedidoBonifCfgCredito) throws SQLException {
		if (countByExample(itemPedidoBonifCfgCredito) > 0) {
			update(itemPedidoBonifCfgCredito);
		} else {
			insert(itemPedidoBonifCfgCredito);
		}
	}

	private void insereItemPedidoBonificacaoAutomatica(ItemPedido itemPedido, ItemPedidoBonifCfg itemPedidoBonifCfgCredito, BonifCfg bonifCfg, ItemPedido itemPedidoBonificadoFilter) throws SQLException {
		changeItemTabelaPrecoSimple(itemPedidoBonificadoFilter, itemPedido.pedido.cdTabelaPreco);
		ItemPedidoService.getInstance().preparaItemPedidoBonificacao(itemPedidoBonificadoFilter, itemPedidoBonifCfgCredito.qtBonificacao, true);
		itemPedidoBonificadoFilter.isBonificacaoAutomatica = true;
		ItemPedidoService.getInstance().insert(itemPedidoBonificadoFilter);
		itemPedido.pedido.itemPedidoList.addElement(itemPedidoBonificadoFilter);
		ItemPedidoBonifCfg itemPedidoBonifCfgBonificadoAutomatico = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedidoBonificadoFilter, bonifCfg);
		insert(itemPedidoBonifCfgBonificadoAutomatico);
	}

	protected void changeItemTabelaPrecoSimple(ItemPedido itemPedido, String cdTabelaPreco) throws SQLException {
		itemPedido.cdTabelaPreco = cdTabelaPreco;
		Produto itemProduto = itemPedido.getProduto();
		if (itemProduto != null) {
			itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
			itemPedido.getProduto().itemTabelaPreco = itemTabelaPreco;
			if (itemTabelaPreco != null) {
				itemPedido.cdUnidade = itemProduto.cdUnidade;
				itemPedido.setItemTabelaPreco(itemTabelaPreco);
				PedidoService.getInstance().resetDadosItemPedido(itemPedido.pedido, itemPedido);
			}
		}
	}

	@Override
	public void insert(BaseDomain domain) throws SQLException {
		validate(domain);
		getCrudDao().insert(domain);
	}
	
	
	public double getVlSaldoBonificacao(ItemPedidoBonifCfg itemPedBon) throws SQLException {
		return ((ItemPedidoBonifCfgDbxDao) getCrudDao()).getVlSaldoBonificacao(itemPedBon);
	}
	
	private void insertAllItemPedidoBonifCfg(ItemPedido itemPedido) throws SQLException {
		for (int i = 0; i < itemPedido.getBonifCfgList().size(); i++) {
			BonifCfg bonifCfg = (BonifCfg) itemPedido.getBonifCfgList().items[i];
			if (houveSaldoPoliticaBonificacao(bonifCfg, itemPedido)) {
				break;
			}
		}
	}
	
	private boolean houveSaldoPoliticaBonificacao(BonifCfg bonifCfg, ItemPedido itemPedido) throws SQLException {
		if (bonifCfg.isTipoRegraContaCorrente()) {
			return houveSaldoRegraContaCorrente(itemPedido, bonifCfg);
		} else if (bonifCfg.isBonificacaoAutomatica()) {
			return houveSaldoRegrasAutomaticas(itemPedido, bonifCfg);
		} else {
			return houveSaldoRegrasNaoAutomaticas(itemPedido, bonifCfg);
		}
	}

	private boolean houveSaldoRegraContaCorrente(ItemPedido itemPedido, BonifCfg bonifCfg) throws SQLException {
		if (itemPedido.isItemBonificacao() && itemPedido.pedido.isPedidoVenda()) {
			return false;
		}
		ItemPedidoBonifCfg itemBonifCfg = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedido, bonifCfg);
		itemBonifCfg.qtBonificacao = itemPedido.isItemBonificacao() ? itemPedido.getQtItemFisico(): Math.ceil(itemPedido.getQtItemFisico() * bonifCfg.vlPctSobreVenda / 100);
		itemBonifCfg.flTipoAlteracao = ItemPedidoBonifCfg.FLTIPOALTERACAO_INSERIDO;
		insertOrUpdateItemPedidoBonifCfgCredito(itemBonifCfg);
		return true;
	}

	private boolean houveSaldoRegrasAutomaticas(ItemPedido itemPedido, BonifCfg bonifCfg) throws SQLException {
		if (!itemPedido.isItemBonificacao()) {
			updateQtSaldoBonifCfgFaixaQtdePedido(itemPedido, bonifCfg);
		}
		ItemPedidoBonifCfg itemPedBon = createItemPedBonCfgByItemPedidoAndBonifCfg(itemPedido, bonifCfg);
		if (itemPedBon.isFlTipoRegistroCredito()) {
			bonificaItemPedidoAutomaticamente(itemPedido, bonifCfg, itemPedBon);
			if (isDeveConcederOuRemoverBrindes(itemPedido.pedido, itemPedBon)) {
				processaBrindesRelacionados(itemPedido, itemPedBon, bonifCfg);
			}
			return false;
		} else if (itemPedido.isItemBonificacao()) {
			return houveSaldoRegrasNaoAutomaticas(itemPedido, bonifCfg);
		} else {
			return false;
		}
	}

	private boolean houveSaldoRegrasNaoAutomaticas(ItemPedido itemPedido, BonifCfg bonifCfg) throws SQLException {
		if (itemPedido.isItemBrinde) return true;

		if (bonifCfg.isTipoRegraQuantidade()) {
			return prepareAndInsertFaixaQtd(itemPedido, bonifCfg);
		} else if (bonifCfg.isTipoRegraValor()) {
			return prepareAndInsertValorSobreVenda(itemPedido, bonifCfg);
		}
		return false;
	}

	private void updateQtSaldoBonifCfgFaixaQtdePedido(ItemPedido itemPedido, BonifCfg bonifCfg) {
		Double qtSaldoBonifCfg = itemPedido.pedido.qtSaldoBonifCfgFaixaQtde.get(bonifCfg.cdBonifCfg);
		if (qtSaldoBonifCfg == null) {
			qtSaldoBonifCfg = 0d;
		}
		qtSaldoBonifCfg += itemPedido.getQtItemFisico();
		itemPedido.pedido.qtSaldoBonifCfgFaixaQtde.put(bonifCfg.cdBonifCfg, qtSaldoBonifCfg);
	}

	public void atualizaDomainParaEnvio(Pedido pedido, String flTipoAlteracao) throws SQLException {
		ItemPedidoBonifCfg itemPedBonFilter = createItemPedidoBonCfgByPedido(pedido);
		Vector itemPedBonCfgList = findAllByExample(itemPedBonFilter);
		int size = itemPedBonCfgList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoBonifCfg itemPedBon = (ItemPedidoBonifCfg) itemPedBonCfgList.items[i];
			updateColumn(itemPedBon.getRowKey(), ItemPedidoBonifCfg.NMCAMPOTIPOALTERACAO, flTipoAlteracao, Types.VARCHAR);
		}
	}
	
	private void sortItemPedidoList(Vector itemPedidoList) {
		Vector itemPedidoVendaList = new Vector();
		Vector itemPedidoBonificacaoList = new Vector();
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.elementAt(i);
			if (itemPedido.isItemVendaNormal()) {
				itemPedidoVendaList.addElement(itemPedido);
			} else if (itemPedido.isItemBonificacao()) {
				itemPedidoBonificacaoList.addElement(itemPedido);
		}
	}
		String previousSortAttr = ItemPedido.sortAttr;
		if (!itemPedidoVendaList.isEmpty()) {
			ItemPedido.sortAttr = "NUSEQITEMPEDIDO";
			SortUtil.qsortInt(itemPedidoVendaList.items, 0, itemPedidoVendaList.size() - 1, true);
		}
		if (!itemPedidoBonificacaoList.isEmpty()) {
			ItemPedido.sortAttr = "NUSEQITEMPEDIDO";
			SortUtil.qsortInt(itemPedidoBonificacaoList.items, 0, itemPedidoBonificacaoList.size() - 1, true);
		}
		ItemPedido.sortAttr = previousSortAttr;
		itemPedidoList.removeAllElements();
		itemPedidoList.addElementsNotNull(itemPedidoVendaList.items);
		itemPedidoList.addElementsNotNull(itemPedidoBonificacaoList.items);
	}
	
	private ItemPedidoBonifCfg createItemPedidoBonCfgByPedido(Pedido pedido) {
		ItemPedidoBonifCfg itemPedBonCfg = new ItemPedidoBonifCfg();
		itemPedBonCfg.cdEmpresa = pedido.cdEmpresa;
		itemPedBonCfg.cdRepresentante = pedido.cdRepresentante;
		itemPedBonCfg.nuPedido = pedido.nuPedido;
		itemPedBonCfg.flOrigemPedido = pedido.flOrigemPedido;
		itemPedBonCfg.cdUsuario = pedido.cdUsuario;
		return itemPedBonCfg;
	}

	private Double[] sumBonificacaoBonifCfgValorQtd(ItemPedidoBonifCfg itemPedidoBonifCfgFilter) throws SQLException {
		return ItemPedidoBonifCfgDbxDao.getInstance().sumBonificacaoBonifCfgValorQtd(itemPedidoBonifCfgFilter);
	}

	public ItemPedidoBonifCfg getItemPedidoBonifCfgFilterBonificacoesAutomaticas(ItemPedido itemPedido) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgFilter = new ItemPedidoBonifCfg(itemPedido);
		itemPedidoBonifCfgFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		itemPedidoBonifCfgFilter.flTipoRegistro = ItemPedidoBonifCfg.FLTIPOREGISTRO_DEBITO;
		itemPedidoBonifCfgFilter.flBonificacaoAutomatica = ValueUtil.VALOR_SIM;
		itemPedidoBonifCfgFilter.nuSeqProduto = getNuSeqItemBonifCfg(itemPedidoBonifCfgFilter);
		return itemPedidoBonifCfgFilter;
	}

	private int getNuSeqItemBonifCfg(ItemPedidoBonifCfg filter) throws SQLException {
		if (LavenderePdaConfig.usaKitBonificadoEPoliticaBonificacao()) {
			int nuSeqProduto = ItemPedidoBonifCfgDbxDao.getInstance().findNuSeqProdutoBonifCfg(filter);
			if (nuSeqProduto == 0) {
				nuSeqProduto = ItemPedidoBonifCfgDbxDao.getInstance().findNuSeqProdutoBonificacaoAvulsa(filter) + 1;
			}
			return nuSeqProduto;
		}
		return ItemPedido.NUSEQPRODUTO_UNICO;
	}

	public Double[] sumQtTotalBonificadoAutomaticoEBrindesPorItemPedido(ItemPedido itemPedido) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgFilter = new ItemPedidoBonifCfg(itemPedido);
		return ItemPedidoBonifCfgDbxDao.getInstance().sumQtTotalBonificadoAutomaticoEBrindesPorItemPedido(itemPedidoBonifCfgFilter);
	}

	public ItemPedidoBonifCfg findItemPedidoBonifCfgByItemPedido(ItemPedido itemPedido) throws SQLException {
		ItemPedidoBonifCfg filter = new ItemPedidoBonifCfg(itemPedido);
		filter.limit = 1;
		return ItemPedidoBonifCfgDbxDao.getInstance().findItemPedidoBonifByExample(filter);
	}

	public void desvincularBonificacoesOuBrindesFromPedidoOnDeleteItemVenda(ItemPedido itemPedido) throws SQLException {
		Vector bonifCfgList = itemPedido.getBonifCfgList();
		BonifCfg bonifCfg;
		for (int i = 0; i< bonifCfgList.size(); i++) {
			bonifCfg = (BonifCfg) bonifCfgList.elementAt(i);
			BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual = itemPedido.pedido.bonifCfgFaixaQtdeAtualMap.get(bonifCfg.cdBonifCfg);
			if (bonifCfgFaixaQtdeAtual != null) {
				ItemPedidoBonifCfg itemPedidoBonifCfgFilter = new ItemPedidoBonifCfg(itemPedido);
				if (bonifCfgFaixaQtdeAtual.isConcedeBrindes()) {
				removeBrindesItemPedidoFromPedido(itemPedido.pedido, itemPedidoBonifCfgFilter, bonifCfgFaixaQtdeAtual);
				} else {
					updateBonificacoesVinculadas(itemPedido.pedido, itemPedidoBonifCfgFilter, bonifCfgFaixaQtdeAtual);
			}
		}
		}
		itemPedido.pedido.forceRefreshBonifCfgPedidoList = true;	
	}
	
	private void updateBonificacoesVinculadas(Pedido pedido, ItemPedidoBonifCfg itemPedidoBonifCfgFilter, BonifCfgFaixaQtde bonifCfgFaixa) throws SQLException {
		itemPedidoBonifCfgFilter = getItemPedidoBonifCfgBrindesPorRegraFilter(itemPedidoBonifCfgFilter, bonifCfgFaixa);
		itemPedidoBonifCfgFilter.flTipoRegistro = null;
		Vector itemPedidoBonifCfgBrindesList = findAllByExample(itemPedidoBonifCfgFilter);
		for (ItemPedidoBonifCfg itemPedidoBonifCfg : VectorUtil.iterateOver(itemPedidoBonifCfgBrindesList, ItemPedidoBonifCfg.class)) {
			ItemPedido itemPedido = (ItemPedido)ItemPedidoService.getInstance().findByRowKey(itemPedidoBonifCfg.getPrimaryKeyItemPedido());
			if (itemPedido != null) {
				itemPedido.pedido = pedido;
				if (VerbaService.getInstance().updateVerbaBonifCfg(itemPedido)) {
					atualizaItemNaListaDeItensDoPedido(itemPedido);
				}
			}
		}
	}
	
	private void removeBrindesItemPedidoFromPedido(Pedido pedido, ItemPedidoBonifCfg itemPedidoBonifCfgCredito, BonifCfgFaixaQtde bonifCfgFaixa) throws SQLException {
		ItemPedidoBonifCfg itemPedidoBonifCfgFilter = getItemPedidoBonifCfgBrindesPorRegraFilter(itemPedidoBonifCfgCredito, bonifCfgFaixa);
		Vector itemPedidoBonifCfgBrindesList = findAllByExample(itemPedidoBonifCfgFilter);
		for (int i = 0; i < itemPedidoBonifCfgBrindesList.size(); i++) {
			ItemPedidoBonifCfg itemPedidoBonifCfgBrinde = (ItemPedidoBonifCfg) itemPedidoBonifCfgBrindesList.elementAt(i);
			ItemPedido itemPedidoBrinde = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoBonifCfgBrinde.getPrimaryKeyItemPedido());
			if (itemPedidoBrinde != null) {
				itemPedidoBrinde.pedido = pedido;
				itemPedidoBrinde.pedido.bonifCfgFaixaQtdeAtualMap = null;
				removeBrindeItemPedidoFromPedido(pedido, itemPedidoBrinde, itemPedidoBonifCfgBrinde);
				initializeControlMaps(pedido);
			}
		}
	}

	private void removeBrindeItemPedidoFromPedido(Pedido pedido, ItemPedido itemPedido, ItemPedidoBonifCfg itemPedidobonCfg) {
		if (itemPedidobonCfg.isFlTipoRegistroBrinde()) {
			try {
				itemPedido.isIgnoraControleBonifCfgBrinde = true;
				if (ValueUtil.round(itemPedido.getQtItemFisico()) > ValueUtil.round(itemPedidobonCfg.qtBonificacao)) {
					itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() - itemPedidobonCfg.qtBonificacao);
					PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
					PedidoService.getInstance().updateItemPedido(pedido, itemPedido, true);
				} else {
					pedido.isDeletandoItemAutomaticamente = true;
					itemPedido.isItemBrinde = true;
					PedidoService.getInstance().deleteItemPedido(pedido, itemPedido);
				}
				delete(itemPedidobonCfg);
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			} finally {
				itemPedido.isIgnoraControleBonifCfgBrinde = false;
				pedido.isDeletandoItemAutomaticamente = false;
			}
		}
	}

	public void deleteRegrasBonificacaoItemOnDeletePedido(ItemPedido itemPedido) throws SQLException {
		deleteAllByExample(new ItemPedidoBonifCfg(itemPedido));
	}
	
	public void validaEdicaoQtItemFisicoItemBonificadoAutomaticamente(ItemPedido itemPedido, double newQtItemFisico, Double[] qtTotalMinPermitido) throws SQLException {
		Double[] qtTotalMinItemPedidoPermitido = qtTotalMinPermitido == null ? sumQtTotalBonificadoAutomaticoEBrindesPorItemPedido(itemPedido) : qtTotalMinPermitido;
		if (qtTotalMinItemPedidoPermitido == null) return;
		
		double qtMinBrindeObrigatorio = qtTotalMinItemPedidoPermitido[0] == null ? 0 : qtTotalMinItemPedidoPermitido[0];
		double qtBonificacaoAutomatica = qtTotalMinItemPedidoPermitido[1] == null ? 0 : qtTotalMinItemPedidoPermitido[1];
		if (newQtItemFisico < qtMinBrindeObrigatorio + qtBonificacaoAutomatica) {
			String[] messagesParams = getMessageParamsBonifCfgValidation(itemPedido, qtMinBrindeObrigatorio, qtBonificacaoAutomatica);
			throw new ValidationException(MessageUtil.getMessage(Messages.MSG_AVISO_QTD_MINIMA_ITEMPEDIDOBONIFCFG, messagesParams));
		}
	}
	
	private String getQtItemPedidoFisicoToString(double qtItemPedidoFisico) {
		return StringUtil.getStringValueToInterface(qtItemPedidoFisico, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
	}
	
	private String[] getMessageParamsBonifCfgValidation(ItemPedido itemPedido, double qtMinBrindeObrigatorio, double qtBonificacaoAutomatica) throws SQLException {
		return new String[] {
				itemPedido.getProduto().dsProduto,
				getQtItemPedidoFisicoToString(qtMinBrindeObrigatorio + qtBonificacaoAutomatica),
				qtMinBrindeObrigatorio > 0 ? MessageUtil.getMessage(Messages.MSG_BONIFCFG_QTD_BRINDES, getQtItemPedidoFisicoToString(qtMinBrindeObrigatorio)): ValueUtil.VALOR_NI,
				qtBonificacaoAutomatica > 0 ? MessageUtil.getMessage(Messages.MSG_BONIFCFG_QTD_BONIFAUTOMATICA, getQtItemPedidoFisicoToString(qtBonificacaoAutomatica)) : ValueUtil.VALOR_NI
			};
	}

	public void validaQtdMinimaItemPedidoComBrinde(ItemPedido itemPedido, double qtdCompareValue) throws SQLException {
		double qtItemPedidoMinimoBrinde = findQtItemPedidoMinimoBrinde(itemPedido);
		if (qtItemPedidoMinimoBrinde > qtdCompareValue && !itemPedido.isIgnoraControleBonifCfgBrinde) {
			throw new ValidationException(MessageUtil.getMessage(Messages.BONIFCFG_AVISO_PRODUTO_BRINDE, new String[] {
					StringUtil.getStringValue(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()
							? ValueUtil.getIntegerValueTruncated(qtItemPedidoMinimoBrinde)
							: StringUtil.getStringValue(qtItemPedidoMinimoBrinde)),
					itemPedido.getProduto().dsProduto
			}));
		}
	}

	public double findQtItemPedidoMinimoBrinde(ItemPedido itemPedido) throws SQLException {
		ItemPedidoBonifCfg filter = new ItemPedidoBonifCfg(itemPedido);
		filter.flTipoRegistro = ItemPedidoBonifCfg.FLTIPOREGISTRO_BRINDE;
		filter.getBonifCfgBrinde().flOpcional = ValueUtil.VALOR_NAO;
		return ItemPedidoBonifCfgDbxDao.getInstance().sumByQtBrinde(filter);
	}
	
	public void validateItemPoliticaJaInseridoPedido(ItemPedido itemPedido) throws SQLException {
		if ((BonifCfgService.getInstance().isItemPedidoPossuiBonifCfgAplicaveis(itemPedido) || BonifCfgBrindeService.getInstance().isContemBonifCfgBrinde(itemPedido))
				&& ItemPedidoService.getInstance().getQtProdutoDoItemJaInseridoPedido(itemPedido.pedido, itemPedido) > 0) {
			throw new ValidationException(Messages.MSG_ERRO_ITEM_POLITICA_JA_INSERIDO);
		}
		
	}
	
	private boolean isDeveConcederOuRemoverBrindes(Pedido pedido, ItemPedidoBonifCfg itemPedidoBonifcfg) {
		BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual = pedido.bonifCfgFaixaQtdeAtualMap.get(itemPedidoBonifcfg.cdBonifCfg);
		BonifCfgFaixaQtde bonifCfgFaixaQtdeOld = pedido.bonifCfgFaixaQtdeOldMap.get(itemPedidoBonifcfg.cdBonifCfg);
		if (bonifCfgFaixaQtdeAtual == null && bonifCfgFaixaQtdeOld == null) {
			return false;
		}
		return isDeveConcederBrindes(pedido, bonifCfgFaixaQtdeAtual, bonifCfgFaixaQtdeOld)
				|| isDeveRemoverBrindes(bonifCfgFaixaQtdeAtual, bonifCfgFaixaQtdeOld);
	}
	
	private boolean isDeveConcederBrindes(Pedido pedido, BonifCfgFaixaQtde bonifCfgFaixaQtdeAtual, BonifCfgFaixaQtde bonifCfgFaixaQtdeOld) {
		return (bonifCfgFaixaQtdeAtual != null && bonifCfgFaixaQtdeAtual.isConcedeBrindes())
				|| (bonifCfgFaixaQtdeOld != null && bonifCfgFaixaQtdeOld.isConcedeBrindes()
				&& bonifCfgFaixaQtdeOld.qtVendida <= pedido.qtSaldoBonifCfgFaixaQtde.get(bonifCfgFaixaQtdeOld.cdBonifCfg));
	}
	
	public boolean isPedidoComItemContaCorrente(Pedido pedido) throws SQLException {
		ItemPedidoBonifCfg filter = new ItemPedidoBonifCfg(pedido);
		filter.cdTipoRegraBonificacao = BonifCfg.CDTIPOREGRA_CONTACORRENTE;
		return ItemPedidoBonifCfgDbxDao.getInstance().countItemPedidoContaCorrente(filter) > 0;
	}
	
	public void updateItemPedidoBonifCfgEnviadoServidor(Pedido pedido) throws SQLException {
		ItemPedidoBonifCfg filter = new ItemPedidoBonifCfg(pedido);
		filter.flTipoAlteracao = ItemPedidoBonifCfg.FLTIPOALTERACAO_ORIGINAL;
		ItemPedidoBonifCfgDbxDao.getInstance().updateFlAlteracao(filter);
	}

	public void deleteItemPedidoBonigCfgByItemPedido(ItemPedido itemPedido) {
		ItemPedidoBonifCfgDbxDao.getInstance().deleteItemPedidoBonigCfgByItemPedido(itemPedido);
	}

}
