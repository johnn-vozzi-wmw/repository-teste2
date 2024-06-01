package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdCateg;
import totalcross.sql.ResultSet;

public class VariacaoProdCategDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VariacaoProdCateg();
	}

    private static VariacaoProdCategDbxDao instance;

    public VariacaoProdCategDbxDao() {
        super(VariacaoProdCateg.TABLE_NAME);
    }

    public static VariacaoProdCategDbxDao getInstance() {
        if (instance == null) {
            instance = new VariacaoProdCategDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VariacaoProdCateg varicaoProdCategoria = new VariacaoProdCateg();
        varicaoProdCategoria.rowKey = rs.getString("rowkey");
        varicaoProdCategoria.cdEmpresa = rs.getString("cdEmpresa");
        varicaoProdCategoria.cdRepresentante = rs.getString("cdRepresentante");
        varicaoProdCategoria.cdProduto = rs.getString("cdProduto");
        varicaoProdCategoria.cdCategoria = rs.getString("cdCategoria");
        varicaoProdCategoria.vlPctVariacao = ValueUtil.round(rs.getDouble("vlPctVariacao"));
        varicaoProdCategoria.nuCarimbo = rs.getInt("nuCarimbo");
        varicaoProdCategoria.flTipoAlteracao = rs.getString("flTipoAlteracao");
        varicaoProdCategoria.cdUsuario = rs.getString("cdUsuario");
        return varicaoProdCategoria;
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
        sql.append(" CDCATEGORIA,");
        sql.append(" VLPCTVARIACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdCateg varicaoProdCategoria = (VariacaoProdCateg) domain;
        sql.append(Sql.getValue(varicaoProdCategoria.cdEmpresa)).append(",");
        sql.append(Sql.getValue(varicaoProdCategoria.cdRepresentante)).append(",");
        sql.append(Sql.getValue(varicaoProdCategoria.cdProduto)).append(",");
        sql.append(Sql.getValue(varicaoProdCategoria.cdCategoria)).append(",");
        sql.append(Sql.getValue(varicaoProdCategoria.vlPctVariacao)).append(",");
        sql.append(Sql.getValue(varicaoProdCategoria.nuCarimbo)).append(",");
        sql.append(Sql.getValue(varicaoProdCategoria.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(varicaoProdCategoria.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdCateg varicaoProdCategoria = (VariacaoProdCateg) domain;
        sql.append(" VLPCTVARIACAO = ").append(Sql.getValue(varicaoProdCategoria.vlPctVariacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(varicaoProdCategoria.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(varicaoProdCategoria.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(varicaoProdCategoria.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdCateg varicaoProdCategoria = (VariacaoProdCateg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", varicaoProdCategoria.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", varicaoProdCategoria.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", varicaoProdCategoria.cdProduto);
		sqlWhereClause.addAndCondition("CDCATEGORIA = ", varicaoProdCategoria.cdCategoria);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}