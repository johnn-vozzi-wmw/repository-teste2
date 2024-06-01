package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.Tree;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MenuCategoria;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MenuCategoriaDbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.ButtonMenuCategoria;
import totalcross.ui.font.FontMetrics;
import totalcross.util.Vector;

public class MenuCategoriaService extends CrudService {

	private static MenuCategoriaService instance;
	private final String RETICENCIAS = "...";

	private MenuCategoriaService() {
	}

	public static MenuCategoriaService getInstance() {
		if (instance == null) {
			instance = new MenuCategoriaService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) {

	}

	@Override
	protected CrudDao getCrudDao() {
		return MenuCategoriaDbxDao.getInstance();
	}

	public List<String> getCategoriaAtualText(Tree<ButtonMenuCategoria> currentButtonMenuCategoriaTree) {
		List<String> descList = new ArrayList<>();
		appendDescriptionCategoriaAtual(descList, currentButtonMenuCategoriaTree);
		return descList;
	}

	public void populateTree(Tree<ButtonMenuCategoria> buttonMenuCategoriaTree) throws SQLException {
		MenuCategoria menuCategoriaFilter = createFilterMenuPaiNull();
		Vector list = findAllByExample(menuCategoriaFilter);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			MenuCategoria menuCategoria = (MenuCategoria) list.items[i];
			ButtonMenuCategoria buttonMenuCategoria = new ButtonMenuCategoria(menuCategoria, false);
			buttonMenuCategoriaTree.addLeaf(buttonMenuCategoria);
		}
		for (Tree<ButtonMenuCategoria> tree : buttonMenuCategoriaTree.getSubTrees()) {
			populateTreeChildren(tree);
		}
	}

	private void populateTreeChildren(Tree<ButtonMenuCategoria> buttonMenuCategoriaTree) throws SQLException {
		MenuCategoria menuCategoriaPai = buttonMenuCategoriaTree.head.menuCategoria;
		MenuCategoria menuCategoriaChildrenFilter = new MenuCategoria();
		menuCategoriaChildrenFilter.cdEmpresa = menuCategoriaPai.cdEmpresa;
		menuCategoriaChildrenFilter.cdRepresentante = menuCategoriaPai.cdRepresentante;
		menuCategoriaChildrenFilter.cdUsuario = menuCategoriaPai.cdUsuario;
		menuCategoriaChildrenFilter.cdMenuPai = menuCategoriaPai.cdMenu;
		Vector children = findAllByExample(menuCategoriaChildrenFilter);
		int size = children.size();
		for (int i = 0; i < size; i++) {
			MenuCategoria menuCategoria = (MenuCategoria) children.items[i];
			ButtonMenuCategoria buttonMenuCategoria = new ButtonMenuCategoria(menuCategoria, false);
			buttonMenuCategoriaTree.addLeaf(buttonMenuCategoria);
			populateTreeChildren(buttonMenuCategoriaTree.getTree(buttonMenuCategoria));
		}
	}

	private MenuCategoria createFilterMenuPaiNull() {
		MenuCategoria menuCategoria = new MenuCategoria();
		menuCategoria.cdMenuPai = null;
		menuCategoria.cdEmpresa = SessionLavenderePda.cdEmpresa;
		menuCategoria.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		return menuCategoria;
	}

	public void appendDescriptionCategoriaAtual(List<String> descriptionList, Tree<ButtonMenuCategoria> buttonMenuCategoriaTree) {
		if (buttonMenuCategoriaTree != null && buttonMenuCategoriaTree.head.menuCategoria != null) {
			descriptionList.add(buttonMenuCategoriaTree.head.menuCategoria.dsMenu);
		}
		if (buttonMenuCategoriaTree != null && buttonMenuCategoriaTree.getParent() != null && buttonMenuCategoriaTree.getParent().head.menuCategoria != null) {
			appendDescriptionCategoriaAtual(descriptionList, buttonMenuCategoriaTree.getParent());
		}
	}

	public MenuCategoria getMenuCategoriaPrincipal() throws SQLException {
		return ((MenuCategoriaDbxDao) getCrudDao()).findMenuPrincipal(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
	}

	//retorna o número correspondente do intervalo de 1 a 10 no intervalo de 0.8 a 2;
	public double getNuTamanhoPadraoMap() {
		return map(LavenderePdaConfig.getNuTamanhoPadrao(), 1, 10, 0.8, 2);
	}

	private double map(double x, double inMin, double inMax, double outMin, double outMax) {
		return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}

	public String getCategoriaAtualShortText(List<String> fullText, int labelValueWidth, FontMetrics fm) {
		int size = fullText.size();
		if (size > 3) {
			String shortText = fullText.get(size - 1) + Messages.MENU_CATEGORIA_CATEGORIA_ATUAL_SEPARADOR + fullText.get(size - 2) +
					Messages.MENU_CATEGORIA_CATEGORIA_ATUAL_SEPARADOR + RETICENCIAS +
					Messages.MENU_CATEGORIA_CATEGORIA_ATUAL_SEPARADOR + fullText.get(0);
			return getTextForBounds(shortText, labelValueWidth, fm);
		} else {
			return getTextForBounds(getFullTextPath(fullText), labelValueWidth, fm);
		}
	}

	public String getFullTextPath(List<String> textList) {
		StringBuilder str = new StringBuilder();
		int size = textList.size();
		for (int i = size - 1; i >= 0; i--) {
			str.append(textList.get(i));
			if (i > 0) {
				str.append(Messages.MENU_CATEGORIA_CATEGORIA_ATUAL_SEPARADOR);
			}
		}
		return str.toString();
	}

	private String getTextForBounds(String text, int labelValueWidth, FontMetrics fm) {
		if (fm.stringWidth(text) > labelValueWidth) {
			boolean isOutOfBounds = true;
			int index = 0;
			while (isOutOfBounds) {
				index++;
				isOutOfBounds = fm.stringWidth(text.substring(index, text.length() - 1)) > labelValueWidth;
			}
			return RETICENCIAS + (text.substring(index + 3).trim());
		} else {
			return text;
		}
	}
}
