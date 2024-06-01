package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaGrupo;
import totalcross.sql.ResultSet;

public class SugestaoVendaGrupoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SugestaoVendaGrupo();
	}

    private static SugestaoVendaGrupoDbxDao instance;

    public SugestaoVendaGrupoDbxDao() {
        super(SugestaoVendaGrupo.TABLE_NAME);
    }

    public static SugestaoVendaGrupoDbxDao getInstance() {
        if (instance == null) {
            instance = new SugestaoVendaGrupoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        SugestaoVendaGrupo sugestaoVendaGrupo = new SugestaoVendaGrupo();
        sugestaoVendaGrupo.rowKey = rs.getString("rowkey");
        sugestaoVendaGrupo.cdEmpresa = rs.getString("cdEmpresa");
        sugestaoVendaGrupo.cdSugestaoVenda = rs.getString("cdSugestaoVenda");
        sugestaoVendaGrupo.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        sugestaoVendaGrupo.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        sugestaoVendaGrupo.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
        sugestaoVendaGrupo.qtMixProdutosVenda = rs.getInt("qtMixProdutosVenda");
        sugestaoVendaGrupo.qtUnidadesVenda = rs.getInt("qtUnidadesVenda");
        sugestaoVendaGrupo.nuCarimbo = rs.getInt("nuCarimbo");
        sugestaoVendaGrupo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        sugestaoVendaGrupo.cdUsuario = rs.getString("cdUsuario");
        return sugestaoVendaGrupo;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUGESTAOVENDA,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" QTMIXPRODUTOSVENDA,");
        sql.append(" QTUNIDADESVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUGESTAOVENDA,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" QTMIXPRODUTOSVENDA,");
        sql.append(" QTUNIDADESVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) domain;
        sql.append(Sql.getValue(sugestaoVendaGrupo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.cdSugestaoVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.cdGrupoProduto2)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.cdGrupoProduto3)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.qtMixProdutosVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.qtUnidadesVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(sugestaoVendaGrupo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) domain;
        sql.append(" QTMIXPRODUTOSVENDA = ").append(Sql.getValue(sugestaoVendaGrupo.qtMixProdutosVenda)).append(",");
        sql.append(" QTUNIDADESVENDA = ").append(Sql.getValue(sugestaoVendaGrupo.qtUnidadesVenda)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(sugestaoVendaGrupo.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(sugestaoVendaGrupo.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(sugestaoVendaGrupo.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", sugestaoVendaGrupo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSUGESTAOVENDA = ", sugestaoVendaGrupo.cdSugestaoVenda);
		sqlWhereClause.addAndConditionOr("CDSUGESTAOVENDA = ", sugestaoVendaGrupo.cdsSugestao);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", sugestaoVendaGrupo.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", sugestaoVendaGrupo.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", sugestaoVendaGrupo.cdGrupoProduto3);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}