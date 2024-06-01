package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.service.BoletoConfigService;
import totalcross.sys.Convert;
import totalcross.sys.Vm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadPedidoBoletoWindow extends WmwWindow {
	
	private String ICONE_COPIA = "images/copy.png";
	
	private PedidoBoleto pedidoBoleto;
	private LabelValue lvNuSequenciaBoletoPedido;
    private LabelValue lvNuBoletoConfig;
    private LabelValue lvCdBarras;
    private LabelValue lvDsLinhasDigitavel;
    private LabelValue lvDtVencimento;
    private LabelValue lvNuAgenciaCodigoCedente;
    private LabelValue lvDtDocumento;
    private LabelValue lvNuDocumento;
    private LabelValue lvVlBoleto;
    private LabelValue lvNuNossoNumero;
    private EditMemo edDsLocalPagamento;
    private LabelValue lvNuCarteira;
    private LabelValue lvDsEspecieDocumento;
    private EditMemo edDsObsCedente;
    private ButtonGroupBoolean bgAceite;
    private ButtonAction btCopiarCdBarras;
	private ButtonAction btCopiarLinhaDigitavel;
	private ButtonAction btCopiarNuCedente;
	private ButtonAction btCopiarNuDocumento;
	private ButtonAction btCopiarLocalPagamento;
	private ButtonAction btCopiarDsObsCedente;


	public CadPedidoBoletoWindow(PedidoBoleto pedidoBoleto) {
		super(Messages.PEDIDOBOLETO_DETALHES);
		this.pedidoBoleto = pedidoBoleto;
		lvNuSequenciaBoletoPedido = new LabelValue(StringUtil.getStringValueToInterface(pedidoBoleto.nuSequenciaBoletoPedido));
		lvNuBoletoConfig = new LabelValue(BoletoConfigService.getInstance().getNuBanco(pedidoBoleto.cdBoletoConfig));
		lvDtVencimento = new LabelValue(StringUtil.getStringValue(pedidoBoleto.dtVencimento));
		lvDtDocumento = new LabelValue(StringUtil.getStringValue(pedidoBoleto.dtDocumento));
		lvVlBoleto = new LabelValue(StringUtil.getStringValueToInterface(pedidoBoleto.vlBoleto));
		lvNuNossoNumero = new LabelValue(pedidoBoleto.nuNossoNumero);
		lvNuCarteira = new LabelValue(pedidoBoleto.nuCarteira);
		lvDsEspecieDocumento = new LabelValue(pedidoBoleto.dsEspecieDocumento);
		recriaComponentesAlturaDinamico();
		edDsLocalPagamento = new EditMemo("@@@@@@@@@@", 5, 2000);
		edDsLocalPagamento.setEditable(false);
		edDsLocalPagamento.setEnabled(false);
		edDsLocalPagamento.setValue(pedidoBoleto.dsLocalPagamento);
		edDsObsCedente = new EditMemo("@@@@@@@@@@", 5, 2000);
		edDsObsCedente.setEditable(false);
		edDsObsCedente.setEnabled(false);
		edDsObsCedente.setValue(pedidoBoleto.dsObsCedente);
		bgAceite = new ButtonGroupBoolean();
		bgAceite.setEnabled(false);
		bgAceite.setValue(pedidoBoleto.flAceite);
		btCopiarCdBarras = new ButtonAction(UiUtil.getIconButtonAction(ICONE_COPIA));
		btCopiarLinhaDigitavel = new ButtonAction(UiUtil.getIconButtonAction(ICONE_COPIA));
		btCopiarNuCedente = new ButtonAction(UiUtil.getIconButtonAction(ICONE_COPIA));
		btCopiarNuDocumento = new ButtonAction(UiUtil.getIconButtonAction(ICONE_COPIA));
		btCopiarLocalPagamento = new ButtonAction(UiUtil.getIconButtonAction(ICONE_COPIA));
		btCopiarDsObsCedente = new ButtonAction(UiUtil.getIconButtonAction(ICONE_COPIA));
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		recriaComponentesAlturaDinamico();
		addComponents();
	}
	private void recriaComponentesAlturaDinamico() {
		int maxLvWidth = width - UiUtil.BASE_MARGIN_GAP * 2 - UiUtil.getControlPreferredHeight() - WIDTH_GAP_BIG;
		lvCdBarras = new LabelValue(Convert.insertLineBreak(maxLvWidth, fm, pedidoBoleto.cdBarras));
		lvDsLinhasDigitavel = new LabelValue(Convert.insertLineBreak(maxLvWidth, fm, pedidoBoleto.dsLinhasDigitavel));
		lvNuAgenciaCodigoCedente = new LabelValue(Convert.insertLineBreak(maxLvWidth, fm, pedidoBoleto.nuAgenciaCodigoCedente));
		lvNuDocumento = new LabelValue(Convert.insertLineBreak(maxLvWidth, fm, StringUtil.getStringValue(pedidoBoleto.nuDocumento)));
	}
	public void addComponents() {
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_NUSEQUENCIABOLETOPEDIDO), lvNuSequenciaBoletoPedido, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_NUBANCO), lvNuBoletoConfig, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_CDBARRAS), lvCdBarras, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(scBase, btCopiarCdBarras, AFTER + WIDTH_GAP, CENTER_OF);
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_DSLINHASDIGITAVEL), lvDsLinhasDigitavel, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(scBase, btCopiarLinhaDigitavel, AFTER + WIDTH_GAP, CENTER_OF);
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_DTVENCIMENTO), lvDtVencimento, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_NUAGENCIACODIGOCEDENTE), lvNuAgenciaCodigoCedente, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(scBase, btCopiarNuCedente, AFTER, CENTER_OF);
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_DTDOCUMENTO), lvDtDocumento, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_NUDOCUMENTO), lvNuDocumento, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(scBase, btCopiarNuDocumento, AFTER, CENTER_OF);
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_VLBOLETO), lvVlBoleto, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_NUNOSSONUMERO), lvNuNossoNumero, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_NUCARTEIRA), lvNuCarteira, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_DSESPECIEDOCUMENTO), lvDsEspecieDocumento, getLeft(), getNextY());
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_DSLOCALPAGAMENTO), getLeft(), getNextY());
		UiUtil.add(scBase, btCopiarLocalPagamento, getRight(), AFTER + WIDTH_GAP);
		UiUtil.add(scBase, edDsLocalPagamento, getLeft(), SAME, getWFill() - btCopiarLocalPagamento.getPreferredWidth() - WIDTH_GAP);
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_DSOBSCEDENTE), getLeft(), getNextY());
		UiUtil.add(scBase, btCopiarDsObsCedente, getRight(), AFTER + WIDTH_GAP);
		UiUtil.add(scBase, edDsObsCedente, getLeft(), SAME, getWFill() - btCopiarDsObsCedente.getPreferredWidth() - WIDTH_GAP);
		UiUtil.add(scBase, new LabelName(Messages.PEDIDOBOLETO_LABEL_FLACEITE), bgAceite, getLeft(), getNextY(), getWFill());
	}
	@Override
	public void screenResized() {
		super.screenResized();
		scBase.removeAll();
		scBase.scrollToOrigin();
		recriaComponentesAlturaDinamico();
		addComponents();
	}
	
	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btCopiarCdBarras) {
				Vm.clipboardCopy(lvCdBarras.getValue());
				UiUtil.showInfoMessage(Messages.CDBARRAS_COPIADO);
			}
			if (event.target == btCopiarLinhaDigitavel) {
				Vm.clipboardCopy(lvDsLinhasDigitavel.getValue());
				UiUtil.showInfoMessage(Messages.NULINHADIGITAVEL_COPIADO);
			}
			if (event.target == btCopiarNuCedente) {
				Vm.clipboardCopy(lvNuAgenciaCodigoCedente.getValue());
				UiUtil.showInfoMessage(Messages.NUCEDENTE_COPIADO);
			}
			if (event.target == btCopiarNuDocumento) {
				Vm.clipboardCopy(lvNuDocumento.getValue());
				UiUtil.showInfoMessage(Messages.NUDOCUMENTO_COPIADO);
			}
			if (event.target == btCopiarLocalPagamento) {
				Vm.clipboardCopy(edDsLocalPagamento.getValue());
				UiUtil.showInfoMessage(Messages.LOCALPAGAMENTO_COPIADO);
			}
			if (event.target == btCopiarDsObsCedente) {
				Vm.clipboardCopy(edDsObsCedente.getValue());
				UiUtil.showInfoMessage(Messages.OBSCEDENTE_COPIADO);
			}
			break;

		default:
			break;
		}
	}
	
}
