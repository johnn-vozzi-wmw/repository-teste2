package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescontoPacote;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.validation.DescAcresMaximoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescontoPacotePdbxDao;
import totalcross.util.Vector;

public class DescontoPacoteService extends CrudPersonLavendereService {

    private static DescontoPacoteService instance;

    public static DescontoPacoteService getInstance() {
        return instance == null ? instance = new DescontoPacoteService() : instance;
    }

    protected CrudDao getCrudDao() { return DescontoPacotePdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}
    
	public boolean hasDescPacoteInItemPedido(final ItemPedido itemPedido, final String cdPacote, final String cdTabelaPreco) {
		try {
			if (itemPedido == null) return false;
			DescontoPacote descPacoteFilter = getDescontoPacoteFilterByPacote(itemPedido, cdPacote, cdTabelaPreco);
			return countByExample(descPacoteFilter) > 0;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}

	private DescontoPacote getDescontoPacoteFilterByPacote(final ItemPedido itemPedido, final String cdPacote, final String cdTabelaPreco) {
		DescontoPacote descPacoteFilter = getDescPacoteFilter(itemPedido, cdTabelaPreco);
		descPacoteFilter.cdPacote = cdPacote;
		return descPacoteFilter;
	}
	
	public Vector findAllByExampleByItemPedido(ItemPedido itemPedido, String cdPacote) throws SQLException {
		DescontoPacote descPacoteFilter = getDescPacoteFilter(itemPedido, itemPedido.getCdTabelaPreco());
		descPacoteFilter.cdPacote = cdPacote;
		return super.findAllByExample(descPacoteFilter);
	}
	
	public DescontoPacote getDescPacoteFilter(ItemPedido itemPedido, String cdTabelaPreco) {
		return new DescontoPacote(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdProduto, LavenderePdaConfig.usaTabPrecoDescQuantidadePorPacote ? cdTabelaPreco : DescontoPacote.CD_TABELA_PRECO_0);
	}
	
	public Vector verificaPctMaxDescontoPorPacote(final Pedido pedido, final boolean lanceException) throws SQLException {
		if (!LavenderePdaConfig.usaDescQuantidadePorPacote || !TipoPedidoService.getInstance().validaDescontoItem(pedido.getTipoPedido())) return new Vector(0);
		Vector itensNaoConforme = new Vector();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (ValueUtil.isEmpty(itemPedido.cdPacote) || !hasDescPacoteInItemPedido(itemPedido, itemPedido.cdPacote, itemPedido.cdTabelaPreco)) continue;
			double pctMaxDesconto = getPctMaxDescontoPorPacoteEQuantidade(pedido, itemPedido);
			if (ValueUtil.VALOR_SIM.equals(itemPedido.flPrecoLiberadoSenha) || ValueUtil.VALOR_SIM.equals(itemPedido.pedido.flPrecoLiberadoSenha) || ValueUtil.round(itemPedido.vlPctDesconto) <= ValueUtil.round(pctMaxDesconto)) continue;
			trataItensExtrapoladosDoPacote(pedido, lanceException, itensNaoConforme, itemPedido, pctMaxDesconto);
		}
		return itensNaoConforme;
	}

	private void trataItensExtrapoladosDoPacote(final Pedido pedido, final boolean lanceException, Vector itensNaoConforme, ItemPedido itemPedido, double pctMaxDesconto) throws SQLException {
		if (lanceException) {
			itensNaoConforme = null;
			StringBuffer strBuffer = new StringBuffer();
			throw new ValidationException(strBuffer.append(DescontoPacote.SIGLE_EXCEPTION).append(Messages.DESCONTOPACOTE_MSG_VALIDACAO).toString());
		}
		itemPedido.vlPctMaxDescPacote = pctMaxDesconto;
		itemPedido.qtItemMesmoPacote = getQtdItensAdicionadoPacote(itemPedido.cdPacote, pedido.itemPedidoList);
		itensNaoConforme.addElement(itemPedido);
	}
	

	public double getPctMaxDescontoPorPacoteEQuantidade(Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		Vector descontoPacoteList = findAllByExampleByItemPedido(itemPedido, itemPedido.cdPacote);
		descontoPacoteList.qsort();
		if (ValueUtil.isEmpty(descontoPacoteList)) return 0d;
		double qtItensPacote = getQtdItensAdicionadoPacote(itemPedido.cdPacote, pedido.itemPedidoList);
		DescontoPacote descontoPacote;
		double pctMaxDesconto = 0;
		int size = descontoPacoteList.size();
		for (int j = 0; j < size; j++) {
			descontoPacote = (DescontoPacote) descontoPacoteList.items[j];
			if (qtItensPacote < descontoPacote.qtItem) continue;
			pctMaxDesconto = descontoPacote.vlPctDesconto;
			break;
		}
		return pctMaxDesconto;
	}

	public double getQtdItensAdicionadoPacote(String cdPacote, Vector itemPedidoList) throws SQLException {
		int size = itemPedidoList.size();
		double qtdItensPacote = 0d;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPacote = (ItemPedido) itemPedidoList.items[i];
			if (!ValueUtil.valueEquals(cdPacote, itemPedidoPacote.cdPacote)) continue;
			qtdItensPacote += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedidoPacote, itemPedidoPacote.getQtItemFisico());
		}
		return qtdItensPacote;
	}
	
   public void validatePctMaxDescontoPacoteMaiorFaixaDisponivel(ItemPedido itemPedido) throws SQLException {
    	double vlPctMaxDesconto = ValueUtil.round(ValueUtil.getDoubleValue(maxByExample(getDescontoPacoteFilterByPacote(itemPedido, itemPedido.cdPacote, itemPedido.cdTabelaPreco), "vlPctDesconto")));
    	if (!ValueUtil.VALOR_SIM.equals(itemPedido.flPrecoLiberadoSenha) && !ValueUtil.VALOR_SIM.equals(itemPedido.pedido.flPrecoLiberadoSenha) && ValueUtil.round(itemPedido.vlPctDesconto) > vlPctMaxDesconto && !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata) {
    		double vlPermitido = itemPedido.vlBaseItemPedido * (1 - vlPctMaxDesconto / 100);
    		throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.DESCONTOPACOTE_MSG_ERROR_MAXDESC_FAIXA, new String[] { StringUtil.getStringValueToInterface(ValueUtil.round(itemPedido.vlPctDesconto)), StringUtil.getStringValueToInterface(vlPctMaxDesconto) }), vlPermitido);
    	}
    }
	
	
}
