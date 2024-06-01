package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.PopUpSearchFilter;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Concorrente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoDesejado;
import br.com.wmw.lavenderepda.business.service.ConcorrenteService;
import br.com.wmw.lavenderepda.business.service.ProdutoDesejadoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConcorrentePdbxDao;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class CadProdutoDesejadoForm extends BaseLavendereCrudPersonCadForm {

    private LabelValue lvCdProdutoDesejado;
    private EditText edDsProdutoDesejado;
    private EditText edConcorrente;
    private PopUpSearchFilter popUpSearchConcorrente;
    private Pedido pedido;
    private String dsFiltro;
    private CheckBoolean ckFlOutroConcorrente;
    private EditText edOutroConcorrente;
    
    public CadProdutoDesejadoForm(Pedido pedido) {
    	this(pedido, null);
    }

    public CadProdutoDesejadoForm(Pedido pedido, String dsFiltro) {
        super(Messages.PRODUTODESEJADO_TITULO_CADASTRO);
        this.pedido = pedido;
        lvCdProdutoDesejado = new LabelValue();
        edDsProdutoDesejado = new EditText("@@@@@@@@@@", 100);
        edConcorrente = new EditText("@@@@@@@@@@", 100);
        edOutroConcorrente = new EditText("@@@@@@@@@@", 100);
        ckFlOutroConcorrente = new CheckBoolean(Messages.PRODUTODESEJADO_LABEL_CHECK_OUTRO_CONCORRENTE);
        popUpSearchConcorrente = new PopUpSearchFilter(Messages.PRODUTODESEJADO_LABEL_CDCONCORRENTE, edConcorrente, new Concorrente(), ConcorrentePdbxDao.getInstance(), true, Concorrente.CONCORRENTE_NMCOLUNA_CDCONCORRENTE, Concorrente.CONCORRENTE_NMCOLUNA_DSCONCORRENTE);
        this.dsFiltro = dsFiltro;
    }
    
    //-----------------------------------------------

    @Override
    public String getEntityDescription() {
    	return Messages.PRODUTODESEJADO_NOME_ENTIDADE;
    }

    @Override
    protected CrudService getCrudService() {
        return ProdutoDesejadoService.getInstance();
    }
    
    @Override
    protected BaseDomain createDomain() throws SQLException {
    	ProdutoDesejado produtoDesejado = new ProdutoDesejado.Builder(pedido).comCdUsuarioEmissao(pedido.cdUsuario).comDtEmissaoPedido(pedido.dtEmissao).build();
    	produtoDesejado.cdProdutoDesejado = getCrudService().generateIdGlobal();
		produtoDesejado.flOrigemRegistro = ProdutoDesejado.ORIGEM_REGISTRO_PEDIDO;
    	produtoDesejado.flTipoAlteracao = "";
    	produtoDesejado.rowKey = produtoDesejado.getRowKey();
        return produtoDesejado;
    }

    @Override
    protected BaseDomain screenToDomain() throws SQLException {
    	super.screenToDomain();
        ProdutoDesejado produtoDesejado = (ProdutoDesejado) getDomain();
        produtoDesejado.dsProdutoDesejado = edDsProdutoDesejado.getValue();
        if (ValueUtil.isNotEmpty(produtoDesejado.dsProdutoDesejado)) {
        	produtoDesejado.dsProdutoDesejado = produtoDesejado.dsProdutoDesejado.trim();
        }
        produtoDesejado.dsOutroConcorrente = edOutroConcorrente.getValue();
        produtoDesejado.flOutroConcorrente = ckFlOutroConcorrente.getValue();
        produtoDesejado.cdConcorrente = popUpSearchConcorrente.getValue();
        setUltimoConcorrenteUtilizado(produtoDesejado);
        return produtoDesejado;
    }

	private void setUltimoConcorrenteUtilizado(ProdutoDesejado produtoDesejado) {
		pedido.ultimoConcorrenteProdutoDesejadoFlag = ValueUtil.VALOR_SIM.equals(produtoDesejado.flOutroConcorrente);
        pedido.ultimoConcorrenteProdutoDesejado = new String[2];
        if (pedido.ultimoConcorrenteProdutoDesejadoFlag) {
        	pedido.ultimoConcorrenteProdutoDesejado[0] = null;
        	pedido.ultimoConcorrenteProdutoDesejado[1] = produtoDesejado.dsOutroConcorrente;
        } else {
        	pedido.ultimoConcorrenteProdutoDesejado[0] = produtoDesejado.cdConcorrente;
        	pedido.ultimoConcorrenteProdutoDesejado[1] = popUpSearchConcorrente.dsBaseDomain;
        }
	}
    
    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	super.domainToScreen(domain);
        ProdutoDesejado produtoDesejado = (ProdutoDesejado) domain;
        lvCdProdutoDesejado.setValue(produtoDesejado.cdProdutoDesejado);
        edDsProdutoDesejado.setValue(produtoDesejado.dsProdutoDesejado);
        edConcorrente.setValue(ConcorrenteService.getInstance().getDsConcorrente(produtoDesejado.cdConcorrente));
        popUpSearchConcorrente.setValue(produtoDesejado.cdConcorrente);
        ckFlOutroConcorrente.setValue(produtoDesejado.flOutroConcorrente);
        edOutroConcorrente.setValue(produtoDesejado.dsOutroConcorrente);
    }
    
    @Override
    protected void addTabsFixas(Vector tableTitles) {
    	if (ValueUtil.isNotEmpty(tableTitles)) {
    		tableTitles.items[0] = Messages.PRODUTODESEJADO_LABEL_TAB_PRINCIPAL;
    	}
    }
    
    @Override
    protected void clearScreen() throws SQLException {
        clear();
        lvCdProdutoDesejado.setValue(((ProdutoDesejado) getDomain()).cdProdutoDesejado);
        boolean outroConcorrente = pedido.ultimoConcorrenteProdutoDesejadoFlag;
        ckFlOutroConcorrente.setValue(outroConcorrente ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO);
        if (ValueUtil.isNotEmpty(pedido.ultimoConcorrenteProdutoDesejado) && pedido.ultimoConcorrenteProdutoDesejado.length > 1) {
        	if (outroConcorrente) {
        		edOutroConcorrente.setText(pedido.ultimoConcorrenteProdutoDesejado[1]);
        	} else {
        		popUpSearchConcorrente.cdBaseDomain = pedido.ultimoConcorrenteProdutoDesejado[0];
        		popUpSearchConcorrente.dsBaseDomain = pedido.ultimoConcorrenteProdutoDesejado[1];
        		edConcorrente.setText(ConcorrenteService.getInstance().getDsConcorrente(pedido.ultimoConcorrenteProdutoDesejado[0]));
        	}
        }
    }
    
	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		if (dsFiltro != null) {
			edDsProdutoDesejado.setText(dsFiltro);
			dsFiltro = null;
		}
	}
    
	@Override
    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
        edDsProdutoDesejado.setEditable(enabled);
        popUpSearchConcorrente.setEditable(enabled);
        edOutroConcorrente.setEnabled(ckFlOutroConcorrente.isChecked() && enabled);
		popUpSearchConcorrente.setEnabled(!ckFlOutroConcorrente.isChecked() && enabled);
		ckFlOutroConcorrente.setEditable(enabled);
		edConcorrente.setEnabled(false);
    }
    
    @Override
    protected void visibleState() {
    	boolean visible = !pedido.isPedidoTransmitido() && !OrigemPedido.FLORIGEMPEDIDO_ERP.equals(pedido.flOrigemPedido);
    	btSalvar.setVisible(visible);
    	btExcluir.setVisible(visible && isEditing());
    }
    
    //-----------------------------------------------
    
    @Override
    protected void addComponentesFixosInicio() {
        UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PRODUTODESEJADO_LABEL_DSPRODUTODESEJADO), edDsProdutoDesejado, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PRODUTODESEJADO_LABEL_CDCONCORRENTE), popUpSearchConcorrente, getLeft(), AFTER + HEIGHT_GAP, getWFill(), UiUtil.getControlPreferredHeight());
        UiUtil.add(getContainerPrincipal(), ckFlOutroConcorrente, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(getContainerPrincipal(), new LabelName(Messages.PRODUTODESEJADO_LABEL_DESC_OUTRO_CONCORRENTE), edOutroConcorrente, getLeft(), AFTER + HEIGHT_GAP);
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	super.onFormEvent(event);
		switch (event.type) {
			case PenEvent.PEN_UP: {
				if (event.target == ckFlOutroConcorrente && ckFlOutroConcorrente.isEnabled()) {
					if (ckFlOutroConcorrente.isChecked()) {
						edOutroConcorrente.setEnabled(true);
						popUpSearchConcorrente.setEnabled(false);
						edConcorrente.setText("");
						popUpSearchConcorrente.setValue(null);
					} else {
						edOutroConcorrente.setEnabled(false);
						popUpSearchConcorrente.setEnabled(true);
						edOutroConcorrente.setText("");
					}
				}
				break;
			}
		}
    }
    
    @Override
    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	setEnabled(!pedido.isPedidoTransmitido());
    }

	@Override
	protected String getDsTable() {
		return ProdutoDesejado.TABLE_NAME;
	}

}
