package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CanalCliGrupo;
import totalcross.sql.ResultSet;

public class CanalCliGrupoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CanalCliGrupo();
	}

    private static CanalCliGrupoDao instance;

    public CanalCliGrupoDao() {
        super(CanalCliGrupo.TABLE_NAME);
    }
    
    public static CanalCliGrupoDao getInstance() {
        if (instance == null) {
            instance = new CanalCliGrupoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CanalCliGrupo canalCliGrupo = new CanalCliGrupo();
        canalCliGrupo.rowKey = rs.getString("rowkey");
        canalCliGrupo.cdEmpresa = rs.getString("cdEmpresa");
        canalCliGrupo.cdRepresentante = rs.getString("cdRepresentante");
        canalCliGrupo.cdCanalCliente = rs.getString("cdCanalCliente");
        canalCliGrupo.cdCanalGrupo = rs.getString("cdCanalGrupo");
        canalCliGrupo.vlPctMaxDesconto = ValueUtil.round(rs.getDouble("vlPctMaxDesconto"));
        canalCliGrupo.nuCarimbo = rs.getInt("nuCarimbo");
        canalCliGrupo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        canalCliGrupo.cdUsuario = rs.getString("cdUsuario");
        return canalCliGrupo;
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
        sql.append(" CDCANALCLIENTE,");
        sql.append(" CDCANALGRUPO,");
        sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCANALCLIENTE,");
        sql.append(" CDCANALGRUPO,");
        sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CanalCliGrupo canalCliGrupo = (CanalCliGrupo) domain;
        sql.append(Sql.getValue(canalCliGrupo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(canalCliGrupo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(canalCliGrupo.cdCanalCliente)).append(",");
        sql.append(Sql.getValue(canalCliGrupo.cdCanalGrupo)).append(",");
        sql.append(Sql.getValue(canalCliGrupo.vlPctMaxDesconto)).append(",");
        sql.append(Sql.getValue(canalCliGrupo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(canalCliGrupo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(canalCliGrupo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CanalCliGrupo canalCliGrupo = (CanalCliGrupo) domain;
        sql.append(" VLPCTMAXDESCONTO = ").append(Sql.getValue(canalCliGrupo.vlPctMaxDesconto)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(canalCliGrupo.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(canalCliGrupo.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(canalCliGrupo.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CanalCliGrupo canalCliGrupo = (CanalCliGrupo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", canalCliGrupo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", canalCliGrupo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCANALCLIENTE = ", canalCliGrupo.cdCanalCliente);
		sqlWhereClause.addAndCondition("CDCANALGRUPO = ", canalCliGrupo.cdCanalGrupo);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}