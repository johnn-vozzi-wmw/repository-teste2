package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemNotaFiscal;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;
import br.com.wmw.lavenderepda.business.service.ItemNotaFiscalService;
import br.com.wmw.lavenderepda.business.service.NotaFiscalService;
import totalcross.ui.Container;
import totalcross.util.Vector;

public class CadNotaFiscalDynForm extends BaseLavendereCrudPersonCadForm {
	
	private NotaFiscal notaFiscal;
	private GridListContainer gridListContainerItensNotaFiscal;
	private EditText edCdSerie;
	private EditText edNuNotaFiscal;

	public CadNotaFiscalDynForm(NotaFiscal notaFiscal) throws SQLException {
		super(Messages.NOTA_FISCAL);
		this.notaFiscal = notaFiscal;
		gridListContainerItensNotaFiscal = new GridListContainer(4, 2);
		gridListContainerItensNotaFiscal.setColPosition(1, RIGHT);
		gridListContainerItensNotaFiscal.setColPosition(3, RIGHT);
		gridListContainerItensNotaFiscal.setBarTopSimple();
		edCdSerie = new EditText("@@@@@@@@@@", 20);
		edCdSerie.setEditable(false);
		edNuNotaFiscal = new EditText("@@@@@@@@@@", 20);
		edNuNotaFiscal.setEditable(false);
		
	}

	@Override
	protected String getDsTable() throws SQLException {
		return NotaFiscal.TABLE_NAME;
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return notaFiscal;
	}

	@Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return NotaFiscalService.getInstance();
	}

	
	@Override
	public void initCabecalhoRodape() throws SQLException {
		lbTitle = new LabelValue();
	}
	
	@Override
	protected void addTabsFixas(Vector tableTitles) throws SQLException {
		if (!LavenderePdaConfig.ocultaItens) {
			tableTitles.addElement(Messages.ITENS_NOTA_FISCAL);
		}
	}
	
	public void carregaCampos() throws SQLException {
		edit(notaFiscal);
	}
	
	private void carregaItensNotaFiscal() throws SQLException {
		Vector itemNotaFiscalList = ItemNotaFiscalService.getInstance().findItemNotaFiscalList(notaFiscal.cdEmpresa, notaFiscal.cdSerie, notaFiscal.nuNotaFiscal);
		if (ValueUtil.isEmpty(itemNotaFiscalList)) return;
		
		gridListContainerItensNotaFiscal.removeAllContainers();
		Container[] all = new Container[itemNotaFiscalList.size()];
		BaseListContainer.Item c;
		int size = itemNotaFiscalList.size();
		for (int i = 0; i < size; i++) {
			ItemNotaFiscal itemNotaFiscal = (ItemNotaFiscal) itemNotaFiscalList.elementAt(i);
			all[i] = c = new BaseListContainer.Item(gridListContainerItensNotaFiscal.getLayout());
			c.id = notaFiscal.getRowKey();
		    c.setItens(getItemNotaFiscal(itemNotaFiscal));
		}
		gridListContainerItensNotaFiscal.addContainers(all);
		
	}

	private String[] getItemNotaFiscal(ItemNotaFiscal itemNotaFiscal) {
		String[] item = {
				StringUtil.getStringValue(itemNotaFiscal.cdProduto) + " - " + StringUtil.getStringValue(itemNotaFiscal.dsProduto),
				"",
				StringUtil.getStringValue(itemNotaFiscal.getQtItem()) + " " + StringUtil.getStringValue(itemNotaFiscal.dsUnidade) + " x " + StringUtil.getStringValueToInterface(itemNotaFiscal.vlUnitario),
				StringUtil.getStringValueToInterface(itemNotaFiscal.vlTotalItem)};
        return item;
	}
	
	@Override
	protected void addComponentesFixosInicio() throws SQLException {
		Container containerNotaFiscal = hashTabs.size() > 1 ? tabDinamica.getContainer(0) : getContainerPrincipal();
		Container containerItemNotaFiscal = hashTabs.size() > 1 ? tabDinamica.getContainer(hashTabs.size() - 1) : getContainerPrincipal();
		UiUtil.add(containerNotaFiscal, new LabelName(Messages.IMPRESSAONFE_SERIE), edCdSerie, getLeft(), getNextY());
		UiUtil.add(containerNotaFiscal, new LabelName(Messages.NOTA_FISCAL), edNuNotaFiscal, getLeft(), getNextY());
		if (!LavenderePdaConfig.ocultaItens) {
			UiUtil.add(containerItemNotaFiscal, gridListContainerItensNotaFiscal, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		}
	}
	
	@Override
	protected void addComponentesFixosFim() throws SQLException {
	}
	
	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		super.domainToScreen(domain);
		edCdSerie.setText(notaFiscal.cdSerie);
		edNuNotaFiscal.setText(notaFiscal.nuNotaFiscal);
		carregaItensNotaFiscal();
	}

}
