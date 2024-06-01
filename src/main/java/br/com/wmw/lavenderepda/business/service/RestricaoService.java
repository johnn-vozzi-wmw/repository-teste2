package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import br.com.wmw.lavenderepda.business.domain.Restricao;
import br.com.wmw.lavenderepda.business.domain.RestricaoCliente;
import br.com.wmw.lavenderepda.business.domain.RestricaoProduto;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.validation.RestricaoValidationException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RestricaoDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RestricaoService extends CrudService {

	private static RestricaoService instance;
	@Override public void validate(BaseDomain baseDomain) { }
	@Override protected CrudDao getCrudDao() { return RestricaoDbxDao.getInstance(); }

	public static RestricaoService getInstance() { return instance == null ? instance = new RestricaoService() : instance; }

	public RestricaoProduto isProdutoRestrito(final String cdProduto, final String cdCliente, final String nuPedido, final double qtdItemInserindo) throws SQLException {
		if (!LavenderePdaConfig.usaRestricaoVendaClienteProduto || ValueUtil.isEmpty(cdProduto)) return null;
		return RestricaoDbxDao.getInstance().findRestricaoProdutoByProdutoCliente(getRestricaoFilter(cdProduto, cdCliente, nuPedido, qtdItemInserindo, null, false, false, false));
	}

	public String getSqlRestricao(final String cdProduto, final String cdCliente, final String nuPedido, final double qtdItemInserindo, final String qtdDynamicFilter, final boolean dynamicProduto, final boolean dynamicCliente, final boolean dynamicQuantidade) {
		return RestricaoDbxDao.getInstance().getSqlRestricaoForJoin(getRestricaoFilter(cdProduto, cdCliente, nuPedido, qtdItemInserindo, qtdDynamicFilter, dynamicProduto, dynamicCliente, dynamicQuantidade));
	}

	public Restricao getRestricaoFilter(String cdProduto,String cdCliente, String nuPedido, double qtdItemInserindo, String qtdDynamicFilter, boolean dynamicProduto, boolean dynamicCliente, boolean dynamicQuantidade) {
		Restricao restricao = new Restricao();
		restricao.dynamicProduto = dynamicProduto;
		restricao.dynamicCliente = dynamicCliente;
		restricao.dynamicQuantidade = dynamicQuantidade;
		restricao.qtdDynamicFilter = qtdDynamicFilter;

		restricao.cdEmpresa = SessionLavenderePda.cdEmpresa;
		restricao.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		restricao.dtInicialVigenciaFilterDiario = DateUtil.getCurrentDate();
		restricao.dtFimVigenciaFilterDiario = restricao.dtInicialVigenciaFilterDiario;
		restricao.dtInicialVigenciaFilterMensal = DateUtil.getFirstDayOfMonth();
		restricao.dtFimVigenciaFilterMensal = DateUtil.getLastDayOfMonth();
		setRestricaoSemanalDaysFilter(restricao);
		restricao.dtInicialVigenciaFilterPersonalizado = DateUtil.getCurrentDate();
		restricao.dtFimVigenciaFilterPersonalizado = restricao.dtInicialVigenciaFilterPersonalizado;

		if (cdProduto != null) {
			restricao.restricaoProdutoFilter = new RestricaoProduto();
			restricao.restricaoProdutoFilter.cdEmpresa = restricao.cdEmpresa;
			restricao.restricaoProdutoFilter.cdRepresentante = restricao.cdRepresentante;
			restricao.restricaoProdutoFilter.cdProduto = cdProduto;
			restricao.restricaoProdutoFilter.qtItemComprando = qtdItemInserindo;
		}
		if (cdCliente != null) {
			restricao.restricaoClienteFilter = new RestricaoCliente();
			restricao.restricaoClienteFilter.cdCliente = cdCliente;
			restricao.restricaoClienteFilter.cdEmpresa = restricao.cdEmpresa;
			restricao.restricaoClienteFilter.cdRepresentante = restricao.cdRepresentante;

			restricao.itemPedidoFilter = new ItemPedido();
			restricao.itemPedidoFilter.cdEmpresa = restricao.cdEmpresa;
			restricao.itemPedidoFilter.cdRepresentante = restricao.cdRepresentante;
			restricao.itemPedidoFilter.nuPedido = nuPedido;
			restricao.itemPedidoFilter.cdProduto = cdProduto;
			restricao.itemPedidoFilter.cdCliente = cdCliente;
			restricao.itemPedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
			restricao.itemPedidoFilter.cdStatusPedidoFilter = LavenderePdaConfig.cdStatusPedidoAberto;
			restricao.itemPedidoFilter.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		}
		return restricao;
	}
	
	private void setRestricaoSemanalDaysFilter(Restricao restricao) {
		if (LavenderePdaConfig.nuDiaSemanaVigenciaSemanal > 0 && LavenderePdaConfig.nuDiaSemanaVigenciaSemanal < 8) {
			Date dtFinal = DateUtil.getFirstDayOfWeek();
			dtFinal.advance(LavenderePdaConfig.nuDiaSemanaVigenciaSemanal - 1);
			if (dtFinal.isBefore(DateUtil.getCurrentDate())) {
				dtFinal.advance(7);
			}
			restricao.dtFimVigenciaFilterSemanal = dtFinal;
			Date dtInicial = DateUtil.getDateValue(dtFinal);
			dtInicial.advance(-6);
			restricao.dtInicialVigenciaFilterSemanal = dtInicial;
		} else {
			restricao.dtInicialVigenciaFilterSemanal = DateUtil.getFirstDayOfWeek();
			restricao.dtFimVigenciaFilterSemanal = DateUtil.getLastDayOfWeek();
		}
	}

	public Vector filtraProdutosPorRestricao(Vector listProduto, Cliente cliente, final String nuPedido, final double qtdItemInserindo) throws SQLException {
		Vector newListProduto = new Vector();
		int size = listProduto.size();
		for (int i = 0; i < size; i++) {
			if (isProdutoRestrito(getProduto(listProduto.items[i]), cliente.cdCliente, nuPedido, qtdItemInserindo) != null) continue;
			newListProduto.addElement(listProduto.items[i]);
		}
		return newListProduto;
	}

	private String getProduto(Object item) {
		if (item instanceof ProdutoBase) {
			return ((ProdutoBase) item).cdProduto;
		} else if (item instanceof SugestaoVendaProd) {
			return ((SugestaoVendaProd) item).cdProduto;
		} else if (item instanceof ProdutoSimilar) {
			return ((ProdutoSimilar) item).cdProduto;
		} else if (item instanceof GiroProduto) {
			return ((GiroProduto) item).cdProduto;
		} else {
			return null;
		}
	}

	public void validateProdutoRestrito(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		RestricaoProduto restricaoProduto = RestricaoService.getInstance().isProdutoRestrito(itemPedido.cdProduto, pedido.cdCliente, pedido.nuPedido, itemPedido.getQtItemFisico());
		if (restricaoProduto != null) {
			throw new RestricaoValidationException(MessageUtil.getMessage(Messages.RESTRICAO_VENDA_ITEM_VALIDATION_PRODUTO_RESTRITO_QTD,
					new String[]{
							LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) restricaoProduto.qtItemDisponivel) : StringUtil.getStringValueToInterface(restricaoProduto.qtItemDisponivel),
							LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) restricaoProduto.qtItemComprando) : StringUtil.getStringValueToInterface(restricaoProduto.qtItemComprando)
					}
			));
		}
	}
}