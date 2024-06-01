package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCod;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoClienteCodDbxDao;
import totalcross.util.Vector;

public class ProdutoClienteCodService extends CrudService  {

	private static ProdutoClienteCodService instance;

	public static ProdutoClienteCodService getInstance() {
		if (instance == null) {
			instance = new ProdutoClienteCodService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
		if (ValueUtil.isEmpty(produtoClienteCod.cdCliente)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_PRODUTOCLIENTECOD, new Object[] {Messages.CAD_PROD_CLI_COD_CLIENTE}));
		}
		if (ValueUtil.isEmpty(produtoClienteCod.cdProduto)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_PRODUTOCLIENTECOD, new Object[] {Messages.CAD_PROD_CLI_COD_PRODUTO}));
		}
		if (ValueUtil.isEmpty(produtoClienteCod.cdProdutoCliente)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_PRODUTOCLIENTECOD, new Object[] {Messages.CAD_PROD_CLI_COD_CODIGO}));
		}
	}

	@Override
	protected CrudDao getCrudDao() {
		return ProdutoClienteCodDbxDao.getInstance();
	}
	
	public Vector findAllProdutoClienteCodFromFechamentoPedido(BaseDomain domain) throws SQLException {
		return ProdutoClienteCodDbxDao.getInstance().findAllProdutoClienteCodFromFechamentoPedido(domain);
	}
	
	@Override
	public void insert(BaseDomain domain) throws SQLException {
		super.insert(domain);
		ProdutoClienteCodAtuaService.getInstance().insert(domain);
	}
	
	@Override
	public void update(BaseDomain domain) throws SQLException {
		super.update(domain);
		ProdutoClienteCodAtuaService.getInstance().update(domain);
	}
	
	@Override
	public void delete(BaseDomain domain) throws SQLException {
		super.delete(domain);
		ProdutoClienteCodAtuaService.getInstance().delete(domain);
	}
	
	@Override
	public void validateDuplicated(BaseDomain domain) throws SQLException {
		if (findColumnByRowKey(domain.getRowKey(), "ROWKEY") != null) {
			throw new ValidationException(Messages.CAD_PROD_CLI_COD_REGISTRO_DUPLICADO);
		}
	}

}
