package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoCondPagtoCli;
import totalcross.sql.ResultSet;

public class TipoCondPagtoCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoCondPagtoCli();
	}

    private static TipoCondPagtoCliDbxDao instance;

    public TipoCondPagtoCliDbxDao() {
        super(TipoCondPagtoCli.TABLE_NAME);
    }

    public static TipoCondPagtoCliDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoCondPagtoCliDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoCondPagtoCli tipoCondPagtoCli = new TipoCondPagtoCli();
        tipoCondPagtoCli.rowKey = rs.getString("rowkey");
        tipoCondPagtoCli.cdEmpresa = rs.getString("cdEmpresa");
        tipoCondPagtoCli.cdRepresentante = rs.getString("cdRepresentante");
        tipoCondPagtoCli.cdCliente = rs.getString("cdCliente");
        tipoCondPagtoCli.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        tipoCondPagtoCli.cdTipoPagamento = rs.getString("cdTipoPagamento");
        tipoCondPagtoCli.nuCarimbo = rs.getInt("nuCarimbo");
        tipoCondPagtoCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoCondPagtoCli.cdUsuario = rs.getString("cdUsuario");
        return tipoCondPagtoCli;
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
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" nucarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" nucarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoCondPagtoCli tipoCondPagtoCli = (TipoCondPagtoCli) domain;
        sql.append(Sql.getValue(tipoCondPagtoCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoCondPagtoCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(tipoCondPagtoCli.cdCliente)).append(",");
        sql.append(Sql.getValue(tipoCondPagtoCli.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(tipoCondPagtoCli.cdTipoPagamento)).append(",");
        sql.append(Sql.getValue(tipoCondPagtoCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoCondPagtoCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoCondPagtoCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoCondPagtoCli tipoCondPagtoCli = (TipoCondPagtoCli) domain;
        sql.append(" nucarimbo = ").append(Sql.getValue(tipoCondPagtoCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoCondPagtoCli.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(tipoCondPagtoCli.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoCondPagtoCli tipoCondPagtoCli = (TipoCondPagtoCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoCondPagtoCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoCondPagtoCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", tipoCondPagtoCli.cdCliente);
		sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", tipoCondPagtoCli.cdCondicaoPagamento);
		sqlWhereClause.addAndCondition("CDTIPOPAGAMENTO = ", tipoCondPagtoCli.cdTipoPagamento);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}