package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PontoGpsDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontoGps();
	}

    private static PontoGpsDbxDao instance;

    public PontoGpsDbxDao() {
        super(PontoGps.TABLE_NAME);
    }

    public static PontoGpsDbxDao getInstance() {
        if (instance == null) {
            instance = new PontoGpsDbxDao();
        }
        return instance;
    }


    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PontoGps pontogps = new PontoGps();
        pontogps.rowKey = rs.getString("rowkey");
        pontogps.cdEmpresa = rs.getString("cdEmpresa");
        pontogps.cdRepresentante = rs.getString("cdRepresentante");
        pontogps.vlLatitude = ValueUtil.round(rs.getDouble("vlLatitude"));
        pontogps.vlLongitude = ValueUtil.round(rs.getDouble("vlLongitude"));
        pontogps.vlVelocidade = ValueUtil.round(rs.getDouble("vlVelocidade"));
        	pontogps.vlPrecisao = ValueUtil.round(rs.getDouble("vlPrecisao"));
        pontogps.dtColeta = rs.getDate("dtColeta");
        pontogps.hrColeta = rs.getString("hrColeta");
        pontogps.nuCarimbo = rs.getInt("nuCarimbo");
        pontogps.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pontogps.flStatus = rs.getString("flStatus");
        pontogps.cdUsuario = rs.getString("cdUsuario");
        return pontogps;
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
        sql.append(" VLLATITUDE,");
        sql.append(" VLLONGITUDE,");
        sql.append(" VLVELOCIDADE,");
        	sql.append(" VLPRECISAO,");
        sql.append(" DTCOLETA,");
        sql.append(" HRCOLETA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLSTATUS,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" VLLATITUDE,");
        sql.append(" VLLONGITUDE,");
        sql.append(" VLVELOCIDADE,");
        	sql.append(" VLPRECISAO,");
        sql.append(" DTCOLETA,");
        sql.append(" HRCOLETA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLSTATUS,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PontoGps pontogps = (PontoGps) domain;
        sql.append(Sql.getValue(pontogps.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pontogps.cdRepresentante)).append(",");
        sql.append(ValueUtil.getDoubleValueTruncated(pontogps.vlLatitude,7) + "").append(",");
        sql.append(ValueUtil.getDoubleValueTruncated(pontogps.vlLongitude,7) + "").append(",");
        sql.append(Sql.getValue(pontogps.vlVelocidade)).append(",");
        	sql.append(Sql.getValue(pontogps.vlPrecisao)).append(",");
        sql.append(Sql.getValue(pontogps.dtColeta)).append(",");
        sql.append(Sql.getValue(pontogps.hrColeta)).append(",");
        sql.append(Sql.getValue(pontogps.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pontogps.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pontogps.flStatus)).append(",");
        sql.append(Sql.getValue(pontogps.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PontoGps pontogps = (PontoGps) domain;
        sql.append(" VLLATITUDE = ").append(Sql.getValue(ValueUtil.getDoubleValueTruncated(pontogps.vlLatitude,7))).append(",");
        sql.append(" VLLONGITUDE = ").append(Sql.getValue(ValueUtil.getDoubleValueTruncated(pontogps.vlLongitude,7))).append(",");
        sql.append(" VLVELOCIDADE = ").append(Sql.getValue(pontogps.vlVelocidade)).append(",");
        	sql.append(" VLPRECISAO = ").append(Sql.getValue(pontogps.vlPrecisao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(pontogps.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pontogps.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(pontogps.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PontoGps pontogps = (PontoGps) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pontogps.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pontogps.cdRepresentante);
		sqlWhereClause.addAndCondition("DTCOLETA = ", pontogps.dtColeta);
		sqlWhereClause.addAndCondition("HRCOLETA = ", pontogps.hrColeta);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public void deletePontoGpsEnviadosServidor() {
        StringBuffer sql = getSqlBuffer();
        sql.append(" delete from ");
        sql.append(tableName);
    	sql.append(" where fltipoalteracao = ");
    	sql.append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
        try {
        	executeUpdate(sql.toString());
        } catch (Throwable e) {
			// Não faz anda, apenas não exclui nenhum registro
		}
    }

    public String findAllEnvioServidorRs() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where flTipoAlteracao != ").append(Sql.getValue(PontoGps.FLTIPOALTERACAO_ORIGINAL));
        return sql.toString();
    }

    public void updatePontosGpsEnviadosServidor(Vector listRowkeys, boolean envioComSucesso) throws SQLException {
    	for (int i = 0; i < listRowkeys.size(); i++) {
    		StringBuffer sql = getSqlBuffer();
    		sql.append(" update ");
    		sql.append(tableName);
    		if (envioComSucesso) {
    			sql.append(" set flTipoAlteracao = ").append(Sql.getValue(PontoGps.FLTIPOALTERACAO_ORIGINAL));
    		} else {
    			sql.append(" set flTipoAlteracao = ").append(Sql.getValue(PontoGps.FLTIPOALTERACAO_ALTERADO));
    		}
    		sql.append(" where rowKey = ").append(Sql.getValue((String) listRowkeys.items[i]));
    		executeUpdate(sql.toString());
    	}
    }

	public Vector findAllPontosGpsOrdemDtColeta() throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" order by dtColeta desc, hrColeta desc");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector listPontosGps = new Vector();
			PontoGps pontoGps;
			while (rs.next()) {
				pontoGps = (PontoGps) populate(getBaseDomainDefault(), rs);
				listPontosGps.addElement(pontoGps);
			}
			return listPontosGps;
		}
	}

}