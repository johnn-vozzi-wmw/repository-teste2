package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RedeCliente;
import totalcross.sql.ResultSet;

public class RedeClientePdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RedeCliente();
	}

    private static RedeClientePdbxDao instance;

    public RedeClientePdbxDao() {
        super(RedeCliente.TABLE_NAME);
    }

    public static RedeClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new RedeClientePdbxDao();
        }
        return instance;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDREDE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NMFANTASIA,");
        sql.append(" DSBAIRROCOMERCIAL,");
        sql.append(" DSCIDADECOMERCIAL,");
        sql.append(" DSESTADOCOMERCIAL,");
        sql.append(" FLSTATUSCLIENTE,");
        sql.append(" VLMAXTITULOSATRASO,");
        sql.append(" NUDIASTOLERANCIAATRASO ");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        RedeCliente redeCliente = new RedeCliente();
        redeCliente.rowKey = rs.getString("rowkey");
        redeCliente.cdCliente = rs.getString("cdCliente");
        redeCliente.cdRede = rs.getString("cdRede");
        redeCliente.nmRazaoSocial = rs.getString("nmRazaoSocial");
        redeCliente.nmFantasia = rs.getString("nmFantasia");
        redeCliente.dsBairroComercial = rs.getString("dsBairroComercial");
        redeCliente.dsCidadeComercial = rs.getString("dsCidadeComercial");
        redeCliente.dsEstadoComercial = rs.getString("dsEstadoComercial");
        redeCliente.setFlStatusCliente(rs.getString("flStatusCliente"));
        redeCliente.vlMaxTitulosAtraso = rs.getDouble("vlMaxTitulosAtraso");
        redeCliente.nuDiasToleranciaAtraso = rs.getInt("nuDiasToleranciaAtraso");
        return redeCliente;
    }
    
    //@Override
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" rowKey,");
        sql.append(" CDCLIENTE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NMFANTASIA,");
        sql.append(" FLSTATUSCLIENTE ");
    }

    //@Override
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        RedeCliente redeCliente = new RedeCliente();
        redeCliente.rowKey = rs.getString("rowkey");
        redeCliente.cdCliente = rs.getString("cdCliente");
        redeCliente.nmRazaoSocial = rs.getString("nmRazaoSocial");
        redeCliente.nmFantasia = rs.getString("nmFantasia");
        redeCliente.setFlStatusCliente(rs.getString("flStatusCliente"));
        return redeCliente;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDCLIENTE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NMFANTASIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" ROWKEY");
    }
    
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	RedeCliente redeCliente = (RedeCliente) domain;
        sql.append(Sql.getValue(redeCliente.cdCliente)).append(",");
        sql.append(Sql.getValue(redeCliente.nmRazaoSocial)).append(",");
        sql.append(Sql.getValue(redeCliente.nmFantasia)).append(",");
        sql.append(Sql.getValue(redeCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(redeCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(redeCliente.cdUsuario)).append(",");
        sql.append(Sql.getValue(redeCliente.rowKey));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    }

    //@Override
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        RedeCliente redeClienteFilter = (RedeCliente) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDCLIENTE = ", redeClienteFilter.cdCliente);
        sqlWhereClause.addAndCondition("CDREDE = ", redeClienteFilter.cdRede);
        sqlWhereClause.addAndCondition("FLSTATUSCLIENTE = ", redeClienteFilter.flStatusClienteFilter);
        sql.append(sqlWhereClause.getSql());
    }

}