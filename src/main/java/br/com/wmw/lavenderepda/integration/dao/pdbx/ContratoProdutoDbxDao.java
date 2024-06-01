package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ContratoProduto;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class ContratoProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ContratoProduto();
	}

    private static ContratoProdutoDbxDao instance;

    public ContratoProdutoDbxDao() {
        super(ContratoProduto.TABLE_NAME);
    }

    public static ContratoProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new ContratoProdutoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ContratoProduto contratoProduto = new ContratoProduto();
        contratoProduto.rowKey = rs.getString("rowkey");
        contratoProduto.cdEmpresa = rs.getString("cdEmpresa");
        contratoProduto.cdRepresentante = rs.getString("cdRepresentante");
        contratoProduto.cdCliente = rs.getString("cdCliente");
        contratoProduto.flTipoContrato = rs.getString("flTipoContrato");
        contratoProduto.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
        contratoProduto.cdProduto = rs.getString("cdProduto");
        contratoProduto.qtProdutoContrato = ValueUtil.round(rs.getDouble("qtProdutoContrato"));
        contratoProduto.nuCarimbo = rs.getInt("nuCarimbo");
        contratoProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        contratoProduto.cdUsuario = rs.getString("cdUsuario");
        return contratoProduto;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		ContratoProduto contratoProduto = new ContratoProduto();
		contratoProduto.rowKey = rs.getString("rowkey");
		contratoProduto.cdEmpresa = rs.getString("cdEmpresa");
		contratoProduto.cdRepresentante = rs.getString("cdRepresentante");
		contratoProduto.cdCliente = rs.getString("cdCliente");
		contratoProduto.flTipoContrato = rs.getString("flTipoContrato");
		contratoProduto.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
		contratoProduto.cdProduto = rs.getString("cdProduto");
		contratoProduto.qtProdutoContrato = ValueUtil.round(rs.getDouble("qtProdutoContrato"));
		contratoProduto.nuCarimbo = rs.getInt("nuCarimbo");
		contratoProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
		contratoProduto.cdUsuario = rs.getString("cdUsuario");
		return contratoProduto;
	}

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDCLIENTE,");
		sql.append(" FLTIPOCONTRATO,");
		sql.append(" DTVIGENCIAINICIAL,");
		sql.append(" CDPRODUTO,");
		sql.append(" QTPRODUTOCONTRATO,");
		sql.append(" nuCarimbo,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO");
	}

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDCLIENTE,");
		sql.append(" tb.FLTIPOCONTRATO,");
		sql.append(" tb.DTVIGENCIAINICIAL,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.QTPRODUTOCONTRATO,");
		sql.append(" tb.nuCarimbo,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" tb.CDUSUARIO");
	}

	//@Override
	protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDCLIENTE,");
		sql.append(" FLTIPOCONTRATO,");
		sql.append(" DTVIGENCIAINICIAL,");
		sql.append(" CDPRODUTO,");
		sql.append(" QTPRODUTOCONTRATO,");
		sql.append(" nuCarimbo,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO");
	}

	//@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ContratoProduto contratoProduto = (ContratoProduto) domain;
		sql.append(Sql.getValue(contratoProduto.cdEmpresa)).append(",");
		sql.append(Sql.getValue(contratoProduto.cdRepresentante)).append(",");
		sql.append(Sql.getValue(contratoProduto.cdCliente)).append(",");
		sql.append(Sql.getValue(contratoProduto.flTipoContrato)).append(",");
		sql.append(Sql.getValue(contratoProduto.dtVigenciaInicial)).append(",");
		sql.append(Sql.getValue(contratoProduto.cdProduto)).append(",");
		sql.append(Sql.getValue(contratoProduto.qtProdutoContrato)).append(",");
		sql.append(Sql.getValue(contratoProduto.nuCarimbo)).append(",");
		sql.append(Sql.getValue(contratoProduto.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(contratoProduto.cdUsuario));
	}

	//@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ContratoProduto contratoProduto = (ContratoProduto) domain;
		sql.append(" QTPRODUTOCONTRATO = ").append(Sql.getValue(contratoProduto.qtProdutoContrato)).append(",");
		sql.append(" nuCarimbo = ").append(Sql.getValue(contratoProduto.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(contratoProduto.flTipoAlteracao)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(contratoProduto.cdUsuario));
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ContratoProduto contratoProduto = (ContratoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", contratoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", contratoProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", contratoProduto.cdCliente);
		sqlWhereClause.addAndCondition("FLTIPOCONTRATO = ", contratoProduto.flTipoContrato);
		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL = ", contratoProduto.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", contratoProduto.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findAllByExampleSummary(BaseDomain domain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectSummaryColumns(domain, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereByExampleWithJoin(domain, sql);
		addOrderBy(sql, domain);
		return findAllSummary(domain, sql.toString());
	}

	protected void addWhereByExampleWithJoin(BaseDomain domain, StringBuffer sql) {
		ContratoProduto contratoProduto = (ContratoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addJoin(", TBLVPPRODUTO p where tb.cdProduto = p.cdProduto and  tb.cdEmpresa = p.cdEmpresa and '" + SessionLavenderePda.usuarioPdaRep.cdRepresentante + "' = p.cdRepresentante");
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", contratoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", contratoProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", contratoProduto.cdCliente);
		sqlWhereClause.addAndCondition("tb.FLTIPOCONTRATO = ", contratoProduto.flTipoContrato);
		sqlWhereClause.addAndCondition("tb.DTVIGENCIAINICIAL = ", contratoProduto.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", contratoProduto.cdProduto);
		sql.append(sqlWhereClause.getSql());
	}

}