package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import totalcross.sql.ResultSet;

public class SugestaoVendaProdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SugestaoVendaProd();
	}

    private static SugestaoVendaProdDbxDao instance;

    public SugestaoVendaProdDbxDao() {
        super(SugestaoVendaProd.TABLE_NAME);
    }

    public static SugestaoVendaProdDbxDao getInstance() {
        if (instance == null) {
            instance = new SugestaoVendaProdDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        SugestaoVendaProd sugestaoVendaProd = new SugestaoVendaProd();
        sugestaoVendaProd.rowKey = rs.getString("rowkey");
        sugestaoVendaProd.cdEmpresa = rs.getString("cdEmpresa");
        sugestaoVendaProd.cdSugestaoVenda = rs.getString("cdSugestaoVenda");
        sugestaoVendaProd.cdProduto = rs.getString("cdProduto");
        sugestaoVendaProd.qtUnidadesVenda = rs.getInt("qtUnidadesVenda");
        sugestaoVendaProd.nuCarimbo = rs.getInt("nuCarimbo");
        sugestaoVendaProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        sugestaoVendaProd.nuRelevancia = rs.getInt("nuRelevancia");
        sugestaoVendaProd.cdUsuario = rs.getString("cdUsuario");
        return sugestaoVendaProd;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUGESTAOVENDA,");
        sql.append(" CDPRODUTO,");
        sql.append(" QTUNIDADESVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
		sql.append(" NURELEVANCIA,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUGESTAOVENDA,");
        sql.append(" CDPRODUTO,");
        sql.append(" QTUNIDADESVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) domain;
        sql.append(Sql.getValue(sugestaoVendaProd.cdEmpresa)).append(",");
        sql.append(Sql.getValue(sugestaoVendaProd.cdSugestaoVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVendaProd.cdProduto)).append(",");
        sql.append(Sql.getValue(sugestaoVendaProd.qtUnidadesVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVendaProd.nuCarimbo)).append(",");
        sql.append(Sql.getValue(sugestaoVendaProd.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(sugestaoVendaProd.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(sugestaoVendaProd.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(sugestaoVendaProd.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(sugestaoVendaProd.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", sugestaoVendaProd.cdEmpresa);
		sqlWhereClause.addAndConditionOr("CDSUGESTAOVENDA = ", StringUtil.split(sugestaoVendaProd.cdSugestaoVenda, ','));
		sqlWhereClause.addAndCondition("CDPRODUTO = ", sugestaoVendaProd.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}