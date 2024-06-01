package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class GiroProdutoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GiroProduto();
	}

    private static GiroProdutoPdbxDao instance;

    public GiroProdutoPdbxDao() {
        super(GiroProduto.TABLE_NAME);
    }

    public static GiroProdutoPdbxDao getInstance() {
        if (instance == null) {
            instance = new GiroProdutoPdbxDao();
        }
        return instance;
    }
    
    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { /**/ }
    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }
    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }

    @Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
    	GiroProduto giroProdutoFilter = (GiroProduto) domain;
    	sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.QTMEDIASEMANAL,");
        sql.append(" tb.QTMAIORCOMPRA,");
        sql.append(" tb.DSDIA,");
        sql.append(" tb.QTCOMPRA,");
        sql.append(" tb.DSOBSERVACAO,");
    	sql.append(" tb.CDITEMGRADE1,");
    	sql.append(" tb.CDITEMGRADE2,");
    	sql.append(" tb.CDITEMGRADE3,");
    	sql.append(" tb.NURELEVANCIA,");
    	sql.append(" tb.VLUNITARIO");
    	if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && LavenderePdaConfig.ocultaTabelaPrecoPedido) {
    		sql.append(", tb.CDTABELAPRECO");
    	}
		if (giroProdutoFilter.produtoFilter != null) {
			sql.append(",").append(DaoUtil.ALIAS_PRODUTO).append(".DSPRODUTO");			
		}
	}

    @Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	GiroProduto giroProdutoFilter = (GiroProduto) domainFilter;
    	GiroProduto giroProduto = new GiroProduto();
		giroProduto.rowKey = rs.getString("rowkey");
		giroProduto.cdEmpresa = rs.getString("CDEMPRESA");
		giroProduto.cdRepresentante = rs.getString("CDREPRESENTANTE");
		giroProduto.cdProduto = rs.getString("CDPRODUTO");
		giroProduto.qtMediasemanal = rs.getDouble("QTMEDIASEMANAL");
		giroProduto.qtMaiorcompra = rs.getDouble("QTMAIORCOMPRA");
		giroProduto.dsDia = rs.getString("DSDIA");
		giroProduto.dsObservacao = rs.getString("DSOBSERVACAO");
		giroProduto.qtCompra = rs.getDouble("QTCOMPRA");
		giroProduto.vlUnitario = rs.getDouble("VLUNITARIO");
		giroProduto.cdItemGrade1 = rs.getString("CDITEMGRADE1");
		giroProduto.cdItemGrade2 = rs.getString("CDITEMGRADE2");
		giroProduto.cdItemGrade3 = rs.getString("CDITEMGRADE3");
		giroProduto.nuRelevancia = rs.getInt("NURELEVANCIA");
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && LavenderePdaConfig.ocultaTabelaPrecoPedido) {
			giroProduto.cdTabelaPreco = rs.getString("CDTABELAPRECO");
		}
		if (giroProdutoFilter.produtoFilter != null) {
			giroProduto.dsProduto = rs.getString("DSPRODUTO");
		}
		return giroProduto;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
    	sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.QTMEDIASEMANAL,");
        sql.append(" tb.QTMAIORCOMPRA,");
        sql.append(" tb.DSDIA,");
        sql.append(" tb.QTCOMPRA,");
        sql.append(" tb.DSOBSERVACAO,");
        sql.append(" tb.VLUNITARIO,");
        if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && LavenderePdaConfig.ocultaTabelaPrecoPedido) {
        	sql.append(" tb.CDTABELAPRECO,");
        }
    	sql.append(" tb.CDITEMGRADE1,");
    	sql.append(" tb.CDITEMGRADE2,");
    	sql.append(" tb.CDITEMGRADE3,");
		sql.append(" tb.NURELEVANCIA");
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GiroProduto giroproduto = new GiroProduto();
        giroproduto.rowKey = rs.getString("rowkey");
        giroproduto.cdEmpresa = rs.getString("cdEmpresa");
        giroproduto.cdRepresentante = rs.getString("cdRepresentante");
        giroproduto.cdCliente = rs.getString("cdCliente");
        giroproduto.cdProduto = rs.getString("cdProduto");
        giroproduto.qtMediasemanal = ValueUtil.round(rs.getDouble("qtMediasemanal"));
        giroproduto.qtMaiorcompra = ValueUtil.round(rs.getDouble("qtMaiorcompra"));
        giroproduto.dsDia = rs.getString("dsDia");
        giroproduto.qtCompra = ValueUtil.round(rs.getDouble("qtCompra"));
        giroproduto.dsObservacao = rs.getString("dsObservacao");
        giroproduto.vlUnitario = ValueUtil.round(rs.getDouble("vlUnitario"));
		giroproduto.nuRelevancia = rs.getInt("nuRelevancia");
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && LavenderePdaConfig.ocultaTabelaPrecoPedido) {
			giroproduto.cdTabelaPreco = rs.getString("cdTabelaPreco");
		}
		giroproduto.cdItemGrade1 = rs.getString("cdItemGrade1");
		giroproduto.cdItemGrade2 = rs.getString("cdItemGrade2");
		giroproduto.cdItemGrade3 = rs.getString("cdItemGrade3");
        return giroproduto;
    }

	@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		GiroProduto giroProdutoFilter = (GiroProduto) domain;
		addWhereByExampleBase(sqlWhereClause, giroProdutoFilter, "tb.");
		if (giroProdutoFilter.itemPedidoFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getExistsItemPedidoOrItemPedidoErpCondition(giroProdutoFilter.itemPedidoFilter, false, true));
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante && giroProdutoFilter.produtoClienteFilter != null) {
       		sqlWhereClause.addAndCondition(ProdutoClienteDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante && giroProdutoFilter.clienteProdutoFilter != null) {
       		sqlWhereClause.addAndCondition(ClienteProdutoDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
       	}
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante && giroProdutoFilter.produtoCondPagtoFilter != null) {
			sqlWhereClause.addAndCondition(ProdutoCondPagtoDbxDao.getInstance().getFlTipoRelacaoCteCondition(false));
		}
		if (giroProdutoFilter.produtoFilter != null) {
			sqlWhereClause.addStartAndMultipleCondition();
			boolean adicionouInicioBloco = false;
			adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(DaoUtil.ALIAS_PRODUTO + ".CDPRODUTO", giroProdutoFilter.produtoFilter.dsProduto, true);
			adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition(DaoUtil.ALIAS_PRODUTO + ".DSPRODUTO", giroProdutoFilter.produtoFilter.cdProduto, true);
			if (adicionouInicioBloco) {
				sqlWhereClause.addEndMultipleCondition();
			} else {
				sqlWhereClause.removeStartMultipleCondition();
			}
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido && giroProdutoFilter.produtoFilter != null) {
			DaoUtil.addAndFlExcecaoProdutoTipoPedCondition(giroProdutoFilter.produtoFilter, sqlWhereClause);
		}
		sql.append(sqlWhereClause.getSql());
	    if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
		    DaoUtil.addNotExistsRestricaoProduto(sql, "TB.CDPRODUTO", SessionLavenderePda.getCliente().cdCliente, null, 1, null, true, false, false, false);
	    }
    }

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		addWhereByExampleBase(sqlWhereClause, (GiroProduto) domain, ValueUtil.VALOR_NI);
		sql.append(sqlWhereClause.getSql());
	}

	private void addWhereByExampleBase(SqlWhereClause sqlWhereClause, GiroProduto giroProdutoFilter, String alias) {
		sqlWhereClause.addAndCondition(alias + "CDEMPRESA = ", giroProdutoFilter.cdEmpresa);
		if (SessionLavenderePda.usuarioPdaRep.representante.isSupervisor()) {
			String[] cdReps = new String[]{SessionLavenderePda.usuarioPdaRep.cdRepresentante, giroProdutoFilter.cdRepresentante};
			sqlWhereClause.addAndConditionIn(alias + "CDREPRESENTANTE ", cdReps);
			sqlWhereClause.addAndConditionIn("PROD.CDREPRESENTANTE ", cdReps);
		} else {
			sqlWhereClause.addAndCondition(alias + "CDREPRESENTANTE = ", giroProdutoFilter.cdRepresentante);
			sqlWhereClause.addAndCondition("PROD.CDREPRESENTANTE = ", giroProdutoFilter.cdRepresentante);
		}
		sqlWhereClause.addAndCondition(alias + "CDCLIENTE = ", giroProdutoFilter.cdCliente);
		sqlWhereClause.addAndCondition(alias + "CDPRODUTO = ", giroProdutoFilter.cdProduto);
		sqlWhereClause.addAndCondition(alias + "CDITEMGRADE1 = ", giroProdutoFilter.cdItemGrade1);
		sqlWhereClause.addAndCondition(alias + "CDITEMGRADE2 = ", giroProdutoFilter.cdItemGrade2);
		sqlWhereClause.addAndCondition(alias + "CDITEMGRADE3 = ", giroProdutoFilter.cdItemGrade3);
		sqlWhereClause.addAndConditionIn(alias + "CDTABELAPRECO", giroProdutoFilter.cdTabelaPrecoList);
	}

	public Vector findAllByItemPedidoNaoInseridoList(Pedido pedido, BaseDomain domain) throws java.sql.SQLException {
		StringBuffer sql = new StringBuffer();
		addCTESummary(domain, sql);
        sql.append("select ");
        addSelectSummaryColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addJoinSummary(domain, sql);
        addWhereByExample(domain, sql);
    	adicionaProdutoInseridosNaoVendaProduto(pedido, sql);
    	adicionaProdutoExistenteNaTabelaProduto(pedido.cdEmpresa, pedido.cdRepresentante, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
		        result.addElement(populateSummary(domain, rs));
			}
			return result;
		}
    }
    
	private void adicionaProdutoInseridosNaoVendaProduto(Pedido pedido, StringBuffer sql) {
		if (pedido == null) return;
		
		sql.append(" AND tb.CDPRODUTO not in (SELECT MOTNVP.CDPRODUTO FROM TBLVPNAOVENDAPRODPEDIDO MOTNVP WHERE ");
		sql.append("MOTNVP.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND MOTNVP.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND MOTNVP.NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(" AND MOTNVP.FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append(")");
	}
	
	private void adicionaProdutoExistenteNaTabelaProduto(String cdEmpresa, String cdRepresentante, StringBuffer sql) {
		sql.append(" AND tb.CDPRODUTO in (SELECT PRODUTO.CDPRODUTO FROM TBLVPPRODUTO PRODUTO WHERE ");
		sql.append(" PRODUTO.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PRODUTO.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(")");
	}
	
	@Override
	protected void addJoinSummary(BaseDomain domainFilter, StringBuffer sql) {
		GiroProduto giroProdutoFilter = (GiroProduto) domainFilter;
		Produto produtoFilter = giroProdutoFilter.produtoFilter;
		if (produtoFilter != null) {
			sql.append(" JOIN TBLVPPRODUTO PROD").append(" ON ");
			sql.append("PROD.CDEMPRESA = tb.CDEMPRESA AND ").append("PROD.CDPRODUTO = tb.CDPRODUTO ");
			if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(produtoFilter.cdGrupoPermProd)) {
	        	DaoUtil.addJoinGrupoCliPermProd(sql, DaoUtil.ALIAS_GRUPOPERMPROD, produtoFilter.cdGrupoPermProd);
	        }
			if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(produtoFilter.cdTipoPedidoFilter)) {
				DaoUtil.addJoinProdutoTipoPedido(sql, produtoFilter);
			}
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante && giroProdutoFilter.produtoClienteFilter != null) {
			DaoUtil.addJoinCTEProdutoCliente(giroProdutoFilter.produtoClienteFilter, sql);
		}
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante && giroProdutoFilter.clienteProdutoFilter != null) {
			DaoUtil.addJoinCTEClienteProduto(giroProdutoFilter.clienteProdutoFilter, sql);
		}
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante && giroProdutoFilter.produtoCondPagtoFilter != null) {
			DaoUtil.addJoinCTEProdutoCondPagto(giroProdutoFilter.produtoCondPagtoFilter, sql);
		}
	}
	
	@Override
	protected void addCTESummary(BaseDomain domainFilter, StringBuffer sql) {
		GiroProduto giroProdutoFilter = (GiroProduto) domainFilter;
		
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante && giroProdutoFilter.produtoClienteFilter != null) {
			DaoUtil.addCTEsProdutoCliente(sql, giroProdutoFilter.produtoClienteFilter);
		}
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante && giroProdutoFilter.clienteProdutoFilter != null) {
			DaoUtil.addCTEsClienteProduto(sql, giroProdutoFilter.clienteProdutoFilter);
		}
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante && giroProdutoFilter.produtoCondPagtoFilter != null) {
			DaoUtil.addCTEsProdutoCondPagto(sql, giroProdutoFilter.produtoCondPagtoFilter);
		}
	}

}