package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PerguntaResposta;
import totalcross.sql.ResultSet;

public class PerguntaRespostaPdbxDao extends LavendereCrudPersonDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PerguntaResposta();
	}

	private static PerguntaRespostaPdbxDao instance;

	public PerguntaRespostaPdbxDao() {
		super(PerguntaResposta.TABLE_NAME);
	}

	public static PerguntaRespostaPdbxDao getInstance() {
		if (instance == null) {
			instance = new PerguntaRespostaPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PerguntaResposta perguntaResposta = new PerguntaResposta();
		perguntaResposta.rowKey = rs.getString(1);
		perguntaResposta.cdEmpresa = rs.getString(2);
		perguntaResposta.cdPergunta = rs.getString(3);
		perguntaResposta.cdResposta = rs.getString(4);
		perguntaResposta.dsResposta = rs.getString(5);
		perguntaResposta.dtCadastro = rs.getDate(6);
		perguntaResposta.nuSequencia = rs.getInt(7);
		perguntaResposta.cdPerguntasSecundarias = rs.getString(8);
		perguntaResposta.dsExpressao = rs.getString(9);
		perguntaResposta.flObrigaFoto = rs.getString(10);
		return perguntaResposta;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" TB.ROWKEY,");
		sql.append(" TB.CDEMPRESA,");
		sql.append(" TB.CDPERGUNTA,");
		sql.append(" TB.CDRESPOSTA,");
		sql.append(" TB.DSRESPOSTA,");
		sql.append(" TB.DTCADASTRO,");
		sql.append(" TB.NUSEQUENCIA,");
		sql.append(" TB.CDPERGUNTASSECUNDARIAS,");
		sql.append(" TB.DSEXPRESSAO,");
		sql.append(" TB.FLOBRIGAFOTO");
		
	}

	@Override
	protected void addUpdateValues(BaseDomain baseDomain, StringBuffer stringBuffer) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		PerguntaResposta perguntaResposta = (PerguntaResposta) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", perguntaResposta.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDPERGUNTA = ", perguntaResposta.cdPergunta);
		sqlWhereClause.addAndCondition("TB.CDRESPOSTA = ", perguntaResposta.cdResposta);
		sqlWhereClause.addAndLikeCondition("TB.DSRESPOSTA = ", perguntaResposta.dsResposta, false);
		sqlWhereClause.addAndCondition("TB.DTCADASTRO = ", perguntaResposta.dtCadastro);
		sqlWhereClause.addAndCondition("TB.FLOBRIGAFOTO = ", perguntaResposta.flObrigaFoto);
		sqlWhereClause.addAndCondition("TB.NUSEQUENCIA = ", perguntaResposta.nuSequencia == null ? 0 : perguntaResposta.nuSequencia);
		sql.append(sqlWhereClause.getSql());
	}
	
}
