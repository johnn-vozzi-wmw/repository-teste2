package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoDestaque;
import totalcross.sql.ResultSet;

public class ProdutoDestaqueDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoDestaque();
	}
	private static ProdutoDestaqueDao instance;

	public ProdutoDestaqueDao() {
		super(ProdutoDestaque.TABLE_NAME);
	}

	public static ProdutoDestaqueDao getInstance() {
        if (instance == null) {
            instance = new ProdutoDestaqueDao();
        }
        return instance;
    }

	//@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPODESTAQUE,");
        sql.append(" DSGRUPODESTAQUE,");
        sql.append(" CDESQUEMACOR,");
        sql.append(" CDCOR,");
        sql.append(" IMICONE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLFILTROSELECIONADO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
	}

	// @Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ProdutoDestaque produtoDestaque = (ProdutoDestaque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoDestaque.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoDestaque.cdRepresentante);
		sqlWhereClause.addAndCondition("CDGRUPODESTAQUE = ", produtoDestaque.cdGrupoDestaque);
		sql.append(sqlWhereClause.getSql());
	}

	// @Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ProdutoDestaque produtoDestaque = new ProdutoDestaque();
		produtoDestaque.rowKey = rs.getString("rowkey");
		produtoDestaque.cdEmpresa = rs.getString("cdEmpresa");
		produtoDestaque.cdRepresentante = rs.getString("cdRepresentante");
		produtoDestaque.cdGrupoDestaque = rs.getString("cdGrupoDestaque");
		produtoDestaque.cdEsquemaCor = rs.getInt("cdEsquemaCor");
		produtoDestaque.cdCor = rs.getInt("cdCor");
		produtoDestaque.dsGrupoDestaque = rs.getString("dsGrupoDestaque");
		produtoDestaque.imIcone = rs.getBytes("imIcone");
		produtoDestaque.flFiltroSelecionado = rs.getString("flFiltroSelecionado");
		produtoDestaque.flTipoAlteracao = rs.getString("flTipoAlteracao");
		produtoDestaque.cdUsuario = rs.getString("cdUsuario");
		return produtoDestaque;
	}

	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	}

	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
	}
}


