package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorForn;
import br.com.wmw.lavenderepda.business.service.FornecedorService;
import br.com.wmw.lavenderepda.business.service.MetasPorFornService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import totalcross.ui.event.Event;

public class CadMetasPorFornForm extends BaseCrudCadForm {

	private LabelValue lbRepresentante;
    private LabelValue lbDsPeriodo;
    private LabelValue lbFornecedor;
    private LabelValue lbVlMeta;
    private LabelValue lbVlRealizado;
    private LabelValue lbVlPctRealizado;
    private LabelValue lbQtUnidademeta;
    private LabelValue lbQtCaixameta;
    private LabelValue lbQtMixclientemeta;
    private LabelValue lbQtMixprodutometa;
    private BaseScrollContainer scroolContainer;

    public CadMetasPorFornForm() {
        super(Messages.METASPORFORNECEDOR_NOME_ENTIDADE);
        scroolContainer = new BaseScrollContainer(false, true);
        lbVlMeta = new LabelValue("@@@@@@@@@@@@@@");
        lbVlRealizado = new LabelValue("@@@@@@@@@@@@@@");
        lbVlPctRealizado = new LabelValue("@@@@@@@@@@@@@@");
        lbQtUnidademeta = new LabelValue("@@@@@@@@@@@@@@");
        lbQtCaixameta = new LabelValue("@@@@@@@@@@@@@@");
        lbQtMixclientemeta = new LabelValue("@@@@@@@@@@@@@@");
        lbQtMixprodutometa = new LabelValue("@@@@@@@@@@@@@@");
        //--
        setReadOnly();
    }

    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return Messages.METAS_META;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return MetasPorFornService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new MetasPorForn();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        return null;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	MetasPorForn metasporfornecedor = (MetasPorForn) domain;
        lbRepresentante.setValue(RepresentanteService.getInstance().getDescriptionWithId(metasporfornecedor.cdRepresentante));
        lbDsPeriodo.setValue(metasporfornecedor.dsPeriodo);
        lbFornecedor.setValue(FornecedorService.getInstance().getDsFornecedor(metasporfornecedor.cdEmpresa, StringUtil.getStringValue(metasporfornecedor.cdFornecedor)));
        lbVlMeta.setValue(metasporfornecedor.vlMeta);
        lbVlRealizado.setValue(metasporfornecedor.vlRealizado);
        lbVlPctRealizado.setValue(metasporfornecedor.getPctRealizadoMeta());
        lbQtUnidademeta.setValue(metasporfornecedor.qtUnidadeMeta);
        lbQtCaixameta.setValue(metasporfornecedor.qtCaixaMeta);
        lbQtMixclientemeta.setValue(metasporfornecedor.qtMixClienteMeta);
        lbQtMixprodutometa.setValue(metasporfornecedor.qtMixProdutoMeta);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        lbVlMeta.setText("");
        lbVlRealizado.setText("");
        lbQtUnidademeta.setText("");
        lbQtCaixameta.setText("");
        lbQtMixclientemeta.setText("");
        lbQtMixprodutometa.setText("");
        lbVlPctRealizado.setText("");
    }

    protected void visibleState() throws SQLException {
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, scroolContainer, LEFT, getTop(), FILL, FILL);
        UiUtil.add(scroolContainer, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), lbRepresentante = new LabelValue(), getLeft(), TOP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.FORNECEDOR_NOME_ENTIDADE), lbFornecedor = new LabelValue(), getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PERIODO), lbDsPeriodo = new LabelValue(), getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
        //--
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_VLMETA), lbVlMeta, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_VLRESULTADO), lbVlRealizado, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PCTREALIZADO), lbVlPctRealizado, getLeft(), AFTER + HEIGHT_GAP);
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_UNIDADE), lbQtUnidademeta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_QTCAIXA), lbQtCaixameta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_MIXCLIENTE), lbQtMixclientemeta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_MIXPRODUTO), lbQtMixprodutometa, getLeft(), AFTER + HEIGHT_GAP);
        }
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

}