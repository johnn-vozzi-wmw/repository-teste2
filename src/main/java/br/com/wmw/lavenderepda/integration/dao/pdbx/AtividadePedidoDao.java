package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AtividadePedido;
import totalcross.sql.ResultSet;

public class AtividadePedidoDao extends LavendereCrudPersonDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AtividadePedido();
	}

    private static AtividadePedidoDao instance;

    public AtividadePedidoDao() {
        super(AtividadePedido.TABLE_NAME);
    }
    
    public static AtividadePedidoDao getInstance() {
        if (instance == null) {
            instance = new AtividadePedidoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AtividadePedido atividadePedido = new AtividadePedido();
        atividadePedido.rowKey = rs.getString("rowkey");
        atividadePedido.cdEmpresa = rs.getString("cdEmpresa");
        atividadePedido.cdRepresentante = rs.getString("cdRepresentante");
        atividadePedido.flOrigemPedido = rs.getString("flOrigemPedido");
        atividadePedido.nuPedido = rs.getString("nuPedido");
        atividadePedido.nuSequencia = rs.getInt("nuSequencia");
        atividadePedido.cdStatusAtividade = rs.getString("cdStatusAtividade");
        atividadePedido.cdUsuarioCriacao = rs.getString("cdUsuarioCriacao");
        atividadePedido.nmUsuarioCriacao = rs.getString("nmUsuarioCriacao");
        atividadePedido.nuCarimbo = rs.getInt("nuCarimbo");
        atividadePedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        atividadePedido.cdUsuario = rs.getString("cdUsuario");
        atividadePedido.dtAlteracao = rs.getDate("dtAlteracao");
        atividadePedido.hrAlteracao = rs.getString("hrAlteracao");
        return atividadePedido;
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" CDSTATUSATIVIDADE,");
        sql.append(" CDUSUARIOCRIACAO,");
        sql.append(" NMUSUARIOCRIACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        AtividadePedido atividadePedido = (AtividadePedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", atividadePedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", atividadePedido.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", atividadePedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", atividadePedido.nuPedido);
		sqlWhereClause.addAndCondition("NUSEQUENCIA = ", atividadePedido.nuSequencia);
		sqlWhereClause.addAndCondition("DTALTERACAO <= ", atividadePedido.dtAlteracao);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected void addInsertColumns(StringBuffer sql) {}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {}
    
}