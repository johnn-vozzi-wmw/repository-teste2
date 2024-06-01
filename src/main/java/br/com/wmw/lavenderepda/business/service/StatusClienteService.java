package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipo;
import br.com.wmw.lavenderepda.business.domain.StatusCliente;
import totalcross.util.Vector;


public class StatusClienteService extends CrudService {

    private static StatusClienteService instance;
    
    private StatusClienteService() {
        //--
    }
    
    public static StatusClienteService getInstance() {
        if (instance == null) {
            instance = new StatusClienteService();
        }
        return instance;
    }
    
    //@Override
    protected CrudDao getCrudDao() {
    	return null;
    }
    
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    //@Override
    public Vector findAll() throws java.sql.SQLException {
		Vector statusClienteList = new Vector();
		if (LavenderePdaConfig.usaStatusClienteVinculado) {
			addOnlyStatusClienteVinculado(statusClienteList, null);
		} else {
			addAllStatusCliente(statusClienteList);
		}
	    return statusClienteList;
    }

	private void addOnlyStatusClienteVinculado(Vector statusClienteList, String cdRepresentanteFilter) throws SQLException {
		Vector flStatusClientes;
		if (LavenderePdaConfig.usarFlStatusClienteDaFichaFinanceira) {
			flStatusClientes = FichaFinanceiraService.getInstance().findDistinctStatusCliente(cdRepresentanteFilter);
	    } else {
		    flStatusClientes = ClienteService.getInstance().findDistinctStatusCliente(cdRepresentanteFilter);
	    }
		if (ValueUtil.isNotEmpty(flStatusClientes)) {
			int size = flStatusClientes.size();
			for (int i = 0; i < size; i++) {
				String flStatus = (String) flStatusClientes.items[i];
				StatusCliente statusCliente = new StatusCliente();
				if (flStatus == null) continue;
				switch (flStatus) {
					case StatusCliente.STATUSCLIENTE_CDBLOQUEADO:
						statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDBLOQUEADO;
						statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSBLOQUEADO;
						statusClienteList.addElement(statusCliente);
						break;
					case StatusCliente.STATUSCLIENTE_CDSEMTITULO:
						statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDSEMTITULO;
						statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSSEMTITULO;
						statusClienteList.addElement(statusCliente);
						break;
					case StatusCliente.STATUSCLIENTE_CDATRASADO:
						statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDATRASADO;
						statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSATRASADO;
						statusClienteList.addElement(statusCliente);
						break;
					case StatusCliente.STATUSCLIENTE_CDCOMTITULOS:
						statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDCOMTITULOS;
						statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSCOMTITULOS;
						statusClienteList.addElement(statusCliente);
						break;
					case StatusCliente.STATUSCLIENTE_CDINATIVOS:
						statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDINATIVOS;
						statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSINATIVOS;
						statusClienteList.addElement(statusCliente);
						break;
					case StatusCliente.STATUSCLIENTE_CDEMAVALIACAO:
						statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDEMAVALIACAO;
						statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSEMAVALIACAO;
						statusClienteList.addElement(statusCliente);
						break;
					case StatusCliente.STATUSCLIENTE_CDBLOQUEADOPORATRASO:
						statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDBLOQUEADOPORATRASO;
						statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSBLOQUEADOPORATRASO;
						statusClienteList.addElement(statusCliente);
						break;
				}
			}
		}
	}

	private void addAllStatusCliente(Vector statusClienteList) {
		StatusCliente statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDBLOQUEADO;
		statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSBLOQUEADO;
		statusClienteList.addElement(statusCliente);
		statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDSEMTITULO;
		statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSSEMTITULO;
		statusClienteList.addElement(statusCliente);
		statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDATRASADO;
		statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSATRASADO;
		statusClienteList.addElement(statusCliente);
		statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDCOMTITULOS;
		statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSCOMTITULOS;
		statusClienteList.addElement(statusCliente);
		statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDINATIVOS;
		statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSINATIVOS;
		statusClienteList.addElement(statusCliente);
		statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDEMAVALIACAO;
		statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSEMAVALIACAO;
		statusClienteList.addElement(statusCliente);
		statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = StatusCliente.STATUSCLIENTE_CDBLOQUEADOPORATRASO;
		statusCliente.dsStatusCliente = Messages.STATUSCLIENTE_DSBLOQUEADOPORATRASO;
		statusClienteList.addElement(statusCliente);
	}

	public Vector findAllByFlFiltroStatusCliente(String flFiltroStatusCliente, String dsStatusClienteList) throws SQLException {
		Vector statusClienteList = findAll();
		if (ValueUtil.valueEquals(RequisicaoServTipo.FLFILTRO_STATUS_CLIENTE_RESTRITO, flFiltroStatusCliente)) {
			  filterStatusClienteRestricao(statusClienteList, dsStatusClienteList);
		} else if (ValueUtil.valueEquals(RequisicaoServTipo.FLFILTRO_STATUS_CLIENTE_EXCECAO, flFiltroStatusCliente)) {
			  filterStatusClienteExcecao(statusClienteList, dsStatusClienteList);
		}
		return statusClienteList;
	}

	private void filterStatusClienteExcecao(Vector statusClienteList,String dsStatusClienteList) {
		int index = 0;
		while (index < statusClienteList.size()) {
			if (dsStatusClienteList.contains(((StatusCliente)statusClienteList.items[index]).cdStatusCliente)) {
				statusClienteList.removeElement(statusClienteList.items[index]);
			} else {
				index++;
			}
		}
	}

	private void filterStatusClienteRestricao(Vector statusClienteList, String dsStatusClienteList) {
		int index = 0;
		while (index < statusClienteList.size()) {
			if (!dsStatusClienteList.contains(((StatusCliente)statusClienteList.items[index]).cdStatusCliente)) {
				statusClienteList.removeElement(statusClienteList.items[index]);
			} else {
				index++;
			}
		}
	}

	public Vector findAllByCdRepresentante(String cdRepresentante) throws SQLException {
    	Vector statusClienteList = new Vector();
		addOnlyStatusClienteVinculado(statusClienteList, cdRepresentante);
		return statusClienteList;
	}
}
