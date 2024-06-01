package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoqueRep;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EstoqueRepDbxDao;
import br.com.wmw.lavenderepda.print.FechamentoEstoquePrint;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONObject;
import totalcross.util.Date;
import totalcross.util.Vector;

public class EstoqueRepService extends CrudService {

    private static EstoqueRepService instance;
    
    private EstoqueRepService() {
        //--
    }
    
    public static EstoqueRepService getInstance() {
        if (instance == null) {
            instance = new EstoqueRepService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return EstoqueRepDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public void deleteRegistroEnviadoServidor() throws SQLException {
		EstoqueRep estoqueRepFilter = new EstoqueRep();
		estoqueRepFilter.deleteRegistroEnviadoServidor = true;
		deleteAllByExample(estoqueRepFilter);
	}

	public void insereEstoqueRepByEstoque(Estoque estoque) throws SQLException {
    	EstoqueRep estoqueRep = getNewEstoqueRep(estoque);
    	insereEstoqueRepByEstoque(estoqueRep);
	}

	private void insereEstoqueRepByEstoque(EstoqueRep estoqueRep) throws SQLException {
		try {
			delete(estoqueRep);
		} catch (Throwable e) {
			// nao excluiu nada
		}
    	insert(estoqueRep);
	}

	private EstoqueRep getNewEstoqueRep(Estoque estoque) {
		EstoqueRep estoqueRep = new EstoqueRep();
    	estoqueRep.cdEmpresa = estoque.cdEmpresa;            
    	estoqueRep.cdRepresentante = estoque.cdRepresentante;
    	estoqueRep.cdProduto = estoque.cdProduto;      
    	estoqueRep.cdItemGrade1 = estoque.cdItemGrade1;   
    	estoqueRep.cdItemGrade2 = estoque.cdItemGrade2;   
    	estoqueRep.cdItemGrade3 = estoque.cdItemGrade3;   
    	estoqueRep.cdLocalEstoque = estoque.cdLocalEstoque; 
    	estoqueRep.qtEstoque = estoque.qtEstoque;      
    	estoqueRep.dtEstoque = DateUtil.getCurrentDate();
    	estoqueRep.hrEstoque = TimeUtil.getCurrentTimeHHMM();
		return estoqueRep;
	}
	
	private void executaValidacoes(Vector remessaEstoqueList) throws Exception {
		validaSeExisteRemessaNoEquipamento(remessaEstoqueList);
		validaSeRemessasEstaoTodasFinalizadas(remessaEstoqueList.size());
		validaRemessaSemEstoqueParaDevolver(remessaEstoqueList);
		validaRemessasNaoLiberadas();
		validaPedidoAbertosEFechados();
		validaPeriodoDevolucaoEstoque();
	}
	
	private void validaSeExisteRemessaNoEquipamento(Vector remessaEstoqueList) {
		if (ValueUtil.isEmpty(remessaEstoqueList)) {
			throw new ValidationException(Messages.REMESSAESTOQUE_MSG_SEM_REGISTRO_EQUIPAMENTO);
		}
	}
	
	private void validaSeRemessasEstaoTodasFinalizadas(int qtdRemessas) throws SQLException {
		int qtdRemessaEstoqueFinalizada = RemessaEstoqueService.getInstance().qtdRemessaEstoqueFinalizada();
		if (qtdRemessas == qtdRemessaEstoqueFinalizada) {
			throw new ValidationException(Messages.REMESSAESTOQUE_MSG_TODAS_FINALIZADAS);
		}
	}
	
	private void validaRemessaSemEstoqueParaDevolver(Vector remessaEstoqueList) throws SQLException {
		int size = remessaEstoqueList.size();
		int qtdRemessaSemEstoqueParaDevolver = 0;
		Vector cdLocalEstoqueConsultadoList = new Vector();
		for (int i = 0; i < size; i++) {
			RemessaEstoque remessaEstoque = (RemessaEstoque) remessaEstoqueList.items[i];
			if (!remessaEstoque.isFinalizada()) {
				String cdLocalEstoque = remessaEstoque.cdLocalEstoque;
				if (!cdLocalEstoqueConsultadoList.contains(cdLocalEstoque)) {
					if (getQtdEstoquePorRemessa(cdLocalEstoque) == 0) {
						qtdRemessaSemEstoqueParaDevolver++;
					}
					cdLocalEstoqueConsultadoList.addElement(cdLocalEstoque);
				}
				
			}
		}
		if (size == qtdRemessaSemEstoqueParaDevolver) {
			throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_MSG_EXISTE_REMESSA_SEM_ESTOQUE);
		}
	}

	private double getQtdEstoquePorRemessa(String cdLocalEstoque) throws SQLException {
		return EstoqueService.getInstance().qtdRemessaSemEstoqueParaDevolver(cdLocalEstoque);
	}

	private void validaRemessasNaoLiberadas() throws SQLException {
		int qtdEstoqueNaoLiberado = RemessaEstoqueService.getInstance().qtdEstoqueNaoLiberado();
		if (qtdEstoqueNaoLiberado > 0) {
			throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_MSG_EXISTE_REMESSA_BLOQUEADA);
		}
		
	}
	
	private void validaPedidoAbertosEFechados() throws SQLException {
		if (PedidoService.getInstance().countPedidosEmAberto() > 0) {
			throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_MSG_EXISTE_PEDIDO_ABERTO);
		}
		if (PedidoService.getInstance().countPedidosFechado() > 0) {
			throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_MSG_EXISTE_PEDIDO_FECHADO);
		}
	}
	
	private void validaPeriodoDevolucaoEstoque() throws Exception {
		if (!SyncManager.isConexaoPdaDisponivel()) {
			throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_MSG_SEM_CONEXAO_WEB);
		}
		int nuDiasIntDevolEstoque = EmpresaService.getInstance().findNuDiasIntDevolEstoque();
		if (nuDiasIntDevolEstoque > 0) {
			String retorno = SyncManager.validaPeriodoDevolucaoEstoque(StringUtil.getStringValue(nuDiasIntDevolEstoque));
			if (!ValueUtil.valueEquals(ValueUtil.VALOR_SIM, retorno)) {
				if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, retorno)) {
					throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_MSG_PERIODO_INVALIDO);
				} else {
					throw new ValidationException(MessageUtil.getMessage(Messages.DEVOLUCAO_ESTOQUE_MSG_ERRO_DESCONHECIDO, retorno));
				}
			}
				
		} 
	}

	public void devolveEstoqueTotal() throws Exception {
		Vector remessaEstoqueList = RemessaEstoqueService.getInstance().findAllRemessaEstoqueNoEquipamento();
		executaValidacoes(remessaEstoqueList);
		processaDevolucaoEstoqueWeb(remessaEstoqueList);
		int size = remessaEstoqueList.size();
		for (int i = 0; i < size; i++) {
			RemessaEstoque remessaEstoque = (RemessaEstoque) remessaEstoqueList.items[i];
			if (!remessaEstoque.isFinalizada()) {
				insereRegistroEstoqueRep(remessaEstoque.cdLocalEstoque);
				remessaEstoque.flFinalizada = ValueUtil.VALOR_SIM;
				remessaEstoque.dtFinalizacao = new Date();
				remessaEstoque.hrFinalizacao = TimeUtil.getCurrentTimeHHMM();;
				RemessaEstoqueService.getInstance().update(remessaEstoque);
			} 
		}
		imprimirRelatorioDevolucao();
	}
	
	private void processaDevolucaoEstoqueWeb(Vector listRemessaEstoque) {
		try {
			if (!SyncManager.isConexaoPdaDisponivel()) {
				throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_MSG_SEM_CONEXAO_WEB);
			}
			String retorno = SyncManager.processaDevolucaoEstoqueWeb(listRemessaEstoque);
			if(ValueUtil.isEmpty(retorno)) {
				throw new ValidationException(Messages.DEVOLUCAO_ESTOQUE_RETORNO_VAZIO);
			}
			try {
				JSONObject jsonRetorno = new JSONObject(retorno);
				if(jsonRetorno != null && jsonRetorno.has("OK")) {
					return;
				}
				prepareErrorRetorno(jsonRetorno);
			} catch (Throwable ex) {
				if (!ex.getClass().equals(ValidationException.class)) {
					prepareErrorRetorno(null);
				} else {
					throw ex;
				}
			}
		} catch (Throwable e) {
			throw new ValidationException(e.getMessage());
		}
	}

	private void prepareErrorRetorno(JSONObject jsonRetorno) {
		String errorMessage = null;
		if(jsonRetorno != null && jsonRetorno.has("ERROR")) {
			errorMessage = jsonRetorno.getString("ERROR");
		}
		if (ValueUtil.isEmpty(errorMessage)) {
			errorMessage = Messages.RETORNO_DEVOLUCAO_INVALIDO;
		}
		throw new ValidationException(MessageUtil.getMessage(Messages.DEVOLUCAO_ESTOQUE_ERRO, errorMessage));
	}

	private void imprimirRelatorioDevolucao() throws Exception {
		if (UiUtil.showConfirmYesNoMessage(Messages.DEVOLUCAO_ESTOQUE_MSG_IMPRIMIR)) {
			new FechamentoEstoquePrint().run();
		}
	}

	private void insereRegistroEstoqueRep(String cdLocalEstoque) throws SQLException {
		Vector estoqueList = EstoqueService.getInstance().findEstoqueByCdLocal(cdLocalEstoque);
		insereRegistroEstoqueRep(estoqueList);
		EstoqueService.getInstance().deleteByLocalEstoque(cdLocalEstoque);
	}
	
	private void insereRegistroEstoqueRep(Vector estoqueList) throws SQLException {
		int size = estoqueList.size();
		for (int i = 0; i < size; i++) {
			Estoque estoque = (Estoque) estoqueList.items[i];
			EstoqueRep estoqueRep = getNewEstoqueRep(estoque);
			estoqueRep.flTipoDevolucao = EstoqueRep.TIPO_DEVOLUCAO_COMPLETA;
			insereEstoqueRepByEstoque(estoqueRep);
		}
	}

	public List<EstoqueRep> findDevolucaoEstoque(String cdEmpresa, String cdRepresentante) throws SQLException {
		return EstoqueRepDbxDao.getInstance().findDevolucaoEstoque(cdEmpresa, cdRepresentante);
	}
	public void deleteEstoqueRepAntigos(Date dataLimite) throws SQLException {
		EstoqueRep estoqueRepFilter = new EstoqueRep();
		estoqueRepFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;            
		estoqueRepFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(EstoqueRep.class);
		estoqueRepFilter.dtEstoqueFilter = dataLimite;
		deleteAllByExample(estoqueRepFilter);
		
	}

	
}