package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ProducaoProd;
import br.com.wmw.lavenderepda.business.domain.Produto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ProducaoProdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProducaoProd();
	}

    private static ProducaoProdDbxDao instance;
	

    public ProducaoProdDbxDao() {
        super(ProducaoProd.TABLE_NAME);
    }
    
    public static ProducaoProdDbxDao getInstance() {
        if (instance == null) {
            instance = new ProducaoProdDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ProducaoProd producaoProd = new ProducaoProd();
        producaoProd.rowKey = rs.getString("rowkey");
        producaoProd.cdEmpresa = rs.getString("cdEmpresa");
        producaoProd.cdProduto = rs.getString("cdProduto");
        producaoProd.dtInicial = rs.getDate("dtInicial");
        producaoProd.dtFinal = rs.getDate("dtFinal");
        producaoProd.qtdProducaoProd = ValueUtil.round(rs.getDouble("qtdProducaoProd"));
        producaoProd.qtdDisponivel = ValueUtil.round(rs.getDouble("qtdDisponivel"));
        producaoProd.nuCarimbo = rs.getInt("nuCarimbo");
        producaoProd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        producaoProd.flEstoqueExcluido = rs.getString("flEstoqueExcluido");
        producaoProd.cdUsuario = rs.getString("cdUsuario");
        return producaoProd;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDPRODUTO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" QTDPRODUCAOPROD,");
        sql.append(" QTDDISPONIVEL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLESTOQUEEXCLUIDO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDPRODUTO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" QTDPRODUCAOPROD,");
        sql.append(" QTDDISPONIVEL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLESTOQUEEXCLUIDO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ProducaoProd producaoProd = (ProducaoProd) domain;
        sql.append(Sql.getValue(producaoProd.cdEmpresa)).append(",");
        sql.append(Sql.getValue(producaoProd.cdProduto)).append(",");
        sql.append(Sql.getValue(producaoProd.dtInicial)).append(",");
        sql.append(Sql.getValue(producaoProd.dtFinal)).append(",");
        sql.append(Sql.getValue(producaoProd.qtdProducaoProd)).append(",");
        sql.append(Sql.getValue(producaoProd.qtdDisponivel)).append(",");
        sql.append(Sql.getValue(producaoProd.nuCarimbo)).append(",");
        sql.append(Sql.getValue(producaoProd.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(producaoProd.flEstoqueExcluido)).append(",");
        sql.append(Sql.getValue(producaoProd.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ProducaoProd producaoProd = (ProducaoProd) domain;
        sql.append(" QTDPRODUCAOPROD = ").append(Sql.getValue(producaoProd.qtdProducaoProd)).append(",");
        sql.append(" QTDDISPONIVEL = ").append(Sql.getValue(producaoProd.qtdDisponivel)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(producaoProd.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(producaoProd.flTipoAlteracao)).append(",");
        sql.append(" FLESTOQUEEXCLUIDO = ").append(Sql.getValue(producaoProd.flEstoqueExcluido)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(producaoProd.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ProducaoProd producaoProd = (ProducaoProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", producaoProd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", producaoProd.cdProduto);
		sqlWhereClause.addAndCondition("DTINICIAL = ", producaoProd.dtInicial);
		sqlWhereClause.addAndCondition("DTFINAL = ", producaoProd.dtFinal);
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
    //@Override
    public Vector findAllDistinctPeriodo(ProducaoProd producaoProdFilter) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select DTINICIAL, DTFINAL from ");
        sql.append(tableName);
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", producaoProdFilter.cdEmpresa);
		sql.append(sqlWhereClause.getSql());
        sql.append(" order by DTINICIAL, DTFINAL");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				Date dtInicial = rs.getDate("DTINICIAL");
				Date dtFinal = rs.getDate("DTFINAL");
				if (ValueUtil.isNotEmpty(dtInicial) && ValueUtil.isNotEmpty(dtFinal)) {
					String periodo = dtInicial + " - " + dtFinal;
					if (result.indexOf(periodo) == -1) {
						result.addElement(periodo);
					}
				}
			}
			return result;
		}
    }

    //@Override
    public Vector findAllNaoVigentes(ProducaoProd producaoProd) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select * from ");
    	sql.append(tableName);
    	sql.append(" where dtFinal < ").append(Sql.getValue(producaoProd.dtFinalFilter));
    	sql.append(" and (flEstoqueExcluido = 'N' or flEstoqueExcluido is null)");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector result = new Vector(50);
    		while (rs.next()) {
    			result.addElement(populate(producaoProd, rs));
    		}
    		return result;
    	}
    }
    
    public Vector findAllByExample(ProducaoProd producaoProd) throws SQLException {
    	Produto produtoFilter = new Produto();
    	produtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	produtoFilter.dsProduto = producaoProd.dsProdutoFilter;
    	produtoFilter.cdProdutoLikeFilter = producaoProd.dsProdutoFilter;
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select tb.* from ").append(tableName).append(" tb");
    	sql.append(" join ").append(Produto.TABLE_NAME).append(" prod");
    	sql.append(" on ").append("tb.CDPRODUTO = prod.CDPRODUTO");
    	sql.append(" where (").append("prod.CDPRODUTO").append(" like ").append("'").append(produtoFilter.cdProdutoLikeFilter).append("'");
    	sql.append(" or prod.DSPRODUTO").append(" like ").append("'").append(produtoFilter.dsProduto).append("'");
    	sql.append(" ) and prod.CDREPRESENTANTE = ").append(Sql.getValue(produtoFilter.cdRepresentante));
		Vector result = new Vector(50);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			result.addElement(populate(produtoFilter, rs));
    		}
    		return result;
    	}
    }

	public boolean hasProducaoProdVigente(ProducaoProd producaoProd) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select count(*) as qtd  from ").append(tableName).append(" where ");
		sql.append("cdEmpresa = ").append(Sql.getValue(producaoProd.cdEmpresa));
		sql.append(" and dtInicial <= ").append(Sql.getValue(producaoProd.dtFilter));
		sql.append(" and dtFinal >= ").append(Sql.getValue(producaoProd.dtFilter));
		sql.append(" and cdProduto = ").append(Sql.getValue(producaoProd.cdProduto));
		return getInt(sql.toString()) > 0;
	}
    
}