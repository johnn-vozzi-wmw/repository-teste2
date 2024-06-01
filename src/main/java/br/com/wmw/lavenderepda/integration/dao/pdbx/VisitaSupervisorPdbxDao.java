package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.VisitaSupervisor;
import totalcross.sql.ResultSet;

public class VisitaSupervisorPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VisitaSupervisor();
	}

    private static VisitaSupervisorPdbxDao instance;

    public VisitaSupervisorPdbxDao() {
        super(VisitaSupervisor.TABLE_NAME);
    }

    public static VisitaSupervisorPdbxDao getInstance() {
        if (instance == null) {
            instance = new VisitaSupervisorPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
	    VisitaSupervisor visita = new VisitaSupervisor();
	    visita.rowKey = rs.getString("rowkey");
	    visita.cdEmpresa = rs.getString("cdEmpresa");
	    visita.cdVisita = rs.getString("cdVisita");
	    visita.cdSupervisor = rs.getString("cdSupervisor");
	    visita.flOrigemVisita = rs.getString("flOrigemVisita");
	    visita.cdRepresentante = rs.getString("cdRepresentante");
	    visita.cdCliente = rs.getString("cdCliente");
	    visita.dtVisita = rs.getDate("dtVisita");
	    visita.hrVisita = rs.getString("hrVisita");
	    visita.dsObservacao = rs.getString("dsObservacao");
	    visita.qtTempoAtendimento = rs.getString("qtTempoAtendimento");
	    visita.flRepresentantePresente = rs.getString("flRepresentantePresente");
	    return visita;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDVISITA,");
        sql.append(" CDSUPERVISOR,");
        sql.append(" FLORIGEMVISITA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" DTVISITA,");
        sql.append(" HRVISITA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" QTTEMPOATENDIMENTO,");
        sql.append(" FLREPRESENTANTEPRESENTE");
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDVISITA,");
        sql.append(" CDSUPERVISOR,");
        sql.append(" FLORIGEMVISITA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" DTVISITA,");
        sql.append(" HRVISITA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" QTTEMPOATENDIMENTO,");
        sql.append(" FLREPRESENTANTEPRESENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaSupervisor visita = (VisitaSupervisor) domain;
        sql.append(Sql.getValue(visita.cdEmpresa)).append(",");
        sql.append(Sql.getValue(visita.cdVisita)).append(",");
        sql.append(Sql.getValue(visita.cdSupervisor)).append(",");
        sql.append(Sql.getValue(visita.flOrigemVisita)).append(",");
        sql.append(Sql.getValue(visita.cdRepresentante)).append(",");
        sql.append(Sql.getValue(visita.cdCliente)).append(",");
        sql.append(Sql.getValue(visita.dtVisita)).append(",");
        sql.append(Sql.getValue(visita.hrVisita)).append(",");
        sql.append(Sql.getValue(visita.dsObservacao)).append(",");
        sql.append(Sql.getValue(visita.qtTempoAtendimento)).append(",");
        sql.append(Sql.getValue(visita.flRepresentantePresente)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(visita.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(visita.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaSupervisor visita = (VisitaSupervisor) domain;
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(visita.cdRepresentante)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(visita.cdCliente)).append(",");
        sql.append(" DTVISITA = ").append(Sql.getValue(visita.dtVisita)).append(",");
        sql.append(" HRVISITA = ").append(Sql.getValue(visita.hrVisita)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(visita.dsObservacao)).append(",");
        sql.append(" QTTEMPOATENDIMENTO = ").append(Sql.getValue(visita.qtTempoAtendimento)).append(",");
        sql.append(" FLREPRESENTANTEPRESENTE = ").append(Sql.getValue(visita.flRepresentantePresente)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(visita.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(visita.cdUsuario));
    }

    //@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaSupervisor visita = (VisitaSupervisor) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addJoin(" tb, TBLVPCLIENTE c where tb.cdEmpresa = c.cdEmpresa and tb.cdRepresentante = c.cdRepresentante and tb.cdCliente = c.cdCliente ");
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", visita.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDVISITA", visita.cdVisita);
		sqlWhereClause.addAndConditionEquals("CDSUPERVISOR", visita.cdSupervisor);
		sqlWhereClause.addAndConditionEquals("FLORIGEMVISITA", visita.flOrigemVisita);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    //@Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DTVISITA desc, HRVISITA desc ");
    }

    protected void addSelectGridColumns(StringBuffer sql) {
        sql.append(" tb.rowKey,");
        sql.append(" tb.DTVISITA,");
        sql.append(" tb.HRVISITA,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" c.NMRAZAOSOCIAL");
    }

	protected void addOrderByGrid(StringBuffer sql) {
    	sql.append(" order by DTVISITA desc, HRVISITA desc ");
	}
}