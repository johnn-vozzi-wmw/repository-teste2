package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKit;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemKitPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemKit();
	}

	private static ItemKitPdbxDao instance;

	public ItemKitPdbxDao() {
		super(ItemKit.TABLE_NAME);
	}

	public static ItemKitPdbxDao getInstance() {
		if (instance == null) {
			instance = new ItemKitPdbxDao();
		}
		return instance;
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.QTMINITEM,");
		sql.append(" tb.QTMAXITEM,");
		sql.append(" tb.VLPCTDESCONTO,");
		sql.append(" tb.VLUNITARIOKIT,");
		sql.append(" tb.QTITEMKIT,");
		sql.append(" tb.FLAGRUPADORSIMILARIDADE,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDKIT, ");
		sql.append(" tb.CDTABELAPRECO, ");
		sql.append(" tb.CDUNIDADE, ");
		sql.append(" tb.FLBONIFICADO ");
	}
	
	//@Override
	protected void addCacheColumns(StringBuffer sql) {
		sql.append(" CDPRODUTO,");
		sql.append(" QTMINITEM,");
		sql.append(" QTMAXITEM,");
		sql.append(" VLPCTDESCONTO,");
		sql.append(" VLUNITARIOKIT,");
		sql.append(" QTITEMKIT,");
		sql.append(" FLAGRUPADORSIMILARIDADE,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDKIT, ");
		sql.append(" CDTABELAPRECO, ");
		sql.append(" CDUNIDADE, ");
		sql.append(" FLBONIFICADO");
	}

	//@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		ItemKit itemKit = new ItemKit();
		itemKit.rowKey = rs.getString("rowkey");
		itemKit.cdProduto = rs.getString("cdProduto");
		itemKit.qtMinItem = ValueUtil.round(rs.getDouble("qtMinItem"));
		itemKit.qtMaxItem = ValueUtil.round(rs.getDouble("qtMaxItem"));
		itemKit.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
		itemKit.vlUnitarioKit = rs.getDouble("vlUnitarioKit");
		itemKit.qtItemKit = rs.getInt("qtItemKit");
		itemKit.flAgrupadorSimilaridade = rs.getString("flAgrupadorSimilaridade");
		itemKit.cdEmpresa = rs.getString("cdEmpresa");
		itemKit.cdRepresentante = rs.getString("cdRepresentante");
		itemKit.cdKit = rs.getString("cdKit");
		itemKit.cdTabelaPreco = rs.getString("cdTabelaPreco");
		itemKit.cdUnidade = rs.getString("cdUnidade");
		itemKit.flBonificado = rs.getString("flBonificado");
		return itemKit;
	}

	//@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ItemKit itemKit = (ItemKit) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (!itemKit.isBuscandoSimilar()) {
			sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemKit.cdProduto);
		}
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemKit.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemKit.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDKIT = ", itemKit.cdKit);
		sqlWhereClause.addAndCondition("itp.NUPEDIDO = ", itemKit.nuPedido);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", itemKit.cdTabelaPreco);
		sql.append(sqlWhereClause.getSql());
	}

	public Vector findItemKitJoinProduto(ItemKit itemKitFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDKIT,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.QTMINITEM,");
		sql.append(" tb.QTMAXITEM,");
		sql.append(" tb.VLPCTDESCONTO,");
		sql.append(" tb.VLUNITARIOKIT,");
		sql.append(" tb.QTITEMKIT,");
		sql.append(" tb.FLAGRUPADORSIMILARIDADE,");
		sql.append(" tb.CDUNIDADE,");
		sql.append(" prod.DSPRODUTO,");
		sql.append(" tb.FLBONIFICADO,");
		sql.append(" prod.NUCONVERSAOUNIDADESMEDIDA");
		if (LavenderePdaConfig.isApresentaPrecoTabela()) {
			sql.append(" , tabPreco.VLUNITARIO");
		}
		sql.append(" FROM ").append(tableName).append(" tb");
		sql.append(" JOIN ").append(Produto.TABLE_NAME).append(" prod ");
		sql.append(" ON tb.CDEMPRESA = prod.CDEMPRESA");
		sql.append(" AND tb.CDREPRESENTANTE = prod.CDREPRESENTANTE");
		sql.append(" AND tb.CDPRODUTO = prod.CDPRODUTO");
		sql.append(" JOIN ").append(Kit.TABLE_NAME).append(" kit");
		sql.append(" ON tb.CDEMPRESA = kit.CDEMPRESA");
		sql.append(" AND tb.CDREPRESENTANTE = kit.CDREPRESENTANTE");
		sql.append(" AND tb.CDKIT = kit.CDKIT");
		sql.append(" AND tb.CDTABELAPRECO = kit.CDTABELAPRECO");
		if (LavenderePdaConfig.isApresentaPrecoTabela()) {
			sql.append(" JOIN TBLVPITEMTABELAPRECO tabPreco ON ")
			.append(" tabPreco.CDEMPRESA = tb.CDEMPRESA AND ")
			.append(" tabPreco.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
			.append(" tabPreco.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
			.append(" tabPreco.CDPRODUTO = tb.CDPRODUTO");
		}
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemKitFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemKitFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDKIT = ", itemKitFilter.cdKit);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemKitFilter.cdProduto);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", itemKitFilter.cdTabelaPreco);
		sqlWhereClause.addAndCondition("tb.CDUNIDADE = ", itemKitFilter.cdUnidade);
		sql.append(sqlWhereClause.getSql());
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
				ItemKit itemKit = (ItemKit) populate(itemKitFilter, rs);
				if (LavenderePdaConfig.isApresentaPrecoTabela()) {
					itemKit.vlUnitario = rs.getDouble("vlUnitario");
				}
				itemKit.dsProduto = rs.getString("dsProduto");
				itemKit.nuConversaoUnidadesMedida = rs.getDouble("nuConversaoUnidadesMedida");
				result.addElement(itemKit);
			}
			return result;
		}
	}

	public Vector findCdProdutosByExample(BaseDomain domain) throws SQLException {
		ItemKit itemKitFilter = (ItemKit) domain;
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT CDPRODUTO FROM ");
		sql.append(tableName);
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(itemKitFilter.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(itemKitFilter.cdRepresentante));
		sql.append(" and CDKIT = ").append(Sql.getValue(itemKitFilter.cdKit));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector list = new Vector();
			while (rs.next()) {
				list.addElement(rs.getString(1));
			}
			return list;
		}
	}

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" CDPRODUTO,");
		sql.append(" CDKIT");
	}

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		ItemKit itemKit = new ItemKit();
		itemKit.cdProduto = rs.getString("cdProduto");
		itemKit.cdKit = rs.getString("cdKit");
		return itemKit;
	}

	protected void addSelectGridColumns(StringBuffer sql) {
		sql.append(" rowkey,");
		sql.append(" CDPRODUTO,");
		sql.append(" QTMINITEM ");
	}
	
	public Vector findAllKitByItemPedido(ItemKit itemKitFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
        addSelectColumnsByItemPedido(sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addJoinItemPedido(sql);
        addWhereByExample(itemKitFilter, sql);
		return findAllItemKit(sql.toString());
	}
	
	private void addJoinItemPedido(StringBuffer sql) {
		sql.append(" JOIN TBLVPITEMPEDIDO itp ON ")
		.append(" tb.CDEMPRESA = itp.CDEMPRESA AND")
		.append(" tb.CDREPRESENTANTE = itp.CDREPRESENTANTE AND")
		.append(" tb.CDKIT = itp.CDKIT AND tb.CDPRODUTO = itp.CDPRODUTO AND")
		.append(" (tb.CDUNIDADE = itp.CDUNIDADE OR tb.CDUNIDADE='0')");
		
	}

	private void addSelectColumnsByItemPedido(StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.QTMINITEM,");
		sql.append(" tb.QTMAXITEM,");
		sql.append(" itp.VLPCTDESCONTO,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDKIT,");
		sql.append(" tb.CDUNIDADE,");
		sql.append(" itp.QTITEMFISICO,");
		sql.append(" itp.QTITEMFATURAMENTO");
	}
	
	private BaseDomain populateItemKitJoinItemPedido(ResultSet rs) throws SQLException {
		ItemKit itemKit = new ItemKit();
		itemKit.rowKey = rs.getString("rowkey");
		itemKit.cdProduto = rs.getString("cdProduto");
		itemKit.qtMinItem = ValueUtil.round(rs.getDouble("qtMinItem"));
		itemKit.qtMaxItem = ValueUtil.round(rs.getDouble("qtMaxItem"));
		itemKit.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
		itemKit.cdEmpresa = rs.getString("cdEmpresa");
		itemKit.cdRepresentante = rs.getString("cdRepresentante");
		itemKit.cdKit = rs.getString("cdKit");
		itemKit.qtItemFisicoItemPedido = rs.getDouble("qtItemFisico");
		itemKit.qtItemFaturamentoItemPedido = rs.getDouble("qtItemFaturamento");
		itemKit.cdUnidade = rs.getString("cdUnidade");
		return itemKit;
	}
	
	protected Vector findAllItemKit(String sql) throws SQLException {
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql)) {
			Vector result = new Vector(50);
			while (rs.next()) {
				result.addElement(populateItemKitJoinItemPedido(rs));
			}
			return result;
		}
	}
	
	
	public ItemKit findItemKitSimilar(ItemKit filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ")
		.append(" tb.rowkey,")
		.append(" tb.CDPRODUTO,")
		.append(" tb.QTMINITEM,")
		.append(" tb.QTMAXITEM,")
		.append(" tb.VLPCTDESCONTO,")
		.append(" tb.VLUNITARIOKIT,")
		.append(" tb.QTITEMKIT,")
		.append(" tb.FLAGRUPADORSIMILARIDADE,")
		.append(" tb.CDEMPRESA,")
		.append(" tb.CDREPRESENTANTE,")
		.append(" tb.CDKIT, ")
		.append(" tb.CDTABELAPRECO, ")
		.append(" tb.CDUNIDADE, ")
		.append(" tb.FLBONIFICADO")
		.append(" FROM TBLVPITEMKIT tb ")
		.append(" JOIN TBLVPITEMKITAGRSIMILAR similar ON ")
		.append(" tb.CDEMPRESA = similar.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = similar.CDREPRESENTANTE AND ")
		.append(" tb.CDKIT = similar.CDKIT AND ")
		.append(" tb.CDTABELAPRECO = similar.CDTABELAPRECO AND ")
		.append(" tb.CDPRODUTO = similar.CDPRODUTO AND ")
		.append(" similar.CDPRODUTOSIMILAR = ").append(Sql.getValue(filter.cdProduto));
		addWhereByExample(filter, sql);
		sql.append(" AND FLAGRUPADORSIMILARIDADE = 'S'");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
	        if (rs.next()) {
	            return (ItemKit)populate(filter, rs);
	        } else {
	            return null;
	        }
        }
	}

	public String getProdutoInseridoByPedidoAndKit(Pedido pedido, Vector itemKitList, String cdKit) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT TB.CDPRODUTO FROM ");
		sql.append(ItemPedido.TABLE_NAME_ITEMPEDIDO);
		sql.append(" tb ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", pedido.cdEmpresa);
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", pedido.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.FLORIGEMPEDIDO = ", pedido.flOrigemPedido);
		sqlWhereClause.addAndCondition(" tb.NUPEDIDO = ", pedido.nuPedido);
		sqlWhereClause.addAndCondition(" tb.FLTIPOITEMPEDIDO = ", TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		sqlWhereClause.addAndCondition(" tb.NUSEQPRODUTO = ", ItemPedido.NUSEQPRODUTO_UNICO);
		sqlWhereClause.addAndCondition(" (tb.CDKIT IS NULL OR TB.CDKIT = '' OR tb.CDKIT <> ", cdKit);
		sqlWhereClause.addEndMultipleCondition();
		sqlWhereClause.addAndCondition(" (tb.CDPRODUTO = ", ((ItemKit)itemKitList.items[0]).cdProduto);
		int size = itemKitList.size();
		for (int i = 1; i < size; i++) {
			sqlWhereClause.addOrCondition(" tb.CDPRODUTO = ", ((ItemKit)itemKitList.items[i]).cdProduto);
		}
		sqlWhereClause.addEndMultipleCondition();
		sql.append(sqlWhereClause.getSql());
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return rs.getString(1);
			}
		}
		return null;
	}
	
	public boolean isItemKitComPreco(ItemKit itemKit, String cdTabelaPreco) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT count(*) ").
		append(" FROM ").append(ItemKit.TABLE_NAME).append(" TB ");
		addJoinItemTabelaPreco(sql, cdTabelaPreco);
		DaoUtil.addJoinProduto(sql, DaoUtil.ALIAS_PRODUTO, true);
		addWhereByExample(itemKit, sql);
		return getInt(sql.toString()) > 0;
	}
	
	private String addJoinItemTabelaPreco(StringBuffer sql, String cdTabelaPreco) {
		sql.append(" JOIN ").append(ItemTabelaPreco.TABLE_NAME).append(" TABPRECO ")
		.append(" ON TABPRECO.CDEMPRESA = TB.CDEMPRESA ")
		.append(" AND TABPRECO.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
		.append(" AND TABPRECO.CDPRODUTO = TB.CDPRODUTO ")
		.append(" AND (TABPRECO.CDUNIDADE = TB.CDUNIDADE OR TABPRECO.CDUNIDADE = ").append(ProdutoUnidade.CDUNIDADE_PADRAO).append(")")
		.append(" AND TABPRECO.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco));
		return sql.toString();
	}
}
