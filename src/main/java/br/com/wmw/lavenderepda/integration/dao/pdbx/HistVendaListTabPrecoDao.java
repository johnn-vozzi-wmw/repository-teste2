package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.HistVendaListTabPreco;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class HistVendaListTabPrecoDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new HistVendaListTabPreco();
	}

	private static HistVendaListTabPrecoDao instance;
	
	public HistVendaListTabPrecoDao() {
		super(HistVendaListTabPreco.TABLE_NAME);
	}
	
	public static HistVendaListTabPrecoDao getInstance() {
		if (instance == null) {
			instance = new HistVendaListTabPrecoDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		HistVendaListTabPreco histVendasListaTabPreco = populateDefault(rs);
		histVendasListaTabPreco.grupoProduto1 = new GrupoProduto1();
		histVendasListaTabPreco.grupoProduto1.dsGrupoproduto1 = rs.getString("dsGrupoProduto1");
		histVendasListaTabPreco.grupoProduto1.cdGrupoproduto1 = rs.getString("cdGrupoProduto1");
		return histVendasListaTabPreco;
	}

	private HistVendaListTabPreco populateDefault(ResultSet rs) throws SQLException {
		HistVendaListTabPreco histVendasListaTabPreco = new HistVendaListTabPreco();
		histVendasListaTabPreco.cdEmpresa = rs.getString("cdEmpresa");
		histVendasListaTabPreco.cdRepresentante = rs.getString("cdRepresentante");
		histVendasListaTabPreco.cdCliente = rs.getString("cdCliente");
		histVendasListaTabPreco.cdColunaTabelaPreco = rs.getInt("cdColunaTabelaPreco");
		histVendasListaTabPreco.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
		histVendasListaTabPreco.vlRealizado = rs.getDouble("vlRealizado");
		histVendasListaTabPreco.qtRealizado = rs.getDouble("qtRealizado");
		return histVendasListaTabPreco;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		addDefaultSelectColumns(sql)
		.append(" '' as DSGRUPOPRODUTO1");
	}

	private StringBuffer addDefaultSelectColumns(StringBuffer sql) {
		return sql.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" CDCLIENTE,")
		.append(" CDLISTATABELAPRECO,")
		.append(" CDCOLUNATABELAPRECO,")
		.append(" CDGRUPOPRODUTO1,")
		.append(" VLREALIZADO,")
		.append(" QTREALIZADO,");
	}
	
	
	private BaseDomain populateDetalhe(ResultSet rs) throws SQLException {
		HistVendaListTabPreco histVendasListaTabPreco = populateDefault(rs);
		histVendasListaTabPreco.cdListaTabelaPreco = rs.getInt("cdListaTabelaPreco");
		histVendasListaTabPreco.dsListaTabelaPreco = rs.getString("dsListaTabelaPreco");
		return histVendasListaTabPreco;
	}
	
	public Vector findAllDetalhesByExample(BaseDomain domain) throws SQLException {
		HistVendaListTabPreco hist = (HistVendaListTabPreco) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DSLISTATABELAPRECO, CDEMPRESA, CDREPRESENTANTE, CDCLIENTE, CDLISTATABELAPRECO, CDCOLUNATABELAPRECO, CDGRUPOPRODUTO1,")
		.append(" SUM(QTREALIZADO) QTREALIZADO, SUM(VLREALIZADO) VLREALIZADO FROM (")
		.append(" SELECT SUM(QTITEMFISICO) QTREALIZADO, SUM(VLTOTALITEMPEDIDO) VLREALIZADO,")
		.append(" TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDCLIENTE, TAB.CDLISTATABELAPRECO, TAB.CDCOLUNATABELAPRECO, lista.DSLISTATABELAPRECO, PROD.CDGRUPOPRODUTO1")
		.append(" FROM TBLVPITEMPEDIDO IT")
		.append(" JOIN TBLVPPRODUTO PROD ON IT.CDEMPRESA = PROD.CDEMPRESA")
		.append(" AND PROD.CDPRODUTO = IT.CDPRODUTO")
		.append(" AND PROD.CDREPRESENTANTE = IT.CDREPRESENTANTE")
		.append(" AND PROD.CDGRUPOPRODUTO1 = ").append(Sql.getValue(hist.cdGrupoProduto1))
		.append(" JOIN TBLVPPEDIDO TB ON TB.CDEMPRESA = IT.CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = IT.CDREPRESENTANTE")
		.append(" AND TB.NUPEDIDO = IT.NUPEDIDO")
		.append(" AND TB.FLORIGEMPEDIDO = IT.FLORIGEMPEDIDO")
		.append(" JOIN TBLVPTABELAPRECO TAB ON TB.CDEMPRESA = TAB.CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = TAB.CDREPRESENTANTE")
		.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO")
		.append(" LEFT JOIN TBLVPLISTATABELAPRECO lista ON")
		.append(" lista.CDEMPRESA = tb.CDEMPRESA AND")
		.append(" lista.CDREPRESENTANTE = tab.CDREPRESENTANTE AND")
		.append(" lista.CDLISTATABELAPRECO = tab.CDLISTATABELAPRECO")
		.append(" WHERE TB.CDEMPRESA = ").append(Sql.getValue(hist.cdEmpresa))
		.append(" AND TB.CDCLIENTE = ").append(Sql.getValue(hist.cdCliente))
		.append(" AND TAB.CDCOLUNATABELAPRECO = ").append(Sql.getValue(hist.cdColunaTabelaPreco))
		.append(" GROUP BY TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDCLIENTE, TAB.CDLISTATABELAPRECO,")
		.append(" TAB.CDCOLUNATABELAPRECO, PROD.CDGRUPOPRODUTO1, lista.DSLISTATABELAPRECO")
		.append(" UNION ALL")
		.append(" SELECT TB.QTREALIZADO, TB.VLREALIZADO, TB.CDEMPRESA, TB.CDREPRESENTANTE,")
		.append(" TB.CDCLIENTE, TB.CDLISTATABELAPRECO, TB.CDCOLUNATABELAPRECO, lista.DSLISTATABELAPRECO, TB.CDGRUPOPRODUTO1")
		.append(" FROM TBLVPHISTVENDALISTTABPRECO TB")
		.append(" LEFT JOIN TBLVPLISTATABELAPRECO lista ON")
		.append(" lista.CDEMPRESA = tb.CDEMPRESA AND")
		.append(" lista.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
		.append(" lista.CDLISTATABELAPRECO = tb.CDLISTATABELAPRECO")
		.append(" WHERE TB.CDEMPRESA = ").append(Sql.getValue(hist.cdEmpresa))
		.append(" AND TB.CDREPRESENTANTE = ").append(Sql.getValue(hist.cdRepresentante))
		.append(" AND TB.CDCLIENTE = ").append(Sql.getValue(hist.cdCliente))
		.append(" AND TB.CDCOLUNATABELAPRECO = ").append(Sql.getValue(hist.cdColunaTabelaPreco))
		.append(" AND TB.CDGRUPOPRODUTO1 = ").append(Sql.getValue(hist.cdGrupoProduto1))
		.append(") a GROUP BY DSLISTATABELAPRECO, CDEMPRESA, CDREPRESENTANTE, CDCLIENTE,")
		.append(" CDLISTATABELAPRECO, CDCOLUNATABELAPRECO, CDGRUPOPRODUTO1")
		.append(" ORDER BY CDLISTATABELAPRECO DESC");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				result.addElement(populateDetalhe(rs));
			}
			return result;
		}
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		HistVendaListTabPreco histVendasListaTabPreco = (HistVendaListTabPreco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", histVendasListaTabPreco.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", histVendasListaTabPreco.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", histVendasListaTabPreco.cdCliente);
		sqlWhereClause.addAndCondition("tb.CDLISTATABELAPRECO = ", histVendasListaTabPreco.cdListaTabelaPreco);
		sqlWhereClause.addAndCondition("tb.CDCOLUNATABELAPRECO = ", histVendasListaTabPreco.cdColunaTabelaPreco);
		sqlWhereClause.addAndCondition("tb.CDGRUPOPRODUTO1 = ", histVendasListaTabPreco.cdGrupoProduto1);
		sql.append(sqlWhereClause.getSql());
		if (ValueUtil.isEmpty(histVendasListaTabPreco.cdGrupoProduto1)) {
			sql.append(" GROUP BY tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDCLIENTE, tb.CDLISTATABELAPRECO, tb.CDCOLUNATABELAPRECO,")
			.append(" tb.CDGRUPOPRODUTO1");
		}
	}
	
	@Override
	public Vector findAllByExample(BaseDomain domain) throws SQLException {
		HistVendaListTabPreco hist = (HistVendaListTabPreco) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DSGRUPOPRODUTO1, CDEMPRESA, CDREPRESENTANTE, CDCLIENTE, CDCOLUNATABELAPRECO, CDGRUPOPRODUTO1,");
		if (hist.media) {
			sql.append(" AVG(QTREALIZADO) QTREALIZADO, AVG(VLREALIZADO) VLREALIZADO FROM (");
		} else {
			sql.append(" SUM(QTREALIZADO) QTREALIZADO, SUM(VLREALIZADO) VLREALIZADO FROM (");
		}
		sql.append(" SELECT SUM(QTITEMFISICO) QTREALIZADO, SUM(VLTOTALITEMPEDIDO) VLREALIZADO,")
		.append(" TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDCLIENTE, TAB.CDCOLUNATABELAPRECO, PROD.CDGRUPOPRODUTO1, GRUPO.DSGRUPOPRODUTO1")
		.append(" FROM TBLVPITEMPEDIDO IT")
		.append(" JOIN TBLVPPRODUTO PROD ON IT.CDEMPRESA = PROD.CDEMPRESA")
		.append(" AND PROD.CDPRODUTO = IT.CDPRODUTO")
		.append(" AND PROD.CDREPRESENTANTE = IT.CDREPRESENTANTE")
		.append(" JOIN TBLVPPEDIDO TB ON TB.CDEMPRESA = IT.CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = IT.CDREPRESENTANTE")
		.append(" AND TB.NUPEDIDO = IT.NUPEDIDO")
		.append(" AND TB.FLORIGEMPEDIDO = IT.FLORIGEMPEDIDO")
		.append(" JOIN TBLVPTABELAPRECO TAB ON TB.CDEMPRESA = TAB.CDEMPRESA")
		.append(" AND TB.CDREPRESENTANTE = TAB.CDREPRESENTANTE")
		.append(" AND TB.CDTABELAPRECO = TAB.CDTABELAPRECO")
		.append(" LEFT JOIN TBLVPGRUPOPRODUTO1 GRUPO ON GRUPO.CDGRUPOPRODUTO1 = PROD.CDGRUPOPRODUTO1")
		.append(" WHERE TB.CDEMPRESA = ").append(Sql.getValue(hist.cdEmpresa))
		.append(" AND TB.CDCLIENTE = ").append(Sql.getValue(hist.cdCliente))
		.append(" AND TAB.CDCOLUNATABELAPRECO = ").append(Sql.getValue(hist.cdColunaTabelaPreco));
		if (hist.media) {
			sql.append(" AND TAB.CDLISTATABELAPRECO <> ").append(Sql.getValue(hist.cdListaTabelaPreco));
		} else {
			sql.append(" AND TAB.CDLISTATABELAPRECO = ").append(Sql.getValue(hist.cdListaTabelaPreco));
		}
		sql.append(" GROUP BY TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDCLIENTE, ")
		.append(" TAB.CDCOLUNATABELAPRECO, PROD.CDGRUPOPRODUTO1, GRUPO.DSGRUPOPRODUTO1")
		.append(" UNION ALL")
		.append(" SELECT TB.QTREALIZADO, TB.VLREALIZADO, TB.CDEMPRESA, TB.CDREPRESENTANTE,")
		.append(" TB.CDCLIENTE, TB.CDCOLUNATABELAPRECO, TB.CDGRUPOPRODUTO1, GRUPO.DSGRUPOPRODUTO1")
		.append(" FROM TBLVPHISTVENDALISTTABPRECO TB")
		.append(" LEFT JOIN TBLVPGRUPOPRODUTO1 GRUPO ON GRUPO.CDGRUPOPRODUTO1 = TB.CDGRUPOPRODUTO1")
		.append(" WHERE TB.CDEMPRESA = ").append(Sql.getValue(hist.cdEmpresa))
		.append(" AND TB.CDREPRESENTANTE = ").append(Sql.getValue(hist.cdRepresentante))
		.append(" AND TB.CDCLIENTE = ").append(Sql.getValue(hist.cdCliente))
		.append(" AND TB.CDCOLUNATABELAPRECO = ").append(Sql.getValue(hist.cdColunaTabelaPreco));
		if (hist.media) {
			sql.append(" AND TB.CDLISTATABELAPRECO <> ").append(Sql.getValue(hist.cdListaTabelaPreco));
		} else {
			sql.append(" AND TB.CDLISTATABELAPRECO = ").append(Sql.getValue(hist.cdListaTabelaPreco));
		}
		sql.append(") a GROUP BY DSGRUPOPRODUTO1, CDEMPRESA, CDREPRESENTANTE, CDCLIENTE,")
		.append(" CDCOLUNATABELAPRECO, CDGRUPOPRODUTO1");
		return findAll(null, sql.toString());
	}

}
