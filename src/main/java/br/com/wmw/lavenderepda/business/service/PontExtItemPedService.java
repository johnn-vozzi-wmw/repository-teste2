package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PontExtItemPed;
import br.com.wmw.lavenderepda.business.domain.PontExtPed;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontExtItemPedPdbxDao;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.io.ByteArrayStream;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PontExtItemPedService extends CrudPersonLavendereService {

	private static PontExtItemPedService instance;

	public static PontExtItemPedService getInstance() {
		return instance == null ? instance = new PontExtItemPedService() : instance;
	}

	protected CrudDao getCrudDao() {
		return PontExtItemPedPdbxDao.getInstance();
	}

	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public void insertOrUpdateByPedido(Pedido pedido) throws SQLException {
		PontExtItemPed pontExtItemPed = createPontuacaoExtratoItemByPedidoFilter(pedido);
		int qtItens = pedido.itemPedidoList.size();
		for (int i = 0; i < qtItens; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			pontExtItemPed.cdProduto = itemPedido.cdProduto;
			pontExtItemPed.flTipoItemPedido = itemPedido.flTipoItemPedido;
			pontExtItemPed.nuSeqProduto = itemPedido.nuSeqProduto;
			pontExtItemPed.flTipoLancamento = PontExtItemPed.TIPO_LANCAMENTO_NOVO_PEDIDO;
			pontExtItemPed.dsTipoLancamento = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_NOVO_PEDIDO_ITEM, new Object[] {itemPedido.getProduto().toString()});
			pontExtItemPed.qtItemFisico = itemPedido.getQtItemFisico();
			pontExtItemPed.cdUnidade = itemPedido.cdUnidade;
			pontExtItemPed.nuConversaoUnidade = itemPedido.nuConversaoUnidade;
			pontExtItemPed.qtItemFaturamento = itemPedido.qtItemFaturamento;
			pontExtItemPed.vlTotalItemPedido = itemPedido.vlTotalItemPedido;
			pontExtItemPed.vlPontuacaoBase = itemPedido.vlPontuacaoBaseItem;
			pontExtItemPed.vlPontuacaoRealizado = itemPedido.vlPontuacaoRealizadoItem;
			pontExtItemPed.vlPesoPontuacao = itemPedido.vlPesoPontuacao;
			pontExtItemPed.vlFatorCorrecaoFaixaPreco = itemPedido.vlFatorCorrecaoFaixaPreco;
			pontExtItemPed.vlFatorCorrecaoFaixaDias = itemPedido.vlFatorCorrecaoFaixaDias;
			pontExtItemPed.vlPctFaixaPrecoPontuacao = itemPedido.vlPctFaixaPrecoPontuacao;
			pontExtItemPed.vlBasePontuacao = itemPedido.vlBasePontuacao;
			pontExtItemPed.dtEmissao = DateUtil.getCurrentDate();
			pontExtItemPed.hrEmissao = TimeUtil.getCurrentTimeHHMMSS();
			try {
				super.insert(pontExtItemPed);
			} catch (Throwable e) {
				update(pontExtItemPed);
			}
		}
	}

	private PontExtItemPed createPontuacaoExtratoItemByPedidoFilter(Pedido pedido) {
		PontExtItemPed PontExtItemPed = new PontExtItemPed();
		PontExtItemPed.cdEmpresa = pedido.cdEmpresa;
		PontExtItemPed.cdRepresentante = pedido.cdRepresentante;
		PontExtItemPed.flOrigemPedido = pedido.flOrigemPedido;
		PontExtItemPed.nuPedido = pedido.nuPedido;
		return PontExtItemPed;
	}

	public void deleteByPedido(Pedido pedido) throws SQLException {
		try {
			deleteAllByExample(createPontuacaoExtratoItemByPedidoFilter(pedido));
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public void enviaExtratoItensBackground(String cdSessao, PontExtPed pontExtPed) throws SQLException {
		try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
				ResultSet pontuacaoExtratoItensRS = st.executeQuery(findAllByExampleRS(pontExtPed))) {
			if (pontuacaoExtratoItensRS.isBeforeFirst()) {
				LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
	    		ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
	    		Vector rowKeys = lavendereTc2Web.writeDados(PontExtItemPed.TABLE_NAME, pontuacaoExtratoItensRS, cbasRetorno, false, null);
	    		EnviaDadosThread.getInstance().put(PontExtItemPed.TABLE_NAME, rowKeys.size(), cbasRetorno, cdSessao, rowKeys, ValueUtil.VALOR_NI, null);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

}