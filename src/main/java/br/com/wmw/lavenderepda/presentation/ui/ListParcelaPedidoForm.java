package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ParcelaPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.sys.Settings;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListParcelaPedidoForm extends BaseCrudListForm {
	
	private static final int COLUMN_VENCIMENTO = 3;
	private Pedido pedido;
	private LabelName lbVlParcelas;
	private LabelValue lvVlParcelas;
	private LabelName lbVlRestante;
	private LabelValue lvVlRestante;
	private LabelName lbDiasParcelamento;
	private EditText edDiasParcelamento;
	private LabelName lbObsDiasParcelamento;
	private LabelName lbQtParcelas;
	private EditText edQtParcelas;

	private BaseButton btGerarParcelas;
	private double vlTotal;
	private boolean mostrouAlertaPeloCalendario;

    public ListParcelaPedidoForm() throws SQLException {
        super(Messages.PARCELAPEDIDO_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadParcelaPedidoForm());
        singleClickOn = true;
        lbVlParcelas = new LabelName(Messages.PARCELAPEDIDO_LABEL_VLTOTAL);
        lvVlParcelas = new LabelValue("99999999");
        lvVlRestante = new LabelValue("99999999");
		edQtParcelas = new EditText("@@@@", 4);
		edQtParcelas.setEnabled(false);
		lbDiasParcelamento = new LabelName(Messages.PARCELAPEDIDO_LABEL_PARCELAS);
		lbObsDiasParcelamento = new LabelName(Messages.PARCELAPEDIDO_LABEL_PARCELAS_OBS);
		lbQtParcelas = new LabelName(Messages.PARCELAPEDIDO_LABEL_QT_PARCELAS);
		edDiasParcelamento = new EditText("@@@@@@@@@@", 50);
		btGerarParcelas = new BaseButton(Messages.PARCELAPEDIDO_BT_GERAR_PARCELAS);
        if (LavenderePdaConfig.isGeraParcelasEmPercentual()) {
        	lbVlRestante = new LabelName(Messages.PARCELAPEDIDO_LABEL_PCTRESTANTE);
        	lvVlParcelas.usePercentValue = true;
        	lvVlRestante.usePercentValue = true;
        } else {
        	lbVlRestante = new LabelName(Messages.PARCELAPEDIDO_LABEL_VLRESTANTE);
        	lvVlParcelas.useCurrencyValue = true;
        	lvVlRestante.useCurrencyValue = true;
        }
        barTopContainer.setVisible(false);
        barBottomContainer.setVisible(false);
    	btNovo.setText(Messages.PARCELAPEDIDO_BT_INSERIR_PARCELA);
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return ParcelaPedidoService.getInstance();
    }

    public void setPedido(Pedido pedido) {
		this.pedido = pedido;
		((CadParcelaPedidoForm)getBaseCrudCadForm()).pedido = pedido;
	}

    @Override
    protected Vector getDomainList() throws java.sql.SQLException {
    	if (pedido == null) {
    		return new Vector(0);
    	}
    	return pedido.parcelaPedidoList;
    }

    @Override
    protected String[] getItem(Object domain) throws SQLException {
        ParcelaPedido parcelaPedido = (ParcelaPedido) domain;
	    return new String[] {
        		StringUtil.getStringValue(parcelaPedido.rowKey),
        		LavenderePdaConfig.isGeraParcelasEmPercentual() ? StringUtil.getStringValue(parcelaPedido.vlPctParcela) : "",
        		LavenderePdaConfig.isGeraParcelasEmPercentual() ? StringUtil.getStringValue(parcelaPedido.qtDiasPrazo) : "",
        		StringUtil.getStringValue(parcelaPedido.dtVencimento),
        		LavenderePdaConfig.isGeraParcelasEmPercentual() ? "" : StringUtil.getStringValueToInterface(parcelaPedido.vlParcela)
        };
    }

	
	private void trocaVencimento(Date newDtVencimento) throws SQLException {
		if (LavenderePdaConfig.permiteEditarVecimentoParcela && gridEdit.getSelectedIndex() >= 0) {
			try {
				UiUtil.createProcessingMessage();
				if (ValueUtil.isEmpty(newDtVencimento)) {
					return;
				}
				ParcelaPedido parcelaPedido = (ParcelaPedido) getSelectedDomain();
				if (parcelaPedido.dtVencimento != null && !parcelaPedido.dtVencimento.equals(newDtVencimento)) {
					validaTrocaDataVencimento(newDtVencimento);
					int diffDay = 0;
					if (isSugereAlteracaoProximosVencimentos(parcelaPedido)) {
						diffDay = DateUtil.getDaysBetween(newDtVencimento, parcelaPedido.dtVencimento);
					}
					parcelaPedido.dtVencimento = newDtVencimento;
					parcelaPedido.qtDiasPrazo = DateUtil.getDaysBetween(parcelaPedido.dtVencimento, new Date());
					ParcelaPedidoService.getInstance().update(parcelaPedido);
					ajustaProximasParcelas(parcelaPedido, diffDay);
					list();
				}
			} catch (Exception e) {
				mostrouAlertaPeloCalendario = true;
				throw e;
			} finally {
				PedidoService.getInstance().findParcelaPedidoList(pedido);
				list();
				UiUtil.unpopProcessingMessage();
			}
		}
	}

	private void ajustaProximasParcelas(final ParcelaPedido parcelaPedido, int diffDay) throws SQLException {
		if (isSugereAlteracaoProximosVencimentos(parcelaPedido) && UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.PARCELAPEDIDO_MSG_AJUSTAR_PROXIMOS_VENCIMENTOS, diffDay))) {
			int cdParcela = ValueUtil.getIntegerValue(parcelaPedido.cdParcela);
			int size = pedido.parcelaPedidoList.size();
			for (int i = cdParcela; i < size; i++) {
				ParcelaPedido proxParcela = (ParcelaPedido) parcelaPedido.clone();
				proxParcela.cdParcela = StringUtil.getStringValue(i + 1);
				int index = pedido.parcelaPedidoList.indexOf(proxParcela);
				proxParcela = (ParcelaPedido) pedido.parcelaPedidoList.elementAt(index);
				proxParcela.dtVencimento.advance(diffDay);
				proxParcela.qtDiasPrazo += diffDay;
				ParcelaPedidoService.getInstance().update(proxParcela);
			}
		}
	}
	
	private boolean isSugereAlteracaoProximosVencimentos(final ParcelaPedido parcelaPedido) {
		int cdParcela = ValueUtil.getIntegerValue(parcelaPedido.cdParcela);
		int size = pedido.parcelaPedidoList.size();
		return LavenderePdaConfig.sugereAlteracaoProximosVencimentos && cdParcela < size;
	}

	private void validaTrocaDataVencimento(Date newDtVencimento) throws SQLException {
		ParcelaPedidoService.getInstance().validaTrocaDataVencimento(newDtVencimento, pedido, (ParcelaPedido) getSelectedDomain());
	}
	
	@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

	@Override
    public void list() throws SQLException {
		if (LavenderePdaConfig.permiteEditarVecimentoParcela && pedido != null && pedido.isPedidoAbertoEditavel()) {
			if (ValueUtil.isNotEmpty(pedido.parcelaPedidoList)) {
				ParcelaPedido entrada = (ParcelaPedido) pedido.parcelaPedidoList.elementAt(0);
				Date dtEntradaAtual = new Date();
				dtEntradaAtual.advance(entrada.qtDiasPrazo);
				if (!entrada.dtVencimento.equals(dtEntradaAtual)) {
					ParcelaPedidoService.getInstance().insertParcelasPedido(pedido);
				}
			}
		}
    	super.list();
    	updateTotalizador();
		updateQtDiasVctoParcelas();
	}

	private void updateQtDiasVctoParcelas() throws SQLException {
		if (pedido != null && geraParcelasPorVencimento()) {
			if (!ValueUtil.isEmpty(pedido.qtDiasVctoParcelas)) {
				edDiasParcelamento.setValue(pedido.qtDiasVctoParcelas);
			}
			edQtParcelas.setValue(StringUtil.getStringValue(pedido.getCondicaoPagamento().getNuParcelas()));
		}

	}

	protected void updateTotalizador() {
    	if (pedido != null) {
	    	vlTotal = 0;
			int size = pedido.parcelaPedidoList.size();
	        for (int i = 0; i < size; i++) {
	    		ParcelaPedido parcelaPedido = (ParcelaPedido) pedido.parcelaPedidoList.items[i];
	    		if (LavenderePdaConfig.isGeraParcelasEmPercentual()) {
	    			vlTotal += ValueUtil.getDoubleValue(StringUtil.getStringValue(parcelaPedido.vlPctParcela, 3));
	    		} else {
	    			vlTotal += ValueUtil.getDoubleValue(StringUtil.getStringValue(parcelaPedido.vlParcela, 3));
	    		}
	    	}
	    	vlTotal = ValueUtil.getDoubleValue(StringUtil.getStringValue(vlTotal, 3));
	    	lvVlParcelas.setValue(vlTotal);
	    	lvVlRestante.setValue(ParcelaPedidoService.getInstance().getRestante(pedido, vlTotal));
    	} else {
    		lvVlParcelas.setValue(0);
    		lvVlRestante.setValue(0);
    	}
    }

    @Override
    protected void onFormStart() throws SQLException {
        GridColDefinition[] gridColDefiniton = {
    		new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
    		LavenderePdaConfig.isGeraParcelasEmPercentual() ? new GridColDefinition(Messages.PARCELAPEDIDO_LABEL_PCT, -20, LEFT) : new GridColDefinition(FrameworkMessages.CAMPO_VAZIO, 0, LEFT),
    		LavenderePdaConfig.isGeraParcelasEmPercentual() ? new GridColDefinition(Messages.PARCELAPEDIDO_LABEL_PRAZO, -20, LEFT) : new GridColDefinition(FrameworkMessages.CAMPO_VAZIO, 0, LEFT),
    		new GridColDefinition(Messages.PARCELAPEDIDO_LABEL_DTVENCIMENTO, Settings.screenWidth / 3, LEFT),
    		LavenderePdaConfig.isGeraParcelasEmPercentual() ? new GridColDefinition(FrameworkMessages.CAMPO_VAZIO, 0, LEFT) : new GridColDefinition(Messages.PARCELAPEDIDO_LABEL_VLPARCELA, -60, LEFT)
        };
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, lbVlParcelas, lvVlParcelas, getLeft(), TOP + HEIGHT_GAP);
        UiUtil.add(this, btNovo, RIGHT - WIDTH_GAP_BIG, SAME - lvVlParcelas.getHeight());
        UiUtil.add(this, lbVlRestante, lvVlRestante, getLeft(), AFTER + HEIGHT_GAP);
		if (geraParcelasPorVencimento()) {
			UiUtil.add(this, lbQtParcelas, edQtParcelas, getLeft(), AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
			UiUtil.add(this, lbDiasParcelamento, AFTER + WIDTH_GAP_BIG, BEFORE, PREFERRED, PREFERRED);
			UiUtil.add(this, edDiasParcelamento, SAME, AFTER, FILL - btGerarParcelas.getPreferredWidth() - WIDTH_GAP_BIG * 2);
			UiUtil.add(this, btGerarParcelas, getRight(), SAME);
			UiUtil.add(this, lbObsDiasParcelamento, getLeft(), AFTER + HEIGHT_GAP);
		} else if (LavenderePdaConfig.permiteEditarVecimentoParcela) {
			UiUtil.add(this, btGerarParcelas, getRight(), SAME);
		}
		UiUtil.add(this, gridEdit, getLeft(), AFTER + HEIGHT_GAP, getWFill(), FILL - HEIGHT_GAP_BIG);
        if (pedido != null) {
			edDiasParcelamento.setEnabled(pedido.isPedidoAberto() || pedido.isPedidoReaberto);
			btGerarParcelas.setVisible(pedido.isPedidoAberto() || pedido.isPedidoReaberto);
		}
		if (LavenderePdaConfig.permiteEditarVecimentoParcela) {
			gridEdit.setColumnEditableDate(COLUMN_VENCIMENTO, true);
		}
    }
	
    public void setPermiteEdicaoParcelas(boolean permiteEdicao) {
    	singleClickOn = permiteEdicao;
    	btNovo.setVisible(permiteEdicao);
    }

    @Override
    public BaseDomain getSelectedDomain() throws SQLException {
    	return (BaseDomain)pedido.parcelaPedidoList.items[gridEdit.getSelectedIndex()];
    }

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btGerarParcelas) {
				btGerarParcelasClick();
			} else if (event.target instanceof CalendarWmw) {
				CalendarWmw calendarWmw = (CalendarWmw) event.target;
				Date teste = calendarWmw.getSelectedDate();
				trocaVencimento(teste);
			}
			break;
		}
		case ValueChangeEvent.VALUE_CHANGE_GRID: {
			if (event.target instanceof EditDate) {
				EditDate editDate = (EditDate) event.target;
				if (!mostrouAlertaPeloCalendario) {
					trocaVencimento(editDate.getValue());
				} else {
					editDate.setValue(null);
				}
				mostrouAlertaPeloCalendario = false;
			}
			break;
		}
		}
	}

	protected BaseDomain getDomainFilter() throws SQLException {
		return null;
	}

	private void btGerarParcelasClick() throws SQLException {
		if (LavenderePdaConfig.permiteEditarVecimentoParcela) {
			if (UiUtil.showWarnConfirmYesNoMessage(Messages.PARCELAPEDIDO_MSG_PARCELA_COFIRMA_GERACAO)) {
				ParcelaPedidoService.getInstance().resetParcelasPedidoPorTipoCondPgto(pedido);
			}
		} else {
			if (ValueUtil.isEmpty(edDiasParcelamento.getValue())) {
				UiUtil.showWarnMessage(Messages.PARCELAPEDIDO_LABEL_PARCELAS_ERRO_VAZIO);
				return;
			}
			pedido.qtDiasVctoParcelas = edDiasParcelamento.getValue();
			PedidoService.getInstance().updateColumn(pedido.rowKey, "qtDiasVctoParcelas", pedido.qtDiasVctoParcelas, totalcross.sql.Types.VARCHAR);
			ParcelaPedidoService.getInstance().tipoCondPagtoVencimento(pedido, pedido.getCondicaoPagamento());
		}
		list();
	}

	private boolean geraParcelasPorVencimento() throws SQLException {
		return pedido != null && pedido.geraParcelasPorVencimento();
	}

}
