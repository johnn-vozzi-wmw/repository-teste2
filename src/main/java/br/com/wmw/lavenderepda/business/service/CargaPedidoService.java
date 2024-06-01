package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RotaEntregaCli;
import br.com.wmw.lavenderepda.business.domain.StatusCargaPedido;
import br.com.wmw.lavenderepda.business.validation.CargaPedidoPesoMinimoException;
import br.com.wmw.lavenderepda.business.validation.CargaPedidoValidadeException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CargaPedidoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CargaPedidoService extends CrudService {

    private static CargaPedidoService instance;
    
    public static int validationFechamentoCount = 0;
    
    private CargaPedidoService() {
        //--
    }
    
    public static CargaPedidoService getInstance() {
        if (instance == null) {
            instance = new CargaPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CargaPedidoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
        CargaPedido cargaPedido = (CargaPedido) domain;
        //dsCargaPedido
        if (ValueUtil.isEmpty(cargaPedido.dsCargaPedido)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPEDIDO_DESCRICAO);
        }
        //cdRotaEntrega
        if (ValueUtil.isEmpty(cargaPedido.cdRotaEntrega)) {
        	throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPEDIDO_ROTA);
        }
        //dtEntrega
        if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()) { 
        	if (ValueUtil.isEmpty(cargaPedido.dtEntrega) && LavenderePdaConfig.isObrigaUsaControleDataEntregaPedidoPelaCarga()) {
        		throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CARGAPEDIDO_DATA_ENTREGA);
        	}
        	if (ValueUtil.isNotEmpty(cargaPedido.dtEntrega) && cargaPedido.dtEntrega.isBefore(DateUtil.getCurrentDate())) {
				throw new ValidationException(Messages.PEDIDO_DTENTREGA_ANTERIOR_PERMITIDO);
			}
        } 
    }
    
    private void validaDataEntregaPedidosRelacionadosCarga(CargaPedido cargaPedido) throws SQLException {
    	if (LavenderePdaConfig.isUsaControleDataEntregaPedidoPelaCarga()) {
	    	Vector pedidoList = findAllPedidoByCarga(cargaPedido);
	    	for (int i = 0; i < pedidoList.size(); i++) {
	    		try {
					PedidoService.getInstance().validateDataEntrega((Pedido) pedidoList.items[i], false);
				} catch (Throwable e) {
					throw new ValidationException(Messages.CARGAPEDIDO_MSG_DATA_ENTREGA);
				}
			}
    	}
	}

	public void validaFecharCarga(CargaPedido cargaPedido) throws SQLException {
		Vector pedidoList = findAllPedidoByCarga(cargaPedido);
		double qtPesoCarga = getPesoCarga(pedidoList);
		switch (validationFechamentoCount) {
			case 0:
				validationFechamentoCount++;
				if (ValueUtil.isEmpty(pedidoList)) {
					validationFechamentoCount--;
					throw new ValidationException(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_NENHUM_PEDIDO);
				}
			case 1:
				validationFechamentoCount++;
				if (isCargaRelacionadaPedidoAberto(pedidoList)) {
					validationFechamentoCount--;
					throw new ValidationException(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_PEDIDO_ABERTO);
				} 
			case 2:
				validationFechamentoCount++;
				if (validaDataValidadeCarga(cargaPedido)) {
					if (LavenderePdaConfig.sementeSenhaFecharCargaVencida != 0) {
						throw new CargaPedidoValidadeException(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_VALIDADE);
					}
					throw new ValidationException(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_VALIDADE);
				}
			case 3:
				validationFechamentoCount++;
				if (LavenderePdaConfig.isValidaPesoMaximoCargaPedido() && qtPesoCarga > LavenderePdaConfig.qtdPesoMaximoCargaPedido) {
					validationFechamentoCount--;
					throw new ValidationException(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_PESO_MAXIMO);
				}
			case 4:
				validationFechamentoCount++;
				if (LavenderePdaConfig.isValidaPesoMinimoCargaPedido() && !cargaPedido.isLiberadoPesoMin() && qtPesoCarga < LavenderePdaConfig.qtdPesoMinimoCargaPedido) {
					if (LavenderePdaConfig.sementeSenhaFecharCargaPesoMenorMinimo != 0) {
						throw new CargaPedidoPesoMinimoException(MessageUtil.getMessage(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_PESO_MINIMO, LavenderePdaConfig.qtdPesoMinimoCargaPedido));
					}
					throw new ValidationException(Messages.CARGAPEDIDO_VALIDACAO_FECHAR_PESO_MINIMO);
				}
			default:
				validationFechamentoCount = 0;
		}
	}
	
	public boolean validaDataValidadeCarga(CargaPedido cargaPedido) throws SQLException {
		if (cargaPedido.isLiberadoValidade() || cargaPedido.cdCargaPedido == null) {
			return false;
		}
		Date dtPedidoMaisAntigo = getDtOldestPedidoCargaPedido(cargaPedido);
		if (ValueUtil.isNotEmpty(dtPedidoMaisAntigo)) {
			Date dtVencimentoCarga = DateUtil.getCurrentDate(); 
			DateUtil.decDay(dtVencimentoCarga, LavenderePdaConfig.nuDiasValidadeCargaPedido);
			if (dtVencimentoCarga.isAfter(dtPedidoMaisAntigo)) {
				return true;
			}
		}
		return false;
	}

	private double getPesoCarga(Vector pedidoList) {
		double qtPesoCarga = 0;
		if (LavenderePdaConfig.isValidaPesoMaximoCargaPedido() || LavenderePdaConfig.isValidaPesoMinimoCargaPedido()) {
			int size = pedidoList.size();
			for (int i = 0; i < size; i++) {
				Pedido pedido = (Pedido) pedidoList.items[i];
				qtPesoCarga += pedido.qtPeso;
			}
		}
		return qtPesoCarga;
	}


	public Vector findAllPedidoByCarga(CargaPedido cargaPedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = cargaPedido.cdEmpresa;
		pedidoFilter.cdRepresentante = cargaPedido.cdRepresentante;
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		pedidoFilter.cdCargaPedido = cargaPedido.cdCargaPedido;
		return PedidoService.getInstance().findAllByExampleSummary(pedidoFilter);
	}

	
	private boolean isCargaRelacionadaPedidoAberto(Vector pedidoList) {
		int size = pedidoList.size();
		for (int i = 0; i < size; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (pedido.isPedidoAberto()) {
				return true;
			}
		}
		return false;
	}

	private boolean isCargaRelacionadaPedido(CargaPedido cargaPedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = cargaPedido.cdEmpresa;
		pedidoFilter.cdRepresentante = cargaPedido.cdRepresentante;
		pedidoFilter.flOrigemPedido = OrigemPedido.FLORIGEMPEDIDO_PDA;
		pedidoFilter.cdCargaPedido = cargaPedido.cdCargaPedido;
		return PedidoService.getInstance().countByExample(pedidoFilter) != 0;
	}

	public void validaTrocarRota(CargaPedido cargaPedido) throws SQLException {
		if (isCargaRelacionadaPedido(cargaPedido)) {
			throw new ValidationException(Messages.CARGAPEDIDO_VALIDACAO_TROCAR_ROTA);
		}
	}
	
	public String fecharCargasPedido(Vector cargaPedidoList) {
		if (ValueUtil.isEmpty(cargaPedidoList)) {
			return "";
		}
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < cargaPedidoList.size(); i++) {
			CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
			try {
				fecharCarga(cargaPedido);
			} catch (Throwable ex) {
				strBuffer.append(cargaPedido.rowKey);
				strBuffer.append("*");
				strBuffer.append(cargaPedido.cdCargaPedido);
				strBuffer.append("*");
				strBuffer.append(ex.toString());
				strBuffer.append("*");
				strBuffer.append(ex.getMessage());
				strBuffer.append("&");
			}
		}
		return strBuffer.toString();
	}
	

	public void fecharCarga(CargaPedido cargaPedido) throws SQLException {
		validationFechamentoCount = 0;
		validaFecharCarga(cargaPedido);
		cargaPedido.dtFechamento = DateUtil.getCurrentDate();
		cargaPedido.hrFechamento = TimeUtil.getCurrentTimeHHMM();
		cargaPedido.flCargaFechada = ValueUtil.VALOR_SIM;
		update(cargaPedido, true);
	}
	
	@Override
	public void delete(BaseDomain domain) throws java.sql.SQLException {
		validateDelete((CargaPedido) domain);
		super.delete(domain);
	}

	private void validateDelete(CargaPedido cargaPedido) throws SQLException {
		if (isCargaRelacionadaPedido(cargaPedido)) {
			throw new ValidationException(Messages.CARGAPEDIDO_VALIDACAO_DELETAR_CARGA);
		}
	}

	public Vector findAllCombo(String cdCliente) throws SQLException {
		Vector cargaPedidoComboList = new Vector();
		Vector cargaPedidoList = getAllCargaPedido();
		if (ValueUtil.isNotEmpty(cargaPedidoList)) {
			RotaEntregaCli rotaEntregaCliFilter = new RotaEntregaCli();
			rotaEntregaCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			rotaEntregaCliFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RotaEntregaCli.class);
			rotaEntregaCliFilter.cdCliente = cdCliente;
			Vector rotaEntregaCliList = RotaEntregaCliService.getInstance().findAllByExample(rotaEntregaCliFilter);
			if (ValueUtil.isNotEmpty(rotaEntregaCliList)) {
				int sizeCargaPedidoList = cargaPedidoList.size();
				cargaPedidoFor : for (int i = 0; i < sizeCargaPedidoList; i++) {
					CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
					int sizeRotaEntrList = rotaEntregaCliList.size();
					for (int j = 0; j < sizeRotaEntrList; j++) {
						RotaEntregaCli rotaEntregaCli = (RotaEntregaCli) rotaEntregaCliList.items[j];
						if (ValueUtil.valueEquals(cargaPedido.cdRotaEntrega, rotaEntregaCli.cdRotaEntrega)) {
							cargaPedidoComboList.addElement(cargaPedido);
							continue cargaPedidoFor;
						}
					}
				}
			}
		}
		return cargaPedidoComboList;
	}

	public CargaPedido getCargaPedido(String cdCargaPedido) throws SQLException {
		CargaPedido cargaPedidoFilter = new CargaPedido();
		cargaPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cargaPedidoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cargaPedidoFilter.cdCargaPedido = cdCargaPedido;
		cargaPedidoFilter = (CargaPedido) findByRowKey(cargaPedidoFilter.getRowKey());
		if (cargaPedidoFilter != null) {
			return cargaPedidoFilter;
		}
		return new CargaPedido();
	}
	
	public void deleteCargasPedidoAntigas() throws SQLException {
		CargaPedido cargaPedidoFilter = new CargaPedido();
		cargaPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cargaPedidoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cargaPedidoFilter.dtLimiteExclusao = DateUtil.getCurrentDate();
		DateUtil.decDay(cargaPedidoFilter.dtLimiteExclusao, LavenderePdaConfig.nuDiasPermanenciaCargaPedido);
		cargaPedidoFilter.flCargaFechada = ValueUtil.VALOR_SIM;
		cargaPedidoFilter.forceFlTipoAlteracao = true;
		cargaPedidoFilter.flTipoAlteracao = CargaPedido.FLTIPOALTERACAO_ORIGINAL;
		deleteAllByExample(cargaPedidoFilter);
	}
	
	//@Override
	public void insert(BaseDomain domain) throws SQLException {
		validate(domain);
		validateDuplicated(domain);
		getCrudDao().insert(domain);
	}
	
	//@Override
	public void update(BaseDomain domain) throws SQLException {
		validaDataEntregaPedidosRelacionadosCarga((CargaPedido)domain);
		super.update(domain, false);
	}

	public Date getDtOldestPedidoCargaPedido(CargaPedido cargaPedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdCargaPedido = cargaPedido.cdCargaPedido;
		Date dtOldestPedido = PedidoPdbxDao.getInstance().findDtOldestPedidoCargaPedido(pedidoFilter);
		return ValueUtil.isNotEmpty(dtOldestPedido) ? dtOldestPedido : null;
	}
	
	public boolean isNecessarioValidarValidadeDasCargas() throws SQLException {
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			Vector cargaPedidoList = getAllCargaPedido();
			int size = cargaPedidoList.size();
			for (int i = 0; i < size; i++) {
				CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
				if (!cargaPedido.isEnviadoServidor()) {
					Date dtOldestPedido = getDtOldestPedidoCargaPedido(cargaPedido);
					if (ValueUtil.isNotEmpty(dtOldestPedido)) {
						DateUtil.addDay(dtOldestPedido, LavenderePdaConfig.nuDiasValidadeCargaPedido);
						if (dtOldestPedido.isBefore(DateUtil.getCurrentDate())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private Vector getAllCargaPedido() throws SQLException {
		CargaPedido cargaPedido = new CargaPedido();
		cargaPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cargaPedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		return findAllByExample(cargaPedido);
	}

	public boolean avisaValidadeCarga() throws SQLException {
		Vector cargaPedidoList = getAllCargaPedido();
		int size = cargaPedidoList.size();
		for (int i = 0; i < size; i++) {
			CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
			if (!cargaPedido.isEnviadoServidor() && isCargaRelacionadaPedido(cargaPedido)) {
				Date dtOldestPedido = getDtOldestPedidoCargaPedido(cargaPedido);
				if (ValueUtil.isEmpty(dtOldestPedido)) {
					continue;
				}
				DateUtil.addDay(dtOldestPedido, LavenderePdaConfig.nuDiasValidadeCargaPedido);
				if (dtOldestPedido.isBefore(DateUtil.getCurrentDate())) {
					continue;
				}
				DateUtil.decDay(dtOldestPedido, LavenderePdaConfig.nuDiasRestantesAvisoVencimentoCarga);
				if (dtOldestPedido.isBefore(DateUtil.getCurrentDate())) {
					return true;
				}
			}
		}
		return false;
	}

	public void validaCargaEnvioPedido(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			if (LavenderePdaConfig.isObrigaRelacionamentoEntreCargaEPedido() && ValueUtil.isEmpty(pedido.getCargaPedido().cdEmpresa)) {
				throw new ValidationException(Messages.CARGAPEDIDO_PEDIDO_SEM_CARGA);
			} 
			if (ValueUtil.isNotEmpty(pedido.getCargaPedido().cdEmpresa) && !pedido.getCargaPedido().isCargaFechada()) {
				throw new ValidationException(Messages.CARGAPEDIDO_ABERTA);
			}
		}
	}
	
	public String getDsStatusCarga(CargaPedido cargaPedido) throws SQLException {
		if (cargaPedido.isEnviadoServidor()) {
			return StatusCargaPedido.STATUS_CARGAPEDIDO_DSTRANSMITIDO;
		} 
		if (CargaPedidoService.getInstance().validaDataValidadeCarga(cargaPedido)) {
			return StatusCargaPedido.STATUS_CARGAPEDIDO_DSVENCIDO;
		}
		if (cargaPedido.isCargaFechada()) {
			return StatusCargaPedido.STATUS_CARGAPEDIDO_DSFECHADO;
		}
	   	return StatusCargaPedido.STATUS_CARGAPEDIDO_DSABERTO;
	}
	
	public Vector findAllCargasPedidoVencidas() throws SQLException {
		CargaPedido cargaPedidoFilter = new CargaPedido();
	    cargaPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    cargaPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
	    Vector cargaPedidoList = CargaPedidoService.getInstance().findAllByExample(cargaPedidoFilter);
	    for (int i = 0; i < cargaPedidoList.size(); i++) {
	    	CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
	    	if (cargaPedido.isEnviadoServidor() || !validaDataValidadeCarga(cargaPedido)) { 
	    		cargaPedidoList.removeElement(cargaPedido);
	    		i--;
			}
		}
	    return cargaPedidoList;
	}
	
	public Vector findAllCargasPedidoProximoVencimento() throws SQLException {
		CargaPedido cargaPedidoFilter = new CargaPedido();
	    cargaPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    cargaPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
	    Vector cargaPedidoList = CargaPedidoService.getInstance().findAllByExample(cargaPedidoFilter);
	    for (int i = 0; i < cargaPedidoList.size(); i++) {
	    	CargaPedido cargaPedido = (CargaPedido) cargaPedidoList.items[i];
	    	if (!cargaPedido.isEnviadoServidor()) {
	    		Date dtOldestPedido = CargaPedidoService.getInstance().getDtOldestPedidoCargaPedido(cargaPedido);
	    		if (ValueUtil.isEmpty(dtOldestPedido)) {
	    			cargaPedidoList.removeElement(cargaPedido);
	    			i--;
	    			continue;
	    		}
	    		DateUtil.addDay(dtOldestPedido, LavenderePdaConfig.nuDiasValidadeCargaPedido);
	    		if (dtOldestPedido.isBefore(DateUtil.getCurrentDate())) {
	    			cargaPedidoList.removeElement(cargaPedido);
	    			i--;
					continue;
				}
	    		DateUtil.decDay(dtOldestPedido, LavenderePdaConfig.nuDiasRestantesAvisoVencimentoCarga);
	    		if (!dtOldestPedido.isBefore(DateUtil.getCurrentDate())) {
	    			cargaPedidoList.removeElement(cargaPedido);
	    			i--;
					continue;
	    		}
			}
		}
	    return cargaPedidoList;
	}

}