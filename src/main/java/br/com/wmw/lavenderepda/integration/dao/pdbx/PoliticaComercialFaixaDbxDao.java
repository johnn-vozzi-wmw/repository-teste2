package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PoliticaComercialFaixa;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PoliticaComercialFaixaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PoliticaComercialFaixa();
	}

    private static PoliticaComercialFaixaDbxDao instance;

    public PoliticaComercialFaixaDbxDao() {
        super(PoliticaComercialFaixa.TABLE_NAME); 
    }
    
    public static PoliticaComercialFaixaDbxDao getInstance() {
        if (instance == null) {
            instance = new PoliticaComercialFaixaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PoliticaComercialFaixa politicaComercialFaixa = new PoliticaComercialFaixa();
        politicaComercialFaixa.rowKey = rs.getString("rowkey");
        politicaComercialFaixa.cdEmpresa = rs.getString("cdEmpresa");
        politicaComercialFaixa.cdPoliticaComercial = rs.getString("cdPoliticaComercial");
        politicaComercialFaixa.vlPctPoliticaComercial = ValueUtil.round(rs.getDouble("vlPctPoliticaComercial"));
        politicaComercialFaixa.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
        if (LavenderePdaConfig.isUsaMotivoPendencia()) {
	        politicaComercialFaixa.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
	        politicaComercialFaixa.nuOrdemLiberacao = rs.getInt("nuOrdemLiberacao");
        }
        politicaComercialFaixa.flTipoAlteracao = rs.getString("flTipoAlteracao");
        politicaComercialFaixa.nuCarimbo = rs.getInt("nuCarimbo");
        politicaComercialFaixa.cdUsuario = rs.getString("cdUsuario");
        return politicaComercialFaixa;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDPOLITICACOMERCIAL,");
        sql.append(" VLPCTPOLITICACOMERCIAL,");
        sql.append(" VLPCTCOMISSAO,");
        if (LavenderePdaConfig.isUsaMotivoPendencia()) {
        	sql.append(" CDMOTIVOPENDENCIA,");
            sql.append(" NUORDEMLIBERACAO,");
        }
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDPOLITICACOMERCIAL,");
        sql.append(" VLPCTPOLITICACOMERCIAL,");
        sql.append(" VLPCTCOMISSAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PoliticaComercialFaixa politicaComercialFaixa = (PoliticaComercialFaixa) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", politicaComercialFaixa.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPOLITICACOMERCIAL = ", politicaComercialFaixa.cdPoliticaComercial);
		sqlWhereClause.addAndCondition("VLPCTPOLITICACOMERCIAL = ", politicaComercialFaixa.vlPctPoliticaComercial);
		sql.append(sqlWhereClause.getSql());
    }
    
    public double findPctComissaoByPoliticaComercialItemPedido(PoliticaComercialFaixa politicaComercialFaixa) throws SQLException {
    	if (politicaComercialFaixa == null) return 0;
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT VLPCTCOMISSAO FROM ").append(PoliticaComercialFaixa.TABLE_NAME);
    	addWhere(sql, politicaComercialFaixa);
    	return getDouble(sql.toString());
    }
    
    public PoliticaComercialFaixa findPoliticaComercialFaixa(PoliticaComercialFaixa domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(domain, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereRegraPoliticaComercialFaixa(sql, domain);
    	sql.append(" order by VLPCTPOLITICACOMERCIAL DESC");
    	sql.append(" limit 1");
    	
    	Vector results = findAll(domain, sql.toString());
    	if (ValueUtil.isEmpty(results)) return null;
    	return (PoliticaComercialFaixa) results.items[0];
    }
    
	public PoliticaComercialFaixa findPoliticaComercialFaixaFromItemPedido(PoliticaComercialFaixa politicaComercialFaixa) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDMOTIVOPENDENCIA, NUORDEMLIBERACAO FROM ").append(PoliticaComercialFaixa.TABLE_NAME);
		addWhere(sql, politicaComercialFaixa);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			return new PoliticaComercialFaixa(rs.getString(1), rs.getInt(2));
		}
	}
	
	private void addWhere(StringBuffer sql, PoliticaComercialFaixa politicaComercialFaixa) {
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(politicaComercialFaixa.cdEmpresa));
		sql.append(" AND CDPOLITICACOMERCIAL = ").append(Sql.getValue(politicaComercialFaixa.cdPoliticaComercial));
		sql.append(" AND VLPCTPOLITICACOMERCIAL <= ").append(Sql.getValue(politicaComercialFaixa.vlPctPoliticaComercial));
		sql.append(" ORDER BY VLPCTPOLITICACOMERCIAL DESC");
		sql.append(" LIMIT 1");
		
	}
	

    protected void addWhereRegraPoliticaComercialFaixa(StringBuffer sql, PoliticaComercialFaixa politicaComercialFaixa) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", politicaComercialFaixa.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPOLITICACOMERCIAL = ", politicaComercialFaixa.cdPoliticaComercial);
		sqlWhereClause.addAndCondition("VLPCTPOLITICACOMERCIAL <= ", Sql.getValue(politicaComercialFaixa.vlPctPoliticaComercial));
		sql.append(sqlWhereClause.getSql());
    }
	
}