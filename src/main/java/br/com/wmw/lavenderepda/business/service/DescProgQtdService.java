package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescProgQtd;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.validation.DescItemMaiorDescProgressivoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgQtdDbxDao;
import totalcross.util.Vector;

public class DescProgQtdService extends CrudService {

    private static DescProgQtdService instance;

    private DescProgQtdService() {
        //--
    }

    public static DescProgQtdService getInstance() {
        if (instance == null) {
            instance = new DescProgQtdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescProgQtdDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public DescProgQtd getDescProgQtdPedido(double qtItens) throws SQLException {
    	DescProgQtd descProgQtd = new DescProgQtd();
		descProgQtd.cdEmpresa = SessionLavenderePda.cdEmpresa;
		descProgQtd.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		descProgQtd.cdUnidade = DescProgQtd.SEM_UNIDADE;
		if (qtItens == 0) {
			descProgQtd.qtUnidade = -1;
		} else {
			descProgQtd.qtUnidade = ValueUtil.getIntegerValue(qtItens);
		}
		descProgQtd.sortAsc = ValueUtil.VALOR_NAO;
		descProgQtd.sortAtributte = "qtUnidade";
		Vector vector = findAllByExample(descProgQtd);
		if (vector.size() > 0) {
			return (DescProgQtd)vector.items[0];
		} else {
			return null;
		}
    }

    public void validateDescontoMaiorDescontoProgressivo(final Pedido pedido) throws SQLException {
    	int size = pedido.itemPedidoList.size();
    	ItemPedido itemPedido;
    	double pctDescProgressivo = pedido.getVlPctDescProgressivo();
    	for (int i = 0; i < size; i++) {
    		itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
    		if (!itemPedido.isFlPrecoLiberadoSenha() && isDescontoItemMaiorDescProgressivo(itemPedido, pctDescProgressivo)) {
    			throw new DescItemMaiorDescProgressivoException(Messages.DESCONTO_PROGRESSIVO_MSG_DESC_EXTRAPOLADO);
    		}
		}
    }

    private boolean isDescontoItemMaiorDescProgressivo(final ItemPedido itemPedido, final double pctDescProgressivoPrevisto) {
    	if (itemPedido != null) {
    		double vlMinimoItemPedidoComDescontoProgressivo = itemPedido.vlBaseItemPedido - ((itemPedido.vlBaseItemPedido * pctDescProgressivoPrevisto) / 100);
    		if (LavenderePdaConfig.isBloqueiaDescontoMaiorDescontoProgressivoComValorTruncado()) {
    			vlMinimoItemPedidoComDescontoProgressivo = ValueUtil.getDoubleValueTruncated(vlMinimoItemPedidoComDescontoProgressivo, ValueUtil.doublePrecision);
			} else {
				vlMinimoItemPedidoComDescontoProgressivo = ValueUtil.round(vlMinimoItemPedidoComDescontoProgressivo);
			}
    		return itemPedido.vlItemPedido < vlMinimoItemPedidoComDescontoProgressivo;
    	}
    	return false;
    }

    public Vector getItensDescontoMaiorDescProgressivo(final Pedido pedido) throws SQLException {
    	Vector itensNaoConforme = new Vector();
    	if (pedido != null && pedido.itemPedidoList.size() > 0) {
    		int size = pedido.itemPedidoList.size();
    		ItemPedido itemPedido;
    		double pctDescProgressivo = pedido.getVlPctDescProgressivo();
    		for (int i = 0; i < size; i++) {
				itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (!itemPedido.isFlPrecoLiberadoSenha() && isDescontoItemMaiorDescProgressivo(itemPedido, pctDescProgressivo)) {
					itensNaoConforme.addElement(itemPedido);
					itemPedido.vlPctDescPrev = pctDescProgressivo;
				}
			}
    	}
    	return itensNaoConforme;
    }
    
    public double getVlPctDescProgressivoMix(String cdEmpresa, String cdRepresentante, int qtUnidade) throws SQLException {
    	if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
    		DescProgQtd descProgQtdFilter = new DescProgQtd();
    		descProgQtdFilter.cdEmpresa = cdEmpresa;
    		descProgQtdFilter.cdRepresentante = cdRepresentante;
    		descProgQtdFilter.cdUnidade = DescProgQtd.SEM_UNIDADE;
    		descProgQtdFilter.qtUnidade = qtUnidade;
    		int maxQtUnidade = (int) ValueUtil.getDoubleValue(maxByExample(descProgQtdFilter, DescProgQtd.NOME_COLUNA_QTUNIDADE));
    		descProgQtdFilter.qtUnidade = maxQtUnidade;
	    	return ValueUtil.getDoubleValue(findColumnByRowKey(descProgQtdFilter.getRowKey(), DescProgQtd.NOME_COLUNA_VLPCTDESCONTO));
    	}
    	return 0;
    }
    
    protected void aplicaDescontoProgressivoPorMixDeProdutos(Vector itemPedidoList, double vlPctDescProgressivoMix) throws SQLException {
    	if (LavenderePdaConfig.aplicaDescProgressivoPorMixPorItemFinalPedido) {
    		for (int i = 0; i < itemPedidoList.size(); i++) {
    			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
    			if (LavenderePdaConfig.ignoraDescontroProgressivoProdutoPromocional && ItemPedidoService.getInstance().isItemPedidoPromocional(itemPedido)) {
    				continue;
    			}
    			double vlBase = LavenderePdaConfig.aplicaDescontoProgressivoPorItemFinalPedido ? itemPedido.vlItemPedido : itemPedido.vlBaseItemPedido;
    			if (LavenderePdaConfig.aplicaDescontoProgressivoPorItemFinalPedido) {
    				vlPctDescProgressivoMix = !itemPedido.descontoProgressivoAplicado && (itemPedido.vlPctDesconto > 0 || itemPedido.vlPctAcrescimo > 0) ? 0 : vlPctDescProgressivoMix;  
    			} else {
    				vlPctDescProgressivoMix = itemPedido.vlPctAcrescimo == 0 && itemPedido.vlPctDesconto == 0 ? vlPctDescProgressivoMix : 0;
    			}
    			itemPedido.vlItemPedido = ValueUtil.round(ItemPedidoService.getInstance().getVlItemPedidoComDescontoProgressivoMix(itemPedido, vlBase, vlPctDescProgressivoMix));
    			double vlPctMaxDesconto = itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
			    if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
				    vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
			    } else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
				    vlPctMaxDesconto = itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
    			}
    			itemPedido.vlPctDescProgressivoMix = (vlPctDescProgressivoMix > vlPctMaxDesconto) ? vlPctMaxDesconto : vlPctDescProgressivoMix;
    			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
			}
    	}
    }
}
