package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FormulaCalculo;
import totalcross.sql.ResultSet;

public class FormulaCalculoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FormulaCalculo();
	}

    private static FormulaCalculoDbxDao instance;

    public FormulaCalculoDbxDao() {
        super(FormulaCalculo.TABLE_NAME); 
    }
    
    public static FormulaCalculoDbxDao getInstance() {
        if (instance == null) {
            instance = new FormulaCalculoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        FormulaCalculo formulaCalculo = new FormulaCalculo();
        formulaCalculo.rowKey = rs.getString("rowkey");
        formulaCalculo.cdFormulaCalculo = rs.getString("cdFormulaCalculo");
        formulaCalculo.dsFormulaCalculo = rs.getString("dsFormulaCalculo");
        formulaCalculo.flTipoFormulaCalculo = rs.getString("flTipoFormulaCalculo");
        formulaCalculo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        formulaCalculo.nuCarimbo = rs.getInt("nuCarimbo");
        formulaCalculo.cdUsuario = rs.getString("cdUsuario");
        return formulaCalculo;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDFORMULACALCULO,");
        sql.append(" DSFORMULACALCULO,");
        sql.append(" FLTIPOFORMULACALCULO,");
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
        sql.append(" DSFORMULACALCULO,");
        sql.append(" FLTIPOFORMULACALCULO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FormulaCalculo formulaCalculo = (FormulaCalculo) domain;
        sql.append(Sql.getValue(formulaCalculo.cdFormulaCalculo)).append(",");
        sql.append(Sql.getValue(formulaCalculo.dsFormulaCalculo)).append(",");
        sql.append(Sql.getValue(formulaCalculo.flTipoFormulaCalculo)).append(",");
        sql.append(Sql.getValue(formulaCalculo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(formulaCalculo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(formulaCalculo.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FormulaCalculo formulaCalculo = (FormulaCalculo) domain;
        sql.append(" FLTIPOFORMULACALCULO = ").append(Sql.getValue(formulaCalculo.flTipoFormulaCalculo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(formulaCalculo.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(formulaCalculo.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(formulaCalculo.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FormulaCalculo formulaCalculo = (FormulaCalculo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDFORMULACALCULO = ", formulaCalculo.cdFormulaCalculo);
		sqlWhereClause.addAndCondition("DSFORMULACALCULO = ", formulaCalculo.dsFormulaCalculo);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}