package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class TransportadoraPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Transportadora();
	}

    private static TransportadoraPdbxDao instance;

    public TransportadoraPdbxDao() {
        super(Transportadora.TABLE_NAME);
    }

    public static TransportadoraPdbxDao getInstance() {
        if (instance == null) {
            instance = new TransportadoraPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Transportadora transportadora = new Transportadora();
        transportadora.rowKey = rs.getString("rowkey");
        transportadora.cdEmpresa = rs.getString("cdEmpresa");
        transportadora.cdRepresentante = rs.getString("cdRepresentante");
        transportadora.cdTransportadora = rs.getString("cdTransportadora");
        transportadora.nmTransportadora = rs.getString("nmTransportadora");
        transportadora.flSomaFrete = rs.getString("flSomaFrete");
        transportadora.flMostraFrete = rs.getString("flMostraFrete");
        transportadora.nuCnpj = rs.getString("nuCnpj");
        transportadora.nuPlaca = rs.getString("nuPlaca");
        return transportadora;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDTRANSPORTADORA,");
        sql.append(" tb.FLSOMAFRETE,");
        sql.append(" tb.FLMOSTRAFRETE,");
        sql.append(" tb.NMTRANSPORTADORA,");
        sql.append(" tb.NUCNPJ,");
        sql.append(" tb.NUPLACA");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Transportadora transportadora = (Transportadora) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", transportadora.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", transportadora.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDTRANSPORTADORA = ", transportadora.cdTransportadora);
		sql.append(sqlWhereClause.getSql());
		
		if (transportadora.transpTipoPedFilter != null) {
			DaoUtil.addExistsTranspTipoPed(sql, transportadora.transpTipoPedFilter);
		}
    }
    
    public String findPlacaPor(String cdRepresenante) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select nuPlaca ");
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        sql.append(" where tb.cdrepresentante = '");
        sql.append(cdRepresenante);
        sql.append("'");
    	return getString(sql.toString());
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	Transportadora transportadora = (Transportadora) domainFilter;
    	if (transportadora.consideraFreteConfig) {
	    	sql.append(" JOIN TBLVPFRETECONFIG FC ON ")
		    	.append(" tb.CDEMPRESA = FC.CDEMPRESA AND ")
		    	.append(" tb.CDREPRESENTANTE = FC.CDREPRESENTANTE AND ")
		    	.append(" tb.CDTRANSPORTADORA = FC.CDTRANSPORTADORA ");
	    	boolean addAnd = false;
	    	if (ValueUtil.isNotEmpty(transportadora.cdRegiao)) {
	    		addAnd = true;
	    		sql.append("AND (");
	    		sql.append(" (FC.CDREGIAO = ").append(Sql.getValue(transportadora.cdRegiao))
	    		.append(" AND FC.FLTIPOTABFRETE = ").append(Sql.getValue(FreteConfig.FL_TIPO_TAB_FRETE_REGIAO)).append(")");
	    	}
	    	if (transportadora.dsCepTratado != 0) {
	    		if (addAnd) {
	    			sql.append(" OR ");
	    		} else {
	    			addAnd = true;
	    			sql.append("AND (");
	    		}
	    		sql.append(" (FC.NUCEPINICIO >= ").append(Sql.getValue(transportadora.dsCepTratado)).append(" AND ")
	    		.append(" FC.NUCEPFIM <= ").append(Sql.getValue(transportadora.dsCepTratado)).append(" AND ")
	    		.append(" FC.FLTIPOTABFRETE = ").append(Sql.getValue(FreteConfig.FL_TIPO_TAB_FRETE_CEP)).append(")");
	    	}
	    	if (addAnd) {
	    		sql.append(")");
	    	}
    	}
    }
}