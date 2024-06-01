package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaFotoDao;
import totalcross.util.Vector;

public class VisitaFotoService extends CrudService {

    private static VisitaFotoService instance;

    private VisitaFotoService() {
        //--
    }

    public static VisitaFotoService getInstance() {
        if (instance == null) {
            instance = new VisitaFotoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VisitaFotoDao.getInstance();
    }

	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public Vector findAllVisitaFotoByVisita(Visita visita) throws SQLException {
    	VisitaFoto visitaFotoFilter = new VisitaFoto();
    	visitaFotoFilter.cdEmpresa = visita.cdEmpresa;
    	visitaFotoFilter.cdRepresentante = visita.cdRepresentante;
    	visitaFotoFilter.flOrigemVisita = visita.flOrigemVisita;
    	visitaFotoFilter.cdVisita = visita.cdVisita;
    	return findAllByExample(visitaFotoFilter);
    }

	public void insertOrUpdateVisitaFotoList(Visita visita) throws SQLException {
		Vector visitaFotoList = visita.getVisitaFotoList();
		deleteArquivoFotosFisicoInserirRegistro(visita.getVisitaFotoExcluidasList());
		deleteAllVisitaFotoByExample(visita.getVisitaFotoExcluidasList());
		visita.setVisitaFotoExcluidasList(new Vector());
		Vector imgServidorList = new Vector();
		for (int i = 0; i < visitaFotoList.size(); i++) {
			VisitaFoto visitaFotoFilter = (VisitaFoto) visitaFotoList.items[i];
			if (findByRowKey(visitaFotoFilter.getRowKey()) == null) {
				visitaFotoFilter.cdFoto = i + 1;
				visitaFotoFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
				if (!visitaFotoFilter.isEnviadoServidor()) {
					visitaFotoFilter.flTipoAlteracao = VisitaFoto.FLTIPOALTERACAO_INSERIDO;
				}
				getCrudDao().insert(visitaFotoFilter);
				imgServidorList.addElement(visitaFotoFilter);
			}
		}
	}
	

	public void deleteAllVisitaFotoByExample(Vector visitaFotoDeletedList) throws SQLException {
		int size = visitaFotoDeletedList.size();
		for (int i = 0; i < size; i++) {
			try {
				VisitaFoto visitaFotoFilter = (VisitaFoto) visitaFotoDeletedList.items[i];
				delete(visitaFotoFilter);
			} catch (ApplicationException e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	public void cancelaAlteracoesFotosFisicamente(Visita visita) throws SQLException {
		Vector visitaFotoExcluidasList = visita.getVisitaFotoExcluidasList();
		Vector visitaFotoMemoryList  = visita.getVisitaFotoList();
		Vector visitaFotoDatabaseList = findAllVisitaFotoByVisita(visita);
		deleteArquivoCancelar(visitaFotoExcluidasList, visitaFotoDatabaseList);
		deleteArquivoCancelar(visitaFotoMemoryList, visitaFotoDatabaseList);
	}

	private void deleteArquivoCancelar(Vector visitaFotoList, Vector visitaFotoDatabaseList) {
		for (int i = 0; i < visitaFotoList.size(); i++) {
			VisitaFoto visitaFoto = (VisitaFoto) visitaFotoList.items[i];
			if (visitaFotoDatabaseList.size() == 0 || !visitaFotoDatabaseList.contains(visitaFoto)) {
			   	String fileName = VisitaFoto.getPathImg() + visitaFoto.imFoto;
	    		FileUtil.deleteFile(fileName);
			}
		}
		visitaFotoList.removeAllElements();
	}


	public void deleteArquivoFotosFisicoInserirRegistro(Vector visitaFotoExcluidasList) {
		for (int i = 0; i < visitaFotoExcluidasList.size(); i++) {
			VisitaFoto visitaFotoExcluida = (VisitaFoto) visitaFotoExcluidasList.items[i];
			String fileName = VisitaFoto.getPathImg() + visitaFotoExcluida.imFoto;
    		FileUtil.deleteFile(fileName);
		}
	}

	public void deleteAllVisitaFotoByVisita(Visita visita) throws SQLException {
		Vector visitasFotoList = findAllVisitaFotoByVisita(visita);
		for (int i = 0; i < visitasFotoList.size(); i++) {
			delete((VisitaFoto)visitasFotoList.items[i]);
		}
    }

	public void delete(BaseDomain domain) throws java.sql.SQLException {
		super.delete(domain);
		String fileName = VisitaFoto.getPathImg() + ((VisitaFoto)domain).imFoto;
		FileUtil.deleteFile(fileName);
	}

	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector visitaFotoList = VisitaFotoDao.getInstance().findAllImagesToSend();
		for (int i = 0; i < visitaFotoList.size(); i++) {
			VisitaFoto visitaFoto = (VisitaFoto) visitaFotoList.items[i];
			imageList.addElement(visitaFoto.imFoto);
		}
		return imageList;
	}

	public void atualizaFotoAposEnvio(Vector imFotoList, String flEnviadoServidor) throws SQLException {
		int size = imFotoList.size();
		for (int i = 0; i < size; i++) {
			String imFoto = (String) imFotoList.items[i];
			VisitaFoto visitaFotoFilter = new VisitaFoto();
			visitaFotoFilter.imFoto = imFoto;
			Vector visitaFotoList = findAllByExample(visitaFotoFilter);
			if (ValueUtil.isNotEmpty(visitaFotoList)) {
				VisitaFoto visitaFoto = (VisitaFoto) visitaFotoList.items[0];
				visitaFoto.flEnviadoServidor = flEnviadoServidor;
				try {
					update(visitaFoto);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	public Vector findAllVisitaFotoNaoEnviadosServidor() throws SQLException {
		return VisitaFotoDao.getInstance().findAllImagesToSend();
	}

}