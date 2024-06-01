package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Cidade;
import totalcross.sql.ResultSet;

public class CidadeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Cidade();
	}

    private static CidadeDbxDao instance;

    public CidadeDbxDao() {
        super(Cidade.TABLE_NAME);
    }
    
    public static CidadeDbxDao getInstance() {
        if (instance == null) {
            instance = new CidadeDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Cidade cidade = new Cidade();
        cidade.rowKey = rs.getString("rowkey");
        cidade.cdCidade = rs.getString("cdCidade");
        cidade.nmCidade = rs.getString("nmCidade");
        cidade.cdUf = rs.getString("cdUf");
        cidade.flCapital = rs.getString("flCapital");
        cidade.nuCarimbo = rs.getInt("nuCarimbo");
        cidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cidade.cdUsuario = rs.getString("cdUsuario");
        return cidade;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDCIDADE,");
        sql.append(" NMCIDADE,");
        sql.append(" CDUF,");
        sql.append(" FLCAPITAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDCIDADE,");
        sql.append(" NMCIDADE,");
        sql.append(" CDUF,");
        sql.append(" FLCAPITAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cidade cidade = (Cidade) domain;
        sql.append(Sql.getValue(cidade.cdCidade)).append(",");
        sql.append(Sql.getValue(cidade.nmCidade)).append(",");
        sql.append(Sql.getValue(cidade.cdUf)).append(",");
        sql.append(Sql.getValue(cidade.flCapital)).append(",");
        sql.append(Sql.getValue(cidade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(cidade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(cidade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cidade cidade = (Cidade) domain;
        sql.append(" NMCIDADE = ").append(Sql.getValue(cidade.nmCidade)).append(",");
        sql.append(" CDUF = ").append(Sql.getValue(cidade.cdUf)).append(",");
        sql.append(" FLCAPITAL = ").append(Sql.getValue(cidade.flCapital)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(cidade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(cidade.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(cidade.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cidade cidade = (Cidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDCIDADE = ", cidade.cdCidade);
		sqlWhereClause.addAndCondition("NMCIDADE = ", cidade.nmCidade);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}