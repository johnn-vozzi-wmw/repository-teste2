package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.ProdutoGradeBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueDisponivel;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ProdutoGradeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoGrade();
	}

    private static ProdutoGradeDbxDao instance;

    public ProdutoGradeDbxDao() {
        super(ProdutoGrade.TABLE_NAME);
    }

    public static ProdutoGradeDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoGradeDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoGrade produtoGrade = new ProdutoGrade();
        produtoGrade.rowKey = rs.getString("rowkey");
        produtoGrade.cdEmpresa = rs.getString("cdEmpresa");
        produtoGrade.cdRepresentante = rs.getString("cdRepresentante");
        produtoGrade.cdProduto = rs.getString("cdProduto");
        produtoGrade.cdItemGrade1 = rs.getString("cdItemGrade1");
        produtoGrade.cdItemGrade2 = rs.getString("cdItemGrade2");
        produtoGrade.cdItemGrade3 = rs.getString("cdItemGrade3");
        produtoGrade.cdTabelaPreco = rs.getString("cdTabelaPreco");
        produtoGrade.cdTipoItemGrade1 = rs.getString("cdTipoItemGrade1");
        produtoGrade.cdTipoItemGrade2 = rs.getString("cdTipoItemGrade2");
        produtoGrade.cdTipoItemGrade3 = rs.getString("cdTipoItemGrade3");
        produtoGrade.nuCarimbo = rs.getInt("nuCarimbo");
        produtoGrade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoGrade.cdUsuario = rs.getString("cdUsuario");
        produtoGrade.flInfoComplVolume = rs.getString("flInfoComplVolume");
        produtoGrade.vlAlturaMin = rs.getDouble("vlAlturaMin");
        produtoGrade.vlAlturaMax = rs.getDouble("vlAlturaMax");
        produtoGrade.vlLarguraMin = rs.getDouble("vlLarguraMin");
        produtoGrade.vlLarguraMax = rs.getDouble("vlLarguraMax");
        produtoGrade.vlComprimentoMin = rs.getDouble("vlComprimentoMin");
        produtoGrade.vlComprimentoMax = rs.getDouble("vlComprimentoMax");
        produtoGrade.nuCodigoBarras = rs.getString("nuCodigoBarras");
        produtoGrade.qtPeso = rs.getDouble("qtPeso");
        produtoGrade.nuOrdemLinha = rs.getInt("nuOrdemLinha");
        produtoGrade.nuOrdemColuna = rs.getInt("nuOrdemColuna");
        return produtoGrade;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
	
	 //@Override
    protected BaseDomain populateCache(ResultSet rs) throws java.sql.SQLException {
        ProdutoGrade produtoGrade = new ProdutoGrade();
        produtoGrade.rowKey = rs.getString("rowKey");
        produtoGrade.cdEmpresa = rs.getString("cdEmpresa");
        produtoGrade.cdRepresentante = rs.getString("cdRepresentante");
        produtoGrade.cdProduto = rs.getString("cdProduto");
        produtoGrade.cdItemGrade1 = rs.getString("cdItemGrade1");
        produtoGrade.cdItemGrade2 = rs.getString("cdItemGrade2");
        produtoGrade.cdItemGrade3 = rs.getString("cdItemGrade3");
        return produtoGrade;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.cdItemGrade1,");
        sql.append(" tb.cdItemGrade2,");
        sql.append(" tb.cdItemGrade3,");
        sql.append(" tb.cdTabelaPreco,");
        sql.append(" tb.cdTipoItemGrade1,");
        sql.append(" tb.cdTipoItemGrade2,");
        sql.append(" tb.cdTipoItemGrade3,");
        sql.append(" tb.nuCarimbo,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.cdUsuario,");
        sql.append(" tb.flInfoComplVolume,");
        sql.append(" tb.vlAlturaMin,");
        sql.append(" tb.vlAlturaMax,");
        sql.append(" tb.vlLarguraMin,");
        sql.append(" tb.vlLarguraMax,");
        sql.append(" tb.vlComprimentoMin,");
        sql.append(" tb.vlComprimentoMax,");
        sql.append(" tb.nuCodigoBarras,");
        sql.append(" tb.qtPeso");
        sql.append(" , tb.nuOrdemColuna ").append(" , tb.nuOrdemLinha ");
    }

    @Override
    protected void addCacheColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" cdItemGrade1,");
        sql.append(" cdItemGrade2,");
        sql.append(" cdItemGrade3,");
        sql.append(" nuCodigoBarras");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" cdItemGrade1,");
        sql.append(" cdItemGrade2,");
        sql.append(" cdItemGrade3,");
        sql.append(" cdTabelaPreco,");
        sql.append(" cdTipoItemGrade1,");
        sql.append(" cdTipoItemGrade2,");
        sql.append(" cdTipoItemGrade3,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" flInfoComplVolume,");
        sql.append(" vlAlturaMin,");
        sql.append(" vlAlturaMax,");
        sql.append(" vlLarguraMin,");
        sql.append(" vlLarguraMax,");
        sql.append(" vlComprimentoMin,");
        sql.append(" vlComprimentoMax,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoGrade produtoGrade = (ProdutoGrade) domain;
        sql.append(Sql.getValue(produtoGrade.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdProduto)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdItemGrade1)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdItemGrade2)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdItemGrade3)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdTipoItemGrade1)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdTipoItemGrade2)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdTipoItemGrade3)).append(",");
        sql.append(Sql.getValue(produtoGrade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(produtoGrade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoGrade.flInfoComplVolume)).append(",");
        sql.append(Sql.getValue(produtoGrade.vlAlturaMin)).append(",");
        sql.append(Sql.getValue(produtoGrade.vlAlturaMax)).append(",");
        sql.append(Sql.getValue(produtoGrade.vlLarguraMin)).append(",");
        sql.append(Sql.getValue(produtoGrade.vlLarguraMax)).append(",");
        sql.append(Sql.getValue(produtoGrade.vlComprimentoMin)).append(",");
        sql.append(Sql.getValue(produtoGrade.vlComprimentoMax)).append(",");
        sql.append(Sql.getValue(produtoGrade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoGrade produtoGrade = (ProdutoGrade) domain;
        sql.append(" cdTipoItemGrade1 = ").append(Sql.getValue(produtoGrade.cdTipoItemGrade1)).append(",");
        sql.append(" cdTipoItemGrade2 = ").append(Sql.getValue(produtoGrade.cdTipoItemGrade2)).append(",");
        sql.append(" cdTipoItemGrade3 = ").append(Sql.getValue(produtoGrade.cdTipoItemGrade3)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(produtoGrade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoGrade.flTipoAlteracao)).append(",");
        sql.append(" FLINFOCOMPLVOLUME = ").append(Sql.getValue(produtoGrade.flInfoComplVolume)).append(",");
        sql.append(" VLALTURAMIN = ").append(Sql.getValue(produtoGrade.vlAlturaMin)).append(",");
        sql.append(" VLALTURAMAX = ").append(Sql.getValue(produtoGrade.vlAlturaMax)).append(",");
        sql.append(" VLLARGURAMIN = ").append(Sql.getValue(produtoGrade.vlLarguraMin)).append(",");
        sql.append(" VLLARGURAMAX = ").append(Sql.getValue(produtoGrade.vlLarguraMax)).append(",");
        sql.append(" VLCOMPRIMENTOMIN = ").append(Sql.getValue(produtoGrade.vlComprimentoMin)).append(",");
        sql.append(" VLCOMPRIMENTOMAX = ").append(Sql.getValue(produtoGrade.vlComprimentoMax)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(produtoGrade.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoGrade produtoGrade = (ProdutoGrade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", produtoGrade.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", produtoGrade.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", produtoGrade.cdProduto);
		sqlWhereClause.addAndCondition("tb.cdItemGrade1 = ", produtoGrade.cdItemGrade1);
		sqlWhereClause.addAndCondition("tb.cdItemGrade2 = ", produtoGrade.cdItemGrade2);
		sqlWhereClause.addAndCondition("tb.cdItemGrade3 = ", produtoGrade.cdItemGrade3);
		sqlWhereClause.addAndCondition("tb.cdTabelaPreco = ", produtoGrade.cdTabelaPreco);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    public void initCacheByPartialRowKey(BaseDomain domain) throws SQLException {
    	super.initCacheByPartialRowKey(new ProdutoGradeBuilder((ProdutoBase) domain, true).build());
    }
    
    public Vector findAllProdutoGradeErpByItemPedido(ItemPedido itemPedido) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("select tb.* from TBLVPPRODUTOGRADE tb ");
    	sql.append(" JOIN TBLVPITEMPEDIDOGRADEERP ITEMPEDIDOGRADEERP");
    	sql.append(" ON tb.CDEMPRESA = ITEMPEDIDOGRADEERP.CDEMPRESA AND tb.CDREPRESENTANTE = ITEMPEDIDOGRADEERP.CDREPRESENTANTE"); 
    	sql.append(" AND tb.CDPRODUTO = ITEMPEDIDOGRADEERP.CDPRODUTO  AND tb.CDITEMGRADE1 = ITEMPEDIDOGRADEERP.CDITEMGRADE1");
    	sql.append(" AND tb.CDPRODUTO = ITEMPEDIDOGRADEERP.CDPRODUTO  AND tb.CDITEMGRADE2 = ITEMPEDIDOGRADEERP.CDITEMGRADE2");
    	sql.append(" AND tb.CDPRODUTO = ITEMPEDIDOGRADEERP.CDPRODUTO  AND tb.CDITEMGRADE3 = ITEMPEDIDOGRADEERP.CDITEMGRADE3");
		sql.append(" WHERE ITEMPEDIDOGRADEERP.CDEMPRESA = '");
		sql.append(itemPedido.cdEmpresa);
		sql.append("' AND ITEMPEDIDOGRADEERP.CDREPRESENTANTE = '");
		sql.append(itemPedido.cdRepresentante);
    	sql.append("' AND tb.CDPRODUTO = '");
    	sql.append(itemPedido.cdProduto);
    	sql.append("' AND ITEMPEDIDOGRADEERP .QTITEMFISICO > 0");
    	sql.append(" AND ITEMPEDIDOGRADEERP.NUPEDIDO = '");
    	sql.append(itemPedido.nuPedido);
    	sql.append("'");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector produtoGradeList = new Vector();
			while (rs.next()) {
				produtoGradeList.addElement(populate(itemPedido, rs));
			}
			return produtoGradeList;	
		}
    }
    
    public Vector findAllProdutoGradeInseridosByItemPedido(ItemPedido itemPedido) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("select tb.* from TBLVPPRODUTOGRADE tb ");
    	sql.append(" JOIN TBLVPITEMPEDIDOGRADE ITEMPEDIDOGRADE");
    	sql.append(" ON tb.CDEMPRESA = ITEMPEDIDOGRADE.CDEMPRESA AND tb.CDREPRESENTANTE = ITEMPEDIDOGRADE.CDREPRESENTANTE"); 
    	sql.append(" AND tb.CDPRODUTO = ITEMPEDIDOGRADE.CDPRODUTO  AND tb.CDITEMGRADE1 = ITEMPEDIDOGRADE.CDITEMGRADE1");
    	sql.append(" AND tb.CDPRODUTO = ITEMPEDIDOGRADE.CDPRODUTO  AND tb.CDITEMGRADE2 = ITEMPEDIDOGRADE.CDITEMGRADE2");
    	sql.append(" AND tb.CDPRODUTO = ITEMPEDIDOGRADE.CDPRODUTO  AND tb.CDITEMGRADE3 = ITEMPEDIDOGRADE.CDITEMGRADE3");
		sql.append(" WHERE ITEMPEDIDOGRADE.CDEMPRESA = '");
		sql.append(itemPedido.cdEmpresa);
		sql.append("' AND ITEMPEDIDOGRADE.CDREPRESENTANTE = '");
		sql.append(itemPedido.cdRepresentante);
    	sql.append("' AND tb.CDPRODUTO = '");
    	sql.append(itemPedido.cdProduto);
    	sql.append("' AND ITEMPEDIDOGRADE .QTITEMFISICO > 0");
    	sql.append(" AND ITEMPEDIDOGRADE.NUPEDIDO = '");
    	sql.append(itemPedido.nuPedido);
    	sql.append("'");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector produtoGradeList = new Vector();
			while (rs.next()) {
				produtoGradeList.addElement(populate(itemPedido, rs));
			}
			return produtoGradeList;	
		}
    }
    
    public Vector findAllItemPedidoGradePorItemPedido(ItemPedidoGrade itemPedidoGrade, ProdutoGrade produtoGrade) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT ITEMGRADE1.DSITEMGRADE ITEMGRADE1, ITEMGRADE2.DSITEMGRADE ITEMGRADE2, ITEMGRADE3.DSITEMGRADE ITEMGRADE3, ITEMGRADE1.NUSEQUENCIA NUSEQUENCIA1, ITEMGRADE2.NUSEQUENCIA NUSEQUENCIA2, ITEMGRADE3.NUSEQUENCIA NUSEQUENCIA3, ITEMPEDIDOGRADE.QTITEMFISICO, PRODUTOGRADE.NUCODIGOBARRAS, PRODUTOGRADE.CDITEMGRADE1 CDITEMGRADE1, PRODUTOGRADE.CDITEMGRADE2 CDITEMGRADE2, PRODUTOGRADE.CDITEMGRADE3 CDITEMGRADE3");
		if (ValueUtil.valueEquals(itemPedidoGrade.flOrigemPedido, ItemPedido.ITEMPEDIDO_FLORIGEMERP)) {
			sql.append(" FROM TBLVPITEMPEDIDOGRADEERP ITEMPEDIDOGRADE");
		} else {
			sql.append(" FROM TBLVPITEMPEDIDOGRADE ITEMPEDIDOGRADE");
		}
		sql.append(" JOIN TBLVPPRODUTOGRADE PRODUTOGRADE ON PRODUTOGRADE.CDEMPRESA = ITEMPEDIDOGRADE.CDEMPRESA AND PRODUTOGRADE.CDPRODUTO = ITEMPEDIDOGRADE.CDPRODUTO AND (PRODUTOGRADE.CDITEMGRADE1 = ITEMPEDIDOGRADE.CDITEMGRADE1 AND PRODUTOGRADE.CDITEMGRADE2 = ITEMPEDIDOGRADE.CDITEMGRADE2 AND PRODUTOGRADE.CDITEMGRADE3 = ITEMPEDIDOGRADE.CDITEMGRADE3)");
		sql.append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE1 ON ITEMGRADE1.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ITEMGRADE1.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ITEMGRADE1.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE1 AND ITEMGRADE1.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE1");
		sql.append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE2 ON ITEMGRADE2.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ITEMGRADE2.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ITEMGRADE2.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE2 AND ITEMGRADE2.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE2");
		sql.append(" LEFT JOIN TBLVPITEMGRADE ITEMGRADE3 ON ITEMGRADE3.CDEMPRESA = PRODUTOGRADE.CDEMPRESA AND ITEMGRADE3.CDREPRESENTANTE = PRODUTOGRADE.CDREPRESENTANTE AND ITEMGRADE3.CDTIPOITEMGRADE = PRODUTOGRADE.CDTIPOITEMGRADE3 AND ITEMGRADE3.CDITEMGRADE = PRODUTOGRADE.CDITEMGRADE3");
		sql.append(" WHERE ITEMPEDIDOGRADE.CDEMPRESA = '");
		sql.append(itemPedidoGrade.cdEmpresa);
		sql.append("' AND ITEMPEDIDOGRADE.CDREPRESENTANTE = '");
		sql.append(itemPedidoGrade.cdRepresentante);
		sql.append("' AND ITEMPEDIDOGRADE.FLORIGEMPEDIDO = '");
		sql.append(itemPedidoGrade.flOrigemPedido);
		sql.append("' AND ITEMPEDIDOGRADE.NUPEDIDO = '");
		sql.append(itemPedidoGrade.nuPedido);
		sql.append("' AND ITEMPEDIDOGRADE.CDPRODUTO = '");
		sql.append(itemPedidoGrade.cdProduto);
		sql.append("' AND ITEMPEDIDOGRADE.FLTIPOITEMPEDIDO = '");
		sql.append(itemPedidoGrade.flTipoItemPedido);
		sql.append("' ORDER BY ITEMGRADE1.NUSEQUENCIA ASC, ITEMGRADE2.NUSEQUENCIA ASC, ITEMGRADE3.NUSEQUENCIA ASC");
		Vector itemPedidoGradeList = new Vector();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
        	ItemPedidoGrade itemPedidoGradeResult = null;
			while (rs.next()) {
				itemPedidoGradeResult = new ItemPedidoGrade();
				itemPedidoGradeResult.cdEmpresa = itemPedidoGrade.cdEmpresa;
				itemPedidoGradeResult.cdRepresentante = itemPedidoGrade.cdRepresentante;
				itemPedidoGradeResult.cdProduto = itemPedidoGrade.cdProduto;
				itemPedidoGradeResult.flOrigemPedido = itemPedidoGrade.flOrigemPedido;
				itemPedidoGradeResult.nuPedido = itemPedidoGrade.nuPedido;
				itemPedidoGradeResult.flTipoItemPedido = itemPedidoGrade.flTipoItemPedido;
				itemPedidoGradeResult.nuSeqProduto = itemPedidoGrade.nuSeqProduto;
				
				itemPedidoGradeResult.qtItemFisico = rs.getDouble("QTITEMFISICO");
				itemPedidoGradeResult.cdItemGrade1 = rs.getString("CDITEMGRADE1");
				itemPedidoGradeResult.cdItemGrade2 = rs.getString("CDITEMGRADE2");
				itemPedidoGradeResult.cdItemGrade3 = rs.getString("CDITEMGRADE3");
				itemPedidoGradeResult.itemGrade1 = new ItemGrade();
				itemPedidoGradeResult.itemGrade1.dsItemGrade = rs.getString("ITEMGRADE1");
				itemPedidoGradeResult.itemGrade1.nuSequencia = rs.getInt("NUSEQUENCIA1");
				itemPedidoGradeResult.itemGrade2 = new ItemGrade();
				itemPedidoGradeResult.itemGrade2.dsItemGrade = rs.getString("ITEMGRADE2");
				itemPedidoGradeResult.itemGrade2.nuSequencia = rs.getInt("NUSEQUENCIA2");
				itemPedidoGradeResult.itemGrade3 = new ItemGrade();
				itemPedidoGradeResult.itemGrade3.dsItemGrade = rs.getString("ITEMGRADE3");
				itemPedidoGradeResult.itemGrade3.nuSequencia = rs.getInt("NUSEQUENCIA3");
				itemPedidoGradeResult.produtoGrade = new ProdutoGrade();
				itemPedidoGradeResult.produtoGrade.nuCodigoBarras = rs.getString("NUCODIGOBARRAS");
				itemPedidoGradeList.addElement(itemPedidoGradeResult);
	        }
        } catch (Throwable e) {
        	ExceptionUtil.handle(e);
		}
    	return itemPedidoGradeList;
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	if (domainFilter == null) return;
    	ProdutoGrade produtoGrade = (ProdutoGrade) domainFilter;
    	if (produtoGrade.relNovidadeProdFilter != null) {
    		sql.append(" JOIN TBLVPRELNOVIDADEPROD REL ON");
    		try {
    			StringBuffer builder = getSqlBuffer();
				RelNovidadeProdPdbxDao.getInstance().addWhereByExample(produtoGrade.relNovidadeProdFilter, builder);
				builder.replace(0, 7, " ");
				sql.append(builder.toString().replaceAll("TB", "REL"));
	    		sql.append(" AND REL.CDPRODUTO = TB.CDPRODUTO AND");
	    		sql.append(" REL.CDITEMGRADE1 = TB.CDITEMGRADE1 AND");
	    		sql.append(" REL.CDITEMGRADE2 = TB.CDITEMGRADE2 AND");
	    		sql.append(" REL.CDITEMGRADE3 = TB.CDITEMGRADE3");
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
    	}
    }
    
    public Vector findAllProdutoGradeAgrupadorGrade(ProdutoGrade filter, Boolean existeNoPedido) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	ProdutoBase ultimoProdutoFilter = SessionLavenderePda.getUltimoProdutoFilter();
    	sql.append("SELECT ");
    	addSelectColumns(filter, sql);
    	sql.append(" FROM TBLVPPRODUTOGRADE tb ")
    	.append(" JOIN TBLVPPRODUTO PROD ON ")
    	.append(" PROD.CDEMPRESA = tb.CDEMPRESA AND ")
    	.append(" PROD.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
    	.append(" PROD.CDPRODUTO = tb.CDPRODUTO ");
    	if (ultimoProdutoFilter != null) {
    		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && ultimoProdutoFilter.itemTabelaPreco != null) {
    			DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter.itemTabelaPreco, false, false);
    		}
    		if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdTipoPedidoFilter)) {
    			DaoUtil.addJoinProdutoTipoPedido(sql, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter);
    		}
    		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && ultimoProdutoFilter.cdPlataformaVendaFilter != null) {
    			DaoUtil.addJoinPlataformaVendaProduto(sql, ultimoProdutoFilter.cdPlataformaVendaFilter, DaoUtil.ALIAS_PRODUTO);
    		}
    		if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdCentroCustoProdFilter)) {
    			DaoUtil.addJoinCentroCustoProd(sql, ultimoProdutoFilter, DaoUtil.ALIAS_PRODUTO);
    		}
    		if (ValueUtil.isNotEmpty(ultimoProdutoFilter.cdStatusEstoque)) {
    			if (ultimoProdutoFilter.estoque != null) {
    				DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, ultimoProdutoFilter.estoque.cdLocalEstoque, Estoque.FLORIGEMESTOQUE_ERP);
    				DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, ultimoProdutoFilter.estoque.cdLocalEstoque, Estoque.FLORIGEMESTOQUE_PDA);
    			} else {
					DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_ERP);
					DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_PDA);
    			}
    		}
    	}
	    if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
		    DaoUtil.addJoinProdutoClienteExclusivo(sql, SessionLavenderePda.getCliente().cdCliente, DaoUtil.ALIAS_PRODUTO);
	    }
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", filter.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", filter.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", filter.cdProduto);
		sqlWhereClause.addAndCondition("tb.cdItemGrade1 = ", filter.cdItemGrade1);
		sqlWhereClause.addAndCondition("tb.cdItemGrade2 = ", filter.cdItemGrade2);
		sqlWhereClause.addAndCondition("tb.cdItemGrade3 = ", filter.cdItemGrade3);
		sqlWhereClause.addAndCondition("tb.cdTabelaPreco = ", filter.cdTabelaPreco);
		if (ultimoProdutoFilter != null && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdStatusEstoque) && !existeNoPedido) {
			addFilterStatusEstoque(sqlWhereClause, ultimoProdutoFilter.cdStatusEstoque);
		}
		if (ultimoProdutoFilter != null) {
			addWhereProdutoGrade5(ultimoProdutoFilter, sqlWhereClause, DaoUtil.ALIAS_PRODUTO);
		}
		sql.append(sqlWhereClause.getSql());
    	Vector cdMarcadores = ultimoProdutoFilter != null ? ultimoProdutoFilter.cdMarcadores : null;
    	if (LavenderePdaConfig.filtraProdutoClienteRepresentante && ultimoProdutoFilter != null) {
        	sql.append(" AND (PRODCLIENTE.FLTIPORELACAO IS NOT NULL OR")
	    	.append(" NOT EXISTS ")
	    	.append("(SELECT 1 FROM TBLVPPRODUTOCLIENTE WHERE")
	    	.append(" CDEMPRESA = TB.CDEMPRESA AND ")
	    	.append(" CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = TB.CDPRODUTO AND ")
	    	.append(" CDCLIENTE <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente))
	    	.append(" AND FLTIPORELACAO = 'X'")
	    	.append("))");
        }
    	if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao && ValueUtil.isNotEmpty(cdMarcadores)) {
        	sql.append(" AND EXISTS (")
        	.append(" SELECT 1 FROM TBLVPMARCADOR M ")
    		.append(" JOIN TBLVPMARCADORPRODUTO MP ON ")
    		.append(" M.CDMARCADOR = MP.CDMARCADOR ")
    		.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE()")
    		.append(" AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE() ) AND ")
    		.append(" MP.CDEMPRESA = prod.CDEMPRESA AND ")
    		.append(" MP.CDREPRESENTANTE = prod.CDREPRESENTANTE AND ")
    		.append(" MP.CDPRODUTO = prod.CDPRODUTO AND ")
    		.append(" MP.CDMARCADOR IN (");
        	int size = cdMarcadores.size();
        	for (int i = 0; i < size; i++) {
        		sql.append(Sql.getValue(cdMarcadores.items[i])).append(",");
        	}
        	sql.setLength(sql.length() - 1);
    		sql.append("))");
        }
    	sql.append(" AND prod.DSAGRUPADORGRADE = ").append(Sql.getValue(filter.dsAgrupadorGrade));
    	if (ultimoProdutoFilter != null) {
    		if (ValueUtil.isNotEmpty(ultimoProdutoFilter.cdGrupoProduto1)) {
    			sql.append(" and prod.CDGRUPOPRODUTO1 = ").append(Sql.getValue(ultimoProdutoFilter.cdGrupoProduto1));
    		}
    		if (ValueUtil.isNotEmpty(ultimoProdutoFilter.cdGrupoProduto2)) {
    			sql.append(" and prod.CDGRUPOPRODUTO2 = ").append(Sql.getValue(ultimoProdutoFilter.cdGrupoProduto2));
    		}
    		if (ValueUtil.isNotEmpty(ultimoProdutoFilter.cdGrupoProduto3)) {
    			sql.append(" and prod.CDGRUPOPRODUTO3 = ").append(Sql.getValue(ultimoProdutoFilter.cdGrupoProduto3));
    		}
    		if (ValueUtil.isNotEmpty(ultimoProdutoFilter.cdGrupoProduto4)) {
    			sql.append(" and prod.CDGRUPOPRODUTO4 = ").append(Sql.getValue(ultimoProdutoFilter.cdGrupoProduto4));
    		}
    	}
    	return findAll(filter, sql.toString());
    }
    
    public Vector findProdutoGradeAgrupadorGrade1(ProdutoGrade filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT tb.CDITEMGRADE1, tb.CDITEMGRADE2, tb.CDITEMGRADE3, tb.CDPRODUTO, MARCADOR.CDMARCADOR FROM TBLVPPRODUTOGRADE tb")
    	.append(" JOIN TBLVPPRODUTO prod ON ")
    	.append(" prod.CDEMPRESA = tb.CDEMPRESA AND ")
    	.append(" prod.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
    	.append(" prod.CDPRODUTO = tb.CDPRODUTO ")
    	.append(" LEFT JOIN (")
		.append(" SELECT MP.CDEMPRESA, MP.CDREPRESENTANTE, MP.CDPRODUTO, MP.CDMARCADOR FROM TBLVPMARCADOR M ")
		.append(" JOIN TBLVPMARCADORPRODUTO MP ON ")
		.append(" M.CDMARCADOR = MP.CDMARCADOR ")
		.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE()")
		.append(" AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE() )")
		.append(" AND M.FLAGRUPADORGRADE = 'S'")
		.append(") MARCADOR ON ")
		.append(" MARCADOR.CDEMPRESA = TB.CDEMPRESA AND ")
		.append(" MARCADOR.CDREPRESENTANTE = TB.CDREPRESENTANTE AND ")
		.append(" MARCADOR.CDPRODUTO = TB.CDPRODUTO");
    	addWhereByExample(filter, sql);
    	sql.append(" AND prod.DSAGRUPADORGRADE = ").append(Sql.getValue(filter.dsAgrupadorGrade));
    	Vector list = new Vector();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			list.addElement(populateProdutoAgrupadorGrade(rs));
    		}
    		return list;
    	}
    }
    
    private ProdutoGrade populateProdutoAgrupadorGrade(ResultSet rs) throws SQLException {
    	ProdutoGrade produtoGrade = new ProdutoGrade();
    	produtoGrade.cdItemGrade1 = rs.getString(1);
    	produtoGrade.cdItemGrade2 = rs.getString(2);
    	produtoGrade.cdItemGrade3 = rs.getString(3);
    	produtoGrade.cdProduto = rs.getString(4);
    	produtoGrade.cdMarcador = rs.getString(5);
    	return produtoGrade;
    }
    
    public void addWhereProdutoGrade5(ProdutoBase filter, SqlWhereClause sqlWhereClause, String alias) {
    	if (LavenderePdaConfig.usaFiltroFornecedor) {
    		sqlWhereClause.addAndConditionEquals(alias + ".CDFORNECEDOR", filter.cdFornecedor);
    	}
    	if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(filter.cdTipoPedidoFilter)) {
    		DaoUtil.addAndFlExcecaoProdutoTipoPedCondition(filter, sqlWhereClause);
    	}
    	if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto() && ValueUtil.isNotEmpty(filter.dsAplicacaoProduto)) {
    		//addOrLikeCondition(alias + "DSAPLICACAOPRODUTO", produto.dsAplicacaoProduto, false);
		}
    }
    
    private void addFilterStatusEstoque(SqlWhereClause sqlWhereClause, String cdStatusEstoque) {
		boolean usaMultiplicaCondicaoEstoquePrevisto = false;
		sqlWhereClause.addStartAndMultipleCondition();
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_1) ) {
			sqlWhereClause.addOrCondition("ifnull("+DaoUtil.ALIAS_ESTOQUE_ERP+".QTESTOQUE, 0) - "+"ifnull("+DaoUtil.ALIAS_ESTOQUE_PDA+".QTESTOQUE,0) > 0");
			usaMultiplicaCondicaoEstoquePrevisto = true;
		}
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_2)) {
			sqlWhereClause.addOrCondition("ifnull("+DaoUtil.ALIAS_ESTOQUE_ERP+".QTESTOQUE, 0) - "+"ifnull("+DaoUtil.ALIAS_ESTOQUE_PDA+".QTESTOQUE,0) <= 0");
			usaMultiplicaCondicaoEstoquePrevisto = true;
		} 
		if (cdStatusEstoque.contains(EstoqueDisponivel.ESTOQUE_DISPONIVEL_COMBO_OPCAO_3)) {
			if (usaMultiplicaCondicaoEstoquePrevisto) sqlWhereClause.addStartOrMultipleCondition();	
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".QTESTOQUEPREVISTO > 0");
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".DTESTOQUEPREVISTO IS NOT NULL");
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".DTESTOQUEPREVISTO >= " , DateUtil.getCurrentDate());
			if (usaMultiplicaCondicaoEstoquePrevisto) sqlWhereClause.addEndMultipleCondition();
		}
		sqlWhereClause.addEndMultipleCondition();
	}
    
}
