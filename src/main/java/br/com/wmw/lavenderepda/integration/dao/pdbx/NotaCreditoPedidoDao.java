package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.NotaCreditoPedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class NotaCreditoPedidoDao extends CrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NotaCreditoPedido();
	}

	private static NotaCreditoPedidoDao instance = null;

    public NotaCreditoPedidoDao() {
        super(NotaCreditoPedido.TABLE_NAME);
    }
    
    public static NotaCreditoPedidoDao getInstance() {
        if (instance == null) {
            instance = new NotaCreditoPedidoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        NotaCreditoPedido notaCreditoPedido = new NotaCreditoPedido();
        notaCreditoPedido.rowKey = rs.getString("rowkey");
        notaCreditoPedido.cdEmpresa = rs.getString("cdEmpresa");
        notaCreditoPedido.cdRepresentante = rs.getString("cdRepresentante");
        notaCreditoPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        notaCreditoPedido.nuPedido = rs.getString("nuPedido");
        notaCreditoPedido.cdNotaCredito = rs.getString("cdNotaCredito");
        notaCreditoPedido.cdUsuario = rs.getString("cdUsuario");
        notaCreditoPedido.nuCarimbo = rs.getInt("nuCarimbo");
        notaCreditoPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        notaCreditoPedido.dtAlteracao = rs.getDate("dtAlteracao");
        notaCreditoPedido.hrAlteracao = rs.getString("hrAlteracao");
        return notaCreditoPedido;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDNOTACREDITO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDNOTACREDITO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        NotaCreditoPedido notaCreditoPedido = (NotaCreditoPedido) domain;
        sql.append(Sql.getValue(notaCreditoPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.cdNotaCredito)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.cdUsuario)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.dtAlteracao)).append(",");
        sql.append(Sql.getValue(notaCreditoPedido.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        NotaCreditoPedido notacreditopedido = (NotaCreditoPedido) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(notacreditopedido.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(notacreditopedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(notacreditopedido.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(notacreditopedido.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(notacreditopedido.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        NotaCreditoPedido notaCreditoPedido = (NotaCreditoPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", notaCreditoPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", notaCreditoPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", notaCreditoPedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", notaCreditoPedido.nuPedido);
		sqlWhereClause.addAndCondition("CDNOTACREDITO = ", notaCreditoPedido.cdNotaCredito);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findCdNotasCreditoUtilizaNoPedido(NotaCreditoPedido notaCreditoPedidoFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT CDNOTACREDITO FROM ").append(tableName);
    	addWhereByExample(notaCreditoPedidoFilter, sql);
    	Vector cdNotaCreditoList = new Vector();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			cdNotaCreditoList.addElement(rs.getString("CDNOTACREDITO"));
    		}
			return cdNotaCreditoList;
		}
    }
    
}