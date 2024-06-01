package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Feriado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FeriadoDbxDao;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class FeriadoService extends CrudService {

    private static FeriadoService instance;

    private FeriadoService() {
        //--
    }

    public static FeriadoService getInstance() {
        if (instance == null) {
            instance = new FeriadoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FeriadoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }

	public Vector findFeriadosPeriodo(Date dataAtual, Date dataVencimento) throws SQLException {
		Vector feriadoList = FeriadoDbxDao.getInstance().findFeriadosPeriodo(dataAtual, dataVencimento);
		return filtraPorFeriadosReferenteAoPeriodoSelecionado(feriadoList, dataAtual, dataVencimento);
	}
		
	private Vector filtraPorFeriadosReferenteAoPeriodoSelecionado(Vector feriadoList, Date dataAtual, Date dataVencimento) {
		Vector novoFeriadoList = new Vector();
		if (ValueUtil.isNotEmpty(feriadoList)) {
			for (int i = 0; i < feriadoList.size(); i++) {
				Feriado feriado = (Feriado) feriadoList.items[i];
				Date dataFeriado = new Date();
				int nuAno = feriado.nuAno != 0 ? feriado.nuAno : dataFeriado.getYear();
				try {
					dataFeriado.set(feriado.nuDia, feriado.nuMes, nuAno);
				} catch (InvalidDateException e) {
					dataFeriado = new Date();
				}
				if ((dataAtual.equals(dataFeriado) || dataVencimento.equals(dataFeriado)) || (dataFeriado.isAfter(dataVencimento) && dataFeriado.isBefore(dataAtual))) {
					novoFeriadoList.addElement(feriado);
				}
			}
		}
		return novoFeriadoList;
	}

	public boolean isFeriado(Date data) throws SQLException {
		if (LavenderePdaConfig.isPulaDataEntregaEmDiasDeFeriadoProxDia()) {
			return isDtFeriado(data);
		}
		return false;
	}

	public boolean isDtFeriado(Date data) throws SQLException {
		Feriado feriadoFilter = new Feriado();
		feriadoFilter.nuDia = data.getDay();
		feriadoFilter.nuMes = data.getMonth();
		Vector feriadoList = findAllFeriadosByExample(feriadoFilter);
		for (int i = 0; i < feriadoList.size(); i++) {
			Feriado feriado = (Feriado) feriadoList.items[i];
			if (feriado.nuAno != 0 && feriado.nuAno != data.getYear()) {
				feriadoList.removeElement(feriado);
				i--;
			}
		}
		return ValueUtil.isNotEmpty(feriadoList);
	}
	
	public Vector findAllFeriadosByExample(Feriado feriadoFilter) throws SQLException {
		Vector feriadoList = super.findAllByExample(feriadoFilter);
		Vector feriadoEmpresaList = new Vector();
		feriadoEmpresaList = FeriadoEmpresaService.getInstance().findAllByExampleFeriadoEmpresaAsFeriado(feriadoFilter);
		return VectorUtil.concatVectors(feriadoList, feriadoEmpresaList);
	}
	
	//@Override
	public int countByExample(BaseDomain domain) throws SQLException {
		Feriado feriado = (Feriado) domain;
		int countFeriadoEmpresa = FeriadoEmpresaService.getInstance().countByExampleFeriadoEmpresaByFeriado(feriado);
		return super.countByExample(feriado) + countFeriadoEmpresa;
	}

}