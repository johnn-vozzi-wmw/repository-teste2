package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.business.service.NfDevolucaoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusNfDevolucaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListNfDevolucaoForm extends LavendereCrudListForm {

	private NfDevolucao notaDevolucao;
	private StatusNfDevolucaoComboBox cbStatusNotaDevolucao;
	private ButtonAction btAprovar;
	private ButtonAction btReprovar;
	private boolean onPopup;

	public ListNfDevolucaoForm(NfDevolucao masterDomain, boolean onPopup) throws SQLException {
        super(Messages.NFDEVOLUCAO_NOME_ENTIDADE);
        singleClickOn = true;
    	this.notaDevolucao = masterDomain;
    	this.onPopup = onPopup;
    	cbStatusNotaDevolucao = new StatusNfDevolucaoComboBox();
    	btAprovar = new ButtonAction(Messages.NFDEVOLUCAO_APROVAR, "images/ok.png");
    	btReprovar = new ButtonAction(Messages.NFDEVOLUCAO_REPROVAR, "images/cancel.png");
        constructorListContainer();
		listResizeable = false;
		ScrollPosition.AUTO_HIDE = true;

    }

	private void constructorListContainer() {
		configListContainer("NUNFDEVOLUCAO");
		listContainer = new GridListContainer(3, 2, true, false);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(2, RIGHT);
		listContainer.setColsSort(new String[][]{{Messages.NFDEVOLUCAO_NUNOTA, "NUNFDEVOLUCAO"}, {Messages.NFDEVOLUCAO_SERIENOTA, "CDSERIE"}, {Messages.NFDEVOLUCAO_DTEMISSAONOTA, "DTEMISSAO"}});
	}
	

    @Override
    protected CrudService getCrudService() throws SQLException {
        return NfDevolucaoService.getInstance();
    }

    @Override
	protected BaseDomain getDomainFilter() throws SQLException {
    	NfDevolucao nfDevolucao = new NfDevolucao();
    	nfDevolucao.cdEmpresa = notaDevolucao.cdEmpresa;
    	nfDevolucao.cdCliente = notaDevolucao.cdCliente;
    	nfDevolucao.cdRepresentante = notaDevolucao.cdRepresentante;
    	nfDevolucao.flAprovacao = cbStatusNotaDevolucao.getValue();
    	nfDevolucao.sortAtributte = sortAtributte;
    	nfDevolucao.sortAsc = sortAsc;
    	return nfDevolucao;
    }

    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	return NfDevolucaoService.getInstance().findAllByExample(getDomainFilter());
    }

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		notaDevolucao = (NfDevolucao) domain;
		return new String[] { notaDevolucao.cdSerie + "/" + notaDevolucao.nuNfDevolucao,
				StringUtil.getStringValue(notaDevolucao.dtEmissao), Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(notaDevolucao.vlNfDevolucao) };
	}

    @Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		NfDevolucao nfDevolucao = (NfDevolucao) domain;
		if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, nfDevolucao.flAprovacao)) {
			c.setBackColor(LavendereColorUtil.COR_CLIENTE_ATRASADO_BLOQUEADO_BACK);
		} else if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, nfDevolucao.flAprovacao)) {
			c.setBackColor(LavendereColorUtil.COR_PRODUTO_COMKIT_BACK);
		}

	}

    @Override
	protected void onFormStart() throws SQLException {
    	UiUtil.add(this, new LabelName(Messages.NFDEVOLUCAO_STATUS), cbStatusNotaDevolucao, getLeft(), TOP);
    	UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL);
    	UiUtil.add(barBottomContainer, btAprovar, 5);
    	UiUtil.add(barBottomContainer, btReprovar, 1);
    }
    	
    @Override
	public void visibleState() {
    	super.visibleState();
		barTopContainer.setVisible(false);
		barBottomContainer.setVisible(false);
    }

	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		return getCrudService().findByRowKey(getSelectedRowKey());
	}

	
	@Override
	public void detalhesClick() throws SQLException {
		if (listContainer != null) {
			BaseDomain domain = getCrudService().findByRowKeyDyn(getSelectedRowKey());
			domain.rowKey = getSelectedRowKey();
			if (onPopup) {
				CadNfDevolucaoWindow nfDevolucaoWindow = new CadNfDevolucaoWindow((NfDevolucao) domain, true);
				nfDevolucaoWindow.popup();
				list();
			} else {
				setBaseCrudCadForm(new CadNfDevolucaoDynForm((NfDevolucao) domain, false));
				getBaseCrudCadForm().edit(domain);
				show(getBaseCrudCadForm());
			}
		}
	}
	
    @Override
	protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btAprovar) {
				btAprovarClick();
			} else if (event.target == btReprovar) {
				btReprovarClick();
			} else if (event.target == cbStatusNotaDevolucao) {
				listContainer.setCheckable(ValueUtil.valueEquals(cbStatusNotaDevolucao.getValue(), NfDevolucao.NFDEVOLUCAO_PENDENTE));
				list();
			}
			break;
		}
    }
    
    public void btAprovarClick() throws SQLException {
    	processButtonClick(ValueUtil.VALOR_SIM);
    }

    public void btReprovarClick() throws SQLException {
    	processButtonClick(ValueUtil.VALOR_NAO);
    }

    private void processButtonClick(String status) throws SQLException {
    	if (!UiUtil.showConfirmYesNoMessage(ValueUtil.valueEquals(status, ValueUtil.VALOR_SIM) ? Messages.NFDEVOLUCAO_COMFIRM_APROVACAO : Messages.NFDEVOLUCAO_COMFIRM_REPROVACAO)) {
    		return;
    	}
        int[] checkedItems = listContainer.getCheckedItens();
        if (isSelecaoInvalida(checkedItems)) {
            UiUtil.showWarnMessage(Messages.QTMIN_CHECKED_ITEM_LIST);
            return;
        }
        updateNfDevolucaoStatus(checkedItems, status);
        list();
    }

    private boolean isSelecaoInvalida(int[] checkedItems) {
        return checkedItems.length < 1;
    }

    private void updateNfDevolucaoStatus(int[] checkedItems, String flAprovacao) throws SQLException {
        for (int checkedItemIndex : checkedItems) {
            NfDevolucao nfDevolucao = getNfDevolucaoByIndex(checkedItemIndex);

            if (nfDevolucao != null && NfDevolucao.NFDEVOLUCAO_PENDENTE.equals(nfDevolucao.flAprovacao)) {
                nfDevolucao.flAprovacao = flAprovacao;
                nfDevolucao.hrAlteracao = TimeUtil.getCurrentTimeHHMM();
                nfDevolucao.dtAlteracao = new Date();

                try {
                    NfDevolucaoService.getInstance().update(nfDevolucao, true);
                } catch (SQLException e) {
                    ExceptionUtil.handle(e);
                }
            }
        }
    }

    private NfDevolucao getNfDevolucaoByIndex(int checkedItemIndex) throws SQLException {
        return (NfDevolucao) NfDevolucaoService.getInstance().findByRowKey(listContainer.getId(checkedItemIndex));
    }

}
    


