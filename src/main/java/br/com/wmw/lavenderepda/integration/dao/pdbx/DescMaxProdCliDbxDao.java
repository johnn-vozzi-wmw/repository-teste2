package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescMaxProdCli;
import totalcross.sql.ResultSet;

public class DescMaxProdCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescMaxProdCli();
	}

    private static DescMaxProdCliDbxDao instance;

    public DescMaxProdCliDbxDao() {
        super(DescMaxProdCli.TABLE_NAME); 
    }
    
    public static DescMaxProdCliDbxDao getInstance() {
        if (instance == null) {
            instance = new DescMaxProdCliDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        DescMaxProdCli descMaxProdCli = new DescMaxProdCli();
        descMaxProdCli.rowKey = rs.getString("rowkey");
        descMaxProdCli.cdEmpresa = rs.getString("cdEmpresa");
        descMaxProdCli.cdRepresentante = rs.getString("cdRepresentante");
        descMaxProdCli.cdGrupoDescMaxProd = rs.getString("cdGrupoDescMaxProd");
        descMaxProdCli.cdCliente = rs.getString("cdCliente");
        descMaxProdCli.vlPctDescMax = ValueUtil.round(rs.getDouble("vlPctDescMax"));
        descMaxProdCli.cdUsuario = rs.getString("cdUsuario");
        descMaxProdCli.nuCarimbo = rs.getInt("nuCarimbo");
        descMaxProdCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return descMaxProdCli;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPODESCMAXPROD,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLPCTDESCMAX,");
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
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPODESCMAXPROD,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLPCTDESCMAX,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        DescMaxProdCli descMaxProdCli = (DescMaxProdCli) domain;
        sql.append(Sql.getValue(descMaxProdCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descMaxProdCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descMaxProdCli.cdGrupoDescMaxProd)).append(",");
        sql.append(Sql.getValue(descMaxProdCli.cdCliente)).append(",");
        sql.append(Sql.getValue(descMaxProdCli.vlPctDescMax)).append(",");
        sql.append(Sql.getValue(descMaxProdCli.cdUsuario)).append(",");
        sql.append(Sql.getValue(descMaxProdCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(descMaxProdCli.flTipoAlteracao)).append(",");
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        DescMaxProdCli descMaxProdCli = (DescMaxProdCli) domain;
        sql.append(" VLPCTDESCMAX = ").append(Sql.getValue(descMaxProdCli.vlPctDescMax)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descMaxProdCli.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(descMaxProdCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descMaxProdCli.flTipoAlteracao)).append(",");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        DescMaxProdCli descMaxProdCli = (DescMaxProdCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descMaxProdCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descMaxProdCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDGRUPODESCMAXPROD = ", descMaxProdCli.cdGrupoDescMaxProd);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", descMaxProdCli.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}