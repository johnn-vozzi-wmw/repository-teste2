package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondComCondPagto;
import totalcross.sql.ResultSet;

public class CondComCondPagtoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondComCondPagto();
	}

    private static CondComCondPagtoDbxDao instance;

    public CondComCondPagtoDbxDao() {
        super(CondComCondPagto.TABLE_NAME);
    }

    public static CondComCondPagtoDbxDao getInstance() {
        if (instance == null) {
            instance = new CondComCondPagtoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondComCondPagto condComCondPagto = new CondComCondPagto();
        condComCondPagto.rowKey = rs.getString("rowkey");
        condComCondPagto.cdEmpresa = rs.getString("cdEmpresa");
        condComCondPagto.cdRepresentante = rs.getString("cdRepresentante");
        condComCondPagto.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        condComCondPagto.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condComCondPagto.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        condComCondPagto.nuCarimbo = rs.getInt("nuCarimbo");
        condComCondPagto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condComCondPagto.cdUsuario = rs.getString("cdUsuario");
        return condComCondPagto;
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
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" VLINDICEFINANCEIRO,");
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
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondComCondPagto condComCondPagto = (CondComCondPagto) domain;
        sql.append(Sql.getValue(condComCondPagto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(condComCondPagto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(condComCondPagto.cdCondicaoComercial)).append(",");
        sql.append(Sql.getValue(condComCondPagto.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(condComCondPagto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(condComCondPagto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(condComCondPagto.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondComCondPagto condComCondPagto = (CondComCondPagto) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(condComCondPagto.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(condComCondPagto.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(condComCondPagto.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondComCondPagto condComCondPagto = (CondComCondPagto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condComCondPagto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condComCondPagto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCONDICAOCOMERCIAL = ", condComCondPagto.cdCondicaoComercial);
		sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", condComCondPagto.cdCondicaoPagamento);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}