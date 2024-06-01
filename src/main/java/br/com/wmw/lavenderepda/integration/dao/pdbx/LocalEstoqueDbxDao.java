package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.LocalEstoque;
import totalcross.sql.ResultSet;

public class LocalEstoqueDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new LocalEstoque();
	}
	
	private static LocalEstoqueDbxDao instance;
	
	public LocalEstoqueDbxDao() {
        super(LocalEstoque.TABLE_NAME); 
    }
	    
	public static LocalEstoqueDbxDao getInstance() {
		if (instance == null) {
		    instance = new LocalEstoqueDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		LocalEstoque localEstoque = new LocalEstoque();
		localEstoque.rowKey = rs.getString("rowkey");
		localEstoque.cdLocalEstoque = rs.getString("cdLocalEstoque");
		localEstoque.dsLocalEstoque = rs.getString("dsLocalEstoque");
		localEstoque.nuOrdem = rs.getInt("nuOrdem");
		localEstoque.flTipoAlteracao = rs.getString("flTipoAlteracao");
		localEstoque.cdUsuario = rs.getString("cdUsuario");
		localEstoque.nuCarimbo = rs.getInt("nuCarimbo");
		localEstoque.dtAlteracao = rs.getDate("dtAlteracao");
		localEstoque.hrAlteracao = rs.getString("hrAlteracao");
        return localEstoque;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" DSLOCALESTOQUE,");
        sql.append(" NUORDEM,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		LocalEstoque localEstoque = (LocalEstoque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", localEstoque.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", localEstoque.cdRepresentante);
		sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", localEstoque.cdLocalEstoque);
		sql.append(sqlWhereClause.getSql());		
	}

}
