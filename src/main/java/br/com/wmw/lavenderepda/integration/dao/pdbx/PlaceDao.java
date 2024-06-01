package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Place;
import totalcross.sql.ResultSet;

public class PlaceDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Place();
	}

    private static PlaceDao instance;
	
    public PlaceDao() {
        super(Place.TABLE_NAME);
    }
    
    public static PlaceDao getInstance() {
        if (instance == null) {
            instance = new PlaceDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Place place = new Place();
        place.rowKey = rs.getString("rowkey");
        place.cdEmpresa = rs.getString("cdEmpresa");
        place.cdRepresentante = rs.getString("cdRepresentante");
        place.cdLista = rs.getString("cdLista");
        if (LavenderePdaConfig.isUsaCategoriaLeads()) {
            place.cdCategoria = rs.getString("cdCategoria");
        }
        place.cdPlaceId = rs.getString("cdPlaceId");
        place.flTipoAlteracao = rs.getString("flTipoAlteracao");
        place.nmPlace = rs.getString("nmPlace");
        place.dsEndereco = rs.getString("dsEndereco");
        place.dsTelefone = rs.getString("dsTelefone");
        place.dsUrl = rs.getString("dsUrl");
        place.cdLatitude = rs.getDouble("cdLatitude");
        place.cdLongitude = rs.getDouble("cdLongitude");
        place.vlRating = rs.getDouble("vlRating");
        place.flQualificado = rs.getString("flQualificado");
        place.dsObservacao = rs.getString("dsObservacao");
        place.dtInativacao = rs.getDate("dtInativacao");
        place.nuOrdem = rs.getInt("nuOrdem");
        place.nuCarimbo = rs.getInt("nuCarimbo");
        return place;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPLACEID,");
        sql.append(" CDLISTA,");
        if (LavenderePdaConfig.isUsaCategoriaLeads()) {
            sql.append(" CDCATEGORIA,");
        }
        sql.append(" NMPLACE,");
        sql.append(" DSENDERECO,");
        sql.append(" DSTELEFONE,");
        sql.append(" DSURL,");
        sql.append(" CDLATITUDE,");
        sql.append(" CDLONGITUDE,");
        sql.append(" VLRATING,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" FLQUALIFICADO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTINATIVACAO,");
        sql.append(" NUORDEM,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO");
    }
    
    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPLACEID,");
        sql.append(" CDLISTA,");
        if (LavenderePdaConfig.isUsaCategoriaLeads()) {
            sql.append(" CDCATEGORIA,");
        }
        sql.append(" NMPLACE,");
        sql.append(" DSENDERECO,");
        sql.append(" DSTELEFONE,");
        sql.append(" DSURL,");
        sql.append(" CDLATITUDE,");
        sql.append(" CDLONGITUDE,");
        sql.append(" VLRATING,");
        sql.append(" FLQUALIFICADO,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTINATIVACAO,");
        sql.append(" NUORDEM,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Place place = (Place) domain;
        sql.append(Sql.getValue(place.cdEmpresa)).append(",");
        sql.append(Sql.getValue(place.cdRepresentante)).append(",");
        sql.append(Sql.getValue(place.cdPlaceId)).append(",");
        sql.append(Sql.getValue(place.cdLista)).append(",");
        if (LavenderePdaConfig.isUsaCategoriaLeads()) {
            sql.append(Sql.getValue(place.cdCategoria)).append(",");
        }
        sql.append(Sql.getValue(place.nmPlace)).append(",");
        sql.append(Sql.getValue(place.dsEndereco)).append(",");
        sql.append(Sql.getValue(place.dsTelefone)).append(",");
        sql.append(Sql.getValue(place.dsUrl)).append(",");
        sql.append(Sql.getValue(place.cdLatitude)).append(",");
        sql.append(Sql.getValue(place.cdLongitude)).append(",");
        sql.append(Sql.getValue(place.vlRating)).append(",");
        sql.append(Sql.getValue(place.flQualificado)).append(",");
        sql.append(Sql.getValue(place.dsObservacao)).append(",");
        sql.append(Sql.getValue(place.dtInativacao)).append(",");
        sql.append(Sql.getValue(place.nuOrdem)).append(",");
        sql.append(Sql.getValue(place.cdUsuario)).append(",");
        sql.append(Sql.getValue(place.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(place.nuCarimbo));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Place place = (Place) domain;
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(place.cdRepresentante)).append(",");
        sql.append(" CDLISTA = ").append(Sql.getValue(place.cdLista)).append(",");
        if (LavenderePdaConfig.isUsaCategoriaLeads()) {
            sql.append(" CDCATEGORIA = ").append(Sql.getValue(place.cdCategoria)).append(",");
        }
        sql.append(" CDPLACEID = ").append(Sql.getValue(place.cdPlaceId)).append(",");
        sql.append(" NMPLACE = ").append(Sql.getValue(place.nmPlace)).append(",");
        sql.append(" DSENDERECO = ").append(Sql.getValue(place.dsEndereco)).append(",");
        sql.append(" DSTELEFONE = ").append(Sql.getValue(place.dsTelefone)).append(",");
        sql.append(" DSURL = ").append(Sql.getValue(place.dsUrl)).append(",");
        sql.append(" CDLATITUDE = ").append(Sql.getValue(place.cdLatitude)).append(",");
        sql.append(" CDLONGITUDE = ").append(Sql.getValue(place.cdLongitude)).append(",");
        sql.append(" VLRATING = ").append(Sql.getValue(place.vlRating)).append(",");
        sql.append(" FLQUALIFICADO = ").append(Sql.getValue(place.flQualificado)).append(",");
        sql.append(" DTINATIVACAO = ").append(Sql.getValue(place.dtInativacao)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(place.dsObservacao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(place.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(DateUtil.getCurrentDate())).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(TimeUtil.getCurrentTimeHHMMSS()));
        
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Place place = (Place) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", place.cdEmpresa);
		sqlWhereClause.addAndCondition("CDLISTA = ", place.cdLista);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", place.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPLACEID = ", place.cdPlaceId);
		sqlWhereClause.addAndCondition("NMPLACE = ", place.nmPlace);
		sqlWhereClause.addAndCondition("DSENDERECO = ", place.dsEndereco);
		sqlWhereClause.addAndCondition("FLQUALIFICADO = ", place.flQualificado);
		//--
		sql.append(sqlWhereClause.getSql());
		
		String dsFiltro = getDsFiltro(place.dsFiltro);
		if (ValueUtil.isNotEmpty(dsFiltro)) {
    		sql.append(" AND (");
    		sql.append("CDPLACEID LIKE ").append(Sql.getValue(dsFiltro));
    		sql.append(" OR NMPLACE LIKE ").append(Sql.getValue(dsFiltro));
   			sql.append(")");
   		}
    }
    
    private String getDsFiltro(String dsFiltro) {
    	if (ValueUtil.isEmpty(dsFiltro)) {
    		return "";
    	}
    	boolean onlyStartString = false;
    	if (LavenderePdaConfig.usaPesquisaInicioString) {
    		if (dsFiltro.startsWith("*")) {
    			dsFiltro = dsFiltro.substring(1);
    		} else {
    			onlyStartString = true;
    		}
    	}
    	return onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    }

    public int countByRowKey(Place place) throws SQLException {
    	return getInt("select count(*) as qtde from " + tableName + " where rowkey = " + Sql.getValue(place.getRowKey()));
    }
    
}