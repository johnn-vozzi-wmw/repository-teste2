package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DecimalFormat;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Indicador;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ValorIndicadorPdbxDao;
import totalcross.util.Vector;

public class ValorIndicadorService extends CrudService {

    private static ValorIndicadorService instance;

    private ValorIndicadorService() {
        //--
    }

    public static ValorIndicadorService getInstance() {
        if (instance == null) {
            instance = new ValorIndicadorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ValorIndicadorPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector getValorIndicadorListByRep(String dsPeriodo, String cdRep) throws SQLException {
    	ValorIndicador valorIndicadorFilter = new ValorIndicador(SessionLavenderePda.cdEmpresa, cdRep, null, dsPeriodo);
    	return ValorIndicadorPdbxDao.getInstance().findAllValorIndicador(valorIndicadorFilter);
    }

    public Vector getValorIndicadorList(String dsPeriodo, String cdIndicador) throws SQLException {
    	ValorIndicador valorIndicadorFilter = new ValorIndicador(SessionLavenderePda.cdEmpresa, null, cdIndicador, dsPeriodo);
    	return ValorIndicadorPdbxDao.getInstance().findAllValorIndicador(valorIndicadorFilter);
    }

    public void addTicketMedioList(Vector list, String cdRep, String cdIndicador, String periodo, boolean supervisor) throws SQLException {
    	if (!ValorIndicador.PERIODO_TICKET_MEDIO.equalsIgnoreCase(periodo)) {
    		return;
    	}
    	//Ticket Médio da Empresa
    	if ((LavenderePdaConfig.ticketMedioEmpresa >= 0)
    			&& (ValueUtil.isEmpty(cdIndicador) || (Indicador.TICKET_MEDIO_EMPRESA == ValueUtil.getIntegerValue(cdIndicador)))) {
    		ValorIndicador ticketMedioEmpresa = new ValorIndicador();
    		ticketMedioEmpresa.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		ticketMedioEmpresa.cdIndicador = StringUtil.getStringValue(Indicador.TICKET_MEDIO_EMPRESA);
    		ticketMedioEmpresa.cdRepresentante = cdRep;
    		ticketMedioEmpresa.dsPeriodo = ValorIndicador.PERIODO_TICKET_MEDIO;
    		ticketMedioEmpresa.dsVlIndicador = StringUtil.getStringValueToInterface(LavenderePdaConfig.ticketMedioEmpresa);
    		ticketMedioEmpresa.rowKey = ticketMedioEmpresa.getRowKey();
        	list.addElement(ticketMedioEmpresa);
    	}
    	//Ticket Médio Diário
    	if (!supervisor && LavenderePdaConfig.geraApresentaTicketMedioDiario
    			&& (ValueUtil.isEmpty(cdIndicador) || (Indicador.TICKET_MEDIO_DIARIO == ValueUtil.getIntegerValue(cdIndicador)))) {
        	cdRep = (cdRep == null ? SessionLavenderePda.getRepresentante().cdRepresentante : cdRep);
    		ValorIndicador ticketMedioDiario = new ValorIndicador();
    		ticketMedioDiario.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		ticketMedioDiario.cdIndicador = StringUtil.getStringValue(Indicador.TICKET_MEDIO_DIARIO);
    		ticketMedioDiario.cdRepresentante = cdRep;
    		ticketMedioDiario.dsPeriodo = ValorIndicador.PERIODO_TICKET_MEDIO;
    		ticketMedioDiario.dsVlIndicador = StringUtil.getStringValueToInterface(ItemPedidoService.getInstance().getTicketMedioDiarioRep(cdRep));
    		ticketMedioDiario.rowKey = ticketMedioDiario.getRowKey();
        	list.addElement(ticketMedioDiario);
    	}
    }
	
	public String applyMaskOnDsVlIndicador(String value, String mask) {
		if (ValueUtil.isEmpty(value) || ValueUtil.isEmpty(mask)) return value;

		try {
			return DecimalFormat.applyPattern(mask, value);
		} catch (Throwable e) {
			return value;
		}
	}
	
}