package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PrazoEntrega;
import totalcross.sql.ResultSet;

public class PrazoEntregaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PrazoEntrega();
	}

    private static PrazoEntregaPdbxDao instance;

    public PrazoEntregaPdbxDao() {
        super(PrazoEntrega.TABLE_NAME);
    }
    
    public static PrazoEntregaPdbxDao getInstance() {
        return (instance == null) ? new PrazoEntregaPdbxDao() : instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PrazoEntrega prazoEntrega = new PrazoEntrega();
        prazoEntrega.rowKey = rs.getString("rowkey");
        prazoEntrega.cdEmpresa = rs.getString("cdEmpresa");
        prazoEntrega.cdRepresentante = rs.getString("cdRepresentante");
        prazoEntrega.cdProduto = rs.getString("cdProduto");
        prazoEntrega.cdUsuario = rs.getString("cdUsuario");
        prazoEntrega.qtItem = rs.getInt("qtItem");
        prazoEntrega.nuDiasEntrega = rs.getInt("nuDiasEntrega");
        prazoEntrega.flTipoAlteracao = rs.getString("flTipoAlteracao");
        prazoEntrega.nuCarimbo = rs.getInt("nuCarimbo");
        return prazoEntrega;
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
        sql.append(" CDPRODUTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" QTITEM,");
        sql.append(" NUDIASENTREGA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	if(domain == null || sql == null) {
    		return;
    	}
    	PrazoEntrega prazoEntrega = (PrazoEntrega) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", prazoEntrega.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", prazoEntrega.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", prazoEntrega.cdProduto);
		sqlWhereClause.addAndCondition("CDUSUARIO = ", prazoEntrega.cdUsuario);
		sqlWhereClause.addAndCondition("QTITEM BETWEEN 0 AND ", prazoEntrega.qtItem);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	PrazoEntrega prazoEntrega = (PrazoEntrega) domain;
    	if(prazoEntrega.qtItem > 0) {
    		sql.append(" ORDER BY QTITEM DESC");
    	}
    }
    
    @Override
    protected void addLimit(StringBuffer sql, BaseDomain domain) {
    	PrazoEntrega prazoEntrega = (PrazoEntrega) domain;
    	if(prazoEntrega.qtItem > 0) {
    		sql.append(" LIMIT 1");
    	}
    }

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}
    
}