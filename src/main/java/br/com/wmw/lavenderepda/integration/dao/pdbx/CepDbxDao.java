package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Cep;
import totalcross.sql.ResultSet;

public class CepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Cep();
	}

    private static CepDbxDao instance;

    public CepDbxDao() {
        super(Cep.TABLE_NAME);
    }
    
    public static CepDbxDao getInstance() {
        if (instance == null) {
            instance = new CepDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Cep cep = new Cep();
        cep.rowKey = rs.getString("rowkey");
        cep.dsCep = rs.getString("dsCep");
        cep.cdLogradouro = rs.getString("cdLogradouro");
        cep.cdBairro = rs.getString("cdBairro");
        cep.cdCidade = rs.getString("cdCidade");
        cep.nuCarimbo = rs.getInt("nuCarimbo");
        cep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cep.cdUsuario = rs.getString("cdUsuario");
        return cep;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" DSCEP,");
        sql.append(" CDLOGRADOURO,");
        sql.append(" CDBAIRRO,");
        sql.append(" CDCIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" DSCEP,");
        sql.append(" CDLOGRADOURO,");
        sql.append(" CDBAIRRO,");
        sql.append(" CDCIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cep cep = (Cep) domain;
        sql.append(Sql.getValue(cep.dsCep)).append(",");
        sql.append(Sql.getValue(cep.cdLogradouro)).append(",");
        sql.append(Sql.getValue(cep.cdBairro)).append(",");
        sql.append(Sql.getValue(cep.cdCidade)).append(",");
        sql.append(Sql.getValue(cep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(cep.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(cep.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cep cep = (Cep) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(cep.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(cep.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(cep.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Cep cep = (Cep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DSCEP = ", cep.dsCep);
		sqlWhereClause.addAndCondition("CDLOGRADOURO = ", cep.cdLogradouro);
		sqlWhereClause.addAndCondition("CDBAIRRO = ", cep.cdBairro);
		sqlWhereClause.addAndCondition("CDCIDADE = ", cep.cdCidade);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}