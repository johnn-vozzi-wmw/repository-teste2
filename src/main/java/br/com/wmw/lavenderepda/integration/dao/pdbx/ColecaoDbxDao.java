package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Colecao;
import totalcross.sql.ResultSet;

public class ColecaoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Colecao();
	}

    private static ColecaoDbxDao instance;

    public ColecaoDbxDao() {
        super(Colecao.TABLE_NAME); 
    }
    
    public static ColecaoDbxDao getInstance() {
        if (instance == null) {
            instance = new ColecaoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        Colecao colecao = new Colecao();
        colecao.rowKey = rs.getString("rowkey");
        colecao.cdEmpresa = rs.getString("cdEmpresa");
        colecao.cdMarca = rs.getString("cdMarca");
        colecao.cdColecao = rs.getString("cdColecao");
        colecao.dsColecao = rs.getString("dsColecao");
        colecao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        colecao.nuCarimbo = rs.getInt("nuCarimbo");
        colecao.cdUsuario = rs.getString("cdUsuario");
        return colecao;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDMARCA,");
        sql.append(" CDCOLECAO,");
        sql.append(" DSCOLECAO,");
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
        sql.append(" CDMARCA,");
        sql.append(" CDCOLECAO,");
        sql.append(" DSCOLECAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Colecao colecao = (Colecao) domain;
        sql.append(Sql.getValue(colecao.cdEmpresa)).append(",");
        sql.append(Sql.getValue(colecao.cdMarca)).append(",");
        sql.append(Sql.getValue(colecao.cdColecao)).append(",");
        sql.append(Sql.getValue(colecao.dsColecao)).append(",");
        sql.append(Sql.getValue(colecao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(colecao.nuCarimbo)).append(",");
        sql.append(Sql.getValue(colecao.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Colecao colecao = (Colecao) domain;
        sql.append(" DSCOLECAO = ").append(Sql.getValue(colecao.dsColecao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(colecao.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(colecao.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(colecao.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Colecao colecao = (Colecao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", colecao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMARCA = ", colecao.cdMarca);
		sqlWhereClause.addAndCondition("CDCOLECAO = ", colecao.cdColecao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}