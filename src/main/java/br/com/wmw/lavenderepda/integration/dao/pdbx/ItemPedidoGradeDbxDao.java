package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ItemPedidoGradeDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemPedidoGrade();
	}

    private static ItemPedidoGradeDbxDao instance;
    private static ItemPedidoGradeDbxDao instanceErp;

    public ItemPedidoGradeDbxDao() {
        super(ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADE);
    }

    public ItemPedidoGradeDbxDao(String newTableName) {
    	super(newTableName);
    }
    
    public static ItemPedidoGradeDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemPedidoGradeDbxDao(ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADE);
        }
        return instance;
    }

    public static ItemPedidoGradeDbxDao getInstanceErp() {
    	if (instanceErp == null) {
    		instanceErp = new ItemPedidoGradeDbxDao(ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADEERP);
    	}
    	return instanceErp;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemPedidoGrade itemPedidoGrade = new ItemPedidoGrade();
        itemPedidoGrade.rowKey = rs.getString("rowkey");
        itemPedidoGrade.cdEmpresa = rs.getString("cdEmpresa");
        itemPedidoGrade.cdRepresentante = rs.getString("cdRepresentante");
        itemPedidoGrade.flOrigemPedido = rs.getString("flOrigemPedido");
        itemPedidoGrade.nuPedido = rs.getString("nuPedido");
        itemPedidoGrade.cdProduto = rs.getString("cdProduto");
        itemPedidoGrade.flTipoItemPedido = rs.getString("flTipoItemPedido");
        itemPedidoGrade.nuSeqProduto = rs.getInt("nuSeqProduto");
        itemPedidoGrade.cdItemGrade1 = rs.getString("cdItemGrade1");
        itemPedidoGrade.cdItemGrade2 = rs.getString("cdItemGrade2");
        itemPedidoGrade.cdItemGrade3 = rs.getString("cdItemGrade3");
        itemPedidoGrade.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
        itemPedidoGrade.nuCarimbo = rs.getInt("nuCarimbo");
        itemPedidoGrade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemPedidoGrade.cdUsuario = rs.getString("cdUsuario");
        return itemPedidoGrade;
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
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
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
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
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
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" QTITEMFISICO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) domain;
        sql.append(Sql.getValue(itemPedidoGrade.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.nuPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.cdProduto)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.cdItemGrade1)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.cdItemGrade2)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.cdItemGrade3)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.qtItemFisico)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(itemPedidoGrade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) domain;
        sql.append(" QTITEMFISICO = ").append(Sql.getValue(itemPedidoGrade.qtItemFisico)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(itemPedidoGrade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemPedidoGrade.flTipoAlteracao)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(itemPedidoGrade.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemPedidoGrade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemPedidoGrade.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemPedidoGrade.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemPedidoGrade.nuPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", itemPedidoGrade.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", itemPedidoGrade.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", itemPedidoGrade.nuSeqProduto);
		sqlWhereClause.addAndCondition("CDITEMGRADE1 = ", itemPedidoGrade.cdItemGrade1);
		sqlWhereClause.addAndCondition("CDITEMGRADE2 = ", itemPedidoGrade.cdItemGrade2);
		sqlWhereClause.addAndCondition("CDITEMGRADE3 = ", itemPedidoGrade.cdItemGrade3);
		if (itemPedidoGrade.filtraItemPedGradeNivel3) {
			sqlWhereClause.addAndCondition("CDITEMGRADE3 IS NOT NULL ");
		}
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
    		return getInstanceErp().findByRowKeyErp(rowKey);
    	}
		return super.findByRowKey(rowKey);
	}

	private BaseDomain findByRowKeyErp(String rowKey) throws java.sql.SQLException {
		return super.findByRowKey(rowKey);
	}

	//@Override
	public Vector findAll() throws java.sql.SQLException {
		return VectorUtil.concatVectors(findAllSimple(), getInstanceErp().findAllSimple());
	}

	public Vector findAllSimple() throws java.sql.SQLException {
		return super.findAll();
	}

	//@Override
	public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExample(domain), getInstanceErp().findAllByExampleUnique(domain));
	}

	public Vector findAllByExampleUnique(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExample(domain);
	}

	//@Override
	public Vector findAllByExampleSummary(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleSummary(domain), getInstanceErp().findAllByExampleSummaryUnique(domain));
	}

	public Vector findAllByExampleSummaryUnique(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleSummary(domain);
	}

	//@Override
	public Vector findAllInCache() throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllInCache(), getInstanceErp().findAllInCacheErp());
	}

	private Vector findAllInCacheErp() throws SQLException {
		return super.findAllInCache();
	}

	//@Override
	public Vector findAllByExampleInCache(BaseDomain domain) throws java.sql.SQLException {
		return VectorUtil.concatVectors(super.findAllByExampleInCache(domain), getInstanceErp().findAllByExampleInCacheErp(domain));
	}

	private Vector findAllByExampleInCacheErp(BaseDomain domain) throws java.sql.SQLException {
		return super.findAllByExampleInCache(domain);
	}

	//@Override
	public void delete(BaseDomain domain) throws java.sql.SQLException {
		ItemPedidoGrade itemPedido = (ItemPedidoGrade) domain;
    	if (!OrigemPedido.FLORIGEMPEDIDO_PDA.equals(itemPedido.flOrigemPedido)) {
    		getInstanceErp().deleteErp(domain);
    	} else {
        	super.delete(domain);
    	}
	}

	private void deleteErp(BaseDomain domain) throws SQLException {
    	super.delete(domain);
    }

	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
	
	@Override
	public void deleteAllByExample(BaseDomain domain) throws SQLException {
		ItemPedidoGrade itemPedido = (ItemPedidoGrade) domain;
		if (!OrigemPedido.FLORIGEMPEDIDO_PDA.equals(itemPedido.flOrigemPedido)) {
			getInstanceErp().deleteAllByExample(domain);
		} else {
			super.deleteAllByExample(domain);
		}
	}
	
	public Vector verificaInconsistenciasGrade(ItemPedido itemPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT itp.* FROM TBLVPITEMPEDIDOGRADE itp");
		sql.append(" JOIN TBLVPPRODUTOGRADE pg ON itp.CDEMPRESA = pg.CDEMPRESA AND itp.CDREPRESENTANTE = pg.CDREPRESENTANTE AND itp.CDPRODUTO = pg.CDPRODUTO");
		sql.append(" AND itp.CDITEMGRADE1 = pg.CDITEMGRADE1 and itp.CDITEMGRADE2 = pg.CDITEMGRADE2 and itp.CDITEMGRADE3 = pg.CDITEMGRADE3");
		sql.append(" WHERE itp.CDEMPRESA = '" + itemPedido.cdEmpresa + "' and itp.CDREPRESENTANTE = '" + itemPedido.cdRepresentante + "' and itp.NUPEDIDO = '" + itemPedido.nuPedido + "'");
		if (itemPedido.cdProduto != null) {
			sql.append(" and itp.CDPRODUTO = '" + itemPedido.cdProduto + "'");
		}
		sql.append(" AND (itp.CDITEMGRADE1 like '" + ValueUtil.VALOR_ZERO + "' OR EXISTS (SELECT CDITEMGRADE FROM TBLVPITEMGRADE ig WHERE itp.CDITEMGRADE1 = ig.CDITEMGRADE))");
		sql.append(" AND (itp.CDITEMGRADE2 like '" + ValueUtil.VALOR_ZERO + "' OR EXISTS (SELECT CDITEMGRADE FROM TBLVPITEMGRADE ig WHERE itp.CDITEMGRADE2 = ig.CDITEMGRADE))");
		sql.append(" AND (itp.CDITEMGRADE3 like '" + ValueUtil.VALOR_ZERO + "' OR EXISTS (SELECT CDITEMGRADE FROM TBLVPITEMGRADE ig WHERE itp.CDITEMGRADE3 = ig.CDITEMGRADE));");
		Vector resultados = new Vector(50);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				resultados.addElement(populate(itemPedido, rs));
			}
		}
		return resultados;
	}
}