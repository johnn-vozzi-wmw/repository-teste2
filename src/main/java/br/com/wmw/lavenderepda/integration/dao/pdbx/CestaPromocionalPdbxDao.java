package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CestaPromocional;
import totalcross.sql.ResultSet;

public class CestaPromocionalPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CestaPromocional();
	}

    private static CestaPromocionalPdbxDao instance;

    public CestaPromocionalPdbxDao() {
        super(CestaPromocional.TABLE_NAME);
    }

    public static CestaPromocionalPdbxDao getInstance() {
        if (instance == null) {
            instance = new CestaPromocionalPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" VLUNITARIO,");
        sql.append(" vlPctMaxDesconto,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CestaPromocional cestapromocional = new CestaPromocional();
        cestapromocional.rowKey = rs.getString("rowkey");
        cestapromocional.cdEmpresa = rs.getString("cdEmpresa");
        cestapromocional.cdRepresentante = rs.getString("cdRepresentante");
        cestapromocional.cdCliente = rs.getString("cdCliente");
        cestapromocional.cdProduto = rs.getString("cdProduto");
        cestapromocional.cdTabelaPreco = rs.getString("cdTabelaPreco");
        cestapromocional.dtVigenciaInicial = rs.getDate("dtVigenciainicial");
        cestapromocional.dtVigenciaFinal = rs.getDate("dtVigenciafinal");
        cestapromocional.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
        cestapromocional.vlPctMaxDesconto = ValueUtil.round(rs.getDouble("vlPctMaxDesconto"));
        cestapromocional.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return cestapromocional;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CestaPromocional cestapromocional = (CestaPromocional) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", cestapromocional.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cestapromocional.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", cestapromocional.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", cestapromocional.cdProduto);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", cestapromocional.cdTabelaPreco);
		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL <= ", cestapromocional.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("DTVIGENCIAFINAL >= ", cestapromocional.dtVigenciaFinal);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}