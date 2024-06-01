package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.business.domain.UsuarioPda;
import totalcross.sql.ResultSet;

public class UsuarioPdaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioPda();
	}

    private static UsuarioPdaPdbxDao instance;

    public UsuarioPdaPdbxDao() {
        super(UsuarioPda.TABLE_NAME);
    }

    public static UsuarioPdaPdbxDao getInstance() {
        if (instance == null) {
            instance = new UsuarioPdaPdbxDao();
        }
        return instance;
    }

    public int executeUpdate(String sql) throws java.sql.SQLException {
    	return super.executeUpdate(sql);
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null; 
	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	UsuarioPda usuarioPda = new UsuarioPda();
        usuarioPda.rowKey = rs.getString("rowkey");
        usuarioPda.cdUsuario = rs.getString("cdUsuario");
        usuarioPda.nuHoraDiferencaServidor = rs.getInt("nuHoraDiferencaServidor");
        usuarioPda.nuHoraDiferencaHrVerao = rs.getInt("nuHoraDiferencaHrVerao");
        return usuarioPda;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUHORADIFERENCASERVIDOR,");
        sql.append(" NUHORADIFERENCAHRVERAO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioPda usuarioPda = (UsuarioPda) domain;
        sql.append(" where CDUSUARIO = ").append(Sql.getValue(usuarioPda.cdUsuario));

    }
}