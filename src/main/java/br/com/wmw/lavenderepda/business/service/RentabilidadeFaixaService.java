package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeFaixa;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RentabilidadeFaixaDbxDao;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class RentabilidadeFaixaService extends CrudService {

	private RentabilidadeFaixaService() {}

	private static final String IMAGES_RENTABILIDADE_PNG = "images/rentabilidade.png";
	private static RentabilidadeFaixaService instance;

    public static RentabilidadeFaixaService getInstance() { return instance == null ? instance = new RentabilidadeFaixaService() : instance; }
    @Override protected CrudDao getCrudDao() { return RentabilidadeFaixaDbxDao.getInstance(); }
    @Override public void validate(BaseDomain domain) throws java.sql.SQLException {}
    
    public RentabilidadeFaixa getRentabilidadeFaixaItemPedido(final ItemPedido itemPedido) throws SQLException {
    	if (itemPedido == null || ValueUtil.isEmpty(itemPedido.pedido.getRentabilidadeFaixaList())) return null;
        Vector rentabilidadeFaixaList = itemPedido.pedido.rentabilidadeFaixaList;
        calculaRentabilidadeItemPedidoRetornado(itemPedido);
	    int size = rentabilidadeFaixaList.size();
	    for (int i = 0; i < size; i++) {
			RentabilidadeFaixa faixaAtual = (RentabilidadeFaixa)rentabilidadeFaixaList.items[i];
		    double vlPctRentabilidade = itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false);
		    if (size - 1 == i) return faixaAtual;

			RentabilidadeFaixa proximaFaixa = (RentabilidadeFaixa)rentabilidadeFaixaList.items[1 + i];
            if (isMantemFaixaRentabilidadeAtual(faixaAtual, proximaFaixa, vlPctRentabilidade)) return faixaAtual;
			if (isProximaFaixaRentabilidadeAtingida(faixaAtual, proximaFaixa, vlPctRentabilidade)) return proximaFaixa;
		}
    	return null;
    }
    
    public void calculaRentabilidadeItemPedidoRetornado(ItemPedido itemPedido) throws SQLException {
    	if (itemPedido == null || itemPedido.pedido == null || itemPedido.getProduto().isNeutro()) return;
    	if (LavenderePdaConfig.calculaRentabilidadePedidoRetornado && itemPedido.pedido.isFlOrigemPedidoErp() && itemPedido.vlRentabilidade == 0) {
    		ItemPedidoService.getInstance().calculaRentabilidadeItem(itemPedido, itemPedido.pedido);
    	}
	}

	public RentabilidadeFaixa getRentabilidadeFaixaPedido(final Pedido pedido) throws SQLException {
		if (pedido == null || ValueUtil.isEmpty(pedido.getRentabilidadeFaixaList())) return null;
        Vector rentabilidadeFaixaList = pedido.rentabilidadeFaixaList;
        calculaRentabilidadePedidoRetornado(pedido);
        double vlPctRentabilidade = pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false);
		int size = rentabilidadeFaixaList.size();
		for (int i = 0; i < size; i++) {
			RentabilidadeFaixa faixaAtual = (RentabilidadeFaixa)rentabilidadeFaixaList.items[i];
			if (size - 1 == i) return faixaAtual;

			RentabilidadeFaixa proximaFaixa = (RentabilidadeFaixa)rentabilidadeFaixaList.items[1 + i];
            if (isMantemFaixaRentabilidadeAtual(faixaAtual, proximaFaixa, vlPctRentabilidade)) return faixaAtual;
            if (isProximaFaixaRentabilidadeAtingida(faixaAtual, proximaFaixa, vlPctRentabilidade)) return faixaAtual;
		}
        return null;
    }

	private boolean isMantemFaixaRentabilidadeAtual(RentabilidadeFaixa faixaAtual, RentabilidadeFaixa proximaFaixa, double vlPctRentabilidade) {
		return vlPctRentabilidade >= faixaAtual.vlPctRentabilidade
				&& faixaAtual.vlPctRentabilidade >= proximaFaixa.vlPctRentabilidade;
	}

	private boolean isProximaFaixaRentabilidadeAtingida(RentabilidadeFaixa faixaAtual, RentabilidadeFaixa proximaFaixa, double vlPctRentabilidade) {
		return vlPctRentabilidade < 0
				&& vlPctRentabilidade <= faixaAtual.vlPctRentabilidade
				&& faixaAtual.vlPctRentabilidade >= proximaFaixa.vlPctRentabilidade
				&& vlPctRentabilidade >= proximaFaixa.vlPctRentabilidade;
	}

	private void calculaRentabilidadePedidoRetornado(Pedido pedido) throws SQLException {
    	if (!LavenderePdaConfig.calculaRentabilidadePedidoRetornado || !pedido.isFlOrigemPedidoErp() || !isCalculaRentPedidoRetornado(pedido)) return;
        int size = pedido.itemPedidoList.size();
        for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.getProduto().isNeutro()) continue;
			ItemPedidoService.getInstance().calculaRentabilidadeItem(itemPedido, pedido);
		}
        PedidoService.getInstance().calculateRentabilidadePedido(pedido);
	}

	private boolean isCalculaRentPedidoRetornado(Pedido pedido) {
		if (pedido.vlRentabilidade == 0) return true;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.vlRentabilidade == 0) return true;
		}
		return false;
	}

	public Image getIconRentabilidadeItem(final ItemPedido itemPedido) throws SQLException {
		RentabilidadeFaixa rentabilidadeFaixaItem = getRentabilidadeFaixaItemPedido(itemPedido);
		if (rentabilidadeFaixaItem != null) {
			int corFaixa = getCorRentabilidadeFaixa(rentabilidadeFaixaItem);
			return UiUtil.getIconButtonAction(IMAGES_RENTABILIDADE_PNG, corFaixa, true);
		}
		return UiUtil.getIconButtonAction(IMAGES_RENTABILIDADE_PNG, Color.BRIGHT);
	}
    
    public Image getIconRentabilidadePedido(final Pedido pedido) throws SQLException {
		RentabilidadeFaixa rentabilidadeFaixaPedido = getRentabilidadeFaixaPedido(pedido);
		if (rentabilidadeFaixaPedido != null) {
			int corFaixa = getCorRentabilidadeFaixa(rentabilidadeFaixaPedido);
			return UiUtil.getColorfulImage(IMAGES_RENTABILIDADE_PNG, UiUtil.getLabelPreferredHeight() / 7 * 6, UiUtil.getLabelPreferredHeight() / 7 * 6, corFaixa);
		}
		return UiUtil.getColorfulImage(IMAGES_RENTABILIDADE_PNG, UiUtil.getLabelPreferredHeight() / 7 * 6, UiUtil.getLabelPreferredHeight() / 7 * 6, Color.BRIGHT);
    }
    
    public int getCorRentabilidadeFaixa(final RentabilidadeFaixa rentabilidadeFaixa) {
    	if (rentabilidadeFaixa == null) return 0;
        return Color.getRGB(rentabilidadeFaixa.vlR, rentabilidadeFaixa.vlG, rentabilidadeFaixa.vlB);
    }
    
    public double getVlPctRentabilidadeFaixaMinima(Vector rentabilidadeFaixaList) {
    	if (ValueUtil.isEmpty(rentabilidadeFaixaList)) return 0;
        for (int i = 0; i < rentabilidadeFaixaList.size(); i++) {
			RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) rentabilidadeFaixaList.items[i];
			if (rentabilidadeFaixa.isFaixaMinima()) return rentabilidadeFaixa.vlPctRentabilidade;
		}
        return 0;
    }
    
    public double getVlPctRentabilidadeFaixaIdeal(Vector rentabilidadeFaixaList) {
    	if (ValueUtil.isEmpty(rentabilidadeFaixaList)) return 0;
        for (int i = 0; i < rentabilidadeFaixaList.size(); i++) {
			RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) rentabilidadeFaixaList.items[i];
			if (rentabilidadeFaixa.isFaixaIdeal()) return rentabilidadeFaixa.vlPctRentabilidade;
		}
    	return 0;
    }
    
    public Vector getRentabilidadeFaixaList() throws SQLException {
    	RentabilidadeFaixa rentabilidadeFaixaFilter = new RentabilidadeFaixa();
		rentabilidadeFaixaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		rentabilidadeFaixaFilter.sortAtributte = RentabilidadeFaixa.NOMECOLUNA_VLPCTRENTABILIDADE;
		rentabilidadeFaixaFilter.sortAsc = ValueUtil.VALOR_NAO;
		return findAllByExample(rentabilidadeFaixaFilter);
    }

}