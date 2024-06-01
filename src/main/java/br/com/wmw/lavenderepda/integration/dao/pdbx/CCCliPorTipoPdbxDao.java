package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CCCliPorTipo;
import totalcross.sql.ResultSet;

public class CCCliPorTipoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CCCliPorTipo();
	}

    private static CCCliPorTipoPdbxDao instance;

    public CCCliPorTipoPdbxDao() {
        super(CCCliPorTipo.TABLE_NAME);
    }

    public static CCCliPorTipoPdbxDao getInstance() {
        if (instance == null) {
            instance = new CCCliPorTipoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        CCCliPorTipo cliPorTipo = new CCCliPorTipo();
        cliPorTipo.rowKey = rs.getString("rowkey");
        cliPorTipo.cdEmpresa = rs.getString("cdEmpresa");
        cliPorTipo.cdRepresentante = rs.getString("cdRepresentante");
        cliPorTipo.cdCliente = rs.getString("cdCliente");
        cliPorTipo.nuDocumento = rs.getString("nuDocumento");
        cliPorTipo.vlQuebra = ValueUtil.round(rs.getDouble("vlQuebra"));
        cliPorTipo.vlMortalidade = ValueUtil.round(rs.getDouble("vlMortalidade"));
        cliPorTipo.vlPeso = ValueUtil.round(rs.getDouble("vlPeso"));
        cliPorTipo.dtDocumento = rs.getDate("dtDocumento");
        cliPorTipo.nuPedido = rs.getString("nuPedido");
        cliPorTipo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cliPorTipo.cdUsuario = rs.getString("cdUsuario");
        return cliPorTipo;
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
        sql.append(" nuDocumento,");
        sql.append(" vlQuebra,");
        sql.append(" vlMortalidade,");
        sql.append(" vlPeso,");
        sql.append(" dtDocumento,");
        sql.append(" nuPedido,");
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
        sql.append(" nuDocumento,");
        sql.append(" vlQuebra,");
        sql.append(" vlMortalidade,");
        sql.append(" vlPeso,");
        sql.append(" dtDocumento,");
        sql.append(" nuPedido,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CCCliPorTipo cliPorTipo = (CCCliPorTipo) domain;
        sql.append(Sql.getValue(cliPorTipo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(cliPorTipo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(cliPorTipo.cdCliente)).append(",");
        sql.append(Sql.getValue(cliPorTipo.nuDocumento)).append(",");
        sql.append(Sql.getValue(cliPorTipo.vlQuebra)).append(",");
        sql.append(Sql.getValue(cliPorTipo.vlMortalidade)).append(",");
        sql.append(Sql.getValue(cliPorTipo.vlPeso)).append(",");
        sql.append(Sql.getValue(cliPorTipo.dtDocumento)).append(",");
        sql.append(Sql.getValue(cliPorTipo.nuPedido)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(cliPorTipo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(cliPorTipo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CCCliPorTipo cliPorTipo = (CCCliPorTipo) domain;
        sql.append(" vlQuebra = ").append(Sql.getValue(cliPorTipo.vlQuebra)).append(",");
        sql.append(" vlMortalidade = ").append(Sql.getValue(cliPorTipo.vlMortalidade)).append(",");
        sql.append(" vlPeso = ").append(Sql.getValue(cliPorTipo.vlPeso)).append(",");
        sql.append(" dtDocumento = ").append(Sql.getValue(cliPorTipo.dtDocumento)).append(",");
        sql.append(" nuPedido = ").append(Sql.getValue(cliPorTipo.nuPedido)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(cliPorTipo.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(cliPorTipo.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CCCliPorTipo cliPorTipo = (CCCliPorTipo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", cliPorTipo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cliPorTipo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", cliPorTipo.cdCliente);
		sqlWhereClause.addAndCondition("nuDocumento = ", cliPorTipo.nuDocumento);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", cliPorTipo.nuPedido);
		if (cliPorTipo.usaFindPedidoVazio) {
			sqlWhereClause.addAndConditionForced("NUPEDIDO = ", "");
		}
		sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		sql.append(" order by dtDocumento");
	}

}