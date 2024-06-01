package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescontoGrupo;
import totalcross.sql.ResultSet;

public class DescontoGrupoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescontoGrupo();
	}

    private static DescontoGrupoDbxDao instance;

    public DescontoGrupoDbxDao() {
        super(DescontoGrupo.TABLE_NAME);
    }

    public static DescontoGrupoDbxDao getInstance() {
        if (instance == null) {
            instance = new DescontoGrupoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescontoGrupo descontogrupo = new DescontoGrupo();
        descontogrupo.rowKey = rs.getString("rowkey");
        descontogrupo.cdEmpresa = rs.getString("cdEmpresa");
        descontogrupo.cdRepresentante = rs.getString("cdRepresentante");
        descontogrupo.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        descontogrupo.cdGrupoDescProd = rs.getString("cdGrupoDescProd");
        descontogrupo.qtItem = ValueUtil.round(rs.getDouble("qtItem"));
        descontogrupo.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        descontogrupo.nuCarimbo = rs.getInt("nuCarimbo");
        descontogrupo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        descontogrupo.cdUsuario = rs.getString("cdUsuario");
        descontogrupo.cdTabelaPreco = rs.getString("cdTabelaPreco");
        return descontogrupo;
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
        sql.append(" CDGRUPODESCPROD,");
        sql.append(" QTITEM,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDTABELAPRECO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPODESCPROD,");
        sql.append(" QTITEM,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDTABELAPRECO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	DescontoGrupo descontogrupo = (DescontoGrupo) domain;
        sql.append(Sql.getValue(descontogrupo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descontogrupo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descontogrupo.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(descontogrupo.cdGrupoDescProd)).append(",");
        sql.append(Sql.getValue(descontogrupo.qtItem)).append(",");
        sql.append(Sql.getValue(descontogrupo.vlPctDesconto)).append(",");
        sql.append(Sql.getValue(descontogrupo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(descontogrupo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(descontogrupo.cdUsuario)).append(",");
        sql.append(Sql.getValue(descontogrupo.cdTabelaPreco));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	DescontoGrupo descontogrupo = (DescontoGrupo) domain;
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(descontogrupo.vlPctDesconto)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(descontogrupo.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descontogrupo.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descontogrupo.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	DescontoGrupo descontogrupo = (DescontoGrupo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descontogrupo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descontogrupo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", descontogrupo.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPODESCPROD = ", descontogrupo.cdGrupoDescProd);
		sqlWhereClause.addAndCondition("QTITEM = ", descontogrupo.qtItem);
		sqlWhereClause.addAndCondition("CDTABELAPRECO = ", descontogrupo.cdTabelaPreco);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by qtitem desc");
    }

}