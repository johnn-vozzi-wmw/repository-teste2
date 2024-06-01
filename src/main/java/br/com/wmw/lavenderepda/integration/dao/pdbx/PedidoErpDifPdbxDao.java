package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PedidoErpDif;
import totalcross.sql.ResultSet;

public class PedidoErpDifPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PedidoErpDif();
	}

    private static PedidoErpDifPdbxDao instance;

    public PedidoErpDifPdbxDao() {
        super(PedidoErpDif.TABLE_NAME);
    }

    public static PedidoErpDifPdbxDao getInstance() {
        if (instance == null) {
            instance = new PedidoErpDifPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { 
    	sql.append(" CDEMPRESA,")
    	.append(" CDREPRESENTANTE,")
    	.append(" FLORIGEMPEDIDO,")
    	.append(" NUPEDIDO,")
    	.append(" CDSTATUSPEDIDO,")
    	.append(" DSOBSERVACAOORG,")
    	.append(" DSOBSERVACAOERP");
    }
    
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	PedidoErpDif pedidoErpDif = (PedidoErpDif) domain;
    	sql.append(Sql.getValue(pedidoErpDif.cdEmpresa)).append(",")
    	.append(Sql.getValue(pedidoErpDif.cdRepresentante)).append(",")
    	.append(Sql.getValue(pedidoErpDif.flOrigemPedido)).append(",")
    	.append(Sql.getValue(pedidoErpDif.nuPedido)).append(",")
    	.append(Sql.getValue(pedidoErpDif.cdStatusPedido)).append(",")
    	.append(Sql.getValue(pedidoErpDif.dsObservacaoorg)).append(",")
    	.append(Sql.getValue(pedidoErpDif.dsObservacaoerp));
    }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDSTATUSPEDIDO,");
        sql.append(" DSOBSERVACAOORG,");
        sql.append(" DSOBSERVACAOERP,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PedidoErpDif pedidoerpdif = new PedidoErpDif();
        pedidoerpdif.rowKey = rs.getString("rowkey");
        pedidoerpdif.cdEmpresa = rs.getString("cdEmpresa");
        pedidoerpdif.cdRepresentante = rs.getString("cdRepresentante");
        pedidoerpdif.flOrigemPedido = rs.getString("flOrigemPedido");
        pedidoerpdif.nuPedido = rs.getString("nuPedido");
        pedidoerpdif.cdStatusPedido = rs.getString("cdStatusPedido");
        pedidoerpdif.dsObservacaoorg = rs.getString("dsObservacaoorg");
        pedidoerpdif.dsObservacaoerp = rs.getString("dsObservacaoerp");
        pedidoerpdif.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pedidoerpdif.cdUsuario = rs.getString("cdUsuario");
        return pedidoerpdif;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoErpDif pedidoerpdif = (PedidoErpDif) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pedidoerpdif.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pedidoerpdif.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pedidoerpdif.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pedidoerpdif.nuPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}