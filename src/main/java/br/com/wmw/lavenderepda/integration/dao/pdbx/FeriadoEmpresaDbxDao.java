package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FeriadoEmpresa;
import totalcross.sql.ResultSet;

public class FeriadoEmpresaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FeriadoEmpresa();
	}

    private static FeriadoEmpresaDbxDao instance;

    public FeriadoEmpresaDbxDao() {
        super(FeriadoEmpresa.TABLE_NAME);
    }
    
    public static FeriadoEmpresaDbxDao getInstance() {
        if (instance == null) {
            instance = new FeriadoEmpresaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        FeriadoEmpresa feriadoEmpresa = new FeriadoEmpresa();
        feriadoEmpresa.rowKey = rs.getString("rowkey");
        feriadoEmpresa.cdEmpresa = rs.getString("cdEmpresa");
        feriadoEmpresa.nuDia = rs.getInt("nuDia");
        feriadoEmpresa.nuMes = rs.getInt("nuMes");
        feriadoEmpresa.nuAno = rs.getInt("nuAno");
        feriadoEmpresa.dsFeriado = rs.getString("dsFeriado");
        feriadoEmpresa.nuCarimbo = rs.getInt("nuCarimbo");
        feriadoEmpresa.flTipoAlteracao = rs.getString("flTipoAlteracao");
        feriadoEmpresa.cdUsuario = rs.getString("cdUsuario");
        return feriadoEmpresa;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" NUDIA,");
        sql.append(" NUMES,");
        sql.append(" NUANO,");
        sql.append(" DSFERIADO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
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
        FeriadoEmpresa feriadoEmpresa = (FeriadoEmpresa) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", feriadoEmpresa.cdEmpresa);
		sqlWhereClause.addAndCondition("NUDIA = ", feriadoEmpresa.nuDia);
		sqlWhereClause.addAndCondition("NUMES = ", feriadoEmpresa.nuMes);
		if (feriadoEmpresa.nuAno != 0) {
			sqlWhereClause.addAndCondition("(NUANO = ", feriadoEmpresa.nuAno);
			sqlWhereClause.addOrConditionForced("NUANO = ", 0);
			sqlWhereClause.addEndMultipleCondition();
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}