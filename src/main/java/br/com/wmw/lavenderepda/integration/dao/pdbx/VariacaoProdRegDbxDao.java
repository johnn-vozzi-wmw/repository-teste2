package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdReg;
import totalcross.sql.ResultSet;

public class VariacaoProdRegDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VariacaoProdReg();
	}

    private static VariacaoProdRegDbxDao instance;

    public VariacaoProdRegDbxDao() {
        super(VariacaoProdReg.TABLE_NAME);
    }

    public static VariacaoProdRegDbxDao getInstance() {
        if (instance == null) {
            instance = new VariacaoProdRegDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VariacaoProdReg varicaoProdRegiao = new VariacaoProdReg();
        varicaoProdRegiao.rowKey = rs.getString("rowkey");
        varicaoProdRegiao.cdEmpresa = rs.getString("cdEmpresa");
        varicaoProdRegiao.cdRepresentante = rs.getString("cdRepresentante");
        varicaoProdRegiao.cdProduto = rs.getString("cdProduto");
        varicaoProdRegiao.cdRegiao = rs.getString("cdRegiao");
        varicaoProdRegiao.vlPctVariacao = ValueUtil.round(rs.getDouble("vlPctVariacao"));
        varicaoProdRegiao.nuCarimbo = rs.getInt("nuCarimbo");
        varicaoProdRegiao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        varicaoProdRegiao.cdUsuario = rs.getString("cdUsuario");
        return varicaoProdRegiao;
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
        sql.append(" VLPCTVARIACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdReg varicaoProdRegiao = (VariacaoProdReg) domain;
        sql.append(Sql.getValue(varicaoProdRegiao.cdEmpresa)).append(",");
        sql.append(Sql.getValue(varicaoProdRegiao.cdRepresentante)).append(",");
        sql.append(Sql.getValue(varicaoProdRegiao.cdProduto)).append(",");
        sql.append(Sql.getValue(varicaoProdRegiao.cdRegiao)).append(",");
        sql.append(Sql.getValue(varicaoProdRegiao.vlPctVariacao)).append(",");
        sql.append(Sql.getValue(varicaoProdRegiao.nuCarimbo)).append(",");
        sql.append(Sql.getValue(varicaoProdRegiao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(varicaoProdRegiao.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdReg varicaoProdRegiao = (VariacaoProdReg) domain;
        sql.append(" VLPCTVARIACAO = ").append(Sql.getValue(varicaoProdRegiao.vlPctVariacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(varicaoProdRegiao.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(varicaoProdRegiao.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(varicaoProdRegiao.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VariacaoProdReg varicaoProdRegiao = (VariacaoProdReg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", varicaoProdRegiao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", varicaoProdRegiao.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", varicaoProdRegiao.cdProduto);
		sqlWhereClause.addAndCondition("CDREGIAO = ", varicaoProdRegiao.cdRegiao);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}