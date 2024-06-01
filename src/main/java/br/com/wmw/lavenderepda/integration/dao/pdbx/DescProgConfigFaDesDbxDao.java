package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescProgConfigFaDes;
import br.com.wmw.lavenderepda.business.domain.DescProgFaixa;
import br.com.wmw.lavenderepda.business.domain.DescProgFamilia;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class DescProgConfigFaDesDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgConfigFaDes();
	}

    private static DescProgConfigFaDesDbxDao instance;

    public DescProgConfigFaDesDbxDao() { super(DescProgConfigFaDes.TABLE_NAME); }
    public static DescProgConfigFaDesDbxDao getInstance() { return (instance == null) ? instance = new DescProgConfigFaDesDbxDao() : instance; }
	@Override protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { addSelectColumns(null, sql); }
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return populate(domainFilter, rs); }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
	    DescProgConfigFaDes descProgConfigFaDes = new DescProgConfigFaDes();
	    descProgConfigFaDes.rowKey = rs.getString("rowkey");
	    descProgConfigFaDes.cdEmpresa = rs.getString("cdEmpresa");
	    descProgConfigFaDes.cdRepresentante = rs.getString("cdRepresentante");
	    descProgConfigFaDes.cdDescProgressivo = rs.getString("cdDescProgressivo");
	    descProgConfigFaDes.qtFamilia = rs.getInt("qtFamilia");
	    descProgConfigFaDes.vlPctDescProg = rs.getDouble("vlPctDescProg");
	    descProgConfigFaDes.dsFaixa = rs.getString("dsFaixa");
	    descProgConfigFaDes.cdUsuario = rs.getString("cdUsuario");
        return descProgConfigFaDes;
    }

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDREPRESENTANTE,");
	    sql.append(" tb.CDDESCPROGRESSIVO,");
	    sql.append(" tb.QTFAMILIA,");
	    sql.append(" tb.VLPCTDESCPROG,");
	    sql.append(" tb.DSFAIXA,");
	    sql.append(" tb.CDUSUARIO");
    }

	@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		DescProgConfigFaDes descProgConfigFaDes = (DescProgConfigFaDes) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("CDEMPRESA = ", descProgConfigFaDes.cdEmpresa);
	    sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descProgConfigFaDes.cdRepresentante);
	    sqlWhereClause.addAndCondition("CDDESCPROGRESSIVO  = ", descProgConfigFaDes.cdDescProgressivo);
		sqlWhereClause.addAndCondition("QTFAMILIA  = ", descProgConfigFaDes.qtFamilia);
		sql.append(sqlWhereClause.getSql());
    }

    public DescProgConfigFaDes findFaixaDescProgByProdutoCliente(Produto produto, Cliente cliente) throws SQLException {
    	if (produto == null || cliente == null || ValueUtil.isEmpty(produto.cdFamiliaDescProg)) return null;
	    StringBuffer sql = getSqlBuffer();
	    getBeginSql(produto, sql);
	    sql.append("                 SELECT COUNT(DISTINCT PRODUTO.CDFAMILIADESCPROG) ");
	    getSelectItemPedidoErp(cliente, sql);
	    sql.append("                ) >= TB.QTFAMILIA");

		// VlMinimo
	    sql.append("                AND COALESCE((");
	    sql.append(DescProgressivoConfigDbxDao.getSqlSumVlTotalItemPedido(true));
	    addJoinProduto(sql, false);
	    sql.append("                 WHERE PEDIDO.CDEMPRESA = DESCPROGRESSIVOCONFIG.CDEMPRESA");
	    sql.append("                   AND PEDIDO.CDREPRESENTANTE = DESCPROGRESSIVOCONFIG.CDREPRESENTANTE");
	    sql.append(DescProgressivoConfigDbxDao.addPedidoRequirements(cliente.cdCliente, "PEDIDO", "DESCPROGRESSIVOCONFIG", true, false, false));
	    sql.append("             ), 0) >= DESCPROGRESSIVOCONFIG.VLMINDESCPROGRESSIVO");

	    // VlMaximo
		sql.append("                AND ( CASE WHEN COALESCE(DESCPROGRESSIVOCONFIG.VLMAXDESCPROGRESSIVO, 0) > 0 THEN COALESCE((");
		sql.append(DescProgressivoConfigDbxDao.getSqlSumVlTotalItemPedido(true));
		addJoinProduto(sql, true);
		sql.append("                 WHERE PEDIDO.CDEMPRESA = DESCPROGRESSIVOCONFIG.CDEMPRESA");
		sql.append("                   AND PEDIDO.CDREPRESENTANTE = DESCPROGRESSIVOCONFIG.CDREPRESENTANTE");
		sql.append(DescProgressivoConfigDbxDao.addPedidoRequirements(cliente.cdCliente, "PEDIDO", "DESCPROGRESSIVOCONFIG", true, true, false));
		sql.append("             ), 0) ELSE -1 END < COALESCE(DESCPROGRESSIVOCONFIG.VLMAXDESCPROGRESSIVO, 0))");
	    
	    return addEndSql(produto, cliente, sql);
    }

	private void getSelectItemPedidoErp(Cliente cliente, StringBuffer sql) {
		sql.append("                 FROM TBLVPPEDIDOERP PEDIDO ");
		sql.append("                          JOIN TBLVPITEMPEDIDOERP TC ON ");
		sql.append("                         PEDIDO.CDEMPRESA = TC.CDEMPRESA ");
		sql.append("                         AND PEDIDO.CDREPRESENTANTE = TC.CDREPRESENTANTE ");
		sql.append("                         AND PEDIDO.NUPEDIDO = TC.NUPEDIDO ");
		sql.append("                         AND PEDIDO.FLORIGEMPEDIDO = TC.FLORIGEMPEDIDO ");
		sql.append(DescProgressivoConfigDbxDao.addItemPedidoRequirements("TC"));
		sql.append("                          JOIN TBLVPPRODUTO PRODUTO ON ");
		sql.append("                         PRODUTO.CDEMPRESA = TC.CDEMPRESA ");
		sql.append("                         AND PRODUTO.CDREPRESENTANTE = TC.CDREPRESENTANTE ");
		sql.append("                         AND PRODUTO.CDPRODUTO = TC.CDPRODUTO ");
		sql.append("                          JOIN TBLVPDESCPROGCONFIGFAM DPCF ON ");
		sql.append("                             DPCF.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("                         AND DPCF.CDREPRESENTANTE = DPCF.CDREPRESENTANTE ");
		sql.append("                         AND DPCF.CDDESCPROGRESSIVO = DESCPROGRESSIVOCONFIG.CDDESCPROGRESSIVO ");
		sql.append("                         AND PRODUTO.CDFAMILIADESCPROG = DPCF.CDFAMILIADESCPROG ");
		sql.append("                    WHERE PEDIDO.CDEMPRESA = DPCF.CDEMPRESA AND PEDIDO.CDREPRESENTANTE = DPCF.CDREPRESENTANTE AND DPCF.FLTIPOFAMILIAPRO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append(DescProgressivoConfigDbxDao.addPedidoRequirements(cliente.cdCliente, "PEDIDO", "DESCPROGRESSIVOCONFIG", true, false, true));
	}

	public DescProgConfigFaDes findFaixaDescProgByProdutoClienteConsideraPedidoAtual(ItemPedido itemPedido) throws SQLException {
    	Produto produto = itemPedido.getProduto();
    	Pedido pedido = itemPedido.pedido;
    	Cliente cliente = pedido.getCliente();
		if (produto == null || cliente == null || ValueUtil.isEmpty(produto.cdFamiliaDescProg)) return null;
		StringBuffer sql = getSqlBuffer();
		getBeginSql(produto, sql);
		sql.append("                 SELECT COUNT(DISTINCT INNERSQL.CDFAMILIADESCPROG) FROM ");
		sql.append("                 (SELECT PRODUTO.CDFAMILIADESCPROG ");
		getSelectItemPedidoErp(cliente, sql);
		sql.append("                 UNION ALL ");
		sql.append("                 SELECT PRODUTO.CDFAMILIADESCPROG ");
		sql.append("                 FROM TBLVPITEMPEDIDO ITP ");
		sql.append("                          JOIN TBLVPPEDIDO PEDIDO ON ");
		sql.append("                         PEDIDO.CDEMPRESA = ITP.CDEMPRESA ");
		sql.append("                         AND PEDIDO.CDREPRESENTANTE = ITP.CDREPRESENTANTE ");
		sql.append("                         AND PEDIDO.NUPEDIDO = ITP.NUPEDIDO ");
		sql.append("                         AND PEDIDO.FLORIGEMPEDIDO = ITP.FLORIGEMPEDIDO ");
		sql.append(DescProgressivoConfigDbxDao.addItemPedidoRequirements("ITP"));
		sql.append("                          JOIN TBLVPPRODUTO PRODUTO ON ");
		sql.append("                         PRODUTO.CDEMPRESA = ITP.CDEMPRESA ");
		sql.append("                         AND PRODUTO.CDREPRESENTANTE = ITP.CDREPRESENTANTE ");
		sql.append("                         AND (PRODUTO.CDPRODUTO = ITP.CDPRODUTO OR PRODUTO.CDPRODUTO = ").append(Sql.getValue(produto.cdProduto)).append(")");
		sql.append("                          JOIN TBLVPDESCPROGCONFIGFAM DPCF ON ");
		sql.append("                             DPCF.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("                         AND DPCF.CDREPRESENTANTE = DPCF.CDREPRESENTANTE ");
		sql.append("                         AND DPCF.CDDESCPROGRESSIVO = DESCPROGRESSIVOCONFIG.CDDESCPROGRESSIVO ");
		sql.append("                         AND PRODUTO.CDFAMILIADESCPROG = DPCF.CDFAMILIADESCPROG ");
		sql.append("                    WHERE ITP.CDEMPRESA = DPCF.CDEMPRESA AND ITP.CDREPRESENTANTE = DPCF.CDREPRESENTANTE AND DPCF.FLTIPOFAMILIAPRO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		if (LavenderePdaConfig.ignoraOutroPedidoAbertoDescProg) {
			sql.append("                   AND PEDIDO.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		}
		sql.append("                   AND PEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(OrigemPedido.FLORIGEMPEDIDO_PDA));
		sql.append("                   AND PEDIDO.CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
		DescProgressivoConfigDbxDao.addStatusPedidoConsideraFamilia("PEDIDO", sql);
		sql.append("                ) INNERSQL ");
		sql.append("                ) >= TB.QTFAMILIA");

		// Valor Minimo
		sql.append("                AND (");
		addSelectForGetTotalValuesFromItem(itemPedido, produto, pedido, cliente, sql, false);
		sql.append(" ) >= DESCPROGRESSIVOCONFIG.VLMINDESCPROGRESSIVO )");

		// Valor Máximo
		sql.append("                AND (( CASE WHEN COALESCE(DESCPROGRESSIVOCONFIG.VLMAXDESCPROGRESSIVO, 0) > 0 THEN (");
		addSelectForGetTotalValuesFromItem(itemPedido, produto, pedido, cliente, sql, true);
		sql.append(" )) ELSE -1 END) < COALESCE(DESCPROGRESSIVOCONFIG.VLMAXDESCPROGRESSIVO, 0) )");

		return addEndSql(produto, cliente, sql);
	}

	private void addSelectForGetTotalValuesFromItem(ItemPedido itemPedido, Produto produto, Pedido pedido, Cliente cliente, StringBuffer sql, boolean isMax) throws SQLException {
		sql.append("                COALESCE((");
		sql.append(DescProgressivoConfigDbxDao.getSqlSumVlTotalItemPedido(true));
		addJoinProduto(sql, isMax);
		sql.append("                 WHERE PEDIDO.CDEMPRESA = DESCPROGRESSIVOCONFIG.CDEMPRESA");
		sql.append("                   AND PEDIDO.CDREPRESENTANTE = DESCPROGRESSIVOCONFIG.CDREPRESENTANTE");
		sql.append(DescProgressivoConfigDbxDao.addPedidoRequirements(cliente.cdCliente, "PEDIDO", "DESCPROGRESSIVOCONFIG", true, isMax, false));
		sql.append("             ), 0) + ");
		sql.append("                 (COALESCE((");
		sql.append(DescProgressivoConfigDbxDao.getSqlSumVlTotalItemPedido(false, itemPedido.pedido.getCondicaoPagamento()));
		//sql.append("                             AND ITEMPEDIDO.CDPRODUTO != ").append(Sql.getValue(itemPedido.cdProduto));
		addJoinProduto(sql, isMax);
		sql.append("                 WHERE PEDIDO.CDEMPRESA = DESCPROGRESSIVOCONFIG.CDEMPRESA");
		sql.append("                   AND PEDIDO.CDREPRESENTANTE = DESCPROGRESSIVOCONFIG.CDREPRESENTANTE");
		sql.append("                   AND PEDIDO.CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
		//sql.append("                   AND PEDIDO.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(DescProgressivoConfigDbxDao.addPedidoRequirements(null, "PEDIDO", null, false, isMax, false));
		sql.append("                   AND PEDIDO.FLORIGEMPEDIDO = ").append(Sql.getValue(OrigemPedido.FLORIGEMPEDIDO_PDA));
		sql.append("  AND (PRODUTO.CDPRODUTO <> " + Sql.getValue(produto.cdProduto));
		sql.append(" AND PEDIDO.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(" OR PEDIDO.NUPEDIDO <> ").append(Sql.getValue(pedido.nuPedido)).append(")");
		sql.append("             ), 0) ");
		sql.append("   + (");
		sql.append(" SELECT CASE WHEN (");
		sql.append("                         SELECT TF.").append(isMax ? "FLFAMACUVALORMAX" : "FLFAMACUVALORMIN").append(" FROM TBLVPDESCPROGCONFIGFAM TF");
		sql.append("                         WHERE TF.CDEMPRESA = DESCPROGRESSIVOCONFIG.CDEMPRESA");
		sql.append("                           AND TF.CDREPRESENTANTE = DESCPROGRESSIVOCONFIG.CDREPRESENTANTE");
		sql.append("                           AND TF.CDDESCPROGRESSIVO = DESCPROGRESSIVOCONFIG.CDDESCPROGRESSIVO");
		sql.append("                           AND TF.CDFAMILIADESCPROG = ").append(Sql.getValue(produto.cdFamiliaDescProg));
		sql.append(") = 'S' THEN ( ");
		sql.append(" SELECT round( ").append(itemPedido.vlBaseItemTabelaPreco);
		sql.append(DescProgressivoConfigDbxDao.getCalculosIndiceSql(pedido.getCondicaoPagamento()));
		sql.append(" , ").append(LavenderePdaConfig.nuCasasDecimais).append(") * " ).append(Sql.getValue(itemPedido.getQtItemFisico()));
		sql.append(")");
		sql.append(" ELSE 0 END");
		sql.append(")");
	}

	private void getBeginSql(Produto produto, StringBuffer sql) {
		sql.append("SELECT * FROM (");
		sql.append("         SELECT TB.CDDESCPROGRESSIVO, ");
		sql.append("                TB.QTFAMILIA, ");
		sql.append("                TB.VLPCTDESCPROG, ");
		sql.append("                TB.DSFAIXA, ");
		sql.append("                DESCPROGFAMILIA.DSFAMILIAPROD, ");
		sql.append("                DESCPROGFAIXA.DSFAIXACLI, ");
		sql.append("                DESCPROGCONFIGFACLI.CDCLIENTE ");
		sql.append("         FROM TBLVPDESCPROGCONFIGFADES TB ");
		sql.append("                  JOIN TBLVPDESCPROGRESSIVOCONFIG DESCPROGRESSIVOCONFIG ON ");
		sql.append("                 DESCPROGRESSIVOCONFIG.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("                 AND DESCPROGRESSIVOCONFIG.CDREPRESENTANTE = TB.CDREPRESENTANTE ");
		sql.append("                 AND DESCPROGRESSIVOCONFIG.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO ");
		sql.append("                  JOIN TBLVPDESCPROGCONFIGFAM DESCPROGCONFIGFAM ON ");
		sql.append("                 DESCPROGCONFIGFAM.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("                 AND DESCPROGCONFIGFAM.CDREPRESENTANTE = TB.CDREPRESENTANTE ");
		sql.append("                 AND DESCPROGCONFIGFAM.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO ");
		sql.append("                 AND DESCPROGCONFIGFAM.CDFAMILIADESCPROG = ").append(Sql.getValue(produto.cdFamiliaDescProg));
		sql.append("                 AND ( ");
	}

	private void addJoinProduto(StringBuffer sql, boolean isMax) {
		sql.append("                             JOIN TBLVPPRODUTO PRODUTO ON");
		sql.append("                             PRODUTO.CDEMPRESA = DESCPROGRESSIVOCONFIG.CDEMPRESA");
		sql.append("                         AND PRODUTO.CDREPRESENTANTE = DESCPROGRESSIVOCONFIG.CDREPRESENTANTE");
		sql.append("                         AND PRODUTO.CDPRODUTO = ITEMPEDIDO.CDPRODUTO");
		sql.append("                         AND PRODUTO.CDFAMILIADESCPROG IN (");
		sql.append("                         SELECT TF.CDFAMILIADESCPROG FROM TBLVPDESCPROGCONFIGFAM TF");
		sql.append("                         WHERE TF.CDEMPRESA = DESCPROGRESSIVOCONFIG.CDEMPRESA");
		sql.append("                           AND TF.CDREPRESENTANTE = DESCPROGRESSIVOCONFIG.CDREPRESENTANTE");
		sql.append("                           AND TF.CDDESCPROGRESSIVO = DESCPROGRESSIVOCONFIG.CDDESCPROGRESSIVO");
		if (!isMax) {
			sql.append("                           AND TF.FLFAMACUVALORMIN = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		} else {
			sql.append("                           AND TF.FLFAMACUVALORMAX = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		}
		sql.append("                     )");
	}

	private DescProgConfigFaDes addEndSql(Produto produto, Cliente cliente, StringBuffer sql) throws SQLException {
		sql.append("                 AND DESCPROGCONFIGFAM.FLTIPOFAMILIACON = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append("                  JOIN TBLVPDESCPROGFAMILIA DESCPROGFAMILIA ON ");
		sql.append("                 DESCPROGFAMILIA.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("                 AND DESCPROGFAMILIA.CDREPRESENTANTE = TB.CDREPRESENTANTE ");
		sql.append("                 AND DESCPROGFAMILIA.CDFAMILIADESCPROG = DESCPROGCONFIGFAM.CDFAMILIADESCPROG ");
		sql.append("                  JOIN TBLVPDESCPROGCONFIGFACLI DESCPROGCONFIGFACLI ON ");
		sql.append("                 DESCPROGCONFIGFACLI.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("                 AND DESCPROGCONFIGFACLI.CDREPRESENTANTE = TB.CDREPRESENTANTE ");
		sql.append("                 AND DESCPROGCONFIGFACLI.CDDESCPROGRESSIVO = TB.CDDESCPROGRESSIVO ");
		sql.append("                 AND (DESCPROGCONFIGFACLI.CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente)).append(" OR ");
		String cdFaixaCli = cliente.cdFaixaCli;
		if (ValueUtil.isEmpty(cdFaixaCli)) {
			sql.append(" DESCPROGCONFIGFACLI.CDFAIXACLI = FAIXACLIENTE.CDFAIXACLI) ")
			.append(" LEFT JOIN TBLVPFAIXACLIENTE FAIXACLIENTE ON FAIXACLIENTE.CDCLIENTE =  ").append(Sql.getValue(cliente.cdCliente))
			.append(" AND FAIXACLIENTE.CDFAIXACLI = DESCPROGCONFIGFACLI.CDFAIXACLI ")
			.append(" AND FAIXACLIENTE.CDEMPRESA = tb.CDEMPRESA ")
			.append(" AND FAIXACLIENTE.CDREPRESENTANTE = tb.CDREPRESENTANTE ");
		} else {
			sql.append(" DESCPROGCONFIGFACLI.CDFAIXACLI = ").append(Sql.getValue(cdFaixaCli)).append(")");
		}
		sql.append("                  LEFT JOIN TBLVPDESCPROGFAIXA DESCPROGFAIXA ON ");
		sql.append("                 DESCPROGFAIXA.CDEMPRESA = TB.CDEMPRESA ");
		sql.append("                 AND DESCPROGFAIXA.CDREPRESENTANTE = TB.CDREPRESENTANTE ");
		sql.append("                 AND DESCPROGFAIXA.CDFAIXACLI = DESCPROGCONFIGFACLI.CDFAIXACLI ");
		sql.append("         WHERE TB.CDEMPRESA = ").append(Sql.getValue(produto.cdEmpresa));
		sql.append("           AND TB.CDREPRESENTANTE = ").append(Sql.getValue(produto.cdRepresentante));
		sql.append("           AND DESCPROGRESSIVOCONFIG.DTINICIALVIGENCIA <= ").append(Sql.getValue(DateUtil.getCurrentDate()));
		sql.append("           AND DESCPROGRESSIVOCONFIG.DTFIMVIGENCIA >= ").append(Sql.getValue(DateUtil.getCurrentDate()));
		sql.append("         GROUP BY TB.CDDESCPROGRESSIVO, TB.QTFAMILIA, TB.VLPCTDESCPROG, TB.DSFAIXA, DESCPROGFAMILIA.DSFAMILIAPROD, ");
		sql.append("                  DESCPROGFAIXA.DSFAIXACLI, DESCPROGCONFIGFACLI.CDCLIENTE ");
		sql.append("     ) TA ");
		sql.append("ORDER BY CASE WHEN (TA.CDCLIENTE IS NULL OR TA.CDCLIENTE = '0') THEN 2 ELSE 1 END ASC, QTFAMILIA DESC ");
		sql.append("LIMIT 1 ");
		try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				DescProgConfigFaDes descProgConfigFaDes = new DescProgConfigFaDes();
				descProgConfigFaDes.descProgFamilia = new DescProgFamilia();
				descProgConfigFaDes.descProgFaixa = new DescProgFaixa();
				descProgConfigFaDes.cdEmpresa = produto.cdEmpresa;
				descProgConfigFaDes.cdRepresentante = produto.cdRepresentante;
				int i = 1;
				descProgConfigFaDes.cdDescProgressivo = rs.getString(i++);
				descProgConfigFaDes.qtFamilia = rs.getInt(i++);
				descProgConfigFaDes.vlPctDescProg = rs.getDouble(i++);
				descProgConfigFaDes.dsFaixa = rs.getString(i++);

				descProgConfigFaDes.descProgFamilia.dsFamiliaProd = rs.getString(i++);
				descProgConfigFaDes.descProgFaixa.dsFaixaCli  = rs.getString(i);
				return descProgConfigFaDes;
			}
		}
		return null;
	}


}
