package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PontuacaoFaixaDias;
import totalcross.sql.ResultSet;

public class PontuacaoFaixaDiasPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontuacaoFaixaDias();
	}

    private static PontuacaoFaixaDiasPdbxDao instance;

    public PontuacaoFaixaDiasPdbxDao() {
        super(PontuacaoFaixaDias.TABLE_NAME);
    }

    public static PontuacaoFaixaDiasPdbxDao getInstance() {
        return instance == null ? instance = new PontuacaoFaixaDiasPdbxDao() : instance;
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
        sql.append(" QTDIASMEDIOS,");
        sql.append(" VLFATORCORRECAO,");
        sql.append(" VLTOTALPEDIDO");
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PontuacaoFaixaDias pontuacaoFaixaDias = new PontuacaoFaixaDias();
        pontuacaoFaixaDias.rowKey = rs.getString("rowkey");
        pontuacaoFaixaDias.cdEmpresa = rs.getString("cdEmpresa");
        pontuacaoFaixaDias.cdRepresentante = rs.getString("cdRepresentante");
        pontuacaoFaixaDias.cdPontuacaoConfig = rs.getString("cdPontuacaoConfig");
        pontuacaoFaixaDias.flTipoFatorCorrecao = rs.getString("flTipoFatorCorrecao");
        pontuacaoFaixaDias.qtDiasMedios = rs.getInt("qtDiasMedios");
        pontuacaoFaixaDias.vlFatorCorrecao = rs.getDouble("vlFatorCorrecao");
        pontuacaoFaixaDias.vlTotalPedido = rs.getDouble("vlTotalPedido");
        return pontuacaoFaixaDias;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        PontuacaoFaixaDias pontuacaoFaixaDiasFilter = (PontuacaoFaixaDias) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", pontuacaoFaixaDiasFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pontuacaoFaixaDiasFilter.cdRepresentante);
        sqlWhereClause.addAndCondition("CDPONTUACAOCONFIG = ", pontuacaoFaixaDiasFilter.cdPontuacaoConfig);
        sqlWhereClause.addAndCondition("FLTIPOFATORCORRECAO = ", pontuacaoFaixaDiasFilter.flTipoFatorCorrecao);
        sqlWhereClause.addAndCondition("QTDIASMEDIOS = ", pontuacaoFaixaDiasFilter.qtDiasMedios);
        sqlWhereClause.addAndCondition("VLTOTALPEDIDO = ", pontuacaoFaixaDiasFilter.vlTotalPedido);
        sql.append(sqlWhereClause.getSql());
    }

}