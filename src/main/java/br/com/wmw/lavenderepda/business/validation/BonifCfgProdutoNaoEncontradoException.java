package br.com.wmw.lavenderepda.business.validation;

import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.BonifCfg;

public class BonifCfgProdutoNaoEncontradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public BonifCfgProdutoNaoEncontradoException(BonifCfg bonifCfg) {
		super(MessageUtil.getMessage(Messages.BONIFCFGPRODUTO_NAO_ENCONTRADO_ERROR, bonifCfg.dsBonifCfg));
	}
}
