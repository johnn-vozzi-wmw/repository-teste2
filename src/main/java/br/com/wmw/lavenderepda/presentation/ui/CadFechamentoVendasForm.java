package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ResumoDia;
import br.com.wmw.lavenderepda.business.domain.SaldoVendaRep;
import br.com.wmw.lavenderepda.business.service.ResumoDiaService;
import br.com.wmw.lavenderepda.business.service.SaldoVendaRepService;
import totalcross.ui.Label;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class CadFechamentoVendasForm extends BaseCrudCadForm {
	
	private BaseScrollContainer sContainer;
	private SessionContainer containerCabecalho;
	private LabelName lbDataUltimoFechamentoVendas;
	private EditText edDataUltimoFechamentoVendas;
	private LabelName lbDataFechamentoVendas;
	private EditDate edDataFechamentoVendas;
	private LabelName lbVlSaldoAnterior;
	private LabelValue lvVlSaldoAnterior;
	private LabelName lbVlTotalPagamentos;
	private LabelValue lvVlTotalPagamentos;
	private LabelName lbVlTotalVendas;
	private LabelValue lvVlTotalVendas;
	private LabelName lbVlTotalValorizacoes;
	private LabelValue lvVlTotalValorizacoes;
	private LabelName lbVlTotalPromissorias;
	private EditNumberFrac edVlTotalPromissorias;
	private LabelName lbVlSaldoFinal;
	private LabelValue lvVlSaldoFinal;
	private Label lbAvisoFechamentoJaEfetuado;
	
	private ResumoDia resumoDia; 
    
    public CadFechamentoVendasForm() {
		super(Messages.FECHAMENTO_VENDAS_TITULO);
		containerCabecalho = new SessionContainer();
		sContainer = new BaseScrollContainer(false, true);
		sContainer.setBackColor(getBackColor());
		lbVlSaldoAnterior = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_VLSALDOANTERIOR);
		lvVlSaldoAnterior = new LabelValue();
		lbVlTotalPagamentos = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_VLTOTALPAGAMENTO);
		lvVlTotalPagamentos = new LabelValue();
		lbVlTotalVendas = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_VLTOTALVENDAS);
		lvVlTotalVendas = new LabelValue();
		lbVlTotalValorizacoes = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_VLTOTALVALORIZACAO);
		lvVlTotalValorizacoes = new LabelValue();
		lbVlTotalPromissorias = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_VLTOTALPROMISSORIA);
		edVlTotalPromissorias = new EditNumberFrac("0123456789", 10);
		edVlTotalPromissorias.setEditable(false);
		lbVlSaldoFinal = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_VLSALDOFINAL);
		lvVlSaldoFinal = new LabelValue();
		lbAvisoFechamentoJaEfetuado = new Label(" ");
		lbDataFechamentoVendas = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_DTFIMPERIODO);
		edDataFechamentoVendas = new EditDate();
		edDataFechamentoVendas.onlySelectByCalendar();
		lbDataUltimoFechamentoVendas = new LabelName(Messages.FECHAMENTO_VENDAS_LABEL_DTINICIOPERIODO);
		edDataUltimoFechamentoVendas = new EditText(DateUtil.getCurrentDate().toString(), 10);
		edDataUltimoFechamentoVendas.setEnabled(false);
		getLasDtFechamento();
    }

    //-----------------------------------------------

    @Override
    public String getEntityDescription() {
    	return Messages.FECHAMENTO_VENDAS_TITULO;
    }

    @Override
    protected CrudService getCrudService() {
        return ResumoDiaService.getInstance();
    }
    
    @Override
    protected BaseDomain createDomain() {
        return new ResumoDia();
    }
    
    @Override
    protected BaseDomain screenToDomain() throws SQLException {
    	ResumoDia resumoDia = ((ResumoDia) getDomain());
    	resumoDia.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	resumoDia.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	resumoDia.dtResumo = DateUtil.getDateValue(edDataFechamentoVendas.getValue());
    	resumoDia.dtUltimofechamento = DateUtil.getDateValue(edDataUltimoFechamentoVendas.getValue());
    	resumoDia.vlSaldoAnterior = ValueUtil.getDoubleValue(lvVlSaldoAnterior.getValue());
    	resumoDia.vlTotalPagamento = ValueUtil.getDoubleValue(lvVlTotalPagamentos.getValue());
		resumoDia.vlTotalVendido = ValueUtil.getDoubleValue(lvVlTotalVendas.getValue());
		resumoDia.vlTotalValorizacao = ValueUtil.getDoubleValue(lvVlTotalValorizacoes.getValue());
		resumoDia.vlTotalPromissoria = edVlTotalPromissorias.getValueDouble();
		resumoDia.vlSaldoFinal = ValueUtil.getDoubleValue(lvVlSaldoFinal.getValue());
		resumoDia.dtAlteracao = DateUtil.getCurrentDate();
		resumoDia.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
        return resumoDia;
    }
    
    @Override
    protected void domainToScreen(BaseDomain domain) {
		if (domain != null) {
	        ResumoDia resumoDia = (ResumoDia) domain;
	        edDataUltimoFechamentoVendas.setValue(DateUtil.formatDateDDMMYYYY(resumoDia.dtUltimofechamento));
			lvVlSaldoAnterior.setValue(resumoDia.vlSaldoAnterior);
			lvVlTotalPagamentos.setValue(resumoDia.vlTotalPagamento);
			lvVlTotalVendas.setValue(resumoDia.vlTotalVendido);
			lvVlTotalValorizacoes.setValue(resumoDia.vlTotalValorizacao);
			edVlTotalPromissorias.setValue(resumoDia.vlTotalPromissoria);
			lvVlSaldoFinal.setValue(ResumoDiaService.getInstance().retornaSaldoFechamentoVendas(edVlTotalPromissorias.getValueDouble(), resumoDia.vlSaldoAnterior, resumoDia.vlTotalPagamento, resumoDia.vlTotalVendido, resumoDia.vlTotalValorizacao));
		}
    }
    
    @Override
    protected void clearScreen() throws SQLException {
    }
    
    //-----------------------------------------------
    
    @Override
    protected void onFormStart() throws SQLException {
		UiUtil.add(this, containerCabecalho, LEFT, getTop(), FILL, UiUtil.getControlPreferredHeight() + (HEIGHT_GAP * 2));
		UiUtil.add(containerCabecalho, lbDataUltimoFechamentoVendas, LEFT + WIDTH_GAP, CENTER);
		UiUtil.add(containerCabecalho, edDataUltimoFechamentoVendas, AFTER + WIDTH_GAP, CENTER, PREFERRED);
		UiUtil.add(containerCabecalho, lbDataFechamentoVendas, AFTER + WIDTH_GAP, CENTER);
		UiUtil.add(containerCabecalho, edDataFechamentoVendas, AFTER + WIDTH_GAP, CENTER, PREFERRED);
		UiUtil.add(this, sContainer, LEFT, AFTER, FILL, FILL - barBottomContainer.getHeight());
		//--
		UiUtil.add(sContainer, lbVlSaldoAnterior, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlSaldoAnterior, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalPagamentos, CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(sContainer, lvVlTotalPagamentos, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalVendas, CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(sContainer, lvVlTotalVendas, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalValorizacoes, CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(sContainer, lvVlTotalValorizacoes, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalPromissorias, CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(sContainer, edVlTotalPromissorias, AFTER + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		UiUtil.add(sContainer, lbVlSaldoFinal, CENTEREDLABEL, AFTER + HEIGHT_GAP);
		UiUtil.add(sContainer, lvVlSaldoFinal, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbAvisoFechamentoJaEfetuado, CENTER, BOTTOM - HEIGHT_GAP_BIG);
    }

    @Override
    protected void onFormEvent(Event event) {
    	switch (event.type) {
	    	case ControlEvent.FOCUS_OUT: {
	    		if (event.target == edVlTotalPromissorias) {
	    			lvVlSaldoFinal.setValue(ResumoDiaService.getInstance().retornaSaldoFechamentoVendas(edVlTotalPromissorias.getValueDouble(), ValueUtil.getDoubleValue(lvVlSaldoAnterior.getValue()), ValueUtil.getDoubleValue(lvVlTotalPagamentos.getValue()), ValueUtil.getDoubleValue(lvVlTotalVendas.getValue()), ValueUtil.getDoubleValue(lvVlTotalValorizacoes.getValue())));
				}
	    		break;
	    	}
	    	case ValueChangeEvent.VALUE_CHANGE: {
	    		if (event.target == edDataFechamentoVendas && edDataFechamentoVendas.getValue() != null) {
	    			if (DateUtil.isBeforeOrEquals(DateUtil.getDateValue(edDataFechamentoVendas.getValue()), DateUtil.getCurrentDate()) && DateUtil.isAfterOrEquals(DateUtil.getDateValue(edDataFechamentoVendas.getValue()), DateUtil.getDateValue(edDataUltimoFechamentoVendas.getValue()))) {
	    				edVlTotalPromissorias.setEditable(true);
						calculateAndLoadFechamentoVendas();
						domainToScreen(resumoDia);
						erroComData();
						edVlTotalPromissorias.setEditable(true);
					} else if (DateUtil.isAfterOrEquals(DateUtil.getDateValue(edDataFechamentoVendas.getValue()), DateUtil.getDateValue(edDataUltimoFechamentoVendas.getValue()))) {
						UiUtil.showErrorMessage(Messages.FECHAMENTO_VENDAS_AVISO_DATAMAIOR);
						erroComData();
					} else {
						UiUtil.showErrorMessage(Messages.FECHAMENTO_VENDAS_AVISO_DATAMENOR);
						erroComData();
					}
				}
	    	}
    	}
    }
    
    public void erroComData(){
		if (resumoDia != null) {
			edDataFechamentoVendas.setValue(resumoDia.dtResumo);
		} else {
			edDataFechamentoVendas.setValue(null);
		}
    }
    
	private void calculateAndLoadFechamentoVendas() {
		UiUtil.showProcessingMessage();
		try {
			ResumoDia resumo = ResumoDiaService.getInstance().calculateAndGetFechamentoVendas(DateUtil.getDateValue(edDataUltimoFechamentoVendas.getValue()), DateUtil.getDateValue(edDataFechamentoVendas.getValue())); 
			resumoDia = (resumo != null) ? resumo : resumoDia;  
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}
	
	@Override
	protected void salvarClick() throws SQLException {
		if (DateUtil.isAfterOrEquals(DateUtil.getDateValue(edDataFechamentoVendas.getValue()), DateUtil.getCurrentDate())) {
			if (UiUtil.showConfirmYesNoMessage(Messages.FECHAMENTO_VENDAS_AVISO_FECHAMENTODIA)) {
				super.salvarClick();
	    		SaldoVendaRepService.getInstance().insert(getSaldoVendaRepTela());
	    	}
		} else {
			super.salvarClick();
    		SaldoVendaRepService.getInstance().insert(getSaldoVendaRepTela());
		}
	}
    
	public ResumoDia getResumoDiaTela() {
		resumoDia.dtResumo = DateUtil.getDateValue(edDataFechamentoVendas.getValue());
		resumoDia.vlTotalPromissoria = edVlTotalPromissorias.getValueDouble();
		resumoDia.vlSaldoFinal = ValueUtil.getDoubleValue(lvVlSaldoFinal.getValue());
		return resumoDia;
	}

	public SaldoVendaRep getSaldoVendaRepTela() {
		SaldoVendaRep saldoVendedor = new SaldoVendaRep();
		saldoVendedor.cdEmpresa = SessionLavenderePda.cdEmpresa;
		saldoVendedor.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		saldoVendedor.vlUltimoSaldo = ValueUtil.getDoubleValue(lvVlSaldoFinal.getValue());
		saldoVendedor.dtUltimoSaldo = edDataFechamentoVendas.getValue();
		saldoVendedor.dtAlteracao = DateUtil.getCurrentDate();
		saldoVendedor.hrAlteracao = TimeUtil.getCurrentTimeHHMMSS();
		return saldoVendedor;
	}
	
	public void getLasDtFechamento() {
		SaldoVendaRep saldoVendaFilter = new SaldoVendaRep();
		saldoVendaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		saldoVendaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		try {
			String dataRetorno = SaldoVendaRepService.getInstance().maxByExample(saldoVendaFilter, "DTULTIMOSALDO");
			if (ValueUtil.isEmpty(dataRetorno)) {
				lbAvisoFechamentoJaEfetuado = new Label(" ");
				edDataUltimoFechamentoVendas.setValue(DateUtil.getCurrentDate().toString());
				edDataFechamentoVendas.setValue(DateUtil.getCurrentDate());
				calculateAndLoadFechamentoVendas();
				domainToScreen(resumoDia);
				setVisibleOrEditable(true);
			} else {
				Date data = DateUtil.toDate(dataRetorno);
				DateUtil.addDay(data, 1);
				edDataUltimoFechamentoVendas.setValue(data.toString());
				if (DateUtil.isAfterOrEquals(DateUtil.toDate(dataRetorno), DateUtil.getCurrentDate())) {
					edDataFechamentoVendas.setValue(DateUtil.getCurrentDate());
					lbAvisoFechamentoJaEfetuado = new Label(Messages.FECHAMENTO_VENDAS_AVISO_FECHAMENTOJAEFETUADO);
					calculateAndLoadFechamentoVendas();
					domainToScreen(resumoDia);
					setVisibleOrEditable(false);
				} else {
					lbAvisoFechamentoJaEfetuado = new Label(" ");
					setVisibleOrEditable(true);
				}
			}
		} catch (Throwable e) {	}
	}
	
	public void setVisibleOrEditable(boolean visivel) {
		btSalvar.setVisible(visivel);
		btSalvar.setEnabled(visivel);
		edVlTotalPromissorias.setEditable(visivel);
		edDataFechamentoVendas.setEditable(visivel);
	}
	
}