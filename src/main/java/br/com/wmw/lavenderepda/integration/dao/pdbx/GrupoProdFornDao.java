package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.GrupoProdForn;
import totalcross.sql.ResultSet;

public class GrupoProdFornDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoProdForn();
	}

    private static GrupoProdFornDao instance;
	

    public GrupoProdFornDao() {
        super(GrupoProdForn.TABLE_NAME);
    }
    
    public static GrupoProdFornDao getInstance() {
        if (instance == null) {
            instance = new GrupoProdFornDao();
        }
        return instance;
    }
    
	//@Override
	protected BaseDomain populateSummary(ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDFORNECEDOR,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDFORNECEDOR,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        GrupoProdForn grupoProdForn = (GrupoProdForn) domain;
        sql.append(Sql.getValue(grupoProdForn.cdEmpresa)).append(",");
        sql.append(Sql.getValue(grupoProdForn.cdFornecedor)).append(",");
        sql.append(Sql.getValue(grupoProdForn.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(grupoProdForn.nuCarimbo)).append(",");
        sql.append(Sql.getValue(grupoProdForn.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(grupoProdForn.dtAlteracao)).append(",");
        sql.append(Sql.getValue(grupoProdForn.hrAlteracao)).append(",");
        sql.append(Sql.getValue(grupoProdForn.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        GrupoProdForn grupoProdForn = (GrupoProdForn) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(grupoProdForn.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(grupoProdForn.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(grupoProdForn.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(grupoProdForn.hrAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(grupoProdForn.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        GrupoProdForn grupoProdForn = (GrupoProdForn) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", grupoProdForn.cdEmpresa);
		sqlWhereClause.addAndCondition("CDFORNECEDOR = ", grupoProdForn.cdFornecedor);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", grupoProdForn.cdGrupoProduto1);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, totalcross.sql.ResultSet rs) throws SQLException {
		GrupoProdForn grupoProdForn = new GrupoProdForn();
        grupoProdForn.rowKey = rs.getString("rowkey");
        grupoProdForn.cdEmpresa = rs.getString("cdEmpresa");
        grupoProdForn.cdFornecedor = rs.getString("cdFornecedor");
        grupoProdForn.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        grupoProdForn.nuCarimbo = rs.getInt("nuCarimbo");
        grupoProdForn.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoProdForn.dtAlteracao = rs.getDate("dtAlteracao");
        grupoProdForn.hrAlteracao = rs.getString("hrAlteracao");
        grupoProdForn.cdUsuario = rs.getString("cdUsuario");
        return grupoProdForn;
	}
    
}