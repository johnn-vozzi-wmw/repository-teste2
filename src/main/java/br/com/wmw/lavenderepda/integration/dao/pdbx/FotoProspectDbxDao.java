package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FotoProspect;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class FotoProspectDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoProspect();
	}

    private static FotoProspectDbxDao instance;

    public FotoProspectDbxDao() {
        super(FotoProspect.TABLE_NAME); 
    }
    
    public static FotoProspectDbxDao getInstance() {
        if (instance == null) {
            instance = new FotoProspectDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        FotoProspect fotoProspect = new FotoProspect();
        fotoProspect.rowKey = rs.getString("rowkey");
        fotoProspect.cdEmpresa = rs.getString("cdEmpresa");
        fotoProspect.cdRepresentante = rs.getString("cdRepresentante");
        fotoProspect.flOrigemProspect = rs.getString("flOrigemProspect");
        fotoProspect.cdProspect = rs.getString("cdProspect");
        fotoProspect.nmFoto = rs.getString("nmFoto");
        fotoProspect.nuTamanho = rs.getInt("nuTamanho");
        fotoProspect.dtModificacao = rs.getDate("dtModificacao");
        fotoProspect.cdUsuario = rs.getString("cdUsuario");
        fotoProspect.nuCarimbo = rs.getInt("nuCarimbo");
        fotoProspect.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return fotoProspect;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPROSPECT,");
        sql.append(" CDPROSPECT,");
        sql.append(" NMFOTO,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPROSPECT,");
        sql.append(" CDPROSPECT,");
        sql.append(" NMFOTO,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FotoProspect fotoProspect = (FotoProspect) domain;
        sql.append(Sql.getValue(fotoProspect.cdEmpresa)).append(",");
        sql.append(Sql.getValue(fotoProspect.cdRepresentante)).append(",");
        sql.append(Sql.getValue(fotoProspect.flOrigemProspect)).append(",");
        sql.append(Sql.getValue(fotoProspect.cdProspect)).append(",");
        sql.append(Sql.getValue(fotoProspect.nmFoto)).append(",");
        sql.append(Sql.getValue(fotoProspect.nuTamanho)).append(",");
        sql.append(Sql.getValue(fotoProspect.dtModificacao)).append(",");
        sql.append(Sql.getValue(fotoProspect.cdUsuario)).append(",");
        sql.append(Sql.getValue(fotoProspect.nuCarimbo)).append(",");
        sql.append(Sql.getValue(fotoProspect.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FotoProspect fotoProspect = (FotoProspect) domain;
        sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoProspect.nuTamanho)).append(",");
        sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoProspect.dtModificacao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoProspect.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(fotoProspect.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoProspect.flTipoAlteracao)).append(",");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FotoProspect fotoProspect = (FotoProspect) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoProspect.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoProspect.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPROSPECT = ", fotoProspect.flOrigemProspect);
		sqlWhereClause.addAndCondition("CDPROSPECT = ", fotoProspect.cdProspect);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoProspect.nmFoto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findNmFotoAllAlterados() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT NMFOTO FROM TBLVPFOTOPROSPECT")
    	.append(" WHERE FLTIPOALTERACAO <> ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
    	Vector list = new Vector(50);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			list.addElement(rs.getString(1));
    		}
		}
    	return list;
    }
    
}