package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CestaPositProduto;
import totalcross.sql.ResultSet;

public class CestaPositProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CestaPositProduto();
	}

    private static CestaPositProdutoPdbxDao instance;

    public CestaPositProdutoPdbxDao() {
        super(CestaPositProduto.TABLE_NAME);
    }

    public static CestaPositProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new CestaPositProdutoPdbxDao();
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
        sql.append(" CDCAMPANHA,");
        sql.append(" CDCESTA,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" vlMeta,");
        sql.append(" vlRealizado,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	CestaPositProduto cestapositproduto = new CestaPositProduto();
        cestapositproduto.rowKey = rs.getString("rowkey");
        cestapositproduto.cdEmpresa = rs.getString("cdEmpresa");
        cestapositproduto.cdRepresentante = rs.getString("cdRepresentante");
        cestapositproduto.cdCampanha = rs.getString("cdCampanha");
        cestapositproduto.cdCesta = rs.getString("cdCesta");
        cestapositproduto.cdProduto = rs.getString("cdProduto");
        cestapositproduto.cdCliente = rs.getString("cdCliente");
        cestapositproduto.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        cestapositproduto.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
        cestapositproduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cestapositproduto.cdUsuario = rs.getString("cdUsuario");
        return cestapositproduto;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	CestaPositProduto cestapositproduto = (CestaPositProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", cestapositproduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cestapositproduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCAMPANHA = ", cestapositproduto.cdCampanha);
		sqlWhereClause.addAndCondition("CDCESTA = ", cestapositproduto.cdCesta);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", cestapositproduto.cdProduto);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", cestapositproduto.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}