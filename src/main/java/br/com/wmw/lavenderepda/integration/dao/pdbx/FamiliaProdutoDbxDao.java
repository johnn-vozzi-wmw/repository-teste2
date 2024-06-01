package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FamiliaProduto;
import totalcross.sql.ResultSet;

public class FamiliaProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FamiliaProduto();
	}

    private static FamiliaProdutoDbxDao instance;

    public FamiliaProdutoDbxDao() {
        super(FamiliaProduto.TABLE_NAME); 
    }
    
    public static FamiliaProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new FamiliaProdutoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        FamiliaProduto familiaProduto = new FamiliaProduto();
        familiaProduto.rowKey = rs.getString("rowkey");
        familiaProduto.cdEmpresa = rs.getString("cdEmpresa");
        familiaProduto.cdFamilia = rs.getString("cdFamilia");
        familiaProduto.cdProduto = rs.getString("cdProduto");
        familiaProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        familiaProduto.nuCarimbo = rs.getInt("nuCarimbo");
        familiaProduto.cdUsuario = rs.getString("cdUsuario");
        return familiaProduto;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDFAMILIA,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDFAMILIA,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FamiliaProduto familiaProduto = (FamiliaProduto) domain;
        sql.append(Sql.getValue(familiaProduto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(familiaProduto.cdFamilia)).append(",");
        sql.append(Sql.getValue(familiaProduto.cdProduto)).append(",");
        sql.append(Sql.getValue(familiaProduto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(familiaProduto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(familiaProduto.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FamiliaProduto familiaProduto = (FamiliaProduto) domain;
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(familiaProduto.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(familiaProduto.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(familiaProduto.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FamiliaProduto familiaProduto = (FamiliaProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", familiaProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDFAMILIA = ", familiaProduto.cdFamilia);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", familiaProduto.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}