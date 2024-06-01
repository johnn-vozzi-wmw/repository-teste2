package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CategoriaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusAgendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoCadastroClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoClienteRedeComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class FiltroAgendaVisitaAvancadoWindow extends WmwWindow {
	
	private ListAgendaVisitaForm listAgendaVisitaForm;
	private ButtonPopup btLimpar;
	private ButtonPopup btFiltrar;
	public CheckBoolean ckClientesAtrasados;
	protected MarcadorComboBox cbMarcador;
	protected StatusAgendaComboBox cbStatusAgenda;
	protected TipoClienteRedeComboBox cbTipoClienteRedeComboBox;
	protected TipoCadastroClienteComboBox cbTipoCadastroClienteComboBox;
	protected EditDate edDtInicial;
	protected EditDate edDtFinal;
	protected RepresentanteSupervComboBox cbRepresentante;
	protected RedeComboBox cbRede;
	protected CategoriaComboBox cbCategoria;
	protected FornecedorComboBox cbFornecedor;
	
	public boolean aplicouFiltros = false;

	public FiltroAgendaVisitaAvancadoWindow(ListAgendaVisitaForm listAgendaVisitaForm) throws SQLException {
		super(Messages.PRODUTO_FILTRO_AVANCADO);
		this.listAgendaVisitaForm = listAgendaVisitaForm;
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		btFiltrar = new ButtonPopup(FrameworkMessages.BOTAO_FILTRAR);
		cbRepresentante = new RepresentanteSupervComboBox();
		edDtInicial = new EditDate();
		edDtInicial.setValue(DateUtil.getCurrentDate());
		edDtFinal = new EditDate();
		edDtFinal.setValue(DateUtil.getCurrentDate());
		cbTipoCadastroClienteComboBox = new TipoCadastroClienteComboBox();
		cbTipoClienteRedeComboBox = new TipoClienteRedeComboBox();
		cbStatusAgenda = new StatusAgendaComboBox();
		cbMarcador = new MarcadorComboBox(Marcador.ENTIDADE_MARCADOR_CLIENTE);
		ckClientesAtrasados = new CheckBoolean(MessageUtil.getMessage(Messages.MSG_CLIENTE_SEM_PEDIDOS, LavenderePdaConfig.nuDiasFiltroClienteSemPedido));
		cbRede = new RedeComboBox();
		cbCategoria = new CategoriaComboBox();
		cbFornecedor = new FornecedorComboBox();
		setDefaultRect();
		loadDefaultFilters();
		carregaFiltrosAplicados();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_REPRESENTANTE)) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
    	}
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_PERIODO_DATA)) {
        	UiUtil.add(this, new LabelName(Messages.AGENDAVISITA_LABEL_PERIODO), getLeft(), getNextY());
        	UiUtil.add(this, edDtInicial, getLeft(), getNextY());
        	UiUtil.add(this, edDtFinal, AFTER + WIDTH_GAP, SAME);
        }
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CADASTRO_CLIENTE)) {
			UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_TIPOCADASTRO), cbTipoCadastroClienteComboBox, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CLIENTE_REDE)) {
	       	UiUtil.add(this, new LabelName(Messages.TIPO_CLIENTE_REDE_TITULO), cbTipoClienteRedeComboBox, getLeft(), getNextY());
	    }
		if (!LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_STATUS_AGENDA)) {
        	UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_STATUS), cbStatusAgenda, getLeft(), getNextY());
        }
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_MARCADORES)) {
			UiUtil.add(this, new LabelName(Messages.MARCADOR_NOME_ENTIDADE), cbMarcador, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRede, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			UiUtil.add(this, new LabelName(Messages.CATEGORIA_NOME_ENTIDADE), cbCategoria, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_FORNECEDOR)) {
			UiUtil.add(this, new LabelName(MessageUtil.getMessage(Messages.CLIENTE_NUDIAS_FORNECEDOR_SEM_PEDIDO, LavenderePdaConfig.nuDiasClienteSemPedidoFornecedor)), cbFornecedor, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CLIENTE_SEM_PEDIDO)) {
        	UiUtil.add(this, ckClientesAtrasados, getLeft(), getNextY());
        }
		addButtonPopup(btFiltrar);
		addButtonPopup(btLimpar);
		addButtonPopup(btFechar);
	}
	
	@Override
	public void onEvent(Event event) {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFechar) {
					super.onEvent(event);
				} else if (event.target == btFiltrar) {
					try {
						aplicaFiltrosNoListAgendaVisitaForm();
						btFiltrarClick();
					} catch (SQLException e) {
						ExceptionUtil.handle(e);
					}
				} else if (event.target == btLimpar) {
					try {
						loadDefaultFilters();
						aplicaFiltrosNoListAgendaVisitaForm();
					} catch (SQLException e) {
						ExceptionUtil.handle(e);
					}
				} else {
					listAgendaVisitaForm.onEvent(event);
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
	    		if (event.target == edDtInicial || event.target == edDtFinal) {
	    			if (event.target == edDtInicial) {
	    				try {
							edDtInicialClick();
						} catch (SQLException e) {
							ExceptionUtil.handle(e);
						}
	    			} else {
	    				edDtFinalClick();
	    			}
	    		}
			}
		}
	}
	
	@Override
	protected void onPopup() {
		super.onPopup();
		reposition();
		ajustaTamanhoBotoes();
	}
	
	private void btFiltrarClick() throws SQLException {
		fecharWindow();
		closedByBtFechar = false;
	}
	
	private void loadDefaultFilters() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_REPRESENTANTE)) {
    		cbRepresentante.carregaRepresentantes();
    	}
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_PERIODO_DATA)) {
        	edDtInicial.setValue(DateUtil.getCurrentDate());
        	edDtFinal.setValue(DateUtil.getCurrentDate());
        }
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CADASTRO_CLIENTE)) {
			cbTipoCadastroClienteComboBox.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CLIENTE_REDE)) {
	       	cbTipoClienteRedeComboBox.setSelectedIndex(0);
	    }
		if (!LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_STATUS_AGENDA)) {
        	cbStatusAgenda.setSelectedIndex(1);
        }
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_MARCADORES)) {
			cbMarcador.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CLIENTE_SEM_PEDIDO)) {
        	ckClientesAtrasados.setChecked(false);
        }
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			cbRede.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			cbCategoria.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_FORNECEDOR)) {
			cbFornecedor.setSelectedIndex(0);
		}
	}
	
	public void carregaFiltrosAplicados() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_REPRESENTANTE)) {
			cbRepresentante.setSelectedIndex(listAgendaVisitaForm.cbRepresentante.getSelectedIndex());
    	}
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_PERIODO_DATA)) {
			edDtInicial.setValue(listAgendaVisitaForm.edDtInicial.getValue());
			edDtFinal.setValue(listAgendaVisitaForm.edDtFinal.getValue());
        }
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CADASTRO_CLIENTE)) {
			cbTipoCadastroClienteComboBox.setSelectedIndex(listAgendaVisitaForm.cbTipoCadastroClienteComboBox.getSelectedIndex());
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CLIENTE_REDE)) {
			cbTipoClienteRedeComboBox.setSelectedIndex(listAgendaVisitaForm.cbTipoClienteRedeComboBox.getSelectedIndex());
	    }
		if (!LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_STATUS_AGENDA)) {
			cbStatusAgenda.setSelectedIndex(listAgendaVisitaForm.cbStatusAgenda.getSelectedIndex());
        }
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_MARCADORES)) {
			cbMarcador.setSelectedIndex(listAgendaVisitaForm.cbMarcador.getSelectedIndex());
		}
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CLIENTE_SEM_PEDIDO)) {
			ckClientesAtrasados.setChecked(listAgendaVisitaForm.ckClientesAtrasados.isChecked());
        }
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			cbRede.setSelectedIndex(listAgendaVisitaForm.cbRede.getSelectedIndex());
		}
		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			cbCategoria.setSelectedIndex(listAgendaVisitaForm.cbCategoria.getSelectedIndex());
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_FORNECEDOR)) {
			cbFornecedor.setSelectedIndex(listAgendaVisitaForm.cbFornecedor.getSelectedIndex());
		}
	}
	
	public void aplicaFiltrosNoListAgendaVisitaForm() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_REPRESENTANTE)) {
			listAgendaVisitaForm.cbRepresentante.setSelectedIndex(cbRepresentante.getSelectedIndex());
			cbRepresentanteChange();
    	}
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_PERIODO_DATA)) {
			listAgendaVisitaForm.edDtInicial.setValue(edDtInicial.getValue());
			listAgendaVisitaForm.edDtFinal.setValue(edDtFinal.getValue());
        }
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CADASTRO_CLIENTE)) {
			listAgendaVisitaForm.cbTipoCadastroClienteComboBox.setSelectedIndex(cbTipoCadastroClienteComboBox.getSelectedIndex());
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_TIPO_CLIENTE_REDE)) {
			listAgendaVisitaForm.cbTipoClienteRedeComboBox.setSelectedIndex(cbTipoClienteRedeComboBox.getSelectedIndex());
	    }
		if (!LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_STATUS_AGENDA)) {
			listAgendaVisitaForm.cbStatusAgenda.setSelectedIndex(cbStatusAgenda.getSelectedIndex());
        }
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_MARCADORES)) {
			listAgendaVisitaForm.cbMarcador.setSelectedIndex(cbMarcador.getSelectedIndex());
		}
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CLIENTE_SEM_PEDIDO)) {
			listAgendaVisitaForm.ckClientesAtrasados.setChecked(ckClientesAtrasados.isChecked());
        }
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			listAgendaVisitaForm.cbRede.setSelectedIndex(cbRede.getSelectedIndex());
		}
		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
			listAgendaVisitaForm.cbCategoria.setSelectedIndex(cbCategoria.getSelectedIndex());
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_FORNECEDOR)) {
			listAgendaVisitaForm.cbFornecedor.setSelectedIndex(cbFornecedor.getSelectedIndex());
		}
		aplicouFiltros = true;
	}
	
	protected void edDtInicialClick() throws SQLException {
		if (ValueUtil.isEmpty(edDtInicial.getValue()) || ValueUtil.isEmpty(edDtFinal.getValue())) return;
		
		if (edDtInicial.getValue().isAfter(edDtFinal.getValue())) {
			edDtFinal.setValue(edDtInicial.getValue());
			return;
		} 
		Date limitPastDate = AgendaVisitaService.getInstance().getLastValidDay(new Date());
		if (DateUtil.isBefore(edDtInicial.getValue(), limitPastDate)) {
			edDtInicial.setValue(limitPastDate);
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.AGENDAVISITA_MSG_ERRO, DateUtil.formatDateDDMMYYYY(limitPastDate)));
		}
	}
	
	protected void edDtFinalClick() {
		if (ValueUtil.isEmpty(edDtFinal.getValue()) || ValueUtil.isEmpty(edDtInicial.getValue())) return;
		
		if (edDtFinal.getValue().isBefore(edDtInicial.getValue())) {
			edDtFinal.setValue(edDtInicial.getValue());
			UiUtil.showErrorMessage(Messages.AGENDAVISITA_ERRO_FINAL_ANTERIOR_INICIAL);
		}
	}
	
	protected void cbRepresentanteChange() throws SQLException {
		if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
		} else {
			SessionLavenderePda.setRepresentante(null);
		}
	}
}
