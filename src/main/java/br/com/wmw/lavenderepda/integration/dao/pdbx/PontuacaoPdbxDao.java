package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Pontuacao;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class PontuacaoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Pontuacao();
	}

    private static PontuacaoPdbxDao instance;

    public PontuacaoPdbxDao() {
        super(Pontuacao.TABLE_NAME);
    }

    public static PontuacaoPdbxDao getInstance() {
        if (instance == null) {
            instance = new PontuacaoPdbxDao();
        }
        return instance;
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append("ORDER BY VLPCTLUCRO");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Pontuacao pontuacao = new Pontuacao();
        pontuacao.rowKey = rs.getString("rowkey");
        pontuacao.cdEmpresa = rs.getString("cdEmpresa");
        pontuacao.cdRepresentante = rs.getString("cdRepresentante");
        pontuacao.vlPctLucro = ValueUtil.round(rs.getDouble("vlPctlucro"));
        pontuacao.qtPontos = rs.getInt("qtPontos");
        pontuacao.nuCarimbo = rs.getInt("nuCarimbo");
        pontuacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pontuacao.cdUsuario = rs.getString("cdUsuario");
        return pontuacao;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" VLPCTLUCRO,");
        sql.append(" QTPONTOS,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Pontuacao pontuacao = (Pontuacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pontuacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pontuacao.cdRepresentante);
		sqlWhereClause.addAndCondition("VLPCTLUCRO = ", pontuacao.vlPctLucro);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public Pontuacao findPontuacaoByVlPctLucroCalculado(String cdEmpresa, String cdRepresentante, double vlPctLucroCalculado) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT VLPCTLUCRO, QTPONTOS FROM TBLVPPONTUACAO");
    	sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
    	sql.append(" AND VLPCTLUCRO <= ").append(Sql.getValue(vlPctLucroCalculado));
    	sql.append(" ORDER BY VLPCTLUCRO DESC");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			Pontuacao pontuacao = new Pontuacao();
    			pontuacao.cdEmpresa = cdEmpresa;
    			pontuacao.cdRepresentante = cdRepresentante;
    			pontuacao.vlPctLucro = rs.getDouble("VLPCTLUCRO");
    			pontuacao.qtPontos = rs.getInt("QTPONTOS");
				return pontuacao;
			}
    		return null;
		}
	}

}