package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CatalogoExterno;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

import java.sql.SQLException;

public class CatalogoExternoDbxDao extends LavendereCrudDbxDao {

	private static CatalogoExternoDbxDao instance;

	public CatalogoExternoDbxDao(String tableName) {
		super(tableName);
	}

	public static CatalogoExternoDbxDao getInstance() {
		if (instance == null) {
			instance = new CatalogoExternoDbxDao(CatalogoExterno.TABLE_NAME);
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		CatalogoExterno catalogoExterno = new CatalogoExterno();
		catalogoExterno.rowKey = rs.getString("rowkey");
		catalogoExterno.cdCatalogoExterno = rs.getString("cdCatalogoExterno");
		catalogoExterno.cdEmpresa = rs.getString("cdEmpresa");
		catalogoExterno.nmArquivo = rs.getString("nmArquivo");
		catalogoExterno.dsCatalogoExterno = rs.getString("dsCatalogoExterno");
		catalogoExterno.nuTamanho = rs.getInt("nuTamanho");
		catalogoExterno.nmExtensao = rs.getString("nmExtensao");
		catalogoExterno.dtAlteracao = rs.getDate("dtAlteracao");
		catalogoExterno.hrAlteracao = rs.getString("hrAlteracao");
		catalogoExterno.nuCarimbo = rs.getInt("nuCarimbo");
		return catalogoExterno;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDCATALOGOEXTERNO,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.NMARQUIVO,");
		sql.append(" tb.DSCATALOGOEXTERNO,");
		sql.append(" tb.NUTAMANHO,");
		sql.append(" tb.NMEXTENSAO,");
		sql.append(" tb.DTALTERACAO,");
		sql.append(" tb.HRALTERACAO,");
		sql.append(" tb.NUCARIMBO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDCATALOGOEXTERNO,");
		sql.append(" CDEMPRESA,");
		sql.append(" DSCATALOGOEXTERNO,");
		sql.append(" NMARQUIVO,");
		sql.append(" NUTAMANHO,");
		sql.append(" NMEXTENSAO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO");
		sql.append(" NUCARIMBO");
		sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		CatalogoExterno catalogoExterno = (CatalogoExterno) domain;
		sql.append(Sql.getValue(catalogoExterno.cdCatalogoExterno)).append(",");
		sql.append(Sql.getValue(catalogoExterno.cdEmpresa)).append(",");
		sql.append(Sql.getValue(catalogoExterno.dsCatalogoExterno)).append(",");
		sql.append(Sql.getValue(catalogoExterno.nmArquivo)).append(",");
		sql.append(Sql.getValue(catalogoExterno.nuTamanho)).append(",");
		sql.append(Sql.getValue(catalogoExterno.nmExtensao)).append(",");
		sql.append(Sql.getValue(catalogoExterno.dtAlteracao)).append(",");
		sql.append(Sql.getValue(catalogoExterno.hrAlteracao)).append(",");
		sql.append(Sql.getValue(catalogoExterno.nuCarimbo)).append(",");
		sql.append(Sql.getValue(catalogoExterno.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		CatalogoExterno catalogoExterno = (CatalogoExterno) domain;
		sql.append(" DSCATALOGOEXTERNO = ").append(Sql.getValue(catalogoExterno.dsCatalogoExterno)).append(",");
		sql.append(" NMARQUIVO = ").append(Sql.getValue(catalogoExterno.nmArquivo)).append(",");
		sql.append(" NUTAMANHO = ").append(Sql.getValue(catalogoExterno.nuTamanho)).append(",");
		sql.append(" DTALTERACAO = ").append(Sql.getValue(catalogoExterno.dtAlteracao)).append(",");
		sql.append(" HRALTERACAO = ").append(Sql.getValue(catalogoExterno.hrAlteracao)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(catalogoExterno.flTipoAlteracao)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(catalogoExterno.nuCarimbo));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		CatalogoExterno catalogoExterno = (CatalogoExterno) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", catalogoExterno.cdEmpresa);
		if (catalogoExterno.filtraFlTipoAlteracaoNotNull) {
			sqlWhereClause.addAndCondition("(FLTIPOALTERACAO <> '' AND FLTIPOALTERACAO IS NOT NULL)");
		}
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CatalogoExterno();
	}

	public Vector findAllNaoAlterados(CatalogoExterno catalogoExterno) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(catalogoExterno, sql);
		sql.append(" from ");
		sql.append(tableName).append(" tb");
		sql.append(" where flTipoAlteracao = '' or flTipoAlteracao is null");
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
				result.addElement(populate(null, rs));
			}
			return result;
		}
	}

}
