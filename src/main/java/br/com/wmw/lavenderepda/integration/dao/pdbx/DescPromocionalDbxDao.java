package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class DescPromocionalDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescPromocional();
	}

    private static DescPromocionalDbxDao instance;
	
    public DescPromocionalDbxDao() {
        super(DescPromocional.TABLE_NAME);
    }
    
    public static DescPromocionalDbxDao getInstance() {
        return (instance == null) ? instance = new DescPromocionalDbxDao() : instance;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	
    @Override
    protected String getKeyCache(ResultSet rs, BaseDomain domain) throws SQLException {
    	return domain.getRowKey();
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescPromocional descPromocional = new DescPromocional();
        descPromocional.rowKey = rs.getString(1);
        descPromocional.cdEmpresa = rs.getString(2);
        descPromocional.cdRepresentante = rs.getString(3);
        descPromocional.cdCliente = rs.getString(4);
        descPromocional.cdProduto = rs.getString(5);
        descPromocional.cdGrupoDescCli = rs.getString(6);
        descPromocional.cdGrupoDescProd = rs.getString(7);
        descPromocional.nuPromocao = rs.getInt(8);
        descPromocional.cdCondicaoComercial = rs.getString(9);
        descPromocional.dsDescPromocional = rs.getString(10);
        descPromocional.vlProdutoFinal = rs.getDouble(11);
        descPromocional.vlDescontoProduto = rs.getDouble(12);
        descPromocional.vlPctDescontoProduto = rs.getDouble(13);
        descPromocional.dtInicial = rs.getDate(14);
        descPromocional.dtFinal = rs.getDate(15);
        descPromocional.nuCarimbo = rs.getInt(16);
        descPromocional.flTipoAlteracao = rs.getString(17);
        descPromocional.cdUsuario = rs.getString(18);
        descPromocional.qtItem = rs.getInt(19);
        descPromocional.cdTabelaPreco = rs.getString(20);
        descPromocional.qtMinVenda = rs.getDouble(21);
        descPromocional.qtMinGrade1 = rs.getDouble(22);
        descPromocional.qtMinGrade2 = rs.getDouble(23);
        descPromocional.vlPctComissao = rs.getDouble(24);
        descPromocional.vlPctRentabilidade = rs.getDouble(25);
        descPromocional.cdLocal = rs.getString(26);
        descPromocional.cdUnidade = rs.getString(27);
        descPromocional.cdCondicaoPagamento = rs.getString(28);
        descPromocional.cdTipoFrete = rs.getString(29);
        descPromocional.cdLocalEstoque = rs.getString(30);
        descPromocional.cdUf = rs.getString(31);
        int i = 32;
        if (LavenderePdaConfig.usaQuantidadeMinimaDescPromocional) {
        	descPromocional.flValidaQtMinDescPromocional = rs.getString(i++);
        }
		descPromocional.cdTipoPedido = rs.getString(i);
        return descPromocional;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        getColumns(sql, null);
    }
    
    protected void addCacheColumns(StringBuffer sql) {
    	getColumns(sql, null);
    }

	private void getColumns(StringBuffer sql, String aliasTable) {
		if (ValueUtil.isNotEmpty(aliasTable)) {
			aliasTable = " "+aliasTable+".";
		} else {
			aliasTable = " ";
		}
		sql.append(aliasTable).append("CDEMPRESA,");
        sql.append(aliasTable).append("CDREPRESENTANTE,");
        sql.append(aliasTable).append("CDCLIENTE,");
        sql.append(aliasTable).append("CDPRODUTO,");
        sql.append(aliasTable).append("CDGRUPODESCCLI,");
        sql.append(aliasTable).append("CDGRUPODESCPROD,");
        sql.append(aliasTable).append("NUPROMOCAO,");
        sql.append(aliasTable).append("CDCONDICAOCOMERCIAL,");
        sql.append(aliasTable).append("DSDESCPROMOCIONAL,");
        sql.append(aliasTable).append("VLPRODUTOFINAL,");
        sql.append(aliasTable).append("VLDESCONTOPRODUTO,");
        sql.append(aliasTable).append("VLPCTDESCONTOPRODUTO,");
        sql.append(aliasTable).append("DTINICIAL,");
        sql.append(aliasTable).append("DTFINAL,");
        sql.append(aliasTable).append("NUCARIMBO,");
        sql.append(aliasTable).append("FLTIPOALTERACAO,");
        sql.append(aliasTable).append("CDUSUARIO,");
        sql.append(aliasTable).append("QTITEM,");
        sql.append(aliasTable).append("CDTABELAPRECO,");
        sql.append(aliasTable).append("QTMINVENDA,");
        sql.append(aliasTable).append("QTMINGRADE1,");
        sql.append(aliasTable).append("QTMINGRADE2,");
        sql.append(aliasTable).append("VLPCTCOMISSAO,");
        sql.append(aliasTable).append("VLPCTRENTABILIDADE,");
        sql.append(aliasTable).append("CDLOCAL,");
        sql.append(aliasTable).append("CDUNIDADE,");
        sql.append(aliasTable).append("CDCONDICAOPAGAMENTO,");
        sql.append(aliasTable).append("CDTIPOFRETE,");
        sql.append(aliasTable).append("CDLOCALESTOQUE,");
        sql.append(aliasTable).append("CDUF");
        if (LavenderePdaConfig.usaQuantidadeMinimaDescPromocional) {
        	sql.append(" , CASE WHEN "+aliasTable+"CDPRODUTO <> '0' AND "+aliasTable+"CDCLIENTE = '0' AND "+aliasTable+"CDGRUPODESCCLI = '0' AND "+aliasTable+"CDGRUPODESCPROD = '0' AND "+aliasTable+"CDTABELAPRECO <> '0' AND "+aliasTable+"VLPRODUTOFINAL IS NOT NULL")
        	.append(" THEN 'S' END AS FLVALIDAQTMINDESCPROMOCIONAL");
        }
        sql.append(",").append(aliasTable).append("CDTIPOPEDIDO");
	}
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDGRUPODESCCLI,");
        sql.append(" CDGRUPODESCPROD,");
        sql.append(" NUPROMOCAO,");
        sql.append(" DSDESCPROMOCIONAL,");
        sql.append(" VLPRODUTOFINAL,");
        sql.append(" VLDESCONTOPRODUTO,");
        sql.append(" VLPCTDESCONTOPRODUTO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescPromocional descPromocional = (DescPromocional) domain;
        sql.append(Sql.getValue(descPromocional.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descPromocional.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descPromocional.cdCliente)).append(",");
        sql.append(Sql.getValue(descPromocional.cdProduto)).append(",");
        sql.append(Sql.getValue(descPromocional.cdGrupoDescCli)).append(",");
        sql.append(Sql.getValue(descPromocional.cdGrupoDescProd)).append(",");
        sql.append(Sql.getValue(descPromocional.nuPromocao)).append(",");
        sql.append(Sql.getValue(descPromocional.dsDescPromocional)).append(",");
        sql.append(Sql.getValue(descPromocional.vlProdutoFinal)).append(",");
        sql.append(Sql.getValue(descPromocional.vlDescontoProduto)).append(",");
        sql.append(Sql.getValue(descPromocional.vlPctDescontoProduto)).append(",");
        sql.append(Sql.getValue(descPromocional.dtInicial)).append(",");
        sql.append(Sql.getValue(descPromocional.dtFinal)).append(",");
        sql.append(Sql.getValue(descPromocional.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(descPromocional.nuCarimbo)).append(",");
        sql.append(Sql.getValue(descPromocional.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(descPromocional.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescPromocional descPromocional = (DescPromocional) domain;
        sql.append(" DSDESCPROMOCIONAL = ").append(Sql.getValue(descPromocional.dsDescPromocional)).append(",");
        sql.append(" VLPRODUTOFINAL = ").append(Sql.getValue(descPromocional.vlProdutoFinal)).append(",");
        sql.append(" VLDESCONTOPRODUTO = ").append(Sql.getValue(descPromocional.vlDescontoProduto)).append(",");
        sql.append(" VLPCTDESCONTOPRODUTO = ").append(Sql.getValue(descPromocional.vlPctDescontoProduto)).append(",");
        sql.append(" DTFINAL = ").append(Sql.getValue(descPromocional.dtFinal)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(descPromocional.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descPromocional.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descPromocional.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescPromocional descPromocional = (DescPromocional) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descPromocional.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descPromocional.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", descPromocional.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", descPromocional.cdProduto);
		sqlWhereClause.addAndCondition("CDGRUPODESCCLI = ", descPromocional.cdGrupoDescCli);
		sqlWhereClause.addAndCondition("CDGRUPODESCPROD = ", descPromocional.cdGrupoDescProd);
		sqlWhereClause.addAndCondition("DTINICIAL <= ", descPromocional.dtInicial);
		sqlWhereClause.addAndCondition("DTFINAL >= ", descPromocional.dtFinal);
		sqlWhereClause.addAndCondition("QTITEM = ", descPromocional.qtItem);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", descPromocional.cdTabelaPreco);
		sqlWhereClause.addAndCondition("CDCONDICAOCOMERCIAL = ", descPromocional.cdCondicaoComercial);
		sqlWhereClause.addAndCondition("CDLOCAL = ", descPromocional.cdLocal);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", descPromocional.cdTipoPedido);
		sqlWhereClause.addAndCondition("CDUNIDADE = ", descPromocional.cdUnidade);
		sqlWhereClause.addAndCondition("CDCONDICAOPAGAMENTO = ", descPromocional.cdCondicaoPagamento);
		sqlWhereClause.addAndCondition("CDTIPOFRETE = ", descPromocional.cdTipoFrete);
		sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", descPromocional.cdLocalEstoque);
		sqlWhereClause.addAndCondition("CDUF = ", descPromocional.cdUf);
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllByExampleCombo(BaseDomain domain) throws java.sql.SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT ");
    	addSelectColumns(domain, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExampleCombo(domain, sql);
    	sql.append(" GROUP BY DSDESCPROMOCIONAL, NUPROMOCAO");
		sql.append(" ORDER BY DSDESCPROMOCIONAL");
    	return findAll(domain, sql.toString());
    }
    
    private void addWhereByExampleCombo(BaseDomain domain, StringBuffer sql) {
    	DescPromocional descPromocional = (DescPromocional) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descPromocional.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descPromocional.cdRepresentante);
		sqlWhereClause.addAndOrCondition("CDCLIENTE = ", descPromocional.cdCliente, DescPromocional.CD_CHAVE_VAZIO); 
		sqlWhereClause.addAndOrCondition("CDGRUPODESCCLI = ", descPromocional.cdGrupoDescCli, DescPromocional.CD_CHAVE_VAZIO);
		sqlWhereClause.addAndCondition("DTINICIAL <= ", descPromocional.dtInicial);
		sqlWhereClause.addAndCondition("DTFINAL >= ", descPromocional.dtFinal);
		sqlWhereClause.addAndOrCondition("CDTABELAPRECO = ", descPromocional.cdTabelaPreco, DescPromocional.CD_CHAVE_VAZIO);
		sqlWhereClause.addAndOrCondition("CDCONDICAOCOMERCIAL = ", descPromocional.cdCondicaoComercial, DescPromocional.CD_CHAVE_VAZIO);
		sqlWhereClause.addAndCondition("DSDESCPROMOCIONAL = ", descPromocional.dsDescPromocional);
		sqlWhereClause.addAndCondition("NUPROMOCAO = ", descPromocional.nuPromocao);
		sqlWhereClause.addAndOrCondition("CDTIPOPEDIDO = ", descPromocional.cdTipoPedido, DescPromocional.CD_CHAVE_VAZIO);
		sqlWhereClause.addAndCondition("CDUNIDADE = ", descPromocional.cdUnidade);
		sqlWhereClause.addAndOrCondition("CDCONDICAOPAGAMENTO = ", descPromocional.cdCondicaoPagamento, DescPromocional.CD_CHAVE_VAZIO);
		sqlWhereClause.addAndOrCondition("CDTIPOFRETE = ", descPromocional.cdTipoFrete, DescPromocional.CD_CHAVE_VAZIO);
		sqlWhereClause.addAndOrCondition("CDLOCALESTOQUE = ", descPromocional.cdLocalEstoque, DescPromocional.CD_CHAVE_VAZIO);
		sqlWhereClause.addAndOrCondition("CDUF = ", descPromocional.cdUf, DescPromocional.CD_CHAVE_VAZIO);
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findDescPromocionalPrior(DescPromocional descPromocional) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ")
		.append(" tb.rowKey, ");
		getColumns(sql, "tb");
		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoinGrupoDescCli(sql, descPromocional);
		addJoinGrupoDescProd(sql, descPromocional);
		sql.append(" JOIN ( ")
				.append(" SELECT TB.CDEMPRESA, TB.CDREPRESENTANTE, TB.CDGRUPODESCCLI, TB.CDGRUPODESCPROD ")
				.append(" FROM TBLVPDESCPROMOCIONAL TB ");
			addJoinGrupoDescCli(sql, descPromocional);
			addJoinGrupoDescProd(sql, descPromocional);
		SqlWhereClause sqlWhereClause = addWherePrioridadeDescInter(descPromocional);
		sql.append(sqlWhereClause.getSql())
				.append(" ORDER BY GRUPODESCCLI.NUPRIORIDADE ASC, GRUPODESCPROD.NUPRIORIDADE ASC ").append(" LIMIT 1 ")
				.append(" ) AUX ON ").append(" AUX.CDEMPRESA = TB.CDEMPRESA ")
				.append(" AND AUX.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
				.append(" AND AUX.CDGRUPODESCCLI =  TB.CDGRUPODESCCLI ")
				.append(" AND AUX.CDGRUPODESCPROD =  TB.CDGRUPODESCPROD ");
		sql.append(sqlWhereClause.getSql())
		.append(" ORDER BY TB.QTITEM ASC, TB.DTINICIAL DESC ");
       return findAll(descPromocional, sql.toString());
    }

	private SqlWhereClause addWherePrioridadeDescInter(DescPromocional descPromocional) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", descPromocional.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", descPromocional.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.DTINICIAL <= ", descPromocional.dtInicial);
		sqlWhereClause.addAndCondition("TB.DTFINAL >= ", descPromocional.dtFinal);
		sqlWhereClause.addAndCondition("TB.NUPROMOCAO = ", descPromocional.nuPromocao);
		sqlWhereClause.addAndCondition("TB.CDTIPOPEDIDO = ", descPromocional.cdTipoPedido);
		sqlWhereClause.addAndCondition("GRUPODESCCLI.CDCLIENTE = ", descPromocional.cdCliente);
		sqlWhereClause.addAndCondition("GRUPODESCPROD.CDPRODUTO = ", descPromocional.cdProduto);
		return sqlWhereClause;
	} 
    
    private void addJoinGrupoDescCli(StringBuffer sql, final DescPromocional descPromocional) {
    	sql.append(" JOIN TBLVPGRUPODESCCLI GRUPODESCCLI ON")
    	.append(" GRUPODESCCLI.CDEMPRESA = TB.CDEMPRESA")
    	.append(" AND GRUPODESCCLI.CDREPRESENTANTE = TB.CDREPRESENTANTE")
    	.append(" AND GRUPODESCCLI.CDGRUPODESCCLI = TB.CDGRUPODESCCLI")
    	.append(" AND GRUPODESCCLI.CDCLIENTE = " + Sql.getValue(descPromocional.cdCliente));
    }
    
    private void addJoinGrupoDescProd(StringBuffer sql, final DescPromocional descPromocional) {
    	sql.append(" JOIN TBLVPGRUPODESCPROD GRUPODESCPROD ON")
    	.append(" GRUPODESCPROD.CDEMPRESA = TB.CDEMPRESA")
    	.append(" AND GRUPODESCPROD.CDREPRESENTANTE = TB.CDREPRESENTANTE")
    	.append(" AND GRUPODESCPROD.CDGRUPODESCPROD = TB.CDGRUPODESCPROD")
    	.append(" AND GRUPODESCPROD.CDPRODUTO = " + Sql.getValue(descPromocional.cdProduto));
    }

}