package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Pergunta;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespApp;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class PesquisaRespAppPdbxDao extends LavendereCrudPersonDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaRespApp();
	}

	private static PesquisaRespAppPdbxDao instance;

	public PesquisaRespAppPdbxDao() {
		super(PesquisaRespApp.TABLE_NAME);
	}

	public static PesquisaRespAppPdbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaRespAppPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaRespApp pesquisaRespApp = new PesquisaRespApp();
		pesquisaRespApp.rowKey = rs.getString(1);
		pesquisaRespApp.cdEmpresa = rs.getString(2);
		pesquisaRespApp.cdRepresentante = rs.getString(3);
		pesquisaRespApp.cdPesquisaApp = rs.getString(4);
		pesquisaRespApp.cdQuestionario = rs.getString(5);
		pesquisaRespApp.cdPergunta = rs.getString(6);
		pesquisaRespApp.cdResposta = rs.getString(7);
		pesquisaRespApp.cdCliente = rs.getString(8);
		pesquisaRespApp.dtResposta = rs.getDate(9);
		pesquisaRespApp.hrResposta = rs.getString(10);
		pesquisaRespApp.dsResposta = rs.getString(11);
		pesquisaRespApp.dsObservacao = rs.getString(12);
		return pesquisaRespApp;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" TB.ROWKEY,");
		sql.append(" TB.CDEMPRESA,");
		sql.append(" TB.CDREPRESENTANTE,");
		sql.append(" TB.CDPESQUISAAPP,");
		sql.append(" TB.CDQUESTIONARIO,");
		sql.append(" TB.CDPERGUNTA,");
		sql.append(" TB.CDRESPOSTA,");
		sql.append(" TB.CDCLIENTE,");
		sql.append(" TB.DTRESPOSTA,");
		sql.append(" TB.HRRESPOSTA,");
		sql.append(" TB.DSRESPOSTA,");
		sql.append(" TB.DSOBSERVACAO");
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		PesquisaRespApp pesquisaRespApp = (PesquisaRespApp) domain;
        sql.append(" DSRESPOSTA = ").append(Sql.getValue(pesquisaRespApp.dsResposta)).append(",");
        sql.append(" CDRESPOSTA = ").append(Sql.getValue(pesquisaRespApp.cdResposta)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(pesquisaRespApp.dsObservacao)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(pesquisaRespApp.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pesquisaRespApp.flTipoAlteracao)).append(",");
        sql.append(" DTRESPOSTA = ").append(Sql.getValue(pesquisaRespApp.dtResposta)).append(",");
        sql.append(" HRRESPOSTA = ").append(Sql.getValue(pesquisaRespApp.hrResposta)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(pesquisaRespApp.cdUsuario));
	}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		PesquisaRespApp pesquisaRespApp = (PesquisaRespApp) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", pesquisaRespApp.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", pesquisaRespApp.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDPESQUISAAPP = ", pesquisaRespApp.cdPesquisaApp);
		sqlWhereClause.addAndCondition("TB.CDQUESTIONARIO = ", pesquisaRespApp.cdQuestionario);
		sqlWhereClause.addAndCondition("TB.CDPERGUNTA = ", pesquisaRespApp.cdPergunta);
		sqlWhereClause.addAndCondition("TB.CDRESPOSTA = ", pesquisaRespApp.cdResposta);
		sqlWhereClause.addAndCondition("TB.CDCLIENTE = ", pesquisaRespApp.cdCliente);
		sqlWhereClause.addAndCondition("TB.DTRESPOSTA = ", pesquisaRespApp.dtResposta);
		sqlWhereClause.addAndLikeCondition("TB.DSRESPOSTA", pesquisaRespApp.dsResposta, false);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		PesquisaRespApp pesquisaRespApp = (PesquisaRespApp) domain;
		if ("ROWKEY".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.getRowKey()); }
		if ("CDEMPRESA".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.cdEmpresa); }
		if ("CDREPRESENTANTE".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.cdRepresentante); }
		if ("CDPESQUISAAPP".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.cdPesquisaApp); }
		if ("CDQUESTIONARIO".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.cdQuestionario); }
		if ("CDPERGUNTA".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.cdPergunta); }
		if ("CDRESPOSTA".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.cdResposta); }
		if ("CDCLIENTE".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.cdCliente); }
		if ("DTRESPOSTA".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.dtResposta); }
		if ("HRRESPOSTA".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.hrResposta); }
		if ("DSRESPOSTA".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.dsResposta); }
		if ("DSOBSERVACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(pesquisaRespApp.dsObservacao); }
		if ("NUCARIMBO".equalsIgnoreCase(columnName)) { return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);	}
		if ("FLTIPOALTERACAO".equalsIgnoreCase(columnName)) { return Sql.getValue(domain.flTipoAlteracao); }
		if ("CDUSUARIO".equalsIgnoreCase(columnName)) { return Sql.getValue(domain.cdUsuario);	}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}

	@Override
	protected void beforeInsert(BaseDomain domain) {
		super.beforeInsert(defineDtHr(domain));
	}
	
	@Override
	protected void beforeUpdate(BaseDomain domain) {
		super.beforeUpdate(defineDtHr(domain));
	}

	private PesquisaRespApp defineDtHr(BaseDomain domain) {
		PesquisaRespApp pesquisaRespApp = (PesquisaRespApp) domain;
		pesquisaRespApp.dtResposta = DateUtil.getCurrentDate();
		pesquisaRespApp.hrResposta = TimeUtil.getCurrentTimeHHMMSS();
		return pesquisaRespApp;
	}	
	
	public PesquisaRespApp findLast(BaseDomain baseDomain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(baseDomain, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(baseDomain, sql);
    	sql.append(" order by cast(tb.CDPERGUNTA as unsigned) desc ");
    	return (PesquisaRespApp) findAll(baseDomain, sql.toString()).items[0];
	}
	
	public PesquisaRespApp findLastPesquisaRespApp(BaseDomain baseDomain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(baseDomain, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(baseDomain, sql);
    	sql.append(" order by cast(tb.CDPESQUISAAPP as unsigned) desc ");
    	Vector result = findAll(baseDomain, sql.toString());
    	if(!ValueUtil.isEmpty(result)) {
    		PesquisaRespApp filter = (PesquisaRespApp) baseDomain;
    		PesquisaRespApp ultimaPesquisaRespApp = (PesquisaRespApp) result.items[0];
    		filter.cdPesquisaApp = ultimaPesquisaRespApp.cdPesquisaApp;
    		filter.cdResposta = ultimaPesquisaRespApp.cdResposta;
    		return (PesquisaRespApp) findByRowKey(filter.getRowKey());
    	}
    	return null;
	}
	
    public static String getSqlDadosEnvioServidor() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("SELECT tb.* FROM TBLVPPESQUISARESPAPP tb ");
    	sb.append(" JOIN TBLVPPESQUISAAPP p ON tb.CDEMPRESA = p.CDEMPRESA AND tb.CDCLIENTE = p.CDCLIENTE AND p.CDPESQUISAAPP = tb.CDPESQUISAAPP ");
    	sb.append(" WHERE tb.").append(PesquisaRespApp.NMCAMPOTIPOALTERACAO);
    	sb.append(" <> ").append(Sql.getValue(PesquisaRespApp.FLTIPOALTERACAO_ORIGINAL));
    	sb.append(" AND p.DTFIMPESQUISA is not null");
    	return sb.toString(); 
    }
    
    @Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PesquisaRespApp pesquisaRespApp = (PesquisaRespApp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pesquisaRespApp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pesquisaRespApp.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPESQUISAAPP = ", pesquisaRespApp.cdPesquisaApp);
		sqlWhereClause.addAndCondition("CDQUESTIONARIO = ", pesquisaRespApp.cdQuestionario);
		sqlWhereClause.addAndCondition("CDPERGUNTA = ", pesquisaRespApp.cdPergunta);
		sqlWhereClause.addAndCondition("CDRESPOSTA = ", pesquisaRespApp.cdResposta);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", pesquisaRespApp.cdCliente);
		sqlWhereClause.addAndCondition("DTRESPOSTA = ", pesquisaRespApp.dtResposta);
		sqlWhereClause.addAndLikeCondition("DSRESPOSTA", pesquisaRespApp.dsResposta, false);
		sql.append(sqlWhereClause.getSql());
	}
    
    public int deleteAllRespostasFromPerguntasSecundarias(Pergunta filter, PesquisaApp pesquisa) {
		StringBuffer sql = getSqlBuffer();
        sql.append(" delete from ").append(tableName);
        sql.append(" where CDEMPRESA = " + filter.cdEmpresa);
        sql.append(" and cdPesquisaApp = " + pesquisa.cdPesquisaApp);
        sql.append(" and cdPergunta in (select group_concat(cdPerguntasSecundarias) from TBLVPPERGUNTARESPOSTA where cdpergunta = " + filter.cdPergunta + ")");
        try {
        	return executeUpdate(sql.toString());
        } catch(Throwable e) {
        	ExceptionUtil.handle(e);
        	return 0;
        }
	}

}
