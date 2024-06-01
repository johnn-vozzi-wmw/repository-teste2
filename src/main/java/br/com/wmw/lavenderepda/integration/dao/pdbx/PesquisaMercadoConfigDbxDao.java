package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConcorrente;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.business.domain.Produto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PesquisaMercadoConfigDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaMercadoConfig();
	}

	private static PesquisaMercadoConfigDbxDao instance;

	public PesquisaMercadoConfigDbxDao() {
		super(PesquisaMercadoConfig.TABLE_NAME);
	}

	public static PesquisaMercadoConfigDbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoConfigDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaMercadoConfig pesquisaMercadoConfig = new PesquisaMercadoConfig();
		pesquisaMercadoConfig.rowKey = rs.getString("rowkey");
		pesquisaMercadoConfig.cdEmpresa = rs.getString("cdEmpresa");
		pesquisaMercadoConfig.cdRepresentante = rs.getString("cdRepresentante");
		pesquisaMercadoConfig.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
		pesquisaMercadoConfig.dsPesquisaMercado = rs.getString("dsPesquisaMercado");
		pesquisaMercadoConfig.dtInicialVigencia = rs.getDate("dtInicialVigencia");
		pesquisaMercadoConfig.dtFimVigencia = rs.getDate("dtFimVigencia");
		return pesquisaMercadoConfig;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		getColumns(sql);
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDPESQUISAMERCADOCONFIG,");
		sql.append(" tb.DSPESQUISAMERCADO,");
		sql.append(" tb.DTINICIALVIGENCIA,");
		sql.append(" tb.DTFIMVIGENCIA,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.NUCARIMBO,");
		sql.append(" tb.FLTIPOALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoConfig pesquisaMercadoConfig = (PesquisaMercadoConfig) domain;
		sql.append(Sql.getValue(pesquisaMercadoConfig.cdEmpresa)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.cdRepresentante)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.cdPesquisaMercadoConfig)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.dsPesquisaMercado)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.dtInicialVigencia)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.dtFimVigencia)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.nuCarimbo)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoConfig.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoConfig pesquisaMercadoConfig = (PesquisaMercadoConfig) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pesquisaMercadoConfig.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", pesquisaMercadoConfig.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoConfig.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition("tb.DTINICIALVIGENCIA <= ", pesquisaMercadoConfig.dtFimVigencia);
		sqlWhereClause.addAndCondition("tb.DTFIMVIGENCIA >= ", pesquisaMercadoConfig.dtInicialVigencia);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findAllByExampleWithQuantidades(PesquisaMercadoConfig pesquisaMercadoConfigFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectSummaryColumns(pesquisaMercadoConfigFilter, sql);
		sql.append(", count(distinct p.CDPRODUTO) AS QTITENS");
		sql.append(", count(distinct c.CDCONCORRENTE) AS QTCONCORRENTES");
		sql.append(" FROM ");
		sql.append(PesquisaMercadoConfig.TABLE_NAME);
		sql.append(" tb join ");
		sql.append(PesquisaMercadoProduto.TABLE_NAME);
		sql.append(" p ON tb.CDEMPRESA = p.CDEMPRESA");
		sql.append(" AND tb.CDPESQUISAMERCADOCONFIG = p.CDPESQUISAMERCADOCONFIG");
		sql.append(" join ");
		sql.append(Produto.TABLE_NAME);
		sql.append(" prod ON prod.CDPRODUTO = p.CDPRODUTO");
		sql.append(" AND prod.CDEMPRESA = p.CDEMPRESA ");
		sql.append(" join ");
		sql.append(PesquisaMercadoConcorrente.TABLE_NAME);
		sql.append(" c ON tb.CDEMPRESA = c.CDEMPRESA");
		sql.append(" AND tb.CDPESQUISAMERCADOCONFIG = c.CDPESQUISAMERCADOCONFIG");
		addWhereByExample(pesquisaMercadoConfigFilter, sql);
		addGroupBySummary(pesquisaMercadoConfigFilter, sql);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateSummary(pesquisaMercadoConfigFilter, rs));
			}
		}
		return list;
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaMercadoConfig pesquisaMercadoConfig = new PesquisaMercadoConfig();
		pesquisaMercadoConfig.cdEmpresa = rs.getString("cdEmpresa");
		pesquisaMercadoConfig.cdRepresentante = rs.getString("cdRepresentante");
		pesquisaMercadoConfig.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
		pesquisaMercadoConfig.dsPesquisaMercado = rs.getString("dsPesquisaMercado");
		pesquisaMercadoConfig.dtInicialVigencia = rs.getDate("dtInicialVigencia");
		pesquisaMercadoConfig.dtFimVigencia = rs.getDate("dtFimVigencia");
		pesquisaMercadoConfig.qtItensPesquisa = rs.getInt("qtItens");
		pesquisaMercadoConfig.qtConcorrentesPesquisa = rs.getInt("qtConcorrentes");
		return pesquisaMercadoConfig;
	}

	@Override
	protected void addGroupBySummary(BaseDomain domain, StringBuffer sql) {
		sql.append(" group by ");
		addSelectSummaryColumns(domain, sql);
	}

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDPESQUISAMERCADOCONFIG,");
		sql.append(" tb.DSPESQUISAMERCADO,");
		sql.append(" tb.DTINICIALVIGENCIA,");
		sql.append(" tb.DTFIMVIGENCIA");
	}

	public Vector findAllByExampleWithQuantidadesAdicionadas(PesquisaMercadoConfig pesquisaMercadoConfigFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectSummaryColumns(pesquisaMercadoConfigFilter, sql);
		sql.append(", count(distinct p.CDPRODUTO) AS QTITENS");
		sql.append(", count(distinct c.CDCONCORRENTE) AS QTCONCORRENTES");
		sql.append(" FROM ");
		sql.append(PesquisaMercadoConfig.TABLE_NAME);
		sql.append(" tb join (SELECT ");
		sql.append(" p.CDEMPRESA,");
		sql.append(" p.CDPESQUISAMERCADOCONFIG,");
		sql.append(" p.CDPRODUTO from ");
		sql.append(PesquisaMercadoProduto.TABLE_NAME);
		sql.append(" p join ");
		sql.append(Produto.TABLE_NAME);
		sql.append(" prod ON prod.CDPRODUTO = p.CDPRODUTO");
		sql.append(" AND prod.CDEMPRESA = p.CDEMPRESA ");
		sql.append(" union all SELECT");
		sql.append(" reg.CDEMPRESA,");
		sql.append(" reg.CDPESQUISAMERCADOCONFIG,");
		sql.append(" reg.CDPRODUTO from ");
		sql.append(PesquisaMercadoReg.TABLE_NAME);
		sql.append(" reg");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			sqlWhereClause.addAndCondition("reg.CDCLIENTE = ", ValueUtil.VALOR_ZERO);
		} else {
			sqlWhereClause.addAndCondition("reg.CDCLIENTE = ", SessionLavenderePda.getCliente().cdCliente);
		}
		sql.append(sqlWhereClause.getSql());
		sql.append(" group by CDEMPRESA, CDPESQUISAMERCADOCONFIG, CDPRODUTO");
		sql.append(" ) p ON tb.CDEMPRESA = p.CDEMPRESA");
		sql.append(" AND tb.CDPESQUISAMERCADOCONFIG = p.CDPESQUISAMERCADOCONFIG");
		sql.append(" join ");
		sql.append(PesquisaMercadoConcorrente.TABLE_NAME);
		sql.append(" c ON tb.CDEMPRESA = c.CDEMPRESA");
		sql.append(" AND tb.CDPESQUISAMERCADOCONFIG = c.CDPESQUISAMERCADOCONFIG");
		addWhereByExample(pesquisaMercadoConfigFilter, sql);
		addGroupBySummary(pesquisaMercadoConfigFilter, sql);
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateSummary(pesquisaMercadoConfigFilter, rs));
			}
		}
		return list;

	}

	public Vector findAllPesquisaMercadoExpirada(Date currentDate) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(domainFilter, sql);
		sql.append(" FROM ");
		sql.append(PesquisaMercadoConfig.TABLE_NAME);
		sql.append(" tb ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.DTFIMVIGENCIA < ", currentDate);
		sql.append(sqlWhereClause.getSql());
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populate(domainFilter, rs));
			}
		}
		return list;
	}
}
