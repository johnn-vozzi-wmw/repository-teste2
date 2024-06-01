package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaVigenciaPedido;
import totalcross.sql.ResultSet;

public class VerbaVigenciaPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaVigenciaPedido();
	}

    private static VerbaVigenciaPedidoDbxDao instance;

    public VerbaVigenciaPedidoDbxDao() {
        super(VerbaVigenciaPedido.TABLE_NAME); 
    }
    
    public static VerbaVigenciaPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaVigenciaPedidoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        VerbaVigenciaPedido verbaVigenciaPedido = new VerbaVigenciaPedido();
        verbaVigenciaPedido.rowKey = rs.getString("rowkey");
        verbaVigenciaPedido.cdEmpresa = rs.getString("cdEmpresa");
        verbaVigenciaPedido.cdRepresentante = rs.getString("cdRepresentante");
        verbaVigenciaPedido.cdMesSaldo = rs.getInt("cdMesSaldo");
        verbaVigenciaPedido.nuPedido = rs.getString("nuPedido");
        verbaVigenciaPedido.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
        verbaVigenciaPedido.cdUsuario = rs.getString("cdUsuario");
        verbaVigenciaPedido.nuCarimbo = rs.getInt("nuCarimbo");
        verbaVigenciaPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return verbaVigenciaPedido;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMESSALDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" VLSALDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMESSALDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" VLSALDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        VerbaVigenciaPedido verbaVigenciaPedido = (VerbaVigenciaPedido) domain;
        sql.append(Sql.getValue(verbaVigenciaPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(verbaVigenciaPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaVigenciaPedido.cdMesSaldo)).append(",");
        sql.append(Sql.getValue(verbaVigenciaPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(verbaVigenciaPedido.vlSaldo)).append(",");
        sql.append(Sql.getValue(verbaVigenciaPedido.cdUsuario)).append(",");
        sql.append(Sql.getValue(verbaVigenciaPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(verbaVigenciaPedido.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        VerbaVigenciaPedido verbaVigenciaPedido = (VerbaVigenciaPedido) domain;
        sql.append(" VLSALDO = ").append(Sql.getValue(verbaVigenciaPedido.vlSaldo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaVigenciaPedido.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(verbaVigenciaPedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaVigenciaPedido.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        VerbaVigenciaPedido verbaVigenciaPedido = (VerbaVigenciaPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", verbaVigenciaPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", verbaVigenciaPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMESSALDO = ", verbaVigenciaPedido.cdMesSaldo);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", verbaVigenciaPedido.nuPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}