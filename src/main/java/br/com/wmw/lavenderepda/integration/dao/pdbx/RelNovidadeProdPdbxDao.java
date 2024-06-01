package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.LocalEstoque;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemGrade;
import br.com.wmw.lavenderepda.business.domain.TipoNovidade;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RelNovidadeProdPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelNovidadeProd();
	}

    private static RelNovidadeProdPdbxDao instance;

    public RelNovidadeProdPdbxDao() {
        super(RelNovidadeProd.TABLE_NAME);
    }
    
    protected void addInsertColumns(StringBuffer sql) throws SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

    public static RelNovidadeProdPdbxDao getInstance() {
        return instance == null ? instance = new RelNovidadeProdPdbxDao() : instance;
    }

	public BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		RelNovidadeProd relNovidadeProd = new RelNovidadeProd();
		relNovidadeProd.dsNovidadeProduto = rs.getString(1);
		return relNovidadeProd;
	}

	public void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" TB.DSNOVIDADEPRODUTO");
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" DISTINCT TB.rowkey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.CDPRODUTO,");
        sql.append(" TB.CDTIPONOVIDADE,");
        sql.append(" TB.CDITEMGRADE1,");
        sql.append(" TB.CDITEMGRADE2,");
        sql.append(" TB.CDITEMGRADE3,");
        sql.append(" TB.CDTABELAPRECO,");
        sql.append(" TB.CDUF,");
        sql.append(" TB.CDUNIDADE,");
        sql.append(" TB.QTITEM,");
        sql.append(" TB.CDPRAZOPAGTOPRECO,");
        sql.append(" TB.CDLOCALESTOQUE,");
        sql.append(" TB.FLORIGEMESTOQUE,");
        sql.append(" TB.DSPRODUTO,");
        sql.append(" TB.VLPRECOANTIGO,");
        if (LavenderePdaConfig.isConfigGradeProduto()) {
        	sql.append(" CASE WHEN TB.CDTIPONOVIDADE = ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE))
			        .append(" THEN SUM(TB.DSNOVIDADEPRODUTO) ELSE TB.DSNOVIDADEPRODUTO END AS DSNOVIDADEPRODUTO,");
        } else {
	        sql.append(" TB.DSNOVIDADEPRODUTO,");
        }
        sql.append(" TB.DTEMISSAORELATORIO,");
        
        addSelectGradeColumns(sql);
        addSelectTabPrecoColumns(sql);
        addSelectLocalEstoqueColumns(sql);
        
        if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos()) {
    		sql.append(","+DaoUtil.ALIAS_PRODUTO).append(".DSMARCA");
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	sql.append(","+DaoUtil.ALIAS_PRODUTO).append(".DSREFERENCIA");
        }
    }

	private void addSelectLocalEstoqueColumns(StringBuffer sql) {
        sql.append(" LOCALESTOQUE.DSLOCALESTOQUE");
	}

	private void addSelectTabPrecoColumns(StringBuffer sql) {
        sql.append(" TABPRECO.DSTABELAPRECO,");
	}

	private void addSelectGradeColumns(StringBuffer sql) {
        if (!LavenderePdaConfig.isConfigGradeProduto()) return;
    	sql.append(" ITEMGRADE1.DSITEMGRADE ITEMGRADE1,");
    	sql.append(" TIPOITEMGRADE1.DSTIPOITEMGRADE DSTIPOITEMGRADE1,");
    	sql.append(" ITEMGRADE2.DSITEMGRADE ITEMGRADE2,");
    	sql.append(" TIPOITEMGRADE2.DSTIPOITEMGRADE DSTIPOITEMGRADE2,");
    	sql.append(" ITEMGRADE3.DSITEMGRADE ITEMGRADE3,");
    	sql.append(" TIPOITEMGRADE3.DSTIPOITEMGRADE DSTIPOITEMGRADE3,");
    	sql.append(" ITEMGRADE1.NUSEQUENCIA NUSEQUENCIA1,");
    	sql.append(" ITEMGRADE2.NUSEQUENCIA NUSEQUENCIA2,");
    	sql.append(" ITEMGRADE3.NUSEQUENCIA NUSEQUENCIA3,");
    	sql.append(" PRODUTOGRADE.NUCODIGOBARRAS,");
	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        RelNovidadeProd relNovidadeProduto = new RelNovidadeProd();
        relNovidadeProduto.rowKey = rs.getString("rowkey");
        relNovidadeProduto.cdEmpresa = rs.getString("cdEmpresa");
        relNovidadeProduto.cdRepresentante = rs.getString("cdRepresentante");
        relNovidadeProduto.cdProduto = rs.getString("cdProduto");
        relNovidadeProduto.cdTipoNovidade = rs.getString("cdTipoNovidade");
        relNovidadeProduto.cdItemGrade1 = rs.getString("cdItemGrade1");
        relNovidadeProduto.cdItemGrade2 = rs.getString("cdItemGrade2");
        relNovidadeProduto.cdItemGrade3 = rs.getString("cdItemGrade3");
        relNovidadeProduto.cdTabelaPreco = getStringValueOrDefault("cdTabelaPreco", rs);
        relNovidadeProduto.cdUf = getStringValueOrDefault("cdUf", rs);
        relNovidadeProduto.cdUnidade = getStringValueOrDefault("cdUnidade", rs);
        relNovidadeProduto.qtItem = rs.getInt("qtItem");
        relNovidadeProduto.cdPrazoPagtoPreco = rs.getInt("cdPrazoPagtoPreco");
        relNovidadeProduto.cdLocalEstoque = getStringValueOrDefault("cdLocalEstoque", rs);
        relNovidadeProduto.flOrigemEstoque = getStringValueOrDefault("flOrigemEstoque", rs);
        relNovidadeProduto.dsProduto = rs.getString("dsProduto");
        relNovidadeProduto.vlPrecoAntigo = rs.getDouble("vlPrecoAntigo");
        relNovidadeProduto.dsNovidadeProduto = rs.getString("dsNovidadeProduto");
        relNovidadeProduto.dtEmissaoRelatorio = rs.getDate("dtEmissaoRelatorio");
        if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos()) {
    		relNovidadeProduto.dsMarcaProduto = rs.getString("dsMarca");
        }
        if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	relNovidadeProduto.dsReferenciaProduto = rs.getString("dsReferencia");
        }
        populateTabelaPreco(rs, relNovidadeProduto);
        populateLocalEstoque(rs, relNovidadeProduto);
        populateGrade(rs, relNovidadeProduto);
        return relNovidadeProduto;
    }

	private void populateGrade(ResultSet rs, RelNovidadeProd relNovidadeProduto) throws SQLException {
        if (!LavenderePdaConfig.isConfigGradeProduto() || !relNovidadeProduto.possuiGrades()) return;
    	relNovidadeProduto.produtoGrade = new ProdutoGrade();
        relNovidadeProduto.produtoGrade.cdEmpresa = relNovidadeProduto.cdEmpresa;
        relNovidadeProduto.produtoGrade.cdRepresentante = relNovidadeProduto.cdRepresentante;        	
    	relNovidadeProduto.produtoGrade.cdItemGrade1 = relNovidadeProduto.cdItemGrade1;
    	relNovidadeProduto.produtoGrade.cdItemGrade2 = relNovidadeProduto.cdItemGrade2;
    	relNovidadeProduto.produtoGrade.cdItemGrade3 = relNovidadeProduto.cdItemGrade3;
    	relNovidadeProduto.produtoGrade.nuCodigoBarras = rs.getString("NUCODIGOBARRAS");
        relNovidadeProduto.itemGrade1 = new ItemGrade();
		relNovidadeProduto.itemGrade1.dsItemGrade = rs.getString("ITEMGRADE1");
		relNovidadeProduto.itemGrade1.nuSequencia = rs.getInt("NUSEQUENCIA1");
		relNovidadeProduto.itemGrade2 = new ItemGrade();
		relNovidadeProduto.itemGrade2.dsItemGrade = rs.getString("ITEMGRADE2");
		relNovidadeProduto.itemGrade2.nuSequencia = rs.getInt("NUSEQUENCIA2");
		relNovidadeProduto.itemGrade3 = new ItemGrade();
		relNovidadeProduto.itemGrade3.dsItemGrade = rs.getString("ITEMGRADE3");
		relNovidadeProduto.itemGrade3.nuSequencia = rs.getInt("NUSEQUENCIA3");
		relNovidadeProduto.tipoItemGrade1 = new TipoItemGrade();
		relNovidadeProduto.tipoItemGrade1.dsTipoItemGrade = rs.getString("DSTIPOITEMGRADE1");
		relNovidadeProduto.tipoItemGrade2 = new TipoItemGrade();
		relNovidadeProduto.tipoItemGrade2.dsTipoItemGrade = rs.getString("DSTIPOITEMGRADE2");
		relNovidadeProduto.tipoItemGrade3 = new TipoItemGrade();
		relNovidadeProduto.tipoItemGrade3.dsTipoItemGrade = rs.getString("DSTIPOITEMGRADE3");
	}

	private void populateLocalEstoque(ResultSet rs, RelNovidadeProd relNovidadeProduto) throws SQLException {
        if (!relNovidadeProduto.possuiLocalEstoque()) return;
        relNovidadeProduto.localEstoque = new LocalEstoque();
        relNovidadeProduto.localEstoque.cdEmpresa = relNovidadeProduto.cdEmpresa;
        relNovidadeProduto.localEstoque.cdRepresentante = relNovidadeProduto.cdRepresentante;
        relNovidadeProduto.localEstoque.dsLocalEstoque = rs.getString("dsLocalEstoque");
	}

	private void populateTabelaPreco(ResultSet rs, RelNovidadeProd relNovidadeProduto) throws SQLException {
        if (!relNovidadeProduto.possuiTabelaPreco()) return;
    	relNovidadeProduto.tabelaPreco = new TabelaPreco();
        relNovidadeProduto.tabelaPreco.cdEmpresa = relNovidadeProduto.cdEmpresa;
        relNovidadeProduto.tabelaPreco.cdRepresentante = relNovidadeProduto.cdRepresentante;
        relNovidadeProduto.tabelaPreco.cdTabelaPreco = relNovidadeProduto.cdTabelaPreco;
        relNovidadeProduto.tabelaPreco.dsTabelaPreco = rs.getString("dsTabelaPreco");
	}

	private String getStringValueOrDefault(String columnName, ResultSet rs) throws SQLException {
		String value = rs.getString(columnName);
		return ValueUtil.isNotEmpty(value) ? value : RelNovidadeProd.VALOR_PADRAO;
	}

    public Vector getRegistrosPorTipoNovidade(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select count(*) as qtRegistro, cdtiponovidade  from ");
    	sql.append(tableName);
        sql.append(" tb ");
        addJoin(domain, sql);
    	addWhereByExample(domain, sql);
    	sql.append(" group by cdtiponovidade;");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
            Vector vetor = new Vector();
	        while (rs.next()) {
	        	RelNovidadeProd relNovidadeProd = new RelNovidadeProd();
	        	relNovidadeProd.cdTipoNovidade = rs.getString("cdtiponovidade");
	        	relNovidadeProd.qtRegistrosTipoNovidade = rs.getInt("qtRegistro");
	            vetor.addElement(relNovidadeProd);
	        }
	        return vetor;
		}
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        RelNovidadeProd relNovidadeProduto = (RelNovidadeProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", relNovidadeProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", relNovidadeProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDPRODUTO = ", relNovidadeProduto.cdProdutoExato);
		sqlWhereClause.addAndCondition("TB.CDTIPONOVIDADE = ", relNovidadeProduto.cdTipoNovidade);
		sqlWhereClause.addAndCondition("TB.DTEMISSAORELATORIO > ", relNovidadeProduto.dtEmissaoRelatorio);
		if (relNovidadeProduto.filterGrade) {
			sqlWhereClause.addAndCondition("TB.CDITEMGRADE1 = ", relNovidadeProduto.cdItemGrade1);
			sqlWhereClause.addAndCondition("TB.CDITEMGRADE2 = ", relNovidadeProduto.cdItemGrade2);
			sqlWhereClause.addAndCondition("TB.CDITEMGRADE3 = ", relNovidadeProduto.cdItemGrade3);
		}
		sqlWhereClause.addAndCondition("TB.CDTABELAPRECO = ", relNovidadeProduto.cdTabelaPreco);
		sqlWhereClause.addAndCondition("TB.CDUF = ", relNovidadeProduto.cdUf);
		sqlWhereClause.addAndCondition("TB.CDUNIDADE = ", relNovidadeProduto.cdUnidade);
		sqlWhereClause.addAndCondition("TB.QTITEM = ", relNovidadeProduto.qtItem);
		sqlWhereClause.addAndCondition("TB.CDPRAZOPAGTOPRECO = ", relNovidadeProduto.cdPrazoPagtoPreco);
		sqlWhereClause.addAndCondition("TB.CDLOCALESTOQUE = ", relNovidadeProduto.cdLocalEstoque);
		sqlWhereClause.addAndCondition("TB.FLORIGEMESTOQUE = ", relNovidadeProduto.flOrigemEstoque);
		
		if (relNovidadeProduto.addWhereTabPreco) {
			sqlWhereClause.addAndCondition("((TB.CDTIPONOVIDADE IN (2, 10, 11) AND TABPRECO.CDTABELAPRECO IS NOT NULL) OR TB.CDTIPONOVIDADE NOT IN (2, 10, 11))");
		}
		
		sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("TB.DSPRODUTO", relNovidadeProduto.dsProduto, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("TB.CDPRODUTO", relNovidadeProduto.cdProduto, false);
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		sql.append(sqlWhereClause.getSql());
        if (LavenderePdaConfig.usaTabelaPrecoPorCliente && TipoNovidadeService.getInstance().isTipoNovidadeAlteracaoPreco(relNovidadeProduto.cdTipoNovidade) && LavenderePdaConfig.isEntidadeSyncWebAppRepZero(ItemTabelaPreco.TABLE_NAME.substring(5))) {
            TabelaPrecoCliPdbxDao.addTabPrecoCliFilter(sql, true);
		}
    }

    public void deleteAllByDtEmissao(BaseDomain domain) {
        StringBuffer sql = getSqlBuffer();
        sql.append(" delete from ").append(tableName);
        //--
        RelNovidadeProd relNovidadeProduto = (RelNovidadeProd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DTEMISSAORELATORIO <= ", relNovidadeProduto.dtEmissaoRelatorio);
		sql.append(sqlWhereClause.getSql());
        //--
        try {
        	executeUpdate(sql.toString());
        } catch (Throwable e) {
        	ExceptionUtil.handle(e);
		}
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos() || LavenderePdaConfig.isMostraDescricaoReferencia()) {
    		DaoUtil.addJoinProduto(sql, DaoUtil.ALIAS_PRODUTO, true);
		}
    	addLeftJoinTabelaPreco(sql);
    	addLeftJoinLocalEstoque(sql);
		addLeftJoinGrades(sql);
    }

	private void addLeftJoinGrades(StringBuffer sql) {
		if (!LavenderePdaConfig.isConfigGradeProduto()) return;
		sql.append(" LEFT JOIN TBLVPPRODUTOGRADE PRODUTOGRADE ON PRODUTOGRADE.CDEMPRESA = TB.CDEMPRESA AND PRODUTOGRADE.CDREPRESENTANTE = TB.CDREPRESENTANTE AND PRODUTOGRADE.CDPRODUTO = TB.CDPRODUTO AND (PRODUTOGRADE.CDITEMGRADE1 = TB.CDITEMGRADE1 AND PRODUTOGRADE.CDITEMGRADE2 = TB.CDITEMGRADE2 AND PRODUTOGRADE.CDITEMGRADE3 = TB.CDITEMGRADE3)");
		sql.append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE1 ON ITEMGRADE1.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ITEMGRADE1.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ITEMGRADE1.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE1 AND ITEMGRADE1.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE1");
		sql.append(" LEFT JOIN TBLVPTIPOITEMGRADE TIPOITEMGRADE1 ON TIPOITEMGRADE1.CDEMPRESA = ITEMGRADE1.CDEMPRESA AND TIPOITEMGRADE1.CDREPRESENTANTE = ITEMGRADE1.CDREPRESENTANTE AND TIPOITEMGRADE1.CDTIPOITEMGRADE = ITEMGRADE1.CDTIPOITEMGRADE");
		sql.append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE2 ON ITEMGRADE2.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ITEMGRADE2.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ITEMGRADE2.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE2 AND ITEMGRADE2.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE2");
		sql.append(" LEFT JOIN TBLVPTIPOITEMGRADE TIPOITEMGRADE2 ON TIPOITEMGRADE2.CDEMPRESA = ITEMGRADE2.CDEMPRESA AND TIPOITEMGRADE2.CDREPRESENTANTE = ITEMGRADE2.CDREPRESENTANTE AND TIPOITEMGRADE2.CDTIPOITEMGRADE = ITEMGRADE2.CDTIPOITEMGRADE");
		sql.append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE3 ON ITEMGRADE3.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ITEMGRADE3.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ITEMGRADE3.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE3 AND ITEMGRADE3.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE3");
		sql.append(" LEFT JOIN TBLVPTIPOITEMGRADE TIPOITEMGRADE3 ON TIPOITEMGRADE3.CDEMPRESA = ITEMGRADE3.CDEMPRESA AND TIPOITEMGRADE3.CDREPRESENTANTE = ITEMGRADE3.CDREPRESENTANTE AND TIPOITEMGRADE3.CDTIPOITEMGRADE = ITEMGRADE3.CDTIPOITEMGRADE");
	}

	private void addLeftJoinLocalEstoque(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPLOCALESTOQUE LOCALESTOQUE ON ");
		sql.append("LOCALESTOQUE.CDEMPRESA = tb.CDEMPRESA AND LOCALESTOQUE.CDREPRESENTANTE = tb.CDREPRESENTANTE AND LOCALESTOQUE.CDLOCALESTOQUE = tb.CDLOCALESTOQUE");
	}

	private void addLeftJoinTabelaPreco(StringBuffer sql) {
		sql.append(" LEFT JOIN TBLVPTABELAPRECO TABPRECO ON ");
		sql.append("TABPRECO.CDEMPRESA = tb.CDEMPRESA AND TABPRECO.CDREPRESENTANTE = tb.CDREPRESENTANTE AND TABPRECO.CDTABELAPRECO = tb.CDTABELAPRECO");
	}
	
    @Override
    protected void addGroupBy(StringBuffer sql) {
    	if (LavenderePdaConfig.isConfigGradeProduto()) {
	    	sql.append(" GROUP BY CASE WHEN TB.CDTIPONOVIDADE = ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDEMPRESA ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE = ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDREPRESENTANTE ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE = ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDPRODUTO ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE = ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDTIPONOVIDADE ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDITEMGRADE1 ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDITEMGRADE2 ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDITEMGRADE3 ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDTABELAPRECO ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDUF ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDUNIDADE ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.QTITEM ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDPRAZOPAGTOPRECO ELSE 0 END, ")
	     	             .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.CDLOCALESTOQUE ELSE 0 END, ")
	    				 .append("CASE WHEN TB.CDTIPONOVIDADE <> ").append(Sql.getValue(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE)).append(" THEN TB.FLORIGEMESTOQUE ELSE 0 END ");
    	}
    }
    
}
