package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.StatusRentCli;
import totalcross.sql.ResultSet;

public class StatusRentCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new StatusRentCli();
	}

    private static StatusRentCliDbxDao instance;

    public StatusRentCliDbxDao() {
        super(StatusRentCli.TABLE_NAME);
    }
    
    public static StatusRentCliDbxDao getInstance() {
        if (instance == null) {
            instance = new StatusRentCliDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        StatusRentCli statusRentCli = new StatusRentCli();
        statusRentCli.rowKey = rs.getString("rowkey");
        statusRentCli.cdEmpresa = rs.getString("cdEmpresa");
        statusRentCli.cdStatusRentCli = rs.getString("cdStatusRentCli");
        statusRentCli.dsStatusRentCli = rs.getString("dsStatusRentCli");
        statusRentCli.vlRLista = rs.getInt("vlRLista");
        statusRentCli.vlGLista = rs.getInt("vlGLista");
        statusRentCli.vlBLista = rs.getInt("vlBLista");
        statusRentCli.vlRIcon = rs.getInt("vlRIcon");
        statusRentCli.vlGIcon = rs.getInt("vlGIcon");
        statusRentCli.vlBIcon = rs.getInt("vlBIcon");
        statusRentCli.nuCarimbo = rs.getInt("nuCarimbo");
        statusRentCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        statusRentCli.cdUsuario = rs.getString("cdUsuario");
        return statusRentCli;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDSTATUSRENTCLI,");
        sql.append(" DSSTATUSRENTCLI,");
        sql.append(" VLRLISTA,");
        sql.append(" VLGLISTA,");
        sql.append(" VLBLISTA,");
        sql.append(" VLRICON,");
        sql.append(" VLGICON,");
        sql.append(" VLBICON,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDSTATUSRENTCLI,");
        sql.append(" DSSTATUSRENTCLI,");
        sql.append(" VLRLISTA,");
        sql.append(" VLGLISTA,");
        sql.append(" VLBLISTA,");
        sql.append(" VLRICON,");
        sql.append(" VLGICON,");
        sql.append(" VLBICON,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        StatusRentCli statusRentCli = (StatusRentCli) domain;
        sql.append(Sql.getValue(statusRentCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(statusRentCli.cdStatusRentCli)).append(",");
        sql.append(Sql.getValue(statusRentCli.dsStatusRentCli)).append(",");
        sql.append(Sql.getValue(statusRentCli.vlRLista)).append(",");
        sql.append(Sql.getValue(statusRentCli.vlGLista)).append(",");
        sql.append(Sql.getValue(statusRentCli.vlBLista)).append(",");
        sql.append(Sql.getValue(statusRentCli.vlRIcon)).append(",");
        sql.append(Sql.getValue(statusRentCli.vlGIcon)).append(",");
        sql.append(Sql.getValue(statusRentCli.vlBIcon)).append(",");
        sql.append(Sql.getValue(statusRentCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(statusRentCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(statusRentCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        StatusRentCli statusRentCli = (StatusRentCli) domain;
        sql.append(" DSSTATUSRENTCLI = ").append(Sql.getValue(statusRentCli.dsStatusRentCli)).append(",");
        sql.append(" VLRLISTA = ").append(Sql.getValue(statusRentCli.vlRLista)).append(",");
        sql.append(" VLGLISTA = ").append(Sql.getValue(statusRentCli.vlGLista)).append(",");
        sql.append(" VLBLISTA = ").append(Sql.getValue(statusRentCli.vlBLista)).append(",");
        sql.append(" VLRICON = ").append(Sql.getValue(statusRentCli.vlRIcon)).append(",");
        sql.append(" VLGICON = ").append(Sql.getValue(statusRentCli.vlGIcon)).append(",");
        sql.append(" VLBICON = ").append(Sql.getValue(statusRentCli.vlBIcon)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(statusRentCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(statusRentCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(statusRentCli.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        StatusRentCli statusRentCli = (StatusRentCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", statusRentCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSTATUSRENTCLI = ", statusRentCli.cdStatusRentCli);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}