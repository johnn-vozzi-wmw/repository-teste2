package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteConc;
import totalcross.sql.ResultSet;

public class ClienteConcDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteConc();
	}

    private static ClienteConcDbxDao instance;

    public ClienteConcDbxDao() {
        super(ClienteConc.TABLE_NAME);
    }
    
    public static ClienteConcDbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteConcDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ClienteConc clienteConc = new ClienteConc();
        clienteConc.rowKey = rs.getString("rowkey");
        clienteConc.cdEmpresa = rs.getString("cdEmpresa");
        clienteConc.cdRepresentante = rs.getString("cdRepresentante");
        clienteConc.cdCliente = rs.getString("cdCliente");
        clienteConc.cdConcorrente = rs.getString("cdConcorrente");
        clienteConc.cdUsuario = rs.getString("cdUsuario");
        clienteConc.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteConc.nuCarimbo = rs.getInt("nuCarimbo");
        return clienteConc;
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
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONCORRENTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONCORRENTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteConc clienteConc = (ClienteConc) domain;
        sql.append(Sql.getValue(clienteConc.cdEmpresa)).append(",");
        sql.append(Sql.getValue(clienteConc.cdRepresentante)).append(",");
        sql.append(Sql.getValue(clienteConc.cdCliente)).append(",");
        sql.append(Sql.getValue(clienteConc.cdConcorrente)).append(",");
        sql.append(Sql.getValue(clienteConc.cdUsuario)).append(",");
        sql.append(Sql.getValue(clienteConc.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(clienteConc.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteConc clienteConc = (ClienteConc) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(clienteConc.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(clienteConc.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(clienteConc.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteConc clienteConc = (ClienteConc) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteConc.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteConc.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", clienteConc.cdCliente);
		sqlWhereClause.addAndCondition("CDCONCORRENTE = ", clienteConc.cdConcorrente);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}