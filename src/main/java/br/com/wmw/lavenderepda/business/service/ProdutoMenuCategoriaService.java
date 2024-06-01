package br.com.wmw.lavenderepda.business.service;

import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.Tree;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MenuCategoria;
import br.com.wmw.lavenderepda.business.domain.ProdutoMenuCategoria;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoMenuCategoriaDbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.ButtonMenuCategoria;

public class ProdutoMenuCategoriaService extends CrudService {

	private static ProdutoMenuCategoriaService instance;

	private ProdutoMenuCategoriaService() {
	}

	public static ProdutoMenuCategoriaService getInstance() {
		if (instance == null) {
			instance = new ProdutoMenuCategoriaService();
		}
		return instance;
	}


	@Override
	public void validate(BaseDomain domain) {
		/**/
	}

	@Override
	protected CrudDao getCrudDao() {
		return ProdutoMenuCategoriaDbxDao.getInstance();
	}
	
	public ProdutoMenuCategoria getProdutoMenuCategoriaFilter(Tree<ButtonMenuCategoria> tree) {
		List<String> cdMenuList = new ArrayList<>(); 
		populateCdMenuList(tree, cdMenuList, false);
		ProdutoMenuCategoria produtoMenuCategoriaFilter = new ProdutoMenuCategoria();
		produtoMenuCategoriaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoMenuCategoriaFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoMenuCategoria.class);
		produtoMenuCategoriaFilter.cdMenuListFilter = cdMenuList.toArray(new String[0]);
		return produtoMenuCategoriaFilter;
		
	}

	private void populateCdMenuList(Tree<ButtonMenuCategoria> tree, List<String> inList, boolean ignoreMostraProdutos) {
		if (tree != null && tree.head.menuCategoria == null) {
			for (Tree<ButtonMenuCategoria> buttonMenuCategoriaTree : tree.getSubTrees()) {
				appendCdMenu(buttonMenuCategoriaTree, inList, ignoreMostraProdutos);
			}
		} else if (tree != null) {
			appendCdMenu(tree, inList, ignoreMostraProdutos);
		}

	}

	private void appendCdMenu(Tree<ButtonMenuCategoria> tree, List<String> inList, boolean ignoreMostraProdutos) {
		MenuCategoria menuCategoria = tree.head.menuCategoria;
		if (menuCategoria.mostraProdutos() || ignoreMostraProdutos) {
			inList.add(menuCategoria.cdMenu);
		}
		if (menuCategoria.mostraProdutosFilhos()) {
			for (Tree<ButtonMenuCategoria> subTree : tree.getSubTrees()) {
				populateCdMenuList(subTree, inList, true);
			}
		}
	}

}
