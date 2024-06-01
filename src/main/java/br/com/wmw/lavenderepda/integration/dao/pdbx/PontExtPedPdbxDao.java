package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PontExtPed;
import totalcross.sql.ResultSet;

public class PontExtPedPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontExtPed();
	}

    private static PontExtPedPdbxDao instance;

    public PontExtPedPdbxDao() {
        super(PontExtPed.TABLE_NAME);
    }

    public static PontExtPedPdbxDao getInstance() {
        return instance == null ? instance = new PontExtPedPdbxDao() : instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    
    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDSEGMENTO,");
        sql.append(" CDCANAL,");
        sql.append(" DSTIPOLANCAMENTO,");
        sql.append(" VLPONTUACAOBASE,");
        sql.append(" VLPONTUACAOREALIZADO,");
        sql.append(" VLTOTALPEDIDO,");
        sql.append(" QTDIASMEDIOS,");
        sql.append(" FLPEDIDOCANCELADO,");
        sql.append(" DTEMISSAO,");
        sql.append(" HREMISSAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PontExtPed pontExtPed = (PontExtPed) domain;
        sql.append(Sql.getValue(pontExtPed.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pontExtPed.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pontExtPed.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(pontExtPed.nuPedido)).append(",");
        sql.append(Sql.getValue(pontExtPed.cdSegmento)).append(",");
        sql.append(Sql.getValue(pontExtPed.cdCanal)).append(",");
        sql.append(Sql.getValue(pontExtPed.dsTipoLancamento)).append(",");
        sql.append(Sql.getValue(pontExtPed.vlPontuacaoBase)).append(",");
        sql.append(Sql.getValue(pontExtPed.vlPontuacaoRealizado)).append(",");
        sql.append(Sql.getValue(pontExtPed.vlTotalPedido)).append(",");
        sql.append(Sql.getValue(pontExtPed.qtDiasMedios)).append(",");
        sql.append(Sql.getValue(pontExtPed.flPedidoCancelado)).append(",");
        sql.append(Sql.getValue(pontExtPed.dtEmissao)).append(",");
        sql.append(Sql.getValue(pontExtPed.hrEmissao)).append(",");
        sql.append(Sql.getValue(pontExtPed.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pontExtPed.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pontExtPed.cdUsuario));
    }
    
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		PontExtPed pontExtPed = (PontExtPed) domain;
		sql.append(" VLPONTUACAOBASE = ").append(Sql.getValue(pontExtPed.vlPontuacaoBase)).append(",");
		sql.append(" VLPONTUACAOREALIZADO = ").append(Sql.getValue(pontExtPed.vlPontuacaoRealizado)).append(",");
		sql.append(" VLTOTALPEDIDO = ").append(Sql.getValue(pontExtPed.vlTotalPedido)).append(",");
		sql.append(" QTDIASMEDIOS = ").append(Sql.getValue(pontExtPed.qtDiasMedios));
    }

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" DISTINCT tb.rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.FLORIGEMPEDIDO,");
        sql.append(" tb.NUPEDIDO,");
        sql.append(" tb.CDSEGMENTO,");
        sql.append(" tb.CDCANAL,");
        sql.append(" tb.DSTIPOLANCAMENTO,");
        sql.append(" tb.VLPONTUACAOBASE,");
        sql.append(" tb.VLPONTUACAOREALIZADO,");
        sql.append(" COALESCE(" + getSqlSomatorioColuna("VLPONTUACAOBASE") + ", tb.VLPONTUACAOBASE) AS VLREALBASE,");
        sql.append(" COALESCE(" + getSqlSomatorioColuna("VLPONTUACAOREALIZADO") + ", tb.VLPONTUACAOREALIZADO) AS VLREALREALIZADO,");
        sql.append(" tb.VLTOTALPEDIDO,");
        sql.append(" tb.QTDIASMEDIOS,");
        sql.append(" tb.FLPEDIDOCANCELADO,");
        sql.append(" tb.DTEMISSAO,");
        sql.append(" tb.HREMISSAO");
    }
    
    private String getSqlSomatorioColuna(String nmColuna) {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" (SELECT SUM(").append(nmColuna).append(") FROM TBLVPPONTEXTITEMPED ITEM WHERE ITEM.CDEMPRESA = tb.CDEMPRESA")
    	   .append(" AND ITEM.CDREPRESENTANTE = tb.CDREPRESENTANTE")
    	   .append(" AND ITEM.FLORIGEMPEDIDO = tb.FLORIGEMPEDIDO")
    	   .append(" AND ITEM.NUPEDIDO = tb.NUPEDIDO)");
    	return sql.toString();
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PontExtPed pontExtPed = new PontExtPed();
        pontExtPed.rowKey = rs.getString("rowkey");
        pontExtPed.cdEmpresa = rs.getString("cdEmpresa");
        pontExtPed.cdRepresentante = rs.getString("cdRepresentante");
        pontExtPed.flOrigemPedido = rs.getString("flOrigemPedido");
        pontExtPed.nuPedido = rs.getString("nuPedido");
        pontExtPed.cdSegmento = rs.getString("cdSegmento");
        pontExtPed.cdCanal = rs.getString("cdCanal");
        pontExtPed.dsTipoLancamento = rs.getString("dsTipoLancamento");
        pontExtPed.vlPontuacaoBase = rs.getDouble("vlPontuacaoBase");
        pontExtPed.vlPontuacaoRealizado = rs.getDouble("vlPontuacaoRealizado");
        pontExtPed.vlPontuacaoBaseReal = rs.getDouble("vlRealBase");
        pontExtPed.vlPontuacaoRealizadoReal = rs.getDouble("vlRealRealizado");
        pontExtPed.vlTotalPedido = rs.getDouble("vlTotalPedido");
        pontExtPed.qtDiasMedios = rs.getInt("qtDiasMedios");
        pontExtPed.flPedidoCancelado = rs.getString("flPedidoCancelado");
        pontExtPed.dtEmissao = rs.getDate("dtEmissao");
        pontExtPed.hrEmissao = rs.getString("hrEmissao");
        pontExtPed.hasRetornoDiferencaErp = !ValueUtil.valueEquals(pontExtPed.vlPontuacaoBase, pontExtPed.vlPontuacaoBaseReal) || !ValueUtil.valueEquals(pontExtPed.vlPontuacaoRealizado, pontExtPed.vlPontuacaoRealizadoReal);
        return pontExtPed;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        PontExtPed pontExtPedFilter = (PontExtPed) domainFilter;
        addSqlWhereByExample("tb", sql, pontExtPedFilter);
    }

	private void addSqlWhereByExample(String aliasTableLocal, StringBuffer sql, PontExtPed pontExtPedFilter) {
		aliasTableLocal = aliasTableLocal != null ? aliasTableLocal + "." : ValueUtil.VALOR_NI;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition(aliasTableLocal + "CDEMPRESA = ", pontExtPedFilter.cdEmpresa);
        sqlWhereClause.addAndCondition(aliasTableLocal + "CDREPRESENTANTE = ", pontExtPedFilter.cdRepresentante);
        sqlWhereClause.addAndCondition(aliasTableLocal + "FLORIGEMPEDIDO = ", pontExtPedFilter.flOrigemPedido);
        sqlWhereClause.addAndCondition(aliasTableLocal + "NUPEDIDO = ", pontExtPedFilter.nuPedido);
        sqlWhereClause.addAndCondition(aliasTableLocal + "CDSEGMENTO = ", pontExtPedFilter.cdSegmento);
        sqlWhereClause.addAndCondition(aliasTableLocal + "CDCANAL = ", pontExtPedFilter.cdCanal);
        sqlWhereClause.addAndCondition(aliasTableLocal + "DTEMISSAO >= ", pontExtPedFilter.dtEmissaoInicialFilter);
        sqlWhereClause.addAndCondition(aliasTableLocal + "DTEMISSAO <= ", pontExtPedFilter.dtEmissaoFinalFilter);
        sql.append(sqlWhereClause.getSql());
        if (pontExtPedFilter.cliente != null) {
        	sql.append(" AND ((PEDIDO.CDCLIENTE LIKE ").append(Sql.getValue("%" + pontExtPedFilter.cliente.cdCliente + "%"));
        	sql.append(" OR PEDIDOERP.CDCLIENTE LIKE ").append(Sql.getValue("%" + pontExtPedFilter.cliente.cdCliente + "%")).append(")");
        	sql.append(" OR CLIENTE.NMFANTASIA LIKE ").append(Sql.getValue("%" + pontExtPedFilter.cliente.nmFantasia + "%")).append(" OR CLIENTE.NMRAZAOSOCIAL LIKE ").append(Sql.getValue("%" + pontExtPedFilter.cliente.nmRazaoSocial + "%")).append(")");
        }
	}
    
    @Override
    protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
    	PontExtPed pontExtPedFilter = (PontExtPed) domain;
    	addSqlWhereByExample(null, sql, pontExtPedFilter);
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	if (domainFilter == null) return;
    	PontExtPed pontExtPedFilter = (PontExtPed) domainFilter;
    	if (pontExtPedFilter.cliente != null) {
    		sql.append(" LEFT JOIN TBLVPPEDIDO PEDIDO");
    		sql.append(" ON PEDIDO.CDEMPRESA = ").append("TB.CDEMPRESA");
    		sql.append(" AND PEDIDO.CDREPRESENTANTE = ").append("TB.CDREPRESENTANTE");
    		sql.append(" AND PEDIDO.FLORIGEMPEDIDO = ").append("TB.FLORIGEMPEDIDO");
    		sql.append(" AND PEDIDO.NUPEDIDO = ").append("TB.NUPEDIDO");
    		
    		sql.append(" LEFT JOIN TBLVPPEDIDOERP PEDIDOERP");
    		sql.append(" ON PEDIDOERP.CDEMPRESA = ").append("TB.CDEMPRESA");
    		sql.append(" AND PEDIDOERP.CDREPRESENTANTE = ").append("TB.CDREPRESENTANTE");
    		sql.append(" AND PEDIDOERP.FLORIGEMPEDIDORELACIONADO = ").append("TB.FLORIGEMPEDIDO");
    		sql.append(" AND PEDIDOERP.NUPEDIDORELACIONADO = ").append("TB.NUPEDIDO");
    		
    		sql.append(" LEFT JOIN TBLVPCLIENTE CLIENTE");
    		sql.append(" ON CLIENTE.CDEMPRESA = ").append("TB.CDEMPRESA");
    		sql.append(" AND CLIENTE.CDREPRESENTANTE = ").append("TB.CDREPRESENTANTE");
    		sql.append(" AND ((PEDIDO.CDCLIENTE IS NOT NULL AND CLIENTE.CDCLIENTE = PEDIDO.CDCLIENTE) OR (PEDIDOERP.CDCLIENTE IS NOT NULL AND CLIENTE.CDCLIENTE = PEDIDOERP.CDCLIENTE))");
    	}
    }
    
}