package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemKitAgrSimilar;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.service.ItemKitService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class KitPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Kit();
	}

	private static KitPdbxDao instance;

	public KitPdbxDao() {
		super(Kit.TABLE_NAME);
	}

	public static KitPdbxDao getInstance() {
		if (instance == null) {
			instance = new KitPdbxDao();
		}
		return instance;
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDKIT,");
		sql.append(" tb.DSKIT,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.DTVIGENCIAINICIAL,");
		sql.append(" tb.DTVIGENCIAFINAL,");
		sql.append(" tb.QTMINITENSKIT,");
		sql.append(" tb.FLEDITADESCONTO,");
		sql.append(" tb.FLVALIDAESTOQUE,");
		sql.append(" tb.DSAGRUPADORKIT");
	}

	//@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		Kit kit = new Kit();
		kit.rowKey = rs.getString("rowkey");
		kit.cdEmpresa = rs.getString("cdEmpresa");
		kit.cdRepresentante = rs.getString("cdRepresentante");
		kit.cdKit = rs.getString("cdKit");
		kit.dsKit = rs.getString("dsKit");
		kit.cdTabelaPreco = rs.getString("cdTabelaPreco");
		kit.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
		kit.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
		kit.qtMinItensKit = rs.getInt("qtMinItensKit");
		kit.flEditaDesconto = rs.getString("flEditaDesconto");
		kit.flValidaEstoque = rs.getString("flValidaEstoque");
		kit.dsAgrupadorKit = rs.getString("dsAgrupadorKit");
		return kit;
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		Kit kit = (Kit) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", kit.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", kit.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDKIT = ", kit.cdKit);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", kit.cdTabelaPreco);
		sqlWhereClause.addAndCondition("tb.DTVIGENCIAINICIAL <= ", kit.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("tb.DTVIGENCIAFINAL >= ", kit.dtVigenciaFinal);
		//--
		sql.append(sqlWhereClause.getSql());
	}

	protected void addWhereByExampleDtVigencia(Kit kit, StringBuffer sql) throws java.sql.SQLException {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", kit.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", kit.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDKIT = ", kit.cdKit);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", kit.cdTabelaPreco);
		//--
		sql.append(sqlWhereClause.getSql());
		sql.append("AND (tb.DTVIGENCIAINICIAL <= " + Sql.getValue(kit.dtVigenciaInicial)
				+ "		AND (tb.DTVIGENCIAFINAL >= " + Sql.getValue(kit.dtVigenciaFinal)
				+ "			OR tb.DTVIGENCIAFINAL IS NULL)"
				+ "		OR tb.DTVIGENCIAFINAL >= " + Sql.getValue(kit.dtVigenciaFinal)
				+ "			AND (tb.DTVIGENCIAINICIAL <= " + Sql.getValue(kit.dtVigenciaInicial)
				+ "			OR tb.DTVIGENCIAINICIAL IS NULL)"
				+ "		OR tb.DTVIGENCIAINICIAL IS NULL AND tb.DTVIGENCIAFINAL IS NULL)");
	}
	
	public Vector findListKitFechado(Kit kitFilter, String cdTabelaPreco) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(kitFilter, sql);
		sql.append(" FROM ").append(Kit.TABLE_NAME).append(" TB ");
		addJoinKitTabPreco(sql, cdTabelaPreco);
		addWhereByExampleDtVigencia(kitFilter, sql);
		Vector listKit = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				final Kit kit = (Kit) populate(kitFilter, rs);
				final ItemKit itemKit = createItemKitFilterFromKit(kit);
				if (ItemKitService.getInstance().isItensKitComPreco(itemKit, cdTabelaPreco)) {
					listKit.addElement(kit);
				}
			}
			return listKit;
		}
	}
	
	private void addJoinKitTabPreco(StringBuffer sql, String cdTabelaPreco) {
		sql.append(" JOIN TBLVPKITTABPRECO kitTabPreco ON")
		.append(" tb.CDEMPRESA = kitTabPreco.CDEMPRESA AND")
		.append(" tb.CDREPRESENTANTE = kitTabPreco.CDREPRESENTANTE AND")
		.append(" tb.CDKIT = kitTabPreco.CDKIT AND")
		.append(" kitTabPreco.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
	}

	private ItemKit createItemKitFilterFromKit(Kit kitFinal) {
		ItemKit itemKit = new ItemKit();
		itemKit.cdEmpresa = kitFinal.cdEmpresa;
		itemKit.cdRepresentante = kitFinal.cdRepresentante;
		itemKit.cdKit = kitFinal.cdKit;
		return itemKit;
	}
	
	
	public Vector findKitListForCombo(Kit kit, String cdCliente, String nuPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(kit, sql);
		sql.append(" FROM TBLVPKIT tb ");
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			sql.append(" LEFT JOIN TBLVPKITCLIENTE kitCliente ON ")
			.append(" tb.CDEMPRESA = kitCliente.CDEMPRESA AND ")
			.append(" tb.CDREPRESENTANTE = kitCliente.CDREPRESENTANTE AND ")
			.append(" tb.CDKIT = kitCliente.CDKIT AND ")
			.append(" tb.CDTABELAPRECO = kitCliente.CDTABELAPRECO AND ")
			.append(" kitCliente.CDCLIENTE = ").append(Sql.getValue(cdCliente));
		}
		addWhereByExample(kit, sql);
		addExistsItemKitComPreco(kit, sql);
		//-- Todos os itens com estoque
		sql.append(" AND (NOT EXISTS (")
		   .append(getInnerSqlKitVigenteEstoque(cdCliente, kit.cdLocalEstoque))
           .append(" AND (IFNULL(estErp.QTESTOQUE, 0 ) - IFNULL(estPda.QTESTOQUE, 0 ) <= 0 ");
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			sql.append( "AND IFNULL(item.FLAGRUPADORSIMILARIDADE, 'N') = 'N'");
		}
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" OR prod.CDPRODUTO IS NOT NULL");
		}
		sql.append("))");
		if (LavenderePdaConfig.isUsaValidaEstoqueKit()) {
			sql.append(" OR COALESCE(tb.FLVALIDAESTOQUE, 'N') = 'N'");
		}
		sql.append(")");
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			sql.append(" AND NOT EXISTS (")
			.append(getSqlRestricaoVendaClienteProduto(cdCliente, nuPedido))
			.append(")");
		}
		if (LavenderePdaConfig.isUsaKitTipo3()) {
			sql.append(" AND (kitCliente.CDCLIENTE IS NOT NULL OR")
			.append(" NOT EXISTS (SELECT 1")
			.append(" FROM TBLVPKITCLIENTE WHERE CDEMPRESA = tb.CDEMPRESA AND ")
			.append(" CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
			.append(" CDKIT = tb.CDKIT AND ")
			.append(" CDTABELAPRECO = tb.CDTABELAPRECO AND ")
			.append(" CDCLIENTE <> ").append(Sql.getValue(cdCliente)).append(")")
			.append(")");
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			addsGruposSimilaresComEstoque(kit, sql);
		}
		return findAll(kit, sql.toString());
	}

	private void addExistsItemKitComPreco(Kit kit, StringBuffer sql) {
		sql.append(" AND NOT EXISTS (");
		sql.append("SELECT 1 FROM ").append(ItemKit.TABLE_NAME).append(" item ");
		sql.append("LEFT JOIN ").append(ItemTabelaPreco.TABLE_NAME).append(" tabPreco ");
		sql.append("ON tabPreco.CDEMPRESA = item.CDEMPRESA ");
		sql.append("AND tabPreco.CDREPRESENTANTE = item.CDREPRESENTANTE ");
		sql.append("AND tabPreco.CDPRODUTO = item.CDPRODUTO ");
		sql.append("AND tabPreco.CDTABELAPRECO = tb.CDTABELAPRECO ");
		sql.append("LEFT JOIN ").append(Produto.TABLE_NAME).append(" prod ");
		sql.append("ON prod.CDEMPRESA = item.CDEMPRESA ");
		sql.append("AND prod.CDREPRESENTANTE = item.CDREPRESENTANTE ");
		sql.append("AND prod.CDPRODUTO = item.CDPRODUTO ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("item.CDEMPRESA = ", kit.cdEmpresa);
		sqlWhereClause.addAndCondition("item.CDREPRESENTANTE = ", kit.cdRepresentante);
		sqlWhereClause.addAndCondition("item.CDTABELAPRECO = ", kit.cdTabelaPreco);
		sqlWhereClause.addAndCondition("item.CDKIT = tb.CDKIT");
		sqlWhereClause.addAndCondition("(tabPreco.CDEMPRESA IS NULL or prod.CDPRODUTO IS NULL)");
		sql.append(sqlWhereClause.getSql());
		sql.append(")");
	}

	private String getInnerSqlKitVigenteEstoque(String cdCliente, String cdLocalEstoque) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 ")
		.append("FROM TBLVPITEMKIT item ");
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" LEFT JOIN TBLVPPRODUTOBLOQUEADO prod ON ")
			.append("prod.CDEMPRESA =  item.CDEMPRESA AND ")
			.append("prod.CDREPRESENTANTE = item.CDREPRESENTANTE AND ")
			.append("prod.CDPRODUTO = item.CDPRODUTO AND ")
			.append("(prod.CDTABELAPRECO = item.CDTABELAPRECO OR prod.CDTABELAPRECO = '0') ");
		}
        sql.append("LEFT JOIN TBLVPESTOQUE estErp ON estErp.CDEMPRESA = item.CDEMPRESA AND ")
        .append("estErp.CDREPRESENTANTE = item.CDREPRESENTANTE AND ")
        .append("estErp.CDPRODUTO = item.CDPRODUTO AND ")
        .append("estErp.CDITEMGRADE1 = '0' AND ")
        .append("estErp.CDITEMGRADE2 = '0' AND ")
        .append("estErp.CDITEMGRADE3 = '0' AND ")
        .append("estErp.CDLOCALESTOQUE = ").append(Sql.getValue(cdLocalEstoque)).append(" AND ")
        .append("estErp.FLORIGEMESTOQUE = 'E' ")
        .append("LEFT JOIN TBLVPESTOQUE estPda ON estPda.CDEMPRESA = item.CDEMPRESA AND ")
        .append("estPda.CDREPRESENTANTE = item.CDREPRESENTANTE AND ")
        .append("estPda.CDPRODUTO = item.CDPRODUTO AND ")
        .append("estPda.CDITEMGRADE1 = '0' AND ")
        .append("estPda.CDITEMGRADE2 = '0' AND ")
        .append("estPda.CDITEMGRADE3 = '0' AND ")
        .append("estPda.CDLOCALESTOQUE = ").append(Sql.getValue(cdLocalEstoque)).append(" AND ")
        .append("estPda.FLORIGEMESTOQUE = 'P' ")
		.append(" WHERE item.CDEMPRESA = tb.CDEMPRESA AND ")
        .append("item.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append("item.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
        .append("item.CDKIT = tb.CDKIT ");
        return sql.toString();
	}
	
	private String getSqlRestricaoVendaClienteProduto(String cdCliente, String nuPedido) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 ")
		.append("FROM TBLVPITEMKIT item ")
		.append(" WHERE item.CDEMPRESA = tb.CDEMPRESA AND ")
        .append("item.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append("item.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
        .append("item.CDKIT = tb.CDKIT ");
		DaoUtil.addNotExistsRestricaoProduto(sql, "item.cdProduto", cdCliente, nuPedido, 1, "item.qtItemKit", true, false, true, true);
		return sql.toString();
	}
	
	public void addsGruposSimilaresComEstoque(Kit kit, StringBuffer sql) throws SQLException {
		sql.append(" AND (NOT (");
		DaoUtil.addGrupoSimilarEstoqueExists(sql, kit, ItemKit.TABLE_NAME, "CDPRODUTO");
		sql.append(" AND ");
		DaoUtil.addGrupoSimilarEstoqueExists(sql, kit, ItemKitAgrSimilar.TABLE_NAME, "CDPRODUTOSIMILAR");
		sql.append(")");
		if (LavenderePdaConfig.isUsaValidaEstoqueKit()) {
			sql.append(" OR COALESCE(tb.FLVALIDAESTOQUE, 'N') = 'N'");
		}
		sql.append(")");
	}
	
	public Vector findKitsVigenciaExtrapoladaByPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select ");
		addSelectColumns(pedido, sql);
		sql.append(" from ");
		sql.append(tableName).append(" tb ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", pedido.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", pedido.cdRepresentante);
		sqlWhereClause.addAndCondition(" CDTABELAPRECO = ", pedido.cdTabelaPreco);
		sql.append(sqlWhereClause.getSql());
		sql.append(" and CDKIT in (select i.CDKIT from ");
		sql.append(ItemPedido.TABLE_NAME_ITEMPEDIDO);
		sql.append(" i ");
		sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" i.CDEMPRESA = ", pedido.cdEmpresa);
		sqlWhereClause.addAndCondition(" i.CDREPRESENTANTE = ", pedido.cdRepresentante);
		sqlWhereClause.addAndCondition(" i.FLORIGEMPEDIDO = ", pedido.flOrigemPedido);
		sqlWhereClause.addAndCondition(" i.NUPEDIDO = ", pedido.nuPedido);
		sqlWhereClause.addAndCondition(" i.FLTIPOITEMPEDIDO = ", TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		sqlWhereClause.addAndCondition(" i.NUSEQPRODUTO = ", ItemPedido.NUSEQPRODUTO_UNICO);
		sql.append(sqlWhereClause.getSql());
		sql.append(" ) ");
		sql.append(" and DTVIGENCIAFINAL < ").append(Sql.getValue(DateUtil.getCurrentDate()));
		return findAll(pedido, sql.toString());
	}
}