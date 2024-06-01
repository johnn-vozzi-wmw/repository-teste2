package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.PermissaoSolAutService;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class SolAutorizacaoDbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SolAutorizacao();
	}

    private static SolAutorizacaoDbxDao instance;
    
    private boolean maxAutorizacao;

    public SolAutorizacaoDbxDao() { super(SolAutorizacao.TABLE_NAME); }
    public static SolAutorizacaoDbxDao getInstance() { return (instance == null) ? instance = new SolAutorizacaoDbxDao() : instance; }
	@Override protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { addSelectColumns(null, sql); }
	@Override protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return populate(domainFilter, rs); }

	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		SolAutorizacao solAutorizacao = (SolAutorizacao) domain;
		if ("ROWKEY".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.getRowKey()); }
		if ("CDEMPRESA".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdEmpresa); }
		if ("CDSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdSolAutorizacao); }
		if ("FLORIGEMPEDIDO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.flOrigemPedido); }
		if ("NUPEDIDO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.nuPedido); }
		if ("CDCLIENTE".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdCliente); }
		if ("CDPRODUTO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdProduto); }
		if ("FLTIPOITEMPEDIDO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.flTipoItemPedido); }
		if ("NUSEQPRODUTO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.nuSeqProduto); }
		if ("CDTIPOSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.tipoSolicitacaoAutorizacaoEnum.ordinal()); }
		if ("CDTABELAPRECO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdTabelaPreco); }
		if ("QTITEMFISICO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.qtItemFisico); }
		if ("VLITEMPEDIDO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.vlItemPedido); }
		if ("VLTOTALITEMPEDIDO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.vlTotalItemPedido); }
		if ("VLORIGINAL".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.vlOriginal); }
		if ("FLAUTORIZADO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.flAutorizado); }
		if ("DSOBSERVACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.dsObservacao); }
		if ("CDUSUARIOSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdUsuarioSolAutorizacao); }
		if ("NMUSUARIOLIBSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.nmUsuarioLibSolAutorizacao); }
		if ("DTSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.dtSolAutorizacao); }
		if ("HRSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.hrSolAutorizacao); }
		if ("CDREPRESENTANTE".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdRepresentante); }
		if ("CDUSUARIOLIBSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdUsuarioLibSolAutorizacao); }
		if ("DTLIBSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.dtLibSolAutorizacao); }
		if ("HRLIBSOLAUTORIZACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.hrLibSolAutorizacao); }
		if ("FLVISUALIZADO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.flVisualizado); }
		if ("CDUSUARIO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdUsuario); }
		if ("FLTIPOALTERACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.flTipoAlteracao); }
		if ("FLEXCLUIDO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.flExcluido); }
		if ("CDCONDICAOPAGAMENTO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.cdCondicaoPagamento); }
		if ("VLPARCELAMINMAX".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.vlParcelaMinMax); }
		if ("VLPARCELAPEDIDO".equalsIgnoreCase(columnName)) { return Sql.getValue(solAutorizacao.vlparcelaPedido); }
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}
    
    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
	    SolAutorizacao solAutorizacao = (SolAutorizacao) domain;
	    sql.append(" FLAUTORIZADO = ").append(Sql.getValue(solAutorizacao.flAutorizado)).append(",");
	    sql.append(" DSOBSERVACAO = ").append(Sql.getValue(solAutorizacao.dsObservacao)).append(",");
	    sql.append(" CDUSUARIOLIBSOLAUTORIZACAO = ").append(Sql.getValue(solAutorizacao.cdUsuarioLibSolAutorizacao)).append(",");
	    sql.append(" NMUSUARIOLIBSOLAUTORIZACAO = ").append(Sql.getValue(solAutorizacao.nmUsuarioLibSolAutorizacao)).append(",");
	    sql.append(" DTLIBSOLAUTORIZACAO = ").append(Sql.getValue(solAutorizacao.dtLibSolAutorizacao)).append(",");
	    sql.append(" HRLIBSOLAUTORIZACAO = ").append(Sql.getValue(solAutorizacao.hrLibSolAutorizacao)).append(",");
	    sql.append(" FLVISUALIZADO = ").append(Sql.getValue(solAutorizacao.flVisualizado)).append(",");
	    sql.append(" FLEXCLUIDO = ").append(Sql.getValue(solAutorizacao.flExcluido)).append(",");
	    sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(solAutorizacao.flTipoAlteracao));
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        SolAutorizacao solAutorizacao = new SolAutorizacao();
	    solAutorizacao.rowKey = rs.getString("rowkey");
	    solAutorizacao.cdEmpresa = rs.getString("cdEmpresa");
	    solAutorizacao.cdRepresentante = rs.getString("cdRepresentante");
	    solAutorizacao.cdSolAutorizacao = rs.getString("cdSolAutorizacao");
	    solAutorizacao.flOrigemPedido = rs.getString("flOrigemPedido");
	    solAutorizacao.nuPedido = rs.getString("nuPedido");
	    solAutorizacao.cdCliente = rs.getString("cdCliente");
	    solAutorizacao.cdProduto = rs.getString("cdProduto");
	    solAutorizacao.flTipoItemPedido = rs.getString("flTipoItemPedido");
	    solAutorizacao.nuSeqProduto = rs.getInt("nuSeqProduto");
	    solAutorizacao.tipoSolicitacaoAutorizacaoEnum = TipoSolicitacaoAutorizacaoEnum.values()[rs.getInt("cdTipoSolAutorizacao")];
	    solAutorizacao.qtItemFisico = rs.getDouble("qtItemFisico");
		solAutorizacao.cdTabelaPreco = rs.getString("cdTabelaPreco");
	    solAutorizacao.vlItemPedido = rs.getDouble("vlItemPedido");
	    solAutorizacao.vlTotalItemPedido = rs.getDouble("vlTotalItemPedido");
	    solAutorizacao.vlOriginal = rs.getDouble("vlOriginal");
	    solAutorizacao.flAutorizado = rs.getString("flAutorizado");
	    solAutorizacao.dsObservacao = rs.getString("dsObservacao");
	    solAutorizacao.cdUsuarioSolAutorizacao = rs.getString("cdUsuarioSolAutorizacao");
	    solAutorizacao.dtSolAutorizacao = rs.getDate("dtSolAutorizacao");
	    solAutorizacao.hrSolAutorizacao = rs.getString("hrSolAutorizacao");
	    solAutorizacao.cdUsuarioLibSolAutorizacao = rs.getString("cdUsuarioLibSolAutorizacao");
	    solAutorizacao.nmUsuarioLibSolAutorizacao = rs.getString("nmUsuarioLibSolAutorizacao");
	    solAutorizacao.dtLibSolAutorizacao = rs.getDate("dtLibSolAutorizacao");
	    solAutorizacao.hrLibSolAutorizacao = rs.getString("hrLibSolAutorizacao");
	    solAutorizacao.flVisualizado = rs.getString("flVisualizado");
	    solAutorizacao.flExcluido = rs.getString("flExcluido");
	    solAutorizacao.flAgrupadorSimilaridade = rs.getString("flAgrupadorSimilaridade");
	    solAutorizacao.cdUsuario = rs.getString("cdUsuario");
	    solAutorizacao.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
		solAutorizacao.vlParcelaMinMax = rs.getDouble("vlParcelaMinMax");
		solAutorizacao.vlparcelaPedido = rs.getDouble("vlparcelaPedido");

	    solAutorizacao.empresa = new Empresa();
	    solAutorizacao.empresa.cdEmpresa = solAutorizacao.cdEmpresa;
	    solAutorizacao.empresa.nmEmpresa = rs.getString("NMEMPRESA");
	    solAutorizacao.empresa.nmEmpresaCurto = rs.getString("NMEMPRESACURTO");

	    solAutorizacao.representante = new Representante();
	    solAutorizacao.representante.cdRepresentante = solAutorizacao.cdRepresentante;
	    solAutorizacao.representante.nmRepresentante = rs.getString("NMREPRESENTANTE");

	    solAutorizacao.cliente = new Cliente();
	    solAutorizacao.cliente.cdEmpresa = solAutorizacao.cdEmpresa;
	    solAutorizacao.cliente.cdRepresentante = solAutorizacao.cdRepresentante;
	    solAutorizacao.cliente.cdCliente = solAutorizacao.cdCliente;
	    solAutorizacao.cliente.nmRazaoSocial = rs.getString("NMRAZAOSOCIAL");
	    solAutorizacao.cliente.nuCnpj = rs.getString("NUCNPJ");

	    solAutorizacao.produto = new Produto();
	    solAutorizacao.produto.cdEmpresa = solAutorizacao.cdEmpresa;
	    solAutorizacao.produto.cdRepresentante = solAutorizacao.cdRepresentante;
	    solAutorizacao.produto.cdProduto = solAutorizacao.cdProduto;
	    solAutorizacao.produto.dsProduto = rs.getString("DSPRODUTO");
	    solAutorizacao.produto.cdGrupoProduto1 = rs.getString("CDGRUPOPRODUTO1");
        return solAutorizacao;
    }

	@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
	    sql.append(" tb.rowkey,");
	    sql.append(" tb.CDEMPRESA,");
	    sql.append(" tb.CDREPRESENTANTE,");
	    if (maxAutorizacao) {
	    	sql.append(" MAX(tb.CDSOLAUTORIZACAO) CDSOLAUTORIZACAO,");
	    } else {
	    	sql.append(" tb.CDSOLAUTORIZACAO,");
	    }
	    sql.append(" tb.FLORIGEMPEDIDO,");
	    sql.append(" tb.NUPEDIDO,");
	    sql.append(" tb.CDCLIENTE,");
	    sql.append(" tb.CDPRODUTO,");
	    sql.append(" tb.FLTIPOITEMPEDIDO,");
	    sql.append(" tb.NUSEQPRODUTO,");
	    sql.append(" tb.CDTIPOSOLAUTORIZACAO,");
	    sql.append(" tb.QTITEMFISICO,");
		sql.append(" tb.CDTABELAPRECO,");
	    sql.append(" tb.VLITEMPEDIDO,");
	    sql.append(" tb.VLTOTALITEMPEDIDO,");
	    sql.append(" tb.VLORIGINAL,");
	    sql.append(" tb.FLAUTORIZADO,");
	    sql.append(" tb.DSOBSERVACAO,");
	    sql.append(" tb.CDUSUARIOSOLAUTORIZACAO,");
	    sql.append(" tb.DTSOLAUTORIZACAO,");
	    sql.append(" tb.HRSOLAUTORIZACAO,");
	    sql.append(" tb.CDUSUARIOLIBSOLAUTORIZACAO,");
	    sql.append(" tb.NMUSUARIOLIBSOLAUTORIZACAO,");
	    sql.append(" tb.DTLIBSOLAUTORIZACAO,");
	    sql.append(" tb.HRLIBSOLAUTORIZACAO,");
		sql.append(" tb.FLVISUALIZADO,");
		sql.append(" tb.FLEXCLUIDO,");
		sql.append(" tb.FLAGRUPADORSIMILARIDADE,");
	    sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.CDCONDICAOPAGAMENTO,");
		sql.append(" tb.VLPARCELAMINMAX,");
		sql.append(" tb.VLPARCELAPEDIDO,");
	    sql.append(" EMPRESA.NMEMPRESA,");
	    sql.append(" EMPRESA.NMEMPRESACURTO,");
	    sql.append(" REPRESENTANTE.NMREPRESENTANTE,");
	    sql.append(" CLI.NMRAZAOSOCIAL,");
	    sql.append(" CLI.NUCNPJ,");
	    sql.append(" PRODUTO.DSPRODUTO,");
		sql.append(" PRODUTO.CDGRUPOPRODUTO1");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        SolAutorizacao solAutorizacao = (SolAutorizacao) domain;
	    SqlWhereClause sqlWhereClause = new SqlWhereClause();
	    sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", solAutorizacao.cdEmpresa);
	    sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", solAutorizacao.cdRepresentante);
	    sqlWhereClause.addAndCondition("tb.CDSOLAUTORIZACAO = ", solAutorizacao.cdSolAutorizacao);
	    sqlWhereClause.addAndCondition("tb.FLORIGEMPEDIDO = ", solAutorizacao.flOrigemPedido);
	    sqlWhereClause.addAndCondition("tb.NUPEDIDO = ", solAutorizacao.nuPedido);
	    sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", solAutorizacao.cdCliente);
	    sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", solAutorizacao.cdProduto);
	    sqlWhereClause.addAndCondition("tb.FLAGRUPADORSIMILARIDADE = ", solAutorizacao.flAgrupadorSimilaridade);
	    sqlWhereClause.addAndCondition("prod.CDAGRUPADORSIMILARIDADE = ", solAutorizacao.cdAgrupadorSimilaridade);
	    if (solAutorizacao.tipoSolicitacaoAutorizacaoEnum != null) {
		    sqlWhereClause.addAndCondition("tb.CDTIPOSOLAUTORIZACAO = ", solAutorizacao.tipoSolicitacaoAutorizacaoEnum.ordinal());
	    }
	    boolean iniciouCondicaoMultipla = false;
	    if (solAutorizacao.produto != null || solAutorizacao.cliente != null || ValueUtil.isNotEmpty(solAutorizacao.nuPedidoLike)) {
		    sqlWhereClause.addStartAndMultipleCondition();
		    if (solAutorizacao.produto != null) {
			    iniciouCondicaoMultipla = true;
			    ProdutoPdbxDao.getProdutoSearchCondition("PRODUTO.", solAutorizacao.produto, sqlWhereClause, iniciouCondicaoMultipla);
		    }
		    if (solAutorizacao.cliente != null) {
			    if (iniciouCondicaoMultipla) {
				    sqlWhereClause.addStartOrMultipleCondition();
			    } else {
				    sqlWhereClause.addStartMultipleCondition();
			    }
			    boolean adicionouInicioBloco = sqlWhereClause.addOrLikeCondition("CLI.NMRAZAOSOCIAL", solAutorizacao.cliente.nmRazaoSocial, false);
			    adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("CLI.CDCLIENTE", solAutorizacao.cliente.cdCliente, false);
			    adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("CLI.NMFANTASIA", solAutorizacao.cliente.nmFantasia, false);
			    if (adicionouInicioBloco) {
				    sqlWhereClause.addEndMultipleCondition();
			    } else {
				    sqlWhereClause.removeStartMultipleCondition();
			    }
		    }
		    sqlWhereClause.addOrLikeCondition("tb.NUPEDIDO", solAutorizacao.nuPedidoLike, false);
		    if (iniciouCondicaoMultipla) {
			    sqlWhereClause.addEndMultipleCondition();
		    }
	    }
	    if (solAutorizacao.flAutorizado != null) {
		    sqlWhereClause.addAndConditionForced("tb.FLAUTORIZADO = ", solAutorizacao.flAutorizado);
	    }
	    if (solAutorizacao.filterPermission) {
			sqlWhereClause.addAndCondition(" (PERMISSAOSOLAUT.CDUSUARIOPERMISSAO IS NOT NULL OR tb.CDUSUARIOSOLAUTORIZACAO = " + Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario) + ")");
	    }
	    sqlWhereClause.addAndCondition("tb.FLVISUALIZADO <> ", solAutorizacao.flVisualizadoDifferenceFilter);
	    if (!solAutorizacao.ignoreRemovidos) {
		    sqlWhereClause.addAndCondition("(tb.FLEXCLUIDO <> " + Sql.getValue(ValueUtil.VALOR_SIM) + " OR tb.FLEXCLUIDO IS NULL)");
	    }
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		SolAutorizacao solAutorizacao = (SolAutorizacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", solAutorizacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", solAutorizacao.cdRepresentante);
		sqlWhereClause.addAndCondition("CDSOLAUTORIZACAO = ", solAutorizacao.cdSolAutorizacao);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", solAutorizacao.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", solAutorizacao.nuPedido);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", solAutorizacao.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", solAutorizacao.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", solAutorizacao.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", solAutorizacao.nuSeqProduto);
		if (solAutorizacao.tipoSolicitacaoAutorizacaoEnum != null) {
			sqlWhereClause.addAndCondition("CDTIPOSOLAUTORIZACAO = ", solAutorizacao.tipoSolicitacaoAutorizacaoEnum.ordinal());
		}
		if (solAutorizacao.flAutorizado != null) {
			sqlWhereClause.addAndConditionForced("FLAUTORIZADO = ", solAutorizacao.flAutorizado);
		}
		if (!solAutorizacao.ignoreRemovidos) {
			sqlWhereClause.addAndCondition("(FLEXCLUIDO <> " + Sql.getValue(ValueUtil.VALOR_SIM) + " OR FLEXCLUIDO IS NULL)");
		}
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		SolAutorizacao solAutorizacao = (SolAutorizacao) domainFilter;
    	DaoUtil.addJoinEmpresa(sql, "EMPRESA");
    	DaoUtil.addJoinRepresentante(sql, "REPRESENTANTE");
		DaoUtil.addJoinCliente(sql);
		sql.append(" JOIN TBLVPPRODUTO produto ON ");
		sql.append("produto.CDEMPRESA = tb.CDEMPRESA AND produto.CDREPRESENTANTE = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante));
		sql.append(" AND produto.CDPRODUTO = tb.CDPRODUTO ");
		if (solAutorizacao != null && solAutorizacao.filterPermission) {
			DaoUtil.addJoinPermissaoSolAut(sql, "PERMISSAOSOLAUT", PermissaoSolAutService.getInstance().getPermissaoFilterBySession());
		}
		super.addJoin(domainFilter, sql);
	}

	@Override protected void addOrderBy(StringBuffer sql, BaseDomain domain) { super.addOrderByWithoutAlias(sql, domain); }

	public int countItensNaoAutorizadosOuPendentesByFilter(SolAutorizacao domainFilter) throws SQLException {
		StringBuffer sql = getSqCountPedido(domainFilter);
		sql.append(") TA WHERE TA.FLAUTORIZADO <> ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		return executeSqlByPedido(sql);
	}

	public int countItensAutorizadosOuPendentesByFilter(SolAutorizacao domainFilter) throws SQLException {
		StringBuffer sql = getSqCountPedido(domainFilter);
		sql.append(") TA WHERE TA.FLAUTORIZADO <> ").append(Sql.getValue(ValueUtil.VALOR_NAO));
		return executeSqlByPedido(sql);
	}
	
	public int countItensAutorizadosOuPendentesSimilaresByFilter(SolAutorizacao domainFilter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) as qtde FROM ( ")
			.append("SELECT MAX(tb.CDSOLAUTORIZACAO) as MAXCDSOLAUTORIZACAO, tb.FLAUTORIZADO ")
			.append("FROM TBLVPSOLAUTORIZACAO tb ")
			.append(" JOIN TBLVPPRODUTO prod ON ")
			.append(" prod.CDEMPRESA = tb.CDEMPRESA AND")
			.append(" prod.CDREPRESENTANTE = tb.CDREPRESENTANTE AND")
			.append(" prod.CDPRODUTO = tb.CDPRODUTO");
		addWhereByExample(domainFilter, sql);
		sql.append(" AND ((tb.FLAUTORIZADO = 'S' AND tb.FLAGRUPADORSIMILARIDADE = 'S') OR COALESCE(tb.FLAUTORIZADO, '') = '')")	
		.append(" GROUP BY tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.FLORIGEMPEDIDO, tb.NUPEDIDO, tb.CDCLIENTE, tb.CDPRODUTO, tb.FLTIPOITEMPEDIDO, tb.CDTIPOSOLAUTORIZACAO, tb.FLAUTORIZADO ")
		.append(") TA WHERE TA.FLAUTORIZADO <> ").append(Sql.getValue(ValueUtil.VALOR_NAO));
		return executeSqlByPedido(sql);
	}

	public int countItensByFilter(SolAutorizacao domainFilter, final String flAutorizado) throws SQLException {
		StringBuffer sql = getSqCountPedido(domainFilter);
		sql.append(") TA WHERE TA.FLAUTORIZADO = ").append(Sql.getValue(flAutorizado));
		return executeSqlByPedido(sql);
	}

	private int executeSqlByPedido(StringBuffer sql) throws SQLException {
		return getInt(sql.toString());
	}

	private StringBuffer getSqCountPedido(SolAutorizacao domainFilter) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) as qtde FROM ( ")
			.append("SELECT MAX(tb.CDSOLAUTORIZACAO), tb.FLAUTORIZADO ")
			.append("FROM TBLVPSOLAUTORIZACAO tb ");
			addWhereByExample(domainFilter, sql);
		sql.append(" GROUP BY tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.FLORIGEMPEDIDO, tb.NUPEDIDO, tb.CDCLIENTE, tb.CDPRODUTO, tb.FLTIPOITEMPEDIDO, tb.CDTIPOSOLAUTORIZACAO ");
		return sql;
	}

	public void updateFlAtualizadoByExample(SolAutorizacao domainFilter) {
    	try {
		    StringBuffer sql = getSqlBuffer();
		    sql.append(" UPDATE ").append(tableName).append(" SET FLVISUALIZADO = ").append(Sql.getValue(domainFilter.flVisualizado))
		    .append(", FLTIPOALTERACAO = ").append(Sql.getValue(SolAutorizacao.FLTIPOALTERACAO_ALTERADO));
		    addWhereDeleteByExample(domainFilter, sql);
		    executeUpdate(sql.toString());
	    } catch (Throwable e) {
		    ExceptionUtil.handle(e);
	    }
	}

	public boolean deleteAllByItemPedido(SolAutorizacao solAutorizacao) {
		try {
			StringBuffer sql = getSqlBuffer();
			sql.append(" UPDATE ").append(tableName).append(" SET FLEXCLUIDO = ").append(Sql.getValue(solAutorizacao.flExcluido))
			.append(", FLTIPOALTERACAO = ").append(Sql.getValue(SolAutorizacao.FLTIPOALTERACAO_ALTERADO));
			addWhereDeleteByExample(solAutorizacao, sql);
			executeUpdate(sql.toString());
			return true;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}
	
	public SolAutorizacao getSolAutorizacao(SolAutorizacao solAutorizacao) throws SQLException {
		maxAutorizacao = solAutorizacao.maxAutorizacao;
		try {
			Vector solAutorizacaoList = findAllByExample(solAutorizacao);
			if (solAutorizacaoList.size() > 0) return (SolAutorizacao) solAutorizacaoList.items[0];
		} catch (SQLException e) {
			throw e;
		} finally {
			maxAutorizacao = false;
		}
		return null;
	}
	
	@Override
	public Vector findAllByExample(BaseDomain domain) throws SQLException {
		SolAutorizacao solAutorizacao = (SolAutorizacao) domain;
		maxAutorizacao = solAutorizacao.maxAutorizacao;
		try {
			return super.findAllByExample(domain);
		} finally {
			maxAutorizacao = false;
		}
	}
	
	@Override
	protected void addGroupBy(StringBuffer sql) throws SQLException {
		if (maxAutorizacao) {
			sql.append(" GROUP BY tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.FLORIGEMPEDIDO, tb.NUPEDIDO, tb.CDCLIENTE, tb.CDPRODUTO");
		} else {
			super.addGroupBy(sql);
		}
	}

}
