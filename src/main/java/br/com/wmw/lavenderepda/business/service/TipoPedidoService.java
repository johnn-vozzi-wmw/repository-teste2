package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedidoCli;
import br.com.wmw.lavenderepda.business.validation.EstoqueException;
import br.com.wmw.lavenderepda.business.validation.ItensDivergentesSemEstoqueException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPedidoPdbxDao;
import totalcross.util.Vector;

public class TipoPedidoService extends CrudService {

    private static TipoPedidoService instance;

    private TipoPedidoService() {
        //--
    }

    public static TipoPedidoService getInstance() {
        if (instance == null) {
            instance = new TipoPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoPedidoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public boolean isTipoPedidoExigeSenha(TipoPedido tipoPedido) throws SQLException {
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			return tipoPedido.isExigeSenha();
		}
		return false;
	}

    public void validateTrocaTipoPedido(Pedido pedido, TipoPedido tipoPedidoOld, TipoPedido tipoPedidoNew) throws SQLException {
    	validateTrocaTipoPedido(pedido, tipoPedidoOld, tipoPedidoNew, false);
    }

    public void validateTrocaTipoPedido(Pedido pedido, TipoPedido tipoPedidoOld, TipoPedido tipoPedidoNew, boolean ignoraValidacaoControleVerba) throws SQLException {
    	String cdTipoPedidoOld = pedido.oldCdTipoPedido;
    	try {
    		pedido.cdTipoPedido = tipoPedidoNew.cdTipoPedido;
    		//Tipo Pedido Oportunidade
    		if (LavenderePdaConfig.usaOportunidadeVenda && (ValueUtil.isNotEmpty(pedido.itemPedidoList) || ValueUtil.isNotEmpty(pedido.itemPedidoOportunidadeList))) {
				if (tipoPedidoOld.isOportunidade() && !tipoPedidoNew.isOportunidade()) {
					throw new ValidationException(Messages.OPORTUNIDADE_ERRO_TROCA_TIPOPEDIDO_DE_OPORTUNIDADE);
				} else if (!tipoPedidoOld.isOportunidade() && tipoPedidoNew.isOportunidade()) {
					throw new ValidationException(Messages.OPORTUNIDADE_ERRO_TROCA_TIPOPEDIDO_PARA_OPORTUNIDADE);
				}
    		}
			//Pedido de orçamento com itens que possuem estoque negativo sendo alterado para tipo de pedido de venda
			if (tipoPedidoOld != null && tipoPedidoOld.isIgnoraControleEstoque() && !tipoPedidoNew.isIgnoraControleEstoque()) {
				Vector list = PedidoService.getInstance().getItensListSemEstoqueEstoqueInsuficiente(pedido);
				if (ValueUtil.isNotEmpty(list)) {
					throw new ItensDivergentesSemEstoqueException(list);
				}
			}
    		//Estoque
    		int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				if ((tipoPedidoOld == null || tipoPedidoOld.isIgnoraControleEstoque()) && !tipoPedidoNew.isIgnoraControleEstoque()) {
					try {
						if (!ProdutoService.getInstance().getProduto(itemPedido.cdProduto).isPermiteEstoqueNegativo()) {
							validaEstoque(pedido, itemPedido);
						}
					} catch (EstoqueException e) {
						throw new ValidationException(Messages.TROCA_TIPO_PEDIDO_ERRO_ESTOQUE);
					}
				}
			}
			//Limite de Crédito
			if ((tipoPedidoOld == null || tipoPedidoOld.isIgnoraLimiteCreditoCliente()) && !tipoPedidoNew.isIgnoraLimiteCreditoCliente()) {
				FichaFinanceiraService.getInstance().validateLimCred(pedido, null);
			}
			//Saldo de Verba
			if ((ValueUtil.isNotEmpty(pedido.itemPedidoList) || ValueUtil.isNotEmpty(pedido.itemPedidoOportunidadeList)) && !ignoraValidacaoControleVerba) {
				if ((tipoPedidoNew.isIgnoraControleVerba() && (tipoPedidoOld != null && !tipoPedidoOld.isIgnoraControleVerba())) || (!tipoPedidoNew.isIgnoraControleVerba() && (tipoPedidoOld != null && tipoPedidoOld.isIgnoraControleVerba()))) {
					throw new ValidationException(Messages.IGNORA_VERBA_ERRO_TROCA_TIPO_PEDIDO);
				}
			}
			if ((tipoPedidoOld == null || tipoPedidoOld.isSimulaControleVerba()) && !tipoPedidoNew.isSimulaControleVerba()) {
				VerbaService.getInstance().validateSaldo(pedido, 0d);
			}
			//Restricao Venda Un
			if (LavenderePdaConfig.usaRestricaoVendaProdutoPorUnidade && LavenderePdaConfig.usaUnidadeAlternativa) {
				RestricaoVendaUnService.getInstance().validateUnidadeRestritaItensDoPedido(pedido);
			}
			//Produtos por tipo de pedido
			if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
				ProdutoTipoPedService.getInstance().validateProdutoTipoPedidoChange(pedido);
			}
			//Item Combo tipo 3
			if (LavenderePdaConfig.isExibeComboMenuInferior() && tipoPedidoNew.isBonificacao() && ComboService.getInstance().isPedidoComItemCombo(pedido.itemPedidoList)) {
				throw new ValidationException(Messages.MSG_ERRO_COMBO_TIPO_PEDIDO_BONIFICACAO);
			}
			if (pedido.itemPedidoList.size() > 0) {
				if (tipoPedidoOld != null && tipoPedidoOld.isBonificacao()) {
					tipoPedidoOld.flIgnoraControleVerba = ValueUtil.isEmpty(tipoPedidoOld.flIgnoraControleVerba) ? ValueUtil.VALOR_NAO : tipoPedidoOld.flIgnoraControleVerba;
					tipoPedidoOld.flIgnoraControleEstoque = ValueUtil.isEmpty(tipoPedidoOld.flIgnoraControleEstoque) ? ValueUtil.VALOR_NAO : tipoPedidoOld.flIgnoraControleEstoque;
					tipoPedidoOld.flIgnoraLimiteCreditoCliente = ValueUtil.isEmpty(tipoPedidoOld.flIgnoraLimiteCreditoCliente) ? ValueUtil.VALOR_NAO : tipoPedidoOld.flIgnoraLimiteCreditoCliente;
					tipoPedidoNew.flIgnoraControleVerba = ValueUtil.isEmpty(tipoPedidoNew.flIgnoraControleVerba) ? ValueUtil.VALOR_NAO : tipoPedidoNew.flIgnoraControleVerba;
					tipoPedidoNew.flIgnoraControleEstoque = ValueUtil.isEmpty(tipoPedidoNew.flIgnoraControleEstoque) ? ValueUtil.VALOR_NAO : tipoPedidoNew.flIgnoraControleEstoque;
					tipoPedidoNew.flIgnoraLimiteCreditoCliente = ValueUtil.isEmpty(tipoPedidoNew.flIgnoraLimiteCreditoCliente) ? ValueUtil.VALOR_NAO : tipoPedidoNew.flIgnoraLimiteCreditoCliente;
					if (!tipoPedidoNew.isBonificacao() || !tipoPedidoOld.flIgnoraControleVerba.equals(tipoPedidoNew.flIgnoraControleVerba) || !tipoPedidoOld.flIgnoraControleEstoque.equals(tipoPedidoNew.flIgnoraControleEstoque) || !tipoPedidoOld.flIgnoraLimiteCreditoCliente.equals(tipoPedidoNew.flIgnoraLimiteCreditoCliente)) {
						throw new ValidationException(Messages.BONIFICACAO_EQUIVALENTE_ERRO_CONTROLE_TROCA_TIPO_PEDIDO);
					}
				} else if (tipoPedidoNew.isBonificacao()) {
					throw new ValidationException(Messages.BONIFICACAO_EQUIVALENTE_ERRO_TROCA_TIPO_PEDIDO);
				}
			}
    	} finally {
    		pedido.cdTipoPedido = cdTipoPedidoOld;
    	}
    }

	private void validaEstoque(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		EstoqueService.getInstance().validateEstoque(pedido, itemPedido, false);
		LoteProdutoService.getInstance().validateEstoque(itemPedido, false);
	}


    public TipoPedido getFirstTipoPedido() throws SQLException {
    	TipoPedido tipoPedido = new TipoPedido();
    	tipoPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoPedido.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedido.class);
    	Vector tipoPedidoList = findAllByExample(tipoPedido);
    	int size = tipoPedidoList.size();
    	if (size > 0) {
    		return (TipoPedido) tipoPedidoList.items[0];
    	}
    	return null;
    }

    public TipoPedido getTipoPedido(String cdTipoPedido) throws SQLException {
    	if (ValueUtil.isEmpty(cdTipoPedido)) return null;
		TipoPedido tipoPedidoFilter = new TipoPedido();
		tipoPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoPedidoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedido.class);
		tipoPedidoFilter.cdTipoPedido = cdTipoPedido;
		return (TipoPedido) findByRowKey(tipoPedidoFilter.getRowKey());
	}

    public TipoPedido getTipoPedidoDefault() throws SQLException {
    	TipoPedido tipoPedido = new TipoPedido();
    	tipoPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoPedido.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedido.class);
    	tipoPedido.flDefault = ValueUtil.VALOR_SIM;
    	Vector tipoPedidoList = findAllByExample(tipoPedido);
    	int size = tipoPedidoList.size();
    	if (size > 0) {
    		return (TipoPedido) tipoPedidoList.items[0];
    	}
    	return null;
    }

	public TipoPedido findTipoPedidoDefault(Cliente cliente) throws SQLException {
		//Cliente
		TipoPedido tipoPedido = getTipoPedido(cliente.cdTipoPedido);
		if (tipoPedido != null) {
			return tipoPedido;
		} else {
			//Default
			tipoPedido = getTipoPedidoDefault();
			if (tipoPedido != null) {
				return tipoPedido;
			} else {
				//Primeiro da lista
				tipoPedido = getFirstTipoPedido();
				if (tipoPedido != null) {
					return tipoPedido;
				}
			}
		}
		return null;
	}

	public boolean isTipoPedidoPermitePositivacaoVisita(String cdTipoPedido) throws SQLException {
		TipoPedido tipoPedido = getTipoPedido(cdTipoPedido);
		return tipoPedido != null && tipoPedido.isPermitePositivacaoVisita();
	}

	public boolean isTipoPedidoObrigaQtdProdutos(String cdTipoPedido) throws SQLException {
		TipoPedido tipoPedido = getTipoPedido(cdTipoPedido);
		return tipoPedido != null && tipoPedido.isObrigaQtdProdutos();
	}

    public boolean isPossuiTipoPedidoComFlIgnoraControleVerba() throws SQLException {
    	TipoPedido tipoPedidoExample = new TipoPedido();
    	tipoPedidoExample.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoPedidoExample.flIgnoraControleVerba = ValueUtil.VALOR_SIM;
    	return countByExample(tipoPedidoExample) > 0;
    }

	public TipoPedido findTipoPedidoOportunidade(String cdEmpresa, String cdRepresentante) throws SQLException {
		TipoPedido tipoPedidoFilter = new TipoPedido();
		tipoPedidoFilter.cdEmpresa = cdEmpresa;
		tipoPedidoFilter.cdRepresentante = cdRepresentante;
		tipoPedidoFilter.flOportunidade = ValueUtil.VALOR_SIM;
		Vector tipoPedidoList = findAllByExample(tipoPedidoFilter);
		int size = tipoPedidoList.size();
		for (int i = 0; i < size; i++) {
			TipoPedido tipoPedido = (TipoPedido) tipoPedidoList.items[i];
			if (tipoPedido.isDefault()) {
				return tipoPedido;
			}
		}
		if (ValueUtil.isNotEmpty(tipoPedidoList)) {
			return (TipoPedido) tipoPedidoList.items[0];
		}
		return null;
	}

	public Vector getTipoPedidoList(TipoPedido tipoPedido) throws SQLException {
		if (LavenderePdaConfig.usaTipoPedidoPorCliente && SessionLavenderePda.getCliente() != null) {
			tipoPedido.tipoPedidoCliFilter = new TipoPedidoCli();
			tipoPedido.tipoPedidoCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			tipoPedido.tipoPedidoCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedidoCli.class);
			tipoPedido.tipoPedidoCliFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		return findAllByExample(tipoPedido);
	}

	public boolean validaDescontoItem(TipoPedido tipoPedido) throws SQLException {
		if ((LavenderePdaConfig.tipoPedidoOcultoNoPedido || !LavenderePdaConfig.isUsaDescontoMaximoPorTipoPedido()) && tipoPedido == null) {
			return true;
		}
		return tipoPedido != null && !(ValueUtil.getBooleanValue(tipoPedido.flIgnoraDescontoItem) && isTipoPedidoExigeSenha(tipoPedido));
	}

	public boolean validaQuantidadeItem(TipoPedido tipoPedido) throws SQLException {
		return tipoPedido != null && !(ValueUtil.getBooleanValue(tipoPedido.flIgnoraQuantidadeItem) && isTipoPedidoExigeSenha(tipoPedido));
	}

	public boolean possuiMaisDeUmTipoBonificadoNaCarga(Pedido pedido) throws SQLException {
		if (pedido == null) return false;
		TipoPedido filter = new TipoPedido();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedido.class);
		int result = TipoPedidoPdbxDao.getInstance().countTipoPedidosBonificacaoCarga(filter);
		return result > 1;
	}
	
	public Vector findAllDistinctTipoPedido(String cdEmpresa, String cdRepresentante) throws SQLException {
		TipoPedido tipoPedidoFilter = new TipoPedido();
		tipoPedidoFilter.cdEmpresa = cdEmpresa;
		tipoPedidoFilter.cdRepresentante = cdRepresentante;
		return TipoPedidoPdbxDao.getInstance().findAllDistinctTipoPedido(tipoPedidoFilter);
	}
	
	public TipoPedido findTipoPedidoBonifContaCorrente(String cdEmpresa, String cdRepresentante) throws SQLException {
		TipoPedido filter = new TipoPedido();
		filter.cdEmpresa = cdEmpresa;
		filter.cdRepresentante = cdRepresentante;
		filter.flBonificacaoContaCorrente = ValueUtil.VALOR_SIM;
		filter.limit = 1;
		Vector list = findAllByExample(filter);
		if (ValueUtil.isNotEmpty(list)) {
			return (TipoPedido) list.items[0];
		}
		return null;
	}
	
	public boolean isUsaDescontoMaximoPorTipoPedido(final Pedido pedido) throws SQLException {
		return isPedidoComTipoPedidoValido(pedido) && LavenderePdaConfig.isUsaDescontoMaximoPorTipoPedido() && pedido.getTipoPedido().isVlPctMaxDescontoValido() && !LavenderePdaConfig.isUsaDescontoMaximoEmValor() && !LavenderePdaConfig.validaPctMaxDescAcrescPorUsuario;
	}
	
	public boolean isUsaAcrescimoMaximoPorTipoPedido(final Pedido pedido) throws SQLException {
		return isPedidoComTipoPedidoValido(pedido) && LavenderePdaConfig.isUsaAcrescimoMaximoPorTipoPedido() && pedido.getTipoPedido().isVlPctMaxAcrescimoValido() && !LavenderePdaConfig.isUsaAcrescimoMaximoEmValor() && !LavenderePdaConfig.validaPctMaxDescAcrescPorUsuario;
	}
	
	private boolean isPedidoComTipoPedidoValido(final Pedido pedido) throws SQLException {
		return !LavenderePdaConfig.tipoPedidoOcultoNoPedido && pedido != null && pedido.getTipoPedido() != null;
	}
	
}
