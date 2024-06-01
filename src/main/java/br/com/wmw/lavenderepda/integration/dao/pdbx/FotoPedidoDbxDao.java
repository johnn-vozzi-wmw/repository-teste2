package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FotoPedido;
import totalcross.sql.ResultSet;

public class FotoPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoPedido();
	}

    private static FotoPedidoDbxDao instance = null;

    public FotoPedidoDbxDao() {
        super(FotoPedido.TABLE_NAME);
    }
    
    public static FotoPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new FotoPedidoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        FotoPedido fotoPedido = new FotoPedido();
        fotoPedido.rowKey = rs.getString("rowkey");
        fotoPedido.cdEmpresa = rs.getString("cdEmpresa");
        fotoPedido.cdRepresentante = rs.getString("cdRepresentante");
        fotoPedido.nuPedido = rs.getString("nuPedido");
        fotoPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        fotoPedido.nmFoto = rs.getString("nmFoto");
        fotoPedido.cdUsuario = rs.getString("cdUsuario");
        fotoPedido.flEnviadoServidor = rs.getString("flEnviadoServidor");
        fotoPedido.nuTamanho = rs.getInt("nuTamanho");
        fotoPedido.dtModificacao = rs.getDate("dtModificacao");
        fotoPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        fotoPedido.nuCarimbo = rs.getInt("nuCarimbo");
        return fotoPedido;
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
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NMFOTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NMFOTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FotoPedido fotoPedido = (FotoPedido) domain;
        sql.append(Sql.getValue(fotoPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(fotoPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(fotoPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(fotoPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(fotoPedido.nmFoto)).append(",");
        sql.append(Sql.getValue(fotoPedido.cdUsuario)).append(",");
        sql.append(Sql.getValue(fotoPedido.flEnviadoServidor)).append(",");
        sql.append(Sql.getValue(fotoPedido.nuTamanho)).append(",");
        sql.append(Sql.getValue(fotoPedido.dtModificacao)).append(",");
        sql.append(Sql.getValue(fotoPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(fotoPedido.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FotoPedido fotoPedido = (FotoPedido) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoPedido.cdUsuario)).append(",");
        sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(fotoPedido.flEnviadoServidor)).append(",");
        sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoPedido.nuTamanho)).append(",");
        sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoPedido.dtModificacao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoPedido.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(fotoPedido.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FotoPedido fotoPedido = (FotoPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoPedido.cdRepresentante);
		if (fotoPedido.forceFilter) {
			sqlWhereClause.addAndConditionForced("NUPEDIDO = ", fotoPedido.nuPedido);
			sqlWhereClause.addAndConditionForced("FLORIGEMPEDIDO = ", fotoPedido.flOrigemPedido);
		} else {
			sqlWhereClause.addAndCondition("NUPEDIDO = ", fotoPedido.nuPedido);
			sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", fotoPedido.flOrigemPedido);
		}
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoPedido.nmFoto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
}