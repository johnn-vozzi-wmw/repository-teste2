package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.VisitaPedido;
import totalcross.sql.ResultSet;

public class VisitaPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VisitaPedido();
	}

    private static VisitaPedidoDbxDao instance;

    public VisitaPedidoDbxDao() {
        super(VisitaPedido.TABLE_NAME);
    }
    
    public static VisitaPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new VisitaPedidoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VisitaPedido visitaPedido = new VisitaPedido();
        visitaPedido.rowKey = rs.getString("rowkey");
        visitaPedido.cdEmpresa = rs.getString("cdEmpresa");
        visitaPedido.cdRepresentante = rs.getString("cdRepresentante");
        visitaPedido.nuPedido = rs.getString("nuPedido");
        visitaPedido.cdVisita = rs.getString("cdVisita");
        visitaPedido.nuCarimbo = rs.getInt("nuCarimbo");
        visitaPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        visitaPedido.cdUsuario = rs.getString("cdUsuario");
        return visitaPedido;
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
        sql.append(" CDVISITA,");
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
        sql.append(" CDVISITA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaPedido visitaPedido = (VisitaPedido) domain;
        sql.append(Sql.getValue(visitaPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(visitaPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(visitaPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(visitaPedido.cdVisita)).append(",");
        sql.append(Sql.getValue(visitaPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(visitaPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(visitaPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaPedido visitaPedido = (VisitaPedido) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(visitaPedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(visitaPedido.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(visitaPedido.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaPedido visitaPedido = (VisitaPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", visitaPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", visitaPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", visitaPedido.nuPedido);
		sqlWhereClause.addAndCondition("CDVISITA = ", visitaPedido.cdVisita);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
        VisitaPedido visitaPedido = (VisitaPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", visitaPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", visitaPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", visitaPedido.nuPedido);
		sqlWhereClause.addAndCondition("CDVISITA = ", visitaPedido.cdVisita);
		sql.append(sqlWhereClause.getSql());
    }
    
}