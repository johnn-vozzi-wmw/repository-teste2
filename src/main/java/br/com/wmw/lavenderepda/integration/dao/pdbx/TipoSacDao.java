package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoSac;
import totalcross.sql.ResultSet;

public class TipoSacDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoSac();
	}

    private static TipoSacDao instance;
	

    public TipoSacDao() {
        super(TipoSac.TABLE_NAME);
    }
    
    public static TipoSacDao getInstance() {
        if (instance == null) {
            instance = new TipoSacDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoSac tipoSac = new TipoSac();
        tipoSac.rowKey = rs.getString("rowkey");
        tipoSac.cdEmpresa = rs.getString("cdEmpresa");
        tipoSac.cdTipoSac = rs.getString("cdTipoSac");
        tipoSac.dsTipoSac = rs.getString("dsTipoSac");
        tipoSac.flRelacionarNotaFiscal = rs.getString("flRelacionarNotaFiscal");
        tipoSac.flRelacionarPedido = rs.getString("flRelacionarPedido");
        tipoSac.flRelacionarProduto = rs.getString("flRelacionarProduto");
        tipoSac.nuCarimbo = rs.getInt("nuCarimbo");
        tipoSac.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoSac.cdUsuario = rs.getString("cdUsuario");
        return tipoSac;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOSAC,");
        sql.append(" DSTIPOSAC,");
        sql.append(" FLRELACIONARNOTAFISCAL,");
        sql.append(" FLRELACIONARPEDIDO,");
        sql.append(" FLRELACIONARPRODUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOSAC,");
        sql.append(" DSTIPOSAC,");
        sql.append(" FLRELACIONARNOTAFISCAL,");
        sql.append(" FLRELACIONARPEDIDO,");
        sql.append(" FLRELACIONARPRODUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoSac tipoSac = (TipoSac) domain;
        sql.append(Sql.getValue(tipoSac.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoSac.cdTipoSac)).append(",");
        sql.append(Sql.getValue(tipoSac.dsTipoSac)).append(",");
        sql.append(Sql.getValue(tipoSac.flRelacionarNotaFiscal)).append(",");
        sql.append(Sql.getValue(tipoSac.flRelacionarPedido)).append(",");
        sql.append(Sql.getValue(tipoSac.flRelacionarProduto)).append(",");
        sql.append(Sql.getValue(tipoSac.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoSac.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoSac.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoSac tipoSac = (TipoSac) domain;
        sql.append(" DSTIPOSAC = ").append(Sql.getValue(tipoSac.dsTipoSac)).append(",");
        sql.append(" FLRELACIONARNOTAFISCAL = ").append(Sql.getValue(tipoSac.flRelacionarNotaFiscal)).append(",");
        sql.append(" FLRELACIONARPEDIDO = ").append(Sql.getValue(tipoSac.flRelacionarPedido)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoSac.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoSac.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoSac.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoSac tipoSac = (TipoSac) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoSac.cdEmpresa);
		sqlWhereClause.addAndCondition("CDTIPOSAC = ", tipoSac.cdTipoSac);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}