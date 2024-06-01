package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ErroEnvio;
import totalcross.sql.ResultSet;

public class ErroEnvioDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ErroEnvio();
	}

    private static ErroEnvioDbxDao instance;

    public ErroEnvioDbxDao() {
        super(ErroEnvio.TABLE_NAME); 
    }
    
    public static ErroEnvioDbxDao getInstance() {
        if (instance == null) {
            instance = new ErroEnvioDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        ErroEnvio erroEnvio = new ErroEnvio();
        erroEnvio.rowKey = rs.getString("rowkey");
        erroEnvio.cdEmpresa = rs.getString("cdEmpresa");
        erroEnvio.cdRepresentante = rs.getString("cdRepresentante");
        erroEnvio.nmEntidade = rs.getString("nmEntidade");
        erroEnvio.dsChave = rs.getString("dsChave");
        erroEnvio.dsErro = rs.getString("dsErro");
        erroEnvio.cdUsuario = rs.getString("cdUsuario");
        erroEnvio.nuCarimbo = rs.getInt("nuCarimbo");
        erroEnvio.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return erroEnvio;
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
        sql.append(" NMENTIDADE,");
        sql.append(" DSCHAVE,");
        sql.append(" DSERRO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NMENTIDADE,");
        sql.append(" DSCHAVE,");
        sql.append(" DSERRO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ErroEnvio erroEnvio = (ErroEnvio) domain;
        sql.append(Sql.getValue(erroEnvio.cdEmpresa)).append(",");
        sql.append(Sql.getValue(erroEnvio.cdRepresentante)).append(",");
        sql.append(Sql.getValue(erroEnvio.nmEntidade)).append(",");
        sql.append(Sql.getValue(erroEnvio.dsChave)).append(",");
        sql.append(Sql.getValue(erroEnvio.dsErro)).append(",");
        sql.append(Sql.getValue(erroEnvio.cdUsuario)).append(",");
        sql.append(Sql.getValue(erroEnvio.nuCarimbo)).append(",");
        sql.append(Sql.getValue(erroEnvio.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ErroEnvio erroEnvio = (ErroEnvio) domain;
        sql.append(" DSERRO = ").append(Sql.getValue(erroEnvio.dsErro)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(erroEnvio.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(erroEnvio.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(erroEnvio.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ErroEnvio erroEnvio = (ErroEnvio) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", erroEnvio.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", erroEnvio.cdRepresentante);
		sqlWhereClause.addAndCondition("NMENTIDADE = ", erroEnvio.nmEntidade);
		sqlWhereClause.addAndCondition("DSCHAVE = ", erroEnvio.dsChave);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}