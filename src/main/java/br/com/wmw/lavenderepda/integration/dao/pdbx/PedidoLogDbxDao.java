package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PedidoLog;
import totalcross.sql.ResultSet;

public class PedidoLogDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PedidoLog();
	}

    private static PedidoLogDbxDao instance;
	

    public PedidoLogDbxDao() {
        super(PedidoLog.TABLE_NAME);
    }
    
    public static PedidoLogDbxDao getInstance() {
        if (instance == null) {
            instance = new PedidoLogDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
    	PedidoLog pedidoLog = new PedidoLog();
        pedidoLog.rowKey = rs.getString("rowkey");
        pedidoLog.cdEmpresa = rs.getString("cdEmpresa");
        pedidoLog.cdRepresentante = rs.getString("cdRepresentante");
        pedidoLog.flOrigemPedido = rs.getString("flOrigemPedido");
        pedidoLog.nuPedido = rs.getString("nuPedido");
        pedidoLog.dtAcao = rs.getDate("dtAcao");
        pedidoLog.hrAcao = rs.getString("hrAcao");
        pedidoLog.cdMotivoCancelamento = rs.getString("cdMotivoCancelamento");
        pedidoLog.cdStatusPedido = rs.getString("cdStatusPedido");
        pedidoLog.vlTotalPedido = ValueUtil.round(rs.getDouble("vlTotalPedido"));
        pedidoLog.dtEntrega = rs.getDate("dtEntrega");
        pedidoLog.dtEntregaLiberada = rs.getDate("dtEntregaLiberada");
        pedidoLog.cdUsuarioLibEntrega = rs.getString("cdUsuarioLibEntrega");
        pedidoLog.cdTipoPedido = rs.getString("cdTipoPedido");
        pedidoLog.nuOrdemCompraCliente = rs.getString("nuOrdemCompraCliente");
        pedidoLog.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        pedidoLog.dsObservacao = rs.getString("dsObservacao");
        pedidoLog.dsObservacaoNotaFiscal = rs.getString("dsObservacaoNotaFiscal");
        pedidoLog.flEmergencial = rs.getString("flEmergencial");
        pedidoLog.cdMotivoEmergencia = rs.getString("cdMotivoEmergencia");
        pedidoLog.dsObsEmergencia = rs.getString("dsObsEmergencia");
        pedidoLog.nuPedidoRelEmergencial = rs.getString("nuPedidoRelEmergencial");
        pedidoLog.cdUsuarioAutorizacao = rs.getString("cdUsuarioAutorizacao");
        pedidoLog.flEnviaEmail = rs.getString("flEnviaEmail");
        pedidoLog.dsEmailsDestino = rs.getString("dsEmailsDestino");
        pedidoLog.cdEnderecoCliente = rs.getString("cdEnderecoCliente");
        pedidoLog.cdTipoPagamento = rs.getString("cdTipoPagamento");
        pedidoLog.flTipoRegistro = rs.getString("flTipoRegistro");
        pedidoLog.flOrigemAcao = rs.getString("flOrigemAcao");
        pedidoLog.cdUsuarioCriacao = rs.getString("cdUsuarioCriacao");
        pedidoLog.nuCarimbo = rs.getInt("nuCarimbo");
        pedidoLog.cdUsuario = rs.getString("cdUsuario");
        pedidoLog.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return pedidoLog;
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
        sql.append(" DTACAO,");
        sql.append(" HRACAO,");
        sql.append(" CDMOTIVOCANCELAMENTO,");
        sql.append(" CDSTATUSPEDIDO,");
        sql.append(" VLTOTALPEDIDO,");
        sql.append(" DTENTREGA,");
        sql.append(" DTENTREGALIBERADA,");
        sql.append(" CDUSUARIOLIBENTREGA,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" NUORDEMCOMPRACLIENTE,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DSOBSERVACAONOTAFISCAL,");
        sql.append(" FLEMERGENCIAL,");
        sql.append(" CDMOTIVOEMERGENCIA,");
        sql.append(" DSOBSEMERGENCIA,");
        sql.append(" NUPEDIDORELEMERGENCIAL,");
        sql.append(" CDUSUARIOAUTORIZACAO,");
        sql.append(" FLENVIAEMAIL,");
        sql.append(" DSEMAILSDESTINO,");
        sql.append(" CDENDERECOCLIENTE,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" FLTIPOREGISTRO,");
        sql.append(" FLORIGEMACAO,");
        sql.append(" CDUSUARIOCRIACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" DTACAO,");
        sql.append(" HRACAO,");
        sql.append(" CDMOTIVOCANCELAMENTO,");
        sql.append(" CDSTATUSPEDIDO,");
        sql.append(" VLTOTALPEDIDO,");
        sql.append(" DTENTREGA,");
        sql.append(" DTENTREGALIBERADA,");
        sql.append(" CDUSUARIOLIBENTREGA,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" NUORDEMCOMPRACLIENTE,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DSOBSERVACAONOTAFISCAL,");
        sql.append(" FLEMERGENCIAL,");
        sql.append(" CDMOTIVOEMERGENCIA,");
        sql.append(" DSOBSEMERGENCIA,");
        sql.append(" NUPEDIDORELEMERGENCIAL,");
        sql.append(" CDUSUARIOAUTORIZACAO,");
        sql.append(" FLENVIAEMAIL,");
        sql.append(" DSEMAILSDESTINO,");
        sql.append(" CDENDERECOCLIENTE,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" FLTIPOREGISTRO,");
        sql.append(" FLORIGEMACAO,");
        sql.append(" CDUSUARIOCRIACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        PedidoLog pedidoLog = (PedidoLog) domain;
        sql.append(Sql.getValue(pedidoLog.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pedidoLog.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(pedidoLog.nuPedido)).append(",");
        sql.append(Sql.getValue(pedidoLog.dtAcao)).append(",");
        sql.append(Sql.getValue(pedidoLog.hrAcao)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdMotivoCancelamento)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdStatusPedido)).append(",");
        sql.append(Sql.getValue(pedidoLog.vlTotalPedido)).append(",");
        sql.append(Sql.getValue(pedidoLog.dtEntrega)).append(",");
        sql.append(Sql.getValue(pedidoLog.dtEntregaLiberada)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdUsuarioLibEntrega)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(pedidoLog.nuOrdemCompraCliente)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdCondicaoComercial)).append(",");
        sql.append(Sql.getValue(pedidoLog.dsObservacao)).append(",");
        sql.append(Sql.getValue(pedidoLog.dsObservacaoNotaFiscal)).append(",");
        sql.append(Sql.getValue(pedidoLog.flEmergencial)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdMotivoEmergencia)).append(",");
        sql.append(Sql.getValue(pedidoLog.dsObsEmergencia)).append(",");
        sql.append(Sql.getValue(pedidoLog.nuPedidoRelEmergencial)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdUsuarioAutorizacao)).append(",");
        sql.append(Sql.getValue(pedidoLog.flEnviaEmail)).append(",");
        sql.append(Sql.getValue(pedidoLog.dsEmailsDestino)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdEnderecoCliente)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdTipoPagamento)).append(",");
        sql.append(Sql.getValue(pedidoLog.flTipoRegistro)).append(",");
        sql.append(Sql.getValue(pedidoLog.flOrigemAcao)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdUsuarioCriacao)).append(",");
        sql.append(Sql.getValue(pedidoLog.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pedidoLog.cdUsuario)).append(",");
        sql.append(Sql.getValue(pedidoLog.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        PedidoLog pedidoLog = (PedidoLog) domain;
        sql.append(" CDMOTIVOCANCELAMENTO = ").append(Sql.getValue(pedidoLog.cdMotivoCancelamento)).append(",");
        sql.append(" CDSTATUSPEDIDO = ").append(Sql.getValue(pedidoLog.cdStatusPedido)).append(",");
        sql.append(" VLTOTALPEDIDO = ").append(Sql.getValue(pedidoLog.vlTotalPedido)).append(",");
        sql.append(" DTENTREGA = ").append(Sql.getValue(pedidoLog.dtEntrega)).append(",");
        sql.append(" DTENTREGALIBERADA = ").append(Sql.getValue(pedidoLog.dtEntregaLiberada)).append(",");
        sql.append(" CDUSUARIOLIBENTREGA = ").append(Sql.getValue(pedidoLog.cdUsuarioLibEntrega)).append(",");
        sql.append(" CDTIPOPEDIDO = ").append(Sql.getValue(pedidoLog.cdTipoPedido)).append(",");
        sql.append(" NUORDEMCOMPRACLIENTE = ").append(Sql.getValue(pedidoLog.nuOrdemCompraCliente)).append(",");
        sql.append(" CDCONDICAOCOMERCIAL = ").append(Sql.getValue(pedidoLog.cdCondicaoComercial)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(pedidoLog.dsObservacao)).append(",");
        sql.append(" DSOBSERVACAONOTAFISCAL = ").append(Sql.getValue(pedidoLog.dsObservacaoNotaFiscal)).append(",");
        sql.append(" FLEMERGENCIAL = ").append(Sql.getValue(pedidoLog.flEmergencial)).append(",");
        sql.append(" CDMOTIVOEMERGENCIA = ").append(Sql.getValue(pedidoLog.cdMotivoEmergencia)).append(",");
        sql.append(" DSOBSEMERGENCIA = ").append(Sql.getValue(pedidoLog.dsObsEmergencia)).append(",");
        sql.append(" NUPEDIDORELEMERGENCIAL = ").append(Sql.getValue(pedidoLog.nuPedidoRelEmergencial)).append(",");
        sql.append(" CDUSUARIOAUTORIZACAO = ").append(Sql.getValue(pedidoLog.cdUsuarioAutorizacao)).append(",");
        sql.append(" FLENVIAEMAIL = ").append(Sql.getValue(pedidoLog.flEnviaEmail)).append(",");
        sql.append(" DSEMAILSDESTINO = ").append(Sql.getValue(pedidoLog.dsEmailsDestino)).append(",");
        sql.append(" CDENDERECOCLIENTE = ").append(Sql.getValue(pedidoLog.cdEnderecoCliente)).append(",");
        sql.append(" CDTIPOPAGAMENTO = ").append(Sql.getValue(pedidoLog.cdTipoPagamento)).append(",");
        sql.append(" FLTIPOREGISTRO = ").append(Sql.getValue(pedidoLog.flTipoRegistro)).append(",");
        sql.append(" FLORIGEMACAO = ").append(Sql.getValue(pedidoLog.flOrigemAcao)).append(",");
        sql.append(" CDUSUARIOCRIACAO = ").append(Sql.getValue(pedidoLog.cdUsuarioCriacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(pedidoLog.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(pedidoLog.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pedidoLog.flTipoAlteracao));
        
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PedidoLog pedidoLog = (PedidoLog) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pedidoLog.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pedidoLog.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pedidoLog.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pedidoLog.nuPedido);
		sqlWhereClause.addAndCondition("CDMOTIVOCANCELAMENTO = ", pedidoLog.cdMotivoCancelamento);
		sqlWhereClause.addAndCondition("FLTIPOREGISTRO = ", pedidoLog.flTipoRegistro);
		if (pedidoLog.onlyEnviadosServidor) {
			sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO = ", pedidoLog.flTipoAlteracao);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}