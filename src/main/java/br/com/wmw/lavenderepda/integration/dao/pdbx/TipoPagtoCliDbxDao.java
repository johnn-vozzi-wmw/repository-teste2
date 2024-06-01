package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoPagtoCli;
import totalcross.sql.ResultSet;

public class TipoPagtoCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoPagtoCli();
	}

    private static TipoPagtoCliDbxDao instance;

    public TipoPagtoCliDbxDao() {
        super(TipoPagtoCli.TABLE_NAME);
    }

    public static TipoPagtoCliDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoPagtoCliDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoPagtoCli tipoPagtoCli = new TipoPagtoCli();
        tipoPagtoCli.rowKey = rs.getString("rowkey");
        tipoPagtoCli.cdEmpresa = rs.getString("cdEmpresa");
        tipoPagtoCli.cdRepresentante = rs.getString("cdRepresentante");
        tipoPagtoCli.cdTipoPagamento = rs.getString("cdTipoPagamento");
        tipoPagtoCli.cdCliente = rs.getString("cdCliente");
        tipoPagtoCli.nuCarimbo = rs.getInt("nuCarimbo");
        tipoPagtoCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoPagtoCli.cdUsuario = rs.getString("cdUsuario");
        return tipoPagtoCli;
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
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" CDCLIENTE,");
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
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPagtoCli tipoPagtoCli = (TipoPagtoCli) domain;
        sql.append(Sql.getValue(tipoPagtoCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoPagtoCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(tipoPagtoCli.cdTipoPagamento)).append(",");
        sql.append(Sql.getValue(tipoPagtoCli.cdCliente)).append(",");
        sql.append(Sql.getValue(tipoPagtoCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoPagtoCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoPagtoCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPagtoCli tipoPagtoCli = (TipoPagtoCli) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoPagtoCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoPagtoCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoPagtoCli.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPagtoCli tipoPagtoCli = (TipoPagtoCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoPagtoCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoPagtoCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOPAGAMENTO = ", tipoPagtoCli.cdTipoPagamento);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", tipoPagtoCli.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}