package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.UsuarioConfigEmp;
import totalcross.sql.ResultSet;

public class UsuarioConfigEmpDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioConfigEmp();
	}

    private static UsuarioConfigEmpDao instance;

    public UsuarioConfigEmpDao() {
        super(UsuarioConfigEmp.TABLE_NAME);
    }
    
    public static UsuarioConfigEmpDao getInstance() {
        if (instance == null) {
            instance = new UsuarioConfigEmpDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        UsuarioConfigEmp usuarioConfigEmp = new UsuarioConfigEmp();
        usuarioConfigEmp.rowKey = rs.getString("rowkey");
        usuarioConfigEmp.cdEmpresa = rs.getString("cdEmpresa");
        usuarioConfigEmp.vlPctMaxDesconto = ValueUtil.round(rs.getDouble("vlPctMaxDesconto"));
        usuarioConfigEmp.vlPctMaxAcrescimo = ValueUtil.round(rs.getDouble("vlPctMaxAcrescimo"));
        usuarioConfigEmp.vlPctAlcadaLibLimiteCredito = ValueUtil.round(rs.getDouble("vlPctAlcadaLibLimiteCredito"));
        usuarioConfigEmp.flTipoAlteracao = rs.getString("flTipoAlteracao");
        usuarioConfigEmp.nuCarimbo = rs.getInt("nuCarimbo");
        usuarioConfigEmp.cdUsuario = rs.getString("cdUsuario");
        return usuarioConfigEmp;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
    	sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" VLPCTMAXACRESCIMO,");
        sql.append(" VLPCTALCADALIBLIMITECREDITO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
   
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    
}