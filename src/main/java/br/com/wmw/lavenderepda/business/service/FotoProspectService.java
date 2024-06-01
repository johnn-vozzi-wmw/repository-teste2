package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FotoProspect;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoProspectDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProspectDbxDao;
import totalcross.io.File;
import totalcross.util.Vector;

public class FotoProspectService extends CrudService {

    private static FotoProspectService instance;
    
    private FotoProspectService() {
        //--
    }
    
    public static FotoProspectService getInstance() {
        if (instance == null) {
            instance = new FotoProspectService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FotoProspectDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
/*    
        FotoProspect fotoProspect = (FotoProspect) domain;

        cdEmpresa
        if (ValueUtil.isEmpty(fotoProspect.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_CDEMPRESA);
        }
        cdRepresentante
        if (ValueUtil.isEmpty(fotoProspect.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_CDREPRESENTANTE);
        }
        flOrigemProspect
        if (ValueUtil.isEmpty(fotoProspect.flOrigemProspect)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_FLORIGEMPROSPECT);
        }
        cdProspect
        if (ValueUtil.isEmpty(fotoProspect.cdProspect)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_CDPROSPECT);
        }
        nmFoto
        if (ValueUtil.isEmpty(fotoProspect.nmFoto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_NMFOTO);
        }
        imFoto
        if (ValueUtil.isEmpty(fotoProspect.imFoto)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_IMFOTO);
        }
        nuTamanho
        if (ValueUtil.isEmpty(fotoProspect.nuTamanho)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_NUTAMANHO);
        }
        dtModificacao
        if (ValueUtil.isEmpty(fotoProspect.dtModificacao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_DTMODIFICACAO);
        }
        flControlewmw
        if (ValueUtil.isEmpty(fotoProspect.flControlewmw)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_FLCONTROLEWMW);
        }
        dsMensagemcontrolewmw
        if (ValueUtil.isEmpty(fotoProspect.dsMensagemcontrolewmw)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_DSMENSAGEMCONTROLEWMW);
        }
        flControleerp
        if (ValueUtil.isEmpty(fotoProspect.flControleerp)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_FLCONTROLEERP);
        }
        dsMensagemcontroleerp
        if (ValueUtil.isEmpty(fotoProspect.dsMensagemcontroleerp)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_DSMENSAGEMCONTROLEERP);
        }
        cdUsuario
        if (ValueUtil.isEmpty(fotoProspect.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_CDUSUARIO);
        }
        nuCarimbo
        if (ValueUtil.isEmpty(fotoProspect.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_NUCARIMBO);
        }
        flTipoAlteracao
        if (ValueUtil.isEmpty(fotoProspect.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_FLTIPOALTERACAO);
        }
        dtAlteracao
        if (ValueUtil.isEmpty(fotoProspect.dtAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_DTALTERACAO);
        }
        hrAlteracao
        if (ValueUtil.isEmpty(fotoProspect.hrAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.FOTOPROSPECT_LABEL_HRALTERACAO);
        }
*/
    }
    
    public Vector findAllFotoProspectByProspect(Prospect prospect) throws SQLException {
    	FotoProspect filter = createFilter(prospect);
    	return findAllByExample(filter);
    }

	private FotoProspect createFilter(Prospect prospect) {
		FotoProspect filter = new FotoProspect();
		filter.cdEmpresa = prospect.cdEmpresa;
		filter.cdRepresentante = prospect.cdRepresentante;
		filter.cdProspect = prospect.cdProspect;
		filter.flOrigemProspect = prospect.flOrigemProspect;
		return filter;
	}
	
	public Vector geraListaImagemForCarrousel(Prospect prospect) throws SQLException {
		Vector fotoProspectList;
		if (ValueUtil.isEmpty(prospect.fotoProspectList)) {
			prospect.fotoProspectList = findAllFotoProspectByProspect(prospect);
		}
		fotoProspectList = prospect.fotoProspectList;
		Vector imgList = new Vector();
		int size = ValueUtil.isNotEmpty(fotoProspectList) ? fotoProspectList.size() : 0;
		for (int i = 0; i < size; i++) {
			FotoProspect fotoProspect = (FotoProspect) fotoProspectList.items[i];
			String nmFoto = fotoProspect.getNmFotoSemExtensao();
			String extensao = fotoProspect.getExtensao();
			imgList.addElement(new String[] {nmFoto, prospect.cdProspect, extensao});
		}
		if (imgList.isEmpty()) {
			imgList.addElement(new String[]{ValueUtil.VALOR_NI, prospect.cdProspect});
		}
		return imgList;
	}
	
	public int getSequencialCdFotoProspect(Prospect prospect) throws SQLException {
		FotoProspect filter = createFilter(prospect);
		return countByExample(filter);
	}
	
	public void insereFotoProspectNaMemoria(Prospect prospect, String nmFoto) {
		FotoProspect fotoProspect = createFilter(prospect);
		fotoProspect.nmFoto = nmFoto;
		fotoProspect.nuTamanho = getTamanhoFoto(nmFoto);
		fotoProspect.dtModificacao = DateUtil.getCurrentDate();
		prospect.fotoProspectList.addElement(fotoProspect);
	}
	
	public void insereFotoProspectNoBanco(Prospect prospect) throws SQLException {
		int size = ValueUtil.isEmpty(prospect.fotoProspectExcluidaList) ? 0 : prospect.fotoProspectExcluidaList.size();
		for (int i = 0; i < size; i++) {
			FotoProspect fotoProspect = (FotoProspect)prospect.fotoProspectExcluidaList.items[i];
			deleteAllByExample(fotoProspect);
			FileUtil.deleteFile(Prospect.getPathImg() + "/" + fotoProspect.nmFoto);
		}
		size = ValueUtil.isEmpty(prospect.fotoProspectList) ? 0 : prospect.fotoProspectList.size();
		for (int i = 0; i < size; i++) {
			FotoProspect fotoProspect = (FotoProspect)prospect.fotoProspectList.items[i];
			deleteAllByExample(fotoProspect);
			insert(fotoProspect);
		}
	}
	
	private int getTamanhoFoto(String nmFoto) {
		File file = null;
		try {
			file = FileUtil.openFile(Prospect.getPathImg() + "/" + nmFoto, File.READ_ONLY);
			return file.getSize();
		} catch (Throwable e) {
			return 0;
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}
	
	public void excluiFotoProspect(Prospect prospect, String nmFoto) {
		FotoProspect fotoProspect = createFilter(prospect);
		fotoProspect.nmFoto = nmFoto;
		prospect.fotoProspectList.removeElement(fotoProspect);
		prospect.fotoProspectExcluidaList.addElement(fotoProspect);
	}
	
	public boolean isPermiteExcluirFoto(Prospect prospect, String nmFoto) throws SQLException {
		FotoProspect filter = createFilter(prospect);
		filter.nmFoto = nmFoto;
		FotoProspect fotoProspect = (FotoProspect) findByPrimaryKey(filter);
		if (fotoProspect != null) {
			return fotoProspect.isAlteradoPalm();
		}
		return true;
	}
	
	public void excluiFotosProspect(Prospect prospect) throws SQLException {
		FotoProspect filter = createFilter(prospect);
		Vector fotoList = findAllByExample(filter);
		int size = fotoList.size();
		if (size > 0) {
			deleteAllByExample(filter);
			for (int i = 0; i < size; i++) {
				FileUtil.deleteFile(Prospect.getPathImg() + "/" + ((FotoProspect)fotoList.items[i]).nmFoto);
			}
		}
	}
	
	public void deleteFotosByDtLimite(Prospect filter) throws SQLException {
		Vector list = ProspectDbxDao.getInstance().findAllByExample(filter);
		if (ValueUtil.isNotEmpty(list)) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				excluiFotosProspect((Prospect)list.items[i]);
			}
		}
	}
	
	public Vector getImageListSync() throws SQLException {
		return FotoProspectDbxDao.getInstance().findNmFotoAllAlterados();
	}

}