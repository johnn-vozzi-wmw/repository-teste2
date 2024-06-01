package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TabelaPrecoSeg;
import totalcross.sql.ResultSet;

public class TabelaPrecoSegDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabelaPrecoSeg();
	}

    private static TabelaPrecoSegDbxDao instance;

    public TabelaPrecoSegDbxDao() {
        super(TabelaPrecoSeg.TABLE_NAME);
    }

    public static TabelaPrecoSegDbxDao getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoSegDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TabelaPrecoSeg tabelaPrecoSeg = new TabelaPrecoSeg();
        tabelaPrecoSeg.rowKey = rs.getString("rowkey");
        tabelaPrecoSeg.cdEmpresa = rs.getString("cdEmpresa");
        tabelaPrecoSeg.cdRepresentante = rs.getString("cdRepresentante");
        tabelaPrecoSeg.cdSegmento = rs.getString("cdSegmento");
        tabelaPrecoSeg.cdTabelaPreco = rs.getString("cdTabelaPreco");
        tabelaPrecoSeg.nuCarimbo = rs.getInt("nuCarimbo");
        tabelaPrecoSeg.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tabelaPrecoSeg.cdUsuario = rs.getString("cdUsuario");
        return tabelaPrecoSeg;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDSEGMENTO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDSEGMENTO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TabelaPrecoSeg tabelaPrecoSeg = (TabelaPrecoSeg) domain;
        sql.append(Sql.getValue(tabelaPrecoSeg.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tabelaPrecoSeg.cdRepresentante)).append(",");
        sql.append(Sql.getValue(tabelaPrecoSeg.cdSegmento)).append(",");
        sql.append(Sql.getValue(tabelaPrecoSeg.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(tabelaPrecoSeg.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tabelaPrecoSeg.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tabelaPrecoSeg.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TabelaPrecoSeg tabelaPrecoSeg = (TabelaPrecoSeg) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tabelaPrecoSeg.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tabelaPrecoSeg.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tabelaPrecoSeg.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TabelaPrecoSeg tabelaPrecoSeg = (TabelaPrecoSeg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tabelaPrecoSeg.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tabelaPrecoSeg.cdRepresentante);
		sqlWhereClause.addAndCondition("CDSEGMENTO = ", tabelaPrecoSeg.cdSegmento);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", tabelaPrecoSeg.cdTabelaPreco);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public void addTabPrecoSegmentoFilter(ItemTabelaPreco itemTabelaPrecoFilter, StringBuffer sql) {
        sql.append(" AND EXISTS ( SELECT 1 FROM ").append(instance.tableName).append(" SEG ")
            .append(" WHERE SEG.CDEMPRESA = TB.CDEMPRESA")
            .append(" AND SEG.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
            .append(" AND SEG.CDSEGMENTO = ").append(Sql.getValue(itemTabelaPrecoFilter.cdSegmentoFilter)).append(")");
    }

}
