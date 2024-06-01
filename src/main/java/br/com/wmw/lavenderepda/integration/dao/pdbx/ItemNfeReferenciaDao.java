package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import totalcross.sql.ResultSet;

public class ItemNfeReferenciaDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemNfeReferencia();
	}

    private static ItemNfeReferenciaDao instance;
	public static boolean houveRecebimentoItemNfeReferenciaBackground;
	public static String erroOcorridoAtualizacao;

    public ItemNfeReferenciaDao() {
        super(ItemNfeReferencia.TABLE_NAME);
    }
    
    public static ItemNfeReferenciaDao getInstance() {
        if (instance == null) {
            instance = new ItemNfeReferenciaDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	ItemNfeReferencia itemNfeReferencia = new ItemNfeReferencia();
        itemNfeReferencia.rowKey = rs.getString("rowkey");
        itemNfeReferencia.cdEmpresa = rs.getString("cdEmpresa");
        itemNfeReferencia.cdRepresentante = rs.getString("cdRepresentante");
        itemNfeReferencia.nuPedido = rs.getString("nuPedido");
        itemNfeReferencia.flOrigemPedido = rs.getString("flOrigemPedido");
        itemNfeReferencia.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemNfeReferencia.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemNfeReferencia.cdTributacaoConfig = rs.getString("cdTributacaoConfig");
        itemNfeReferencia.cdReferencia = rs.getString("cdReferencia");
        itemNfeReferencia.dsReferencia = rs.getString("dsReferencia");
        itemNfeReferencia.cdCfop = rs.getString("cdCfop");
        itemNfeReferencia.cdClassificFiscal = rs.getString("cdClassificFiscal");
        itemNfeReferencia.cdUnidade = rs.getString("cdUnidade");
        itemNfeReferencia.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
        itemNfeReferencia.vlBaseItemTabelaPreco = ValueUtil.round(rs.getDouble("vlBaseItemTabelaPreco"));
        itemNfeReferencia.vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
        itemNfeReferencia.vlTotalIcmsItem = ValueUtil.round(rs.getDouble("vlTotalIcmsItem"));
        itemNfeReferencia.vlPctIcms = ValueUtil.round(rs.getDouble("vlPctIcms"));
        itemNfeReferencia.vlTotalItemPedido = ValueUtil.round(rs.getDouble("vlTotalItemPedido"));
        itemNfeReferencia.nuCarimbo = rs.getInt("nuCarimbo");
        itemNfeReferencia.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemNfeReferencia.cdUsuario = rs.getString("cdUsuario");
        return itemNfeReferencia;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDTRIBUTACAOCONFIG,");
        sql.append(" CDREFERENCIA,");
        sql.append(" DSREFERENCIA,");
        sql.append(" CDCFOP,");
        sql.append(" CDCLASSIFICFISCAL,");
        sql.append(" CDUNIDADE,");
        sql.append(" QTITEMFISICO,");
        sql.append(" VLBASEITEMTABELAPRECO,");
        sql.append(" VLITEMPEDIDO,");
        sql.append(" VLTOTALICMSITEM,");
        sql.append(" VLPCTICMS,");
        sql.append(" VLTOTALITEMPEDIDO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
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
         sql.append(" FLTIPOITEMPEDIDO,");
         sql.append(" NUSEQPRODUTO,");
         sql.append(" CDTRIBUTACAOCONFIG,");
         sql.append(" CDREFERENCIA,");
         sql.append(" DSREFERENCIA,");
         sql.append(" CDCFOP,");
         sql.append(" CDCLASSIFICFISCAL,");
         sql.append(" CDUNIDADE,");
         sql.append(" QTITEMFISICO,");
         sql.append(" VLBASEITEMTABELAPRECO,");
         sql.append(" VLITEMPEDIDO,");
         sql.append(" VLTOTALICMSITEM,");
         sql.append(" VLPCTICMS,");
         sql.append(" VLTOTALITEMPEDIDO,");
         sql.append(" NUCARIMBO,");
         sql.append(" FLTIPOALTERACAO,");
         sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ItemNfeReferencia itemNfeReferencia = (ItemNfeReferencia) domain;
        sql.append(Sql.getValue(itemNfeReferencia.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.nuPedido)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.cdTributacaoConfig)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.cdReferencia)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.dsReferencia)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.cdCfop)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.cdClassificFiscal)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.cdUnidade)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.qtItemFisico)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.vlBaseItemTabelaPreco)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.vlItemPedido)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.vlTotalIcmsItem)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.vlPctIcms)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.vlTotalItemPedido)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(itemNfeReferencia.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ItemNfeReferencia itemNfeReferencia = (ItemNfeReferencia) domain;
        sql.append(" DSREFERENCIA = ").append(Sql.getValue(itemNfeReferencia.dsReferencia)).append(",");
        sql.append(" CDCFOP = ").append(Sql.getValue(itemNfeReferencia.cdCfop)).append(",");
        sql.append(" CDCLASSIFICFISCAL = ").append(Sql.getValue(itemNfeReferencia.cdClassificFiscal)).append(",");
        sql.append(" CDUNIDADE = ").append(Sql.getValue(itemNfeReferencia.cdUnidade)).append(",");
        sql.append(" QTITEMFISICO = ").append(Sql.getValue(itemNfeReferencia.qtItemFisico)).append(",");
        sql.append(" VLBASEITEMTABELAPRECO = ").append(Sql.getValue(itemNfeReferencia.vlBaseItemTabelaPreco)).append(",");
        sql.append(" VLITEMPEDIDO = ").append(Sql.getValue(itemNfeReferencia.vlItemPedido)).append(",");
        sql.append(" VLTOTALICMSITEM = ").append(Sql.getValue(itemNfeReferencia.vlTotalIcmsItem)).append(",");
        sql.append(" VLPCTICMS = ").append(Sql.getValue(itemNfeReferencia.vlPctIcms)).append(",");
        sql.append(" VLTOTALITEMPEDIDO = ").append(Sql.getValue(itemNfeReferencia.vlTotalItemPedido)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(itemNfeReferencia.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemNfeReferencia.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemNfeReferencia.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemNfeReferencia itemNfeReferencia = (ItemNfeReferencia) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemNfeReferencia.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemNfeReferencia.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemNfeReferencia.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemNfeReferencia.flOrigemPedido);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", itemNfeReferencia.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemNfeReferencia.nuSeqProduto);
		sqlWhereClause.addAndCondition("CDTRIBUTACAOCONFIG = ", itemNfeReferencia.cdTributacaoConfig);
		sqlWhereClause.addAndCondition("CDREFERENCIA = ", itemNfeReferencia.cdReferencia);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}