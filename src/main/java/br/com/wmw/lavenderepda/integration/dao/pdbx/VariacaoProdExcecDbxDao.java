package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdExcec;
import totalcross.sql.ResultSet;

public class VariacaoProdExcecDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VariacaoProdExcec();
	}

    private static VariacaoProdExcecDbxDao instance;

    public VariacaoProdExcecDbxDao() {
        super(VariacaoProdExcec.TABLE_NAME);
    }

    public static VariacaoProdExcecDbxDao getInstance() {
        if (instance == null) {
            instance = new VariacaoProdExcecDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VariacaoProdExcec variacaoProdExcec = new VariacaoProdExcec();
        variacaoProdExcec.rowKey = rs.getString("rowkey");
        variacaoProdExcec.cdEmpresa = rs.getString("cdEmpresa");
        variacaoProdExcec.cdRepresentante = rs.getString("cdRepresentante");
        variacaoProdExcec.cdProduto = rs.getString("cdProduto");
        variacaoProdExcec.cdRegiao = rs.getString("cdRegiao");
        variacaoProdExcec.cdCategoria = rs.getString("cdCategoria");
        variacaoProdExcec.vlPctVariacao = ValueUtil.round(rs.getDouble("vlPctVariacao"));
        variacaoProdExcec.nuCarimbo = rs.getInt("nuCarimbo");
        variacaoProdExcec.flTipoAlteracao = rs.getString("flTipoAlteracao");
        variacaoProdExcec.cdUsuario = rs.getString("cdUsuario");
        return variacaoProdExcec;
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
        sql.append(" CDPRODUTO,");
        sql.append(" CDREGIAO,");
        sql.append(" CDCATEGORIA,");
        sql.append(" VLPCTVARIACAO,");
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
        sql.append(" CDPRODUTO,");
        sql.append(" CDREGIAO,");
        sql.append(" CDCATEGORIA,");
        sql.append(" VLPCTVARIACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdExcec variacaoProdExcec = (VariacaoProdExcec) domain;
        sql.append(Sql.getValue(variacaoProdExcec.cdEmpresa)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.cdRepresentante)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.cdProduto)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.cdRegiao)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.cdCategoria)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.vlPctVariacao)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.nuCarimbo)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(variacaoProdExcec.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdExcec varicaoProdExcec = (VariacaoProdExcec) domain;
        sql.append(" VLPCTVARIACAO = ").append(Sql.getValue(varicaoProdExcec.vlPctVariacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(varicaoProdExcec.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(varicaoProdExcec.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(varicaoProdExcec.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdExcec varicaoProdExcec = (VariacaoProdExcec) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", varicaoProdExcec.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", varicaoProdExcec.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", varicaoProdExcec.cdProduto);
		sqlWhereClause.addAndCondition("CDREGIAO = ", varicaoProdExcec.cdRegiao);
		sqlWhereClause.addAndCondition("CDCATEGORIA = ", varicaoProdExcec.cdCategoria);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}