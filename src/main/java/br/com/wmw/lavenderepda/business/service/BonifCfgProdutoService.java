package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;
import br.com.wmw.lavenderepda.business.domain.BonifCfgProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.validation.BonifCfgProdutoNaoEncontradoException;
import br.com.wmw.lavenderepda.business.validation.BonifCfgProdutoQuantidadeAutomaticaExcedenteException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.BonifCfgProdutoDbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ListBonifCfgProdutoWindow;

import java.sql.SQLException;

public class BonifCfgProdutoService extends CrudService {

	public static BonifCfgProdutoService instance;

	public static BonifCfgProdutoService getInstance() {
		if (instance == null) {
			instance = new BonifCfgProdutoService();
		}
		return instance;
	}

	public static void invalidateInstance() {
		instance = null;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		//nao cadastrado no app
	}

	@Override
	protected CrudDao getCrudDao() {
		return BonifCfgProdutoDbxDao.getInstance();
	}

	public BonifCfgProduto findBonifCfgProdutoBonificar(ItemPedidoBonifCfg itemPedidoBonificadoFilter, BonifCfg bonifCfg) throws SQLException {
		BonifCfgProduto bonifCfgProdutoFilter = new BonifCfgProduto();
		bonifCfgProdutoFilter.cdEmpresa = itemPedidoBonificadoFilter.cdEmpresa;
		bonifCfgProdutoFilter.cdBonifCfg = itemPedidoBonificadoFilter.cdBonifCfg;
		bonifCfgProdutoFilter.cdRepresentanteFilter = itemPedidoBonificadoFilter.cdRepresentante;
		bonifCfgProdutoFilter.flPermiteBonificar = ValueUtil.VALOR_SIM;
		try {
			return BonifCfgProdutoDbxDao.getInstance().findBonifCfgProdutoBonificar(bonifCfgProdutoFilter, bonifCfg);
		} catch (BonifCfgProdutoQuantidadeAutomaticaExcedenteException | BonifCfgProdutoNaoEncontradoException excedenteException) {
			itemPedidoBonificadoFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
			UiUtil.showErrorMessage(excedenteException.getMessage());
			return null;
		}
	}

}
