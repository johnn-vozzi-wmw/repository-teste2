package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.validator.CpfCnpjValidator;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.ClienteRedeLiberadoComSenhaConfig;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.MarcadorCliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoFreteCli;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.validation.ClienteInadimplenteException;
import br.com.wmw.lavenderepda.business.validation.EstoquePrevistoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MarcadorDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoClientePdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.CadItemPedidoForm;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sql.Types;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ClienteService extends CrudPersonLavendereService {

    private static ClienteService instance;

    public static ClienteService getInstance() {
        if (instance == null) {
            instance = new ClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClientePdbxDao.getInstance();
    }

    //--------------------------------------------------------------------------------------------------------------
    // Validações
    //--------------------------------------------------------------------------------------------------------------

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void validateClienteBloqueado(Cliente cliente) throws SQLException {
    	if (LavenderePdaConfig.bloquearNovoPedidoClienteBloqueado || LavenderePdaConfig.confirmaNovoPedidoClienteBloqueado) {
	    	if (cliente.isStatusBloqueado()) {
		    	throw new ValidationException(Messages.MSG_CLIENTE_BLOQUEADO);
	    	}
	    }
    }

    public void validateClienteBloqueadoPorAtraso(Cliente cliente) throws SQLException {
    	if (cliente.isStatusBloqueadoPorAtraso()) {
			throw new ValidationException(Messages.MSG_CLIENTE_BLOQUEADO_POR_ATRASO);
    	}
    }
    //--Metodo coberto por testes Unitarios--//
    public void validateProdutoControladoClienteComAlvaraOuLicenca(ItemPedido itemPedido, Cliente cliente) throws SQLException {
		if (ValueUtil.getBooleanValue(itemPedido.getProduto().flProdutoControlado)) {
			if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado()) {
				validateClienteSemAlvaraOuVencidoProdutoControlado(itemPedido, cliente);
			}
			if (LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
				validateClienteSemLicencaProdutoControlado(itemPedido, cliente);
			}
			
		}
    }

	private void validateClienteSemLicencaProdutoControlado(ItemPedido itemPedido, Cliente cliente) throws SQLException {
		if(ValueUtil.isEmpty(cliente.nuLicencaProdutoControlado)) {
			ItemPedidoService.getInstance().clearDadosItemPedido(itemPedido);
			itemPedido.cdProduto = null;
			throw new ValidationException(Messages.CLIENTE_MSG_SEM_LICENCA_PRODUTO_CONTROLADO);
		}
	}

	private void validateClienteSemAlvaraOuVencidoProdutoControlado(ItemPedido itemPedido, Cliente cliente) throws SQLException {
		if (ValueUtil.isEmpty(cliente.dtVencimentoAlvara) || (DateUtil.getDaysBetween(DateUtil.getCurrentDate(), cliente.dtVencimentoAlvara) > 0)) {
			ItemPedidoService.getInstance().clearDadosItemPedido(itemPedido);
			itemPedido.cdProduto = null;
			throw new ValidationException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_CONTROLADO, (ValueUtil.isEmpty(cliente.dtVencimentoAlvara)) ? Messages.PEDIDO_LABEL_STATUSALVARA_NAO : cliente.dtVencimentoAlvara.toString()));
		}
	}

    public void setVlTotalPedClienteExcetoPedParam(Pedido pedido, Cliente cliente) throws SQLException {
       	if (LavenderePdaConfig.controlarLimiteCreditoCliente || LavenderePdaConfig.bloquearLimiteCreditoCliente || LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
       		if (cliente.vlTotalPedidosValidateLimiteCredito == -1) {
       			cliente.vlTotalPedidosValidateLimiteCredito = FichaFinanceiraService.getInstance().getVlPedidosCreditoAberto(cliente, pedido, null);
       		}
       	}
    }
    
    public void setVlTotalPedClienteExcetoPedConsignadoParam(Pedido pedido, Cliente cliente) throws SQLException {
       	if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
       		if (cliente.vlTotalPedidosValidateLimiteCreditoConsignado == -1) {
       			cliente.vlTotalPedidosValidateLimiteCreditoConsignado = FichaFinanceiraService.getInstance().getVlPedidosConsignadoCreditoAberto(pedido, cliente);
       		}
       	}
    }


    //--------------------------------------------------------------------------------------------------------------
    // Filtro
    //--------------------------------------------------------------------------------------------------------------

    public Vector findClientes(String dsFiltro, Cliente clienteFilter) throws SQLException {
       	//ve se encontra * na string de pesquisa;
    	boolean onlyStartString = false;
    	if (LavenderePdaConfig.usaPesquisaInicioString && !ValueUtil.isEmpty(dsFiltro)) {
    		if (dsFiltro.startsWith("*")) {
    			dsFiltro = dsFiltro.substring(1);
    		} else {
    			onlyStartString = true;
    		}
    	}

    	if (ValueUtil.isNotEmpty(dsFiltro)) {
    		clienteFilter.nmRazaoSocial = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    		clienteFilter.nmFantasia = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    		clienteFilter.cdCliente = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    		if (LavenderePdaConfig.isUsaFiltroCidadeUFBairroNaListaDeClientes()) {
    			clienteFilter.dsCidadeComercial = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    			clienteFilter.dsBairroComercial = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    			clienteFilter.dsEstadoComercial = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    			if (LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes()) {
    				clienteFilter.dsLogradouroComercial = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    				clienteFilter.nuLogradouroComercial = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    			}
    		}
    		if (LavenderePdaConfig.usaFiltroCnpjClienteListaClientes()) {
    			clienteFilter.nuCnpj = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    		}
    		if (LavenderePdaConfig.usaFiltroContatoListaClientes()) {    			
    			clienteFilter.nmContatoCliente = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    			clienteFilter.cdContatoCliente = onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    		}
    	} else {
    		clienteFilter.nmRazaoSocial = null;
    		clienteFilter.nmFantasia = null;
    		clienteFilter.cdCliente = null;
    		clienteFilter.dsCidadeComercial = null;
    		clienteFilter.dsBairroComercial = null;
    		clienteFilter.dsEstadoComercial = null;
    		clienteFilter.dsLogradouroComercial = null;
    		clienteFilter.nuLogradouroComercial = null;
    		clienteFilter.nuCnpj = null;
    		clienteFilter.nmContatoCliente = null;
    	}
    	
    	return findAllByExampleSummary(clienteFilter);
    }

    public Cliente getCliente(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
    	Cliente clienteFilter = new Cliente();
    	clienteFilter.cdEmpresa = cdEmpresa;
    	clienteFilter.cdRepresentante = cdRepresentante;
    	clienteFilter.cdCliente = cdCliente;
    	Cliente cliente = (Cliente)findByRowKey(clienteFilter.getRowKey());
    	if (cliente == null) {
    		cliente = new Cliente();
    	}
    	return cliente;
    }

    public String getClienteColumn(String cdEmpresa, String cdRepresentante, String cdCliente, String nmColumn) throws SQLException {
    	Cliente clienteFilter = new Cliente();
    	clienteFilter.cdEmpresa = cdEmpresa;
    	clienteFilter.cdRepresentante = cdRepresentante;
    	clienteFilter.cdCliente = cdCliente;
    	String columnValue = findColumnByRowKey(clienteFilter.getRowKey(), nmColumn);
    	if (columnValue == null) {
    		columnValue = "";
    	}
    	return columnValue;
    }

	public String getNmRazaoSocial(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
		if (!ValueUtil.isEmpty(cdEmpresa) && !ValueUtil.isEmpty(cdRepresentante) && !ValueUtil.isEmpty(cdCliente)) {
			Cliente clienteFilter = new Cliente();
			clienteFilter.cdEmpresa = cdEmpresa;
			clienteFilter.cdRepresentante = cdRepresentante;
			clienteFilter.cdCliente = cdCliente;
			clienteFilter.nmRazaoSocial = ClienteService.getInstance().findColumnByRowKey(clienteFilter.getRowKey(), "nmRazaoSocial");
			if (!ValueUtil.isEmpty(clienteFilter.nmRazaoSocial)) {
				return clienteFilter.nmRazaoSocial;
			}
			return cdCliente;
		} else {
			return cdCliente;
		}
	}

	public String getDescriptionWithKey(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
		if (ValueUtil.isNotEmpty(cdEmpresa) && ValueUtil.isNotEmpty(cdRepresentante) && ValueUtil.isNotEmpty(cdCliente)) {
			Cliente clienteFilter = new Cliente();
			clienteFilter.cdEmpresa = cdEmpresa;
			clienteFilter.cdRepresentante = cdRepresentante;
			clienteFilter.cdCliente = cdCliente;
			clienteFilter = (Cliente) ClienteService.getInstance().findByRowKey(clienteFilter.getRowKey());
			if (clienteFilter != null) {
				return clienteFilter.toString();
			}
		}
		return cdCliente;
	}

    public Vector getItemTabelaPrecoUf() throws SQLException {
		return ClientePdbxDao.getInstance().findAllUfsClientes();
    }

    public boolean isClientePossuiTabPrecoDefaultEspecial(Cliente cliente, TabelaPreco tabelaPreco) {
    	if (LavenderePdaConfig.restringeTabPrecoCondPagtoClienteEspecial) {
        	if (ValueUtil.VALOR_SIM.equals(cliente.flEspecial)) {
                boolean clientePossuiTabPrecoDefault = (tabelaPreco != null) && ValueUtil.isNotEmpty(cliente.cdTabelaPreco) && cliente.cdTabelaPreco.equals(tabelaPreco.cdTabelaPreco);
                if (clientePossuiTabPrecoDefault && tabelaPreco.isEspecial()) {
                	return true;
                }
        	}
        }
    	return false;
    }

    public Pedido copyAndInsertPedidoCliente(Pedido pedido, Cliente cliente, boolean aplicaVerbaConfirmacaoSugestaoPedido, boolean replicacaoPedido) {
    	return copyAndInsertPedidoCliente(pedido, cliente, null, aplicaVerbaConfirmacaoSugestaoPedido, replicacaoPedido, false);
    }

    public Pedido copyAndInsertPedidoCliente(Pedido pedido, Cliente cliente, Vector itensPedido, boolean aplicaVerbaConfirmacaoSugestaoPedido, boolean onReplicacaoPedido, boolean onAgrupaPedidosConsignacao) {
    	if (pedido == null) return null;
    	Pedido pedidoRef = (Pedido) pedido.clone();
		try {
			if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && !PlataformaVendaClienteService.getInstance().validatePlataformaVendaCliente(cliente, pedido)) {
				throw new ValidationException(Messages.CLIENTE_SEM_PLATAFORMA_ERRO);
			}
			alteraAtributosDoPedido(pedido, cliente, onReplicacaoPedido, onAgrupaPedidosConsignacao, pedidoRef);
			if (LavenderePdaConfig.isUsaTabelaPrecoPedido() && ((pedido.getTabelaPreco() == null) || ValueUtil.isEmpty(pedido.getTabelaPreco().cdTabelaPreco))) {
				throw new ValidationException(Messages.TABELA_PRECO_AUSENTE);
			}
			if (LavenderePdaConfig.isUsaTipoFretePedido()) {
				replicaTipoFretePedido(pedido);
			}
			PedidoService.getInstance().insert(pedido);
			PedidoService.getInstance().update(pedido); // importante fazer insert e update
			if (ValueUtil.isEmpty(itensPedido) && !pedido.isGeradoDiferenca()) {
				PedidoService.getInstance().findItemPedidoList(pedidoRef, true);
			} else {
				pedidoRef.itemPedidoList = itensPedido;
			}
			pedido.itemPedidoInseridoDivergList = new Vector();
			pedido.itemPedidoList = new Vector();
			if (LavenderePdaConfig.usaPoliticaComercial()) {
				PoliticaComercialService.getInstance().createTabelaTemporariaPoliticaComercialPedido(pedido);
			}
			insereItensVendaEBonificacao(pedido, cliente, itensPedido, aplicaVerbaConfirmacaoSugestaoPedido, onReplicacaoPedido, onAgrupaPedidosConsignacao, pedidoRef);
			insereItensTroca(pedido, pedidoRef);
			verificaErrosKitCombo(pedido);
			pedido.vlTotalItens = 1; //Tem que ter valor para o método findItemPedidoList encontrar os itens.
			PedidoService.getInstance().findItemPedidoList(pedido);
			if ((LavenderePdaConfig.isUsaConfirmacaoVerbaPedidoSugestao() || (onReplicacaoPedido && LavenderePdaConfig.isUsaConfirmacaoVerbaReplicacaoPedido())) && !LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
				VerbaSaldoService.getInstance().ajustaVerbaPedidoSugestao(pedido, aplicaVerbaConfirmacaoSugestaoPedido);
			}
			if (onReplicacaoPedido) {
				replicarCondicaoPagamentoAVista(pedido);
			}
			PedidoService.getInstance().update(pedido);
			if (onReplicacaoPedido) {
				PedidoService.getInstance().validaFuncionalidadesReplicacaoPedido(pedido);
			}
			pedido.bonificacaoLiberada = false; //Teve que ser setado para true antes da copia do pedido e agora é devolvido o false
			pedido.inserindoFromSugestaoPedido = false;
			pedido.cdContato = null;
			pedido.onReplicacao = false;
			return pedido;
		} catch (Throwable e) {
			try {
				PedidoService.getInstance().delete(pedido);
				ItemPedidoService.getInstance().desfazValorNegocionado(pedido);
				
			} catch (Throwable ex) {
				ExceptionUtil.handle(ex);
			}
			pedido = pedidoRef;
			resetaChaveItensPedido(pedido);
			if (e instanceof ValidationException) {
				throw (ValidationException)e;
			}
			throw new ValidationException(e.getMessage());
		}
    }
    
	private void verificaErrosKitCombo(Pedido pedido) throws SQLException {
		if ((LavenderePdaConfig.isExibeComboMenuInferior() || LavenderePdaConfig.isUsaKitTipo3()) && ValueUtil.isNotEmpty(pedido.itemPedidoNaoInseridoSugestaoPedList)) {
			int size = pedido.itemPedidoNaoInseridoSugestaoPedList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoNaoInseridoSugestaoPedList.items[i];
				if (ValueUtil.isNotEmpty(itemPedido.cdKit)) {
					ItemPedidoService.getInstance().deleteItensKit(itemPedido.cdKit, pedido);
					continue;
				}
				if (ValueUtil.isNotEmpty(itemPedido.cdCombo)) {
					ItemPedidoService.getInstance().deleteItensCombo(itemPedido);
				}
			}
		}
	}

	private void replicarCondicaoPagamentoAVista(Pedido pedido) throws SQLException {
		if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, pedido.getCliente().flClienteLiberadoPedidoAVista) && LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueadoAtrasado()) {
			String oldFlPagamentoAVista = pedido.flPagamentoAVista;
			pedido.flPagamentoAVista = ValueUtil.VALOR_SIM;
			Vector condicoes = CondicaoPagamentoService.getInstance().loadCondicoesPagamento(pedido);
			pedido.flPagamentoAVista = oldFlPagamentoAVista;
			if (ValueUtil.isEmpty(condicoes)) {
				return;
			}
			pedido.cdCondicaoPagamento = ((CondicaoPagamento) condicoes.items[0]).cdCondicaoPagamento;
		}
	}
    
    private void replicaTipoFretePedido(Pedido pedido) throws SQLException {
    	if (LavenderePdaConfig.usaTipoFretePorCliente) {
    		TipoFreteCli tipoFreteCliFilter = getTipoFreteCliFilter(pedido);
    		if (TipoFreteCliService.getInstance().findByPrimaryKey(getTipoFreteCliFilter(pedido)) != null) {
    			return;
    		}
    		tipoFreteCliFilter.cdTipoFrete = null;
    		tipoFreteCliFilter.limit = 1;
    		Vector tipoFreteCliList = TipoFreteCliService.getInstance().findAllByExample(tipoFreteCliFilter, "CASE TB.FLDEFAULT WHEN 'S' THEN 1 ELSE 2 END");
    		if (ValueUtil.isNotEmpty(tipoFreteCliList)) {
    			pedido.cdTipoFrete = ((TipoFreteCli) tipoFreteCliList.elementAt(0)).cdTipoFrete;
    		} else {
    			pedido.cdTipoFrete = null;
    		}
    	}	
    	//TODO rever outras regras de tipo de frete
	}

	private TipoFreteCli getTipoFreteCliFilter(Pedido pedido) {
		TipoFreteCli tipoFreteCliFilter =  new TipoFreteCli();
    	tipoFreteCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoFreteCliFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	tipoFreteCliFilter.cdCliente = pedido.cdCliente;
    	tipoFreteCliFilter.cdTipoFrete = pedido.cdTipoFrete;
		return tipoFreteCliFilter;
	}

	private void resetaChaveItensPedido(Pedido pedido) {
    	int size = pedido.itemPedidoList.size();
    	for (int i = 0; i < size; i++) {
    		ItemPedido item = (ItemPedido) pedido.itemPedidoList.elementAt(i);
    		item.nuPedido = pedido.nuPedido;
    		item.flOrigemPedido = pedido.flOrigemPedido;
    	}
    }

	private void insereItensTroca(Pedido pedido, Pedido pedidoRef) throws SQLException {
		int sizeItensTroca = pedidoRef.itemPedidoTrocaList.size();
		for (int j = 0; j < sizeItensTroca; j++) {
			ItemPedido itemPedido = (ItemPedido) pedidoRef.itemPedidoTrocaList.items[j];
			itemPedido.nuPedido = pedido.nuPedido;
			itemPedido.flOrigemPedido = pedido.flOrigemPedido;
			itemPedido.nuSeqItemPedido = j + 1;
			itemPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
			//--
			ItemPedidoService.getInstance().calculateSimples(itemPedido, pedido);
			ItemPedidoService.getInstance().insertItemSimples(itemPedido);
		}
	}

	private void insereItensVendaEBonificacao(Pedido pedido, Cliente cliente, Vector itensPedido, boolean aplicaVerbaConfirmacaoSugestaoPedido, boolean onReplicacaoPedido, boolean onAgrupaPedidosConsignacao, Pedido pedidoRef) throws SQLException, Exception {
		int sizeItens = pedidoRef.itemPedidoList.size();
		if (sizeItens == 0) return;
		HashMap<String, Produto> produtosSugPerson = null;
		boolean usaSugVendaPerson;
		if (usaSugVendaPerson = LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
			produtosSugPerson = SugVendaPersonService.getInstance().getProdutosSelecionadosMap(pedido);
		}
		pedido.itemPedidoPrecoNegociadoList = new Vector();
		for (int j = 0; j < sizeItens; j++) {
			ItemPedido itemPedido = (ItemPedido) pedidoRef.itemPedidoList.items[j];
			setaValoresIniciaisItemReplicacaoPedido(pedido, j, itemPedido);
			Produto produto = itemPedido.getProduto();
			if ((produto == null) || ValueUtil.isEmpty(produto.cdProduto)) {
				itemPedido.dsMotivoItemNaoInseridoSugestaoPedido = Messages.CLI_PRODUTO_NAO_ENCONTRADO;
				pedido.itemPedidoNaoInseridoSugestaoPedList.addElement(itemPedido);
				continue;
			}
			if (isItemSolAutorizacao(pedidoRef, itemPedido)) {
				continue;
			}
			if (isItemLiberadoPrecoSenha(pedidoRef, itemPedido)) {
				continue;
			}
			if (isItemPedidoPoliticaBonificacaoJaInseridoPedido(itemPedido)) {
				continue;
			}
			if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
				itemPedido.vlPctDescCliente = cliente.vlIndiceFinanceiro > 0 && cliente.vlIndiceFinanceiro < 1 ? (1 - cliente.vlIndiceFinanceiro) * 100 : 0;
				itemPedido.vlPctDescontoCondicao = 0.0;
				itemPedido.vlPctDescFrete = 0.0;
				itemPedido.vlPctDesconto = 0.0;
			}
			itemPedido.reloadItemTabelaPreco();
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
			if ((itemTabelaPreco == null) || ValueUtil.isEmpty(itemTabelaPreco.cdProduto)) {
				itemPedido.dsMotivoItemNaoInseridoSugestaoPedido = Messages.PRODUTO_SEM_PRECO;
				pedido.itemPedidoNaoInseridoSugestaoPedList.addElement(itemPedido);
				continue;
			}
			if (usaSugVendaPerson) {
				itemPedido.flSugVendaPerson = produtosSugPerson.containsKey(itemPedido.cdProduto) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			}
			if (LavenderePdaConfig.permiteItemPedidoComQuantidadeZero) {
				double qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
				double saldoEstoque = ValueUtil.round(qtEstoque);
				itemPedido.setQtItemFisico(saldoEstoque < itemPedido.getQtItemFisico() ? saldoEstoque : itemPedido.getQtItemFisico());
			}
			if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido()) {
				itemPedido.nuOrdemCompraCliente = ValueUtil.VALOR_NI;
				itemPedido.nuSeqOrdemCompraCliente = 0;
			}
			try {
				if (!onAgrupaPedidosConsignacao) {
					if (LavenderePdaConfig.filtraClientePorProdutoRepresentante || LavenderePdaConfig.filtraProdutoClienteRepresentante || LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
						ProdutoService.getInstance().validateProdutoRelacaoDisponivel(pedido, itemPedido);
					}
					ItemPedidoService.getInstance().ajustaDescontoItemPedido(pedido, itemPedido);
					GrupoCliPermProdService.getInstance().validatePermissaoGrupoProduto(pedido, itemPedido);
					ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, pedido);
					PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
					itemPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
					
					alteraFlTipoAlteracaoConformeNecessario(onReplicacaoPedido, pedido, itemPedido);
					
					if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
						if (itemTabelaPreco != null) {
							itemTabelaPreco.descontoQuantidadeList = DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto);
							DescQuantidadeService.getInstance().loadDescQuantidadeItemPedido(itemPedido);
						}
					}
					ajustaEstoque(pedido, onReplicacaoPedido, onAgrupaPedidosConsignacao, itemPedido);
					ajustaVerbaItemPedido(pedido, aplicaVerbaConfirmacaoSugestaoPedido, onReplicacaoPedido, itemPedido);
					if (LavenderePdaConfig.isConfigGradeProduto()) {
						ItemPedidoGradeService.getInstance().ajustaItemPedidoGrade(itemPedido, pedido);
					}
					if (ValueUtil.isNotEmpty(itensPedido)) {
						itemPedido.flOportunidade = ValueUtil.VALOR_NAO;
					}
					if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
						Vector condicaoComercialList = LavenderePdaConfig.permiteCondComercialItemDiferentePedido ? CondicaoComercialService.getInstance().loadCondicaoComercialListForCombo(pedido) : new Vector();
						ItemPedidoService.getInstance().validaCondicaoComercialItemPedido(itemPedido, condicaoComercialList);
					}
					if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
						ItemPedidoService.getInstance().validaTabelaPrecoItemPedido(pedido, itemPedido);
					}
					if (LavenderePdaConfig.usaCalculoReversoNaST) {
						itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEMST;
					}
					if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
						PedidoService.getInstance().recalculaValoresInterpolacaoReplicacao(pedido, itemPedido, true);
					}
					ItemPedidoService.getInstance().mantemPrecoNegociadoReplicacaoPedido(pedido, itemPedido, onReplicacaoPedido);
					ItemPedidoService.getInstance().zeraDescAcresByPoliticaComercial(itemPedido);
					ItemPedidoService.getInstance().calculate(itemPedido, pedido);
					setVlTotalPedClienteExcetoPedParam(pedido, cliente);
					setVlTotalPedClienteExcetoPedConsignadoParam(pedido, cliente);
					itemPedido.isIgnoraMensagemEstoqueNegativo = true;
					validateItemPedidoUINaReplicacao(pedido, itemPedido);
				} else {
					ItemPedidoService.getInstance().calculate(itemPedido, pedido);
				}
				validateAndInsertItemPedido(pedido, itemPedido);
			} catch (Throwable ex) {
				if (onAgrupaPedidosConsignacao) {
					throw ex;
				}
				if (!itemPedido.auxiliarVariaveis.isItemComDescAcresMaxExtrapolado) {
					preencheListaItensNaoInseridosNaReplicacao(pedido, itemPedido, ex);
				}
			} finally {
				pedido.ignoraValidacaoEstoque = false;
			}
		}
		if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto() && onReplicacaoPedido) {
			ItemPedidoService.getInstance().atualizaDescontosInterpolacaoPedido(pedido, null);
		}
	}

	private void preencheListaItensNaoInseridosNaReplicacao(Pedido pedido, ItemPedido itemPedido, Throwable ex) {
		itemPedido.dsMotivoItemNaoInseridoSugestaoPedido = ex.getMessage();
		pedido.itemPedidoNaoInseridoSugestaoPedList.addElement(itemPedido);
		if (pedido.itemPedidoInseridoDivergList != null) {
			pedido.itemPedidoInseridoDivergList.removeElement(itemPedido);
		}
	}

	private void validateAndInsertItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		ItemPedidoService.getInstance().validate(itemPedido);
		ItemPedidoService.getInstance().beforeSave(itemPedido, false, false, false);
		ItemPedidoService.getInstance().insert(itemPedido);
		if (!pedido.isReplicandoPedido && LavenderePdaConfig.usaDescCapaPedidoConsumindoVerbaPositivaApenasPedidoCorrente()) {
			PedidoService.getInstance().calculate(pedido);
		}
		itemPedido.pedido = pedido;
		pedido.itemPedidoList.addElement(itemPedido);
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && !itemPedido.isBonificacaoAutomatica && !itemPedido.isItemBrinde && !itemPedido.isKitTipo3() && !itemPedido.pedido.getTipoPedido().isIgnoraPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, true);
			if (LavenderePdaConfig.isUsaConsumoVerbaSupervisor() && itemPedido.isItemBonificacao()) {
				VerbaService.getInstance().aplicaVerbaBonifCfg(itemPedido, ItemPedidoBonifCfgService.getInstance().findItemPedidoBonifCfgByItemPedido(itemPedido));
				ItemPedidoService.getInstance().updateValuesVerba(itemPedido);
			}
		}
	}

	private void validateItemPedidoUINaReplicacao(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		try {
			CadItemPedidoForm.validateItemPedidoUI(itemPedido, pedido, true);
		} catch (EstoquePrevistoException e) {
			pedido.itemPedidoEstoquePrevistoExcepList.addElement(e.getMessage());
		}
		CadItemPedidoForm.validaProdutoRestrito(itemPedido.getProduto());
		itemPedido.isIgnoraMensagemEstoqueNegativo = false;
		itemPedido.cdLoteProduto = null;
	}
	
	public void validateAndInsertItemPedidoNaReplicacao(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		try {
			validateItemPedidoUINaReplicacao(pedido, itemPedido);
			validateAndInsertItemPedido(pedido, itemPedido);
		} catch (Throwable e) {
			preencheListaItensNaoInseridosNaReplicacao(pedido,itemPedido, e);
		}
	}

	private void alteraFlTipoAlteracaoConformeNecessario(boolean onReplicacaoPedido, Pedido pedido, ItemPedido itemPedido) {
		if (isDeveManterPrecoNegociado(pedido) && onReplicacaoPedido) return;
	
		if (itemPedido.vlPctDesconto > 0) {
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
		} else if (itemPedido.vlPctAcrescimo > 0) {
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()){
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANTO_RENTABILIDADE;
		}
	}
	
	private boolean isDeveManterPrecoNegociado(Pedido pedido) {
		return (LavenderePdaConfig.mantemPrecoNegociadoReplicacaoPedido || pedido.isPedidoReplicadoConvertidoTipoPedido);
	}
	
	private boolean isItemSolAutorizacao(Pedido pedido, ItemPedido itemPedido) {
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && ValueUtil.VALOR_SIM.equals(itemPedido.flAutorizado)) {
			itemPedido.dsMotivoItemNaoInseridoSugestaoPedido = Messages.SOL_AUTORIZACAO_ERRO_AUTORIZADO_REPLICACAO;
			pedido.itemPedidoNaoInseridoSugestaoPedList.addElement(itemPedido);
			return true;
		}
		return false;
	}
	
	private boolean isItemLiberadoPrecoSenha(Pedido pedido, ItemPedido itemPedido) {
		if (LavenderePdaConfig.liberaComSenhaPrecoProduto && itemPedido.isFlPrecoLiberadoSenha()) {
			itemPedido.dsMotivoItemNaoInseridoSugestaoPedido = Messages.ITEMPEDIDO_LIBERASENHA_ERRO_REPLICACAO;
			pedido.itemPedidoNaoInseridoSugestaoPedList.addElement(itemPedido);
			return true;
		}
		return false;
	}

	private void ajustaVerbaItemPedido(Pedido pedido, boolean aplicaVerbaConfirmacaoSugestaoPedido, boolean onReplicacaoPedido, ItemPedido itemPedido) throws SQLException {
		boolean ajustaVerbaItemPedido = LavenderePdaConfig.isUsaConfirmacaoVerbaPedidoSugestao() || (onReplicacaoPedido && LavenderePdaConfig.isUsaConfirmacaoVerbaReplicacaoPedido());
		if (!ajustaVerbaItemPedido && !pedido.isPedidoCriticoOuConversaoFob()) return;
		
		if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
			VerbaFornecedorService.getInstance().ajustaVerbaItemSugestaoPedido(pedido, itemPedido, aplicaVerbaConfirmacaoSugestaoPedido);
		} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
			VerbaUsuarioService.getInstance().ajustaVerbaItemSugestaoPedido(pedido, itemPedido, aplicaVerbaConfirmacaoSugestaoPedido);
		} else {
			VerbaSaldoService.getInstance().ajustaVerbaItemSugestaoPedido(pedido, itemPedido, aplicaVerbaConfirmacaoSugestaoPedido);
		}
	}

	private void ajustaEstoque(Pedido pedido, boolean onReplicacaoPedido, boolean onAgrupaPedidosConsignacao, ItemPedido itemPedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		pedido.ignoraValidacaoEstoque = (onReplicacaoPedido && tipoPedido != null && tipoPedido.isIgnoraEstoqueReplicacao()) || onAgrupaPedidosConsignacao;
		if (pedido.ignoraValidacaoEstoque) return;
		
		boolean usaEstoqueDisponivel = LavenderePdaConfig.isUsaEstoqueDisponivelItemPedidoSugestao() || (onReplicacaoPedido && LavenderePdaConfig.isUsaEstoqueDisponivelItemPedidoReplicacao());
		EstoqueService.getInstance().ajustaEstoqueItemPedido(pedido, itemPedido, usaEstoqueDisponivel);
	}

	private void setaValoresIniciaisItemReplicacaoPedido(Pedido pedido, int sequencia, ItemPedido itemPedido) {
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.pedido = pedido;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		itemPedido.vlVerbaItemOld = 0;
		itemPedido.vlVerbaItemPositivoOld = 0;
		itemPedido.vlRentabilidadeOld = 0;
		itemPedido.vlFreteOld = 0;
		itemPedido.oldQtItemFisicoDescQtd = 0;
		itemPedido.oldQtItemFisicoDescPromocionalQtd = 0;
		itemPedido.setOldQtItemFisico(0D);
		itemPedido.vlTotalItemPedidoOld = 0;
		itemPedido.nuSeqItemPedido = sequencia + 1;
		itemPedido.flTipoCadastroItem = null;
		itemPedido.qtdCreditoDesc = 0;
		itemPedido.cdProdutoCreditoDesc = null;
		itemPedido.dtEntrega = null;
		itemPedido.dtSugestaoCliente = null;
		itemPedido.dtEstoquePrevisto = null;
		if (LavenderePdaConfig.usaTabelaPrecoPorCanalAtendimento) {
			itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
		}
		if (ValueUtil.isEmpty(itemPedido.cdItemGrade1) || LavenderePdaConfig.usaGradeProduto5()) {
			itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		}
		if (!LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido && LavenderePdaConfig.getConfigGradeProduto() == 0 && !(LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) && !(LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && LavenderePdaConfig.permiteIncluirMesmoProdutoLoteDiferenteNoPedido)) {
			itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
		}
		itemPedido.vlNegociado = itemPedido.vlItemPedido;
		itemPedido.vlPctDescInicial = itemPedido.vlPctDesconto;
		itemPedido.vlPctAcrescInicial = itemPedido.vlPctAcrescimo;
		itemPedido.politicaComercial = null;
		itemPedido.cdPoliticaComercial = null;
		itemPedido.possuiDiferenca = false;
	}

	private void alteraAtributosDoPedido(Pedido pedido, Cliente cliente, boolean onReplicacaoPedido,boolean onAgrupaPedidosConsignacao, Pedido pedidoRef) throws SQLException {
		pedido.nuPedido = null; // É preciso pois caso de erro, ele exclui o pedido de referencia, pois é o mesmo n nuPedido
		pedido.dtEmissao = DateUtil.getCurrentDate();
		pedido.hrEmissao = TimeUtil.getCurrentTimeHHMM();
		pedido.hrFimEmissao = TimeUtil.getCurrentTimeHHMM();
		pedido.dtFechamento = null;
		pedido.hrFechamento = "";
		pedido.dtTransmissaoPda = null;
		pedido.hrTransmissaoPda = "";
		pedido.nuPedidoRelacionado = "";
		pedido.nuPedidoRelBonificacao = "";
		pedido.flOrigemPedidoRelacionado = ValueUtil.VALOR_NI;
		pedido.cdCargaPedido = null;
		pedido.dtEntrega = null;
		pedido.dtSugestaoCliente = null;
		if (LavenderePdaConfig.usaTabelaPrecoPorCanalAtendimento) {
			Vector tabPrecoList = TabelaPrecoService.getInstance().loadTabelasPrecos(pedido);
			pedido.cdTabelaPreco = ValueUtil.isNotEmpty(tabPrecoList) ? ((TabelaPreco) tabPrecoList.items[0]).cdTabelaPreco : "";
		}
		pedido.setCliente(cliente);
		PedidoService.getInstance().setCdClienteHashValuesDinamicos(pedido);
		PedidoService.getInstance().setDsEmailsDestinoHashValuesDinamicos(pedido);
		PedidoService.getInstance().setHashValuesDinamicosByEntidadeCliente(pedido);
		if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada) {
			pedido.cliente.somentePedidoPreOrcamento = SessionLavenderePda.getCliente().somentePedidoPreOrcamento;
		}
		if (LavenderePdaConfig.permiteIndicarDataEntregaManualQuandoUsaCadastroEntrega) {
			pedido.dtEntrega = null;
		} else if (LavenderePdaConfig.usaWebserviceSankhyaComplementaPedido) {
			PedidoService.getInstance().getAndSetInformacoesComplementaresWebserviceSankhya(pedido);
		} else {
			pedido.dtEntrega = PedidoService.getInstance().getDataPrevisaoEntrega(pedido, cliente);
		}
		if (onAgrupaPedidosConsignacao) {
			pedido.cdPedidosAgrupados = null;
		} else if (onReplicacaoPedido) {
			pedido.flPedidoReplicado = ValueUtil.VALOR_SIM;
			pedido.nuPedidoOriginal = pedidoRef.nuPedido;
			pedido.nuPedidoSugestao = pedidoRef.nuPedido;
			pedido.flOrigemPedidoSugestao = pedidoRef.flOrigemPedido;
			pedido.inserindoFromSugestaoPedido = false;
			pedido.onReplicacao = true;
			if (LavenderePdaConfig.isIgnoraConfiguracoesReplicacao(Pedido.IGNORA_CONDPAGTO_REPLICACAO) && !CondicaoPagamentoService.getInstance().isCondicaoPagamentoPedidoValida(pedido)) {
				pedido.setCondicaoPagamento(null);
				pedido.cdCondicaoPagamento = ValueUtil.VALOR_NI;
				if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico() || LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalItensPedidoDescontadosIncentivos()) {
					pedido.vlPctDesconto = 0;
				} else if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual()) {
					pedido.vlPctDescontoCondicao = 0;
				}
			}
		} else {
			pedido.nuPedidoSugestao = "";
			pedido.flOrigemPedidoSugestao = "";
			pedido.flPedidoReplicado = "";
			pedido.nuPedidoOriginal = "";
			pedido.inserindoFromSugestaoPedido = !pedido.isGeradoDiferenca();
		}
		pedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		pedido.bonificacaoLiberada = true; // Necessário para não validar bonificação durante a cópia dos itens do pedido
		pedido.showMessageLimiteCredito = false; // Necessário para não exibir mesnagens de limite de crédito a cada item inserido
		pedido.insertVisita = pedido.deletadoPelaIntefacePedido = LavenderePdaConfig.isUsaAgendaDeVisitas() && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita;
		pedido.flSituacaoReservaEst = null;
		pedido.flImpresso = null;
		pedido.flNfeImpressa = null;
		pedido.flNfeContImpressa = null;
		pedido.flBoletoImpresso = null;
		pedido.flConsignacaoImpressa = null;
		pedido.flPossuiDiferenca = null;
		pedido.flClienteAtrasadoLiberadoSenha = null;
		pedido.flCreditoClienteLiberadoSenha = null;
		pedido.flEtapaVerba = null;
		pedido.flGeraBoleto = null;
		pedido.flGeraNfe = null;
		pedido.flGeraNfce = null;
		pedido.flMaxVendaLiberadoSenha = null;
		pedido.flPrecoLiberadoSenha = null;
		pedido.flSugestaoVendaLiberadoSenha = null;
		pedido.flPendente = null;
		pedido.flPendenteLimCred = null;
		pedido.flPendenteCondPagto = null;
		pedido.dtEntregaLiberada = null;
		pedido.cdUsuarioLibEntrega = null;
		pedido.vlTotalPedido = 0.0;
		pedido.dsObservacao = null;
		pedido.cdEntrega = null;
		pedido.flConvertePedidoReplicacao = null;
		pedido.cdContaCorrente = null;
		pedido.bonifCfgFaixaQtdeAtualMap = null;
		pedido.bonifCfgFaixaQtdeOldMap = null;
		if(!pedido.isEdicaoBloqueada()){
			pedido.getHashValuesDinamicos().remove(Pedido.NMCOLUNA_DSOBSERVACAO);
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			TipoPedido tipoPedido = TipoPedidoService.getInstance().getTipoPedido(pedido.cdTipoPedido);
			if (tipoPedido == null) {
				tipoPedido = TipoPedidoService.getInstance().getFirstTipoPedido();
				if (tipoPedido == null) {
					throw new NullPointerException(Messages.MSG_NECESSARIO_RECEBER_DADOS);
				}
			}
		}
		if (LavenderePdaConfig.isTipoPagamentoOcultoAndSetaPadraoCliente()) {
			pedido.cdTipoPagamento = cliente.cdTipoPagamento;
		}
		if (pedido.isGeradoDiferenca()) {
			pedido.vlPctDescCliente = 0;
			pedido.vlPctDescontoCondicao = 0.0;
			pedido.vlPctDescFrete = 0.0;
			pedido.vlPctDesc2 = 0;
			pedido.vlPctDesc3 = 0;
		} else {
			if (LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
				pedido.vlPctDescCliente = cliente.vlIndiceFinanceiro > 0 && cliente.vlIndiceFinanceiro < 1 ? (1 - cliente.vlIndiceFinanceiro) * 100 : 0;
				pedido.vlPctDescontoCondicao = 0.0;
				pedido.vlPctDescFrete = 0.0;
			}
			if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
				pedido.vlPctDescCliente = 0;
				pedido.vlPctDesc2 = 0;
				pedido.vlPctDesc3 = 0;
			}
		}
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() && pedido.getCliente().isNovoCliente()) {
			pedido.flPedidoNovoCliente = ValueUtil.VALOR_SIM;
		} else {
			pedido.flPedidoNovoCliente = ValueUtil.VALOR_NAO;
		}
		if (!onAgrupaPedidosConsignacao) {
			pedido.itemPedidoList = new Vector();
		}
	}

	public String[] getClienteInadimplementeParams(Cliente cliente, int qtDiasAtrasoCliente) {
		String param1 = StringUtil.getStringValue(cliente.nmRazaoSocial) + ".";
		if (param1.length() >= 21) {
			param1 = cliente.nmRazaoSocial.substring(0, 20) + ".";
		}
		String[] params = { param1,	StringUtil.getStringValueToInterface(qtDiasAtrasoCliente) };
		return params;
	}

	public void saveClienteRedeLiberadoComSenha(Cliente cliente) {
		ClienteRedeLiberadoComSenhaConfig.put(cliente.cdContratoEspecial);
		String value = ClienteRedeLiberadoComSenhaConfig.prepareValues();
		//--
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.clienteRedeConfig;
		configInterno.vlChave = ConfigInterno.VLCHAVEDEFAULT;
		configInterno.vlConfigInterno = value;
		//--
		try {
			ConfigInternoService.getInstance().insertOrUpdateConfigInterno(configInterno);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public double getVltotalTitulosVencidos(Cliente cliente, Date date, double vlTotalPagamentos) throws SQLException {
		double vlTotalTitulosVencidos = 0;
		Vector listVlTitulosVlPago = TituloFinanceiroService.getInstance().getVlTitulosVlPagoByExample(cliente, date);
		int listSize = listVlTitulosVlPago.size();
		if (listSize > 0) {
			for (int i = 0; i < listSize; i++) {
				TituloFinanceiro tituloFinanceiroFilter = (TituloFinanceiro)listVlTitulosVlPago.items[i];
				vlTotalTitulosVencidos += tituloFinanceiroFilter.vlTitulo - tituloFinanceiroFilter.vlPago;
			}
		}
    	return ValueUtil.round(vlTotalTitulosVencidos - vlTotalPagamentos, LavenderePdaConfig.nuCasasDecimais);
    }

	public boolean isClienteAtrasadoLiberadoPagamento(Cliente cliente, Date date, int nuDias) throws SQLException {
		if (LavenderePdaConfig.isUsaRegistroPagamentoParaClienteAtrasado()) {
			DateUtil.decDay(date, nuDias);
			double vlTotalPagamentos = PagamentoService.getInstance().getVlTotalPagamento(cliente);
			double vlTotalSaldoTituloVencidoPago = getVltotalTitulosVencidos(cliente, date, vlTotalPagamentos);
			if (vlTotalSaldoTituloVencidoPago <= 0 && vlTotalPagamentos > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidaBloqueiaClienteAtrasado(int qtDiasAtrasoCliente) {
		if ((LavenderePdaConfig.bloqueiaClienteAtrasadoNovoPedido > 0) && (LavenderePdaConfig.bloqueiaClienteAtrasadoNovoPedido <= qtDiasAtrasoCliente)) {
			return true;
		}
		return false;
	}

	public boolean isValidaLiberaSenhaClienteAtrasado(int qtDiasAtrasoCliente, boolean onFechamentoPedido) {
		final int nuDiasAtrasoParam = LavenderePdaConfig.nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido;
		if (nuDiasAtrasoParam > 0 && nuDiasAtrasoParam <= qtDiasAtrasoCliente) {
			return LavenderePdaConfig.validaClienteAtrasadoApenasAoFecharPedido ? onFechamentoPedido : true;
		}
		return false;
	}

	public boolean isValidaAvisaClienteAtrasado(int qtDiasAtrasoCliente, boolean onFechamentoPedido) {
		if ((LavenderePdaConfig.avisaClienteAtrasadoNovoPedido > 0) && (LavenderePdaConfig.avisaClienteAtrasadoNovoPedido <= qtDiasAtrasoCliente)) {
			return LavenderePdaConfig.validaClienteAtrasadoApenasAoFecharPedido ? onFechamentoPedido : true;
		}
		return false;
	}

	public boolean isValidaLiberaSenhaClienteRedeAtrasado(int qtDiasAtrasoCliente, boolean onFechamentoPedido) {
		Cliente cliente = SessionLavenderePda.getCliente();
		if ((SessionLavenderePda.getCliente().isClienteRedeContratoEspecial() || ValueUtil.isNotEmpty(cliente.cdRede)) && LavenderePdaConfig.isLiberaComSenhaClienteRedeAtrasadoNovoPedido() && LavenderePdaConfig.liberaComSenhaClienteRedeAtrasadoNovoPedido <= qtDiasAtrasoCliente) {
			if (ClienteRedeLiberadoComSenhaConfig.isClienteRedeLiberadoComSenha(cliente.cdContratoEspecial)) {
				return false;
			}
			return !LavenderePdaConfig.validaClienteAtrasadoApenasAoFecharPedido || onFechamentoPedido;
		}
		return false;
	}
	
	
	/**
	 * Validação para:
	 * <p>Liberação por senha para cliente de rede atrasado</p>
	 * <p>Bloqueio para cliente atrasado</p>
	 * <p>Liberação por senha para cliente atrasdo</p>
	 * <p>Aviso de cliente atrasado</p>
	 * <p>cliente atrasado porém liberado por pagamento</p>
	 * Validações acima são realizadas se o cliente não for do tipo especial.
	 * @param cliente 
	 * @param onFecharPedidosEmLote 
	 * @param flClienteAtrasadoLiberadoSenha 
	 * @param ignoraValidacaoClienteAtrasado 
	 * @throws SQLException 
	 */

	public void validateClienteInadimplente(Cliente cliente, boolean onFecharPedidosEmLote, boolean flClienteAtrasadoLiberadoSenha, boolean ignoraValidacaoClienteAtrasado, boolean onFechamentoPedido) throws SQLException {
		if (!ValueUtil.VALOR_SIM.equals(SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista)) {
			int qtDiasAtrasoCliente = TituloFinanceiroService.getInstance().getDiasAtrasoCliente(SessionLavenderePda.getCliente());
			String[] params = getClienteInadimplementeParams(cliente, qtDiasAtrasoCliente);
			boolean isClienteAtrasadoLiberadoPagamento = false;
			boolean isClienteRedeLiberadoComSenha = ClienteRedeLiberadoComSenhaConfig.isClienteRedeLiberadoComSenha(cliente.cdContratoEspecial);
			Rede redeCliente = cliente.getRede();
			int qtClienteAtrasado = RedeClienteService.getInstance().getQtClientesAtrasoByRede(redeCliente);
			if (qtDiasAtrasoCliente == 0 && qtClienteAtrasado == 0) {
				return;
			}
			if (!cliente.isEspecial()) {
				String message = MessageUtil.getMessage(Messages.MSG_CLIENTE_ATRASADO_TITULO, params);
				StringBuffer strBuffer = new StringBuffer();
				if (isValidaLiberaSenhaClienteRedeAtrasado(qtDiasAtrasoCliente, onFechamentoPedido) && !isClienteRedeLiberadoComSenha) {
					int qtClientesAtrasado = RedeClienteService.getInstance().getQtClientesAtrasoByRede(cliente.getRede());
					throw new ClienteInadimplenteException(MessageUtil.getMessage(Messages.MSG_REDE_CLIENTE_ATRASADO, new String[] {cliente.getRede().toString(), StringUtil.getStringValue(qtClientesAtrasado)}), Cliente.CLIENTE_ATRASADO_REDE_LIBERA_SENHA);
				}
				//-- Bloqueia cliente atrasado
				if (isValidaBloqueiaClienteAtrasado(qtDiasAtrasoCliente) && !isClienteRedeLiberadoComSenha) {
					isClienteAtrasadoLiberadoPagamento = isClienteAtrasadoLiberadoPagamento(cliente, DateUtil.getCurrentDate(), LavenderePdaConfig.bloqueiaClienteAtrasadoNovoPedido);
					if (!isClienteAtrasadoLiberadoPagamento) {
						throw new ClienteInadimplenteException(strBuffer.append(Messages.CLIENTE_FINANCEIRO_BLOQUEADO).append(message).toString(), Cliente.CLIENTE_ATRASADO_BLOQUEADO);
					}
				} 
				//-- Libera com senha cliente atrasado
				if (isValidaLiberaSenhaClienteAtrasado(qtDiasAtrasoCliente, onFechamentoPedido) && !isClienteRedeLiberadoComSenha && !flClienteAtrasadoLiberadoSenha) {
					isClienteAtrasadoLiberadoPagamento = isClienteAtrasadoLiberadoPagamento(cliente, DateUtil.getCurrentDate(), LavenderePdaConfig.nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido);
					if (!isClienteAtrasadoLiberadoPagamento) {
						throw new ClienteInadimplenteException(MessageUtil.getMessage(Messages.MSG_CLIENTE_ATRASADO_TITULO, params), Cliente.CLIENTE_ATRASADO_LIBERA_SENHA);
					}
				}
				//-- Avisa cliente atrasado
				if (ignoraValidacaoClienteAtrasado) {
					return;
				}
				if (isValidaAvisaClienteAtrasado(qtDiasAtrasoCliente, onFechamentoPedido) && !isClienteRedeLiberadoComSenha && !flClienteAtrasadoLiberadoSenha) {
					isClienteAtrasadoLiberadoPagamento = isClienteAtrasadoLiberadoPagamento(cliente, DateUtil.getCurrentDate(), LavenderePdaConfig.avisaClienteAtrasadoNovoPedido);
					if (!isClienteAtrasadoLiberadoPagamento) {
						ClienteInadimplenteException cliInadiEx = new ClienteInadimplenteException(MessageUtil.getMessage(Messages.MSG_CLIENTE_ATRASADO_TITULO, params), Cliente.CLIENTE_ATRASADO_AVISA);
						cliInadiEx.redeAtraso = redeCliente;
						cliInadiEx.qtClientesRedeAtraso = qtClienteAtrasado;
						throw cliInadiEx;
					}
				} else if (LavenderePdaConfig.apresentaListaClienteRedeTituloFinanceiro && !onFechamentoPedido && !onFecharPedidosEmLote && ValueUtil.isNotEmpty(cliente.cdRede)) {
					if (qtClienteAtrasado > 0) {
						params = new String[]{cliente.getRede().toString(), StringUtil.getStringValue(qtClienteAtrasado)};
						ClienteInadimplenteException cliInadiEx = new ClienteInadimplenteException(MessageUtil.getMessage(Messages.MSG_REDE_CLIENTE_ATRASADO, params), Cliente.CLIENTE_ATRASADO_REDE);
						cliInadiEx.qtClientesRedeAtraso = qtClienteAtrasado;
						cliInadiEx.redeAtraso = redeCliente;
						throw cliInadiEx;
					}
				}
				if (isClienteAtrasadoLiberadoPagamento && !onFecharPedidosEmLote && !flClienteAtrasadoLiberadoSenha) {
					throw new ClienteInadimplenteException(MessageUtil.getMessage(Messages.CLIENTE_ATRASADO_LIBERADO_PAGAMENTO, params), Cliente.CLIENTE_ATRASADO_LIBERADO_PAGAMENTO);
				}
			}
		}
	}

	public void updateFlAtendido(Cliente cliente) throws SQLException {
		ClientePdbxDao.getInstance().updateFlAtendido(cliente);
	}

	public void updateFlTipoCadastro(NovoCliente novoCliente) throws SQLException {
		ClientePdbxDao.getInstance().updateFlTipoCadastro(novoCliente);
	}

	public Vector findAllEmpresaCliente(String cdCliente) throws SQLException {
		return ClientePdbxDao.getInstance().findAllEmpresaCliente(cdCliente);
	}

	public boolean isAvisaVencimentoAlvara(Cliente cliente, int nuDiasControleAlvara) {
		if (cliente != null) {
			return ValueUtil.isNotEmpty(cliente.dtVencimentoAlvara) && (DateUtil.getDaysBetween(cliente.dtVencimentoAlvara, DateUtil.getCurrentDate()) <= nuDiasControleAlvara);
		}
		return false;
	}

	public boolean isAvisaVencimentoAfe(Cliente cliente, int nuDiasControleAfe) {
		if (cliente != null) {
			return ValueUtil.isNotEmpty(cliente.dtVencimentoAfe) && (DateUtil.getDaysBetween(cliente.dtVencimentoAfe, DateUtil.getCurrentDate()) <= nuDiasControleAfe);
		}
		return false;
    }
	
	public Vector findAllClientesRede(Rede rede) throws SQLException {
		Cliente clienteFilter = new Cliente();
		if (!LavenderePdaConfig.usaLimiteCreditoRedeCompartilhadoEmpresas) {
			clienteFilter.cdEmpresa = rede.cdEmpresa;
		}
		clienteFilter.cdRepresentante = rede.cdRepresentante;
		clienteFilter.cdRede = rede.cdRede;
		return ClienteService.getInstance().findAllByExample(clienteFilter);
	}

	public Cliente getClienteDefault(String cdClienteDefault, String nmClienteDefault) throws SQLException {
		Cliente clienteDefault = new Cliente();
		clienteDefault.cdEmpresa = SessionLavenderePda.cdEmpresa;
		clienteDefault.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Cliente.class);
		clienteDefault.cdCliente = cdClienteDefault;
		clienteDefault.nmFantasia = clienteDefault.nmRazaoSocial = nmClienteDefault;
		if ((Cliente) findByRowKey(clienteDefault.getRowKey()) == null) {
			ClientePdbxDao.getInstance().insert(clienteDefault);
		} 
		return clienteDefault;
	}
	
	public void deleteNovoCliente(NovoCliente novoCliente) {
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() || LavenderePdaConfig.permitePesquisaMercadoNovoCliente || LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente() || LavenderePdaConfig.isUsaPesquisaNovoCliente() || LavenderePdaConfig.isUsaContatosNovoCliente()) {	
			Cliente clienteFilter = new Cliente();
			clienteFilter.cdCliente = novoCliente.nuCnpj;
			clienteFilter.cdEmpresa = novoCliente.cdEmpresa;
			clienteFilter.cdRepresentante = novoCliente.cdRepresentante;
			try {
				delete(clienteFilter);
			} catch (Throwable e) {
				//Não faz nada
			}
		}
	}
	
	public String getNuFoneCliente(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = cdEmpresa;
		cliente.cdRepresentante = cdRepresentante;
		cliente.cdCliente = cdCliente;
		return findColumnByRowKey(cliente.getRowKey(), "nuFone");
	}

	public void atualizaNuDiasSemPesquisa(String nmColuna) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cliente.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		cliente.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		updateColumn(cliente.getRowKey(), nmColuna, 0, Types.INTEGER);
	}
	
	public int getAvisaClienteSemPesquisaMercado(String nmColuna) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cliente.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		cliente.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		int nuDiasSemPesquisa = ValueUtil.getIntegerValue(findColumnByRowKey(cliente.getRowKey(), nmColuna));
		if (nuDiasSemPesquisa == -1) {
			return -1;
		}
		int nuDiasMaxSemPesquisa = LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado();
		if (nuDiasMaxSemPesquisa != 0 && nuDiasSemPesquisa >= nuDiasMaxSemPesquisa) {
			return 0;
		}
		return 1;
	}
	
	protected String getFlRecebeEmailOld(String cdCliente) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cliente.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cliente.cdCliente = cdCliente;
		return findColumnByRowKey(cliente.getRowKey(), "FLRECEBEEMAIL");
	}
	
	protected String getFlRecebeSMSOld(String cdCliente) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cliente.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cliente.cdCliente = cdCliente;
		return findColumnByRowKey(cliente.getRowKey(), "FLRECEBESMS");
	}

	public boolean isClienteValidaQtMaxVendaProduto(Cliente cliente) {
		if ("S".equals(LavenderePdaConfig.dsTipoClienteQtMaxVendaProduto) || "1".equals(LavenderePdaConfig.dsTipoClienteQtMaxVendaProduto)) {
			return true;
		} else if ("2".equals(LavenderePdaConfig.dsTipoClienteQtMaxVendaProduto) && ValueUtil.isNotEmpty(cliente.nuCnpj) && CpfCnpjValidator.isCnpj(cliente.nuCnpj)) {
			return true;
		} else if ("3".equals(LavenderePdaConfig.dsTipoClienteQtMaxVendaProduto) && ValueUtil.isNotEmpty(cliente.nuCnpj) && CpfCnpjValidator.isCpf(cliente.nuCnpj)) {
			return true;
		}
		return false;
	}
	
	public Vector findAllCidadeByExample(String cdEstadoComercial) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cliente.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (ValueUtil.isNotEmpty(cdEstadoComercial)) cliente.cdEstadoComercial = cdEstadoComercial;
		return ClientePdbxDao.getInstance().findAllCidadeByExample(cliente);
	}
	
	public Vector findAllBairroByExample(String dsCidadeComercial, String cdEstadoComercial) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cliente.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cliente.dsCidadeComercial = dsCidadeComercial;
		cliente.cdEstadoComercial = cdEstadoComercial;
		return ClientePdbxDao.getInstance().findAllBairroByExample(cliente);
	}
	
	public boolean isPossuiVisitaEmAndamento(String cdCliente) {
		return SessionLavenderePda.visitaAndamento != null && ValueUtil.isNotEmpty(cdCliente) && cdCliente.equals(SessionLavenderePda.visitaAndamento.cdCliente);
		
	}
	
	public Image[] getIconsIndicadores(Cliente cliente, Map<String, Image> indicadoresMap, int iconSize, String origem) throws SQLException {
		if (isApresentaIndicadores(origem)) {
    		List<Image> iconesIndicadores = new ArrayList<>();
    		MarcadorCliente marcadorCliente = new MarcadorCliente();
    		marcadorCliente.cdEmpresa = cliente.cdEmpresa;
    		marcadorCliente.cdRepresentante = cliente.cdRepresentante;
    		marcadorCliente.cdCliente = cliente.cdCliente;
    		Vector marcadores = MarcadorDbxDao.getInstance().buscaMarcadoresPorCliente(marcadorCliente);
    		for (int i = 0; i < marcadores.size(); i++) {
    			Marcador marcador = (Marcador) marcadores.elementAt(i);
    			addMarcador(indicadoresMap, iconSize, iconesIndicadores, marcador);
			}
    		return iconesIndicadores.toArray(new Image[iconesIndicadores.size()]);
    	}
		return null;
	}
	
	public Image[] getIconsIndicadores(Cliente cliente, HashMap<String, Marcador> marcadoresHash, Map<String, Image> indicadoresMap, int iconSize, String origem) throws SQLException {
		if (!isApresentaIndicadores(origem) || cliente.cdMarcadores == null || marcadoresHash == null) return null;

		List<Image> iconesIndicadores = new ArrayList<>();
		String[] marcadoresCliente = cliente.cdMarcadores.split(",");

		for (int i = 0; i < marcadoresCliente.length; i++) {
			Marcador marcador = marcadoresHash.get(marcadoresCliente[i]);
			if (marcador == null || marcador.imMarcadorAtivo == null) continue;
			try {
				Image image = UiUtil.getImage(marcador.imMarcadorAtivo.clone());
				image = UiUtil.getSmoothScaledImage(image, iconSize, iconSize);
				indicadoresMap.put(marcador.cdMarcador, image);
				iconesIndicadores.add(image);
			} catch (ApplicationException ex) {
				ExceptionUtil.handle(ex);
			}
		}
		return iconesIndicadores.toArray(new Image[iconesIndicadores.size()]);
    	}

	private boolean isApresentaIndicadores(String origem) {
		return (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas && Cliente.APRESENTA_LISTAGENDA.equals(origem)) 
				|| (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && Cliente.APRESENTA_LISTACLI.equals(origem))
				|| (LavenderePdaConfig.apresentaIndicadoresClienteRiscoChurn && ClienteChurn.APRESENTA_INDICADOR.equals(origem));
	}

	private void addMarcador(Map<String, Image> indicadoresMap, int iconSize, List<Image> iconesIndicadores, Marcador marcador) {
		if (marcador.imMarcadorAtivo != null) {
			Image image = UiUtil.getImage(marcador.imMarcadorAtivo.clone());
			image = UiUtil.getSmoothScaledImage(image, iconSize, iconSize);
			indicadoresMap.put(marcador.cdMarcador, image);
			iconesIndicadores.add(image);
		}
	}

	
	public boolean isValidaClienteAtrasadoPorValorTotalTitulosEmAtraso(Pedido pedido, Cliente cliente) throws SQLException {
		return LavenderePdaConfig.isUsaValidacaoClienteAtrasadoPorValorTotalTitulosEmAtraso()
				&& (pedido == null || (!ValueUtil.VALOR_SIM.equals(pedido.flClienteAtrasadoLiberadoSenha) && (pedido.getTipoPedido() == null || !pedido.getTipoPedido().isIgnoraClienteAtrasado()))) 
				&& (cliente == null || (!cliente.flClienteAtrasadoLiberadoSenha && !cliente.isEspecial()));
	}

	public boolean isClienteAtrasadoPorVlTotalTitulosAtraso(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.isBloqueiaClienteAtrasadoPorValorTotalTitulosEmAtraso() && !cliente.isEspecial()) {
			return TituloFinanceiroService.getInstance().getVlTotalTitulosAtraso() > cliente.vlMaxTitulosAtraso;
		}
		return false;
	}
	
	public boolean isPermiteMultEnd() throws SQLException {
		Campo campoPermiteMultEnd = ClienteService.getInstance().getCampo(Cliente.TABLE_NAME_WEB, Cliente.FLPERMITEMULTIPLOSENDERECOS);
		return campoPermiteMultEnd != null && campoPermiteMultEnd.isVisivelCad();
	}

	public int getStyleCliente(Cliente cliente, boolean apresentaIndicadores) throws SQLException {
		if (LavenderePdaConfig.usaCorCadastroMarcador && apresentaIndicadores) {
			String vlRGB = MarcadorClienteService.getInstance().findColorLastMarcadorCli(cliente);
			if (ValueUtil.isNotEmpty(vlRGB)) {
				String[] values = vlRGB.split(",");
				if (values.length == 3) {
					return Color.getRGB(ValueUtil.getIntegerValue(values[0]), ValueUtil.getIntegerValue(values[1]), ValueUtil.getIntegerValue(values[2]));
				}
			}
		}
		return -1;
	}
	
	public String verificaClienteAberturaOuFundacao(Cliente cliente) throws SQLException {
		String message = null;
		if (LavenderePdaConfig.nuDiaClienteAbertura > 0 || LavenderePdaConfig.nuDiaClienteFundacao > 0) {
			if (cliente.cdCliente.equals(Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO)) return message;
			Date dataAtual = DateUtil.getCurrentDate();
			int dataAbertura = cliente.dtAbertura != null ? DateUtil.getDaysBetween(dataAtual, cliente.dtAbertura) : 0;
			int dataFundacao = cliente.dtFundacao != null ? DateUtil.getDaysBetween(dataAtual, cliente.dtFundacao) : 0;
			boolean abertura = LavenderePdaConfig.nuDiaClienteAbertura > 0 && dataAbertura >= 0 && dataAbertura <= LavenderePdaConfig.nuDiaClienteAbertura;
			boolean fundacao = LavenderePdaConfig.nuDiaClienteFundacao > 0 && dataFundacao >= 0 && dataFundacao <= LavenderePdaConfig.nuDiaClienteFundacao;
			if (fundacao) {
				message = Messages.CLIENTE_FUNDACAO;
				cliente.isFundacao = true;
			} else if (abertura) {
				message = Messages.CLIENTE_ABERTURA;
				cliente.isAbertura = true;
			}
			Cliente clienteSession = SessionLavenderePda.getCliente();
			if (clienteSession != null && clienteSession.equals(cliente)) {
				clienteSession.isAbertura = cliente.isAbertura;
				clienteSession.isFundacao = cliente.isFundacao;
			}
		}
		return message;
	}
	
	public double sumVlVendaMensalClientesRede(Cliente cliente) throws SQLException {
		return ClientePdbxDao.getInstance().sumVlVendasMensalClientesRede(cliente.cdRede);
	}
	
	public boolean hasClienteUpdates() {
		try {
			return SyncManager.hasTableUpdate(Cliente.TABLE_NAME, getMaxCarimbo());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}
	
	public void recebeAtualizacaoCliente() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.CLIENTE_MSG_RECEBENDO_DADOS);
		try {
			mb.popupNonBlocking();
			ConfigIntegWebTc configIntegWebTc = new ConfigIntegWebTc();
			configIntegWebTc.dsTabela = Cliente.TABLE_NAME;
			configIntegWebTc = (ConfigIntegWebTc)ConfigIntegWebTcService.getInstance().findByPrimaryKey(configIntegWebTc);
			ConfigIntegWebTc[] configArray = {configIntegWebTc};
			Vector list = new Vector(configArray);
			LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
			erpToPda.recebeDadosDisponiveisServidor(SyncManager.getInfoAtualizacaoByWeb2SyncList(list));
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
		} finally {
			mb.unpop();
		}
	}
	
	public void limpaFlAtualizaCadastroWmw(Cliente cliente) throws SQLException {
		cliente.flAtualizaCadastroWmw = null;
		ClientePdbxDao.getInstance().updateFlAtualizaCadastroWMW(cliente);
	}
	
	public boolean isClienteComAtrasoTolerancia(Cliente cliente) throws SQLException {
		return LavenderePdaConfig.liberaTipoCondPagtoPedidoDias && 
				LavenderePdaConfig.nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido > 0 && 
				TituloFinanceiroService.getInstance().getDiasAtrasoCliente(cliente) < LavenderePdaConfig.nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido;
	}
	
	public Vector findUltimosPrecosClienteRede(ItemPedido item, String cdRede) throws SQLException {
		return ClientePdbxDao.getInstance().findUltimosPrecosClienteRede(item, cdRede);
	}
	
	public boolean isPossuiUltimosPrecosRede(ItemPedido item, String cdRede) throws SQLException {
		return ClientePdbxDao.getInstance().countUltimosPrecosClienteRede(item, cdRede) > 0;
	}


	public int resetaDivulgaInfo() {
		return ClientePdbxDao.getInstance().resetaNuDivulgaInfo();
	}

	public int updateVisualizacaoDivulgaInfo(final String rowKey) throws SQLException {
		return ClientePdbxDao.getInstance().updateVisualizacaoDivulgaInfo(rowKey);
	}
	
	public Vector findAllRedeByExample() throws SQLException {
		return ClientePdbxDao.getInstance().fildAllDistinctRedeCliente();
	}

	public Vector findAllByExampleForCombo(Cliente clienteFilter) throws SQLException {
		return ClientePdbxDao.getInstance().findAllByExampleForCombo(clienteFilter);
	}

	public int getTotalClientePorRepresentante(String cdRepresentante) throws SQLException {
		Cliente clienteFilter = new Cliente();
		clienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		clienteFilter.cdRepresentante = cdRepresentante;
		return ClientePdbxDao.getInstance().countByExample(clienteFilter);
	}

	public void marcaFlOcultoClienteNovoCliente(Cliente cliente) {
		try {
			ClienteService.getInstance().updateColumn(cliente.getRowKey(), NovoCliente.NMCOLUNA_FLOCULTO, ValueUtil.VALOR_SIM, Types.VARCHAR);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public Vector findDistinctStatusCliente(String cdRepresentanteFilter) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cliente.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cdRepresentanteFilter : SessionLavenderePda.getRepresentante().cdRepresentante;
		return ClientePdbxDao.getInstance().findDistinctStatusCliente(cliente);
	}

	public void insertClienteByNovoCliente(NovoCliente novoCliente) throws SQLException {
		ClientePdbxDao.getInstance().insertByNovoCliente(novoCliente.getRowKey());
		Cliente clienteOficialByNovoCliente = NovoClienteService.getInstance().getClienteOficialByNovoCliente(novoCliente);
		if (clienteOficialByNovoCliente.cdEstadoComercial != null && clienteOficialByNovoCliente.dsEstadoComercial == null) {
			clienteOficialByNovoCliente.dsEstadoComercial = clienteOficialByNovoCliente.cdEstadoComercial;
			ClientePdbxDao.getInstance().updateDsEstadoComercial(clienteOficialByNovoCliente);
			NovoClientePdbxDao.getInstance().updateCdUfComercial(clienteOficialByNovoCliente);
		}

	}
	
	public boolean isDescontoBloqueado(Cliente cliente, String desconto) {
		if(ValueUtil.isEmpty(cliente.dsDescontoBloqueadoList)) return false;
		
		List<String> valor = new ArrayList<>(Arrays.asList(cliente.dsDescontoBloqueadoList.split("\\|")));
		if(valor.contains(desconto)) return true;
		
		return false;
	}
	
	public boolean isAcrescimoBloqueado(Cliente cliente, String acrescimo) {
		if(ValueUtil.isEmpty(cliente.dsAcrescimoBloqueadoList)) return false;
		
		List<String> valor = new ArrayList<>(Arrays.asList(cliente.dsAcrescimoBloqueadoList.split("\\|")));
		if(valor.contains(acrescimo)) return true;
		
		return false;
	}
	
	private boolean isItemPedidoPoliticaBonificacaoJaInseridoPedido(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.isUsaPoliticaBonificacao() && !itemPedido.isKitTipo3() && ItemPedidoBonifCfgService.getInstance().countByExample(new ItemPedidoBonifCfg(itemPedido)) > 0;
	}
	
	public void updateDtFimPesquisa(PesquisaApp pesquisaApp) throws SQLException {
		ClientePdbxDao.getInstance().updateDtFimPesquisa(pesquisaApp);
	}

}
