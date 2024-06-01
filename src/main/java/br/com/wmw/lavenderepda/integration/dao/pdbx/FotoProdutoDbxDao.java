package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueDisponivel;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class FotoProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoProduto();
	}

    private static FotoProdutoDbxDao instance;

    public FotoProdutoDbxDao() {
        super(FotoProduto.TABLE_NAME);
    }

    public static FotoProdutoDbxDao getInstance() {
        if (instance == null) {
			instance = new FotoProdutoDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        FotoProduto fotoProduto = new FotoProduto();
        fotoProduto.rowKey = rs.getString("rowkey");
        fotoProduto.cdProduto = rs.getString("cdProduto");
        fotoProduto.nmFoto = rs.getString("nmFoto");
        fotoProduto.nuTamanho = rs.getInt("nuTamanho");
        fotoProduto.dtModificacao = rs.getDate("dtModificacao");
        fotoProduto.hrModificacao = rs.getString("hrModificacao");
        fotoProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        fotoProduto.nuCarimbo = rs.getInt("nuCarimbo");
        fotoProduto.cdUsuario = rs.getString("cdUsuario");
        return fotoProduto;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.NMFOTO,");
        sql.append(" tb.NUTAMANHO,");
        sql.append(" tb.DTMODIFICACAO,");
        sql.append(" tb.HRMODIFICACAO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.nuCarimbo,");
        sql.append(" tb.CDUSUARIO");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDPRODUTO,");
        sql.append(" NMFOTO,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" HRMODIFICACAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" nuCarimbo,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoProduto fotoProduto = (FotoProduto) domain;
        sql.append(Sql.getValue(fotoProduto.cdProduto)).append(",");
        sql.append(Sql.getValue(fotoProduto.nmFoto)).append(",");
        sql.append(Sql.getValue(fotoProduto.nuTamanho)).append(",");
        sql.append(Sql.getValue(fotoProduto.dtModificacao)).append(",");
        sql.append(Sql.getValue(fotoProduto.hrModificacao)).append(",");
        sql.append(Sql.getValue(fotoProduto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(fotoProduto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(fotoProduto.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoProduto fotoProduto = (FotoProduto) domain;
        sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoProduto.nuTamanho)).append(",");
        sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoProduto.dtModificacao)).append(",");
        sql.append(" HRMODIFICACAO = ").append(Sql.getValue(fotoProduto.hrModificacao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoProduto.flTipoAlteracao)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(fotoProduto.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoProduto.cdUsuario));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoProduto fotoProduto = (FotoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDPRODUTO = ", fotoProduto.cdProduto);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoProduto.nmFoto);
		sqlWhereClause.addAndCondition("NUCARIMBO = ", fotoProduto.nuCarimbo);
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", fotoProduto.flTipoAlteracao);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public Vector findAllNaoAlterados() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		sql.append(" cdProduto, nmFoto, flTipoAlteracao, nuCarimbo, dtModificacao, hrModificacao ");
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereAllNaoAlterados(sql);
		addOrderBy(sql, null);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector fotoProdutoList = new Vector(256);
			FotoProduto fotoProduto;
			while (rs.next()) {
				fotoProduto = new FotoProduto();
				fotoProduto.cdProduto = rs.getString(1);
				fotoProduto.nmFoto = rs.getString(2);
				fotoProduto.flTipoAlteracao = rs.getString(3);
				fotoProduto.nuCarimbo = rs.getInt(4);
				fotoProduto.dtModificacao = rs.getDate(5);
				fotoProduto.hrModificacao = rs.getString(6);
				fotoProdutoList.addElement(fotoProduto);
			}
			return fotoProdutoList;
		}
	}
	
	public Hashtable findAllFotoProduto() throws SQLException {
		Hashtable hash = new Hashtable(100);
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDPRODUTO, NMFOTO FROM ")
			.append(tableName);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				if (hash.exists(rs.getString(1))) {
					continue;
				}
				hash.put(rs.getString(1), rs.getString(2));
			}
		}
		return hash;
	}
	
	public void updateAllFlTipoAlteracaoInserido() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName)
			.append(" SET FLTIPOALTERACAO = '").append(BaseDomain.FLTIPOALTERACAO_INSERIDO)
			.append("' WHERE FLTIPOALTERACAO IS NOT NULL ");
		updateAll(sql.toString());
	}
	
	public void updateResetReceberFotosNovamente() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_ALTERADO);
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_INSERIDO);
	}

	private void updateResetFlTipoAlteracao(String flTipoAlteracao, String flTipoAlteracaoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName)
		.append(" SET FLTIPOALTERACAO = '").append(flTipoAlteracao)
		.append("' WHERE FLTIPOALTERACAO = '").append(flTipoAlteracaoFilter).append("'");
		updateAll(sql.toString());
	}
	
	public int countFotoCargaInicial() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(*) NUM FROM ").append(tableName).append(" WHERE FLTIPOALTERACAO IS NOT NULL ");
		return getInt(sql.toString());
	}
	
	public int countFotoProdutoByAgrupadorGrade(FotoProduto filter) throws SQLException {
		StringBuffer sql = getSqlFotoProdutoAgrupadorGrade(filter, true);
		return getInt(sql.toString());
	}
	
	public Vector findAllFotoProdutoByAgrupadorGrade(FotoProduto filter) throws SQLException {
		StringBuffer sql = getSqlFotoProdutoAgrupadorGrade(filter, false);
		return findAll(filter, sql.toString());
	}

	private StringBuffer getSqlFotoProdutoAgrupadorGrade(FotoProduto filter, boolean count) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		if (count) {
			sql.append("COUNT(1) ");
		} else {
			addSelectColumns(filter, sql);
		}
        sql.append(" FROM TBLVPFOTOPRODUTO TB ")
        .append(" JOIN TBLVPPRODUTO PROD ON ")
        .append(" PROD.CDPRODUTO = TB.CDPRODUTO")
        .append(" JOIN TBLVPPRODUTOGRADE pg ON ")
		.append(" prod.CDEMPRESA = pg.CDEMPRESA AND ")
		.append(" prod.CDREPRESENTANTE = pg.CDREPRESENTANTE AND ")
		.append(" prod.CDPRODUTO = pg.CDPRODUTO AND ")
		.append(" pg.CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
        ProdutoBase ultimoProdutoFilter = SessionLavenderePda.getUltimoProdutoFilter();
        if (ultimoProdutoFilter != null) {
    		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && ultimoProdutoFilter.itemTabelaPreco != null) {
    			DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter.itemTabelaPreco, false, false);
    			sql.append(" AND ").append(DaoUtil.ALIAS_ITEMTABELAPRECO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
    		}
    		if (LavenderePdaConfig.filtraProdutoPorTipoPedido && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdTipoPedidoFilter)) {
    			DaoUtil.addJoinProdutoTipoPedido(sql, DaoUtil.ALIAS_PRODUTO, ultimoProdutoFilter);
    			sql.append(" AND ").append(DaoUtil.ALIAS_PRODUTOTIPOPED).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
    		}
    		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && ultimoProdutoFilter.cdPlataformaVendaFilter != null) {
    			DaoUtil.addJoinPlataformaVendaProduto(sql, ultimoProdutoFilter.cdPlataformaVendaFilter, DaoUtil.ALIAS_PRODUTO);
    			sql.append(" AND ").append(DaoUtil.ALIAS_PLATAFORMAVENDAPRODUTO).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
    		}
    		if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto() && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais() && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdCentroCustoProdFilter)) {
    			DaoUtil.addJoinCentroCustoProd(sql, ultimoProdutoFilter, DaoUtil.ALIAS_PRODUTO);
    			sql.append(" AND ").append(DaoUtil.ALIAS_CENTROCUSTOPROD).append(".CDUSUARIO = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario));
    		}
    		if (ValueUtil.isNotEmpty(ultimoProdutoFilter.cdStatusEstoque)) {
    			if (ultimoProdutoFilter.estoque != null) {
    				DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, ultimoProdutoFilter.estoque.cdLocalEstoque, Estoque.FLORIGEMESTOQUE_ERP, DaoUtil.ALIAS_PRODUTO);
    				DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, ultimoProdutoFilter.estoque.cdLocalEstoque, Estoque.FLORIGEMESTOQUE_PDA, DaoUtil.ALIAS_PRODUTO);
    			} else {
					DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_ERP, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_ERP, DaoUtil.ALIAS_PRODUTO);
					DaoUtil.addJoinEstoque(sql, DaoUtil.ALIAS_ESTOQUE_PDA, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_PDA, DaoUtil.ALIAS_PRODUTO);
    			}
    		}
    	}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			DaoUtil.addJoinProdutoClienteExclusivo(sql, SessionLavenderePda.getCliente().cdCliente, DaoUtil.ALIAS_PRODUTO);
		}
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" PROD.CDEMPRESA = ", SessionLavenderePda.cdEmpresa);
		sqlWhereClause.addAndCondition(" PROD.CDREPRESENTANTE = ", SessionLavenderePda.usuarioPdaRep.cdRepresentante);
		sqlWhereClause.addAndCondition(" PROD.DSAGRUPADORGRADE = ", filter.dsAgrupadorGrade);
		sqlWhereClause.addAndCondition(" pg.CDITEMGRADE1 = ", filter.cdItemGrade1);
		if (ultimoProdutoFilter != null && ValueUtil.isNotEmpty(ultimoProdutoFilter.cdStatusEstoque)) {
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
	    	.append(" CDEMPRESA = PROD.CDEMPRESA AND ")
	    	.append(" CDREPRESENTANTE = PROD.CDREPRESENTANTE AND ")
	    	.append(" CDPRODUTO = PROD.CDPRODUTO AND ")
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
		return sql;
	}

//	public Vector findAllVerificacaoFotosBaixadas() throws SQLException {
//		StringBuffer sql = getSqlBuffer();
//		sql.append(" select cdProduto, nmFoto, dtModificacao from ");
//		sql.append(tableName)/*.append(" tb where fltipoalteracao = ");
//		sql.append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
//		sql.append(" or ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_INSERIDO));
//		sql.append(" or fltipoalteracao is null")*/;
//		ResultSet rs = executeQuery(sql.toString());
//		try {
//			Vector fotoProdutoList = new Vector(0);
//			while (rs.next()) {
//				FotoProduto fotoProduto = new FotoProduto();
//				fotoProduto.cdProduto = rs.getString(1);
//				fotoProduto.nmFoto = rs.getString(2);
//				fotoProduto.dtModificacao = rs.getDate(3);
//				fotoProdutoList.addElement(fotoProduto);
//			}
//			return fotoProdutoList;
//		} finally {
//			CrudDbxDao.close(rs);
//		}
//	}
	
	public void addWhereProdutoGrade5(ProdutoBase filter, SqlWhereClause sqlWhereClause, String alias) {
    	if (LavenderePdaConfig.usaFiltroFornecedor) {
    		sqlWhereClause.addAndConditionEquals(alias + ".CDFORNECEDOR", filter.cdFornecedor);
    	}
    	if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
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
