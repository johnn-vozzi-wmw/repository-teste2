package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PedidoDescErp;
import totalcross.sql.ResultSet;

public class PedidoDescErpDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PedidoDescErp();
	}

    private static PedidoDescErpDbxDao instance;

    public PedidoDescErpDbxDao() {
        super(PedidoDescErp.TABLE_NAME);
    }
    
    public static PedidoDescErpDbxDao getInstance() {
        if (instance == null) {
            instance = new PedidoDescErpDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PedidoDescErp pedidoDescErp = new PedidoDescErp();
        pedidoDescErp.rowKey = rs.getString("rowkey");
        pedidoDescErp.cdEmpresa = rs.getString("cdEmpresa");
        pedidoDescErp.cdRepresentante = rs.getString("cdRepresentante");
        pedidoDescErp.flOrigemPedido = rs.getString("flOrigemPedido");
        pedidoDescErp.nuPedido = rs.getString("nuPedido");
        pedidoDescErp.cdUsuarioLiberacao = rs.getString("cdUsuarioLiberacao");
        pedidoDescErp.nmUsuario = rs.getString("nmUsuario");
        pedidoDescErp.nuSequencia = rs.getInt("nuSequencia");
        pedidoDescErp.vlPctDescontoLiberado = ValueUtil.round(rs.getDouble("vlPctDescontoLiberado"));
        pedidoDescErp.vlDescontoLiberado = ValueUtil.round(rs.getDouble("vlDescontoLiberado"));
        pedidoDescErp.dtLiberacao = rs.getDate("dtLiberacao");
        pedidoDescErp.hrLiberacao = rs.getString("hrLiberacao");
        pedidoDescErp.nuCarimbo = rs.getInt("nuCarimbo");
        pedidoDescErp.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pedidoDescErp.cdUsuario = rs.getString("cdUsuario");
        return pedidoDescErp;
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDUSUARIOLIBERACAO,");
        sql.append(" NMUSUARIO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" VLPCTDESCONTOLIBERADO,");
        sql.append(" VLDESCONTOLIBERADO,");
        sql.append(" DTLIBERACAO,");
        sql.append(" HRLIBERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDUSUARIOLIBERACAO,");
        sql.append(" NMUSUARIO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" VLPCTDESCONTOLIBERADO,");
        sql.append(" VLDESCONTOLIBERADO,");
        sql.append(" DTLIBERACAO,");
        sql.append(" HRLIBERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoDescErp pedidoDescErp = (PedidoDescErp) domain;
        sql.append(Sql.getValue(pedidoDescErp.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.nuPedido)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.cdUsuarioLiberacao)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.nmUsuario)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.nuSequencia)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.vlPctDescontoLiberado)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.vlDescontoLiberado)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.dtLiberacao)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.hrLiberacao)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pedidoDescErp.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoDescErp pedidoDescErp = (PedidoDescErp) domain;
        sql.append(" NMUSUARIO = ").append(Sql.getValue(pedidoDescErp.nmUsuario)).append(",");
        sql.append(" NUSEQUENCIA = ").append(Sql.getValue(pedidoDescErp.nuSequencia)).append(",");
        sql.append(" VLPCTDESCONTOLIBERADO = ").append(Sql.getValue(pedidoDescErp.vlPctDescontoLiberado)).append(",");
        sql.append(" VLDESCONTOLIBERADO = ").append(Sql.getValue(pedidoDescErp.vlDescontoLiberado)).append(",");
        sql.append(" DTLIBERACAO = ").append(Sql.getValue(pedidoDescErp.dtLiberacao)).append(",");
        sql.append(" HRLIBERACAO = ").append(Sql.getValue(pedidoDescErp.hrLiberacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(pedidoDescErp.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pedidoDescErp.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(pedidoDescErp.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoDescErp pedidoDescErp = (PedidoDescErp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pedidoDescErp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pedidoDescErp.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pedidoDescErp.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pedidoDescErp.nuPedido);
		sqlWhereClause.addAndCondition("CDUSUARIOLIBERACAO = ", pedidoDescErp.cdUsuarioLiberacao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addWhereMaxKey(BaseDomain domain, StringBuffer sql) {
    	PedidoDescErp pedidoDescErp = (PedidoDescErp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pedidoDescErp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pedidoDescErp.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pedidoDescErp.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pedidoDescErp.nuPedido);
		sqlWhereClause.addAndCondition("CDUSUARIOLIBERACAO = ", pedidoDescErp.cdUsuarioLiberacao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}