package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CampoDinamicoComboBox;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilterDyn;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cep;
import br.com.wmw.lavenderepda.business.domain.Cidade;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.service.BairroService;
import br.com.wmw.lavenderepda.business.service.CidadeService;
import br.com.wmw.lavenderepda.business.service.ClienteAtuaService;
import br.com.wmw.lavenderepda.business.service.LogradouroService;
import br.com.wmw.lavenderepda.util.CepUtil;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadClienteAtuaDynWindow extends WmwWindow {

	private CadClienteAtuaDynForm cadClienteAtuaDynForm;
	private ButtonPopup btSalvar;
	private boolean exibeMensagem;
	public boolean updateSucess;
	private boolean ativaBuscaPorCodigoCidade;

	public CadClienteAtuaDynWindow(Cliente cliente, boolean exibeMensagem) throws SQLException {
		super(Messages.CLIENTEATUA_TITULO_TELA);
		cadClienteAtuaDynForm = new CadClienteAtuaDynForm();
		cadClienteAtuaDynForm.setCliente(cliente);
		btSalvar = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		btFechar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		this.exibeMensagem = exibeMensagem;
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadClienteAtuaDynForm, LEFT, getTop(), FILL, FILL);
		addButtonPopup(btSalvar);
		addButtonPopup(btFechar);
		exibeMensagemClienteRevisaoCadastral();
	}
	
	@Override
	protected void addBtFechar() {
		//Não tem
	}

	@Override
	public void popup() {
		try {
			ClienteAtua clienteAtuaFilter = new ClienteAtua();
			clienteAtuaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			clienteAtuaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			clienteAtuaFilter.cdCliente = cadClienteAtuaDynForm.getCliente().cdCliente;
			Vector clienteAtuaList = ClienteAtuaService.getInstance().findAllByExample(clienteAtuaFilter);
			if (ValueUtil.isEmpty(clienteAtuaList)) {
				cadClienteAtuaDynForm.add();
			} else {
				clienteAtuaFilter = (ClienteAtua) clienteAtuaList.items[0];
				ClienteAtua clienteAtuaDyn = (ClienteAtua) ClienteAtuaService.getInstance().findByRowKeyDyn(clienteAtuaFilter.getRowKey());
				cadClienteAtuaDynForm.edit(clienteAtuaDyn);
			}
			atualizaComboRelacionadaUF(cadClienteAtuaDynForm.getCliente().cdEstadoComercial);
			cadClienteAtuaDynForm.onFormShow();
			super.popup();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void atualizaComboRelacionadaUF(String cdEstadoComercial) throws SQLException {
		boolean cepComercial = cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL) != null;
		Control edCidade = (Control) cadClienteAtuaDynForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSCIDADECOMERCIAL : NovoCliEndereco.NMCOLUNA_DSCIDADE);
		Control cbUf = (Control) cadClienteAtuaDynForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_CDUFCOMERCIAL : NovoCliEndereco.NMCOLUNA_CDUF);
		if (cbUf == null) {
			ativaBuscaPorCodigoCidade = true;
			edCidade = (Control) cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_CDCIDADECOMERCIAL); 
			cbUf = (Control) cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL);
		}		
		if (edCidade instanceof CampoDinamicoComboBox) {
			((CampoDinamicoComboBox) edCidade).loadComboRelacionada("TBLVWUF", NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL, cdEstadoComercial);
		}		
	}

	@Override
	public void fecharWindow() throws SQLException {
		cadClienteAtuaDynForm.onFormClose();
		super.fecharWindow();
	}

	@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btSalvar) {
						try {
							cadClienteAtuaDynForm.salvarClick();
							updateSucess = true;
							unpop();
						} catch (ValidationException ex) {
							UiUtil.showErrorMessage(ex);
							updateSucess = false;
						}
					}
					break;
				}
				case ValueChangeEvent.VALUE_CHANGE: {
					if (event.target == cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEP) || event.target == cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL)) {
						EditText edDsCep = (EditText) cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEP);
						EditText edDsCepComercial = (EditText) cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSCEPCOMERCIAL);
						if (edDsCep != null) {
							consultaCEP(edDsCep.getValue(), false);
						} else if (edDsCepComercial != null) {
							consultaCEP(edDsCepComercial.getValue(), true);
						} 							
					}				
				}
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
		}
	}

	@Override
	protected void btFecharClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.MSG_CONFIRM_CANCELAR_ATUALIZACAO_DADOSCLI)) {
			super.btFecharClick();
		}
	}
	
	private void consultaCEP(String dsCep, boolean cepComercial) throws Exception {
		if (dsCep == null ) {
			return;
		}
		Cep cep = CepUtil.cepOffLine(dsCep);
		boolean cepOffLine = true;
		if (cep == null) {
			cep = new Cep(dsCep);
			cepOffLine = !CepUtil.consultaCepOnline(cep);
		}
		Control edLogradouro = (Control) cadClienteAtuaDynForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSLOGRADOUROCOMERCIAL : NovoCliEndereco.NMCOLUNA_DSLOGRADOURO);
		Control edBairro = (Control) cadClienteAtuaDynForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSBAIRROCOMERCIAL : NovoCliEndereco.NMCOLUNA_DSBAIRRO);
		Control edCidade = (Control) cadClienteAtuaDynForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSCIDADECOMERCIAL : NovoCliEndereco.NMCOLUNA_DSCIDADE);
		Control cbUf = (Control) cadClienteAtuaDynForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_CDUFCOMERCIAL : NovoCliEndereco.NMCOLUNA_CDUF);
		if (cbUf == null) {
			ativaBuscaPorCodigoCidade = true;
			edCidade = (Control) cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_CDCIDADECOMERCIAL); 
			cbUf = (Control) cadClienteAtuaDynForm.getHashComponentes().get(NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL);
		}
		Control edDsTipoLogradouro = (Control) cadClienteAtuaDynForm.getHashComponentes().get(cepComercial ? NovoCliEndereco.NMCOLUNA_DSTIPOLOGRADOUROCOMERCIAL : NovoCliEndereco.NMCOLUNA_DSTIPOLOGRADOURO);
		
		setaValoresNosCamposDinamicos(cep, cepOffLine, edLogradouro, edBairro, edCidade, cbUf, edDsTipoLogradouro);		
	}
	
	private void setaValoresNosCamposDinamicos(Cep cep, boolean cepOffline, Control edLogradouro, Control edBairro, Control edCidade, Control cbUf, Control edDsTipoLogradouro) throws SQLException {
		if (edLogradouro instanceof EditText) {
			((EditText) edLogradouro).setValue(cepOffline ? LogradouroService.getInstance().getDsLogradouro(cep.cdLogradouro) : cep.dsLogradouro);
		} else if (edLogradouro instanceof CampoDinamicoComboBox) {
			((CampoDinamicoComboBox) edLogradouro).setValue(cep.cdLogradouro);
		}
		if (edDsTipoLogradouro instanceof EditText) {
			((EditText) edDsTipoLogradouro).setValue(cepOffline ? LogradouroService.getInstance().getDsTipoLogradouro(cep.cdLogradouro) : cep.dsLogradouro);
		}
		if (edBairro instanceof EditText) {
			((EditText) edBairro).setValue(cepOffline ? BairroService.getInstance().getDsBairro(cep.cdBairro) : cep.dsBairro);
		} else if (edBairro instanceof CampoDinamicoComboBox) {
			((CampoDinamicoComboBox) edBairro).setValue(cepOffline ? cep.cdBairro : cep.dsBairro);
		}
		if (ativaBuscaPorCodigoCidade) {
			Cidade cidade = new Cidade();
			cidade.nmCidade = StringUtil.changeStringAccented(cep.dsCidade).toUpperCase();
			Vector list = CidadeService.getInstance().findAllByExample(cidade);
			if (list.size() > 0) {
				cidade = (Cidade) list.items[0];
				cep.cdCidade = cidade.cdCidade;
			}
		}
		if (edCidade instanceof EditText) {
			((EditText) edCidade).setValue(cepOffline ? CidadeService.getInstance().getNmCidade(cep.cdCidade) : cep.dsCidade);	
		} else if (edCidade instanceof CampoDinamicoComboBox) {
			((CampoDinamicoComboBox) edCidade).loadComboRelacionada("TBLVWUF", NovoCliEndereco.NMCOLUNA_CDESTADOCOMERCIAL, cep.dsUf);
			((CampoDinamicoComboBox) edCidade).setValue(cepOffline ? cep.cdCidade : ativaBuscaPorCodigoCidade ?  cep.cdCidade : cep.dsCidade);
		} else if (edCidade instanceof PopUpSearchFilterDyn) {
			((PopUpSearchFilterDyn) edCidade).setValue(cepOffline ? cep.cdCidade : cep.dsCidade);
		}
		if (cbUf instanceof EditText) {
			((EditText) cbUf).setValue(CidadeService.getInstance().getUfCidade(cepOffline ? cep.cdCidade : cep.dsUf));
		} else if (cbUf instanceof CampoDinamicoComboBox) {
			((CampoDinamicoComboBox) cbUf).setValue(cepOffline ? CidadeService.getInstance().getUfCidade(cep.cdCidade) : cep.dsUf);
		}
	}
	
	private void exibeMensagemClienteRevisaoCadastral() {
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente == 0 || !this.exibeMensagem) {
			return;
		}
		Cliente cliente = cadClienteAtuaDynForm.getCliente();
		if (cliente == null || ValueUtil.isEmpty(cliente.dtAtualizacaoCadastro)) {
			return;
		}
		int nuDias = DateUtil.getDaysBetween(DateUtil.getCurrentDate(), cliente.dtAtualizacaoCadastro);
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente >= nuDias) {
			return;
		}
		
		UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.CLIENTE_SEM_REVISAO_CADASTRAL, LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente));
		
	}

}
