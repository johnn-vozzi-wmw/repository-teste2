package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Contato;
import br.com.wmw.lavenderepda.business.domain.ContatoPda;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.service.ContatoErpService;
import br.com.wmw.lavenderepda.business.service.ContatoPdaService;
import br.com.wmw.lavenderepda.business.service.ContatoService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListContatoForm extends LavendereCrudListForm {

	public CadContatoForm cadContatoForm;
	public CadNovoClienteForm cadNovoClienteForm;
	public Vector contatosCadastradoSemMemoria;
	public Vector contatosCadastradosSalvos;

	public ListContatoForm() throws SQLException {
		this(null);
	}

	public ListContatoForm(CadNovoClienteForm cadNovoClienteForm) throws SQLException {
		super(Messages.CONTATO_NOME_CONTATOS);
		this.cadNovoClienteForm = cadNovoClienteForm;
		cadContatoForm = new CadContatoForm(false, this);
		setBaseCrudCadForm(cadContatoForm);
		singleClickOn = true;
		configListContainer("NMCONTATO");
		listContainer = new GridListContainer(LavenderePdaConfig.padronizaListaContatoEAniversariante ? 5 : 4, 2);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColsSort(new String[][]{{Messages.CONTATO_LABEL_NMCONTATO, "NMCONTATO"}});
		listContainer.btResize.setVisible(false);
	}

	//@Override
	protected CrudService getCrudService() throws SQLException {
		return ContatoPdaService.getInstance();
	}

	//@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new Contato();
	}

	//@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Contato.sortAttr = domain.sortAtributte;
		Vector todos = new Vector();
		if (cadNovoClienteForm != null) {
			getListContatoNovoCliente(todos);
		} else {
			todos =  ContatoService.getInstance().findAllContatoByCliente(SessionLavenderePda.getCliente().cdCliente);
		}
		todos.qsort();
   		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
   			todos.reverse();
   		}
		return todos;
	}

	private void getListContatoNovoCliente(Vector todos) throws SQLException {
		if (isNovoClienteSalvo()) {
			todos.addElementsNotNull(getContatosNovoClienteSalvos().items);
		} else if (cadNovoClienteForm != null && ValueUtil.isNotEmpty(cadNovoClienteForm.contatosCadastrados) && ValueUtil.isEmpty(cadContatoForm.getContatosCadastrados())) {
			todos.addElementsNotNull(cadNovoClienteForm.contatosCadastrados.items);
		}
		if (ValueUtil.isNotEmpty(cadContatoForm.getContatosCadastrados()) && !isNovoClienteSalvo()) {
			todos.addElementsNotNull(cadContatoForm.getContatosCadastrados().items);
		}
		if (ValueUtil.isNotEmpty(todos)) {
			contatosCadastradoSemMemoria = todos;
		}
	}

	public Vector getContatosNovoClienteSalvos() throws SQLException {
		return contatosCadastradosSalvos = ContatoService.getInstance().findAllContatoByCliente(SessionLavenderePda.getCliente().cdCliente);
	}
	public boolean isNovoClienteSalvo() throws SQLException {
		NovoCliente novoCli = NovoClienteService.getInstance().getNovoClienteByCliente(SessionLavenderePda.getCliente());
		return novoCli != null ? true : false;
	}

	@Override
	protected void voltarClick() throws SQLException {
		saveContatosCadastrados();
		super.voltarClick();
	}

	private void saveContatosCadastrados() throws SQLException {
		if (cadNovoClienteForm != null && !isNovoClienteSalvo()) {
			cadNovoClienteForm.contatosCadastrados = contatosCadastradoSemMemoria;
		}
	}

	//@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	//@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		if (cadNovoClienteForm != null && !isNovoClienteSalvo()) {
			return getSelectedDomainContatoEmMemoria();
		}
		if (c.getItem(5).equals(OrigemPedido.FLORIGEMPEDIDO_PDA)) {
			return getCrudService().findByRowKeyDyn(getSelectedRowKey());
		} else {
			return ContatoErpService.getInstance().findByRowKeyDyn(getSelectedRowKey());
		}
	}

	public BaseDomain getSelectedDomainContatoEmMemoria() throws SQLException {
		int size = contatosCadastradoSemMemoria.size();
		for (int i = 0; i < size; i++) {
			String idRowKey = ((Contato) contatosCadastradoSemMemoria.items[i]).rowKey;
			if (ValueUtil.valueEquals(getSelectedRowKey(), idRowKey)) {
				return (Contato) contatosCadastradoSemMemoria.items[i];
			}
		}
		return (Contato) contatosCadastradoSemMemoria.items[0];
	}

    protected String[] getItem(Object domain) throws SQLException {
    	Contato contato = (Contato)domain;
    	String flOrigem = OrigemPedido.FLORIGEMPEDIDO_ERP;
    	if (contato instanceof ContatoPda) {
    		flOrigem = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	} 
    	//--
    	String[] item;
		boolean padronizaLista = LavenderePdaConfig.padronizaListaContatoEAniversariante;
		if (cadNovoClienteForm != null && contato != null) {
			verificaItensDinamicosCadastradoNovoCliente(contato);
		}
		item = new String[]{
				StringUtil.getStringValue(contato.nmContato),
				StringUtil.getStringValue(contato.dsCargo),
				StringUtil.getStringValue(padronizaLista ? contato.dsEmail : contato.nuFone),
				StringUtil.getStringValue(padronizaLista ? contato.nuFone : contato.dsEmail),
				StringUtil.getStringValue(contato.dtAniversario),
				StringUtil.getStringValue(flOrigem)};
		return item;
    }

    private void verificaItensDinamicosCadastradoNovoCliente(Contato contato) {
		if (ValueUtil.isEmpty(contato.nmContato)) {
			contato.nmContato = StringUtil.getStringValue(contato.getHashValuesDinamicos().get("NMCONTATO"));
		}
		if (ValueUtil.isEmpty(contato.dsEmail)) {
			contato.dsEmail = StringUtil.getStringValue(contato.getHashValuesDinamicos().get("DSEMAIL"));
		}
		if (ValueUtil.isEmpty(contato.nuFone)) {
			contato.nuFone = StringUtil.getStringValue(contato.getHashValuesDinamicos().get("NUFONE"));
		}
	}
    
	//@Override
	protected void onFormStart() throws SQLException {
		if (LavenderePdaConfig.isHabilitaCadastroModuloContatoCliente()
			&& isPermiteInserirContatoNovoCliente()) {
			UiUtil.add(barBottomContainer, btNovo , 5);
		}
    	UiUtil.add(this, new LabelContainer(SessionLavenderePda.getCliente().toString()), LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	UiUtil.add(this, listContainer, LEFT, AFTER, FILL, FILL - barBottomContainer.getHeight());
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {}

	private boolean isPermiteInserirContatoNovoCliente() throws SQLException {
		Cliente cliente = SessionLavenderePda.getCliente();
		NovoCliente novoCli = NovoClienteService.getInstance().getNovoClienteByCliente(cliente);
		return (novoCli != null && ((LavenderePdaConfig.permiteContatosNovoClienteTransmitido() && novoCli.isEnviadoServidor()) || !novoCli.isEnviadoServidor())) || (novoCli == null && cliente.isNovoCliente()) || !cliente.isNovoCliente();
	}

}