package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.PedidoBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.NfeService;
import br.com.wmw.lavenderepda.business.service.PedidoBoletoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfeDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoBoletoDao;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.print.PedidoServicePrint;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDocNaoImpressoForm extends LavendereCrudListForm {
	
	private Vector pedidoList;
	private Vector clienteList;
	private Vector nfeList;
	private Vector pedidoBoletoList;
	private boolean onWindow;

	public ListDocNaoImpressoForm() {
		super(Messages.DOCS_NAO_IMPRESSOS_NOME);
		contructorListContainer();
		singleClickOn = true;
		nfeList = new Vector();
		pedidoBoletoList = new Vector();
		pedidoList = new Vector();
	}

	protected void contructorListContainer() {
		listContainer = new GridListContainer(4, 2);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setBarTopSimple();
	}
	
	public void setOnWindow() {
		barTopContainer.setVisible(false);
		barBottomContainer.setVisible(false);
		onWindow = true;
	}
	
	@Override
	protected int getTop() {
		if (onWindow) {
			return TOP;
		} else {
			return super.getTop();
		}
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new Pedido();
	}

	@Override
	protected CrudService getCrudService() {
		return PedidoService.getInstance();
	}

	@Override
	protected void onFormStart() {
    	UiUtil.add(this, listContainer, LEFT,  getTop(), FILL, FILL - (onWindow ? 0 : barBottomContainer.getHeight()));
	}

	@Override
	protected void onFormEvent(Event event) {
		
	}
	
	@Override
	public void onFormShow() throws SQLException {
		loadDados();
	}
	
	@Override
	public void onFormExibition() throws SQLException {
		list();
	}
	
	public void loadDados() throws SQLException {
		loadClienteList();
		loadPedidoList();
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
			loadNfe();
		}
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
			loadPedidoBoleto();
		}
		list();
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		loadPedidoList();
		String cdEmpresa = SessionLavenderePda.cdEmpresa;
		String cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector domainList = new Vector();
		Pedido pedido;
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosNfeEItemNfeBackground()) {
			Nfe nfe;
			for (int i = 0; i < nfeList.size(); i++) {
				pedido = new PedidoBuilder(cdEmpresa, cdRepresentante, "", OrigemPedido.FLORIGEMPEDIDO_PDA).build();
				nfe = (Nfe) nfeList.items[i];
				pedido.nuPedido = nfe.nuPedido;
				pedido = getPedidoInfoNfeImpresso(pedido);
				if (pedido != null) {
					pedido.setCliente(getCliente(pedido));
					pedido.pedidoNfeBoletoLabel = Messages.NFE_LABEL;
					domainList.addElement(pedido);
				}
			}
		}
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosPedidoBoletoBackground()) {
			PedidoBoleto pedidoBoleto;
			for (int i = 0; i < pedidoBoletoList.size(); i++) {
				pedido = new PedidoBuilder(cdEmpresa, cdRepresentante, "", OrigemPedido.FLORIGEMPEDIDO_PDA).build();
				pedidoBoleto = (PedidoBoleto) pedidoBoletoList.items[i];
				pedido.nuPedido = pedidoBoleto.nuPedido;
				if (domainList.contains(pedido)) {
					continue;
				}
				pedido = getInfoBoletoImpressoPedido(pedido);
				if (pedido != null) {
					pedido.setCliente(getCliente(pedido));
					pedido.pedidoNfeBoletoLabel = Messages.PEDIDOBOLETO_LABEL;
					domainList.addElement(pedido);
				}
			}
		}
		return domainList;
	}
	
	private Pedido getPedidoInfoNfeImpresso(Pedido pedidoFilter) {
		for (int i = 0; i < pedidoList.size(); i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (pedido.nuPedido.equals(pedidoFilter.nuPedido) && !ValueUtil.VALOR_SIM.equals(pedido.flNfeImpressa)) {
				return pedido;
			}
		}
		return null;
	}
	
	private Pedido getInfoBoletoImpressoPedido(Pedido pedidoFilter) {
		for (int i = 0; i < pedidoList.size(); i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (pedido.nuPedido.equals(pedidoFilter.nuPedido) && !ValueUtil.VALOR_SIM.equals(pedido.flBoletoImpresso)) {
				return pedido;
			}
		}
		return null;
	}
	
	private Cliente getCliente(Pedido pedido) {
		Cliente cliente = null;
		for (int j = 0; j < clienteList.size(); j++) {
			cliente = (Cliente) clienteList.items[j];
			if (ValueUtil.valueEquals(pedido.cdCliente, cliente.cdCliente)) {
				return cliente;
			}
		}
		return null;
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Pedido pedido = (Pedido) domain;
		String[] item = {
    			StringUtil.getStringValue(pedido.getCliente()),
    			"",
    			new StringBuffer(Messages.DOCS_NAO_IMPRESSOS_LABEL_PEDIDO).append(StringUtil.getStringValue(pedido.nuPedido)).toString(),
    			StringUtil.getStringValue(pedido.pedidoNfeBoletoLabel)
		};
		return item;
	}
	
	private void loadPedidoList() throws SQLException {
		pedidoList = new Vector();
		Pedido pedido = new Pedido();
		pedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		pedido.filtraDocNaoImpressos = true;
		pedidoList = PedidoService.getInstance().findAllByExampleOnlyPda(pedido);
	}
	
	private void loadClienteList() throws SQLException {
		if (ValueUtil.isEmpty(clienteList)) {
			Cliente cliente = new Cliente();
			cliente.cdEmpresa = SessionLavenderePda.cdEmpresa;
			cliente.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			clienteList = ClienteService.getInstance().findAllByExampleSummary(cliente);
		}
	}
	
	private void loadNfe() throws SQLException {
		Nfe nfeFilter = new Nfe();
		nfeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		nfeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (onWindow) {
			if (!NfeDao.houveRecebimentoNfeBackground) {
				nfeList = new Vector();
				return;
			}
			nfeFilter.nuCarimbo = NfeService.getInstance().getMaxCarimbo();
		}
		nfeList = NfeService.getInstance().findAllByExample(nfeFilter);
	}
	
	private void loadPedidoBoleto() throws SQLException {
		PedidoBoleto pedidoBoletoFilter = new PedidoBoleto();
		pedidoBoletoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoBoletoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (onWindow) {
			if (!PedidoBoletoDao.houveRecebimentoBoletoBackground) {
				pedidoBoletoList = new Vector();
				return;
			}
			pedidoBoletoFilter.nuCarimbo = PedidoBoletoService.getInstance().getMaxCarimbo();
		}
		pedidoBoletoList = PedidoBoletoService.getInstance().findAllByExample(pedidoBoletoFilter);
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		super.detalhesClick();
		if (getSelectedDomain() != null) {
			Pedido pedido = (Pedido) getSelectedDomain();
			sugereImpressao(pedido);
		}
	}
	
	private void sugereImpressao(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isSugereImpressaoDocumentosEContingenciaAposEnvio()) {
			TipoPedido tipoPedido = pedido.getTipoPedido();
			if (tipoPedido != null) {
				if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais() && tipoPedido.isGeraNfe() && ValueUtil.isNotEmpty(pedido.getInfoNfe().nuPedido)) {
					sugereImpressaoNfeClick(pedido);
				}
				if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0 && tipoPedido.isGeraBoleto() && ValueUtil.isNotEmpty(pedido.getPedidoBoletoList())) {
					sugereImpressaoBoleto(pedido);
				}
			}
			loadDados();
		}
	}
	
	private void sugereImpressaoNfeClick(Pedido pedido) {
		if (pedido.isNfeImpressa()) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_NFE_NOVAMENTE)) {
				btImprimirNfeClick(pedido);
			}
		} else if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_NFE)) {
			btImprimirNfeClick(pedido);
		}
	}
	
	private void btImprimirNfeClick(Pedido pedido) {
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		try {
			PedidoServicePrint pedidoServicePrint = new PedidoServicePrint(pedido);
			pedidoServicePrint.imprimeNfe();
			if (LavenderePdaConfig.usaImpressaoComprovanteNfe == 2) {
				if (UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAONFE_MSG_CONFIRMACAO_COMPROVANTE)) {
					pedidoServicePrint.imprimeComprovanteNfe();
				}
			}
			PedidoService.getInstance().updateFlImpressoNfe(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAONFE_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}
	
	private void sugereImpressaoBoleto(Pedido pedido) {
		if (pedido.isBoletoImpresso()) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_IMPRIMIR_BOLETO_NOVAMENTE)) {
				btImprimirBoletoClick(pedido);
			}
		} else if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_SUGERE_IMPRESSAO_BOLETO)) {
			btImprimirBoletoClick(pedido);
		}
	}
	
	private void btImprimirBoletoClick(Pedido pedido) {
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
		try {
			PedidoServicePrint pedidoServicePrint = new PedidoServicePrint(pedido);
			pedidoServicePrint.imprimeBoleto();
			if (LavenderePdaConfig.usaImpressaoComprovanteBoleto == 2 && UiUtil.showConfirmYesNoMessage(Messages.IMPRESSAOBOLETO_MSG_CONFIRMACAO_COMPROVANTE)) {
				pedidoServicePrint.imprimeBoleto(true);
			}
			PedidoService.getInstance().updateFlImpressoBoleto(pedido);
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.IMPRESSAOBOLETO_MSG_ERRO, ex.getMessage()));
		} finally {
			pb.unpop();
		}
	}

}
