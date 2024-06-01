package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondPagtoCli;
import totalcross.sql.ResultSet;

public class CondPagtoCliPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondPagtoCli();
	}

    private static CondPagtoCliPdbxDao instance;

    public CondPagtoCliPdbxDao() {
        super(CondPagtoCli.TABLE_NAME);
    }

    public static CondPagtoCliPdbxDao getInstance() {
        if (instance == null) {
            instance = new CondPagtoCliPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondPagtoCli condPagtoCli = new CondPagtoCli();
        condPagtoCli.rowKey = rs.getString("rowkey");
        condPagtoCli.cdEmpresa = rs.getString("cdEmpresa");
        condPagtoCli.cdRepresentante = rs.getString("cdRepresentante");
        condPagtoCli.cdCliente = rs.getString("cdCliente");
        condPagtoCli.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condPagtoCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return condPagtoCli;
    }
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoCli condpagtocli = (CondPagtoCli) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", condpagtocli.cdEmpresa);
       	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condpagtocli.cdRepresentante);
        sqlWhereClause.addAndCondition("CDCLIENTE = ", condpagtocli.cdCliente);
       	sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", condpagtocli.cdCondicaoPagamento);
        sql.append(sqlWhereClause.getSql());
    }
}