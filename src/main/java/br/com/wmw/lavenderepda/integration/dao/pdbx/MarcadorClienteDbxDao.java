package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.MarcadorCliente;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import totalcross.sql.ResultSet;

public class MarcadorClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MarcadorCliente();
	}

    private static MarcadorClienteDbxDao instance;

    public MarcadorClienteDbxDao() {
        super(MarcadorCliente.TABLE_NAME); 
    }
    
    public static MarcadorClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new MarcadorClienteDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        MarcadorCliente marcadorcliente = new MarcadorCliente();
        marcadorcliente.rowKey = rs.getString("rowkey");
        marcadorcliente.cdEmpresa = rs.getString("cdEmpresa");
        marcadorcliente.cdRepresentante = rs.getString("cdRepresentante");
        marcadorcliente.cdCliente = rs.getString("cdCliente");
        marcadorcliente.cdMarcador = rs.getString("cdMarcador");
        marcadorcliente.nuCarimbo = rs.getInt("nuCarimbo");
        marcadorcliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        marcadorcliente.cdUsuario = rs.getString("cdUsuario");
        return marcadorcliente;
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
        sql.append(" CDMARCADOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDMARCADOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        MarcadorCliente marcadorcliente = (MarcadorCliente) domain;
        sql.append(Sql.getValue(marcadorcliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(marcadorcliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(marcadorcliente.cdCliente)).append(",");
        sql.append(Sql.getValue(marcadorcliente.cdMarcador)).append(",");
        sql.append(Sql.getValue(marcadorcliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(marcadorcliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(marcadorcliente.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        MarcadorCliente marcadorcliente = (MarcadorCliente) domain;
        sql.append(" NUCARIMBO = ").append(Sql.getValue(marcadorcliente.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(marcadorcliente.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(marcadorcliente.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        MarcadorCliente marcadorcliente = (MarcadorCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", marcadorcliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", marcadorcliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", marcadorcliente.cdCliente);
		sqlWhereClause.addAndCondition("CDMARCADOR = ", marcadorcliente.cdMarcador);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
	public String findColorLastMarcadorCli(Cliente cliente) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append(" vlR || ',' || vlG || ',' || vlB AS RGB ");
		sql.append(" FROM ").append(tableName).append(" TB ");
		sql.append(" JOIN TBLVPMARCADOR MARC ON ");
		sql.append(" TB.CDMARCADOR = MARC.CDMARCADOR ");
		sql.append(" LEFT OUTER JOIN TBLVPCORSISTEMA COR ON ");
		sql.append(" MARC.CDCORFONTE = COR.cdCor ");
		sql.append(" AND COR.CDESQUEMACOR = ").append(Sql.getValue(TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual().cdEsquemaCor));
		sql.append(" WHERE TB.CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa)); 
		sql.append(" AND TB.CDREPRESENTANTE = ").append(Sql.getValue(cliente.cdRepresentante));
		sql.append(" AND TB.CDCLIENTE = ").append(Sql.getValue(cliente.cdCliente));
		sql.append(" AND MARC.CDCORFONTE IS NOT NULL AND MARC.CDCORFONTE > 0");
		sql.append(" ORDER BY NUSEQUENCIA DESC limit 1 ");
		return getString(sql.toString());
	}

}
