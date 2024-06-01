package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CondicaoNegociacao;
import totalcross.sql.ResultSet;

public class CondicaoNegociacaoDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondicaoNegociacao();
	}

    private static CondicaoNegociacaoDao instance = null;

    public CondicaoNegociacaoDao() {
        super(CondicaoNegociacao.TABLE_NAME);
    }
    
    public static CondicaoNegociacaoDao getInstance() {
        if (instance == null) {
            instance = new CondicaoNegociacaoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        CondicaoNegociacao condicaoNegociacao = new CondicaoNegociacao();
        condicaoNegociacao.rowKey = rs.getString("rowkey");
        condicaoNegociacao.cdEmpresa = rs.getString("cdEmpresa");
        condicaoNegociacao.cdRepresentante = rs.getString("cdRepresentante");
        condicaoNegociacao.cdCondicaoNegociacao = rs.getString("cdCondicaoNegociacao");
        condicaoNegociacao.dsCondicaoNegociacao = rs.getString("dsCondicaoNegociacao");
        condicaoNegociacao.cdLocalEstoque = rs.getString("cdLocalEstoque");
        condicaoNegociacao.vlPctEstoque = rs.getDouble("vlPctEstoque");
        condicaoNegociacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condicaoNegociacao.cdUsuario = rs.getString("cdUsuario");
        condicaoNegociacao.nuCarimbo = rs.getInt("nuCarimbo");
        condicaoNegociacao.dtAlteracao = rs.getDate("dtAlteracao");
        condicaoNegociacao.hrAlteracao = rs.getString("hrAlteracao");
        return condicaoNegociacao;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONDICAONEGOCIACAO,");
        sql.append(" DSCONDICAONEGOCIACAO,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" VLPCTESTOQUE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        CondicaoNegociacao condicaoNegociacao = (CondicaoNegociacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", condicaoNegociacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", condicaoNegociacao.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCONDICAONEGOCIACAO = ", condicaoNegociacao.cdCondicaoNegociacao);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected void addInsertColumns(StringBuffer sql) {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
	}
    
}