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
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PontExtPed;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontExtPedPdbxDao;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import totalcross.io.ByteArrayStream;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class PontExtPedService extends CrudPersonLavendereService {

    private static PontExtPedService instance;

    public static PontExtPedService getInstance() {
        return instance == null ? instance = new PontExtPedService() : instance;
    }

    protected CrudDao getCrudDao() { return PontExtPedPdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}

	public void insertOrUpdateByPedido(Pedido pedido) throws SQLException {
		if (pedido == null || (pedido.vlTotalPontuacaoBase == 0 && pedido.vlTotalPontuacaoRealizado == 0)) return;
		PontExtPed pontExtPed = createPontuacaoExtratoByPedidoFilter(pedido);
		final Cliente cliente = pedido.getCliente();
		pontExtPed.cdCanal = cliente.cdCanal;
		pontExtPed.cdSegmento = cliente.cdSegmento;
		pontExtPed.dsTipoLancamento = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_NOVO_PEDIDO, new Object[] {cliente.toString()});
		pontExtPed.vlPontuacaoBase = pedido.vlTotalPontuacaoBase;
		pontExtPed.vlPontuacaoRealizado = pedido.vlTotalPontuacaoRealizado;
		pontExtPed.vlTotalPedido = pedido.vlTotalPedido;
		pontExtPed.qtDiasMedios = pedido.getCondicaoPagamento().qtDiasMediosPagamento;
		pontExtPed.flPedidoCancelado = ValueUtil.VALOR_NAO;
		pontExtPed.dtEmissao = DateUtil.getCurrentDate();
		pontExtPed.hrEmissao = TimeUtil.getCurrentTimeHHMMSS();
    	try {
    		super.insert(pontExtPed);
		} catch (Throwable e) {
			update(pontExtPed);
		}
    	PontExtItemPedService.getInstance().insertOrUpdateByPedido(pedido);
	}

	private PontExtPed createPontuacaoExtratoByPedidoFilter(Pedido pedido) {
		PontExtPed pontExtPed = new PontExtPed();
		pontExtPed.cdEmpresa = pedido.cdEmpresa;
		pontExtPed.cdRepresentante = pedido.cdRepresentante;
		pontExtPed.flOrigemPedido = pedido.flOrigemPedido;
		pontExtPed.nuPedido = pedido.nuPedido;
		return pontExtPed;
	}

	public void deleteByPedido(Pedido pedido) throws SQLException {
		try {
			deleteAllByExample(createPontuacaoExtratoByPedidoFilter(pedido));
			PontExtItemPedService.getInstance().deleteByPedido(pedido);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public void enviaExtratoBackground(String cdSessao, Pedido pedido) throws SQLException {
		if (pedido == null) return;
		PontExtPed pontExtPed = createPontuacaoExtratoByPedidoFilter(pedido);
		try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
				ResultSet pontExtPedRS = st.executeQuery(findByRowKeySql(pontExtPed.rowKey))) {
			if (pontExtPedRS.next()) {
				LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
				ByteArrayStream cbasRetorno = new ByteArrayStream(4096);
				int nuLinhas = lavendereTc2Web.writeDados(PontExtPed.TABLE_NAME, pontExtPedRS, cbasRetorno, false, null).size();
				EnviaDadosThread.getInstance().put(PontExtPed.TABLE_NAME, nuLinhas, cbasRetorno, cdSessao, pontExtPed.rowKey);
				PontExtItemPedService.getInstance().enviaExtratoItensBackground(cdSessao, pontExtPed);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

}