package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.MotRegistroVisita;
import totalcross.sql.ResultSet;

public class MotRegistroVisitaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotRegistroVisita();
	}

    private static MotRegistroVisitaPdbxDao instance;

    public MotRegistroVisitaPdbxDao() {
        super(MotRegistroVisita.TABLE_NAME);
    }

    public static MotRegistroVisitaPdbxDao getInstance() {
        if (instance == null) {
            instance = new MotRegistroVisitaPdbxDao();
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
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDMOTIVOREGISTROVISITA,");
        sql.append(" DSMOTIVOREGISTROVISITA,");
        sql.append(" FLVISITAPOSITIVADA");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MotRegistroVisita motregistrovisita = new MotRegistroVisita();
        motregistrovisita.rowKey = rs.getString("rowkey");
        motregistrovisita.cdEmpresa = rs.getString("cdEmpresa");
        motregistrovisita.cdMotivoRegistroVisita = rs.getString("cdMotivoregistrovisita");
        motregistrovisita.dsMotivoRegistroVisita = rs.getString("dsMotivoregistrovisita");
        motregistrovisita.flVisitaPositivada = rs.getString("flVisitapositivada");
        return motregistrovisita;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotRegistroVisita motregistrovisita = (MotRegistroVisita) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", motregistrovisita.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMOTIVOREGISTROVISITA = ", motregistrovisita.cdMotivoRegistroVisita);
		sqlWhereClause.addAndCondition("FLVISITAPOSITIVADA = ", motregistrovisita.flVisitaPositivada);
		if (Cliente.TIPO_PROSPECTS.equals(motregistrovisita.flTipoCadastroCliente)) {
			sqlWhereClause.addAndCondition("FLTIPOCADASTROCLIENTE <> ", Cliente.TIPO_CLIENTE);
		} else {
			sqlWhereClause.addAndCondition("FLTIPOCADASTROCLIENTE <> ", Cliente.TIPO_PROSPECTS);
		}
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	if ((domain != null) && ValueUtil.isNotEmpty(domain.sortAtributte)) {
			super.addOrderBy(sql, domain);
		} else {
			sql.append(" order by DSMOTIVOREGISTROVISITA");
		}
    }
}