package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Pontuacao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontuacaoPdbxDao;

public class PontuacaoService extends CrudService {

    private static PontuacaoService instance;

    private PontuacaoService() {}
    
    @Override
    protected CrudDao getCrudDao() { return PontuacaoPdbxDao.getInstance(); }
    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {}

    public static PontuacaoService getInstance() { 
    	return instance == null ? instance = new PontuacaoService() : instance; 
    }

    private Pontuacao getPontuacao(String cdEmpresa, String cdRepresentante, double vlPctLucroCalculado) throws SQLException {
    	return PontuacaoPdbxDao.getInstance().findPontuacaoByVlPctLucroCalculado(cdEmpresa, cdRepresentante, vlPctLucroCalculado);
    }

    public void aplicaPontosPedido(Pedido pedido) throws SQLException {
    	pedido.qtPontosPedido = 0;
    	if (ValueUtil.isEmpty(pedido.itemPedidoList)) return;
		double totVlCustoItens = 0;
		int size  = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			totVlCustoItens += (itemPedido.getVlPrecoCusto() * itemPedido.getQtItemFisico());
		}
		if (totVlCustoItens == 0) return;
		double vlPctLucroCalculado = ValueUtil.round((pedido.vlTotalPedido / totVlCustoItens) -1) * 100;
		Pontuacao pontuacao = getPontuacao(pedido.cdEmpresa, pedido.cdRepresentante, vlPctLucroCalculado);
		pedido.qtPontosPedido = pontuacao != null ? pontuacao.qtPontos : 0;
    }

    public void aplicaPontosItem(ItemPedido itemPedido) throws SQLException {
    	itemPedido.pontosMinimoItemNaoAlcancado = false;
    	itemPedido.isVlPrecoCustoNull = false;
    	if (itemPedido.getVlPrecoCusto() <= 0) {
    		itemPedido.isVlPrecoCustoNull = true;
    		LogPdaService.getInstance().warn(LogPda.LOG_CATEGORIA_PONTUACAO_PEDIDO, Messages.MSG_PRODUTO_NAO_POSSUI_VL_CUSTO);
    		return;
    	}
		double vlPctLucroCalculado = ValueUtil.round(((itemPedido.vlItemPedido / itemPedido.getVlPrecoCusto()) -1) * 100);
		Pontuacao pontuacao = getPontuacao(itemPedido.cdEmpresa, itemPedido.cdRepresentante, vlPctLucroCalculado);
		if (pontuacao == null) {
			itemPedido.pontosMinimoItemNaoAlcancado = true;
			LogPdaService.getInstance().warn(LogPda.LOG_CATEGORIA_PONTUACAO_PEDIDO, Messages.MSG_NAO_ALCANCOU_VL_MINS_PONTOS);
			itemPedido.qtPontosItem = 0;
			return;
		}
		itemPedido.qtPontosItem = pontuacao.qtPontos;
    }

}