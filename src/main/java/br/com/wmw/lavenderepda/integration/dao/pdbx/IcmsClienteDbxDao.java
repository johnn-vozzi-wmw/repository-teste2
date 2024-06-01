package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.IcmsCliente;
import totalcross.sql.ResultSet;

public class IcmsClienteDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new IcmsCliente();
	}

    private static IcmsClienteDbxDao instance;

    public IcmsClienteDbxDao() {
        super(IcmsCliente.TABLE_NAME); 
    }
    
    public static IcmsClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new IcmsClienteDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        IcmsCliente icmsCliente = new IcmsCliente();
        icmsCliente.rowKey = rs.getString("rowkey");
        icmsCliente.cdEmpresa = rs.getString("cdEmpresa");
        icmsCliente.cdRepresentante = rs.getString("cdRepresentante");
        icmsCliente.cdRamoAtividade = rs.getString("cdRamoAtividade");
        icmsCliente.cdUf = rs.getString("cdUf");
        icmsCliente.flContribuinte = rs.getString("flContribuinte");
        icmsCliente.flConsumidorFinal = rs.getString("flConsumidorFinal");
        icmsCliente.cdConfigIcmsEspecial = rs.getString("cdConfigIcmsEspecial");
        icmsCliente.vlPctIcms = ValueUtil.round(rs.getDouble("vlPctIcms"));
        icmsCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        icmsCliente.nuCarimbo = rs.getInt("nuCarimbo");
        icmsCliente.cdUsuario = rs.getString("cdUsuario");
        return icmsCliente;
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
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" CDUF,");
        sql.append(" FLCONTRIBUINTE,");
        sql.append(" FLCONSUMIDORFINAL,");
        sql.append(" CDCONFIGICMSESPECIAL,");
        sql.append(" VLPCTICMS,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        IcmsCliente icmsCliente = (IcmsCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", icmsCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", icmsCliente.cdRepresentante);
		sqlWhereClause.addAndConditionOr("CDRAMOATIVIDADE = ", new String[] {icmsCliente.cdRamoAtividade, ValueUtil.VALOR_ZERO});
		sqlWhereClause.addAndConditionOr("CDUF = ", new String[] {icmsCliente.cdUf, ValueUtil.VALOR_ZERO});
		sqlWhereClause.addAndCondition("FLCONTRIBUINTE = ", icmsCliente.flContribuinte);
		sqlWhereClause.addAndCondition("FLCONSUMIDORFINAL = ", icmsCliente.flConsumidorFinal);
		sqlWhereClause.addAndCondition("CDCONFIGICMSESPECIAL = ", icmsCliente.cdConfigIcmsEspecial);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" ORDER BY CASE ")
    	.append(" WHEN CDUF <> '0' AND CDRAMOATIVIDADE <> '0' THEN 1 ")
    	.append(" WHEN (CDUF <> '0' AND CDRAMOATIVIDADE = '0') OR ")
    	.append(" (CDUF = '0' AND CDRAMOATIVIDADE <> '0') THEN 2 ELSE 3 END");
    }
    
}