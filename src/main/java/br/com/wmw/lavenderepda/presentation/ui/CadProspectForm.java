package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.CampoDinamicoComboBox;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditInscricaoEstadual;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditNumberMask;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.EditTextMask;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilterDyn;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.enums.GroupTypeFile;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.domain.Cidade;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.service.BairroService;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import br.com.wmw.lavenderepda.business.service.DocumentoAnexoService;
import br.com.wmw.lavenderepda.business.service.FotoProspectService;
import br.com.wmw.lavenderepda.business.service.LogradouroService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.ProspectService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPessoaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderProspectWindow;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.util.CepUtil;
import br.com.wmw.lavenderepda.util.JSONUtil;
import br.com.wmw.lavenderepda.util.LavendereFileChooserBoxUtil;
import totalcross.json.JSONObject;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class CadProspectForm extends BaseLavendereCrudPersonCadForm {
	
	private EditText edNuCnpj;
    private EditText edNuCpf;
    private TipoPessoaComboBox cbTipoPessoa;
    private ButtonAction btCadastrarCoordenada;
	private ButtonAction btFoto;
	private ButtonAction btAnexarDoc;
	private RepresentanteSupervComboBox cbRepresentantesSupervisor;
	private EditText edRepresentante;

	public CadProspectForm() throws SQLException {
		super(Messages.PROSPECT_NOME_ENTIDADE);
        if (LavenderePdaConfig.isUsaSistemaIdiomaIngles()) {
        	edNuCnpj = new EditText("", 100).setID("edNuCnpj");
            edNuCpf = new EditText("", 100).setID("edNuCpf");
        } else {
    		String mascaraCnpj = "##.###.###/####-##";
            String mascaraCpf = "###.###.###-##";
            if (LavenderePdaConfig.isUsaSistemaIdiomaEspanhol()) {
            	mascaraCnpj = "########-#";
                mascaraCpf = "#######";
            }
            edNuCnpj = new EditNumberMask(mascaraCnpj).setID("edNuCnpj");
            if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpj()) {
            	edNuCnpj.setRightIcon("images/reload.png");
            }
            edNuCpf = new EditNumberMask(mascaraCpf).setID("edNuCpf");
        }
        btFoto = new ButtonAction(Messages.LABEL_FOTO, "images/camera.png");
        btAnexarDoc = new ButtonAction(Messages.BOTAO_ANEXAR_DOC, "images/anexo.png");
        cbTipoPessoa = new TipoPessoaComboBox().setID("cbTipoPessoa");
        if (LavenderePdaConfig.usaCadastroProspectPF() ^ LavenderePdaConfig.usaCadastroProspectPJ()) {
        	cbTipoPessoa.setEditable(false);
        }
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        	edRepresentante = new EditText("@@@@@@@@@@", 10);
        	edRepresentante.setEditable(false);
        }
        btCadastrarCoordenada = new ButtonAction(Messages.CAD_COORD_AREV, "images/gps.png");
	}
	
	@Override
	public void add() throws SQLException {
		super.add();
		Prospect prospect = (Prospect) getDomain();
		prospect.cdEmpresa = SessionLavenderePda.cdEmpresa;
		prospect.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentantesSupervisor.getValue() : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		prospect.flOrigemProspect = OrigemPedido.FLORIGEMPEDIDO_PDA;
		prospect.cdProspect = getCrudService().generateIdGlobal();
		prospect.dtCadastro = DateUtil.getCurrentDate();
		prospect.hrCadastro = TimeUtil.getCurrentTimeHHMMSS();
	}

	@Override
	protected String getDsTable() throws SQLException {
		return Prospect.TABLE_NAME;
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new Prospect();
	}

	@Override
	protected String getEntityDescription() {
		return Messages.PROSPECT_NOME_ENTIDADE;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ProspectService.getInstance();
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		Prospect prospect = (Prospect) super.screenToDomain();
		prospect.flTipoPessoa = cbTipoPessoa.getValue();
		prospect.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentantesSupervisor.getValue() : SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (Messages.TIPOPESSOA_FLAG_FISICA.equals(prospect.flTipoPessoa)) {
			prospect.nuCnpj = LavenderePdaConfig.ignoraSeparadoresProspectCpfCnpj ? edNuCpf.getValue() : edNuCpf.getText();
    	} else {
    		prospect.nuCnpj = LavenderePdaConfig.ignoraSeparadoresProspectCpfCnpj ? edNuCnpj.getValue() : edNuCnpj.getText();
    	}
		return prospect;
	}
	
	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		super.domainToScreen(domain);
		Prospect prospect = (Prospect) domain;
		cbTipoPessoa.setValue(prospect.flTipoPessoa);
		cbTipoPessoaClick();
		if (SessionLavenderePda.isUsuarioSupervisor()) {
        	cbRepresentantesSupervisor.setValue(prospect.cdRepresentante);
        	edRepresentante.setText(SessionLavenderePda.getRepresentante().nmRepresentante + " - " + "[" + SessionLavenderePda.getRepresentante().cdRepresentante + "]");
        }
		String nuCnpjCpf = ValueUtil.getValidNumbers(prospect.nuCnpj);
        if (ValueUtil.isNotEmpty(nuCnpjCpf)) {
	    	if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
	    		edNuCpf.setValue(nuCnpjCpf);
	    	} else {
	    		edNuCnpj.setValue(nuCnpjCpf);
	    	}
        }
	}
	
	private void cbTipoPessoaClick() {
    	if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
    		edNuCpf.setVisible(true);
	        edNuCpf.setValue("");
	        edNuCnpj.setVisible(false);
    	} else {
    		edNuCnpj.setVisible(true);
			edNuCnpj.setValue("");
	        edNuCpf.setVisible(false);
    	}
    }
	
	@Override
	protected void clearScreen() throws SQLException {
		super.clearScreen();
		edNuCnpj.setValue("");
    	edNuCpf.setValue("");
    	if (LavenderePdaConfig.usaCadastroProspectPJ()) {
    		cbTipoPessoa.setValue(Messages.TIPOPESSOA_FLAG_JURIDICA);
    	} else if (LavenderePdaConfig.usaCadastroProspectPF()) {
    		cbTipoPessoa.setValue(Messages.TIPOPESSOA_FLAG_FISICA);
    	}
    	edNuCnpj.setVisible(cbTipoPessoa.getValue().equals(Messages.TIPOPESSOA_FLAG_JURIDICA));
		edNuCpf.setVisible(cbTipoPessoa.getValue().equals(Messages.TIPOPESSOA_FLAG_FISICA));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		boolean usaCadastroProspect = LavenderePdaConfig.usaCadastroProspectPF() || LavenderePdaConfig.usaCadastroProspectPJ();
		super.setEnabled(enabled && usaCadastroProspect);
		edNuCnpj.setEditable(!isEditing() && usaCadastroProspect);
        edNuCpf.setEditable(!isEditing() && usaCadastroProspect);
        if (LavenderePdaConfig.usaCadastroProspectPF() && LavenderePdaConfig.usaCadastroProspectPJ()) {
        	cbTipoPessoa.setEditable(!isEditing());
        } else if (!usaCadastroProspect) {
        	cbTipoPessoa.setEditable(false);
        }
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		setEnabled(!isEditing() || getDomain().isAlteradoPalm());
	}
	
	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		setEnabled(!isEditing() || getDomain().isAlteradoPalm());
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		barBottomContainer.removeAll();
		if (!getDomain().isEnviadoServidor() && (LavenderePdaConfig.usaCadastroProspectPF() || LavenderePdaConfig.usaCadastroProspectPJ())) {
			UiUtil.add(barBottomContainer, btExcluir, 1);
			int posicao = 5;
			if (LavenderePdaConfig.permiteTirarFotoProspect()) {
				UiUtil.add(barBottomContainer, btFoto, posicao--);
			}
			if (LavenderePdaConfig.permiteCadastroCoordenada) {
				UiUtil.add(barBottomContainer, btCadastrarCoordenada, posicao--);
			}
			if (LavenderePdaConfig.permiteAnexarDocumentosProspect) {
				UiUtil.add(barBottomContainer, btAnexarDoc, posicao);
			}
		}
	}

	@Override
	protected void addComponentesFixosInicio() throws SQLException {
		int yPos = TOP;
		Container container = getContainerPrincipal();
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			UiUtil.add(container, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), edRepresentante, getLeft(), yPos);
			yPos = AFTER + HEIGHT_GAP_BIG;
		}
		if (!LavenderePdaConfig.isUsaSistemaIdiomaIngles() || (LavenderePdaConfig.usaCadastroProspectPF() && LavenderePdaConfig.usaCadastroProspectPJ())) {
			UiUtil.add(container, new LabelName(Messages.NOVOCLIENTE_LABEL_TIPOPESSOA), cbTipoPessoa, getLeft(), yPos);
		}
		UiUtil.add(container, new LabelName(Messages.NOVOCLIENTE_LABEL_NUCNPJ), edNuCnpj, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(container, edNuCpf, SAME, SAME);
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCadastrarCoordenada) {
					btCadastrarCoordenadaClick();
				} else if (event.target == btFoto) {
					btFotoClick();
				} else if (event.target == btAnexarDoc) {
					btAnexarDocClick();
				} else if (event.target == cbTipoPessoa) {
					cbTipoPessoaClick();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == hashComponentes.get(Prospect.NMCOLUNA_DSCEPCOMERCIAL)) {
					try {
						edDsCepFocusOut();
					} catch (Throwable e) {
						ExceptionUtil.handle(e);
					}
				} else if (event.target == edNuCnpj) {
					edNuCnpj.enabledIconEvent = true;
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edNuCnpj) {
					btPreencheCamposAutoClick();
				}
				break;
			}
		}
	}
	
	private boolean isRepresentanteSelecionadoValido() throws SQLException {
    	if (ValueUtil.isEmpty(((Prospect) getDomain()).cdRepresentante)) {
			UiUtil.showErrorMessage(Messages.MENUREPRESENTANTE_MSG_SELECIONEANTESACAO);
			return false;
		}
		return true;
	}

	private void btCadastrarCoordenadaClick() throws SQLException {
		Prospect prospect = (Prospect) getDomain();
		if (isRepresentanteSelecionadoValido()) {
			LoadingBoxWindow mb = UiUtil.createProcessingMessage();
	    	mb.popupNonBlocking();
	    	try {
	    		save();
	    		edit(prospect);
	    		if (!prospect.isAlteradoPalm()) {
	    			UiUtil.showErrorMessage(Messages.PROSPECT_MSG_CADCOORDENADA_BLOQUEADO);
	    			return;
	    		}
	    		if (!ConexaoPdaService.getInstance().validateEnvioRecebimentoDadosObrigatorio()) {
	    			return;
	    		}
	    		CadCoordenadasGeograficasWindow cadCoordenadasGeograficasWindow = new CadCoordenadasGeograficasWindow(prospect, true, false);
	    		cadCoordenadasGeograficasWindow.popup();
	    	} finally {
				mb.unpop();
			}
		}
	}
	
	private void btFotoClick() throws SQLException {
		if (!isRepresentanteSelecionadoValido()) {
    		return;
    	}
		new ImageSliderProspectWindow((Prospect) getDomain()).popup();
	}
	
	@Override
	protected void insert(BaseDomain domain) throws SQLException {
		Prospect prospect = (Prospect) domain;
		prospect.cdUsuarioCriacao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		super.insert(prospect);
	}
	
	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		boolean usaCadastroProspect = LavenderePdaConfig.usaCadastroProspectPF() || LavenderePdaConfig.usaCadastroProspectPJ();
		Prospect prospect = (Prospect) getDomain();
		btSalvar.setVisible(usaCadastroProspect && (prospect.isAlteradoPalm() || !isEditing()));
        btExcluir.setVisible(usaCadastroProspect && isEditing() && prospect.isAlteradoPalm());
	}
	
	@Override
	protected void delete(BaseDomain domain) throws SQLException {
		super.delete(domain);
		Prospect prospect = (Prospect) domain;
		FotoProspectService.getInstance().excluiFotosProspect(prospect);
		DocumentoAnexoService.getInstance().deleteDocAnexo(DocumentoAnexo.NM_ENTIDADE_PROSPECT, prospect.getRowKey());
	}
	
	
	private void btAnexarDocClick() throws SQLException {
		if (!isRepresentanteSelecionadoValido()) {
			return;
		}
		try {
			new LavendereFileChooserBoxUtil(GroupTypeFile.ALL, DocumentoAnexo.NM_ENTIDADE_PROSPECT, getDomain(), false).showListDocumentoAnexo();
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}

	private void edDsCepFocusOut() throws Exception {
		EditText edCep = (EditText) hashComponentes.get(Prospect.NMCOLUNA_DSCEPCOMERCIAL);
		if (edCep == null || ValueUtil.isEmpty(edCep.getValue())) {
			return;
		}
		Cep cep = CepUtil.cepOffLine(edCep.getValue());
		boolean cepOffLine = true;
		if (cep == null && LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			cep = new Cep(edCep.getValue());
			cepOffLine = !CepUtil.consultaCepOnline(cep);
		}
		Control edLogradouro = (Control) hashComponentes.get(Prospect.NMCOLUNA_DSLOGRADOUROCOMERCIAL);
		Control edDsTipoLogradouro = (Control) hashComponentes.get(Prospect.NMCOLUNA_DSTIPOLOGRADOURTOCOMERCIAL);
		Control edBairro = (Control) hashComponentes.get(Prospect.NMCOLUNA_DSBAIRROCOMERCIAL);
		Control edCDCidade = (Control) hashComponentes.get(Prospect.NMCOLUNA_CDCIDADECOMERCIAL);
		Control edDSCidade = (Control) hashComponentes.get(Prospect.NMCOLUNA_DSCIDADECOMERCIAL);
		Control cbEstado = (Control) hashComponentes.get(Prospect.NMCOLUNA_CDESTADOCOMERCIAL);
		Control cbUf = (Control) hashComponentes.get(Prospect.NMCOLUNA_CDUFCOMERCIAL);

		if (cep != null) {
			setaValoresNosCamposDinamicos(cep, cepOffLine, edLogradouro, edBairro, edDSCidade, edCDCidade, cbUf, cbEstado, edDsTipoLogradouro);
		}
	}

	private void setaValoresNosCamposDinamicos(Cep cep, boolean cepOffline, Control edLogradouro, Control edBairro, Control edDSCidade, Control edCDCidade, Control cbUf, Control cbEstado, Control edDsTipoLogradouro) throws SQLException {
		if (edLogradouro instanceof EditText) {
			String cdLogradouro = cepOffline ? LogradouroService.getInstance().getDsLogradouro(cep.cdLogradouro) : cep.dsLogradouro;
			if (ValueUtil.isNotEmpty(cdLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edLogradouro).setValue(cdLogradouro);
			}
		} else if (edLogradouro instanceof CampoDinamicoComboBox && (ValueUtil.isNotEmpty(cep.cdLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((CampoDinamicoComboBox) edLogradouro).setValue(cep.cdLogradouro);
		}
		if (edDsTipoLogradouro instanceof EditText) {
			String dsLogradouro = cepOffline ? LogradouroService.getInstance().getDsTipoLogradouro(cep.cdLogradouro) : cep.dsLogradouro;
			if (ValueUtil.isNotEmpty(dsLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edDsTipoLogradouro).setValue(dsLogradouro);
			}
		}
		if (edBairro instanceof EditText) {
			String dsBairro = cepOffline ? BairroService.getInstance().getDsBairro(cep.cdBairro) : cep.dsBairro;
			if (ValueUtil.isNotEmpty(dsBairro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edBairro).setValue(dsBairro);
			}
		} else if (edBairro instanceof CampoDinamicoComboBox) {
			String cdBairro = cepOffline ? cep.cdBairro : cep.dsBairro;
			if (ValueUtil.isNotEmpty(cdBairro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((CampoDinamicoComboBox) edBairro).setValue(cdBairro);
			}
		}
		String dsUf = cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.dsUf;
		if (cbUf instanceof EditText && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((EditText) cbUf).setValue(dsUf);
		} else if (cbUf instanceof CampoDinamicoComboBox && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((CampoDinamicoComboBox) cbUf).setValue(dsUf);
		}
		if (cbEstado instanceof EditText && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((EditText) cbEstado).setValue(dsUf);
		} else if (cbEstado instanceof CampoDinamicoComboBox && (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((CampoDinamicoComboBox) cbEstado).setValue(dsUf);
		}
		if (edDSCidade instanceof EditText) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edDSCidade).setValue(dsCidade);
			}
		} else if (edDSCidade instanceof CampoDinamicoComboBox) {
			String cdCidade = cepOffline ? cep.cdCidade : cep.dsCidade;
			((CampoDinamicoComboBox) edDSCidade).loadComboRelacionada("TBLVWUF", Prospect.NMCOLUNA_CDESTADOCOMERCIAL, dsUf);
			if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((CampoDinamicoComboBox) edDSCidade).setValue(cdCidade);
			}
		} else if (edDSCidade instanceof PopUpSearchFilterDyn) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((PopUpSearchFilterDyn) edDSCidade).setValue(dsCidade);
			}
		}
		if (edCDCidade instanceof EditText) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((EditText) edCDCidade).setValue(dsCidade);
			}
		} else if (edCDCidade instanceof CampoDinamicoComboBox) {
			((CampoDinamicoComboBox) edCDCidade).loadComboRelacionada("TBLVWUF", Prospect.NMCOLUNA_CDESTADOCOMERCIAL, dsUf);
			String cdCidade = cepOffline ? cep.cdCidade : cep.dsCidade;
			if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((CampoDinamicoComboBox) edCDCidade).setValue(cdCidade);
			}
			if (((CampoDinamicoComboBox) edCDCidade).getSelectedIndex() == -1) {
				Cidade cidade = new Cidade();
				cidade.nmCidade = StringUtil.changeStringAccented(cdCidade).toUpperCase();
				Vector list = CidadeService.getInstance().findAllByExample(cidade);
				if (list.size() > 0) {
					cidade = (Cidade) list.items[0];
					if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
						((CampoDinamicoComboBox) edCDCidade).setValue(cidade.cdCidade);
					}
				}
			}
		} else if (edCDCidade instanceof PopUpSearchFilterDyn) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((PopUpSearchFilterDyn) edCDCidade).setValue(dsCidade);
			}
		}
	}

	private void btPreencheCamposAutoClick() {
		NovoClienteService.getInstance().validateCnpj(edNuCnpj.getValue());
		try {
			JSONObject jsonResponse = SyncManager.consultaCnpjOnline(edNuCnpj.getValue());
			processaRetornoConsultaCnpj(jsonResponse, hashComponentes);
			edNuCnpj.enabledIconEvent = false;
		} catch (Exception ex) {
			UiUtil.showErrorMessage(ex);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}
	
	private void processaRetornoConsultaCnpj(JSONObject jsonResponse, Hashtable hashComponentes) throws SQLException {
		boolean changeCep = false;
		if (jsonResponse != null && hashComponentes != null) {
			Vector camposList = getConfigPersonCadList();
			int camposSize = camposList.size();
			StringBuilder camposComErro = new StringBuilder();
			String vir = "";
			String inscricaoEstadualValue = "";
			EditInscricaoEstadual editInscricaoEstadual = null;
			for (int i = 0; i < camposSize; i++) {
				Campo campo = (Campo) camposList.items[i];
				if (ValueUtil.isNotEmpty(campo.dsTagJson) && campo.isVisivelCad()) {
					Control control = (Control) hashComponentes.get(campo.nmCampo);
					control.clear();
					if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjComBloqueio()) {
						control.setEnabled(true);
					}
					try {
						String value = null;
						if (campo.dsTagJson.contains(".") || campo.dsTagJson.contains("_")) {
							value = JSONUtil.percorreTags(campo.dsTagJson, jsonResponse);
						} else {
							value = jsonResponse.getString(campo.dsTagJson);
						}
						if (control instanceof EditInscricaoEstadual) {
							inscricaoEstadualValue = value;
							editInscricaoEstadual = (EditInscricaoEstadual) control;
						}
						if (value == null) {
							camposComErro.append(vir).append(campo.dsLabel);
							vir = ", ";
						}
						if (campo.nmCampo.contains("CEP")) {
							changeCep = true;
							edDsCepFocusOut();
						}
						setValueCampoDinamicoJsonTag(control, campo, value);
					} catch (Throwable ex) {
						camposComErro.append(vir).append(campo.dsLabel);
						vir = ", ";
						ExceptionUtil.handle(ex);
					}
				}
			}
			if (ValueUtil.isNotEmpty(camposComErro.toString())) {
				String msgCamposComErro = MessageUtil.getMessage(Messages.NOVOCLIENTE_CONSULTA_WEBSERVICE_ALERTA_NAO_PREENCHIDOS, camposComErro.toString());
				VmUtil.debug(msgCamposComErro);
				VmUtil.debug(jsonResponse.toString());
				if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjNotificaFalhas()) {
					UiUtil.showWarnMessage(msgCamposComErro);
				}
			}
			if (changeCep) {
				try {
					edDsCepFocusOut();
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
			if (editInscricaoEstadual != null) {
				editInscricaoEstadual.setValue(inscricaoEstadualValue);
				if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjComBloqueio()) {
					editInscricaoEstadual.setEnabled(false);
				}
			}
		}
	}

	private void setValueCampoDinamicoJsonTag(Control control, Campo campo, String value) {
		if (value != null) {
			if (control instanceof EditTextMask) {
				value = StringUtil.delete(StringUtil.delete(value, '-'), '.');
				if (campo.isDinamico() && value.length() > campo.getNuMaxLen()) {
					value = value.substring(0, campo.getNuMaxLen());
				}
				EditTextMask editMaskControl = (EditTextMask) control;
				editMaskControl.setValue(editMaskControl.clearMask(value));
			} else if (control instanceof EditText) {
				if (campo.isDinamico() && value.length() > campo.getNuMaxLen()) {
					value = value.substring(0, campo.getNuMaxLen());
				}
				((EditText) control).setValue(value);
			} else if (control instanceof EditDate) {
				((EditDate)control).setValue(DateUtil.getDateValue(value));
			} else if (control instanceof EditNumberInt) {
				((EditNumberInt)control).setValue(ValueUtil.getIntegerValue(value));
			} else if (control instanceof EditNumberFrac) {
				((EditNumberFrac)control).setValue(ValueUtil.getDoubleValue(value));
			} else if (control instanceof CampoDinamicoComboBox) {
				((CampoDinamicoComboBox)control).setValue(value);
			} else if (control instanceof LabelValue) {
				((LabelValue) control).setValue(value);
			} else if (control instanceof EditInscricaoEstadual) {
				((EditInscricaoEstadual) control).setValue(value);
			}
			if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpjComBloqueio()) {
				control.setEnabled(false);
			}
		}
	}
	
	private boolean showRepresentanteCombo() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
	    	cbRepresentantesSupervisor.setValue(null);
	    	cbRepresentantesSupervisor.popup();
	    	((Prospect)getDomain()).cdRepresentante = cbRepresentantesSupervisor.getValue();
	    	cbRepresentantesSupervisor.configureSession();
	    	edRepresentante.setText(SessionLavenderePda.getRepresentante().nmRepresentante + " - " + "[" + SessionLavenderePda.getRepresentante().cdRepresentante + "]");
	    	return cbRepresentantesSupervisor.getSelectedItem() != null;
    	}
    	return true;
	}
	
	@Override
	protected void beforeAdd() throws SQLException {
		if (!showRepresentanteCombo()) {
			throw new ValidationException(Messages.MENUREPRESENTANTE_MSG_SELECIONEANTESACAO);
		}
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		Prospect prospect = (Prospect) getDomain();
    	if (!prospect.isEnviadoServidor() && !UiUtil.showConfirmYesNoMessage(Messages.NOVOCLIENTE_VOLTARCLICK)) {
    		return;
    	}
    	if (!isEditing() && LavenderePdaConfig.permiteAnexarDocumentosProspect) {
    		DocumentoAnexoService.getInstance().deleteDocAnexo(DocumentoAnexo.NM_ENTIDADE_PROSPECT, prospect.getRowKey());
    	}
		super.voltarClick();
	}
	
	@Override
	protected void afterSave() throws SQLException {
		super.afterSave();
		if (LavenderePdaConfig.usaGeracaoVisitaProspectNovoCliente) {
			VisitaService.getInstance().geraVisitaNovoProspect((Prospect)getDomain());
		}
	}

}
