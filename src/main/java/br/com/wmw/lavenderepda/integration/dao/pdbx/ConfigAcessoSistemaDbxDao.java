package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigAcessoSistema;
import totalcross.sql.ResultSet;

public class ConfigAcessoSistemaDbxDao extends LavendereCrudDbxDao {

    private static ConfigAcessoSistemaDbxDao instance;

    public ConfigAcessoSistemaDbxDao() {
        super(ConfigAcessoSistema.TABLE_NAME); 
    }
    
    public static ConfigAcessoSistemaDbxDao getInstance() {
        if (instance == null) {
            instance = new ConfigAcessoSistemaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ConfigAcessoSistema configAcessoSistema = new ConfigAcessoSistema();
        configAcessoSistema.rowKey = rs.getString("rowkey");
        configAcessoSistema.cdFuncao = rs.getString("cdFuncao");
        configAcessoSistema.nuDiaSemana = rs.getInt("nuDiaSemana");
        configAcessoSistema.hrInicio = rs.getString("hrInicio");
        configAcessoSistema.hrFim = rs.getString("hrFim");
        configAcessoSistema.cdUsuario = rs.getString("cdUsuario");
        configAcessoSistema.nuCarimbo = rs.getInt("nuCarimbo");
        configAcessoSistema.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return configAcessoSistema;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDFUNCAO,");
        sql.append(" NUDIASEMANA,");
        sql.append(" HRINICIO,");
        sql.append(" HRFIM,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDFUNCAO,");
        sql.append(" NUDIASEMANA,");
        sql.append(" HRINICIO,");
        sql.append(" HRFIM,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ConfigAcessoSistema configAcessoSistema = (ConfigAcessoSistema) domain;
        sql.append(Sql.getValue(configAcessoSistema.cdFuncao)).append(",");
        sql.append(Sql.getValue(configAcessoSistema.nuDiaSemana)).append(",");
        sql.append(Sql.getValue(configAcessoSistema.hrInicio)).append(",");
        sql.append(Sql.getValue(configAcessoSistema.hrFim)).append(",");
        sql.append(Sql.getValue(configAcessoSistema.cdUsuario)).append(",");
        sql.append(Sql.getValue(configAcessoSistema.nuCarimbo)).append(",");
        sql.append(Sql.getValue(configAcessoSistema.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ConfigAcessoSistema configAcessoSistema = (ConfigAcessoSistema) domain;
        sql.append(" HRFIM = ").append(Sql.getValue(configAcessoSistema.hrFim)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(configAcessoSistema.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(configAcessoSistema.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(configAcessoSistema.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ConfigAcessoSistema configAcessoSistema = (ConfigAcessoSistema) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDFUNCAO = ", configAcessoSistema.cdFuncao);
		sqlWhereClause.addAndCondition("NUDIASEMANA = ", configAcessoSistema.nuDiaSemana);
		if (ValueUtil.isNotEmpty(configAcessoSistema.hrFilter)) {
			sqlWhereClause.addAndCondition(getBetweenHoursCondition(configAcessoSistema.hrFilter));
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }


	private String getBetweenHoursCondition(String hrFilter) {
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(Sql.getValue(hrFilter));
		sqlCondition.append(" BETWEEN HRINICIO AND HRFIM");
		return sqlCondition.toString();
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ConfigAcessoSistema();
	}
    
}