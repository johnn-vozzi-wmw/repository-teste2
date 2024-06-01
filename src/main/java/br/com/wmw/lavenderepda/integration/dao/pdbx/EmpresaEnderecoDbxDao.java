package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.EmpresaEndereco;
import totalcross.sql.ResultSet;

public class EmpresaEnderecoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EmpresaEndereco();
	}

    private static EmpresaEnderecoDbxDao instance;

    public EmpresaEnderecoDbxDao() {
        super(EmpresaEndereco.TABLE_NAME);
    }
    
    public static EmpresaEnderecoDbxDao getInstance() {
        if (instance == null) {
            instance = new EmpresaEnderecoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        EmpresaEndereco empresaEndereco = new EmpresaEndereco();
        empresaEndereco.rowKey = rs.getString("rowkey");
        empresaEndereco.cdEmpresa = rs.getString("cdEmpresa");
        empresaEndereco.dsCidade = rs.getString("dsCidade");
        empresaEndereco.dsBairro = rs.getString("dsBairro");
        empresaEndereco.dsDiasEntrega = rs.getString("dsDiasEntrega");
        empresaEndereco.nuCarimbo = rs.getInt("nuCarimbo");
        empresaEndereco.flTipoAlteracao = rs.getString("flTipoAlteracao");
        empresaEndereco.cdUsuario = rs.getString("cdUsuario");
        return empresaEndereco;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" DSCIDADE,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSDIASENTREGA,");
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
        sql.append(" DSCIDADE,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSDIASENTREGA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        EmpresaEndereco empresaEndereco = (EmpresaEndereco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", empresaEndereco.cdEmpresa);
		sqlWhereClause.addAndCondition("DSCIDADE = ", empresaEndereco.dsCidade);
		sqlWhereClause.addAndCondition("DSBAIRRO = ", empresaEndereco.dsBairro);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}