package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PrazoPagtoPreco;
import totalcross.sql.ResultSet;

public class PrazoPagtoPrecoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PrazoPagtoPreco();
	}

    private static PrazoPagtoPrecoDbxDao instance;

    public PrazoPagtoPrecoDbxDao() {
        super(PrazoPagtoPreco.TABLE_NAME);
    }

    public static PrazoPagtoPrecoDbxDao getInstance() {
        if (instance == null) {
            instance = new PrazoPagtoPrecoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PrazoPagtoPreco prazoPagtoPreco = new PrazoPagtoPreco();
        prazoPagtoPreco.rowKey = rs.getString("rowkey");
        prazoPagtoPreco.cdEmpresa = rs.getString("cdEmpresa");
        prazoPagtoPreco.cdRepresentante = rs.getString("cdRepresentante");
        prazoPagtoPreco.cdPrazoPagtoPreco = rs.getInt("cdPrazoPagtoPreco");
        prazoPagtoPreco.nuDiasPrazoPagtoPreco = rs.getInt("nuDiasPrazoPagtoPreco");
        prazoPagtoPreco.nuCarimbo = rs.getInt("nuCarimbo");
        prazoPagtoPreco.flTipoAlteracao = rs.getString("flTipoAlteracao");
        prazoPagtoPreco.cdUsuario = rs.getString("cdUsuario");
        return prazoPagtoPreco;
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
        sql.append(" CDPRAZOPAGTOPRECO,");
        sql.append(" NUDIASPRAZOPAGTOPRECO,");
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
        sql.append(" CDPRAZOPAGTOPRECO,");
        sql.append(" NUDIASPRAZOPAGTOPRECO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PrazoPagtoPreco prazoPagtoPreco = (PrazoPagtoPreco) domain;
        sql.append(Sql.getValue(prazoPagtoPreco.cdEmpresa)).append(",");
        sql.append(Sql.getValue(prazoPagtoPreco.cdRepresentante)).append(",");
        sql.append(Sql.getValue(prazoPagtoPreco.cdPrazoPagtoPreco)).append(",");
        sql.append(Sql.getValue(prazoPagtoPreco.nuDiasPrazoPagtoPreco)).append(",");
        sql.append(Sql.getValue(prazoPagtoPreco.nuCarimbo)).append(",");
        sql.append(Sql.getValue(prazoPagtoPreco.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(prazoPagtoPreco.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PrazoPagtoPreco prazoPagtoPreco = (PrazoPagtoPreco) domain;
        sql.append(" NUDIASPRAZOPAGTOPRECO = ").append(Sql.getValue(prazoPagtoPreco.nuDiasPrazoPagtoPreco)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(prazoPagtoPreco.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(prazoPagtoPreco.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(prazoPagtoPreco.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PrazoPagtoPreco prazoPagtoPreco = (PrazoPagtoPreco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", prazoPagtoPreco.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", prazoPagtoPreco.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRAZOPAGTOPRECO = ", prazoPagtoPreco.cdPrazoPagtoPreco);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}