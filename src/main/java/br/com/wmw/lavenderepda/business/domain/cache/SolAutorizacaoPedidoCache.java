package br.com.wmw.lavenderepda.business.domain.cache;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SolAutorizacaoPedidoCache {

    public static final String TIPO_AUTORIZACAO_NULO = "NULL";

    private Map<String, Boolean> hasSolAutorizacaoPendenteOuNaoAutorizadaByPedido = new HashMap<>();
    private Map<String, Boolean> hasSolAutorizacaoPendenteOuAutorizadaByPedido = new HashMap<>();
    private Map<String, Boolean> hasSolAutorizacaoPendentePedido = new HashMap<>();
    private Map<String, Boolean> hasSolAutorizacaoNaoAutorizadaPedido = new HashMap<>();
    private Map<String, Boolean> hasSolAutorizacaoPedido = new HashMap<>();

    public void clearCaches(Pedido pedido) {
        hasSolAutorizacaoPendenteOuNaoAutorizadaByPedido = new HashMap<>();
        hasSolAutorizacaoPendenteOuAutorizadaByPedido = new HashMap<>();
        hasSolAutorizacaoPendentePedido = new HashMap<>();
        hasSolAutorizacaoNaoAutorizadaPedido = new HashMap<>();
        hasSolAutorizacaoPedido = new HashMap<>();
        if (pedido != null && ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
            int size = pedido.itemPedidoList.size();
            for (int i = 0; i < size; i++) {
                ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
                itemPedido.solAutorizacaoItemPedidoCache.clearCaches();
            }
        }
    }

    private Boolean getMapValue(Map<String, Boolean> map, String cacheKey) {
        if (!LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) return false;
        if (map == null) map = new HashMap<>();
        return map.get(cacheKey);
    }

    private String getCacheKey(String identify, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) {
        return identify + "-" + (tipoSolicitacaoAutorizacaoEnum != null ? tipoSolicitacaoAutorizacaoEnum.name() : TIPO_AUTORIZACAO_NULO);
    }

    public Boolean getHasSolAutorizacaoPendenteOuNaoAutorizadaByPedido(Pedido pedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (pedido == null || ValueUtil.isEmpty(pedido.nuPedido)) return false;
        String cacheKey = getCacheKey(pedido.nuPedido, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(hasSolAutorizacaoPendenteOuNaoAutorizadaByPedido, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().hasSolAutorizacaoPendenteOuNaoAutorizadaByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
        hasSolAutorizacaoPendenteOuNaoAutorizadaByPedido.put(cacheKey, valor);
        return valor;
    }

    public Boolean getHasSolAutorizacaoPendenteOuAutorizadaByPedido(Pedido pedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (pedido == null || ValueUtil.isEmpty(pedido.nuPedido)) return false;
        String cacheKey = getCacheKey(pedido.nuPedido, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(hasSolAutorizacaoPendenteOuAutorizadaByPedido, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().hasSolAutorizacaoPendenteOuAutorizadaByPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
        hasSolAutorizacaoPendenteOuAutorizadaByPedido.put(cacheKey, valor);
        return valor;
    }

    public Boolean getHasSolAutorizacaoPendentePedido(Pedido pedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (pedido == null || ValueUtil.isEmpty(pedido.nuPedido)) return false;
        String cacheKey = getCacheKey(pedido.nuPedido, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(hasSolAutorizacaoPendentePedido, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().hasSolAutorizacaoPendentePedido(pedido, tipoSolicitacaoAutorizacaoEnum);
        hasSolAutorizacaoPendentePedido.put(cacheKey, valor);
        return valor;
    }

    public Boolean getHasSolAutorizacaoNaoAutorizadaPedido(Pedido pedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (pedido == null || ValueUtil.isEmpty(pedido.nuPedido)) return false;
        String cacheKey = getCacheKey(pedido.nuPedido, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(hasSolAutorizacaoNaoAutorizadaPedido, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().hasSolAutorizacaoNaoAutorizadaPedido(pedido, tipoSolicitacaoAutorizacaoEnum);
        hasSolAutorizacaoNaoAutorizadaPedido.put(cacheKey, valor);
        return valor;
    }

    public Boolean getHasSolAutorizacaoPedido(Pedido pedido, boolean ignoreRemovidos) throws SQLException {
        if (pedido == null || ValueUtil.isEmpty(pedido.nuPedido)) return false;
        String cacheKey = pedido.nuPedido + "-" + StringUtil.getStringValue(ignoreRemovidos);
        Boolean valor = getMapValue(hasSolAutorizacaoPedido, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().hasSolAutorizacaoPedido(pedido, ignoreRemovidos);
        hasSolAutorizacaoPedido.put(cacheKey, valor);
        return valor;
    }

    public void reloadCaches(Pedido pedido) throws SQLException {
        clearCaches(pedido);
        getHasSolAutorizacaoNaoAutorizadaPedido(pedido, null);
        getHasSolAutorizacaoPendentePedido(pedido, null);
    }

}
