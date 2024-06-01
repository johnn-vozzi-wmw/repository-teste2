package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Restricao;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class GrupoProduto1PdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoProduto1();
	}

    private static GrupoProduto1PdbxDao instance;

    public GrupoProduto1PdbxDao() {
        super(GrupoProduto1.TABLE_NAME);
    }

    public static GrupoProduto1PdbxDao getInstance() {
        if (instance == null) {
            instance = new GrupoProduto1PdbxDao();
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
        sql.append(" rowkey,");
        sql.append(" cdGrupoProduto1,");
        sql.append(" dsGrupoProduto1,");
        if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
        	sql.append(" cdCanalGrupo,");
		}
        if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata) {
        	sql.append(" FLCOMPARADESC,");
        }
        if (LavenderePdaConfig.usaInfoComplementarItemPedido()) {
        	sql.append(" FLOBRIGACULTURAPRAGA,");
        }
        sql.append(" flBloqueiaVendaPorMeta,");
        sql.append(" flValidaInfoCompProduto");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoProduto1 grupoproduto1 = new GrupoProduto1();
        grupoproduto1.rowKey = rs.getString("rowkey");
        grupoproduto1.cdGrupoproduto1 = rs.getString("cdGrupoproduto1");
        grupoproduto1.dsGrupoproduto1 = rs.getString("dsGrupoproduto1");
        grupoproduto1.flBloqueiaVendaPorMeta  = rs.getString("flBloqueiaVendaPorMeta");
        if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
        	grupoproduto1.cdCanalGrupo  = rs.getString("cdCanalGrupo");
        }
        if (LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata) {
        	grupoproduto1.flComparaDesc = rs.getString("flComparaDesc");
        }
        if (LavenderePdaConfig.usaInfoComplementarItemPedido()) {
        	grupoproduto1.flObrigaCulturaPraga = rs.getString("flObrigaCulturaPraga");
        }
        grupoproduto1.flValidaInfoCompProduto = rs.getString("flValidaInfoCompProduto");
        return grupoproduto1;
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProduto1 grupoproduto1 = (GrupoProduto1) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("cdGrupoProduto1 = ", grupoproduto1.cdGrupoproduto1);
		sqlWhereClause.addAndCondition("flBloqueiaVendaPorMeta = ", grupoproduto1.flBloqueiaVendaPorMeta);
		sqlWhereClause.addAndLikeCondition("dsGrupoProduto1", grupoproduto1.dsGrupoproduto1);
		
		if (LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido && grupoproduto1.grupoProdTipoPedFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsGrupoProdTipoPedCondition(grupoproduto1.grupoProdTipoPedFilter, 1));
		}
		if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor && grupoproduto1.grupoProdFornFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsGrupoProdFornCondition(grupoproduto1.grupoProdFornFilter));
		}
		if (grupoproduto1.filterByGrupoProdutoExistente) {
			sqlWhereClause.addAndCondition(getCdGrupoProdExistenteProdutoCondition(grupoproduto1, grupoproduto1.restricaoFilter));
		}
		sql.append(sqlWhereClause.getSql());
    }

    public Vector findAllCdGrupoProduto1() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT tb.cdgrupoproduto1 from TBLVPPRODUTO tb, TBLVPGRUPOPRODUTO1 grupo where grupo.cdGrupoProduto1 = tb.cdGrupoProduto1 order by tb.cdGrupoProduto1");
    	Vector itens = new Vector();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
	    	while (rs.next()) {
	    		String cd = rs.getString(1);
				if (itens.indexOf(cd) == -1) {
					itens.addElement(cd);
				}
	    	}
    	}
    	return itens;
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by DSGRUPOPRODUTO1 ");
    }
    
    public List<GrupoProduto1> findCdGrupoProdutoByPedido(Pedido pedido) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT prod.CDGRUPOPRODUTO1, gp1.DSGRUPOPRODUTO1 FROM TBLVPITEMPEDIDO tb JOIN TBLVPPRODUTO prod ON tb.CDEMPRESA = prod.CDEMPRESA AND tb.CDREPRESENTANTE = prod.CDREPRESENTANTE ")
    	.append(" AND tb.CDPRODUTO = prod.CDPRODUTO") 
    	.append(" JOIN TBLVPGRUPOPRODUTO1 gp1 ON prod.CDGRUPOPRODUTO1 = gp1.CDGRUPOPRODUTO1 ") 
    	.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa)).append(" AND tb.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante))
    	.append(" AND tb.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido)).append(" GROUP BY prod.CDGRUPOPRODUTO1");
    	List<GrupoProduto1> grupoProduto1List = new ArrayList<>();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		GrupoProduto1 gp1;
    		while (rs.next()) {
    			gp1 = new GrupoProduto1();
    			gp1.cdGrupoproduto1 = rs.getString(1);
    			gp1.dsGrupoproduto1 = rs.getString(2);
    			grupoProduto1List.add(gp1);
    		}
    	}
    	return grupoProduto1List;
    }
    
	private void appendSqlGrupoProduto(GrupoProduto1 domainFilter, StringBuffer sql) {
		sql.append("SELECT DISTINCT PROD.cdGrupoProduto1 ");
		sql.append("from ");
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			sql.append("TBLVPPRODUTOTABPRECO");
		} else {
			sql.append("TBLVPPRODUTO");
		}
		sql.append(" PROD ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" PROD.cdEmpresa = ", SessionLavenderePda.cdEmpresa);
		sqlWhereClause.addAndCondition(" PROD.cdRepresentante = ", SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProduto1.class));
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			sqlWhereClause.addAndLikeCondition(" PROD.dsTabPrecoList", domainFilter.produtoFilter.cdTabelaPreco);
		}
		sql.append(sqlWhereClause.getSql());
	}
	
	private String getCdGrupoProdExistenteProdutoCondition(GrupoProduto1 domainFilter, Restricao restricaoFilter) {
		StringBuffer sbGrupoProdExistente = new StringBuffer();
		
		sbGrupoProdExistente.append(" CDGRUPOPRODUTO1 IN (");
		appendSqlGrupoProduto(domainFilter, sbGrupoProdExistente);
		if (domainFilter.addCteRestricao && restricaoFilter != null) {
			RestricaoDbxDao.getInstance().addConditionRestricao(sbGrupoProdExistente, restricaoFilter);
		}
		sbGrupoProdExistente.append(")");
		return sbGrupoProdExistente.toString();
	}
	
	@Override
	protected void addCTE(BaseDomain domainFilter, StringBuffer sql) {
		GrupoProduto1 grupoProduto1 = (GrupoProduto1) domainFilter;
		if (grupoProduto1.addCteRestricao) {
			RestricaoDbxDao.getInstance().appendCTERestricao(sql, grupoProduto1.restricaoFilter);
		}
	}

}