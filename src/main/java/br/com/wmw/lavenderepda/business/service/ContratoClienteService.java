package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationWarnException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ContratoCliente;
import br.com.wmw.lavenderepda.business.domain.ContratoProdEst;
import br.com.wmw.lavenderepda.business.domain.ContratoProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContratoClienteDbxDao;
import totalcross.util.Vector;

public class ContratoClienteService extends CrudService {

    private static ContratoClienteService instance;

    private ContratoClienteService() {
        //--
    }

    public static ContratoClienteService getInstance() {
        if (instance == null) {
            instance = new ContratoClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContratoClienteDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public ContratoCliente getContratoClienteForecastVigente() throws SQLException {
    	return getContratoClienteVigente(ContratoCliente.FORECAST);
    }

    public ContratoCliente getContratoClienteGradeVigente() throws SQLException {
    	return getContratoClienteVigente(ContratoCliente.GRADE);
    }

	public ContratoCliente getContratoClienteVigente(String flTipoCadastro) throws SQLException {
		ContratoCliente contratoClienteFilter = new ContratoCliente();
		contratoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		contratoClienteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		contratoClienteFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		contratoClienteFilter.flTipoContrato = flTipoCadastro;
		contratoClienteFilter.dtVigenciaFilter = DateUtil.getCurrentDate();
		Vector contratos = findAllByExample(contratoClienteFilter);
		ContratoCliente contratoClienteVigente = getContratoClienteVigente(contratos);
		if (contratoClienteVigente != null) {
	    	ContratoProduto contratoProdutoFilter = new ContratoProduto();
	    	contratoProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    	contratoProdutoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
	    	contratoProdutoFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
			contratoProdutoFilter.dtVigenciaInicial = contratoClienteVigente.dtVigenciaInicial;
	    	contratoProdutoFilter.flTipoContrato = flTipoCadastro;
	    	contratoProdutoFilter.sortAsc = ValueUtil.VALOR_SIM;
	    	contratoProdutoFilter.sortAtributte = "cdProduto";
	        contratoClienteVigente.contratoProdutoList = ContratoProdutoService.getInstance().findAllByExampleSummary(contratoProdutoFilter);
	        if (ValueUtil.isEmpty(contratoClienteVigente.contratoProdutoList)) {
				throw new ApplicationWarnException(Messages.CONTRATOCLIENTE_MSG_ALERTA_NENHUM_CONTRATO_PRODUTO);
	        }
		}
		return contratoClienteVigente;
	}

	protected ContratoCliente getContratoClienteVigente(Vector contratos) {
		if (ValueUtil.isEmpty(contratos)) {
			throw new ApplicationWarnException(Messages.CADASTROCLIENTE_MSG_ALERTA_NENHUM_CONTRATO_VIGENTE);
		} else if (contratos.size() > 1) {
			throw new ApplicationWarnException(Messages.CADASTROCLIENTE_MSG_ALERTA_VARIOS_CONTRATO_VIGENTE);
		}
		ContratoCliente contratoClienteVigente = (ContratoCliente)contratos.items[0];
		return contratoClienteVigente;
	}

	public Pedido createNewPedidoByForecast(ContratoCliente contratoCliente) throws SQLException {
		//Pedido
		Pedido pedido = PedidoService.getInstance().createNewPedido(SessionLavenderePda.getCliente());
		convertContratoClienteToPedido(contratoCliente, pedido);
		//Itens
		ContratoProduto contratoProduto;
		ItemPedido itemPedido;
		Vector contratoProdutoList = contratoCliente.contratoProdutoList;
		int size = contratoProdutoList.size();
		for (int i = 0; i < size; i++) {
			contratoProduto = (ContratoProduto) contratoProdutoList.items[i];
			itemPedido = ItemPedidoService.getInstance().createNewItemPedido(pedido);
			itemPedido.nuSeqItemPedido = i + 1;
			ContratoProdutoService.getInstance().convertForecastToItemPedido(contratoProduto, pedido, itemPedido);
			pedido.itemPedidoList.addElement(itemPedido);
		}
		//Grava
		PedidoService.getInstance().insertPedidoAndItensPedido(pedido);
		return pedido;
	}

	public Pedido createNewPedidoByGrade(ContratoCliente contratoCliente, Vector contratoProdEstList) throws SQLException {
		//Pedido
		Pedido pedido = PedidoService.getInstance().createNewPedido(SessionLavenderePda.getCliente());
		convertContratoClienteToPedido(contratoCliente, pedido);
		//Itens
		ContratoProdEst contratoProdEst;
		ItemPedido itemPedido;
		int size = contratoProdEstList.size();
		int j = 0;
		for (int i = 0; i < size; i++) {
			contratoProdEst = (ContratoProdEst) contratoProdEstList.items[i];
			itemPedido = ItemPedidoService.getInstance().createNewItemPedido(pedido);
			ContratoProdutoService.getInstance().convertGradeToItemPedido(contratoProdEst.contratoProduto, contratoProdEst, pedido, itemPedido);
			if (itemPedido.getQtItemFisico() != 0) {
				itemPedido.nuSeqItemPedido = j + 1;
				pedido.itemPedidoList.addElement(itemPedido);
				//--
				j++;
			}
		}
		//Grava
		PedidoService.getInstance().insertPedidoAndItensPedido(pedido);
		return pedido;
	}

	protected void convertContratoClienteToPedido(ContratoCliente contratoCliente, Pedido pedido) throws SQLException {
		TabelaPreco tabelaPreco = TabelaPrecoService.getInstance().getTabelaPreco(contratoCliente.cdTabelaPreco);
		if (tabelaPreco != null) {
			pedido.cdTabelaPreco = tabelaPreco.cdTabelaPreco;
		} else {
			//Se a do Forecast não existir, não aceita nenhuma outra.
			pedido.cdTabelaPreco = null;
		}
		//--
		CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(contratoCliente.cdCondicaoPagamento);
		if (condicaoPagamento != null) {
			pedido.cdCondicaoPagamento = condicaoPagamento.cdCondicaoPagamento;
		} else {
			//Se a do Forecast não existir, não aceita nenhuma outra.
			pedido.cdCondicaoPagamento = null;
		}
		//--
		pedido.flBloqueadoEdicao = ValueUtil.VALOR_SIM;
	}

}