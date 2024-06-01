package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MargemContribFaixa;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class MargemContribFaixaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MargemContribFaixa();
	}

    private static MargemContribFaixaDbxDao instance;

    public MargemContribFaixaDbxDao() {
        super(MargemContribFaixa.TABLE_NAME); 
    }
    
    public static MargemContribFaixaDbxDao getInstance() {
        if (instance == null) {
            instance = new MargemContribFaixaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        MargemContribFaixa margemContribFaixa = new MargemContribFaixa();
        margemContribFaixa.rowKey = rs.getString("rowkey");
        margemContribFaixa.cdEmpresa = rs.getString("cdEmpresa");
        margemContribFaixa.cdRepresentante = rs.getString("cdRepresentante");
        margemContribFaixa.cdFaixa = rs.getString("cdFaixa");
        margemContribFaixa.dsFaixa = rs.getString("dsFaixa");
        margemContribFaixa.vlPctMargemInicio = ValueUtil.round(rs.getDouble("vlPctMargemInicio"));
        margemContribFaixa.vlPctMargemFim = ValueUtil.round(rs.getDouble("vlPctMargemFim"));
        margemContribFaixa.flBloqueiaPedido = rs.getString("flBloqueiaPedido");
        margemContribFaixa.flAcaoVerba = rs.getString("flAcaoVerba");
        margemContribFaixa.cdUsuario = rs.getString("cdUsuario");
        margemContribFaixa.nuCarimbo = rs.getInt("nuCarimbo");
        margemContribFaixa.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return margemContribFaixa;
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
        sql.append(" CDFAIXA,");
        sql.append(" DSFAIXA,");
        sql.append(" VLPCTMARGEMINICIO,");
        sql.append(" VLPCTMARGEMFIM,");
        sql.append(" FLBLOQUEIAPEDIDO,");
        sql.append(" FLACAOVERBA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDFAIXA,");
        sql.append(" DSFAIXA,");
        sql.append(" VLPCTMARGEMINICIO,");
        sql.append(" VLPCTMARGEMFIM,");
        sql.append(" FLBLOQUEIAPEDIDO,");
        sql.append(" FLACAOVERBA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        MargemContribFaixa margemContribFaixa = (MargemContribFaixa) domain;
        sql.append(Sql.getValue(margemContribFaixa.cdEmpresa)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.cdRepresentante)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.cdFaixa)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.dsFaixa)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.vlPctMargemInicio)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.vlPctMargemFim)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.flBloqueiaPedido)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.flAcaoVerba)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.cdUsuario)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.nuCarimbo)).append(",");
        sql.append(Sql.getValue(margemContribFaixa.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        MargemContribFaixa margemContribFaixa = (MargemContribFaixa) domain;
        sql.append(" DSFAIXA = ").append(Sql.getValue(margemContribFaixa.dsFaixa)).append(",");
        sql.append(" VLPCTMARGEMINICIO = ").append(Sql.getValue(margemContribFaixa.vlPctMargemInicio)).append(",");
        sql.append(" VLPCTMARGEMFIM = ").append(Sql.getValue(margemContribFaixa.vlPctMargemFim)).append(",");
        sql.append(" FLBLOQUEIAPEDIDO = ").append(Sql.getValue(margemContribFaixa.flBloqueiaPedido)).append(",");
        sql.append(" FLACAOVERBA = ").append(Sql.getValue(margemContribFaixa.flAcaoVerba)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(margemContribFaixa.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(margemContribFaixa.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(margemContribFaixa.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MargemContribFaixa margemContribFaixa = (MargemContribFaixa) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", margemContribFaixa.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", margemContribFaixa.cdRepresentante);
		sqlWhereClause.addAndCondition("CDFAIXA = ", margemContribFaixa.cdFaixa);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public MargemContribFaixa findMargemContribuicaoFaixa(double vlPctMargem) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT * FROM ");
    	sql.append(tableName); 
    	sql.append(" WHERE (VLPCTMARGEMINICIO != 0 OR VLPCTMARGEMFIM != 0) "); 
    	sql.append(" AND (").append(Sql.getValue(vlPctMargem)).append(" BETWEEN VLPCTMARGEMINICIO AND VLPCTMARGEMFIM OR ");
    	sql.append(" ((VLPCTMARGEMINICIO = 0 AND ").append(Sql.getValue(vlPctMargem)).append(" <= VLPCTMARGEMFIM) OR (VLPCTMARGEMINICIO <= ").append(Sql.getValue(vlPctMargem)).append(" AND VLPCTMARGEMFIM = 0)))");
    	sql.append(" AND CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
    	sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(MargemContribFaixa.class)));
    	sql.append(" LIMIT 1");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			return (MargemContribFaixa) populate(getBaseDomainDefault(), rs);
    		}
    	}
    	return null;
    }   
    
}