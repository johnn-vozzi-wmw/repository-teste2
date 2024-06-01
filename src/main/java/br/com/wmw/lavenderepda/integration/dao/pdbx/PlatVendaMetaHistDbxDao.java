package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMetaHist;
import totalcross.sql.ResultSet;

public class PlatVendaMetaHistDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlatVendaMetaHist();
	}

	private static PlatVendaMetaHistDbxDao instance;

	public PlatVendaMetaHistDbxDao() {
		super(PlatVendaMetaHist.TABLE_NAME);
	}

	public static PlatVendaMetaHistDbxDao getInstance() {
		if (instance == null) {
			instance = new PlatVendaMetaHistDbxDao();
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
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPLATVENDAMETA,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCENTROCUSTO,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" DTMETA,");
        sql.append(" VLVENDAREALIZADA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");		
	}

	@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PlatVendaMetaHist platVendaMetaHist = (PlatVendaMetaHist) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", platVendaMetaHist.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", platVendaMetaHist.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPLATVENDAMETA = ", platVendaMetaHist.cdPlatVendaMeta);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", platVendaMetaHist.cdCliente);
		sqlWhereClause.addAndCondition("CDCENTROCUSTO = ", platVendaMetaHist.cdCentroCusto);
		sqlWhereClause.addAndCondition("CDPLATAFORMAVENDA = ", platVendaMetaHist.cdPlataformaVenda);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PlatVendaMetaHist platVendaMetaHist = new PlatVendaMetaHist();
		platVendaMetaHist.rowKey = rs.getString("rowkey");
		platVendaMetaHist.cdEmpresa = rs.getString("cdEmpresa");
		platVendaMetaHist.cdRepresentante = rs.getString("cdRepresentante");
		platVendaMetaHist.cdPlatVendaMeta = rs.getString("cdPlatVendaMeta");
		platVendaMetaHist.cdCliente  = rs.getString("cdCliente");
		platVendaMetaHist.cdCentroCusto  = rs.getString("cdCentroCusto");		
		platVendaMetaHist.cdPlataformaVenda  = rs.getString("cdPlataformaVenda");	
		platVendaMetaHist.dtMeta  = rs.getDate("dtMeta");
		platVendaMetaHist.vlVendaRealizada  = rs.getDouble("vlVendaRealizada");
		platVendaMetaHist.flTipoAlteracao = rs.getString("flTipoAlteracao");
		platVendaMetaHist.nuCarimbo = rs.getInt("nuCarimbo");
		platVendaMetaHist.cdUsuario = rs.getString("cdUsuario");	
		return platVendaMetaHist;
	}

}
