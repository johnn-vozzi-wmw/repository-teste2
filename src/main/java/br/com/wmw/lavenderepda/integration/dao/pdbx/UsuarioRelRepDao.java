package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;
import totalcross.sql.ResultSet;

public class UsuarioRelRepDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioRelRep();
	}

    private static UsuarioRelRepDao instance;

    public UsuarioRelRepDao() {
        super(UsuarioRelRep.TABLE_NAME);
    }
    
    public static UsuarioRelRepDao getInstance() {
        if (instance == null) {
            instance = new UsuarioRelRepDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        UsuarioRelRep usuarioRelRep = new UsuarioRelRep();
        usuarioRelRep.rowKey = rs.getString("rowkey");
        usuarioRelRep.cdEmpresa = rs.getString("cdEmpresa");
        usuarioRelRep.cdRepresentante = rs.getString("cdRepresentante");
        usuarioRelRep.cdSupervisor = rs.getString("cdSupervisor");
        usuarioRelRep.cdUsuarioRep = rs.getString("cdUsuarioRep");
        usuarioRelRep.nmUsuario = rs.getString("nmUsuario");
        usuarioRelRep.vlPctMaxDesconto = ValueUtil.round(rs.getDouble("vlPctMaxDesconto"));
        usuarioRelRep.vlPctMaxAcrescimo = ValueUtil.round(rs.getDouble("vlPctMaxAcrescimo"));
        usuarioRelRep.vlPctAlcadaLibLimiteCredito = ValueUtil.round(rs.getDouble("vlPctAlcadaLibLimiteCredito"));
        usuarioRelRep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        usuarioRelRep.nuCarimbo = rs.getInt("nuCarimbo");
        usuarioRelRep.cdUsuario = rs.getString("cdUsuario");
        return usuarioRelRep;
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
        sql.append(" CDSUPERVISOR,");
        sql.append(" CDUSUARIOREP,");
        sql.append(" NMUSUARIO,");
        sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" VLPCTMAXACRESCIMO,");
        sql.append(" VLPCTALCADALIBLIMITECREDITO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioRelRep usuarioRelRep = (UsuarioRelRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", usuarioRelRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", usuarioRelRep.cdRepresentante);
		sqlWhereClause.addAndCondition("CDSUPERVISOR = ", usuarioRelRep.cdSupervisor);
		sqlWhereClause.addAndCondition("CDUSUARIOREP = ", usuarioRelRep.cdUsuarioRep);
		sqlWhereClause.addAndCondition("CDUSUARIO = ", usuarioRelRep.cdUsuario);
		if (usuarioRelRep.filtraLiberacaoPorUsuarioEAlcada) {
			sqlWhereClause.addAndConditionForced("VLPCTALCADALIBLIMITECREDITO > ", 0);
			sqlWhereClause.addAndCondition("VLPCTALCADALIBLIMITECREDITO >= ", usuarioRelRep.vlPctAlcadaLibLimiteCredito);
		}
		sql.append(sqlWhereClause.getSql());
    }
    
}