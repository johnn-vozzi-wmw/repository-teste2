package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueDisponivel;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class FotoProdutoEmpDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoProdutoEmp();
	}

	private static FotoProdutoEmpDao instance;
	
	public FotoProdutoEmpDao() {
		super(FotoProdutoEmp.TABLE_NAME);
	}
	
	public static FotoProdutoEmpDao getInstance() {
		if (instance == null) {
			instance = new FotoProdutoEmpDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		FotoProdutoEmp fotoProdutoEmp = new FotoProdutoEmp();
		fotoProdutoEmp.rowKey = rs.getString("rowKey");
		fotoProdutoEmp.cdEmpresa = rs.getString("cdEmpresa");
		fotoProdutoEmp.cdProduto = rs.getString("cdProduto");
		fotoProdutoEmp.nmFoto = rs.getString("nmFoto");
		fotoProdutoEmp.nuTamanho = rs.getInt("nuTamanho");
		fotoProdutoEmp.dtModificacao = rs.getDate("dtModificacao");
		fotoProdutoEmp.hrModificacao = rs.getString("hrModificacao");
		return fotoProdutoEmp;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,")
		.append(" tb.CDEMPRESA,")
		.append(" tb.CDPRODUTO,")
		.append(" tb.NMFOTO,")
		.append(" tb.NUTAMANHO,")
		.append(" tb.DTMODIFICACAO,")
		.append(" tb.HRMODIFICACAO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoProdutoEmp fotoProduto = (FotoProdutoEmp) domain;
        sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoProduto.nuTamanho)).append(",");
        sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoProduto.dtModificacao)).append(",");
        sql.append(" HRMODIFICACAO = ").append(Sql.getValue(fotoProduto.hrModificacao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoProduto.flTipoAlteracao)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(fotoProduto.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoProduto.cdUsuario));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoProdutoEmp fotoProdutoEmp = (FotoProdutoEmp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoProdutoEmp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", fotoProdutoEmp.cdProduto);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoProdutoEmp.nmFoto);
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", fotoProdutoEmp.flTipoAlteracao);
		sql.append(sqlWhereClause.getSql());
	}
	
	public Vector findCdEmpresaList() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDEMPRESA FROM TBLVPFOTOPRODUTOEMP GROUP BY CDEMPRESA");
		return findColumn(FotoProdutoEmp.NMCOLUNA_CDEMPRESA, sql);
	}
	
	public Vector findAllNaoAlterados() throws SQLException {
		return findAllNaoAlterados(null);
	}

	public Vector findAllNaoAlterados(String cdEmpresa) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDEMPRESA, CDPRODUTO, NMFOTO, NUCARIMBO, DTMODIFICACAO, HRMODIFICACAO FROM ").append(tableName);
		addWhereAllNaoAlterados(sql);
		if (ValueUtil.isNotEmpty(cdEmpresa)) {
			sql.append(" AND CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		}
		Vector list = new Vector(256);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				FotoProdutoEmp fotoProdutoEmp = new FotoProdutoEmp();
				fotoProdutoEmp.cdEmpresa = rs.getString(1);
				fotoProdutoEmp.cdProduto = rs.getString(2);
				fotoProdutoEmp.nmFoto = rs.getString(3);
				fotoProdutoEmp.nuCarimbo = rs.getInt(4);
				fotoProdutoEmp.dtModificacao = rs.getDate(5);
				fotoProdutoEmp.hrModificacao = rs.getString(6);
				list.addElement(fotoProdutoEmp);
			}
		}
		return list;
	}
	
	public Hashtable findAllFotoProdutoEmp() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDEMPRESA, CDPRODUTO, NMFOTO FROM TBLVPFOTOPRODUTOEMP")
		.append(" WHERE CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
		Hashtable hash = new Hashtable(100);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				if (hash.exists(rs.getString("cdProduto"))) {
					continue;
				}
				hash.put(rs.getString("cdProduto"), rs.getString("cdEmpresa") + "/" + rs.getString("nmFoto"));
			}
		}
		return hash;
	}
	
	public int countFotoCargaInicial() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(*) NUM FROM ").append(tableName).append(" WHERE FLTIPOALTERACAO IS NOT NULL ");
		return getInt(sql.toString());
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
	
	public int countFotoProdutoByAgrupadorGrade(FotoProdutoEmp filter) throws SQLException {
		StringBuffer sql = getSqlFotoProdutoAgrupadorGrade(filter, true);
		return getInt(sql.toString());
	}
	
	public Vector findAllFotoProdutoByAgrupadorGrade(FotoProdutoEmp filter) throws SQLException {
		StringBuffer sql = getSqlFotoProdutoAgrupadorGrade(filter, false);
		return findAll(filter, sql.toString());
	}

	private StringBuffer getSqlFotoProdutoAgrupadorGrade(FotoProdutoEmp filter, boolean count) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
		if (count) {
			sql.append("COUNT(1) ");
		} else {
			addSelectColumns(filter, sql);
		}
        sql.append(" FROM TBLVPFOTOPRODUTOEMP TB ")
        .append(" JOIN TBLVPPRODUTO PROD ON ")
        .append(" TB.CDEMPRESA = PROD.CDEMPRESA AND ")
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
		sqlWhereClause.addAndCondition(" PROD.CDEMPRESA = ", filter.cdEmpresa);
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
    	if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
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
			if (usaMultiplicaCondicaoEstoquePrevisto) {
				sqlWhereClause.addStartOrMultipleCondition();	
			}
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".QTESTOQUEPREVISTO > 0");
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".DTESTOQUEPREVISTO IS NOT NULL");
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_ESTOQUE_ERP+".DTESTOQUEPREVISTO >= " , DateUtil.getCurrentDate());
			if (usaMultiplicaCondicaoEstoquePrevisto) {
				sqlWhereClause.addEndMultipleCondition();
			}
		}
		sqlWhereClause.addEndMultipleCondition();
	}
	
}
