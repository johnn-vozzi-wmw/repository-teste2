package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TipoPedProdDescAcres;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.validation.DescAcresMaximoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPedProdDescAcresPdbxDao;

import java.sql.SQLException;

public class TipoPedProdDescAcresService extends CrudService {

	private static TipoPedProdDescAcresService instance;

	public static TipoPedProdDescAcresService getInstance() {
		if (instance == null) {
			instance = new TipoPedProdDescAcresService();
		}
		return instance;
	}


	public TipoPedProdDescAcres findTipoPedProdDescAcresFilter(final ItemPedido itemPedido) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcresFilter = new TipoPedProdDescAcres();
		tipoPedProdDescAcresFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoPedProdDescAcresFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TipoPedProdDescAcres.class);
		tipoPedProdDescAcresFilter.cdTipoPedido = itemPedido.pedido.getTipoPedido() != null ? itemPedido.pedido.getTipoPedido().cdTipoPedido : "";
		tipoPedProdDescAcresFilter.cdProduto = itemPedido.cdProduto;
		TipoPedProdDescAcres tipoPedProdDescAcres = (TipoPedProdDescAcres) TipoPedProdDescAcresPdbxDao.getInstance().findByRowKey(tipoPedProdDescAcresFilter.getRowKey());
		if (tipoPedProdDescAcres == null) return tipoPedProdDescAcresFilter;
		return tipoPedProdDescAcres;
	}

	public double getPctDescontoPorTipoPedidoEProduto(final ItemPedido itemPedido) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcresFilter = findTipoPedProdDescAcresFilter(itemPedido);
		if (ValueUtil.isNotEmpty(tipoPedProdDescAcresFilter.vlPctMaxDesconto) && tipoPedProdDescAcresFilter.isVlPctMaxDescontoValido())
			return tipoPedProdDescAcresFilter.vlPctMaxDesconto;
		return itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
	}

	public double getPctAcrescimoPorTipoPedidoEProduto(final ItemPedido itemPedido) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcresFilter = findTipoPedProdDescAcresFilter(itemPedido);
		boolean vlPctMaxAcrescimoValido = ValueUtil.isNotEmpty(tipoPedProdDescAcresFilter.vlPctMaxAcrescimo) && tipoPedProdDescAcresFilter.isVlPctMaxAcrescimoValido();
		return vlPctMaxAcrescimoValido ? tipoPedProdDescAcresFilter.vlPctMaxAcrescimo : itemPedido.getItemTabelaPreco().vlPctMaxAcrescimo;
	}

	public double getVlBaseProduto(final ItemPedido itemPedido) throws SQLException {
		TipoPedProdDescAcres tipoPedProdDescAcresFilter = findTipoPedProdDescAcresFilter(itemPedido);
		if (ValueUtil.isNotEmpty(tipoPedProdDescAcresFilter.vlBase)) return tipoPedProdDescAcresFilter.vlBase;
		return itemPedido.getItemTabelaPreco().vlBase;
	}

	public void validadePctDescMaxTipoPedido(final ItemPedido itemPedido, double vlPctMaxDesconto) {
		if (itemPedido.vlPctDesconto > vlPctMaxDesconto) {
			double vlPermitido = itemPedido.vlBaseItemPedido * (1 - vlPctMaxDesconto / 100);
			throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_ULTRAPASSADO, new String[]{StringUtil.getStringValueToInterface(vlPctMaxDesconto)}), vlPermitido);
		}
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	@Override
	protected CrudDao getCrudDao() {
		return null;
	}
}
