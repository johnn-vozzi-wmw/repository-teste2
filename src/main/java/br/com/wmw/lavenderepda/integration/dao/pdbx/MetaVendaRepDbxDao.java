package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MetaVendaRep;
import totalcross.sql.ResultSet;

public class MetaVendaRepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MetaVendaRep();
	}

    private static MetaVendaRepDbxDao instance;

    public MetaVendaRepDbxDao() {
        super(MetaVendaRep.TABLE_NAME);
    }

    public static MetaVendaRepDbxDao getInstance() {
        if (instance == null) {
            instance = new MetaVendaRepDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MetaVendaRep metaVendaRep = new MetaVendaRep();
        metaVendaRep.rowKey = rs.getString("rowkey");
        metaVendaRep.cdEmpresa = rs.getString("cdEmpresa");
        metaVendaRep.cdMetaVenda = rs.getString("cdMetaVenda");
        metaVendaRep.cdGerente = rs.getString("cdGerente");
        metaVendaRep.vlChaveTipo = rs.getString("vlChaveTipo");
        metaVendaRep.cdSupervisor = rs.getString("cdSupervisor");
        metaVendaRep.cdRepresentante = rs.getString("cdRepresentante");
        metaVendaRep.vlMetaVenda = ValueUtil.round(rs.getDouble("vlMetaVenda"));
        metaVendaRep.vlRealizadoVenda = ValueUtil.round(rs.getDouble("vlRealizadoVenda"));
        metaVendaRep.nuCarimbo = rs.getInt("nuCarimbo");
        metaVendaRep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        metaVendaRep.cdUsuario = rs.getString("cdUsuario");
        return metaVendaRep;
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
        sql.append(" CDREPRESENTANTE,");
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
        sql.append(" CDREPRESENTANTE,");
        sql.append(" VLMETAVENDA,");
        sql.append(" VLREALIZADOVENDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaRep metaVendaRep = (MetaVendaRep) domain;
        sql.append(Sql.getValue(metaVendaRep.cdEmpresa)).append(",");
        sql.append(Sql.getValue(metaVendaRep.cdMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaRep.cdGerente)).append(",");
        sql.append(Sql.getValue(metaVendaRep.vlChaveTipo)).append(",");
        sql.append(Sql.getValue(metaVendaRep.cdSupervisor)).append(",");
        sql.append(Sql.getValue(metaVendaRep.cdRepresentante)).append(",");
        sql.append(Sql.getValue(metaVendaRep.vlMetaVenda)).append(",");
        sql.append(Sql.getValue(metaVendaRep.vlRealizadoVenda)).append(",");
        sql.append(Sql.getValue(metaVendaRep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(metaVendaRep.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(metaVendaRep.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaRep metaVendaRep = (MetaVendaRep) domain;
        sql.append(" VLMETAVENDA = ").append(Sql.getValue(metaVendaRep.vlMetaVenda)).append(",");
        sql.append(" VLREALIZADOVENDA = ").append(Sql.getValue(metaVendaRep.vlRealizadoVenda)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(metaVendaRep.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(metaVendaRep.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(metaVendaRep.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MetaVendaRep metaVendaRep = (MetaVendaRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", metaVendaRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDMETAVENDA = ", metaVendaRep.cdMetaVenda);
		sqlWhereClause.addAndCondition("CDGERENTE = ", metaVendaRep.cdGerente);
		sqlWhereClause.addAndCondition("VLCHAVETIPO = ", metaVendaRep.vlChaveTipo);
		sqlWhereClause.addAndCondition("CDSUPERVISOR = ", metaVendaRep.cdSupervisor);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", metaVendaRep.cdRepresentante);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}