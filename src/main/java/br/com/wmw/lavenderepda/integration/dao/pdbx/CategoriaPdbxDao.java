package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import totalcross.sql.ResultSet;

public class CategoriaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Categoria();
	}

    private static CategoriaPdbxDao instance;

    public CategoriaPdbxDao() {
        super(Categoria.TABLE_NAME);
    }

    public static CategoriaPdbxDao getInstance() {
        if (instance == null) {
            instance = new CategoriaPdbxDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { /**/ }
	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { /**/ }
	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }
	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,")
        .append(" cdEmpresa,")
        .append(" cdCategoria,")
        .append(" dsCategoria,")
        .append(" vlMinPedido,")
        .append(" flDivideVlMin,")
        .append(" flTipoAlteracao,")
        .append(" flTipoCategoria,")
        .append(" vlPctDescEspecial,")
        .append(" vlPctDescAtacado,")
        .append(" vlMinPedidoEspecial,")
        .append(" vlMinPedidoAtacado,")
        .append(" qtMinVendidoBonifTroca,")
        .append(" cdCategoriaLeads");
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.rowKey = rs.getString("rowkey");
        categoria.cdEmpresa = rs.getString("cdEmpresa");
        categoria.cdCategoria = rs.getString("cdCategoria");
        categoria.dsCategoria = rs.getString("dsCategoria");
        categoria.vlMinPedido = rs.getDouble("vlMinPedido");
        categoria.flDivideVlMin = rs.getString("flDivideVlMin");
        categoria.flTipoAlteracao = rs.getString("flTipoAlteracao");
        categoria.flTipoCategoria = rs.getString("flTipoCategoria");
        categoria.vlPctDescEspecial = rs.getDouble("vlPctDescEspecial");
        categoria.vlPctDescAtacado = rs.getDouble("vlPctDescAtacado");
        categoria.vlMinPedidoEspecial = rs.getDouble("vlMinPedidoEspecial");
        categoria.vlMinPedidoAtacado = rs.getDouble("vlMinPedidoAtacado");
        categoria.qtMinVendidoBonifTroca = rs.getDouble("qtMinVendidoBonifTroca");
        categoria.cdCategoriaLeads = rs.getString("cdCategoriaLeads");
        return categoria;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        Categoria categoria = (Categoria) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdEmpresa = ", categoria.cdEmpresa);
		sqlWhereClause.addAndCondition("cdCategoria = ", categoria.cdCategoria);
		if (categoria.isBuscaCategoriaLeadas()) {
			sqlWhereClause.addAndCondition("cdCategoriaLeads is not null and cdCategoriaLeads <> '' ");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }

}