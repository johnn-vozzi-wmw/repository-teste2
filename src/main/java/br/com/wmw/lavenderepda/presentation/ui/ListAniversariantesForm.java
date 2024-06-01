package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Contato;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import br.com.wmw.lavenderepda.business.domain.ContatoPda;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ContatoErpService;
import br.com.wmw.lavenderepda.business.service.ContatoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.io.IOException;
import totalcross.phone.Dial;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListAniversariantesForm extends LavendereCrudListForm {

    public ListAniversariantesForm() throws SQLException {
        super(Messages.ANIVERSARIANTES_NOME_ENTIDADE);
        constructorListContainer();
        setBaseCrudCadForm(new CadContatoForm(true, null));
        singleClickOn = true;
    }

    public void constructorListContainer() {
    	configListContainer("DTANIVERSARIO");
    	listContainer = new GridListContainer(LavenderePdaConfig.padronizaListaContatoEAniversariante ? 7 : 5, 2);
    	listContainer.setColPosition(1, RIGHT);
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColsSort(new String[][] {{Messages.ANIVERSARIANTES_DATA_ANIVERSARIO, "DTANIVERSARIO"}, {Messages.ANIVERSARIANTES_CONTATO, "NMCONTATO"}});
    	listContainer.setUseBtHidden(Messages.ANIVERSARIANTES_SEM_NUMERO);
    	listContainer.btResize.setVisible(false);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ContatoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new ContatoPda();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Vector domainList = ((ContatoService)getCrudService()).findAllContatoByDtAniversario(domain);
    	if ("DTANIVERSARIO".equals(domain.sortAtributte)) {
    		Contato.sortAttr = domain.sortAtributte;
    		SortUtil.qsortInt(domainList.items, 0, domainList.size() - 1, true);
    		orderListaMesCorrente(domainList);
    		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
    			domainList.reverse();
    		}
    	} else {
    		Contato.sortAttr = domain.sortAtributte;
    		domainList.qsort();
    		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
    			domainList.reverse();
    		}
    	}
    	return domainList;
    }
    
    private void orderListaMesCorrente(Vector aniversarianteList) {
    	Vector aniversarianteProximoMesList = new Vector();
    	for (int i = 0; i < aniversarianteList.size(); i++) {
    		Contato contato =  (Contato) aniversarianteList.items[i];
    		if (contato != null) {
    			if (contato.dtAniversario != null && DateUtil.getCurrentDate().getMonth() <= contato.dtAniversario.getMonth()) {
    				aniversarianteProximoMesList.addElement(contato);
    				aniversarianteList.removeElementAt(i);
    				i--;
    			}
    		}
		}
    	for (int i = 0; i < aniversarianteProximoMesList.size(); i++) {
    		Contato contato =  (Contato) aniversarianteProximoMesList.items[i];
    		aniversarianteList.addElement(contato);
		}
    }

	//@Override
	protected String[] getItem(Object domain) throws SQLException {
		Contato contato = (Contato)domain;
		String flOrigem;
		if (contato instanceof ContatoPda) {
			flOrigem = OrigemPedido.FLORIGEMPEDIDO_PDA;
		} else {
			flOrigem = OrigemPedido.FLORIGEMPEDIDO_ERP;
		}
		//--
		String[] item;
		boolean padronizaLista = LavenderePdaConfig.padronizaListaContatoEAniversariante;
		String dsCliente = ClienteService.getInstance().getDescriptionWithKey(contato.cdEmpresa, contato.cdRepresentante, contato.cdCliente);
		item = new String[] {
				StringUtil.getStringValue(contato.nmContato),
				StringUtil.getStringValue(padronizaLista ? contato.dsCargo : contato.dtAniversario),
				StringUtil.getStringValue(contato.dsEmail),
				StringUtil.getStringValue(contato.nuFone),
				StringUtil.getStringValue(padronizaLista ? contato.dtAniversario : dsCliente),
				StringUtil.getStringValue(""),
				StringUtil.getStringValue(dsCliente),
				StringUtil.getStringValue(flOrigem),
				StringUtil.getStringValue(contato.nuCelular)
		};
		return item;
	}

    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, listContainer);
        listContainer.setRect(LEFT, AFTER, FILL, FILL, barTopContainer);
    }

    //@Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	if (listContainer.getValueFromContainer(listContainer.getSelectedIndex(), 7).equals(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
    		Contato contato = new Contato();
    		if (SessionLavenderePda.isUsuarioSupervisor()) {
    			contato = getContatoSelecionado(contato, OrigemPedido.FLORIGEMPEDIDO_PDA);
    		} else {
    			contato = (Contato) ContatoService.getInstance().findByRowKeyDyn(getSelectedRowKey());
    		}
    		SessionLavenderePda.setCliente(ClienteService.getInstance().getCliente(contato.cdEmpresa, contato.cdRepresentante, contato.cdCliente));
    		return contato;
    	} else {
    		ContatoErp contatoErp = new ContatoErp();
    		if (SessionLavenderePda.isUsuarioSupervisor()) {
    			contatoErp = (ContatoErp) getContatoSelecionado(contatoErp, OrigemPedido.FLORIGEMPEDIDO_ERP);
    		} else {
    			contatoErp = (ContatoErp) ContatoErpService.getInstance().findByRowKeyDyn(getSelectedRowKey());
    		}
    		SessionLavenderePda.setCliente(ClienteService.getInstance().getCliente(contatoErp.cdEmpresa, contatoErp.cdRepresentante, contatoErp.cdCliente));
    		return contatoErp;
    	}
    }

	private Contato getContatoSelecionado(Contato contato, String origemContato) throws SQLException {
		String[] rowKey = getSelectedRowKey().split(";");
		if (rowKey.length > 0) {
			contato.cdEmpresa = rowKey[0];
			contato.cdCliente = rowKey[2];
			contato.cdContato = rowKey[3];
		}
		if (origemContato.equals(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
			Vector contatoList = ContatoService.getInstance().findAllByExampleDyn(contato);
			if (contatoList.size() > 0) {
				contato = (Contato) contatoList.items[0];
			}
		} else {
			Vector contatoErpList = ContatoErpService.getInstance().findAllByExampleDyn(contato);
			if (contatoErpList.size() > 0) {
				contato = (ContatoErp) contatoErpList.items[0];
			}
		}
		return contato;
	}

	protected void configureContainerBtHidden(Item containerItem) throws SQLException {
		String nuFone = ValueUtil.getValidNumbers(containerItem.getItem(3));
		String nuCelular = ValueUtil.getValidNumbers(containerItem.getItem(8));
		if (ValueUtil.isNotEmpty(nuFone) || ValueUtil.isNotEmpty(nuCelular)) {
			containerItem.setBtHiddenConfigs(" " + Messages.BOTAO_LIGAR + " ", Color.getRGB(154, 219, 115), Color.getRGB(60, 60, 60));
		} else {
			containerItem.setBtHiddenConfigs(" " + Messages.ANIVERSARIANTES_SEM_NUMERO + " ", Color.BRIGHT, Color.BLACK);
		}
	}

	public void btHidedClickInList() throws SQLException {
		int selectedIndex = listContainer.getSelectedIndex();
		String nuFone = ValueUtil.getValidNumbers(listContainer.getValueFromContainer(selectedIndex, 3));
		String nuCelular = ValueUtil.getValidNumbers(listContainer.getValueFromContainer(selectedIndex, 8));
		if (ValueUtil.isNotEmpty(nuFone) || ValueUtil.isNotEmpty(nuCelular)) {
			if (VmUtil.isAndroid()) {
				if (UiUtil.showConfirmYesNoMessage(Messages.MSG_DESEJA_LIGACAO)) {
					fazerLigacao();
				}
			} else {
				fazerLigacao();
			}
		}
	}


	private void fazerLigacao() {
		String nuLigacao = getNumeroLigacao();
		try {
			Dial.number(nuLigacao);
		} catch (IOException e) {
			UiUtil.showErrorMessage(e.getMessage());
		}
	}

	private String getNumeroLigacao() {
		int selectedIndex = listContainer.getSelectedIndex();
		String nuLigacao = listContainer.getValueFromContainer(selectedIndex, 3);
		if (ValueUtil.isNotEmpty(nuLigacao)) {
			return ValueUtil.getValidNumbers(nuLigacao);
		} else {
			return ValueUtil.getValidNumbers(listContainer.getValueFromContainer(selectedIndex, 8));
		}
	}

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

    public int size() {
    	return listContainer.size();
    }

}