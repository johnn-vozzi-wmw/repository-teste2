package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class DescQuantidadePdbxDao extends LavendereCrudDbxDao {

    private static DescQuantidadePdbxDao instance;

    public DescQuantidadePdbxDao() {
        super(DescQuantidade.TABLE_NAME);
    }

    public static DescQuantidadePdbxDao getInstance() {
        if (instance == null) {
            instance = new DescQuantidadePdbxDao();
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
        sql.append(" tb.rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDTABELAPRECO,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.QTITEM,");
        sql.append(" tb.VLPCTDESCONTO,");
        sql.append(" tb.CDDESCONTO,");
        sql.append(" tb.FLAGRUPADORSIMILARIDADE");
        if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
        	sql.append(", tb.VLDESCONTO ");
        }
    }

    protected void addCacheColumns(StringBuffer sql) {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDPRODUTO,");
        sql.append(" QTITEM,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" CDDESCONTO");
        if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
        	sql.append(", VLDESCONTO ");
        }
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DescQuantidade descontoQuantidade = new DescQuantidade();
        descontoQuantidade.rowKey = rs.getString("rowkey");
        descontoQuantidade.cdEmpresa = rs.getString("cdEmpresa");
        descontoQuantidade.cdRepresentante = rs.getString("cdRepresentante");
        descontoQuantidade.cdTabelaPreco = rs.getString("cdTabelaPreco");
        descontoQuantidade.cdProduto = rs.getString("cdProduto");
        descontoQuantidade.qtItem = rs.getInt("qtItem");
        descontoQuantidade.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        descontoQuantidade.cdDesconto = rs.getString("cdDesconto");
        descontoQuantidade.flAgrupadorSimilaridade = rs.getString("flAgrupadorSimilaridade");
        if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
        	descontoQuantidade.vlDesconto = rs.getDouble("vlDesconto");
        }
        return descontoQuantidade;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescQuantidade descontoQuantidade = (DescQuantidade) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
       	sqlWhereClause.addAndCondition("CDEMPRESA = ", descontoQuantidade.cdEmpresa);
       	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descontoQuantidade.cdRepresentante);
       	sqlWhereClause.addAndCondition("CDTABELAPRECO = ", descontoQuantidade.cdTabelaPreco);
       	sqlWhereClause.addAndCondition("CDPRODUTO = ", descontoQuantidade.cdProduto);
       	sqlWhereClause.addAndCondition("CDDESCONTO = ", descontoQuantidade.cdDesconto);
       	sqlWhereClause.addAndCondition("QTITEM = ", descontoQuantidade.qtItem);
       	if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
       		sqlWhereClause.addAndCondition("VLDESCONTO = ", descontoQuantidade.vlDesconto);
       	}
	    sql.append(sqlWhereClause.getSql());
	    if (LavenderePdaConfig.usaTabelaPrecoPorCliente && LavenderePdaConfig.isEntidadeSyncWebAppRepZero(ItemTabelaPreco.TABLE_NAME.substring(5))) {
		    TabelaPrecoCliPdbxDao.addTabPrecoCliFilter(sql, false);
	    }
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
			sql.append(" order by ");
			String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
			String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
			for (int i = 0; i < sortAtributtes.length; i++) {
				sql.append(sortAtributtes[i]);
				sql.append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
				if (!(i == (sortAtributtes.length - 1))) {
					sql.append(" , ");
				}
			}
		} else {
			sql.append(" order by qtitem desc, vlpctdesconto desc");
		}
    }

    protected void addOrderBy(StringBuffer sql) {
    	sql.append(" order by qtitem desc, vlpctdesconto desc");
    }

    public Vector findAllByCdDesconto(String cdDesconto) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domainFilter, sql);
        sql.append(" from ").append(tableName).append(" tb ");
        if (LavenderePdaConfig.permiteVincularCliente) {
        	addJoinDescQuantidadeCliente(sql);
        }
        sql.append(" where cdDesconto = ").append(Sql.getValue(cdDesconto));
        if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
        	sql.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA ");
        }
        if (LavenderePdaConfig.permiteVincularCliente) {
			sql.append(" AND (descCliente.CDCLIENTE IS NOT NULL OR ")
			.append(" NOT EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADECLIENTE WHERE ")
			.append(" CDEMPRESA = tb.CDEMPRESA AND ")
			.append(" CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
			.append(" CDPRODUTO = tb.CDPRODUTO AND ")
			.append(" CDTABELAPRECO = tb.CDTABELAPRECO AND ")
			.append(" CDCLIENTE <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente)).append(")")
			.append(")");
		}
	    if (LavenderePdaConfig.usaTabelaPrecoPorCliente && LavenderePdaConfig.isEntidadeSyncWebAppRepZero(ItemTabelaPreco.TABLE_NAME.substring(5))) {
		    TabelaPrecoCliPdbxDao.addTabPrecoCliFilter(sql, false);
	    }
        addOrderBy(sql);
        //--
        try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				result.addElement(populate(domainFilter, rs));
			}
			return result;
		}
    }
    
    public String findCdProdutoCdDesconto(String cdDesconto) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select tb.cdproduto ");
        sql.append(" from ").append(tableName).append(" tb ");
        sql.append(" where cdDesconto = ").append(Sql.getValue(cdDesconto));
        if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
        	sql.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA ");
        }
        addOrderBy(sql);
        sql.append(" limit 1");
        return getString(sql.toString());
    }
    
    public Vector findAllByDescSimilar(DescQuantidade filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(filter, sql);
        sql.append(" from ").append(tableName).append(" tb");
        if (LavenderePdaConfig.permiteVincularCliente) {
        	addJoinDescQuantidadeCliente(sql);
        }
        sql.append(" JOIN TBLVPDESCQTDEAGRSIMILAR descq ON ")
        .append(" descq.CDEMPRESA = tb.CDEMPRESA AND ")
        .append(" descq.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append(" descq.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
        .append(" descq.CDPRODUTO = tb.CDPRODUTO ")
        .append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(filter.cdEmpresa)).append(" AND ")
        .append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(filter.cdRepresentante)).append(" AND ")
        .append(" tb.CDTABELAPRECO = ").append(Sql.getValue(filter.cdTabelaPreco)).append(" AND ")
        .append("(tb.CDPRODUTO = ").append(Sql.getValue(filter.cdProduto)).append(" OR descq.CDPRODUTOSIMILAR = ").append(Sql.getValue(filter.cdProduto)).append(")");
        if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
        	sql.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA ");
        }
        if (LavenderePdaConfig.permiteVincularCliente) {
			sql.append(" AND (descCliente.CDCLIENTE IS NOT NULL OR ")
			.append(" NOT EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADECLIENTE WHERE ")
			.append(" CDEMPRESA = tb.CDEMPRESA AND ")
			.append(" CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
			.append(" CDPRODUTO = tb.CDPRODUTO AND ")
			.append(" CDTABELAPRECO = ").append(Sql.getValue(filter.cdTabelaPreco)).append(" AND ")
			.append(" CDPRODUTO <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente)).append(")")
			.append(")");
		}
        addOrderBy(sql);
        //--
        try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			while (rs.next()) {
				result.addElement(populate(filter, rs));
			}
			return result;
		}
    }
    
    public String findCdProdutoDescSimilar(DescQuantidade filter, ItemPedido itemPedido) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
        sql.append(" select tb.cdproduto");
        sql.append(" from ").append(tableName).append(" tb");
        if (LavenderePdaConfig.permiteVincularCliente) {
        	addJoinDescQuantidadeCliente(sql);
        }
        if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido != null) {
        	addJoinItemKitPedido(itemPedido, sql);
        }
        sql.append(" JOIN TBLVPDESCQTDEAGRSIMILAR descq ON ")
        .append(" descq.CDEMPRESA = tb.CDEMPRESA AND ")
        .append(" descq.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append(" descq.CDTABELAPRECO = tb.CDTABELAPRECO AND ")
        .append(" descq.CDPRODUTO = tb.CDPRODUTO ")
        .append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(filter.cdEmpresa)).append(" AND ")
        .append(" tb.CDREPRESENTANTE = ").append(Sql.getValue(filter.cdRepresentante)).append(" AND ");
        if (!(LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido != null)) {
        	sql.append(" tb.CDTABELAPRECO = ").append(Sql.getValue(filter.cdTabelaPreco)).append(" AND ");
        }
        if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && itemPedido != null) {
        	sql.append("(tb.CDPRODUTO = kit.CDPRODUTO OR descq.CDPRODUTOSIMILAR = kit.CDPRODUTO)");
        } else {
        	sql.append("(tb.CDPRODUTO = ").append(Sql.getValue(filter.cdProduto)).append(" OR descq.CDPRODUTOSIMILAR = ").append(Sql.getValue(filter.cdProduto)).append(")");
		}
        if (LavenderePdaConfig.usaVigenciaDescQuantidade) {
        	sql.append(" AND ").append(Sql.getValue(DateUtil.getCurrentDate())).append(" BETWEEN DTINICIALVIGENCIA AND DTFIMVIGENCIA ");
        }
        if (LavenderePdaConfig.permiteVincularCliente) {
			sql.append(" AND (descCliente.CDCLIENTE IS NOT NULL OR ")
			.append(" NOT EXISTS (SELECT 1 FROM TBLVPDESCQUANTIDADECLIENTE WHERE ")
			.append(" CDEMPRESA = tb.CDEMPRESA AND ")
			.append(" CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
			.append(" CDPRODUTO = tb.CDPRODUTO AND ")
			.append(" CDTABELAPRECO = ").append(Sql.getValue(filter.cdTabelaPreco)).append(" AND ")
			.append(" CDCLIENTE <> ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente)).append(")")
			.append(")");
		}
        addOrderBy(sql);
        sql.append(" limit 1");
        return getString(sql.toString());
    }
    
    private void addJoinItemKitPedido(ItemPedido itemPedido, StringBuffer sql) {
    	sql.append(" JOIN TBLVPPRODUTOKIT kit ON ")
        .append(" kit.CDEMPRESA = tb.CDEMPRESA AND ")
        .append(" kit.CDREPRESENTANTE = tb.CDREPRESENTANTE AND ")
        .append(" kit.CDKIT =  ").append(Sql.getValue(itemPedido.cdProduto));
    }
    
    private void addJoinDescQuantidadeCliente(StringBuffer sql) {
    	sql.append(" LEFT JOIN TBLVPDESCQUANTIDADECLIENTE descCliente ON ")
    	.append(" tb.CDEMPRESA = descCliente.CDEMPRESA AND ")
    	.append(" tb.CDREPRESENTANTE = descCliente.CDREPRESENTANTE AND ")
    	.append(" tb.CDTABELAPRECO = descCliente.CDTABELAPRECO AND ")
    	.append(" tb.CDPRODUTO = descCliente.CDPRODUTO AND ")
    	.append(" descCliente.CDCLIENTE = ").append(Sql.getValue(SessionLavenderePda.getCliente().cdCliente));
    }
    
	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescQuantidade();
	}
    
}