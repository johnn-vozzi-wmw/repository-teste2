package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FormulaCalculoSql;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class FormulaCalculoSqlDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FormulaCalculoSql();
	}

    private static FormulaCalculoSqlDbxDao instance;

    public FormulaCalculoSqlDbxDao() {
        super(FormulaCalculoSql.TABLE_NAME); 
    }
    
    public static FormulaCalculoSqlDbxDao getInstance() {
        if (instance == null) {
            instance = new FormulaCalculoSqlDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        FormulaCalculoSql formulaCalculoSql = new FormulaCalculoSql();
        formulaCalculoSql.rowKey = rs.getString("rowkey");
        formulaCalculoSql.cdFormulaCalculo = rs.getString("cdFormulaCalculo");
        formulaCalculoSql.cdFormulaCalculoSql = rs.getString("cdFormulaCalculoSql");
        formulaCalculoSql.dsSql = rs.getString("dsSql");
        formulaCalculoSql.flTipoFormulaCalculo = rs.getString("flTipoFormulaCalculo");
        formulaCalculoSql.nuOrdem = rs.getInt("nuOrdem");
        formulaCalculoSql.flTipoAlteracao = rs.getString("flTipoAlteracao");
        formulaCalculoSql.nuCarimbo = rs.getInt("nuCarimbo");
        formulaCalculoSql.cdUsuario = rs.getString("cdUsuario");
        formulaCalculoSql.flTipoSistema= rs.getString("flTipoSistema");
        return formulaCalculoSql;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDFORMULACALCULO,");
        sql.append(" CDFORMULACALCULOSQL,");
        sql.append(" FLTIPOSISTEMA, ");
        sql.append(" DSSQL,");
        sql.append(" FLTIPOFORMULACALCULO,");
        sql.append(" NUORDEM,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDFORMULACALCULO,");
        sql.append(" CDFORMULACALCULOSQL,");
        sql.append(" FLTIPOSISTEMA,");
        sql.append(" DSSQL,");
        sql.append(" FLTIPOFORMULACALCULO,");
        sql.append(" NUORDEM,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FormulaCalculoSql formulaCalculoSql = (FormulaCalculoSql) domain;
        sql.append(Sql.getValue(formulaCalculoSql.cdFormulaCalculo)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.cdFormulaCalculoSql)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.flTipoSistema)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.dsSql)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.flTipoFormulaCalculo)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.nuOrdem)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.nuCarimbo)).append(",");
        sql.append(Sql.getValue(formulaCalculoSql.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FormulaCalculoSql formulaCalculoSql = (FormulaCalculoSql) domain;
        sql.append(" DSSQL = ").append(Sql.getValue(formulaCalculoSql.dsSql)).append(",");
        sql.append(" FLTIPOFORMULACALCULO = ").append(Sql.getValue(formulaCalculoSql.flTipoFormulaCalculo)).append(",");
        sql.append(" NUORDEM = ").append(Sql.getValue(formulaCalculoSql.nuOrdem)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(formulaCalculoSql.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(formulaCalculoSql.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(formulaCalculoSql.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FormulaCalculoSql formulaCalculoSql = (FormulaCalculoSql) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDFORMULACALCULO = ", formulaCalculoSql.cdFormulaCalculo);
		sqlWhereClause.addAndCondition("CDFORMULACALCULOSQL = ", formulaCalculoSql.cdFormulaCalculoSql);
		sqlWhereClause.addAndCondition("FLTIPOSISTEMA = ", formulaCalculoSql.flTipoSistema);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	domain.sortAtributte = "nuOrdem";
    	domain.sortAsc = "S";
    	super.addOrderBy(sql, domain);
    }
    

    public Map<String, Object> executaFormulaSql(String sql) throws SQLException {
    	if (sql.length() < 5 || !sql.substring(0, 6).equalsIgnoreCase("select")) {
    		throw new ValidationException("Sql não inicia com 'select'! " + sql);
    	}
    	HashMap<String, Object> result = new HashMap<>();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql)) {
	        if (rs.next()) {
	        	for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
	        		result.put(rs.getMetaData().getColumnName(i), rs.getString(rs.getMetaData().getColumnName(i)));
				}
	        }
	        return result;
        }
    }
    
    public String[] getOperandos(String[] operandos) throws SQLException {
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery("select ROUND(" + operandos[0] + ", 7) as operando0, ROUND(" + operandos[1] + ", 7) as operando1")) {
	        if (rs.next()) {
	        	operandos[0] = rs.getString(1);
	        	operandos[1] = rs.getString(2);
	    		return operandos;
	        }
        }
		return null;
	}
}