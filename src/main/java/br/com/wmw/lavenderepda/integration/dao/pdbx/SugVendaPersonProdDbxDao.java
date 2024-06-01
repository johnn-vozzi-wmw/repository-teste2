package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SugVendaPersonProd;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class SugVendaPersonProdDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SugVendaPersonProd();
	}

	private static SugVendaPersonProdDbxDao instance;
	
	public static SugVendaPersonProdDbxDao getInstance() {
		if (instance == null) {
			instance = new SugVendaPersonProdDbxDao();
		}
		return instance;
	}

	public SugVendaPersonProdDbxDao() {
		super(SugVendaPersonProd.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		Produto produto = new Produto();
		produto.cdEmpresa = rs.getString(1);
		produto.cdRepresentante = rs.getString(2);
		produto.cdProduto = rs.getString(3);
		produto.dsProduto = rs.getString(4);
		produto.dsPrincipioAtivo = rs.getString(5);
		produto.cdUnidade = rs.getString(6);
		produto.cdGrupoProduto1 = rs.getString(7);
		produto.cdGrupoProduto2 = rs.getString(8);
		produto.cdGrupoProduto3 = rs.getString(9);
		produto.cdGrupoProduto4 = rs.getString(10);
		produto.cdGrupoProduto5 = rs.getString(11);
		produto.cdSugVendaPerson = rs.getString(12);
		produto.qtdMediaHistorico = rs.getDouble(13);
		produto.vlMedioHistorico = rs.getDouble(14);
		produto.qtEstoqueProduto = rs.getDouble(15);
		produto.flUtilizaVerba = rs.getString(16);
		produto.cdFamiliaDescProg = rs.getString(17);
		int i = LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson ? 19 : 18;
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			produto.dsReferencia = rs.getString(i++);
		}
		if (LavenderePdaConfig.isMostraFotoProdutoListSugPerson()) {
			FotoProduto fotoProduto = new FotoProduto();
			fotoProduto.cdProduto = produto.cdProduto;
			fotoProduto.nmFoto = rs.getString(i++);
			produto.fotoProduto = fotoProduto;
		}
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			produto.nuFracao = rs.getInt(i++);
			produto.cdUnidadeFracao = rs.getString(i++);
			produto.flUsaUnidadeBaseDsFracao = rs.getString(i++);
		}
		if (LavenderePdaConfig.mostraQtdPorEmbalagemProduto) {
			produto.nuConversaoUnidadesMedida = rs.getDouble(i++);
		}
		if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado) {
			produto.nuConversaoUMCreditoAntecipado = rs.getDouble(i);
		}
		return produto;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
	}
	
	public Vector getProdutosSugestoesPerson(Pedido pedido, String cdSugVendaPerson, String cdGrupoCliente, String cdTabelaPrecoFilter, String dsOrdenacao) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT produto.CDEMPRESA, produto.CDREPRESENTANTE, produto.CDPRODUTO, produto.DSPRODUTO, produto.DSPRINCIPIOATIVO, produto.CDUNIDADE,")
		.append("produto.CDGRUPOPRODUTO1, produto.CDGRUPOPRODUTO2, produto.CDGRUPOPRODUTO3, produto.CDGRUPOPRODUTO4, produto.CDGRUPOPRODUTO5, sprod.CDSUGVENDAPERSON, giroProduto.QTDMEDIAHISTORICO, giroProduto.VLMEDIOHISTORICO, ")
		.append(" (ifnull(estoqueE.QTESTOQUE,0) - ifnull(estoqueP.QTESTOQUE,0)) QTDESTOQUE, produto.FLUTILIZAVERBA, produto.CDFAMILIADESCPROG");
		if (LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson) {
			sql.append(", MIN(tab.NUCONVERSAOUNIDADE)");
		}
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			sql.append(" ,produto.DSREFERENCIA");
		}
		if (LavenderePdaConfig.isMostraFotoProdutoListSugPerson()) {
			if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
				sql.append(", FOTO.CDEMPRESA || '/' || FOTO.NMFOTO AS NMFOTO");
			} else {
				sql.append(", FOTO.NMFOTO");
			}
		}
		if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
			sql.append(" ,produto.NUFRACAO,")
			.append(" produto.CDUNIDADEFRACAO,")
			.append(" produto.FLUSAUNIDADEBASEDSFRACAO");
		}
        if (LavenderePdaConfig.mostraQtdPorEmbalagemProduto) {
        	sql.append(", produto.NUCONVERSAOUNIDADESMEDIDA");
        }
		if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado) {
			sql.append(", produto.NUCONVERSAOUMCREDITOANTECIPADO");
		}
		sql.append(" FROM TBLVPSUGVENDAPERSONPROD sprod")
		.append(" JOIN TBLVPPRODUTO produto ON sprod.CDEMPRESA = produto.CDEMPRESA AND")
		.append(" produto.CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante)).append(" AND")
		.append(" sprod.CDPRODUTO = produto.CDPRODUTO")
		.append(" JOIN TBLVPSUGVENDAPERSON sug ON sprod.CDSUGVENDAPERSON = sug.CDSUGVENDAPERSON")
		.append(" JOIN TBLVPSUGVENDAPERSONCLI scli ON scli.CDEMPRESA = sprod.CDEMPRESA AND")
		.append(" scli.CDSUGVENDAPERSON = sprod.CDSUGVENDAPERSON");
		addJoinItemTabelaPreco(sql, pedido);
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(cdGrupoCliente)) {
			sql.append(" JOIN TBLVPGRUPOCLIPERMPROD perm ON perm.CDPRODUTO = produto.CDPRODUTO")
			.append(" AND perm.CDGRUPOCLIENTE = ").append(Sql.getValue(cdGrupoCliente));
		}
		sql.append(" LEFT JOIN TBLVPESTOQUE estoqueE ON ")
		.append(" estoqueE.CDEMPRESA = produto.CDEMPRESA AND")
		.append(" estoqueE.CDREPRESENTANTE = produto.CDREPRESENTANTE AND")
		.append(" estoqueE.CDPRODUTO = produto.CDPRODUTO AND")
		.append(" estoqueE.CDITEMGRADE1 = '0' AND")
		.append(" estoqueE.CDITEMGRADE2 = '0' AND")
		.append(" estoqueE.CDITEMGRADE3 = '0' AND")
		.append(" estoqueE.CDLOCALESTOQUE = '0' AND")
		.append(" estoqueE.FLORIGEMESTOQUE = 'E'")
		.append(" LEFT JOIN TBLVPESTOQUE estoqueP ON ")
		.append(" estoqueP.CDEMPRESA = produto.CDEMPRESA AND")
		.append(" estoqueP.CDREPRESENTANTE = produto.CDREPRESENTANTE AND")
		.append(" estoqueP.CDPRODUTO = produto.CDPRODUTO AND")
		.append(" estoqueP.CDITEMGRADE1 = '0' AND")
		.append(" estoqueP.CDITEMGRADE2 = '0' AND")
		.append(" estoqueP.CDITEMGRADE3 = '0' AND")
		.append(" estoqueP.CDLOCALESTOQUE = '0' AND")
		.append(" estoqueP.FLORIGEMESTOQUE = 'P'")
		.append(" LEFT JOIN TBLVPGIROPRODUTO giroProduto ON ")
		.append(" produto.CDEMPRESA = giroProduto.CDEMPRESA AND")
		.append(" giroProduto.CDREPRESENTANTE = ").append(Sql.getValue(pedido.getCliente().cdRepresentante)).append(" AND")
		.append(" scli.CDCLIENTE = giroProduto.CDCLIENTE AND")
		.append(" produto.CDPRODUTO = giroProduto.CDPRODUTO ");
		if (LavenderePdaConfig.isMostraFotoProdutoListSugPerson()) {
			String fotoProdutoTable = LavenderePdaConfig.usaFotoProdutoPorEmpresa ? "TBLVPFOTOPRODUTOEMP" : "TBLVPFOTOPRODUTO";
			sql.append(" LEFT JOIN ").append(fotoProdutoTable).append(" FOTO ON")
			.append(" produto.CDPRODUTO = foto.CDPRODUTO");
			if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
				sql.append(" AND produto.CDEMPRESA = foto.CDEMPRESA");
			}
		}
		if (LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson) {
			addSqlNuConversaoUnidadeComRestricao(sql, pedido.cdTipoPedido);
		}
		sql.append(" WHERE scli.CDCLIENTE = ").append(Sql.getValue(pedido.cdCliente)).append(" AND")
		.append(" produto.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa))
		.append(" AND (sug.DTVIGENCIAINICIAL <= date('now', 'localtime') OR sug.DTVIGENCIAINICIAL IS NULL) AND (sug.DTVIGENCIAFINAL >= date('now', 'localtime') OR sug.DTVIGENCIAFINAL IS NULL)");
		if (LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson) {
			sql.append(" AND tab.NUCONVERSAOUNIDADE <= QTDESTOQUE AND tab.NUCONVERSAOUNIDADE IS NOT NULL");
		}
		if (LavenderePdaConfig.ocultaProdutoSemEstoqueListaSugestaoVendaPerson) {
			sql.append(" GROUP BY produto.CDEMPRESA,")
			.append(" produto.CDREPRESENTANTE,")
			.append(" produto.CDPRODUTO,")
			.append(" produto.DSPRODUTO,")
			.append(" produto.DSPRINCIPIOATIVO,")
			.append(" produto.CDUNIDADE");
		}
		sql.append(" ORDER BY sug.NURELEVANCIA DESC, ").append(dsOrdenacao).append(" LIMIT ").append(LavenderePdaConfig.qtLimiteProdutosSug * 100);
		return findAll(pedido, sql.toString());
	}
	
	private void addSqlNuConversaoUnidadeComRestricao(StringBuffer sql, String cdTipoPedido) {
		sql.append(" LEFT JOIN (")
		.append(" SELECT NUCONVERSAOUNIDADE, CDEMPRESA, CDREPRESENTANTE, CDPRODUTO FROM (")
		.append(" SELECT NUCONVERSAOUNIDADE, tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDPRODUTO FROM TBLVPPRODUTOUNIDADE tb")
		.append(" JOIN TBLVPUNIDADE un ON")
		.append(" un.CDEMPRESA = tb.CDEMPRESA")
		.append(" AND un.CDREPRESENTANTE = tb.CDREPRESENTANTE")
		.append(" AND un.CDUNIDADE = tb.CDUNIDADE")
		.append(" LEFT JOIN TBLVPRESTRICAOVENDAUN rest ON")
		.append(" tb.CDEMPRESA = rest.CDEMPRESA")
		.append(" AND tb.CDREPRESENTANTE = rest.CDREPRESENTANTE")
		.append(" AND tb.CDPRODUTO = rest.CDPRODUTO")
		.append(" AND tb.CDUNIDADE = rest.CDUNIDADE")
		.append(" AND rest.FLBLOQUEIAVENDA = 'S'");
		if (ValueUtil.isNotEmpty(cdTipoPedido)) {
			sql.append(" AND (rest.CDTIPOPEDIDO = ").append(Sql.getValue(cdTipoPedido)).append(" OR rest.CDTIPOPEDIDO = '0')");
		}
		sql.append(" WHERE rest.CDEMPRESA IS NULL UNION ALL")
		.append(" SELECT NUCONVERSAOUNIDADESMEDIDA NUCONVERSAOUNIDADE, tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDPRODUTO FROM TBLVPPRODUTO tb")
		.append(" JOIN TBLVPUNIDADE un ON")
		.append(" un.CDEMPRESA = tb.CDEMPRESA")
		.append(" AND un.CDREPRESENTANTE = tb.CDREPRESENTANTE")
		.append(" AND un.CDUNIDADE = tb.CDUNIDADE")
		.append(" LEFT JOIN TBLVPRESTRICAOVENDAUN rest ON")
		.append(" tb.CDEMPRESA = rest.CDEMPRESA")
		.append(" AND tb.CDREPRESENTANTE = rest.CDREPRESENTANTE")
		.append(" AND tb.CDPRODUTO = rest.CDPRODUTO")
		.append(" AND tb.CDUNIDADE = rest.CDUNIDADE")
		.append(" AND rest.FLBLOQUEIAVENDA = 'S'");
		if (ValueUtil.isNotEmpty(cdTipoPedido)) {
			sql.append(" AND (rest.CDTIPOPEDIDO = ").append(Sql.getValue(cdTipoPedido)).append(" OR rest.CDTIPOPEDIDO = '0')");
		}
		sql.append(" WHERE rest.CDEMPRESA IS NULL ) tab ORDER BY NUCONVERSAOUNIDADE ASC )")
		.append(" tab ON tab.CDEMPRESA = produto.CDEMPRESA")
		.append(" AND tab.CDREPRESENTANTE = produto.CDREPRESENTANTE")
		.append(" AND tab.CDPRODUTO = produto.CDPRODUTO");
	}
	
	public void addJoinItemTabelaPreco(StringBuffer sql, Pedido pedido) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		itemTabelaPreco.qtItem = ItemTabelaPreco.QTITEM_VALOR_PADRAO;
		itemTabelaPreco.cdTabelaPreco = pedido.cdTabelaPreco;
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			itemTabelaPreco.cdPrazoPagtoPreco = pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
		}
		if (LavenderePdaConfig.usaPrecoPorUf) {
			itemTabelaPreco.cdUf = ValueUtil.isNotEmpty(pedido.getCliente().cdEstadoComercial) ? pedido.getCliente().cdEstadoComercial : ValueUtil.VALOR_ZERO;
		} else {
			itemTabelaPreco.cdUf = ValueUtil.VALOR_ZERO;
		}
		DaoUtil.addJoinItemTabelaPreco(sql, DaoUtil.ALIAS_ITEMTABELAPRECO, "produto", itemTabelaPreco, false, false);
	}
	
}
