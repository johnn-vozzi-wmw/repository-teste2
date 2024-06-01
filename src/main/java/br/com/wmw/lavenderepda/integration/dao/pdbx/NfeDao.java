package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import totalcross.sql.ResultSet;

public class NfeDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Nfe();
	}

	private static NfeDao instance;
	public static boolean houveRecebimentoNfeBackground;
	public static String erroOcorridoAtualizacao;

	public NfeDao() {
		super(Nfe.TABLE_NAME);
	}

	public static NfeDao getInstance() {
        if (instance == null) {
            instance = new NfeDao();
        }
        return instance;
    }

	protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDSTATUSNFE,");
        sql.append(" DSNATUREZAOPERACAO,");
        sql.append(" VLCHAVEACESSO,");
        sql.append(" DTSOLICITACAO,");
        sql.append(" DSSERIENFE,");
        sql.append(" DTRESPOSTA,");
        sql.append(" NULOTE,");
        sql.append(" DSTIPOEMISSAO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" NUNFE,");
        sql.append(" CDTIPOOPERACAONFE,");
        sql.append(" DTEMISSAO,");
        sql.append(" DTSAIDA,");
        sql.append(" HRSAIDA,");
        sql.append(" VLIBPT,");
        sql.append(" VLTOTALNFE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" VLTOTALICMS,");
        sql.append(" VLTOTALST,");
        sql.append(" VLTOTALIPI,");
        sql.append(" VLTOTALFRETE,");
        sql.append(" VLTOTALSEGURO,");
        sql.append(" VLTOTALDESCONTO,");
        sql.append(" VLTOTALPRODUTOSNFE,");
        sql.append(" CDTRANSPORTADORA,");
        sql.append(" FLAMBIENTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDMENSAGEMRETORNO,");
        sql.append(" DSMENSAGEMRETORNO,");
        sql.append(" HREMISSAO,");
        sql.append(" DTRESPOSTA,");
        sql.append(" HRRESPOSTA,");
        sql.append(" VLTOTALDESCONTOFIN,");
        sql.append(" DSMENSAGEMNOTACREDITO, ");
        sql.append(" NUPROTOCOLO ");
	}

	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Nfe nfe = (Nfe) domain;
        sql.append(Sql.getValue(nfe.cdEmpresa)).append(",");
        sql.append(Sql.getValue(nfe.cdRepresentante)).append(",");
        sql.append(Sql.getValue(nfe.nuPedido)).append(",");
        sql.append(Sql.getValue(nfe.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(nfe.cdStatusNfe)).append(",");
        sql.append(Sql.getValue(nfe.dsNaturezaOperacao)).append(",");
        sql.append(Sql.getValue(nfe.vlChaveAcesso)).append(",");
        sql.append(Sql.getValue(nfe.dtSolicitacao)).append(",");
        sql.append(Sql.getValue(nfe.dsSerieNfe)).append(",");
        sql.append(Sql.getValue(nfe.dtResposta)).append(",");
        sql.append(Sql.getValue(nfe.nuLote)).append(",");
        sql.append(Sql.getValue(nfe.dsTipoEmissao)).append(",");
        sql.append(Sql.getValue(nfe.dsObservacao)).append(",");
        sql.append(Sql.getValue(nfe.nuNfe)).append(",");
        sql.append(Sql.getValue(nfe.cdTipoOperacaoNfe)).append(",");
        sql.append(Sql.getValue(nfe.dtEmissao)).append(",");
        sql.append(Sql.getValue(nfe.dtSaida)).append(",");
        sql.append(Sql.getValue(nfe.hrSaida)).append(",");
        sql.append(Sql.getValue(nfe.vlIbpt)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalNfe)).append(",");
        sql.append(Sql.getValue(nfe.nuCarimbo)).append(",");
        sql.append(Sql.getValue(nfe.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(nfe.cdUsuario)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalIcms)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalSt)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalIpi)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalFrete)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalSeguro)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalDesconto)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalProdutosNfe)).append(",");
        sql.append(Sql.getValue(nfe.cdTransportadora)).append(",");
        sql.append(Sql.getValue(nfe.flAmbiente)).append(",");
        sql.append(Sql.getValue(nfe.cdCliente)).append(",");
        sql.append(Sql.getValue(nfe.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(nfe.cdMensagemRetorno)).append(",");
        sql.append(Sql.getValue(nfe.dsMensagemRetorno)).append(",");
        sql.append(Sql.getValue(nfe.hrEmissao)).append(",");
        sql.append(Sql.getValue(nfe.dtResposta)).append(",");
        sql.append(Sql.getValue(nfe.hrResposta)).append(",");
        sql.append(Sql.getValue(nfe.vlTotalDescontoFin)).append(",");
        sql.append(Sql.getValue(nfe.dsMensagemNotaCredito)).append(",");
        sql.append(Sql.getValue(nfe.nuProtocolo));
	}

	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" rowkey,");
        sql.append(" cdEmpresa,");
        sql.append(" cdRepresentante,");
        sql.append(" nuPedido,");
        sql.append(" flOrigemPedido,");
        sql.append(" cdStatusNfe,");
        sql.append(" dsNaturezaOperacao,");
        sql.append(" vlChaveAcesso,");
        sql.append(" dtSolicitacao,");
        sql.append(" dsSerieNfe,");
        sql.append(" dtResposta,");
        sql.append(" hrResposta,");
        sql.append(" nuLote,");
        sql.append(" dsTipoEmissao,");
        sql.append(" dsObservacao,");
        sql.append(" nuNfe,");
        sql.append(" cdTipoOperacaoNfe,");
        sql.append(" dtEmissao,");
        sql.append(" dtSaida,");
        sql.append(" hrSaida,");
        sql.append(" vlIbpt,");
        sql.append(" vlTotalNfe,");
        sql.append(" vlTotalIcms,");
        sql.append(" vlTotalSt,");
        sql.append(" vlTotalIpi,");
        sql.append(" vlTotalFrete,");
        sql.append(" vlTotalSeguro,");
        sql.append(" vlTotalDesconto,");
        sql.append(" vlTotalProdutosNfe,");
        sql.append(" cdTransportadora,");
        sql.append(" nuCarimbo,");
        sql.append(" flTipoAlteracao,");
        sql.append(" cdUsuario,");
        sql.append(" flAmbiente,");
        sql.append(" cdCliente,");
        sql.append(" cdTipoPedido,");
        sql.append(" hrEmissao,");
        sql.append(" cdMensagemRetorno,");
        sql.append(" dsMensagemRetorno,");
        sql.append(" vlTotalDescontoFin,");
        sql.append(" dsMensagemNotaCredito,");
        sql.append(" nuProtocolo ");
	}

	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Nfe nfe = (Nfe) domain;
        sql.append(" CDSTATUSNFE = ").append(Sql.getValue(nfe.cdStatusNfe)).append(",");
        sql.append(" DSNATUREZAOPERACAO = ").append(Sql.getValue(nfe.dsNaturezaOperacao)).append(",");
        sql.append(" VLCHAVEACESSO = ").append(Sql.getValue(nfe.vlChaveAcesso)).append(",");
        sql.append(" DTSOLICITACAO = ").append(Sql.getValue(nfe.dtSolicitacao)).append(",");
        sql.append(" DSSERIENFE = ").append(Sql.getValue(nfe.dsSerieNfe)).append(",");
        sql.append(" DTRESPOSTA = ").append(Sql.getValue(nfe.dtResposta)).append(",");
        sql.append(" NULOTE = ").append(Sql.getValue(nfe.nuLote)).append(",");
        sql.append(" DSTIPOEMISSAO = ").append(Sql.getValue(nfe.dsTipoEmissao)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(nfe.dsObservacao)).append(",");
        sql.append(" NUNFE = ").append(Sql.getValue(nfe.nuNfe)).append(",");
        sql.append(" CDTIPOOPERACAONFE = ").append(Sql.getValue(nfe.cdTipoOperacaoNfe)).append(",");
        sql.append(" DTEMISSAO = ").append(Sql.getValue(nfe.dtEmissao)).append(",");
        sql.append(" DTSAIDA = ").append(Sql.getValue(nfe.dtSaida)).append(",");
        sql.append(" HRSAIDA = ").append(Sql.getValue(nfe.hrSaida)).append(",");
        sql.append(" VLIBPT = ").append(Sql.getValue(nfe.vlIbpt)).append(",");
        sql.append(" VLTOTALNFE = ").append(Sql.getValue(nfe.vlTotalNfe)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(nfe.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(nfe.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(nfe.cdUsuario)).append(",");
        sql.append(" VLTOTALICMS = ").append(Sql.getValue(nfe.vlTotalIcms)).append(",");
        sql.append(" VLTOTALST = ").append(Sql.getValue(nfe.vlTotalSt)).append(",");
        sql.append(" VLTOTALIPI = ").append(Sql.getValue(nfe.vlTotalIpi)).append(",");
        sql.append(" VLTOTALFRETE = ").append(Sql.getValue(nfe.vlTotalFrete)).append(",");
        sql.append(" VLTOTALSEGURO = ").append(Sql.getValue(nfe.vlTotalSeguro)).append(",");
        sql.append(" VLTOTALDESCONTO = ").append(Sql.getValue(nfe.vlTotalDesconto)).append(",");
        sql.append(" VLTOTALPRODUTOSNFE = ").append(Sql.getValue(nfe.vlTotalProdutosNfe)).append(",");
        sql.append(" CDTRANSPORTADORA = ").append(Sql.getValue(nfe.cdTransportadora)).append(",");
        sql.append(" FLAMBIENTE = ").append(Sql.getValue(nfe.flAmbiente)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(nfe.cdCliente)).append(",");
        sql.append(" CDTIPOPEDIDO = ").append(Sql.getValue(nfe.cdTipoPedido)).append(",");
        sql.append(" CDMENSAGEMRETORNO = ").append(Sql.getValue(nfe.cdMensagemRetorno)).append(",");
        sql.append(" DSMENSAGEMRETORNO = ").append(Sql.getValue(nfe.dsMensagemRetorno)).append(",");
        sql.append(" HREMISSAO = ").append(Sql.getValue(nfe.hrEmissao)).append(",");
        sql.append(" NUPROTOCOLO = ").append(Sql.getValue(nfe.nuProtocolo));
	}

	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		Nfe nfe = (Nfe) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", nfe.cdEmpresa);
		if (ValueUtil.isEmpty(nfe.cdRepresentante) && ValueUtil.isNotEmpty(nfe.cdRepresentanteSupList)) {
			sqlWhereClause.addAndConditionIn("CDREPRESENTANTE", nfe.cdRepresentanteSupList);
		} else {
			sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", nfe.cdRepresentante);
		}
		sqlWhereClause.addAndCondition("cdCliente = ", nfe.cdCliente);
		sqlWhereClause.addAndCondition("nuPedido = ", nfe.nuPedido);
		sqlWhereClause.addAndCondition("flOrigemPedido = ", nfe.flOrigemPedido);
		sqlWhereClause.addAndCondition("nuNfe = ", nfe.nuNfe);
		sqlWhereClause.addAndCondition("dsSerieNfe = ", nfe.dsSerieNfe);
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground() && LavenderePdaConfig.usaEnvioPedidoBackground) {
			sqlWhereClause.addAndCondition(" ( nuCarimbo = " + Sql.getValue(nfe.nuCarimbo));
			sqlWhereClause.addOrConditionForced("nuCarimbo = ", 0);
			sqlWhereClause.addEndMultipleCondition();
		} else {
			sqlWhereClause.addAndCondition("nuCarimbo = ", nfe.nuCarimbo);
		}
		if (ValueUtil.isNotEmpty(nfe.dsTipoEmissaoFiter)) {
			sqlWhereClause.addAndCondition("dsTipoEmissao = ", nfe.dsTipoEmissaoFiter);
		}
		if (nfe.filtraPorNaoEnviadoServidor) {
			sqlWhereClause.addAndCondition("flTipoAlteracao  <> ''");
		}
		if (nfe.filtraPorNaoCancelado) {
			sqlWhereClause.addAndCondition(" cdMensagemRetorno <> " + Sql.getValue(Nfe.CDMENSAGEMRETORNO_CANCELAMENTO));
		}
		sql.append(sqlWhereClause.getSql());
	}

	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		Nfe nfe = new Nfe();
		nfe.rowKey = rs.getString("rowkey");
		nfe.cdEmpresa = rs.getString("cdEmpresa");
		nfe.cdRepresentante = rs.getString("cdRepresentante");
		nfe.nuPedido = rs.getString("nuPedido");
		nfe.flOrigemPedido = rs.getString("flOrigemPedido");
		nfe.cdStatusNfe = rs.getString("cdStatusNfe");
		nfe.dsNaturezaOperacao =  rs.getString("dsNaturezaOperacao");
		nfe.vlChaveAcesso = rs.getString("vlChaveAcesso");
		nfe.dtSolicitacao = rs.getDate("dtSolicitacao");
		nfe.dsSerieNfe = rs.getString("dsSerieNfe");
		nfe.dtResposta = rs.getDate("dtResposta");
		nfe.hrResposta = rs.getString("hrResposta");
		nfe.nuLote = rs.getInt("nuLote");
		nfe.dsTipoEmissao = rs.getString("dsTipoEmissao");
		nfe.dsObservacao = rs.getString("dsObservacao");
		nfe.nuNfe = rs.getInt("nuNfe");
		nfe.cdTipoOperacaoNfe = rs.getString("cdTipoOperacaoNfe");
		nfe.dtEmissao = rs.getDate("dtEmissao");
		nfe.dtSaida = rs.getDate("dtSaida");
		nfe.hrSaida = rs.getString("hrSaida");
		nfe.vlIbpt = rs.getDouble("vlIbpt");
		nfe.vlTotalNfe = rs.getDouble("vlTotalNfe");
		nfe.vlTotalIcms = rs.getDouble("vlTotalIcms");
		nfe.vlTotalSt = rs.getDouble("vlTotalSt");
		nfe.vlTotalIpi = rs.getDouble("vlTotalIpi");
		nfe.vlTotalFrete = rs.getDouble("vlTotalFrete");
		nfe.vlTotalSeguro = rs.getDouble("vlTotalSeguro");
		nfe.vlTotalDesconto = rs.getDouble("vlTotalDesconto");
		nfe.vlTotalProdutosNfe = rs.getDouble("vlTotalProdutosNfe");
		nfe.cdTransportadora = rs.getString("cdTransportadora");
		nfe.nuCarimbo = rs.getInt("nuCarimbo");
		nfe.flTipoAlteracao = rs.getString("flTipoAlteracao");
		nfe.cdUsuario = rs.getString("cdUsuario");
		nfe.flAmbiente = rs.getString("flAmbiente");
		nfe.cdCliente = rs.getString("cdCliente");
		nfe.cdTipoPedido = rs.getString("cdTipoPedido");
		nfe.hrEmissao = rs.getString("hrEmissao");
		nfe.cdMensagemRetorno = rs.getString("cdMensagemRetorno");
		nfe.dsMensagemRetorno = rs.getString("dsMensagemRetorno");
		nfe.vlTotalDescontoFin = rs.getDouble("vlTotalDescontoFin");
		nfe.dsMensagemNotaCredito = rs.getString("dsMensagemNotaCredito");
		nfe.nuProtocolo = rs.getString("nuProtocolo");
        return nfe;
	}
	
	public int getMaxNuNfe(Nfe nfe) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT MAX(NUNFE) MAXNFE FROM ").append(Nfe.TABLE_NAME);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", nfe.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", nfe.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("DSSERIENFE", nfe.dsSerieNfe);
		sql.append(sqlWhereClause.getSql());
		return getInt(sql.toString());
	}
	
	public void updateRetornoNfeByVlChaveAcesso(Nfe nfe) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(Nfe.TABLE_NAME)
		.append(" SET ")
		.append(" CDMENSAGEMRETORNO = ").append(Sql.getValue(nfe.cdMensagemRetorno)).append(",")
		.append(" DSMENSAGEMRETORNO = ").append(Sql.getValue(nfe.dsMensagemRetorno)).append(",")
		.append(" DTRESPOSTA = ").append(Sql.getValue(nfe.dtResposta)).append(",")
		.append(" HRRESPOSTA = ").append(Sql.getValue(nfe.hrResposta)).append(",")
		.append(" NUPROTOCOLO = ").append(Sql.getValue(nfe.nuProtocolo));
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" vlChaveAcesso = ", nfe.vlChaveAcesso);
		sql.append(sqlWhereClause.getSql());
		executeUpdate(sql.toString());
	}
	
	public void updateCancelamentoNfe(Nfe nfe) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(Nfe.TABLE_NAME)
		.append(" SET ")
		.append(" CDMENSAGEMRETORNO = ").append(Sql.getValue(nfe.cdMensagemRetorno)).append(",")
		.append(" DSMENSAGEMRETORNO = ").append(Sql.getValue(nfe.dsMensagemRetorno)).append(",")
		.append(" DTRESPOSTA = ").append(Sql.getValue(nfe.dtResposta)).append(",")
		.append(" HRRESPOSTA = ").append(Sql.getValue(nfe.hrResposta));
		addWhereByExample(nfe, sql);
		executeUpdate(sql.toString());
	}
	
	public void updateNfeTransmitidaComSucesso(Nfe nfeFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		addWhereByExample(nfeFilter, sql);
		executeUpdate(sql.toString());
		
	}
}
