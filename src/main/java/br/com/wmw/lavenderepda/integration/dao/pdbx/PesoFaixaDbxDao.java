package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PesoFaixa;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class PesoFaixaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesoFaixa();
	}

    private static PesoFaixaDbxDao instance;

    public PesoFaixaDbxDao() {
        super(PesoFaixa.TABLE_NAME);
    }
    
    public static PesoFaixaDbxDao getInstance() {
        return (instance == null) ? instance = new PesoFaixaDbxDao() : instance;
    }
    
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    @Override protected void addInsertColumns(StringBuffer sql) {}
    @Override protected void addInsertValues(BaseDomain domain, StringBuffer sql) {}
    @Override protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {}
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) { return null; }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PesoFaixa pesoFaixa = new PesoFaixa();
		pesoFaixa.rowKey = rs.getString("rowkey");
        pesoFaixa.cdEmpresa = rs.getString("cdEmpresa");
        pesoFaixa.dsFaixa = rs.getString("dsFaixa");
        pesoFaixa.qtPeso = rs.getDouble("qtPeso");
	    pesoFaixa.flFaixaIdeal = rs.getString("flFaixaIdeal");
        pesoFaixa.cdCorFaixa = rs.getString("cdCorFaixa");
        pesoFaixa.cdUsuario = rs.getString("cdUsuario");
        pesoFaixa.corSistema = new CorSistema();
        pesoFaixa.corSistema.cdEsquemaCor = rs.getInt("cdEsquemaCor");
        pesoFaixa.corSistema.cdCor = rs.getInt("cdCorFaixa");
        pesoFaixa.corSistema.dsUtilizacao = rs.getString("dsUtilizacao");
        pesoFaixa.corSistema.vlR = rs.getInt("vlR");
        pesoFaixa.corSistema.vlG = rs.getInt("vlG");
        pesoFaixa.corSistema.vlB = rs.getInt("vlB");
        return pesoFaixa;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.QTPESO,");
        sql.append(" tb.DSFAIXA,");
        sql.append(" tb.FLFAIXAIDEAL,");
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
        PesoFaixa pesoFaixa = (PesoFaixa) domain;
		SqlWhereClause sqlWhereClause = getSqlWhereClauseByExample(pesoFaixa);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoin(domainFilter, sql);
    	try {
			TemaSistema temaAtual = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
			if (temaAtual == null) return;
			 PesoFaixa pesoFaixa = (PesoFaixa) domainFilter;
			 pesoFaixa.temaAtual = temaAtual;
			sql.append(" JOIN TBLVPCORSISTEMA CORSISTEMA ON ")
			.append(" CORSISTEMA.CDESQUEMACOR = ").append(temaAtual.cdEsquemaCor)
			.append(" AND CORSISTEMA.CDCOR = TB.CDCORFAIXA");
    	} catch (SQLException e) {
    		ExceptionUtil.handle(e);
    	}
    }
    
	private SqlWhereClause getSqlWhereClauseByExample(PesoFaixa pesoFaixa) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pesoFaixa.cdEmpresa);
		sqlWhereClause.addAndCondition("FLFAIXAIDEAL = ", pesoFaixa.flFaixaIdeal);
		return sqlWhereClause;
	}

    public PesoFaixa findPesoFaixaByPeso(PesoFaixa pesoFaixaFilter) {
    	try {
    		if (pesoFaixaFilter == null) return null;
	    	StringBuffer sql = getSqlPesoFaixaByAtingido(pesoFaixaFilter);
	    	try (Statement st = getCurrentDriver().getStatement();
	    			ResultSet rs = st.executeQuery(sql.toString())) {
	        	return rs.next() ? populatePesoFaixaAtingido(rs, pesoFaixaFilter) : null;
	        }
    	} catch (SQLException sqlEx) {
    		ExceptionUtil.handle(sqlEx);
    		return null;
    	}
    }

	private PesoFaixa populatePesoFaixaAtingido(ResultSet rs, PesoFaixa pesoFaixaFilter) {
		try {
			PesoFaixa pesoFaixa = (PesoFaixa) populate(pesoFaixaFilter, rs);
			pesoFaixa.temaAtual = pesoFaixaFilter.temaAtual;
			return pesoFaixa;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return null;
		}
	}

	private StringBuffer getSqlPesoFaixaByAtingido(PesoFaixa pesoFaixaFilter) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ");
		addSelectColumns(pesoFaixaFilter, sql);
		sql.append(" FROM ").append(tableName).append (" tb");
		addJoin(pesoFaixaFilter, sql);
		SqlWhereClause sqlWhereClause = getSqlWhereClauseByExample(pesoFaixaFilter);
		sqlWhereClause.addAndCondition(" QTPESO <= " + Sql.getValue(ValueUtil.round(pesoFaixaFilter.qtPeso)));
		sql.append(sqlWhereClause.getSql());
		sql.append(" ORDER BY QTPESO DESC LIMIT 1");
		return sql;
	}

}