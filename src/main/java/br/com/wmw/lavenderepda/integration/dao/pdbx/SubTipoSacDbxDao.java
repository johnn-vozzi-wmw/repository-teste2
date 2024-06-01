package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.SubTipoSac;
import totalcross.sql.ResultSet;

public class SubTipoSacDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SubTipoSac();
	}

    private static SubTipoSacDbxDao instance;

    public SubTipoSacDbxDao() {
        super(SubTipoSac.TABLE_NAME);
    }
    
    public static SubTipoSacDbxDao getInstance() {
        if (instance == null) {
            instance = new SubTipoSacDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        SubTipoSac subTipoSac = new SubTipoSac();
        subTipoSac.rowKey = rs.getString("rowkey");
        subTipoSac.cdEmpresa = rs.getString("cdEmpresa");
        subTipoSac.cdTipoSac = rs.getString("cdTipoSac");
        subTipoSac.cdSubTipoSac = rs.getString("cdSubTipoSac");
        subTipoSac.dsSubTipoSac = rs.getString("dsSubTipoSac");
        subTipoSac.nuCarimbo = rs.getInt("nuCarimbo");
        subTipoSac.flTipoAlteracao = rs.getString("flTipoAlteracao");
        subTipoSac.cdUsuario = rs.getString("cdUsuario");
        return subTipoSac;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOSAC,");
        sql.append(" CDSUBTIPOSAC,");
        sql.append(" DSSUBTIPOSAC,");
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
        sql.append(" CDTIPOSAC,");
        sql.append(" CDSUBTIPOSAC,");
        sql.append(" DSSUBTIPOSAC,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SubTipoSac subTipoSac = (SubTipoSac) domain;
        sql.append(Sql.getValue(subTipoSac.cdEmpresa)).append(",");
        sql.append(Sql.getValue(subTipoSac.cdTipoSac)).append(",");
        sql.append(Sql.getValue(subTipoSac.cdSubTipoSac)).append(",");
        sql.append(Sql.getValue(subTipoSac.dsSubTipoSac)).append(",");
        sql.append(Sql.getValue(subTipoSac.nuCarimbo)).append(",");
        sql.append(Sql.getValue(subTipoSac.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(subTipoSac.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SubTipoSac subTipoSac = (SubTipoSac) domain;
        sql.append(" DSSUBTIPOSAC = ").append(Sql.getValue(subTipoSac.dsSubTipoSac)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(subTipoSac.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(subTipoSac.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(subTipoSac.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SubTipoSac subTipoSac = (SubTipoSac) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", subTipoSac.cdEmpresa);
		sqlWhereClause.addAndCondition("CDTIPOSAC = ", subTipoSac.cdTipoSac);
		sqlWhereClause.addAndCondition("CDSUBTIPOSAC = ", subTipoSac.cdSubTipoSac);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}