package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.EmpresaEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.service.CampoLavendereService;
import br.com.wmw.lavenderepda.business.service.EmpresaEnderecoService;
import br.com.wmw.lavenderepda.business.service.NovoCliEnderecoService;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class CadNovoCliEnderecoForm extends BaseLavendereCrudPersonCadForm {
	
	private NovoCliente novoCliente;
	private NovoCliEndereco novoCliEnderecoSelecionado;
    private boolean enderecoExcluido;

	public CadNovoCliEnderecoForm() {
		super(Messages.NOVOCLIENDERECO_ENTIDADE);
		barBottomContainer.setVisible(false);
        barTopContainer.setVisible(false);
	}
		
	public NovoCliente getNovoCliente() {
		return novoCliente;
	}

	public void setNovoCliente(NovoCliente novoCliente) {
		this.novoCliente = novoCliente;
	}

	public NovoCliEndereco getNovoCliEnderecoSelecionado() {
		return novoCliEnderecoSelecionado;
	}

	public void setNovoCliEnderecoSelecionado(NovoCliEndereco novoCliEnderecoSelecionado) {
		this.novoCliEnderecoSelecionado = novoCliEnderecoSelecionado;
	}

	public boolean isEnderecoExcluido() {
		return enderecoExcluido;
	}

	//@Override
	protected String getDsTable() throws SQLException {
		return NovoCliEndereco.TABLE_NAME;
	}
	
	//@Override
	protected int getTop() {
		return TOP;
	}

	//@Override
	protected BaseDomain createDomain() throws SQLException {
		NovoCliEndereco novoCliEndereco = new NovoCliEndereco();
		if(novoCliente != null) {
		novoCliEndereco.cdEmpresa = novoCliente.cdEmpresa;
		novoCliEndereco.cdRepresentante = novoCliente.cdRepresentante;
		novoCliEndereco.cdNovoCliente = novoCliente.cdNovoCliente;
		novoCliEndereco.flOrigemNovoCliente = novoCliente.flOrigemNovoCliente;
		}
		return novoCliEndereco;
	}
		
	//@Override
	protected String getEntityDescription() {
		return title;
	}

	//@Override
	protected CrudService getCrudService() throws SQLException {
		return NovoCliEnderecoService.getInstance();
	}
	
	protected NovoCliEndereco getNovoCliEndereco() throws SQLException {
		return (NovoCliEndereco) getDomain();
	}

	
	protected Hashtable getHashComponentes() {
		return hashComponentes;
	}
	
	//@Override
	protected void salvarClick() throws SQLException {
		screenToDomain();
		getDomain().flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
		NovoCliEnderecoService.getInstance().validate(getDomain());
		if (!isEditing()) {
			((NovoCliEndereco) getDomain()).cdEndereco = StringUtil.getStringValue(novoCliente.novoCliEnderecoList.size() + 1);
			novoCliente.novoCliEnderecoList.addElement(getDomain());
		}
	}
	
	//@Override
	protected void excluirClick() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.NOVOCLIENDERECO_CONFIRMA_EXCLUSAO)) {
			novoCliente.novoCliEnderecoList.removeElement(getDomain());
			enderecoExcluido = true;
		} else {
			enderecoExcluido = false;
		}
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		if (isApresentaDiasEntregaEmpresaCadastroEndereco()) {
			PushButtonGroupBase pbDsDiaEntregaEmpresa = (PushButtonGroupBase) getHashComponentes().get(NovoCliEndereco.NMCOLUNA_DSDIAENTREGAEMPRESA);
			if (pbDsDiaEntregaEmpresa != null) {
				pbDsDiaEntregaEmpresa.setEnabled(false);
				NovoCliEndereco novoCliEndereco = getNovoCliEndereco();
				EmpresaEndereco empresaEndereco;
				if (isEditing()) {
					empresaEndereco = EmpresaEnderecoService.getInstance().findEmpresaEndereco(novoCliEndereco.cdEmpresa, StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_DSBAIRRO)), StringUtil.getStringValue(novoCliEndereco.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_DSCIDADE)));
				} else {
					empresaEndereco = EmpresaEnderecoService.getInstance().findEmpresaEndereco(novoCliEndereco.cdEmpresa);
				}
				if (empresaEndereco != null) {
					pbDsDiaEntregaEmpresa.setValuesFormatted(empresaEndereco.dsDiasEntrega);
				}
			}
		}
	}
	
	public boolean isApresentaDiasEntregaEmpresaCadastroEndereco() throws SQLException {
		return LavenderePdaConfig.apresentaDiasEntregaEmpresaCadastroEndereco && isDsDiaEntregaVisivelCad();
	}
	
	private boolean isDsDiaEntregaVisivelCad() throws SQLException {
		Vector personFields =  getConfigPersonCadList();
    	Campo campoExample = new Campo();
    	campoExample.cdSistema = Campo.CD_SISTEMA_PADRAO;
    	campoExample.nmEntidade = CampoLavendereService.getCampoLavendereInstance().getNmEntidadeFromPdaTableName(NovoCliEndereco.TABLE_NAME);
    	campoExample.nmCampo = NovoCliEndereco.NMCOLUNA_DSDIAENTREGAEMPRESA;
    	int index = personFields.indexOf(campoExample);
    	if (index != -1) {
    		Campo campo = (Campo)personFields.items[index];
    		return campo.isVisivelCad();
    	}
    	return false;
	}

}
