package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.StatusRecado;
import br.com.wmw.lavenderepda.business.domain.TipoRecado;
import totalcross.util.Vector;

public class StatusRecadoService extends CrudService {

    private static StatusRecadoService instance;

    private StatusRecadoService() {
        //--
    }

    public static StatusRecadoService getInstance() {
        if (instance == null) {
            instance = new StatusRecadoService();
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
    public Vector findAll(int cdTipoRecado) {
		Vector statusRecadoList = new Vector();
		//--
		if (cdTipoRecado == TipoRecado.TIPORECADO_CDCAIXA_DE_ENTRADA) {
			StatusRecado statusRecado = new StatusRecado();
			statusRecado.cdStatusRecado = StatusRecado.STATUSRECADO_CDNAOLIDO;
			statusRecado.dsStatusRecado = Messages.STATUSRECADO_DSNAOLIDO;
			statusRecadoList.addElement(statusRecado);
			//--
			statusRecado = new StatusRecado();
			statusRecado.cdStatusRecado = StatusRecado.STATUSRECADO_CDLIDO;
			statusRecado.dsStatusRecado = Messages.STATUSRECADO_DSLIDO;
			statusRecadoList.addElement(statusRecado);
		} else {
			StatusRecado statusRecado = new StatusRecado();
			statusRecado.cdStatusRecado = StatusRecado.STATUSRECADO_CDAENVIAR;
			statusRecado.dsStatusRecado = Messages.STATUSRECADO_DSAENVIAR;
			statusRecadoList.addElement(statusRecado);
			//--
			statusRecado = new StatusRecado();
			statusRecado.cdStatusRecado = StatusRecado.STATUSRECADO_CDTRANSMITIDO;
			statusRecado.dsStatusRecado = Messages.STATUSRECADO_DSENVIADO;
			statusRecadoList.addElement(statusRecado);
		}
		//--
		return statusRecadoList;
    }

}
