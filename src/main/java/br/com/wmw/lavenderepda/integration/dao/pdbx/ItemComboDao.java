package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.service.ItemComboAgrSimilarService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemComboDao extends CrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemCombo();
	}

	private static ItemComboDao instance;

	public ItemComboDao() {
		super(ItemCombo.TABLE_NAME);
	}
	
	public static ItemComboDao getInstance() {
		if (instance == null) {
			instance = new ItemComboDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ItemCombo itemCombo = new ItemCombo();
		itemCombo.rowKey = rs.getString("rowkey");
		itemCombo.cdProduto = rs.getString("cdProduto");
		itemCombo.cdEmpresa = rs.getString("cdEmpresa");
		itemCombo.cdRepresentante = rs.getString("cdRepresentante");
		itemCombo.cdCombo = rs.getString("cdCombo");
		itemCombo.cdTabelaPreco = rs.getString("cdTabelaPreco");
		itemCombo.flTipoItemCombo = rs.getString("flTipoItemCombo");
		itemCombo.vlUnitarioCombo = rs.getDouble("vlUnitarioCombo");
		itemCombo.qtItemCombo = rs.getInt("qtItemCombo");
		return itemCombo;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDCOMBO,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.FLTIPOITEMCOMBO,");
		sql.append(" tb.VLUNITARIOCOMBO,");
		sql.append(" tb.QTITEMCOMBO ");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ItemCombo itemCombo = (ItemCombo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		if (!itemCombo.buscandoSimilar) {
			sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", itemCombo.cdProduto);
		}
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", itemCombo.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", itemCombo.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCOMBO = ", itemCombo.cdCombo);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", itemCombo.cdTabelaPreco);
		sqlWhereClause.addAndCondition("tb.FLTIPOITEMCOMBO = ", itemCombo.flTipoItemCombo);
		sql.append(sqlWhereClause.getSql());
	}

	public int countItemComboVigente(ItemCombo filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPITEMCOMBO tb")
				.append(" JOIN TBLVPCOMBO combo ON")
				.append(" tb.CDEMPRESA = combo.CDEMPRESA AND")
				.append(" tb.CDREPRESENTANTE = combo.CDREPRESENTANTE AND")
				.append(" tb.CDCOMBO = combo.CDCOMBO AND")
				.append(" tb.CDTABELAPRECO = combo.CDTABELAPRECO");
		addWhereByExample(filter, sql);
		sql.append(" AND ((combo.DTVIGENCIAINICIAL <= date('now', 'localtime') OR (combo.DTVIGENCIAINICIAL IS NULL OR combo.DTVIGENCIAINICIAL = ''))")
				.append(" AND (combo.DTVIGENCIAFINAL >= date('now', 'localtime') OR (combo.DTVIGENCIAFINAL IS NULL or combo.DTVIGENCIAFINAL ='')))");
		return getInt(sql.toString());
	}

	public int countItemComboSimilarVigente(ItemCombo filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		filter.buscandoSimilar = true;
		sql.append("SELECT COUNT(1) FROM TBLVPPRODUTO prod")
		.append(" JOIN TBLVPITEMCOMBO tb ON")
		.append(" tb.CDEMPRESA = prod.CDEMPRESA AND")
		.append(" tb.CDREPRESENTANTE = prod.CDREPRESENTANTE AND")
		.append(" tb.CDPRODUTO = ITEMCOMBOAGRSIMILAR.CDPRODUTO")
		.append(" JOIN TBLVPCOMBO combo ON")
		.append(" tb.CDEMPRESA = combo.CDEMPRESA AND")
		.append(" tb.CDREPRESENTANTE = combo.CDREPRESENTANTE AND")
		.append(" tb.CDCOMBO = combo.CDCOMBO AND")
		.append(" tb.CDTABELAPRECO = combo.CDTABELAPRECO");
		DaoUtil.addJoinItemComboAgrSimilarBySimilar(sql, ItemComboAgrSimilarService.getInstance().getItemComboAgrSimilarFilterByItemCombo(filter));
		addWhereByExample(filter, sql);
		sql.append(" AND prod.CDPRODUTO = ").append(Sql.getValue(filter.cdProduto))
		.append(" AND prod.CDAGRUPADORSIMILARIDADE = ").append(Sql.getValue(filter.cdAgrupadorSimilaridade))
		.append(" AND ((combo.DTVIGENCIAINICIAL <= date('now', 'localtime') OR combo.DTVIGENCIAINICIAL IS NULL)")
		.append(" AND (combo.DTVIGENCIAFINAL >= date('now', 'localtime') OR combo.DTVIGENCIAFINAL IS NULL))");
		
		filter.buscandoSimilar = false;
		return getInt(sql.toString());
	}

	public Vector findItemComboVencidoList(ItemCombo filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDPRODUTO, tb.FLTIPOITEMCOMBO, tb.CDCOMBO, prod.DSPRODUTO, combo.DSCOMBO ")
		.append("FROM TBLVPITEMCOMBO tb ")
		.append("JOIN TBLVPCOMBO combo ON combo.CDEMPRESA = tb.CDEMPRESA AND ")
		.append("combo.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append("combo.CDCOMBO = tb.CDCOMBO AND ")
		.append("combo.CDTABELAPRECO = tb.CDTABELAPRECO ")
		.append("JOIN TBLVPPRODUTO prod ON prod.CDEMPRESA = tb.CDEMPRESA AND ")
		.append("prod.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append("prod.CDPRODUTO = tb.CDPRODUTO ")
		.append("JOIN TBLVPITEMPEDIDO item ON item.CDEMPRESA = tb.CDEMPRESA AND ")
		.append("item.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append("item.CDPRODUTO = tb.CDPRODUTO ")
		.append("WHERE tb.CDEMPRESA = ").append(Sql.getValue(filter.cdEmpresa)).append(" AND ")
		.append("tb.CDREPRESENTANTE = ").append(Sql.getValue(filter.cdRepresentante)).append(" AND ");
		if (ValueUtil.isNotEmpty(filter.cdTabelaPreco)) {
			sql.append("tb.CDTABELAPRECO = ").append(Sql.getValue(filter.cdTabelaPreco)).append(" AND ");
		}
		sql.append("item.NUPEDIDO = ").append(Sql.getValue(filter.nuPedido)).append(" AND ")
		.append("item.FLORIGEMPEDIDO = ").append(Sql.getValue(filter.flOrigemPedido)).append(" AND ")
		.append("(combo.DTVIGENCIAINICIAL > ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" OR ")
		.append("combo.DTVIGENCIAFINAL <").append(Sql.getValue(DateUtil.getCurrentDate())).append(")");
		Vector list = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populateItemComboVencido(rs));
			}
			return list;
		}
	}
	
	private ItemCombo populateItemComboVencido(ResultSet rs) throws SQLException {
		ItemCombo itemCombo = new ItemCombo();
		itemCombo.cdProduto = rs.getString("cdProduto");
		itemCombo.flTipoItemCombo = rs.getString("flTipoItemCombo");
		itemCombo.cdCombo = rs.getString("cdCombo");
		itemCombo.dsProduto = rs.getString("dsProduto");
		itemCombo.combo = new Combo();
		itemCombo.combo.cdCombo = itemCombo.cdCombo;
		itemCombo.combo.dsCombo = rs.getString("dsCombo");
		return itemCombo;
	}
	
	public ItemCombo findItemComboSimilar(ItemCombo filter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		addSelectColumns(filter, sql);
		sql.append(" FROM TBLVPITEMCOMBO tb ")
		.append(" JOIN TBLVPITEMCOMBOAGRSIMILAR similar ON ")
		.append(" tb.CDEMPRESA = similar.CDEMPRESA AND ")
		.append(" tb.CDREPRESENTANTE = similar.CDREPRESENTANTE AND ")
		.append(" tb.CDCOMBO = similar.CDCOMBO AND ")
		.append(" tb.CDTABELAPRECO = similar.CDTABELAPRECO AND ")
		.append(" tb.CDPRODUTO = similar.CDPRODUTO AND")
		.append(" similar.CDPRODUTOSIMILAR = ").append(Sql.getValue(filter.cdProduto));
		addWhereByExample(filter, sql);
		sql.append(" AND tb.FLAGRUPADORSIMILARIDADE = 'S'");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
	        if (rs.next()) {
	            return (ItemCombo)populate(filter, rs);
	        }
        }
		return null;
	}
	
}
