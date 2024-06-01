package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.business.service.RequisicaoServService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RequisicaoServTipoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusRequisicaoServComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListRequisicaoServForm extends LavendereCrudListForm {
	RequisicaoServTipoComboBox cbRequisicaoServTipoCombo;
	StatusRequisicaoServComboBox cbStatusRequisicaoCombo;
	public EditDate edDateInicial;
	public EditDate edDateFinal;
	private ButtonAction btNovaRequisicao;

	public ListRequisicaoServForm() throws SQLException {
		super(Messages.REQUISICAO_SERV_NOME_ENTIDADE);
		setBaseCrudCadForm(new CadRequisicaoServForm(true));
		cbRequisicaoServTipoCombo = new RequisicaoServTipoComboBox(BaseComboBox.DefaultItemType_ALL);
		cbStatusRequisicaoCombo = new StatusRequisicaoServComboBox();
        edDateInicial = new EditDate();
        edDateFinal = new EditDate();
		singleClickOn = true;
		constructorListContainer();
		btNovaRequisicao = new ButtonAction(Messages.BOTAO_NOVA_REQUISICAO, "images/add.png", true);
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		RequisicaoServ requisicaoServ = new RequisicaoServ();
		requisicaoServ.tipoServ.cdRequisicaoServTipo = cbRequisicaoServTipoCombo.getValue();
		requisicaoServ.motivoServ.flStatusRequisicao = cbStatusRequisicaoCombo.getValue();
		requisicaoServ.dtInicioFilter = edDateInicial.getValue();
		requisicaoServ.dtFimFilter = edDateFinal.getValue();
		return requisicaoServ;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return RequisicaoServService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERV_COMBO_TIPO_SERVICO), cbRequisicaoServTipoCombo, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERV_COMBO_STATUS), cbStatusRequisicaoCombo, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.REQUISICAOSERV_LABEL_PERIODO), getLeft(), getNextY());
    	UiUtil.add(this, edDateInicial, getLeft(), getNextY());
    	UiUtil.add(this, edDateFinal, AFTER + WIDTH_GAP, SAME);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
    	UiUtil.add(barBottomContainer, btNovaRequisicao, 5);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED:
				if (event.target == cbRequisicaoServTipoCombo || event.target == cbStatusRequisicaoCombo) {
					list();
				} else if (event.target == btNovaRequisicao) {
					btNovaRequisicaoServClick();
				}
				break;
			case ValueChangeEvent.VALUE_CHANGE: 
				if ((event.target == edDateFinal  || event.target == edDateInicial)) {
					if ((!ValueUtil.isEmpty(edDateInicial.getValue()) && !ValueUtil.isEmpty(edDateFinal.getValue())) && edDateFinal.getValue().isBefore(edDateInicial.getValue())) {
						EditDate dtSelected = (EditDate)event.target;
						dtSelected.setValue(dtSelected == edDateInicial ? edDateFinal.getValue() : edDateInicial.getValue());
						throw new ValidationException(Messages.ERRO_CAMPO_DATA_REQUISICAOSERV);
					}
					list();
				}
			break;
		}
	}
	
	private void btNovaRequisicaoServClick() throws SQLException {
		CadRequisicaoServForm cadRequisicaoServFrom = new CadRequisicaoServForm();
		cadRequisicaoServFrom.add();
		show(cadRequisicaoServFrom);
	}

	public void initUI() {
		try {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			msg.unpop();
			super.initUI();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(requisicaoServ.cdRequisicaoServ));
		item.addElement(StringUtil.getStringValue(requisicaoServ.motivoServ.getDsStatusRequisicao()));
		item.addElement(StringUtil.getStringValue(requisicaoServ.tipoServ));
		item.addElement(StringUtil.getStringValue(requisicaoServ.dtRequisicaoServ) + " - " + StringUtil.getStringValue(requisicaoServ.hrRequisicaoServ).substring(0, 5));
		item.addElement(StringUtil.getStringValue(requisicaoServ.cliente));
		return (String[]) item.toObjectArray();
	}
	
    protected void constructorListContainer() {
    	configListContainer("DTREQUISICAOSERV");
    	listContainer = new GridListContainer(5, 2);
		String[][] matriz = new String[3][2];
		matriz[0][0] = Messages.REQUISICAOSERV_STATUS;
		matriz[0][1] = "FLSTATUSATUAL";
		matriz[1][0] = Messages.REQUISICAOSERV_DATA;
		matriz[1][1] = "DTREQUISICAOSERV";
		matriz[2][0] = Messages.REQUISICAOSERV_TIPO;
		matriz[2][1] = "DSREQUISICAOSERVTIPO";
		listContainer.setColsSort(matriz);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
    }
    
    @Override
    public void onFormExibition() throws SQLException {
    	super.onFormExibition();
    	list();
    }
    
    @Override
    public void detalhesClick() throws SQLException {
    	BaseDomain domain = getSelectedDomain();
    	CadRequisicaoServForm cadRequisicao = new CadRequisicaoServForm(true);
    	cadRequisicao.edit(domain);
    	show(cadRequisicao);
    }
   
}
