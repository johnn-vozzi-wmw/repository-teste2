package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.DomainUtil;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoConsignacaoDao;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.sql.Types;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class PedidoConsignacaoService extends CrudService {

    private static PedidoConsignacaoService instance = null;
    
    private PedidoConsignacaoService() {
        //--
    }
    
    public static PedidoConsignacaoService getInstance() {
        if (instance == null) {
            instance = new PedidoConsignacaoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PedidoConsignacaoDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain domain) {
		
	}
	
	protected PedidoConsignacao criaPedidoConsignacaoAPartirDoItemPedido(ItemPedido itemPedido, String tipoRegistro) throws SQLException {
		PedidoConsignacao pedidoConsignacao = new PedidoConsignacao();
		pedidoConsignacao.copyPropertiesfromItem(itemPedido); 
		pedidoConsignacao.copyPropertiesfromPedido(itemPedido.pedido); 
		pedidoConsignacao.nuSeqConsignacao = findMaxKey(pedidoConsignacao, "NUSEQCONSIGNACAO") + 1;
		pedidoConsignacao.flTipoRegistro = tipoRegistro; 
		return pedidoConsignacao;
	}

	protected void inserePedidoConsignado(Vector itemPedidoList) throws SQLException {
		inserePedidoConsignado(itemPedidoList, PedidoConsignacao.TIPO_REGISTRO_INCLUSAO);
	}
	
	protected void inserePedidoConsignadoFecharPedido(Vector itemPedidoList) throws SQLException {
		inserePedidoConsignado(itemPedidoList, PedidoConsignacao.TIPO_REGISTRO_FECHAMENTO);
	}
	
	protected int qtdPedidoConsignacaoPorPedido(Pedido pedido) throws SQLException {
		if (pedido != null) {
			PedidoConsignacao pedidoConsignacaoFilter = getInstanceByPedido(pedido);
			return countByExample(pedidoConsignacaoFilter);
		}
		return 0;
	}
	
	public void inserePedidoConsignado(Vector itemPedidoList, String flTipoRegistro) throws SQLException {
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			PedidoConsignacao pedidoConsignacao = criaPedidoConsignacaoAPartirDoItemPedido(itemPedido, flTipoRegistro);
			pedidoConsignacao.dtEmissao = DateUtil.getCurrentDate();
			insert(pedidoConsignacao);
		}
	}
	
	public void enviaPedidosConsignacaoBackground(Pedido pedido) throws SQLException {
		PedidoConsignacao pedidoConsignacaoFilter = new PedidoConsignacao();
		pedidoConsignacaoFilter.copyPropertiesfromPedido(pedido);
		pedidoConsignacaoFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
		Vector pedidoConsignacaoDevolvidoList = findAllByExample(pedidoConsignacaoFilter);
		if (!pedidoConsignacaoDevolvidoList.isEmpty()) {
			EnviaDadosThread.getInstance().enviaPedidoConsignadoBackground(pedidoConsignacaoDevolvidoList);
		}
	}

	public Vector findItensDevolvidosPorPedido(Pedido pedido) throws SQLException {
		PedidoConsignacao pedidoConsignacaoFilter = getInstanceByPedido(pedido);
		pedidoConsignacaoFilter.flTipoRegistro = PedidoConsignacao.TIPO_REGISTRO_DEVOLUCAO;
		Vector produtoConsignacaoList = findAllByExample(pedidoConsignacaoFilter); 
		setProdutos(produtoConsignacaoList);
		return produtoConsignacaoList;
	}
	
	public Vector getListaAgrupadaPorProdutosEQuePossuamQtDevolvida(Vector pedidoConsignacaoList) {
		Vector newPedidoConsignacaoList = new Vector();
		for (int i = 0; i < pedidoConsignacaoList.size(); i++) {
			PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) pedidoConsignacaoList.items[i];
			pedidoConsignacao.nuSeqConsignacao = 0;
			if (newPedidoConsignacaoList.contains(pedidoConsignacao)) {
				int posicao =  newPedidoConsignacaoList.indexOf(pedidoConsignacao);
				PedidoConsignacao newPedidoConsignacao = (PedidoConsignacao) newPedidoConsignacaoList.items[posicao];
				newPedidoConsignacao.qtItemFisico += pedidoConsignacao.qtItemFisico;
			} else if (pedidoConsignacao.qtItemFisico != 0) {
				newPedidoConsignacaoList.addElement(pedidoConsignacao);
			}
		}
		return newPedidoConsignacaoList;
	}
	
	public Vector getItemPedidoDevolvidoList(Vector itemPedidoList) {
		Vector itemPedidoDevolvidoList = new Vector();
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (itemPedido.qtDevolvida != 0) {
				itemPedidoDevolvidoList.addElement(itemPedido);
			}
		}
		return itemPedidoDevolvidoList;
	}
	
	public void validaPercentualLimiteDevolucaoCliente(Pedido pedido, Vector itemPedidoDevolvidoList) throws SQLException {
		double vlTotalDevolvido = pedido.vlTotalDevolucoes;
		for (int i = 0; i < itemPedidoDevolvidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoDevolvidoList.items[i];
			vlTotalDevolvido += ValueUtil.round(itemPedido.vlItemPedido * itemPedido.qtDevolvida);
		}
		if (pedido.isPercentualDevolucaoClienteUltrapassada(vlTotalDevolvido)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_CONSIGNACAO_MSG_PERCENTUAL_DEVOLUCAO_CLIENTE_ULTRAPASSADA, new String[] {StringUtil.getStringValueToInterface(pedido.getVlPercentualDevolucaoAtingido(vlTotalDevolvido)), StringUtil.getStringValueToInterface(pedido.getCliente().vlPctDevolucaoConsig)}));
		}
	}
	
	public PedidoConsignacao findRegistroDevolvidoSelecionado(String rowKey) throws SQLException {
		List<String> rowKeyList = new ArrayList<String>(Arrays.asList(rowKey.split(";")));
		PedidoConsignacao pedidoConsignacaoFilter = new PedidoConsignacao();
		pedidoConsignacaoFilter.cdEmpresa = rowKeyList.get(0);
		pedidoConsignacaoFilter.cdRepresentante = rowKeyList.get(1);
		pedidoConsignacaoFilter.flOrigemPedido = rowKeyList.get(2);
		pedidoConsignacaoFilter.nuPedido = rowKeyList.get(3);
		pedidoConsignacaoFilter.cdProduto = rowKeyList.get(4);
		pedidoConsignacaoFilter.flTipoItemPedido = rowKeyList.get(5);
		pedidoConsignacaoFilter.nuSeqProduto = ValueUtil.getIntegerValue(rowKeyList.get(6));
		pedidoConsignacaoFilter.cdCliente = rowKeyList.get(7);
		pedidoConsignacaoFilter.flTipoRegistro = PedidoConsignacao.TIPO_REGISTRO_DEVOLUCAO;
		Vector pedidoConsignacaoList = findAllByExample(pedidoConsignacaoFilter);
		double qtDevolvida = 0;
		for (int i = 0; i < pedidoConsignacaoList.size(); i++) {
			PedidoConsignacao pedidoConsignacao = (PedidoConsignacao)pedidoConsignacaoList.items[i];
			qtDevolvida += pedidoConsignacao.qtItemFisico;
		}
		pedidoConsignacaoFilter = (PedidoConsignacao) pedidoConsignacaoList.items[0];
		pedidoConsignacaoFilter.qtItemFisico = qtDevolvida;
		
		return pedidoConsignacaoFilter;
	}
	
	public void insereDevolucaoPedidoConsignado(Vector itemPedidoList) throws SQLException {
		for (int i = 0; i < itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (itemPedido.qtDevolvida != 0) {
				PedidoConsignacao pedidoConsignacao = criaPedidoConsignacaoAPartirDoItemPedido(itemPedido, PedidoConsignacao.TIPO_REGISTRO_DEVOLUCAO);
				pedidoConsignacao.qtItemFisico = itemPedido.qtDevolvida;
				pedidoConsignacao.vlTotalItemPedido = itemPedido.qtDevolvida * itemPedido.vlItemPedido;
				insert(pedidoConsignacao);
			}
		}
	}
	
	private PedidoConsignacao criaPedidoConsignacaoDevolucao(ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null) {
			PedidoConsignacao pedidoConsignacao = criaPedidoConsignacaoAPartirDoItemPedido(itemPedido, PedidoConsignacao.TIPO_REGISTRO_DEVOLUCAO);
			pedidoConsignacao.qtItemFisico = itemPedido.qtDevolvida;
			pedidoConsignacao.vlTotalItemPedido = ValueUtil.round(itemPedido.qtDevolvida * itemPedido.vlItemPedido);
			pedidoConsignacao.vlPctDesconto = 0;
			pedidoConsignacao.vlPctAcrescimo = 0;
			return pedidoConsignacao;
		}
		return null;
	}
	
	public Vector getPedidoConsignacaoDevolucaoAtualList(Vector itemPedidoList) throws SQLException {
		Vector pedidoConsignacaoDevolucaoAtualList = new Vector(10);
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			for (int i = 0; i < itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				PedidoConsignacao pedidoConsignacao = criaPedidoConsignacaoDevolucao(itemPedido);
				pedidoConsignacaoDevolucaoAtualList.addElement(pedidoConsignacao);
			}
			return pedidoConsignacaoDevolucaoAtualList;
		}
		return new Vector(0);
	}
	
	public boolean isPossuiEstoqueDisponivel(PedidoConsignacao pedidoConsignacao, Pedido pedido) throws SQLException {
		ItemPedido itemPedidoFilter = new ItemPedido(); 
		DomainUtil.copyProperties(pedidoConsignacao, itemPedidoFilter);
		itemPedidoFilter = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedidoFilter.getRowKey());
		if (itemPedidoFilter != null) {
			itemPedidoFilter.setPedido(pedido);
			double qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedidoFilter, itemPedidoFilter.getCdLocalEstoque());
			double saldoEstoque = ValueUtil.round(qtEstoque) - ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedidoFilter, pedidoConsignacao.qtItemFisico);
			if (ValueUtil.round(saldoEstoque) < 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ESTOQUE_INSUFICIENTE, StringUtil.getStringValueToInterface(saldoEstoque * -1)));
			}
		}
		return true;
	}
	
	private void setProdutos(Vector produtoConsignacaoList) throws SQLException {
		for (int i = 0; i < produtoConsignacaoList.size(); i++) {
			PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) produtoConsignacaoList.items[i];
			pedidoConsignacao.produto = ProdutoService.getInstance().getProduto(pedidoConsignacao.cdEmpresa, pedidoConsignacao.cdRepresentante, pedidoConsignacao.cdProduto);
			pedidoConsignacao.produto = pedidoConsignacao.produto != null ? pedidoConsignacao.produto : new Produto();
		}
	}

	public Hashtable getSynInfoTable() throws SQLException {
		ConfigIntegWebTc configIntegWebTc = new ConfigIntegWebTc();
		configIntegWebTc.dsTabela = PedidoConsignacao.TABLE_NAME;
		configIntegWebTc = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTc.getRowKey());
		SyncInfo syncInfo = new SyncInfo();
		syncInfo.tableName = configIntegWebTc.dsTabela;
		syncInfo.keys = configIntegWebTc.getChaves();
		syncInfo.flRemessa = configIntegWebTc.isRemessa();
		syncInfo.flRetorno = configIntegWebTc.isRetorno();
		Hashtable hash = new Hashtable(1);
		hash.put(syncInfo.tableName, syncInfo);
		return hash;
	}
	
	public void updatePedidoConsignacaoProcessandoTransmissao(String rowKey) throws SQLException {
		updateColumn(rowKey, "FLTIPOALTERACAO", Pedido.FLTIPOALTERACAO_PROCESSANDO_ENVIO, Types.VARCHAR);
	}

	public Vector findPedidoConsignacaoTipoInclusaoList(Pedido pedido) throws SQLException {
		return findPedidoConsignacaoList(pedido, PedidoConsignacao.TIPO_REGISTRO_INCLUSAO);
	}
	
	public Vector findPedidoConsignacaoTipoDevolucaoList(Pedido pedido) throws SQLException {
		return findPedidoConsignacaoList(pedido, PedidoConsignacao.TIPO_REGISTRO_DEVOLUCAO);
	}

	private Vector findPedidoConsignacaoList(Pedido pedido, String flTipoRegistro) throws SQLException {
		if (pedido != null) {
			PedidoConsignacao pedidoConsignacaoFilter = getInstanceByPedido(pedido);
			pedidoConsignacaoFilter.cdCliente = pedido.cdCliente;
			pedidoConsignacaoFilter.flTipoRegistro = flTipoRegistro;
			pedidoConsignacaoFilter.sortAtributte = PedidoConsignacao.NMCOLUNA_NUSEQITEMPEDIDO;
			pedidoConsignacaoFilter.sortAsc = ValueUtil.VALOR_SIM;
			return findAllByExample(pedidoConsignacaoFilter);
		}
		return null;
	}
	
	public Date getDtVencimento(PedidoConsignacao pedidoConsignacao) {
		if (pedidoConsignacao != null) {
			Date dtVencimento = DateUtil.getDateValue(pedidoConsignacao.dtEmissao);
			DateUtil.addDay(dtVencimento, LavenderePdaConfig.getNuDiasValidadePedidoEmConsignacao());
			return dtVencimento;
		}
		return null;
	}

	public void executaLimpezaPedidoConsignacao() throws SQLException {
		PedidoConsignacao pedidoConsignacaoFilter = new PedidoConsignacao();
		pedidoConsignacaoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoConsignacaoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Date dataLimite = DateUtil.getCurrentDate();
		DateUtil.decDay(dataLimite, LavenderePdaConfig.getNuDiasPermanenciaPedidoConsignacao());
		pedidoConsignacaoFilter.dtEmissaoFilter = dataLimite;
		deleteAllByExample(pedidoConsignacaoFilter);
	}

	private PedidoConsignacao getInstanceByPedido(Pedido pedido) {
		PedidoConsignacao pedidoConsignacaoFilter = new PedidoConsignacao();
		pedidoConsignacaoFilter.cdEmpresa = pedido.cdEmpresa;
		pedidoConsignacaoFilter.cdRepresentante = pedido.cdRepresentante;
		pedidoConsignacaoFilter.nuPedido = pedido.nuPedido;
		pedidoConsignacaoFilter.flOrigemPedido = pedido.flOrigemPedido;
		return pedidoConsignacaoFilter;
	}

    public Pedido getPedidoConsignadoAgrupado(List<Pedido> pedidoList) throws SQLException {
		Pedido pedidoAnterior = null;
		StringBuffer dsObservacao = new StringBuffer(256);
		HashMap<String, ItemPedido> itemPedidoHash = new HashMap<String, ItemPedido>(32);
		//--
		pedidoAnterior = montaItensDoPedidoConsignacaoAgrupado(pedidoList, pedidoAnterior, dsObservacao, itemPedidoHash);
		if (pedidoAnterior != null) {
			Vector itemPedidoList = convertItemPedidoHashToVector(itemPedidoHash);
			SessionLavenderePda.setCliente(pedidoAnterior.getCliente());
			Pedido newPedido;
			CrudDbxDao.getCurrentDriver().startTransaction();
			try {
				newPedido = ClienteService.getInstance().copyAndInsertPedidoCliente((Pedido)pedidoAnterior.clone(), pedidoAnterior.getCliente(), itemPedidoList, false, false, true);
				atualizadaDadosDoPedidoAgrupado(pedidoList, dsObservacao, newPedido);
				inserePedidoConsignado(newPedido.itemPedidoList);
				insereDevolucaoPedidoConsignado(newPedido.itemPedidoList);
				atualizaRegistrosConsignacaoAntigosEExcluiPedidos(pedidoList);
				CrudDbxDao.getCurrentDriver().commit();
			} catch (Throwable e) {
				CrudDbxDao.getCurrentDriver().rollback();
				throw e;
			} finally {
				CrudDbxDao.getCurrentDriver().finishTransaction();
			}
			return newPedido;
		}
		return null;
    }

    // Processo de montagem coberto por TestCase
	protected Pedido montaItensDoPedidoConsignacaoAgrupado(List<Pedido> pedidoList, Pedido pedidoAnterior, StringBuffer dsObservacao, HashMap<String, ItemPedido> itemPedidoHash) {
		StringBuffer auxHashKey = new StringBuffer(24);
		for (Pedido pedidoTemp : pedidoList) {
			validateAgruparPedidosConsignacao(pedidoAnterior, pedidoTemp); 
			selecionaItensDoPedido(itemPedidoHash, pedidoTemp, auxHashKey);
			String dsObsTemp = (String) pedidoTemp.getHashValuesDinamicos().get(Pedido.NMCOLUNA_DSOBSERVACAO);
			if (ValueUtil.isNotEmpty(dsObsTemp)) {
				dsObservacao.append(dsObsTemp).append("; ");
			}
			pedidoAnterior = pedidoTemp;
		}
		return pedidoAnterior;
	}

	private Vector convertItemPedidoHashToVector(HashMap<String, ItemPedido> itemPedidoHash) {
		Vector itemPedidoList = new Vector();
		for (ItemPedido item : itemPedidoHash.values()) {
			itemPedidoList.addElement(item.clone());
		}
		return itemPedidoList;
	}

	private void validateAgruparPedidosConsignacao(Pedido pedidoAnterior, Pedido pedido) {
    	if (!pedido.isPedidoConsignado()) {
    		throw new ValidationException(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_STATUS_INVALIDO);
    	}
		Date dtConsignacao = pedido.dtConsignacao;
		if (ValueUtil.isNotEmpty(dtConsignacao)) {
			dtConsignacao.advance(LavenderePdaConfig.getNuDiasValidadePedidoEmConsignacao());
			if (DateUtil.getCurrentDate().isAfter(dtConsignacao)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_VENCIDA, pedido.nuPedido));
			}
		}
    	if (pedidoAnterior != null) {
    		if (!ValueUtil.valueEquals(pedidoAnterior.cdCliente, pedido.cdCliente)) {
    			throw new ValidationException(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_CLIENTE);
    		}
    		if (!StringUtil.getStringValue(pedidoAnterior.cdTipoPedido).equals(StringUtil.getStringValue(pedido.cdTipoPedido))) {
    			throw new ValidationException(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_TIPO_PEDIDO);
    		}
    		if (!StringUtil.getStringValue(pedidoAnterior.cdTipoPagamento).equals(StringUtil.getStringValue(pedido.cdTipoPagamento))) {
    			throw new ValidationException(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_TIPO_PAGAMENTO);
    		}
    		if (!StringUtil.getStringValue(pedidoAnterior.cdCondicaoComercial).equals(StringUtil.getStringValue(pedido.cdCondicaoComercial))) {
    			throw new ValidationException(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_CONDICAO_COMERCIAL);
    		}
    		if (!StringUtil.getStringValue(pedidoAnterior.cdCondicaoPagamento).equals(StringUtil.getStringValue(pedido.cdCondicaoPagamento))) {
    			throw new ValidationException(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_CONSIGNACAO_CONDICAO_PAGAMENTO);
    		}
    	}
    }

	private void selecionaItensDoPedido(HashMap<String, ItemPedido> itemPedidoHash, Pedido pedidoTemp, StringBuffer auxHashKey) {
		int itemListSize = pedidoTemp.itemPedidoList.size();
		for (int j = 0; j < itemListSize; j++) {
			auxHashKey.setLength(0);
			ItemPedido itemPedido = (ItemPedido) ((ItemPedido) pedidoTemp.itemPedidoList.items[j]).clone();
			auxHashKey.append(itemPedido.cdProduto).append(";").append(itemPedido.flTipoItemPedido);
			ItemPedido itemHash = (ItemPedido) itemPedidoHash.get(auxHashKey.toString());
			setQtDevolvida(pedidoTemp, itemPedido);
			if (itemHash == null) {
				itemPedidoHash.put(auxHashKey.toString(), itemPedido);
			} else {
				setItemMenorPreco(itemPedidoHash, itemHash, itemPedido, auxHashKey.toString());
			}
		}
	}

	private void setQtDevolvida(Pedido pedidoTemp, ItemPedido itemPedido) {
		if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao && ValueUtil.isNotEmpty(pedidoTemp.pedidoConsignacaoDevolucaoList)) {
			int size = pedidoTemp.pedidoConsignacaoDevolucaoList.size();
			for (int k = 0; k < size; k++) {
				PedidoConsignacao pedidoCon = (PedidoConsignacao) pedidoTemp.pedidoConsignacaoDevolucaoList.items[k];
				if (pedidoCon.cdProduto.equals(itemPedido.cdProduto)) {
					itemPedido.qtDevolvida = pedidoCon.qtItemFisico;
					break;
				}
			}
		}
	}

	private void atualizadaDadosDoPedidoAgrupado(List<Pedido> pedidoList, StringBuffer dsObservacao, Pedido newPedido) throws SQLException {
		StringBuffer sbCdPedidosAgrupados =  new StringBuffer(64);
		newPedido.flConsignacaoImpressa = ValueUtil.VALOR_NAO;
		newPedido.vlTotalTrocaPedido = newPedido.vlPedidoOriginal = newPedido.vlTotalDevolucoes = 0;
		for (Pedido pedidoTemp : pedidoList) {
			sbCdPedidosAgrupados.append(pedidoTemp.nuPedido).append(";");
			newPedido.vlPedidoOriginal += pedidoTemp.vlPedidoOriginal;
			newPedido.vlTotalDevolucoes += pedidoTemp.vlTotalDevolucoes;
			newPedido.vlTotalTrocaPedido += pedidoTemp.vlTotalTrocaPedido;
		}
		newPedido.cdPedidosAgrupados = sbCdPedidosAgrupados.toString();
		newPedido.getHashValuesDinamicos().put(Pedido.NMCOLUNA_DSOBSERVACAO, dsObservacao.toString());
		Date dtConsignacao = DateUtil.getCurrentDate();
		dtConsignacao.advance(- LavenderePdaConfig.getNuDiasValidadePedidoEmConsignacao());
		newPedido.dtConsignacao = dtConsignacao;
		newPedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoConsignado;
		PedidoService.getInstance().update(newPedido);
	}

	private void atualizaRegistrosConsignacaoAntigosEExcluiPedidos(List<Pedido> pedidoList) throws SQLException {
		for (Pedido pedido : pedidoList) {
			PedidoConsignacao pedidoConsignacaoFilter = getInstanceByPedido(pedido);
			pedidoConsignacaoFilter.cdCliente = pedido.cdCliente;
			inserePedidoConsignado(pedido.itemPedidoList, PedidoConsignacao.TIPO_REGISTRO_AGRUPADO);
    		PedidoService.getInstance().delete(pedido);
    	}
	}

    private void setItemMenorPreco(HashMap<String, ItemPedido> hash, ItemPedido itemHash, ItemPedido itemPedido, String auxHashKey) {
    	if (itemPedido.vlItemPedido < itemHash.vlItemPedido) {
    		itemPedido.setQtItemFisico(itemPedido.getQtItemFisico() + itemHash.getQtItemFisico());
    		itemPedido.qtDevolvida += itemHash.qtDevolvida;
    		hash.put(auxHashKey, itemPedido);
    	} else {
    		itemHash.setQtItemFisico(itemHash.getQtItemFisico() + itemPedido.getQtItemFisico());
    		itemHash.qtDevolvida += itemPedido.qtDevolvida;
    		hash.put(auxHashKey, itemHash);
    	}
	}
	
}