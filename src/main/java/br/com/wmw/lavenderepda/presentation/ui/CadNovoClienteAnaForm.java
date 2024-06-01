package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberMask;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoClienteAna;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import br.com.wmw.lavenderepda.business.service.FotoNovoClienteService;
import br.com.wmw.lavenderepda.business.service.NovoCliEnderecoService;
import br.com.wmw.lavenderepda.business.service.NovoClienteAnaService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.StatusAnaliseClienteService;
import br.com.wmw.lavenderepda.business.service.SupervisorRepService;
import br.com.wmw.lavenderepda.business.service.TipoAnaliseClienteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPessoaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderNovoClienteWindow;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class CadNovoClienteAnaForm extends BaseLavendereCrudPersonCadForm {

	private static int TABPANEL_NOVOCLIENTEANA;
	private static int TABPANEL_NOVOCLIENTE;
	private static int TABPANEL_NOVOCLIENDERECO;
	
	private EditNumberMask edNuCnpj;
    private EditNumberMask edNuCpf;
    private TipoPessoaComboBox cbTipoPessoa;
    private RepresentanteSupervComboBox cbRepresentantesSupervisor;
    private GridListContainer listGridNovoCliEndereco;
    private EditMemo edDsObservacao;
    private LabelValue lvTipoAnalise;
    private EditNumberFrac edVlLimite;
    private LabelValue lvStatus;
    private LabelValue lvCondCom;
    private LabelValue lvPermiteMultiplosEnderecos;
    private LabelValue lvDtAnalise; 
    private LabelValue lvUsuario;
    private LabelValue lvAviso;
    public String sortAtributteListSecundaria = null;
    public String sortAscListSecundaria = ValueUtil.VALOR_SIM;
    private ButtonAction btFoto;
    private ButtonAction btEditar;
    
    public CadNovoClienteAnaForm() throws SQLException {
        super(Messages.NOVOCLIENTEANA_TITULO_CADASTRO);
        edNuCnpj = new EditNumberMask("##.###.###/####-##");
        edNuCpf = new EditNumberMask("###.###.###-##");
        cbTipoPessoa = new TipoPessoaComboBox();
        cbTipoPessoa.setEditable(false);
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor = new RepresentanteSupervComboBox();
        	cbRepresentantesSupervisor.setEditable(false);
        }
        listGridNovoCliEndereco = new GridListContainer(4, 2);
        listGridNovoCliEndereco.setColsSort(new String[][] {{Messages.NOVOCLIENDERECO_LABEL_LOGRADOURO, NovoCliEndereco.NMCOLUNA_DSLOGRADOURO}, {Messages.NOVOCLIENDERECO_LABEL_BAIRRO, NovoCliEndereco.NMCOLUNA_DSBAIRRO}, {Messages.NOVOCLIENDERECO_LABEL_CIDADE, NovoCliEndereco.NMCOLUNA_DSCIDADE}, {Messages.NOVOCLIENDERECO_LABEL_UF, NovoCliEndereco.NMCOLUNA_CDUF}});
        listGridNovoCliEndereco.atributteSortSelected = NovoCliEndereco.NMCOLUNA_DSLOGRADOURO;
        listGridNovoCliEndereco.sortAsc = sortAscListSecundaria;
        listGridNovoCliEndereco.setColPosition(1, RIGHT);
        listGridNovoCliEndereco.setColPosition(3, RIGHT);
        btFoto = new ButtonAction(Messages.LABEL_FOTO, "images/camera.png");
        btEditar = new ButtonAction(Messages.LABEL_EDITAR_NOVOCLI, "images/editarNovoCliente.png");
        edDsObservacao = new EditMemo("@@@@@@@@@@", 6, 4000);
    	edDsObservacao.drawDots = false;
    	edDsObservacao.setEditable(false);
    	lvTipoAnalise = new LabelValue(); 
    	edVlLimite = new EditNumberFrac("9999999999", 9);
    	edVlLimite.setEditable(false);
    	lvStatus = new LabelValue("");      
    	lvCondCom = new LabelValue("");    
    	lvPermiteMultiplosEnderecos = new LabelValue("");
    	lvDtAnalise = new LabelValue("");   
    	lvUsuario = new LabelValue("");
    	lvAviso = new LabelValue("");     
    	lvAviso.autoSplit = true;
    }

    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() {
    	return NovoClienteService.getInstance();
    }
    
    //@Override
    protected BaseDomain createDomain() {
    	return new NovoCliente();
    }
    
    protected void addTabsFixas(Vector tableTitles) throws SQLException {
    	super.addTabsFixas(tableTitles);
    	TABPANEL_NOVOCLIENTEANA = 0;
    	TABPANEL_NOVOCLIENTE = 1;
    	tableTitles.insertElementAt(Messages.NOVOCLIENTEANA_NOME_ENTIDADE, TABPANEL_NOVOCLIENTEANA);
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    		int indexNovoCliente = tableTitles.indexOf(Messages.NOVOCLIENTE_ABA_CADASTRO);
    		if (indexNovoCliente != -1) {
    			tableTitles.removeElement(Messages.NOVOCLIENTE_ABA_CADASTRO);
    		}
    		if (tableTitles.size() == 1) {
    			tableTitles.insertElementAt(Messages.NOVOCLIENTE_ABA_CADASTRO, TABPANEL_NOVOCLIENTE);
    		}
    		int indexNovoCliEndereco = tableTitles.indexOf(Messages.NOVOCLIENTE_ABA_ENDERECO);
    		if (indexNovoCliEndereco != -1) {
    			tableTitles.removeElement(Messages.NOVOCLIENTE_ABA_ENDERECO);
    		}
    		TABPANEL_NOVOCLIENDERECO = tableTitles.size();
    		tableTitles.insertElementAt(Messages.NOVOCLIENTE_ABA_ENDERECO, TABPANEL_NOVOCLIENDERECO);
    	}
    }

    private NovoClienteAna getNovoClienteAna() throws SQLException {
    	if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
    		Prospect prospect = (Prospect) getDomain();
    		NovoClienteAnaService.getInstance().loadNovoClienteAna(prospect);
    		return prospect.novoClienteAna;
		}
		NovoCliente novoCliente = (NovoCliente) getDomain();
		NovoClienteAnaService.getInstance().loadNovoCliAna(novoCliente);
		return novoCliente.novoClienteAna;
	}
    
    @Override
    protected void addComponentesFixosInicio() throws SQLException {
		NovoClienteAna novoClienteAna = getNovoClienteAna();
		String cdRepresentante = LavenderePdaConfig.isValidaCadastroDuasEtapas() ? ((Prospect)getDomain()).cdRepresentante : ((NovoCliente)getDomain()).cdRepresentante;
    	Container containerAna = tabDinamica.getContainer(TABPANEL_NOVOCLIENTEANA);
    	int y = TOP;
    	if (novoClienteAna.isCnpjRecorrente() || novoClienteAna.isNaoPossuiCobertura()) {
        	lvAviso.setText(novoClienteAna.isCnpjRecorrente() ? Messages.NOVOCLIENTEANA_LABEL_LABEL_CNPJ_DUPLICADO : Messages.NOVOCLIENTEANA_LABEL_LABEL_NAOPOSSUICOBERTURA);
        	lvAviso.setForeColor(novoClienteAna.isCnpjRecorrente() ? LavendereColorUtil.COR_LABEL_CNPJ_DUPLICADO : LavendereColorUtil.COR_LABEL_ERRO_CAD_CIDADEUF);
			UiUtil.add(containerAna, lvAviso, getLeft(), TOP + HEIGHT_GAP);
			UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_CDTIPOANALISE), lvTipoAnalise, getLeft(), getNextY());
    	} else {
    		UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_CDTIPOANALISE), lvTipoAnalise, getLeft(), TOP + HEIGHT_GAP);
    	}
    	UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_VLLIMITEAPROVADO), edVlLimite, getLeft(), getNextY());
    	UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_CDSTATUSANALISE), lvStatus, getLeft(), getNextY());
    	UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_CDCONDICAOCOMERCIAL), lvCondCom, getLeft(), getNextY());
    	if (ClienteService.getInstance().isPermiteMultEnd()) {
    		UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_FLPERMITEMULTIPLOSENDERECOS), lvPermiteMultiplosEnderecos, getLeft(), getNextY());
    	}
    	UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_DTEDICAOUSUARIO), lvDtAnalise, getLeft(), getNextY());
    	UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_CDUSUARIO), lvUsuario, getLeft(), getNextY());
    	UiUtil.add(containerAna, new LabelName(Messages.NOVOCLIENTEANA_LABEL_DSOBSERVACAO), edDsObservacao, getLeft(), getNextY());
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    		int indexNovoCliente = tabDinamica.getIndexTab(Messages.NOVOCLIENTE_ABA_CADASTRO); 
    		Container container = indexNovoCliente != -1 ? tabDinamica.getContainer(TABPANEL_NOVOCLIENDERECO) : tabDinamica.getContainer(TABPANEL_NOVOCLIENTE);
    		if (SessionLavenderePda.isUsuarioSupervisor()) {
    			if (SupervisorRepService.getInstance().isRepresentanteValido(cdRepresentante) ) {
    				UiUtil.add(container, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentantesSupervisor, getLeft(), y);
    				y = AFTER;
    			}
    		}
    		UiUtil.add(container, new LabelName(Messages.NOVOCLIENTE_LABEL_TIPOPESSOA), cbTipoPessoa, getLeft(), y + HEIGHT_GAP);
	        UiUtil.add(container, new LabelName(Messages.NOVOCLIENTE_LABEL_NUCNPJ), edNuCnpj, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(container, edNuCpf, SAME, SAME);
	    	UiUtil.add(tabDinamica.getContainer(TABPANEL_NOVOCLIENDERECO), listGridNovoCliEndereco, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
    	} else {
    		if (SessionLavenderePda.isUsuarioSupervisor()) {
    			if (SupervisorRepService.getInstance().isRepresentanteValido(cdRepresentante) ) {
    				UiUtil.add(tabDinamica.getContainer(TABPANEL_NOVOCLIENTE), new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentantesSupervisor, getLeft(), y);
    				y = AFTER;
    			}
    		}
    		UiUtil.add(tabDinamica.getContainer(TABPANEL_NOVOCLIENTE), new LabelName(Messages.NOVOCLIENTE_LABEL_TIPOPESSOA), cbTipoPessoa, getLeft(), y + HEIGHT_GAP);
	        UiUtil.add(tabDinamica.getContainer(TABPANEL_NOVOCLIENTE), new LabelName(Messages.NOVOCLIENTE_LABEL_NUCNPJ), edNuCnpj, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(tabDinamica.getContainer(TABPANEL_NOVOCLIENTE), edNuCpf, SAME, SAME);
    	}
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	super.domainToScreen(domain);
    	if (LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			prospectToScreen((Prospect) domain);
		} else {
			novoClienteToScreen((NovoCliente) domain);
		}
	}

	private void novoClienteToScreen(NovoCliente novoCli) throws SQLException {
		cbTipoPessoa.setValue(novoCli.flTipoPessoa);
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			if (SupervisorRepService.getInstance().isRepresentanteValido(novoCli.cdRepresentante) ) {
				cbRepresentantesSupervisor.setValue(novoCli.cdRepresentante);
			}
		}
		String nuCnpjCpf = LavenderePdaConfig.gravaCpfCnpjNovoClienteSemSeparadores ? novoCli.nuCnpj : ValueUtil.getValidNumbers(novoCli.nuCnpj);
		if (ValueUtil.isNotEmpty(nuCnpjCpf)) {
			if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
				edNuCpf.setValue(nuCnpjCpf);
				edNuCpf.setVisible(true);
				edNuCnpj.setVisible(false);
			} else {
				edNuCnpj.setValue(nuCnpjCpf);
				edNuCnpj.setVisible(true);
				edNuCpf.setVisible(false);
			}
		}
		edDsObservacao.setText(StringUtil.getStringValue(novoCli.novoClienteAna.dsObservacao));
		lvTipoAnalise.setText(TipoAnaliseClienteService.getInstance().getTipoAnalise(novoCli.novoClienteAna.cdTipoAnalise));
		edVlLimite.setText(StringUtil.getStringValueToInterface(novoCli.novoClienteAna.vlLimiteAprovado));
		lvStatus.setText(StatusAnaliseClienteService.getInstance().getDsStatusAnaliseCliente(novoCli.novoClienteAna.cdStatusAnalise));
		lvCondCom.setText(StringUtil.getStringValue(CondicaoComercialService.getInstance().getCondicaoComercial(novoCli.novoClienteAna.cdCondicaoComercial)));
		lvPermiteMultiplosEnderecos.setText(StringUtil.getStringValueFromFlboolean(novoCli.novoClienteAna.flPermiteMultiplosEnderecos));
		lvDtAnalise.setText(StringUtil.getStringValue(novoCli.novoClienteAna.dtEdicaoUsuario));
		lvUsuario.setText(StringUtil.getStringValue(novoCli.novoClienteAna.nmUsuarioEdicao));
		carregaNovoCliEndereco(false);
	}

	private void prospectToScreen(Prospect prospect) throws SQLException {
		cbTipoPessoa.setValue(prospect.flTipoPessoa);
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			if (SupervisorRepService.getInstance().isRepresentanteValido(prospect.cdRepresentante) ) {
				cbRepresentantesSupervisor.setValue(prospect.cdRepresentante);
			}
		}
		String nuCnpjCpf = LavenderePdaConfig.ignoraSeparadoresProspectCpfCnpj ? prospect.nuCnpj : ValueUtil.getValidNumbers(prospect.nuCnpj);
		if (ValueUtil.isNotEmpty(nuCnpjCpf)) {
			if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
				edNuCpf.setValue(nuCnpjCpf);
				edNuCpf.setVisible(true);
				edNuCnpj.setVisible(false);
			} else {
				edNuCnpj.setValue(nuCnpjCpf);
				edNuCnpj.setVisible(true);
				edNuCpf.setVisible(false);
			}
		}
		edDsObservacao.setText(StringUtil.getStringValue(prospect.novoClienteAna.dsObservacao));
		lvTipoAnalise.setText(TipoAnaliseClienteService.getInstance().getTipoAnalise(prospect.novoClienteAna.cdTipoAnalise));
		edVlLimite.setText(StringUtil.getStringValueToInterface(prospect.novoClienteAna.vlLimiteAprovado));
		lvStatus.setText(StatusAnaliseClienteService.getInstance().getDsStatusAnaliseCliente(prospect.novoClienteAna.cdStatusAnalise));
		lvCondCom.setText(StringUtil.getStringValue(CondicaoComercialService.getInstance().getCondicaoComercial(prospect.novoClienteAna.cdCondicaoComercial)));
		lvPermiteMultiplosEnderecos.setText(StringUtil.getStringValueFromFlboolean(prospect.novoClienteAna.flPermiteMultiplosEnderecos));
		lvDtAnalise.setText(StringUtil.getStringValue(prospect.novoClienteAna.dtEdicaoUsuario));
		lvUsuario.setText(StringUtil.getStringValue(prospect.novoClienteAna.nmUsuarioEdicao));
	}

	private void carregaNovoCliEndereco(boolean enderecoExcluido) throws SQLException {
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    		Vector domainList = null;
    		int listSize = 0;
	    	listGridNovoCliEndereco.removeAllContainers();
	    	domainList = getDomainListNovoCliEndereco();
	    	listSize = domainList.size();
			Container[] all = new Container[listSize];
			if (listSize > 0) {
				BaseListContainer.Item c;
				BaseDomain domain;
				for (int i = 0; i < listSize; i++) {
			        all[i] = c = new BaseListContainer.Item(listGridNovoCliEndereco.getLayout());
			        domain = (BaseDomain)domainList.items[i];
			        c.id = domain.getRowKey();
			        c.setItens(getItem(domain));
			        domain = null;
				}
				listGridNovoCliEndereco.addContainers(all);
			}
    	}
    }

    private String[] getItem(Object domain) {
    	NovoCliEndereco novoCliEndereco = (NovoCliEndereco) domain;
        String[] item = {
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("DSLOGRADOURO")),
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("DSBAIRRO")),
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("DSCIDADE")),
        		StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get("CDUF"))};
        return item;
    }
    
    private Vector getDomainListNovoCliEndereco() throws SQLException {
    	NovoCliente domain = (NovoCliente) getDomain();
    	NovoCliEndereco novoCliEnderecoFilter = (NovoCliEndereco) getDomainFilterSortableNovoCliEndereco();
    	if (ValueUtil.isEmpty(domain.novoCliEnderecoList)) {
    		novoCliEnderecoFilter.cdEmpresa = domain.cdEmpresa;
    		novoCliEnderecoFilter.cdRepresentante = domain.cdRepresentante;
    		novoCliEnderecoFilter.cdNovoCliente = domain.cdNovoCliente;
    		novoCliEnderecoFilter.flOrigemNovoCliente = domain.flOrigemNovoCliente;
    		domain.novoCliEnderecoList = NovoCliEnderecoService.getInstance().findAllByExampleDyn(novoCliEnderecoFilter);
    	}
		NovoCliEndereco.sortAttr = novoCliEnderecoFilter.sortAtributte;
		domain.novoCliEnderecoList.qsort();
		if (novoCliEnderecoFilter.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
			domain.novoCliEnderecoList.reverse();
   		}
		return domain.novoCliEnderecoList;
    }
    
    
    private BaseDomain getDomainFilterSortableNovoCliEndereco() {
    	NovoCliEndereco novoCliEndereco = new NovoCliEndereco();
    	novoCliEndereco.sortAtributte = sortAtributteListSecundaria;
    	novoCliEndereco.sortAsc = sortAscListSecundaria;
    	return novoCliEndereco;
    }
    
    @Override
    public void edit(BaseDomain domain) throws SQLException {
    	super.edit(domain);
    	if (!LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			NovoCliente novoCliente =  (NovoCliente) domain;
			if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
				novoCliente.setFotoNovoClienteList(FotoNovoClienteService.getInstance().findAllFotoNovoClienteByNovoCliente(novoCliente));
			}
		}
    }
    
    @Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	setEnabled(false);
    }
    
    
    //@Override
    protected void clearScreen() throws SQLException {
    	super.clearScreen(); 
    	edNuCnpj.setValue("");
     	edNuCpf.setValue("");
    }
    
    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
    	edNuCnpj.setEditable(false);
	    edNuCpf.setEditable(false);
    }
    
    //-----------------------------------------------
    
    //@Override
    protected void onFormStart() throws SQLException {
    	super.onFormStart();
    	if (!LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
			if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
				UiUtil.add(barBottomContainer, btFoto, 1);
				UiUtil.add(barBottomContainer, btEditar, 5);
			} else {
				NovoClienteAna novoClienteAna = getNovoClienteAna();
				if (novoClienteAna != null && StatusAnaliseClienteService.getInstance().isStatusAnaliseClientePermiteEdicao(novoClienteAna.cdStatusAnalise)){
					UiUtil.add(barBottomContainer, btEditar, 5);
				}
			}
		}
    }
    
    @Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
    	btSalvar.setVisible(false);
    	btExcluir.setVisible(false);
    	btEditar.setVisible(StatusAnaliseClienteService.getInstance().isStatusAnaliseClientePermiteEdicao(getNovoClienteAna().cdStatusAnalise));
    }


	@Override
	protected String getDsTable() throws SQLException {
		return LavenderePdaConfig.isValidaCadastroDuasEtapas() ? Prospect.TABLE_NAME : NovoCliente.TABLE_NAME;
	}
	
	private NovoCliente getNovoCliente() throws SQLException {
    	return (NovoCliente) getDomain();
    }

    private void editaEnderecoNovoCliente() throws SQLException {
		NovoCliEndereco novoCliEndereco = NovoCliEnderecoService.getInstance().localizaNovoCliEnderecoNaLista(getNovoCliente().novoCliEnderecoList, listGridNovoCliEndereco.getSelectedId());
		novoCliEndereco.flTipoAlteracao = NovoCliEndereco.FLTIPOALTERACAO_ORIGINAL;
		CadNovoCliEnderecoWindow cadNovoCliEnderecoWindow = new CadNovoCliEnderecoWindow(getNovoCliente(), novoCliEndereco);
		cadNovoCliEnderecoWindow.popup();
		carregaNovoCliEndereco(cadNovoCliEnderecoWindow.cadNovoCliEnderecoForm.isEnderecoExcluido());
	}
    
    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	super.onFormEvent(event);
    	switch (event.type) {
    		case ControlEvent.PRESSED: {
	    		if (event.target == btFoto) {
					btFotoClick();
	    		} else if (event.target == btEditar) {
	    			editarNovoCliente();
	    		}
    			break;
    		}
	    	case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (listGridNovoCliEndereco.isEventoClickUnicoDisparado())) {
					editaEnderecoNovoCliente();
				}
				break;
			}
			case ControlEvent.WINDOW_CLOSED: {
				if ((listGridNovoCliEndereco != null) && (event.target == listGridNovoCliEndereco.popupMenuOrdenacao)) {
					if (listGridNovoCliEndereco.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listGridNovoCliEndereco.reloadSortSettings();
						sortAtributteListSecundaria = listGridNovoCliEndereco.atributteSortSelected;
						sortAscListSecundaria = StringUtil.getStringValue(listGridNovoCliEndereco.sortAsc);
						carregaNovoCliEndereco(false);
					}
				}
				break;
			}
    	}
    }

    private void editarNovoCliente() throws SQLException {
    	NovoCliente novoCliente = (NovoCliente) NovoClienteService.getInstance().findByRowKeyDyn(getNovoCliente().getRowKey());
    	if (novoCliente != null) {
    		CadNovoClienteForm cadNovoClienteForm = new CadNovoClienteForm();
    		getNovoCliente().flTipoAlteracao = NovoCliente.FLTIPOALTERACAO_ALTERADO;
    		cadNovoClienteForm.edit(getNovoCliente());
    		cadNovoClienteForm.setFromAnaliseNovoCli(true);
    		show(cadNovoClienteForm);
    	} else  {
    		UiUtil.showErrorMessage(Messages.NOVOCLIENTE_NAO_ENCONTRADO_EDICAO);
    	}
	}

	private void btFotoClick() throws SQLException {
		ImageSliderNovoClienteWindow imageSliderNovoClienteWindow = new ImageSliderNovoClienteWindow((NovoCliente) getDomain(), true, true);
		imageSliderNovoClienteWindow.popup();
	}
}
