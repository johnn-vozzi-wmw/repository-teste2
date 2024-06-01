package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PreferenciaFuncao;
import totalcross.sql.ResultSet;

public class PreferenciaFuncaoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PreferenciaFuncao();
	}

    private static PreferenciaFuncaoDbxDao instance;

    public PreferenciaFuncaoDbxDao() {
        super(PreferenciaFuncao.TABLE_NAME); 
    }
    
    public static PreferenciaFuncaoDbxDao getInstance() {
        if (instance == null) {
            instance = new PreferenciaFuncaoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PreferenciaFuncao preferenciafuncao = new PreferenciaFuncao();
        preferenciafuncao.rowKey = rs.getString(1);
        preferenciafuncao.cdSistema = rs.getInt(2);
        preferenciafuncao.cdPreferencia = rs.getInt(3);
        preferenciafuncao.cdFuncao = rs.getString(4);
        preferenciafuncao.cdUsuario = rs.getString(5);
        preferenciafuncao.nuCarimbo = rs.getInt(6);
        preferenciafuncao.flTipoAlteracao = rs.getString(7);
        preferenciafuncao.dtAlteracao = rs.getDate(8);
        preferenciafuncao.hrAlteracao = rs.getString(9);
        return preferenciafuncao;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDSISTEMA,");
        sql.append(" CDPREFERENCIA,");
        sql.append(" CDFUNCAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDSISTEMA,");
        sql.append(" CDPREFERENCIA,");
        sql.append(" CDFUNCAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        PreferenciaFuncao preferenciafuncao = (PreferenciaFuncao) domain;
        sql.append(Sql.getValue(preferenciafuncao.cdSistema)).append(",");
        sql.append(Sql.getValue(preferenciafuncao.cdPreferencia)).append(",");
        sql.append(Sql.getValue(preferenciafuncao.cdFuncao)).append(",");
        sql.append(Sql.getValue(preferenciafuncao.cdUsuario)).append(",");
        sql.append(Sql.getValue(preferenciafuncao.nuCarimbo)).append(",");
        sql.append(Sql.getValue(preferenciafuncao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(preferenciafuncao.dtAlteracao)).append(",");
        sql.append(Sql.getValue(preferenciafuncao.hrAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        PreferenciaFuncao preferenciafuncao = (PreferenciaFuncao) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(preferenciafuncao.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(preferenciafuncao.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(preferenciafuncao.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(preferenciafuncao.hrAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PreferenciaFuncao preferenciafuncao = (PreferenciaFuncao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDSISTEMA = ", preferenciafuncao.cdSistema);
		sqlWhereClause.addAndCondition("CDPREFERENCIA = ", preferenciafuncao.cdPreferencia);
		sqlWhereClause.addAndCondition("CDFUNCAO = ", preferenciafuncao.cdFuncao);
		sqlWhereClause.addAndCondition("CDUSUARIO = ", preferenciafuncao.cdUsuario);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}