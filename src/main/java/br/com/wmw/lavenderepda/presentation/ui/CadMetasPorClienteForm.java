package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorCliente;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.MetasPorClienteService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import totalcross.ui.event.Event;

public class CadMetasPorClienteForm extends BaseCrudCadForm {

	private LabelValue lbRepresentante;
    private LabelValue lbDsPeriodo;
    private LabelValue lbCliente;
    private LabelValue lbVlMeta;
    private LabelValue lbVlRealizado;
    private LabelValue lbVlPctRealizado;
    private LabelValue lbQtUnidadeMeta;
    private LabelValue lbQtCaixaMeta;
    private LabelValue lbQtMixProdutoMeta;
    private BaseScrollContainer scroolContainer;

    public CadMetasPorClienteForm() {
        super(Messages.METASPORCLIENTE_NOME_ENTIDADE);
        scroolContainer = new BaseScrollContainer(false, true);
        lbRepresentante = new LabelValue();
        lbCliente = new LabelValue();
        lbDsPeriodo = new LabelValue();
        lbVlMeta = new LabelValue("@@@@@@@@");
        lbVlRealizado = new LabelValue("@@@@@@@@");
        lbVlPctRealizado = new LabelValue("@@@@@@@@");
        lbQtUnidadeMeta = new LabelValue("@@@@@@@@");
        lbQtCaixaMeta = new LabelValue("@@@@@@@@");
        lbQtMixProdutoMeta = new LabelValue("@@@@@@@@");
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
        return MetasPorClienteService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new MetasPorCliente();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        return null;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	MetasPorCliente metasPorCliente = (MetasPorCliente) domain;
        lbRepresentante.setValue(RepresentanteService.getInstance().getDescriptionWithId(metasPorCliente.cdRepresentante));
        lbDsPeriodo.setValue(metasPorCliente.dsPeriodo);
        lbCliente.setValue(ClienteService.getInstance().getNmRazaoSocial(SessionLavenderePda.cdEmpresa, metasPorCliente.cdRepresentante, metasPorCliente.cdCliente));
        lbVlMeta.setValue(metasPorCliente.vlMeta);
        lbVlRealizado.setValue(metasPorCliente.vlRealizado);
        lbVlPctRealizado.setValue(metasPorCliente.getPctRealizadoMeta());
        lbQtUnidadeMeta.setValue(metasPorCliente.qtUnidadeMeta);
        lbQtCaixaMeta.setValue(metasPorCliente.qtCaixaMeta);
        lbQtMixProdutoMeta.setValue(metasPorCliente.qtMixProdutoMeta);

    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        lbVlMeta.setText("");
        lbVlRealizado.setText("");
        lbQtUnidadeMeta.setText("");
        lbQtCaixaMeta.setText("");
        lbQtMixProdutoMeta.setText("");
        lbVlPctRealizado.setText("");
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, scroolContainer, LEFT, getTop(), FILL, FILL);
        UiUtil.add(scroolContainer, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), lbRepresentante, getLeft(), TOP + HEIGHT_GAP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lbCliente, getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PERIODO), lbDsPeriodo, getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_VLMETA), lbVlMeta, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_VLRESULTADO), lbVlRealizado, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PCTREALIZADO), lbVlPctRealizado, getLeft(), AFTER + HEIGHT_GAP);
        if (LavenderePdaConfig.mostraDetalhesAdicionaisNosRelatoriosDeMetas) {
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_UNIDADE), lbQtUnidadeMeta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_QTCAIXA), lbQtCaixaMeta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_MIXPRODUTO), lbQtMixProdutoMeta, getLeft(), AFTER + HEIGHT_GAP);
        }
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }
    
    protected void visibleState() throws SQLException {
    	barBottomContainer.setVisible(false);
    }

}
