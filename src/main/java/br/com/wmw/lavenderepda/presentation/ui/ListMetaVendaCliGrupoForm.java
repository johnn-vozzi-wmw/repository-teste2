package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCli;
import br.com.wmw.lavenderepda.business.domain.MetaVendaCliGrupo;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.MetaVendaCliGrupoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListMetaVendaCliGrupoForm extends LavendereCrudListForm {

	private LabelValue lbNmRazaoSocial;
	private BaseToolTip tipNmRazaoSocial;
	private LabelTotalizador lbVlRealizado;
	private LabelTotalizador lbVlMeta;
	private LabelName lbRazaoSocial;
	private MetaVendaCli metaVendaCli;
	private final String COL_CDGRUPOPRODUTO = "CDGRUPOPRODUTO";
	private SessionTotalizerContainer sessaoTotalizadores;
	private double vlTotalMeta;
	private double vlTotalRealizado;

	public ListMetaVendaCliGrupoForm(MetaVendaCli metaVendaCli) throws SQLException {
		super(Messages.META_VENDA_GRUPO_PRODUTO);
		this.metaVendaCli = metaVendaCli;
		lbRazaoSocial = new LabelName(Messages.META_VENDA_LABEL_RAZAO_SOCIAL);
		lbNmRazaoSocial = new LabelValue();
		lbVlMeta = new LabelTotalizador(Messages.META_VENDA_LABEL_TOTAL_METAS + " " + StringUtil.getStringValueToInterface(metaVendaCli.vlMeta));
		lbVlRealizado = new LabelTotalizador(Messages.META_VENDA_LABEL_TOTAL_REALIZADO + " " + StringUtil.getStringValueToInterface(metaVendaCli.vlRealizado) + "%");
		String dsCliente = ClienteService.getInstance().getDescriptionWithKey(metaVendaCli.cdEmpresa, metaVendaCli.cdRepresentante, metaVendaCli.cdCliente);
		lbNmRazaoSocial.setValue(dsCliente);
		tipNmRazaoSocial = new BaseToolTip(lbNmRazaoSocial, dsCliente);
		sessaoTotalizadores = new SessionTotalizerContainer();
		constructorListContainer();
	}
	
	private int getListSize() {
		return (!LavenderePdaConfig.apresentaInformacoesAdicionaisRelatorioMetaVendaCliente) ? 4 : 6;
	}

	private void constructorListContainer() {
		configListContainer(COL_CDGRUPOPRODUTO);
		listContainer = new GridListContainer(getListSize(), 2);
		listContainer.setColsSort(new String[][] { { Messages.META_VENDA_LABEL_CODIGO, COL_CDGRUPOPRODUTO }, { Messages.META_VENDA_LABEL_META, "VLMETA" }, { Messages.META_VENDA_LABEL_VALOR_REALIZADO, "VLREALIZADO" } });
		listContainer.setColPosition(3, RIGHT);
		if(LavenderePdaConfig.apresentaInformacoesAdicionaisRelatorioMetaVendaCliente) {
			listContainer.setColPosition(5, RIGHT);
		}
	}

	//@Override
	protected CrudService getCrudService() throws SQLException {
		return MetaVendaCliGrupoService.getInstance();
	}

	protected BaseDomain getDomainFilter() throws SQLException {
		MetaVendaCliGrupo domainFilter = new MetaVendaCliGrupo();
		return domainFilter;
	}

	//@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vector metaVendaCliGrupoList = MetaVendaCliGrupoService.getInstance().findAllMetaVendaCliGrupoByMetaVendaCli(metaVendaCli.cdEmpresa, metaVendaCli.cdRepresentante, metaVendaCli.cdCliente, metaVendaCli.cdMetaVendaCli);
		MetaVendaCliGrupo.sortAttr = domain.sortAtributte;
		MetaVendaCliGrupoService.getInstance().calculaMetaVendaCliGrupo(metaVendaCliGrupoList);
		if (!COL_CDGRUPOPRODUTO.equals(domain.sortAtributte)) {
			SortUtil.qsortInt(metaVendaCliGrupoList.items, 0, metaVendaCliGrupoList.size() - 1, true);
		} else {
			metaVendaCliGrupoList.qsort();
		}
		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
			metaVendaCliGrupoList.reverse();
		}
		return metaVendaCliGrupoList;
	}

	//@Override
	protected String[] getItem(Object domain) throws SQLException {
		MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) domain;
		String[] item = new String[getListSize()];
		item[0] = StringUtil.getStringValue(GrupoProduto1Service.getInstance().getDsGrupoProduto(metaVendaCliGrupo.cdGrupoProduto));
		item[1] = "";
		item[2] = Messages.META_VENDA_LABEL_META + " " + StringUtil.getStringValueToInterface(metaVendaCliGrupo.vlMeta);
		item[3] = Messages.META_VENDA_LABEL_VALOR_REALIZADO + " (" + StringUtil.getStringValueToInterface(metaVendaCliGrupo.getPctRealizadoMeta()) + "%)";
		if(LavenderePdaConfig.apresentaInformacoesAdicionaisRelatorioMetaVendaCliente) {
			item[4] = Messages.META_VENDA_LABEL_VL_ATINGIDO + " " + StringUtil.getStringValueToInterface(metaVendaCliGrupo.vlRealizado);
			item[5] = Messages.META_VENDA_LABEL_PCT_RESTANTE + " (" + StringUtil.getStringValueToInterface(metaVendaCliGrupo.getPctRestanteMeta()) + "%)";
		}
		return item;
	}
	
	protected void setPropertiesInRowList(Item currentItem, BaseDomain domain) throws SQLException {
		MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) domain;
		if(LavenderePdaConfig.grifaMetaNaoAtingida && metaVendaCliGrupo.isMetaNaoAtingida()) {
			listContainer.setContainerBackColor(currentItem, LavendereColorUtil.COR_FUNDO_META_NAO_ATINGIDA);
		}
	}
	
	@Override
	protected void afterList(Vector domainList) throws SQLException {
		super.afterList(domainList);
		calculaTotalizadores(domainList);
		lbVlMeta.setValue(Messages.META_VENDA_LABEL_TOTAL_METAS + " " + StringUtil.getStringValueToInterface(vlTotalMeta));
		lbVlRealizado.setValue(Messages.META_VENDA_LABEL_TOTAL_REALIZADO + " " + StringUtil.getStringValueToInterface(getTotalPctRealizado()) + "%");
		reposition();
	}

	private void calculaTotalizadores(Vector domainList) {
		int metaVendaCliGrupoSize = domainList.size();
		vlTotalMeta = 0;
		vlTotalRealizado = 0;
		for(int i=0; i<metaVendaCliGrupoSize; i++) {
			MetaVendaCliGrupo metaVendaCliGrupo = (MetaVendaCliGrupo) domainList.items[i];
			vlTotalMeta += metaVendaCliGrupo.vlMeta;
			vlTotalRealizado += metaVendaCliGrupo.vlRealizado;
		}
	}
	
	private double getTotalPctRealizado() {
		double pctRealizadoMeta = 0;
		if (vlTotalMeta > 0) {
			pctRealizadoMeta = (vlTotalRealizado * 100) / vlTotalMeta;
		}
		return pctRealizadoMeta;
	}

	//@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	//@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbRazaoSocial, lbNmRazaoSocial, getLeft(), getNextY() + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
		
		UiUtil.add(this, lbVlMeta, CENTER, BOTTOM - (barBottomContainer.getHeight() + HEIGHT_GAP), PREFERRED, PREFERRED);
		UiUtil.add(this, lbVlRealizado, RIGHT - UiUtil.getTotalizerGap(), BOTTOM - (barBottomContainer.getHeight() + HEIGHT_GAP), PREFERRED, PREFERRED);
    	sessaoTotalizadores.resizeHeight();
    	sessaoTotalizadores.resetSetPositions();
    	sessaoTotalizadores.setRect(LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
	}

	protected void onFormEvent(Event event) throws SQLException {
	}
}
