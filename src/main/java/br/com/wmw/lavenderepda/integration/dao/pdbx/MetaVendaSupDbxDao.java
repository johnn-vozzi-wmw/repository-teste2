package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MetaVendaSup;
import totalcross.sql.ResultSet;

public class MetaVendaSupDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaVendaSup();
	}

    private static MetaVendaSupDbxDao instance;

    public MetaVendaSupDbxDao() {
        super(MetaVendaSup.TABLE_NAME);
    }

    public static MetaVendaSupDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaVendaSupDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaVendaSup metaVendaSup = new MetaVendaSup();
        metaVendaSup.rowKey = rs.getString("rowkey");
        metaVendaSup.cdEmpresa = rs.getString("cdEmpresa");
        metaVendaSup.cdMetaVenda = rs.getString("cdMetaVenda");
        metaVendaSup.cdGerente = rs.getString("cdGerente");
        metaVendaSup.vlChaveTipo = rs.getString("vlChaveTipo");
        metaVendaSup.cdSupervisor = rs.getString("cdSupervisor");
        metaVendaSup.vlMetaVenda = ValueUtil.round(rs.getDouble("vlMetaVenda"));
        metaVendaSup.vlRealizadoVenda = ValueUtil.round(rs.getDouble("vlRealizadoVenda"));
        metaVendaSup.nuCarimbo = rs.getInt("nuCarimbo");
        metaVendaSup.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaVendaSup.cdUsuario = rs.getString("cdUsuario");
        return metaVendaSup;
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
        sql.append(" CDSUPERVISOR,");
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
        sql.append(" CDSUPERVISOR,");
        sql.append(" VLMETAVENDA,");
        sql.append(" VLREALIZADOVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaSup metaVendaSup = (MetaVendaSup) domain;
        sql.append(Sql.getValue(metaVendaSup.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaVendaSup.cdMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaSup.cdGerente)).append(",");
        sql.append(Sql.getValue(metaVendaSup.vlChaveTipo)).append(",");
        sql.append(Sql.getValue(metaVendaSup.cdSupervisor)).append(",");
        sql.append(Sql.getValue(metaVendaSup.vlMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaSup.vlRealizadoVenda)).append(",");
        sql.append(Sql.getValue(metaVendaSup.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaVendaSup.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaVendaSup.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaSup metaVendaSup = (MetaVendaSup) domain;
        sql.append(" VLMETAVENDA = ").append(Sql.getValue(metaVendaSup.vlMetaVenda)).append(",");
        sql.append(" VLREALIZADOVENDA = ").append(Sql.getValue(metaVendaSup.vlRealizadoVenda)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(metaVendaSup.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(metaVendaSup.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(metaVendaSup.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaSup metaVendaSup = (MetaVendaSup) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", metaVendaSup.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMETAVENDA = ", metaVendaSup.cdMetaVenda);
		sqlWhereClause.addAndCondition("CDGERENTE = ", metaVendaSup.cdGerente);
		sqlWhereClause.addAndCondition("VLCHAVETIPO = ", metaVendaSup.vlChaveTipo);
		sqlWhereClause.addAndCondition("CDSUPERVISOR = ", metaVendaSup.cdSupervisor);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}