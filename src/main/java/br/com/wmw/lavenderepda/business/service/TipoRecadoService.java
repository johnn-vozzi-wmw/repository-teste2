package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.TipoRecado;
import totalcross.util.Vector;

public class TipoRecadoService extends CrudService {

    private static TipoRecadoService instance;

    private TipoRecadoService() {
        //--
    }

    public static TipoRecadoService getInstance() {
        if (instance == null) {
            instance = new TipoRecadoService();
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
		Vector tipoRecadoList = new Vector();
		//--
		TipoRecado tipoRecado = new TipoRecado();
		tipoRecado.cdTipoRecado = TipoRecado.TIPORECADO_CDCAIXA_DE_ENTRADA;
		tipoRecado.dsTipoRecado = Messages.TIPORECADO_DSCAIXA_DE_ENTRADA;
		tipoRecadoList.addElement(tipoRecado);
		//--
		tipoRecado = new TipoRecado();
		tipoRecado.cdTipoRecado = TipoRecado.TIPORECADO_CDCAIXA_DE_SAIDA;
		tipoRecado.dsTipoRecado = Messages.TIPORECADO_DSCAIXA_DE_SAIDA;
		tipoRecadoList.addElement(tipoRecado);
		//--
		tipoRecado = new TipoRecado();
		tipoRecado.cdTipoRecado = TipoRecado.TIPORECADO_CDITENS_ENVIADOS;
		tipoRecado.dsTipoRecado = Messages.TIPORECADO_DSITENS_ENVIADOS;
		tipoRecadoList.addElement(tipoRecado);
		//--
		return tipoRecadoList;
    }

}
