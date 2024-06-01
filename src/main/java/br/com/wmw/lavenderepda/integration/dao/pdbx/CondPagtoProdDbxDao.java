package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondPagtoProd;
import totalcross.sql.ResultSet;

public class CondPagtoProdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondPagtoProd();
	}

    private static CondPagtoProdDbxDao instance;

    public CondPagtoProdDbxDao() {
        super(CondPagtoProd.TABLE_NAME);
    }
    
    public static CondPagtoProdDbxDao getInstance() {
        if (instance == null) {
            instance = new CondPagtoProdDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondPagtoProd condPagtoProd = new CondPagtoProd();
        condPagtoProd.rowKey = rs.getString("rowkey");
        condPagtoProd.cdEmpresa = rs.getString("cdEmpresa");
        condPagtoProd.cdRepresentante = rs.getString("cdRepresentante");
        condPagtoProd.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condPagtoProd.cdProduto = rs.getString("cdProduto");
        condPagtoProd.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        condPagtoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condPagtoProd.nuCarimbo = rs.getInt("nuCarimbo");
        condPagtoProd.cdUsuario = rs.getString("cdUsuario");
        return condPagtoProd;
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
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLINDICEFINANCEIRO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDPRODUTO,");
        sql.append(" VLINDICEFINANCEIRO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoProd condPagtoProd = (CondPagtoProd) domain;
        sql.append(Sql.getValue(condPagtoProd.cdEmpresa)).append(",");
        sql.append(Sql.getValue(condPagtoProd.cdRepresentante)).append(",");
        sql.append(Sql.getValue(condPagtoProd.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(condPagtoProd.cdProduto)).append(",");
        sql.append(Sql.getValue(condPagtoProd.vlIndiceFinanceiro)).append(",");
        sql.append(Sql.getValue(condPagtoProd.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(condPagtoProd.nuCarimbo)).append(",");
        sql.append(Sql.getValue(condPagtoProd.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoProd condPagtoProd = (CondPagtoProd) domain;
        sql.append(" VLINDICEFINANCEIRO = ").append(Sql.getValue(condPagtoProd.vlIndiceFinanceiro)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(condPagtoProd.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(condPagtoProd.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(condPagtoProd.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondPagtoProd condPagtoProd = (CondPagtoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condPagtoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condPagtoProd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", condPagtoProd.cdCondicaoPagamento);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", condPagtoProd.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}