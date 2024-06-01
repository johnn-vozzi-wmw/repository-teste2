package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.service.PedidoEstoqueService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListGeracaoRemessaEstoqueForm extends BaseCrudListForm {
	
	private ButtonAction btGerarRemessa;

	public ListGeracaoRemessaEstoqueForm() {
		super(Messages.REMESSAESTOQUE_TITULO_CADASTRO);
		btGerarRemessa = new ButtonAction(Messages.REMESSAESTOQUE_GERAR_REMESSA, "images/novaRemessa.png");
		GridColDefinition[] gridColDefinition = {
				new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
				new GridColDefinition(Messages.REMESSAESTOQUE_LABEL_PRODUTO, -50, LEFT),
				new GridColDefinition(Messages.REMESSAESTOQUE_LABEL_ESTOQUE_EMPRESA, -25, LEFT),
				new GridColDefinition(Messages.REMESSAESTOQUE_DESEJADO, -25, LEFT)
		}; 
		gridEdit = UiUtil.createGridEdit(gridColDefinition);
		gridEdit.setColumnEditableDoubleNoCalc(3, true, 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface, 0);
	}
	
	@Override
	protected Vector getDomainList() throws SQLException {
		Produto filter = (Produto)getDomainFilter();
		filter.dsProduto = edFiltro.getText();
		return ProdutoService.getInstance().findProdutoListGeracaoRemessa(filter);
	}
	
	@Override
	protected void filtrarClick() throws SQLException {
		list();
	}
	
	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		Produto filter = new Produto();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		return filter;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return RemessaEstoqueService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, gridEdit, LEFT, getTop() + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
		UiUtil.add(barBottomContainer, btGerarRemessa, 5);
		gridEdit.setGridControllable();
		gridEdit.disableSort = true;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btGerarRemessa) {
				gerarRemessaClick();
			}
			break;
		}
		
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		Produto produto = (Produto) domain;
		return new String[] {
			StringUtil.getStringValue(produto.cdProduto),
			ProdutoService.getInstance().getDescriptionWithId(produto, produto.cdProduto),
			StringUtil.getStringValueToInterface(produto.qtEstoqueProduto, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface),
			ValueUtil.VALOR_NI
		};
	}
	
	private void gerarRemessaClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.REMESSAESTOQUE_CONFIRMACAO_REMESSA)) {
			try {
				UiUtil.showProcessingMessage();
				int size = gridEdit.size();
				PedidoEstoqueDto pedidoEstoque = new PedidoEstoqueDto();
				pedidoEstoque.cdEmpresa = SessionLavenderePda.cdEmpresa;
				pedidoEstoque.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
				pedidoEstoque.cdPedidoEstoque = RemessaEstoqueService.getInstance().generateIdGlobal();
				pedidoEstoque.flTipoRemessa = PedidoEstoqueDto.TIPOREMESSA_R;
				pedidoEstoque.dtPedidoEstoque = DateUtil.getCurrentDate();
				pedidoEstoque.hrPedidoEstoque = TimeUtil.getCurrentTimeHHMMSS();
				Vector itemPedidoRemessaList = new Vector(size);
				double value = 0;
				for (int i = 0; i < size; i++) {
					if ((value = ValueUtil.getDoubleValue(gridEdit.getItem(i)[3])) > 0) {
						if (ValueUtil.getDoubleValue(gridEdit.getItem(i)[2]) < value) {
							throw new ValidationException(Messages.REMESSAESTOQUE_ERRO_SEM_ESTOQUE);
						}
						ItemPedidoEstoqueDto itemPedidoEstoque = new ItemPedidoEstoqueDto();
						itemPedidoEstoque.cdEmpresa = pedidoEstoque.cdEmpresa;
						itemPedidoEstoque.cdRepresentante = pedidoEstoque.cdRepresentante;
						itemPedidoEstoque.cdPedidoEstoque = pedidoEstoque.cdPedidoEstoque;
						itemPedidoEstoque.cdProduto = gridEdit.getItem(i)[0];
						itemPedidoEstoque.qtRemessa = value;
						itemPedidoRemessaList.addElement(itemPedidoEstoque);
					}
				}
				if (itemPedidoRemessaList.size() == 0) {
					throw new ValidationException(Messages.REMESSAESTOQUE_ERRO_NENHUM_PRODUTO);
				}
				pedidoEstoque.itemPedidoEstoqueList = itemPedidoRemessaList;
				if (SyncManager.isConexaoPdaDisponivel()) {
					if (PedidoEstoqueService.getInstance().gerarRemessa(pedidoEstoque)) {
						UiUtil.showSucessMessage(Messages.REMESSAESTOQUE_GERADA_SUCESSO);
						voltarClick();
					}
				} else {
					UiUtil.showErrorMessage(Messages.REMESSAESTOQUE_ERRO_SEM_CONEXAO);
				}
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		}
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		super.voltarClick();
		((BaseCrudListForm)this.prevContainer).list();
	}

}
