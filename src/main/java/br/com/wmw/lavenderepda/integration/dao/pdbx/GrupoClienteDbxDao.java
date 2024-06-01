package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.GrupoCliente;
import totalcross.sql.ResultSet;

public class GrupoClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new GrupoCliente();
	}

    private static GrupoClienteDbxDao instance;

    public GrupoClienteDbxDao() {
        super(GrupoCliente.TABLE_NAME); 
    }
    
    public static GrupoClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new GrupoClienteDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        GrupoCliente grupoCliente = new GrupoCliente();
        grupoCliente.rowKey = rs.getString("rowkey");
        grupoCliente.cdGrupoCliente = rs.getString("cdGrupoCliente");
        grupoCliente.dsGrupoCliente = rs.getString("dsGrupoCliente");
        grupoCliente.flTipoCadastro = rs.getString("flTipoCadastro");
        grupoCliente.flRestringeCondPagto = rs.getString("flRestringeCondPagto");
        grupoCliente.vlMinPedido = ValueUtil.round(rs.getDouble("vlMinPedido"));
        grupoCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        grupoCliente.nuCarimbo = rs.getInt("nuCarimbo");
        grupoCliente.cdUsuario = rs.getString("cdUsuario");
        return grupoCliente;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDGRUPOCLIENTE,");
        sql.append(" DSGRUPOCLIENTE,");
        sql.append(" FLTIPOCADASTRO,");
        sql.append(" FLRESTRINGECONDPAGTO,");
        sql.append(" VLMINPEDIDO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDGRUPOCLIENTE,");
        sql.append(" DSGRUPOCLIENTE,");
        sql.append(" FLTIPOCADASTRO,");
        sql.append(" FLRESTRINGECONDPAGTO,");
        sql.append(" VLMINPEDIDO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        GrupoCliente grupoCliente = (GrupoCliente) domain;
        sql.append(Sql.getValue(grupoCliente.cdGrupoCliente)).append(",");
        sql.append(Sql.getValue(grupoCliente.dsGrupoCliente)).append(",");
        sql.append(Sql.getValue(grupoCliente.flTipoCadastro)).append(",");
        sql.append(Sql.getValue(grupoCliente.flRestringeCondPagto)).append(",");
        sql.append(Sql.getValue(grupoCliente.vlMinPedido)).append(",");
        sql.append(Sql.getValue(grupoCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(grupoCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(grupoCliente.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        GrupoCliente grupoCliente = (GrupoCliente) domain;
        sql.append(" DSGRUPOCLIENTE = ").append(Sql.getValue(grupoCliente.dsGrupoCliente)).append(",");
        sql.append(" FLTIPOCADASTRO = ").append(Sql.getValue(grupoCliente.flTipoCadastro)).append(",");
        sql.append(" FLRESTRINGECONDPAGTO = ").append(Sql.getValue(grupoCliente.flRestringeCondPagto)).append(",");
        sql.append(" VLMINPEDIDO = ").append(Sql.getValue(grupoCliente.vlMinPedido)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(grupoCliente.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(grupoCliente.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(grupoCliente.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        GrupoCliente grupoCliente = (GrupoCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDGRUPOCLIENTE = ", grupoCliente.cdGrupoCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}