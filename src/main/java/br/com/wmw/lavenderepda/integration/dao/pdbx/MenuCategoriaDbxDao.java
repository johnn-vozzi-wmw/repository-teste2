package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MenuCategoria;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class MenuCategoriaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MenuCategoria();
	}

	private static MenuCategoriaDbxDao instance;

	public MenuCategoriaDbxDao() {
		super(MenuCategoria.TABLE_NAME);
	}

	public static MenuCategoriaDbxDao getInstance() {
		if (instance == null) {
			instance = new MenuCategoriaDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet resultSet) throws SQLException {
		MenuCategoria menuCategoria = new MenuCategoria();
		menuCategoria.rowKey = resultSet.getString("rowkey");
		menuCategoria.cdEmpresa = resultSet.getString("cdEmpresa");
		menuCategoria.cdRepresentante = resultSet.getString("cdRepresentante");
		menuCategoria.cdMenu = resultSet.getString("cdMenu");
		menuCategoria.cdMenuPai = resultSet.getString("cdMenuPai");
		menuCategoria.dsMenu = resultSet.getString("dsMenu");
		menuCategoria.imFoto = resultSet.getBytes("imFoto");
		menuCategoria.nmFoto = resultSet.getString("nmFoto");
		menuCategoria.flMostraProdutos = resultSet.getString("flMostraProdutos");
		menuCategoria.flMostraProdutosFilhos = resultSet.getString("flMostraProdutosFilhos");
		menuCategoria.flPrincipal = resultSet.getString("flPrincipal");
		menuCategoria.cdUsuario = resultSet.getString("cdUsuario");
		menuCategoria.dtAlteracao = resultSet.getString("dtAlteracao");
		menuCategoria.hrAlteracao = resultSet.getString("hrAlteracao");
		return menuCategoria;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		getColumns(sql);
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDMENU,");
		sql.append(" CDMENUPAI,");
		sql.append(" DSMENU,");
		sql.append(" IMFOTO,");
		sql.append(" NMFOTO,");
		sql.append(" FLMOSTRAPRODUTOS,");
		sql.append(" FLMOSTRAPRODUTOSFILHOS,");
		sql.append(" FLPRINCIPAL, ");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" HRALTERACAO,");
		sql.append(" DTALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		MenuCategoria menuCategoria = (MenuCategoria) baseDomain;
		sql.append(Sql.getValue(menuCategoria.cdEmpresa)).append(",");
		sql.append(Sql.getValue(menuCategoria.cdRepresentante)).append(",");
		sql.append(Sql.getValue(menuCategoria.cdMenu)).append(",");
		sql.append(Sql.getValue(menuCategoria.cdMenuPai)).append(",");
		sql.append(Sql.getValue(menuCategoria.dsMenu)).append(",");
		sql.append(Sql.getValue(menuCategoria.imFoto)).append(",");
		sql.append(Sql.getValue(menuCategoria.nmFoto)).append(",");
		sql.append(Sql.getValue(menuCategoria.flMostraProdutos)).append(",");
		sql.append(Sql.getValue(menuCategoria.flMostraProdutosFilhos)).append(",");
		sql.append(Sql.getValue(menuCategoria.flPrincipal)).append(",");
		sql.append(Sql.getValue(menuCategoria.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(menuCategoria.cdUsuario)).append(",");
		sql.append(Sql.getValue(menuCategoria.nuCarimbo)).append(",");
		sql.append(Sql.getValue(menuCategoria.dtAlteracao)).append(",");
		sql.append(Sql.getValue(menuCategoria.hrAlteracao));
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		MenuCategoria menuCategoria = (MenuCategoria) baseDomain;
		sql.append(" IMFOTO = ").append(Sql.getValue(menuCategoria.imFoto)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(menuCategoria.nuCarimbo)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(menuCategoria.flTipoAlteracao)).append(",");
		sql.append(" HRALTERACAO = ").append(Sql.getValue(menuCategoria.hrAlteracao)).append(",");
		sql.append(" DTALTERACAO = ").append(Sql.getValue(menuCategoria.dtAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		MenuCategoria menuCategoria = (MenuCategoria) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", menuCategoria.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", menuCategoria.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDMENU = ", menuCategoria.cdMenu);
		if (ValueUtil.isEmpty(menuCategoria.cdMenuPai)) {
			sqlWhereClause.addAndCondition("(tb.CDMENUPAI is null or tb.CDMENUPAI = '')");
		} else {
			sqlWhereClause.addAndCondition("tb.CDMENUPAI = ", menuCategoria.cdMenuPai);
		}
		sqlWhereClause.addAndCondition("tb.FLPRINCIPAL != 'S'");
		sql.append(sqlWhereClause.getSql());
	}

	public MenuCategoria findMenuPrincipal(String cdEmpresa, String cdRepresentante) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sql.append("SELECT ");
		addSelectColumns(domainFilter, sql);
		sql.append(" FROM ");
		sql.append(MenuCategoria.TABLE_NAME);
		sqlWhereClause.addAndCondition("FLPRINCIPAL = 'S'");
		sqlWhereClause.addAndCondition("CDEMPRESA = ", cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cdRepresentante);
		sql.append(sqlWhereClause.getSql());
		sql.append(" LIMIT 1");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (MenuCategoria) populate(domainFilter, rs);
			}
			return null;
		}
	}
}
