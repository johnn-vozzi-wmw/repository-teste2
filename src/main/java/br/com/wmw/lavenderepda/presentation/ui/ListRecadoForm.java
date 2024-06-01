package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Recado;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.TipoRecado;
import br.com.wmw.lavenderepda.business.domain.UsuarioWeb;
import br.com.wmw.lavenderepda.business.service.RecadoService;
import br.com.wmw.lavenderepda.business.service.UsuarioWebService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoRecadoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListRecadoForm extends LavendereCrudListForm {

    private TipoRecadoComboBox cbTipoRecado;
	private boolean hasUsuarioRepValid;
	private Vector listUsuariosWeb;
	private Cliente clienteSelecionado;
	
	private boolean isFiltroRecado = LavenderePdaConfig.isPermiteAplicarFiltros();
	
	public EditText edDsCliente;
	public BaseButton btFiltrarCliente;
	public BaseButton btTodosClientes;
	public boolean isSelecionado;

	public static final String LABEL_TODOS = Messages.LABEL_TODOS;

    public ListRecadoForm() throws SQLException {
        super(Messages.RECADO_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadRecadoForm());
        cbTipoRecado = new TipoRecadoComboBox();
        cbTipoRecado.setID("cbTipoRecado");
        singleClickOn = true;
        if (isFiltroRecado) {
        	edDsCliente = new EditText("@@@@@@@@@@", 100);
            edDsCliente.drawBackgroundWhenDisabled = true;
            edDsCliente.setEditable(false);
            edDsCliente.setText(LABEL_TODOS);
            btFiltrarCliente = new BaseButton(UiUtil.getColorfulImage("images/alterarfiltro.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
            btTodosClientes = new BaseButton(Messages.BOTAO_TODOS);
        }
        isSelecionado = false;
        constructorListContainer();
    }
    
    public ListRecadoForm(Cliente cliente) throws SQLException {
        super(Messages.RECADO_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadRecadoForm());
        cbTipoRecado = new TipoRecadoComboBox();
        singleClickOn = true;
        clienteSelecionado = cliente;
        isSelecionado = true;
        constructorListContainer();
    }

    private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		configListContainer("DTENVIO,HRENVIO");
		listContainer = new GridListContainer(4, 2);
		listContainer.setColsSort(new String[][] {
			{Messages.RECADO_TITLE_DTENVIO, "DTENVIO,HRENVIO"},
			{Messages.RECADO_LABEL_DSASSUNTO, "DSASSUNTO"}
		});
		listContainer.showLeftImage(UiUtil.getImageAndApplyColor("images/recadolido.png", ColorUtil.baseForeColorSystem));
    	listContainer.setColPosition(3, RIGHT);
		ScrollPosition.AUTO_HIDE = true;
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return RecadoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new Recado();
    }

    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	hasUsuarioRepValid = true;
    	if (ValueUtil.isEmpty(listUsuariosWeb)) {
    		listUsuariosWeb = UsuarioWebService.getInstance().findAll();
    	}
    	
    	if (clienteSelecionado == null) {
    		return RecadoService.getInstance().getListRecadosByTipoAndStatus(cbTipoRecado.getValue(), domain);
    	}
    	
    	return RecadoService.getInstance().getListRecadosByTipoAndStatusAndClienteSelecionado(cbTipoRecado.getValue(), domain, clienteSelecionado);
    }
    
    private void btFiltroClienteClick() throws SQLException {
		Representante representanteOld = SessionLavenderePda.getRepresentante();
    	ListClienteWindow listCliente = new ListClienteWindow();
    	listCliente.popup();
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		if (SessionLavenderePda.getRepresentante().cdRepresentante != null && listCliente.cliente == null) {
    			SessionLavenderePda.setRepresentante(representanteOld);
    		}
    	}
    	if (listCliente.cliente != null) {
    		edDsCliente.setText(listCliente.cliente.getDescription());
    		clienteSelecionado = listCliente.cliente;
    	}
    }

    public void btTodosClientesClick() {
    	edDsCliente.setText(LABEL_TODOS);
    	clienteSelecionado = null;
    }

    @Override
    protected String[] getItem(Object domain) throws SQLException {
        Recado recado = (Recado) domain;
        UsuarioWeb usuarioWeb = new UsuarioWeb();
        usuarioWeb.cdUsuarioWeb = recado.cdUsuarioRemetente;
        usuarioWeb.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
        int index = listUsuariosWeb.indexOf(usuarioWeb);
        String nmUsuario = "";
        if (index != -1) {
        	nmUsuario = ((UsuarioWeb)listUsuariosWeb.items[index]).nmUsuarioWeb;
        }
		String[] item = {
			StringUtil.getStringValue(recado.dsAssunto),
            "",
            StringUtil.getStringValue(nmUsuario),
            StringUtil.getStringValue(recado.dtEnvio)
        };
        return item;
    }

    @Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    @Override
    public void initUI() {
    	super.initUI();
    	if (!hasUsuarioRepValid) {
    		UiUtil.showWarnMessage(Messages.RECADO_MSG_SEM_USUARIOREP_CADASTRADO);
    	}
    }

    @Override
    public void loadDefaultFilters() throws SQLException {
        cbTipoRecado.setSelectedIndex(0);
    }

    @Override
    protected void onFormStart() throws SQLException {
    	if (isFiltroRecado && !isSelecionado) {
    		UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), getLeft(), getNextY(), FILL - HEIGHT_GAP, UiUtil.getLabelPreferredHeight());
       		UiUtil.add(this, edDsCliente, getLeft(), AFTER, FILL - btFiltrarCliente.getPreferredWidth() - btTodosClientes.getPreferredWidth() - WIDTH_GAP_BIG);
    		UiUtil.add(this, btTodosClientes, getRight(), SAME, PREFERRED, SAME);
    		UiUtil.add(this, btFiltrarCliente, BEFORE - WIDTH_GAP, SAME, PREFERRED, SAME);
    	}
    	UiUtil.add(this, cbTipoRecado, getLeft(), getNextY() + HEIGHT_GAP_BIG);
    	if (LavenderePdaConfig.isPermiteUsuarioNormalEnviarRecado()) {
            UiUtil.add(barBottomContainer, btNovo, 5);
            UiUtil.add(this, listContainer, LEFT, cbTipoRecado.getY2() + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
    	} else {
    		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
    	}
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFiltrarCliente) {
					btFiltroClienteClick();
					list();
				} else if (event.target == btTodosClientes) {
					btTodosClientesClick();
					list();
				} else if (event.target == cbTipoRecado) {
					list();
				}
				break;
			}
		}
    }

    @Override
    public void detalhesClick() throws SQLException {
    	if (cbTipoRecado.getValue() == TipoRecado.TIPORECADO_CDCAIXA_DE_ENTRADA) {
    		((CadRecadoForm)getBaseCrudCadForm()).setInRecadoEnviado(false);
    		((CadRecadoForm)getBaseCrudCadForm()).setInRecadoRecebido(true);
    	} else {
    		((CadRecadoForm)getBaseCrudCadForm()).setInRecadoEnviado(true);
    		((CadRecadoForm)getBaseCrudCadForm()).setInRecadoRecebido(false);
    	}
    	super.detalhesClick();
    }

    @Override
    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		Recado recado = (Recado)domain;
        int tipoRecado = cbTipoRecado.getValue();
        if (tipoRecado == TipoRecado.TIPORECADO_CDCAIXA_DE_ENTRADA) {
        	if (ValueUtil.VALOR_SIM.equals(recado.flRespostaEnviada)) {
        		listContainer.setImageLeftItem(c, UiUtil.getImageAndApplyColor("images/recadorespondido.png", ColorUtil.baseForeColorSystem));
        	} else if (ValueUtil.VALOR_NAO.equals(recado.flLido)) {
            	listContainer.setImageLeftItem(c, UiUtil.getImageAndApplyColor("images/recadonaolido.png", ColorUtil.baseForeColorSystem));
            }
        } else if (tipoRecado == TipoRecado.TIPORECADO_CDCAIXA_DE_SAIDA) {
        	listContainer.setImageLeftItem(c, UiUtil.getImageAndApplyColor("images/recadoenviar.png", ColorUtil.baseForeColorSystem));
        } else if (tipoRecado == TipoRecado.TIPORECADO_CDITENS_ENVIADOS) {
        	listContainer.setImageLeftItem(c, UiUtil.getImageAndApplyColor("images/recadoenviado.png", ColorUtil.baseForeColorSystem));
        }
    }

    @Override
    public void close() throws SQLException {
    	if (LavenderePdaConfig.obrigaLeituraRecados && RecadoService.getInstance().existeRecadosNaoLidos()) {
    		UiUtil.showErrorMessage(Messages.RECADO_MSG_HARECADOSNAOLIDOS);
    	} else if (LavenderePdaConfig.obrigaRespostaRecado && RecadoService.getInstance().existeRecadosNaoRespondidos()) {
    		UiUtil.showErrorMessage(Messages.RECADO_MSG_OBRIGARESPONDERRECADO);
    	} else {
    		super.close();
    		listUsuariosWeb = null;
    	}
    }
}
