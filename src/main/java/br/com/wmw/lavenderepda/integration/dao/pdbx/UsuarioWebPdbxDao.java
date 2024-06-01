package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.UsuarioWeb;
import totalcross.sql.ResultSet;

public class UsuarioWebPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioWeb();
	}

    private static UsuarioWebPdbxDao instance;

    public UsuarioWebPdbxDao() {
        super(UsuarioWeb.TABLE_NAME);
    }

    public static UsuarioWebPdbxDao getInstance() {
        if (instance == null) {
            instance = new UsuarioWebPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        UsuarioWeb usuarioweb = new UsuarioWeb();
        usuarioweb.rowKey = rs.getString("rowkey");
        usuarioweb.cdUsuarioWeb = rs.getString("cdUsuarioweb");
        usuarioweb.nmUsuarioWeb = rs.getString("nmUsuarioweb");
        usuarioweb.flTipoUsuarioWeb = rs.getString("flTipousuarioweb");
        usuarioweb.cdUsuario = rs.getString("cdUsuario");
        return usuarioweb;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDUSUARIOWEB,");
        sql.append(" NMUSUARIOWEB,");
        sql.append(" FLTIPOUSUARIOWEB,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioWeb usuarioweb = (UsuarioWeb) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDUSUARIOWEB = ", usuarioweb.cdUsuarioWeb);
		sqlWhereClause.addAndCondition("CDUSUARIO = ", usuarioweb.cdUsuario);
		sqlWhereClause.addAndCondition("nmUsuarioWeb = ", usuarioweb.nmUsuarioWeb);
		sqlWhereClause.addAndOrLikeCondition("CDUSUARIOWEB", "NMUSUARIOWEB", usuarioweb.likeFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}