package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorForn;
import br.com.wmw.lavenderepda.business.domain.MetasPorProduto;
import br.com.wmw.lavenderepda.business.service.MetasPorFornService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import totalcross.ui.event.Event;

public class CadMetasPorProdutoForm extends BaseCrudCadForm {

	private LabelValue lbRepresentante;
    private LabelValue lbDsPeriodo;
    private LabelValue lbProduto;
    private LabelValue lbVlMeta;
    private LabelValue lbVlRealizado;
    private LabelValue lbVlPctRealizado;
    private LabelValue lbQtUnidademeta;
    private LabelValue lbQtUnidadeRealizado;
    private LabelValue lbQtCaixaMeta;
    private LabelValue lbQtCaixaRealizado;
    private LabelValue lbQtMixclientemeta;
    private LabelValue lbQtMixclienteRealizado;
    private BaseScrollContainer scroolContainer;

    public CadMetasPorProdutoForm() {
        super(Messages.METASPORPRODUTO_TITULO_CADASTRO);
        scroolContainer = new BaseScrollContainer(false, true);
        lbProduto = new LabelValue("@@@@@@@@@@@@");
        lbVlMeta = new LabelValue("@@@@@@@@@@@@");
        lbVlRealizado = new LabelValue("@@@@@@@@@@");
        lbVlPctRealizado = new LabelValue("@@@@@@@@@@@@@");
        lbQtUnidademeta = new LabelValue("@@@@@@@@@@@@@");
        lbQtUnidadeRealizado = new LabelValue("@@@@@@@@@@@@@");
        lbQtCaixaMeta = new LabelValue("@@@@@@@@@@@@@");
        lbQtCaixaRealizado = new LabelValue("@@@@@@@@@@@@@");
        lbQtMixclientemeta = new LabelValue("@@@@@@@@@@@@@");
        lbQtMixclienteRealizado = new LabelValue("@@@@@@@@@@@@@");
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
    	MetasPorProduto metasporfornecedor = (MetasPorProduto) domain;
        lbRepresentante.setValue(RepresentanteService.getInstance().getDescriptionWithId(metasporfornecedor.cdRepresentante));
        lbDsPeriodo.setValue(" " + metasporfornecedor.dsPeriodo);
        lbProduto.setValue(" " + ProdutoService.getInstance().getDescriptionWithId(metasporfornecedor.cdProduto));
        lbVlMeta.setValue(metasporfornecedor.vlMeta);
        lbVlRealizado.setValue(metasporfornecedor.vlRealizado);
        lbQtUnidademeta.setValue(metasporfornecedor.qtUnidadeMeta);
        lbQtUnidadeRealizado.setValue(metasporfornecedor.qtUnidadeRealizado);
        lbQtCaixaMeta.setValue(metasporfornecedor.qtCaixaMeta);
        lbQtCaixaRealizado.setValue(metasporfornecedor.qtCaixaRealizado);
        lbQtMixclientemeta.setValue(metasporfornecedor.qtMixClienteMeta);
        lbQtMixclienteRealizado.setValue(metasporfornecedor.qtMixClienteRealizado);
        lbVlPctRealizado.setValue(metasporfornecedor.getPctRealizadoMeta());
    }

    protected void visibleState() throws SQLException {
    	btExcluir.setVisible(false);
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        lbVlMeta.setText("");
        lbVlRealizado.setText("");
        lbQtUnidademeta.setText("");
        lbQtUnidadeRealizado.setText("");
        lbQtCaixaMeta.setText("");
        lbQtCaixaRealizado.setText("");
        lbQtMixclientemeta.setText("");
        lbQtMixclienteRealizado.setText("");
        lbVlPctRealizado.setText("");
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, scroolContainer, LEFT, getTop(), FILL, FILL);
        UiUtil.add(scroolContainer, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), lbRepresentante = new LabelValue(), getLeft(), TOP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.PRODUTO_NOME_ENTIDADE), lbProduto, getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PERIODO), lbDsPeriodo = new LabelValue(), getLeft(), AFTER + HEIGHT_GAP, PREFERRED);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_VLMETA), lbVlMeta, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_VLRESULTADO), lbVlRealizado, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(scroolContainer, new LabelName(Messages.METAS_PCTREALIZADO), lbVlPctRealizado, getLeft(), AFTER + HEIGHT_GAP);
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorProdutoLigado()) {
        	String[] infos = LavenderePdaConfig.getMostraDetalhesAdicionaisRelMetasPorProduto();
        	for (int i = 0; i < infos.length; i++) {
        		switch (i) {
					case 0:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(scroolContainer, new LabelName(infos[i] + ":"), lbQtUnidademeta, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 1:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(scroolContainer, new LabelName(infos[i] + ":"), lbQtUnidadeRealizado, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 2:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(scroolContainer, new LabelName(infos[i] + ":"), lbQtCaixaMeta, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 3:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(scroolContainer, new LabelName(infos[i] + ":"), lbQtCaixaRealizado, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 4:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(scroolContainer, new LabelName(infos[i] + ":"), lbQtMixclientemeta, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 5:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(scroolContainer, new LabelName(infos[i] + ":"), lbQtMixclienteRealizado, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
        		}
        	}
        }
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

}