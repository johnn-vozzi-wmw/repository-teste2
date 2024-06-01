package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ContratoCliente;
import br.com.wmw.lavenderepda.business.domain.ContratoProduto;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ContratoClienteService;
import br.com.wmw.lavenderepda.business.service.ContratoProdutoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListForecastForm extends BaseCrudListForm {

	private ButtonAction btEmitirPedido;
	private String dsMsgQtContratoProduto;
	private ContratoCliente contratoClienteVigente;

    public ListForecastForm(ContratoCliente contratoClienteVigente) {
        super(Messages.FORECAST_TITULO_CADASTRO);
        this.contratoClienteVigente = contratoClienteVigente;
        btEmitirPedido = new ButtonAction(Messages.BOTAO_GERAR_PEDIDO, "images/add.png");
        singleClickOn = false;
		listContainer = new GridListContainer(2, 1);
		listContainer.setBarTopSimple();
		dsMsgQtContratoProduto = Messages.FORECAST_QTCONTRATOPRODUTO;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ContratoProdutoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new ContratoProduto();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	return contratoClienteVigente.contratoProdutoList;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        ContratoProduto forecast = (ContratoProduto) domain;
        String descriptionProduto = getDescriptionProduto(forecast.cdProduto);
        if (LavenderePdaConfig.ocultaColunaCdProduto) {
        	descriptionProduto += " [" + forecast.cdProduto + "]";
        } else {
        	descriptionProduto = forecast.cdProduto + " - " + descriptionProduto;
        }
		String[] item = {
            descriptionProduto,
            dsMsgQtContratoProduto + " " + StringUtil.getStringValueToInterface(forecast.qtProdutoContrato)};
        return item;
    }

	public String getDescriptionProduto(String cdProduto) throws SQLException {
		Produto produto = new Produto();
    	produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produto.cdProduto = cdProduto;
    	return ProdutoService.getInstance().getDsProduto(produto);
	}

    //@Override
    protected String getSelectedRowKey() throws SQLException {
		return listContainer.getSelectedId();
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
        UiUtil.add(barBottomContainer, btEmitirPedido, 5);
        UiUtil.add(this, listContainer, LEFT, clienteContainer.getY2() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btEmitirPedido) {
					btGerarPedidoClick();
				}
				break;
			}
		}
    }

	private void btGerarPedidoClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRM_GERAR_PEDIDO)) {
			Pedido pedido;
			try {
				pedido = ContratoClienteService.getInstance().createNewPedidoByForecast(contratoClienteVigente);
			} catch (ValidationException ex) {
				if (ex.getMessage().indexOf(FrameworkMessages.MSG_VALIDACAO_VALOR_INVALIDO) != -1) {
					boolean result = UiUtil.showConfirmYesNoMessage(ex.getMessage());
					if (result) {
						close();
					}
					return;
				} else {
					throw ex;
				}
			}
			boolean enviaAutomaticoERP = LavenderePdaConfig.sugereEnvioAutomaticoPedido && UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_ENVIAR_PEDIDOS_AGORA);
			//--
			Vector pedidosList = new Vector();
			pedidosList.addElement(pedido);
			String fecharPedido = "";
			try {
				fecharPedido = PedidoService.getInstance().fecharPedidos(pedidosList, true, true);
			} catch (ValidationException e) {
				PedidoService.getInstance().delete(pedido);
				throw e;
			}
			if (ValueUtil.isEmpty(fecharPedido) && enviaAutomaticoERP) {
				pedido.hrFimEmissao = TimeUtil.getCurrentTimeHHMM();
				PedidoPdbxDao.getInstance().update(pedido);
				PedidoUiUtil.enviaPedido(false, true);
			}
			close();
		}
	}

}
