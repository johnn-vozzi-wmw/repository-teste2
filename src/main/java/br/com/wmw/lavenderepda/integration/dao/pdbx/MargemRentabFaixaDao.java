package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.MargemRentabFaixa;
import totalcross.sql.ResultSet;

public class MargemRentabFaixaDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MargemRentabFaixa();
	}

    private static MargemRentabFaixaDao instance = null;

    public MargemRentabFaixaDao() {
        super(MargemRentabFaixa.TABLE_NAME);
    }
    
    public static MargemRentabFaixaDao getInstance() {
        if (instance == null) {
            instance = new MargemRentabFaixaDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        MargemRentabFaixa margemRentabFaixa = new MargemRentabFaixa();
        margemRentabFaixa.rowKey = rs.getString("rowkey");
        margemRentabFaixa.cdEmpresa = rs.getString("cdEmpresa");
        margemRentabFaixa.cdMargemRentab = rs.getString("cdMargemRentab");
        margemRentabFaixa.vlPctMargemRentab = rs.getDouble("vlPctMargemRentab");
        margemRentabFaixa.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
        margemRentabFaixa.nuOrdemLiberacao = rs.getInt("nuOrdemLiberacao");
        margemRentabFaixa.cdCorFaixa = rs.getInt("cdCorFaixa");
        margemRentabFaixa.dsCorFaixa = rs.getString("dsCorFaixa");
        margemRentabFaixa.flTipoAlteracao = rs.getString("flTipoAlteracao");
        margemRentabFaixa.nuCarimbo = rs.getInt("nuCarimbo");
        margemRentabFaixa.dtAlteracao = rs.getDate("dtAlteracao");
        margemRentabFaixa.hrAlteracao = rs.getString("hrAlteracao");
        margemRentabFaixa.cdUsuario = rs.getString("cdUsuario");
        return margemRentabFaixa;
    }
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDMARGEMRENTAB,");
        sql.append(" VLPCTMARGEMRENTAB,");
        sql.append(" CDMOTIVOPENDENCIA,");
        sql.append(" NUORDEMLIBERACAO,");
        sql.append(" CDCORFAIXA,");
        sql.append(" DSCORFAIXA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
    //@Override
    protected void addInsertColumns(StringBuffer sql) { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) { }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MargemRentabFaixa margemrentabfaixa = (MargemRentabFaixa) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", margemrentabfaixa.cdEmpresa);
		sqlWhereClause.addAndConditionForced("CDMARGEMRENTAB = ", margemrentabfaixa.cdMargemRentab);
		sqlWhereClause.addAndConditionForced("VLPCTMARGEMRENTAB <= ", margemrentabfaixa.vlPctMargemRentab);
		sql.append(sqlWhereClause.getSql());
    }
        
}