package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MetaVendaTipo;
import totalcross.sql.ResultSet;

public class MetaVendaTipoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaVendaTipo();
	}

    private static MetaVendaTipoDbxDao instance;

    public MetaVendaTipoDbxDao() {
        super(MetaVendaTipo.TABLE_NAME);
    }

    public static MetaVendaTipoDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaVendaTipoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaVendaTipo metaVendaTipo = new MetaVendaTipo();
        metaVendaTipo.rowKey = rs.getString("rowkey");
        metaVendaTipo.cdEmpresa = rs.getString("cdEmpresa");
        metaVendaTipo.cdMetaVenda = rs.getString("cdMetaVenda");
        metaVendaTipo.cdFamilia = rs.getString("cdFamilia");
        metaVendaTipo.vlMetaVenda = ValueUtil.round(rs.getDouble("vlMetaVenda"));
        metaVendaTipo.vlRealizadoVenda = ValueUtil.round(rs.getDouble("vlRealizadoVenda"));
        metaVendaTipo.nuCarimbo = rs.getInt("nuCarimbo");
        metaVendaTipo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaVendaTipo.cdUsuario = rs.getString("cdUsuario");
        return metaVendaTipo;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDMETAVENDA,");
        sql.append(" CDFAMILIA,");
        sql.append(" VLMETAVENDA,");
        sql.append(" VLREALIZADOVENDA,");
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
        sql.append(" CDMETAVENDA,");
        sql.append(" CDFAMILIA,");
        sql.append(" VLMETAVENDA,");
        sql.append(" VLREALIZADOVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaTipo metaVendaTipo = (MetaVendaTipo) domain;
        sql.append(Sql.getValue(metaVendaTipo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaVendaTipo.cdMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaTipo.cdFamilia)).append(",");
        sql.append(Sql.getValue(metaVendaTipo.vlMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaTipo.vlRealizadoVenda)).append(",");
        sql.append(Sql.getValue(metaVendaTipo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaVendaTipo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaVendaTipo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaTipo metaVendaTipo = (MetaVendaTipo) domain;
        sql.append(" VLMETAVENDA = ").append(Sql.getValue(metaVendaTipo.vlMetaVenda)).append(",");
        sql.append(" VLREALIZADOVENDA = ").append(Sql.getValue(metaVendaTipo.vlRealizadoVenda)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(metaVendaTipo.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(metaVendaTipo.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(metaVendaTipo.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaTipo metaVendaTipo = (MetaVendaTipo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", metaVendaTipo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMETAVENDA = ", metaVendaTipo.cdMetaVenda);
		sqlWhereClause.addAndCondition("CDFAMILIA = ", metaVendaTipo.cdFamilia);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}