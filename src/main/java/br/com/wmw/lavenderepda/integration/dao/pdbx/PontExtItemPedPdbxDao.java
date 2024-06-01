package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.PontExtItemPed;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class PontExtItemPedPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontExtItemPed();
	}

    private static PontExtItemPedPdbxDao instance;

    public PontExtItemPedPdbxDao() {
        super(PontExtItemPed.TABLE_NAME);
    }

    public static PontExtItemPedPdbxDao getInstance() {
        return instance == null ? instance = new PontExtItemPedPdbxDao() : instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    
    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOITEMPEDIDO,");
        sql.append(" NUSEQPRODUTO,");
        sql.append(" FLTIPOLANCAMENTO,");
        sql.append(" DSTIPOLANCAMENTO,");
        sql.append(" QTITEMFISICO,");
        sql.append(" CDUNIDADE,");
        sql.append(" NUCONVERSAOUNIDADE,");
        sql.append(" QTITEMFATURAMENTO,");
        sql.append(" VLTOTALITEMPEDIDO,");
        sql.append(" VLPESOPONTUACAO,");
        sql.append(" VLFATORCORRECAOFAIXAPRECO,");
        sql.append(" VLFATORCORRECAOFAIXADIAS,");
        sql.append(" VLPCTFAIXAPRECOPONTUACAO,");
        sql.append(" VLBASEPONTUACAO,");
        sql.append(" VLPONTUACAOBASE,");
        sql.append(" VLPONTUACAOREALIZADO,");
        sql.append(" DTEMISSAO,");
        sql.append(" HREMISSAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PontExtItemPed pontExtItemPed = (PontExtItemPed) domain;
        sql.append(Sql.getValue(pontExtItemPed.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.nuPedido)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.cdProduto)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.flTipoItemPedido)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.nuSeqProduto)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.flTipoLancamento)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.dsTipoLancamento)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.qtItemFisico)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.cdUnidade)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.nuConversaoUnidade)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.qtItemFaturamento)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlTotalItemPedido)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlPesoPontuacao)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlFatorCorrecaoFaixaPreco)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlFatorCorrecaoFaixaDias)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlPctFaixaPrecoPontuacao)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlBasePontuacao)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlPontuacaoBase)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.vlPontuacaoRealizado)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.dtEmissao)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.hrEmissao)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pontExtItemPed.cdUsuario));
    }
    
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		PontExtItemPed pontExtItemPed = (PontExtItemPed) domain;
		sql.append(" DSTIPOLANCAMENTO = ").append(Sql.getValue(pontExtItemPed.dsTipoLancamento)).append(",");
		sql.append(" QTITEMFISICO = ").append(Sql.getValue(pontExtItemPed.qtItemFisico)).append(",");
		sql.append(" CDUNIDADE = ").append(Sql.getValue(pontExtItemPed.cdUnidade)).append(",");
		sql.append(" NUCONVERSAOUNIDADE = ").append(Sql.getValue(pontExtItemPed.nuConversaoUnidade)).append(",");
		sql.append(" QTITEMFATURAMENTO = ").append(Sql.getValue(pontExtItemPed.qtItemFaturamento)).append(",");
		sql.append(" VLTOTALITEMPEDIDO = ").append(Sql.getValue(pontExtItemPed.vlTotalItemPedido)).append(",");
		sql.append(" VLPESOPONTUACAO = ").append(Sql.getValue(pontExtItemPed.vlPesoPontuacao)).append(",");
		sql.append(" VLFATORCORRECAOFAIXAPRECO = ").append(Sql.getValue(pontExtItemPed.vlFatorCorrecaoFaixaPreco)).append(",");
		sql.append(" VLPCTFAIXAPRECOPONTUACAO = ").append(Sql.getValue(pontExtItemPed.vlPctFaixaPrecoPontuacao)).append(",");
		sql.append(" VLBASEPONTUACAO = ").append(Sql.getValue(pontExtItemPed.vlBasePontuacao)).append(",");
		sql.append(" VLPONTUACAOBASE = ").append(Sql.getValue(pontExtItemPed.vlPontuacaoBase)).append(",");
		sql.append(" VLPONTUACAOREALIZADO = ").append(Sql.getValue(pontExtItemPed.vlPontuacaoRealizado));
    }

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" DISTINCT tb.rowKey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.FLORIGEMPEDIDO,");
        sql.append(" tb.NUPEDIDO,");
        sql.append(" tb.CDPRODUTO,");
        sql.append(" tb.FLTIPOITEMPEDIDO,");
        sql.append(" tb.NUSEQPRODUTO,");
        sql.append(" tb.FLTIPOLANCAMENTO,");
        sql.append(" tb.DSTIPOLANCAMENTO,");
        sql.append(" tb.QTITEMFISICO,");
        sql.append(" tb.CDUNIDADE,");
        sql.append(" tb.NUCONVERSAOUNIDADE,");
        sql.append(" tb.QTITEMFATURAMENTO,");
        sql.append(" tb.VLTOTALITEMPEDIDO,");
        sql.append(" tb.VLPESOPONTUACAO,");
        sql.append(" tb.VLFATORCORRECAOFAIXAPRECO,");
        sql.append(" tb.VLFATORCORRECAOFAIXADIAS,");
        sql.append(" tb.VLPCTFAIXAPRECOPONTUACAO,");
        sql.append(" tb.VLBASEPONTUACAO,");
        sql.append(" tb.VLPONTUACAOBASE,");
        sql.append(" tb.VLPONTUACAOREALIZADO,");
        sql.append(" tb.DTEMISSAO,");
        sql.append(" tb.HREMISSAO");
        if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
        	DaoUtil.getSelectNmFotoProdutoEmp(sql);
        } else {
        	DaoUtil.getSelectNmFotoProduto(sql);
        }
        getSelectSumVlRealizadoPontuacao(sql);
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        PontExtItemPed pontExtItemPed = new PontExtItemPed();
        pontExtItemPed.rowKey = rs.getString("rowkey");
        pontExtItemPed.cdEmpresa = rs.getString("cdEmpresa");
        pontExtItemPed.cdRepresentante = rs.getString("cdRepresentante");
        pontExtItemPed.flOrigemPedido = rs.getString("flOrigemPedido");
        pontExtItemPed.nuPedido = rs.getString("nuPedido");
        pontExtItemPed.cdProduto = rs.getString("cdProduto");
        pontExtItemPed.flTipoItemPedido = rs.getString("flTipoItemPedido");
        pontExtItemPed.nuSeqProduto = rs.getInt("nuSeqProduto");
        pontExtItemPed.flTipoLancamento = rs.getString("flTipoLancamento");
        pontExtItemPed.dsTipoLancamento = rs.getString("dsTipoLancamento");
        pontExtItemPed.qtItemFisico = rs.getDouble("qtItemFisico");
        pontExtItemPed.cdUnidade = rs.getString("cdUnidade");
        pontExtItemPed.nuConversaoUnidade = rs.getDouble("nuConversaoUnidade");
        pontExtItemPed.qtItemFaturamento = rs.getDouble("qtItemFaturamento");
        pontExtItemPed.vlTotalItemPedido = rs.getDouble("vlTotalItemPedido");
        pontExtItemPed.vlPesoPontuacao = rs.getDouble("vlPesoPontuacao");
        pontExtItemPed.vlFatorCorrecaoFaixaPreco = rs.getDouble("vlFatorCorrecaoFaixaPreco");
        pontExtItemPed.vlFatorCorrecaoFaixaDias = rs.getDouble("vlFatorCorrecaoFaixaDias");
        pontExtItemPed.vlPctFaixaPrecoPontuacao = rs.getDouble("vlPctFaixaPrecoPontuacao");
        pontExtItemPed.vlBasePontuacao = rs.getDouble("vlBasePontuacao");
        pontExtItemPed.vlPontuacaoBase = rs.getDouble("vlPontuacaoBase");
        pontExtItemPed.vlPontuacaoRealizado = rs.getDouble("vlPontuacaoRealizado");
        pontExtItemPed.dtEmissao = rs.getDate("dtEmissao");
        pontExtItemPed.hrEmissao = rs.getString("hrEmissao");
        pontExtItemPed.produto = new Produto();
        pontExtItemPed.produto.cdEmpresa = pontExtItemPed.cdEmpresa;
        pontExtItemPed.produto.cdRepresentante = pontExtItemPed.cdRepresentante;
        pontExtItemPed.produto.cdProduto = pontExtItemPed.cdProduto;
        pontExtItemPed.produto.fotoProduto = new FotoProduto();
        pontExtItemPed.produto.fotoProduto.nmFoto = rs.getString("NMFOTO");
        pontExtItemPed.vlSaldoParcial = rs.getDouble("VLSALDOPARCIAL");
        return pontExtItemPed;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        PontExtItemPed pontExtItemPedFilter = (PontExtItemPed) domainFilter;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", pontExtItemPedFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pontExtItemPedFilter.cdRepresentante);
        sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pontExtItemPedFilter.flOrigemPedido);
        sqlWhereClause.addAndCondition("NUPEDIDO = ", pontExtItemPedFilter.nuPedido);
        sqlWhereClause.addAndCondition("CDPRODUTO = ", pontExtItemPedFilter.cdProduto);
        sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", pontExtItemPedFilter.flTipoItemPedido);
        sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", pontExtItemPedFilter.nuSeqProduto);
        sqlWhereClause.addAndCondition("FLTIPOLANCAMENTO = ", pontExtItemPedFilter.flTipoLancamento);
        sqlWhereClause.addAndCondition("DTEMISSAO >= ", pontExtItemPedFilter.dtEmissaoInicialFilter);
        sqlWhereClause.addAndCondition("DTEMISSAO <= ", pontExtItemPedFilter.dtEmissaoFinalFilter);
        sqlWhereClause.addAndLikeCondition("DSTIPOLANCAMENTO", pontExtItemPedFilter.dsTipoLancamento);
        sql.append(sqlWhereClause.getSql());
    }
    
	private static void getSelectSumVlRealizadoPontuacao(StringBuffer sql) {
		sql.append(", CASE WHEN TB.FLTIPOLANCAMENTO != 'N' THEN ");
		sql.append(" (SELECT SUM(ROUND(VLPONTUACAOREALIZADO, ").append(Sql.getValue(LavenderePdaConfig.nuCasasDecimaisPontuacao)).append(")) FROM TBLVPPONTEXTITEMPED PEIP WHERE PEIP.CDEMPRESA = TB.CDEMPRESA" +
				" AND PEIP.CDREPRESENTANTE = TB.CDREPRESENTANTE" +
				" AND PEIP.FLORIGEMPEDIDO = TB.FLORIGEMPEDIDO" +
				" AND PEIP.NUPEDIDO = TB.NUPEDIDO" +
				" AND PEIP.CDPRODUTO = TB.CDPRODUTO" +
				" AND PEIP.FLTIPOITEMPEDIDO = TB.FLTIPOITEMPEDIDO" +
				" AND PEIP.NUSEQPRODUTO = TB.NUSEQPRODUTO ");
		sql.append(" GROUP BY ");
		sql.append(" PEIP.CDEMPRESA,");
		sql.append(" PEIP.CDREPRESENTANTE,");
		sql.append(" PEIP.FLORIGEMPEDIDO,");
		sql.append(" PEIP.NUPEDIDO,");
		sql.append(" PEIP.CDPRODUTO,");
		sql.append(" PEIP.FLTIPOITEMPEDIDO,");
		sql.append(" PEIP.NUSEQPRODUTO )");
		sql.append(" ELSE TB.VLPONTUACAOREALIZADO END ");
		sql.append(" AS VLSALDOPARCIAL");
	}
    
}