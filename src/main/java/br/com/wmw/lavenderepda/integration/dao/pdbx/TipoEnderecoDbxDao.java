package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoEndereco;
import totalcross.sql.ResultSet;

public class TipoEnderecoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoEndereco();
	}

    private static TipoEnderecoDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPTIPOENDERECO";

    public TipoEnderecoDbxDao() {
        super(TABLE_NAME);
    }
    
    public static TipoEnderecoDbxDao getInstance() {
        if (instance == null) {
            instance = new TipoEnderecoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        TipoEndereco tipoEndereco = new TipoEndereco();
        tipoEndereco.rowKey = rs.getString("rowkey");
        tipoEndereco.cdEmpresa = rs.getString("cdEmpresa");
        tipoEndereco.cdTipoEndereco = rs.getString("cdTipoEndereco");
        tipoEndereco.nuMinDiasEntregaPedido = rs.getInt("nuMinDiasEntregaPedido");
        tipoEndereco.hrLimiteEnvioPedido = rs.getString("hrLimiteEnvioPedido");
        tipoEndereco.dsTipoEndereco = rs.getString("dsTipoEndereco");
        tipoEndereco.nuCarimbo = rs.getInt("nuCarimbo");
        tipoEndereco.cdUsuario = rs.getString("cdUsuario");
        tipoEndereco.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoEndereco.dtAlteracao = rs.getDate("dtAlteracao");
        tipoEndereco.hrAlteracao = rs.getString("hrAlteracao");
        return tipoEndereco;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOENDERECO,");
        sql.append(" DSTIPOENDERECO,");
        sql.append(" NUMINDIASENTREGAPEDIDO,");
        sql.append(" HRLIMITEENVIOPEDIDO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOENDERECO,");
        sql.append(" DSTIPOENDERECO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        TipoEndereco tipoEndereco = (TipoEndereco) domain;
        sql.append(Sql.getValue(tipoEndereco.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoEndereco.cdTipoEndereco)).append(",");
        sql.append(Sql.getValue(tipoEndereco.dsTipoEndereco)).append(",");
        sql.append(Sql.getValue(tipoEndereco.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoEndereco.cdUsuario)).append(",");
        sql.append(Sql.getValue(tipoEndereco.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoEndereco.dtAlteracao)).append(",");
        sql.append(Sql.getValue(tipoEndereco.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        TipoEndereco tipoEndereco = (TipoEndereco) domain;
        sql.append(" CDEMPRESA = ").append(Sql.getValue(tipoEndereco.cdEmpresa)).append(",");
        sql.append(" DSTIPOENDERECO = ").append(Sql.getValue(tipoEndereco.dsTipoEndereco)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoEndereco.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoEndereco.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoEndereco.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(tipoEndereco.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(tipoEndereco.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        TipoEndereco tipoEndereco = (TipoEndereco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDTIPOENDERECO = ", tipoEndereco.cdTipoEndereco);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}