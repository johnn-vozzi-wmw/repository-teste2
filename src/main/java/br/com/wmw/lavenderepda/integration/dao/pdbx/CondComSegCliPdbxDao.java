package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondComSegCli;
import totalcross.sql.ResultSet;

public class CondComSegCliPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondComSegCli();
	}

    private static CondComSegCliPdbxDao instance;

    public CondComSegCliPdbxDao() {
        super(CondComSegCli.TABLE_NAME);
    }

    public static CondComSegCliPdbxDao getInstance() {
        if (instance == null) {
            instance = new CondComSegCliPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondComSegCli condComSegCli = new CondComSegCli();
        condComSegCli.rowKey = rs.getString("rowkey");
        condComSegCli.cdEmpresa = rs.getString("cdEmpresa");
        condComSegCli.cdRepresentante = rs.getString("cdRepresentante");
        condComSegCli.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        condComSegCli.cdSegmento = rs.getString("cdSegmento");
        condComSegCli.cdCliente = rs.getString("cdCliente");
        condComSegCli.flDefault = rs.getString("flDefault");
        condComSegCli.nuCarimbo = rs.getInt("nuCarimbo");
        condComSegCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condComSegCli.cdUsuario = rs.getString("cdUsuario");
        return condComSegCli;
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
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" CDSEGMENTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLDEFAULT,");
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
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" CDSEGMENTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLDEFAULT,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondComSegCli condComSegCli = (CondComSegCli) domain;
        sql.append(Sql.getValue(condComSegCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(condComSegCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(condComSegCli.cdCondicaoComercial)).append(",");
        sql.append(Sql.getValue(condComSegCli.cdSegmento)).append(",");
        sql.append(Sql.getValue(condComSegCli.cdCliente)).append(",");
        sql.append(Sql.getValue(condComSegCli.flDefault)).append(",");
        sql.append(Sql.getValue(condComSegCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(condComSegCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(condComSegCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	CondComSegCli condComSegCli = (CondComSegCli) domain;
        sql.append(" FLDEFAULT = ").append(Sql.getValue(condComSegCli.flDefault)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(condComSegCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(condComSegCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(condComSegCli.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	CondComSegCli condComSegCli = (CondComSegCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condComSegCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condComSegCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCONDICAOCOMERCIAL = ", condComSegCli.cdCondicaoComercial);
		sqlWhereClause.addAndCondition("CDSEGMENTO = ", condComSegCli.cdSegmento);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", condComSegCli.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}