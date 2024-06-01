package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto2Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto3Service;
import br.com.wmw.lavenderepda.business.service.SenhaDinamicaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.EmpresaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FuncLiberaSenhaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadGeraSenhaWindow extends WmwWindow {

	private FuncLiberaSenhaComboBox cbFuncionalidade;
	private LabelValue lbSemente;
	private LabelValue lbSenha;
	private EditNumberInt edNumber1;
	private EditNumberInt edNumber2;
	private EditNumberFrac edGrupo1;
	private EditNumberFrac edGrupo2;
	private EditNumberFrac edGrupo3;
	private EditNumberFrac edqtPeso;
	private EditNumberFrac edVlProduto;
	private EditNumberFrac edVlPedido;
	private EditNumberFrac edQtProduto;
	private EditText edCliente;
	private EditText edProduto;
	private ButtonPopup btGerarSenha;
	private boolean validateCliente;
	private boolean validaProdutoVlQt;
	private boolean validaValorPedido;
	private boolean validaCdProduto;
	private boolean validaGrupo1;
	private boolean validaData;
	private LabelName lbFunc;
	private LabelName lbSementenm;
	private LabelName lbFator1;
	private LabelName lbFator2;
	private LabelName lbCliente;
	private LabelName lbProduto;
	private LabelName lbGrupo1;
	private LabelName lbGrupo2;
	private LabelName lbGrupo3;
	private LabelName lbQtProduto;
	private LabelName lbPesoExc;
	private LabelName lbVlProduto;
	private LabelName lbVlPedido;
	private LabelName lbSenhanm;
	public int cbFuncValue;
    private LabelName lbData;
    private EditDate edData;
    private EmpresaComboBox cbEmpresa;
    private RepresentanteSupervComboBox cbRepresentante;
    private LabelName lbEmpresaCb;
    private LabelName lbRepresentanteCb;
	
	public CadGeraSenhaWindow() throws SQLException {
		super(Messages.SENHADINAMICA_GERADOR_SENHA);
		cbFuncionalidade = new FuncLiberaSenhaComboBox();
		lbSemente = new LabelValue();
		lbSenha = new LabelValue();
		edNumber1 = new EditNumberInt("99999999", 6);
		edNumber2 = new EditNumberInt("99999999", 6);
		edGrupo1 = new EditNumberFrac("99999999", 10);
		edGrupo2 = new EditNumberFrac("99999999", 10);
		edGrupo3 = new EditNumberFrac("99999999", 10);
		edqtPeso = new EditNumberFrac("99999999", 10);
		edCliente = new EditText("", 10);
		edProduto = new EditText("", 10);
		edQtProduto = new EditNumberFrac("99999999", 6);
		edVlProduto = new EditNumberFrac("99999999", 10);
		edVlPedido = new EditNumberFrac("99999999", 10);
		btGerarSenha = new ButtonPopup(Messages.SENHADINAMICA_GERAR_SENHA);
		lbFator2 = new LabelName(Messages.SENHADINAMICA_LABEL_FATOR_2);
		lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		lbGrupo1 = new LabelName(GrupoProduto1Service.getInstance().getLabelGrupoProduto1());
		lbGrupo2 = new LabelName(GrupoProduto2Service.getInstance().getLabelGrupoProduto2());
		lbGrupo3 = new LabelName(GrupoProduto3Service.getInstance().getLabelGrupoProduto3());
		lbPesoExc = new LabelName(Messages.GRUPOPRODUTO_QTPESO_EXCE_ABREV);
		lbProduto = new LabelName(Messages.SENHADINAMICA_LABEL_CDPRODUTO);
		lbQtProduto = new LabelName(Messages.SENHADINAMICA_LABEL_QTD);
		lbVlProduto = new LabelName(Messages.SENHADINAMICA_LABEL_VALOR);
		lbVlPedido = new LabelName(Messages.SENHADINAMICA_LABEL_VALOR_PEDIDO);
		lbFunc = new LabelName(Messages.SENHADINAMICA_LABEL_FUNCIONALIDADE);
		lbSementenm = new LabelName(Messages.SENHADINAMICA_LABEL_SEMENTE);
		lbFator1 = new LabelName(Messages.SENHADINAMICA_LABEL_FATOR_1);
		lbSenhanm = new LabelName(Messages.SENHADINAMICA_LABEL_SENHA);
        lbData = new LabelName(Messages.SENHADINAMICA_LABEL_DATA);
        edData = new EditDate();
        lbEmpresaCb = new LabelName(Messages.SENHADINAMICA_LABEL_EMPRESA);
        cbEmpresa = new EmpresaComboBox(Messages.SENHADINAMICA_LABEL_EMPRESA, BaseComboBox.DefaultItemType_SELECT_ONE_ITEM);
        cbEmpresa.loadEmpresa();
        
        lbRepresentanteCb = new LabelName(Messages.SENHADINAMICA_LABEL_REPRESENTANTE);
        cbRepresentante = new RepresentanteSupervComboBox();
        cbRepresentante.carregaRepresentantes();
        cbFuncionalidade.setSelectedIndex(0);
		setDefaultRect();
		SessionLavenderePda.cdEmpresaOld = SessionLavenderePda.cdEmpresa;
		SessionLavenderePda.setRepresentanteOld(SessionLavenderePda.getRepresentante().cdRepresentante, SessionLavenderePda.getRepresentante().nmRepresentante);
		
	}

	//@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbFunc, cbFuncionalidade, getLeft(), getTop() + (HEIGHT_GAP * 2));
		if (cbFuncionalidade.getValue() == ValorParametro.SEMENTESENHAVISITACLIENTEFORAAGENDA) {
			UiUtil.add(this, lbEmpresaCb, cbEmpresa, getLeft(), AFTER + HEIGHT_GAP);
			cbEmpresa.setSelectedIndex(0);
		} else {
			scBase.remove(cbEmpresa);
			scBase.remove(lbEmpresaCb);
		}
		if (cbFuncionalidade.getValue() == ValorParametro.SEMENTESENHAVISITACLIENTEFORAAGENDA) {
			UiUtil.add(this, lbRepresentanteCb, cbRepresentante, getLeft(), AFTER + HEIGHT_GAP);
			cbRepresentante.setSelectedIndex(0);
		} else {
			scBase.remove(cbRepresentante);
		}
		if (!LavenderePdaConfig.ocultaSementeTelasGeracaoSenha) {
			UiUtil.add(this, lbSementenm, lbSemente, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(this, lbFator1, edNumber1, getLeft(), AFTER + HEIGHT_GAP);
		if (validaProdutoVlQt || validaValorPedido) {
			scBase.remove(lbFator2);
			scBase.remove(edNumber2);
		} else {
			UiUtil.add(this, lbFator2, edNumber2, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (validateCliente) {
			UiUtil.add(this, lbCliente, edCliente, getLeft(), AFTER + HEIGHT_GAP);
		} else {
			scBase.remove(lbCliente);
			scBase.remove(edCliente);
		}
		if (!validaGrupo1) {
			scBase.remove(lbGrupo1);
			scBase.remove(edGrupo1);
			scBase.remove(lbGrupo2);
			scBase.remove(edGrupo2);
			scBase.remove(lbGrupo3);
			scBase.remove(edGrupo3);
			scBase.remove(lbPesoExc);
			scBase.remove(edqtPeso);
		} else {
			scBase.remove(lbFator2);
			scBase.remove(edNumber2);
			scBase.remove(lbCliente);
			scBase.remove(edCliente);
			UiUtil.add(this, lbGrupo1, edGrupo1, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(this, lbGrupo2, edGrupo2, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(this, lbGrupo3, edGrupo3, getLeft(), AFTER + HEIGHT_GAP);
			UiUtil.add(this, lbPesoExc, edqtPeso, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (validaCdProduto) {
			UiUtil.add(this, lbProduto, edProduto, getLeft(), AFTER + HEIGHT_GAP);
		} else {
			scBase.remove(lbProduto);
			scBase.remove(edProduto);
		}
		if (validaData) {
			UiUtil.add(this, lbData, edData, getLeft(), AFTER + HEIGHT_GAP);
		} else {
			scBase.remove(lbData);
			scBase.remove(edData);
		}
		if (validaProdutoVlQt || validaValorPedido) {
			if (validaProdutoVlQt) {
				UiUtil.add(this, lbQtProduto, edQtProduto, getLeft(), AFTER + HEIGHT_GAP);
				UiUtil.add(this, lbVlProduto, edVlProduto, getLeft(), AFTER + HEIGHT_GAP);
			} else {
				removeComponentesProduto();
			}
			if (validaValorPedido) {
				UiUtil.add(this, lbVlPedido, edVlPedido, getLeft(), AFTER + HEIGHT_GAP);
			} else {
				removeComponentesPedido();
			}
		} else {
			removeComponentesProduto();
			removeComponentesPedido();
		}
		UiUtil.add(this, lbSenhanm, lbSenha, getLeft(), AFTER + HEIGHT_GAP);
		if (cbFuncionalidade.getValue() == BaseComboBox.DefaultItemNull) removeTodosComponentes();
		addButtonPopup(btGerarSenha);
		addButtonPopup(btFechar);
		setEnabled();
	}

	private void removeTodosComponentes() {
		scBase.remove(lbPesoExc);
		scBase.remove(edqtPeso);
		scBase.remove(lbProduto);
		scBase.remove(edProduto);
		removeComponentesProduto();
		removeComponentesPedido();
		scBase.remove(lbSementenm);
		scBase.remove(lbSemente);
		scBase.remove(lbFator1);
		scBase.remove(edNumber1);
		scBase.remove(lbFator2);
		scBase.remove(edNumber2);
		scBase.remove(lbCliente);
		scBase.remove(edCliente);
		scBase.remove(lbSenhanm);
		scBase.remove(lbSenha);
		scBase.remove(lbData);
		scBase.remove(edData);
		scBase.remove(lbEmpresaCb);
		scBase.remove(cbEmpresa);
		scBase.remove(lbGrupo1);
		scBase.remove(edGrupo1);
		scBase.remove(lbGrupo2);
		scBase.remove(edGrupo2);
		scBase.remove(lbGrupo3);
		scBase.remove(edGrupo3);
	}

	private void removeComponentesPedido() {
		scBase.remove(lbVlPedido);
		scBase.remove(edVlPedido);
	}

	private void removeComponentesProduto() {
		scBase.remove(lbQtProduto);
		scBase.remove(edQtProduto);
		scBase.remove(lbVlProduto);
		scBase.remove(edVlProduto);
	}

	protected void addBtFechar() {
	}

	public void setEnabled() {
		final boolean b = cbFuncionalidade.getValue() != 0;
		edNumber1.setEnabled(b);
		edNumber2.setEnabled(b);
		edGrupo1.setEnabled(b);
		edGrupo2.setEnabled(b);
		edGrupo3.setEnabled(b);
		edqtPeso.setEnabled(b);
		edCliente.setEnabled(b);
		btGerarSenha.setEnabled(b);
		edData.setEnabled(b);
	}

	//@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbFuncionalidade) {
					cbFuncionalidadeClick();
					break;
				} else if (event.target == btGerarSenha) {
					validateFields();
					int senha;
					if (validaValorPedido) {
						String cdUsuario = cbFuncionalidade.getValue() == ValorParametro.LIBERACAOPORUSUARIOEALCADA ? Session.getCdUsuario() : "1";
						senha = SenhaDinamicaService.getSenhaCorreta(edNumber1.getValueInt(), lbSemente.getIntegerValue(),  edCliente.getText(), edVlPedido.getText(), cdUsuario);
						senha = senha >= 0 ? senha : senha*-1;
					} else if (validaProdutoVlQt) {
						senha = SenhaDinamicaService.getSenhaCorreta(edNumber1.getValueInt(), edNumber2.getValueInt(), lbSemente.getIntegerValue(), edCliente.getValue(), edProduto.getValue(), edQtProduto.getValueDouble(), edVlProduto.getValueDouble());
						String senhaStr = StringUtil.getStringValue(senha);
						if(senhaStr.length() > 6){
							senhaStr = senhaStr.substring(senhaStr.length() - 6);
						}
						senha = ValueUtil.getIntegerValue(senhaStr);
					} else 	if (validaData) {
						senha = SenhaDinamicaService.getSenhaCorreta(edNumber1.getValueInt(), edNumber2.getValueInt(), lbSemente.getIntegerValue(), edData.getValue(), Session.getCdUsuario());
					} else 	if (validaCdProduto) {
						senha = SenhaDinamicaService.getSenhaProduto(edNumber1.getValueInt(), edNumber2.getValueInt(), lbSemente.getIntegerValue(), edProduto.getValue());
					} else 	if (!validaGrupo1) {
						senha = SenhaDinamicaService.getSenhaCorreta(edNumber1.getValueInt(), edNumber2.getValueInt(), lbSemente.getIntegerValue(), edCliente.getValue());
					} else {
						senha = SenhaDinamicaService.getSenhaCorreta(edNumber1.getValueInt(),  lbSemente.getIntegerValue(), StringUtil.getStringValue(edGrupo1.getValueDouble()), StringUtil.getStringValue(edGrupo2.getValueDouble()), StringUtil.getStringValue(edGrupo3.getValueDouble()), edqtPeso.getValueDouble());
					}
					lbSenha.setValue(senha);
					break;
				}
				if (event.target == cbEmpresa) {
			        SessionLavenderePda.cdEmpresa = cbEmpresa.getValue();
					cbRepresentante.setSelectedIndex(0);
					lbSemente.setValue(ValueUtil.getIntegerValue(getValorParametro().vlParametro));
					break;
				}
				if (event.target == cbRepresentante) {
					SessionLavenderePda.cdEmpresa = SessionLavenderePda.cdEmpresaOld;
					cbEmpresa.setSelectedIndex(0);
					lbSemente.setValue(ValueUtil.getIntegerValue(getValorParametro().vlParametro));
					break;
				}
			}
		}
	}

	private void cbFuncionalidadeClick() throws SQLException {
		clearCampos();
		validaData = false;
		validaValorPedido = false;
		if (cbFuncionalidade.getValue() == 0) {
        	lbSemente.setValue("");
        } else if (cbFuncionalidade.getValue() == ValorParametro.NUDIASLIBERACAOCOMSENHACLIENTEATRASADONOVOPEDIDO) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaClienteAtrasadoNovoPedido);
        	validateCliente = true;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE || cbFuncionalidade.getValue() == ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTEAOFECHARPEDIDO || cbFuncionalidade.getValue() == ValorParametro.LIBERACAOPORUSUARIOEALCADA) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaLimiteCreditoCliente);
        	validateCliente = true;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        	validaValorPedido = cbFuncionalidade.getValue() == ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTEAOFECHARPEDIDO || cbFuncionalidade.getValue() == ValorParametro.LIBERACAOPORUSUARIOEALCADA;
        } else if (cbFuncionalidade.getValue() == (ValorParametro.LIBERACOMSENHAPRECODEVENDA)) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaPrecoDeVenda);
        	validateCliente = true;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == (ValorParametro.LIBERACOMSENHATIPOPEDIDOFLEXIGESENHA)) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaTipoPedido);
        	validateCliente = true;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == (ValorParametro.OBRIGAENVIODEPEDIDOSPORTEMPODECORRIDO)) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaObrigaEnvioDePedidos);
        	validateCliente = false;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == (ValorParametro.BLOQUEIASISTEMAGPSINATIVO)) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaBloqueiaSistemaGpsInativo);
        	validateCliente = false;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == (ValorParametro.OBRIGARECEBERDADOSBLOQUEIANOVOPEDIDO)) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaObrigaReceberDadosBloqueiaNovoPedido);
        	validateCliente = false;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == (ValorParametro.OBRIGARECEBERDADOSBLOQUEIAUSOSISTEMA)) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaObrigaReceberDadosBloqueiaUsoSistema);
        	validateCliente = false;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == (ValorParametro.NUDIASBLOQUEIACLIENTESEMPEDIDO)) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaClienteSemPedido);
        	validateCliente = false;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.LIBERACOMSENHACLIENTEREDEATRASADONOVOPEDIDO) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaClienteRedeAtrasadoNovoPedido);
        	validateCliente = true;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.LIBERACOMSENHAVENDAPRODUTOBLOQUEADO) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaLiberaComSenhaVendaProdutoBloqueado);
        	validateCliente = false;
        	validaGrupo1 = true;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.HORALIMITEPARAENVIOPEDIDOS) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaHoraLimiteEnvioPedidos);
        	validateCliente = false;
        	validaGrupo1 = false;
        	validaProdutoVlQt = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.LIBERACOMSENHAPRECOPRODUTO) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaLiberaPrecoProduto);
        	validateCliente = true;
        	validaProdutoVlQt = true;
        	validaGrupo1 = false;
        	validaCdProduto = true;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.LIBERASENHAQTDITEMMAIORPEDIDOORIGINAL) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaQtdItemMaiorPedidoOriginal);
        	validateCliente = false;
        	validaProdutoVlQt = false;
        	validaGrupo1 = false;
        	validaCdProduto = true;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.LIBERASENHADIAENTREGAPEDIDO) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaDiaEntregaPedido);
        	validateCliente = false;
        	validaProdutoVlQt = false;
        	validaGrupo1 = false;
        	validaCdProduto = false;
        	validaData = true;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	edData = new EditDate();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.SEMENTESENHASALDOBONIFICACAOEXTRAPOLADO) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaSaldoBonificacaoExtrapolado);
        	validateCliente = true;
        	validaProdutoVlQt = false;
        	validaGrupo1 = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.SEMENTESENHALIBERACAORENTABILIDADEMINIMA) {
        	lbSemente.setValue(LavenderePdaConfig.sementeSenhaLiberacaoRentabilidadeMinima);
        	validateCliente = true;
        	validaProdutoVlQt = false;
        	validaGrupo1 = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        } else if (cbFuncionalidade.getValue() == ValorParametro.SEMENTESENHAVISITACLIENTEFORAAGENDA) {
        	lbSemente.setValue(ValueUtil.getIntegerValue(getValorParametro().vlParametro));
        	validateCliente = true;
        	validaProdutoVlQt = false;
        	validaGrupo1 = false;
        	validaCdProduto = false;
        	cbFuncValue = cbFuncionalidade.getSelectedIndex();
        	cbFuncionalidade.setSelectedIndex(cbFuncValue);
        }
		initUI();
		setEnabled();
	}

	private ValorParametro getValorParametro() throws SQLException {
		ValorParametro parametroPorRepresentante = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.SEMENTESENHAVISITACLIENTEFORAAGENDA, cbEmpresa.getValue(), cbRepresentante.getValue());
		if (parametroPorRepresentante == null) {
			return new ValorParametro();
		}
		return parametroPorRepresentante;
	}

	private void clearCampos() {
		edNumber1.setText("");
		edNumber2.setText("");
		lbSenha.setText("");
		edCliente.setText("");
		edGrupo1.setText("");
		edGrupo2.setText("");
		edGrupo3.setText("");
		edqtPeso.setText("");
		edProduto.setText("");
		edQtProduto.setText("");
		edVlProduto.setText("");
		edVlPedido.setText("");
		edData.setText("");
	}

	private void validateFields() {
		if (lbSemente.getIntegerValue() == 0) {
			throw new ValidationException(Messages.SENHADINAMICA_SEMENTE_INVALIDA);
		}
		if (edNumber1.getValueInt() == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SENHADINAMICA_LABEL_FATOR_1);
		}
		if (edNumber2.getValueInt() ==0 && !validaGrupo1 && !validaProdutoVlQt && !validaValorPedido) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SENHADINAMICA_LABEL_FATOR_2);
		}
		if (ValueUtil.isEmpty(edCliente.getValue()) && (validateCliente || validaProdutoVlQt)) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CLIENTE_NOME_ENTIDADE);
		}
		if (ValueUtil.isEmpty(edProduto.getValue()) && validaCdProduto) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SENHADINAMICA_LABEL_CDPRODUTO);
		}
		if (edQtProduto.getValueDouble() == 0 && validaProdutoVlQt) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SENHADINAMICA_LABEL_QTD);
		}
		if (edVlProduto.getValueDouble() == 0 && validaProdutoVlQt) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SENHADINAMICA_LABEL_VALOR);
		}
		if (edGrupo1.getValueDouble() == 0 && validaGrupo1) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, GrupoProduto1Service.getInstance().getLabelGrupoProduto1());
		}
		if (ValueUtil.isEmpty(edData.getValue()) && validaData) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SENHADINAMICA_LABEL_DATA);
		}
		if (edVlPedido.getValueDouble() == 0 && validaValorPedido) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.SENHADINAMICA_LABEL_VALOR_PEDIDO);
		}
	}
	
	@Override
	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		SessionLavenderePda.cdEmpresa = SessionLavenderePda.cdEmpresaOld;
		SessionLavenderePda.setRepresentante(SessionLavenderePda.representanteOld);
	}
	
}
