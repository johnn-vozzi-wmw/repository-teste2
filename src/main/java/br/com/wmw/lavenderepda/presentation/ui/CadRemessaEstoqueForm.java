package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadRemessaEstoqueForm extends BaseCrudCadForm {

    private LabelValue lvNuNotaRemessa;
    private LabelValue lvNuSerieRemessa;
    private LabelValue lvCdLocalEstoque;
    private LabelValue lvDtRemessa;
    private LabelValue lvHrRemessa;
    private LabelValue lvFlStatus;
    private LabelValue lvTipoRemessa;
    private GridListContainer listContainer;
    protected ButtonAction btDesbloquearRemessa;
    
    public CadRemessaEstoqueForm() throws SQLException {
        super(Messages.REMESSAESTOQUE_TITULO_CADASTRO);
        lvNuNotaRemessa = new LabelValue();
        lvNuSerieRemessa = new LabelValue();
        lvCdLocalEstoque = new LabelValue();
        lvDtRemessa = new LabelValue();
        lvHrRemessa = new LabelValue();
        listContainer = new GridListContainer(2, 1);
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	lvTipoRemessa = new LabelValue();
        } else {
        	lvFlStatus = new LabelValue();
        }
        listContainer = new GridListContainer(4, 2);
        listContainer.setBarTopSimple();
        listContainer.setColPosition(1, RIGHT);
        listContainer.setColPosition(3, RIGHT);
        btDesbloquearRemessa = new ButtonAction(Messages.REMESSAESTOQUE_LABEL_DESBLOQUEAR, "images/desbloquear.png");
    }

    //-----------------------------------------------

    
    @Override
    public String getEntityDescription() {
    	return Messages.REMESSAESTOQUE_TITULO_CADASTRO;
    }

    @Override
    protected CrudService getCrudService() {
        return RemessaEstoqueService.getInstance();
    }
    
    @Override
    protected BaseDomain createDomain() {
        return new RemessaEstoque();
    }
    
    @Override
    protected BaseDomain screenToDomain() throws SQLException {
        RemessaEstoque remessaEstoque = ((RemessaEstoque) getDomain());
        return remessaEstoque;
    }
    
    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        RemessaEstoque remessaEstoque = (RemessaEstoque) domain;
        lvNuNotaRemessa.setValue(remessaEstoque.nuNotaRemessa);
        lvNuSerieRemessa.setValue(remessaEstoque.nuSerieRemessa);
        lvCdLocalEstoque.setValue(remessaEstoque.cdLocalEstoque);
        lvDtRemessa.setValue(remessaEstoque.dtRemessa);
        lvHrRemessa.setValue(remessaEstoque.hrRemessa);
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	lvTipoRemessa.setValue(remessaEstoque.getDsTipoRemessa());
        } else {
        	lvFlStatus.setValue(remessaEstoque.getDsStatus(null));
        }
        list();
    }
    
    @Override
    protected void clearScreen() {
        lvNuNotaRemessa.setText("");
        lvNuSerieRemessa.setText("");
        lvCdLocalEstoque.setText("");
        lvDtRemessa.setText("");
        lvHrRemessa.setText("");
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	lvTipoRemessa.setText("");
        } else {
        	lvFlStatus.setText("");
        }
    }
    
    
    @Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
    	btSalvar.setVisible(false);
    	btExcluir.setVisible(false);
    	btDesbloquearRemessa.setVisible(!((RemessaEstoque) getDomain()).isEstoqueLiberado());
    }
    
    //-----------------------------------------------
    
    @Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_LABEL_NUNOTAREMESSA), lvNuNotaRemessa, getLeft(), getNextY());
        UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_LABEL_NUSERIEREMESSA), lvNuSerieRemessa, getLeft(), getNextY());
        UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_LABEL_CDLOCALESTOQUE), lvCdLocalEstoque, getLeft(), getNextY());
        UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_LABEL_DTREMESSA), lvDtRemessa, getLeft(), getNextY());
        UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_LABEL_HRREMESSA), lvHrRemessa, getLeft(), getNextY());
        if (!LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_LABEL_STATUS), lvFlStatus, getLeft(), getNextY());
        	UiUtil.add(barBottomContainer, btDesbloquearRemessa, 5);
        } else {
        	UiUtil.add(this, new LabelName(Messages.REMESSAESTOQUE_TIPO_REMESSA), lvTipoRemessa, getLeft(), getNextY());
        }
        UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - barBottomContainer.getHeight());
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btDesbloquearRemessa) {
	    			DesbloqueioRemessaWindow desbloqueioRemessaWindow = new DesbloqueioRemessaWindow((RemessaEstoque) getDomain());
	    			desbloqueioRemessaWindow.popup();
	    			if (desbloqueioRemessaWindow.desbloqueou) {
	    				UiUtil.showInfoMessage(Messages.REMESSAESTOQUE_LABEL_DESBLOQUEIO_SUCESSO);
	    				btDesbloquearRemessa.setVisible(false);
	    				lvFlStatus.setValue(Messages.REMESSAESTOQUE_LABEL_LIBERADA);
	    				updateCurrentRecordInList(getDomain());
	    			}
	    		}
	    		break;
			}	
    	}
    }

    
    public void list() throws SQLException {
		if (listContainer != null) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			int listSize = 0;
			Vector domainList = null;
			try {
				listContainer.removeAllContainers();
				listContainer.uncheckAll();
				domainList = getEstoqueList();
				listSize = domainList.size();
				Container[] all = new Container[listSize];
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					for (int i = 0; i < listSize; i++) {
						if (i % 250 == 0)
							VmUtil.executeGarbageCollector();
						all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
						domain = (BaseDomain) domainList.items[i];
						c.id = domain.getRowKey();
						c.setItens(getItem(domain));
					}
					listContainer.addContainers(all);
				}

			} finally {
				domainList = null;
				msg.unpop();
			}
		}
    }

	private String[] getItem(BaseDomain domain) {
		Estoque estoque = (Estoque) domain;
        String[] item = {
                estoque.getProduto().toString(),
                " ",
                Messages.REMESSAESTOQUE_LABEL_ESTOQUE +  " " + StringUtil.getStringValueToInterface(estoque.qtEstoque),
                getEstoqueRemessa(estoque)
                };
        return item;
	}

	private String getEstoqueRemessa(Estoque estoque) {
		if (estoque.qtEstoqueRemessa > 0) {
			return  Messages.REMESSAESTOQUE_LABEL_ESTOQUE_REMESSA +  " " + StringUtil.getStringValueToInterface(estoque.qtEstoqueRemessa);
		}
		return " " + " ";
	}

	private Vector getEstoqueList() throws SQLException {
		String cdLocalEstoque = ((RemessaEstoque) getDomain()).cdLocalEstoque;
		return EstoqueService.getInstance().findEstoqueByCdLocal(cdLocalEstoque);
	}
    
}
