package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondPagtoLinha;
import totalcross.sql.ResultSet;

public class CondPagtoLinhaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondPagtoLinha();
	}

    private static CondPagtoLinhaPdbxDao instance;

    public CondPagtoLinhaPdbxDao() {
        super(CondPagtoLinha.TABLE_NAME);
    }

    public static CondPagtoLinhaPdbxDao getInstance() {
        if (instance == null) {
            instance = new CondPagtoLinhaPdbxDao();
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
        sql.append(" rowKey,");
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" cdCondicaoPagamento,");
        sql.append(" cdRegiao,");
        sql.append(" cdLinha,");
        sql.append(" nuDiasPrazo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario,");
        sql.append(" vlIndiceFinanceiro");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondPagtoLinha condPagtoLinha = new CondPagtoLinha();
        condPagtoLinha.rowKey = rs.getString("rowkey");
        condPagtoLinha.cdEmpresa = rs.getString("cdEmpresa");
        condPagtoLinha.cdRepresentante = rs.getString("cdRepresentante");
        condPagtoLinha.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condPagtoLinha.cdRegiao = rs.getString("cdRegiao");
        condPagtoLinha.cdLinha = rs.getString("cdLinha");
        condPagtoLinha.nuDiasPrazo = rs.getInt("nuDiasPrazo");
        condPagtoLinha.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condPagtoLinha.cdUsuario = rs.getString("cdUsuario");
        condPagtoLinha.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        return condPagtoLinha;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoLinha condPagtoLinha = (CondPagtoLinha) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("cdEmpresa = ", condPagtoLinha.cdEmpresa);
       	sqlWhereClause.addAndCondition("cdRepresentante = ", condPagtoLinha.cdRepresentante);
       	sqlWhereClause.addAndCondition("cdCondicaoPagamento = ", condPagtoLinha.cdCondicaoPagamento);
       	sqlWhereClause.addAndCondition("cdRegiao = ", condPagtoLinha.cdRegiao);
       	sqlWhereClause.addAndCondition("cdLinha = ", condPagtoLinha.cdLinha);
        sql.append(sqlWhereClause.getSql());
    }
}