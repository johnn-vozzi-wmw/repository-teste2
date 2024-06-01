package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MarcadorPedido;
import totalcross.sql.ResultSet;

public class MarcadorPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MarcadorPedido();
	}

    private static MarcadorPedidoDbxDao instance;

    public MarcadorPedidoDbxDao() {
        super(MarcadorPedido.TABLE_NAME); 
    }
    
    public static MarcadorPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new MarcadorPedidoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        MarcadorPedido marcadorpedido = new MarcadorPedido();
        marcadorpedido.rowKey = rs.getString("rowkey");
        marcadorpedido.cdEmpresa = rs.getString("cdEmpresa");
        marcadorpedido.cdRepresentante = rs.getString("cdRepresentante");
        marcadorpedido.flOrigemPedido = rs.getString("flOrigemPedido");
        marcadorpedido.nuPedido = rs.getString("nuPedido");
        marcadorpedido.cdMarcador = rs.getString("cdMarcador");
        marcadorpedido.nuCarimbo = rs.getInt("nuCarimbo");
        marcadorpedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        marcadorpedido.cdUsuario = rs.getString("cdUsuario");
        return marcadorpedido;
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDMARCADOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDMARCADOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        MarcadorPedido marcadorpedido = (MarcadorPedido) domain;
        sql.append(Sql.getValue(marcadorpedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(marcadorpedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(marcadorpedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(marcadorpedido.nuPedido)).append(",");
        sql.append(Sql.getValue(marcadorpedido.cdMarcador)).append(",");
        sql.append(Sql.getValue(marcadorpedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(marcadorpedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(marcadorpedido.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        MarcadorPedido marcadorpedido = (MarcadorPedido) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(marcadorpedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(marcadorpedido.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(marcadorpedido.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MarcadorPedido marcadorpedido = (MarcadorPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", marcadorpedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", marcadorpedido.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", marcadorpedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", marcadorpedido.nuPedido);
		sqlWhereClause.addAndCondition("CDMARCADOR = ", marcadorpedido.cdMarcador);
		sql.append(sqlWhereClause.getSql());
    }
    
}
