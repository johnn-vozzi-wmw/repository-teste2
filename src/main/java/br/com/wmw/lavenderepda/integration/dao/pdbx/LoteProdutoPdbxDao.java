package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Local;
import br.com.wmw.lavenderepda.business.domain.LoteProduto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class LoteProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new LoteProduto();
	}

    private static LoteProdutoPdbxDao instance;

    public LoteProdutoPdbxDao() {
        super(LoteProduto.TABLE_NAME);
    }

    public static LoteProdutoPdbxDao getInstance() {
        instance = new LoteProdutoPdbxDao();
        return instance;
    }

    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" TB.rowkey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.CDPRODUTO,");
        sql.append(" TB.CDLOTEPRODUTO,");
        sql.append(" TB.QTESTOQUEDISPONIVEL,");
        sql.append(" TB.QTESTOQUERESERVADO,");
        sql.append(" TB.DTVALIDADE,");
        sql.append(" TB.FLORIGEMESTOQUE,");
        sql.append(" TB.QTESTOQUE,");
        sql.append(" TB.CDLOCAL,");
        if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
        	sql.append(" LOCAL.DSLOCAL,");
        }
        if (LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco()) {
        	sql.append(" COUNT(TABPRECOLOTEPRODUTO.CDLOTEPRODUTO) AS QTTABPRECOLOTEPROD,");
        }
        sql.append(" TB.VLPCTVIDAUTILPRODUTO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        LoteProduto loteProduto = new LoteProduto();
        loteProduto.rowKey = rs.getString("rowkey");
        loteProduto.cdEmpresa = rs.getString("cdEmpresa");
        loteProduto.cdRepresentante = rs.getString("cdRepresentante");
        loteProduto.cdProduto = rs.getString("cdProduto");
        loteProduto.cdLoteproduto = rs.getString("cdLoteproduto");
        loteProduto.qtEstoquedisponivel = ValueUtil.round(rs.getDouble("qtEstoquedisponivel"));
        loteProduto.qtEstoquereservado = ValueUtil.round(rs.getDouble("qtEstoquereservado"));
        loteProduto.dtValidade = rs.getDate("dtValidade");
        loteProduto.flOrigemEstoque = rs.getString("flOrigemEstoque");
        loteProduto.qtEstoque = rs.getDouble("qtEstoque");
        loteProduto.cdLocal = rs.getString("cdLocal");
        if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
        	loteProduto.getLocal().cdLocal = loteProduto.cdLocal;
        	loteProduto.getLocal().dsLocal = rs.getString("dsLocal");
        }
        if (LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco()) {
        	loteProduto.qtTabPrecoLoteProd = rs.getInt("QTTABPRECOLOTEPROD");
    }
        loteProduto.vlPctvidautilproduto = ValueUtil.round(rs.getDouble("vlPctvidautilproduto"));
        return loteProduto;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        LoteProduto loteproduto = (LoteProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", loteproduto.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", loteproduto.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDPRODUTO = ", loteproduto.cdProduto);
		sqlWhereClause.addAndCondition("TB.CDLOTEPRODUTO = ", loteproduto.cdLoteproduto);
		sqlWhereClause.addAndCondition("TB.FLORIGEMESTOQUE = ", loteproduto.flOrigemEstoque);
		sqlWhereClause.addAndCondition("TB.CDLOCAl = ", loteproduto.cdLocal);
		if (LavenderePdaConfig.usaDescPromo && !LavenderePdaConfig.ignoraLocalSemDescPro && ValueUtil.isEmpty(loteproduto.cdLocal) && !loteproduto.ignoraLocal) {
			sqlWhereClause.addAndCondition("(TB.CDLOCAl = '' OR TB.CDLOCAl IS NULL) ");
		}
		sql.append(sqlWhereClause.getSql());
		agrupaPorTabPrecoLoteProd(sql,loteproduto.agrupaCdLoteProduto);
    }
    
    
    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDLOTEPRODUTO,");
        sql.append(" QTESTOQUEDISPONIVEL,");
        sql.append(" QTESTOQUERESERVADO,");
        sql.append(" DTVALIDADE,");
        sql.append(" VLPCTVIDAUTILPRODUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" QTESTOQUE,");
        sql.append(" FLORIGEMESTOQUE" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        LoteProduto loteProduto = (LoteProduto) domain;
        sql.append(Sql.getValue(loteProduto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(loteProduto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(loteProduto.cdProduto)).append(",");
        sql.append(Sql.getValue(loteProduto.cdLoteproduto)).append(",");
        sql.append(Sql.getValue(loteProduto.qtEstoquedisponivel)).append(",");
        sql.append(Sql.getValue(loteProduto.qtEstoquereservado)).append(",");
        sql.append(Sql.getValue(loteProduto.dtValidade)).append(",");
        sql.append(Sql.getValue(loteProduto.vlPctvidautilproduto)).append(",");
        sql.append(Sql.getValue(loteProduto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(loteProduto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(loteProduto.cdUsuario)).append(",");
        sql.append(Sql.getValue(loteProduto.qtEstoque)).append(",");
        sql.append(Sql.getValue(loteProduto.flOrigemEstoque));
    }
    
    public int updateEstoqueConsumidoPda(LoteProduto loteProdutoFilter) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("UPDATE ").append(tableName);
    	sql.append(" SET QTESTOQUE =  QTESTOQUE + ").append(Sql.getValue(loteProdutoFilter.qtEstoque));
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", loteProdutoFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", loteProdutoFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", loteProdutoFilter.cdProduto);
		sqlWhereClause.addAndCondition("CDLOTEPRODUTO = ", loteProdutoFilter.cdLoteproduto);
		sqlWhereClause.addAndCondition("FLORIGEMESTOQUE = ", loteProdutoFilter.flOrigemEstoque);
		sqlWhereClause.addAndCondition("CDLOCAl = ", loteProdutoFilter.cdLocal);
		if (LavenderePdaConfig.usaDescPromo && ValueUtil.isEmpty(loteProdutoFilter.cdLocal) && !loteProdutoFilter.ignoraLocal) {
			sqlWhereClause.addAndCondition("(CDLOCAl = '' OR CDLOCAl IS NULL) ");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    	return updateAll(sql.toString());
    }

    
    public Vector findLoteProdutoPor(String cdGrupoproduto1, LoteProduto loteProdutoFilter, String flOrigemEstoque, String cdLocal, boolean ignoraLocal) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select TB.cdLoteproduto, TB.dtValidade, TB.vlPctvidautilproduto, TB.qtEstoquereservado, qtEstoquedisponivel, TB.cdLocal, ");
    	 if (LavenderePdaConfig.usaDescPromo) {
         	sql.append(" DSLOCAL,");
         }
    	if (LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco()) {
        	sql.append(" COUNT(TABPRECOLOTEPRODUTO.CDLOTEPRODUTO) AS QTTABPRECOLOTEPROD,");
        }
        sql.append("(SELECT vlPctDesconto FROM tblvpdescvidautilgrupo ds WHERE ");
        sql.append("	ds.cdGrupoproduto1 = ").append(Sql.getValue(cdGrupoproduto1));
        sql.append("	AND ds.vlPctvidautilminimo = TB.VLPCTVIDAUTILPRODUTO ");
        sql.append("	AND ds.vlPctvidautilmaximo = TB.VLPCTVIDAUTILPRODUTO ");
        sql.append("	limit 1 ");
        sql.append(") as vlPctDesconto, ");
        sql.append("(SELECT sum(qtestoque) "); 
        sql.append("	FROM tblvploteproduto lote "); 
        sql.append("	where lote.CDEMPRESA = TB.CDEMPRESA "); 
        sql.append("	and lote.CDREPRESENTANTE = TB.CDREPRESENTANTE "); 
        sql.append("	and lote.CDPRODUTO = TB.CDPRODUTO ");
        sql.append("	and lote.CDLOTEPRODUTO = TB.CDLOTEPRODUTO) as qtestoque "); 
        sql.append("FROM tblvploteproduto TB ");
        addJoinLocal(sql);
        addJoinTabPrecoLoteProd(sql, loteProdutoFilter);
        sql.append(" WHERE TB.cdEmpresa = ").append(Sql.getValue(loteProdutoFilter.cdEmpresa));
        sql.append("	AND TB.cdRepresentante = ").append(Sql.getValue(loteProdutoFilter.cdRepresentante));
        sql.append("	AND TB.cdProduto = ").append(Sql.getValue(loteProdutoFilter.cdProduto));
        sql.append("	AND TB.flOrigemEstoque = ").append(Sql.getValue(flOrigemEstoque));
        if (ValueUtil.isNotEmpty(cdLocal)) {
        	sql.append(" AND TB.CDLOCAl = ").append(Sql.getValue(cdLocal));
        }
		if (LavenderePdaConfig.usaDescPromo && ValueUtil.isEmpty(cdLocal) && !ignoraLocal) {
			sql.append(" AND (TB.CDLOCAl = '' OR TB.CDLOCAl IS NULL)  ");
		}
		agrupaPorTabPrecoLoteProd(sql,loteProdutoFilter.agrupaCdLoteProduto);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector result = new Vector();
    		Local local;
    		LoteProduto lote;
        	while (rs.next()) {
        		lote = new LoteProduto();
        		local = new Local();
        		lote.cdLoteproduto = rs.getString("cdLoteproduto");
        		lote.dtValidade = rs.getDate("dtValidade");
        		lote.vlPctvidautilproduto = rs.getDouble("vlPctvidautilproduto");
        		lote.qtEstoquedisponivel = rs.getDouble("qtEstoquedisponivel");
        		lote.qtEstoquereservado = rs.getDouble("qtEstoquereservado");
        		lote.vlPctDesconto = rs.getString("vlPctDesconto");
        		lote.qtEstoque = rs.getDouble("qtestoque");
        		if (LavenderePdaConfig.usaDescPromo) {
        			local.cdLocal = rs.getString("cdLocal");
        			local.dsLocal = rs.getString("dsLocal");
        		}
        		if (LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco()) {
        			lote.qtTabPrecoLoteProd = rs.getInt("QTTABPRECOLOTEPROD");
                }
        		lote.setLocal(local); 
        		result.addElement(lote);
        	} 
        	return result;
    	}
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	addJoinLocal(sql);
    	addJoinTabPrecoLoteProd(sql, (LoteProduto)domainFilter);
    }

	private void addJoinLocal(StringBuffer sql) {
    	if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal){
    		sql.append(" LEFT OUTER JOIN TBLVPLOCAL LOCAL ON");
    		sql.append(" TB.CDEMPRESA = LOCAL.CDEMPRESA");
    		sql.append(" AND TB.CDREPRESENTANTE = LOCAL.CDREPRESENTANTE");
    		sql.append(" AND TB.CDLOCAL = LOCAL.CDLOCAL");
    		}
    	}
    
    private void addJoinTabPrecoLoteProd(StringBuffer sql, LoteProduto loteProduto) {
    	if (!LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco()) {
    		return;
    	}
        sql.append(" LEFT OUTER JOIN TBLVPTABPRECOLOTEPROD TABPRECOLOTEPRODUTO ON");
        sql.append(" TB.CDEMPRESA = TABPRECOLOTEPRODUTO.CDEMPRESA");
        sql.append(" AND TB.CDREPRESENTANTE = TABPRECOLOTEPRODUTO.CDREPRESENTANTE");
        sql.append(" AND TB.CDLOTEPRODUTO = TABPRECOLOTEPRODUTO.CDLOTEPRODUTO");
		if (loteProduto != null && ValueUtil.isNotEmpty(loteProduto.cdTabelaPrecoFilter)) {
			sql.append(" AND TABPRECOLOTEPRODUTO.CDTABELAPRECO = ").append(Sql.getValue(loteProduto.cdTabelaPrecoFilter));
		}
    }
    
	private void agrupaPorTabPrecoLoteProd(StringBuffer sql, boolean agrupaCdLoteProduto) {
		if (!LavenderePdaConfig.isPermiteApenasVendaLoteProdutoVinculadoTabelaPreco()) {
			return;
		}
		if (agrupaCdLoteProduto) {
			sql.append(" GROUP BY TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDLOTEPRODUTO");
		} else {
			sql.append(" GROUP BY TB.CDEMPRESA, TB.CDREPRESENTANTE");
		}
	}

}