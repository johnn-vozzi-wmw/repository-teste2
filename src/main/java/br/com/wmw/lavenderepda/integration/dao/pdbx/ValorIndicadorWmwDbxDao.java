package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.business.domain.ValorIndicadorWmw;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ValorIndicadorWmwDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ValorIndicadorWmw();
	}

    private static ValorIndicadorWmwDbxDao instance;

    public ValorIndicadorWmwDbxDao() {
        super(ValorIndicadorWmw.TABLE_NAME); 
    }
    
    public static ValorIndicadorWmwDbxDao getInstance() {
        if (instance == null) {
            instance = new ValorIndicadorWmwDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        ValorIndicadorWmw valorIndicadorWmw = new ValorIndicadorWmw();
        valorIndicadorWmw.rowKey = rs.getString("rowkey");
        valorIndicadorWmw.cdEmpresa = rs.getString("cdEmpresa");
        valorIndicadorWmw.cdRepresentante = rs.getString("cdRepresentante");
        valorIndicadorWmw.cdIndicador = rs.getString("cdIndicador");
        valorIndicadorWmw.cdApuracao = rs.getString("cdApuracao");
        valorIndicadorWmw.vlIndicador = ValueUtil.round(rs.getDouble("vlIndicador"));
        valorIndicadorWmw.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        valorIndicadorWmw.dtApuracao = rs.getDate("dtApuracao");
        valorIndicadorWmw.hrApuracao = rs.getString("hrApuracao");
        valorIndicadorWmw.nuCarimbo = rs.getInt("nuCarimbo");
        valorIndicadorWmw.cdUsuario = rs.getString("cdUsuario");
        valorIndicadorWmw.flTipoAlteracao = rs.getString("flTipoAlteracao");
        valorIndicadorWmw.dsApuracao = rs.getString("dsApuracao");
        valorIndicadorWmw.dtOrdenacao = rs.getDate("dtOrdenacao");
        return valorIndicadorWmw;
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
        sql.append(" CDINDICADOR,");
        sql.append(" CDAPURACAO,");
        sql.append(" VLINDICADOR,");
        sql.append(" DTAPURACAO,");
        sql.append(" HRAPURACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" VLMETA,");
        sql.append(" DSAPURACAO," );
        sql.append(" DTORDENACAO");
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
        ValorIndicadorWmw valorIndicadorWmw = (ValorIndicadorWmw) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", valorIndicadorWmw.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", valorIndicadorWmw.cdRepresentante);
		sqlWhereClause.addAndCondition("CDINDICADOR = ", valorIndicadorWmw.cdIndicador);
		sqlWhereClause.addAndCondition("CDAPURACAO = ", valorIndicadorWmw.cdApuracao);
		sqlWhereClause.addAndCondition("DSAPURACAO = ", valorIndicadorWmw.dsApuracao);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public Vector findDistinctPeriodoList(BaseDomain domain) throws SQLException {
		ValorIndicadorWmw valorIndicadorFilter = (ValorIndicadorWmw) domain;
		StringBuffer sql = getSqlBuffer();
        sql.append(" select distinct DSAPURACAO FROM ");
        sql.append(tableName);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", valorIndicadorFilter.cdRepresentante);
		sqlWhereClause.addAndCondition(" CDINDICADOR = ", valorIndicadorFilter.cdIndicador);
		sql.append(sqlWhereClause.getSql());
		sql.append( " order by DTORDENACAO ASC ");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
		        result.addElement(rs.getString(1));
			}
			return result;
		}
	}
 
	public Vector findAllValorIndicador(BaseDomain domain) throws SQLException {
		ValorIndicador valorIndicadorFilter = (ValorIndicador) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append(" select tb.*, ind.DSMASCARAFORMATO, ind.DSINDICADOR from ");
		sql.append(tableName);
		sql.append(" tb");
		sql.append(" join TBLVPINDICADORWMW ind on (tb.cdindicador = ind.cdindicador)");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", valorIndicadorFilter.cdEmpresa);
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", valorIndicadorFilter.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.CDINDICADOR = ", valorIndicadorFilter.cdIndicador);
		sqlWhereClause.addAndCondition(" tb.DSAPURACAO = ", valorIndicadorFilter.dsPeriodo);
		sql.append(sqlWhereClause.getSql());
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
				ValorIndicador valorIndicador = new ValorIndicador();
				valorIndicador.rowKey = rs.getString("rowKey");
				valorIndicador.cdEmpresa = rs.getString("cdEmpresa");
				valorIndicador.cdRepresentante = rs.getString("cdRepresentante");
				valorIndicador.dsPeriodo = rs.getString("dsApuracao");
				valorIndicador.cdIndicador = rs.getString("dsIndicador");
				valorIndicador.dsVlIndicador = rs.getString("vlIndicador");
				valorIndicador.dsMascaraFormato = rs.getString("dsMascaraFormato");
				result.addElement(valorIndicador);
			}
			return result;
		}
	}

}