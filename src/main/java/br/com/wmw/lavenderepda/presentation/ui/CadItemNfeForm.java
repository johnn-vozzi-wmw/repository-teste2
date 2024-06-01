package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.service.ItemNfeReferenciaService;
import br.com.wmw.lavenderepda.business.service.ItemNfeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import totalcross.ui.event.Event;

public class CadItemNfeForm extends BaseCrudCadForm {
	
    private LabelValue lvProduto;
    private LabelValue lvCdClassificFiscal;
    private LabelValue lvCdUnidade;
    private LabelValue lvQtItemFisico;
    private LabelValue lvVlBaseItemTabelaPreco;
    private LabelValue lvVlItemPedido;
    private LabelValue lvVlTotalIcmsItem;
    private LabelValue lvVlPctIcms;
    private LabelValue lvVlTotalStItem;
    private LabelValue lvVlTotalItemPedido;
    private LabelValue lvCdCfop;
    private LabelValue lvNuSeqProduto;

	public CadItemNfeForm() {
		super(Messages.ITEMNFE_TITULO_CADASTRO);
		setReadOnly();
		scrollable = true;
		lvProduto = new LabelValue();
		lvCdClassificFiscal = new LabelValue();
		lvCdUnidade = new LabelValue();
		lvQtItemFisico = new LabelValue();
		lvVlBaseItemTabelaPreco = new LabelValue();
		lvVlItemPedido = new LabelValue();
		lvVlTotalIcmsItem = new LabelValue();
		lvVlPctIcms = new LabelValue();
		lvVlTotalStItem = new LabelValue();
		lvVlTotalItemPedido = new LabelValue();
		lvCdCfop = new LabelValue();
		lvNuSeqProduto = new LabelValue();
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
        return null;
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		if (LavenderePdaConfig.usaNfePorReferencia) {
			domainToScreenItemNfeReferencia(domain);
		} else {
			domainToScreenItemNfe(domain);
		}
	}
	
	private void domainToScreenItemNfeReferencia(BaseDomain domain) {
		ItemNfeReferencia itemNfeReferencia = (ItemNfeReferencia) domain;
		lvProduto.setMultipleLinesText(itemNfeReferencia.dsReferencia);
		lvCdClassificFiscal.setValue(itemNfeReferencia.cdClassificFiscal);
		lvCdUnidade.setValue(itemNfeReferencia.cdUnidade);
		lvQtItemFisico.setValue(itemNfeReferencia.qtItemFisico);
		lvVlBaseItemTabelaPreco.setValue(itemNfeReferencia.vlBaseItemTabelaPreco);
		lvVlItemPedido.setValue(itemNfeReferencia.vlItemPedido);
		lvVlTotalIcmsItem.setValue(itemNfeReferencia.vlTotalIcmsItem);
		lvVlPctIcms.setValue(itemNfeReferencia.vlPctIcms);
		lvCdCfop.setValue(itemNfeReferencia.cdCfop);
		lvNuSeqProduto.setValue(itemNfeReferencia.nuSeqProduto);
		lvVlTotalItemPedido.setValue(itemNfeReferencia.vlTotalItemPedido);
	}

	private void domainToScreenItemNfe(BaseDomain domain) throws SQLException {
		ItemNfe itemNfe = (ItemNfe) domain;
		String dsProduto = "";
		if (LavenderePdaConfig.isConfigGradeProduto() && ItemNfeService.getInstance().isItemNfeGrade(itemNfe)) {
			ItemPedidoGrade itemPedidoGrade = ItemNfeService.getInstance().getItemPedidoGradeByItemNfe(itemNfe);
			dsProduto = ItemPedidoGradeService.getInstance().getDescricaoProdutoGradeCompleta(itemPedidoGrade);
		} else {
			dsProduto = itemNfe.getDsItemNfe();
		}
		lvProduto.setMultipleLinesText(dsProduto);
		lvCdClassificFiscal.setValue(itemNfe.cdClassificFiscal);
		lvCdUnidade.setValue(itemNfe.cdUnidade);
		lvQtItemFisico.setValue(itemNfe.qtItemFisico);
		lvVlBaseItemTabelaPreco.setValue(itemNfe.vlBaseItemTabelaPreco);
		lvVlItemPedido.setValue(itemNfe.vlItemPedido);
		lvVlTotalIcmsItem.setValue(itemNfe.vlTotalIcmsItem);
		lvVlPctIcms.setValue(itemNfe.vlPctIcms);
		if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe()) {
			lvVlTotalStItem.setValue(itemNfe.vlTotalStItem);
		}
		lvVlTotalItemPedido.setValue(itemNfe.vlTotalItemPedido);
	}

	@Override
	protected void clearScreen() throws java.sql.SQLException {
		lvProduto.setText("");
		lvCdClassificFiscal.setText("");
		lvCdUnidade.setText("");
		lvQtItemFisico.setText("");
		lvVlBaseItemTabelaPreco.setText("");
		lvVlItemPedido.setText("");
		lvVlTotalIcmsItem.setText("");
		lvVlPctIcms.setText("");
		lvVlTotalStItem.setText("");
		lvVlTotalItemPedido.setText("");
		lvCdCfop.setText("");
		lvNuSeqProduto.setText("");
	}
	
	@Override
	protected BaseDomain createDomain() throws SQLException {
		return LavenderePdaConfig.usaNfePorReferencia ? new ItemNfeReferencia() :  new ItemNfe();
	}

	@Override
	protected String getEntityDescription() {
		return Messages.ITEMNFE_NOME_ENTIDADE;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return LavenderePdaConfig.usaNfePorReferencia ? ItemNfeReferenciaService.getInstance() : ItemNfeService.getInstance();
	}
	
	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		reposition();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(getDescricaoProdutoReferencia()), lvProduto, getLeft(), getTop() + HEIGHT_GAP);
		if (LavenderePdaConfig.usaNfePorReferencia) {
			UiUtil.add(this, new LabelName(Messages.PEDIDODESCERP_LABEL_NUSEQUENCIA), lvNuSeqProduto, getLeft(), AFTER);
		}
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_CDCLASSIFICFISCAL), lvCdClassificFiscal, getLeft(), AFTER);
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_CDUNIDADE), lvCdUnidade, getLeft(), AFTER);
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_QTITEMFISICO), lvQtItemFisico, getLeft(), AFTER);
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_VLBASEITEMTABELAPRECO), lvVlBaseItemTabelaPreco, getLeft(), AFTER);
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_VLITEMPEDIDO), lvVlItemPedido, getLeft(), AFTER);
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_VLTOTALICMSITEM), lvVlTotalIcmsItem, getLeft(), AFTER);
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_VLPCTICMS), lvVlPctIcms, getLeft(), AFTER);
		if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe()) {
			UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_VLTOTALSTITEM), lvVlTotalStItem, getLeft(), AFTER);
		}
		if (LavenderePdaConfig.usaNfePorReferencia) {
			UiUtil.add(this, new LabelName(Messages.ITEMNFEREFERENCIA_LABEL_CDCFOP), lvCdCfop, getLeft(), AFTER);
		}
		UiUtil.add(this, new LabelName(Messages.ITEMNFE_LABEL_VLTOTALITEMPEDIDO), lvVlTotalItemPedido, getLeft(), AFTER);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		//--
	}
	
	private String getDescricaoProdutoReferencia() {
		return LavenderePdaConfig.usaNfePorReferencia ? Messages.PRODUTO_LABEL_DSREFERENCIA : Messages.ITEMNFE_LABEL_PRODUTO;
	}

}
