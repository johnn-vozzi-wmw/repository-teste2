package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Questionario;
import totalcross.sql.ResultSet;

public class QuestionarioPdbxDao extends LavendereCrudPersonDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Questionario();
	}

	private static QuestionarioPdbxDao instance;

	public QuestionarioPdbxDao() {
		super(Questionario.TABLE_NAME);
	}

	public static QuestionarioPdbxDao getInstance() {
		if (instance == null) {
			instance = new QuestionarioPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Questionario questionario = new Questionario();
		questionario.rowKey = rs.getString(1);
		questionario.cdEmpresa = rs.getString(2);
		questionario.cdQuestionario = rs.getString(3);
		questionario.nmQuestionario = rs.getString(4);
		questionario.dtCadastro = rs.getDate(5);
		return questionario;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey, ");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDQUESTIONARIO,");
		sql.append(" tb.NMQUESTIONARIO,");
		sql.append(" tb.DTCADASTRO");
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		Questionario questionario = (Questionario) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", questionario.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDQUESTIONARIO = ", questionario.cdQuestionario);
		sqlWhereClause.addAndLikeCondition("TB.NMQUESTIONARIO", questionario.nmQuestionario, false);
		sqlWhereClause.addAndCondition("TB.DTCADASTRO = ", questionario.dtCadastro);
		sql.append(sqlWhereClause.getSql());
	}

}
