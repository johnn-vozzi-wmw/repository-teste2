package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMetaPerid;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PlatVendaMetaPeridDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlatVendaMetaPerid();
	}

	private static PlatVendaMetaPeridDbxDao instance;

	public PlatVendaMetaPeridDbxDao() {
		super(PlatVendaMetaPerid.TABLE_NAME);
	}

    public static PlatVendaMetaPeridDbxDao getInstance() {
        if (instance == null) {
            instance = new PlatVendaMetaPeridDbxDao();
        }
        return instance;
    }	
	
	@Override
	protected void addInsertColumns(StringBuffer arg0) throws SQLException {

	}

	@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {

	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPERIODO,");
        sql.append(" DSPERIODO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
	}

	@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {

	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PlatVendaMetaPerid platVendaMetaPerid = (PlatVendaMetaPerid) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", platVendaMetaPerid.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", platVendaMetaPerid.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPERIODO = ", platVendaMetaPerid.cdPeriodo);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PlatVendaMetaPerid platVendaMetaPerid = new PlatVendaMetaPerid();
		platVendaMetaPerid.rowKey = rs.getString("rowkey");
		platVendaMetaPerid.cdEmpresa = rs.getString("cdEmpresa");
		platVendaMetaPerid.cdRepresentante = rs.getString("cdRepresentante");
		platVendaMetaPerid.cdPeriodo = rs.getString("cdPeriodo");
		platVendaMetaPerid.dsPeriodo = rs.getString("dsPeriodo");
		platVendaMetaPerid.dtInicial = rs.getDate("dtInicial");		
		platVendaMetaPerid.dtFinal = rs.getDate("dtFinal");		
		platVendaMetaPerid.flTipoAlteracao = rs.getString("flTipoAlteracao");
		platVendaMetaPerid.nuCarimbo = rs.getInt("nuCarimbo");
		platVendaMetaPerid.cdUsuario = rs.getString("cdUsuario");	
		return platVendaMetaPerid;
	}
	
	public String findDsPeriodoByDataAndRepresentante(Date data, String cdRepresentante) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		
		sql.append(" SELECT DSPERIODO FROM TBLVPPLATVENDAMETAPERID ");
		sql.append(" WHERE ");
		sql.append(" CDEMPRESA = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND ").append(Sql.getValue(data)).append(" BETWEEN DTINICIAL AND DTFINAL");
		sql.append(" LIMIT 1");
		return getString(sql.toString());
	}
	
	public Vector findAllGroupBy() throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
		Vector list = new Vector();
		
		sql.append(" SELECT ");
		addSelectColumns(domainFilter, sql);
		sql.append(" FROM TBLVPPLATVENDAMETAPERID");
		sql.append(" GROUP BY CDEMPRESA, CDPERIODO");
		
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				PlatVendaMetaPerid platVendaMetaPerid = (PlatVendaMetaPerid) populate(domainFilter, rs);
				list.addElement(platVendaMetaPerid);
			}
			return list;
		}
	}
	
}
