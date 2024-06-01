package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
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
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilterDyn;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.domain.Cidade;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.service.BairroService;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.ClienteAtuaService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.LogradouroService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPessoaComboBox;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.util.CepUtil;
import br.com.wmw.lavenderepda.util.JSONUtil;
import totalcross.json.JSONObject;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class CadClienteAtuaDynForm extends BaseLavendereCrudPersonCadForm {
	
	private static String PADRAO_REGEX_CNPJ = "[^0-9]*";
	
	private Cliente cliente;
	private LabelName lbNmRazaoSocial;
    private TipoPessoaComboBox cbTipoPessoa;
    private EditNumberMask edNuCnpj;
    private EditNumberMask edNuCpf;
    private String nuCpfCnpjOriginal;

    public CadClienteAtuaDynForm() throws SQLException {
        super(Messages.CLIENTE_NOME_ENTIDADE);
        barBottomContainer.setVisible(false);
        barTopContainer.setVisible(false);
        lbNmRazaoSocial = new LabelName("@@@@@@@@@@", LEFT);
        cbTipoPessoa = new TipoPessoaComboBox();
        
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
        	edNuCpf = new EditNumberMask(mascaraCpf).setID("edNuCpf");
        }
    	if (LavenderePdaConfig.isUsaPreenchimentoCamposNovoClientePorCnpj() && LavenderePdaConfig.isUsaPermitePreenchimentoCamposAtualizacaoClientePorCNPJ()) {
    		edNuCnpj.setRightIcon("images/reload.png");
        }
    }

    protected String getDsTable() throws SQLException {
    	return ClienteAtua.TABLE_NAME;
    }
    
    public Cliente getCliente() {
    	return this.cliente;
    }

    public void setCliente(Cliente cliente) {
    	this.cliente = cliente;
    	this.nuCpfCnpjOriginal = ValueUtil.isNotEmpty(cliente.nuCnpj) ? cliente.nuCnpj.replaceAll(PADRAO_REGEX_CNPJ, "") : "";
    }

    @Override
    public String getEntityDescription() {
    	return title;
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return ClienteAtuaService.getInstance();
    }

    private ClienteAtua getClienteAtua() throws SQLException {
    	return (ClienteAtua) getDomain();
    }

    @Override
    protected BaseDomain createDomain() throws SQLException {
    	ClienteAtua clienteAtua = new ClienteAtua();
    	clienteAtua.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	clienteAtua.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	clienteAtua.cdCliente = cliente.cdCliente;
    	clienteAtua.cdRegistro = getCrudService().generateIdGlobal();
    	clienteAtua.dtAtualizacao = DateUtil.getCurrentDate();
    	clienteAtua.flOrigemAtualizacao = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	clienteAtua.flAtualizaCadastroWmw = LavenderePdaConfig.habilitaAtualizacaoCadastroCliente ? ClienteAtua.FLTIPOALTERACAO_ALTERADO : null;
        return clienteAtua;
    }
    
    @Override
    protected void addComponentesFixosInicio() throws SQLException {
    	UiUtil.add(getContainerPrincipal(), new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lbNmRazaoSocial, getLeft(), getNextY(), SAME);
    	if (LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente) {
    		UiUtil.add(getContainerPrincipal(), new LabelName(Messages.CLIENTEATUA_LABEL_TIPOPESSOA), cbTipoPessoa, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(getContainerPrincipal(), new LabelName(Messages.NOVOCLIENTE_LABEL_NUCNPJ), edNuCnpj, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(getContainerPrincipal(), edNuCpf, SAME, SAME);
    	}
    }
    
    @Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
    	barTopContainer.setVisible(false);
    	barBottomContainer.setVisible(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
        btSalvar.setVisible(enabled);
        edNuCpf.setEnabled(enabled);
        edNuCpf.setEnabled(enabled);
        cbTipoPessoa.setEnabled(enabled);
    }

    @Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	cliente = (Cliente) ClienteService.getInstance().findByRowKeyDyn(cliente.getRowKey());
    	setEnabled(!isEditing() || getClienteAtua().isAlteradoPalm());
    	if (LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisCliente()) {
    		if (!isEditing()) {
    			ClienteAtuaService.getInstance().loadValuesClienteAtuaByCliente(getClienteAtua(), cliente);
    		}
    		ClienteAtuaService.getInstance().loadHashValuesDinamicosOriginal(getClienteAtua());
    		domainToScreen(getClienteAtua());
    	}
    }
    
    @Override
    protected void clearScreen() throws SQLException {
    	super.clearScreen();
    	if (LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente) {
    		edNuCnpj.setValue("");
    		edNuCpf.setValue("");
    		if (nuCpfCnpjOriginal.length() != 14) {
    			cbTipoPessoa.setValue(Messages.TIPOPESSOA_FLAG_FISICA);
    			edNuCpf.setValue(nuCpfCnpjOriginal);
    			edNuCpf.setVisible(true);
    			edNuCnpj.setVisible(false);
    		} else {
    			cbTipoPessoa.setValue(Messages.TIPOPESSOA_FLAG_JURIDICA);
    			edNuCnpj.setValue(nuCpfCnpjOriginal);
    			edNuCnpj.setVisible(true);
    			edNuCpf.setVisible(false);
    		}
    	}
    }
    
    @Override
    protected void insertOrUpdate(BaseDomain domain) throws SQLException {
    	ClienteAtua clienteAtua = (ClienteAtua) domain;
    	if (!LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisClienteSalvaDadosNaoAlterados()) {
    		ClienteAtuaService.getInstance().limpaInfoDinamicasSemAlteracoesCadastro(clienteAtua);
    		if (LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente && ValueUtil.isNotEmpty(clienteAtua.nuCnpj) && clienteAtua.nuCnpj.replaceAll(PADRAO_REGEX_CNPJ, "").equals(nuCpfCnpjOriginal)) {
    			clienteAtua.nuCnpj = null;
    			clienteAtua.flTipoPessoa = null;
    		}
    	} else if (LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente && ValueUtil.isEmpty(clienteAtua.nuCnpj)) {
    		clienteAtua.nuCnpj = nuCpfCnpjOriginal;
    		clienteAtua.flTipoPessoa = nuCpfCnpjOriginal.length() != 14 ? Messages.TIPOPESSOA_FLAG_FISICA : Messages.TIPOPESSOA_FLAG_JURIDICA;
    	}
    	if (LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente && ValueUtil.isEmpty(clienteAtua.nuCnpj)) {
    		clienteAtua.flTipoPessoa = null;
    	}
		clienteAtua.cdUsuarioAlteracao = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    	super.insertOrUpdate(domain);
    }
    
    @Override
    protected BaseDomain screenToDomain() throws SQLException {
    	super.screenToDomain();
    	ClienteAtua clienteAtua = (ClienteAtua) getDomain();
    	if (LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente) {
    		if (Messages.TIPOPESSOA_FLAG_JURIDICA.equals(cbTipoPessoa.getValue())) {
    			String nuCnpj = edNuCnpj.getText().replaceAll(PADRAO_REGEX_CNPJ, "");
    			clienteAtua.nuCnpj = ValueUtil.isNotEmpty(nuCnpj) ? edNuCnpj.getText() : null;
    			clienteAtua.flTipoPessoa = Messages.TIPOPESSOA_FLAG_JURIDICA;
    		} else if (Messages.TIPOPESSOA_FLAG_FISICA.equals(cbTipoPessoa.getValue())) {
    			String nuCnpj = edNuCpf.getText().replaceAll(PADRAO_REGEX_CNPJ, "");
    			clienteAtua.nuCnpj = ValueUtil.isNotEmpty(nuCnpj) ? edNuCpf.getText() : null;
    			clienteAtua.flTipoPessoa = Messages.TIPOPESSOA_FLAG_FISICA;
    		}
    		if (ValueUtil.isNotEmpty(clienteAtua.nuCnpj) && !clienteAtua.nuCnpj.replaceAll(PADRAO_REGEX_CNPJ, "").equals(nuCpfCnpjOriginal)) {
    			clienteAtua.houveAlteracaoCpfCnpj = true;
    		}
    	}
    	return clienteAtua;
    }
    
    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	super.domainToScreen(domain);
    	ClienteAtua clienteAtua = (ClienteAtua) getDomain();
    	if ((LavenderePdaConfig.usaAtualizacaoTipoPessoaCliente) && ValueUtil.isNotEmpty(clienteAtua.nuCnpj)) {
    		nuCpfCnpjOriginal = clienteAtua.nuCnpj.replaceAll(PADRAO_REGEX_CNPJ, "");
    		if (Messages.TIPOPESSOA_FLAG_JURIDICA.equals(clienteAtua.flTipoPessoa)) {
    			cbTipoPessoa.setValue(clienteAtua.flTipoPessoa);
    			edNuCnpj.setValue(nuCpfCnpjOriginal);
    			edNuCnpj.setVisible(true);
    			edNuCpf.setVisible(false);
    		} else if (Messages.TIPOPESSOA_FLAG_FISICA.equals(clienteAtua.flTipoPessoa)) {
    			cbTipoPessoa.setValue(clienteAtua.flTipoPessoa);
    			edNuCpf.setValue(nuCpfCnpjOriginal);
    			edNuCpf.setVisible(true);
    			edNuCnpj.setVisible(false);
    		}
    	}
    }

    @Override
    protected void onFormStart() throws SQLException {
    	super.onFormStart();
    	lbNmRazaoSocial.setValue(cliente.toString());
    }

    @Override
    protected int getTop() {
    	return TOP;
    }

	@Override
    protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
    	switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == cbTipoPessoa) {
				cbTipoPessoaClick();
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
	
	protected Hashtable getHashComponentes() {
		return hashComponentes;
	}
	
	@Override
	public void salvarClick() throws SQLException {
		super.salvarClick();
	}
	
	@Override
    protected void list() throws java.sql.SQLException {
    }
	
	@Override
    public void close() throws SQLException {
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
	
	private void btPreencheCamposAutoClick() {
		NovoClienteService.getInstance().validateCnpj(edNuCnpj.getValue());
		UiUtil.showProcessingMessage();
		try {
			JSONObject jsonResponse = SyncManager.consultaCnpjOnline(edNuCnpj.getValue());
			processaRetornoConsultaCnpj(jsonResponse, hashComponentes);
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
						if (value == null || ValueUtil.valueEquals(value, "null")) {
							value = null;
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
	
	private void edDsCepFocusOut() throws Exception {
		EditText edCep = (EditText) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL);
		if (edCep == null || ValueUtil.isEmpty(edCep.getValue())) {
			return;
		}

		Cep cep = CepUtil.cepOffLine(edCep.getValue());
		boolean cepOffLine = true;
		if (cep == null && LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			cep = new Cep(edCep.getValue());
			cepOffLine = !CepUtil.consultaCepOnline(cep);
		}

		Control edLogradouro = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSLOGRADOUROCOMERCIAL);
		Control edBairro = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSBAIRROCOMERCIAL);
		Control edDSCidade = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSCIDADECOMERCIAL);
		Control edCDCidade = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_CDCIDADECOMERCIAL);
		Control cbUf = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_CDUFCOMERCIAL);
		Control cbEstado = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL);
		Control edDsTipoLogradouro = (Control) hashComponentes.get(NovoCliEndereco.NMCOLUNA_DSTIPOLOGRADOUROCOMERCIAL);
		if (cep != null) {
			setaValoresNosCamposDinamicos(cep, cepOffLine, edLogradouro, edBairro, edDSCidade, edCDCidade, cbUf, cbEstado, edDsTipoLogradouro);
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
			setaValorComboDsCidadeRelacionada(cep, cepOffline, edDSCidade, dsUf);
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
			setaValorComboCdCidadeRelacionada(cep, cepOffline, edCDCidade, dsUf);
		} else if (edCDCidade instanceof PopUpSearchFilterDyn) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((PopUpSearchFilterDyn) edCDCidade).setValue(dsCidade);
			}
		}
	}
	
	protected void setaValorComboDsCidadeRelacionada(Cep cep, boolean cepOffline, Control edDSCidade, String dsUf) throws SQLException {
		String cdCidade = cepOffline ? cep.cdCidade : cep.dsCidade;
		((CampoDinamicoComboBox) edDSCidade).loadComboRelacionada("TBLVWUF", NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL, dsUf);
		if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
			((CampoDinamicoComboBox) edDSCidade).setValue(cdCidade);
		}
	}
	
	protected void setaValorComboCdCidadeRelacionada(Cep cep, boolean cepOffline, Control edCDCidade, String dsUf) throws SQLException {
		((CampoDinamicoComboBox) edCDCidade).loadComboRelacionada("TBLVWUF", NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL, dsUf);
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
	}
	
	@Override
	protected void afterSave() throws SQLException {
		super.afterSave();
		ClienteService.getInstance().limpaFlAtualizaCadastroWmw(cliente);
	}
	
}