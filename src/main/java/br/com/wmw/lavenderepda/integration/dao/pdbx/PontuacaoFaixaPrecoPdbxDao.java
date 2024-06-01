package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaPreco;
import totalcross.sql.ResultSet;

public class PontuacaoFaixaPrecoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontuacaoFaixaPreco();
	}

    private static PontuacaoFaixaPrecoPdbxDao instance;

    public PontuacaoFaixaPrecoPdbxDao() {
        super(PontuacaoFaixaPreco.TABLE_NAME);
    }

    public static PontuacaoFaixaPrecoPdbxDao getInstance() {
        return instance == null ? instance = new PontuacaoFaixaPrecoPdbxDao() : instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPONTUACAOCONFIG,");
        sql.append(" FLTIPOFATORCORRECAO,");
        sql.append(" VLPCTFAIXA,");
        sql.append(" VLFATORCORRECAO");
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PontuacaoFaixaPreco pontuacaoFaixaPreco = new PontuacaoFaixaPreco();
        pontuacaoFaixaPreco.rowKey = rs.getString("rowkey");
        pontuacaoFaixaPreco.cdEmpresa = rs.getString("cdEmpresa");
        pontuacaoFaixaPreco.cdRepresentante = rs.getString("cdRepresentante");
        pontuacaoFaixaPreco.cdPontuacaoConfig = rs.getString("cdPontuacaoConfig");
        pontuacaoFaixaPreco.flTipoFatorCorrecao = rs.getString("flTipoFatorCorrecao");
        pontuacaoFaixaPreco.vlPctFaixa = rs.getDouble("vlPctFaixa");
        pontuacaoFaixaPreco.vlFatorCorrecao = rs.getDouble("vlFatorCorrecao");
        return pontuacaoFaixaPreco;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        PontuacaoFaixaPreco pontuacaoFaixaPrecoFilter = (PontuacaoFaixaPreco) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", pontuacaoFaixaPrecoFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pontuacaoFaixaPrecoFilter.cdRepresentante);
        sqlWhereClause.addAndCondition("CDPONTUACAOCONFIG = ", pontuacaoFaixaPrecoFilter.cdPontuacaoConfig);
        sqlWhereClause.addAndCondition("FLTIPOFATORCORRECAO = ", pontuacaoFaixaPrecoFilter.flTipoFatorCorrecao);
        sqlWhereClause.addAndCondition("VLPCTFAIXA = ", pontuacaoFaixaPrecoFilter.vlPctFaixa);
        sql.append(sqlWhereClause.getSql());
    }

}