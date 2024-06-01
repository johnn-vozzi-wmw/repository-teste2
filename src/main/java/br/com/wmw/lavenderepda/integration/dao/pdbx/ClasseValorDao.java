package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClasseValor;
import totalcross.sql.ResultSet;

public class ClasseValorDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClasseValor();
	}

    private static ClasseValorDao instance;

    public ClasseValorDao() {
        super(ClasseValor.TABLE_NAME);
    }

    public static ClasseValorDao getInstance() {
        if (instance == null) {
            instance = new ClasseValorDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ClasseValor classeValor = new ClasseValor();
        classeValor.rowKey = rs.getString("rowkey");
        classeValor.cdEmpresa = rs.getString("cdEmpresa");
        classeValor.cdRepresentante = rs.getString("cdRepresentante");
        classeValor.cdClasseValor = rs.getString("cdClasseValor");
        classeValor.dsClasseValor = rs.getString("dsClasseValor");
        classeValor.flTipoAlteracao = rs.getString("flTipoAlteracao");
        classeValor.nuCarimbo = rs.getInt("nuCarimbo");
        classeValor.cdUsuario = rs.getString("cdUsuario");
        return classeValor;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLASSEVALOR,");
        sql.append(" DSCLASSEVALOR,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
	}

	//@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	}

	//@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ClasseValor classeValor = (ClasseValor) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", classeValor.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", classeValor.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLASSEVALOR = ", classeValor.cdClasseValor);
		sql.append(sqlWhereClause.getSql());
	}

}