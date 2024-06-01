package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.business.domain.Produto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PesquisaMercadoProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaMercadoProduto();
	}

	private static PesquisaMercadoProdutoDbxDao instance;

	public PesquisaMercadoProdutoDbxDao() {
		super(PesquisaMercadoProduto.TABLE_NAME);
	}

	public static PesquisaMercadoProdutoDbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoProdutoDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaMercadoProduto pesquisaMercadoProduto = new PesquisaMercadoProduto();
		pesquisaMercadoProduto.rowKey = rs.getString("rowkey");
		pesquisaMercadoProduto.cdEmpresa = rs.getString("cdEmpresa");
		pesquisaMercadoProduto.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
		pesquisaMercadoProduto.cdProduto = rs.getString("cdProduto");
		pesquisaMercadoProduto.dsProduto = rs.getString("dsProduto");
		return pesquisaMercadoProduto;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		getColumns(sql);
		sql.append(", p.DSPRODUTO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDPESQUISAMERCADOCONFIG,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.NUCARIMBO,");
		sql.append(" tb.FLTIPOALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoProduto pesquisaMercadoProduto = (PesquisaMercadoProduto) domain;
		sql.append(Sql.getValue(pesquisaMercadoProduto.cdEmpresa)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoProduto.cdPesquisaMercadoConfig)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoProduto.cdProduto)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoProduto.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoProduto.nuCarimbo)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoProduto.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" join ");
		sql.append(Produto.TABLE_NAME);
		sql.append(" p on p.CDEMPRESA = tb.CDEMPRESA");
		sql.append(" AND p.CDPRODUTO = tb.CDPRODUTO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoProduto pesquisaMercadoProduto = (PesquisaMercadoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pesquisaMercadoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoProduto.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", pesquisaMercadoProduto.cdProduto);
		sqlWhereClause.addAndOrLikeCondition("tb.CDPRODUTO", "p.DSPRODUTO", pesquisaMercadoProduto.dsFiltro);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addGroupBy(StringBuffer sql) {
		sql.append(" group by tb.rowkey");
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoProduto pesquisaMercadoProduto = (PesquisaMercadoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", pesquisaMercadoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoProduto.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition(" CDPRODUTO = ", pesquisaMercadoProduto.cdProduto);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findProdutosAdicionados(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select ");
		sql.append(" reg.rowkey, ");
		sql.append(" reg.CDEMPRESA, ");
		sql.append(" reg.CDPESQUISAMERCADOCONFIG, ");
		sql.append(" reg.CDPRODUTO, ");
		sql.append(" pp.DSPRODUTO ");
		sql.append(" from ");
		sql.append(PesquisaMercadoReg.TABLE_NAME);
		sql.append(" reg left join ");
		sql.append(PesquisaMercadoProduto.TABLE_NAME);
		sql.append(" p on p.CDPRODUTO = reg.CDPRODUTO");
		sql.append(" and p.CDEMPRESA = reg.CDEMPRESA");
		sql.append(" and p.CDPESQUISAMERCADOCONFIG = reg.CDPESQUISAMERCADOCONFIG");
		sql.append(" left join ");
		sql.append(PesquisaMercadoConfig.TABLE_NAME);
		sql.append(" cfg on cfg.CDEMPRESA = p.CDEMPRESA");
		sql.append(" and cfg.CDPESQUISAMERCADOCONFIG = p.CDPESQUISAMERCADOCONFIG");
		sql.append(" join ");
		sql.append(Produto.TABLE_NAME);
		sql.append(" pp on pp.CDPRODUTO = reg.CDPRODUTO");
		sql.append(" and pp.CDREPRESENTANTE = reg.CDREPRESENTANTE");
		sql.append(" and pp.CDEMPRESA = reg.CDEMPRESA");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" p.CDPRODUTO is null ");
		sqlWhereClause.addAndCondition(" reg.CDEMPRESA = ", pesquisaMercadoConfig.cdEmpresa);
		sqlWhereClause.addAndCondition(" reg.CDREPRESENTANTE = ", pesquisaMercadoConfig.cdRepresentante);
		sqlWhereClause.addAndCondition(" reg.CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoConfig.cdPesquisaMercadoConfig);
		if (ValueUtil.isNotEmpty(pesquisaMercadoConfig.dsProdutoFiltro)) {
			sqlWhereClause.addAndOrLikeCondition("pp.DSPRODUTO", "pp.CDPRODUTO", pesquisaMercadoConfig.dsProdutoFiltro);
		}
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			sqlWhereClause.addAndCondition(" reg.CDCLIENTE = ", ValueUtil.VALOR_ZERO);
		} else {
			sqlWhereClause.addAndCondition(" reg.CDCLIENTE = ", SessionLavenderePda.getCliente().cdCliente);
		}
		sql.append(sqlWhereClause.getSql());
		sql.append(" group by pp.CDPRODUTO");
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populate(pesquisaMercadoConfig, rs));
			}
		}
		return list;
	}

	public PesquisaMercadoProduto findByRegRowKey(String rowkey) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select ");
		addSelectColumns(null, sql);
		sql.append(" from ");
		sql.append(PesquisaMercadoReg.TABLE_NAME);
		sql.append(" tb ");
		sql.append(" join ");
		sql.append(Produto.TABLE_NAME);
		sql.append(" p on p.CDPRODUTO = tb.CDPRODUTO");
		sql.append(" and p.CDREPRESENTANTE = tb.CDREPRESENTANTE");
		sql.append(" and p.CDEMPRESA = tb.CDEMPRESA");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.rowkey = ", rowkey);
		sql.append(sqlWhereClause.getSql());
		PesquisaMercadoProduto pesquisaMercadoProduto = null;
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				pesquisaMercadoProduto = (PesquisaMercadoProduto) populate(pesquisaMercadoProduto, rs);
			}
		}
		return pesquisaMercadoProduto;
	}
}
