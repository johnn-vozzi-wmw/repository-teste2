package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DepositoBancario;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DepositoBancarioService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListDepositoBancarioForm extends LavendereCrudListForm {
	
	private RelFechamentoDiarioForm relFechamentoDiarioForm;
	private Date dateFilter;

	public ListDepositoBancarioForm(RelFechamentoDiarioForm relFechamentoDiarioForm, Date dateFilter) throws SQLException {
		super(Messages.DEPOSITO_BANCARIO_TITULO_LISTA);
		setBaseCrudCadForm(new CadDepositoBancarioForm());
		constructorListContainer();
		this.relFechamentoDiarioForm = relFechamentoDiarioForm;
		this.dateFilter = dateFilter;
		btNovo.setVisible(getVisibilidadeBotaoNovo(dateFilter));
		listResizeable = false;
		singleClickOn = true;
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return DepositoBancarioService.getInstance().getNewDepositoBancario(dateFilter);
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return DepositoBancarioService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
		UiUtil.add(barBottomContainer, btNovo, 5);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		DepositoBancario depositoBancario = (DepositoBancario) domain;
		Vector item = new Vector();
		item.addElement(StringUtil.getStringValue(depositoBancario.dtDepositoBancario) + " - " +  depositoBancario.nuDepositoBancario);
		item.addElement(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(depositoBancario.vlTotalDepositoBancario));
		return (String[]) item.toObjectArray();
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		FechamentoDiario fechamentoDiario = FechamentoDiarioService.getInstance().findFechamentoDiarioPorData(dateFilter);
		if (fechamentoDiario != null) {
			relFechamentoDiarioForm.atualizaValores(fechamentoDiario);
			relFechamentoDiarioForm.setaVisibilidadeBotaoFechar(fechamentoDiario);
			relFechamentoDiarioForm.setaVisibilidadeBotaoLiberarSenha(fechamentoDiario);
			relFechamentoDiarioForm.ajustaBotoesDinamicamente();
		} else {
			relFechamentoDiarioForm.atualizaValores(null);
			relFechamentoDiarioForm.setaVisibilidadeBotaoFechar(FechamentoDiarioService.getInstance().findFechamentoDiarioPorData(dateFilter));
			relFechamentoDiarioForm.setaVisibilidadeBotaoLiberarSenha(null);
			relFechamentoDiarioForm.ajustaBotoesDinamicamente();
		}
		super.voltarClick();
	}
	
	@Override
	public void novoClick() throws SQLException {
		CadDepositoBancarioForm cadDepositoBancarioForm = (CadDepositoBancarioForm) getBaseCrudCadForm();
		cadDepositoBancarioForm.setDtFechamentoDiario(dateFilter);
		super.novoClick();
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		CadDepositoBancarioForm cadDepositoBancarioForm = (CadDepositoBancarioForm) getBaseCrudCadForm();
		cadDepositoBancarioForm.setDtFechamentoDiario(dateFilter);
		super.detalhesClick();
	}
	
		
	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		configListContainer("NUDEPOSITOBANCARIO");
		listContainer = new GridListContainer(2, 1);
		listContainer.setColsSort(new String[][]{{Messages.DEPOSITO_BANCARIO_NUDEPOSITOBANCARIO, "NUDEPOSITOBANCARIO"}, {Messages.DEPOSITO_BANCARIO_VLTOTALDEPOSITOBANCARIO, "vlTotalDepositoBancario"}});
		listContainer.setColTotalizerRight(1, Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_DEPOSITO);
		ScrollPosition.AUTO_HIDE = true;
	}
	
	private boolean getVisibilidadeBotaoNovo(Date dataFechamentoDiario) throws SQLException {
		if (FechamentoDiarioService.getInstance().isFechamentoDiarioExecutado(dataFechamentoDiario)) return false;
		double vlTotalDinheiroPorData = getVlDinheiroPedido(dataFechamentoDiario) + PagamentoService.getInstance().getSomaTotalDinheiroPorData(dataFechamentoDiario, dataFechamentoDiario);
		return vlTotalDinheiroPorData > 0 || (!LavenderePdaConfig.consideraValorPedidoAtualRetornado() && PagamentoPedidoService.getInstance().getSomaDinheiroPedidoPorData(dataFechamentoDiario, dataFechamentoDiario) > 0);
	}
	
	private double getVlDinheiroPedido(Date dataFechamentoDiario) throws SQLException {
		Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(dataFechamentoDiario);
		return pedidoSum != null ? pedidoSum.vlTotalDinheiro : 0;
	}
	
}
