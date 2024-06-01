package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemConta;
import totalcross.sql.ResultSet;

public class ItemContaDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemConta();
	}

    private static ItemContaDao instance;

    public ItemContaDao() {
        super(ItemConta.TABLE_NAME);
    }

    public static ItemContaDao getInstance() {
        if (instance == null) {
            instance = new ItemContaDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemConta itemConta = new ItemConta();
        itemConta.rowKey = rs.getString("rowkey");
        itemConta.cdEmpresa = rs.getString("cdEmpresa");
        itemConta.cdRepresentante = rs.getString("cdRepresentante");
        itemConta.cdItemConta = rs.getString("cdItemConta");
        itemConta.dsItemConta = rs.getString("dsItemConta");
        itemConta.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemConta.nuCarimbo = rs.getInt("nuCarimbo");
        itemConta.cdUsuario = rs.getString("cdUsuario");
        return itemConta;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDITEMCONTA,");
        sql.append(" DSITEMCONTA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }


	//@Override
	protected void addInsertColumns(StringBuffer arg0) {
	}

	//@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ItemConta itemConta = (ItemConta) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemConta.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemConta.cdRepresentante);
		sqlWhereClause.addAndCondition("CDITEMCONTA = ", itemConta.cdItemConta);
		sql.append(sqlWhereClause.getSql());
	}

}