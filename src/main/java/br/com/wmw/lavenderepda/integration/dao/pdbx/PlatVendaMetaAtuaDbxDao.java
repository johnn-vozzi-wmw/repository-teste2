package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMetaAtua;
import totalcross.sql.ResultSet;

public class PlatVendaMetaAtuaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlatVendaMetaAtua();
	}

	private static PlatVendaMetaAtuaDbxDao instance;

	public PlatVendaMetaAtuaDbxDao() {
		super(PlatVendaMetaAtua.TABLE_NAME);
	}

	public static PlatVendaMetaAtuaDbxDao getInstance() {
		if (instance == null) {
			instance = new PlatVendaMetaAtuaDbxDao();
		}
		return instance;
	}	

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPLATVENDAMETA,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCENTROCUSTO,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" DTMETA,");
        sql.append(" VLMETAPLANEJADAREP,");
        sql.append(" VLMETAPLANEJADASUP,");
        sql.append(" FLPLANEJADO,");
        sql.append(" FLENCERRADO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
	}
	
	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		PlatVendaMetaAtua platVendaMetaAtua = (PlatVendaMetaAtua) domain;
		sql.append(Sql.getValue(platVendaMetaAtua.cdEmpresa)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.cdRepresentante)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.cdPlatVendaMeta)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.cdCliente)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.cdCentroCusto)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.cdPlataformaVenda)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.dtMeta)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.vlMetaPlanejadaRep)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.vlMetaPlanejadaSup)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.flPlanejado)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.flEncerrado)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.nuCarimbo)).append(",");
		sql.append(Sql.getValue(platVendaMetaAtua.cdUsuario));
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
        sql.append(" VLMETAPLANEJADAREP,");
        sql.append(" VLMETAPLANEJADASUP,");
        sql.append(" FLPLANEJADO,");
        sql.append(" FLENCERRADO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		PlatVendaMetaAtua platVendaMetaAtua = (PlatVendaMetaAtua) domain;
		sql.append(" VLMETAPLANEJADAREP = ").append(Sql.getValue(platVendaMetaAtua.vlMetaPlanejadaRep)).append(",");
		sql.append(" VLMETAPLANEJADASUP = ").append(Sql.getValue(platVendaMetaAtua.vlMetaPlanejadaSup)).append(",");
		sql.append(" FLPLANEJADO = ").append(Sql.getValue(platVendaMetaAtua.flPlanejado)).append(",");
		sql.append(" FLENCERRADO = ").append(Sql.getValue(platVendaMetaAtua.flEncerrado)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(platVendaMetaAtua.flTipoAlteracao)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(platVendaMetaAtua.nuCarimbo)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(platVendaMetaAtua.cdUsuario));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PlatVendaMetaAtua platVendaMetaAtua = (PlatVendaMetaAtua) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", platVendaMetaAtua.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", platVendaMetaAtua.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPLATVENDAMETA = ", platVendaMetaAtua.cdPlatVendaMeta);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", platVendaMetaAtua.cdCliente);
		sqlWhereClause.addAndCondition("CDCENTROCUSTO = ", platVendaMetaAtua.cdCentroCusto);
		sqlWhereClause.addAndCondition("CDPLATAFORMAVENDA = ", platVendaMetaAtua.cdPlataformaVenda);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PlatVendaMetaAtua platVendaMetaAtua = new PlatVendaMetaAtua();
		platVendaMetaAtua.rowKey = rs.getString("rowkey");
		platVendaMetaAtua.cdEmpresa = rs.getString("cdEmpresa");
		platVendaMetaAtua.cdRepresentante = rs.getString("cdRepresentante");
		platVendaMetaAtua.cdPlatVendaMeta = rs.getString("cdPlatVendaMeta");
		platVendaMetaAtua.cdCliente  = rs.getString("cdCliente");
		platVendaMetaAtua.cdCentroCusto  = rs.getString("cdCentroCusto");		
		platVendaMetaAtua.cdPlataformaVenda  = rs.getString("cdPlataformaVenda");	
		platVendaMetaAtua.dtMeta  = rs.getDate("dtMeta");
		platVendaMetaAtua.vlMetaPlanejadaRep = rs.getDouble("vlMetaPlanejadaRep");
		platVendaMetaAtua.vlMetaPlanejadaSup = rs.getDouble("vlMetaPlanejadaSup");
		platVendaMetaAtua.flPlanejado = rs.getString("flPlanejado");
		platVendaMetaAtua.flEncerrado = rs.getString("flEncerrado");
		platVendaMetaAtua.flTipoAlteracao = rs.getString("flTipoAlteracao");
		platVendaMetaAtua.nuCarimbo = rs.getInt("nuCarimbo");
		platVendaMetaAtua.cdUsuario = rs.getString("cdUsuario");	
		return platVendaMetaAtua;
	}

}
