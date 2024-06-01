package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoFreteCli;
import totalcross.sql.ResultSet;

public class TipoFreteCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoFreteCli();
	}

    private static TipoFreteCliDbxDao instance;

    public TipoFreteCliDbxDao() {
        super(TipoFreteCli.TABLE_NAME);
    }
    
    public static TipoFreteCliDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoFreteCliDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoFreteCli tipoFreteCli = new TipoFreteCli();
        tipoFreteCli.rowKey = rs.getString("rowkey");
        tipoFreteCli.cdEmpresa = rs.getString("cdEmpresa");
        tipoFreteCli.cdRepresentante = rs.getString("cdRepresentante");
        tipoFreteCli.cdCliente = rs.getString("cdCliente");
        tipoFreteCli.cdTipoFrete = rs.getString("cdTipoFrete");
        tipoFreteCli.flDefault = rs.getString("flDefault");
        tipoFreteCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoFreteCli.nuCarimbo = rs.getInt("nuCarimbo");
        tipoFreteCli.cdUsuario = rs.getString("cdUsuario");
        return tipoFreteCli;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDTIPOFRETE,");
        sql.append(" FLDEFAULT,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDTIPOFRETE,");
        sql.append(" FLDEFAULT,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoFreteCli tipoFreteCli = (TipoFreteCli) domain;
        sql.append(Sql.getValue(tipoFreteCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoFreteCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(tipoFreteCli.cdCliente)).append(",");
        sql.append(Sql.getValue(tipoFreteCli.cdTipoFrete)).append(",");
        sql.append(Sql.getValue(tipoFreteCli.flDefault)).append(",");
        sql.append(Sql.getValue(tipoFreteCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoFreteCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoFreteCli.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoFreteCli tipoFreteCli = (TipoFreteCli) domain;
        sql.append(" FLDEFAULT = ").append(Sql.getValue(tipoFreteCli.flDefault)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoFreteCli.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoFreteCli.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoFreteCli.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoFreteCli tipoFreteCli = (TipoFreteCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoFreteCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", tipoFreteCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", tipoFreteCli.cdCliente);
		sqlWhereClause.addAndCondition("CDTIPOFRETE = ", tipoFreteCli.cdTipoFrete);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}