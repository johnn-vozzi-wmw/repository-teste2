package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelSubtitle;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetasPorForn;
import br.com.wmw.lavenderepda.business.domain.MetasPorGrupoProduto;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto2Service;
import br.com.wmw.lavenderepda.business.service.GrupoProduto3Service;
import br.com.wmw.lavenderepda.business.service.MetasPorFornService;
import br.com.wmw.lavenderepda.business.service.MetasPorGrupoProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.ScrollContainer;
import totalcross.ui.event.Event;

public class CadMetasPorGrupoProduto1Form extends BaseCrudCadForm {

	ScrollContainer containerPrincipal;

	private LabelValue lbRepresentante;
    private LabelValue lbDsPeriodo;
    private LabelValue lbDsGrupoProduto1;
    private LabelValue lbDsGrupoProduto2;
    private LabelValue lbDsGrupoProduto3;
    private LabelValue lbDsProduto;
    private LabelValue lbVlMeta;
    private LabelValue lbVlRealizado;
    private LabelValue lbVlPctRealizado;
    private LabelValue lbQtUnidademeta;
    private LabelValue lbQtUnidadeRealizado;
    private LabelValue lbVlPctRealizadoUnidade;
    private LabelValue lbQtCaixaMeta;
    private LabelValue lbQtCaixaRealizado;
    private LabelValue lbVlPctRealizadoCaixa;
    private LabelValue lbQtMixclientemeta;
    private LabelValue lbQtMixclienteRealizado;
    private LabelValue lbVlPctRealizadoMixCliente;
    private LabelValue lbDtInicial;
    private LabelValue lbDtFinal;

	private LabelName lbRepresentanteResumido;
	private LabelName lbGrupoProduto1;
	private LabelName lbGrupoProduto2;
	private LabelName lbGrupoProduto3;
	private LabelName lbProduto;
	private LabelName lbMetasPeriodo;

	private int nuNivelRel;
	private boolean fromListProduto;

    public CadMetasPorGrupoProduto1Form(int nuNivelRel, boolean fromListProduto) {
        super(Messages.MENU_OPCAO_METAS_GRUPO_PRODUTO);
        this.nuNivelRel = nuNivelRel;
        this.fromListProduto = fromListProduto;
        lbDsGrupoProduto1 = new LabelValue("@@@@@@@@@@@@");
        lbDsGrupoProduto2 = new LabelValue("@@@@@@@@@@@@");
        lbDsGrupoProduto3 = new LabelValue("@@@@@@@@@@@@");
        lbDsProduto = new LabelValue("@@@@@@@@@@@@");
        lbVlMeta = new LabelValue("@@@@@@@@@@@@");
        lbVlRealizado = new LabelValue("@@@@@@@@@@");
        lbVlPctRealizado = new LabelValue("@@@@@@@@@@@@@");
        lbQtUnidademeta = new LabelValue("@@@@@@@@@@@@@");
        lbQtUnidadeRealizado = new LabelValue("@@@@@@@@@@@@@");
        lbVlPctRealizadoUnidade = new LabelValue("@@@@@@@@@@@@@");
        lbQtCaixaMeta = new LabelValue("@@@@@@@@@@@@@");
        lbQtCaixaRealizado = new LabelValue("@@@@@@@@@@@@@");
        lbVlPctRealizadoCaixa = new LabelValue("@@@@@@@@@@@@@");
        lbQtMixclientemeta = new LabelValue("@@@@@@@@@@@@@");
        lbQtMixclienteRealizado = new LabelValue("@@@@@@@@@@@@@");
        lbVlPctRealizadoMixCliente = new LabelValue("@@@@@@@@@@@@@");
        lbDtInicial = new LabelValue("@@@@@@@@@@@@@");
        lbDtFinal = new LabelValue("@@@@@@@@@@@@@");
        //--
        lbRepresentante = new LabelValue("8468468489");
        lbDsPeriodo = new LabelValue();
        lbRepresentanteResumido = new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
        lbGrupoProduto1 = new LabelName(GrupoProduto1Service.getInstance().getLabelGrupoProduto1());
        lbGrupoProduto2 = new LabelName(GrupoProduto2Service.getInstance().getLabelGrupoProduto2());
        lbGrupoProduto3 = new LabelName(GrupoProduto3Service.getInstance().getLabelGrupoProduto3());
        lbProduto = new LabelName(Messages.PRODUTO_NOME_ENTIDADE);
        lbMetasPeriodo = new LabelName(Messages.METAS_META);
        //--
        containerPrincipal = new BaseScrollContainer(false,true);
        containerPrincipal.setBackColor(LavendereColorUtil.formsBackColor);
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
    	MetasPorGrupoProduto metasPorGrupoProduto = (MetasPorGrupoProduto) domain;
    	if (ValueUtil.isEmpty(metasPorGrupoProduto.cdRepresentante)) {
    		lbRepresentante.setValue(FrameworkMessages.OPCAO_TODOS);
    	} else {
    		lbRepresentante.setValue(RepresentanteService.getInstance().getDescriptionWithId(metasPorGrupoProduto.cdRepresentante));
    	}
        lbDsPeriodo.setValue(metasPorGrupoProduto.dsPeriodo);
        lbDsGrupoProduto1.setValue(LavenderePdaConfig.usaRelMetasGrupoProdIndepDoCadDeGrupoProd ? MetasPorGrupoProdutoService.getInstance().getDescricaoCodigoMeta(metasPorGrupoProduto.dsGrupoProduto1, metasPorGrupoProduto.cdGrupoProduto1) : GrupoProduto1Service.getInstance().getDsGrupoProduto(metasPorGrupoProduto.cdGrupoProduto1));
        if (nuNivelRel > 1) {
        	lbDsGrupoProduto2.setValue(GrupoProduto2Service.getInstance().getDsGrupoProduto(metasPorGrupoProduto.cdGrupoProduto1, metasPorGrupoProduto.cdGrupoProduto2));
        }
        if (nuNivelRel > 2) {
        	lbDsGrupoProduto3.setValue(GrupoProduto3Service.getInstance().getDsGrupoProduto(metasPorGrupoProduto.cdGrupoProduto1, metasPorGrupoProduto.cdGrupoProduto2, metasPorGrupoProduto.cdGrupoProduto3));
        }
        if (nuNivelRel > 3 || fromListProduto) {
        	lbDsProduto.setValue(ProdutoService.getInstance().getDescriptionWithId(metasPorGrupoProduto.cdProduto));
        }
        lbVlMeta.setValue(metasPorGrupoProduto.vlMeta);
        lbVlRealizado.setValue(metasPorGrupoProduto.vlRealizado);
        lbQtUnidademeta.setValue(metasPorGrupoProduto.qtUnidadeMeta);
        lbQtUnidadeRealizado.setValue(metasPorGrupoProduto.qtUnidadeRealizado);
        lbQtCaixaMeta.setValue(metasPorGrupoProduto.qtCaixaMeta);
        lbQtCaixaRealizado.setValue(metasPorGrupoProduto.qtCaixaRealizado);
        lbQtMixclientemeta.setValue(metasPorGrupoProduto.qtMixClienteMeta);
        lbQtMixclienteRealizado.setValue(metasPorGrupoProduto.qtMixClienteRealizado);
        lbVlPctRealizado.setValue(metasPorGrupoProduto.getPctRealizadoMeta());
        lbVlPctRealizadoUnidade.setValue(metasPorGrupoProduto.getPctRealizadoQtUnidade());
        lbVlPctRealizadoCaixa.setValue(metasPorGrupoProduto.getPctRealizadoQtCaixa());
        lbVlPctRealizadoMixCliente.setValue(metasPorGrupoProduto.getPctRealizadoQtMixCliente());
        lbDtInicial.setValue(metasPorGrupoProduto.dtInicial);
        lbDtFinal.setValue(metasPorGrupoProduto.dtFinal);
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
        lbDtInicial.setText("");
        lbDtFinal.setText("");
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, containerPrincipal, LEFT, getTop(), FILL, FILL);
    	SessionContainer containerLabelFunc = new SessionContainer();
    	UiUtil.add(containerPrincipal, containerLabelFunc, LEFT, TOP, FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
		UiUtil.add(containerLabelFunc, new LabelSubtitle("Dados"), LEFT + 20, CENTER, PREFERRED, PREFERRED);
        UiUtil.add(containerPrincipal, lbRepresentanteResumido, lbRepresentante, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(containerPrincipal, lbMetasPeriodo, lbDsPeriodo, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(containerPrincipal, lbGrupoProduto1, lbDsGrupoProduto1, getLeft(), AFTER + HEIGHT_GAP);
        if (nuNivelRel > 1) {
        	UiUtil.add(containerPrincipal, lbGrupoProduto2, lbDsGrupoProduto2, getLeft(), AFTER + HEIGHT_GAP);
        }
        if (nuNivelRel > 2) {
        	UiUtil.add(containerPrincipal, lbGrupoProduto3, lbDsGrupoProduto3, getLeft(), AFTER + HEIGHT_GAP);
        }
        if (nuNivelRel > 3 || fromListProduto) {
        	UiUtil.add(containerPrincipal, lbProduto, lbDsProduto, getLeft(), AFTER + HEIGHT_GAP);
        }
    	SessionContainer containerLabelValores = new SessionContainer();
    	UiUtil.add(containerPrincipal, containerLabelValores, LEFT, AFTER + HEIGHT_GAP * 2, FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
		UiUtil.add(containerLabelValores, new LabelSubtitle("Meta/Realizado"), LEFT + 20, CENTER, PREFERRED, PREFERRED);
        if (LavenderePdaConfig.isMostraDetalhesAdicionaisRelMetasPorGrupoProduto1Ligado()) {
        	String[] infos = LavenderePdaConfig.getMostraDetalhesAdicionaisRelMetasPorGrupoProduto1();
        	for (int i = 0; i < infos.length; i++) {
        		switch (i) {
	        		case 0:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbVlMeta, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
	        		case 1:
	        			if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
	        				UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbVlRealizado, getLeft(), AFTER + HEIGHT_GAP);
	        			}
	        			break;
	        		case 2:
	        			if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
	        				UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbVlPctRealizado, getLeft(), AFTER + HEIGHT_GAP);
	        			}
	        			break;
					case 3:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbQtUnidademeta, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 4:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbQtUnidadeRealizado, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
	        		case 5:
	        			if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
	        				UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbVlPctRealizadoUnidade, getLeft(), AFTER + HEIGHT_GAP);
	        			}
	        			break;
					case 6:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbQtCaixaMeta, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 7:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbQtCaixaRealizado, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
	        		case 8:
	        			if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
	        				UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbVlPctRealizadoCaixa, getLeft(), AFTER + HEIGHT_GAP);
	        			}
	        			break;
					case 9:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbQtMixclientemeta, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 10:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
					        UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbQtMixclienteRealizado, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 11:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
							UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbVlPctRealizadoMixCliente, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 12:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
							UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbDtInicial, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;
					case 13:
						if (!ValueUtil.isEmpty(infos[i]) && !".".equals(infos[i])) {
							UiUtil.add(containerPrincipal, new LabelName(infos[i]), lbDtFinal, getLeft(), AFTER + HEIGHT_GAP);
						}
						break;						
        		}
        	}
        } else {
	        UiUtil.add(containerPrincipal, new LabelName(Messages.METAS_VLMETA), lbVlMeta, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(containerPrincipal, new LabelName(Messages.METAS_VLRESULTADO), lbVlRealizado, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(containerPrincipal, new LabelName(Messages.METAS_PCTREALIZADO), lbVlPctRealizado, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(containerPrincipal, new LabelName(Messages.METAS_DTINICIAL), lbDtInicial, getLeft(), AFTER + HEIGHT_GAP);
	        UiUtil.add(containerPrincipal, new LabelName(Messages.METAS_DTFINAL), lbDtFinal, getLeft(), AFTER + HEIGHT_GAP);
        }
        reposition();
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

}
