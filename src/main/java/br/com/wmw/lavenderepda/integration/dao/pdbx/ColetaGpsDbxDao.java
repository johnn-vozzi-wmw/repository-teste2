package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ColetaGps;
import totalcross.sql.ResultSet;

public class ColetaGpsDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ColetaGps();
	}

    private static ColetaGpsDbxDao instance;

    public ColetaGpsDbxDao() {
        super(ColetaGps.TABLE_NAME);
    }
    
    public static ColetaGpsDbxDao getInstance() {
        if (instance == null) {
            instance = new ColetaGpsDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ColetaGps coletaGps = new ColetaGps();
        coletaGps.rowKey = rs.getString("rowkey");
        coletaGps.cdEmpresa = rs.getString("cdEmpresa");
        coletaGps.cdRepresentante = rs.getString("cdRepresentante");
        coletaGps.dtColetaGps = rs.getDate("dtColetagps");
        coletaGps.hrInicioColeta = rs.getString("hrIniciocoleta");
        coletaGps.hrFimColeta = rs.getString("hrFimcoleta");
        coletaGps.nuCarimbo = rs.getInt("nuCarimbo");
        coletaGps.flTipoAlteracao = rs.getString("flTipoAlteracao");
        if (LavenderePdaConfig.usaMotivoParadaColetaGps) {
        	coletaGps.cdMotivoColeta = rs.getString("cdMotivoColeta");
        }
        if (LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual()) {
        	coletaGps.dtEncerramentoAuto = rs.getDate("dtEncerramentoAuto");
        	coletaGps.hrEncerramentoAuto = rs.getString("hrEncerramentoAuto");
		}
        coletaGps.cdUsuario = rs.getString("cdUsuario");
        return coletaGps;
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
        sql.append(" DTCOLETAGPS,");
        sql.append(" HRINICIOCOLETA,");
        sql.append(" HRFIMCOLETA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        if (LavenderePdaConfig.usaMotivoParadaColetaGps) {
        	sql.append(" CDMOTIVOCOLETA,");
        }
        if (LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual()) {
        	sql.append(" DTENCERRAMENTOAUTO,");
        	sql.append(" HRENCERRAMENTOAUTO,");
		}
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTCOLETAGPS,");
        sql.append(" HRINICIOCOLETA,");
        sql.append(" HRFIMCOLETA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        if (LavenderePdaConfig.usaMotivoParadaColetaGps) {
        	sql.append(" CDMOTIVOCOLETA,");
        }
        if (LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual()) {
        	sql.append(" DTENCERRAMENTOAUTO,");
        	sql.append(" HRENCERRAMENTOAUTO,");
		}
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ColetaGps coletaGps = (ColetaGps) domain;
        sql.append(Sql.getValue(coletaGps.cdEmpresa)).append(",");
        sql.append(Sql.getValue(coletaGps.cdRepresentante)).append(",");
        sql.append(Sql.getValue(coletaGps.dtColetaGps)).append(",");
        sql.append(Sql.getValue(coletaGps.hrInicioColeta)).append(",");
        sql.append(Sql.getValue(coletaGps.hrFimColeta)).append(",");
        sql.append(Sql.getValue(coletaGps.nuCarimbo)).append(",");
        sql.append(Sql.getValue(coletaGps.flTipoAlteracao)).append(",");
        if (LavenderePdaConfig.usaMotivoParadaColetaGps) {
        	sql.append(Sql.getValue(coletaGps.cdMotivoColeta)).append(",");
        }
        if (LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual()) {
        	sql.append(Sql.getValue(coletaGps.dtEncerramentoAuto)).append(",");
        	sql.append(Sql.getValue(coletaGps.hrEncerramentoAuto)).append(",");
		}
        sql.append(Sql.getValue(coletaGps.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ColetaGps coletaGps = (ColetaGps) domain;
        sql.append(" HRFIMCOLETA = ").append(Sql.getValue(coletaGps.hrFimColeta)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(coletaGps.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(coletaGps.flTipoAlteracao)).append(",");
        if (LavenderePdaConfig.usaMotivoParadaColetaGps) {
        	sql.append(" CDMOTIVOCOLETA = ").append(Sql.getValue(coletaGps.cdMotivoColeta)).append(",");
        }
        if (LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual()) {
        	sql.append(" DTENCERRAMENTOAUTO = ").append(Sql.getValue(coletaGps.dtEncerramentoAuto)).append(",");
        	sql.append(" HRENCERRAMENTOAUTO = ").append(Sql.getValue(coletaGps.hrEncerramentoAuto)).append(",");
		}
        sql.append(" CDUSUARIO = ").append(Sql.getValue(coletaGps.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ColetaGps coletaGps = (ColetaGps) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", coletaGps.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", coletaGps.cdRepresentante);
		sqlWhereClause.addAndCondition("DTCOLETAGPS = ", coletaGps.dtColetaGps);
		sqlWhereClause.addAndCondition("HRINICIOCOLETA = ", coletaGps.hrInicioColeta);
		if (coletaGps.onlyColetaGpsEmAndamento) {
			sqlWhereClause.addAndCondition("HRFIMCOLETA = ''");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}