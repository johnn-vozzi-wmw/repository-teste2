package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.HistoricoPedido;
import totalcross.sql.ResultSet;

public class HistoricoPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new HistoricoPedido();
	}

    private static HistoricoPedidoDbxDao instance;

    public HistoricoPedidoDbxDao() {
        super(HistoricoPedido.TABLE_NAME);
    }
    
    public static HistoricoPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new HistoricoPedidoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        HistoricoPedido historicoPedido = new HistoricoPedido();
        historicoPedido.rowKey = rs.getString("rowkey");
        historicoPedido.cdEmpresa = rs.getString("cdEmpresa");
        historicoPedido.cdRepresentante = rs.getString("cdRepresentante");
        historicoPedido.nuPedido = rs.getString("nuPedido");
        historicoPedido.cdStatus = rs.getString("cdStatus");
        historicoPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        historicoPedido.dtAtualizacao = rs.getDate("dtAtualizacao");
        historicoPedido.hrAtualizacao = rs.getString("hrAtualizacao");
        historicoPedido.nuCarimbo = rs.getInt("nuCarimbo");
        historicoPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        historicoPedido.cdUsuario = rs.getString("cdUsuario");
        return historicoPedido;
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
        sql.append(" NUPEDIDO,");
        sql.append(" CDSTATUS,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" DTATUALIZACAO,");
        sql.append(" HRATUALIZACAO,");
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
        sql.append(" NUPEDIDO,");
        sql.append(" CDSTATUS,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" DTATUALIZACAO,");
        sql.append(" HRATUALIZACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        HistoricoPedido historicoPedido = (HistoricoPedido) domain;
        sql.append(Sql.getValue(historicoPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(historicoPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(historicoPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(historicoPedido.cdStatus)).append(",");
        sql.append(Sql.getValue(historicoPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(historicoPedido.dtAtualizacao)).append(",");
        sql.append(Sql.getValue(historicoPedido.hrAtualizacao)).append(",");
        sql.append(Sql.getValue(historicoPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(historicoPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(historicoPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        HistoricoPedido historicoPedido = (HistoricoPedido) domain;
        sql.append(" DTATUALIZACAO = ").append(Sql.getValue(historicoPedido.dtAtualizacao)).append(",");
        sql.append(" HRATUALIZACAO = ").append(Sql.getValue(historicoPedido.hrAtualizacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(historicoPedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(historicoPedido.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(historicoPedido.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        HistoricoPedido historicoPedido = (HistoricoPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", historicoPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", historicoPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", historicoPedido.nuPedido);
		sqlWhereClause.addAndCondition("CDSTATUS = ", historicoPedido.cdStatus);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", historicoPedido.flOrigemPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by dtAtualizacao asc, hrAtualizacao asc");
    }
}