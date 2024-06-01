package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.UsuarioSupervisor;
import totalcross.sql.ResultSet;

public class UsuarioSupervisorDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioSupervisor();
	}

    private static UsuarioSupervisorDbxDao instance;

    public UsuarioSupervisorDbxDao() {
        super(UsuarioSupervisor.TABLE_NAME);
    }
    
    public static UsuarioSupervisorDbxDao getInstance() {
        if (instance == null) {
            instance = new UsuarioSupervisorDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        UsuarioSupervisor usuarioSupervisor = new UsuarioSupervisor();
        usuarioSupervisor.rowKey = rs.getString("rowkey");
        usuarioSupervisor.cdEmpresa = rs.getString("cdEmpresa");
        usuarioSupervisor.cdSupervisor = rs.getString("cdSupervisor");
        usuarioSupervisor.cdUsuarioTelevendas = rs.getString("cdUsuarioTelevendas");
        usuarioSupervisor.cdUsuario = rs.getString("cdUsuario");
        usuarioSupervisor.nmUsuario = rs.getString("nmUsuario");
        usuarioSupervisor.flTipoAlteracao = rs.getString("flTipoAlteracao");
        usuarioSupervisor.nuCarimbo = rs.getInt("nuCarimbo");
        return usuarioSupervisor;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUPERVISOR,");
        sql.append(" CDUSUARIOTELEVENDAS,");
        sql.append(" CDUSUARIO,");
        sql.append(" NMUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUPERVISOR,");
        sql.append(" CDUSUARIOTELEVENDAS,");
        sql.append(" CDUSUARIO,");
        sql.append(" NMUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioSupervisor usuarioSupervisor = (UsuarioSupervisor) domain;
        sql.append(Sql.getValue(usuarioSupervisor.cdEmpresa)).append(",");
        sql.append(Sql.getValue(usuarioSupervisor.cdSupervisor)).append(",");
        sql.append(Sql.getValue(usuarioSupervisor.cdUsuarioTelevendas)).append(",");
        sql.append(Sql.getValue(usuarioSupervisor.cdUsuario)).append(",");
        sql.append(Sql.getValue(usuarioSupervisor.nmUsuario)).append(",");
        sql.append(Sql.getValue(usuarioSupervisor.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(usuarioSupervisor.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioSupervisor usuarioSupervisor = (UsuarioSupervisor) domain;
        sql.append(" NMUSUARIO = ").append(Sql.getValue(usuarioSupervisor.nmUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(usuarioSupervisor.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(usuarioSupervisor.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioSupervisor usuarioSupervisor = (UsuarioSupervisor) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", usuarioSupervisor.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSUPERVISOR = ", usuarioSupervisor.cdSupervisor);
		sqlWhereClause.addAndCondition("CDUSUARIOTELEVENDAS = ", usuarioSupervisor.cdUsuarioTelevendas);
		sqlWhereClause.addAndCondition("CDUSUARIO = ", usuarioSupervisor.cdUsuario);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}