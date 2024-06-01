package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import totalcross.sql.ResultSet;

public class PedidoConsignacaoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PedidoConsignacao();
	}

    private static PedidoConsignacaoDao instance;

    public PedidoConsignacaoDao() {
        super(PedidoConsignacao.TABLE_NAME);
    }
    
    public static PedidoConsignacaoDao getInstance() {
        if (instance == null) {
            instance = new PedidoConsignacaoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PedidoConsignacao pedidoConsignacao = new PedidoConsignacao();
        pedidoConsignacao.rowKey = rs.getString("rowkey");
        pedidoConsignacao.cdEmpresa = rs.getString("cdEmpresa");
        pedidoConsignacao.cdRepresentante = rs.getString("cdRepresentante");
        pedidoConsignacao.flOrigemPedido = rs.getString("flOrigemPedido");
        pedidoConsignacao.nuPedido = rs.getString("nuPedido");
        pedidoConsignacao.cdProduto = rs.getString("cdProduto");
        pedidoConsignacao.flTipoItemPedido = rs.getString("flTipoItemPedido");
        pedidoConsignacao.nuSeqProduto = rs.getInt("nuSeqProduto");
        pedidoConsignacao.cdCliente = rs.getString("cdCliente");
        pedidoConsignacao.nuSeqConsignacao = rs.getInt("nuSeqConsignacao");
        pedidoConsignacao.dtEmissao = rs.getDate("dtEmissao");
        pedidoConsignacao.hrEmissao = rs.getString("hrEmissao");
        pedidoConsignacao.nuSeqItemPedido = rs.getInt("nuSeqItemPedido");
        pedidoConsignacao.cdTabelaPreco = rs.getString("cdTabelaPreco");
        pedidoConsignacao.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        pedidoConsignacao.cdTipoPedido = rs.getString("cdTipoPedido");
        pedidoConsignacao.cdTipoPagamento = rs.getString("cdTipoPagamento");
        pedidoConsignacao.cdCondicaoComercial = rs.getString("cdCondicaocomercial");
        pedidoConsignacao.cdUnidade = rs.getString("cdUnidade");
        pedidoConsignacao.qtItemFisico = ValueUtil.round(rs.getDouble("qtItemFisico"));
        pedidoConsignacao.vlItemPedido = ValueUtil.round(rs.getDouble("vlItemPedido"));
        pedidoConsignacao.vlTotalItemPedido = ValueUtil.round(rs.getDouble("vlTotalItemPedido"));
        pedidoConsignacao.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        pedidoConsignacao.vlPctAcrescimo = ValueUtil.round(rs.getDouble("vlPctAcrescimo"));
        pedidoConsignacao.flTipoRegistro = rs.getString("flTipoRegistro");
        pedidoConsignacao.cdLoteProduto = rs.getString("cdLoteProduto");
        pedidoConsignacao.nuCarimbo = rs.getInt("nuCarimbo");
        pedidoConsignacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pedidoConsignacao.cdUsuario = rs.getString("cdUsuario");
        return pedidoConsignacao;
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
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUSEQCONSIGNACAO,");
        sql.append(" DTEMISSAO,");
        sql.append(" HREMISSAO,");
        sql.append(" NUSEQITEMPEDIDO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" CDUNIDADE,");
        sql.append(" QTITEMFISICO,");
        sql.append(" VLITEMPEDIDO,");
        sql.append(" VLTOTALITEMPEDIDO,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" VLPCTACRESCIMO,");
        sql.append(" FLTIPOREGISTRO,");
        sql.append(" CDLOTEPRODUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
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
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUSEQCONSIGNACAO,");
        sql.append(" DTEMISSAO,");
        sql.append(" HREMISSAO,");
        sql.append(" NUSEQITEMPEDIDO,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDTIPOPEDIDO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" CDUNIDADE,");
        sql.append(" QTITEMFISICO,");
        sql.append(" VLITEMPEDIDO,");
        sql.append(" VLTOTALITEMPEDIDO,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" VLPCTACRESCIMO,");
        sql.append(" FLTIPOREGISTRO,");
        sql.append(" CDLOTEPRODUTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) domain;
        sql.append(Sql.getValue(pedidoConsignacao.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.nuPedido)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdProduto)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdCliente)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.nuSeqConsignacao)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.dtEmissao)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.hrEmissao)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.nuSeqItemPedido)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdTipoPedido)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdTipoPagamento)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdCondicaoComercial)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdUnidade)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.qtItemFisico)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.vlItemPedido)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.vlTotalItemPedido)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.vlPctDesconto)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.vlPctAcrescimo)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.flTipoRegistro)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdLoteProduto)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pedidoConsignacao.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) domain;
        sql.append(" DTEMISSAO = ").append(Sql.getValue(pedidoConsignacao.dtEmissao)).append(",");
        sql.append(" HREMISSAO = ").append(Sql.getValue(pedidoConsignacao.hrEmissao)).append(",");
        sql.append(" NUSEQITEMPEDIDO = ").append(Sql.getValue(pedidoConsignacao.nuSeqItemPedido)).append(",");
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(pedidoConsignacao.cdTabelaPreco)).append(",");
        sql.append(" CDCONDICAOPAGAMENTO = ").append(Sql.getValue(pedidoConsignacao.cdCondicaoPagamento)).append(",");
        sql.append(" CDTIPOPEDIDO = ").append(Sql.getValue(pedidoConsignacao.cdTipoPedido)).append(",");
        sql.append(" CDTIPOPAGAMENTO = ").append(Sql.getValue(pedidoConsignacao.cdTipoPagamento)).append(",");
        sql.append(" CDCONDICAOCOMERCIAL = ").append(Sql.getValue(pedidoConsignacao.cdCondicaoComercial)).append(",");
        sql.append(" CDUNIDADE = ").append(Sql.getValue(pedidoConsignacao.cdUnidade)).append(",");
        sql.append(" QTITEMFISICO = ").append(Sql.getValue(pedidoConsignacao.qtItemFisico)).append(",");
        sql.append(" VLITEMPEDIDO = ").append(Sql.getValue(pedidoConsignacao.vlItemPedido)).append(",");
        sql.append(" VLTOTALITEMPEDIDO = ").append(Sql.getValue(pedidoConsignacao.vlTotalItemPedido)).append(",");
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(pedidoConsignacao.vlPctDesconto)).append(",");
        sql.append(" VLPCTACRESCIMO = ").append(Sql.getValue(pedidoConsignacao.vlPctAcrescimo)).append(",");
        sql.append(" FLTIPOREGISTRO = ").append(Sql.getValue(pedidoConsignacao.flTipoRegistro)).append(",");
        sql.append(" CDLOTEPRODUTO = ").append(Sql.getValue(pedidoConsignacao.cdLoteProduto)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(pedidoConsignacao.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pedidoConsignacao.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(pedidoConsignacao.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pedidoConsignacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pedidoConsignacao.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pedidoConsignacao.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pedidoConsignacao.nuPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", pedidoConsignacao.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", pedidoConsignacao.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", pedidoConsignacao.nuSeqProduto);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", pedidoConsignacao.cdCliente);
		sqlWhereClause.addAndCondition("NUSEQCONSIGNACAO = ", pedidoConsignacao.nuSeqConsignacao);
		sqlWhereClause.addAndCondition("FLTIPOREGISTRO = ", pedidoConsignacao.flTipoRegistro);
		sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", pedidoConsignacao.flTipoAlteracao);
		sqlWhereClause.addAndCondition("DTEMISSAO <= ", pedidoConsignacao.dtEmissaoFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addWhereMaxKey(BaseDomain domain, StringBuffer sql) {
    	PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) domain;
    	sql.append(" where CDEMPRESA = ").append(Sql.getValue(pedidoConsignacao.cdEmpresa));
        sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pedidoConsignacao.cdRepresentante));
        sql.append(" and FLORIGEMPEDIDO = ").append(Sql.getValue(pedidoConsignacao.flOrigemPedido));
        sql.append(" and NUPEDIDO = ").append(Sql.getValue(pedidoConsignacao.nuPedido));
        sql.append(" and CDPRODUTO = ").append(Sql.getValue(pedidoConsignacao.cdProduto));
        sql.append(" and FLTIPOITEMPEDIDO = ").append(Sql.getValue(pedidoConsignacao.flTipoItemPedido));
        sql.append(" and NUSEQPRODUTO = ").append(Sql.getValue(pedidoConsignacao.nuSeqProduto));
        sql.append(" and CDCLIENTE = ").append(Sql.getValue(pedidoConsignacao.cdCliente));
    }
    
}