package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ContaCorrenteCli;
import br.com.wmw.lavenderepda.business.service.ContaCorrenteCliService;
import totalcross.ui.event.Event;

public class CadContaCorrenteCliForm extends BaseCrudCadForm {

    private EditText edCdEmpresa;
    private EditText edCdRepresentante;
    private EditText edCdCliente;
    private EditText edNuDocumento;
    private EditDate edDtMovimentacao;
    private EditNumberFrac edVlCredito;
    private EditNumberFrac edVlDebito;
    private EditText edFlTipoAlteracao;
    private EditText edCdUsuario;

    public CadContaCorrenteCliForm() {
        super(Messages.CONTACORRENTECLI_NOME_ENTIDADE);
        edCdEmpresa = new EditText("9999999999", 20);
        edCdRepresentante = new EditText("9999999999", 20);
        edCdCliente = new EditText("@@@@@@@@@@", 20);
        edNuDocumento = new EditText("@@@@@@@@@@", 20);
        edDtMovimentacao = new EditDate();
        edVlCredito = new EditNumberFrac("9999999999", 9, 7);
        edVlDebito = new EditNumberFrac("9999999999", 9, 7);
        edFlTipoAlteracao = new EditText("@@@@@@@@@@", 1);
        edCdUsuario = new EditText("@@@@@@@@@@", 8);
    }

    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ContaCorrenteCliService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new ContaCorrenteCli();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        ContaCorrenteCli contaCorrenteCli = (ContaCorrenteCli) getDomain();
        contaCorrenteCli.cdEmpresa = edCdEmpresa.getValue();
        contaCorrenteCli.cdRepresentante = edCdRepresentante.getValue();
        contaCorrenteCli.cdCliente = edCdCliente.getValue();
        contaCorrenteCli.nuDocumento = edNuDocumento.getValue();
        contaCorrenteCli.dtMovimentacao = edDtMovimentacao.getValue();
        contaCorrenteCli.vlCredito = edVlCredito.getValueDouble();
        contaCorrenteCli.vlDebito = edVlDebito.getValueDouble();
        contaCorrenteCli.cdUsuario = edCdUsuario.getValue();
        return contaCorrenteCli;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        ContaCorrenteCli contaCorrenteCli = (ContaCorrenteCli) domain;
        edCdEmpresa.setValue(contaCorrenteCli.cdEmpresa);
        edCdRepresentante.setValue(contaCorrenteCli.cdRepresentante);
        edCdCliente.setValue(contaCorrenteCli.cdCliente);
        edNuDocumento.setValue(contaCorrenteCli.nuDocumento);
        edDtMovimentacao.setValue(contaCorrenteCli.dtMovimentacao);
        edVlCredito.setValue(contaCorrenteCli.vlCredito);
        edVlDebito.setValue(contaCorrenteCli.vlDebito);
        edCdUsuario.setValue(contaCorrenteCli.cdUsuario);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        edCdEmpresa.setText("");
        edCdRepresentante.setText("");
        edCdCliente.setText("");
        edNuDocumento.setText("");
        edDtMovimentacao.setText("");
        edVlCredito.setText("");
        edVlDebito.setText("");
        edFlTipoAlteracao.setText("");
        edCdUsuario.setText("");
    }

    public void setEnabled(boolean enabled) {
        edCdEmpresa.setEditable(enabled);
        edCdRepresentante.setEditable(enabled);
        edCdCliente.setEditable(enabled);
        edNuDocumento.setEditable(enabled);
        edDtMovimentacao.setEditable(enabled);
        edVlCredito.setEditable(enabled);
        edVlDebito.setEditable(enabled);
        edFlTipoAlteracao.setEditable(enabled);
        edCdUsuario.setEditable(enabled);
    }

    //-----------------------------------------------

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_CDEMPRESA), edCdEmpresa, getLeft(), getTop() + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_CDREPRESENTANTE), edCdRepresentante, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_CDCLIENTE), edCdCliente, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_NUDOCUMENTO), edNuDocumento, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_DTMOVIMENTACAO), edDtMovimentacao, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_VLCREDITO), edVlCredito, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_VLDEBITO), edVlDebito, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_FLTIPOALTERACAO), edFlTipoAlteracao, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_CDUSUARIO), edCdUsuario, getLeft(), AFTER + HEIGHT_GAP);
    }

	protected void onFormEvent(Event event) throws SQLException {
		// Auto-generated method stub
	}

}
