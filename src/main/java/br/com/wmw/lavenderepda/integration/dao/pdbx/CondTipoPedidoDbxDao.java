package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondTipoPedido;
import totalcross.sql.ResultSet;

public class CondTipoPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondTipoPedido();
	}

    private static CondTipoPedidoDbxDao instance;

    public CondTipoPedidoDbxDao() {
        super(CondTipoPedido.TABLE_NAME);
    }

    public static CondTipoPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new CondTipoPedidoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondTipoPedido condTipoPedido = new CondTipoPedido();
        condTipoPedido.rowKey = rs.getString("rowkey");
        condTipoPedido.cdEmpresa = rs.getString("cdEmpresa");
        condTipoPedido.cdRepresentante = rs.getString("cdRepresentante");
        condTipoPedido.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condTipoPedido.cdTipoPedido = rs.getString("cdTipoPedido");
        condTipoPedido.nuCarimbo = rs.getInt("nuCarimbo");
        condTipoPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condTipoPedido.cdUsuario = rs.getString("cdUsuario");
        return condTipoPedido;
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
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" nuCarimbo,");
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
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondTipoPedido condTipoPedido = (CondTipoPedido) domain;
        sql.append(Sql.getValue(condTipoPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(condTipoPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(condTipoPedido.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(condTipoPedido.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(condTipoPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(condTipoPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(condTipoPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondTipoPedido condTipoPedido = (CondTipoPedido) domain;
        sql.append(" nuCarimbo = ").append(Sql.getValue(condTipoPedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(condTipoPedido.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(condTipoPedido.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondTipoPedido condTipoPedido = (CondTipoPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condTipoPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condTipoPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", condTipoPedido.cdCondicaoPagamento);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", condTipoPedido.cdTipoPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}