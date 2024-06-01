package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PedidoDesc;
import totalcross.sql.ResultSet;

public class PedidoDescDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PedidoDesc();
	}

    private static PedidoDescDbxDao instance;

    public PedidoDescDbxDao() {
        super(PedidoDesc.TABLE_NAME); 
    }
    
    public static PedidoDescDbxDao getInstance() {
        if (instance == null) {
            instance = new PedidoDescDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PedidoDesc pedidoDesc = new PedidoDesc();
        pedidoDesc.rowKey = rs.getString("rowkey");
        pedidoDesc.cdEmpresa = rs.getString("cdEmpresa");
        pedidoDesc.cdRepresentante = rs.getString("cdRepresentante");
        pedidoDesc.flOrigemPedido = rs.getString("flOrigemPedido");
        pedidoDesc.nuPedido = rs.getString("nuPedido");
        pedidoDesc.cdUsuarioLiberacao = rs.getString("cdUsuarioLiberacao");
        pedidoDesc.nmUsuario = rs.getString("nmUsuario");
        pedidoDesc.nuSequencia = rs.getInt("nuSequencia");
        pedidoDesc.vlPctDescontoLiberado = ValueUtil.round(rs.getDouble("vlPctDescontoLiberado"));
        pedidoDesc.vlDescontoLiberado = ValueUtil.round(rs.getDouble("vlDescontoLiberado"));
        pedidoDesc.dtLiberacao = rs.getDate("dtLiberacao");
        pedidoDesc.hrLiberacao = rs.getString("hrLiberacao");
        pedidoDesc.nuCarimbo = rs.getInt("nuCarimbo");
        pedidoDesc.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pedidoDesc.cdUsuario = rs.getString("cdUsuario");
        return pedidoDesc;
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
        sql.append(" CDUSUARIO");
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
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoDesc pedidoDesc = (PedidoDesc) domain;
        sql.append(Sql.getValue(pedidoDesc.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pedidoDesc.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pedidoDesc.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(pedidoDesc.nuPedido)).append(",");
        sql.append(Sql.getValue(pedidoDesc.cdUsuarioLiberacao)).append(",");
        sql.append(Sql.getValue(pedidoDesc.nmUsuario)).append(",");
        sql.append(Sql.getValue(pedidoDesc.nuSequencia)).append(",");
        sql.append(Sql.getValue(pedidoDesc.vlPctDescontoLiberado)).append(",");
        sql.append(Sql.getValue(pedidoDesc.vlDescontoLiberado)).append(",");
        sql.append(Sql.getValue(pedidoDesc.dtLiberacao)).append(",");
        sql.append(Sql.getValue(pedidoDesc.hrLiberacao)).append(",");
        sql.append(Sql.getValue(pedidoDesc.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pedidoDesc.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pedidoDesc.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoDesc pedidoDesc = (PedidoDesc) domain;
        sql.append(" NMUSUARIO = ").append(Sql.getValue(pedidoDesc.nmUsuario)).append(",");
        sql.append(" NUSEQUENCIA = ").append(Sql.getValue(pedidoDesc.nuSequencia)).append(",");
        sql.append(" VLPCTDESCONTOLIBERADO = ").append(Sql.getValue(pedidoDesc.vlPctDescontoLiberado)).append(",");
        sql.append(" VLDESCONTOLIBERADO = ").append(Sql.getValue(pedidoDesc.vlDescontoLiberado)).append(",");
        sql.append(" DTLIBERACAO = ").append(Sql.getValue(pedidoDesc.dtLiberacao)).append(",");
        sql.append(" HRLIBERACAO = ").append(Sql.getValue(pedidoDesc.hrLiberacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(pedidoDesc.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pedidoDesc.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(pedidoDesc.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoDesc pedidoDesc = (PedidoDesc) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pedidoDesc.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pedidoDesc.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pedidoDesc.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pedidoDesc.nuPedido);
		sqlWhereClause.addAndCondition("CDUSUARIOLIBERACAO = ", pedidoDesc.cdUsuarioLiberacao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}