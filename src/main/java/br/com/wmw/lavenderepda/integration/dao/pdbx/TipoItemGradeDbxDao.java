package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoItemGrade;
import totalcross.sql.ResultSet;

public class TipoItemGradeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoItemGrade();
	}

    private static TipoItemGradeDbxDao instance;

    public TipoItemGradeDbxDao() {
        super(TipoItemGrade.TABLE_NAME);
    }

    public static TipoItemGradeDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoItemGradeDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoItemGrade tipoItemGrade = new TipoItemGrade();
        tipoItemGrade.rowKey = rs.getString("rowkey");
        tipoItemGrade.cdEmpresa = rs.getString("cdEmpresa");
        tipoItemGrade.cdRepresentante = rs.getString("cdRepresentante");
        tipoItemGrade.cdTipoItemGrade = rs.getString("cdTipoItemGrade");
        tipoItemGrade.dsTipoItemGrade = rs.getString("dsTipoItemGrade");
        tipoItemGrade.nuCarimbo = rs.getInt("nuCarimbo");
        tipoItemGrade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoItemGrade.cdUsuario = rs.getString("cdUsuario");
        return tipoItemGrade;
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
        sql.append(" cdTipoItemGrade,");
        sql.append(" dsTipoItemGrade,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" cdTipoItemGrade,");
        sql.append(" dsTipoItemGrade,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoItemGrade tipoItemGrade = (TipoItemGrade) domain;
        sql.append(Sql.getValue(tipoItemGrade.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoItemGrade.cdRepresentante)).append(",");
        sql.append(Sql.getValue(tipoItemGrade.cdTipoItemGrade)).append(",");
        sql.append(Sql.getValue(tipoItemGrade.dsTipoItemGrade)).append(",");
        sql.append(Sql.getValue(tipoItemGrade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoItemGrade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoItemGrade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoItemGrade tipoItemGrade = (TipoItemGrade) domain;
        sql.append(" dsTipoItemGrade = ").append(Sql.getValue(tipoItemGrade.dsTipoItemGrade)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(tipoItemGrade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoItemGrade.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(tipoItemGrade.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoItemGrade tipoItemGrade = (TipoItemGrade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoItemGrade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoItemGrade.cdRepresentante);
		sqlWhereClause.addAndCondition("cdTipoItemGrade = ", tipoItemGrade.cdTipoItemGrade);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}