package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServImagem;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServImagem;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RequisicaoServImagemDbxDao;
import totalcross.util.Vector;

public class RequisicaoServImagemService extends CrudService {

    private static RequisicaoServImagemService instance;
    
    private RequisicaoServImagemService() {
        //--
    }
    
    public static RequisicaoServImagemService getInstance() {
        if (instance == null) {
            instance = new RequisicaoServImagemService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return RequisicaoServImagemDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    	//nao faz nada
    }

	public void addImagem(RequisicaoServ requisicaoServ, String defaultFileName) {
		RequisicaoServImagem requisicaoServImagem = createFilter(requisicaoServ, defaultFileName);
		requisicaoServ.getRequisicaoServImagemList().addElement(requisicaoServImagem);
	}

	private RequisicaoServImagem createFilter(RequisicaoServ requisicaoServ, String defaultFileName) {
		RequisicaoServImagem requisicaoServImagem = new RequisicaoServImagem();
		requisicaoServImagem.cdEmpresa = requisicaoServ.cdEmpresa;
		requisicaoServImagem.cdRepresentante = requisicaoServ.cdRepresentante;
		requisicaoServImagem.cdRequisicaoServ = requisicaoServ.cdRequisicaoServ;
		requisicaoServImagem.nmImagem = defaultFileName;
		return requisicaoServImagem;
	}

	public void excluiImagem(RequisicaoServ requisicaoServ, String nmFoto) {
		RequisicaoServImagem requisicaoServImagem = createFilter(requisicaoServ, nmFoto);
		requisicaoServ.getRequisicaoServImagemList().removeElement(requisicaoServImagem);
		requisicaoServ.getRequisicaoServImagemExcluidaList().addElement(requisicaoServImagem);
	}

	public void insertImagesDatabase(RequisicaoServ requisicaoServ) throws SQLException {
		int size = requisicaoServ.getRequisicaoServImagemExcluidaList().size();
		for (int i = 0; i < size; i++) {
			RequisicaoServImagem requisicaoServImagemExcluida = (RequisicaoServImagem) requisicaoServ.getRequisicaoServImagemExcluidaList().items[i];
			deleteAllByExample(requisicaoServImagemExcluida);
			FileUtil.deleteFile(RequisicaoServImagem.getPathImg() + "/" + requisicaoServImagemExcluida.nmImagem);
		}
		size = requisicaoServ.getRequisicaoServImagemList().size();
		for (int i = 0; i < size; i++) {
			RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) requisicaoServ.getRequisicaoServImagemList().items[i];
			deleteAllByExample(requisicaoServImagem);
			insert(requisicaoServImagem);
		}
	}

	public void removeImagensFisicas(Vector list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) list.items[i];
			String fileName = RequisicaoServImagem.getPathImg() + requisicaoServImagem.nmImagem;
			FileUtil.deleteFile(fileName);
		}
	}
	
	public void excluiImagensRequisicaoServ(RequisicaoServ requisicaoServ) throws SQLException {
		RequisicaoServImagem filter = createFilter(requisicaoServ, null);
		Vector requisicaoServImagemList = findAllByExample(filter);
		int size = requisicaoServImagemList.size();
		for (int i = 0; i < size; i++) {
			RequisicaoServImagem requisicaoServImagem = (RequisicaoServImagem) requisicaoServImagemList.items[i];
			delete(requisicaoServImagem);
			FileUtil.deleteFile(RequisicaoServImagem.getPathImg() + "/" + requisicaoServImagem.nmImagem);
		}
	}

	public Vector findAllByRequisicaoServ(RequisicaoServ requisicaoServ) throws SQLException {
		RequisicaoServImagem requisicaoServImagem = createFilter(requisicaoServ, null);
		return findAllByExample(requisicaoServImagem);
	}
	
	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector reqServImagemList = findAllAlterados();
		for (int i = 0; i < reqServImagemList.size(); i++) {
			RequisicaoServImagem reqServImagem = (RequisicaoServImagem) reqServImagemList.items[i];
			imageList.addElement(reqServImagem.nmImagem);
		}
		return imageList;
	}

}