package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliente;
import totalcross.sql.ResultSet;

public class PlataformaVendaClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PlataformaVendaCliente();
	}

    private static PlataformaVendaClienteDbxDao instance;

    public PlataformaVendaClienteDbxDao() {
        super(PlataformaVendaCliente.TABLE_NAME); 
    }
    
    public static PlataformaVendaClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaClienteDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        PlataformaVendaCliente plataformavendacliente = new PlataformaVendaCliente();
        plataformavendacliente.rowKey = rs.getString("rowkey");
        plataformavendacliente.cdEmpresa = rs.getString("cdEmpresa");
        plataformavendacliente.cdRepresentante = rs.getString("cdRepresentante");
        plataformavendacliente.cdCliente = rs.getString("cdCliente");
        plataformavendacliente.cdPlataformaVenda = rs.getString("cdPlataformaVenda");
        plataformavendacliente.cdCentroCusto = rs.getString("cdCentrocusto");
        plataformavendacliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        plataformavendacliente.cdUsuario = rs.getString("cdUsuario");
        return plataformavendacliente;
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
        sql.append(" CDCLIENTE,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" CDCENTROCUSTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPLATAFORMAVENDA,");
        sql.append(" CDCENTROCUSTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        PlataformaVendaCliente plataformavendacliente = (PlataformaVendaCliente) domain;
        sql.append(Sql.getValue(plataformavendacliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(plataformavendacliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(plataformavendacliente.cdCliente)).append(",");
        sql.append(Sql.getValue(plataformavendacliente.cdPlataformaVenda)).append(",");
        sql.append(Sql.getValue(plataformavendacliente.cdCentroCusto)).append(",");
        sql.append(Sql.getValue(plataformavendacliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(plataformavendacliente.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        PlataformaVendaCliente plataformavendacliente = (PlataformaVendaCliente) domain;
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(plataformavendacliente.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(plataformavendacliente.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PlataformaVendaCliente plataformavendacliente = (PlataformaVendaCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", plataformavendacliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", plataformavendacliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", plataformavendacliente.cdCliente);
		sqlWhereClause.addAndCondition("CDPLATAFORMAVENDA = ", plataformavendacliente.cdPlataformaVenda);
		sqlWhereClause.addAndCondition("CDCENTROCUSTO = ", plataformavendacliente.cdCentroCusto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public boolean hasPlataformaForThisClient(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT COUNT(cdPlataformaVenda) as qtd FROM ");
    	sql.append(tableName);
    	
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("CDEMPRESA = ", cdEmpresa);
    	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", cdRepresentante);
    	sqlWhereClause.addAndCondition("CDCLIENTE = ", cdCliente);
    	sql.append(sqlWhereClause.getSql());
    	return getInt(sql.toString()) > 0;
    }
}