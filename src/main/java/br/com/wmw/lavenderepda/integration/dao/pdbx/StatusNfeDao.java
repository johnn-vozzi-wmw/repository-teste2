package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.StatusNfe;
import totalcross.sql.ResultSet;

public class StatusNfeDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new StatusNfe();
	}

	private static StatusNfeDao instance;

	public StatusNfeDao() {
		super(StatusNfe.TABLE_NAME);
	}

	public static StatusNfeDao getInstance() {
        if (instance == null) {
            instance = new StatusNfeDao();
        }
        return instance;
    }

	protected void addInsertColumns(StringBuffer arg0) { }
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) { }
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) { }

	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" rowkey,");
		sql.append(" cdEmpresa,");
		sql.append(" cdRepresentante,");
		sql.append(" cdStatusNfe,");
		sql.append(" dsStatusNfe,");
		sql.append(" flTipoAlteracao,");
		sql.append(" flBloqueiaImpressao");
	}

	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		StatusNfe statusNfe = (StatusNfe) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", statusNfe.cdEmpresa);
		sqlWhereClause.addAndCondition("cdRepresentante = ", statusNfe.cdRepresentante);
		sqlWhereClause.addAndCondition("cdStatusNfe = ", statusNfe.cdStatusNfe);
		//--
		sql.append(sqlWhereClause.getSql());
	}

	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		StatusNfe statusNfe = new StatusNfe();
		statusNfe.rowKey = rs.getString("rowkey");
		statusNfe.cdEmpresa = rs.getString("cdEmpresa");
		statusNfe.cdRepresentante = rs.getString("cdRepresentante");
		statusNfe.cdStatusNfe = rs.getString("cdStatusNfe");
		statusNfe.dsStatusNfe = rs.getString("dsStatusNfe");
		statusNfe.flTipoAlteracao = rs.getString("flTipoAlteracao");
		statusNfe.flBloqueiaImpressao = rs.getString("flBloqueiaImpressao");
        return statusNfe;
	}

}
