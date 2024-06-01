package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MotivoColeta;
import totalcross.sql.ResultSet;

public class MotivoColetaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoColeta();
	}

    private static MotivoColetaDbxDao instance;

    public MotivoColetaDbxDao() {
        super(MotivoColeta.TABLE_NAME);
    }
    
    public static MotivoColetaDbxDao getInstance() {
        if (instance == null) {
            instance = new MotivoColetaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MotivoColeta motivoColeta = new MotivoColeta();
        motivoColeta.rowKey = rs.getString("rowkey");
        motivoColeta.cdMotivo = rs.getString("cdMotivo");
        motivoColeta.dsMotivo = rs.getString("dsMotivo");
        motivoColeta.flEncerraAutomatico = rs.getString("flEncerraAutomatico");
        motivoColeta.flTipoAlteracao = rs.getString("flTipoAlteracao");
        motivoColeta.nuCarimbo = rs.getInt("nuCarimbo");
        motivoColeta.cdUsuario = rs.getString("cdUsuario");
        return motivoColeta;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDMOTIVO,");
        sql.append(" DSMOTIVO,");
        sql.append(" FLENCERRAAUTOMATICO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDMOTIVO,");
        sql.append(" DSMOTIVO,");
        sql.append(" FLENCERRAAUTOMATICO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotivoColeta motivoColeta = (MotivoColeta) domain;
        sql.append(Sql.getValue(motivoColeta.cdMotivo)).append(",");
        sql.append(Sql.getValue(motivoColeta.dsMotivo)).append(",");
        sql.append(Sql.getValue(motivoColeta.flEncerraAutomatico)).append(",");
        sql.append(Sql.getValue(motivoColeta.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(motivoColeta.nuCarimbo)).append(",");
        sql.append(Sql.getValue(motivoColeta.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotivoColeta motivoColeta = (MotivoColeta) domain;
        sql.append(" DSMOTIVO = ").append(Sql.getValue(motivoColeta.dsMotivo)).append(",");
        sql.append(" FLENCERRAAUTOMATICO = ").append(Sql.getValue(motivoColeta.flEncerraAutomatico)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(motivoColeta.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(motivoColeta.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(motivoColeta.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotivoColeta motivoColeta = (MotivoColeta) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDMOTIVO = ", motivoColeta.cdMotivo);
		if(motivoColeta.flEncerraAutomatico != ValueUtil.VALOR_NAO) {
			sqlWhereClause.addAndCondition("FLENCERRAAUTOMATICO = ", motivoColeta.flEncerraAutomatico);
		} else {
			sqlWhereClause.addAndCondition("FLENCERRAAUTOMATICO <> " + ValueUtil.VALOR_SIM);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}