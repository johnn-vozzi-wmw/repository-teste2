package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AcessoContato;
import totalcross.sql.ResultSet;

public class AcessoContatoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AcessoContato();
	}

    private static AcessoContatoDbxDao instance = null;

    public AcessoContatoDbxDao() {
        super(AcessoContato.TABLE_NAME); 
    }
    
    public static AcessoContatoDbxDao getInstance() {
        if (instance == null) {
            instance = new AcessoContatoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AcessoContato acessoContato = new AcessoContato();
        acessoContato.rowKey = rs.getString("rowkey");
        acessoContato.cdAcessoContato = rs.getString("cdAcessoContato");
        acessoContato.dsEmpresa = rs.getString("dsEmpresa");
        acessoContato.cdUsuario = rs.getString("cdUsuario");
        acessoContato.dsNome = rs.getString("dsNome");
        acessoContato.dsEmail = rs.getString("dsEmail");
        acessoContato.nuFuncionarios = rs.getString("nuFuncionarios");
        acessoContato.dtCadastro = rs.getDate("dtCadastro");
        acessoContato.hrCadastro = rs.getString("hrCadastro");
        acessoContato.flTipoAlteracao = rs.getString("flTipoALteracao");
        acessoContato.nuCarimbo = rs.getInt("nuCarimbo");
        return acessoContato;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDACESSOCONTATO,");
        sql.append(" DSEMPRESA,");
        sql.append(" CDUSUARIO,");
        sql.append(" DSNOME,");
        sql.append(" DSEMAIL,");
        sql.append(" NUFUNCIONARIOS,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDACESSOCONTATO,");
    	sql.append(" DSEMPRESA,");
        sql.append(" CDUSUARIO,");
        sql.append(" DSNOME,");
        sql.append(" DSEMAIL,");
        sql.append(" NUFUNCIONARIOS,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        AcessoContato acessoContato = (AcessoContato) domain;
        sql.append(Sql.getValue(acessoContato.cdAcessoContato)).append(",");
        sql.append(Sql.getValue(acessoContato.dsEmpresa)).append(",");
        sql.append(Sql.getValue(acessoContato.cdUsuario)).append(",");
        sql.append(Sql.getValue(acessoContato.dsNome)).append(",");
        sql.append(Sql.getValue(acessoContato.dsEmail)).append(",");
        sql.append(Sql.getValue(acessoContato.nuFuncionarios)).append(",");
        sql.append(Sql.getValue(acessoContato.dtCadastro)).append(",");
        sql.append(Sql.getValue(acessoContato.hrCadastro)).append(",");
        sql.append(Sql.getValue(acessoContato.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(acessoContato.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        AcessoContato acessoContato = (AcessoContato) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(acessoContato.cdUsuario)).append(",");
        sql.append(" DSNOME = ").append(Sql.getValue(acessoContato.dsNome)).append(",");
        sql.append(" DSEMAIL = ").append(Sql.getValue(acessoContato.dsEmail)).append(",");
        sql.append(" NUFUNCIONARIOS = ").append(Sql.getValue(acessoContato.nuFuncionarios));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        AcessoContato acessoContato = (AcessoContato) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDACESSOCONTATO = ", acessoContato.cdAcessoContato);
		sqlWhereClause.addAndCondition("DSEMPRESA = ", acessoContato.dsEmpresa);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}