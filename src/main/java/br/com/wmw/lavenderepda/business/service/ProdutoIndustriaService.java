package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoIndustriaPdbxDao;

public class ProdutoIndustriaService extends CrudPersonLavendereService {

	private static ProdutoIndustriaService instance;

	public static ProdutoIndustriaService getInstance() {
		return instance == null ? instance = new ProdutoIndustriaService() : instance;
	}
	
	public int getQuantidadeSugeridaArredondada(final ProdutoBase produto) {
		if (produto.produtoIndustriaSugestao.vlLitroSugestao > 0 && produto.vlLitro > 0) {
			return (int) (produto.produtoIndustriaSugestao.vlLitroSugestao / produto.vlLitro);
		}
		return 0;
	}

	protected CrudDao getCrudDao() { return ProdutoIndustriaPdbxDao.getInstance(); }
	public void validate(BaseDomain domain) throws SQLException {}

}