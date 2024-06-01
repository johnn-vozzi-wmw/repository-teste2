package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoVeiculo;
import totalcross.sql.ResultSet;

public class TipoVeiculoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoVeiculo();
	}

    private static TipoVeiculoDao instance;

    public TipoVeiculoDao() {
    	 super(TipoVeiculo.TABLE_NAME);
    }
    
    public static TipoVeiculoDao getInstance() {
        if (instance == null) {
            instance = new TipoVeiculoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoVeiculo tipoVeiculo = new TipoVeiculo();
        tipoVeiculo.rowKey = rs.getString("rowkey");
        tipoVeiculo.cdEmpresa = rs.getString("cdEmpresa");
        tipoVeiculo.cdRepresentante = rs.getString("cdRepresentante");
        tipoVeiculo.cdTipoVeiculo = rs.getString("cdTipoVeiculo");
        tipoVeiculo.dsTipoVeiculo = rs.getString("dsTipoVeiculo");
        tipoVeiculo.nuCarimbo = rs.getInt("nuCarimbo");
        tipoVeiculo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoVeiculo.cdUsuario = rs.getString("cdUsuario");
        return tipoVeiculo;
    }
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTIPOVEICULO,");
        sql.append(" DSTIPOVEICULO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	TipoVeiculo tipoVeiculo = (TipoVeiculo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoVeiculo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoVeiculo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPOVEICULO = ", tipoVeiculo.cdTipoVeiculo);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	//@Override
	protected void addInsertColumns(StringBuffer arg0) { }

	//@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) { }

	//@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) { }
    
}