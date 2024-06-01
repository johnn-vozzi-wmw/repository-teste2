package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FotoPedidoErp;
import totalcross.sql.ResultSet;

public class FotoPedidoErpDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoPedidoErp();
	}

    private static FotoPedidoErpDbxDao instance = null;

    public FotoPedidoErpDbxDao() {
        super(FotoPedidoErp.TABLE_NAME);
    }
    
    public static FotoPedidoErpDbxDao getInstance() {
        if (instance == null) {
            instance = new FotoPedidoErpDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        FotoPedidoErp fotoPedidoErp = new FotoPedidoErp();
        fotoPedidoErp.rowKey = rs.getString("rowkey");
        fotoPedidoErp.cdEmpresa = rs.getString("cdEmpresa");
        fotoPedidoErp.cdRepresentante = rs.getString("cdRepresentante");
        fotoPedidoErp.nuPedido = rs.getString("nuPedido");
        fotoPedidoErp.flOrigemPedido = rs.getString("flOrigemPedido");
        fotoPedidoErp.nmFoto = rs.getString("nmFoto");
        fotoPedidoErp.cdUsuario = rs.getString("cdUsuario");
        fotoPedidoErp.flEnviadoServidor = rs.getString("flEnviadoServidor");
        fotoPedidoErp.nuTamanho = rs.getInt("nuTamanho");
        fotoPedidoErp.dtModificacao = rs.getDate("dtModificacao");
        fotoPedidoErp.flTipoAlteracao = rs.getString("flTipoAlteracao");
        fotoPedidoErp.nuCarimbo = rs.getInt("nuCarimbo");
        return fotoPedidoErp;
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
        FotoPedidoErp fotoPedidoErp = (FotoPedidoErp) domain;
        sql.append(Sql.getValue(fotoPedidoErp.cdEmpresa)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.cdRepresentante)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.nuPedido)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.nmFoto)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.cdUsuario)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.flEnviadoServidor)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.nuTamanho)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.dtModificacao)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(fotoPedidoErp.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FotoPedidoErp fotoPedidoErp = (FotoPedidoErp) domain;
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fotoPedidoErp.cdUsuario)).append(",");
        sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(fotoPedidoErp.flEnviadoServidor)).append(",");
        sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoPedidoErp.nuTamanho)).append(",");
        sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoPedidoErp.dtModificacao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoPedidoErp.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(fotoPedidoErp.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FotoPedidoErp fotoPedidoErp = (FotoPedidoErp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoPedidoErp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoPedidoErp.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", fotoPedidoErp.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", fotoPedidoErp.flOrigemPedido);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoPedidoErp.nmFoto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}