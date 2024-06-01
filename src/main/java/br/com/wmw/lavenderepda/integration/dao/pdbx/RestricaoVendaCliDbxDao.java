package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RestricaoVendaCli;
import totalcross.sql.ResultSet;

public class RestricaoVendaCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RestricaoVendaCli();
	}

    private static RestricaoVendaCliDbxDao instance;

    public RestricaoVendaCliDbxDao() {
        super(RestricaoVendaCli.TABLE_NAME);
    }

    public static RestricaoVendaCliDbxDao getInstance() {
        if (instance == null) {
            instance = new RestricaoVendaCliDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        RestricaoVendaCli restricaoVendaCli = new RestricaoVendaCli();
        restricaoVendaCli.rowKey = rs.getString("rowkey");
        restricaoVendaCli.cdEmpresa = rs.getString("cdEmpresa");
        restricaoVendaCli.cdRepresentante = rs.getString("cdRepresentante");
        restricaoVendaCli.cdRestricaoVendaProd = rs.getString("cdRestricaoVendaProd");
        restricaoVendaCli.cdRestricaoVendaCli = rs.getString("cdRestricaoVendaCli");
        restricaoVendaCli.flBloqueiaVenda = rs.getString("flBloqueiaVenda");
        restricaoVendaCli.flBloqueiaAlteracaoPreco = rs.getString("flBloqueiaAlteracaoPreco");
        restricaoVendaCli.nuCarimbo = rs.getInt("nuCarimbo");
        restricaoVendaCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        restricaoVendaCli.cdUsuario = rs.getString("cdUsuario");
        return restricaoVendaCli;
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
        sql.append(" CDRESTRICAOVENDAPROD,");
        sql.append(" CDRESTRICAOVENDACLI,");
        sql.append(" FLBLOQUEIAVENDA,");
        sql.append(" FLBLOQUEIAALTERACAOPRECO,");
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
        sql.append(" CDRESTRICAOVENDAPROD,");
        sql.append(" CDRESTRICAOVENDACLI,");
        sql.append(" FLBLOQUEIAVENDA,");
        sql.append(" FLBLOQUEIAALTERACAOPRECO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RestricaoVendaCli restricaoVendaCli = (RestricaoVendaCli) domain;
        sql.append(Sql.getValue(restricaoVendaCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.cdRestricaoVendaProd)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.cdRestricaoVendaCli)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.flBloqueiaVenda)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.flBloqueiaAlteracaoPreco)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(restricaoVendaCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RestricaoVendaCli restricaoVendaCli = (RestricaoVendaCli) domain;
        sql.append(" FLBLOQUEIAVENDA = ").append(Sql.getValue(restricaoVendaCli.flBloqueiaVenda)).append(",");
        sql.append(" FLBLOQUEIAALTERACAOPRECO = ").append(Sql.getValue(restricaoVendaCli.flBloqueiaAlteracaoPreco)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(restricaoVendaCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(restricaoVendaCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(restricaoVendaCli.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RestricaoVendaCli restricaoVendaCli = (RestricaoVendaCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", restricaoVendaCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", restricaoVendaCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDRESTRICAOVENDAPROD = ", restricaoVendaCli.cdRestricaoVendaProd);
		sqlWhereClause.addAndCondition("CDRESTRICAOVENDACLI = ", restricaoVendaCli.cdRestricaoVendaCli);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}