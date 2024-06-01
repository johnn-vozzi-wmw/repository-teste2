package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoDestaque;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoDestaqueDao;

public class ProdutoDestaqueService extends CrudService {
	private static ProdutoDestaqueService instance;

	public static ProdutoDestaqueService getInstance() {
        if (instance == null) {
            instance = new ProdutoDestaqueService();
        }
        return instance;
    }

	//@Override
	protected CrudDao getCrudDao() {
		return ProdutoDestaqueDao.getInstance();
	}

	public void validate(BaseDomain arg0) throws SQLException {
	}

	public ProdutoDestaque findProdutoDestaqueByProdutoBase(ProdutoBase produtoBase) throws SQLException {
		if (produtoBase != null) {
			return getProdutoDestaque(produtoBase.cdEmpresa, produtoBase.cdRepresentante, produtoBase.cdGrupoDestaque);
		}
		return null;
	}

	public ProdutoDestaque findProdutoDestaqueByProdutoBaseAndTabPreco(ProdutoBase produtoBase, String cdTabPrecoSelecionada, String dsTabPrecoList, String cdGrupoDestaqueList) throws SQLException {
		if (produtoBase != null) {
			return getProdutoDestaque(produtoBase.cdEmpresa, produtoBase.cdRepresentante, getCdGrupoDestaqueRelacionadoTabPreco(cdTabPrecoSelecionada, dsTabPrecoList, cdGrupoDestaqueList));
		}
		return null;
	}

	private String getCdGrupoDestaqueRelacionadoTabPreco(String cdTabPrecoSelecionada, String dsTabPrecoList, String cdGrupoDestaqueList) {
		if (ValueUtil.isNotEmpty(cdTabPrecoSelecionada)) {
			String[] dsTabPrecoListResult = StringUtil.split(StringUtil.getStringValue(dsTabPrecoList), ProdutoTabPreco.SEPARADOR_CAMPOS);
			String[] cdGrupoDestaqueListResult = StringUtil.split(StringUtil.getStringValue(cdGrupoDestaqueList), ProdutoTabPreco.SEPARADOR_CAMPOS, true, true);
			for (int i = 0; i < dsTabPrecoListResult.length; i++) {
				if (cdTabPrecoSelecionada.equals(dsTabPrecoListResult[i])) {
					return cdGrupoDestaqueListResult[i];
				}
			}
		}
		return "";
	}

	private ProdutoDestaque getProdutoDestaque(String cdEmpresa, String cdRepresentante, String cdGrupoDestaque) throws SQLException {
		if (ValueUtil.isEmpty(cdGrupoDestaque)) {
			return null;
		}
		ProdutoDestaque produtoDestaqueFilter = new ProdutoDestaque();
		produtoDestaqueFilter.cdEmpresa = cdEmpresa;
		produtoDestaqueFilter.cdRepresentante = cdRepresentante;
		produtoDestaqueFilter.cdGrupoDestaque = cdGrupoDestaque;
		return (ProdutoDestaque) findByRowKey(produtoDestaqueFilter.getRowKey());
	}
}
