package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CestaPositCliente;
import br.com.wmw.lavenderepda.business.domain.CestaPositProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.CestaPositProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CampanhaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CestaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import totalcross.ui.ToolTip;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListCestaPositProdutoForm extends BaseCrudListForm {

	private CestaComboBox cbCesta;
	private CampanhaComboBox cbCampanha;
	private CestaPositCliente cestaPositiCliente;
	private RepresentanteSupervComboBox cbRepresentante;

    private LabelName lbTtMeta;
    private LabelName lbTtRealizado;
    private LabelName lbQtd;
	private LabelValue lbQtProdutos;
	private LabelValue lbTotalMeta;
	private LabelValue lbTotalReal;
	private boolean inClienteDetail;
	private String nmRazaoSocial;
	private CestaPositCliente cestaCliente;

	public ListCestaPositProdutoForm() throws SQLException {
		this(null);
	}

	public ListCestaPositProdutoForm(CestaPositCliente cestaCliente) throws SQLException {
		this(cestaCliente, "");
	}
	
    public ListCestaPositProdutoForm(CestaPositCliente cestaCliente, String nmRazaoSocial) throws SQLException {
        super(Messages.CESTAPOSITPRODUTO_NOME_ENTIDADE);
        this.cestaCliente = cestaCliente;
        if (ValueUtil.isNotEmpty(nmRazaoSocial)) {
        	this.nmRazaoSocial = nmRazaoSocial; 
        } else if (SessionLavenderePda.getCliente() != null) {
        	this.nmRazaoSocial = SessionLavenderePda.getCliente().nmRazaoSocial;
        }
        cbCesta = new CestaComboBox(Messages.CESTA_NOME_ENTIDADE, false);
        cbCampanha = new CampanhaComboBox(Messages.CAMPANHA_NOME_ENTIDADE);
        cestaPositiCliente = cestaCliente;
        cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
        if (cestaCliente != null) {
        	inClienteDetail = true;
        }
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
        lbTtMeta = new LabelName(Messages.CESTA_TTMETA);
        lbTtRealizado = new LabelName(Messages.CESTA_TTREALIZADO);
        lbQtd = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return CestaPositProdutoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new CestaPositProduto();
    }

    //@Override
    protected Vector getDomainList() throws SQLException {
    	String filtro = (edFiltro.getValue()).toUpperCase();
    	//--
    	String cdCliente = null;
    	if (inClienteDetail) {
    		cdCliente = cestaCliente.cdCliente;
		}
    	Vector list = CestaPositProdutoService.getInstance().findAllCestaPositProd(filtro, cbCampanha.getValue(), cbCesta.getValue(), inClienteDetail, cdCliente);
    	updateTotalizadores(list);
    	return list;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        CestaPositProduto cestapositproduto = (CestaPositProduto) domain;
        double vlPct = 0;
        try {
        	vlPct = (cestapositproduto.vlRealizado * 100) / cestapositproduto.vlMeta;
        } catch (Throwable e) {
        	//Não faz nada
		}
        String[] item = {
            cestapositproduto.rowKey,
        	StringUtil.getStringValue(cestapositproduto.cdProduto),
            ProdutoService.getInstance().getDsProduto(cestapositproduto.cdProduto),
            StringUtil.getStringValueToInterface(cestapositproduto.vlMeta),
            StringUtil.getStringValueToInterface(cestapositproduto.vlRealizado),
            StringUtil.getStringValueToInterface(vlPct)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    private void updateTotalizadores(Vector list) {
    	double vlTotalRealizado = 0.d;
    	double vlTotalMeta = 0.d;
        int listSize = list.size();
        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
            	vlTotalMeta += ((CestaPositProduto)list.items[i]).vlMeta;
            	vlTotalRealizado += ((CestaPositProduto)list.items[i]).vlRealizado;
            }
        }
        lbQtProdutos.setValue(listSize);
        lbTotalMeta.setValue(vlTotalMeta);
       	lbTotalReal.setValue(vlTotalRealizado);
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor() && !inClienteDetail) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), getNextY());
    	} else if (inClienteDetail) {
            UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), new LabelValue(nmRazaoSocial), getLeft(), getNextY());
    	}
    	UiUtil.add(this, new LabelName(Messages.CAMPANHA_NOME_ENTIDADE), cbCampanha, getLeft(), getNextY());
    	UiUtil.add(this, new LabelName(Messages.CESTA_NOME_ENTIDADE), cbCesta, getLeft(), getNextY());
        //--
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
		edFiltro.setText("");
        //--
        int ww = fm.stringWidth("www");
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
            new GridColDefinition(LavenderePdaConfig.ocultaColunaCdProduto ? FrameworkMessages.CAMPO_ID : Messages.PRODUTO_LABEL_CODIGO, fm.stringWidth(LavenderePdaConfig.tamanhoColunaCdProduto), LEFT),
            new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, ww * 4, LEFT),
            new GridColDefinition(Messages.METAS_META, ww * 2, RIGHT),
            new GridColDefinition(Messages.METAS_TTRESULTADO, ww * 2, RIGHT),
    		new GridColDefinition(Messages.CESTA_PERCENTUAL_POSITIVACAO, ww, RIGHT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - lbQtd.getPreferredHeight() - HEIGHT_GAP_BIG);
        //--
        UiUtil.add(this, lbQtd, LEFT + WIDTH_GAP, BOTTOM - HEIGHT_GAP_BIG);
        UiUtil.add(this, lbQtProdutos = new LabelValue("9999"), AFTER + WIDTH_GAP, SAME);
        UiUtil.add(this, lbTtMeta, AFTER + WIDTH_GAP_BIG, SAME);
        UiUtil.add(this, lbTotalMeta = new LabelValue("99999999,99"), AFTER + WIDTH_GAP, SAME);
        UiUtil.add(this, lbTtRealizado, AFTER + WIDTH_GAP_BIG, SAME);
        UiUtil.add(this, lbTotalReal = new LabelValue("99999999,99"), AFTER + WIDTH_GAP, SAME);
        ToolTip tip = new ToolTip(lbTtMeta, Messages.METAS_TTMETA_MSG);
        tip.millisDelay = 100;
        tip = new ToolTip(lbTtRealizado, Messages.METAS_VLREALIZADO_MSG);
        tip.millisDelay = 100;
        tip = new ToolTip(lbQtd, Messages.CESTA_LABEL_QTREGISTROS);
        tip.millisDelay = 100;
    }

    public void loadDefaultFilters() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor() && !inClienteDetail) {
        	cbRepresentante.setSelectedIndex(0);
        	if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
            	cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
            }
    	}
    	//--
        if (cestaPositiCliente != null) {
        	cbCampanha.setValue(cestaPositiCliente.cdCampanha);
        	cbCampanha.setEnabled(false);
        	cbCesta.setValue(cestaPositiCliente.cdCesta);
        	cbCesta.setEnabled(false);
        } else {
        	cbCampanha.setSelectedIndex(0);
        	cbCesta.setSelectedIndex(0);
        }
    }

    //@Override
    public void list() throws SQLException {
    	super.list();
    	gridEdit.qsort(1);
    }
    
    //@Override
    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbRepresentante) {
					if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
					}
					filtrarClick();
				} else  if ((event.target == cbCampanha) || (event.target == cbCesta)) {
					filtrarClick();
				}
				break;
			}
    	}
    }

    protected void filtrarClick() throws SQLException {
		list();
		if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
			edFiltro.setValue("");
		}
        edFiltro.requestFocus();
    }
}
