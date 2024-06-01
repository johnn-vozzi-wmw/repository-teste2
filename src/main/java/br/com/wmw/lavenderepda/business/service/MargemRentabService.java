package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.MargemRentab;
import br.com.wmw.lavenderepda.business.domain.MargemRentabFaixa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.enums.RecalculoRentabilidadeOptions;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MargemRentabDao;

public class MargemRentabService extends CrudService {

    private static MargemRentabService instance = null;
    
    private Map<String, String> hashCacheByDomain = new HashMap<String, String>();
    
    private MargemRentabService() {
    }
    
    public static MargemRentabService getInstance() {
        if (instance == null) {
            instance = new MargemRentabService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MargemRentabDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	public String findCdMargemRent(ItemPedido itemPedido) throws SQLException {
		if (hashCacheByDomain.containsKey(itemPedido.getKeyItemPedidoMargemRentab())) return hashCacheByDomain.get(itemPedido.getKeyItemPedidoMargemRentab());
		MargemRentab margemRentabFilter = new MargemRentab(itemPedido);
		String cdMargem = MargemRentabDao.getInstance().findCdMargemRent(margemRentabFilter);
		hashCacheByDomain.put(itemPedido.getRowKey(), cdMargem);
		return cdMargem;
	}
	
	public String findCdMargemRent(Pedido pedido) throws SQLException {
		if (hashCacheByDomain.containsKey(pedido.getRowKey())) return hashCacheByDomain.get(pedido.getRowKey());
		MargemRentab margemRentabFilter = new MargemRentab(pedido);
		String cdMargem = MargemRentabDao.getInstance().findCdMargemRent(margemRentabFilter);
		hashCacheByDomain.put(pedido.getRowKey(), cdMargem);
		return cdMargem;
	}
	
	public boolean recalcularRentabilidadePedidoAbertoSeNecessario(final Pedido pedido, final RecalculoRentabilidadeOptions option) throws SQLException {
		if (!pedido.isPedidoAberto() || !pedido.isPermiteUtilizarRentabilidade()) {
			return false;
		}
		return recalcularRentabilidadePedidoSeNecessario(pedido, option, true);
	}
	
	private boolean recalcularRentabilidadePedidoSeNecessario(final Pedido pedido, final RecalculoRentabilidadeOptions option, boolean mustBeOpened) throws SQLException {
		if (isRecalculoRentabilidadeNeeded(option)) {
			recalcularRentabilidadePedido(pedido, mustBeOpened);
			return true;
		}
		return false;
	}
	
	public void recalcularRentabilidadePedido(final Pedido pedido, boolean mustBeOpened) throws SQLException {
		if ((pedido.isPedidoAberto() || !mustBeOpened) && pedido.isPermiteUtilizarRentabilidade()) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				if (DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalComRentabilidade(itemPedido)) continue;
				atualizarItemPedidoAposCalculoRentabilidade(itemPedido, pedido);
			}
			PedidoService.getInstance().update(pedido);
		}
	}
	
	public boolean isRecalculoRentabilidadeNeeded(final RecalculoRentabilidadeOptions option) {
		if (option != null) {
			List<String> options = LavenderePdaConfig.defineRecalculoRentabilidade();
			if (ValueUtil.isNotEmpty(options)) {
				return options.contains(option.getOption());
			}
		}
		return false;
	}
 
	private void atualizarItemPedidoAposCalculoRentabilidade(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		ItemPedidoService.getInstance().calculaRentabilidade(pedido, itemPedido, -1);
		ItemPedidoService.getInstance().update(itemPedido);
	}
	
	public static boolean isOcultaPercentualFaixaRentabilidadeSePositiva(final Pedido pedido) throws SQLException {
		return LavenderePdaConfig.isOcultaPercentualFaixaRentabilidadeSePositiva() && pedido.vlPctMargemRentab >= 0 && !isPedidoPendenteAprovacao(pedido);
	}
	
	public static boolean isPedidoPendenteAprovacao(final Pedido pedido) throws SQLException {
		boolean margemPendente = false;
		if (LavenderePdaConfig.isUsaMotivosPendenciaMargemOuRentabilidade()) {
			MargemRentabFaixa margemRentabFaixa = MargemRentabFaixaService.getInstance().findMargemRentabFaixa(pedido);
			margemPendente = margemRentabFaixa != null && margemRentabFaixa.cdMotivoPendencia != null;
		}
		return margemPendente;
	}
	
}