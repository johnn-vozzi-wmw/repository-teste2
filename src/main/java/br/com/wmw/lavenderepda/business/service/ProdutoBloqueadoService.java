package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoBloqueado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoBloqueadoDbxDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;

public class ProdutoBloqueadoService extends CrudService {

	private static ProdutoBloqueadoService instance;

	public static ProdutoBloqueadoService getInstance() {
		if (instance == null) {
			instance = new ProdutoBloqueadoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) {

	}

	@Override
	protected CrudDao getCrudDao() {
		return ProdutoBloqueadoDbxDao.getInstance();
	}

	private boolean countByExampleForTabPreco(ItemPedido itemPedido, boolean useTabPrecoZero) throws SQLException {
		return ProdutoBloqueadoDbxDao.getInstance().countByExample(getProdutoBloqueadoFilter(itemPedido, useTabPrecoZero)) > 0;
	}
	
	public boolean isProdutoBloqueado(ProdutoBase produto, String cdTabelaPreco) throws SQLException {
		ProdutoBloqueado produtoBloqueado = new ProdutoBloqueado();
		produtoBloqueado.cdEmpresa = produto.cdEmpresa;
		produtoBloqueado.cdRepresentante = produto.cdRepresentante;
		produtoBloqueado.cdProduto = produto.cdProduto;
		if (ValueUtil.isEmpty(cdTabelaPreco)) {
			produtoBloqueado.cdTabelaPreco = ProdutoBloqueado.CDTABELAPRECO_DEFAULT;
		} else {
			produtoBloqueado.cdTabelaPrecoInFilter = new String[] {ProdutoBloqueado.CDTABELAPRECO_DEFAULT, cdTabelaPreco};
		}
		return ProdutoBloqueadoDbxDao.getInstance().countByExample(produtoBloqueado) > 0;
	}

	private ProdutoBloqueado getProdutoBloqueadoFilter(ItemPedido itemPedido, boolean useTabPrecoZero) throws SQLException {
		ProdutoBloqueado produtoBloqueado = new ProdutoBloqueado();
		produtoBloqueado.cdEmpresa = itemPedido.cdEmpresa;
		produtoBloqueado.cdRepresentante = itemPedido.cdRepresentante;
		produtoBloqueado.cdProduto = itemPedido.cdProduto;
		if (useTabPrecoZero) {
			produtoBloqueado.cdTabelaPreco = ProdutoBloqueado.CDTABELAPRECO_DEFAULT;
		} else {
			produtoBloqueado.cdTabelaPreco = getCdTabelaPrecoByItemPedido(itemPedido);
		}
		return produtoBloqueado;
	}

	public boolean isBloqueadoAllTabelaPreco(ItemPedido itemPedido) throws SQLException {
		return countByExampleForTabPreco(itemPedido, true);
	}

	public boolean isBloqueadoForTabelaPreco(ItemPedido itemPedido) throws SQLException {
		return countByExampleForTabPreco(itemPedido, false);
	}

	private boolean isBloqueadoForAnyTabelaPreco(ItemPedido itemPedido) throws SQLException {
		return ProdutoBloqueadoDbxDao.getInstance().countProdutoBloqueadoForAnyTabelaPreco(getProdutoBloqueadoFilter(itemPedido, true)) > 0;
	}

	public void validateProdutoBloqueado(ItemPedido itemPedido) throws SQLException {
		validateProdutoBloqueado(itemPedido, null);
	}

	public void validateProdutoBloqueado(ItemPedido itemPedido, TabelaPrecoComboBox cbTabelaPreco) throws SQLException {
		if (cbTabelaPreco != null && ValueUtil.isEmpty(itemPedido.cdTabelaPreco) && ValueUtil.isEmpty(itemPedido.getCdTabelaPreco()) && cbTabelaPreco.getSelectedIndex() != -1) {
			itemPedido.cdTabelaPrecoFilter = cbTabelaPreco.getValue();
		}
		if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			if (ProdutoBloqueadoService.getInstance().isBloqueadoAllTabelaPreco(itemPedido)) {
				throw new ValidationException(Messages.PRODUTO_MSG_BLOQUEADO);
			} else if (ProdutoBloqueadoService.getInstance().isBloqueadoForTabelaPreco(itemPedido)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PRODUTO_MSG_BLOQUEADO_TABELA_PRECO, TabelaPrecoService.getInstance().getDsTabelaPreco(getCdTabelaPrecoByItemPedido(itemPedido))));
			}
		} else {
			if (itemPedido.isFlSituacaoBloqueado() || ProdutoBloqueadoService.getInstance().isBloqueadoAllTabelaPreco(itemPedido)) {
				throw new ValidationException(Messages.PRODUTO_MSG_BLOQUEADO);
			}
		}
	}

	private String getCdTabelaPrecoByItemPedido(ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco)) {
			return itemPedido.cdTabelaPreco;
		} else if (ValueUtil.isNotEmpty(itemPedido.getCdTabelaPreco())) {
			return itemPedido.getCdTabelaPreco();
		} else {
			return itemPedido.cdTabelaPrecoFilter;
		}
	}

	public String getDsSituacao(Produto produto, ItemTabelaPreco itemTabelaPreco) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemPedido.cdRepresentante = produto.cdRepresentante;
		itemPedido.cdProduto = produto.cdProduto;
		if (itemTabelaPreco != null && ValueUtil.isNotEmpty(itemTabelaPreco.cdTabelaPreco)) {
			itemPedido.cdTabelaPreco = itemTabelaPreco.cdTabelaPreco;
		}
		if (isBloqueadoAllTabelaPreco(itemPedido)) {
			return Messages.PRODUTO_DS_SITUACAO_PRODUTO_BLOQUEADO;
		}
		if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			if ((itemTabelaPreco == null || ValueUtil.isEmpty(itemTabelaPreco.cdTabelaPreco)) && isBloqueadoForAnyTabelaPreco(itemPedido)) {
				return Messages.PRODUTO_DS_BLOQUEADO_ALGUMAS_TABELA_PRECO;
			} else if (itemTabelaPreco != null && ValueUtil.isNotEmpty(itemTabelaPreco.cdTabelaPreco) && isBloqueadoForTabelaPreco(itemPedido)) {
				return Messages.PRODUTO_DS_SITUACAO_PRODUTO_BLOQUEADO;
			}
			return Messages.PRODUTO_DS_SITUACAO_PRODUTO_LIBERADO;
		} else {
			return produto.getDsSituacao();
		}

	}

}
