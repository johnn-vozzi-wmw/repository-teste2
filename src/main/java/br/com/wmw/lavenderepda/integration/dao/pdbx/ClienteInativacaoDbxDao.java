package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteInativacao;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class ClienteInativacaoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteInativacao();
	}

    private static ClienteInativacaoDbxDao instance;

    public ClienteInativacaoDbxDao() {
        super(ClienteInativacao.TABLE_NAME);
    }
    
    public static ClienteInativacaoDbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteInativacaoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ClienteInativacao cliente = new ClienteInativacao();
        cliente.rowKey = rs.getString("rowkey");
        cliente.cdEmpresa = rs.getString("cdEmpresa");
        cliente.cdRepresentante = rs.getString("cdRepresentante");
        cliente.cdCliente = rs.getString("cdCliente");
        cliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cliente.flCancelado = rs.getString("flCancelado");
        cliente.cdUsuario = rs.getString("cdUsuario");
        return cliente;
    }
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLCANCELADO,");
        sql.append(" CDUSUARIO" );
    }
    
    //@Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
        ClienteInativacao cliente = (ClienteInativacao) domain;
        sql.append(Sql.getValue(cliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(cliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(cliente.cdCliente)).append(",");
        sql.append(Sql.getValue(cliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(cliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(cliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
        ClienteInativacao cliente = (ClienteInativacao) domain;
        sql.append(" CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa)).append(",");
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(cliente.cdRepresentante)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(cliente.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(cliente.flTipoAlteracao)).append(",");
        sql.append(" FLCANCELADO = ").append(Sql.getValue(cliente.flCancelado)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(cliente.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        ClienteInativacao cliente = (ClienteInativacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", cliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cliente.cdRepresentante);
        sqlWhereClause.addAndCondition("CDCLIENTE = ", cliente.cdCliente);
        sqlWhereClause.addAndCondition("FLCANCELADO = ", cliente.flCancelado);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}