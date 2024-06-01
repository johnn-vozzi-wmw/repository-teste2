package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.lavenderepda.Messages;
import totalcross.util.Vector;

public class BonifCfgProdutoQuantidadeAutomaticaExcedenteException extends Exception {

	private static final long serialVersionUID = 1L;
	public Vector bonifCfgProdutoList;

	public BonifCfgProdutoQuantidadeAutomaticaExcedenteException(Vector bonifCfgProdutoList) {
		super(Messages.BONIFCFGPRODUTO_QUANTIDADE_AUTOMATICA_EXCEDENTE_ERROR);
		this.bonifCfgProdutoList = bonifCfgProdutoList;
	}

}
