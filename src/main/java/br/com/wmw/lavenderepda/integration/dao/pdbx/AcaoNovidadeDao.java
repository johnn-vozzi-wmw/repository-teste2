package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AcaoNovidade;
import totalcross.sql.ResultSet;

public class AcaoNovidadeDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AcaoNovidade();
	}

    private static AcaoNovidadeDao instance;
	

    public AcaoNovidadeDao() {
        super(AcaoNovidade.TABLE_NAME);
    }
    
    public static AcaoNovidadeDao getInstance() {
        if (instance == null) {
            instance = new AcaoNovidadeDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AcaoNovidade acaoNovidade = new AcaoNovidade();
        acaoNovidade.rowKey = rs.getString("rowkey");
        acaoNovidade.cdSistema = rs.getInt("cdSistema");
        acaoNovidade.cdRepresentante = rs.getString("cdRepresentante");
        acaoNovidade.cdNovidade = rs.getString("cdNovidade");
        acaoNovidade.cdAcaoNovidade = rs.getInt("cdAcaoNovidade");
        acaoNovidade.flAcao = rs.getString("flAcao");
        acaoNovidade.dtAcao = rs.getDate("dtAcao");
        acaoNovidade.hrAcao = rs.getString("hrAcao");
        acaoNovidade.nuCarimbo = rs.getInt("nuCarimbo");
        acaoNovidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        acaoNovidade.cdUsuario = rs.getString("cdUsuario");
        return acaoNovidade;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDSISTEMA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDNOVIDADE,");
        sql.append(" CDACAONOVIDADE,");
        sql.append(" FLACAO,");
        sql.append(" DTACAO,");
        sql.append(" HRACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDSISTEMA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDNOVIDADE,");
        sql.append(" CDACAONOVIDADE,");
        sql.append(" FLACAO,");
        sql.append(" DTACAO,");
        sql.append(" HRACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AcaoNovidade acaoNovidade = (AcaoNovidade) domain;
        sql.append(Sql.getValue(acaoNovidade.cdSistema)).append(",");
        sql.append(Sql.getValue(acaoNovidade.cdRepresentante)).append(",");
        sql.append(Sql.getValue(acaoNovidade.cdNovidade)).append(",");
        sql.append(Sql.getValue(acaoNovidade.cdAcaoNovidade)).append(",");
        sql.append(Sql.getValue(acaoNovidade.flAcao)).append(",");
        sql.append(Sql.getValue(acaoNovidade.dtAcao)).append(",");
        sql.append(Sql.getValue(acaoNovidade.hrAcao)).append(",");
        sql.append(Sql.getValue(acaoNovidade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(acaoNovidade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(acaoNovidade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AcaoNovidade acaoNovidade = (AcaoNovidade) domain;
        sql.append(" FLACAO = ").append(Sql.getValue(acaoNovidade.flAcao)).append(",");
        sql.append(" DTACAO = ").append(Sql.getValue(acaoNovidade.dtAcao)).append(",");
        sql.append(" HRACAO = ").append(Sql.getValue(acaoNovidade.hrAcao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(acaoNovidade.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(acaoNovidade.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	AcaoNovidade acaoNovidade = (AcaoNovidade) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("CDSISTEMA = ", acaoNovidade.cdSistema);
    	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", acaoNovidade.cdRepresentante);
    	sqlWhereClause.addAndCondition("CDNOVIDADE = ", acaoNovidade.cdNovidade);
    	sqlWhereClause.addAndCondition("CDACAONOVIDADE = ", acaoNovidade.cdAcaoNovidade);
    	//--
    	sql.append(sqlWhereClause.getSql());
    }
    
    
    public int findMaxCdAcaoNovidadeByExample(AcaoNovidade acaoNovidadeFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT MAX(CDACAONOVIDADE) AS MAXCDACAONOVIDADE ");
    	sql.append(" FROM ").append(tableName);
    	addWhereByExample(acaoNovidadeFilter, sql);
    	return getInt(sql.toString());
//    	ResultSet resultSet = executeQuery(sql.toString());
//		try {
//			int maxCdAcaoNovidade = 0;
//			while (resultSet.next()) {
//				maxCdAcaoNovidade = resultSet.getInt("MAXCDACAONOVIDADE");
//			}
//			return maxCdAcaoNovidade;
//		} finally {
//			close(resultSet);
//		}
    }
    
    public int getNextCdAcaoNovidade(AcaoNovidade acaoNovidadeFilter) throws SQLException {
		int nextCdAcaoNovidade = findMaxCdAcaoNovidadeByExample(acaoNovidadeFilter);
        return nextCdAcaoNovidade + 1;
    }
}