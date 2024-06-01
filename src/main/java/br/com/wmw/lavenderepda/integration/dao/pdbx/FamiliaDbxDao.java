package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Familia;
import totalcross.sql.ResultSet;

public class FamiliaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Familia();
	}

    private static FamiliaDbxDao instance;

    public FamiliaDbxDao() {
        super(Familia.TABLE_NAME);
    }

    public static FamiliaDbxDao getInstance() {
        if (instance == null) {
            instance = new FamiliaDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Familia familia = new Familia();
        familia.rowKey = rs.getString("rowkey");
        familia.cdEmpresa = rs.getString("cdEmpresa");
        familia.cdFamilia = rs.getString("cdFamilia");
        familia.dsFamilia = rs.getString("dsFamilia");
        familia.nuCarimbo = rs.getInt("nuCarimbo");
        familia.flTipoAlteracao = rs.getString("flTipoAlteracao");
        familia.cdUsuario = rs.getString("cdUsuario");
        return familia;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDFAMILIA,");
        sql.append(" DSFAMILIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDFAMILIA,");
        sql.append(" DSFAMILIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Familia familia = (Familia) domain;
        sql.append(Sql.getValue(familia.cdEmpresa)).append(",");
        sql.append(Sql.getValue(familia.cdFamilia)).append(",");
        sql.append(Sql.getValue(familia.dsFamilia)).append(",");
        sql.append(Sql.getValue(familia.nuCarimbo)).append(",");
        sql.append(Sql.getValue(familia.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(familia.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Familia familia = (Familia) domain;
        sql.append(" DSFAMILIA = ").append(Sql.getValue(familia.dsFamilia)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(familia.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(familia.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(familia.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Familia familia = (Familia) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", familia.cdEmpresa);
		sqlWhereClause.addAndCondition("CDFAMILIA = ", familia.cdFamilia);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}