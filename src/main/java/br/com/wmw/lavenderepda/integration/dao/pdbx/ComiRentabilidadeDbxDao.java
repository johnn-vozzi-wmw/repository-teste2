package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ComiRentabilidade;
import totalcross.sql.ResultSet;

public class ComiRentabilidadeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ComiRentabilidade();
	}

    private static ComiRentabilidadeDbxDao instance;

    public ComiRentabilidadeDbxDao() {
        super(ComiRentabilidade.TABLE_NAME);
    }
    
    public static ComiRentabilidadeDbxDao getInstance() {
        if (instance == null) {
            instance = new ComiRentabilidadeDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ComiRentabilidade comiRentabilidade = new ComiRentabilidade();
        comiRentabilidade.rowKey = rs.getString("rowkey");
        comiRentabilidade.cdEmpresa = rs.getString("cdEmpresa");
        comiRentabilidade.cdRepresentante = rs.getString("cdRepresentante");
        comiRentabilidade.vlPctRentabilidade = ValueUtil.round(rs.getDouble("vlPctRentabilidade"));
        comiRentabilidade.vlPctComissao = ValueUtil.round(rs.getDouble("vlPctComissao"));
        if (LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
        	comiRentabilidade.vlPctVerba = ValueUtil.round(rs.getDouble("vlPctVerba"));
        }
        comiRentabilidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        comiRentabilidade.nuCarimbo = rs.getInt("nuCarimbo");
        comiRentabilidade.cdUsuario = rs.getString("cdUsuario");
        return comiRentabilidade;
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
        sql.append(" VLPCTRENTABILIDADE,");
        sql.append(" VLPCTCOMISSAO,");
        if (LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
        	sql.append(" VLPCTVERBA,");
        }
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" VLPCTRENTABILIDADE,");
        sql.append(" VLPCTCOMISSAO,");
        if (LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
        	sql.append(" VLPCTVERBA,");	
        }
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ComiRentabilidade comiRentabilidade = (ComiRentabilidade) domain;
        sql.append(Sql.getValue(comiRentabilidade.cdEmpresa)).append(",");
        sql.append(Sql.getValue(comiRentabilidade.cdRepresentante)).append(",");
        sql.append(Sql.getValue(comiRentabilidade.vlPctRentabilidade)).append(",");
        sql.append(Sql.getValue(comiRentabilidade.vlPctComissao)).append(",");
        if (LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
        	sql.append(Sql.getValue(comiRentabilidade.vlPctVerba)).append(",");
        }
        sql.append(Sql.getValue(comiRentabilidade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(comiRentabilidade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(comiRentabilidade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ComiRentabilidade comiRentabilidade = (ComiRentabilidade) domain;
        sql.append(" VLPCTCOMISSAO = ").append(Sql.getValue(comiRentabilidade.vlPctComissao)).append(",");
        if (LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao) {
        	sql.append(" VLPCTVERBA = ").append(Sql.getValue(comiRentabilidade.vlPctVerba)).append(",");
        }
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(comiRentabilidade.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(comiRentabilidade.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(comiRentabilidade.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ComiRentabilidade comiRentabilidade = (ComiRentabilidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", comiRentabilidade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", comiRentabilidade.cdRepresentante);
		sqlWhereClause.addAndCondition("VLPCTRENTABILIDADE = ", comiRentabilidade.vlPctRentabilidade);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}