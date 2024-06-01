package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ListaLeadsDao;

public class ListaLeadsService extends CrudService {

    private static ListaLeadsService instance;
    
    private ListaLeadsService() {
        //--
    }
    
    public static ListaLeadsService getInstance() {
        if (instance == null) {
            instance = new ListaLeadsService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ListaLeadsDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		
	}
	
	public GpsData getCoordenadaAtual(boolean showLoading) {
		LoadingBoxWindow msg = showLoading ? UiUtil.createProcessingMessage(Messages.CAD_COORD_COLETANDO) : null;
		if (showLoading) {
			msg.popupNonBlocking();
		}
		try {
			GpsData gpsData = GpsService.getInstance().forceReadData();
			if (gpsData.isGpsOff()) {
				UiUtil.showWarnMessage(Messages.CAD_COORD_GPS_DESLIGADO);
				return null;
			} else if (gpsData.isSuccess()) {
				return gpsData;
			} else {
				UiUtil.showWarnMessage(Messages.CAD_COORD_COLETA_ERRO);
			}
		} catch (Throwable ex) {
			//Apenas nao realiza a coleta
		} finally {
			if (msg != null) {
				msg.unpop();
			}
		}
		return null;
	}
	
}