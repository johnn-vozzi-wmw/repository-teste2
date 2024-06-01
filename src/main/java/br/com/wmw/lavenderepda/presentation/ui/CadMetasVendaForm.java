package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Meta;
import br.com.wmw.lavenderepda.business.service.MetaAcompanhamentoService;
import br.com.wmw.lavenderepda.business.service.MetaService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;

public class CadMetasVendaForm extends BaseCrudCadForm {

	private LabelValue lbRepresentante;
    private LabelValue lbDsPeriodo;
	private LabelValue edQtPedidos;
    private LabelValue edVlRealizadoVendas;
    private LabelValue edVlMetaVendas;
    private LabelValue edPctRealizado;
    private LabelValue edQtUnidademeta;
    private LabelValue edQtCaixameta;
    private LabelValue edQtMixclientemeta;
    private LabelValue edQtMixprodutometa;
    private BaseScrollContainer scroolContainer;

    public CadMetasVendaForm() {
        super(Messages.METAS_VENDA_TITULO_CADASTRO);
        scroolContainer = new BaseScrollContainer(false, true);
        scroolContainer.setBackColor(LavendereColorUtil.formsBackColor);
        scroolContainer.showScrollBarH = false;
        edQtPedidos = new LabelValue();
        edVlRealizadoVendas = new LabelValue();
        edVlMetaVendas = new LabelValue();
        edPctRealizado = new LabelValue();
        edQtUnidademeta = new LabelValue();
        edQtCaixameta = new LabelValue();
        edQtMixclientemeta = new LabelValue();
        edQtMixprodutometa = new LabelValue();
        setReadOnly();
    }

    //-----------------------------------------------

    //@Override
    protected String getEntityDescription() {
    	return Messages.METAS_META;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetaService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new Meta();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
    	return getDomain();
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        Meta meta = (Meta) domain;
        if (LavenderePdaConfig.usaSistemaModoOffLine) {
    		MetaAcompanhamentoService.getInstance().getMetaAcompanhamentoList(meta);
    		MetaService.getInstance().recalculaMeta(meta , DateUtil.getCurrentDate());
        }
        lbRepresentante.setValue(RepresentanteService.getInstance().getDescriptionWithId(meta.cdRepresentante));
        lbDsPeriodo.setValue(meta.dsPeriodo);
        edQtPedidos.setValue(meta.qtPedidos);
        edVlRealizadoVendas.setValue(meta.vlRealizadoVendas);
        edVlMetaVendas.setValue(meta.vlMetaVendas);
        edQtUnidademeta.setValue(meta.qtUnidadeMeta);
        edQtCaixameta.setValue(meta.qtCaixaMeta);
        edQtMixclientemeta.setValue(meta.qtMixClienteMeta);
        edQtMixprodutometa.setValue(meta.qtMixProdutoMeta);
        edPctRealizado.setValue(meta.getPctRealizadoMeta());
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        edQtPedidos.setText("");
        edVlRealizadoVendas.setText("");
        edVlMetaVendas.setText("");
        edPctRealizado.setText("");
        edQtUnidademeta.setText("");
        edQtCaixameta.setText("");
        edQtMixclientemeta.setText("");
        edQtMixprodutometa.setText("");
    }

    public void setEnabled(boolean enabled) { }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, scroolContainer, LEFT, getTop(), FILL, FILL);
        UiUtil.add(scroolContainer, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), lbRepresentante = new LabelValue(), getLeft(), TOP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PERIODO), lbDsPeriodo = new LabelValue(), getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_LABEL_PEDIDOS), edQtPedidos, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_VLMETA), edVlMetaVendas, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.PRODUTIVIDADE_LABEL_VLREALIZADOVENDAS), edVlRealizadoVendas, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PCTREALIZADO), edPctRealizado, getLeft(), AFTER + HEIGHT_GAP);
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_UNIDADE), edQtUnidademeta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_QTCAIXA), edQtCaixameta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_MIXCLIENTE), edQtMixclientemeta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_MIXPRODUTO), edQtMixprodutometa, getLeft(), AFTER + HEIGHT_GAP);
        }
    }

    protected void visibleState() throws SQLException {
    	btExcluir.setVisible(false);
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

}