package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.FotoPesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.validation.PesquisaMercadoException;
import br.com.wmw.lavenderepda.presentation.ui.combo.ConcorrenteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderPesquisaMercadoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class CadPesquisaMercadoForm extends BaseCrudCadForm {

    private EditText edCdProduto;
    private ConcorrenteComboBox cbConcorrente;
    private EditDate edDtEmissao;
    private EditNumberFrac edVlUnitario;
    private EditNumberFrac edQtItem;
    private EditMemo edDsObservacao;
	private BaseButton btFiltrar;
	private LabelName lbProduto;
	private Produto produto;
	private BaseScrollContainer scroolContainer;
	private boolean loadDadosProduto;
	private String cdProduto;
	private String nuPedido;
	//--
	private ButtonAction btCadastrarCoordenada;
	private double latitude;
	private double longitude;
	private boolean coordenadasClosedByBtMaisTarde;
	//--
	private ButtonAction btCadastrarFoto;
	private boolean liberadoParaSalvar;

    public CadPesquisaMercadoForm(String nuPedido) throws SQLException {
        super(Messages.PESQUISAMERCADO_NOME_ENTIDADE);
        scroolContainer = new BaseScrollContainer(false, true);
        edCdProduto = new EditText("@", 200);
        cbConcorrente = new ConcorrenteComboBox(Messages.CONCORRENTE_NOME_ENTIDADE);
        cbConcorrente.load();
        edDtEmissao = new EditDate();
        edVlUnitario = new EditNumberFrac("99999999", 9);
        edQtItem = new EditNumberFrac("99999999", 9);
        edDsObservacao = new EditMemo("", 3, 255);
        lbProduto = new LabelName(Messages.PRODUTO_NOME_ENTIDADE);
        btFiltrar = new BaseButton(UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
        this.nuPedido = nuPedido;
        btCadastrarCoordenada = new ButtonAction(Messages.PESQUISA_MERCADO_CADASTRO_COORDENADA, "images/gps.png");
        btCadastrarFoto = new ButtonAction(Messages.PESQUISA_MERCADO_CADASTRO_FOTOS, "images/camera.png");
    }
    
    public CadPesquisaMercadoForm(String cdProduto, String nuPedido) throws SQLException {
    	this(null);
    	this.cdProduto = cdProduto;
    	this.nuPedido = nuPedido;
    	loadDadosProduto = true;
    }

    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return PesquisaMercadoService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new PesquisaMercado();
    }

    //@Override
    public void add() throws java.sql.SQLException {
    	super.add();
    	//--
    	PesquisaMercado pesquisaMercado = getPesquisaMercado();
    	pesquisaMercado.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	pesquisaMercado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	pesquisaMercado.flOrigemPesquisaMercado = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	pesquisaMercado.cdPesquisaMercado = getCrudService().generateIdGlobal();
    	pesquisaMercado.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	pesquisaMercado.flPesquisaNovoCliente = SessionLavenderePda.getCliente().isNovoCliente() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
    	pesquisaMercado.dtEmissao = new Date();
    	if (LavenderePdaConfig.excluiPesquisaMercadoPedido) {
    		pesquisaMercado.nuPedido = nuPedido;
    		pesquisaMercado.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
    	}
    	pesquisaMercado.cdUsuarioEmissao = Session.getCdUsuario();
    }

    private PesquisaMercado getPesquisaMercado() throws SQLException {
    	return (PesquisaMercado)getDomain();
    }

    //@Override
    public void edit(BaseDomain domain) throws java.sql.SQLException {
    	PesquisaMercado pesquisaMercado = (PesquisaMercado)domain;
    	//--
    	super.edit(pesquisaMercado);
    	//--
    	internalSetEnabled(!BaseDomain.FLTIPOALTERACAO_ORIGINAL.equals(pesquisaMercado.flTipoAlteracao), false);
    }
    
    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        PesquisaMercado pesquisamercado = (PesquisaMercado) getDomain();
        pesquisamercado.cdProduto = produto == null ? "" : produto.cdProduto;
        pesquisamercado.cdConcorrente = cbConcorrente.getValue();
        double vlUnitario = Messages.MOEDA.equals(Messages.MOEDA_REAL_RS) ? edVlUnitario.getValueDouble() : ValueUtil.getDoubleValueSeparador(edVlUnitario.getValue());
        pesquisamercado.vlUnitario = vlUnitario;
        pesquisamercado.qtItem = edQtItem.getValueDouble();
        pesquisamercado.dsObservacao = edDsObservacao.getValue();
        pesquisamercado.cdLatitude = latitude;
        pesquisamercado.cdLongitude = longitude;
        return pesquisamercado;
    }
    
	@Override
	protected void insert(BaseDomain domain) throws SQLException {
		try {
			getPesquisaMercado().inInsertList = !isEditing();
			super.insert(domain);
			liberadoParaSalvar = ((PesquisaMercado) domain).isLiberadoParaSalvar;
		} catch (PesquisaMercadoException e) {
			if (e.isFotos) {
				handleFotoException(domain);
			} else if (e.isCoordenada) {
				handleCoordenadaException(domain);
			} else {
				throw e;
			}
		}
	}

	private void handleCoordenadaException(BaseDomain domain) throws SQLException {
		if (!isEditing()) {
			if (LavenderePdaConfig.usaCadastroAutomaticoCoordenadaPesquisaDeMercado()) {
				double[] lat_lon = new double[2];
				try {
					lat_lon = PesquisaMercadoService.getInstance().getCoordenadasPesquisaMercado(lat_lon);
				} catch (ValidationException e) {
					UiUtil.showWarnMessage(e);
				}
				if (lat_lon[0] == 0 && lat_lon[1] == 0) {
					btCadastrarCoordenadasClick(true);
				} else {
					latitude = lat_lon[0];
					longitude = lat_lon[1];
				}
			} else {
				btCadastrarCoordenadasClick(true);
			}
			if (!coordenadasClosedByBtMaisTarde) {
				insert(domain);
			}
		}
	}

	private void handleFotoException(BaseDomain domain) throws SQLException {
		if (!isEditing()) {
			UiUtil.showErrorMessage(Messages.PESQUISA_MERCADO_OBRIGATORIO_CADASTRAR_FOTO);
			btCadastrarFotoClick(true);
			if (ValueUtil.isNotEmpty(getPesquisaMercado().fotoList) || SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado) {
				insert(domain);
			}
		}
	}

	@Override
	protected void onSave() throws SQLException {
		save();
		if (liberadoParaSalvar || isEditing()) {
			close();
		}
	}
	
	@Override
	protected void save() throws SQLException {
		super.save();
		if (!isEditing() && LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado() && ValueUtil.isNotEmpty(getPesquisaMercado().fotoList) && liberadoParaSalvar) {
			FotoPesquisaMercadoService.getInstance().insertFotosPesquisaMercado(getPesquisaMercado());
		}
	}
    
    @Override
    protected void afterSave() throws java.sql.SQLException {
    	super.afterSave();
    	if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercado()) {
    		ClienteService.getInstance().atualizaNuDiasSemPesquisa(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
    	}
    }
    
    @Override
    protected void voltarClick() throws SQLException {
    	if (!isEditing() && ValueUtil.isNotEmpty(getPesquisaMercado().fotoList)) {
    		if (UiUtil.showConfirmYesCancelMessage(Messages.PESQUISA_MERCADO_DESEJA_DESCARTAR_FOTOS) == 1) {
    			FotoPesquisaMercadoService.getInstance().excluiFotoPesquisaMercadoArquivo(getPesquisaMercado());
    			super.voltarClick();
    		}
    	} else {
    		super.voltarClick();
    	}
    } 
    
    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        PesquisaMercado pesquisaMercado = (PesquisaMercado) domain;
        //--
        produto = new Produto();
        produto.cdProduto = pesquisaMercado.cdProduto;
        produto.dsProduto = ProdutoService.getInstance().getDsProduto(pesquisaMercado.cdProduto);
        edCdProduto.setValue(produto.dsProduto);
        //--
        cbConcorrente.setValue(pesquisaMercado.cdConcorrente);
        edDtEmissao.setValue(pesquisaMercado.dtEmissao);
        edVlUnitario.setValue(pesquisaMercado.vlUnitario);
        edQtItem.setValue(pesquisaMercado.qtItem);
        edDsObservacao.setValue(pesquisaMercado.dsObservacao);
        if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
        	latitude = pesquisaMercado.cdLatitude != null ? pesquisaMercado.cdLatitude : 0;
        	longitude = pesquisaMercado.cdLongitude != null ? pesquisaMercado.cdLongitude : 0;
        }
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        edCdProduto.setText("");
        cbConcorrente.setValue("");
        edDtEmissao.setValue(new Date());
        edVlUnitario.setText("");
        edQtItem.setText("");
        edDsObservacao.setText("");
        produto = null;
        latitude = 0;
        longitude = 0;
    }

    public void setEnabled(boolean enabled) {
    	edCdProduto.setEnabled(false);
        cbConcorrente.setEditable(enabled);
        edDtEmissao.setEditable(false);
        edVlUnitario.setEditable(enabled);
        edQtItem.setEditable(enabled);
        edDsObservacao.setEditable(enabled);
    }

    public void setVisible() throws SQLException {
    	if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
    		btCadastrarCoordenada.setVisible(isEnabled());
    	}
    	if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado()) {
    		btCadastrarFoto.setVisible(isEnabled());
    	}
    	btSalvar.setVisible(isEnabled());
    	btExcluir.setVisible(isEditing() && isEnabled());
    	btFiltrar.setVisible(!isEditing());
    }

    //@Override
    public void onFormShow() throws SQLException {
        setEnabled(isEnabled());
        setVisible();
    	super.onFormShow();
    	if (loadDadosProduto) {
    		PesquisaMercado pesquisaMercado = getPesquisaMercado();
    		pesquisaMercado.cdProduto = cdProduto;
    		domainToScreen(pesquisaMercado);
    	}
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, scroolContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(scroolContainer, clienteContainer, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
        //--
        UiUtil.add(scroolContainer, new LabelName(Messages.CONCORRENTE_NOME_ENTIDADE), cbConcorrente, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_DTEMISSAO), edDtEmissao, getLeft(), AFTER + HEIGHT_GAP);
        //--
        UiUtil.add(scroolContainer, lbProduto, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
        UiUtil.add(scroolContainer, edCdProduto, getLeft(), AFTER, FILL - (btFiltrar.getPreferredWidth() + WIDTH_GAP_BIG + WIDTH_GAP_BIG));
        UiUtil.add(scroolContainer, btFiltrar, RIGHT - WIDTH_GAP_BIG, SAME);
        //--
        UiUtil.add(scroolContainer, new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO), edQtItem, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.PRODUTO_LABEL_VLUNITARIO), edVlUnitario, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP);
        //--
        int indexButtonAction = 5;
        if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
        	UiUtil.add(barBottomContainer, btCadastrarCoordenada, indexButtonAction--);
        }
        //--
        if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado()) {
        	UiUtil.add(barBottomContainer, btCadastrarFoto, indexButtonAction--);
        }
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btFiltrar) {
					btFiltrarClick();
				} else if (event.target == btCadastrarCoordenada) {
					btCadastrarCoordenadasClick(false);
				} else if (event.target == btCadastrarFoto) {
					btCadastrarFotoClick(false);
				}
	    		break;
	    	}
    	}
    }
    
    private void btCadastrarFotoClick(boolean onFechamento) throws SQLException {
    	PesquisaMercado pesquisaMercado = getPesquisaMercado();
    	if (!onFechamento) {
		    pesquisaMercado.cdProduto = edCdProduto.getText();
	    }
		ImageSliderPesquisaMercadoWindow imageSliderPesquisaMercadoWindow = new ImageSliderPesquisaMercadoWindow(SessionLavenderePda.getCliente(), pesquisaMercado, isEnabled(), isEditing());
		imageSliderPesquisaMercadoWindow.popup();
	}

	private void btCadastrarCoordenadasClick(boolean onFechamento) throws SQLException {
		PesquisaMercado pesquisaMercado = getPesquisaMercado();
		CadCoordenadasPesquisaMercadoWindow cadCoordenadasPesquisaMercadoWindow = new CadCoordenadasPesquisaMercadoWindow(SessionLavenderePda.getCliente(), new double[] {latitude, longitude}, isEditing());
		cadCoordenadasPesquisaMercadoWindow.popup();
		coordenadasClosedByBtMaisTarde = cadCoordenadasPesquisaMercadoWindow.closedByBtFechar;
		if (onFechamento) {
			pesquisaMercado.cdLatitude = cadCoordenadasPesquisaMercadoWindow.latitude;
			pesquisaMercado.cdLongitude = cadCoordenadasPesquisaMercadoWindow.longitude;
		} else if (cadCoordenadasPesquisaMercadoWindow.latitude != 0 && cadCoordenadasPesquisaMercadoWindow.longitude != 0 && cadCoordenadasPesquisaMercadoWindow.cadastrouCoordenada) {
			latitude = cadCoordenadasPesquisaMercadoWindow.latitude;
			longitude = cadCoordenadasPesquisaMercadoWindow.longitude;
		}
	}	

	private void btFiltrarClick() throws SQLException {
    	ListProdutoWindow produtoForm = new ListProdutoWindow();
		produtoForm.popup();
		if (produtoForm.produto != null) {
			produto = produtoForm.produto;
			edCdProduto.setValue(produto.dsProduto);
		}
    }

	@Override
	protected boolean delete() throws SQLException {
		boolean result = UiUtil.showConfirmDeleteMessage(getEntityDescription());
		if (result) {
			if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado()) {
				FotoPesquisaMercadoService.getInstance().excluiFotosJuntoComPesquisaMercado((PesquisaMercado) getDomain());
			}
			delete(screenToDomain());
		}
		return result;
	}

}