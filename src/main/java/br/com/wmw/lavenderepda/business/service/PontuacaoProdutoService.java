package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PontuacaoConfig;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontuacaoProdutoPdbxDao;

public class PontuacaoProdutoService extends CrudPersonLavendereService {

    private static PontuacaoProdutoService instance;

    public static PontuacaoProdutoService getInstance() {
        return instance == null ? instance = new PontuacaoProdutoService() : instance;
    }

    protected CrudDao getCrudDao() { return PontuacaoProdutoPdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}

	public double getVlPesoPontuacaoItemPedido(final Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		final PontuacaoConfig pontuacaoConfig = PontuacaoConfigService.getInstance().findPontuacaoConfigItemPedido(pedido, itemPedido);
		return pontuacaoConfig != null && pontuacaoConfig.pontuacaoProduto != null ? getVlPesoPontuacaoProduto(itemPedido, pontuacaoConfig) : 0d;
	}

	public double getVlPesoPontuacaoProduto(ItemPedido itemPedido, PontuacaoConfig pontuacaoConfig) throws SQLException {
		if (pontuacaoConfig == null) return 1d;
		final Produto produto = itemPedido.getProduto();
		double vlLitroProduto = produto != null && produto.vlLitro > 0 ? produto.vlLitro : 1;
		double pesoPontuacaoLitro = getPesoPontuacaoLitro(pontuacaoConfig, vlLitroProduto);
		if (LavenderePdaConfig.usaConversaoUnidadeAlternativaPesoPontuacao && LavenderePdaConfig.usaUnidadeAlternativa) {
			ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
			if (produtoUnidade != null) {
				double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
				return produtoUnidade.isMultiplica() ? ValueUtil.round(pesoPontuacaoLitro * nuConversaoUnidade) : ValueUtil.round(pesoPontuacaoLitro / nuConversaoUnidade);
			}
		}
		return pesoPontuacaoLitro;
	}

	public double getPesoPontuacaoLitro(PontuacaoConfig pontuacaoConfig, double vlLitroProduto) {
		return pontuacaoConfig.pontuacaoProduto != null && pontuacaoConfig.pontuacaoProduto.vlPesoPontuacao != 0 ? pontuacaoConfig.pontuacaoProduto.vlPesoPontuacao * vlLitroProduto : 1d * vlLitroProduto;
	}
    
}