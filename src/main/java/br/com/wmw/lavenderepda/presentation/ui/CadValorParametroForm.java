package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.ValorParametroService;
import br.com.wmw.lavenderepda.util.DominioParametroUtil;
import totalcross.ui.event.Event;

public class CadValorParametroForm extends BaseCrudCadForm {

    private LabelValue lvCdParametro;
    private EditMemo edDsParametro;
    private ButtonGroupBoolean bgVlParametro;
    private PushButtonGroupBase btGroupDominio;
    private ValorParametro parametro;
    private LabelName lbValorParametroDominio;
    private LabelName lbValorParametroAtivo;
    private LabelName lbCdParametro;
    private LabelName lbDsParametro;
    
    private String[] dominioParametro;

    public CadValorParametroForm() {
        super(Messages.CONFIGPARAMETRO_NOME_ENTIDADE);
        lvCdParametro = new LabelValue("@");
        edDsParametro = new EditMemo("@@@@@@@@@@", 7, 500);
        edDsParametro.setEnabled(false);
        bgVlParametro = new ButtonGroupBoolean();
        lbValorParametroDominio = new LabelName(Messages.CONFIGPARAMETRO_LABEL_VLPARAMETROPDA_DOMINIO);
        lbValorParametroAtivo = new LabelName(Messages.CONFIGPARAMETRO_LABEL_VLPARAMETROPDA_ATIVO);
        lbCdParametro = new LabelName(Messages.CONFIGPARAMETRO_LABEL_CDPARAMETRO);
        lbDsParametro = new LabelName(Messages.CONFIGPARAMETRO_LABEL_DSPARAMETRO);
    }

    public CadValorParametroForm(ValorParametro parametro) {
    	this();
    	this.parametro = parametro;
		dominioParametro = DominioParametroUtil.getDominioParametro(parametro.cdParametro);
		final byte buttonGroupType = DominioParametroUtil.isParametroDominioSubDominio(parametro.cdParametro) ? PushButtonGroupBase.TYPE_MULTICHOICE : PushButtonGroupBase.NORMAL;
		btGroupDominio = new PushButtonGroupBase(dominioParametro, true, 1, -1, 1, 1, true, buttonGroupType);
    }

    //-----------------------------------------------

    @Override
    protected String getEntityDescription() {
    	return title;
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return ValorParametroService.getInstance();
    }

    @Override
    protected BaseDomain createDomain() throws SQLException {
        return new ValorParametro();
    }

    @Override
    protected BaseDomain screenToDomain() throws SQLException {
    	ValorParametro configParametro = (ValorParametro) getDomain();
    	if (parametro != null && DominioParametroUtil.isParametroDominio(parametro.cdParametro)) {
    		int index = btGroupDominio.getSelectedIndex();
    		String valorParametro = dominioParametro[index];
    		configParametro.vlParametro = valorParametro;
    	} else {
    		configParametro.vlParametro = StringUtil.getStringValue(bgVlParametro.getValue());
    	}
        return configParametro;
    }

    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	parametro = (ValorParametro) domain;
        lvCdParametro.setValue(parametro.cdParametro);
        edDsParametro.setValue(parametro.dsParametro);
        if (DominioParametroUtil.isParametroDominio(parametro.cdParametro)) {
        	int index = DominioParametroUtil.getIndexDominioParametro(parametro.cdParametro, parametro.vlParametro);
        	btGroupDominio.setSelectedIndex(index);
        } else {
        	bgVlParametro.setValue(StringUtil.getStringValue(parametro.vlParametro));
        }
    }

    @Override
    protected void clearScreen() throws java.sql.SQLException {
        lvCdParametro.setText("");
        edDsParametro.setText("");
        bgVlParametro.setValue(ValueUtil.VALOR_NI);
    }

    @Override
    protected void visibleState() throws SQLException {
    	edDsParametro.setEditable(false);
    	bgVlParametro.setEnabled(isEnabled());
    	btExcluir.setVisible(false);
    	barBottomContainer.setVisible(false);
    }

    @Override
    protected void onFormStart() throws SQLException {
    	removeComponentsFromScreen();
    	addComponentsOnScreen();
    }
    
	private void removeComponentsFromScreen() {
		if (btGroupDominio != null) {
			remove(btGroupDominio);
		}
    	remove(bgVlParametro);
    	remove(lvCdParametro);
    	remove(edDsParametro);
    	remove(lbValorParametroDominio);
    	remove(lbValorParametroAtivo);
    	remove(lbCdParametro);
    	remove(lbDsParametro);
	}

	private void addComponentsOnScreen() {
		if (parametro != null && DominioParametroUtil.isParametroDominio(parametro.cdParametro) && btGroupDominio != null) {
			UiUtil.add(this, lbValorParametroDominio, btGroupDominio, getLeft(), getNextY());
    	} else {
			UiUtil.add(this, lbValorParametroAtivo, bgVlParametro, getLeft(), getNextY(), FILL, UiUtil.getControlPreferredHeight());
    	}
		UiUtil.add(this, lbCdParametro, lvCdParametro, getLeft(), getNextY());
		UiUtil.add(this, lbDsParametro, edDsParametro, getLeft(), getNextY(), FILL, FILL);
	}

    @Override
    protected void onFormEvent(Event event) throws SQLException {

    }

    @Override
    protected void salvarClick() throws SQLException {
    	super.salvarClick();
    	depoisSalvarParametro();
    }
    

    private void depoisSalvarParametro() {
    	if (ValorParametro.USATECLADOVIRTUAL == parametro.cdParametro) {
			BaseEdit.tecladoAwaysVisible = LavenderePdaConfig.usaTecladoVirtual;
			EditMemo.tecladoAwaysVisible = LavenderePdaConfig.usaTecladoVirtual;
    	}
    	if (ValorParametro.USATECLADOFIXOTELAITEMPEDIDO == parametro.cdParametro) {
    		CadItemPedidoForm.invalidateInstance();
    		ListItemPedidoForm.invalidateInstance();
    	}
    	if (ValorParametro.NIVELLOGSYNCBACKGROUND == parametro.cdParametro) {
    		AppConfig.instance.nivelLogSyncBackground = parametro.vlParametro;
    	}
    }

}
