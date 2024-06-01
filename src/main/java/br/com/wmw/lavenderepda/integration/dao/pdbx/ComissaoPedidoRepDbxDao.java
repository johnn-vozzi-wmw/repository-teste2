package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ComissaoPedidoRep;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class ComissaoPedidoRepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ComissaoPedidoRep();
	}

    private static ComissaoPedidoRepDbxDao instance;

    public ComissaoPedidoRepDbxDao() {
        super(ComissaoPedidoRep.TABLE_NAME); 
    }
    
    public static ComissaoPedidoRepDbxDao getInstance() {
        return (instance == null) ? instance = new ComissaoPedidoRepDbxDao() : instance;
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    @Override
    protected void addInsertColumns(StringBuffer sql) {}

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {}

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {}
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) { return null; }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        ComissaoPedidoRep comissaoPedidoRep = new ComissaoPedidoRep();
		comissaoPedidoRep.rowKey = rs.getString("rowkey");
        comissaoPedidoRep.cdEmpresa = rs.getString("cdEmpresa");
        comissaoPedidoRep.cdRepresentante = rs.getString("cdRepresentante");
        comissaoPedidoRep.cdTabelaPreco = rs.getString("cdTabelaPreco");
        comissaoPedidoRep.cdComissaoPedidoRep = rs.getInt("cdComissaopedidorep");
        comissaoPedidoRep.dsComissaoPedidoRep = rs.getString("dsComissaopedidorep");
        comissaoPedidoRep.vlPctMaxComissao = ValueUtil.round(rs.getDouble("vlPctmaxcomissao"));
        comissaoPedidoRep.cdCorFaixa = rs.getString("cdCorFaixa");
        comissaoPedidoRep.cdUsuario = rs.getString("cdUsuario");
        comissaoPedidoRep.corSistema = new CorSistema();
        comissaoPedidoRep.corSistema.cdEsquemaCor = rs.getInt("cdEsquemaCor");
        comissaoPedidoRep.corSistema.cdCor = rs.getInt("cdCorFaixa");
        comissaoPedidoRep.corSistema.dsUtilizacao = rs.getString("dsUtilizacao");
        comissaoPedidoRep.corSistema.vlR = rs.getInt("vlR");
        comissaoPedidoRep.corSistema.vlG = rs.getInt("vlG");
        comissaoPedidoRep.corSistema.vlB = rs.getInt("vlB");
        return comissaoPedidoRep;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDTABELAPRECO,");
        sql.append(" tb.CDCOMISSAOPEDIDOREP,");
        sql.append(" tb.DSCOMISSAOPEDIDOREP,");
        sql.append(" tb.VLPCTMAXCOMISSAO,");
        sql.append(" tb.CDCORFAIXA,");
        sql.append(" CORSISTEMA.CDESQUEMACOR,");
        sql.append(" CORSISTEMA.DSUTILIZACAO,");
        sql.append(" CORSISTEMA.VLR,");
        sql.append(" CORSISTEMA.VLG,");
        sql.append(" CORSISTEMA.VLB,");
        sql.append(" tb.CDUSUARIO");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ComissaoPedidoRep comissaoPedidoRep = (ComissaoPedidoRep) domain;
		SqlWhereClause sqlWhereClause = getSqlWhereClauseByExample(comissaoPedidoRep);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoin(domainFilter, sql);
    	try {
			TemaSistema temaAtual = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
			if (temaAtual == null) return;
			 ComissaoPedidoRep comissaoPedidoRep = (ComissaoPedidoRep) domainFilter;
			 comissaoPedidoRep.temaAtual = temaAtual;
			sql.append(" JOIN TBLVPCORSISTEMA CORSISTEMA ON ")
			.append(" CORSISTEMA.CDESQUEMACOR = ").append(temaAtual.cdEsquemaCor)
			.append(" AND CORSISTEMA.CDCOR = TB.CDCORFAIXA");
    	} catch (SQLException e) {
    		ExceptionUtil.handle(e);
    	}
    }
    
	private SqlWhereClause getSqlWhereClauseByExample(ComissaoPedidoRep comissaoPedidoRep) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", comissaoPedidoRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", comissaoPedidoRep.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", comissaoPedidoRep.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDCOMISSAOPEDIDOREP = ", comissaoPedidoRep.cdComissaoPedidoRep);
		return sqlWhereClause;
	}
    
    public ComissaoPedidoRep findComissaoByPercentual(ComissaoPedidoRep comissaoPedidoRepFilter) {
    	try {
    		if (comissaoPedidoRepFilter == null) return null;
	    	StringBuffer sql = getSqlComissaoPedidoRepByPercentual(comissaoPedidoRepFilter);
	    	try (Statement st = getCurrentDriver().getStatement();
	    			ResultSet rs = st.executeQuery(sql.toString())) {
	        	return rs.next() ? (ComissaoPedidoRep) populateComissaoPercentual(rs, comissaoPedidoRepFilter) : null;
	        }
    	} catch (SQLException sqlEx) {
    		ExceptionUtil.handle(sqlEx);
    		return null;
    	}
    }

	private ComissaoPedidoRep populateComissaoPercentual(ResultSet rs, ComissaoPedidoRep comissaoPedidoRepFilter) {
		try {
			ComissaoPedidoRep comissaoPedidoRep = (ComissaoPedidoRep) populate(comissaoPedidoRepFilter, rs);
			comissaoPedidoRep.temaAtual = comissaoPedidoRepFilter.temaAtual;
			return comissaoPedidoRep;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return null;
		}
	}

	private StringBuffer getSqlComissaoPedidoRepByPercentual(ComissaoPedidoRep comissaoPedidoRepFilter) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ");
		addSelectColumns(comissaoPedidoRepFilter, sql);
		sql.append(" FROM ").append(tableName).append (" tb");
		addJoin(comissaoPedidoRepFilter, sql);
		SqlWhereClause sqlWhereClause = getSqlWhereClauseByExample(comissaoPedidoRepFilter);
		sqlWhereClause.addAndCondition(" VLPCTMAXCOMISSAO >= ", ValueUtil.round(comissaoPedidoRepFilter.vlPctMaxComissao));
		sql.append(sqlWhereClause.getSql());
		sql.append(" ORDER BY VLPCTMAXCOMISSAO ASC LIMIT 1");
		return sql;
	}
    
}