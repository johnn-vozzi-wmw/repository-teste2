package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CondComercialExcec;
import totalcross.sql.ResultSet;

public class CondComercialExcecDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondComercialExcec();
	}

    private static CondComercialExcecDao instance;

    public CondComercialExcecDao() {
        super(CondComercialExcec.TABLE_NAME);
    }

    public static CondComercialExcecDao getInstance() {
        if (instance == null) {
            instance = new CondComercialExcecDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondComercialExcec condComercialExcec = new CondComercialExcec();
        condComercialExcec.rowKey = rs.getString("rowkey");
        condComercialExcec.cdEmpresa = rs.getString("cdEmpresa");
        condComercialExcec.cdRepresentante = rs.getString("cdRepresentante");
        condComercialExcec.cdProduto = rs.getString("cdProduto");
        condComercialExcec.cdCondicaoComercial = rs.getString("cdCondicaocomercial");
        condComercialExcec.cdItemGrade1 = rs.getString("cdItemGrade1");
        condComercialExcec.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        condComercialExcec.vlBase = ValueUtil.round(rs.getDouble("vlBase"));
        condComercialExcec.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condComercialExcec.nuCarimbo = rs.getInt("nuCarimbo");
        condComercialExcec.cdUsuario = rs.getString("cdUsuario");
        return condComercialExcec;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" VLUNITARIO,");
        sql.append(" VLBASE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
    @Override
    protected void addCacheColumns(StringBuffer sql) throws SQLException {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" VLUNITARIO,");
        sql.append(" VLBASE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	//@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {	
		CondComercialExcec condComercialExcec = (CondComercialExcec) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condComercialExcec.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condComercialExcec.cdRepresentante);
		sql.append(sqlWhereClause.getSql());
	}

	//@Override
	protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }

}