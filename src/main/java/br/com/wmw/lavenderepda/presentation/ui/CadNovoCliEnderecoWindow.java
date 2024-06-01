package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseComboBoxDomain;
import br.com.wmw.framework.business.validator.CpfCnpjValidator;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CampoDinamicoComboBox;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.EditTextMask;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.domain.Cidade;
import br.com.wmw.lavenderepda.business.domain.EmpresaEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.service.BairroService;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.EmpresaEnderecoService;
import br.com.wmw.lavenderepda.business.service.LogradouroService;
import br.com.wmw.lavenderepda.util.CepUtil;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class CadNovoCliEnderecoWindow extends WmwWindow {
	
	protected CadNovoCliEnderecoForm cadNovoCliEnderecoForm;
	private ButtonPopup btSalvar;
	private ButtonPopup btExcluir;

	public CadNovoCliEnderecoWindow(NovoCliente novoCliente, NovoCliEndereco novoCliEndereco) {
		this(novoCliente, novoCliEndereco, false);
	}
	
	public CadNovoCliEnderecoWindow(NovoCliente novoCliente, NovoCliEndereco novoCliEndereco, boolean fromAnaliseNovoCli) {
		super(Messages.NOVOCLIENDERECO_TELA_CADASTRO);
		cadNovoCliEnderecoForm = new CadNovoCliEnderecoForm();
		cadNovoCliEnderecoForm.setNovoCliente(novoCliente);
		cadNovoCliEnderecoForm.setNovoCliEnderecoSelecionado(novoCliEndereco);
		btSalvar = new ButtonPopup(FrameworkMessages.BOTAO_SALVAR);
		btExcluir = new ButtonPopup(FrameworkMessages.BOTAO_EXCLUIR);
		btFechar = new ButtonPopup(FrameworkMessages.BOTAO_FECHAR);
		if (novoCliEndereco != null) {
			cadNovoCliEnderecoForm.setEnabled(novoCliEndereco.isAlteradoPalm());
			btSalvar.setVisible(novoCliEndereco.isAlteradoPalm());
	        btExcluir.setVisible(novoCliEndereco.isAlteradoPalm() && !fromAnaliseNovoCli);
		}
		setDefaultRect();
	}
	
	//@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadNovoCliEnderecoForm, LEFT, getTop(), FILL - 1, FILL);
		addButtonPopup(btSalvar);
		if (cadNovoCliEnderecoForm.getNovoCliEnderecoSelecionado() != null) {
			addButtonPopup(btExcluir);
		}
		addButtonPopup(btFechar);
	}
	
	//@Override
	public void popup() {
	   try {
		if (cadNovoCliEnderecoForm.getNovoCliEnderecoSelecionado() == null) {
			cadNovoCliEnderecoForm.add();
		} else {
			cadNovoCliEnderecoForm.edit(cadNovoCliEnderecoForm.getNovoCliEnderecoSelecionado());
		}
		cadNovoCliEnderecoForm.onFormShow();
		super.popup();
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	//@Override
	protected void fecharWindow() throws SQLException {
		cadNovoCliEnderecoForm.setNovoCliEnderecoSelecionado(null);
		cadNovoCliEnderecoForm.onFormClose();
		super.fecharWindow();
	}
	
	//@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		try {
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btSalvar) {
						validaCpfCnpj();
						validaCep();
						cadNovoCliEnderecoForm.salvarClick();
						unpop();
					} else if (event.target == btExcluir) {
						cadNovoCliEnderecoForm.excluirClick();
						if (cadNovoCliEnderecoForm.isEnderecoExcluido()) {
							unpop();
						}
					}
			        break;
				}
				case ValueChangeEvent.VALUE_CHANGE: {
					if (cadNovoCliEnderecoForm.isApresentaDiasEntregaEmpresaCadastroEndereco() && (event.target == cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSBAIRRO) || event.target == cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCIDADE))) {
						PushButtonGroupBase pbDsDiaEntregaEmpresa = (PushButtonGroupBase) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSDIAENTREGAEMPRESA);
						if (pbDsDiaEntregaEmpresa != null) {
							EditText edDsBairro = (EditText) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSBAIRRO);
							EditText edDsCidade = (EditText) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCIDADE);
							NovoCliEndereco novoCliEndereco = cadNovoCliEnderecoForm.getNovoCliEndereco();
							
							EmpresaEndereco empresaEndereco = EmpresaEnderecoService.getInstance().findEmpresaEndereco(novoCliEndereco.cdEmpresa, edDsBairro.getValue(), edDsCidade.getValue());
							if (empresaEndereco != null) {
								pbDsDiaEntregaEmpresa.setValuesFormatted(empresaEndereco.dsDiasEntrega);
							}
						}
					}
					if (event.target == cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEP) || event.target == cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL)) {
						cepValueChange(); 							
					}
					break;
				}
				case PenEvent.PEN_UP: {
					buttonGroupBooleanEnderecoPadraoChange(event);
					buttonGroupBooleanEnderecoCobrancaChange(event);
				}
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
	}

	private void buttonGroupBooleanEnderecoCobrancaChange(Event event) {
		if (cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLCOBRANCAPADRAO) != null && ((ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLCOBRANCAPADRAO)).bgBoolean == event.target) {
			ButtonGroupBoolean flEntrega = (ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLCOBRANCA);
			flEntrega.bgBoolean.setSelectedIndex(1);
		} else if (cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLCOBRANCA) != null && ((ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLCOBRANCA)).bgBoolean == event.target) {
			ButtonGroupBoolean flEntregaPadrao = (ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLCOBRANCAPADRAO);
			if (flEntregaPadrao.bgBoolean.getSelectedIndex() == 1) {
				((ButtonGroupBoolean)cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLCOBRANCA)).bgBoolean.setSelectedIndex(1);
			}
		}
	}

	private void buttonGroupBooleanEnderecoPadraoChange(Event event) {
		if (cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGAPADRAO) != null && ((ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGAPADRAO)).bgBoolean == event.target) {
			ButtonGroupBoolean flEntrega = (ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGA);
			flEntrega.bgBoolean.setSelectedIndex(1);
		} else if (cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGA) != null && ((ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGA)).bgBoolean == event.target) {
			ButtonGroupBoolean flEntregaPadrao = (ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGAPADRAO);
			if (flEntregaPadrao.bgBoolean.getSelectedIndex() == 1) {
				((ButtonGroupBoolean)cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGA)).bgBoolean.setSelectedIndex(1);
			}
		}
	}
	
	private void cepValueChange() throws Exception {
		EditText edDsCep = (EditText) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEP);
		EditText edDsCepComercial = (EditText) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL);
		if (edDsCep != null) {
			consultaCEP(edDsCep.getValue(), false);
		} else if (edDsCepComercial != null) {
			consultaCEP(edDsCepComercial.getValue(), true);
		}
	}
	
	private void validaCep() throws Exception {
		EditTextMask edDsCep = (EditTextMask) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEP);
		EditTextMask edDsCepComercial = (EditTextMask) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL);
		if (edDsCep != null) {
			edDsCep.getValue();
		} else if (edDsCepComercial != null) {
			edDsCepComercial.getValue();
		}
	}
	
	private void consultaCEP(String dsCep, boolean cepComercial) throws Exception {
		Cep cep = CepUtil.cepOffLine(dsCep);
		boolean cepOffLine = true;
		if (cep == null) {
			cep = new Cep(dsCep);
			cepOffLine = !CepUtil.consultaCepOnline(cep);
		}
		
		setValoresCampos(cepComercial, cep, cepOffLine);		
	}

	private void setValoresCampos(boolean cepComercial, Cep cep, boolean cepOffLine) throws SQLException {
		Control edLogradouro = (Control) cadNovoCliEnderecoForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSLOGRADOUROCOMERCIAL : NovoCliEndereco.NMCOLUNA_DSLOGRADOURO);
		Control edBairro = (Control) cadNovoCliEnderecoForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSBAIRROCOMERCIAL : NovoCliEndereco.NMCOLUNA_DSBAIRRO);
		Control edCidade = (Control) cadNovoCliEnderecoForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSCIDADECOMERCIAL : NovoCliEndereco.NMCOLUNA_DSCIDADE);
		Control cbUf = (Control) cadNovoCliEnderecoForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_CDUFCOMERCIAL : NovoCliEndereco.NMCOLUNA_CDUF);
		Control edDsTipoLogradouro = (Control) cadNovoCliEnderecoForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSTIPOLOGRADOUROCOMERCIAL : NovoCliEndereco.NMCOLUNA_DSTIPOLOGRADOURO);
		
		setaValoresNosCamposDinamicos(cep, cepOffLine, edLogradouro, edBairro, edCidade, cbUf, edDsTipoLogradouro);
	}
	
	private void setaValoresNosCamposDinamicos(Cep cep, boolean cepOffline, Control edLogradouro, Control edBairro, Control edCidade, Control cbUf, Control edDsTipoLogradouro) throws SQLException {
		if (edLogradouro instanceof EditText) {
			String dsLogradouro = cepOffline ? LogradouroService.getInstance().getDsLogradouro(cep.cdLogradouro) : cep.dsLogradouro;
			if (ValueUtil.isNotEmpty(dsLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)	((EditText) edLogradouro).setValue(dsLogradouro);
		} else if (edLogradouro instanceof CampoDinamicoComboBox && (ValueUtil.isNotEmpty(cep.cdLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep)) {
			((CampoDinamicoComboBox) edLogradouro).setValue(cep.cdLogradouro);
		}
		if (edDsTipoLogradouro instanceof EditText) {
			String dsTipoLogradouro = cepOffline ? LogradouroService.getInstance().getDsTipoLogradouro(cep.cdLogradouro) : cep.dsLogradouro;
			if (ValueUtil.isNotEmpty(dsTipoLogradouro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) ((EditText) edDsTipoLogradouro).setValue(dsTipoLogradouro);
		}
		if (edBairro instanceof EditText) {
			String dsBairro = cepOffline ? BairroService.getInstance().getDsBairro(cep.cdBairro) : cep.dsBairro;
			if (ValueUtil.isNotEmpty(dsBairro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) ((EditText) edBairro).setValue(dsBairro);
		} else if (edBairro instanceof CampoDinamicoComboBox) {
			String dsBairro = cepOffline ? cep.cdBairro : cep.dsBairro;
			if (ValueUtil.isNotEmpty(dsBairro) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) ((CampoDinamicoComboBox) edBairro).setValue(dsBairro);
		}
		if (cbUf instanceof EditText) {
			String dsUf = CidadeService.getInstance().getUfCidade(cepOffline ? cep.cdCidade : cep.dsUf);
			if (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) ((EditText) cbUf).setValue(dsUf);
		} else if (cbUf instanceof CampoDinamicoComboBox) {
			String dsUf = cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.dsUf;
			if (ValueUtil.isNotEmpty(dsUf) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) ((CampoDinamicoComboBox) cbUf).setValue(dsUf);
		}
		if (edCidade instanceof EditText) {
			String dsCidade = cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade;
			if (ValueUtil.isNotEmpty(dsCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) ((EditText) edCidade).setValue(dsCidade);	
		}  else if (edCidade instanceof CampoDinamicoComboBox) {
			String dsUf = cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.dsUf;
			((CampoDinamicoComboBox) edCidade).loadComboRelacionada("TBLVWUF", NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL, dsUf);
			String cdCidade = cepOffline ? cep.cdCidade : cep.dsCidade;
			if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
				((CampoDinamicoComboBox) edCidade).setValue(cdCidade);
			}
			if (((CampoDinamicoComboBox) edCidade).getSelectedIndex() == -1) {
				Cidade cidade = new Cidade();
				cidade.nmCidade = StringUtil.changeStringAccented(cdCidade).toUpperCase();
				Vector list = CidadeService.getInstance().findAllByExample(cidade);
				if (list.size() > 0) {
					cidade = (Cidade) list.items[0];
					if (ValueUtil.isNotEmpty(cdCidade) || LavenderePdaConfig.usaServicoBuscaEnderecoPorCep) {
						((CampoDinamicoComboBox) edCidade).setValue(cidade.cdCidade);
					}
				}
			}
		}
	}	
	
	private void validaCpfCnpj() throws Exception {
		EditText edCnpj = (EditText) cadNovoCliEnderecoForm.getHashComponentes().get("NUCNPJ");
		if (edCnpj != null && ValueUtil.isNotEmpty(edCnpj.getValue()) && !CpfCnpjValidator.validateCnpjCpf(edCnpj.getValue())) {
			throw new Exception(Messages.NOVOCLIENTE_MSG_NUCNPJCPF_INVALIDO);
		}
		if (!LavenderePdaConfig.isObrigaCnpjParaEnderecosEntregaNovoCliente()) {
			return;
		}
		ButtonGroupBoolean bgFlEntrega = (ButtonGroupBoolean) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLENTREGA);
		if (edCnpj == null || bgFlEntrega == null) {
			return;
		}
		String tipoPessoa = getTipoPessoa();
		if (ValueUtil.valueEquals(Messages.TIPOPESSOA_FLAG_FISICA, tipoPessoa) && bgFlEntrega.getValueBoolean()) {
			throw new Exception(Messages.CLIENTEENDERECO_ENDENTREGA_JURIDICA);
		}
		if (ValueUtil.isEmpty(edCnpj.getValue()) && bgFlEntrega.getValueBoolean()) {
			throw new Exception(Messages.CLIENTEENDERECO_ENDENTREGA_CNPJVAZIO);
		}
	}
	
	private String getTipoPessoa() {
		CampoDinamicoComboBox combo = (CampoDinamicoComboBox) cadNovoCliEnderecoForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_FLTIPOPESSOA);
		if (combo == null) {
			return null;
		}
		Object valorSelecionado = combo.getSelectedItem();
		if (valorSelecionado == null ||  ValueUtil.valueEquals("", valorSelecionado)) {
			return null;
		}
		return  ((BaseComboBoxDomain) valorSelecionado).cdBaseComboDomain;
		
	}
		
}
