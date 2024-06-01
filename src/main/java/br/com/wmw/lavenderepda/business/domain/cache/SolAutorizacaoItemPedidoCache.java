package br.com.wmw.lavenderepda.business.domain.cache;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SolAutorizacaoItemPedidoCache {

    public static final String TIPO_AUTORIZACAO_NULO = "NULL";

    private Map<String, Boolean> hasSolAutorizacaoItemPedido = new HashMap<>();
    private Map<String, Boolean> isItemPedidoNaoAutorizadoOuPendente = new HashMap<>();
    private Map<String, Boolean> isItemPedidoNaoAutorizado = new HashMap<>();
    private Map<String, Boolean> isItemPedidoAutorizado = new HashMap<>();
    private Map<String, Boolean> isItemPedidoAutorizadoOuPendente = new HashMap<>();
    private Map<String, Boolean> hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido = new HashMap<>();
    private Map<String, Boolean> isItemPedidoPendente = new HashMap<>();
    private Map<String, Boolean> isItemAutorizadoSimilar = new HashMap<>();

    public void clearCaches() {
        hasSolAutorizacaoItemPedido = new HashMap<>();
        isItemPedidoNaoAutorizadoOuPendente = new HashMap<>();
        isItemPedidoNaoAutorizado = new HashMap<>();
        isItemPedidoAutorizado = new HashMap<>();
        isItemPedidoAutorizadoOuPendente = new HashMap<>();
        hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido = new HashMap<>();
        isItemPedidoPendente = new HashMap<>();
        isItemAutorizadoSimilar = new HashMap<>();
    }

    private Boolean getMapValue(Map<String, Boolean> map, String cacheKey) {
        if (!LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) return false;
        if (map == null) map = new HashMap<>();
        return map.get(cacheKey);
    }

    private String getCacheKey(String identify, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) {
        return identify + "-" + (tipoSolicitacaoAutorizacaoEnum != null ? tipoSolicitacaoAutorizacaoEnum.name() : TIPO_AUTORIZACAO_NULO);
    }

    public Boolean getHasSolAutorizacaoItemPedido(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) return false;
        String cacheKey = getCacheKey(itemPedido.cdProduto, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(hasSolAutorizacaoItemPedido, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().hasSolAutorizacaoItemPedido(itemPedido, tipoSolicitacaoAutorizacaoEnum);
        hasSolAutorizacaoItemPedido.put(cacheKey, valor);
        return valor;
    }

    public Boolean getIsItemPedidoNaoAutorizadoOuPendente(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) return false;
        String cacheKey = getCacheKey(itemPedido.cdProduto, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(isItemPedidoNaoAutorizadoOuPendente, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().isItemPedidoNaoAutorizadoOuPendente(itemPedido, tipoSolicitacaoAutorizacaoEnum);
        isItemPedidoNaoAutorizadoOuPendente.put(cacheKey, valor);
        return valor;
    }

    public Boolean getIsItemPedidoNaoAutorizado(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) return false;
        String cacheKey = getCacheKey(itemPedido.cdProduto, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(isItemPedidoNaoAutorizado, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().isItemPedidoNaoAutorizado(itemPedido, tipoSolicitacaoAutorizacaoEnum);
        isItemPedidoNaoAutorizado.put(cacheKey, valor);
        return valor;
    }

    public Boolean getIsItemPedidoAutorizado(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) return false;
        String cacheKey = getCacheKey(itemPedido.cdProduto, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(isItemPedidoAutorizado, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().isItemPedidoAutorizado(itemPedido, tipoSolicitacaoAutorizacaoEnum);
        isItemPedidoAutorizado.put(cacheKey, valor);
        return valor;
    }

    public Boolean getIsItemPedidoAutorizadoOuPendente(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) return false;
        String cacheKey = getCacheKey(itemPedido.cdProduto, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(isItemPedidoAutorizadoOuPendente, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().isItemPedidoAutorizadoOuPendente(itemPedido, tipoSolicitacaoAutorizacaoEnum);
        isItemPedidoAutorizadoOuPendente.put(cacheKey, valor);
        return valor;
    }

    public Boolean getHasSolAutorizacaoPendenteOuAutorizadaSimilarPedido(Pedido pedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum, String cdAgrupadorSimilaridade) throws SQLException {
        if (pedido == null || ValueUtil.isEmpty(pedido.nuPedido) || ValueUtil.isEmpty(cdAgrupadorSimilaridade)) return false;
        String cacheKey = getCacheKey(cdAgrupadorSimilaridade, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido(pedido, tipoSolicitacaoAutorizacaoEnum, cdAgrupadorSimilaridade);
        hasSolAutorizacaoPendenteOuAutorizadaSimilarPedido.put(cacheKey, valor);
        return valor;
    }

    public Boolean getIsItemPedidoPendente(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) return false;
        String cacheKey = getCacheKey(itemPedido.cdProduto, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(isItemPedidoPendente, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().isItemPedidoPendente(itemPedido, tipoSolicitacaoAutorizacaoEnum);
        isItemPedidoPendente.put(cacheKey, valor);
        return valor;
    }

    public Boolean getIsItemAutorizadoSimilar(ItemPedido itemPedido, TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
        if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) return false;
        String cacheKey = getCacheKey(itemPedido.cdProduto, tipoSolicitacaoAutorizacaoEnum);
        Boolean valor = getMapValue(isItemAutorizadoSimilar, cacheKey);
        if (valor != null) return valor;
        valor = SolAutorizacaoService.getInstance().isItemAutorizadoSimilar(itemPedido, tipoSolicitacaoAutorizacaoEnum);
        isItemAutorizadoSimilar.put(cacheKey, valor);
        return valor;
    }

}
