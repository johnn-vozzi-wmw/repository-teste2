package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.lavenderepda.business.domain.AplicacaoProduto;
import totalcross.sql.ResultSet;

public class AplicacaoProdutoDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AplicacaoProduto();
	}

	public static AplicacaoProdutoDbxDao instance;
	
	public static AplicacaoProdutoDbxDao getInstance() {
		if (instance == null) {
			instance = new AplicacaoProdutoDbxDao();
		}
		return instance;
	}

	public AplicacaoProdutoDbxDao() {
		super(AplicacaoProduto.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		AplicacaoProduto aplicacaoProduto = new AplicacaoProduto();
		aplicacaoProduto.cdProduto = rs.getString("cdProduto");
		aplicacaoProduto.cdCultura = rs.getString("cdCultura");
		aplicacaoProduto.cdPraga = rs.getString("cdPraga");
		aplicacaoProduto.vlDose = rs.getString("vlDose");
		return aplicacaoProduto;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDPRODUTO,");
		sql.append(" CDCULTURA,");
		sql.append(" CDPRAGA,");
		sql.append(" VLDOSE");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

}
