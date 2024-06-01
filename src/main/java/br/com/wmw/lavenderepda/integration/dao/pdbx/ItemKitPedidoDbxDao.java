package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;


public class ItemKitPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemKitPedido();
	}

    private static ItemKitPedidoDbxDao instance;
    private static ItemKitPedidoDbxDao instanceErp;

    public ItemKitPedidoDbxDao() {
        super(ItemKitPedido.TABLE_NAME_ITEMKITPEDIDO);
    }

    public ItemKitPedidoDbxDao(String newTableName) {
    	super(newTableName);
    }

    public static ItemKitPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemKitPedidoDbxDao(ItemKitPedido.TABLE_NAME_ITEMKITPEDIDO);
        }
        return instance;
    }

    public static ItemKitPedidoDbxDao getInstanceErp() {
        if (instanceErp == null) {
            instanceErp = new ItemKitPedidoDbxDao(ItemKitPedido.TABLE_NAME_ITEMKITPEDIDOERP);
        }
        return instanceErp;
    }


    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemKitPedido itemKitPedido = new ItemKitPedido();
        itemKitPedido.rowKey = rs.getString("rowkey");
        itemKitPedido.cdEmpresa = rs.getString("cdEmpresa");
        itemKitPedido.cdRepresentante = rs.getString("cdRepresentante");
        itemKitPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        itemKitPedido.nuPedido = rs.getString("nuPedido");
        itemKitPedido.cdKit = rs.getString("cdKit");
        itemKitPedido.cdProduto = rs.getString("cdProduto");
        itemKitPedido.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemKitPedido.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemKitPedido.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
        itemKitPedido.nuCarimbo = rs.getInt("nuCarimbo");
        itemKitPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemKitPedido.cdUsuario = rs.getString("cdUsuario");
        return itemKitPedido;
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDKIT,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addCacheColumns(StringBuffer sql) {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDKIT,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDKIT,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemKitPedido itemKitPedido = (ItemKitPedido) domain;
        sql.append(Sql.getValue(itemKitPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemKitPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemKitPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemKitPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(itemKitPedido.cdKit)).append(",");
        sql.append(Sql.getValue(itemKitPedido.cdProduto)).append(",");
        sql.append(Sql.getValue(itemKitPedido.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(itemKitPedido.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(itemKitPedido.qtItemFisico)).append(",");
        sql.append(Sql.getValue(itemKitPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemKitPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(itemKitPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemKitPedido itemKitPedido = (ItemKitPedido) domain;
        sql.append(" QTITEMFISICO = ").append(Sql.getValue(itemKitPedido.qtItemFisico)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemKitPedido.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(itemKitPedido.cdUsuario));
    }

    //@Override
    protected void addWhereKey(BaseDomain domain, StringBuffer sql) {
        ItemKitPedido itemKitPedido = (ItemKitPedido) domain;
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(itemKitPedido.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(itemKitPedido.cdRepresentante));
        sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(itemKitPedido.flOrigemPedido));
        sql.append(" and NUPEDIDO = ").append(Sql.getValue(itemKitPedido.nuPedido));
        sql.append(" and CDKIT = ").append(Sql.getValue(itemKitPedido.cdKit));
        sql.append(" and CDPRODUTO = ").append(Sql.getValue(itemKitPedido.cdProduto));
        sql.append(" and FLTIPOITEMPEDIDO = ").append(Sql.getValue(itemKitPedido.flTipoItemPedido));
        sql.append(" and NUSEQPRODUTO = ").append(Sql.getValue(itemKitPedido.nuSeqProduto));

    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemKitPedido itemKitPedido = (ItemKitPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemKitPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemKitPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemKitPedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemKitPedido.nuPedido);
		sqlWhereClause.addAndCondition("CDKIT = ", itemKitPedido.cdKit);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemKitPedido.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", itemKitPedido.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemKitPedido.nuSeqProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    //-----------------------------------------------------------------------------------------
    // Todos os métodos de busca sobrescritos, para pegar info das tabelas ItemPedido e ItemPedidoErp
    //-----------------------------------------------------------------------------------------

	//@Override
	public BaseDomain findByRowKey(String rowKey) throws java.sql.SQLException {
		StringBuffer strBuffer = getSqlBuffer();
		strBuffer.append(";").append(OrigemPedido.FLORIGEMPEDIDO_PDA).append(";");
    	if (rowKey.indexOf(strBuffer.toString()) == -1) {
    		return ItemKitPedidoDbxDao.getInstanceErp().findByRowKeyErp(rowKey);
    	}
		return super.findByRowKey(rowKey);
	}

	private BaseDomain findByRowKeyErp(String rowKey) throws java.sql.SQLException {
		return super.findByRowKey(rowKey);
	}

	//@Override
	public Vector findAll() throws java.sql.SQLException {
		return VectorUtil.concatVectors(findAllSimple(),
				ItemKitPedidoDbxDao.getInstanceErp().findAllSimple());
	}

	public Vector findAllSimple() throws java.sql.SQLException {
		return super.findAll();
	}

	//@Override
	public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExample(domain),
				ItemKitPedidoDbxDao.getInstanceErp().findAllByExampleUnique(domain));
	}

	public Vector findAllByExampleUnique(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExample(domain);
	}

	//@Override
	public Vector findAllByExampleSummary(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleSummary(domain),
				ItemKitPedidoDbxDao.getInstanceErp().findAllByExampleSummaryUnique(domain));
	}

	public Vector findAllByExampleSummaryUnique(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleSummary(domain);
	}

	//@Override
	public Vector findAllInCache() throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllInCache(),
				ItemKitPedidoDbxDao.getInstanceErp().findAllInCacheErp());
	}

	private Vector findAllInCacheErp() throws SQLException {
		return super.findAllInCache();
	}

	//@Override
	public Vector findAllByExampleInCache(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleInCache(domain),
				ItemKitPedidoDbxDao.getInstanceErp().findAllByExampleInCacheErp(domain));
	}

	private Vector findAllByExampleInCacheErp(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleInCache(domain);
	}

	//@Override
	public void delete(BaseDomain domain) throws java.sql.SQLException {
		ItemKitPedido itemPedido = (ItemKitPedido) domain;
    	if (!OrigemPedido.FLORIGEMPEDIDO_PDA.equals(itemPedido.flOrigemPedido)) {
    		ItemKitPedidoDbxDao.getInstanceErp().deleteErp(domain);
    	} else {
        	super.delete(domain);
    	}
	}

	private void deleteErp(BaseDomain domain) throws SQLException {
    	super.delete(domain);
    }
	
	public double findQtItemKitDescQtdSimilares(ItemKitPedido filter, String cdProdutoDesc, String cdProdutoAtual) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT SUM(QTITEMFISICO) FROM TBLVPITEMKITPEDIDO tb ");
		addWhereByExample(filter, sql);
		sql.append(" AND (EXISTS (")
		.append(" SELECT 1 FROM TBLVPDESCQTDEAGRSIMILAR descq JOIN TBLVPDESCQUANTIDADE desc ON ")
		.append(" descq.CDEMPRESA = desc.CDEMPRESA AND descq.CDREPRESENTANTE = desc.CDREPRESENTANTE AND ")
		.append(" descq.CDTABELAPRECO = desc.CDTABELAPRECO AND descq.CDPRODUTO = desc.CDPRODUTO AND ")
		.append(" desc.CDPRODUTO = ").append(Sql.getValue(cdProdutoDesc)).append(" AND ")
		.append(" desc.FLAGRUPADORSIMILARIDADE = 'S' ")
		.append(" WHERE descq.CDEMPRESA = tb.CDEMPRESA AND ")
		.append(" descq.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ");
		sql.append(" (descq.CDPRODUTO = tb.CDKIT OR descq.CDPRODUTOSIMILAR = tb.CDKIT)");
		if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
			sql.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN desc.DTINICIALVIGENCIA AND desc.DTFIMVIGENCIA");
		}
		sql.append(")")
		.append(" OR tb.CDKIT = ").append(Sql.getValue(cdProdutoAtual))
		.append(")");
		return getDouble(sql.toString());
	}


}