package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Novidade;
import totalcross.sql.ResultSet;

public class NovidadeDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Novidade();
	}

    private static NovidadeDbxDao instance;
	

    public NovidadeDbxDao() {
        super(Novidade.TABLE_NAME);
    }
    
    public static NovidadeDbxDao getInstance() {
        if (instance == null) {
            instance = new NovidadeDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Novidade novidade = new Novidade();
        novidade.rowKey = rs.getString("rowkey");
        novidade.cdSistema = rs.getInt("cdSistema");
        novidade.cdNovidade = rs.getString("cdNovidade");
        novidade.dsNovidade = rs.getString("dsNovidade");
        novidade.dsUrl = rs.getString("dsUrl");
        novidade.dtInicial = rs.getDate("dtInicial");
        novidade.dtFinal = rs.getDate("dtFinal");
        novidade.nuCarimbo = rs.getInt("nuCarimbo");
        novidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        novidade.cdUsuario = rs.getString("cdUsuario");
        return novidade;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDSISTEMA,");
        sql.append(" CDNOVIDADE,");
        sql.append(" DSNOVIDADE,");
        sql.append(" DSURL,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
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
        sql.append(" CDNOVIDADE,");
        sql.append(" DSNOVIDADE,");
        sql.append(" DSURL,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Novidade novidade = (Novidade) domain;
        sql.append(Sql.getValue(novidade.cdSistema)).append(",");
        sql.append(Sql.getValue(novidade.cdNovidade)).append(",");
        sql.append(Sql.getValue(novidade.dsNovidade)).append(",");
        sql.append(Sql.getValue(novidade.dsUrl)).append(",");
        sql.append(Sql.getValue(novidade.dtInicial)).append(",");
        sql.append(Sql.getValue(novidade.dtFinal)).append(",");
        sql.append(Sql.getValue(novidade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(novidade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(novidade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Novidade novidade = (Novidade) domain;
        sql.append(" DSNOVIDADE = ").append(Sql.getValue(novidade.dsUrl)).append(",");
        sql.append(" DSURL = ").append(Sql.getValue(novidade.dsUrl)).append(",");
        sql.append(" DTINICIAL = ").append(Sql.getValue(novidade.dtInicial)).append(",");
        sql.append(" DTFINAL = ").append(Sql.getValue(novidade.dtFinal)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(novidade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(novidade.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(novidade.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Novidade novidade = (Novidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDSISTEMA = ", novidade.cdSistema);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}