package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FuncaoConfig;
import totalcross.sql.ResultSet;

public class FuncaoConfigDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FuncaoConfig();
	}

    private static FuncaoConfigDbxDao instance;

    public FuncaoConfigDbxDao() {
        super(FuncaoConfig.TABLE_NAME); 
    }
    
    public static FuncaoConfigDbxDao getInstance() {
        if (instance == null) {
            instance = new FuncaoConfigDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        FuncaoConfig funcaoConfig = new FuncaoConfig();
        funcaoConfig.rowKey = rs.getString("rowkey");
        funcaoConfig.cdFuncao = rs.getString("cdFuncao");
        funcaoConfig.vlMinPedido = ValueUtil.round(rs.getDouble("vlMinPedido"));
        funcaoConfig.cdUsuario = rs.getString("cdUsuario");
        funcaoConfig.nuCarimbo = rs.getInt("nuCarimbo");
        funcaoConfig.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return funcaoConfig;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDFUNCAO,");
        sql.append(" VLMINPEDIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDFUNCAO,");
        sql.append(" VLMINPEDIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FuncaoConfig funcaoConfig = (FuncaoConfig) domain;
        sql.append(Sql.getValue(funcaoConfig.cdFuncao)).append(",");
        sql.append(Sql.getValue(funcaoConfig.vlMinPedido)).append(",");
        sql.append(Sql.getValue(funcaoConfig.cdUsuario)).append(",");
        sql.append(Sql.getValue(funcaoConfig.nuCarimbo)).append(",");
        sql.append(Sql.getValue(funcaoConfig.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FuncaoConfig funcaoConfig = (FuncaoConfig) domain;
        sql.append(" VLMINPEDIDO = ").append(Sql.getValue(funcaoConfig.vlMinPedido)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(funcaoConfig.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(funcaoConfig.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(funcaoConfig.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FuncaoConfig funcaoConfig = (FuncaoConfig) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDFUNCAO = ", funcaoConfig.cdFuncao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}