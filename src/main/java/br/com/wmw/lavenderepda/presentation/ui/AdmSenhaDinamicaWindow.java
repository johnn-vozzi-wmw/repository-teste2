package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditTimeMask;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto2Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto3Service;
import br.com.wmw.lavenderepda.business.service.SenhaDinamicaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UsuarioRelRepComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class AdmSenhaDinamicaWindow extends WmwWindow {

    public static final boolean SENHA_VALIDA = true;

    private LabelName lbFator1;
    private LabelValue lvFator1;
    private LabelName lbFator2;
    private LabelValue lvFator2;
    private LabelName lbClientenm;
    private LabelValue lbCliente;
    private LabelName lbNuCnpjnm;
    private LabelValue lbNuCnpj;
    private LabelName lbProdutonm;
    private LabelValue lbProduto;
    public LabelName lbQtdeProduto;
    public EditNumberFrac edQtdProduto;
    public LabelName lbValorProduto;
    public EditNumberFrac edValorProduto;
    private LabelName lbSenhanm;
    private EditNumberInt edSenha;
    private LabelName lbVlTotalPedidonm;
    private LabelValue lbVlTotalPedido;
    private LabelName lbCdGrupoProduto1nm;
    private LabelName lbCdGrupoProduto2nm;
    private LabelName lbCdGrupoProduto3nm;
    private LabelValue lbCdGrupoProduto1;
    private LabelValue lbCdGrupoProduto2;
    private LabelValue lbCdGrupoProduto3;
    private LabelName lbPesoExcedentenm;
    private EditNumberFrac edPesoExcedente;
    private ButtonPopup btConfirma;
    private ProdutoComboBox cbProduto;
    private LabelValue lvProduto;
    private LabelName lbCodigo;
    private LabelName lbCdUsuario;
    private LabelValue lvCdUsuario;
    private UsuarioRelRepComboBox cbUsuarioRelRep;
    private LabelValue edMensagem;
    private CheckBoolean ckLiberacaoTipoPedido;
    private CheckBoolean ckLiberacaoClienteAtrasado;
    private CheckBoolean ckLiberacaoLimiteCredito;
    private CheckBoolean ckLiberacaoVisitaForaAgenda;
    private CheckBoolean ckLiberacaoBonificacaoSaldo;
    private LabelName lbData;
    private EditDate edData;
    private LabelName lbCondPagto;
    private LabelValue lvCdCondPagto;
    private LabelName lbTempoLiberacao;
    public EditTimeMask edTempoLiberacao;

    private boolean flSenhaValida;
    private boolean usaCdProduto;
    private boolean usaApenasCdProduto;
    private boolean usaCdCliente;
    private boolean usaVlPedido;
    private boolean usacdGrupoProduto1;
    private boolean usacdGrupoProduto2;
    private boolean usacdGrupoProduto3;
    private boolean usaCbProduto;
    private boolean usaCdUsuario;
    private boolean usaNuCnpj;
    private boolean usaData;
    private boolean usaCdCondPagto;
    private boolean showFuncionalidadesExtras;
    public boolean bloqueiaEdicaoQuantidadeItem;
    public boolean bloqueiaEdicaoValorItem;
    
    private int chaveSemente;
    private String cdUsuario;
    private Vector cdProdutoList;
    private double pesoExcedente;
    private String cdGrupoProduto1;
    private String cdGrupoProduto2;
    private String cdGrupoProduto3;
    private double vlTotalPedido;
    private String mensagem;
    private String cdCondicaoPagamento;

    public double vlPesoLiberado;
    public String cdProdutoLiberado;
    public String cdUsuarioLiberado;
    public boolean editouValoresProduto;
    
    public AdmSenhaDinamicaWindow(int chaveSemente) throws SQLException {
    	super(Messages.SENHADINAMICA_TITULO);
    	this.chaveSemente = chaveSemente;
    	criaComponentesTela();
	}

    public AdmSenhaDinamicaWindow() throws SQLException {
        super(Messages.SENHADINAMICA_TITULO);
        criaComponentesTela();
    }

	private void criaComponentesTela() throws SQLException {
		edMensagem = new LabelValue("@@@");
		lbFator1 = new LabelName(Messages.SENHADINAMICA_LABEL_FATOR_1);
        lvFator1 = new LabelValue(SenhaDinamicaService.getFator1());
        lbFator2 = new LabelName(Messages.SENHADINAMICA_LABEL_FATOR_2);
        lvFator2 = new LabelValue(SenhaDinamicaService.getFator2());
        lbClientenm = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
        lbNuCnpjnm = new LabelName(Messages.NOVOCLIENTE_CNPJ);
        lbData = new LabelName(Messages.DATA_LABEL_DATA);
        edData = new EditDate();
        edData.setEditable(false);
        lbCliente = new LabelValue();
        lbNuCnpj = new LabelValue();
        lbProdutonm = new LabelName(Messages.PRODUTO_NOME_ENTIDADE);
        lbProduto = new LabelValue();
        lbQtdeProduto = new LabelName(Messages.SENHADINAMICA_LABEL_QTD);
        edQtdProduto = new EditNumberFrac("9999999", 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        edQtdProduto.autoSelect = true;
        lbValorProduto = new LabelName(Messages.SENHADINAMICA_LABEL_VALOR);
        edValorProduto = new EditNumberFrac("9999999", 9);
        edValorProduto.autoSelect = true;
        lbVlTotalPedidonm = new LabelName(Messages.FRETE_VL_PEDIDO);
        lbVlTotalPedido = new LabelValue();
        lbCdGrupoProduto1nm = new LabelName(GrupoProduto1Service.getInstance().getLabelGrupoProduto1());
        lbCdGrupoProduto1 = new LabelValue();
        lbCdGrupoProduto2nm = new LabelName(GrupoProduto2Service.getInstance().getLabelGrupoProduto2());
        lbCdGrupoProduto2 = new LabelValue();
        lbCdGrupoProduto3nm = new LabelName(GrupoProduto3Service.getInstance().getLabelGrupoProduto3());
        lbCdGrupoProduto3 = new LabelValue();
        lbPesoExcedentenm = new LabelName(Messages.LABEL_PESO_EXCEDIDO);
        edPesoExcedente = new EditNumberFrac("9999999", 9);
        cbProduto = new ProdutoComboBox();
        lvProduto = new LabelValue();
        lbCodigo = new LabelName(Messages.PRODUTO_LABEL_CODIGO);
        lbSenhanm = new LabelName(Messages.SENHADINAMICA_LABEL_SENHA);
        edSenha = new EditNumberInt("999999999", 10, BaseEdit.EDIT_TYPE_ONLY_NUMBER_INT);
		edSenha.setID("edSenha");
        btConfirma = new ButtonPopup(Messages.SENHADINAMICA_CONFIRMA_SENHA);
		btConfirma.setID("btConfirma");
        lbCdUsuario = new LabelName(Messages.SENHADINAMICA_LABEL_CDUSUARIO);
        lvCdUsuario = new LabelValue();
        cbUsuarioRelRep = new UsuarioRelRepComboBox(Messages.USUARIO_NOME_ENTIDADE, isLiberacaoPorUsuarioEAlcada());
        edTempoLiberacao = new EditTimeMask(EditTimeMask.FORMATO_HHMM);
        lbTempoLiberacao = new LabelName("#Tempo");
        if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica) {
        	ckLiberacaoTipoPedido = new CheckBoolean(Messages.SENHADINAMICA_LIBERACAO_TIPO_PEDIDO);
        	ckLiberacaoClienteAtrasado = new CheckBoolean(Messages.SENHADINAMICA_LIBERACAO_CLIENTE_ATRASADO);
        	ckLiberacaoLimiteCredito = new CheckBoolean(Messages.SENHADINAMICA_LIBERACAO_LIMITE_CREDITO);
        	ckLiberacaoVisitaForaAgenda = new CheckBoolean(Messages.SENHADINAMICA_LIBERACAO_VISITA_FORA_AGENDA);
        	ckLiberacaoBonificacaoSaldo = new CheckBoolean(Messages.SENHADINAMICA_LIBERASENHABONIFICACAOSALDO);
        }
        lbCondPagto = new LabelName(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO);
        lvCdCondPagto = new LabelValue();
	}

	public void popup() {
        setDefaultRect();
        super.popup();
    }

	public void initUI() {
		try {
			super.initUI();
			UiUtil.add(this, edMensagem, getLeft(), getNextY(), FILL - WIDTH_GAP_BIG);
			edMensagem.setMultipleLinesText(mensagem);
			UiUtil.add(this, lbFator1, lvFator1, getLeft(), getNextY() + HEIGHT_GAP);
			if (mostraLbFator2()) {
				UiUtil.add(this, lbFator2, lvFator2, getLeft(), getNextY());
			}
			if (usaVlPedido) {
				UiUtil.add(this, lbVlTotalPedidonm, lbVlTotalPedido, getLeft(), getNextY());
			}
			if (usacdGrupoProduto1) {
				UiUtil.add(this, lbCdGrupoProduto1nm, lbCdGrupoProduto1, getLeft(), getNextY());
				if (usacdGrupoProduto2) {
					UiUtil.add(this, lbCdGrupoProduto2nm, lbCdGrupoProduto2, getLeft(), getNextY());
					if (usacdGrupoProduto3) {
						UiUtil.add(this, lbCdGrupoProduto3nm, lbCdGrupoProduto3, getLeft(), getNextY());
					}
				}
				UiUtil.add(this, lbPesoExcedentenm, edPesoExcedente, getLeft(), getNextY());
			}
			if (usaCdCliente) {
				UiUtil.add(this, lbClientenm, lbCliente, getLeft(), getNextY());
			}
			if (usaNuCnpj) {
				UiUtil.add(this, lbNuCnpjnm, lbNuCnpj, getLeft(), getNextY());
			}
			if (usaData) {
				UiUtil.add(this, lbData, edData, getLeft(), getNextY());
			}
			if (usaCdProduto) {
				UiUtil.add(this, lbProdutonm, lbProduto, getLeft(), getNextY());
				if (bloqueiaEdicaoQuantidadeItem) {
					edQtdProduto.setEditable(false);
				}
				UiUtil.add(this, lbQtdeProduto, edQtdProduto, getLeft(), getNextY());
				if (getChaveSemente() != SenhaDinamica.SENHA_LIBERACAO_VENDA_PRODUTO_SEM_ESTOQUE) {
					if (bloqueiaEdicaoValorItem) {
						edValorProduto.setEditable(false);
					}
					UiUtil.add(this, lbValorProduto, edValorProduto, getLeft(), getNextY());
				}
			}
			if (usaApenasCdProduto && !usaCdProduto) {
				UiUtil.add(this, lbProdutonm, lbProduto, getLeft(), getNextY());
			}
			if (usaCbProduto) {
				UiUtil.add(this, lbProdutonm, cbProduto, getLeft(), getNextY());
				UiUtil.add(this, lbCodigo, lvProduto, getLeft(), getNextY());
			}
			if (isMostraCbUsuarioRelRep()) {
				UiUtil.add(this, new LabelName(Messages.USUARIO_NOME_ENTIDADE), cbUsuarioRelRep, getLeft(), getNextY());
			}
			if (usaCdUsuario) {
				UiUtil.add(this, lbCdUsuario, lvCdUsuario, getLeft(), getNextY());
			}
			if (usaCdCondPagto) {
				UiUtil.add(this, lbCondPagto, lvCdCondPagto, getLeft(), getNextY());
			}
			if (getChaveSemente() == SenhaDinamica.SENHA_SISTEMA_BLOQUEADO) {
				UiUtil.add(this, lbTempoLiberacao, edTempoLiberacao, getLeft(), getNextY(), PREFERRED, UiUtil.getControlPreferredHeight());
			}
			UiUtil.add(this, lbSenhanm, edSenha, getLeft(), getNextY(), PREFERRED, UiUtil.getControlPreferredHeight());
			if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica && isShowFuncionalidadesExtras()) {
				UiUtil.add(this, new LabelName(Messages.SENHADINAMICA_FUNCIONALIDADES_EXTRAS), getLeft(), getNextY());
				if (SenhaDinamica.SENHA_TIPO_PEDIDO_FLEXIGESENHA != getChaveSemente()
						&& !LavenderePdaConfig.tipoPedidoOcultoNoPedido
						&& !ConfigInternoService.getInstance().isTipoPedidoLiberadoSenha(lbCliente.getValue())) {
					UiUtil.add(this, ckLiberacaoTipoPedido, getLeft(), getNextY());
				}
				if (SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO != getChaveSemente()
						&& LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()
						&& !ConfigInternoService.getInstance().isClienteAtrasadoLiberadoSenha(lbCliente.getValue())) {
					UiUtil.add(this, ckLiberacaoClienteAtrasado, getLeft(), getNextY());
				}
				if (SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP != getChaveSemente()
						&& LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()
						&& !ConfigInternoService.getInstance().isLimiteCreditoLiberadoSenha(lbCliente.getValue())) {
					UiUtil.add(this, ckLiberacaoLimiteCredito, getLeft(), getNextY());
				}
				if (SenhaDinamica.SENHA_LIBERACAO_VISITA_FORA_AGENDA != getChaveSemente()
						&& LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda
						&& !ConfigInternoService.getInstance().isVisitaForaAgendaLiberadoSenha(lbCliente.getValue())) {
					UiUtil.add(this, ckLiberacaoVisitaForaAgenda, getLeft(), getNextY());
				}
				if (SenhaDinamica.SENHA_LIBERACAO_SALDO_BONIFICACAO != getChaveSemente()
						&& LavenderePdaConfig.liberaComSenhaSaldoBonificacaoExtrapolado 
						&& !ConfigInternoService.getInstance().isPedidoBloqueadoSaldoBonificacaoLiberadoSenha(lbCliente.getValue())) {
					UiUtil.add(this, ckLiberacaoBonificacaoSaldo, getLeft(), getNextY());
				}
			}
			// --
			addButtonPopup(btConfirma);
			addButtonPopup(btFechar);
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
	}

    public boolean show() throws java.sql.SQLException {
    	if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica) {
    		if (SenhaDinamica.SENHA_TIPO_PEDIDO_FLEXIGESENHA == getChaveSemente()) {
    			showFuncionalidadesExtras = true;
    			boolean tipoPedidoLiberadoSenha = ConfigInternoService.getInstance().isTipoPedidoLiberadoSenha(lbCliente.getValue());
    			if (tipoPedidoLiberadoSenha) {
    				flSenhaValida = SENHA_VALIDA;
    				return flSenhaValida;
    			}
    		} else if (SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO == getChaveSemente()) {
    			showFuncionalidadesExtras = true;
    			boolean clienteAtrasadoLiberadoSenha = ConfigInternoService.getInstance().isClienteAtrasadoLiberadoSenha(lbCliente.getValue());
    			if (clienteAtrasadoLiberadoSenha) {
    				flSenhaValida = SENHA_VALIDA;
    				return flSenhaValida;
    			}
    		} else if (SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP == getChaveSemente() && !usaVlPedido) {
    			showFuncionalidadesExtras = true;
    			boolean limiteCreditoLiberadoSenha = ConfigInternoService.getInstance().isLimiteCreditoLiberadoSenha(lbCliente.getValue());
    			if (limiteCreditoLiberadoSenha) {
    				flSenhaValida = SENHA_VALIDA;
    				return flSenhaValida;
    			}
    		} else if (SenhaDinamica.SENHA_LIBERACAO_SALDO_BONIFICACAO == getChaveSemente()) {
    			showFuncionalidadesExtras = true;
    			boolean isPedidoBloqueadoSaldoBonificacaoLiberadoSenha = ConfigInternoService.getInstance().isPedidoBloqueadoSaldoBonificacaoLiberadoSenha(lbCliente.getValue());
    			if (isPedidoBloqueadoSaldoBonificacaoLiberadoSenha) {
    				flSenhaValida = SENHA_VALIDA;
    				return flSenhaValida;
    			}
    		} else if (SenhaDinamica.SENHA_LIBERACAO_SALDO_VERBA_PEDIDO_BONIFICACAO_EXTRAPOLADO == getChaveSemente()) {
    			showFuncionalidadesExtras = true;
    			boolean isPedidoBloqueadoSaldoBonificacaoLiberadoSenha = ConfigInternoService.getInstance().isPedidoBonificacaoBloqueadoExtrapoladoSaldoLiberadoSenha(lbCliente.getValue());
    			if (isPedidoBloqueadoSaldoBonificacaoLiberadoSenha) {
    				flSenhaValida = SENHA_VALIDA;
    				return flSenhaValida;
    			}
    		} else if (SenhaDinamica.SENHA_LIBERACAO_VISITA_FORA_AGENDA == getChaveSemente()) {
    			showFuncionalidadesExtras = true;
    			boolean visitaForaAgendaLiberadoSenha = ConfigInternoService.getInstance().isVisitaForaAgendaLiberadoSenha(lbCliente.getValue());
    			if (visitaForaAgendaLiberadoSenha) {
    				flSenhaValida = SENHA_VALIDA;
    				return flSenhaValida;
    			}
    		} 
    	}
        popup();
        return flSenhaValida;
    }

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btConfirma) {
				validateConfirmSenha();
				if (! Session.isModoSuporte) {
					cdUsuarioLiberado = isMostraCbUsuarioRelRep() ? cbUsuarioRelRep.getValue() : lvCdUsuario.getValue();
					if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica && isFuncionalidadesExtrasSelecionadas()) {
						SenhaDinamicaService.validateSenhaMultiplasLiberacoes(lvFator1.getIntegerValue(), lvFator2.getIntegerValue(), lbCliente.getValue(), edSenha.getValueInt(), getChaveSementeList());
					} else {
						SenhaDinamicaService.validateSenha(lvFator1.getIntegerValue(), lvFator2.getIntegerValue(),
								lbCliente.getValue(), lbProduto.getValue(), edQtdProduto.getValueDouble(),
								edValorProduto.getValueDouble(), edSenha.getValueInt(), getChaveSemente(),
								lbVlTotalPedido.getValue(), lbCdGrupoProduto1.getValue(), lbCdGrupoProduto2.getValue(),
								lbCdGrupoProduto3.getValue(), edPesoExcedente.getValueDouble(), cbProduto.getValue(),
								cdUsuarioLiberado, lbNuCnpj.getValue(), edData.getValue(), cdCondicaoPagamento, edTempoLiberacao.getValueWithMask());
					}
				}
				flSenhaValida = SENHA_VALIDA;
				vlPesoLiberado = edPesoExcedente.getValueDouble();
				unpop();
			} else if (event.target == cbProduto) {
				lvProduto.setText(cbProduto.getValue());
				cdProdutoLiberado = cbProduto.getValue();
			} else if (event.target == cbUsuarioRelRep) {
				cdUsuarioLiberado = cbUsuarioRelRep.getValue();
			}
			break;
		}
		case ValueChangeEvent.VALUE_CHANGE: {
			if (event.target == edQtdProduto || event.target == edValorProduto) {
				editouValoresProduto = true;
			}
		}

		}
	}
	
	@Override
	public void unpop() {
	    try {
		   	super.unpop();
			if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica && flSenhaValida) {
				String dataAtual = DateUtil.getCurrentDate() + "-" + TimeUtil.getCurrentTime();
				if (SenhaDinamica.SENHA_TIPO_PEDIDO_FLEXIGESENHA == getChaveSemente() || ckLiberacaoTipoPedido.isChecked()) {
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, dataAtual, ConfigInternoService.getInstance().getVlChaveLiberacaoTipoPedido(lbCliente.getValue()));
				} 
				if (SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO == getChaveSemente() || ckLiberacaoClienteAtrasado.isChecked()) {
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, dataAtual, ConfigInternoService.getInstance().getVlChaveLiberacaoClienteAtrasado(lbCliente.getValue()));
				} 
				if (SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP == getChaveSemente() || ckLiberacaoLimiteCredito.isChecked()) {
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, dataAtual, ConfigInternoService.getInstance().getVlChaveLiberacaoLimiteCredito(lbCliente.getValue()));
				} 
				if (SenhaDinamica.SENHA_LIBERACAO_VISITA_FORA_AGENDA == getChaveSemente() || ckLiberacaoVisitaForaAgenda.isChecked()) {
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, dataAtual, ConfigInternoService.getInstance().getVlChaveLiberacaoVisitaForaAgenda(lbCliente.getValue()));
				}
				if (SenhaDinamica.SENHA_LIBERACAO_SALDO_BONIFICACAO == getChaveSemente() || ckLiberacaoBonificacaoSaldo.isChecked()) {
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.FUNCIONALIDADELIBERADASENHA, dataAtual, ConfigInternoService.getInstance().getVlChaveLiberacaoPedidoBloqueadoSaldoBonificacao(lbCliente.getValue()));
				}
			}
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
	}
	
	private void validateConfirmSenha() {
		if (ValueUtil.isEmpty(cbUsuarioRelRep.getValue()) && (isLiberacaoPrecoBaseadoPercentualEscolhido() || isLiberacaoDiaEntregaPedidoPorUsuario() || isLiberacaoPorUsuarioEAlcada())) {
			throw new ValidationException(Messages.VALIDACAO_SENHA_USUARIO_OBRIGATORIO);
		}
	}

    public String getCdUsuario() {
        return cdUsuario;
    }

    public void setCdUsuario(String cdUsuario) {
        this.cdUsuario = cdUsuario;
        usaCdUsuario = ValueUtil.isNotEmpty(cdUsuario);
        lvCdUsuario.setValue(cdUsuario);
    }

    public Vector getCdProdutoList() {
        return cdProdutoList;
    }

    public void setCdProdutoList(Vector cdProdutoList) throws SQLException {
        this.cdProdutoList = cdProdutoList;
        usaCbProduto = ValueUtil.isNotEmpty(cdProdutoList);
        cbProduto.loadProdutos(cdProdutoList);
        lvProduto.setText(cbProduto.getValue());
        cdProdutoLiberado = cbProduto.getValue();
    }

    public double getPesoExcedente() {
        return pesoExcedente;
    }

    public void setPesoExcedente(double pesoExcedente) {
        this.pesoExcedente = pesoExcedente;
        edPesoExcedente.setValue(pesoExcedente);
    }
    
    public String getCdGrupoProduto1() {
        return cdGrupoProduto1;
    }

    public void setCdGrupoProduto1(String cdGrupoProduto1) {
        this.cdGrupoProduto1 = cdGrupoProduto1;
        lbCdGrupoProduto1.setValue(cdGrupoProduto1);
        usacdGrupoProduto1 = ValueUtil.isNotEmpty(cdGrupoProduto1) && !"0".equals(cdGrupoProduto1);
    }

    public String getCdGrupoProduto2() {
        return cdGrupoProduto2;
    }

    public void setCdGrupoProduto2(String cdGrupoProduto2) {
        this.cdGrupoProduto2 = cdGrupoProduto2;
        lbCdGrupoProduto2.setValue(cdGrupoProduto2);
        usacdGrupoProduto2 = ValueUtil.isNotEmpty(cdGrupoProduto2) && !"0".equals(cdGrupoProduto2);
    }

    public String getCdGrupoProduto3() {
        return cdGrupoProduto3;
    }

    public void setCdGrupoProduto3(String cdGrupoProduto3) {
        this.cdGrupoProduto3 = cdGrupoProduto3;
        lbCdGrupoProduto3.setValue(cdGrupoProduto3);
        usacdGrupoProduto3 = ValueUtil.isNotEmpty(cdGrupoProduto3) && !"0".equals(cdGrupoProduto3);
    }

    public double getVlTotalPedido() {
        return vlTotalPedido;
    }

    public void setVlTotalPedido(double vlTotalPedido) {
        this.vlTotalPedido = vlTotalPedido;
        usaVlPedido = vlTotalPedido != 0;
        lbVlTotalPedido.setValue(StringUtil.getStringValueToInterface(vlTotalPedido));
    }

    public int getChaveSemente() {
        return chaveSemente;
    }

    public void setChaveSemente(int chaveSemente) {
        this.chaveSemente = chaveSemente;
    }

    public void setVlProduto(double vlProduto) {
        edValorProduto.setValue(vlProduto);
    }

    public void setQtdeProduto(double qtdeProduto) {
        edQtdProduto.setValue(qtdeProduto);
    }

    public void setCdProduto(String cdProduto) {
        usaCdProduto = ValueUtil.isNotEmpty(cdProduto);
        lbProduto.setValue(cdProduto);
    }

    public void setCdProdutoApenas(String cdProduto) {
    	usaApenasCdProduto = ValueUtil.isNotEmpty(cdProduto);
    	lbProduto.setValue(cdProduto);
    }

    public void setCdCliente(String cdCliente) {
        usaCdCliente = ValueUtil.isNotEmpty(cdCliente);
        lbCliente.setValue(cdCliente);
    }

    public void setNuCnpj(String nuCnpj) {
    	usaNuCnpj = ValueUtil.isNotEmpty(nuCnpj);
    	lbNuCnpj.setValue(nuCnpj);
    }
    
    public void setData(Date data) {
    	usaData = ValueUtil.isNotEmpty(data);
    	edData.setValue(data);
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
   
    public void setLvCdCondPagto(String cdCondPagto) {
    	usaCdCondPagto = ValueUtil.isNotEmpty(cdCondPagto);
    	lvCdCondPagto.setValue(cdCondPagto);
    }

	public void setCdCondicaoPagamento(String cdCondicaoPagamento) {
		this.cdCondicaoPagamento = cdCondicaoPagamento;
	}

    public boolean isFuncionalidadesExtrasSelecionadas() {
    	if (isShowFuncionalidadesExtras()) {
    		return ckLiberacaoTipoPedido.isChecked() || ckLiberacaoClienteAtrasado.isChecked() || ckLiberacaoLimiteCredito.isChecked()  || ckLiberacaoVisitaForaAgenda.isChecked() || ckLiberacaoBonificacaoSaldo.isChecked();
    	}
    	return false;
    }
    
    public Vector getChaveSementeList() {
    	Vector chaveSementeList = new Vector();
    	chaveSementeList.addElement(getChaveSemente());
    	if (ckLiberacaoTipoPedido.isChecked()) {
    		chaveSementeList.addElement(SenhaDinamica.SENHA_TIPO_PEDIDO_FLEXIGESENHA);
    	}
    	if (ckLiberacaoClienteAtrasado.isChecked()) {
    		chaveSementeList.addElement(SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO);
    	}
    	if (ckLiberacaoLimiteCredito.isChecked()) {
    		chaveSementeList.addElement(SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP);
    	}
    	if (ckLiberacaoVisitaForaAgenda.isChecked()) {
    		chaveSementeList.addElement(SenhaDinamica.SENHA_LIBERACAO_VISITA_FORA_AGENDA);
    	}
    	if (ckLiberacaoBonificacaoSaldo.isChecked()) {
    		chaveSementeList.addElement(SenhaDinamica.SENHA_LIBERACAO_SALDO_BONIFICACAO);
    	}
    	return chaveSementeList;
    }

	public boolean isShowFuncionalidadesExtras() {
		return showFuncionalidadesExtras;
	}
	
	public boolean mostraLbFator2() {
		return (!usaCdProduto && !usaVlPedido && !usacdGrupoProduto1) || isLiberacaoPrecoBaseadoPercentualEscolhido();
	}
	
	private boolean isMostraCbUsuarioRelRep() {
		return isLiberacaoPrecoBaseadoPercentualEscolhido() || isLiberacaoDiaEntregaPedidoPorUsuario() || isLiberacaoPorUsuarioEAlcada();
	}
	
	private boolean isLiberacaoPrecoBaseadoPercentualEscolhido() {
		return LavenderePdaConfig.liberaComSenhaPrecoBaseadoPercentualUsuarioEscolhido && getChaveSemente() == SenhaDinamica.SENHA_LIBERACAO_BASEADO_PERCENTUAL_USUARIO_ESCOLHIDO;
	}
	
	private boolean isLiberacaoDiaEntregaPedidoPorUsuario() {
		return LavenderePdaConfig.isLiberaSenhaDiaEntregaPedidoPorUsuario() && getChaveSemente() == SenhaDinamica.SENHA_LIBERACAO_DIA_ENTREGA_PEDIDO;
	}
	
	private boolean isLiberacaoPorUsuarioEAlcada() {
		return LavenderePdaConfig.isUsaLiberacaoPorUsuarioEAlcada() && LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()  && getChaveSemente() == SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP;
		
	}
}