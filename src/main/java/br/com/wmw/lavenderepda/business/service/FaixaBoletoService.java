package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.BoletoConfig;
import br.com.wmw.lavenderepda.business.domain.FaixaBoleto;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FaixaBoletoDbxDao;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

public class FaixaBoletoService extends CrudService {

    private static FaixaBoletoService instance = null;
    
    private FaixaBoletoService() {
        //--
    }
    
    public static FaixaBoletoService getInstance() {
        if (instance == null) {
            instance = new FaixaBoletoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FaixaBoletoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public BigDecimal getNextNuDocumentoFaixa(Pedido pedido, String cdBoletoConfig) throws SQLException {
		FaixaBoleto faixaBoletoFilter = getFaixaBoletoFilter(pedido.cdEmpresa, pedido.cdRepresentante, cdBoletoConfig);
		Vector faixaBoletoList = findAllByExample(faixaBoletoFilter);
		int size = faixaBoletoList.size();
		for (int i = 0; i < size; i++) {
			FaixaBoleto faixaBoleto = (FaixaBoleto) faixaBoletoList.items[i];
			if (faixaBoleto.nuUltimoBoleto == null || faixaBoleto.nuUltimoBoleto.compareTo(BigDecimal.ZERO) == 0) {
				return faixaBoleto.nuBoletoInicio;
			}
			if (faixaBoleto.nuUltimoBoleto.compareTo(faixaBoleto.nuBoletoFim) == -1) {
				return faixaBoleto.nuUltimoBoleto.add(new BigDecimal(1));
			}
		}
		showErrorMessageFaixaBoleto(cdBoletoConfig);
		return null;
	}

	private FaixaBoleto getFaixaBoletoFilter(String cdEmpresa, String cdRepresentante, String cdBoletoConfig) {
		FaixaBoleto faixaBoletoFilter = new FaixaBoleto();
		faixaBoletoFilter.cdEmpresa = cdEmpresa;
		faixaBoletoFilter.cdRepresentante = cdRepresentante;
		faixaBoletoFilter.cdBoletoConfig = cdBoletoConfig;
		faixaBoletoFilter.sortAtributte = FaixaBoleto.NM_COLUNA_NUBOLETOINICIO;
		faixaBoletoFilter.sortAsc = ValueUtil.VALOR_SIM;
		return faixaBoletoFilter;
	}

	public BigDecimal getNextNuDocumento(String cdEmpresa, String cdRepresentante, String cdBoletoConfig, BigDecimal nuDocumento) throws SQLException {
		BigDecimal nuDocTotal = nuDocumento.add(new BigDecimal(1));
		FaixaBoleto faixaBoletoFilter = getFaixaBoletoFilter(cdEmpresa, cdRepresentante, cdBoletoConfig);
		Vector faixaBoletoList = findAllByExample(faixaBoletoFilter);
		int size = faixaBoletoList.size();
		for (int i = 0; i < size; i++) {
			FaixaBoleto faixaBoleto = (FaixaBoleto) faixaBoletoList.items[i];
			if (faixaBoleto.nuBoletoFim.compareTo(nuDocTotal) != -1) {
				if (nuDocTotal.compareTo(faixaBoleto.nuBoletoInicio) != 1) {
					return faixaBoleto.nuBoletoInicio;
				}
				return nuDocTotal;
			}
			if (i == size - 1) {
				showErrorMessageFaixaBoleto(cdBoletoConfig);
			}
		}
		return null;
	}

	private void showErrorMessageFaixaBoleto(String cdBoletoConfig) throws SQLException {
		String nmBanco = BoletoConfigService.getInstance().findColumnByRowKey(getBoletoConfigFilter(cdBoletoConfig).getRowKey(), BoletoConfig.NMBANCO_COLUMN_NAME);
		if (ValueUtil.isNotEmpty(nmBanco)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.BOLETO_OFFLINE_VALIDACAO_NUDOCUMENTO, new String[] {nmBanco, cdBoletoConfig}));
		} else {
			throw new ValidationException(MessageUtil.getMessage(Messages.BOLETO_OFFLINE_VALIDACAO_NUDOCUMENTO_NMBANCO_NULL, new String[] {cdBoletoConfig}));
		}
	}
	
	private BoletoConfig getBoletoConfigFilter(String cdBoletoConfig) {
		BoletoConfig boletoConfigFilter = new BoletoConfig();
		boletoConfigFilter.cdBoletoConfig = cdBoletoConfig;
		return boletoConfigFilter;
	}
	
	public void updateLastFaixaBoleto(FaixaBoleto faixaBoletoFilter) {
		FaixaBoletoDbxDao.getInstance().updateLastFaixaBoleto(faixaBoletoFilter);
	}

}