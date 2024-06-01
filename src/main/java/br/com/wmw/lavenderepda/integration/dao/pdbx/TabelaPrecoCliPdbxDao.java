package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoCli;
import totalcross.sql.ResultSet;

public class TabelaPrecoCliPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabelaPrecoCli();
	}

    private static TabelaPrecoCliPdbxDao instance;

    public TabelaPrecoCliPdbxDao() {
        super(TabelaPrecoCli.TABLE_NAME);
    }

    public static TabelaPrecoCliPdbxDao getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoCliPdbxDao();
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
    	TabelaPrecoCli tabelaPrecoCliente = new TabelaPrecoCli();
        tabelaPrecoCliente.rowKey = rs.getString("rowkey");
        tabelaPrecoCliente.cdEmpresa = rs.getString("cdEmpresa");
        tabelaPrecoCliente.cdRepresentante = rs.getString("cdRepresentante");
        tabelaPrecoCliente.cdCliente = rs.getString("cdCliente");
        tabelaPrecoCliente.cdTabelaPreco = rs.getString("cdTabelaPreco");
        return tabelaPrecoCliente;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDTABELAPRECO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TabelaPrecoCli tabelaPrecoCliente = (TabelaPrecoCli) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", tabelaPrecoCliente.cdEmpresa);
       	sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", tabelaPrecoCliente.cdRepresentante);
        sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", tabelaPrecoCliente.cdCliente);
        sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", tabelaPrecoCliente.cdTabelaPreco);
        sql.append(sqlWhereClause.getSql());
    }
    
    public static void addTabPrecoCliFilter(StringBuffer sql, boolean addTabPrecoZero) {
		sql.append(" AND (exists (select 1 from TBLVPTABELAPRECOCLI tabCli ");
		sql.append(" where");
		sql.append(" tabCli.cdEmpresa = tb.cdEmpresa");
		sql.append(" AND tabCli.cdRepresentante = tb.cdRepresentante");
		sql.append(" AND tabCli.cdTabelaPreco = tb.cdTabelaPreco) ");
		sql.append(addTabPrecoZero ? (" OR tb.cdTabelaPreco = 0) ") : (" ) "));
	}

    //@Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
        if (LavenderePdaConfig.isOrdenaTabelasPrecoPorPesoMinimo()) {
            sql.append(" ORDER BY tbpc.QTPESOMIN, tbpc.DSTABELAPRECO ");
        } else {
            super.addOrderBy(sql, domain);
        }
    }

    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
        if (LavenderePdaConfig.isOrdenaTabelasPrecoPorPesoMinimo()) {
            sql.append(" JOIN TBLVPTABELAPRECO tbpc ON ");
            sql.append(" tbpc.CDTABELAPRECO = tb.CDTABELAPRECO ");
        }
    }

	public static void addTabPrecoCliFilterComSegmento(ItemTabelaPreco itemTabelaPrecoFilter, StringBuffer sql) {
		boolean isSupervisor = SessionLavenderePda.isUsuarioSupervisor();
		sql.append(" AND exists (select 1 from TBLVPTABELAPRECOCLI tabCli ")
			.append(" JOIN TBLVPTABELAPRECOSEG seg ON")
			.append(" seg.CDEMPRESA = tabCli.CDEMPRESA")
			.append(" AND seg.CDREPRESENTANTE = tabCli.CDREPRESENTANTE")
			.append(" AND seg.CDTABELAPRECO = tabCli.CDTABELAPRECO")
			.append(" where")
			.append(" tabCli.cdEmpresa = tb.cdEmpresa");
		if (isSupervisor) {
			sql.append(" AND tabCli.cdRepresentante = ").append(Sql.getValue(itemTabelaPrecoFilter.cdRepresentante));
		} else {
			sql.append(" AND tabCli.cdRepresentante = tb.cdRepresentante");
		}
		sql.append(" AND tabCli.cdTabelaPreco = tb.cdTabelaPreco")
			.append(" AND seg.CDSEGMENTO = ").append(Sql.getValue(itemTabelaPrecoFilter.cdSegmentoFilter))
			.append(" AND tabCli.CDCLIENTE = ").append(Sql.getValue(itemTabelaPrecoFilter.cdClienteSegmentoFilter)).append(")");
	}

}
