package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ComboDao extends CrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Combo();
	}
	
	private static ComboDao instance;

	public ComboDao() {
		super(Combo.TABLE_NAME);
	}
	
	public static ComboDao getInstance() {
		if (instance == null) {
			instance = new ComboDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Combo combo = new Combo();
		combo.rowKey = rs.getString("rowkey");
		combo.cdEmpresa = rs.getString("cdEmpresa");
		combo.cdRepresentante = rs.getString("cdRepresentante");
		combo.cdCombo = rs.getString("cdCombo");
		combo.cdTabelaPreco = rs.getString("cdTabelaPreco");
		combo.dsCombo = rs.getString("dsCombo");
		combo.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
		combo.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
		return combo;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDCOMBO,");
		sql.append(" tb.CDTABELAPRECO,");
		sql.append(" tb.DSCOMBO,");
		sql.append(" tb.DTVIGENCIAINICIAL,");
		sql.append(" tb.DTVIGENCIAFINAL");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		Combo combo = (Combo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", combo.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", combo.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCOMBO = ", combo.cdCombo);
		sqlWhereClause.addAndCondition("tb.CDTABELAPRECO = ", combo.cdTabelaPreco);
		sqlWhereClause.addAndCondition("(tb.DTVIGENCIAINICIAL <= " + Sql.getValue(combo.dtVigenciaInicial) + " OR (tb.DTVIGENCIAINICIAL IS NULL OR tb.DTVIGENCIAINICIAL = ''))" );
		sqlWhereClause.addAndCondition("(tb.DTVIGENCIAFINAL >= " + Sql.getValue(combo.dtVigenciaFinal) + " OR (tb.DTVIGENCIAFINAL IS NULL OR tb.DTVIGENCIAFINAL = ''))");
		//--
		sql.append(sqlWhereClause.getSql());
	}
	
	public String getSqlComboItens(ItemCombo itemCombo, String nuPedido, String flOrigemPedido, boolean detalhada) {
		StringBuffer sb = getSqlBuffer();
		if (ValueUtil.isNotEmpty(itemCombo.cdProduto)) {
    		sb.append("SELECT bt.CDCOMBO");
    		if (detalhada) {
    			sb.append(",c.DSCOMBO, bt.CDEMPRESA, bt.CDREPRESENTANTE");
    		}
    		sb.append(" FROM TBLVPITEMCOMBO bt ");
    		if (detalhada) {
    			sb.append(" JOIN TBLVPCOMBO c ON")
    			.append(" c.CDEMPRESA = bt.CDEMPRESA AND")
    			.append(" c.CDREPRESENTANTE = bt.CDREPRESENTANTE AND")
    			.append(" c.CDCOMBO = bt.CDCOMBO");
    		}
    		sb.append(" WHERE bt.CDEMPRESA = ").append(Sql.getValue(itemCombo.cdEmpresa)).append(" AND ")
    		.append("bt.CDREPRESENTANTE = ").append(Sql.getValue(itemCombo.cdRepresentante)).append(" AND ")
    		.append("bt.CDPRODUTO = ").append(Sql.getValue(itemCombo.cdProduto));
			if (detalhada) {
				sb.append(" AND (c.DTVIGENCIAINICIAL <= date('now', 'localtime') OR (c.DTVIGENCIAINICIAL IS NULL OR c.DTVIGENCIAINICIAL = ''))")
						.append(" AND (c.DTVIGENCIAFINAL >= date('now', 'localtime') OR (c.DTVIGENCIAFINAL IS NULL or c.DTVIGENCIAFINAL =''))");
			}
    	} else {
    		sb.append("SELECT bt.CDCOMBO ");
    		if (detalhada) {
    			sb.append(", bt.DSCOMBO, bt.CDEMPRESA, bt.CDREPRESENTANTE ");
    		}
    		sb.append("FROM TBLVPCOMBO bt")
    		.append(" JOIN TBLVPITEMCOMBO itemc ON itemc.CDEMPRESA = bt.CDEMPRESA AND")
    		.append(" itemc.CDREPRESENTANTE = bt.CDREPRESENTANTE AND")
    		.append(" itemc.CDCOMBO = bt.CDCOMBO")
    		.append(" JOIN TBLVPITEMPEDIDO item ON item.CDEMPRESA = bt.CDEMPRESA AND")
    		.append(" item.CDREPRESENTANTE = bt.CDREPRESENTANTE AND")
    		.append(" item.NUPEDIDO = ").append(Sql.getValue(nuPedido)).append(" AND")
    		.append(" item.FLORIGEMPEDIDO = ").append(Sql.getValue(flOrigemPedido)).append(" AND")
    		.append(" item.CDPRODUTO = itemc.CDPRODUTO")
    		.append(" WHERE bt.CDEMPRESA = ").append(Sql.getValue(itemCombo.cdEmpresa)).append(" AND")
    		.append(" bt.CDREPRESENTANTE = ").append(Sql.getValue(itemCombo.cdRepresentante));
			sb.append(" AND (bt.DTVIGENCIAINICIAL <= date('now', 'localtime') OR (bt.DTVIGENCIAINICIAL IS NULL OR bt.DTVIGENCIAINICIAL = ''))")
					.append(" AND (bt.DTVIGENCIAFINAL >= date('now', 'localtime') OR (bt.DTVIGENCIAFINAL IS NULL or bt.DTVIGENCIAFINAL =''))");
    		sb.append(" GROUP BY bt.CDCOMBO");
    		if (detalhada) {
    			sb.append(", bt.DSCOMBO, bt.CDEMPRESA, bt.CDREPRESENTANTE");
    		}
    	}
		return sb.toString();
	}
	
	public Vector getListCombos(ItemCombo itemCombo, String nuPedido, String flOrigemPedido) throws SQLException {
		String sql = getSqlComboItens(itemCombo, nuPedido, flOrigemPedido, true);
		Vector list = new Vector(50);
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				Combo combo = new Combo();
				combo.cdEmpresa = rs.getString("cdEmpresa");
				combo.cdRepresentante = rs.getString("cdRepresentante");
				combo.cdCombo = rs.getString("cdCombo");
				combo.dsCombo = rs.getString("dsCombo");
				list.addElement(combo);
			}
		}
		return list;
	}
	
	public boolean isPedidoComComboForaVigencia(Pedido pedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(1) FROM TBLVPCOMBO tb ")
		.append("JOIN TBLVPITEMPEDIDO item ON item.CDEMPRESA = tb.CDEMPRESA AND ")
		.append("item.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append("item.CDCOMBO = tb.CDCOMBO ")
		.append("WHERE tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa)).append(" AND ")
		.append("tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante)).append(" AND ");
		String cdTabelaPreco = TabelaPrecoService.getInstance().getCdTabelaPreco(pedido);
		sql.append("tb.CDTABELAPRECO = ").append(Sql.getValue(cdTabelaPreco)).append(" AND ")
		.append("item.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(" AND ")
		.append("item.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido)).append(" AND ")
		.append("(tb.DTVIGENCIAINICIAL > ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" OR ")
		.append("tb.DTVIGENCIAFINAL < ").append(Sql.getValue(DateUtil.getCurrentDate())).append(")");
		return getInt(sql.toString()) > 0;
	}
	
	public Vector findComboVigenteComEstoque(Combo filter, String cdCliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select ");
		sql.append(" tb.CDCOMBO,");
		sql.append(" tb.DSCOMBO");
		sql.append(" from ")
        .append(tableName)
        .append(" tb ");
		addJoinComboCliente(sql, cdCliente);
		addWhereByExample(filter, sql);
		sql.append(getSqlExisteEmTabelaPreco(filter));
		//-- Todos os itens primários com estoque
		sql.append(" AND NOT EXISTS (")
		   .append(getInnerSqlComboVigenteEstoque(ItemCombo.TIPOITEMCOMBO_PRIMARIO, cdCliente, filter.nuPedido))
           .append(" AND (IFNULL(estErp.QTESTOQUE, 0 ) - IFNULL(estPda.QTESTOQUE, 0 ) <= 0 ");
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			sql.append( "AND IFNULL(item.FLAGRUPADORSIMILARIDADE, 'N') = 'N'");
		}
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" OR prod.CDPRODUTO IS NOT NULL");
		}
		sql.append("))");
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			sql.append(" AND NOT EXISTS (")
			.append(getSqlRestricaoVendaClienteProduto(cdCliente, filter.nuPedido))
			.append(")");
		}
		//-- Pelo menos 1 item secundário com estoque
		sql.append(" AND EXISTS (");
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			sql.append(getSqlEstoqueSimilarSecundario(cdCliente, filter.nuPedido)).append(")");
		} else {
			sql.append(getInnerSqlComboVigenteEstoque(ItemCombo.TIPOITEMCOMBO_SECUNDARIO, cdCliente, filter.nuPedido))
		   .append(" AND IFNULL(estErp.QTESTOQUE, 0 ) - IFNULL(estPda.QTESTOQUE, 0 ) > 0 ");
			if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
				sql.append(" AND prod.CDPRODUTO IS NULL");
			}
			sql.append(")");
		}
		sql.append(" AND (COMBOCLIENTE.CDCLIENTE IS NOT NULL OR")
		.append(" NOT EXISTS (SELECT 1")
		.append(" FROM TBLVPCOMBOCLIENTE WHERE CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
		.append(" CDCOMBO = tb.CDCOMBO AND ")
		.append(" CDTABELAPRECO = tb.CDTABELAPRECO AND ")
		.append(" CDCLIENTE <> ").append(Sql.getValue(cdCliente)).append(")")
		.append(")");
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			Combo combo;
			while(rs.next()) {
				combo = new Combo();
				combo.cdCombo = rs.getString(1);
				combo.dsCombo = rs.getString(2);
				result.addElement(combo);
			}
			return result;
		}
	}
	
	private void addJoinComboCliente(StringBuffer sql, String cdCliente) throws SQLException {
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			sql.append(" LEFT JOIN TBLVPCOMBOCLIENTE COMBOCLIENTE ON ")
			.append(" tb.CDEMPRESA =  COMBOCLIENTE.CDEMPRESA AND ")
			.append(" tb.CDREPRESENTANTE = COMBOCLIENTE.CDREPRESENTANTE AND ")
			.append(" tb.CDCOMBO = COMBOCLIENTE.CDCOMBO AND ")
			.append(" tb.CDTABELAPRECO = COMBOCLIENTE.CDTABELAPRECO AND ")
			.append(" COMBOCLIENTE.CDCLIENTE = ").append(Sql.getValue(cdCliente));
		}
	}

	private String getSqlExisteEmTabelaPreco(Combo combo) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" AND NOT EXISTS (");
		sql.append(selectItemComboJoinItemTabelaPreco(true));
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		addWhereFromJoinTabPreco(combo, sqlWhereClause, ItemCombo.TIPOITEMCOMBO_PRIMARIO);
		sqlWhereClause.addAndCondition("tabPreco.CDEMPRESA IS NULL");
		sql.append(sqlWhereClause.getSql());
		sql.append(")");
		sql.append(" AND EXISTS (");
		sql.append(selectItemComboJoinItemTabelaPreco(false));
		sqlWhereClause = new SqlWhereClause();
		addWhereFromJoinTabPreco(combo, sqlWhereClause, ItemCombo.TIPOITEMCOMBO_SECUNDARIO);
		sql.append(sqlWhereClause.getSql());
		sql.append(")");
		return sql.toString();
	}

	private void addWhereFromJoinTabPreco(Combo combo, SqlWhereClause sqlWhereClause, String tipoItemCombo) {
		sqlWhereClause.addAndCondition("item.CDEMPRESA = ", combo.cdEmpresa);
		sqlWhereClause.addAndCondition("item.CDREPRESENTANTE = ", combo.cdRepresentante);
		sqlWhereClause.addAndCondition("item.CDTABELAPRECO = ", combo.cdTabelaPreco);
		sqlWhereClause.addAndCondition("item.FLTIPOITEMCOMBO = ", tipoItemCombo);
		sqlWhereClause.addAndCondition("item.CDCOMBO = tb.CDCOMBO");
	}

	private String selectItemComboJoinItemTabelaPreco(boolean leftJoin) {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT 1 FROM ").append(ItemCombo.TABLE_NAME).append(" item ");
		if (leftJoin) {
			sql.append("LEFT");
		}
		sql.append(" JOIN ").append(ItemTabelaPreco.TABLE_NAME).append(" tabPreco ");
		sql.append("ON tabPreco.CDEMPRESA = item.CDEMPRESA ");
		sql.append("AND tabPreco.CDREPRESENTANTE = item.CDREPRESENTANTE ");
		sql.append("AND tabPreco.CDPRODUTO = item.CDPRODUTO ");
		sql.append("AND tabPreco.CDTABELAPRECO = tb.CDTABELAPRECO ");
		return sql.toString();
	}

	private String getInnerSqlComboVigenteEstoque(String flTipoItemCombo, String cdCliente, String nuPedido) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 ")
		.append("FROM TBLVPITEMCOMBO item ");
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
        .append("estErp.CDLOCALESTOQUE = '0' AND ")
        .append("estErp.FLORIGEMESTOQUE = 'E' ")
        .append("LEFT JOIN TBLVPESTOQUE estPda ON estPda.CDEMPRESA = item.CDEMPRESA AND ")
        .append("estPda.CDREPRESENTANTE = item.CDREPRESENTANTE AND ")
        .append("estPda.CDPRODUTO = item.CDPRODUTO AND ")
        .append("estPda.CDITEMGRADE1 = '0' AND ")
        .append("estPda.CDITEMGRADE2 = '0' AND ")
        .append("estPda.CDITEMGRADE3 = '0' AND ")
        .append("estPda.CDLOCALESTOQUE = '0' AND ")
        .append("estPda.FLORIGEMESTOQUE = 'P' ")
		.append(" WHERE item.CDEMPRESA = tb.CDEMPRESA AND ")
        .append("item.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append("item.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
        .append("item.CDCOMBO = tb.CDCOMBO AND ")
        .append("item.FLTIPOITEMCOMBO = ").append(Sql.getValue(flTipoItemCombo));
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && ItemCombo.TIPOITEMCOMBO_SECUNDARIO.equals(flTipoItemCombo)) {
			DaoUtil.addNotExistsRestricaoProduto(sql, "item.cdProduto", cdCliente, nuPedido, 1, "item.qtItemCombo", true, false, true, false);
		}
        return sql.toString();
	}
	
	private String getSqlRestricaoVendaClienteProduto(String cdCliente, String nuPedido) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT 1 ")
		.append("FROM TBLVPITEMCOMBO item ")
		.append(" WHERE item.CDEMPRESA = tb.CDEMPRESA AND ")
        .append("item.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append("item.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
        .append("item.CDCOMBO = tb.CDCOMBO AND ")
        .append("item.FLTIPOITEMCOMBO = ").append(Sql.getValue(ItemCombo.TIPOITEMCOMBO_PRIMARIO));
		DaoUtil.addNotExistsRestricaoProduto(sql, "item.cdProduto", cdCliente, nuPedido, 1, "item.qtItemCombo", true, false, true, true);
		return sql.toString();
	}
	
	private String getSqlEstoqueSimilarSecundario(String cdCliente, String nuPedido) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT 1 FROM (")
		.append(" SELECT estErp.QTESTOQUE QTE, estPda.QTESTOQUE QTA FROM TBLVPPRODUTO p JOIN (")
		.append(" SELECT p2.CDEMPRESA, p2.CDREPRESENTANTE, p2.CDPRODUTO, CASE WHEN i.FLAGRUPADORSIMILARIDADE = 'S' THEN p2.CDAGRUPADORSIMILARIDADE ELSE NULL END as CDAGRUPADORSIMILARIDADE")
		.append(" FROM TBLVPPRODUTO p2 JOIN ")
		.append(" TBLVPITEMCOMBO i ON p2.CDEMPRESA = i.CDEMPRESA AND p2.CDREPRESENTANTE = i.CDREPRESENTANTE  ")
		.append(" AND i.FLTIPOITEMCOMBO = ").append(Sql.getValue(ItemCombo.TIPOITEMCOMBO_SECUNDARIO))
		.append(" AND p2.CDPRODUTO = i.CDPRODUTO");
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" LEFT JOIN TBLVPPRODUTOBLOQUEADO prod ON ")
			.append("prod.CDEMPRESA =  i.CDEMPRESA AND ")
			.append("prod.CDREPRESENTANTE = i.CDREPRESENTANTE AND ")
			.append("prod.CDPRODUTO = i.CDPRODUTO AND ")
			.append("(prod.CDTABELAPRECO = i.CDTABELAPRECO OR prod.CDTABELAPRECO = '0') ");
		}
		sql.append(" WHERE i.CDEMPRESA = tb.CDEMPRESA AND ")
        .append("i.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append("i.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
        .append("i.CDCOMBO = tb.CDCOMBO ");
		if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido() && LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda) {
			sql.append(" AND prod.CDPRODUTO IS NULL");
		}
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
			DaoUtil.addNotExistsRestricaoProduto(sql, "i.cdProduto", cdCliente, nuPedido, 1, "i.qtItemCombo", true, false, true, false);
		}
		sql.append(") a ON a.CDEMPRESA = p.CDEMPRESA AND a.CDREPRESENTANTE = p.CDREPRESENTANTE AND (a.CDPRODUTO = p.CDPRODUTO OR a.CDAGRUPADORSIMILARIDADE = p.CDAGRUPADORSIMILARIDADE)")
		.append("LEFT JOIN TBLVPESTOQUE estErp ON estErp.CDEMPRESA = p.CDEMPRESA AND ")
        .append("estErp.CDREPRESENTANTE = p.CDREPRESENTANTE AND ")
        .append("estErp.CDPRODUTO = p.CDPRODUTO AND ")
        .append("estErp.CDITEMGRADE1 = '0' AND ")
        .append("estErp.CDITEMGRADE2 = '0' AND ")
        .append("estErp.CDITEMGRADE3 = '0' AND ")
        .append("estErp.CDLOCALESTOQUE = '0' AND ")
        .append("estErp.FLORIGEMESTOQUE = 'E' ")
        .append("LEFT JOIN TBLVPESTOQUE estPda ON estPda.CDEMPRESA = p.CDEMPRESA AND ")
        .append("estPda.CDREPRESENTANTE = p.CDREPRESENTANTE AND ")
        .append("estPda.CDPRODUTO = p.CDPRODUTO AND ")
        .append("estPda.CDITEMGRADE1 = '0' AND ")
        .append("estPda.CDITEMGRADE2 = '0' AND ")
        .append("estPda.CDITEMGRADE3 = '0' AND ")
        .append("estPda.CDLOCALESTOQUE = '0' AND ")
        .append("estPda.FLORIGEMESTOQUE = 'P' ) tab")
        .append(" WHERE IFNULL(QTE, 0 ) - IFNULL(QTA, 0 ) > 0 ");
		return sql.toString();
	}

	public boolean containsGruposSemEstoque(Combo combo) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT tb.CDAGRUPADORSIMILARIDADE, IFNULL(estErp.QTESTOQUE, 0 ) - IFNULL(estPda.QTESTOQUE, 0 ) QTESTOQUE FROM TBLVPPRODUTO tb JOIN (")
		.append("SELECT p.CDEMPRESA, p.CDREPRESENTANTE, p.CDAGRUPADORSIMILARIDADE FROM TBLVPPRODUTO p JOIN ")
		.append("TBLVPITEMCOMBO i ON p.CDEMPRESA = i.CDEMPRESA AND ")
		.append(" i.CDREPRESENTANTE = p.CDREPRESENTANTE AND ")
		.append(" i.CDPRODUTO = p.CDPRODUTO AND")
		.append(" i.FLTIPOITEMCOMBO = 'P' AND ")
		.append(" i.CDCOMBO = ").append(Sql.getValue(combo.cdCombo)).append(" AND ")
		.append(" i.CDTABELAPRECO = ").append(Sql.getValue(combo.cdTabelaPreco)).append(" AND ")
		.append(" i.FLAGRUPADORSIMILARIDADE = 'S'")
		.append(") a ON a.CDEMPRESA = tb.CDEMPRESA AND a.CDREPRESENTANTE = tb.CDREPRESENTANTE AND a.CDAGRUPADORSIMILARIDADE = tb.CDAGRUPADORSIMILARIDADE ")
		.append("LEFT JOIN TBLVPESTOQUE estErp ON estErp.CDEMPRESA = tb.CDEMPRESA AND ")
        .append("estErp.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append("estErp.CDPRODUTO = tb.CDPRODUTO AND ")
        .append("estErp.CDITEMGRADE1 = '0' AND ")
        .append("estErp.CDITEMGRADE2 = '0' AND ")
        .append("estErp.CDITEMGRADE3 = '0' AND ")
        .append("estErp.CDLOCALESTOQUE = '0' AND ")
        .append("estErp.FLORIGEMESTOQUE = 'E' ")
        .append("LEFT JOIN TBLVPESTOQUE estPda ON estPda.CDEMPRESA = tb.CDEMPRESA AND ")
        .append("estPda.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append("estPda.CDPRODUTO = tb.CDPRODUTO AND ")
        .append("estPda.CDITEMGRADE1 = '0' AND ")
        .append("estPda.CDITEMGRADE2 = '0' AND ")
        .append("estPda.CDITEMGRADE3 = '0' AND ")
        .append("estPda.CDLOCALESTOQUE = '0' AND ")
        .append("estPda.FLORIGEMESTOQUE = 'P' ")
        .append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(combo.cdEmpresa)).append(" AND ")
        .append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(combo.cdRepresentante));
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			HashMap<String, Double> mapEstoqueGrupo = new HashMap<>();
			while (rs.next()) {
				Double qtEstoque = mapEstoqueGrupo.get(rs.getString(1));
				qtEstoque = qtEstoque == null ? 0 : qtEstoque;
				mapEstoqueGrupo.put(rs.getString(1), qtEstoque + rs.getDouble(2));
			}
			for (Double count : mapEstoqueGrupo.values()) {
				if (count <= 0d) {
					return true;
				}
			}
			return false;
		}
	}

}
