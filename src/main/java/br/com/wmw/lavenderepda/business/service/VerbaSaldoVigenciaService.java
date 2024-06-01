package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldoVigencia;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaSaldoVigenciaDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class VerbaSaldoVigenciaService extends CrudService {

    private static VerbaSaldoVigenciaService instance;
    
    private VerbaSaldoVigenciaService() {
        //--
    }
    
    public static VerbaSaldoVigenciaService getInstance() {
        if (instance == null) {
            instance = new VerbaSaldoVigenciaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return VerbaSaldoVigenciaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }
    
    public void insertVlSaldo(double vlVerbaPositiva, double vlVerba, Pedido pedido) throws SQLException {
    	Vector verbaList = getVerbaSaldoVigentes();
    	if (vlVerbaPositiva > 0) {
    		VerbaSaldoVigencia verbaAtual = getVerbaSaldoAtual(verbaList);
    		verbaAtual.vlSaldo += vlVerbaPositiva;
    		if (ValueUtil.isEmpty(verbaAtual.dtSaldo)) {
    			verbaAtual.dtSaldo = DateUtil.getCurrentDate();
    			insert(verbaAtual);
    			verbaList.addElement(verbaAtual);
    		} else {
    			verbaAtual.dtSaldo = DateUtil.getCurrentDate();
    			update(verbaAtual);
    		}
    	}
    	if (vlVerba < 0) {
    		consomeVerbaDisponivel(verbaList, vlVerba, pedido);
    	}
    }
    
    public Vector getVerbaSaldoVigentes() throws SQLException {
    	VerbaSaldoVigencia filter = getVerbaSaldoVigenciaFilter();
    	filter.sortAtributte = VerbaSaldoVigencia.NMCOLUNA_DTINICIOVIGENCIA;
    	filter.sortAsc = ValueUtil.VALOR_SIM;
    	return findAllByExample(filter);
    }
    
    private VerbaSaldoVigencia getVerbaSaldoAtual(Vector verbaList) {
    	VerbaSaldoVigencia verbaAtual = null;
    	int mesSaldo = DateUtil.getCurrentDate().getMonth();
    	if (ValueUtil.isNotEmpty(verbaList)) {
    		verbaAtual = (VerbaSaldoVigencia)verbaList.items[verbaList.size() - 1];
    		if (mesSaldo == verbaAtual.cdMesSaldo) return verbaAtual;
    	}
    	verbaAtual = new VerbaSaldoVigencia();
    	verbaAtual.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	verbaAtual.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	verbaAtual.cdMesSaldo = mesSaldo;
    	verbaAtual.dtInicioVigencia = DateUtil.getFirstDayOfMonth();
    	Date nextMonth = DateUtil.getCurrentDate();
    	nextMonth.advanceMonth();
    	verbaAtual.dtFimVigencia = DateUtil.getLastDayOfMonth(nextMonth);
    	return verbaAtual;
    }
    
    private void consomeVerbaDisponivel(Vector verbaList, double vlVerbaItem, Pedido pedido) throws SQLException {
    	VerbaSaldoVigencia verba;
    	if (ValueUtil.isEmpty(verbaList)) {
    		verba = getVerbaSaldoAtual(verbaList);
    		verba.vlSaldo += vlVerbaItem;
    		verba.dtSaldo = DateUtil.getCurrentDate();
    		insert(verba);
    		VerbaVigenciaPedidoService.getInstance().geraVerbaVigenciaPedido(pedido, vlVerbaItem, verba.cdMesSaldo);
    	}
    	int size = verbaList.size();
    	for (int i = 0; i < size; i++) {
    		verba = (VerbaSaldoVigencia) verbaList.items[i];
    		if (verba.vlSaldo + vlVerbaItem >= 0) {
    			verba.vlSaldo += vlVerbaItem;
    			verba.dtSaldo = DateUtil.getCurrentDate();
    			update(verba);
    			VerbaVigenciaPedidoService.getInstance().geraVerbaVigenciaPedido(pedido, vlVerbaItem, verba.cdMesSaldo);
    			break;
    		} else {
    			vlVerbaItem += verba.vlSaldo;
    			VerbaVigenciaPedidoService.getInstance().geraVerbaVigenciaPedido(pedido, -verba.vlSaldo, verba.cdMesSaldo);
    			verba.vlSaldo = 0d;
    			verba.dtSaldo = DateUtil.getCurrentDate();
    			update(verba);
    		}
    	}
    	
    }
    
    public void deleteVerbaSaldoForaVigencia() throws SQLException {
    	VerbaSaldoVigenciaDbxDao.getInstance().deleteVerbaSaldoForaVigencia();
    }
    
    public double getVlSaldo() throws SQLException {
    	VerbaSaldoVigencia filter = getVerbaSaldoVigenciaFilter();
    	return sumByExample(filter, VerbaSaldoVigencia.NMCOLUNA_VLSALDO);
    }

	private VerbaSaldoVigencia getVerbaSaldoVigenciaFilter() {
		VerbaSaldoVigencia filter = new VerbaSaldoVigencia();
    	filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	filter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	filter.dtVigenciaFilter = DateUtil.getCurrentDate();
		return filter;
	}
    
    public void recalculateAndUpdateVerbaPda() throws SQLException {
    	Pedido pedidoFilter = new Pedido();
    	pedidoFilter.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoFechado;
    	Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
    	int size = ValueUtil.isNotEmpty(pedidoList) ? pedidoList.size() : 0;
    	Pedido pedido;
    	for (int i = 0; i < size; i++) {
    		pedido = (Pedido) pedidoList.items[i];
    		insertVlSaldo(pedido.vlVerbaPedidoPositivo, pedido.vlVerbaPedido, pedido);
    	}
    }
    
    public double getVlFinalVerba(Pedido pedido, Vector verbaList) {
    	double vlFinal = pedido.vlVerbaPedidoPositivo + pedido.vlVerbaPedido;
    	if (ValueUtil.isNotEmpty(verbaList)) {
    		int size = verbaList.size();
    		for (int i = 0; i < size; i++) {
    			vlFinal += ((VerbaSaldoVigencia)verbaList.items[i]).vlSaldo;
    		}
    	}
    	return vlFinal;
    }
    
}