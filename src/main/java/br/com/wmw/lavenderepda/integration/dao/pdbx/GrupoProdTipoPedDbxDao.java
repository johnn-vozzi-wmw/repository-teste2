package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.GrupoProdTipoPed;
import totalcross.sql.ResultSet;

public class GrupoProdTipoPedDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoProdTipoPed();
	}

    private static GrupoProdTipoPedDbxDao instance;

    public GrupoProdTipoPedDbxDao() {
        super(GrupoProdTipoPed.TABLE_NAME);
    }

    public static GrupoProdTipoPedDbxDao getInstance() {
        if (instance == null) {
            instance = new GrupoProdTipoPedDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoProdTipoPed grupoProd1TipoPed = new GrupoProdTipoPed();
        grupoProd1TipoPed.rowKey = rs.getString("rowkey");
        grupoProd1TipoPed.cdEmpresa = rs.getString("cdEmpresa");
        grupoProd1TipoPed.cdRepresentante = rs.getString("cdRepresentante");
        grupoProd1TipoPed.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        grupoProd1TipoPed.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        grupoProd1TipoPed.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
        grupoProd1TipoPed.cdTipoPedido = rs.getString("cdTipoPedido");
        grupoProd1TipoPed.nuCarimbo = rs.getInt("nuCarimbo");
        grupoProd1TipoPed.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoProd1TipoPed.cdUsuario = rs.getString("cdUsuario");
        return grupoProd1TipoPed;
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
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProdTipoPed grupoProd1TipoPed = (GrupoProdTipoPed) domain;
        sql.append(Sql.getValue(grupoProd1TipoPed.cdEmpresa)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.cdRepresentante)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.cdGrupoProduto2)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.cdGrupoProduto3)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.nuCarimbo)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(grupoProd1TipoPed.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProdTipoPed grupoProd1TipoPed = (GrupoProdTipoPed) domain;
        sql.append(" nuCarimbo = ").append(Sql.getValue(grupoProd1TipoPed.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(grupoProd1TipoPed.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(grupoProd1TipoPed.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoProdTipoPed grupoProd1TipoPed = (GrupoProdTipoPed) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", grupoProd1TipoPed.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", grupoProd1TipoPed.cdRepresentante);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", grupoProd1TipoPed.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", grupoProd1TipoPed.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", grupoProd1TipoPed.cdGrupoProduto3);
		sqlWhereClause.addAndCondition("CDTIPOPEDIDO = ", grupoProd1TipoPed.cdTipoPedido);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}