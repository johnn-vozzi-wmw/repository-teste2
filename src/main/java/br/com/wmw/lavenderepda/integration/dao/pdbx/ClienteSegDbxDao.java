package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteSeg;
import totalcross.sql.ResultSet;

public class ClienteSegDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteSeg();
	}

    private static ClienteSegDbxDao instance;

    public ClienteSegDbxDao() {
        super(ClienteSeg.TABLE_NAME);
    }

    public static ClienteSegDbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteSegDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ClienteSeg clienteSeg = new ClienteSeg();
        clienteSeg.rowKey = rs.getString("rowkey");
        clienteSeg.cdEmpresa = rs.getString("cdEmpresa");
        clienteSeg.cdRepresentante = rs.getString("cdRepresentante");
        clienteSeg.cdSegmento = rs.getString("cdSegmento");
        clienteSeg.cdCliente = rs.getString("cdCliente");
        clienteSeg.flDefault = rs.getString("flDefault");
        clienteSeg.nuCarimbo = rs.getInt("nuCarimbo");
        clienteSeg.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteSeg.cdUsuario = rs.getString("cdUsuario");
        return clienteSeg;
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
        sql.append(" CDSEGMENTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLDEFAULT,");
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
        sql.append(" CDSEGMENTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLDEFAULT,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteSeg clienteSeg = (ClienteSeg) domain;
        sql.append(Sql.getValue(clienteSeg.cdEmpresa)).append(",");
        sql.append(Sql.getValue(clienteSeg.cdRepresentante)).append(",");
        sql.append(Sql.getValue(clienteSeg.cdSegmento)).append(",");
        sql.append(Sql.getValue(clienteSeg.cdCliente)).append(",");
        sql.append(Sql.getValue(clienteSeg.flDefault)).append(",");
        sql.append(Sql.getValue(clienteSeg.nuCarimbo)).append(",");
        sql.append(Sql.getValue(clienteSeg.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(clienteSeg.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteSeg clienteSeg = (ClienteSeg) domain;
        sql.append(" FLDEFAULT = ").append(Sql.getValue(clienteSeg.flDefault)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(clienteSeg.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(clienteSeg.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(clienteSeg.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ClienteSeg clienteSeg = (ClienteSeg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteSeg.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteSeg.cdRepresentante);
		sqlWhereClause.addAndCondition("CDSEGMENTO = ", clienteSeg.cdSegmento);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", clienteSeg.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}