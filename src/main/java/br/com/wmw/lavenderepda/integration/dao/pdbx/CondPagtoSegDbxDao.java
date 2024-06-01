package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondPagtoSeg;
import totalcross.sql.ResultSet;

public class CondPagtoSegDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondPagtoSeg();
	}

    private static CondPagtoSegDbxDao instance;

    public CondPagtoSegDbxDao() {
        super(CondPagtoSeg.TABLE_NAME);
    }

    public static CondPagtoSegDbxDao getInstance() {
        if (instance == null) {
            instance = new CondPagtoSegDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondPagtoSeg condPagtoSeg = new CondPagtoSeg();
        condPagtoSeg.rowKey = rs.getString("rowkey");
        condPagtoSeg.cdEmpresa = rs.getString("cdEmpresa");
        condPagtoSeg.cdRepresentante = rs.getString("cdRepresentante");
        condPagtoSeg.cdSegmento = rs.getString("cdSegmento");
        condPagtoSeg.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condPagtoSeg.nuCarimbo = rs.getInt("nuCarimbo");
        condPagtoSeg.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condPagtoSeg.cdUsuario = rs.getString("cdUsuario");
        return condPagtoSeg;
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
        sql.append(" CDSEGMENTO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
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
        sql.append(" CDSEGMENTO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoSeg condPagtoSeg = (CondPagtoSeg) domain;
        sql.append(Sql.getValue(condPagtoSeg.cdEmpresa)).append(",");
        sql.append(Sql.getValue(condPagtoSeg.cdRepresentante)).append(",");
        sql.append(Sql.getValue(condPagtoSeg.cdSegmento)).append(",");
        sql.append(Sql.getValue(condPagtoSeg.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(condPagtoSeg.nuCarimbo)).append(",");
        sql.append(Sql.getValue(condPagtoSeg.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(condPagtoSeg.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoSeg condPagtoSeg = (CondPagtoSeg) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(condPagtoSeg.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(condPagtoSeg.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(condPagtoSeg.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoSeg condPagtoSeg = (CondPagtoSeg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condPagtoSeg.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condPagtoSeg.cdRepresentante);
		sqlWhereClause.addAndCondition("CDSEGMENTO = ", condPagtoSeg.cdSegmento);
		sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", condPagtoSeg.cdCondicaoPagamento);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}