package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaRep;
import totalcross.sql.ResultSet;

public class SugestaoVendaRepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SugestaoVendaRep();
	}

    private static SugestaoVendaRepDbxDao instance;

    public SugestaoVendaRepDbxDao() {
        super(SugestaoVendaRep.TABLE_NAME);
    }

    public static SugestaoVendaRepDbxDao getInstance() {
        if (instance == null) {
            instance = new SugestaoVendaRepDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        SugestaoVendaRep sugestaoVendaRep = new SugestaoVendaRep();
        sugestaoVendaRep.rowKey = rs.getString("rowkey");
        sugestaoVendaRep.cdEmpresa = rs.getString("cdEmpresa");
        sugestaoVendaRep.cdRepresentante = rs.getString("cdRepresentante");
        sugestaoVendaRep.cdSugestaoVenda = rs.getString("cdSugestaoVenda");
        sugestaoVendaRep.nuCarimbo = rs.getInt("nuCarimbo");
        sugestaoVendaRep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        sugestaoVendaRep.cdUsuario = rs.getString("cdUsuario");
        return sugestaoVendaRep;
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
        sql.append(" CDSUGESTAOVENDA,");
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
        sql.append(" CDSUGESTAOVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaRep sugestaoVendaRep = (SugestaoVendaRep) domain;
        sql.append(Sql.getValue(sugestaoVendaRep.cdEmpresa)).append(",");
        sql.append(Sql.getValue(sugestaoVendaRep.cdRepresentante)).append(",");
        sql.append(Sql.getValue(sugestaoVendaRep.cdSugestaoVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVendaRep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(sugestaoVendaRep.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(sugestaoVendaRep.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaRep sugestaoVendaRep = (SugestaoVendaRep) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(sugestaoVendaRep.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(sugestaoVendaRep.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(sugestaoVendaRep.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaRep sugestaoVendaRep = (SugestaoVendaRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", sugestaoVendaRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", sugestaoVendaRep.cdRepresentante);
		sqlWhereClause.addAndCondition("CDSUGESTAOVENDA = ", sugestaoVendaRep.cdSugestaoVenda);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}