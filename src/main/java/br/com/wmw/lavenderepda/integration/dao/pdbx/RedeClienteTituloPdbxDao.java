package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RedeClienteTitulo;
import totalcross.sql.ResultSet;

public class RedeClienteTituloPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RedeClienteTitulo();
	}

    private static RedeClienteTituloPdbxDao instance;

    public RedeClienteTituloPdbxDao() {
        super(RedeClienteTitulo.TABLE_NAME);
    }

    public static RedeClienteTituloPdbxDao getInstance() {
        if (instance == null) {
            instance = new RedeClienteTituloPdbxDao();
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
    	RedeClienteTitulo redeClienteTitulo = new RedeClienteTitulo();
        redeClienteTitulo.rowKey = rs.getString("rowkey");
        redeClienteTitulo.cdCliente = rs.getString("cdCliente");
        redeClienteTitulo.nuNf = rs.getString("nuNf");
        redeClienteTitulo.nuSerie = rs.getString("nuSerie");
        redeClienteTitulo.nuTitulo = rs.getString("nuTitulo");
        redeClienteTitulo.nuSubDoc = rs.getString("nuSubDoc");
        redeClienteTitulo.vlNf = ValueUtil.round(rs.getDouble("vlNf"));
        redeClienteTitulo.vlTitulo = ValueUtil.round(rs.getDouble("vlTitulo"));
        redeClienteTitulo.vlPago = ValueUtil.round(rs.getDouble("vlPago"));
        redeClienteTitulo.dtVencimento = rs.getDate("dtVencimento");
        redeClienteTitulo.dsHistorico = rs.getString("dsHistorico");
        redeClienteTitulo.dsObservacao = rs.getString("dsObservacao");
        redeClienteTitulo.dsTipoDocumento = rs.getString("dsTipoDocumento");
        redeClienteTitulo.dsPortador = rs.getString("dsPortador");
        redeClienteTitulo.dsStatus = rs.getString("dsStatus");
        redeClienteTitulo.cdRepresentanteTitulo = rs.getString("cdRepresentanteTitulo");
        return redeClienteTitulo;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUNF,");
        sql.append(" NUSERIE,");
        sql.append(" NUTITULO,");
        sql.append(" NUSUBDOC,");
        sql.append(" VLNF,");
        sql.append(" VLTITULO,");
        sql.append(" VLPAGO,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" DSHISTORICO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DSTIPODOCUMENTO,");
        sql.append(" DSPORTADOR,");
        sql.append(" DSSTATUS,");
        sql.append(" CDREPRESENTANTETITULO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RedeClienteTitulo redeClienteTituloFilter = (RedeClienteTitulo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDCLIENTE = ", redeClienteTituloFilter.cdCliente);
		sqlWhereClause.addAndCondition("NUNF = ", redeClienteTituloFilter.nuNf);
		sqlWhereClause.addAndCondition("NUSERIE = ", redeClienteTituloFilter.nuSerie);
		sqlWhereClause.addAndCondition("NUTITULO = ", redeClienteTituloFilter.nuTitulo);
		sqlWhereClause.addAndCondition("NUSUBDOC = ", redeClienteTituloFilter.nuSubDoc);
		sqlWhereClause.addAndCondition("DTVENCIMENTO < ", redeClienteTituloFilter.dtVencimentoFilter);
		if (redeClienteTituloFilter.filtraSomenteNaoPagos) {
			sqlWhereClause.addAndCondition("VLTITULO > VLPAGO");
		}
		sql.append(sqlWhereClause.getSql());
    }

    @Override
	protected BasePersonDomain populateColumnsDynFixas(ResultSet rs) throws java.sql.SQLException {
		return new RedeClienteTitulo();
	}

}