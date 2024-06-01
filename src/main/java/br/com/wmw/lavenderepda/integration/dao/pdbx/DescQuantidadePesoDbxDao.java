package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadePeso;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class DescQuantidadePesoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescQuantidadePeso();
	}

    private static DescQuantidadePesoDbxDao instance = null;
	

    public DescQuantidadePesoDbxDao() {
        super(DescQuantidadePeso.TABLE_NAME);
    }
    
    public static DescQuantidadePesoDbxDao getInstance() {
        if (instance == null) {
            instance = new DescQuantidadePesoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DescQuantidadePeso descQuantidadePeso = new DescQuantidadePeso();
        descQuantidadePeso.rowKey = rs.getString("rowkey");
        descQuantidadePeso.cdEmpresa = rs.getString("cdEmpresa");
        descQuantidadePeso.cdRepresentante = rs.getString("cdRepresentante");
        descQuantidadePeso.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        descQuantidadePeso.vlPeso = ValueUtil.round(rs.getDouble("vlPeso"));
        descQuantidadePeso.cdUsuario = rs.getString("cdUsuario");
        descQuantidadePeso.cdTabelaPreco = rs.getString("cdTabelaPreco");
        descQuantidadePeso.flTipoAlteracao = rs.getString("flTipoAlteracao");
        descQuantidadePeso.nuCarimbo = rs.getInt("nuCarimbo");
        if(LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
        	descQuantidadePeso.cdTabelaPreco = rs.getString("cdTabelaPreco");
        }
        return descQuantidadePeso;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.VLPCTDESCONTO,");
        sql.append(" tb.VLPESO,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        if(LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
        	sql.append(" tb.CDTABELAPRECO,");
        }
        sql.append(" tb.NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" VLPESO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) domain;
        sql.append(Sql.getValue(descQuantidadePeso.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descQuantidadePeso.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descQuantidadePeso.vlPctDesconto)).append(",");
        sql.append(Sql.getValue(descQuantidadePeso.vlPeso)).append(",");
        sql.append(Sql.getValue(descQuantidadePeso.cdUsuario)).append(",");
        sql.append(Sql.getValue(descQuantidadePeso.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(descQuantidadePeso.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) domain;
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(descQuantidadePeso.vlPctDesconto)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descQuantidadePeso.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descQuantidadePeso.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(descQuantidadePeso.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", descQuantidadePeso.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", descQuantidadePeso.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.VLPESO = ", descQuantidadePeso.vlPeso);
		if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
			if (!ValueUtil.isEmpty(descQuantidadePeso.cdTabelaPreco)) {
				sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", descQuantidadePeso.cdTabelaPreco);
			}
		}
		if (LavenderePdaConfig.usaTabelaPrecoPorCliente && !ValueUtil.valueEquals(descQuantidadePeso.cdTabelaPreco,"0")) {
			sqlWhereClause.addAndCondition("tpcli.cdcliente = '"+SessionLavenderePda.getCliente().cdCliente+"'");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) domainFilter;
		if (LavenderePdaConfig.usaTabelaPrecoPorCliente && !ValueUtil.valueEquals(descQuantidadePeso.cdTabelaPreco,"0")) {
			sql.append(" JOIN TBLVPTABELAPRECOCLI tpCli on tpCli.CDTABELAPRECO = tb.CDTABELAPRECO ");
		}
	}
    
    public double findVlPctDesconto(BaseDomain domain) throws SQLException {
    	DescQuantidadePeso descQuantidadePeso = (DescQuantidadePeso) domain;
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT VLPCTDESCONTO FROM ");
    	sql.append(tableName);
    	sql.append(" WHERE CDEMPRESA = ").append(descQuantidadePeso.cdEmpresa);
    	sql.append(" AND CDREPRESENTANTE = ").append(descQuantidadePeso.cdRepresentante);
    	sql.append(" AND VLPESO <= ").append(descQuantidadePeso.vlPeso);
    	sql.append(" ORDER BY VLPESO DESC LIMIT 1 ");
    	return getDouble(sql.toString());
    }

	public DescQuantidadePeso findByExample(DescQuantidadePeso descQuantidadePeso) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT ");
    	addSelectColumns(descQuantidadePeso, sql);
    	sql.append(" FROM ");
    	sql.append(tableName +" tb ");
    	addJoin(descQuantidadePeso, sql);
    	addWhereByExample(descQuantidadePeso, sql);
    	try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
		    	return (DescQuantidadePeso) populate(descQuantidadePeso, rs);
			}
		}
		return null;
	}
	
	public Vector loadTabelaPrecoDescQuantidadePeso() throws SQLException {
		Vector result = new Vector();
		StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT DISTINCT(tb.CDTABELAPRECO) FROM ");
    	sql.append(tableName+ " tb ");
		if (LavenderePdaConfig.usaTabelaPrecoPorCliente) {
			sql.append(" JOIN TBLVPTABELAPRECOCLI tpCli on tpCli.CDTABELAPRECO = tb.CDTABELAPRECO AND tpcli.cdcliente = '"+SessionLavenderePda.getCliente().cdCliente+"'");
		}
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				result.addElement(rs.getString("CDTABELAPRECO"));
			}
			return result;
		}
	}

}
