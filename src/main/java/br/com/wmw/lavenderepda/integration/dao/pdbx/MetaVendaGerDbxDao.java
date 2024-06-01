package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MetaVendaGer;
import totalcross.sql.ResultSet;

public class MetaVendaGerDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaVendaGer();
	}

    private static MetaVendaGerDbxDao instance;

    public MetaVendaGerDbxDao() {
        super(MetaVendaGer.TABLE_NAME);
    }

    public static MetaVendaGerDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaVendaGerDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaVendaGer metaVendaGer = new MetaVendaGer();
        metaVendaGer.rowKey = rs.getString("rowkey");
        metaVendaGer.cdEmpresa = rs.getString("cdEmpresa");
        metaVendaGer.cdMetaVenda = rs.getString("cdMetaVenda");
        metaVendaGer.cdGerente = rs.getString("cdGerente");
        metaVendaGer.vlChaveTipo = rs.getString("vlChaveTipo");
        metaVendaGer.vlMetaVenda = ValueUtil.round(rs.getDouble("vlMetaVenda"));
        metaVendaGer.vlRealizadoVenda = ValueUtil.round(rs.getDouble("vlRealizadoVenda"));
        metaVendaGer.nuCarimbo = rs.getInt("nuCarimbo");
        metaVendaGer.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaVendaGer.cdUsuario = rs.getString("cdUsuario");
        return metaVendaGer;
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
        sql.append(" CDGERENTE,");
        sql.append(" VLCHAVETIPO,");
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
        sql.append(" CDGERENTE,");
        sql.append(" VLCHAVETIPO,");
        sql.append(" VLMETAVENDA,");
        sql.append(" VLREALIZADOVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaGer metaVendaGer = (MetaVendaGer) domain;
        sql.append(Sql.getValue(metaVendaGer.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaVendaGer.cdMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaGer.cdGerente)).append(",");
        sql.append(Sql.getValue(metaVendaGer.vlChaveTipo)).append(",");
        sql.append(Sql.getValue(metaVendaGer.vlMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaGer.vlRealizadoVenda)).append(",");
        sql.append(Sql.getValue(metaVendaGer.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaVendaGer.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaVendaGer.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaGer metaVendaGer = (MetaVendaGer) domain;
        sql.append(" VLMETAVENDA = ").append(Sql.getValue(metaVendaGer.vlMetaVenda)).append(",");
        sql.append(" VLREALIZADOVENDA = ").append(Sql.getValue(metaVendaGer.vlRealizadoVenda)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(metaVendaGer.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(metaVendaGer.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(metaVendaGer.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaGer metaVendaGer = (MetaVendaGer) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", metaVendaGer.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMETAVENDA = ", metaVendaGer.cdMetaVenda);
		sqlWhereClause.addAndCondition("CDGERENTE = ", metaVendaGer.cdGerente);
		sqlWhereClause.addAndCondition("VLCHAVETIPO = ", metaVendaGer.vlChaveTipo);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}