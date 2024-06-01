package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.service.EstoqueRepService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusRemessaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoRemessaComboBox;
import br.com.wmw.lavenderepda.print.FechamentoEstoquePrint;
import br.com.wmw.lavenderepda.print.InventarioRemessaEstoquePrint;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListRemessaEstoqueForm extends BaseCrudListForm {

	private StatusRemessaComboBox cbStatus;
	private ButtonAction btDevolveEstoqueTotal;
	private ButtonAction btImprimirDevEstoque;
	private TipoRemessaComboBox cbTipoRemessa;
	private ButtonAction btDevolucao;
	private ButtonAction btListaNfe;
	private ButtonAction btImprimirInventario;
	
	private static final int DEVOLUCAO_REPRESENTANTE = 1;
	
    public ListRemessaEstoqueForm() throws SQLException {
        super(Messages.REMESSAESTOQUE_TITULO_LISTA);
        setBaseCrudCadForm(new CadRemessaEstoqueForm());
        singleClickOn = true;
        listContainer = new GridListContainer(4, 2);
        listContainer.setColPosition(1, RIGHT);
        listContainer.setColPosition(3, RIGHT);
        listContainer.setBarTopSimple();
        btDevolveEstoqueTotal = new ButtonAction(Messages.REMESSAESTOQUE_LABEL_DEVOLVER_EST_TOTAL, "images/devolverEstoque.png");
        btImprimirDevEstoque = new ButtonAction(Messages.REMESSAESTOQUE_BOTAO_IMPRIMIR_DEV_ESTOQUE, "images/impressao.png");
        btImprimirInventario = new ButtonAction(Messages.RELATORIO_REMESSA_BOTAO_IMPRIMIR_INVENTARIO, "images/impressao.png");
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	btNovo.setText(Messages.REMESSAESTOQUE_BOTAO_CRIAR_REMESSA);
        	cbTipoRemessa = new TipoRemessaComboBox();
        	btListaNfe = new ButtonAction(Messages.REMESSAESTOQUE_LISTA_NFE, "images/list.png");
        } else {
        	cbStatus = new StatusRemessaComboBox();
        }
        if (LavenderePdaConfig.usaDevolucaoRemessaEstoqueNoEquipamento) {
        	btDevolucao = new ButtonAction(Messages.REMESSAESTOQUE_DEVOLUCAO_ESTOQUE, "images/devolucao.png");
        	if (btListaNfe == null) {
        		btListaNfe = new ButtonAction(Messages.REMESSAESTOQUE_LISTA_NFE, "images/list.png");
        	}
        }
    }
    
    @Override
    protected CrudService getCrudService() {
        return RemessaEstoqueService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		RemessaEstoque domainFilter = new RemessaEstoque();
		domainFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		domainFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
			domainFilter.flTipoRemessa = cbTipoRemessa.getValue();
		} else {
			domainFilter.flEstoqueLiberado = cbStatus.getValue();
			domainFilter.flFinalizada = cbStatus.getValue();
		}
		return domainFilter;
	}
    
    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
    		return RemessaEstoqueService.getInstance().findAllRemessaEstoque(getDomainFilter(), true);
    	}
    	return RemessaEstoqueService.getInstance().findAllRemessaEstoque(getDomainFilter(), cbStatus.isLiberada());
    }
    
    @Override
    protected String[] getItem(Object domain) {
        RemessaEstoque remessaEstoque = (RemessaEstoque) domain;
        String[] item = {
                Messages.REMESSAESTOQUE_LABEL_NUNOTAREMESSA + " " + StringUtil.getStringValue(remessaEstoque.nuNotaRemessa),
                Messages.REMESSAESTOQUE_LABEL_NUSERIEREMESSA + " " + StringUtil.getStringValue(remessaEstoque.nuSerieRemessa),
                Messages.REMESSAESTOQUE_LABEL_CDLOCALESTOQUE +  " " + StringUtil.getStringValue(remessaEstoque.cdLocalEstoque),
                LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento ? 
                Messages.REMESSAESTOQUE_LABEL_TIPO + " " + StringUtil.getStringValue(remessaEstoque.getDsTipoRemessa()) :
                Messages.REMESSAESTOQUE_LABEL_STATUS +  " " + StringUtil.getStringValue(remessaEstoque.getDsStatus(cbStatus.getValue()))
                };
        return item;
    }

    @Override
    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

   
    //--------------------------------------------------------------

    @Override
    protected void onFormStart() {
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	UiUtil.add(barBottomContainer, btNovo, 5);
        	UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_TIPO_REMESSA), cbTipoRemessa, getLeft(), getNextY());
        	UiUtil.add(barBottomContainer, btDevolveEstoqueTotal, 2);
        } else {
        	UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_LABEL_STATUS), cbStatus, getLeft(), getNextY());
        	UiUtil.add(barBottomContainer, btDevolveEstoqueTotal, 5);
        }
        if (LavenderePdaConfig.usaDevolucaoRemessaEstoqueNoEquipamento) {
        	UiUtil.add(barBottomContainer, btDevolucao, 4);
        }
        if (btListaNfe != null) {
        	UiUtil.add(barBottomContainer, btListaNfe, 3);
        }
        UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - barBottomContainer.getHeight());
        int posicao = 1;
        if ("3".equals(LavenderePdaConfig.usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante) || "6".equals(LavenderePdaConfig.usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante)) {
        	UiUtil.add(barBottomContainer, btImprimirDevEstoque,posicao++);
        }
        if (LavenderePdaConfig.usaImpressaoInventarioRemessaEstoque == 3 || LavenderePdaConfig.usaImpressaoInventarioRemessaEstoque == 6) {
        	UiUtil.add(barBottomContainer, btImprimirInventario, posicao);
        }
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == cbStatus || event.target == cbTipoRemessa) {
	    			list();
	    			break;
	    		} else if (event.target == btDevolucao) {
	    			devolucaoClick();
	    		} else if (event.target == btListaNfe) {
	    			show(new ListNfeEstoqueForm());
	    		}
	    		if (event.target == btImprimirDevEstoque) {
	    			try {
	    				btImprimirDevEstoqueClick();
	    			}catch (Throwable e) {
	    				UiUtil.showErrorMessage(e.getMessage());
	    			}
	    			break;
	    		}
	    		if (event.target == btImprimirInventario) {
	    			btImprimirInventario();
	    			break;
	    		}
	    		if (event.target == btDevolveEstoqueTotal) {
	    			btDevolveEstoqueTotalClick();
	    		}
	    		break;
    		}	
    	}
    }

    
    @Override
    public void visibleState() {
    	super.visibleState();
    	btDevolveEstoqueTotal.setVisible(LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.usaDevolucaoTotalEstoqueRemessaRepresentante);
    }
    @Override
    public void novoClick() throws SQLException {
    	show(new ListGeracaoRemessaEstoqueForm());
    }
    
    private void devolucaoClick() throws SQLException {
    	int opcao = UiUtil.showMessage(Messages.REMESSAESTOQUE_DEVOLUCAO_ESCOLHA, new String[] {Messages.EMPRESA_NOME_ENTIDADE, Messages.REPRESENTANTE_NOME_ENTIDADE, FrameworkMessages.BOTAO_CANCELAR});
		if (opcao < 2) {
			if (PedidoService.getInstance().countPedidosNaoTransmitidos() > 0) {
				throw new ValidationException(Messages.REMESSAESTOQUE_ERRO_DEVOLUCAO_PEDIDO_TRANSMITIDO);
			} else {
				boolean parcial = DEVOLUCAO_REPRESENTANTE == opcao;
				Vector produtoList = RemessaEstoqueService.getInstance().validaAndGetListDevolucao(parcial);
				show(new ListProdutosDevolucaoForm(parcial, produtoList));
			}
		}
    }

    private void btImprimirDevEstoqueClick() throws Exception {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.REMESSAESTOQUE_MSG_PROCESSO);
    	mb.popupNonBlocking();
    	new FechamentoEstoquePrint().run();
    	mb.unpop();
    }

    private void btImprimirInventario() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	try {
    		final RemessaEstoque remessaEstoque = (RemessaEstoque) getDomainList().items[0];
    		if (remessaEstoque == null) throw new ValidationException(Messages.REMESSAESTOQUE_MSG_NENHUM_REGISTRO_ENCONTRADO);
    		
    		new InventarioRemessaEstoquePrint(remessaEstoque.cdEmpresa, remessaEstoque.cdRepresentante).run();
    	}catch (Throwable e) {
    		UiUtil.showErrorMessage(e.getMessage());
		} finally {
			mb.unpop();
		}
    }
    
	private void btDevolveEstoqueTotalClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.REMESSAESTOQUE_MSG_CONFIRMACAO)) {
			LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.REMESSAESTOQUE_MSG_PROCESSO);
			mb.popupNonBlocking();
			try {
				CrudDbxDao.getCurrentDriver().startTransaction();
				EstoqueRepService.getInstance().devolveEstoqueTotal();
				list();
				UiUtil.showSucessMessage(Messages.REMESSAESTOQUE_MSG_SUCESSO);
			} catch (Throwable ex) {
				CrudDbxDao.getCurrentDriver().rollback();
				UiUtil.showErrorMessage(ex.getMessage());
			} finally {
				CrudDbxDao.getCurrentDriver().finishTransaction();
				mb.unpop();
			}
		}
		
	}
    

}
