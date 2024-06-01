package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeCli;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeNovoCli;
import br.com.wmw.lavenderepda.business.domain.RelNovidadePesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoConfigService;
import br.com.wmw.lavenderepda.business.service.RelNovidadePesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import totalcross.ui.Label;
import totalcross.ui.event.Event;

public class CadRelNovidadeForm extends BaseCrudCadForm {
	
	private LabelName lbCodigo;
	private LabelValue edCodigo;
	private LabelName lbEntidade;
	private LabelValue edEntidade;
	private LabelName lbTipoNovidade;
	private LabelValue edTipoNovidade;
	private LabelName lbNome;
	private LabelValue edNome;
	private LabelName lbDtemissao;
	private LabelValue edDtemissao;
	private LabelName lbMensagem;
	private EditMemo edMensagem;
	private LabelName lbVigencia;
	private LabelValue edVigencia;
    
    public CadRelNovidadeForm() {
        super(Messages.RELNOVIDADE_NOME_ENTIDADE);
    	lbCodigo = new LabelName(Messages.RELNOVIDADE_DETALHES_CODIGO);
    	edCodigo = new LabelValue();
    	lbEntidade = new LabelName(Messages.RELNOVIDADE_DETALHES_ENTIDADE);
    	edEntidade = new LabelValue();
    	lbTipoNovidade = new LabelName(Messages.RELNOVIDADE_DETALHES_TIPO_NOVIDADE);
    	edTipoNovidade = new LabelValue();
    	lbNome = new LabelName(Messages.RELNOVIDADE_DETALHES_PROD_NOME);
    	edNome = new LabelValue();
    	lbDtemissao = new LabelName(Messages.RELNOVIDADE_DETALHES_DTRETORNO);
    	edDtemissao = new LabelValue();
    	lbMensagem = new LabelName(Messages.RELNOVIDADE_DETALHES_RETORNO);
    	edMensagem = new EditMemo("", 8, 4000);
    	edMensagem.setEditable(false);
	    edMensagem.setID("edMensagem");
    	lbVigencia = new LabelName(Messages.PESQUISA_MERCADO_PROD_CONC_VIGENCIA_FILTRO);
    	edVigencia = new LabelValue();
    }

    //-----------------------------------------------

    @Override
    public String getEntityDescription() {
    	return Messages.RELNOVIDADE_NOME_ENTIDADE;
    }

    @Override
    protected CrudService getCrudService() {
        return null;
    }
    
    @Override
    protected BaseDomain createDomain() {
        return null;
    }
    
    @Override
    protected BaseDomain screenToDomain() {
    	return null;
    }
    
    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
    	if (domain instanceof RelNovidadeProd) {
    		RelNovidadeProd relNovidadeProd = (RelNovidadeProd) domain;
        	edCodigo.setText(relNovidadeProd.cdProduto);
        	edEntidade.setText(Produto.class.getSimpleName());
        	edTipoNovidade.setText(TipoNovidadeService.getInstance().getDsTipoNovidade(relNovidadeProd.cdTipoNovidade));
        	lbNome.setText(Messages.RELNOVIDADE_DETALHES_PROD_NOME);
        	edNome.setText(relNovidadeProd.dsProduto);
        	edDtemissao.setText(relNovidadeProd.dtEmissaoRelatorio.toString());
        	edMensagem.setValue(relNovidadeProd.dsNovidadeProduto);
		} else if (domain instanceof RelNovidadeCli) {
    		RelNovidadeCli relNovidadeCli = (RelNovidadeCli) domain;
        	edCodigo.setText(relNovidadeCli.cdCliente);
        	edEntidade.setText(Cliente.class.getSimpleName());
        	edTipoNovidade.setText(TipoNovidadeService.getInstance().getDsTipoNovidade(relNovidadeCli.cdTipoNovidade));
        	lbNome.setText(Messages.RELNOVIDADE_DETALHES_CLIENTE_NOME);
        	edNome.setText(relNovidadeCli.nmRazaoSocial);
        	edDtemissao.setText(relNovidadeCli.dtEmissaoRelatorio.toString());
        	edMensagem.setText(relNovidadeCli.dsNovidadeCliente);
		} else if (domain instanceof RelNovidadeNovoCli) {
			RelNovidadeNovoCli relNovidadeNovoCli = (RelNovidadeNovoCli) domain;
        	edCodigo.setText(relNovidadeNovoCli.cdNovoCliente);
        	edEntidade.setText(NovoCliente.class.getSimpleName());
        	edTipoNovidade.setText(TipoNovidadeService.getInstance().getDsTipoNovidade(relNovidadeNovoCli.cdTipoNovidade));
        	lbNome.setText(Messages.RELNOVIDADE_DETALHES_CLIENTE_NOME);
        	edNome.setText(relNovidadeNovoCli.nmRazaoSocial);
        	edDtemissao.setText(relNovidadeNovoCli.dtGeracao.toString());
        	edMensagem.setText(relNovidadeNovoCli.dsMensagem);
		} else if (domain instanceof RelNovidadePesquisaMercado) {
    		RelNovidadePesquisaMercado relNovidadePesquisaMercado = (RelNovidadePesquisaMercado) domain;
    		edEntidade.setText(Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE);
    		lbNome.setText(Messages.RELNOVIDADE_DETALHES_PESQUISA_MERCADO_NOME);
    		edNome.setText(PesquisaMercadoConfigService.getInstance().findDsByCd(relNovidadePesquisaMercado));
    		lbDtemissao.setText(Messages.RELNOVIDADE_DETALHES_PESQUISA_MERCADO_DATA_NOVIDADE);
    		edDtemissao.setText(relNovidadePesquisaMercado.dtEmissaoRelatorio.toString());
    		edMensagem.setText(relNovidadePesquisaMercado.dsNovidadePesquisa);
    		edVigencia.setText(RelNovidadePesquisaMercadoService.getInstance().getDsVigencia(relNovidadePesquisaMercado));
	    }
    }
    
    @Override
    protected void clearScreen() {
    	edEntidade.setText("");
    	edTipoNovidade.setText("");
    	edNome.setText("");
    	edDtemissao.setText("");
    	edMensagem.setText("");
    	edVigencia.setText("");
    }
    
    //-----------------------------------------------
    
    @Override
    protected void onFormStart() throws SQLException {
        //--
	    if (getDomain() instanceof RelNovidadePesquisaMercado) {
	    	addComponentsPesquisaMercado();
	    } else {
	    	addAllComponents();
	    }
    }

	private void addComponentsPesquisaMercado() {
		UiUtil.add(this, lbNome, edNome, getLeft(), getNextY());
		UiUtil.add(this, lbDtemissao, edDtemissao, getLeft(), getNextY());
		UiUtil.add(this, lbVigencia, edVigencia, getLeft(), getNextY());
		UiUtil.add(this, lbMensagem, edMensagem, getLeft(), getNextY(), getWFill(), FILL - barBottomContainer.getHeight() - HEIGHT_GAP_BIG);
	}

	private void addAllComponents() {
		UiUtil.add(this, lbCodigo, edCodigo, getLeft(), getNextY());
		UiUtil.add(this, lbEntidade, edEntidade, getLeft(), getNextY());
		UiUtil.add(this, lbTipoNovidade, edTipoNovidade, getLeft(), getNextY());
		UiUtil.add(this, lbNome, edNome, getLeft(), getNextY());
		UiUtil.add(this, lbDtemissao, edDtemissao, getLeft(), getNextY());
		UiUtil.add(this, lbMensagem, edMensagem, getLeft(), getNextY());
	}

	@Override
    protected void addBarButtons() { }
    
    @Override
    protected void onFormEvent(Event event) {  }

}
