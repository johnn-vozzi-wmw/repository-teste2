package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCliGrupo;
import totalcross.sql.ResultSet;

public class MetaVendaCliGrupoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaVendaCliGrupo();
	}

    private static MetaVendaCliGrupoDbxDao instance;
	

    public MetaVendaCliGrupoDbxDao() {
        super(MetaVendaCliGrupo.TABLE_NAME);
    }

    public static MetaVendaCliGrupoDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaVendaCliGrupoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaVendaCliGrupo metaVendaCliGrupo = new MetaVendaCliGrupo();
        metaVendaCliGrupo.rowKey = rs.getString("rowkey");
        metaVendaCliGrupo.cdEmpresa = rs.getString("cdEmpresa");
        metaVendaCliGrupo.cdRepresentante = rs.getString("cdRepresentante");
        metaVendaCliGrupo.cdMetaVendaCli = rs.getString("cdMetaVendaCli");
        metaVendaCliGrupo.cdGrupoProduto = rs.getString("cdGrupoProduto");
        metaVendaCliGrupo.cdCliente = rs.getString("cdCliente");
        metaVendaCliGrupo.vlMeta = ValueUtil.round(rs.getDouble("vlMeta"));
        metaVendaCliGrupo.vlRealizado = ValueUtil.round(rs.getDouble("vlRealizado"));
        metaVendaCliGrupo.nuCarimbo = rs.getInt("nuCarimbo");
        metaVendaCliGrupo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaVendaCliGrupo.cdUsuario = rs.getString("cdUsuario");
        return metaVendaCliGrupo;
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
        sql.append(" CDMETAVENDACLI,");
        sql.append(" CDGRUPOPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLMETA,");
        sql.append(" VLREALIZADO,");
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
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDMETAVENDACLI,");
        sql.append(" CDGRUPOPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" VLMETA,");
        sql.append(" VLREALIZADO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) domain;
        sql.append(Sql.getValue(metaVendaCliGrupo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.cdMetaVendaCli)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.cdGrupoProduto)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.cdCliente)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.vlMeta)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.vlRealizado)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaVendaCliGrupo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) domain;
        sql.append(" VLMETA = ").append(Sql.getValue(metaVendaCliGrupo.vlMeta)).append(",");
        sql.append(" VLREALIZADO = ").append(Sql.getValue(metaVendaCliGrupo.vlRealizado)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(metaVendaCliGrupo.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(metaVendaCliGrupo.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(metaVendaCliGrupo.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", metaVendaCliGrupo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", metaVendaCliGrupo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMETAVENDACLI = ", metaVendaCliGrupo.cdMetaVendaCli);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO = ", metaVendaCliGrupo.cdGrupoProduto);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", metaVendaCliGrupo.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}