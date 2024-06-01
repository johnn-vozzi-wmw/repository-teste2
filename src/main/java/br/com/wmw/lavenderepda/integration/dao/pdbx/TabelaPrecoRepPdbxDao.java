package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoRep;
import totalcross.sql.ResultSet;

public class TabelaPrecoRepPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabelaPrecoRep();
	}

    private static TabelaPrecoRepPdbxDao instance;

    public TabelaPrecoRepPdbxDao() {
        super(TabelaPrecoRep.TABLE_NAME);
    }

    public static TabelaPrecoRepPdbxDao getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoRepPdbxDao();
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
		sql.append(" tb.rowKey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDTABELAPRECO");
		if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			sql.append(", tb.VLPCTCOMISSAO");
		}
	}

	//@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	TabelaPrecoRep tabelaPrecoRep = new TabelaPrecoRep();
        tabelaPrecoRep.rowKey = rs.getString("rowkey");
        tabelaPrecoRep.cdEmpresa = rs.getString("cdEmpresa");
        tabelaPrecoRep.cdRepresentante = rs.getString("cdRepresentante");
        tabelaPrecoRep.cdTabelaPreco = rs.getString("cdTabelaPreco");
        if (LavenderePdaConfig.usaConfigCalculoComissao()) {
			tabelaPrecoRep.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
		}
        return tabelaPrecoRep;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TabelaPrecoRep tabelaPrecoRep = (TabelaPrecoRep) domain;
       	sql.append(" where tb.CDEMPRESA = ").append(Sql.getValue(tabelaPrecoRep.cdEmpresa));
       	if (!ValueUtil.isEmpty(tabelaPrecoRep.cdRepresentante)) {
        	sql.append(" and tb.CDREPRESENTANTE = ").append(Sql.getValue(tabelaPrecoRep.cdRepresentante));
        }
        if (!ValueUtil.isEmpty(tabelaPrecoRep.cdTabelaPreco)) {
        	sql.append(" and tb.CDTABELAPRECO = ").append(Sql.getValue(tabelaPrecoRep.cdTabelaPreco));
        }
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
}