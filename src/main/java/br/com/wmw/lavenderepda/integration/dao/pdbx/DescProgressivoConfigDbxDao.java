package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFam;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class DescProgressivoConfigDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgressivoConfig();
	}

	private static DescProgressivoConfigDbxDao instance;

	public DescProgressivoConfigDbxDao() {
		super(DescProgressivoConfig.TABLE_NAME);
	}

	public static DescProgressivoConfigDbxDao getInstance() {
		return (instance == null) ? instance = new DescProgressivoConfigDbxDao() : instance;
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {
	}

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		addSelectColumns(null, sql);
	}

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return populate(domainFilter, rs);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		DescProgressivoConfig descProgressivoConfig = new DescProgressivoConfig();
		descProgressivoConfig.rowKey = rs.getString("rowkey");
		descProgressivoConfig.cdEmpresa = rs.getString("cdEmpresa");
		descProgressivoConfig.cdRepresentante = rs.getString("cdRepresentante");
		descProgressivoConfig.cdDescProgressivo = rs.getString("cdDescProgressivo");
		descProgressivoConfig.nuNivelCliente = rs.getInt("nuNivelCliente");
		descProgressivoConfig.dsDescProgressivo = rs.getString("dsDescProgressivo");
		descProgressivoConfig.vlMinDescProgressivo = rs.getDouble("vlMinDescProgressivo");
		descProgressivoConfig.vlMaxDescProgressivo = rs.getDouble("vlMaxDescProgressivo");
		descProgressivoConfig.dtInicialVigencia = rs.getDate("dtInicialVigencia");
		descProgressivoConfig.dtFimVigencia = rs.getDate("dtFimVigencia");
		descProgressivoConfig.cdUsuario = rs.getString("cdUsuario");
		return descProgressivoConfig;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDDESCPROGRESSIVO,");
		sql.append(" tb.NUNIVELCLIENTE,");
		sql.append(" tb.DSDESCPROGRESSIVO,");
		sql.append(" tb.VLMINDESCPROGRESSIVO,");
		sql.append(" tb.VLMAXDESCPROGRESSIVO,");
		sql.append(" tb.DTINICIALVIGENCIA,");
		sql.append(" tb.DTFIMVIGENCIA,");
		sql.append(" tb.CDUSUARIO");
	}

	public int countByCliente(DescProgressivoConfig descProgressivoConfig) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as qtde FROM ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoin(descProgressivoConfig, sql);
		addWhereByExample(descProgressivoConfig, sql);
		return getInt(sql.toString());
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		DescProgressivoConfig descProgressivoConfig = (DescProgressivoConfig) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", descProgressivoConfig.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", descProgressivoConfig.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDDESCPROGRESSIVO  = ", descProgressivoConfig.cdDescProgressivo);
		sqlWhereClause.addAndCondition("tb.NUNIVELCLIENTE = ", descProgressivoConfig.nuNivelCliente);
		sqlWhereClause.addAndCondition("tb.DTINICIALVIGENCIA  <= ", descProgressivoConfig.dtFimVigencia);
		sqlWhereClause.addAndCondition("tb.DTFIMVIGENCIA  >= ", descProgressivoConfig.dtInicialVigencia);
		sqlWhereClause.addAndCondition("tb.VLMINDESCPROGRESSIVO >= ", descProgressivoConfig.vlMinDescProgressivo);
		sqlWhereClause.addAndCondition("tb.VLMAXDESCPROGRESSIVO <= ", descProgressivoConfig.vlMaxDescProgressivo);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		super.addJoin(domainFilter, sql);
		DescProgressivoConfig descProgressivoConfig = (DescProgressivoConfig) domainFilter;
		if (descProgressivoConfig != null && descProgressivoConfig.cliente != null) {
			sql.append(" JOIN TBLVPDESCPROGCONFIGFACLI DESCPROGCONFIGFACLI ON ")
					.append(" DESCPROGCONFIGFACLI.CDEMPRESA = TB.CDEMPRESA ")
					.append(" AND DESCPROGCONFIGFACLI.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
					.append(" AND DESCPROGCONFIGFACLI.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO ")
					.append(" AND (DESCPROGCONFIGFACLI.CDCLIENTE = ").append(Sql.getValue(descProgressivoConfig.cliente.cdCliente)).append(" OR ");
			
			String cdFaixaCli = descProgressivoConfig.cliente.cdFaixaCli;
			if (ValueUtil.isEmpty(cdFaixaCli)) {
				sql.append(" DESCPROGCONFIGFACLI.CDFAIXACLI = FAIXACLIENTE.CDFAIXACLI)")
				.append(" LEFT JOIN TBLVPFAIXACLIENTE FAIXACLIENTE ON FAIXACLIENTE.CDCLIENTE = ").append(Sql.getValue(descProgressivoConfig.cliente.cdCliente))
				.append(" AND FAIXACLIENTE.CDFAIXACLI = FAIXACLIENTE.CDFAIXACLI ")
				.append(" AND FAIXACLIENTE.CDEMPRESA = tb.CDEMPRESA ")
				.append(" AND FAIXACLIENTE.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
			} else {
				sql.append(" DESCPROGCONFIGFACLI.CDFAIXACLI = ").append(Sql.getValue(cdFaixaCli))
				.append(")");
			}
		}
	}

	public DescProgressivoConfig findVlAtingidoAndQtdFamiliaByDescProgressivo(DescProgressivoConfig descProgressivoConfig, boolean isMax) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT");
		getSqlVlAtingido(descProgressivoConfig, isMax, sql, false, "VLATINGIDO");
		getSqlVlAtingido(descProgressivoConfig, isMax, sql, true, "VLATINGIDOERP");
		sql.append("        (SELECT COUNT(DISTINCT PRODUTO.CDFAMILIADESCPROG) FROM TBLVPPEDIDOERP PEDIDO");
		sql.append("                JOIN TBLVPITEMPEDIDOERP ITEMPEDIDO ON");
		sql.append("                     ITEMPEDIDO.CDEMPRESA = PEDIDO.CDEMPRESA");
		sql.append("                 AND ITEMPEDIDO.CDREPRESENTANTE = PEDIDO.CDREPRESENTANTE");
		sql.append("                 AND ITEMPEDIDO.NUPEDIDO = PEDIDO.NUPEDIDO");
		sql.append("                 AND ITEMPEDIDO.FLORIGEMPEDIDO = PEDIDO.FLORIGEMPEDIDO");
		sql.append(addItemPedidoRequirements("ITEMPEDIDO"));
		sql.append("                JOIN TBLVPPRODUTO PRODUTO ON");
		sql.append("                     PRODUTO.CDEMPRESA = TB.CDEMPRESA");
		sql.append("                 AND PRODUTO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("                 AND PRODUTO.CDPRODUTO = ITEMPEDIDO.CDPRODUTO");
		sql.append("                 AND PRODUTO.CDFAMILIADESCPROG IN (");
		sql.append("                 SELECT TF.CDFAMILIADESCPROG FROM TBLVPDESCPROGCONFIGFAM TF");
		sql.append("                 WHERE TF.CDEMPRESA = TB.CDEMPRESA");
		sql.append("                   AND TF.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("                   AND TF.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO");
		sql.append("                   AND TF.FLTIPOFAMILIAPRO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append("             )");
		sql.append("         WHERE PEDIDO.CDEMPRESA = TB.CDEMPRESA");
		sql.append("           AND PEDIDO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append(addPedidoRequirements(descProgressivoConfig.cliente.cdCliente, "PEDIDO", "TB", true, isMax, true));
		sql.append("     ) AS QTATINGIDO");
		if (descProgressivoConfig.pedidoFilter != null) {
			sql.append(", (SELECT COALESCE((");
			sql.append(getSqlSumVlTotalItemPedido(false, descProgressivoConfig.pedidoFilter.getCondicaoPagamento()));
			sql.append("              JOIN TBLVPPRODUTO PRODUTO ON PRODUTO.CDEMPRESA = TB.CDEMPRESA");
			sql.append("         AND PRODUTO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
			sql.append("         AND PRODUTO.CDPRODUTO = ITEMPEDIDO.CDPRODUTO");
			sql.append("         AND PRODUTO.CDFAMILIADESCPROG IN (SELECT TF.CDFAMILIADESCPROG");
			sql.append("                                           FROM TBLVPDESCPROGCONFIGFAM TF");
			sql.append("                                           WHERE TF.CDEMPRESA = TB.CDEMPRESA");
			sql.append("                                             AND TF.CDREPRESENTANTE = TB.CDREPRESENTANTE");
			sql.append("                                             AND TF.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO");
			if (!isMax) {
				sql.append("                                             AND TF.FLFAMACUVALORMIN = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
			} else {
				sql.append("                                             AND TF.FLFAMACUVALORMAX = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
			}
			sql.append("     ) WHERE PEDIDO.CDEMPRESA = TB.CDEMPRESA");
			sql.append("       AND PEDIDO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
			sql.append("       AND PEDIDO.CDEMPRESA = ").append(Sql.getValue(descProgressivoConfig.pedidoFilter.cdEmpresa));
			sql.append("       AND PEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(descProgressivoConfig.pedidoFilter.cdRepresentante));
			sql.append("       AND PEDIDO.NUPEDIDO = ").append(Sql.getValue(descProgressivoConfig.pedidoFilter.nuPedido));
			sql.append(addPedidoRequirements(descProgressivoConfig.cliente.cdCliente, "PEDIDO", null, false, isMax, false));
			sql.append("       AND PEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(descProgressivoConfig.pedidoFilter.flOrigemPedido));
			sql.append(getSqlNotInSelectAtual(descProgressivoConfig, false, true));
			sql.append(" )), 0)) AS VLATINGIDOPEDIDOATUAL ");
		}
		sql.append(" FROM TBLVPDESCPROGRESSIVOCONFIG TB  ");
		addWhereByExample(descProgressivoConfig, sql);
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				int i = 1;
				if (isMax) {
					descProgressivoConfig.vlAtingidoMax = rs.getDouble(i++); //VLATINGIDO
					descProgressivoConfig.vlAtingidoMax += rs.getDouble(i++); //VLATINGIDOERP
					descProgressivoConfig.qtAtingidoMax = rs.getInt(i++);
					if (descProgressivoConfig.pedidoFilter != null) {
						descProgressivoConfig.vlAtingidoPedidoAtualMax = rs.getDouble(i);
					}
				} else {
					descProgressivoConfig.vlAtingido = rs.getDouble(i++); //VLATINGIDO
					descProgressivoConfig.vlAtingido += rs.getDouble(i++); //VLATINGIDOERP
					descProgressivoConfig.qtAtingido = rs.getInt(i++);
					if (descProgressivoConfig.pedidoFilter != null) {
						descProgressivoConfig.vlAtingidoPedidoAtual = rs.getDouble(i);
					}
				}
			}
		}
		return descProgressivoConfig;
	}

	private void getSqlVlAtingido(DescProgressivoConfig descProgressivoConfig, boolean isMax, StringBuffer sql, boolean erp, String alias) {
		sql.append("    COALESCE((");
		sql.append(getSqlSumVlTotalItemPedido(erp));
		sql.append("                             JOIN TBLVPPRODUTO PRODUTO ON");
		sql.append("                             PRODUTO.CDEMPRESA = TB.CDEMPRESA");
		sql.append("                         AND PRODUTO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("                         AND PRODUTO.CDPRODUTO = ITEMPEDIDO.CDPRODUTO");
		sql.append("                         AND PRODUTO.CDFAMILIADESCPROG IN (");
		sql.append("                         SELECT TF.CDFAMILIADESCPROG FROM TBLVPDESCPROGCONFIGFAM TF");
		sql.append("                         WHERE TF.CDEMPRESA = TB.CDEMPRESA");
		sql.append("                           AND TF.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("                           AND TF.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO");
		sql.append("                           AND TF.FLFAMACUVALORMIN = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append("                     )");
		sql.append("                 WHERE PEDIDO.CDEMPRESA = TB.CDEMPRESA");
		sql.append("                   AND PEDIDO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		if (descProgressivoConfig.pedidoFilter != null) {
			sql.append("                   AND PEDIDO.NUPEDIDO <> ").append(Sql.getValue(descProgressivoConfig.pedidoFilter.nuPedido));
		}
		if (LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
			sql.append("  AND PEDIDO.CDSTATUSPEDIDO <> ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto)).append("  ");
		}
		sql.append(addPedidoRequirements(descProgressivoConfig.cliente.cdCliente, "PEDIDO", "TB", true, isMax, false));
		sql.append("             ), 0) AS " + alias + ",");
	}

	public static String addDtFaturamentoOuDtEmissao(final String aliasPedido, final String aliasVigencia) {
		final StringBuffer sql = new StringBuffer();
		if (aliasVigencia != null) {
			sql.append(" AND ( ");
			sql.append(aliasPedido).append(".DTFATURAMENTO IS NULL ");
			sql.append(" OR ( ").append(aliasPedido).append(".DTFATURAMENTO >= ").append(aliasVigencia).append(".DTINICIALVIGENCIA ");
			sql.append(" AND ").append(aliasPedido).append(".DTFATURAMENTO <= ").append(aliasVigencia).append(".DTFIMVIGENCIA ").append(" ) ");
			sql.append(" ) ");
			sql.append(" AND ").append(aliasPedido).append(".DTEMISSAO >= ").append(aliasVigencia).append(".DTINICIALVIGENCIA ");
			sql.append(" AND ").append(aliasPedido).append(".DTEMISSAO <= ").append(aliasVigencia).append(".DTFIMVIGENCIA ");
		}
		return sql.toString();
	}
	
	public static String addItemPedidoRequirements(String alias) {
		StringBuffer sql = new StringBuffer();
		sql.append(" AND ").append(alias).append(".FLTIPOITEMPEDIDO = ").append(Sql.getValue(TipoItemPedido.TIPOITEMPEDIDO_NORMAL));
		sql.append(" AND (").append(alias).append(".CDCOMBO = '' OR ").append(alias).append(".CDCOMBO IS NULL) ");
		sql.append(" AND (").append(alias).append(".CDKIT = '' OR ").append(alias).append(".CDKIT IS NULL) ");
		sql.append(" AND (").append(alias).append(".QTITEMFISICO > 0 ) ");
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			sql.append(" AND NOT EXISTS (SELECT 1 FROM ").append(SolAutorizacao.TABLE_NAME).append(" sol ");
			sql.append(" WHERE sol.CDEMPRESA = ").append(alias).append(".CDEMPRESA");
			sql.append(" AND sol.CDREPRESENTANTE = ").append(alias).append(".CDREPRESENTANTE");
			sql.append(" AND sol.FLORIGEMPEDIDO = ").append(alias).append(".FLORIGEMPEDIDO");
			sql.append(" AND sol.NUPEDIDO = ").append(alias).append(".NUPEDIDO");
			sql.append(" AND sol.CDPRODUTO = ").append(alias).append(".CDPRODUTO");
			sql.append(" AND (sol.FLAUTORIZADO = 'S' OR sol.FLAUTORIZADO IS NULL OR sol.FLAUTORIZADO = '') ");
			sql.append(" AND (sol.FLEXCLUIDO <> 'S') ORDER BY sol.CDSOLAUTORIZACAO DESC LIMIT 1) ");
		}
		return sql.toString();
	}

	public static String addPedidoRequirements(String cdCliente, String aliasPedido, String aliasVigencia, boolean erp, boolean isVlMax, boolean isConsideraFamilia) {
		StringBuffer sql = new StringBuffer();
		sql.append(addDtFaturamentoOuDtEmissao(aliasPedido, aliasVigencia));
		if (cdCliente != null) {
			sql.append(" AND ").append(aliasPedido).append(".CDCLIENTE = ").append(Sql.getValue(cdCliente));
		}
		if (isConsideraFamilia) {
			addStatusPedidoConsideraFamilia(aliasPedido, sql);
		} else {
			addStatusPedidoConsideraVlMinMax(aliasPedido, erp, isVlMax, sql);
		}
		return sql.toString();
	}

	private static void addStatusPedidoConsideraVlMinMax(String aliasPedido, boolean erp, boolean isVlMax, StringBuffer sql) {
		if (isVlMax) {
			sql.append(" AND EXISTS (SELECT 1 FROM ").append(StatusPedidoPda.TABLE_NAME);
			sql.append(" WHERE CDSTATUSPEDIDO = ").append(aliasPedido).append(".CDSTATUSPEDIDO");
			sql.append(" AND FLCONSIDERADESCPROGVLMAX = 'S' )");
		} else {
			sql.append(" AND EXISTS (SELECT 1 FROM ").append(StatusPedidoPda.TABLE_NAME);
			sql.append(" WHERE CDSTATUSPEDIDO = ").append(aliasPedido).append(".CDSTATUSPEDIDO");
			sql.append(" AND FLCONSIDERADESCPROGVLMIN = 'S' )");
		}
	}

	public static void addStatusPedidoConsideraFamilia(String aliasPedido, StringBuffer sql) {
		sql.append(" AND EXISTS (SELECT 1 FROM ").append(StatusPedidoPda.TABLE_NAME);
		sql.append(" WHERE CDSTATUSPEDIDO = ").append(aliasPedido).append(".CDSTATUSPEDIDO");
		sql.append(" AND FLCONSIDERAFAMILIA = 'S' )");
	}

	public static String getSqlSumVlTotalItemPedido(boolean erp) {
		return getSqlSumVlTotalItemPedido(erp, null);
	}

	public static String getSqlSumVlTotalItemPedido(boolean erp, CondicaoPagamento condicaoPagamento) {
		StringBuffer sql = new StringBuffer();
		if (erp) {
			sql.append(" SELECT SUM(ITEMPEDIDO.VLTOTALITEMPEDIDO) FROM TBLVPPEDIDOERP");
		} else {
			sql.append(" SELECT SUM(round(ITEMPEDIDO.VLBASEITEMTABELAPRECO ");
			sql.append(getCalculosIndiceSql(condicaoPagamento));
			sql.append(" , ").append(LavenderePdaConfig.nuCasasDecimais).append(") * ITEMPEDIDO.QTITEMFISICO ) FROM TBLVPPEDIDO");
		}
		sql.append(" PEDIDO");
		sql.append(" JOIN TBLVPITEMPEDIDO");
		if (erp) {
			sql.append("ERP");
		}
		sql.append(" ITEMPEDIDO ON");
		sql.append(" ITEMPEDIDO.CDEMPRESA = PEDIDO.CDEMPRESA");
		sql.append(" AND ITEMPEDIDO.CDREPRESENTANTE = PEDIDO.CDREPRESENTANTE");
		sql.append(" AND ITEMPEDIDO.NUPEDIDO = PEDIDO.NUPEDIDO");
		sql.append(" AND ITEMPEDIDO.FLORIGEMPEDIDO = PEDIDO.FLORIGEMPEDIDO");
		sql.append(addItemPedidoRequirements("ITEMPEDIDO"));
		return sql.toString();
	}

	public static String getCalculosIndiceSql(CondicaoPagamento condicaoPagamento) {
		StringBuffer sql = new StringBuffer();
		if (LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
			double vlIndiceFinanceiro = SessionLavenderePda.getRepresentante().vlIndiceFinanceiro;
			vlIndiceFinanceiro = vlIndiceFinanceiro > 0 ? vlIndiceFinanceiro : 1;
			sql.append(" * ").append(Sql.getValue(vlIndiceFinanceiro));
		}
		if (LavenderePdaConfig.isIndiceFinanceiroCondPagtoVlItemPedido() && condicaoPagamento != null) {
			double vlIndiceFinanceiro = condicaoPagamento.vlIndiceFinanceiro;
			vlIndiceFinanceiro = vlIndiceFinanceiro > 0 ? vlIndiceFinanceiro : 1;
			sql.append(" * ").append(Sql.getValue(vlIndiceFinanceiro));
		}
		return sql.toString();
	}

	private String getSqlNotInSelectAtual(DescProgressivoConfig descProgressivoConfig, boolean qtd, boolean erp) {
		StringBuffer sql = getSqlBuffer();
		sql.append("       AND PRODUTO.CDFAMILIADESCPROG NOT IN (");
		sql.append("         SELECT DISTINCT PRODUTO.CDFAMILIADESCPROG");
		sql.append("         FROM TBLVPDESCPROGRESSIVOCONFIG TB");
		sql.append("                  JOIN TBLVPPEDIDO").append(erp ? "ERP" : ValueUtil.VALOR_NI).append(" PEDIDOERP");
		sql.append("                  JOIN TBLVPITEMPEDIDO").append(erp ? "ERP" : ValueUtil.VALOR_NI).append(" ITEMPEDIDOERP");
		sql.append("                       ON ITEMPEDIDOERP.CDEMPRESA = PEDIDOERP.CDEMPRESA");
		sql.append("                           AND ITEMPEDIDOERP.CDREPRESENTANTE = PEDIDOERP.CDREPRESENTANTE");
		sql.append("                           AND ITEMPEDIDOERP.NUPEDIDO = PEDIDOERP.NUPEDIDO");
		sql.append("                           AND ITEMPEDIDOERP.FLORIGEMPEDIDO = PEDIDOERP.FLORIGEMPEDIDO");
		sql.append(addItemPedidoRequirements("ITEMPEDIDOERP"));
		sql.append("                  JOIN TBLVPPRODUTO PRODUTO ON PRODUTO.CDEMPRESA = TB.CDEMPRESA");
		sql.append("             AND PRODUTO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("             AND PRODUTO.CDPRODUTO = ITEMPEDIDOERP.CDPRODUTO");
		sql.append("             AND PRODUTO.CDFAMILIADESCPROG IN (SELECT TF.CDFAMILIADESCPROG");
		sql.append("                                               FROM TBLVPDESCPROGCONFIGFAM TF");
		sql.append("                                               WHERE TF.CDEMPRESA = TB.CDEMPRESA");
		sql.append("                                                 AND TF.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("                                                 AND TF.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO");
		if (qtd) {
			sql.append(" AND TF.FLTIPOFAMILIAPRO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		} else {
			sql.append(" AND TF.FLFAMACUVALORMIN = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		}
		sql.append("         ) WHERE PEDIDOERP.CDEMPRESA = TB.CDEMPRESA");
		sql.append("           AND PEDIDOERP.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		if (descProgressivoConfig.pedidoFilter != null && !erp) {
			sql.append(" AND PEDIDOERP.NUPEDIDO <> ").append(Sql.getValue(descProgressivoConfig.pedidoFilter.nuPedido));
		}
		sql.append(addPedidoRequirements(descProgressivoConfig.cliente.cdCliente, "PEDIDOERP", "TB", true, false, qtd));
		sql.append("           AND tb.CDEMPRESA = ").append(Sql.getValue(descProgressivoConfig.cdEmpresa));
		sql.append("           AND tb.CDREPRESENTANTE = ").append(Sql.getValue(descProgressivoConfig.cdRepresentante));
		sql.append("           AND tb.CDDESCPROGRESSIVO  = ").append(Sql.getValue(descProgressivoConfig.cdDescProgressivo));
		sql.append("           AND tb.NUNIVELCLIENTE = ").append(Sql.getValue(descProgressivoConfig.nuNivelCliente));
		sql.append("           AND tb.DTINICIALVIGENCIA  <= ").append(Sql.getValue(descProgressivoConfig.dtInicialVigencia));
		sql.append("           AND tb.DTFIMVIGENCIA  >= ").append(Sql.getValue(descProgressivoConfig.dtFimVigencia));
		return sql.toString();
	}

	private String getSqlJoinPedidoAtual() {
		StringBuffer sql = getSqlBuffer();
		sql.append("     FROM TBLVPPEDIDO PEDIDO");
		sql.append("              JOIN TBLVPITEMPEDIDO ITEMPEDIDO");
		sql.append("                   ON ITEMPEDIDO.CDEMPRESA = PEDIDO.CDEMPRESA");
		sql.append("                       AND ITEMPEDIDO.CDREPRESENTANTE = PEDIDO.CDREPRESENTANTE");
		sql.append("                       AND ITEMPEDIDO.NUPEDIDO = PEDIDO.NUPEDIDO");
		sql.append("                       AND ITEMPEDIDO.FLORIGEMPEDIDO = PEDIDO.FLORIGEMPEDIDO");
		sql.append(addItemPedidoRequirements("ITEMPEDIDO"));
		sql.append("              JOIN TBLVPPRODUTO PRODUTO ON PRODUTO.CDEMPRESA = TB.CDEMPRESA");
		sql.append("         AND PRODUTO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("         AND PRODUTO.CDPRODUTO = ITEMPEDIDO.CDPRODUTO");
		sql.append("         AND PRODUTO.CDFAMILIADESCPROG IN (SELECT TF.CDFAMILIADESCPROG");
		sql.append("                                           FROM TBLVPDESCPROGCONFIGFAM TF");
		sql.append("                                           WHERE TF.CDEMPRESA = TB.CDEMPRESA");
		sql.append("                                             AND TF.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		sql.append("                                             AND TF.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO");
		sql.append("                                             AND TF.FLTIPOFAMILIAPRO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append("     ) WHERE PEDIDO.CDEMPRESA = TB.CDEMPRESA");
		sql.append("       AND PEDIDO.CDREPRESENTANTE = TB.CDREPRESENTANTE");
		return sql.toString();
	}

	public int countQtExtraByPedido(DescProgressivoConfig descProgressivoConfig, Pedido pedido, boolean consideraOutrosPedidos) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COALESCE((");
		sql.append("    SELECT COUNT(DISTINCT PRODUTO.CDFAMILIADESCPROG)");
		sql.append(getSqlJoinPedidoAtual());
		sql.append("       AND PEDIDO.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append("       AND PEDIDO.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		if (consideraOutrosPedidos) {
			sql.append("       AND PEDIDO.NUPEDIDO <> ").append(Sql.getValue(pedido.nuPedido));
		}
		if (!consideraOutrosPedidos) {
		sql.append("       AND PEDIDO.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append("       AND PEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		}
		sql.append(addPedidoRequirements(pedido.getCliente().cdCliente, "PEDIDO", null, false, false, true));
		sql.append(getSqlNotInSelectAtual(descProgressivoConfig, true, true));
		if (!consideraOutrosPedidos && !LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
			sql.append(") ");
			sql.append(getSqlNotInSelectAtual(descProgressivoConfig, true, false));
		}
		sql.append(" )), 0) AS QTATINGIDO");
		sql.append(" FROM TBLVPDESCPROGRESSIVOCONFIG TB");
		addWhereByExample(descProgressivoConfig, sql);
		return getInt(sql.toString());
	}

	public String getSqlCdProdutoInDescProgressivoConfig(String tabelaCdProduto, DescProgressivoConfig descProgressivoConfigFilter, DescProgConfigFam descProgConfigFamFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(tabelaCdProduto).append(" IN ( ");
		Vector cdProdutoList = getCdProdutoInList(descProgressivoConfigFilter, descProgConfigFamFilter);
		int size = cdProdutoList.size();
		for (int i = 0; i < size; i++) {
			if (i > 0) {
				sql.append(",");
			}
			sql.append(Sql.getValue(cdProdutoList.items[i]));
		}
		sql.append(")");
		return sql.toString();
	}

	private Vector getCdProdutoInList(DescProgressivoConfig descProgressivoConfigFilter, DescProgConfigFam descProgConfigFamFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT produtoFam.CDPRODUTO");
		sql.append("       FROM TBLVPPRODUTO produtoFam");
		sql.append("            JOIN TBLVPDESCPROGCONFIGFAM descProgConfigFam");
		sql.append("            ON produtoFam.CDEMPRESA = descProgConfigFam.CDEMPRESA");
		sql.append("            AND produtoFam.CDREPRESENTANTE = descProgConfigFam.CDREPRESENTANTE");
		sql.append("            AND produtoFam.CDFAMILIADESCPROG = descProgConfigFam.CDFAMILIADESCPROG");
		sql.append("            JOIN TBLVPDESCPROGRESSIVOCONFIG descProgConfig");
		sql.append("            ON descProgConfigFam.CDEMPRESA = descProgConfig.CDEMPRESA");
		sql.append("            AND descProgConfigFam.CDREPRESENTANTE = descProgConfig.CDREPRESENTANTE");
		sql.append("            AND descProgConfigFam.CDDESCPROGRESSIVO = descProgConfig.CDDESCPROGRESSIVO");
		sql.append(" WHERE ");
		sql.append(" descProgConfig.CDDESCPROGRESSIVO = ").append(Sql.getValue(descProgressivoConfigFilter.cdDescProgressivo));
		sql.append(" AND descProgConfig.CDEMPRESA = ").append(Sql.getValue(descProgressivoConfigFilter.cdEmpresa));
		sql.append(" AND descProgConfig.CDREPRESENTANTE = ").append(Sql.getValue(descProgressivoConfigFilter.cdRepresentante));
		if (descProgConfigFamFilter.isFlTipoFamiliaCon()) {
			sql.append(" AND descProgConfigFam.FLTIPOFAMILIACON = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		}

		if (descProgConfigFamFilter.isFlTipoFamiliaPro()) {
			sql.append(" AND descProgConfigFam.FLTIPOFAMILIAPRO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		}

		if (descProgConfigFamFilter.isFlFamAcuValorMin()) {
			sql.append(" AND descProgConfigFam.FLFAMACUVALORMIN = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		}
		if (descProgConfigFamFilter.isFlFamAcuValorMax()) {
			sql.append(" AND descProgConfigFam.FLFAMACUVALORMAX = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		}

		if (ValueUtil.isNotEmpty(descProgConfigFamFilter.cdFamiliaDescProg)) {
			sql.append(" AND descProgConfigFam.CDFAMILIADESCPROG = ").append(Sql.getValue(descProgConfigFamFilter.cdFamiliaDescProg));
		}
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
				result.addElement(rs.getString(1));
			}
			return result;
		}
	}

}
