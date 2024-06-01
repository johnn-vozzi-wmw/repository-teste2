package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ValorParametroPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ValorParametro();
	}

    private static ValorParametroPdbxDao instance;

    public ValorParametroPdbxDao() {
        super(ValorParametro.TABLE_NAME);
    }

    public static ValorParametroPdbxDao getInstance() {
        if (instance == null) {
            instance = new ValorParametroPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ValorParametro valorParametro = new ValorParametro();
        valorParametro.rowKey = rs.getString("rowkey");
        valorParametro.cdSistema = rs.getInt("cdSistema");
        valorParametro.cdParametro = rs.getInt("cdParametro");
        valorParametro.nmEntidade = rs.getString("nmEntidade");
        valorParametro.vlChave = rs.getString("vlChave");
        valorParametro.vlParametro = rs.getString("vlParametro");
        valorParametro.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return valorParametro;
    }

	//@Override
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDSISTEMA,");
        sql.append(" CDPARAMETRO,");
        sql.append(" NMENTIDADE,");
        sql.append(" VLCHAVE,");
        sql.append(" VLPARAMETRO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ValorParametro valorParametro = (ValorParametro) domain;
        sql.append(" VLPARAMETRO = ").append(Sql.getValue(valorParametro.vlParametro));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ValorParametro valorParametro = (ValorParametro) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDSISTEMA = ", valorParametro.cdSistema);
		sqlWhereClause.addAndCondition("CDPARAMETRO = ", valorParametro.cdParametro);
		sqlWhereClause.addAndCondition("NMENTIDADE = ", valorParametro.nmEntidade);
		sqlWhereClause.addAndCondition("VLCHAVE = ", valorParametro.vlChave);
		sqlWhereClause.addAndCondition("VLPARAMETRO <> ", valorParametro.vlParametro);
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector getValorParametroLigadoList(ValorParametro valorParametroFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT VLPARAMETRO FROM TBLVPVALORPARAMETRO ");
    	addWhereByExample(valorParametroFilter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector valorParametroLigadoList = new Vector();
			while (rs.next()) {
				ValorParametro valorParametro = new ValorParametro();
				valorParametro.vlParametro = rs.getString("VLPARAMETRO");
				valorParametroLigadoList.addElement(valorParametro);
			}
			return valorParametroLigadoList;
		}
	}

}