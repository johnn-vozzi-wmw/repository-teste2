package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.GrupoDescCli;
import totalcross.sql.ResultSet;

public class GrupoDescCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoDescCli();
	}

    private static GrupoDescCliDbxDao instance;

    public GrupoDescCliDbxDao() {
        super(GrupoDescCli.TABLE_NAME);
    }
    
    public static GrupoDescCliDbxDao getInstance() {
        return (instance == null) ? instance = new GrupoDescCliDbxDao() : instance;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        GrupoDescCli grupoDescCli = new GrupoDescCli();
        grupoDescCli.rowKey = rs.getString("rowkey");
        grupoDescCli.cdEmpresa = rs.getString("cdEmpresa");
        grupoDescCli.cdRepresentante = rs.getString("cdRepresentante");
        grupoDescCli.cdCliente = rs.getString("cdCliente");
        grupoDescCli.cdGrupoDescCli = rs.getString("cdGrupoDescCli");
        grupoDescCli.dsGrupoDescCli = rs.getString("dsGrupoDescCli");
        grupoDescCli.nuPrioridade = rs.getInt("nuPrioridade");
        grupoDescCli.nuCarimbo = rs.getInt("nuCarimbo");
        grupoDescCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoDescCli.cdUsuario = rs.getString("cdUsuario");
        return grupoDescCli;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDGRUPODESCCLI,");
        sql.append(" DSGRUPODESCCLI,");  
        sql.append(" NUPRIORIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDGRUPODESCCLI,");
        sql.append(" DSGRUPODESCCLI,");
        sql.append(" NUPRIORIDADE,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoDescCli grupoDescCli = (GrupoDescCli) domain;
        sql.append(Sql.getValue(grupoDescCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(grupoDescCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(grupoDescCli.cdCliente)).append(",");
        sql.append(Sql.getValue(grupoDescCli.cdGrupoDescCli)).append(",");
        sql.append(Sql.getValue(grupoDescCli.dsGrupoDescCli)).append(",");
        sql.append(Sql.getValue(grupoDescCli.nuPrioridade)).append(",");
        sql.append(Sql.getValue(grupoDescCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(grupoDescCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(grupoDescCli.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoDescCli grupoDescCli = (GrupoDescCli) domain;
        sql.append(" DSGRUPODESCCLI = ").append(Sql.getValue(grupoDescCli.dsGrupoDescCli)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(grupoDescCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(grupoDescCli.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(grupoDescCli.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        GrupoDescCli grupoDescCli = (GrupoDescCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", grupoDescCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", grupoDescCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", grupoDescCli.cdCliente);
		sqlWhereClause.addAndCondition("CDGRUPODESCCLI = ", grupoDescCli.cdGrupoDescCli);
		sql.append(sqlWhereClause.getSql());
    }
    
}