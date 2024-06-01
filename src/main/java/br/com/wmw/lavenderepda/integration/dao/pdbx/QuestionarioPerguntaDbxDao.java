package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.QuestionarioPergunta;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class QuestionarioPerguntaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new QuestionarioPergunta();
	}

    private static QuestionarioPerguntaDbxDao instance;

    public QuestionarioPerguntaDbxDao() {
        super(QuestionarioPergunta.TABLE_NAME); 
    }

    public static QuestionarioPerguntaDbxDao getInstance() {
        if (instance == null) {
            instance = new QuestionarioPerguntaDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        QuestionarioPergunta questionarioPergunta = new QuestionarioPergunta();
        questionarioPergunta.rowKey = rs.getString("rowkey");
        questionarioPergunta.cdEmpresa = rs.getString("cdEmpresa");
        questionarioPergunta.cdQuestionario = rs.getString("cdQuestionario");
        questionarioPergunta.cdPergunta = rs.getString("cdPergunta");
        questionarioPergunta.dtCadastro = rs.getDate("dtCadastro");
        questionarioPergunta.nuSequencia = rs.getInt("nuSequencia");
        questionarioPergunta.flObrigatorio = rs.getString("flObrigatorio");
        questionarioPergunta.flExibeObservacao = rs.getString("flExibeobservacao");
        questionarioPergunta.cdUsuario = rs.getString("cdUsuario");
        questionarioPergunta.nuCarimbo = rs.getInt("nuCarimbo");
        questionarioPergunta.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return questionarioPergunta;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDQUESTIONARIO,");
        sql.append(" CDPERGUNTA,");
        sql.append(" DTCADASTRO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" FLOBRIGATORIO,");
        sql.append(" FLEXIBEOBSERVACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO ");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDQUESTIONARIO,");
        sql.append(" CDPERGUNTA,");
        sql.append(" DTCADASTRO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" FLOBRIGATORIO,");
        sql.append(" FLEXIBEOBSERVACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO ");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        QuestionarioPergunta questionariopergunta = (QuestionarioPergunta) domain;
        sql.append(Sql.getValue(questionariopergunta.cdEmpresa)).append(",");
        sql.append(Sql.getValue(questionariopergunta.cdQuestionario)).append(",");
        sql.append(Sql.getValue(questionariopergunta.cdPergunta)).append(",");
        sql.append(Sql.getValue(questionariopergunta.dtCadastro)).append(",");
        sql.append(Sql.getValue(questionariopergunta.nuSequencia)).append(",");
        sql.append(Sql.getValue(questionariopergunta.flObrigatorio)).append(",");
        sql.append(Sql.getValue(questionariopergunta.flExibeObservacao)).append(",");
        sql.append(Sql.getValue(questionariopergunta.cdUsuario)).append(",");
        sql.append(Sql.getValue(questionariopergunta.nuCarimbo)).append(",");
        sql.append(Sql.getValue(questionariopergunta.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        QuestionarioPergunta questionariopergunta = (QuestionarioPergunta) domain;
        sql.append(" DTCADASTRO = ").append(Sql.getValue(questionariopergunta.dtCadastro)).append(",");
        sql.append(" NUSEQUENCIA = ").append(Sql.getValue(questionariopergunta.nuSequencia)).append(",");
        sql.append(" FLOBRIGATORIO = ").append(Sql.getValue(questionariopergunta.flObrigatorio)).append(",");
        sql.append(" FLEXIBEOBSERVACAO = ").append(Sql.getValue(questionariopergunta.flExibeObservacao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(questionariopergunta.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(questionariopergunta.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(questionariopergunta.flTipoAlteracao));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        QuestionarioPergunta questionariopergunta = (QuestionarioPergunta) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", questionariopergunta.cdEmpresa);
		sqlWhereClause.addAndCondition("CDQUESTIONARIO = ", questionariopergunta.cdQuestionario);
		sqlWhereClause.addAndCondition("CDPERGUNTA = ", questionariopergunta.cdPergunta);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public boolean isPerguntaObrigatoria(String cdEmpresa, String cdQuestionario, String cdPergunta) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(domainFilter, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	sql.append(" WHERE tb.FLOBRIGATORIO = 'S' ");
    	sql.append(" AND tb.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
    	sql.append(" AND tb.CDQUESTIONARIO = ").append(Sql.getValue(cdQuestionario));
    	sql.append(" AND tb.CDPERGUNTA = ").append(Sql.getValue(cdPergunta));
    	Vector perguntas = findAll(domainFilter, sql.toString());
		return !ValueUtil.isEmpty(perguntas);
	}
    
    public boolean isExibeObservacao(String cdEmpresa, String cdQuestionario, String cdPergunta) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(domainFilter, sql);
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	sql.append(" WHERE tb.FLEXIBEOBSERVACAO = 'S' ");
    	sql.append(" AND tb.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
    	sql.append(" AND tb.CDQUESTIONARIO = ").append(Sql.getValue(cdQuestionario));
    	sql.append(" AND tb.CDPERGUNTA = ").append(Sql.getValue(cdPergunta));
    	Vector perguntas = findAll(domainFilter, sql.toString());
		return !ValueUtil.isEmpty(perguntas);
	}    
}