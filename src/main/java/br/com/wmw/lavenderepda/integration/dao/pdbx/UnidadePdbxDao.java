package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import totalcross.sql.ResultSet;

public class UnidadePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Unidade();
	}

    private static UnidadePdbxDao instance;

    public UnidadePdbxDao() {
        super(Unidade.TABLE_NAME);
    }

    public static UnidadePdbxDao getInstance() {
        if (instance == null) {
            instance = new UnidadePdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Unidade unidade = new Unidade();
        unidade.rowKey = rs.getString("rowkey");
        unidade.cdEmpresa = rs.getString("cdEmpresa");
        unidade.cdRepresentante = rs.getString("cdRepresentante");
        unidade.cdUnidade = rs.getString("cdUnidade");
        unidade.dsUnidade = rs.getString("dsUnidade");
        unidade.nuCarimbo = rs.getInt("nuCarimbo");
        unidade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        unidade.cdUsuario = rs.getString("cdUsuario");
        if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
        	unidade.dsUnidadePlural = rs.getString("dsUnidadePlural");
        }
        if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto()) {
			unidade.flCalculaPrecoMetroQuadrado = rs.getString("flCalculaPrecoMetroQuadrado");
		}
        unidade.flIgnoraMultEspecial = rs.getString("flIgnoraMultEspecial");
        if(LavenderePdaConfig.isConfigCalculoPesoPedido()) {
        	unidade.flCalculaPesoGramatura = rs.getString("flCalculaPesoGramatura");
        }
        return unidade;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUNIDADE,");
        sql.append(" DSUNIDADE,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLIGNORAMULTESPECIAL,");
        sql.append(" CDUSUARIO");
        if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
        	sql.append(" ,DSUNIDADEPLURAL");
        }
        if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto()) {
        	sql.append(" ,FLCALCULAPRECOMETROQUADRADO");
        }
        if(LavenderePdaConfig.isConfigCalculoPesoPedido()) {
        	sql.append(" ,FLCALCULAPESOGRAMATURA");
        }
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Unidade unidade = (Unidade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", unidade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", unidade.cdRepresentante);
		sqlWhereClause.addAndCondition("CDUNIDADE = ", unidade.cdUnidade);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}
