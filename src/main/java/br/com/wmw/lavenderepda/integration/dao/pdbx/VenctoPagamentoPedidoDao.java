package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import totalcross.sql.ResultSet;

public class VenctoPagamentoPedidoDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VenctoPagamentoPedido();
	}

    private static VenctoPagamentoPedidoDao instance;
	

    public VenctoPagamentoPedidoDao() {
        super(VenctoPagamentoPedido.TABLE_NAME);
    }
    
    public static VenctoPagamentoPedidoDao getInstance() {
        if (instance == null) {
            instance = new VenctoPagamentoPedidoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	VenctoPagamentoPedido VenctoPagamentoPedido = new VenctoPagamentoPedido();
        VenctoPagamentoPedido.rowKey = rs.getString("rowkey");
        VenctoPagamentoPedido.cdEmpresa = rs.getString("cdEmpresa");
        VenctoPagamentoPedido.cdRepresentante = rs.getString("cdRepresentante");
        VenctoPagamentoPedido.nuPedido = rs.getString("nuPedido");
        VenctoPagamentoPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        VenctoPagamentoPedido.cdPagamentoPedido = rs.getString("cdPagamentoPedido");
        VenctoPagamentoPedido.nuSeqVenctoPagamentoPedido = rs.getInt("nuSeqVenctoPagamentoPedido");
        VenctoPagamentoPedido.dtVencimento = rs.getDate("dtVencimento");
        VenctoPagamentoPedido.vlBoleto = rs.getDouble("vlBoleto");
        VenctoPagamentoPedido.nuCarimbo = rs.getInt("nuCarimbo");
        VenctoPagamentoPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        VenctoPagamentoPedido.cdUsuario = rs.getString("cdUsuario");
        return VenctoPagamentoPedido;
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPAGAMENTOPEDIDO,");
        sql.append(" NUSEQVENCTOPAGAMENTOPEDIDO,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" VLBOLETO,");
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPAGAMENTOPEDIDO,");
        sql.append(" NUSEQVENCTOPAGAMENTOPEDIDO,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" VLBOLETO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VenctoPagamentoPedido venctoPagamentoPedido = (VenctoPagamentoPedido) domain;
        sql.append(Sql.getValue(venctoPagamentoPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.cdPagamentoPedido)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.nuSeqVenctoPagamentoPedido)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.dtVencimento)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.vlBoleto)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(venctoPagamentoPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VenctoPagamentoPedido venctoPagamentoPedido = (VenctoPagamentoPedido) domain;
        sql.append(" DTVENCIMENTO = ").append(Sql.getValue(venctoPagamentoPedido.dtVencimento)).append(",");
        sql.append(" VLBOLETO = ").append(Sql.getValue(venctoPagamentoPedido.vlBoleto)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(venctoPagamentoPedido.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VenctoPagamentoPedido venctoPagamentoPedido = (VenctoPagamentoPedido) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("CDEMPRESA = ", venctoPagamentoPedido.cdEmpresa);
    	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", venctoPagamentoPedido.cdRepresentante);
    	sqlWhereClause.addAndCondition("NUPEDIDO = ", venctoPagamentoPedido.nuPedido);
    	sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", venctoPagamentoPedido.flOrigemPedido);
    	sqlWhereClause.addAndCondition("CDPAGAMENTOPEDIDO = ", venctoPagamentoPedido.cdPagamentoPedido);
    	sqlWhereClause.addAndCondition("NUSEQVENCTOPAGAMENTOPEDIDO = ", venctoPagamentoPedido.nuSeqVenctoPagamentoPedido);
    	//--
    	sql.append(sqlWhereClause.getSql());
    }
 
}