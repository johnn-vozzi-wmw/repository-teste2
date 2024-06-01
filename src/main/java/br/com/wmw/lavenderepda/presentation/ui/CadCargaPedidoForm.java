package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Menu;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.StatusCargaPedido;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import br.com.wmw.lavenderepda.business.service.MenuLavendereService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.validation.CargaPedidoPesoMinimoException;
import br.com.wmw.lavenderepda.business.validation.CargaPedidoValidadeException;
import br.com.wmw.lavenderepda.presentation.ui.combo.RotaEntregaComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;

public class CadCargaPedidoForm extends BaseCrudCadForm {

    
	private EditText edDsCargaPedido;
	private RotaEntregaComboBox cbRotaEntrega;
	private EditDate edDtEntrega;
	protected LabelValue  lbPesoAtual;
	protected ButtonAction btFecharCarga;
	protected ButtonAction btPedidos;
	protected String cdCargaPedido;
	protected CadCargaPedidoWindow cadCargaPedidoWindow;
	
	public boolean telaAcessadaPeloPedido;
	
	
    public CadCargaPedidoForm(String cdCliente, boolean telaAcessadaPeloPedido) throws SQLException {
        super(Messages.CARGAPEDIDO_TITULO_CADASTRO);
        this.telaAcessadaPeloPedido = telaAcessadaPeloPedido;
        scrollable = true;
        edDsCargaPedido = new EditText("", 100);
        cbRotaEntrega = new RotaEntregaComboBox();
        if (ValueUtil.isNotEmpty(cdCliente)) {
        	cbRotaEntrega.load(cdCliente);
        } else {
        	cbRotaEntrega.load();
        }
        edDtEntrega = new EditDate();
        lbPesoAtual = new LabelValue("@");
        btFecharCarga = new ButtonAction(Messages.CARGAPEDIDO_FECHAR_CARGA, "images/fecharCarga.png");
        btFecharCarga.setVisible(false);
        btPedidos = new ButtonAction(Messages.MENU_OPCAO_PEDIDO, getImage());
    }

    private Image getImage() throws SQLException {
		Menu menuFilter = new Menu();
		menuFilter.cdSistema = LavendereConfig.CDSISTEMALAVENDEREPDA;
		menuFilter.cdMenu = MainMenu.CDTELA_PEDIDOS;
		Menu menu = (Menu) MenuLavendereService.getInstance().findByRowKey(menuFilter.getRowKey());
		int tamImages = UiUtil.getBarMenuPreferredHeight() / 7 * 4;
		try {
			Image img = UiUtil.getImage(menu.imIcone);
			img.applyColor2(ColorUtil.baseForeColorSystem); 
			return UiUtil.getSmoothScaledImage(img, tamImages, tamImages);
		} catch (ApplicationException ex) {
			return UiUtil.getColorfulImage("images/menuIconDefault.png", tamImages, tamImages);
		}
	}

	//-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return Messages.CARGAPEDIDO_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return CargaPedidoService.getInstance();
    }
    
    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new CargaPedido();
    }
    
    
    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        CargaPedido cargaPedido = (CargaPedido) getDomain();
        cargaPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
        cargaPedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
        cargaPedido.cdRotaEntrega = cbRotaEntrega.getValue();
        if (!isEditing()) {
        	cargaPedido.cdCargaPedido = CargaPedidoService.getInstance().generateIdGlobal();
        	cargaPedido.dtCriacao = DateUtil.getCurrentDate();
        	cargaPedido.hrCriacao = TimeUtil.getCurrentTimeHHMM();
        }
        cargaPedido.dtEntrega = edDtEntrega.getValue();
        cargaPedido.dsCargaPedido = edDsCargaPedido.getValue();
        cargaPedido.flTipoAlteracao = CargaPedido.FLTIPOALTERACAO_ORIGINAL;
        cargaPedido.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
        return cargaPedido;
    }
    
    
   // @Override
    protected void updateCurrentRecordInList(BaseDomain domain) throws SQLException {
    	list();
    }
    
    
    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        CargaPedido cargaPedido = (CargaPedido) domain;
        edDsCargaPedido.setText(cargaPedido.dsCargaPedido);
    	cbRotaEntrega.setValue(cargaPedido.cdRotaEntrega);
    	edDtEntrega.setValue(cargaPedido.dtEntrega);
    	lbPesoAtual.setValue(PedidoService.getInstance().findQtPesoTotalPedidosCargaPedido(cargaPedido.cdCargaPedido, null));
    }
    
    //@Override
    protected void clearScreen() throws java.sql.SQLException {
    	edDsCargaPedido.setText("");
    	edDsCargaPedido.setEditable(true);
    	if (cadCargaPedidoWindow == null) {
    		cbRotaEntrega.setSelectedIndex(-1);
    		cbRotaEntrega.setEditable(true);
    	} else if (cbRotaEntrega.size() == 1) {
    		cbRotaEntrega.selectLast();
    	}
    	edDtEntrega.setText("");
    	lbPesoAtual.setText("");
    }
    
    
    //@Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
    	CargaPedido cargaPedido = (CargaPedido) getDomain();
		edDsCargaPedido.setEditable(!cargaPedido.isCargaFechada());
		cbRotaEntrega.setEditable(!cargaPedido.isCargaFechada() && (cadCargaPedidoWindow == null || (cadCargaPedidoWindow != null && cbRotaEntrega.size() > 1)));
    	btFecharCarga.setVisible(isEditing() && StatusCargaPedido.STATUS_CARGAPEDIDO_DSABERTO.equals(CargaPedidoService.getInstance().getDsStatusCarga(cargaPedido)));
    	btSalvar.setVisible(!cargaPedido.isCargaFechada());
    	btExcluir.setVisible(isEditing() && !cargaPedido.isCargaFechada());
    	btPedidos.setVisible(isEditing());
    	barBottomContainer.setVisible(!telaAcessadaPeloPedido);
    	barTopContainer.setVisible(!telaAcessadaPeloPedido);
    }
    
    //@Override
    protected int getTop() {
    	if (telaAcessadaPeloPedido) {
    		return TOP;
    	} else {
    		return super.getTop();
    	}
    }
    
    protected void atualizaTela() throws SQLException {
    	CargaPedido cargaPedido = (CargaPedido) getDomain();
		setDomain(CargaPedidoService.getInstance().getCargaPedido(cargaPedido.cdCargaPedido));
		lbPesoAtual.setValue(PedidoService.getInstance().findQtPesoTotalPedidosCargaPedido(cargaPedido.cdCargaPedido, null));
		visibleState();
		list();
    	
    }
    
    //-----------------------------------------------
    
    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, new LabelName(Messages.CARGAPEDIDO_DESCRICAO), edDsCargaPedido, getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.CARGAPEDIDO_ROTA), cbRotaEntrega, getLeft(), getNextY());
    	if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()) {
			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_DTENTREGA), edDtEntrega, getLeft(), getNextY());
		}
    	UiUtil.add(this, new LabelName(Messages.CARGAPEDIDO_PESO_MINIMO), new LabelValue(StringUtil.getStringValueToInterface(LavenderePdaConfig.qtdPesoMinimoCargaPedido)), getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.CARGAPEDIDO_PESO_MAXIMO), new LabelValue(StringUtil.getStringValueToInterface(LavenderePdaConfig.qtdPesoMaximoCargaPedido)), getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.CARGAPEDIDO_PESO_ATUAL), lbPesoAtual, getLeft(), getNextY());
    	if (!telaAcessadaPeloPedido) {
    		UiUtil.add(barBottomContainer, btPedidos, 4);
    		UiUtil.add(barBottomContainer, btFecharCarga, 5);
    	}
    }

    //@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == btPedidos) {
					btPedidosClick();
				} else if (event.target == btFecharCarga) {
					if (UiUtil.showConfirmYesCancelMessage(Messages.CARGAPEDIDO_CONFIRMACAO_FECHAR_CARGA) == 1) {
						btFecharCargaClick();
					}
				} else if (event.target == cbRotaEntrega && isEditing()) {
					try {
						CargaPedidoService.getInstance().validaTrocarRota((CargaPedido) getDomain());
					} catch (Throwable e) {
						UiUtil.showErrorMessage(e);
						cbRotaEntrega.setValue(((CargaPedido) getDomain()).cdRotaEntrega);
					}
				}
				break;
			default:
				break;
		}
	}

	private void btPedidosClick() throws SQLException {
		ListPedidoForm listPedidoForm = new ListPedidoForm();
		CargaPedido cargaPedido = (CargaPedido) getDomain();
		listPedidoForm.cdCargaPedidoFilter = cargaPedido.cdCargaPedido;
		show(listPedidoForm);
	}

	private void btFecharCargaClick() throws SQLException {
		CargaPedido cargaPedido = (CargaPedido) getDomain();
		try {
			CargaPedidoService.getInstance().fecharCarga(cargaPedido);
			close();
			list();
		} catch (CargaPedidoPesoMinimoException e) {
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(MessageUtil.getMessage(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_PESO_MINIMO, LavenderePdaConfig.qtdPesoMinimoCargaPedido));
			senhaForm.setCdUsuario(SessionLavenderePda.usuarioPdaRep.cdUsuario);
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_PESO_MINIMO_CARGA);
			if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				CargaPedidoService.validationFechamentoCount--;
				return;
			}
			cargaPedido.flPesoMenorLiberado = ValueUtil.VALOR_SIM;
			btFecharCargaClick();
			return;
		} catch (CargaPedidoValidadeException e) {
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_VALIDADE);
			senhaForm.setCdUsuario(SessionLavenderePda.usuarioPdaRep.cdUsuario);
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_VALIDADE_CARGA);
			if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				CargaPedidoService.validationFechamentoCount--;
				return;
			}
			cargaPedido.flCargaVencidaLiberada = ValueUtil.VALOR_SIM;
			CargaPedidoService.getInstance().update(cargaPedido);
			btFecharCargaClick();
			return;
		}
	}

	//@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		CargaPedidoService.validationFechamentoCount = 0;
	}
	
	//@Override
	protected void onSave() throws SQLException {
		if (cadCargaPedidoWindow != null) {
			save();
			CargaPedido cargaPedido = (CargaPedido) getDomain();
			cdCargaPedido = cargaPedido.cdCargaPedido;
			cadCargaPedidoWindow.novaCargaCriada = true;
			cadCargaPedidoWindow.unpop();
		} else {
			super.onSave();
		}
	}
	
	//@Override
	protected void voltarClick() throws SQLException {
		if (cadCargaPedidoWindow != null) {
			cadCargaPedidoWindow.unpop();
		} else { 
			super.voltarClick();
			
		}
	}
	
	//@Override
	public void initCabecalhoRodape() throws SQLException {
		if (!telaAcessadaPeloPedido) {
			super.initCabecalhoRodape();
		}
	}
}
