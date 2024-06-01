package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCod;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class ProdutoClienteCodDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoClienteCod();
	}
	
	private static ProdutoClienteCodDbxDao instance;
	
	public static ProdutoClienteCodDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoClienteCodDbxDao();
        }
        return instance;
	}

	public ProdutoClienteCodDbxDao() {
		super(ProdutoClienteCod.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ProdutoClienteCod produtoClienteCod = new ProdutoClienteCod();
		produtoClienteCod.cdEmpresa = rs.getString("cdEmpresa");
		produtoClienteCod.cdRepresentante = rs.getString("cdRepresentante");
		produtoClienteCod.cdProduto = rs.getString("cdProduto");
		produtoClienteCod.cdCliente = rs.getString("cdCliente");
		produtoClienteCod.cdProdutoCliente = rs.getString("cdProdutoCliente");
		produtoClienteCod.dsProduto = rs.getString("dsProduto");
		produtoClienteCod.dsPrincipioAtivo = rs.getString("dsPrincipioAtivo");
		produtoClienteCod.dsRazaoSocialCliente = rs.getString("nmRazaoSocial");
		return produtoClienteCod;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDPRODUTOCLIENTE,");
        sql.append(" prod.DSPRODUTO,");
        sql.append(" prod.DSPRINCIPIOATIVO,");
        sql.append(" cli.NMRAZAOSOCIAL");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTOCLIENTE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
		sql.append(Sql.getValue(produtoClienteCod.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoClienteCod.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoClienteCod.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoClienteCod.cdCliente)).append(",");
        sql.append(Sql.getValue(produtoClienteCod.cdProdutoCliente)).append(",");
        sql.append(Sql.getValue(produtoClienteCod.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoClienteCod.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoClienteCod.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
		sql.append(" CDPRODUTOCLIENTE = ").append(Sql.getValue(produtoClienteCod.cdProdutoCliente));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", produtoClienteCod.cdEmpresa);
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", produtoClienteCod.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.CDPRODUTO = ", produtoClienteCod.cdProduto);
		sqlWhereClause.addAndCondition(" tb.CDCLIENTE = ", produtoClienteCod.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
    		sql.append(" order by ");
    		String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
    		String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
    		for (int i = 0; i < sortAtributtes.length; i++) {
    			sql.append(sortAtributtes[i]).append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
    			if (!(i == (sortAtributtes.length - 1))) {
    				sql.append(" , ");
    			}
			}
    	}
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPPRODUTO prod ON tb.CDEMPRESA = prod.CDEMPRESA");
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			sql.append(" AND prod.CDREPRESENTANTE =").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante));
		} else {
			sql.append(" AND prod.CDREPRESENTANTE = tb.CDREPRESENTANTE");	
		}
		sql.append(" AND tb.CDPRODUTO = prod.CDPRODUTO")
		.append(" JOIN TBLVPCLIENTE cli ON tb.CDEMPRESA = cli.CDEMPRESA")
		.append(" AND tb.CDREPRESENTANTE = cli.CDREPRESENTANTE")
		.append(" AND tb.CDCLIENTE = cli.CDCLIENTE");
	}

	public Vector findAllProdutoClienteCodFromFechamentoPedido(BaseDomain domain) throws SQLException {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumnsFromFechamentoPedido(sql, produtoClienteCod.pedidoFechado);
        if (produtoClienteCod.realizaBuscaItemPedidoErp) {
        	sql.append(" from TBLVPITEMPEDIDOERP itp");
        } else  {
        	sql.append(" from TBLVPITEMPEDIDO itp");
        }
        addJoinFromFechamentoPedido(sql, produtoClienteCod);
        addWhereByExampleFromFechamentoPedido(domain, sql);
        addOrderBy(sql, domain);
        return findAll(null, sql.toString());
	}


	private void addSelectColumnsFromFechamentoPedido(StringBuffer sql, boolean pedidoFechado) {
		sql.append(" itp.CDEMPRESA,")
		.append(" itp.CDREPRESENTANTE,")
		.append(" itp.CDPRODUTO,")
		.append(" ped.CDCLIENTE,");
		if (pedidoFechado) {
			sql.append(" itp.CDPRODUTOCLIENTE,");
		} else {
			sql.append(" tb.CDPRODUTOCLIENTE,");
		}
		sql.append(" prod.DSPRODUTO,")
		.append(" prod.DSPRINCIPIOATIVO,")
		.append(" cli.NMRAZAOSOCIAL");
	}
	
	private void addJoinFromFechamentoPedido(StringBuffer sql, ProdutoClienteCod produtoClienteCod) {
		sql.append(" LEFT JOIN TBLVPPRODUTOCLIENTECOD tb ON")
		.append(" tb.CDEMPRESA = itp.CDEMPRESA")
		.append(" AND tb.CDREPRESENTANTE = itp.CDREPRESENTANTE")
		.append(" AND tb.CDPRODUTO = itp.CDPRODUTO")
		.append(" AND tb.CDCLIENTE =").append(Sql.getValue(produtoClienteCod.cdCliente));
		if (produtoClienteCod.realizaBuscaItemPedidoErp) {
			sql.append(" JOIN TBLVPPEDIDOERP ped ON");
		} else {
			sql.append(" JOIN TBLVPPEDIDO ped ON");
		}
		sql.append(" ped.CDEMPRESA = itp.CDEMPRESA")
		.append(" AND ped.CDREPRESENTANTE = itp.CDREPRESENTANTE")
		.append(" AND ped.NUPEDIDO = itp.NUPEDIDO")
		.append(" AND ped.FLORIGEMPEDIDO = itp.FLORIGEMPEDIDO")
		.append(" JOIN TBLVPPRODUTO prod ON")
		.append(" itp.CDEMPRESA = prod.CDEMPRESA");
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			sql.append(" AND prod.CDREPRESENTANTE =").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante));
		} else {
			sql.append(" AND prod.CDREPRESENTANTE = itp.CDREPRESENTANTE");
		}
		sql.append(" AND itp.CDPRODUTO = prod.CDPRODUTO")
		.append(" JOIN TBLVPCLIENTE cli ON")
		.append(" ped.CDEMPRESA = cli.CDEMPRESA")
		.append(" AND ped.CDREPRESENTANTE = cli.CDREPRESENTANTE")
		.append(" AND ped.CDCLIENTE = cli.CDCLIENTE");
	}

	private void addWhereByExampleFromFechamentoPedido(BaseDomain domain, StringBuffer sql) {
		ProdutoClienteCod produtoClienteCod = (ProdutoClienteCod) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" itp.CDEMPRESA =", produtoClienteCod.cdEmpresa);
		sqlWhereClause.addAndCondition(" itp.CDREPRESENTANTE = ", produtoClienteCod.cdRepresentante);
		sqlWhereClause.addAndCondition(" itp.NUPEDIDO =", produtoClienteCod.nuPedido);
		sql.append(sqlWhereClause.getSql());
	}
	
}
