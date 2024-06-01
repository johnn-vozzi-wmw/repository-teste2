package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescontoIcms;
import totalcross.sql.ResultSet;

public class DescontoIcmsDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescontoIcms();
	}

    private static DescontoIcmsDbxDao instance;

    public DescontoIcmsDbxDao() {
        super(DescontoIcms.TABLE_NAME); 
    }
    
    public static DescontoIcmsDbxDao getInstance() {
        if (instance == null) {
            instance = new DescontoIcmsDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        DescontoIcms descontoIcms = new DescontoIcms();
        descontoIcms.rowKey = rs.getString("rowkey");
        descontoIcms.cdEmpresa = rs.getString("cdEmpresa");
        descontoIcms.cdRepresentante = rs.getString("cdRepresentante");
        descontoIcms.vlPctIcms = ValueUtil.round(rs.getDouble("vlPctIcms"));
        descontoIcms.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        descontoIcms.vlPctAcrescimo = ValueUtil.round(rs.getDouble("vlPctAcrescimo"));
        descontoIcms.flTipoAlteracao = rs.getString("flTipoAlteracao");
        descontoIcms.nuCarimbo = rs.getInt("nuCarimbo");
        descontoIcms.cdUsuario = rs.getString("cdUsuario");
        return descontoIcms;
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
        sql.append(" VLPCTICMS,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" VLPCTACRESCIMO,");
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
        DescontoIcms descontoIcms = (DescontoIcms) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descontoIcms.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descontoIcms.cdRepresentante);
		sqlWhereClause.addAndCondition("VLPCTICMS = ", descontoIcms.vlPctIcms);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}