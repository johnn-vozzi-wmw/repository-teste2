package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteChurnAtua;
import totalcross.sql.ResultSet;

public class ClienteChurnAtuaDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteChurnAtua();
	}

    private static ClienteChurnAtuaDao instance = null;

    public ClienteChurnAtuaDao() {
        super(ClienteChurnAtua.TABLE_NAME);
    }
    
    public static ClienteChurnAtuaDao getInstance() {
        if (instance == null) {
            instance = new ClienteChurnAtuaDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ClienteChurnAtua clienteChurnAtua = new ClienteChurnAtua();
        clienteChurnAtua.rowKey = rs.getString("rowkey");
        clienteChurnAtua.cdEmpresa = rs.getString("cdEmpresa");
        clienteChurnAtua.cdRepresentante = rs.getString("cdRepresentante");
        clienteChurnAtua.cdChurn = rs.getString("cdChurn");
        clienteChurnAtua.cdChurnAtua = rs.getString("cdChurnAtua");
        clienteChurnAtua.dsObsChurn = rs.getString("dsObsChurn");
        clienteChurnAtua.cdUsuario = rs.getString("cdUsuario");
        clienteChurnAtua.nuCarimbo = rs.getInt("nuCarimbo");
        clienteChurnAtua.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteChurnAtua.dtAlteracao = rs.getDate("dtAlteracao");
        clienteChurnAtua.hrAlteracao = rs.getString("hrAlteracao");
        return clienteChurnAtua;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCHURN,");
        sql.append(" CDCHURNATUA,");
        sql.append(" DSOBSCHURN,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
    	sql.append(" ROWKEY,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCHURN,");
        sql.append(" CDCHURNATUA,");
        sql.append(" CDMOTIVOCHURN,");
        sql.append(" DSOBSCHURN,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ClienteChurnAtua clienteChurnAtua = (ClienteChurnAtua) domain;
        sql.append(Sql.getValue(clienteChurnAtua.rowKey)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.cdEmpresa)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.cdRepresentante)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.cdChurn)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.cdChurnAtua)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.cdMotivoChurn)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.dsObsChurn)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.cdUsuario)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.nuCarimbo)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.dtAlteracao)).append(",");
        sql.append(Sql.getValue(clienteChurnAtua.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ClienteChurnAtua clienteChurnAtua = (ClienteChurnAtua) domain;
        sql.append(" CDMOTIVOCHURN = ").append(Sql.getValue(clienteChurnAtua.cdMotivoChurn)).append(",");
        sql.append(" DSOBSCHURN = ").append(Sql.getValue(clienteChurnAtua.dsObsChurn)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(clienteChurnAtua.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(clienteChurnAtua.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(clienteChurnAtua.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(clienteChurnAtua.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(clienteChurnAtua.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
    	ClienteChurnAtua clienteChurnAtua = (ClienteChurnAtua) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteChurnAtua.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteChurnAtua.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCHURN = ", clienteChurnAtua.cdChurn);
		sqlWhereClause.addAndCondition("CDCHURNATUA = ", clienteChurnAtua.cdChurnAtua);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" order by cdChurnAtua desc ");
    }
    
}