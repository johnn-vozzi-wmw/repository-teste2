package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.TranspTipoPed;
import totalcross.sql.ResultSet;
public class TranspTipoPedDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TranspTipoPed();
	}

    private static TranspTipoPedDbxDao instance;

    public TranspTipoPedDbxDao() {
        super(TranspTipoPed.TABLE_NAME);
    }

    public static TranspTipoPedDbxDao getInstance() {
        if (instance == null) {
            instance = new TranspTipoPedDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TranspTipoPed transpTipoPed = new TranspTipoPed();
        transpTipoPed.rowKey = rs.getString("rowkey");
        transpTipoPed.cdEmpresa = rs.getString("cdEmpresa");
        transpTipoPed.cdRepresentante = rs.getString("cdRepresentante");
        transpTipoPed.cdTipoPedido = rs.getString("cdTipoPedido");
        transpTipoPed.cdTransportadora = rs.getString("cdTransportadora");
        transpTipoPed.vlMinPedidoFreteGratis = ValueUtil.round(rs.getDouble("vlMinPedidoFreteGratis"));
        transpTipoPed.vlFreteMinimo = ValueUtil.round(rs.getDouble("vlFreteMinimo"));
        transpTipoPed.flCobraFreteAdicionalPorProd = rs.getString("flCobraFreteAdicionalPorProd");
        transpTipoPed.nuCarimbo = rs.getInt("nuCarimbo");
        transpTipoPed.flTipoAlteracao = rs.getString("flTipoAlteracao");
        transpTipoPed.cdUsuario = rs.getString("cdUsuario");
        return transpTipoPed;
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
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDTRANSPORTADORA,");
        sql.append(" VLMINPEDIDOFRETEGRATIS,");
        sql.append(" VLFRETEMINIMO,");
        sql.append(" FLCOBRAFRETEADICIONALPORPROD,");
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
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDTRANSPORTADORA,");
        sql.append(" VLMINPEDIDOFRETEGRATIS,");
        sql.append(" VLFRETEMINIMO,");
        sql.append(" FLCOBRAFRETEADICIONALPORPROD,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TranspTipoPed transpTipoPed = (TranspTipoPed) domain;
        sql.append(Sql.getValue(transpTipoPed.cdEmpresa)).append(",");
        sql.append(Sql.getValue(transpTipoPed.cdRepresentante)).append(",");
        sql.append(Sql.getValue(transpTipoPed.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(transpTipoPed.cdTransportadora)).append(",");
        sql.append(Sql.getValue(transpTipoPed.vlMinPedidoFreteGratis)).append(",");
        sql.append(Sql.getValue(transpTipoPed.vlFreteMinimo)).append(",");
        sql.append(Sql.getValue(transpTipoPed.flCobraFreteAdicionalPorProd)).append(",");
        sql.append(Sql.getValue(transpTipoPed.nuCarimbo)).append(",");
        sql.append(Sql.getValue(transpTipoPed.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(transpTipoPed.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TranspTipoPed transpTipoPed = (TranspTipoPed) domain;
        sql.append(" VLMINPEDIDOFRETEGRATIS = ").append(Sql.getValue(transpTipoPed.vlMinPedidoFreteGratis)).append(",");
        sql.append(" VLFRETEMINIMO = ").append(Sql.getValue(transpTipoPed.vlFreteMinimo)).append(",");
        sql.append(" FLCOBRAFRETEADICIONALPORPROD = ").append(Sql.getValue(transpTipoPed.flCobraFreteAdicionalPorProd)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(transpTipoPed.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(transpTipoPed.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(transpTipoPed.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TranspTipoPed transpTipoPed = (TranspTipoPed) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", transpTipoPed.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", transpTipoPed.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", transpTipoPed.cdTipoPedido);
		sqlWhereClause.addAndCondition("CDTRANSPORTADORA = ", transpTipoPed.cdTransportadora);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}