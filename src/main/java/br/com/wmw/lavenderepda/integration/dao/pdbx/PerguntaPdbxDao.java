package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Pergunta;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import br.com.wmw.lavenderepda.business.domain.PesquisaApp;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PerguntaPdbxDao extends LavendereCrudPersonDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Pergunta();
	}

	private static PerguntaPdbxDao instance;

	public PerguntaPdbxDao() {
		super(Pergunta.TABLE_NAME);
	}

	public static PerguntaPdbxDao getInstance() {
		if (instance == null) {
			instance = new PerguntaPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Pergunta pergunta = new Pergunta();
		pergunta.rowKey = rs.getString(1);
		pergunta.cdEmpresa = rs.getString(2);
		pergunta.cdPergunta = rs.getString(3);
		pergunta.dsPergunta = rs.getString(4);
		pergunta.dtCadastro = rs.getDate(5);
		pergunta.flExibeObservacao = rs.getString(6);
		pergunta.cdTipoPergunta = rs.getString(7);
		pergunta.flRespostaAutomatica = rs.getString(8);
		pergunta.nmEntidade = rs.getString(9);
		pergunta.nmCampo = rs.getString(10);
		pergunta.flSomenteLeitura = rs.getString(11);
		return pergunta;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.ROWKEY,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDPERGUNTA,");
		sql.append(" tb.DSPERGUNTA,");
		sql.append(" tb.DTCADASTRO,");
		sql.append(" tb.FLEXIBEOBSERVACAO,");
		sql.append(" tb.CDTIPOPERGUNTA,");
		sql.append(" tb.FLRESPOSTAAUTOMATICA,");
		sql.append(" tb.NMENTIDADE,");
		sql.append(" tb.NMCAMPO,");
		sql.append(" tb.FLSOMENTELEITURA");
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		Pergunta pergunta = (Pergunta) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", pergunta.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDPERGUNTA = ", pergunta.cdPergunta);
		sqlWhereClause.addAndLikeCondition("TB.DSPERGUNTA = ", pergunta.dsPergunta, false);
		sqlWhereClause.addAndCondition("TB.DTCADASTRO = ", pergunta.dtCadastro);
		sqlWhereClause.addAndCondition("TB.FLEXIBEOBSERVACAO = ", pergunta.flExibeObservacao);
		sqlWhereClause.addAndCondition("TB.CDTIPOPERGUNTA = ", pergunta.cdTipoPergunta);
		sqlWhereClause.addAndCondition("TB.FLRESPOSTAAUTOMATICA = ", pergunta.flRespostaAutomatica);
		sqlWhereClause.addAndCondition("TB.NMENTIDADE = ", pergunta.nmEntidade);
		sqlWhereClause.addAndCondition("TB.NMCAMPO = ", pergunta.nmCampo);
		sqlWhereClause.addAndCondition("TB.FLSOMENTELEITURA = ", pergunta.flSomenteLeitura);
		sql.append(sqlWhereClause.getSql());
	}
	
	public Vector findAllPerguntasByQuestionario(Questionario questionario) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(questionario, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	sql.append(" JOIN TBLVPQUESTIONARIOPERGUNTA qp ON qp.CDEMPRESA = tb.CDEMPRESA AND qp.CDPERGUNTA = tb.CDPERGUNTA "); 
    	sql.append(" JOIN TBLVPQUESTIONARIO q ON q.CDEMPRESA = tb.CDEMPRESA AND q.CDQUESTIONARIO = qp.CDQUESTIONARIO ");
    	sql.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(questionario.cdEmpresa));
    	sql.append(" AND q.CDQUESTIONARIO = ").append(Sql.getValue(questionario.cdQuestionario));
    	sql.append(" AND (tb.FLSECUNDARIA != ").append(Sql.getValue(ValueUtil.VALOR_SIM)).append("OR tb.FLSECUNDARIA IS NULL)");
    	sql.append(" ORDER BY qp.NUSEQUENCIA");
    	return findAll(questionario, sql.toString());
	}

	public int countPerguntasObrigatorias(String cdQuestionario) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(null, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	sql.append(" JOIN TBLVPQUESTIONARIOPERGUNTA qp on qp.CDEMPRESA = tb.CDEMPRESA AND qp.CDPERGUNTA = tb.CDPERGUNTA "); 
    	sql.append(" WHERE qp.FLOBRIGATORIO = 'S' ");
    	sql.append(" AND qp.CDQUESTIONARIO = ").append(Sql.getValue(cdQuestionario));
    	Vector perguntas = findAll(null, sql.toString());
		return ValueUtil.isEmpty(perguntas) ? 0 : perguntas.size();
	}
	
	public Vector findUltimaPerguntaObrigatoriaNaoRespondida(Questionario questionario, PesquisaApp pesquisaApp, String[] cdPesquisaArray) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		Vector list = new Vector();
		sql.append(" SELECT ");
		addSelectColumns(questionario, sql);
    	sql.append(" FROM ").append(tableName).append(" tb ");
    	sql.append(" JOIN TBLVPQUESTIONARIOPERGUNTA qp ")
			    .append("on qp.CDEMPRESA = tb.CDEMPRESA ")
			    .append("AND qp.CDPERGUNTA = tb.CDPERGUNTA ");
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("tb.CDEMPRESA =", questionario.cdEmpresa);
    	sqlWhereClause.addAndCondition("qp.FLOBRIGATORIO = ", ValueUtil.VALOR_SIM);
    	sqlWhereClause.addAndCondition("qp.CDQUESTIONARIO = ", questionario.cdQuestionario);
    	if (ValueUtil.isNotEmpty(cdPesquisaArray)) {
    		sqlWhereClause.addAndConditionIn("tb.CDPERGUNTA", cdPesquisaArray);
    	}
    	sql.append(sqlWhereClause.getSql())
		.append(" AND NOT EXISTS (")
		.append("SELECT 1 FROM TBLVPPESQUISARESPAPP ")
		.append(" WHERE CDEMPRESA = qp.CDEMPRESA ")
		.append(" AND CDQUESTIONARIO = qp.CDQUESTIONARIO ")
		.append(" AND CDPERGUNTA = tb.CDPERGUNTA ")
		.append(" AND DSRESPOSTA <> '' ")
		.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pesquisaApp.cdRepresentante))
		.append(" AND CDPESQUISAAPP = ").append(Sql.getValue(pesquisaApp.cdPesquisaApp))
		.append(" AND CDCLIENTE = ").append(Sql.getValue(pesquisaApp.cdCliente))
		.append(")");
		sql.append(" ORDER BY qp.NUSEQUENCIA ");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				list.addElement(populate(questionario, rs));
			}
		}
    	return list;
	}
	
	public Vector findAllPerguntasSecundariasByPerguntaResposta(PerguntaResposta filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	Vector list = new Vector();
    	sql.append(" select ");
    	addSelectColumns(filter, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	sql.append("where TB.CDEMPRESA = '" + filter.cdEmpresa + "'");
    	sql.append(" and cdPergunta in ("+ filter.cdPerguntasSecundarias + ") ");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				 list.addElement(populate(filter, rs));
			}
		}
    	return list;
	}
	
}
