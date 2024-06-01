package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoPedidoCli;
import totalcross.sql.ResultSet;

public class TipoPedidoCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoPedidoCli();
	}

    private static TipoPedidoCliDbxDao instance;
	

    public TipoPedidoCliDbxDao() {
        super(TipoPedidoCli.TABLE_NAME);
    }
    
    public static TipoPedidoCliDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoPedidoCliDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoPedidoCli tipoPedidoCli = new TipoPedidoCli();
        tipoPedidoCli.rowKey = rs.getString("rowkey");
        tipoPedidoCli.cdEmpresa = rs.getString("cdEmpresa");
        tipoPedidoCli.cdRepresentante = rs.getString("cdRepresentante");
        tipoPedidoCli.cdTipoPedido = rs.getString("cdTipoPedido");
        tipoPedidoCli.cdCliente = rs.getString("cdCliente");
        tipoPedidoCli.nuCarimbo = rs.getInt("nuCarimbo");
        tipoPedidoCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoPedidoCli.cdUsuario = rs.getString("cdUsuario");
        return tipoPedidoCli;
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
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPedidoCli tipoPedidoCli = (TipoPedidoCli) domain;
        sql.append(Sql.getValue(tipoPedidoCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoPedidoCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(tipoPedidoCli.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(tipoPedidoCli.cdCliente)).append(",");
        sql.append(Sql.getValue(tipoPedidoCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoPedidoCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoPedidoCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPedidoCli tipoPedidoCli = (TipoPedidoCli) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoPedidoCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoPedidoCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoPedidoCli.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoPedidoCli tipoPedidoCli = (TipoPedidoCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoPedidoCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoPedidoCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", tipoPedidoCli.cdTipoPedido);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", tipoPedidoCli.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}