package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MotCancelPedido;
import totalcross.sql.ResultSet;

public class MotCancelPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotCancelPedido();
	}

    private static MotCancelPedidoDbxDao instance;
	

    public MotCancelPedidoDbxDao() {
        super(MotCancelPedido.TABLE_NAME);
    }
    
    public static MotCancelPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new MotCancelPedidoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MotCancelPedido motCancelPedido = new MotCancelPedido();
        motCancelPedido.rowKey = rs.getString("rowkey");
        motCancelPedido.cdMotivoCancelamento = rs.getString("cdMotivoCancelamento");
        motCancelPedido.dsMotivoCancelamento = rs.getString("dsMotivoCancelamento");
        motCancelPedido.flCancelamentoAuto = rs.getString("flCancelamentoAuto");
        motCancelPedido.nuCarimbo = rs.getInt("nuCarimbo");
        motCancelPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        motCancelPedido.cdUsuario = rs.getString("cdUsuario");
        return motCancelPedido;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDMOTIVOCANCELAMENTO,");
        sql.append(" DSMOTIVOCANCELAMENTO,");
        sql.append(" FLCANCELAMENTOAUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        MotCancelPedido motCancelPedido = (MotCancelPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDMOTIVOCANCELAMENTO = ", motCancelPedido.cdMotivoCancelamento);
		String flDefault = motCancelPedido.flCancelamentoAuto;
		if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flDefault)) {
			sqlWhereClause.addAndCondition("flCancelamentoAuto = ", motCancelPedido.flCancelamentoAuto);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}