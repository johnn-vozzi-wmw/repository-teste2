package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.UsuarioGrupoProduto;
import totalcross.sql.ResultSet;

public class UsuarioGrupoProdutoDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioGrupoProduto();
	}

    private static UsuarioGrupoProdutoDbxDao instance;

    public UsuarioGrupoProdutoDbxDao() { super(UsuarioGrupoProduto.TABLE_NAME); }

    public static UsuarioGrupoProdutoDbxDao getInstance() { return (instance == null) ? instance = new UsuarioGrupoProdutoDbxDao() : instance; }
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { addSelectColumns(null, sql); }
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return populate(domainFilter, rs); }
	@Override protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) { return null; }
    @Override protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {}
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        UsuarioGrupoProduto usuarioGrupoProduto = new UsuarioGrupoProduto();
	    usuarioGrupoProduto.rowKey = rs.getString("rowkey");
	    usuarioGrupoProduto.cdEmpresa = rs.getString("cdEmpresa");
	    usuarioGrupoProduto.cdUsuarioGrupoProduto = rs.getString("cdUsuarioGrupoProduto");
	    usuarioGrupoProduto.cdGrupoProduto1  = rs.getString("cdGrupoProduto1");
	    usuarioGrupoProduto.cdUsuario = rs.getString("cdUsuario");
        return usuarioGrupoProduto;
    }

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDUSUARIOGRUPOPRODUTO,");
	    sql.append(" tb.CDGRUPOPRODUTO1,");
	    sql.append(" tb.CDUSUARIO");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        UsuarioGrupoProduto usuarioGrupoProduto = (UsuarioGrupoProduto) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("CDEMPRESA = ", usuarioGrupoProduto.cdEmpresa);
	    sqlWhereClause.addAndCondition("CDUSUARIOGRUPOPRODUTO = ", usuarioGrupoProduto.cdUsuarioGrupoProduto);
	    sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", usuarioGrupoProduto.cdGrupoProduto1);
    }

}