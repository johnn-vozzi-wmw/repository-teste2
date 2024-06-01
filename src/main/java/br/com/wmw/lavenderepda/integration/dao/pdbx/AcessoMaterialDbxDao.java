package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AcessoMaterial;
import totalcross.sql.ResultSet;

public class AcessoMaterialDbxDao extends LavendereCrudDbxDao {

    private static AcessoMaterialDbxDao instance;

    public AcessoMaterialDbxDao() {
        super(AcessoMaterial.TABLE_NAME); 
    }
    
    public static AcessoMaterialDbxDao getInstance() {
        if (instance == null) {
            instance = new AcessoMaterialDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        AcessoMaterial acessoMaterial = new AcessoMaterial();
        acessoMaterial.rowKey = rs.getString("rowkey");
        acessoMaterial.cdEmpresa = rs.getString("cdEmpresa");
        acessoMaterial.cdRepresentante = rs.getString("cdRepresentante");
        acessoMaterial.cdUsuarioAcesso = rs.getString("cdUsuarioAcesso");
        acessoMaterial.cdAcessoMaterial = rs.getString("cdAcessoMaterial");
        acessoMaterial.nmMaterial = rs.getString("nmMaterial");
        acessoMaterial.dtAcesso = rs.getDate("dtAcesso");
        acessoMaterial.hrAcesso = rs.getString("hrAcesso");
        acessoMaterial.nuCarimbo = rs.getInt("nuCarimbo");
        acessoMaterial.flTipoAlteracao = rs.getString("flTipoAlteracao");
        acessoMaterial.cdUsuario = rs.getString("cdUsuario");
        return acessoMaterial;
    }
    
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUSUARIOACESSO,");
        sql.append(" CDACESSOMATERIAL,");
        sql.append(" NMMATERIAL,");
        sql.append(" DTACESSO,");
        sql.append(" HRACESSO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUSUARIOACESSO,");
        sql.append(" CDACESSOMATERIAL,");
        sql.append(" NMMATERIAL,");
        sql.append(" DTACESSO,");
        sql.append(" HRACESSO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        AcessoMaterial acessoMaterial = (AcessoMaterial) domain;
        sql.append(Sql.getValue(acessoMaterial.cdEmpresa)).append(",");
        sql.append(Sql.getValue(acessoMaterial.cdRepresentante)).append(",");
        sql.append(Sql.getValue(acessoMaterial.cdUsuarioAcesso)).append(",");
        sql.append(Sql.getValue(acessoMaterial.cdAcessoMaterial)).append(",");
        sql.append(Sql.getValue(acessoMaterial.nmMaterial)).append(",");
        sql.append(Sql.getValue(acessoMaterial.dtAcesso)).append(",");
        sql.append(Sql.getValue(acessoMaterial.hrAcesso)).append(",");
        sql.append(Sql.getValue(acessoMaterial.nuCarimbo)).append(",");
        sql.append(Sql.getValue(acessoMaterial.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(acessoMaterial.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        AcessoMaterial acessoMaterial = (AcessoMaterial) domain;
        sql.append(" NMMATERIAL = ").append(Sql.getValue(acessoMaterial.nmMaterial)).append(",");
        sql.append(" DTACESSO = ").append(Sql.getValue(acessoMaterial.dtAcesso)).append(",");
        sql.append(" HRACESSO = ").append(Sql.getValue(acessoMaterial.hrAcesso)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(acessoMaterial.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(acessoMaterial.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(acessoMaterial.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        AcessoMaterial acessoMaterial = (AcessoMaterial) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", acessoMaterial.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", acessoMaterial.cdRepresentante);
		sqlWhereClause.addAndCondition("CDUSUARIOACESSO = ", acessoMaterial.cdUsuarioAcesso);
		sqlWhereClause.addAndCondition("CDACESSOMATERIAL = ", acessoMaterial.cdAcessoMaterial);
		//--
		sql.append(sqlWhereClause.getSql());
    }


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AcessoMaterial();
	}
    
}