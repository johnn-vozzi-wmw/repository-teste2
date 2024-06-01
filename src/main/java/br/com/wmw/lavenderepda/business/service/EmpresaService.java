package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.RepresentanteEmp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EmpresaPdbxDao;
import br.com.wmw.lavenderepda.presentation.ui.CadItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.ListItemPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.util.Vector;

public class EmpresaService extends CrudService {

    private static EmpresaService instance;

    private EmpresaService() {
        //--
    }

    public static EmpresaService getInstance() {
        if (instance == null) {
            instance = new EmpresaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return EmpresaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getEmpresaNameWithId(String cdEmpresa) throws SQLException {
    	Empresa empresaFilter = new Empresa();
    	empresaFilter.cdEmpresa = cdEmpresa;
    	Empresa empresa = (Empresa) findByRowKey(empresaFilter.getRowKey());
    	return empresa.toString();
    }

    public String getEmpresaName(String cdEmpresa) throws SQLException {
    	Empresa empresaFilter = new Empresa();
    	empresaFilter.cdEmpresa = cdEmpresa;
    	Empresa empresa = (Empresa) findByRowKey(empresaFilter.getRowKey());
    	return empresa.toString();
    }

    public Vector findAllByRepresentante(String cdRepresentante) throws SQLException {
    	Vector empresaList = new Vector();
    	Vector fullEmpresaList = findAll();
    	//Pega todas as empresas relacionadas ao representante
    	RepresentanteEmp representanteEmpFilter = new RepresentanteEmp();
    	representanteEmpFilter.cdRepresentante = cdRepresentante;
    	Vector representanteEmpList = RepresentanteEmpService.getInstance().findAllByExample(representanteEmpFilter);
    	if (!ValueUtil.isEmpty(representanteEmpList)) {
    		int size = representanteEmpList.size();
    		RepresentanteEmp representanteEmp;
    		Empresa empresa;
    		for (int i = 0; i < size; i++) {
    			representanteEmp = (RepresentanteEmp)representanteEmpList.items[i];
    			empresa = new Empresa();
    			empresa.cdEmpresa = representanteEmp.cdEmpresa;
    			int index = fullEmpresaList.indexOf(empresa);
    			if (index != -1) {
    				empresaList.addElement(fullEmpresaList.items[index]);
    			}
    		}
    	}
    	fullEmpresaList = null;
    	return empresaList;
    }
    
    public void changeEmpresaSessao(String cdEmpresa) throws SQLException {
    	changeEmpresaSessao(cdEmpresa, false);
    }

	public void changeEmpresaSessao(String cdEmpresa, boolean forceChange) throws SQLException {
		if (!SessionLavenderePda.cdEmpresa.equals(cdEmpresa) || forceChange) {
			SessionLavenderePda.cdEmpresa = cdEmpresa;
			ConfigInternoService.getInstance().salvaEmpRepSelecionado(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante);
			//--
			SyncManager.limpeCaches();
			MainLavenderePda.getInstance().loadSessao(SessionLavenderePda.usuarioPdaRep);
			CadItemPedidoForm.invalidateInstance();
			ListItemPedidoForm.invalidateInstance();
			SessionLavenderePda.setCliente(ClienteService.getInstance().getCliente(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCliente().cdRepresentante, SessionLavenderePda.getCliente().cdCliente));
		}
	}

	public Empresa getEmpresaDefaultRepresentante(String cdRepresentante) throws SQLException {
		Empresa empresaDefault = null;
		Vector representanteEmpList = RepresentanteEmpService.getInstance().findAll();
		if (ValueUtil.isNotEmpty(representanteEmpList)) {
			Empresa empresaFilter = new Empresa();
			int size = representanteEmpList.size();
			RepresentanteEmp representanteEmp;
			for (int i = 0; i < size; i++) {
				representanteEmp = (RepresentanteEmp) representanteEmpList.items[i];
				if (representanteEmp.isDefault() && (empresaDefault == null || representanteEmp.cdRepresentante.equals(cdRepresentante))) {
					empresaFilter.cdEmpresa = representanteEmp.cdEmpresa;
					empresaDefault = (Empresa) EmpresaService.getInstance().findByRowKey(empresaFilter.getRowKey());
				}
			}
			//Não achou a empresa padrão, pega a empresa do configinterno
			if (empresaDefault == null) {
				String ultimoCdEmpresa = ConfigInternoService.getInstance().getUltimoCdEmpresaLogado();
				if (ValueUtil.isNotEmpty(ultimoCdEmpresa)) {
					empresaFilter.cdEmpresa = ultimoCdEmpresa;
					empresaDefault = (Empresa) EmpresaService.getInstance().findByRowKey(empresaFilter.getRowKey());
				}
			}
			//Ainda não achou a empresa padrão, então pega a primeira da lista de empresas
			if (empresaDefault == null) {
				LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_SESSAO, Messages.USUARIO_MSG_LOGIN_EMPRESA_DEFAULT_INEXISTENTE);
				empresaFilter.cdEmpresa = ((RepresentanteEmp) representanteEmpList.items[0]).cdEmpresa;
				empresaDefault = (Empresa) EmpresaService.getInstance().findByRowKey(empresaFilter.getRowKey());
			}
		}
		return empresaDefault;
    }
	
	public Empresa getEmpresa(final String cdEmpresa) throws SQLException {
		Empresa empresaFilter = new Empresa();
		empresaFilter.cdEmpresa = cdEmpresa;
		return (Empresa) EmpresaService.getInstance().findByRowKey(empresaFilter.getRowKey());
	}
	
	public String getLocalEstoqueEmpresa() throws SQLException {
		Empresa empresa = new Empresa();
		empresa.cdEmpresa = SessionLavenderePda.cdEmpresa;
		empresa = (Empresa) findByPrimaryKey(empresa);
		return empresa.cdLocalEstoque;
	}
	
	protected int findNuIntervaloCancNfe(String cdEmpresa) throws SQLException {
		String nuIntervaloCancNfe = findValueColumn("nuIntervaloCancNfe");
		return ValueUtil.getIntegerValue(nuIntervaloCancNfe);
	}

	protected int findNuDiasIntDevolEstoque() throws SQLException {
		String nuDiasIntDevolEstoque = findValueColumn("nuDiasIntDevolEstoque");
		return ValueUtil.getIntegerValue(nuDiasIntDevolEstoque);
	}
	
	public double findVlFatorFaceamento() throws SQLException {
		String vlFatorFaceamento = findValueColumn("vlFatorFaceamento");
		return ValueUtil.getDoubleValue(vlFatorFaceamento);
	}
	
	private String findValueColumn(String columnName) throws SQLException {
		Empresa empresaFilter = new Empresa();
		empresaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		return findColumnByRowKey(empresaFilter.getRowKey(), columnName);
	}

	public double findVlPctFreteByCdEmpresa(String cdEmpresa) throws SQLException {
		return EmpresaPdbxDao.getInstance().findVlPctFreteByCdEmpresa(cdEmpresa);
	}

}