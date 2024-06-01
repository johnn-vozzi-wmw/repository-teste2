package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RotaEntregaCli;
import totalcross.sql.ResultSet;

public class RotaEntregaCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RotaEntregaCli();
	}

    private static RotaEntregaCliDbxDao instance;

    public RotaEntregaCliDbxDao() {
        super(RotaEntregaCli.TABLE_NAME);
    }

    public static RotaEntregaCliDbxDao getInstance() {
        if (instance == null) {
            instance = new RotaEntregaCliDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        RotaEntregaCli rotaEntregaCli = new RotaEntregaCli();
        rotaEntregaCli.rowKey = rs.getString("rowkey");
        rotaEntregaCli.cdEmpresa = rs.getString("cdEmpresa");
        rotaEntregaCli.cdRepresentante = rs.getString("cdRepresentante");
        rotaEntregaCli.cdRotaEntrega = rs.getString("cdRotaEntrega");
        rotaEntregaCli.cdCliente = rs.getString("cdCliente");
        rotaEntregaCli.nuCarimbo = rs.getInt("nuCarimbo");
        rotaEntregaCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        rotaEntregaCli.cdUsuario = rs.getString("cdUsuario");
        return rotaEntregaCli;
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
        sql.append(" CDROTAENTREGA,");
        sql.append(" CDCLIENTE,");
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
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDROTAENTREGA,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RotaEntregaCli rotaEntregaCli = (RotaEntregaCli) domain;
        sql.append(Sql.getValue(rotaEntregaCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(rotaEntregaCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(rotaEntregaCli.cdRotaEntrega)).append(",");
        sql.append(Sql.getValue(rotaEntregaCli.cdCliente)).append(",");
        sql.append(Sql.getValue(rotaEntregaCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(rotaEntregaCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(rotaEntregaCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RotaEntregaCli rotaEntregaCli = (RotaEntregaCli) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(rotaEntregaCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(rotaEntregaCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(rotaEntregaCli.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RotaEntregaCli rotaEntregaCli = (RotaEntregaCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", rotaEntregaCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", rotaEntregaCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDROTAENTREGA = ", rotaEntregaCli.cdRotaEntrega);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", rotaEntregaCli.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}