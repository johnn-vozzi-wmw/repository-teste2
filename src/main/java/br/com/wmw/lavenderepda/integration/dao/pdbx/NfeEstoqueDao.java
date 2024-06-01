package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.NfeEstoque;
import totalcross.sql.ResultSet;

public class NfeEstoqueDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NfeEstoque();
	}

	private static NfeEstoqueDao instance;

	public NfeEstoqueDao() {
		super(NfeEstoque.TABLE_NAME);
	}
	
	public static NfeEstoqueDao getInstance() {
		if (instance == null) {
			instance = new NfeEstoqueDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		NfeEstoque nfeEstoque = new NfeEstoque();
		nfeEstoque.cdEmpresa = rs.getString("cdEmpresa");
		nfeEstoque.cdRepresentante = rs.getString("cdRepresentante");
		nfeEstoque.nuNotaRemessa = rs.getString("nuNotaRemessa");
		nfeEstoque.nuSerieRemessa = rs.getString("nuSerieRemessa");
		nfeEstoque.cdPedidoEstoque = rs.getString("cdPedidoEstoque");
		nfeEstoque.cdStatusNfe = rs.getString("cdStatusNfe");
		nfeEstoque.dsNaturezaOperacao = rs.getString("dsNaturezaOperacao");
		nfeEstoque.nuCfop = rs.getString("nuCfop");
		nfeEstoque.dtSolicitacao = rs.getDate("dtSolicitacao");
		nfeEstoque.vlChaveAcesso = rs.getString("vlChaveAcesso");
		nfeEstoque.dsTipoEmissao = rs.getString("dsTipoEmissao");
		nfeEstoque.dsObservacao = rs.getString("dsObservacao");
		nfeEstoque.cdTipoOperacaoNfe = rs.getString("cdTipoOperacaoNfe");
		nfeEstoque.dtEmissao = rs.getDate("dtEmissao");
		nfeEstoque.hrEmissao = rs.getString("hrEmissao");
		nfeEstoque.dtResposta = rs.getDate("dtResposta");
		nfeEstoque.hrResposta = rs.getString("hrResposta");
		nfeEstoque.dtSaida = rs.getDate("dtSaida");
		nfeEstoque.hrSaida = rs.getString("hrSaida");
		nfeEstoque.vlTotalNfe = rs.getDouble("vlTotalNfe");
		nfeEstoque.vlTotalIcms = rs.getDouble("vlTotalIcms");
		nfeEstoque.vlTotalSt = rs.getDouble("vlTotalSt");
		nfeEstoque.vlTotalIpi = rs.getDouble("vlTotalIpi");
		nfeEstoque.vlTotalFrete = rs.getDouble("vlTotalFrete");
		nfeEstoque.vlTotalSeguro = rs.getDouble("vlTotalSeguro");
		nfeEstoque.vlIbpt = rs.getDouble("vlIbpt");
		nfeEstoque.cdTransportadora = rs.getString("cdTransportadora");
		nfeEstoque.flTipoNfe = rs.getString("flTipoNfe");
		return nfeEstoque;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" NUNOTAREMESSA,")
		.append(" NUSERIEREMESSA,")
		.append(" CDPEDIDOESTOQUE,")
		.append(" CDSTATUSNFE,")
		.append(" DSNATUREZAOPERACAO,")
		.append(" NUCFOP,")
		.append(" DTSOLICITACAO,")
		.append(" VLCHAVEACESSO,")
		.append(" DSTIPOEMISSAO,")
		.append(" DSOBSERVACAO,")
		.append(" CDTIPOOPERACAONFE,")
		.append(" DTEMISSAO,")
		.append(" HREMISSAO,")
		.append(" DTRESPOSTA,")
		.append(" HRRESPOSTA,")
		.append(" DTSAIDA,")
		.append(" HRSAIDA,")
		.append(" VLTOTALNFE,")
		.append(" VLTOTALICMS,")
		.append(" VLTOTALST,")
		.append(" VLTOTALIPI,")
		.append(" VLTOTALFRETE,")
		.append(" VLTOTALSEGURO,")
		.append(" VLIBPT,")
		.append(" CDTRANSPORTADORA,")
		.append(" FLTIPONFE");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" NUNOTAREMESSA,")
		.append(" NUSERIEREMESSA,")
		.append(" CDPEDIDOESTOQUE,")
		.append(" CDSTATUSNFE,")
		.append(" DSNATUREZAOPERACAO,")
		.append(" NUCFOP,")
		.append(" DTSOLICITACAO,")
		.append(" VLCHAVEACESSO,")
		.append(" DSTIPOEMISSAO,")
		.append(" DSOBSERVACAO,")
		.append(" CDTIPOOPERACAONFE,")
		.append(" DTEMISSAO,")
		.append(" HREMISSAO,")
		.append(" DTRESPOSTA,")
		.append(" HRRESPOSTA,")
		.append(" DTSAIDA,")
		.append(" HRSAIDA,")
		.append(" VLTOTALNFE,")
		.append(" VLTOTALICMS,")
		.append(" VLTOTALST,")
		.append(" VLTOTALIPI,")
		.append(" VLTOTALFRETE,")
		.append(" VLTOTALSEGURO,")
		.append(" VLIBPT,")
		.append(" CDTRANSPORTADORA,")
		.append(" FLTIPONFE");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		NfeEstoque nfeEstoque = (NfeEstoque) domain;
		sql.append(Sql.getValue(nfeEstoque.cdEmpresa)).append(", ")
		.append(Sql.getValue(nfeEstoque.cdRepresentante)).append(", ")
		.append(Sql.getValue(nfeEstoque.nuNotaRemessa)).append(", ")
		.append(Sql.getValue(nfeEstoque.nuSerieRemessa)).append(", ")
		.append(Sql.getValue(nfeEstoque.cdPedidoEstoque)).append(", ")
		.append(Sql.getValue(nfeEstoque.cdStatusNfe)).append(", ")
		.append(Sql.getValue(nfeEstoque.dsNaturezaOperacao)).append(", ")
		.append(Sql.getValue(nfeEstoque.nuCfop)).append(", ")
		.append(Sql.getValue(nfeEstoque.dtSolicitacao)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlChaveAcesso)).append(", ")
		.append(Sql.getValue(nfeEstoque.dsTipoEmissao)).append(", ")
		.append(Sql.getValue(nfeEstoque.dsObservacao)).append(", ")
		.append(Sql.getValue(nfeEstoque.cdTipoOperacaoNfe)).append(", ")
		.append(Sql.getValue(nfeEstoque.dtEmissao)).append(", ")
		.append(Sql.getValue(nfeEstoque.hrEmissao)).append(", ")
		.append(Sql.getValue(nfeEstoque.dtResposta)).append(", ")
		.append(Sql.getValue(nfeEstoque.hrResposta)).append(", ")
		.append(Sql.getValue(nfeEstoque.dtSaida)).append(", ")
		.append(Sql.getValue(nfeEstoque.hrSaida)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlTotalNfe)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlTotalIcms)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlTotalSt)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlTotalIpi)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlTotalFrete)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlTotalSeguro)).append(", ")
		.append(Sql.getValue(nfeEstoque.vlIbpt)).append(", ")
		.append(Sql.getValue(nfeEstoque.cdTransportadora)).append(", ")
		.append(Sql.getValue(nfeEstoque.flTipoNfe));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		NfeEstoque nfeEstoque = (NfeEstoque) domain;
		sql.append("CDPEDIDOESTOQUE = ").append(Sql.getValue(nfeEstoque.cdPedidoEstoque)).append(", ")
		.append("CDSTATUSNFE = ").append(Sql.getValue(nfeEstoque.cdStatusNfe)).append(", ")
		.append("DSNATUREZAOPERACAO = ").append(Sql.getValue(nfeEstoque.dsNaturezaOperacao)).append(", ")
		.append("NUCFOP = ").append(Sql.getValue(nfeEstoque.nuCfop)).append(", ")
		.append("DTSOLICITACAO = ").append(Sql.getValue(nfeEstoque.dtSolicitacao)).append(", ")
		.append("VLCHAVEACESSO = ").append(Sql.getValue(nfeEstoque.vlChaveAcesso)).append(", ")
		.append("DSTIPOEMISSAO = ").append(Sql.getValue(nfeEstoque.dsTipoEmissao)).append(", ")
		.append("DSOBSERVACAO = ").append(Sql.getValue(nfeEstoque.dsObservacao)).append(", ")
		.append("CDTIPOOPERACAONFE = ").append(Sql.getValue(nfeEstoque.cdTipoOperacaoNfe)).append(", ")
		.append("DTEMISSAO = ").append(Sql.getValue(nfeEstoque.dtEmissao)).append(", ")
		.append("HREMISSAO = ").append(Sql.getValue(nfeEstoque.hrEmissao)).append(", ")
		.append(" DTRESPOSTA = ").append(Sql.getValue(nfeEstoque.dtResposta)).append(", ")
		.append(" HRRESPOSTA = ").append(Sql.getValue(nfeEstoque.hrResposta)).append(", ")
		.append(" DTSAIDA = ").append(Sql.getValue(nfeEstoque.dtSaida)).append(", ")
		.append(" HRSAIDA = ").append(Sql.getValue(nfeEstoque.hrSaida)).append(", ")
		.append(" VLTOTALNFE = ").append(Sql.getValue(nfeEstoque.vlTotalNfe)).append(", ")
		.append(" VLTOTALICMS = ").append(Sql.getValue(nfeEstoque.vlTotalIcms)).append(", ")
		.append(" VLTOTALST = ").append(Sql.getValue(nfeEstoque.vlTotalSt)).append(", ")
		.append(" VLTOTALIPI = ").append(Sql.getValue(nfeEstoque.vlTotalIpi)).append(", ")
		.append(" VLTOTALFRETE = ").append(Sql.getValue(nfeEstoque.vlTotalFrete)).append(", ")
		.append(" VLTOTALSEGURO = ").append(Sql.getValue(nfeEstoque.vlTotalSeguro)).append(", ")
		.append(" VLIBPT = ").append(Sql.getValue(nfeEstoque.vlIbpt)).append(", ")
		.append(" CDTRANSPORTADORA = ").append(Sql.getValue(nfeEstoque.cdTransportadora)).append(", ")
		.append(" FLTIPONFE = ").append(Sql.getValue(nfeEstoque.flTipoNfe));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		NfeEstoque nfeEstoque = (NfeEstoque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", nfeEstoque.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", nfeEstoque.cdRepresentante);
		sqlWhereClause.addAndCondition("NUNOTAREMESSA = ", nfeEstoque.nuNotaRemessa);
		sqlWhereClause.addAndCondition("NUSERIEREMESSA = ", nfeEstoque.nuSerieRemessa);
		sqlWhereClause.addAndCondition("FLTIPONFE = ", nfeEstoque.flTipoNfe);
		sqlWhereClause.addAndCondition("DTEMISSAO >= ", nfeEstoque.dtEmissaoInicial);
		sqlWhereClause.addAndCondition("DTEMISSAO <= ", nfeEstoque.dtEmissaoFinal);
		sql.append(sqlWhereClause.getSql());
	}

}
