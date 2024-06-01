package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMeta;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PlatVendaMetaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlatVendaMeta();
	}
	
	private static PlatVendaMetaDbxDao instance;

	public PlatVendaMetaDbxDao() {
		super(PlatVendaMeta.TABLE_NAME);
	}
	
	public static PlatVendaMetaDbxDao getInstance() {
		if (instance == null) {
			instance = new PlatVendaMetaDbxDao();
		}
		return instance;
	}

	@Override
	protected void addInsertColumns(StringBuffer arg0) throws SQLException {

	}

	@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {

	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPLATVENDAMETA,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDCENTROCUSTO,");
        sql.append(" tb.CDPLATAFORMAVENDA,");
        sql.append(" tb.DTMETA,");
        sql.append(" tb.VLMETACONTRATADA,");
        sql.append(" tb.VLMETAPLANEJADAREP,");
        sql.append(" tb.VLMETAPLANEJADASUP,");
        sql.append(" tb.FLPLANEJADO,");
        sql.append(" tb.FLENCERRADO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.CDUSUARIO");
	}

	@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PlatVendaMeta platVendaMeta = (PlatVendaMeta) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", platVendaMeta.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", platVendaMeta.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPLATVENDAMETA = ", platVendaMeta.cdPlatVendaMeta);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", platVendaMeta.cdCliente);
		sqlWhereClause.addAndCondition("tb.CDCENTROCUSTO = ", platVendaMeta.cdCentroCusto);
		sqlWhereClause.addAndCondition("tb.CDPLATAFORMAVENDA = ", platVendaMeta.cdPlataformaVenda);
		if (ValueUtil.isNotEmpty(platVendaMeta.dsCidade)) {
			sqlWhereClause.addAndCondition("tc.DSCIDADECOMERCIAL LIKE ", platVendaMeta.dsCidade);
		}
		
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PlatVendaMeta platVendaMeta = new PlatVendaMeta();
		platVendaMeta.cdEmpresa = rs.getString("cdEmpresa");
		platVendaMeta.cdRepresentante = rs.getString("cdRepresentante");
		platVendaMeta.cdPlatVendaMeta = rs.getString("cdPlatVendaMeta");
		platVendaMeta.cdCliente = rs.getString("cdCliente");
		platVendaMeta.cdCentroCusto = rs.getString("cdCentroCusto");
		platVendaMeta.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
		platVendaMeta.dtMeta  = rs.getDate("dtMeta");
		return platVendaMeta;
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		PlatVendaMeta filter = (PlatVendaMeta) domainFilter;
		if (filter != null && ValueUtil.isNotEmpty(filter.dsCidade)) {
			sql.append(" JOIN TBLVPCLIENTE tc");
			sql.append(" ON tc.CDEMPRESA = tb.CDEMPRESA");
			sql.append(" AND tc.CDREPRESENTANTE = tb.CDREPRESENTANTE");
			sql.append(" AND tc.CDCLIENTE = tb.CDCLIENTE");
		}
	}
	
	public Vector findAllByPeriodo(PlatVendaMeta platVendaMeta, boolean groupedBy) throws SQLException {
		Vector platVendaMetaList = new Vector();
		StringBuffer sql = getSqlBuffer();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sql.append("SELECT ");
		addSelectColumns(platVendaMeta, sql);
		sql.append(", perid.CDPERIODO");
		sql.append(" FROM TBLVPPLATVENDAMETA tb ");
		addJoinPeriodo(platVendaMeta, sql);
		
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", platVendaMeta.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", platVendaMeta.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", platVendaMeta.cdCliente);
		sqlWhereClause.addAndCondition("tb.CDCENTROCUSTO = ", platVendaMeta.cdCentroCusto);
		sqlWhereClause.addAndCondition("tb.CDPLATAFORMAVENDA = ", platVendaMeta.cdPlataformaVenda);
		sqlWhereClause.addAndCondition("rede.CDREDE = ", platVendaMeta.cdRede);
		sqlWhereClause.addAndCondition("perid.CDPERIODO = ", platVendaMeta.cdPeriodo);
		sqlWhereClause.addStartAndMultipleCondition();
		sqlWhereClause.addAndCondition("tb.DTMETA >= perid.DTINICIAL");
		sqlWhereClause.addAndCondition("tb.DTMETA <= perid.DTFINAL");
		sqlWhereClause.addEndMultipleCondition();
		sql.append(sqlWhereClause.getSql());
		if (groupedBy) {
			sql.append(" GROUP BY tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDCLIENTE, tb.CDCENTROCUSTO, tb.CDPLATAFORMAVENDA");
		}
		sql.append(" ORDER BY DTMETA ASC");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			PlatVendaMeta platVendaMetaEx;
			while (rs.next()) {
				platVendaMetaEx = (PlatVendaMeta) populate(platVendaMeta, rs);
				platVendaMetaEx.cdPeriodo = rs.getString("cdPeriodo");
				platVendaMetaList.addElement(platVendaMetaEx);
			}
			return platVendaMetaList;
		}
	}
	
	private void addJoinPeriodo(BaseDomain domainFilter, StringBuffer sql) {
		PlatVendaMeta filter = (PlatVendaMeta) domainFilter;
		
		sql.append(" JOIN TBLVPPLATVENDAMETAPERID perid");
		sql.append(" ON perid.CDEMPRESA = tb.CDEMPRESA");
		sql.append(" AND perid.CDREPRESENTANTE = tb.CDREPRESENTANTE");
		
		if (filter != null && ValueUtil.isNotEmpty(filter.dsCidade)) {
			sql.append(" JOIN TBLVPCLIENTE cl");
			sql.append(" ON cl.CDEMPRESA = tb.CDEMPRESA");
			sql.append(" AND cl.CDREPRESENTANTE = tb.CDREPRESENTANTE");
			sql.append(" AND cl.CDCLIENTE  = tb.CDCLIENTE");
			sql.append(" AND cl.DSCIDADECOMERCIAL = " + Sql.getValue(filter.dsCidade) + " ");
		}
		
		if (filter != null && ValueUtil.isNotEmpty(filter.cdRede)) {
			sql.append(" JOIN TBLVPREDECLIENTE rede");
			sql.append(" ON rede.CDCLIENTE = tb.CDCLIENTE");
		}
		
	}
	
	public PlatVendaMeta getPlatVendaMeta(int diasAviso, int diasBloqueio) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();

		StringBuffer sql = getSqlBuffer();

		sql.append(" SELECT ");
		addSelectColumns(domainFilter, sql);
		sql.append(" FROM TBLVPPLATVENDAMETA tb");
		sql.append(" WHERE");
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			sql.append(" CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
			sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.getCdRepresentanteFiltroDados(getClass())));
			sql.append(" AND ");
		}
		
		sql.append(" date('now', 'localtime') >=  date(DTMETA,'-").append(diasAviso + diasBloqueio).append(" day')");
		sql.append(" AND date('now', 'localtime') < date(DTMETA,'-").append(diasBloqueio).append(" day')");
		sql.append(" AND FLPLANEJADO != 'S'");
		sql.append(" ORDER BY DTMETA ASC");
		sql.append(" LIMIT 1");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (PlatVendaMeta) populate(domainFilter, rs);
			}
		}
		return null;
	}

}
