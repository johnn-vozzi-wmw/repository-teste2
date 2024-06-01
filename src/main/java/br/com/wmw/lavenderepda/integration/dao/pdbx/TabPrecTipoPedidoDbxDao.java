package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TabPrecTipoPedido;
import totalcross.sql.ResultSet;

public class TabPrecTipoPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TabPrecTipoPedido();
	}

    private static TabPrecTipoPedidoDbxDao instance;

    public TabPrecTipoPedidoDbxDao() {
        super(TabPrecTipoPedido.TABLE_NAME); 
    }
    
    public static TabPrecTipoPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new TabPrecTipoPedidoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        TabPrecTipoPedido tabPrecTipoPedido = new TabPrecTipoPedido();
        tabPrecTipoPedido.rowKey = rs.getString("rowkey");
        tabPrecTipoPedido.cdEmpresa = rs.getString("cdEmpresa");
        tabPrecTipoPedido.cdRepresentante = rs.getString("cdRepresentante");
        tabPrecTipoPedido.cdTabelaPreco = rs.getString("cdTabelaPreco");
        tabPrecTipoPedido.cdTipoPedido = rs.getString("cdTipoPedido");
        tabPrecTipoPedido.cdUsuario = rs.getString("cdUsuario");
        tabPrecTipoPedido.nuCarimbo = rs.getInt("nuCarimbo");
        tabPrecTipoPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return tabPrecTipoPedido;
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
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        TabPrecTipoPedido tabPrecTipoPedido = (TabPrecTipoPedido) domain;
        sql.append(Sql.getValue(tabPrecTipoPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tabPrecTipoPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(tabPrecTipoPedido.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(tabPrecTipoPedido.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(tabPrecTipoPedido.cdUsuario)).append(",");
        sql.append(Sql.getValue(tabPrecTipoPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tabPrecTipoPedido.flTipoAlteracao)).append(",");
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        TabPrecTipoPedido tabPrecTipoPedido = (TabPrecTipoPedido) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tabPrecTipoPedido.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tabPrecTipoPedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tabPrecTipoPedido.flTipoAlteracao)).append(",");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        TabPrecTipoPedido tabPrecTipoPedido = (TabPrecTipoPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tabPrecTipoPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tabPrecTipoPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", tabPrecTipoPedido.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", tabPrecTipoPedido.cdTipoPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}