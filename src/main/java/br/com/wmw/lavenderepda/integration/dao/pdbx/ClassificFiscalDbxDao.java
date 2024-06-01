package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClassificFiscal;
import totalcross.sql.ResultSet;

public class ClassificFiscalDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClassificFiscal();
	}

    private static ClassificFiscalDbxDao instance;

    public ClassificFiscalDbxDao() {
        super(ClassificFiscal.TABLE_NAME);
    }

    public static ClassificFiscalDbxDao getInstance() {
        if (instance == null) {
            instance = new ClassificFiscalDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ClassificFiscal classificFiscal = new ClassificFiscal();
        classificFiscal.rowKey = rs.getString("rowkey");
        classificFiscal.cdEmpresa = rs.getString("cdEmpresa");
        classificFiscal.cdRepresentante = rs.getString("cdRepresentante");
        classificFiscal.cdClassificFiscal = rs.getString("cdClassificFiscal");
        classificFiscal.dsClassificFiscal = rs.getString("dsClassificFiscal");
        classificFiscal.nuCarimbo = rs.getInt("nuCarimbo");
        classificFiscal.flTipoAlteracao = rs.getString("flTipoAlteracao");
        classificFiscal.cdUsuario = rs.getString("cdUsuario");
        return classificFiscal;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" cdClassificFiscal,");
        sql.append(" dsClassificFiscal,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" cdClassificFiscal,");
        sql.append(" dsClassificFiscal,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClassificFiscal classificFiscal = (ClassificFiscal) domain;
        sql.append(Sql.getValue(classificFiscal.cdClassificFiscal)).append(",");
        sql.append(Sql.getValue(classificFiscal.dsClassificFiscal)).append(",");
        sql.append(Sql.getValue(classificFiscal.nuCarimbo)).append(",");
        sql.append(Sql.getValue(classificFiscal.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(classificFiscal.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClassificFiscal classificFiscal = (ClassificFiscal) domain;
        sql.append(" dsClassificFiscal = ").append(Sql.getValue(classificFiscal.dsClassificFiscal)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(classificFiscal.nuCarimbo)).append(",");
        sql.append(" flTipoAlteracao = ").append(Sql.getValue(classificFiscal.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(classificFiscal.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClassificFiscal classificFiscal = (ClassificFiscal) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdClassificFiscal = ", classificFiscal.cdClassificFiscal);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}