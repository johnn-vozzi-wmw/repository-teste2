package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaUsuario;
import totalcross.sql.ResultSet;

public class VerbaUsuarioPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaUsuario();
	}

    private static VerbaUsuarioPdbxDao instance;
	

    public VerbaUsuarioPdbxDao() {
    	 super(VerbaUsuario.TABLE_NAME);
    }
    
    public static VerbaUsuarioPdbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaUsuarioPdbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VerbaUsuario verbaUsuario = new VerbaUsuario();
        verbaUsuario.rowKey = rs.getString("rowkey");
        verbaUsuario.flOrigemSaldo = rs.getString("flOrigemSaldo");
        verbaUsuario.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        verbaUsuario.vlSaldoOrg = ValueUtil.round(rs.getDouble("vlSaldoOrg"));
        verbaUsuario.nuCarimbo = rs.getInt("nuCarimbo");
        verbaUsuario.flTipoAlteracao = rs.getString("flTipoAlteracao");
        verbaUsuario.cdUsuario = rs.getString("cdUsuario");
        return verbaUsuario;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" VLSALDOORG,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLSALDO,");
        sql.append(" VLSALDOORG,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaUsuario verbaUsuario = (VerbaUsuario) domain;
        sql.append(Sql.getValue(verbaUsuario.flOrigemSaldo)).append(",");
        sql.append(Sql.getValue(verbaUsuario.vlSaldo)).append(",");
        sql.append(Sql.getValue(verbaUsuario.vlSaldoOrg)).append(",");
        sql.append(Sql.getValue(verbaUsuario.nuCarimbo)).append(",");
        sql.append(Sql.getValue(verbaUsuario.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(verbaUsuario.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaUsuario verbaUsuario = (VerbaUsuario) domain;
        sql.append(" VLSALDO = ").append(Sql.getValue(verbaUsuario.vlSaldo)).append(",");
        sql.append(" VLSALDOORG = ").append(Sql.getValue(verbaUsuario.vlSaldoOrg)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(verbaUsuario.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaUsuario.flTipoAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaUsuario verbaUsuario = (VerbaUsuario) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("FLORIGEMSALDO = ", verbaUsuario.flOrigemSaldo);
		sqlWhereClause.addAndCondition("CDUSUARIO = ", verbaUsuario.cdUsuario);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}