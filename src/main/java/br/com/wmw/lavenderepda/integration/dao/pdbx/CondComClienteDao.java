package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondComCliente;
import totalcross.sql.ResultSet;

public class CondComClienteDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondComCliente();
	}

    private static CondComClienteDao instance;

    public CondComClienteDao() {
        super(CondComCliente.TABLE_NAME);
    }
    
    public static CondComClienteDao getInstance() {
        if (instance == null) {
            instance = new CondComClienteDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondComCliente condComCliente = new CondComCliente();
        condComCliente.rowKey = rs.getString("rowkey");
        condComCliente.cdEmpresa = rs.getString("cdEmpresa");
        condComCliente.cdRepresentante = rs.getString("cdRepresentante");
        condComCliente.cdCondicaoComercial = rs.getString("cdCondicaocomercial");
        condComCliente.cdCliente = rs.getString("cdCliente");
        condComCliente.flDefault = rs.getString("flDefault");
        condComCliente.nuCarimbo = rs.getInt("nuCarimbo");
        condComCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condComCliente.cdUsuario = rs.getString("cdUsuario");
        return condComCliente;
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
        sql.append(" CDCONDICAOCOMERCIAL,");
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
	protected void addInsertColumns(StringBuffer arg0) {
	}

	//@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) {
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		CondComCliente condComCliente = (CondComCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condComCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condComCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCONDICAOCOMERCIAL = ", condComCliente.cdCondicaoComercial);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", condComCliente.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
		
	}
    
}