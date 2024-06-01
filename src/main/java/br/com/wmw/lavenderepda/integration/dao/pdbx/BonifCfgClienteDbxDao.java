package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import totalcross.sql.ResultSet;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.BonifCfgCliente;

public class BonifCfgClienteDbxDao extends LavendereCrudDbxDao {

    private static BonifCfgClienteDbxDao instance;

    public BonifCfgClienteDbxDao() {
        super(BonifCfgCliente.TABLE_NAME); 
    }
    
    public static BonifCfgClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new BonifCfgClienteDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        BonifCfgCliente bonifCfgCliente = new BonifCfgCliente();
        bonifCfgCliente.rowKey = rs.getString("rowkey");
        bonifCfgCliente.cdEmpresa = rs.getString("cdEmpresa");
        bonifCfgCliente.cdBonifCfg = rs.getString("cdBonifCfg");
        bonifCfgCliente.cdCliente = rs.getString("cdCliente");
        bonifCfgCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        bonifCfgCliente.nuCarimbo = rs.getInt("nuCarimbo");
        bonifCfgCliente.cdUsuario = rs.getString("cdUsuario");
        return bonifCfgCliente;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDBONIFCFG,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDBONIFCFG,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        BonifCfgCliente bonifCfgCliente = (BonifCfgCliente) domain;
        sql.append(Sql.getValue(bonifCfgCliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(bonifCfgCliente.cdBonifCfg)).append(",");
        sql.append(Sql.getValue(bonifCfgCliente.cdCliente)).append(",");
        sql.append(Sql.getValue(bonifCfgCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(bonifCfgCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(bonifCfgCliente.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        BonifCfgCliente bonifCfgCliente = (BonifCfgCliente) domain;
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(bonifCfgCliente.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(bonifCfgCliente.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(bonifCfgCliente.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        BonifCfgCliente bonifCfgCliente = (BonifCfgCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", bonifCfgCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDBONIFCFG = ", bonifCfgCliente.cdBonifCfg);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", bonifCfgCliente.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BonifCfgCliente();
	}

}