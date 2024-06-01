package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.ProdutoConcorrente;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.FotoPesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoConcorrenteService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ConcorrenteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderPesquisaMercadoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadPesquisaMercadoProdConcorrenteForm extends BaseCrudCadForm {

	private Map<String, PesquisaMercado> pesquisaMercadoHash = new HashMap<>();
	private final int columnKey = 1;
	
    private EditText edCdProduto;
    private ConcorrenteComboBox cbConcorrente;
    private EditDate edDtEmissao;
    private EditNumberFrac edVlUnitario;
    private EditNumberFrac edQtItem;
    private EditNumberInt edQtItemLargura;
    private EditNumberInt edQtItemComprimento;
    private EditNumberInt edQtItemAltura;
    private EditNumberInt edQtItemTotal;
    private EditMemo edDsObservacao;
	private LabelName lbProduto;
	private BaseScrollContainer scroolContainer;
	private BaseGridEdit grid;
	private EditText edObs;
	private EditFiltro edFiltro;
	private String cdTipoPesquisa;
	private boolean pesquisaNovoCliente;
	// coordenadas
	private ButtonAction btCadastrarCoordenadas;
	private double latitude;
	private double longitude;
	private boolean closedByBtMaisTardeCoordenadas;
	//fotos
	private ButtonAction btCadastrarFotos;
	
    public CadPesquisaMercadoProdConcorrenteForm(String cdTipoPesquisa) throws SQLException {
    	super(PesquisaMercado.CDTIPOPESQUISA_VALOR.equals(cdTipoPesquisa) ? Messages.PESQUISAMERCADO_TITULO_PESQUISA_VALOR : Messages.PESQUISAMERCADO_TITULO_PESQUISA_GONDOLA);
    	this.cdTipoPesquisa = cdTipoPesquisa;
        scroolContainer = new BaseScrollContainer(false, true);
        edCdProduto = new EditText("@", 200);
        cbConcorrente = new ConcorrenteComboBox(Messages.CONCORRENTE_NOME_ENTIDADE);
        cbConcorrente.loadClienteConc();
        edDtEmissao = new EditDate();
        edVlUnitario = new EditNumberFrac("99999999", 9);
        edQtItem = new EditNumberFrac("99999999", 9);
        edDsObservacao = new EditMemo("", 3, 255);
        lbProduto = new LabelName(Messages.PRODUTO_NOME_ENTIDADE);
        edFiltro = new EditFiltro("999999999", 50);
		edQtItemLargura = new EditNumberInt("99999", 9);
		edQtItemComprimento = new EditNumberInt("99999", 9);
		edQtItemAltura = new EditNumberInt("99999", 9);
		edQtItemTotal = new EditNumberInt("99999", 9);
		btCadastrarCoordenadas = new ButtonAction(Messages.PESQUISA_MERCADO_CADASTRO_COORDENADA, "images/gps.png");
		btCadastrarFotos = new ButtonAction(Messages.PESQUISA_MERCADO_CADASTRO_FOTOS, "images/camera.png");
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
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        PesquisaMercado pesquisaMercado = (PesquisaMercado) domain;
        edCdProduto.setValue(StringUtil.getStringValue(ProdutoConcorrenteService.getInstance().getProdutoByPesqMercado(pesquisaMercado)));
        cbConcorrente.setValue(pesquisaMercado.cdConcorrente);
        edDtEmissao.setValue(pesquisaMercado.dtEmissao);
        if (isTipoPesquisaMercadoValor()) {
        	edVlUnitario.setValue(pesquisaMercado.vlUnitario);
        	edQtItem.setValue(pesquisaMercado.qtItem);
        }
        if (isTipoPesquisaMercadoGondola()) {
        	edQtItemLargura.setValue(pesquisaMercado.qtItemFrente);
        	edQtItemComprimento.setValue(pesquisaMercado.qtItemProfundidade);
        	edQtItemAltura.setValue(pesquisaMercado.qtItemAndar);
        	edQtItemTotal.setValue(pesquisaMercado.qtItemTotal);
        }
        edDsObservacao.setValue(pesquisaMercado.dsObservacao);
        if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
        	latitude = pesquisaMercado.cdLatitude != null ? pesquisaMercado.cdLatitude : 0;
        	longitude = pesquisaMercado.cdLongitude != null ? pesquisaMercado.cdLongitude : 0;
        }
    }
    
    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        edCdProduto.setText("");
        cbConcorrente.setSelectedIndex(0);
        edDtEmissao.setValue(new Date());
        edVlUnitario.setText("");
        edQtItem.setText("");
        edDsObservacao.setText("");
        edFiltro.setText("");
        edQtItemLargura.setText("");
        edQtItemComprimento.setText("");
        edQtItemAltura.setText("");
        edQtItemTotal.setText("");
    }
    
    public void edit(BaseDomain domain) throws java.sql.SQLException {
    	PesquisaMercado pesquisaMercado = (PesquisaMercado)domain;
    	//--
    	super.edit(pesquisaMercado);
    	//--
    	internalSetEnabled(!BaseDomain.FLTIPOALTERACAO_ORIGINAL.equals(pesquisaMercado.flTipoAlteracao), false);
    }
    
    public void setEnabled(boolean enabled) {
    	edCdProduto.setEnabled(false);
        edDtEmissao.setEditable(false);
        edVlUnitario.setEditable(enabled);
        edQtItem.setEditable(enabled);
        edDsObservacao.setEditable(enabled);
        edQtItemLargura.setEditable(enabled);
        edQtItemComprimento.setEditable(enabled);
        edQtItemAltura.setEditable(enabled);
        edQtItemTotal.setEditable(enabled);
    }

    public void setVisible() throws SQLException {
    	if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
    		btCadastrarCoordenadas.setVisible(!(ValueUtil.isEmpty(getDomain().flTipoAlteracao) && isEditing()));
    	}
    	if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado()) {
    		btCadastrarFotos.setVisible(isEnabled());
    	}
    	btSalvar.setVisible(isEnabled());
    	btExcluir.setVisible(isEditing() && isEnabled());
    }

    //@Override
    public void onFormShow() throws SQLException {
        setEnabled(isEnabled());
        setVisible();
    	super.onFormShow();
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, scroolContainer, LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(scroolContainer, clienteContainer, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
    	//--
    	int indexButtonAction = 5;
    	if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado() && isEnabled()) {
    		UiUtil.add(barBottomContainer, btCadastrarCoordenadas, indexButtonAction--);
    	}
    	if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado()) {
    		UiUtil.add(barBottomContainer, btCadastrarFotos, indexButtonAction--);
    	}
    	UiUtil.add(scroolContainer, new LabelName(Messages.PEDIDO_LABEL_DTEMISSAO), edDtEmissao, getLeft(), getNextY());
        UiUtil.add(scroolContainer, new LabelName(Messages.CONCORRENTE_NOME_ENTIDADE), cbConcorrente, getLeft(), getNextY());
        if (!isEditing()) {
        	UiUtil.add(scroolContainer, edFiltro, getLeft(), getNextY());
        	GridColDefinition[] gridColDefiniton; 
        	if (isTipoPesquisaMercadoValor()) {
        		gridColDefiniton = new GridColDefinition[] {
    				new GridColDefinition("", 0, LEFT),
				    new GridColDefinition("", 0, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_PRODUTO, -70, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_QTD, -20, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_VL, -20, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_OBSERVACAO, -20, LEFT),
        		};
        	} else {
        		gridColDefiniton = new GridColDefinition[] {
    				new GridColDefinition("", 0, LEFT),
				    new GridColDefinition("", 0, LEFT),
				    new GridColDefinition(Messages.PESQUISA_MERCADO_PRODUTO, -70, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_QTITEMFRENTE, -20, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_QTITEMPROFUNDIDADE, -20, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_QTITEMANDAR, -20, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_QTITEMTOTAL, -20, LEFT),
    				new GridColDefinition(Messages.PESQUISA_MERCADO_OBSERVACAO, -20, LEFT),
        		};
        	}
        	grid = UiUtil.createGridEdit(gridColDefiniton, false);
        	UiUtil.add(scroolContainer, grid, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
        	gridSettings();
        	carregaGrid();
        } else {
        	UiUtil.add(scroolContainer, lbProduto, edCdProduto, getLeft(), getNextY());
        	if (isTipoPesquisaMercadoValor()) {
        		UiUtil.add(scroolContainer, new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO), edQtItem, getLeft(), AFTER + HEIGHT_GAP);
        		UiUtil.add(scroolContainer, new LabelName(Messages.PRODUTO_LABEL_VLUNITARIO), edVlUnitario, getLeft(), AFTER + HEIGHT_GAP);
        	} else {
        		UiUtil.add(scroolContainer, new LabelName(Messages.PESQUISA_MERCADO_QTITEMFRENTE), edQtItemLargura, getLeft(), AFTER + HEIGHT_GAP);
        		UiUtil.add(scroolContainer, new LabelName(Messages.PESQUISA_MERCADO_QTITEMPROFUNDIDADE), edQtItemComprimento, getLeft(), AFTER + HEIGHT_GAP);
        		UiUtil.add(scroolContainer, new LabelName(Messages.PESQUISA_MERCADO_QTITEMANDAR), edQtItemAltura, getLeft(), AFTER + HEIGHT_GAP);
        		UiUtil.add(scroolContainer, new LabelName(Messages.PESQUISA_MERCADO_QTITEMTOTAL), edQtItemTotal, getLeft(), AFTER + HEIGHT_GAP);
        	}
        	UiUtil.add(scroolContainer, new LabelName(Messages.OBSERVACAO_LABEL), edDsObservacao, getLeft(), AFTER + HEIGHT_GAP);
        }
    }
    

    @Override
    protected void visibleState() throws SQLException {
    	super.visibleState();
    	if (isEditing()) {
    		cbConcorrente.setEditable(false);
    		repaintNow();
    	}
    	if (grid != null) {
    		carregaGrid();
    	}
    }
    	
    
	private void gridSettings() {
		grid.setGridControllable();
		if (isTipoPesquisaMercadoValor()) {
			edQtItem = grid.setColumnEditableDouble(3, true, 9);
			edVlUnitario = grid.setColumnEditableDouble(4, true, 9);
			edObs = grid.setColumnEditable(5, true, 255);
		} else {
			edQtItemLargura = grid.setColumnEditableInt(3, true, 9);
			edQtItemComprimento = grid.setColumnEditableInt(4, true, 9);
			edQtItemAltura = grid.setColumnEditableInt(5, true, 9);
			edQtItemTotal = grid.setColumnEditableInt(6, true, 9);
			edObs = grid.setColumnEditable(7, true, 255);
		}
        edObs.autoSelect = true;
	}
	
	private void carregaGrid() throws SQLException {
		grid.clear();
		Vector produtoConcorrenteList = new Vector();
		if (cbConcorrente.getValue() != null) {
			produtoConcorrenteList = ProdutoConcorrenteService.getInstance().filtraProdutoConcorrente(edFiltro.getText(), cbConcorrente.getValue());
		}
		if (produtoConcorrenteList.size() > 0) {
			String[][] gridItems = carregaGridItems(produtoConcorrenteList);
			grid.setItems(gridItems);
		}
	}

	private String[][] carregaGridItems(Vector produtoConcorrenteList) {
    	int size = produtoConcorrenteList.size();
    	int qtColumns = isTipoPesquisaMercadoValor() ? 6 : 8;
		String[][] gridItems = new String[size][qtColumns];
		for (int i = 0; i < size; i++) {
			ProdutoConcorrente produtoConcorrente = (ProdutoConcorrente) produtoConcorrenteList.items[i];
			PesquisaMercado pesquisaMercado = pesquisaMercadoHash.get(produtoConcorrente.getPrimaryKey());
			String[] item = new String[qtColumns];
			item[0] = produtoConcorrente.cdProdutoConcorrente;
			item[columnKey] = produtoConcorrente.getPrimaryKey();
			item[2] = produtoConcorrente.toString();
			if (pesquisaMercado != null) {
				if (isTipoPesquisaMercadoValor()) {
					item[3] = StringUtil.getStringValueToInterface(pesquisaMercado.qtItem);
					item[4] = StringUtil.getStringValueToInterface(pesquisaMercado.vlUnitario);
				} else {
					item[3] = StringUtil.getStringValueToInterface(pesquisaMercado.qtItemFrente);
					item[4] = StringUtil.getStringValueToInterface(pesquisaMercado.qtItemProfundidade);
					item[5] = StringUtil.getStringValueToInterface(pesquisaMercado.qtItemAndar);
					item[6] = StringUtil.getStringValueToInterface(pesquisaMercado.qtItemTotal);
				}
				item[isTipoPesquisaMercadoValor() ? 5 : 7] = pesquisaMercado.dsObservacao;
			} else {
				if (isTipoPesquisaMercadoValor()) {
					item[3] = "";
					item[4] = "";
				} else {
					item[3] = "";
					item[4] = "";
					item[5] = "";
					item[6] = "";
				}
				item[isTipoPesquisaMercadoValor() ? 5 : 7] = "";
			}
			gridItems[i] = item;
		}
		return gridItems;
	}

	@Override
	protected void onSave() throws SQLException {
		if (!selecionaNovoClientePesquisaMercado()) {
			return;
		}
		if (isEditing()) {
			setDomain(screenToDomain());
			update(getDomain());
		} else {
			Vector pesquisaMercadoList = new Vector();
			String cdClienteSession = SessionLavenderePda.getCliente().cdCliente;
			String flPesquisaNovoCliente = SessionLavenderePda.getCliente().isNovoCliente() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
			for (PesquisaMercado pesquisaMercado : pesquisaMercadoHash.values()) {
				pesquisaMercado.cdCliente = cdClienteSession;
				pesquisaMercado.flPesquisaNovoCliente = flPesquisaNovoCliente;
				if (isTipoPesquisaMercadoValor()) {
					PesquisaMercadoService.getInstance().validaPesquisaMercadoValor(pesquisaMercado);
				} else {
					PesquisaMercadoService.getInstance().validaPesquisaMercadoGondola(pesquisaMercado);
				}
				if ((pesquisaMercado.vlUnitario != 0 && isTipoPesquisaMercadoValor()) || (pesquisaMercado.qtItemTotal != 0 && isTipoPesquisaMercadoGondola())) {
					pesquisaMercadoList.addElement(pesquisaMercado);
				}
			}
			if (!pesquisaMercadoList.isEmpty()) {
				validaCoordenadas();
			}
			if (LavenderePdaConfig.obrigaCadastroCoordenadaNaPesquisaDeMercado() && closedByBtMaisTardeCoordenadas && longitude == 0 && latitude == 0 && !SessionLavenderePda.autorizadoPorSenhaCoordenadaPesquisaMercado && !pesquisaMercadoList.isEmpty()) {
				return;
			}
			if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado() || LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
				for (PesquisaMercado pesquisaMercado : pesquisaMercadoHash.values()) {
					if (ValueUtil.isNotEmpty(pesquisaMercado.fotoList)) {
						FotoPesquisaMercadoService.getInstance().insertFotosPesquisaMercado(pesquisaMercado);
					}
					if (LavenderePdaConfig.usaCadastroCoordenadaNaPesquisaDeMercado()) {
						pesquisaMercado.cdLatitude = latitude;
						pesquisaMercado.cdLongitude = longitude;
					}
				}
			}
			pesquisaNovoCliente = false;
			PesquisaMercadoService.getInstance().insert(pesquisaMercadoList);
		}
		if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor() && isTipoPesquisaMercadoValor()) {
			ClienteService.getInstance().atualizaNuDiasSemPesquisa(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
		}
		if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoGondola() && isTipoPesquisaMercadoGondola()) {
			ClienteService.getInstance().atualizaNuDiasSemPesquisa(Cliente.NMCOLUNA_NUDIASSEMPESQUISAGONDOLA);
		}
		list();
		close();
	}

	private void validaCoordenadas() {
		if (((LavenderePdaConfig.obrigaCadastroCoordenadaNaPesquisaDeMercado() && !SessionLavenderePda.autorizadoPorSenhaCoordenadaPesquisaMercado) || LavenderePdaConfig.usaCadastroAutomaticoCoordenadaPesquisaDeMercado())) {
			if (latitude == 0 && longitude == 0) {
				if (LavenderePdaConfig.usaCadastroAutomaticoCoordenadaPesquisaDeMercado()) {
					double[] lat_lon = new double[2];
					try {						
						lat_lon = PesquisaMercadoService.getInstance().getCoordenadasPesquisaMercado(lat_lon);
					} catch (ValidationException e) {
						UiUtil.showWarnMessage(e);
					}
					if (lat_lon[0] == 0 && lat_lon[1] == 0) {
						btCadastrarCoordenadasClick();
					} else {
						latitude = lat_lon[0];
						longitude = lat_lon[1];
					}
				} else {
					btCadastrarCoordenadasClick();
				}
			}
		}
	}


	private boolean selecionaNovoClientePesquisaMercado() throws SQLException {
		if (grid != null && grid.size() > 0 && LavenderePdaConfig.permitePesquisaMercadoNovoCliente && SessionLavenderePda.getCliente().isClienteDefaultParaNovaPesquisaMercado()) {
			ListNovoClienteWindow listNovoClienteWindow = new ListNovoClienteWindow(ListNovoClienteWindow.PESQUISAMERCSEMCLI);
			listNovoClienteWindow.popup();
			if (Cliente.CD_CLIENTE_DEFAULT_PARA_NOVA_PESQUISA_MERCADO.equals(SessionLavenderePda.getCliente().cdCliente)) {
				return false;
			}
			pesquisaNovoCliente = true;
			if (prevContainer instanceof ListPesquisaMercadoForm) {
				((ListPesquisaMercadoForm) prevContainer).reloadClientLabel();
			}
		}
		return true;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case KeyboardEvent.KEYBOARD_PRESS: {
				if (event.target == edFiltro) {
					filtrarClick();
				} 
				break;
			}
			case ControlEvent.PRESSED: {
				if (event.target == cbConcorrente) {
					edFiltro.setText("");
					carregaGrid();
				} else if (event.target == btCadastrarCoordenadas) {
					btCadastrarCoordenadasClick();
				} else if (event.target == btCadastrarFotos) {
					btCadastrarFotosClick();
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					filtrarClick();
				}
				break;
			}
			case ControlEvent.FOCUS_IN: {
				if (event.target == edObs) {
					edObs.setEditable(false);
					CadPesquisaMercadoObservacaoWindow cadPesquisaMercadoObservacaoWindow = new CadPesquisaMercadoObservacaoWindow(edObs.getValue());
					cadPesquisaMercadoObservacaoWindow.popup();
					edObs.setEditable(true);
					int nuColuna = isTipoPesquisaMercadoValor() ? 5 : 7;
					if (!cadPesquisaMercadoObservacaoWindow.closedByBtFechar) {
						grid.setCellText(grid.getSelectedIndex(), nuColuna, cadPesquisaMercadoObservacaoWindow.edObs.getValue(), true);
						grid.repaintNow();
						getPesquisaMercadoGrid().dsObservacao = grid.getCellText(grid.getSelectedIndex(), isTipoPesquisaMercadoValor() ? 5 : 7);
					}
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				if (event instanceof KeyEvent) {
					if (event.target == edFiltro && ((KeyEvent)event).isActionKey()) {
						filtrarClick();
						if (grid != null && grid.size() == 0) {
							edFiltro.requestFocus();
						}
					}
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (!isEditing() && isTipoPesquisaMercadoGondola()) {
					if (event.target == edQtItemLargura) {
						getPesquisaMercadoGrid().qtItemFrente = edQtItemLargura.getValueInt();
						calculaQtItemTotalGrid();
					} else if (event.target == edQtItemComprimento) {
						getPesquisaMercadoGrid().qtItemProfundidade = edQtItemComprimento.getValueInt();
						calculaQtItemTotalGrid();
					} else if (event.target == edQtItemAltura) {
						getPesquisaMercadoGrid().qtItemAndar = edQtItemAltura.getValueInt();
						calculaQtItemTotalGrid();
					} else if (event.target == edQtItemTotal) {
						getPesquisaMercadoGrid().qtItemTotal = edQtItemTotal.getValueInt();
					}
				} else if (!isEditing() && isTipoPesquisaMercadoValor()) {
					if (event.target == edQtItem) {
						getPesquisaMercadoGrid().qtItem = edQtItem.getValueDouble();
					} else if (event.target == edVlUnitario) {
						getPesquisaMercadoGrid().vlUnitario = edVlUnitario.getValueDouble();
					}
				}

				if (isEditing() && event.target instanceof EditNumberInt && isTipoPesquisaMercadoGondola() && event.target != edQtItemTotal) {
					calculaQtItemTotal();
				}

				if (event.target instanceof BaseEdit) {
					if (grid != null) grid.hideControl();
				}
				break;
			}
		}
	}

	private PesquisaMercado getPesquisaMercadoGrid() throws SQLException {
		int index = grid.getSelectedIndex();
		PesquisaMercado pesquisaMercado = pesquisaMercadoHash.get(grid.getCellText(index, columnKey));
		if (pesquisaMercado == null) {
			pesquisaMercado = createPesquisaMercado(index);
		}
		addToInsertList(pesquisaMercado, index);
		return pesquisaMercado;
	}

	private PesquisaMercado createPesquisaMercado(int index) throws SQLException {
		PesquisaMercado pesquisaMercado;
		pesquisaMercado = new PesquisaMercado();
		pesquisaMercado.cdPesquisaMercado = PesquisaMercadoService.getInstance().generateIdGlobal() + index;
		pesquisaMercado.cdUsuarioEmissao = Session.getCdUsuario();
		pesquisaMercado.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pesquisaMercado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pesquisaMercado.flOrigemPesquisaMercado = OrigemPedido.FLORIGEMPEDIDO_PDA;
		pesquisaMercado.dtEmissao = new Date();
		pesquisaMercado.cdConcorrente = cbConcorrente.getValue();
		pesquisaMercado.cdProdutoConcorrente = grid.getCellText(index, 0);
		pesquisaMercado.flPesquisaNovoCliente = pesquisaNovoCliente || SessionLavenderePda.getCliente().isNovoCliente() ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		pesquisaMercado.cdTipoPesquisa = cdTipoPesquisa;
		return pesquisaMercado;
	}

	private void btCadastrarFotosClick() throws SQLException {
		if (isEditing()) {
			PesquisaMercado pesquisaMercado = (PesquisaMercado) getDomain();
			pesquisaMercado.cdProduto = edCdProduto.getText();
			ImageSliderPesquisaMercadoWindow imageSliderPesquisaMercadoWindow = new ImageSliderPesquisaMercadoWindow(SessionLavenderePda.getCliente(), pesquisaMercado, isEnabled(), true);
			imageSliderPesquisaMercadoWindow.popup();
		} else {
			int index = grid.getSelectedIndex();
			if (index < 0) {
				UiUtil.showWarnMessage(Messages.PESQUISA_MERCADO_FOTO_ITEM_NAO_SELECIONADO);
			} else {
				PesquisaMercado pesquisaMercado = pesquisaMercadoHash.get(grid.getCellText(index, columnKey));
				if (pesquisaMercado == null) {
					pesquisaMercado = createPesquisaMercado(index);
					addToInsertList(pesquisaMercado, index);
				}
				ImageSliderPesquisaMercadoWindow imageSliderPesquisaMercadoWindow = new ImageSliderPesquisaMercadoWindow(SessionLavenderePda.getCliente(), pesquisaMercado, true, false);
				imageSliderPesquisaMercadoWindow.popup();
				addToInsertList(pesquisaMercado, index);
			}
		}
	}

	private void addToInsertList(PesquisaMercado pesquisaMercado, int index) {
		if (!pesquisaMercado.inInsertList) {
			pesquisaMercado.inInsertList = true;
			pesquisaMercadoHash.put(grid.getCellText(index, columnKey), pesquisaMercado);
		}
	}

	private void btCadastrarCoordenadasClick() {
		CadCoordenadasPesquisaMercadoWindow cadCoordenadasPesquisaMercadoWindow = new CadCoordenadasPesquisaMercadoWindow(SessionLavenderePda.getCliente(), new double[] {latitude, longitude}, isEditing());
		cadCoordenadasPesquisaMercadoWindow.popup();
		closedByBtMaisTardeCoordenadas = cadCoordenadasPesquisaMercadoWindow.closedByBtFechar;
		if (cadCoordenadasPesquisaMercadoWindow.latitude != 0 && cadCoordenadasPesquisaMercadoWindow.longitude != 0 && cadCoordenadasPesquisaMercadoWindow.cadastrouCoordenada) {
			latitude = cadCoordenadasPesquisaMercadoWindow.latitude;
			longitude = cadCoordenadasPesquisaMercadoWindow.longitude;
		}
	}

	private void filtrarClick() throws SQLException {
		if (ValueUtil.isEmpty(cbConcorrente.getValue())) {
			UiUtil.showErrorMessage(Messages.PESQUISA_MERCADO_CONCORRENTE_NAO_SELECIONADO);
		} else {
			carregaGrid();
		}
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		PesquisaMercado pesquisaMercado = (PesquisaMercado) getDomain();
		if (isTipoPesquisaMercadoValor()) {
			pesquisaMercado.vlUnitario = edVlUnitario.getValueDouble();
			pesquisaMercado.qtItem = edQtItem.getValueDouble();
		} else {
			pesquisaMercado.qtItemFrente = edQtItemLargura.getValueInt();
			pesquisaMercado.qtItemProfundidade = edQtItemComprimento.getValueInt();
			pesquisaMercado.qtItemAndar = edQtItemAltura.getValueInt();
			pesquisaMercado.qtItemTotal = edQtItemTotal.getValueInt();
		}
		pesquisaMercado.dsObservacao = edDsObservacao.getValue();
		if (latitude != 0 && longitude != 0) {
			pesquisaMercado.cdLatitude = latitude;
			pesquisaMercado.cdLongitude = longitude;
		}
		return pesquisaMercado;
	}

	@Override
	protected void voltarClick() throws SQLException {
		if (!isEditing() && pesquisaMercadoInsertListHasFotos()) {
			if (UiUtil.showConfirmYesCancelMessage(Messages.PESQUISA_MERCADO_DESEJA_DESCARTAR_FOTOS) == 1) {
				for (PesquisaMercado pesquisaMercado : pesquisaMercadoHash.values()) {
					if (ValueUtil.isNotEmpty(pesquisaMercado.fotoList)) {
						FotoPesquisaMercadoService.getInstance().excluiFotoPesquisaMercadoArquivo(pesquisaMercado);
					}
				}
				super.voltarClick();
			}
		} else {
			super.voltarClick();
		}
	}
	
	private boolean pesquisaMercadoInsertListHasFotos() {
		if (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado()) {
			for (PesquisaMercado pesquisaMercado : pesquisaMercadoHash.values()) {
				if (ValueUtil.isNotEmpty(pesquisaMercado.fotoList)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		((ListPesquisaMercadoForm) prevContainer).invalidateCadInstace();
	}
	
	private boolean isTipoPesquisaMercadoValor() {
		return PesquisaMercado.CDTIPOPESQUISA_VALOR.equals(cdTipoPesquisa);
	}
	
	private boolean isTipoPesquisaMercadoGondola() {
		return PesquisaMercado.CDTIPOPESQUISA_GONDOLA.equals(cdTipoPesquisa);
	}
	
	private void calculaQtItemTotal() {
		edQtItemTotal.setValue(ValueUtil.getIntegerValue(edQtItemLargura.getValueInt() * edQtItemComprimento.getValueInt() * edQtItemAltura.getValueInt()));
	}
	
	private void calculaQtItemTotalGrid() throws SQLException {
		int index = grid.getSelectedIndex();
		PesquisaMercado pesquisaMercado = getPesquisaMercadoGrid();
		pesquisaMercado.qtItemTotal = pesquisaMercado.qtItemAndar * pesquisaMercado.qtItemProfundidade * pesquisaMercado.qtItemFrente;
		grid.setCellText(index, 6 , StringUtil.getStringValueToInterface(pesquisaMercado.qtItemTotal));
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