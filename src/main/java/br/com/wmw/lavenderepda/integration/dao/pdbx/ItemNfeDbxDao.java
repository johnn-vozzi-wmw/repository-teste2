package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class ItemNfeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemNfe();
	}

    private static ItemNfeDbxDao instance;
	public static boolean houveRecebimentoItemNfeBackground;
	public static String erroOcorridoAtualizacao;

    public ItemNfeDbxDao() {
        super(ItemNfe.TABLE_NAME);
    }
    
    public static ItemNfeDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemNfeDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemNfe itemNfe = new ItemNfe();
        itemNfe.rowKey = rs.getString("rowkey");
        itemNfe.cdEmpresa = rs.getString("cdEmpresa");
        itemNfe.cdRepresentante = rs.getString("cdRepresentante");
        itemNfe.nuPedido = rs.getString("nuPedido");
        itemNfe.flOrigemPedido = rs.getString("flOrigemPedido");
        itemNfe.cdProduto = rs.getString("cdProduto");
        itemNfe.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemNfe.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemNfe.cdItemGrade1 = rs.getString("cdItemGrade1");
        itemNfe.cdItemGrade2 = rs.getString("cdItemGrade2");
        itemNfe.cdItemGrade3 = rs.getString("cdItemGrade3");
        itemNfe.cdClassificFiscal = rs.getString("cdClassificFiscal");
        itemNfe.cdUnidade = rs.getString("cdUnidade");
        itemNfe.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
        itemNfe.vlBaseItemTabelaPreco = ValueUtil.round(rs.getDouble("vlBaseItemTabelaPreco"));
        itemNfe.vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
        itemNfe.vlTotalIcmsItem = ValueUtil.round(rs.getDouble("vlTotalIcmsItem"));
        itemNfe.vlPctIcms = ValueUtil.round(rs.getDouble("vlPctIcms"));
        itemNfe.vlTotalItemPedido = ValueUtil.round(rs.getDouble("vlTotalItemPedido"));
        itemNfe.vlTotalStItem = ValueUtil.round(rs.getDouble("vlTotalStItem"));
        itemNfe.flCigarro = rs.getString("flCigarro");
        itemNfe.nuCfopProduto = rs.getString("nuCfopProduto");
        itemNfe.dsNcmProduto = rs.getString("dsNcmProduto");
        itemNfe.vlTotalBaseIcmsItem = rs.getDouble("vlTotalBaseIcmsItem");
        itemNfe.vlTotalBaseStItem = rs.getDouble("vlTotalBaseStItem");
        itemNfe.vlDespesaAcessoria = rs.getDouble("vlDespesaAcessoria");
        itemNfe.nuCarimbo = rs.getInt("nuCarimbo");
        itemNfe.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemNfe.cdUsuario = rs.getString("cdUsuario");
        itemNfe.vlPctCofins = rs.getDouble("vlPctCofins");
        itemNfe.vlPctPis = rs.getDouble("vlPctPis");
        itemNfe.qtPeso = rs.getDouble("qtPeso");
        itemNfe.dsCestProduto = rs.getString("dsCestProduto");
        itemNfe.qtMultiploEmbalagem = rs.getDouble("qtMultiploEmbalagem");
        itemNfe.cdOrigemMercadoria = rs.getString("cdOrigemMercadoria");
        itemNfe.vlPctReducaoBaseIcms = rs.getDouble("vlPctReducaoBaseIcms");
        itemNfe.vlPctReducaoIcms = rs.getDouble("vlPctReducaoIcms");
        itemNfe.vlPctFecopRecolher = rs.getDouble("vlPctFecopRecolher");
        itemNfe.cdBeneficioFiscal = rs.getString("cdBeneficioFiscal");
        return itemNfe;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" TB.rowkey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.NUPEDIDO,");
        sql.append(" TB.FLORIGEMPEDIDO,");
        sql.append(" TB.CDPRODUTO,");
        sql.append(" TB.FLTIPOITEMPEDIDO,");
        sql.append(" TB.NUSEQPRODUTO,");
        sql.append(" TB.CDITEMGRADE1,");
        sql.append(" TB.CDITEMGRADE2,");
        sql.append(" TB.CDITEMGRADE3,");
        sql.append(" TB.CDCLASSIFICFISCAL,");
        sql.append(" TB.CDUNIDADE,");
        sql.append(" TB.QTITEMFISICO,");
        sql.append(" TB.VLBASEITEMTABELAPRECO,");
        sql.append(" TB.VLITEMPEDIDO,");
        sql.append(" TB.VLTOTALICMSITEM,");
        sql.append(" TB.VLPCTICMS,");
        sql.append(" TB.VLTOTALITEMPEDIDO,");
        sql.append(" TB.VLTOTALSTITEM,");
        sql.append(" TB.FLCIGARRO,");
        sql.append(" TB.DSNCMPRODUTO,");
        sql.append(" TB.NUCFOPPRODUTO,");
        sql.append(" TB.VLTOTALBASEICMSITEM,");
        sql.append(" TB.VLTOTALBASESTITEM,");
        sql.append(" TB.VLDESPESAACESSORIA,");
        sql.append(" TB.NUCARIMBO,");
        sql.append(" TB.FLTIPOALTERACAO,");
        sql.append(" TB.CDUSUARIO,");
        sql.append(" TB.VLPCTPIS,");
        sql.append(" TB.VLPCTCOFINS,");
        sql.append(" TB.QTPESO,");
        sql.append(" TB.DSCESTPRODUTO,");
        sql.append(" TB.QTMULTIPLOEMBALAGEM,");
        sql.append(" TB.CDORIGEMMERCADORIA,");
        sql.append(" TB.VLPCTREDUCAOBASEICMS,");
        sql.append(" TB.VLPCTREDUCAOICMS,");
        sql.append(" TB.VLPCTFECOPRECOLHER,");
        sql.append(" TB.CDBENEFICIOFISCAL");
		if (LavenderePdaConfig.isAgrupadaItensInclusaoConfigImpressaoNfeViaBluetooth()) {
			sql.append(" ,GRUPO.CDGRUPOPRODUTO1");
		}
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" CDCLASSIFICFISCAL,");
        sql.append(" CDUNIDADE,");
        sql.append(" QTITEMFISICO,");
        sql.append(" VLBASEITEMTABELAPRECO,");
        sql.append(" VLITEMPEDIDO,");
        sql.append(" VLTOTALICMSITEM,");
        sql.append(" VLPCTICMS,");
        sql.append(" VLTOTALITEMPEDIDO,");
        sql.append(" VLTOTALSTITEM,");
        sql.append(" FLCIGARRO,");
        sql.append(" DSNCMPRODUTO,");
        sql.append(" NUCFOPPRODUTO,");
        sql.append(" VLTOTALBASESTITEM,");
        sql.append(" VLTOTALBASEICMSITEM,");
        sql.append(" VLDESPESAACESSORIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO," );
        sql.append(" VLPCTPIS,");
        sql.append(" VLPCTCOFINS,");
        sql.append(" QTPESO,");
        sql.append(" DSCESTPRODUTO,");
	    sql.append(" QTMULTIPLOEMBALAGEM,");
	    sql.append(" CDORIGEMMERCADORIA,");
        sql.append(" VLPCTREDUCAOBASEICMS,");
        sql.append(" VLPCTREDUCAOICMS,");
        sql.append(" VLPCTFECOPRECOLHER,");
        sql.append(" CDBENEFICIOFISCAL");
    }

	//@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		ItemNfe itemNfe = (ItemNfe) domain;
		sql.append(Sql.getValue(itemNfe.cdEmpresa)).append(",");
		sql.append(Sql.getValue(itemNfe.cdRepresentante)).append(",");
		sql.append(Sql.getValue(itemNfe.nuPedido)).append(",");
		sql.append(Sql.getValue(itemNfe.flOrigemPedido)).append(",");
		sql.append(Sql.getValue(itemNfe.cdProduto)).append(",");
		sql.append(Sql.getValue(itemNfe.flTipoItemPedido)).append(",");
		sql.append(Sql.getValue(itemNfe.nuSeqProduto)).append(",");
		sql.append(Sql.getValue(itemNfe.cdItemGrade1)).append(",");
		sql.append(Sql.getValue(itemNfe.cdItemGrade2)).append(",");
		sql.append(Sql.getValue(itemNfe.cdItemGrade3)).append(",");
		sql.append(Sql.getValue(itemNfe.cdClassificFiscal)).append(",");
		sql.append(Sql.getValue(itemNfe.cdUnidade)).append(",");
		sql.append(Sql.getValue(itemNfe.qtItemFisico)).append(",");
		sql.append(Sql.getValue(itemNfe.vlBaseItemTabelaPreco)).append(",");
		sql.append(Sql.getValue(itemNfe.vlItemPedido)).append(",");
		sql.append(Sql.getValue(itemNfe.vlTotalIcmsItem)).append(",");
		sql.append(Sql.getValue(itemNfe.vlPctIcms)).append(",");
		sql.append(Sql.getValue(itemNfe.vlTotalItemPedido)).append(",");
		sql.append(Sql.getValue(itemNfe.vlTotalStItem)).append(",");
		sql.append(Sql.getValue(itemNfe.flCigarro)).append(",");
		sql.append(Sql.getValue(itemNfe.dsNcmProduto)).append(",");
		sql.append(Sql.getValue(itemNfe.nuCfopProduto)).append(",");
		sql.append(Sql.getValue(itemNfe.vlTotalBaseStItem)).append(",");
		sql.append(Sql.getValue(itemNfe.vlTotalBaseIcmsItem)).append(",");
		sql.append(Sql.getValue(itemNfe.vlDespesaAcessoria)).append(",");
		sql.append(Sql.getValue(itemNfe.nuCarimbo)).append(",");
		sql.append(Sql.getValue(itemNfe.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(itemNfe.cdUsuario)).append(",");
		sql.append(Sql.getValue(itemNfe.vlPctPis)).append(",");
		sql.append(Sql.getValue(itemNfe.vlPctCofins)).append(",");
		sql.append(Sql.getValue(itemNfe.qtPeso)).append(",");
		sql.append(Sql.getValue(itemNfe.dsCestProduto)).append(",");
		sql.append(Sql.getValue(itemNfe.qtMultiploEmbalagem)).append(",");
		sql.append(Sql.getValue(itemNfe.cdOrigemMercadoria)).append(",");
		sql.append(Sql.getValue(itemNfe.vlPctReducaoBaseIcms)).append(",");
		sql.append(Sql.getValue(itemNfe.vlPctReducaoIcms)).append(",");
		sql.append(Sql.getValue(itemNfe.vlPctFecopRecolher)).append(",");
		sql.append(Sql.getValue(itemNfe.cdBeneficioFiscal));
	}

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ItemNfe itemNfe = (ItemNfe) domain;
        sql.append(" TB.CDCLASSIFICFISCAL = ").append(Sql.getValue(itemNfe.cdClassificFiscal)).append(",");
        sql.append(" TB.CDUNIDADE = ").append(Sql.getValue(itemNfe.cdUnidade)).append(",");
        sql.append(" TB.QTITEMFISICO = ").append(Sql.getValue(itemNfe.qtItemFisico)).append(",");
        sql.append(" TB.VLBASEITEMTABELAPRECO = ").append(Sql.getValue(itemNfe.vlBaseItemTabelaPreco)).append(",");
        sql.append(" TB.VLITEMPEDIDO = ").append(Sql.getValue(itemNfe.vlItemPedido)).append(",");
        sql.append(" TB.VLTOTALICMSITEM = ").append(Sql.getValue(itemNfe.vlTotalIcmsItem)).append(",");
        sql.append(" TB.VLPCTICMS = ").append(Sql.getValue(itemNfe.vlPctIcms)).append(",");
        sql.append(" TB.VLTOTALITEMPEDIDO = ").append(Sql.getValue(itemNfe.vlTotalItemPedido)).append(",");
        sql.append(" TB.VLTOTALSTITEM = ").append(Sql.getValue(itemNfe.vlTotalStItem)).append(",");
        sql.append(" TB.FLCIGARRO = ").append(Sql.getValue(itemNfe.flCigarro)).append(",");
        sql.append(" TB.DSNCMPRODUTO = ").append(Sql.getValue(itemNfe.dsNcmProduto)).append(",");
        sql.append(" TB.NUCFOPPRODUTO = ").append(Sql.getValue(itemNfe.nuCfopProduto)).append(",");
        sql.append(" TB.VLTOTALBASESTITEM = ").append(Sql.getValue(itemNfe.vlTotalBaseStItem)).append(",");
        sql.append(" TB.VLTOTALBASEICMSITEM = ").append(Sql.getValue(itemNfe.vlTotalBaseIcmsItem)).append(",");
        sql.append(" TB.VLDESPESAACESSORIA = ").append(Sql.getValue(itemNfe.vlDespesaAcessoria)).append(",");
        sql.append(" TB.NUCARIMBO = ").append(Sql.getValue(itemNfe.nuCarimbo)).append(",");
        sql.append(" TB.FLTIPOALTERACAO = ").append(Sql.getValue(itemNfe.flTipoAlteracao)).append(",");
        sql.append(" TB.CDUSUARIO = ").append(Sql.getValue(itemNfe.cdUsuario)).append(",");
        sql.append(" TB.DSCESTPRODUTO = ").append(Sql.getValue(itemNfe.dsCestProduto)).append(",");
        sql.append(" TB.QTMULTIPLOEMBALAGEM = ").append(Sql.getValue(itemNfe.qtMultiploEmbalagem)).append(",");
        sql.append(" TB.CDORIGEMMERCADORIA = ").append(Sql.getValue(itemNfe.cdOrigemMercadoria)).append(",");
        sql.append(" TB.VLPCTREDUCAOBASEICMS = ").append(Sql.getValue(itemNfe.vlPctReducaoBaseIcms)).append(",");
        sql.append(" TB.VLPCTREDUCAOICMS = ").append(Sql.getValue(itemNfe.vlPctReducaoIcms)).append(",");
        sql.append(" TB.VLPCTFECOPRECOLHER = ").append(Sql.getValue(itemNfe.vlPctFecopRecolher)).append(",");
        sql.append(" TB.CDBENEFICIOFISCAL = ").append(Sql.getValue(itemNfe.cdBeneficioFiscal));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemNfe itemNfe = (ItemNfe) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", itemNfe.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", itemNfe.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.NUPEDIDO = ", itemNfe.nuPedido);
		sqlWhereClause.addAndCondition("TB.FLORIGEMPEDIDO = ", itemNfe.flOrigemPedido);
		sqlWhereClause.addAndCondition("TB.CDPRODUTO = ", itemNfe.cdProduto);
		sqlWhereClause.addAndCondition("TB.FLTIPOITEMPEDIDO = ", itemNfe.flTipoItemPedido);
		sqlWhereClause.addAndCondition("TB.NUSEQPRODUTO = ", itemNfe.nuSeqProduto);
		sqlWhereClause.addAndCondition("TB.CDITEMGRADE1 = ", itemNfe.cdItemGrade1);
		sqlWhereClause.addAndCondition("TB.CDITEMGRADE2 = ", itemNfe.cdItemGrade2);
		sqlWhereClause.addAndCondition("TB.CDITEMGRADE3 = ", itemNfe.cdItemGrade3);
		if (itemNfe.filtraPorNaoEnviadoServidor) {
			sqlWhereClause.addAndCondition("TB.FLTIPOALTERACAO  <> ''");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemNfe itemNfe = (ItemNfe) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemNfe.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemNfe.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemNfe.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemNfe.flOrigemPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemNfe.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", itemNfe.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemNfe.nuSeqProduto);
		sqlWhereClause.addAndCondition("CDITEMGRADE1 = ", itemNfe.cdItemGrade1);
		sqlWhereClause.addAndCondition("CDITEMGRADE2 = ", itemNfe.cdItemGrade2);
		sqlWhereClause.addAndCondition("CDITEMGRADE3 = ", itemNfe.cdItemGrade3);
		if (itemNfe.filtraPorNaoEnviadoServidor) {
			sqlWhereClause.addAndCondition("FLTIPOALTERACAO  <> ''");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }    

	public void updateItemNfeTransmitidaComSucesso(ItemNfe itemNfeFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		addWhereByExample(itemNfeFilter, sql);
		executeUpdate(sql.toString());
		
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		if (LavenderePdaConfig.isAgrupadaItensInclusaoConfigImpressaoNfeViaBluetooth()) {
			DaoUtil.addJoinProduto(sql, DaoUtil.ALIAS_PRODUTO, true);
			sql.append(" LEFT JOIN TBLVPGRUPOPRODUTO1 GRUPO ON GRUPO.CDGRUPOPRODUTO1 = PROD.CDGRUPOPRODUTO1");
		} else {
			super.addJoin(domainFilter, sql);
		}
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		 ItemNfe itemNfe = (ItemNfe) domain;
		if (LavenderePdaConfig.isAgrupadaItensInclusaoConfigImpressaoNfeViaBluetooth() && itemNfe.comJoinExampleRS) {
			sql.append("ORDER BY GRUPO.CDGRUPOPRODUTO1, TB.CDPRODUTO");
		} else {
			super.addOrderBy(sql, domain);
		}			
	}
    
}